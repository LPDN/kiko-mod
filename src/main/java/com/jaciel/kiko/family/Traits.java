package com.jaciel.kiko.family;

import com.jaciel.kiko.KikoMod;
import com.jaciel.kiko.entity.KikoCharacter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;

/**
 * Rasgos del jugador (v3.1). Solo son descriptivos, no cambian nada del juego:
 *  - Hombre o mujer (cambia cómo te hablan en los diálogos)
 *  - Color de piel, de pelo y de ojos (los heredan tus hijos)
 * Se guardan en los datos del jugador y no se pierden al morir.
 */
public final class Traits {
    private Traits() {}

    /** Mismas paletas que tools/gen_kids.py. */
    public static final int[] SKINS = {0xFFE2D0, 0xF6D2BA, 0xE4B492, 0xCC926A, 0xA06C4A, 0x704A32};
    public static final int[] HAIRS = {0x2E282E, 0x543826, 0x845836, 0xAA6034, 0xDE7836, 0xECCC78, 0xECE8DE,
            0xB42C34, 0xFF8270, 0xF096B4, 0xB8A2DE, 0x6088D6, 0x8CD0BC, 0xC0D0E2};
    public static final int[] EYES = {0x6E4628, 0x402C22, 0x967A3C, 0x46A05A, 0x4678C8, 0x8C96A4, 0xE29C28,
            0xD66E8C, 0x9268CE, 0xBE3440};
    public static final int[] CLOTHES = {0xD64046, 0x4678C8, 0x5AAA6E, 0xF0C446, 0xAA6ED2, 0xF096AA, 0x464A5A,
            0xF0F0EC, 0x785438, 0x5ABEC8, 0xEC823C, 0x3C5A46};
    public static final int[] SHOES = {0x3C3232, 0xF0F0F0, 0x784C2E, 0x282830, 0xC83C3C};

    /** Rasgos de un jugador. male = hombre. Los demás son índices de las paletas. */
    public record PlayerTraits(boolean male, int skin, int hair, int eyes) {
        public static final PlayerTraits DEFAULT = new PlayerTraits(true, 1, 1, 0);
        public static final Codec<PlayerTraits> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.BOOL.fieldOf("male").forGetter(PlayerTraits::male),
                Codec.INT.fieldOf("skin").forGetter(PlayerTraits::skin),
                Codec.INT.fieldOf("hair").forGetter(PlayerTraits::hair),
                Codec.INT.fieldOf("eyes").forGetter(PlayerTraits::eyes)
        ).apply(i, PlayerTraits::new));

        public PlayerTraits clamp() {
            return new PlayerTraits(male, Math.floorMod(skin, SKINS.length), Math.floorMod(hair, HAIRS.length), Math.floorMod(eyes, EYES.length));
        }

        public int skinColor() {
            return SKINS[Math.floorMod(skin, SKINS.length)];
        }

        public int hairColor() {
            return HAIRS[Math.floorMod(hair, HAIRS.length)];
        }

        public int eyeColor() {
            return EYES[Math.floorMod(eyes, EYES.length)];
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static final AttachmentType<PlayerTraits> TRAITS = AttachmentRegistry.<PlayerTraits>builder()
            .persistent(PlayerTraits.CODEC)
            .copyOnDeath()
            .initializer(() -> PlayerTraits.DEFAULT)
            .buildAndRegister(KikoMod.id("player_traits"));

    public static void register() {
        // Cargar la clase registra el attachment
    }

    @SuppressWarnings("UnstableApiUsage")
    public static PlayerTraits of(PlayerEntity player) {
        PlayerTraits t = player.getAttached(TRAITS);
        return t != null ? t : PlayerTraits.DEFAULT;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void set(PlayerEntity player, PlayerTraits traits) {
        player.setAttached(TRAITS, traits.clamp());
    }

    public static boolean isFemale(PlayerEntity player) {
        return !of(player).male();
    }

    // ------------------------------------------------------------------
    // Rasgos de las mamás (piel, pelo, ojos) y la herencia
    // ------------------------------------------------------------------

    public static int[] motherColors(KikoCharacter c) {
        return switch (c) {
            case KIKO -> new int[]{0xFFE0CE, 0xFF7F6E, 0xE2A03C};
            case MARU -> new int[]{0xD8A27C, 0x3A363E, 0x46A05A};
            case LUMI -> new int[]{0xFCE8DE, 0xB8A2DE, 0x6E8CC4};
            case NADI -> new int[]{0xC8926C, 0x78482C, 0xAA8C3C};
            case RINA -> new int[]{0xFAE4D6, 0xC0D0E2, 0xD66E8C};
            case TAMI -> new int[]{0xDEB08C, 0xDE7836, 0x966032};
            // v3.3 los chicos (mismos colores que tools/gen_guys.py)
            case TONO -> new int[]{0xCC926A, 0x543826, 0x6E4628};
            case LEO -> new int[]{0xFFE2D0, 0xECCC78, 0x4678C8};
            case DANTE -> new int[]{0x704A32, 0x2E282E, 0xE29C28};
            case IVAN -> new int[]{0xE4B492, 0x2E282E, 0x402C22};
            case EMI -> new int[]{0xFFE2D0, 0xF07896, 0x9268CE};
            case BETO -> new int[]{0xA06C4A, 0x543826, 0x46A05A};
        };
    }

    /** Pelo y ojos de uno o del otro; la piel de uno, del otro o una mezcla de los dos. */
    public static int[] inherit(PlayerTraits dad, KikoCharacter mom, Random r) {
        int[] m = motherColors(mom);
        int skin = switch (r.nextInt(3)) {
            case 0 -> dad.skinColor();
            case 1 -> m[0];
            default -> mix(dad.skinColor(), m[0]);
        };
        int hair = r.nextBoolean() ? dad.hairColor() : m[1];
        int eyes = r.nextBoolean() ? dad.eyeColor() : m[2];
        return new int[]{skin, hair, eyes};
    }

    public static int mix(int a, int b) {
        int r = (((a >> 16) & 255) + ((b >> 16) & 255)) / 2;
        int g = (((a >> 8) & 255) + ((b >> 8) & 255)) / 2;
        int bl = ((a & 255) + (b & 255)) / 2;
        return (r << 16) | (g << 8) | bl;
    }
}
