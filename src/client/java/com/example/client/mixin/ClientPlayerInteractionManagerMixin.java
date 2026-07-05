package com.example.client.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    // --- ZERO-DELAY BREAKING ---
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onCrystalAttack(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (target instanceof EndCrystalEntity crystal) {
            // Instantly delete the crystal entity visually from the client screen
            crystal.discard();

            // Fire explosion audio immediately to eliminate ping-based audio feedback latency
            if (player.getWorld() != null) {
                player.getWorld().playSound(
                    player, 
                    crystal.getX(), crystal.getY(), crystal.getZ(), 
                    SoundEvents.ENTITY_GENERIC_EXPLODE, 
                    SoundCategory.BLOCKS, 
                    4.0F, 
                    (1.0F + (player.getWorld().random.nextFloat() - player.getWorld().random.nextFloat()) * 0.2F) * 0.7F
                );
            }
        }
    }
}
