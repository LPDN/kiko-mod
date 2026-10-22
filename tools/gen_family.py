"""
Familia (v3.1). Modelos de los hijos e hijas, por etapa:
  baby            bebé envueltito (se dibuja acostado en la cuna)
  child_girl      niña        child_boy      niño
  teen_girl       adolescente teen_boy       adolescente
  adult_daughter  hija adulta adult_son      hijo adulto

Los colores de piel, pelo y ojos se heredan (de ti y de su mamá), así que no van pintados en la textura:
cada modelo tiene 4 capas y el juego pinta cada una con su color
  <nombre>.png        ropa y lo que no cambia (blanco de los ojos, boca, zapatos)
  <nombre>_skin.png   piel en gris (se tiñe con el color de piel)
  <nombre>_hair.png   pelo y cejas en gris
  <nombre>_eyes.png   iris en gris

Uso   python tools/gen_family.py [carpeta_preview]
"""
import math
import os
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_assets as g  # noqa: E402
import gen_girls as gg  # noqa: E402

c = g.c
CLEAR = None

# ------------------------------------------------------------------
# Materiales: ("S", brillo) piel, ("H", brillo) pelo, ("E", brillo) ojos
# ------------------------------------------------------------------
_orig_tone = g.tone


def _tone(col, k):
    if isinstance(col, tuple) and len(col) == 2 and isinstance(col[0], str):
        return (col[0], max(0.0, min(1.25, col[1] * k)))
    return _orig_tone(col, k)


g.tone = _tone
SK, HAIR, EYE = ("S", 1.0), ("H", 1.0), ("E", 1.0)


def m(mat, k):
    return (mat[0], k)


def head(G, base, bangs, girl, brows=0.55):
    """Cabeza estándar (10x11x10) con pelo, ojos y bocas de Kiko."""
    gg.build_head(G, base, SK, HAIR, m(HAIR, 0.78), m(HAIR, 1.12), EYE, m(EYE, 0.7), m(EYE, 1.2), c(30, 26, 34),
                  bangs, {}, blush=c(255, 190, 196) if girl else None)
    if not girl:
        # Pelo corto a los lados y atrás
        shell = G.paint["hair_shell"]

        def short(fn, cut):
            return lambda x, y, w, h: CLEAR if cut(x, y, w, h) else (fn(x, y, w, h) if callable(fn) else fn)
        shell["right"] = short(shell["right"], lambda x, y, w, h: y >= 5)
        shell["left"] = short(shell["left"], lambda x, y, w, h: y >= 5)
        shell["back"] = short(shell["back"], lambda x, y, w, h: y >= 8)
        shell["front"] = short(shell["front"], lambda x, y, w, h: x in (0, 9) and y >= 4)
        # Boca sin color de labial
        for p in G.parts:
            if p["name"].startswith("mouth_"):
                for cb in p["cubes"]:
                    pnt = G.paint.get(cb["id"], {})
                    for face, fn in list(pnt.items()):
                        def muted(x, y, w, h, fn=fn):
                            col = fn(x, y, w, h) if callable(fn) else fn
                            if col is None or isinstance(col[0], str) or (col[0] > 220 and col[1] > 220):
                                return col
                            return c(140, 86, 78) if col[0] > col[2] + 40 else col
                        pnt[face] = muted
    # Cejas del color del pelo pero más oscuras
    for p in G.parts:
        if p["name"].endswith("_brow"):
            for cb in p["cubes"]:
                G.paint[cb["id"]] = {"front": m(HAIR, brows)}


# ------------------------------------------------------------------
# Piezas de cuerpo genéricas (unidades del modelo; los pies en y = 0)
# ------------------------------------------------------------------
def limbs(G, arm_x, arm_top, upper, fore, arm_w, upper_p, fore_p, leg_x, leg_top, thigh, shin, leg_w, thigh_p, shin_p, foot_p,
          arm_dil=0.3, leg_dil=0.4):
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (arm_x * sx, arm_top - 0.5, 0), rot=(0, 0, 0.07 * -sx), cubes=[
            G.cube(f"{side}_arm", (arm_x * sx - arm_w / 2, arm_top - upper, -arm_w / 2), (arm_w, upper, arm_w), arm_dil, upper_p)])
        G.part(f"{side}_forearm", f"{side}_arm", (arm_x * sx, arm_top - upper + 0.5, 0), cubes=[
            G.cube(f"{side}_forearm", (arm_x * sx - arm_w / 2, arm_top - upper - fore, -arm_w / 2), (arm_w, fore, arm_w), arm_dil * 0.6, fore_p)])
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (leg_x * sx, leg_top, 0), cubes=[
            G.cube(f"{side}_thigh", (leg_x * sx - leg_w / 2, leg_top - thigh, -leg_w / 2), (leg_w, thigh, leg_w), leg_dil, thigh_p)])
        G.part(f"{side}_shin", f"{side}_leg", (leg_x * sx, leg_top - thigh, 0), cubes=[
            G.cube(f"{side}_shin", (leg_x * sx - leg_w / 2, 2, -leg_w / 2), (leg_w, leg_top - thigh - 2, leg_w), leg_dil * 0.8, shin_p),
            G.cube(f"{side}_foot", (leg_x * sx - leg_w / 2, 0, -leg_w / 2 - 1), (leg_w, 2, leg_w + 1), leg_dil * 0.6, foot_p),
        ])


def solid(main, shade=None):
    return gg.solid(main, shade)


def skin_solid():
    return gg.solid(SK, m(SK, 0.92), top=m(SK, 1.05))


# ------------------------------------------------------------------
# Bebé (envueltito en su cobija, con gorrito)
# ------------------------------------------------------------------
def baby():
    G = gg.Girl("baby", height=0.55)
    G.part("root", None, (0, 0, 0))
    base = 11
    head(G, base, {0: range(10), 1: [0, 1, 4, 5, 8, 9]}, girl=True)
    BL, BL_S = c(250, 236, 214), c(226, 208, 182)
    G.part("body", "root", (0, base, 0), cubes=[
        G.cube("blanket", (-4.5, 0, -3.5), (9, 11, 7), 0.4, {"*": lambda x, y, w, h: BL_S if (x + y) % 4 == 0 else BL, "top": BL}),
        G.cube("bow", (-1.5, 6, -4.4), (3, 1, 1), 0, {"*": c(250, 180, 200)}),
        G.cube("neck", (-1.5, base - 1, -1.5), (3, 1, 3), 0, {"*": m(SK, 0.93), "front": SK}),
    ])
    G.part("hat", "head", (0, base, 0), cubes=[
        G.cube("bonnet", (-5.5, base + 6, -5.6), (11, 6, 11), 0.3, {"*": lambda x, y, w, h: c(255, 214, 226) if y % 2 else c(255, 236, 242),
                                                                      "front": lambda x, y, w, h: c(255, 214, 226) if y >= 3 else CLEAR, "bottom": CLEAR}),
    ])
    # Brazos y piernas van dentro de la cobija (piezas vacías para las animaciones)
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (3 * sx, 9, 0))
        G.part(f"{side}_forearm", f"{side}_arm", (3 * sx, 6, 0))
        G.part(f"{side}_leg", "root", (2 * sx, 4, 0))
        G.part(f"{side}_shin", f"{side}_leg", (2 * sx, 2, 0))
    return G


# ------------------------------------------------------------------
# Niños (1.1 bloques). Cabeza grande, cuerpo cortito
# ------------------------------------------------------------------
def child_girl():
    G = gg.Girl("child_girl", height=1.1)
    G.part("root", None, (0, 0, 0))
    base = 27
    head(G, base, {0: range(10), 1: range(10), 2: [0, 1, 2, 3, 6, 7, 8, 9], 3: [0, 9]}, girl=True)
    # Dos coletas con moñitos
    G.part("ponytail", "head", (0, base + 7, 0), cubes=[
        G.cube("tail_r", (-7.4, base + 1, -1), (2, 7, 2), 0.3, gg.pattern(lambda x, y, w, h: m(HAIR, 0.85) if y % 2 else HAIR)),
        G.cube("tail_l", (5.4, base + 1, -1), (2, 7, 2), 0.3, gg.pattern(lambda x, y, w, h: m(HAIR, 0.85) if y % 2 else HAIR)),
        G.cube("bow_r", (-7.9, base + 7.5, -1.5), (3, 1, 3), 0.1, gg.pattern(c(240, 90, 120))),
        G.cube("bow_l", (4.9, base + 7.5, -1.5), (3, 1, 3), 0.1, gg.pattern(c(240, 90, 120))),
    ])
    G.part("hair_back", "head", (0, base + 6, 5.6), cubes=[
        G.cube("hair_back", (-5.5, base + 3, 5.6), (11, 8, 2), 0, {"*": m(HAIR, 0.85), "back": lambda x, y, w, h: m(HAIR, 0.8) if x % 3 == 1 else HAIR})])
    DR, DR_S, DR_D = c(250, 150, 176), c(226, 122, 150), c(196, 96, 124)
    COL = c(255, 255, 255)
    G.part("body", "root", (0, 26, 0), cubes=[
        G.cube("neck", (-1.5, 26, -1.5), (3, 1, 3), 0, {"*": m(SK, 0.93), "front": SK}),
        G.cube("chest", (-3.5, 19, -2), (7, 7, 4), 0.3, gg.solid(DR, DR_S, front=lambda x, y, w, h: COL if y == 0 and 1 <= x <= 5 else (DR_D if x == 3 and y % 2 == 1 else DR))),
        G.cube("dress_skirt", (-4.5, 9, -3), (9, 10, 6), 0.4, {"*": lambda x, y, w, h: DR_D if x % 3 == 0 else (COL if y == h - 1 else DR), "top": DR, "bottom": DR_S}),
    ])
    limbs(G, 4.6, 26, 6, 6, 3, skin_solid() | {"top": DR}, gg.pattern(lambda x, y, w, h: SK if y < 5 else m(SK, 0.9)),
          1.7, 13, 6, 6, 3, skin_solid(), gg.pattern(lambda x, y, w, h: c(255, 255, 255) if y >= 1 else SK),
          gg.pattern(g.rows((0, c(220, 70, 100)), (1, c(180, 50, 80)))), arm_dil=0.25, leg_dil=0.3)
    # El brazo de arriba con manguita
    for side in ("right", "left"):
        G.paint[f"{side}_arm"] = gg.pattern(g.rows((2, DR), (5, SK)))
    return G


def child_boy():
    G = gg.Girl("child_boy", height=1.1)
    G.part("root", None, (0, 0, 0))
    base = 27
    head(G, base, {0: range(10), 1: range(10), 2: [0, 2, 3, 5, 7, 9], 3: [0, 9]}, girl=False)
    G.part("spikes", "head", (0, base, 0), cubes=[
        G.cube("spike1", (-3, base + 11.4, -2), (3, 2, 3), 0.3, gg.solid(HAIR, m(HAIR, 0.85))),
        G.cube("spike2", (1, base + 11.6, -4), (3, 2, 3), 0.3, gg.solid(HAIR, m(HAIR, 0.85))),
    ])
    TEE, TEE_S, STRIPE = c(90, 150, 220), c(70, 124, 190), c(250, 250, 250)
    SH, SH_S = c(150, 110, 70), c(120, 86, 54)
    G.part("body", "root", (0, 26, 0), cubes=[
        G.cube("neck", (-1.5, 26, -1.5), (3, 1, 3), 0, {"*": m(SK, 0.93), "front": SK}),
        G.cube("chest", (-3.5, 17, -2), (7, 9, 4), 0.3, gg.pattern(lambda x, y, w, h: STRIPE if y in (3, 4) else (TEE_S if x in (0, w - 1) else TEE))),
        G.cube("hips", (-3.5, 13, -2), (7, 4, 4), 0.35, gg.solid(SH, SH_S)),
    ])
    limbs(G, 4.6, 26, 6, 6, 3, gg.pattern(g.rows((2, TEE), (5, SK))), gg.pattern(lambda x, y, w, h: SK),
          1.7, 13, 6, 6, 3, gg.pattern(g.rows((2, g.shade_sides(SH, SH_S)), (5, SK))),
          gg.pattern(lambda x, y, w, h: c(255, 255, 255) if y >= 1 else SK),
          gg.pattern(g.rows((0, c(230, 80, 60)), (1, c(250, 250, 250)))), arm_dil=0.25, leg_dil=0.3)
    return G


# ------------------------------------------------------------------
# Adolescentes (1.55 bloques)
# ------------------------------------------------------------------
def teen_girl():
    G = gg.Girl("teen_girl", height=1.55)
    G.part("root", None, (0, 0, 0))
    base = 38
    head(G, base, {0: range(10), 1: range(10), 2: [0, 1, 2, 3, 4, 7, 8, 9], 3: [0, 1, 9]}, girl=True)
    G.part("hair_back", "head", (0, base + 6, 5.6), cubes=[
        G.cube("hair_back", (-5.5, base - 10, 5.6), (11, 21, 2), 0, {"*": m(HAIR, 0.85), "back": lambda x, y, w, h: m(HAIR, 0.8) if x % 3 == 1 else HAIR})])
    G.part("clip", "head", (0, base, 0), cubes=[
        G.cube("clip", (2.5, base + 8.4, -6.0), (2, 2, 1), 0, {"*": c(140, 210, 255)})])
    HO, HO_S, HO_D = c(170, 214, 200), c(140, 188, 174), c(110, 160, 146)
    JE, JE_S = c(72, 98, 150), c(56, 80, 126)
    G.part("body", "root", (0, 36, 0), cubes=[
        G.cube("neck", (-1.5, 36, -1.5), (3, 2, 3), 0, {"*": m(SK, 0.93), "front": SK}),
        G.cube("chest", (-4, 26, -2), (8, 10, 4), 0.4, gg.solid(HO, HO_S, front=lambda x, y, w, h: HO_D if (y == 7 and 2 <= x <= 5) else (c(255, 255, 255) if (y == 1 and x in (3, 4)) else HO))),
        G.cube("hood_back", (-3.5, 33, 2.0), (7, 3, 2), 0.3, gg.solid(HO, HO_S)),
        G.cube("hips", (-4, 21, -2), (8, 5, 4), 0.45, gg.solid(JE, JE_S)),
    ])
    limbs(G, 5.4, 36, 8, 7, 3, gg.solid(HO, HO_S), gg.pattern(g.rows((4, g.shade_sides(HO, HO_S)), (5, HO_D), (6, SK))),
          2.0, 21, 10, 9, 3, gg.solid(JE, JE_S), gg.pattern(g.rows((6, g.shade_sides(JE, JE_S)), (8, c(250, 250, 250)))),
          gg.pattern(g.rows((0, c(250, 250, 250)), (1, c(200, 200, 210)))), arm_dil=0.3, leg_dil=0.45)
    return G


def teen_boy():
    G = gg.Girl("teen_boy", height=1.6)
    G.part("root", None, (0, 0, 0))
    base = 39
    head(G, base, {0: range(10), 1: range(10), 2: [0, 1, 3, 4, 6, 8, 9], 3: [0, 9]}, girl=False, brows=0.5)
    G.part("spikes", "head", (0, base, 0), cubes=[
        G.cube("spike1", (-4, base + 11.4, -3), (3, 2, 3), 0.3, gg.solid(HAIR, m(HAIR, 0.85))),
        G.cube("spike2", (0, base + 11.7, -1), (4, 2, 3), 0.3, gg.solid(HAIR, m(HAIR, 0.85))),
        G.cube("fringe", (-5.4, base + 8.5, -5.9), (6, 2, 1), 0.2, gg.pattern(HAIR)),
    ])
    JK, JK_S, JK_D = c(60, 70, 96), c(46, 54, 76), c(34, 40, 58)
    TEE = c(240, 240, 236)
    JE, JE_S = c(90, 106, 138), c(70, 84, 112)
    G.part("body", "root", (0, 37, 0), cubes=[
        G.cube("neck", (-1.5, 37, -1.5), (3, 2, 3), 0, {"*": m(SK, 0.93), "front": SK}),
        G.cube("chest", (-4.5, 26, -2.25), (9, 11, 4), 0.5, gg.solid(JK, JK_S, front=lambda x, y, w, h: TEE if 3 <= x <= 5 else (JK_D if x in (2, 6) else JK))),
        G.cube("hips", (-4.5, 22, -2.25), (9, 4, 4), 0.5, gg.solid(JE, JE_S)),
    ])
    limbs(G, 6.0, 37, 8, 7, 3, gg.solid(JK, JK_S), gg.pattern(g.rows((5, g.shade_sides(JK, JK_S)), (6, SK))),
          2.25, 22, 10, 10, 3, gg.solid(JE, JE_S), gg.pattern(g.rows((7, g.shade_sides(JE, JE_S)), (9, c(40, 40, 46)))),
          gg.pattern(g.rows((0, c(250, 250, 250)), (1, c(40, 40, 46)))), arm_dil=0.4, leg_dil=0.5)
    return G


# ------------------------------------------------------------------
# Adultos (la hija como las chicas, el hijo con cuerpo de hombre)
# ------------------------------------------------------------------
def adult_daughter():
    G = gg.Girl("adult_daughter", height=1.75)
    G.part("root", None, (0, 0, 0))
    head(G, 50, {0: range(10), 1: range(10), 2: [0, 1, 2, 3, 6, 7, 8, 9], 3: [0, 1, 8, 9]}, girl=True)
    G.part("hair_back", "head", (0, 56, 6.2), cubes=[
        G.cube("hair_back", (-5.5, 36, 5.6), (11, 20, 2), 0, {"*": m(HAIR, 0.85), "back": lambda x, y, w, h: m(HAIR, 0.8) if x % 3 == 1 else HAIR})])
    BL, BL_S = c(246, 238, 226), c(222, 212, 196)
    SKT, SKT_S, SKT_D = c(120, 90, 140), c(100, 72, 120), c(80, 56, 98)
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": m(SK, 0.93), "front": SK}),
    ])
    gg.curves(G, BL, SKT, bust_scale=0.8, glute_scale=0.8)
    gg.skirt_part(G, "casual_skirt", 31, 12, SKT, SKT_S, SKT_D, pleats=3, wide=11, deep=7, dil=0.45)
    limbs(G, 6.6, 48, 11, 10, 3, gg.solid(BL, BL_S), gg.pattern(g.rows((6, g.shade_sides(BL, BL_S)), (9, SK))),
          2.6, 29, 14, 13, 4, gg.pattern(g.shade_sides(SK, m(SK, 0.93))), gg.pattern(g.shade_sides(SK, m(SK, 0.93))),
          gg.pattern(g.rows((0, c(110, 70, 60)), (1, c(80, 50, 44)))), arm_dil=0.45, leg_dil=0.6)
    return G


def adult_son():
    G = gg.Girl("adult_son", height=1.85)
    G.part("root", None, (0, 0, 0))
    head(G, 50, {0: range(10), 1: range(10), 2: [0, 1, 3, 4, 7, 9], 3: [0, 9]}, girl=False, brows=0.45)
    G.part("spikes", "head", (0, 50, 0), cubes=[
        G.cube("spike1", (-4, 61.4, -3), (4, 2, 3), 0.3, gg.solid(HAIR, m(HAIR, 0.85))),
        G.cube("spike2", (0.5, 61.7, -1), (3, 2, 4), 0.3, gg.solid(HAIR, m(HAIR, 0.85))),
    ])
    SH, SH_S, SH_D = c(110, 150, 190), c(90, 126, 164), c(70, 102, 136)
    JE, JE_S = c(60, 74, 104), c(46, 58, 84)
    BOOT = c(90, 62, 42)
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("neck", (-1.75, 48, -1.75), (3, 2, 3), 0.25, {"*": m(SK, 0.93), "front": SK}),
        # Torso derecho, hombros anchos y pecho plano
        G.cube("chest", (-5.5, 38, -2.75), (11, 10, 5), 0.6, gg.solid(SH, SH_S, front=lambda x, y, w, h: SK if (y == 0 and 4 <= x <= 6) else (SH_D if x == 5 and y % 3 == 1 else (SH_S if x in (0, w - 1) else SH)))),
        G.cube("waist", (-4.75, 33, -2.5), (9, 5, 5), 0.5, gg.solid(SH, SH_S)),
        G.cube("belt", (-5, 32.5, -2.75), (10, 1, 5), 0.55, {"*": c(60, 44, 32), "front": lambda x, y, w, h: c(200, 190, 160) if x == w // 2 else c(60, 44, 32)}),
        G.cube("hips", (-4.75, 28, -2.5), (9, 5, 5), 0.55, gg.solid(JE, JE_S)),
    ])
    limbs(G, 7.4, 48, 11, 10, 4, gg.solid(SH, SH_S), gg.pattern(g.rows((3, g.shade_sides(SH_S, SH_D)), (9, SK))),
          2.6, 29, 14, 13, 4, gg.solid(JE, JE_S), gg.pattern(g.rows((8, g.shade_sides(JE, JE_S)), (11, BOOT))),
          gg.pattern(g.rows((0, BOOT), (1, c(66, 44, 30)))), arm_dil=0.5, leg_dil=0.7)
    return G


MODELS = [baby, child_girl, child_boy, teen_girl, teen_boy, adult_daughter, adult_son]


# ------------------------------------------------------------------
# Texturas por capas
# ------------------------------------------------------------------
LAYERS = {"S": "skin", "H": "hair", "E": "eyes"}


def layered_textures(G):
    imgs = {k: Image.new("RGBA", (g.TEX, g.TEX), (0, 0, 0, 0)) for k in ("base", "S", "H", "E")}
    pxs = {k: im.load() for k, im in imgs.items()}
    for p in G.parts:
        for cb in p["cubes"]:
            painters = G.paint.get(cb["id"], {"*": c(255, 0, 255)})
            for face, (fx, fy, fw, fh) in g.face_rects(cb).items():
                pnt = painters.get(face, painters.get("*"))
                if pnt is None:
                    continue
                for yy in range(fh):
                    for xx in range(fw):
                        col = pnt(xx, yy, fw, fh) if callable(pnt) else pnt
                        if col is None:
                            continue
                        if isinstance(col[0], str):
                            v = max(0, min(255, int(232 * col[1])))
                            pxs[col[0]][fx + xx, fy + yy] = (v, v, v, 255)
                        else:
                            pxs["base"][fx + xx, fy + yy] = col
    return imgs


def tinted(imgs, skin, hair, eyes):
    """Junta las capas con colores de ejemplo (para las vistas previas, en el juego lo hace el render)."""
    out = imgs["base"].copy()
    for k, col in (("S", skin), ("H", hair), ("E", eyes)):
        lay = imgs[k]
        r, gch, b, a = lay.split()
        tint = Image.merge("RGBA", (r.point(lambda v, t=col[0]: min(255, v * t // 232)),
                                    gch.point(lambda v, t=col[1]: min(255, v * t // 232)),
                                    b.point(lambda v, t=col[2]: min(255, v * t // 232)), a))
        out.alpha_composite(tint)
    return out


def export(models):
    tex_dir = os.path.join(g.ASSETS, "textures", "entity", "family")
    os.makedirs(tex_dir, exist_ok=True)
    methods = []
    for G in models:
        gg.use(G)
        imgs = layered_textures(G)
        imgs["base"].save(os.path.join(tex_dir, f"{G.name}.png"))
        for k, suf in LAYERS.items():
            imgs[k].save(os.path.join(tex_dir, f"{G.name}_{suf}.png"))
        body = g.gen_java_body(G.parts, {p["name"]: p for p in G.parts})
        top = max(cb["frm"][1] + cb["size"][1] + cb["dil"] for p in G.parts for cb in p["cubes"])
        methods.append(f"""    /** {G.name} ({G.height} bloques, mide {top:.2f} unidades). */
    public static final float {G.name.upper()}_UNITS = {top:.2f}f;

    public static TexturedModelData {G.name}() {{
        ModelData data = new ModelData();
{body}
        return TexturedModelData.of(data, {g.TEX}, {g.TEX});
    }}
""")
    java = """package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Modelos de los hijos e hijas (bebé, niños, adolescentes y adultos). Los genera tools/gen_family.py (no editar a mano).
 * Mismas piezas que Kiko, así que usan KikoModel con todas sus animaciones y caras.
 * La piel, el pelo y los ojos se pintan al dibujar (capas en textures/entity/family).
 */
@SuppressWarnings("unused")
public final class FamilyModels {
    private FamilyModels() {}

""" + "\n".join(methods) + "}\n"
    with open(os.path.join(os.path.dirname(g.MODEL_JAVA), "FamilyModels.java"), "w") as f:
        f.write(java)


def preview(models, out):
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 15)
    combos = [  # piel, pelo, ojos
        (c(250, 222, 206), c(230, 110, 100), c(214, 150, 60)),
        (c(200, 146, 108), c(70, 50, 40), c(70, 140, 90)),
        (c(140, 96, 70), c(30, 28, 34), c(120, 80, 50)),
    ]
    labels = {"baby": "bebé", "child_girl": "niña", "child_boy": "niño", "teen_girl": "adolescente",
              "teen_boy": "adolescente", "adult_daughter": "hija adulta", "adult_son": "hijo adulto"}
    W, H = 200, 420
    sheet = Image.new("RGBA", (W * len(models), H * len(combos) + 30), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    for i, G in enumerate(models):
        gg.use(G)
        imgs = layered_textures(G)
        top = max(cb["frm"][1] + cb["size"][1] for p in G.parts for cb in p["cubes"])
        scale = G.height * 180 / top       # misma escala para todos: 180 px = 1 bloque
        for j, combo in enumerate(combos):
            tex = tinted(imgs, *combo)
            gg.use_no_pack(G)
            r = g.render(tex, G.name, view_yaw=0.45, view_pitch=-0.1, scale=scale, size=(W, H), center=(W / 2, H - 14),
                         hidden=gg.PREVIEW_HIDDEN)
            sheet.alpha_composite(r, (i * W, 30 + j * H))
        d.text((i * W + 10, 6), labels[G.name], fill=(255, 210, 120), font=font)
    sheet.convert("RGB").save(os.path.join(out, "familia.png"))


def main(out=None):
    models = [gg.complete(fn()) for fn in MODELS]
    export(models)
    if out:
        os.makedirs(out, exist_ok=True)
        preview(models, out)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
