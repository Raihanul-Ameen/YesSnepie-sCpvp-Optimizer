package com.example.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ExampleClientMixin {
    
    @Shadow protected int itemUseCooldown;

    // --- ZERO-DELAY PLACEMENT ---
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickUpdate(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        
        if (client.player != null) {
            // Check if player is holding crystals or obsidian in either hand
            boolean holdingCrystalOrObsidian = 
                client.player.getMainHandStack().isOf(Items.END_CRYSTAL) || 
                client.player.getOffHandStack().isOf(Items.END_CRYSTAL) ||
                client.player.getMainHandStack().isOf(Items.OBSIDIAN) || 
                client.player.getOffHandStack().isOf(Items.OBSIDIAN);

            if (holdingCrystalOrObsidian) {
                // Instantly wipe Minecraft's built-in right-click placement delay
                this.itemUseCooldown = 0;
            }
        }
    }
}
