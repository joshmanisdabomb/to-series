package net.jidb.to.base.mixin;

import net.jidb.to.base.service.Services;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "loadLevel")
	private void init(CallbackInfo info) {
		System.out.println("to_base/common MinecraftServer.loadLevel");
		System.out.println("Do we have Sky and Stars? " + Services.INSTANCE.getEnvironment().isModLoaded("to_sky_and_stars"));
		// This code is injected into the start of MinecraftServer.loadLevel()V
	}
}