package com.jaciel.kiko.world;

import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;

/**
 * Revisa que el lugar de una casa sea tierra firme.
 * Compara la superficie (que cuenta agua y lava) contra el piso de verdad (que no las cuenta):
 * si no coinciden en algún punto, hay líquido encima y ahí no va la casa.
 * También descarta lugares muy empinados para que no queden flotando.
 */
public final class GirlHomePlacement {
    private GirlHomePlacement() {}

    /** Puntos a revisar alrededor del inicio (la casa puede girar hacia cualquier lado, mide hasta 16). */
    private static final int[] OFFSETS = {-14, -7, 0, 7, 14};
    private static final int MAX_SLOPE = 9;

    /** v3.4 el pueblo es más grande: revisa más lejos (hasta 35 bloques) y lo quiere más parejo. */
    private static final int[] TOWN_OFFSETS = {-35, -21, -7, 0, 7, 21, 35};
    private static final int TOWN_SLOPE = 12;

    public static boolean isGoodSpot(Structure.Context ctx) {
        return isGoodSpot(ctx, false);
    }

    public static boolean isGoodSpot(Structure.Context ctx, boolean town) {
        int[] offsets = town ? TOWN_OFFSETS : OFFSETS;
        int maxSlope = town ? TOWN_SLOPE : MAX_SLOPE;
        int x0 = ctx.chunkPos().getStartX();
        int z0 = ctx.chunkPos().getStartZ();
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int dx : offsets) {
            for (int dz : offsets) {
                int x = x0 + dx, z = z0 + dz;
                int surface = ctx.chunkGenerator().getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, ctx.world(), ctx.noiseConfig());
                int floor = ctx.chunkGenerator().getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, ctx.world(), ctx.noiseConfig());
                if (surface != floor) return false;   // agua o lava encima
                min = Math.min(min, floor);
                max = Math.max(max, floor);
                if (max - min > maxSlope) return false;
            }
        }
        return true;
    }
}
