package com.jaciel.kiko.mixin;

import com.jaciel.kiko.world.GirlHomePlacement;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Las casas de las chicas no aparecen sobre agua ni lava, ni en barrancos muy empinados.
 * Solo toca las estructuras del mod (las que empiezan con un pool "kiko:...").
 */
@Mixin(JigsawStructure.class)
public abstract class JigsawStructureMixin {
    @Shadow
    @Final
    private RegistryEntry<StructurePool> startPool;

    @Inject(method = "getStructurePosition", at = @At("HEAD"), cancellable = true)
    private void kiko$avoidLiquids(Structure.Context context, CallbackInfoReturnable<Optional<Structure.StructurePosition>> cir) {
        boolean ours = startPool.getKey().map(k -> k.getValue().getNamespace().equals("kiko")).orElse(false);
        if (!ours) return;
        // v3.4 el pueblo revisa un área más grande
        boolean town = startPool.getKey().map(k -> k.getValue().getPath().startsWith("town/")).orElse(false);
        if (!GirlHomePlacement.isGoodSpot(context, town)) cir.setReturnValue(Optional.empty());
    }
}
