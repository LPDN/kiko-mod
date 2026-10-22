package com.jaciel.kiko.mixin;

import com.jaciel.kiko.world.TownTrees;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** v3.4.1 al generar el mundo no salen árboles encima de las casas, calles ni la cancha del pueblo. */
@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {
    @Inject(method = "generate(Lnet/minecraft/world/gen/feature/util/FeatureContext;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void kiko$noTreesInTown(FeatureContext<TreeFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        if (TownTrees.blocked(context.getOrigin())) cir.setReturnValue(false);
    }
}
