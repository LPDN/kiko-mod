package com.jaciel.kiko.mixin;

import com.jaciel.kiko.world.TownTrees;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** v3.4.1 guarda qué estructuras hay mientras se decora un chunk (para que no salgan árboles dentro del pueblo). */
@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Inject(method = "generateFeatures(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/gen/StructureAccessor;)V", at = @At("HEAD"), require = 0)
    private void kiko$start(StructureWorldAccess world, Chunk chunk, StructureAccessor accessor, CallbackInfo ci) {
        TownTrees.ACCESSOR.set(accessor);
    }

    @Inject(method = "generateFeatures(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/gen/StructureAccessor;)V", at = @At("RETURN"), require = 0)
    private void kiko$end(StructureWorldAccess world, Chunk chunk, StructureAccessor accessor, CallbackInfo ci) {
        TownTrees.ACCESSOR.remove();
    }
}
