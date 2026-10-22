"""
Hijos e hijas (v3.1). Modelos por etapa y una textura "pintable":
los colores de piel, pelo, ojos y ropa no van fijos, se pintan en el juego según los rasgos de cada hijo.

Cómo se pintan
  Cada pixel de la textura trae en el alfa a qué capa pertenece y en el gris qué tan claro es
    alfa 255 = color fijo (blanco de los ojos, boca, etc.)
    alfa 254 = piel   253 = pelo   252 = ojos   251 = ropa 1   250 = ropa 2   249 = zapatos
  En el juego el color final es gris × color del rasgo (ver client/render/KidTextures.java)

Modelos (alto en bloques)
  baby 0.6 · child_boy y child_girl 1.1 · teen_boy y teen_girl 1.55 · adult_man 1.85 · adult_woman 1.75

Uso   python tools/gen_kids.py [carpeta_preview]
"""
import math
import os
import random
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_girls as gg  # noqa: E402
import gen_assets as g  # noqa: E402

c = g.c
CLEAR = None
SKIN, HAIR, EYE, CLOTH1, CLOTH2, SHOE = 254, 253, 252, 251, 250, 249


def T(layer, v):
    """Pixel de una capa pintable: gris v (255 = el color tal cual)."""
    v = max(0, min(255, int(v)))
    return (v, v, v, layer)


# Tonos base de cada capa
SK, SK_S, SK_D = T(SKIN, 248), T(SKIN, 228), T(SKIN, 206)
HR, HR_D, HR_L = T(HAIR, 236), T(HAIR, 186), T(HAIR, 255)
EY, EY_D, EY_L = T(EYE, 225), T(EYE, 160), T(EYE, 255)
C1, C1_S, C1_D = T(CLOTH1, 245), T(CLOTH1, 214), T(CLOTH1, 180)
C2, C2_S, C2_D = T(CLOTH2, 245), T(CLOTH2, 214), T(CLOTH2, 180)
SH, SH_D = T(SHOE, 236), T(SHOE, 190)
WHITE = c(250, 250, 250)
PUPIL = c(36, 26, 30)
LACE = c(250, 250, 252)

MODELS = {}     # nombre -> (constructor, alto en bloques)


def solid(main, shade):
    return gg.solid(main, shade)


def pattern(fn):
    return {"*": fn}


def head(G, bangs, blush=True):
    gg.build_head(G, 50, SK, HR, HR_D, HR_L, EY, EY_D, EY_L, PUPIL, bangs, {},
                  blush=T(SKIN, 214) if blush else None)


def short_hair(G):
    """Pelo corto de chavo: patillas cortas y nuca rapada (en vez de la melena que trae build_head)."""
    def front(x, y, w, h):
        if y <= 1 or (y == 2 and x not in (3, 6)):
            return HR_D if y == 2 else HR
        if x in (0, 9) and y <= 4:
            return HR_D
        return CLEAR

    def side(x, y, w, h):
        if y <= 3:
            return HR_D if x % 3 == 1 else HR
        if y <= 6 and x >= 3:
            return HR_D
        return CLEAR

    def side_l(x, y, w, h):
        return side(w - 1 - x, y, w, h)

    def back(x, y, w, h):
        return (HR_D if x % 3 == 2 else HR) if y <= 7 else (HR_D if y == 8 else CLEAR)
    G.paint["hair_shell"] = {"front": front, "right": side, "left": side_l, "back": back,
                             "top": lambda x, y, w, h: HR_L if x == 3 and 1 <= y <= 8 else HR, "bottom": CLEAR}


def neck(G):
    return G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": SK_S, "front": SK})


def limbs(G, ax, arm, forearm, lx, thigh, shin, foot, arm_rot=0.07,
          upper=(None, 0.4), lower=(None, 0.3), legp=(None, 0.5), shinp=(None, 0.4), footp=(None, 0.3)):
    """Brazos y piernas. arm=(y_arriba, largo, ancho), forearm=(largo), thigh/shin=(largo, ancho), foot=alto."""
    ay, al, aw = arm
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (ax * sx, ay - 0.5, 0), rot=(0, 0, arm_rot * -sx), cubes=[
            G.cube(f"{side}_arm", (ax * sx - aw / 2, ay - al, -aw / 2), (aw, al, aw), upper[1], upper[0])])
        G.part(f"{side}_forearm", f"{side}_arm", (ax * sx, ay - al + 0.5, 0), cubes=[
            G.cube(f"{side}_forearm", (ax * sx - aw / 2, ay - al - forearm, -aw / 2), (aw, forearm, aw), lower[1], lower[0])])
    top = thigh[2]
    tl, tw = thigh[0], thigh[1]
    sl, sw = shin
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (lx * sx, top, 0), cubes=[
            G.cube(f"{side}_thigh", (lx * sx - tw / 2, top - tl, -tw / 2), (tw, tl, tw), legp[1], legp[0])])
        G.part(f"{side}_shin", f"{side}_leg", (lx * sx, top - tl, 0), cubes=[
            G.cube(f"{side}_shin", (lx * sx - sw / 2, top - tl - sl, -sw / 2), (sw, sl, sw), shinp[1], shinp[0]),
            G.cube(f"{side}_foot", (lx * sx - sw / 2, top - tl - sl - foot, -sw / 2 - 1), (sw, foot, sw + 1), footp[1], footp[0]),
        ])
    return top - tl - sl - foot      # dónde quedan los pies


def shift(G, dy):
    """Baja todo el modelo para que los pies queden en y = 0."""
    for p in G.parts:
        x, y, z = p["pivot"]
        if p["parent"] is not None:          # la raíz se queda en los pies (en Java siempre va en y=24)
            p["pivot"] = (x, y + dy, z)
        for cb in p["cubes"]:
            fx, fy, fz = cb["frm"]
            cb["frm"] = (fx, fy + dy, fz)


def top_y(G):
    return max(cb["frm"][1] + cb["size"][1] + cb["dil"] for p in G.parts for cb in p["cubes"])


def finish(G, feet):
    shift(G, -feet)
    G.units = top_y(G)
    return gg.complete(G)


# ------------------------------------------------------------------
# Peinados
# ------------------------------------------------------------------
BANGS_BOY = {0: range(10), 1: range(10), 2: [0, 1, 2, 4, 5, 7, 8, 9], 3: [0, 9]}
BANGS_GIRL = {0: range(10), 1: range(10), 2: [0, 1, 2, 3, 6, 7, 8, 9], 3: [0, 1, 8, 9]}
BANGS_BABY = {0: range(10), 1: [0, 3, 4, 5, 6, 9]}


def hair_long(G, bottom):
    G.part("hair_back", "head", (0, 56, 6.2), cubes=[
        G.cube("hair_back", (-5.5, bottom, 5.6), (11, 56 - bottom, 2), 0,
               {"back": lambda x, y, w, h: HR_D if x % 3 == 1 else HR, "*": HR_D, "top": HR})])


def hair_tuft(G):
    G.part("spikes", "head", (0, 50, 0), cubes=[
        G.cube("tuft", (-2, 61, -2), (4, 1, 4), 0.4, solid(HR, HR_D)),
        G.cube("tuft2", (-1, 61.8, 0), (2, 1, 2), 0.3, solid(HR, HR_D))])


def pigtails(G, length, high=False):
    y0 = 58 if high else 55
    G.part("ponytail", "head", (0, y0, 0), cubes=[
        G.cube("tail_r", (-7.4, y0 - length, -1), (2, length, 2), 0.3, pattern(lambda x, y, w, h: HR_D if y % 3 == 2 else HR)),
        G.cube("tail_l", (5.4, y0 - length, -1), (2, length, 2), 0.3, pattern(lambda x, y, w, h: HR_D if y % 3 == 2 else HR)),
        G.cube("tie_r", (-7.6, y0 - 1.5, -1.2), (2, 1, 2), 0.35, pattern(C1_D)),
        G.cube("tie_l", (5.6, y0 - 1.5, -1.2), (2, 1, 2), 0.35, pattern(C1_D)),
    ])


def ponytail(G, length):
    G.part("ponytail", "head", (0, 58, 6), cubes=[
        G.cube("tail", (-1.5, 58 - length, 6), (3, length, 2), 0.3, pattern(lambda x, y, w, h: HR_D if x == 0 else HR)),
        G.cube("tie", (-2, 57.5, 5.6), (4, 1, 3), 0.1, pattern(C1_D)),
    ])


# ------------------------------------------------------------------
# Bebé
# ------------------------------------------------------------------
def baby():
    G = gg.Girl("baby")
    G.part("root", None, (0, 0, 0))
    head(G, BANGS_BABY)
    hair_tuft(G)
    short_hair(G)
    onesie = solid(C1, C1_S)
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G),
        G.cube("chest", (-3.5, 42, -2.5), (7, 6, 5), 0.6, {"*": C1, "front": lambda x, y, w, h: C1_S if (x + y) % 4 == 0 else C1,
                                                            "right": C1_S, "left": C1_S}),
        G.cube("hips", (-3.5, 39, -2.5), (7, 3, 5), 0.7, solid(C1, C1_S)),
        G.cube("bib", (-2, 45, -3.4), (4, 3, 1), 0.1, {"*": WHITE, "front": lambda x, y, w, h: c(255, 214, 220) if (x, y) == (1, 1) else WHITE}),
    ])
    feet = limbs(G, 4.6, (47.5, 3, 3), 3, 2.0, (4, 3, 39), (3, 3), 1,
                 upper=(onesie, 0.5), lower=(pattern(g.rows((1, C1), (2, SK))), 0.45),
                 legp=(onesie, 0.6), shinp=(onesie, 0.5), footp=(pattern(SK), 0.45), arm_rot=0.3)
    return finish(G, feet)


# ------------------------------------------------------------------
# Niño y niña
# ------------------------------------------------------------------
def child(girl):
    G = gg.Girl("child_girl" if girl else "child_boy")
    G.part("root", None, (0, 0, 0))
    head(G, BANGS_GIRL if girl else BANGS_BOY)
    if girl:
        pigtails(G, 7)
    else:
        hair_tuft(G)
        short_hair(G)
    cubes = [neck(G),
             G.cube("chest", (-3.5, 42, -2), (7, 6, 4), 0.45, {"*": C1, "front": lambda x, y, w, h: (WHITE if (y == 0 and 2 <= x <= 4) else C1),
                                                               "right": C1_S, "left": C1_S, "back": C1, "top": C1, "bottom": C1_S}),
             G.cube("waist", (-3, 39, -2), (6, 3, 4), 0.4, solid(C1, C1_S)),
             G.cube("hips", (-3.5, 36, -2), (7, 3, 4), 0.55, solid(C1 if girl else C2, C1_S if girl else C2_S))]
    G.part("body", "root", (0, 48, 0), cubes=cubes)
    if girl:
        # Vestidito con falda
        G.part("kid_skirt", "body", (0, 39, 0), cubes=[
            G.cube("kid_skirt", (-4.5, 31, -3), (9, 8, 6), 0.4,
                   {"*": lambda x, y, w, h: C1_D if y == h - 1 else (C1_S if x % 3 == 0 else C1), "top": C1, "bottom": C1_D})])
        legp = pattern(SK)
        shinp = pattern(g.rows((3, SK), (5, WHITE)))
    else:
        legp = pattern(g.rows((3, C2), (7, SK)))
        shinp = pattern(g.rows((2, SK), (5, WHITE)))
    feet = limbs(G, 5.0, (47.5, 6, 3), 5, 1.9, (7, 3, 36), (5, 3), 2,
                 upper=(pattern(g.rows((2, C1), (6, SK))), 0.4), lower=(pattern(SK), 0.3),
                 legp=(legp, 0.45), shinp=(shinp, 0.4), footp=(pattern(g.rows((0, SH), (1, SH_D))), 0.35))
    return finish(G, feet)


# ------------------------------------------------------------------
# Adolescente (chavo y chava). Sin curvas: siguen siendo menores.
# ------------------------------------------------------------------
def teen(girl):
    G = gg.Girl("teen_girl" if girl else "teen_boy")
    G.part("root", None, (0, 0, 0))
    head(G, BANGS_GIRL if girl else BANGS_BOY)
    if girl:
        hair_long(G, 46)
        ponytail(G, 10)
    else:
        short_hair(G)
        G.part("spikes", "head", (0, 50, 0), cubes=[
            G.cube("fringe", (-5, 60.6, -5.6), (10, 1, 3), 0.5, solid(HR, HR_D))])
    hoodie = {"*": C1, "front": lambda x, y, w, h: (WHITE if (h == 8 and y in (1, 2, 3) and x in (3, 4) and girl is False and (x + y) % 2 == 0) else
                                                     (C1_D if x == w // 2 and not girl else C1)),
              "right": C1_S, "left": C1_S, "top": C1, "bottom": C1_S}
    cubes = [neck(G),
             G.cube("chest", (-4, 40, -2), (8, 8, 4), 0.45, hoodie),
             G.cube("waist", (-3.5, 36, -2), (7, 4, 4), 0.4, solid(C1, C1_S)),
             G.cube("hips", (-4, 32, -2), (8, 4, 4), 0.5, solid(C2, C2_S)),
             G.cube("hem", (-4, 35.5, -2.2), (8, 1, 4), 0.55, pattern(C1_D))]
    G.part("body", "root", (0, 48, 0), cubes=cubes)
    if girl:
        G.part("kid_skirt", "body", (0, 35, 0), cubes=[
            G.cube("kid_skirt", (-4.8, 24, -2.8), (10, 11, 6), 0.4,
                   {"*": lambda x, y, w, h: C2_D if x % 3 == 0 else (C2_S if y >= h - 1 else C2), "top": C2, "bottom": C2_D})])
        legp = pattern(SK)
        shinp = pattern(g.rows((2, SK), (8, g.shade_sides(WHITE, c(226, 226, 232)))))
    else:
        legp = solid(C2, C2_S)
        shinp = pattern(g.rows((7, g.shade_sides(C2, C2_S)), (8, WHITE)))
    feet = limbs(G, 5.6, (47.5, 9, 3), 8, 2.1, (10, 3, 32), (9, 3), 2,
                 upper=(solid(C1, C1_S), 0.45), lower=(pattern(g.rows((5, g.shade_sides(C1, C1_S)), (6, C1_D), (7, SK))), 0.3),
                 legp=(legp, 0.5), shinp=(shinp, 0.4), footp=(pattern(g.rows((0, SH), (1, SH_D))), 0.35))
    return finish(G, feet)


# ------------------------------------------------------------------
# Adultos (hijo e hija ya grandes; también servirán para los NPC del pueblo)
# ------------------------------------------------------------------
def adult_man():
    G = gg.Girl("adult_man")
    G.part("root", None, (0, 0, 0))
    head(G, BANGS_BOY, blush=False)
    short_hair(G)
    G.part("spikes", "head", (0, 50, 0), cubes=[
        G.cube("fringe", (-5, 60.6, -5.6), (10, 1, 4), 0.55, solid(HR, HR_D)),
        G.cube("crown", (-4, 61.2, -2), (8, 1, 6), 0.4, solid(HR, HR_D))])
    shirt = {"*": C1, "front": lambda x, y, w, h: (WHITE if (h == 10 and y <= 1 and 4 <= x <= 5) else
                                                    (C1_D if (h == 10 and x == w // 2 and y > 1 and y % 3 == 0) else C1)),
             "right": C1_S, "left": C1_S, "top": C1, "bottom": C1_S}
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G),
        G.cube("chest", (-5, 38, -2.5), (10, 10, 5), 0.55, shirt),
        G.cube("waist", (-4.5, 33, -2.25), (9, 5, 4), 0.5, solid(C1, C1_S)),
        G.cube("hips", (-4.5, 28, -2.5), (9, 5, 5), 0.6, solid(C2, C2_S)),
        G.cube("belt", (-4.7, 32.6, -2.7), (9, 1, 5), 0.7, {"*": c(60, 44, 32), "front": lambda x, y, w, h: c(200, 190, 160) if x == w // 2 else c(60, 44, 32)}),
    ])
    feet = limbs(G, 7.1, (47.5, 11, 3), 10, 2.6, (14, 4, 29), (13, 3), 2, arm_rot=0.08,
                 upper=(solid(C1, C1_S), 0.65), lower=(pattern(g.rows((3, g.shade_sides(C1, C1_S)), (4, C1_D), (9, SK))), 0.4),
                 legp=(solid(C2, C2_S), 0.8), shinp=(pattern(g.shade_sides(C2, C2_S)), 0.6),
                 footp=(pattern(g.rows((0, SH), (1, SH_D))), 0.4))
    return finish(G, feet)


def adult_woman():
    G = gg.Girl("adult_woman")
    G.part("root", None, (0, 0, 0))
    head(G, BANGS_GIRL)
    hair_long(G, 38)
    G.part("body", "root", (0, 48, 0), cubes=[neck(G)])
    gg.curves(G, C1, C1, center=lambda x, y, w, h: WHITE if (h == 9 and y == 0 and 3 <= x <= 5) else None)
    G.part("kid_skirt", "body", (0, 31, 0), cubes=[
        G.cube("kid_skirt", (-5.5, 16, -3.2), (11, 15, 7), 0.45,
               {"*": lambda x, y, w, h: C2_D if x % 3 == 0 else (C2_S if y >= h - 1 else C2), "top": C2, "bottom": C2_D})])
    feet = limbs(G, 6.6, (47.5, 11, 3), 10, 2.75, (14, 4, 29), (13, 3), 2,
                 upper=(solid(C1, C1_S), 0.5), lower=(pattern(g.rows((5, g.shade_sides(C1, C1_S)), (6, C1_D), (9, SK))), 0.35),
                 legp=(pattern(g.shade_sides(SK, SK_S)), 0.6), shinp=(pattern(g.shade_sides(SK, SK_S)), 0.45),
                 footp=(pattern(g.rows((0, SH), (1, SH_D))), 0.3))
    return finish(G, feet)


BUILDERS = [("baby", baby, 0.6), ("child_boy", lambda: child(False), 1.1), ("child_girl", lambda: child(True), 1.1),
            ("teen_boy", lambda: teen(False), 1.55), ("teen_girl", lambda: teen(True), 1.55),
            ("adult_man", adult_man, 1.85), ("adult_woman", adult_woman, 1.75)]

# ------------------------------------------------------------------
# Rasgos (los mismos que en el juego, ver family/Traits.java)
# ------------------------------------------------------------------
SKINS = [(255, 226, 208), (246, 210, 186), (228, 180, 146), (204, 146, 106), (160, 108, 74), (112, 74, 50)]
HAIRS = [(46, 40, 46), (84, 56, 38), (132, 88, 54), (170, 96, 52), (222, 120, 54), (236, 204, 120), (236, 232, 222),
         (180, 44, 52), (255, 130, 112), (240, 150, 180), (184, 162, 222), (96, 136, 214), (140, 208, 188), (192, 208, 226)]
EYES = [(110, 70, 40), (64, 44, 34), (150, 122, 60), (70, 160, 90), (70, 120, 200), (140, 150, 164), (226, 156, 40),
        (214, 110, 140), (146, 104, 206), (190, 52, 64)]
CLOTHES = [(214, 64, 70), (70, 120, 200), (90, 170, 110), (240, 196, 70), (170, 110, 210), (240, 150, 170), (70, 74, 90),
           (240, 240, 236), (120, 84, 56), (90, 190, 200), (236, 130, 60), (60, 90, 70)]
SHOES = [(60, 50, 50), (240, 240, 240), (120, 76, 46), (40, 40, 48), (200, 60, 60)]


def colorize(img, skin, hair, eye, cloth1, cloth2, shoe):
    tint = {SKIN: skin, HAIR: hair, EYE: eye, CLOTH1: cloth1, CLOTH2: cloth2, SHOE: shoe}
    out = img.copy()
    px = out.load()
    for y in range(out.height):
        for x in range(out.width):
            r, gg_, b, a = px[x, y]
            if a in tint:
                t = tint[a]
                px[x, y] = (r * t[0] // 255, r * t[1] // 255, r * t[2] // 255, 255)
    return out


def main(out=None):
    tex_dir = os.path.join(g.ASSETS, "textures", "entity", "kid")
    os.makedirs(tex_dir, exist_ok=True)
    built = []
    methods, heights = [], []
    for name, fn, h in BUILDERS:
        G = fn()
        G.name = name
        G.height = h
        gg.use(G)
        tex = gg.texture(G)
        tex.save(os.path.join(tex_dir, f"{name}.png"))
        body = g.gen_java_body(G.parts, {p["name"]: p for p in G.parts})
        methods.append(f"""    /** {name} ({h} bloques, {G.units:.2f} unidades de alto). */
    public static TexturedModelData {name}() {{
        ModelData data = new ModelData();
{body}
        return TexturedModelData.of(data, {g.TEX}, {g.TEX});
    }}
""")
        heights.append(f'        "{name}", {G.units:.3f}f')
        built.append((G, tex))
    java = """package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Modelos de los hijos por etapa (bebé, niño/niña, chavo/chava, adultos). Los genera tools/gen_kids.py (no editar a mano).
 * Mismas piezas que Kiko, así que usan KikoModel con todas sus animaciones y caras.
 */
@SuppressWarnings("unused")
public final class KidModels {
    private KidModels() {}

    /** Alto de cada modelo en unidades (para escalarlo a su altura en bloques). */
    public static final java.util.Map<String, Float> UNITS = java.util.Map.of(
""" + ",\n".join(heights) + """);

""" + "\n".join(methods) + "}\n"
    with open(os.path.join(os.path.dirname(g.MODEL_JAVA), "KidModels.java"), "w") as f:
        f.write(java)

    if out:
        os.makedirs(out, exist_ok=True)
        preview(built, out)


def preview(built, out):
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 15)
    rng = random.Random(7)
    ppb = 150            # pixeles por bloque
    W, H = 200, 330
    rows = 3
    sheet = Image.new("RGBA", (W * len(built), H * rows + 30), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    labels = {"baby": "bebé", "child_boy": "niño", "child_girl": "niña", "teen_boy": "chavo", "teen_girl": "chava",
              "adult_man": "hijo adulto", "adult_woman": "hija adulta"}
    for i, (G, tex) in enumerate(built):
        d.text((i * W + 10, 6), labels[G.name], fill=(255, 210, 120), font=font)
        for r in range(rows):
            tinted = colorize(tex, rng.choice(SKINS), rng.choice(HAIRS), rng.choice(EYES), rng.choice(CLOTHES), rng.choice(CLOTHES),
                              rng.choice(SHOES))
            gg.use_no_pack(G)
            scale = ppb * G.height / G.units
            img = g.render(tinted, G.name, view_yaw=0.35, view_pitch=-0.1, scale=scale, size=(W, H - 10), center=(W / 2, H - 20),
                           hidden=gg.PREVIEW_HIDDEN)
            sheet.alpha_composite(img, (i * W, 30 + r * H))
    # línea del jugador
    for r in range(rows):
        y = 30 + r * H + H - 20 - ppb * 1.8
        for x in range(0, W * len(built), 12):
            d.line([(x, y), (x + 6, y)], fill=(120, 200, 255), width=1)
    sheet.convert("RGB").save(os.path.join(out, "hijos_etapas.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
