package com.jaciel.kiko.client.render;

import com.jaciel.kiko.KikoMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Pinta las texturas de los hijos con sus colores (v3.1).
 * La textura base (tools/gen_kids.py) trae en el alfa a qué capa va cada pixel
 *  255 fijo · 254 piel · 253 pelo · 252 ojos · 251 ropa 1 · 250 ropa 2 · 249 zapatos
 * y en el gris qué tan claro. Aquí se multiplica por el color de cada rasgo y se guarda en caché.
 */
public final class KidTextures {
    private KidTextures() {}

    private static final Map<String, NativeImage> BASES = new HashMap<>();
    private static final Map<String, Identifier> CACHE = new HashMap<>();

    public static Identifier get(String model, int[] colors) {
        StringBuilder key = new StringBuilder(model);
        for (int c : colors) key.append('_').append(Integer.toHexString(c));
        String k = key.toString();
        Identifier id = CACHE.get(k);
        if (id != null) return id;
        NativeImage base = base(model);
        Identifier fallback = KikoMod.id("textures/entity/kid/" + model + ".png");
        if (base == null) return fallback;
        NativeImage img = new NativeImage(base.getWidth(), base.getHeight(), true);
        for (int y = 0; y < base.getHeight(); y++) {
            for (int x = 0; x < base.getWidth(); x++) {
                int abgr = base.getColor(x, y);
                int a = (abgr >>> 24) & 255;
                if (a >= 249 && a <= 254) {
                    int rgb = colors[254 - a];               // 254 piel=0, 253 pelo=1 ... 249 zapatos=5
                    int v = abgr & 255;                      // gris (R = G = B)
                    int r = ((rgb >> 16) & 255) * v / 255;
                    int g = ((rgb >> 8) & 255) * v / 255;
                    int b = (rgb & 255) * v / 255;
                    img.setColor(x, y, 0xFF000000 | (b << 16) | (g << 8) | r);
                } else {
                    img.setColor(x, y, abgr);
                }
            }
        }
        id = KikoMod.id("kid_dyn/" + Integer.toHexString(k.hashCode()) + "_" + CACHE.size());
        MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(img));
        CACHE.put(k, id);
        return id;
    }

    private static NativeImage base(String model) {
        if (BASES.containsKey(model)) return BASES.get(model);
        NativeImage img = null;
        Optional<Resource> res = MinecraftClient.getInstance().getResourceManager()
                .getResource(KikoMod.id("textures/entity/kid/" + model + ".png"));
        if (res.isPresent()) {
            try (InputStream in = res.get().getInputStream()) {
                img = NativeImage.read(in);
            } catch (Exception e) {
                KikoMod.LOGGER.error("No pude leer la textura de {}", model, e);
            }
        }
        BASES.put(model, img);
        return img;
    }
}
