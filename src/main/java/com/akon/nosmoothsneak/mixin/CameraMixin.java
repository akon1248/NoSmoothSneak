package com.akon.nosmoothsneak.mixin;

import com.akon.nosmoothsneak.interfaces.ClientPlayerEntityExtension;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow
	private Entity focusedEntity;
	@Shadow
	private float cameraY;
	@Shadow
	private float lastCameraY;

	@Redirect(method = "updateEyeHeight", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/render/Camera;cameraY:F"))
	public void modifyEyeHeight(Camera instance, float value) {
		if (this.focusedEntity instanceof ClientPlayerEntity player && ((ClientPlayerEntityExtension)player).toggledSneak()) {
			this.cameraY = player.getStandingEyeHeight();
			this.lastCameraY = this.cameraY;
			return;
		}
		this.cameraY = value;
	}
}
