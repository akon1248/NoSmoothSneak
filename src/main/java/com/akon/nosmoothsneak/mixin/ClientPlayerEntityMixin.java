package com.akon.nosmoothsneak.mixin;

import com.akon.nosmoothsneak.interfaces.ClientPlayerEntityExtension;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ClientPlayerEntityExtension {

	private boolean toggledSneak;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
		super(world, profile, publicKey);
	}

	@Override
	public boolean toggledSneak() {
		return this.toggledSneak;
	}

	@Override
	protected void updatePose() {
		EntityPose old = this.getPose();
		super.updatePose();
		EntityPose pose = this.getPose();
		this.toggledSneak = old != pose && (pose == EntityPose.CROUCHING || old == EntityPose.CROUCHING);
	}

	@Override
	public float getStandingEyeHeight() {
		if (this.isInPose(EntityPose.CROUCHING) && this.wouldPoseNotCollide(EntityPose.STANDING)) {
			return this.getEyeHeight(EntityPose.STANDING) - 0.08F;
		}
		return super.getStandingEyeHeight();
	}
}
