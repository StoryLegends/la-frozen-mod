package com.datdevelop.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FreezingEffect extends StatusEffect {

    public FreezingEffect() {
        super(
                StatusEffectCategory.NEUTRAL,
                0x2acaea
        );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // Эффект будет применяться каждый тик
        return true;
    }


    public void updateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player) {
            if (entity.getFrozenTicks() < 100 * (amplifier + 1)) {
                entity.setFrozenTicks(entity.getFrozenTicks() + 3 * (amplifier + 1));
                tickFrozenHands(player);
            }
        }
    }

    private void tickFrozenHands(ServerPlayerEntity player) {
        boolean mainhand = !player.getMainHandStack().isEmpty();
        boolean offhand = !player.getOffHandStack().isEmpty();
        player.sendMessage(Text.literal("§bВам холодно..."), true);

        if (mainhand && offhand) {
            if (player.getRandom().nextBetween(0, 1) == 0) {
                dropOneItem(player, true);
            } else {
                dropOneItem(player, false);
            }
        } else if (mainhand) {
            dropOneItem(player, true);
        } else if (offhand) {
            dropOneItem(player, false);
        }
    }

    private void dropOneItem(ServerPlayerEntity player, boolean mainHand) {
        ItemStack stack = mainHand ? player.getMainHandStack() : player.getOffHandStack();
        ItemStack copy = stack.copy();
        copy.setCount(1);
        player.dropItem(copy, false, true);
        stack.decrement(1);
    }
}
