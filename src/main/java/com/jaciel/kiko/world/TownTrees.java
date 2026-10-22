package com.jaciel.kiko.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.StructureAccessor;

/**
 * v3.4.1 Evita árboles naturales dentro del pueblo.
 * Mientras se decora un chunk (ChunkGeneratorMixin) se guarda el acceso a las estructuras de esa zona,
 * y antes de poner un árbol (TreeFeatureMixin) se revisa si cae en alguna pieza del pueblo (o a 3 bloques).
 */
public final class TownTrees {
    private TownTrees() {}

    public static final ThreadLocal<StructureAccessor> ACCESSOR = new ThreadLocal<>();
    private static final int[][] AROUND = {{0, 0}, {3, 0}, {-3, 0}, {0, 3}, {0, -3}};

    public static boolean blocked(BlockPos origin) {
        StructureAccessor acc = ACCESSOR.get();
        if (acc == null) return false;   // un arbolito que alguien plantó: ese sí crece
        ChunkPos home = new ChunkPos(origin);
        try {
            for (int[] d : AROUND) {
                BlockPos p = origin.add(d[0], 0, d[1]);
                // Solo dentro del mismo chunk (los de al lado pueden no estar listos todavía)
                if (!new ChunkPos(p).equals(home)) continue;
                var start = acc.getStructureContaining(p, entry -> entry.getKey()
                        .map(k -> k.getValue().getNamespace().equals("kiko") && k.getValue().getPath().startsWith("town_"))
                        .orElse(false));
                if (start != null && start.hasChildren()) return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
