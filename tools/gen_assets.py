"""
Kiko Fase 2. Genera desde una sola definición
  - las texturas de los 3 outfits (256x256)
  - la geometría Java de KikoModel (entre los marcadores // <gen> y // </gen>)
  - kiko.bbmodel para Blockbench
  - los íconos de ítems y el ícono del mod
  - vistas previas (cuerpo completo y close-up de los ojos)

Uso   python tools/gen_assets.py [carpeta_para_previews]
Requiere Pillow.

Coordenadas en PARTS
  x  derecha de ella es -x
  y  hacia ARRIBA desde los pies (el script lo convierte al sistema de Minecraft)
  z  el frente es -z
"""
import base64
import io
import json
import math
import os
import re
import sys
import uuid

from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "kiko")
MODEL_JAVA = os.path.join(ROOT, "src", "main", "java", "com", "jaciel", "kiko", "client", "model", "KikoModel.java")
TEX = 256
OUTFITS = ["school", "casual1", "casual2", "beach", "winter", "lumberjack", "miner", "farmer", "fisher", "wedding"]
NEW_OUTFITS = OUTFITS[3:]
# Piezas visibles por outfit (igual que KikoOutfit.java)
FLAGS = {
    "school": {"skirt"}, "casual1": {"hood"}, "casual2": {"skirt"},
    "beach": {"skirt", "hat", "dress"}, "winter": {"beanie", "scarf"},
    "lumberjack": {"beanie"}, "miner": {"hardhat"}, "farmer": {"hat"}, "fisher": {"hat"},
    "wedding": {"skirt", "dress", "kiko_veil"},
}
OPTIONAL_PARTS = ["skirt", "hood", "hat", "hardhat", "beanie", "scarf", "dress", "kiko_veil"]


# ------------------------------------------------------------------
# Geometría
# part(nombre, padre, pivote_abs, rot, [cubo(id, desde_abs, tamaño, dilation)])
# ------------------------------------------------------------------
PARTS = []


def part(name, parent, pivot, rot=(0, 0, 0), cubes=()):
    PARTS.append(dict(name=name, parent=parent, pivot=pivot, rot=rot, cubes=list(cubes)))


def cube(cid, frm, size, dil=0.0):
    return dict(id=cid, frm=frm, size=size, dil=dil)


EYE_Z = -5.15

# Complexión (Fase 3, nivel "media"). El busto y los glúteos se arman por capas
# (centro, dos volúmenes, transición arriba y abajo) para que se vean redondeados y no como caja.
SHAPE = dict(hips=0.8, thigh=0.85, skirt_back=3)
BUST_CUBES = [
    # transición del pecho al busto
    ("bust_upper", (-3.5, 45.4, -3.8), (7, 1, 1), 0.25),
    ("bust_slope", (-4, 44.3, -4.7), (8, 1, 2), 0.3),
    # volumen central
    ("bust", (-4.5, 40.6, -5.4), (9, 4, 3), 0.3),
    # dos volúmenes al frente con un pliegue en medio
    ("right_bust_lobe", (-4.3, 41.0, -5.9), (4, 3, 1), 0.25),
    ("left_bust_lobe", (0.3, 41.0, -5.9), (4, 3, 1), 0.25),
    # parte de abajo, más metida
    ("bust_low", (-4, 39.8, -5.0), (8, 1, 2), 0.2),
]
GLUTE_CUBES = [
    ("glutes_top", (-4, 33.0, 2.0), (8, 1, 2), 0.3),
    ("glutes", (-4.5, 28.4, 2.0), (9, 5, 3), 0.5),
    ("right_glute_lobe", (-4.3, 28.9, 4.55), (4, 4, 1), 0.25),
    ("left_glute_lobe", (0.3, 28.9, 4.55), (4, 4, 1), 0.25),
    ("glutes_low", (-4, 27.7, 2.0), (8, 1, 2), 0.3),
]
part("root", None, (0, 0, 0))

# Cabeza 10x11x10 con capa de pelo inflada
part("head", "root", (0, 50, 0), cubes=[
    cube("head", (-5, 50, -5), (10, 11, 10)),
    cube("hair_shell", (-5, 50, -5), (10, 11, 10), 0.75),
])
part("hair_back", "head", (0, 56, 6.2), cubes=[cube("hair_back", (-5.5, 37, 5.6), (11, 19, 2))])
part("ponytail", "head", (6.0, 58.5, 0.5), rot=(0, 0, -0.18), cubes=[cube("ponytail", (5.0, 45, -1.0), (3, 13, 3))])
part("clip", "head", (0, 50, 0), cubes=[cube("clip", (-4.5, 58, -6.0), (2, 2, 1))])

# Ojos. Planos sin grosor encima de la cara, pivote abajo para que el párpado baje al parpadear
for side, x0 in (("right", -4), ("left", 1)):
    part(f"{side}_eye", "head", (x0 + 1.5, 53, EYE_Z), cubes=[cube(f"{side}_eye", (x0, 53, EYE_Z), (3, 4, 0))])
    part(f"{side}_pupil", f"{side}_eye", (x0 + 1.5, 53, EYE_Z - 0.15), cubes=[cube(f"{side}_pupil", (x0 + 1, 53, EYE_Z - 0.15), (1, 2, 0))])
    part(f"{side}_eye_closed", "head", (x0 + 1.5, 54, EYE_Z), cubes=[cube(f"{side}_eye_closed", (x0, 54, EYE_Z), (3, 1, 0))])
    part(f"{side}_eye_happy", "head", (x0 + 1.5, 54, EYE_Z), cubes=[cube(f"{side}_eye_happy", (x0, 54, EYE_Z), (3, 2, 0))])

# Fase 4: cejas, bocas y extras de la cara (piezas planas encima de la cara, se prenden y apagan)
for side, x0 in (("right", -4), ("left", 1)):
    part(f"{side}_brow", "head", (x0 + 1.5, 57.5, EYE_Z - 0.05), cubes=[cube(f"{side}_brow", (x0, 57, EYE_Z - 0.05), (3, 1, 0))])
MOUTHS = {
    # 4x3, filas de arriba a abajo. M labio, F colmillo, I adentro, T lengua, . vacío
    "neutral":  [".MM.", "....", "...."],
    "smile":    ["M..M", ".MM.", "...."],
    "grin":     ["MMMM", ".II.", "...."],
    "open":     [".MM.", ".II.", "...."],
    "laugh":    ["MMMM", "MIIM", ".TT."],
    "o":        [".MM.", "MIIM", ".MM."],
    "frown":    ["....", ".MM.", "M..M"],
    "pout":     ["....", ".MM.", ".MM."],
    "smirk":    ["...M", "MMM.", "...."],
    "sleepy":   ["....", ".MM.", "...."],
}
for mname in MOUTHS:
    part(f"mouth_{mname}", "head", (0, 52, EYE_Z), cubes=[cube(f"mouth_{mname}", (-2, 49, EYE_Z), (4, 3, 0))])
part("blush", "head", (0, 52, EYE_Z), cubes=[
    cube("right_blush", (-4.5, 51.5, EYE_Z - 0.03), (3, 2, 0)),
    cube("left_blush", (1.5, 51.5, EYE_Z - 0.03), (3, 2, 0)),
])
part("sweat", "head", (4.5, 57, EYE_Z), cubes=[cube("sweat", (4, 55.5, EYE_Z - 0.04), (1, 2, 0))])
part("tears", "head", (0, 52, EYE_Z), cubes=[
    cube("right_tear", (-4, 51, EYE_Z - 0.04), (1, 2, 0)),
    cube("left_tear", (3, 51, EYE_Z - 0.04), (1, 2, 0)),
])
part("anger", "head", (3.5, 60, -5.9), cubes=[cube("anger", (2, 58.5, -5.9), (3, 3, 0))])

# Torso (pivote en el cuello como el jugador)
part("body", "root", (0, 48, 0), cubes=[
    cube("chest", (-4.5, 39, -2.5), (9, 9, 5), 0.45),
    *[cube(*c) for c in BUST_CUBES],
    cube("waist", (-4, 34, -2), (8, 5, 4), 0.35),
    cube("hips", (-5, 28, -2.5), (10, 6, 5), SHAPE["hips"]),
    *[cube(*c) for c in GLUTE_CUBES],
    cube("neck", (-1.5, 48, -1.5), (3, 2, 3)),
])
_SB = SHAPE["skirt_back"]
part("skirt", "body", (0, 34, 0), cubes=[
    cube("skirt_top", (-5.5, 30, -3), (11, 4, 6 + _SB), 0.55),
    cube("skirt_flare", (-6, 24, -3.5), (12, 6, 7 + _SB), 0.6),
])
part("hood", "body", (0, 48, 0), cubes=[cube("hood", (-3.5, 42, 2.5), (7, 6, 2))])
# Vestido largo (playa), bufanda (invierno)
part("dress", "skirt", (0, 34, 0), cubes=[cube("dress", (-6.5, 18, -4), (13, 6, 8 + _SB), 0.6)])
part("scarf", "body", (0, 48, 0), cubes=[
    cube("scarf", (-5, 46, -3), (10, 2, 6), 0.35),
    cube("scarf_tail", (1.2, 43.0, -6.4), (2, 3, 1), 0.1),
])
# Accesorios de cabeza
part("hat", "head", (0, 50, 0), cubes=[
    cube("hat_brim", (-7, 60.4, -7), (14, 1, 14)),
    cube("hat_crown", (-4.5, 61.4, -4.5), (9, 3, 9), 0.1),
])
part("hardhat", "head", (0, 50, 0), cubes=[
    cube("hardhat_dome", (-5.5, 59.6, -5.5), (11, 3, 11), 0.35),
    cube("hardhat_brim", (-5.5, 59.3, -8.4), (11, 1, 3)),
    cube("hardhat_lamp", (-1, 60.2, -6.9), (2, 2, 1)),
])
part("beanie", "head", (0, 50, 0), cubes=[
    cube("beanie", (-5.5, 58.5, -5.5), (11, 4, 11), 0.4),
    cube("pompom", (-1, 62.9, -1), (2, 2, 2), 0.2),
])
# Velo de novia con corona de flores (v3.0)
part("kiko_veil", "head", (0, 50, 0), cubes=[cube("veil", (-5.5, 36, 7.75), (11, 25, 0))] + [
    cube(f"crown_{i}", (fx, 61.3, fz), (1, 1, 1), 0.3)
    for i, (fx, fz) in enumerate([(-4.5, -5.6), (-2.0, -5.9), (0.5, -5.9), (3.0, -5.6), (-5.6, -3), (4.6, -3), (-5.6, 0), (4.6, 0)])])

# Brazos con codo
for side, sx in (("right", -1), ("left", 1)):
    part(f"{side}_arm", "root", (6.6 * sx, 47.5, 0), cubes=[cube(f"{side}_arm", (6.6 * sx - 1.5, 37, -1.5), (3, 11, 3), 0.3)])
    part(f"{side}_forearm", f"{side}_arm", (6.6 * sx, 37.5, 0), cubes=[cube(f"{side}_forearm", (6.6 * sx - 1.5, 27, -1.5), (3, 10, 3), 0.1)])

# Piernas con rodilla
for side, sx in (("right", -1), ("left", 1)):
    part(f"{side}_leg", "root", (2.75 * sx, 29, 0), cubes=[cube(f"{side}_thigh", (2.75 * sx - 2, 15, -2), (4, 14, 4), SHAPE["thigh"])])
    part(f"{side}_shin", f"{side}_leg", (2.75 * sx, 15, 0), cubes=[
        cube(f"{side}_shin", (2.75 * sx - 1.5, 2, -1.5), (3, 13, 3), 0.55),
        cube(f"{side}_foot", (2.75 * sx - 1.5, 0, -2.5), (3, 2, 4), 0.35),
    ])

PART = {p["name"]: p for p in PARTS}
MODEL_HEIGHT = 61.75  # punta del pelo


# ------------------------------------------------------------------
# Empaquetado UV (box UV de Minecraft)
# ------------------------------------------------------------------
def footprint(c):
    w, h, d = c["size"]
    return 2 * (w + d), h + d


def pack():
    cubes = [c for p in PARTS for c in p["cubes"]]
    order = sorted(cubes, key=lambda c: (-footprint(c)[1], -footprint(c)[0]))
    x = y = row_h = 0
    for c in order:
        fw, fh = footprint(c)
        if x + fw > TEX:
            x, y, row_h = 0, y + row_h, 0
        c["uv"] = (x, y)
        x += fw
        row_h = max(row_h, fh)
    assert y + row_h <= TEX, "La textura no alcanza"


pack()


def face_rects(c):
    u, v = c["uv"]
    w, h, d = c["size"]
    return {
        "top": (u + d, v, w, d),
        "bottom": (u + d + w, v, w, d),
        "right": (u, v + d, d, h),
        "front": (u + d, v + d, w, h),
        "left": (u + d + w, v + d, d, h),
        "back": (u + 2 * d + w, v + d, w, h),
    }


# ------------------------------------------------------------------
# Paleta (diseño original de Kiko)
# ------------------------------------------------------------------
def c(r, g, b, a=255):
    return (r, g, b, a)


CLEAR = None
SKIN, SKIN_S, SKIN_D = c(255, 226, 206), c(244, 204, 182), c(226, 178, 158)
HAIR, HAIR_D, HAIR_L, HAIR_DD = c(255, 128, 112), c(226, 94, 88), c(255, 170, 150), c(196, 72, 72)
LASH, IRIS, IRIS_D, IRIS_L, WHITE = c(74, 34, 44), c(236, 156, 44), c(170, 90, 28), c(255, 206, 110), c(255, 255, 255)
PUPIL = c(110, 52, 30)
BLUSH, MOUTH, FANG = c(255, 160, 168), c(170, 62, 76), c(255, 255, 255)
# Fase 4: expresiones
MOUTH_IN, TONGUE = c(110, 28, 44), c(236, 112, 124)
BLUSH_D = c(246, 118, 136)
SWEAT, SWEAT_L = c(140, 196, 255), c(228, 244, 255)
ANGER = c(226, 40, 56)
STAR, STAR_L, STAR_D = c(255, 210, 60), c(255, 238, 150), c(222, 168, 40)

PAL = {
    "school": dict(top=c(248, 248, 252), top_s=c(222, 224, 236), top_d=c(196, 198, 214),
                   acc=c(214, 52, 70), acc_d=c(160, 30, 50),
                   skirt=c(46, 56, 104), skirt_d=c(32, 40, 80), skirt_l=c(70, 82, 132),
                   leg=c(52, 50, 66), leg_s=c(40, 38, 52), shoe=c(118, 70, 42), shoe_d=c(80, 46, 28)),
    "casual1": dict(top=c(140, 216, 192), top_s=c(112, 186, 164), top_d=c(88, 158, 138),
                    acc=c(250, 250, 250), acc_d=c(210, 214, 220),
                    leg=c(74, 112, 178), leg_s=c(58, 90, 150), leg_l=c(108, 144, 204),
                    shoe=c(244, 244, 246), shoe_d=c(160, 160, 170)),
    "beach": dict(top=c(252, 252, 250), top_s=c(232, 232, 228), top_d=c(208, 210, 206),
                  acc=c(120, 190, 230), acc_d=c(80, 150, 200), style="dress",
                  hat=c(250, 246, 236), hat_s=c(226, 218, 200), hat_band=c(120, 190, 230),
                  shoe=c(176, 120, 74), shoe_d=c(140, 90, 54)),
    "winter": dict(top=c(150, 40, 58), top_s=c(122, 30, 46), top_d=c(96, 22, 36),
                   acc=c(236, 200, 120), acc_d=c(70, 16, 28), style="coat",
                   leg=c(52, 50, 64), leg_s=c(40, 38, 50), shoe=c(110, 70, 44), shoe_d=c(78, 48, 30),
                   scarf=c(246, 246, 250), scarf_s=c(214, 60, 70),
                   beanie=c(246, 246, 250), beanie_s=c(206, 210, 222), pom=c(214, 60, 70)),
    "lumberjack": dict(top=c(196, 40, 44), top_s=c(150, 26, 30), top_d=c(40, 30, 34),
                       acc=c(40, 30, 34), acc_d=c(110, 72, 40), style="plaid",
                       leg=c(58, 74, 120), leg_s=c(44, 58, 96), shoe=c(112, 70, 40), shoe_d=c(80, 48, 26),
                       beanie=c(150, 30, 34), beanie_s=c(110, 20, 24), pom=c(150, 30, 34)),
    "miner": dict(top=c(150, 154, 160), top_s=c(124, 128, 134), top_d=c(100, 104, 110),
                  acc=c(54, 66, 110), acc_d=c(40, 50, 86), style="over",
                  leg=c(54, 66, 110), leg_s=c(40, 50, 86), shoe=c(46, 42, 40), shoe_d=c(30, 28, 26),
                  hard=c(250, 200, 40), hard_s=c(214, 160, 20), lamp=c(255, 250, 200), lamp_s=c(150, 150, 150)),
    "farmer": dict(top=c(250, 236, 170), top_s=c(226, 208, 140), top_d=c(200, 184, 120),
                   acc=c(80, 120, 186), acc_d=c(60, 96, 156), style="over",
                   leg=c(80, 120, 186), leg_s=c(60, 96, 156), shoe=c(118, 76, 44), shoe_d=c(84, 52, 30),
                   hat=c(236, 206, 120), hat_s=c(206, 172, 90), hat_band=c(196, 52, 52)),
    "fisher": dict(top=c(170, 206, 236), top_s=c(140, 178, 210), top_d=c(116, 150, 180),
                   acc=c(108, 116, 62), acc_d=c(82, 88, 44), style="vest",
                   leg=c(186, 168, 120), leg_s=c(160, 142, 98), shoe=c(52, 92, 56), shoe_d=c(36, 66, 40),
                   hat=c(186, 168, 120), hat_s=c(160, 142, 98), hat_band=c(108, 116, 62)),
    "wedding": dict(top=c(252, 250, 246), top_s=c(232, 228, 222), top_d=c(214, 210, 204),
                    acc=c(255, 190, 206), acc_d=c(230, 150, 172), style="dress", flower=True,
                    shoe=c(250, 248, 244), shoe_d=c(214, 210, 204)),
    "casual2": dict(top=c(238, 186, 72), top_s=c(214, 158, 54), top_d=c(184, 128, 40),
                    acc=c(250, 250, 250), acc_d=c(220, 220, 226),
                    skirt=c(196, 48, 62), skirt_d=c(140, 30, 45), skirt_l=c(232, 120, 120),
                    leg=c(40, 38, 46), leg_s=c(30, 28, 36), shoe=c(124, 78, 46), shoe_d=c(90, 54, 34)),
}


# ------------------------------------------------------------------
# Pintores. Cada uno regresa {cara: color | función(x, y, w, h) | None}
# ------------------------------------------------------------------
def rows(*spec):
    def f(x, y, w, h):
        for limit, col in spec:
            if y <= limit:
                return col(x, y, w, h) if callable(col) else col
        last = spec[-1][1]
        return last(x, y, w, h) if callable(last) else last
    return f


def shade_sides(main, shade):
    return lambda x, y, w, h: shade if x in (0, w - 1) else main


def p_head(o):
    def face(x, y, w, h):
        if y <= 1:
            return HAIR
        if y == 7 and x == 5:
            return SKIN_S          # nariz
        if y == 8 and x in (1, 2, 7, 8):
            return BLUSH
        return SKIN
    return {"front": face, "top": HAIR, "right": HAIR, "left": HAIR, "back": HAIR, "bottom": SKIN_S}


def p_hair_shell(o):
    bangs = {0: range(10), 1: range(10), 2: [0, 1, 2, 4, 5, 7, 8, 9], 3: [0, 4, 9]}

    def front(x, y, w, h):
        if y in bangs and x in bangs[y]:
            if y == 1 and x in (2, 3, 6):
                return HAIR_L
            return HAIR_D if y >= 2 else HAIR
        if x in (0, 9) and y <= 9:
            return HAIR_D if y >= 7 else HAIR
        return CLEAR

    def right(x, y, w, h):     # x = w-1 es el frente
        if y >= 6 and x >= w - 4:
            return CLEAR if y >= 8 or x >= w - 2 else HAIR_D
        return HAIR_D if y >= 9 or x % 3 == 1 and y > 2 else HAIR

    def left(x, y, w, h):      # x = 0 es el frente
        if y >= 6 and x <= 3:
            return CLEAR if y >= 8 or x <= 1 else HAIR_D
        return HAIR_D if y >= 9 or x % 3 == 1 and y > 2 else HAIR

    def top(x, y, w, h):
        if x == 3 and 1 <= y <= 8:
            return HAIR_L
        return HAIR_D if x in (0, 9) else HAIR

    def back(x, y, w, h):
        return HAIR_D if y >= 9 or x % 3 == 2 else HAIR
    return {"front": front, "top": top, "right": right, "left": left, "back": back, "bottom": CLEAR}


def p_hair_back(o):
    def back(x, y, w, h):
        if y == h - 1 and x % 2 == 0:
            return CLEAR
        if y == h - 2 and x % 4 == 0:
            return CLEAR
        if x % 3 == 1:
            return HAIR_D
        return HAIR_DD if y >= h - 3 else HAIR
    return {"front": HAIR_D, "back": back, "right": back, "left": back, "top": HAIR, "bottom": HAIR_D}


def p_ponytail(o):
    def side(x, y, w, h):
        if y <= 1:
            return STAR if y == 0 else STAR_D
        if y == h - 1 and x != 1:
            return CLEAR
        if x == 1 and 3 <= y <= h - 3:
            return HAIR_L
        return HAIR_D if y >= h - 4 else HAIR
    return {"right": side, "front": side, "left": side, "back": side, "top": STAR, "bottom": HAIR_D}


def p_clip(o):
    if o.get("flower"):
        # Novia: una flor blanca en vez de la estrellita
        return {"*": c(255, 255, 255), "front": lambda x, y, w, h: c(255, 214, 120) if (x, y) == (1, 1) else c(255, 255, 255)}
    return {"*": STAR, "front": lambda x, y, w, h: STAR_L if (x + y) % 2 == 0 else STAR}


def p_eye(o):
    def front(x, y, w, h):
        if y == 0:
            return LASH
        if y == 1 and x == 1:
            return WHITE
        if y == 1:
            return IRIS_D
        if y == 3:
            return IRIS_L
        return IRIS
    return {"front": front}


def p_pupil(o):
    return {"front": PUPIL}


def p_eye_closed(o):
    return {"front": LASH}


def p_eye_happy(o):
    return {"front": lambda x, y, w, h: LASH if (y == 0 and x == 1) or (y == 1 and x != 1) else CLEAR}


def p_brow(o):
    return {"front": HAIR_DD}


def p_mouth(mname):
    rows_ = MOUTHS[mname]
    cmap = {"M": MOUTH, "F": FANG, "I": MOUTH_IN, "T": TONGUE, ".": CLEAR}
    return {"front": lambda x, y, w, h: cmap[rows_[y][x]]}


def p_face_extra(base):
    if base == "blush":
        # Rayitas de sonrojo ///
        return {"front": lambda x, y, w, h: BLUSH_D if (x + y) % 2 == 0 else BLUSH}
    if base == "sweat":
        return {"front": lambda x, y, w, h: SWEAT_L if y == 0 else SWEAT}
    if base == "tear":
        return {"front": lambda x, y, w, h: SWEAT_L if y == 0 else SWEAT}
    if base == "anger":
        # La marquita de enojo de anime (cuatro esquinas)
        return {"front": lambda x, y, w, h: ANGER if (x != 1 and y != 1) else CLEAR}
    return {"*": CLEAR}


def p_neck(o):
    return {"*": SKIN_S, "front": SKIN, "top": CLEAR}


def p_chest(name, o):
    t, s, d, a, ad = o["top"], o["top_s"], o["top_d"], o["acc"], o["acc_d"]
    if name == "school":
        def front(x, y, w, h):
            if y == 0:
                return s if x in (3, 5) else (SKIN if x == 4 else t)
            if y == 1 and 3 <= x <= 5:
                return ad if x == 4 else a      # moño
            if y == 2 and x in (3, 5):
                return a
            if x == 4 and y in (4, 6, 8):
                return d                        # botones
            return s if x in (0, w - 1) else t
        return {"front": front, "back": shade_sides(t, s), "right": shade_sides(t, s), "left": shade_sides(t, s),
                "top": t, "bottom": s}
    if name == "casual1":
        def front(x, y, w, h):
            if y == 0:
                return SKIN if 3 <= x <= 5 else s
            if x in (3, 5) and 1 <= y <= 4:
                return a                        # cordones
            return s if x in (0, w - 1) else t
        return {"front": front, "back": shade_sides(t, s), "right": shade_sides(t, s), "left": shade_sides(t, s),
                "top": t, "bottom": s}

    def front(x, y, w, h):
        if y == 0:
            return a if x in (2, 3, 5, 6) else (SKIN if x == 4 else t)
        if x in (2, 6) and y >= 1:
            return s                            # tejido trenzado
        return s if x in (0, w - 1) else t
    return {"front": front, "back": shade_sides(t, s), "right": shade_sides(t, s), "left": shade_sides(t, s),
            "top": t, "bottom": s}


def lighten(col, f=0.35):
    return tuple(min(255, int(v + (255 - v) * f)) for v in col[:3]) + (255,)


def p_bust(name, o):
    t, s, d = o["top"], o["top_s"], o["top_d"]
    hi = lighten(t, 0.35)

    def front(x, y, w, h):
        mid = w // 2
        if y == h - 1:
            return d if x in (mid - 1, mid) else s          # sombra de abajo
        if x in (mid - 1, mid) and y >= 1:
            return s                                         # pliegue de la tela al centro
        if y == 0 and x not in (0, w - 1):
            return hi                                        # brillo arriba
        if x in (0, w - 1):
            return s
        if name == "casual1" and x in (mid - 2, mid + 1) and y <= 1:
            return o["acc"]                                  # cordones de la sudadera
        if name == "casual2" and x in (1, w - 2):
            return s                                         # tejido
        return t
    side = lambda x, y, w, h: d if y == h - 1 else s
    return {"front": front, "top": hi, "bottom": d, "right": side, "left": side, "back": t}


def p_bust_top(name, o):
    t = o["top"]
    return {"*": t, "top": lighten(t, 0.2), "front": lighten(t, 0.25)}


def p_waist(name, o):
    t, s = o["top"], o["top_s"]
    if name == "casual1":
        def front(x, y, w, h):
            if y >= 2 and x in (1, w - 2):
                return s
            if y == 2 and 1 <= x <= w - 2:
                return s                        # bolsa canguro
            return t
        return {"front": front, "back": t, "right": t, "left": t, "top": t, "bottom": t}
    hem = rows((3, t), (4, s))
    return {"*": hem, "front": hem, "top": t, "bottom": s}


def p_hips(name, o):
    if name == "casual1":
        f = rows((1, o["top"]), (2, o["top_d"]), (5, o["leg"]))
        return {"*": f, "top": o["top"], "bottom": o["leg_s"]}
    return {"*": o["skirt"], "bottom": o["skirt_d"]}


def p_skirt(name, o):
    if name == "school":
        def pleat(x, y, w, h):
            if y == h - 1:
                return o["skirt_l"]
            return o["skirt_d"] if x % 2 else o["skirt"]
        return {"front": pleat, "back": pleat, "right": pleat, "left": pleat, "top": o["skirt"], "bottom": o["skirt_d"]}
    if name == "casual2":
        P, PD, PL = o["skirt"], o["skirt_d"], o["skirt_l"]

        def plaid(x, y, w, h):
            if x % 3 == 1 and y % 3 == 1:
                return PL
            if x % 3 == 1 or y % 3 == 1:
                return PD
            return P
        return {"front": plaid, "back": plaid, "right": plaid, "left": plaid, "top": P, "bottom": PD}
    return {"*": CLEAR}


def p_hood(name, o):
    if name != "casual1":
        return {"*": CLEAR}
    return {"*": o["top_s"], "back": lambda x, y, w, h: o["top_d"] if x in (0, w - 1) or y == h - 1 else o["top"]}


def p_arm(name, o):
    t, s = o["top"], o["top_s"]
    return {"*": t, "back": s, "top": t, "bottom": t, "right": shade_sides(t, s), "left": shade_sides(t, s)}


def p_forearm(name, o):
    t, s, d = o["top"], o["top_s"], o["top_d"]
    cuff = d if name != "school" else o["top_d"]
    f = rows((6, t), (7, cuff), (9, SKIN))
    return {"*": f, "back": rows((6, s), (7, cuff), (9, SKIN_S)), "top": t, "bottom": SKIN}


def p_thigh(name, o):
    if name == "casual1":
        def jeans(x, y, w, h):
            return o["leg_s"] if x == w - 1 or (y % 4 == 0 and x == 1) else o["leg"]
        return {"*": jeans, "top": o["leg"], "bottom": o["leg"]}
    return {"*": shade_sides(o["leg"], o["leg_s"]), "top": o["leg"], "bottom": o["leg"]}


def p_shin(name, o):
    if name == "casual1":
        f = rows((10, lambda x, y, w, h: o["leg_s"] if x == w - 1 else o["leg"]), (12, o["leg_l"]))
        return {"*": f, "top": o["leg"], "bottom": o["leg_l"]}
    if name == "casual2":
        f = rows((5, shade_sides(o["leg"], o["leg_s"])), (6, o["shoe_d"]), (12, o["shoe"]))
        return {"*": f, "top": o["leg"], "bottom": o["shoe"]}
    return {"*": shade_sides(o["leg"], o["leg_s"]), "top": o["leg"], "bottom": o["leg"]}


def p_foot(name, o):
    if name == "casual1":
        return {"*": rows((0, o["shoe"]), (1, o["shoe_d"])), "top": o["shoe"], "bottom": o["shoe_d"]}
    return {"*": rows((0, o["shoe"]), (1, o["shoe_d"])), "top": o["shoe"], "bottom": o["shoe_d"]}


# ------------------------------------------------------------------
# Ropa nueva (trabajo y temporada)
# ------------------------------------------------------------------
def plaid_of(o):
    t, s, d = o["top"], o["top_s"], o["acc"]

    def f(x, y, w, h):
        if x % 4 == 1 and y % 4 == 1:
            return d
        if x % 4 == 1 or y % 4 == 1:
            return s
        return t
    return f


def new_body_painters(base, name, o):
    st = o["style"]
    t, s, d = o["top"], o["top_s"], o["top_d"]
    a, ad = o["acc"], o["acc_d"]
    leg, leg_s = o.get("leg", SKIN), o.get("leg_s", SKIN_S)
    plaid = plaid_of(o) if st == "plaid" else None

    def main_at(x, y, w, h):
        return plaid(x, y, w, h) if plaid else t

    if base == "chest":
        def front(x, y, w, h):
            mid = w // 2
            if st == "dress":
                if y <= 1:
                    return t if x in (2, w - 3) else SKIN
                return lighten(t, 0.3) if y == 2 else t
            if st == "coat":
                if y == 0:
                    return d
                if x == mid:
                    return a if y in (2, 4, 6) else d
                return s if x in (0, w - 1) else t
            if st == "over":
                if y == 0:
                    return s if x not in (mid,) else SKIN
                if x in (2, w - 3) and y <= 2:
                    return a                     # tirantes
                if 2 <= x <= w - 3 and y >= 3:
                    return o["acc_d"] if (y == 3 or x in (2, w - 3)) else a   # peto
                return t
            if st == "vest":
                if y == 0:
                    return SKIN if x == mid else s
                if x <= 2 or x >= w - 3:
                    return ad if x in (2, w - 3) else a
                return t
            # plaid
            if y == 0:
                return SKIN if x == mid else d
            return main_at(x, y, w, h)

        def other(x, y, w, h):
            if st == "dress":
                return t if y >= 2 else (t if x in (2, w - 3) else SKIN)
            if st == "over":
                return a if 2 <= x <= w - 3 and y >= 3 else (a if x in (2, w - 3) else t)
            if st == "vest":
                return a
            return main_at(x, y, w, h)
        side = (lambda x, y, w, h: t if y >= 2 else SKIN) if st == "dress" else (a if st == "vest" else main_at)
        return {"front": front, "back": other, "right": side, "left": side,
                "top": SKIN if st == "dress" else (a if st == "vest" else main_at), "bottom": s}

    if base == "bust":
        def front(x, y, w, h):
            mid = w // 2
            if y == h - 1:
                return d if st != "over" else ad
            if st == "over":
                return a if 1 <= x <= w - 2 else t
            if st == "vest":
                return a if x <= 1 or x >= w - 2 else t
            if st == "coat":
                return d if x == mid else (lighten(t, 0.15) if y == 0 else t)
            if st == "dress":
                return lighten(t, 0.35) if y == 0 else (s if x in (mid - 1, mid) else t)
            return main_at(x, y, w, h)
        col = a if st in ("over",) else (t if st != "plaid" else main_at)
        return {"front": front, "top": col, "bottom": d, "right": col, "left": col, "back": col}

    if base == "bust_top":
        col = a if st == "over" else t
        return {"*": col, "front": lighten(col if not callable(col) else t, 0.2)}

    if base == "waist":
        if st == "over":
            return {"*": a, "front": lambda x, y, w, h: ad if x in (1, w - 2) and y == 0 else a, "top": a, "bottom": a}
        if st == "dress":
            return {"*": rows((0, a), (4, t)), "top": t, "bottom": t}
        if st == "coat":
            return {"*": rows((1, t), (2, o["acc_d"]), (4, t)),
                    "front": lambda x, y, w, h: (o["acc"] if x == w // 2 else o["acc_d"]) if y == 2 else (d if x == w // 2 else t)}
        if st == "vest":
            return {"*": rows((3, t), (4, leg_s)), "front": lambda x, y, w, h: a if (x <= 1 or x >= w - 2) and y <= 3 else (leg_s if y == 4 else t)}
        return {"*": rows((3, main_at), (4, ad)), "top": t, "bottom": ad}

    if base == "hips":
        if st == "over":
            return {"*": a, "bottom": leg_s}
        if st == "dress":
            return {"*": t}
        if st == "coat":
            return {"*": rows((4, t), (5, d)), "front": lambda x, y, w, h: d if x == w // 2 else (t if y < 5 else d)}
        return {"*": shade_sides(leg, leg_s), "bottom": leg_s}

    if base in ("skirt_top", "skirt_flare"):
        if st != "dress":
            return {"*": CLEAR}
        f = lambda x, y, w, h: s if x % 3 == 0 else t
        return {"front": f, "back": f, "right": f, "left": f, "top": t, "bottom": s}

    if base == "dress":
        def f(x, y, w, h):
            if y == h - 1:
                return lighten(t, 0.4) if x % 2 == 0 else s   # encaje
            return s if x % 3 == 0 else t
        return {"front": f, "back": f, "right": f, "left": f, "top": t, "bottom": s}

    if base == "arm":
        if st == "dress":
            return {"*": SKIN, "back": SKIN_S, "top": rows((0, t))}
        if st == "coat":
            return {"*": t, "back": s, "top": t, "bottom": t}
        return {"*": main_at, "back": s if not plaid else main_at, "top": t, "bottom": t}

    if base == "forearm":
        if st == "dress":
            return {"*": SKIN, "back": SKIN_S, "bottom": SKIN}
        if st == "coat":
            return {"*": rows((6, t), (7, o["acc_d"]), (9, SKIN)), "bottom": SKIN}
        if st == "over":
            # Mangas arremangadas
            return {"*": rows((2, s), (9, SKIN)), "back": rows((2, d), (9, SKIN_S)), "top": t, "bottom": SKIN}
        return {"*": rows((6, main_at), (7, d), (9, SKIN)), "bottom": SKIN}

    if base == "thigh":
        if st == "dress":
            return {"*": SKIN, "back": SKIN_S, "right": SKIN_S}
        if st == "over":
            return {"*": shade_sides(a, ad), "top": a, "bottom": a}
        return {"*": shade_sides(leg, leg_s), "top": leg, "bottom": leg}

    if base == "shin":
        if st == "dress":
            return {"*": rows((11, SKIN), (12, o["shoe"])), "back": rows((11, SKIN_S), (12, o["shoe"]))}
        boot_from = 6 if st in ("coat", "vest") else 10
        pants = a if st == "over" else leg
        pants_s = ad if st == "over" else leg_s
        f = rows((boot_from - 1, shade_sides(pants, pants_s)), (boot_from, o["shoe_d"]), (12, o["shoe"]))
        return {"*": f, "top": pants, "bottom": o["shoe"]}

    if base == "foot":
        if st == "dress":
            # Sandalias
            return {"*": rows((0, SKIN), (1, o["shoe"])), "top": lambda x, y, w, h: o["shoe"] if y == 1 else SKIN,
                    "bottom": o["shoe_d"]}
        return {"*": rows((0, o["shoe"]), (1, o["shoe_d"])), "top": o["shoe"], "bottom": o["shoe_d"]}

    return None


def accessory_painters(base, name, o):
    """Sombreros, casco, gorro y bufanda. None si este outfit no lo usa."""
    if base in ("hat_brim", "hat_crown") and "hat" in o:
        h, hs, band = o["hat"], o["hat_s"], o["hat_band"]
        weave = lambda x, y, w, hh: hs if (x + y) % 3 == 0 else h
        if base == "hat_brim":
            return {"top": weave, "bottom": hs, "*": hs}
        return {"*": rows((1, weave), (2, band)), "top": weave, "bottom": CLEAR}
    if base in ("hardhat_dome", "hardhat_brim", "hardhat_lamp") and "hard" in o:
        if base == "hardhat_lamp":
            return {"*": o["lamp_s"], "front": lambda x, y, w, h: o["lamp"]}
        return {"*": o["hard"], "bottom": o["hard_s"], "front": rows((1, o["hard"]), (2, o["hard_s"]))}
    if base in ("beanie", "pompom") and "beanie" in o:
        if base == "pompom":
            return {"*": o["pom"]}
        return {"*": rows((2, lambda x, y, w, h: o["beanie_s"] if x % 2 else o["beanie"]), (3, o["beanie_s"])),
                "top": o["beanie"], "bottom": CLEAR}
    if base in ("scarf", "scarf_tail") and "scarf" in o:
        stripe = lambda x, y, w, h: o["scarf_s"] if (x + y) % 4 == 0 else o["scarf"]
        if base == "scarf_tail":
            return {"*": lambda x, y, w, h: o["scarf_s"] if y % 3 == 2 else o["scarf"]}
        return {"*": stripe}
    return {"*": CLEAR}


# ------------------------------------------------------------------
# Busto y glúteos por capas, con sombreado redondo para que no se vean como caja
# ------------------------------------------------------------------
BODY_DETAIL = {"bust", "bust_lobe", "bust_low", "bust_slope", "bust_upper",
               "glutes", "glute_lobe", "glutes_low", "glutes_top"}


def tone(col, k):
    """Aclara (k > 1) u oscurece (k < 1) un color."""
    if col is None or len(col) < 4 or col[3] == 0:
        return col
    if k >= 1:
        return tuple(min(255, int(v + (255 - v) * (k - 1))) for v in col[:3]) + (col[3],)
    return tuple(int(v * k) for v in col[:3]) + (col[3],)


def at(pnt, x, y, w, h):
    return pnt(x, y, w, h) if callable(pnt) else pnt


def roundish(pnt, top_light=0.35, strength=1.0):
    """Luz al centro (un poco arriba) y sombra hacia las orillas: da volumen a una cara plana."""
    def f(x, y, w, h):
        col = at(pnt, x, y, w, h)
        nx = (x + 0.5) / w * 2 - 1
        ny = (y + 0.5) / h * 2 - 1
        d = min(1.0, nx * nx * 0.75 + (ny + top_light) ** 2 * 0.8)
        return tone(col, 1.0 + (0.1 - 0.32 * d) * strength)
    return f


def fabric_of(painters):
    """Color liso de la tela de una pieza (sin estampados de la parte de enfrente)."""
    return painters.get("back", painters.get("*"))


def body_detail_painters(base, name, o):
    if base.startswith("bust"):
        if base == "bust":
            b = new_body_painters("bust", name, o) if name in NEW_OUTFITS else p_bust(name, o)
            out = dict(b)
            out["front"] = roundish(b["front"], strength=0.6)
            return out
        src = new_body_painters("bust", name, o) if name in NEW_OUTFITS else p_bust(name, o)
        fab = fabric_of(src)
        if base == "bust_lobe":
            return {"front": roundish(fab, strength=0.75), "top": lambda x, y, w, h: tone(at(fab, x, y, w, h), 1.08),
                    "bottom": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.8),
                    "*": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.9)}
        if base == "bust_low":
            return {"*": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.8),
                    "bottom": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.78)}
        # bust_slope / bust_upper: transición desde el pecho, con un poco de brillo arriba
        return {"*": fab, "top": lambda x, y, w, h: tone(at(fab, x, y, w, h), 1.12),
                "front": lambda x, y, w, h: tone(at(fab, x, y, w, h), 1.05)}

    # Glúteos: misma tela que la cadera (pantalón, falda o vestido)
    if name == "casual1":
        jeans = shade_sides(o["leg"], o["leg_s"])
        fab = o["leg"]
        if base == "glutes_top":
            return {"*": jeans, "top": o["leg"], "bottom": o["leg_s"]}
        if base == "glutes":
            # Costura al centro del pantalón
            def core(x, y, w, h):
                if y == 0:
                    return o["top_d"]
                return at(jeans, x, y, w, h)
            return {"*": core, "back": roundish(core, strength=0.6), "top": o["top_d"], "bottom": o["leg_s"]}
        if base == "glute_lobe":
            # Bolsas traseras del pantalón
            def pocket(x, y, w, h):
                # Solo la orilla de arriba de la bolsa, para que no se vea ruidoso
                return o["leg_s"] if (y == 0 and 0 < x < w - 1) else o["leg"]
            return {"back": roundish(pocket), "top": o["leg"],
                    "bottom": tone(o["leg"], 0.8), "*": tone(o["leg"], 0.9)}
    else:
        h = new_body_painters("hips", name, o) if name in NEW_OUTFITS else p_hips(name, o)
        fab = h.get("back", h.get("*"))
        if base in ("glutes", "glutes_top"):
            # Costura al centro
            seam = lambda x, y, w, hh: tone(at(fab, x, y, w, hh), 0.85) if x == w // 2 and base == "glutes" else at(fab, x, y, w, hh)
            return {"*": fab, "back": roundish(seam, strength=0.6),
                    "bottom": lambda x, y, w, hh: tone(at(fab, x, y, w, hh), 0.75)}
    if base == "glute_lobe":
        return {"back": roundish(fab), "top": lambda x, y, w, h: tone(at(fab, x, y, w, h), 1.06),
                "bottom": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.8),
                "*": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.9)}
    # glutes_low (y glutes_top de casual1 ya salió arriba)
    return {"*": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.82),
            "bottom": lambda x, y, w, h: tone(at(fab, x, y, w, h), 0.78)}


def veil_painters(base, o):
    """Velo y corona de flores (solo el vestido de novia)."""
    if not o.get("flower"):
        return {"*": CLEAR}
    veil, lace = c(250, 250, 252), c(222, 216, 226)
    if base == "veil":
        def f(shade):
            def g_(x, y, w, h):
                if y >= h - 2:
                    return lace if (x + y) % 2 == 0 else CLEAR
                if y == 0:
                    return o["acc"] if x % 3 == 1 else veil
                return tone(veil, shade if x % 3 == 0 else 1.0)
            return g_
        return {"back": f(0.94), "front": f(0.9)}
    i = int(base.split("_")[1])
    col = (o["acc"], c(255, 236, 150), c(150, 200, 130))[i % 3]
    return {"*": col, "top": lambda x, y, w, h: c(255, 255, 255) if (x + y) % 2 == 0 else col}


def painters_for(cid, name, o):
    base = re.sub(r"^(right|left)_", "", cid)
    if base == "brow":
        return p_brow(o)
    if base.startswith("mouth_"):
        return p_mouth(base[len("mouth_"):])
    if base in ("blush", "sweat", "tear", "anger"):
        return p_face_extra(base)
    if base in BODY_DETAIL:
        return body_detail_painters(base, name, o)
    if base in ("hat_brim", "hat_crown", "hardhat_dome", "hardhat_brim", "hardhat_lamp", "beanie", "pompom", "scarf", "scarf_tail"):
        return accessory_painters(base, name, o)
    if base == "veil" or base.startswith("crown_"):
        return veil_painters(base, o)
    if base == "dress" and name not in NEW_OUTFITS:
        return {"*": CLEAR}
    if name in NEW_OUTFITS:
        pnt = new_body_painters(base, name, o)
        if pnt is not None:
            return pnt
    table = {
        "head": lambda: p_head(o), "hair_shell": lambda: p_hair_shell(o), "hair_back": lambda: p_hair_back(o),
        "ponytail": lambda: p_ponytail(o), "clip": lambda: p_clip(o),
        "eye": lambda: p_eye(o), "pupil": lambda: p_pupil(o),
        "eye_closed": lambda: p_eye_closed(o), "eye_happy": lambda: p_eye_happy(o),
        "chest": lambda: p_chest(name, o), "bust": lambda: p_bust(name, o), "bust_top": lambda: p_bust_top(name, o), "waist": lambda: p_waist(name, o),
        "hips": lambda: p_hips(name, o), "neck": lambda: p_neck(o),
        "skirt_top": lambda: p_skirt(name, o), "skirt_flare": lambda: p_skirt(name, o), "hood": lambda: p_hood(name, o),
        "arm": lambda: p_arm(name, o), "forearm": lambda: p_forearm(name, o),
        "thigh": lambda: p_thigh(name, o), "shin": lambda: p_shin(name, o), "foot": lambda: p_foot(name, o),
    }
    return table[base]()


def build_texture(name):
    o = PAL[name]
    img = Image.new("RGBA", (TEX, TEX), (0, 0, 0, 0))
    px = img.load()
    for p in PARTS:
        for cb in p["cubes"]:
            painters = painters_for(cb["id"], name, o)
            for face, (fx, fy, fw, fh) in face_rects(cb).items():
                pnt = painters.get(face, painters.get("*"))
                if pnt is None:
                    continue
                for yy in range(fh):
                    for xx in range(fw):
                        col = pnt(xx, yy, fw, fh) if callable(pnt) else pnt
                        if col is not None:
                            px[fx + xx, fy + yy] = col
    return img


# ------------------------------------------------------------------
# Java
# ------------------------------------------------------------------
def model_coords(p):
    x, y, z = p
    return (x, -y, z)


def fmt(v):
    v = round(v, 4)
    return (f"{v:.4f}".rstrip("0").rstrip(".") or "0") + "f" if v != int(v) else f"{int(v)}f"


def gen_java_body(parts=None, part_map=None):
    """Código Java (addChild...) de una lista de piezas. Sirve para Kiko y para las otras chicas."""
    global PARTS, PART
    saved = (PARTS, PART)
    if parts is not None:
        PARTS, PART = parts, part_map
    try:
        return _gen_java_lines()
    finally:
        PARTS, PART = saved


def gen_java():
    body = _gen_java_lines()
    src = open(MODEL_JAVA).read()
    src = re.sub(r"// <gen>\n.*?// </gen>", lambda m: "// <gen>\n" + body + "\n        // </gen>", src, flags=re.S)
    src = re.sub(r"TexturedModelData\.of\(data, \d+, \d+\)", f"TexturedModelData.of(data, {TEX}, {TEX})", src)
    open(MODEL_JAVA, "w").write(src)


def _gen_java_lines():
    lines = []
    var = {"root": "root"}
    for p in PARTS:
        if p["parent"] is None:
            lines.append('ModelPartData root = data.getRoot().addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0f, 24f, 0f));')
            continue
        pv = model_coords(p["pivot"])
        parent = PART[p["parent"]]
        ppv = model_coords(parent["pivot"])
        rel = (pv[0] - ppv[0], pv[1] - ppv[1], pv[2] - ppv[2])
        b = "ModelPartBuilder.create()"
        for cb in p["cubes"]:
            w, h, d = cb["size"]
            fx, fy, fz = cb["frm"]
            ox, oy, oz = fx - pv[0], -(fy + h) - pv[1], fz - pv[2]
            dil = f", new Dilation({fmt(cb['dil'])})" if cb["dil"] else ""
            b += f"\n                .uv({cb['uv'][0]}, {cb['uv'][1]}).cuboid({fmt(ox)}, {fmt(oy)}, {fmt(oz)}, {w}, {h}, {d}{dil})"
        rx, ry, rz = p["rot"]
        if any(p["rot"]):
            tf = f"ModelTransform.of({fmt(rel[0])}, {fmt(rel[1])}, {fmt(rel[2])}, {fmt(rx)}, {fmt(ry)}, {fmt(rz)})"
        else:
            tf = f"ModelTransform.pivot({fmt(rel[0])}, {fmt(rel[1])}, {fmt(rel[2])})"
        v = re.sub(r"_(\w)", lambda m: m.group(1).upper(), p["name"]) + "D"
        var[p["name"]] = v
        has_children = any(q["parent"] == p["name"] for q in PARTS)
        prefix = f"ModelPartData {v} = " if has_children else ""
        lines.append(f'{prefix}{var[p["parent"]]}.addChild("{p["name"]}", {b},\n                {tf});')
    return "\n".join("        " + ln.replace("\n", "\n        ") for ln in lines)


# ------------------------------------------------------------------
# Vista previa (render por software)
# ------------------------------------------------------------------
def rot_matrix(pitch, yaw, roll):
    cx, sx = math.cos(pitch), math.sin(pitch)
    cy, sy = math.cos(yaw), math.sin(yaw)
    cz, sz = math.cos(roll), math.sin(roll)
    rx = [[1, 0, 0], [0, cx, -sx], [0, sx, cx]]
    ry = [[cy, 0, sy], [0, 1, 0], [-sy, 0, cy]]
    rz = [[cz, -sz, 0], [sz, cz, 0], [0, 0, 1]]

    def mul(a, b):
        return [[sum(a[i][k] * b[k][j] for k in range(3)) for j in range(3)] for i in range(3)]
    return mul(mul(rz, ry), rx)


def apply(m, p):
    return tuple(sum(m[i][k] * p[k] for k in range(3)) for i in range(3))


def transform_fn(name, pose):
    p = PART[name]
    ov = pose.get(name, {})
    pv = model_coords(p["pivot"])
    if p["parent"]:
        ppv = model_coords(PART[p["parent"]]["pivot"])
        rel = [pv[i] - ppv[i] for i in range(3)]
    else:
        rel = [0, 0, 0]
    rel[0] += ov.get("dx", 0)
    rel[1] += ov.get("dy", 0)
    rot = [p["rot"][i] + ov.get("rot", (0, 0, 0))[i] for i in range(3)]
    m = rot_matrix(*rot)
    ys = ov.get("ys", 1.0)
    xs, zs = ov.get("xs", 1.0), ov.get("zs", 1.0)
    up = transform_fn(p["parent"], pose) if p["parent"] else (lambda q: q)

    def f(q):
        q = (q[0] * xs, q[1] * ys, q[2] * zs)
        q = apply(m, q)
        return up((q[0] + rel[0], q[1] + rel[1], q[2] + rel[2]))
    return f


def cube_faces(cb, pv):
    w, h, d = cb["size"]
    fx, fy, fz = cb["frm"]
    dil = cb["dil"]
    x0, y0, z0 = fx - pv[0] - dil, -(fy + h) - pv[1] - dil, fz - pv[2] - dil
    x1, y1, z1 = x0 + w + 2 * dil, y0 + h + 2 * dil, z0 + d + 2 * dil
    r = face_rects(cb)
    return [
        (r["front"], (x0, y0, z0), (x1 - x0, 0, 0), (0, y1 - y0, 0)),
        (r["back"], (x1, y0, z1), (x0 - x1, 0, 0), (0, y1 - y0, 0)),
        (r["right"], (x0, y0, z1), (0, 0, z0 - z1), (0, y1 - y0, 0)),
        (r["left"], (x1, y0, z0), (0, 0, z1 - z0), (0, y1 - y0, 0)),
        (r["top"], (x0, y0, z1), (x1 - x0, 0, 0), (0, 0, z0 - z1)),
        (r["bottom"], (x0, y1, z0), (x1 - x0, 0, 0), (0, 0, z1 - z0)),
    ]


def hidden_for(outfit):
    hidden = {"right_eye_closed", "left_eye_closed", "right_eye_happy", "left_eye_happy",
              "blush", "sweat", "tears", "anger"} | {f"mouth_{m}" for m in MOUTHS if m != "neutral"}
    for p in OPTIONAL_PARTS:
        if p not in FLAGS[outfit]:
            hidden.add(p)
    return hidden


def render(tex, outfit, view_yaw=0.5, view_pitch=-0.1, scale=7.0, size=(460, 520), center=None, pose=None, hidden=None):
    pose = pose or {}
    hidden = set(hidden_for(outfit) if hidden is None else hidden)
    for k, v in pose.items():
        if v.get("visible") is True:
            hidden.discard(k)
        elif v.get("visible") is False:
            hidden.add(k)

    def is_hidden(n):
        while n:
            if n in hidden:
                return True
            n = PART[n]["parent"]
        return False

    rx = rot_matrix(view_pitch, 0, 0)
    ry = rot_matrix(0, view_yaw, 0)
    view = [[sum(rx[i][k] * ry[k][j] for k in range(3)) for j in range(3)] for i in range(3)]
    px = tex.load()
    quads = []
    for p in PARTS:
        if is_hidden(p["name"]):
            continue
        tf = transform_fn(p["name"], pose)
        pv = model_coords(p["pivot"])
        for cb in p["cubes"]:
            for (fx, fy, fw, fh), o, eu, ev in cube_faces(cb, pv):
                if fw == 0 or fh == 0:
                    continue
                for ty in range(fh):
                    for tx in range(fw):
                        col = px[fx + tx, fy + ty]
                        if col[3] < 128:
                            continue
                        pts = []
                        for (a, b) in ((tx, ty), (tx + 1, ty), (tx + 1, ty + 1), (tx, ty + 1)):
                            q = tuple(o[i] + eu[i] * a / fw + ev[i] * b / fh for i in range(3))
                            pts.append(apply(view, tf(q)))
                        ax, ay, az = (pts[1][i] - pts[0][i] for i in range(3))
                        bx, by, bz = (pts[3][i] - pts[0][i] for i in range(3))
                        nz = ax * by - ay * bx
                        if nz <= 0:
                            continue
                        nlen = math.sqrt((ay * bz - az * by) ** 2 + (az * bx - ax * bz) ** 2 + nz ** 2) or 1
                        light = 0.74 + 0.26 * (nz / nlen)
                        depth = sum(q[2] for q in pts) / 4
                        quads.append((depth, pts, tuple(int(ch * light) for ch in col[:3]) + (255,)))
    quads.sort(key=lambda q: -q[0])
    img = Image.new("RGBA", size, (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    if center is None:
        cx, cy = size[0] / 2, size[1] - 20
    else:
        cx, cy = center
    for _, pts, col in quads:
        d.polygon([(cx + q[0] * scale, cy + q[1] * scale) for q in pts], fill=col, outline=col)
    return img


def previews(all_textures, out_dir):
    textures = [(n, t) for n, t in all_textures if n not in NEW_OUTFITS]
    bg = c(40, 42, 54)
    # 1) Cuerpo completo, 3 outfits x (frente 3/4, perfil, espalda)
    views = [0.0, 0.5, -math.pi / 2 + 0.05, math.pi + 0.5]
    W, H = 300, 500
    sheet = Image.new("RGBA", (W * len(views), H * 3), bg)
    d = ImageDraw.Draw(sheet)
    for i, (name, img) in enumerate(textures):
        for j, vy in enumerate(views):
            r = render(img, name, view_yaw=vy, scale=7.0, size=(W, H), center=(W / 2, H - 20))
            sheet.alpha_composite(r, (j * W, i * H))
        d.text((10, i * H + 8), name, fill=c(255, 255, 255))
    sheet.save(os.path.join(out_dir, "kiko_v2_cuerpo.png"))

    # 1b) Ropa nueva, de frente y de 3/4
    news = [(n, t) for n, t in all_textures if n in NEW_OUTFITS]
    sheet = Image.new("RGBA", (W * len(news), H * 2), bg)
    d = ImageDraw.Draw(sheet)
    for i, (name, img) in enumerate(news):
        for j, vy in enumerate([0.0, 0.6]):
            r = render(img, name, view_yaw=vy, scale=7.0, size=(W, H), center=(W / 2, H - 20))
            sheet.alpha_composite(r, (i * W, j * H))
        d.text((i * W + 10, 8), name, fill=c(255, 255, 255))
    sheet.save(os.path.join(out_dir, "kiko_ropa_nueva.png"))

    # 2) Close-up de ojos: abiertos, mirando a un lado, parpadeo a la mitad, cerrados, felices, enojada
    img = textures[0][1]
    states = [
        ("normal", {}),
        ("mira a un lado", {"right_pupil": {"dx": 0.8}, "left_pupil": {"dx": 0.8}}),
        ("parpadeo", {"right_eye": {"ys": 0.45}, "left_eye": {"ys": 0.45}}),
        ("cerrados", {"right_eye": {"visible": False}, "left_eye": {"visible": False},
                      "right_eye_closed": {"visible": True}, "left_eye_closed": {"visible": True}}),
        ("felices", {"right_eye": {"visible": False}, "left_eye": {"visible": False},
                     "right_eye_happy": {"visible": True}, "left_eye_happy": {"visible": True}}),
        ("enojada", {"right_eye": {"ys": 0.55}, "left_eye": {"ys": 0.55}}),
    ]
    CW, CH = 260, 260
    face = Image.new("RGBA", (CW * 3, CH * 2), bg)
    d = ImageDraw.Draw(face)
    for i, (label, pose) in enumerate(states):
        hidden = hidden_for("school") | {n for n in PART if n not in ("head", "hair_back", "ponytail", "clip") and not n.endswith(("eye", "pupil", "closed", "happy"))}
        hidden.discard("root")
        r = render(img, "school", view_yaw=0.0, view_pitch=0.0, scale=18, size=(CW, CH),
                   center=(CW / 2, CH / 2 + 55.5 * 18), pose=pose, hidden=hidden)
        face.alpha_composite(r, ((i % 3) * CW, (i // 3) * CH))
        d.text(((i % 3) * CW + 10, (i // 3) * CH + 8), label, fill=c(255, 255, 255))
    face.save(os.path.join(out_dir, "kiko_v2_ojos.png"))


# ------------------------------------------------------------------
# Blockbench
# ------------------------------------------------------------------
def bb_model(texture_imgs):
    elements, groups = [], {}

    def to_bb(p):
        return [-p[0], p[1], p[2]]

    for p in PARTS:
        groups[p["name"]] = {
            "name": p["name"], "origin": to_bb(p["pivot"]),
            "rotation": [-math.degrees(p["rot"][0]), -math.degrees(p["rot"][1]), math.degrees(p["rot"][2])],
            "color": 0, "uuid": str(uuid.uuid4()), "export": True, "mirror_uv": False,
            "isOpen": True, "locked": False, "visibility": True, "autouv": 0, "children": [],
        }
        for cb in p["cubes"]:
            w, h, d = cb["size"]
            a = cb["frm"]
            b = (a[0] + w, a[1] + h, a[2] + d)
            fa, fb = to_bb(a), to_bb(b)
            el = {
                "name": cb["id"], "box_uv": True, "rescale": False, "locked": False, "render_order": "default",
                "allow_mirror_modeling": True,
                "from": [min(fa[0], fb[0]), min(fa[1], fb[1]), min(fa[2], fb[2])],
                "to": [max(fa[0], fb[0]), max(fa[1], fb[1]), max(fa[2], fb[2])],
                "autouv": 0, "color": 0, "inflate": cb["dil"], "origin": to_bb(p["pivot"]),
                "uv_offset": list(cb["uv"]), "type": "cube", "uuid": str(uuid.uuid4()),
            }
            r = face_rects(cb)
            el["faces"] = {k: {"uv": [fx, fy, fx + fw, fy + fh], "texture": 0} for k, (fx, fy, fw, fh) in {
                "north": r["front"], "south": r["back"], "east": r["right"], "west": r["left"],
                "up": r["top"], "down": r["bottom"]}.items()}
            elements.append(el)
            groups[p["name"]]["children"].append(el["uuid"])
    for p in PARTS:
        if p["parent"]:
            groups[p["parent"]]["children"].append(groups[p["name"]])
    textures = []
    for i, (tname, img) in enumerate(texture_imgs):
        buf = io.BytesIO()
        img.save(buf, "PNG")
        textures.append({
            "path": "", "name": tname, "folder": "", "namespace": "", "id": str(i),
            "width": TEX, "height": TEX, "uv_width": TEX, "uv_height": TEX, "particle": False,
            "render_mode": "default", "render_sides": "auto", "visible": True, "internal": True,
            "saved": True, "uuid": str(uuid.uuid4()),
            "source": "data:image/png;base64," + base64.b64encode(buf.getvalue()).decode(),
        })
    return {
        "meta": {"format_version": "4.10", "model_format": "modded_entity", "box_uv": True},
        "name": "kiko", "model_identifier": "kiko",
        "modded_entity_entity_class": "KikoEntity", "modded_entity_version": "Fabric 1.17+",
        "visible_box": [1, 2, 0], "resolution": {"width": TEX, "height": TEX},
        "elements": elements, "outliner": [groups["root"]], "textures": textures,
    }


# ------------------------------------------------------------------
# Íconos
# ------------------------------------------------------------------
SHIRT = [
    "................", "................", "....###..###....", "...####..####...",
    "..############..", ".##############.", ".###.######.###.", "..#..######..#..",
    ".....######.....", ".....######.....", ".....######.....", ".....######.....",
    ".....######.....", ".....######.....", "................", "................",
]


def shirt_icon(main, extra=None):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    mask = [[ch == "#" for ch in row] for row in SHIRT]
    dark = tuple(int(v * 0.6) for v in main[:3]) + (255,)
    for y in range(16):
        for x in range(16):
            if not mask[y][x]:
                continue
            edge = any(not (0 <= x + dx < 16 and 0 <= y + dy < 16 and mask[y + dy][x + dx])
                       for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)))
            px[x, y] = dark if edge else main
    if extra:
        extra(px)
    return img


def icon_school(px):
    for (x, y) in ((6, 4), (7, 5), (8, 5), (9, 4), (7, 4), (8, 4)):
        px[x, y] = c(214, 52, 70)
    for x in range(6, 10):
        px[x, 12] = px[x, 13] = c(46, 56, 104)


def icon_casual1(px):
    for y in range(5, 9):
        px[7, y] = px[9, y] = c(250, 250, 250)
    for x in range(6, 11):
        px[x, 11] = c(112, 186, 164)


def icon_casual2(px):
    px[7, 4] = px[8, 4] = c(250, 250, 250)
    for x in range(6, 10):
        px[x, 12] = c(196, 48, 62)
        px[x, 13] = c(140, 30, 45)


def gift_icon():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    pink, pink_d, yel, yel_d = c(255, 140, 170), c(214, 90, 130), c(255, 214, 70), c(220, 170, 40)
    d.rectangle([2, 7, 13, 14], fill=pink, outline=pink_d)
    d.rectangle([1, 5, 14, 7], fill=pink_d)
    d.rectangle([7, 5, 8, 14], fill=yel)
    d.rectangle([1, 6, 14, 6], fill=yel)
    d.point([(5, 2), (6, 3), (5, 3), (4, 3), (10, 2), (9, 3), (10, 3), (11, 3), (7, 4), (8, 4)], fill=yel)
    d.point([(6, 4), (9, 4)], fill=yel_d)
    return img


def main(preview_dir=None):
    os.makedirs(os.path.join(ASSETS, "textures", "entity"), exist_ok=True)
    os.makedirs(os.path.join(ASSETS, "textures", "item"), exist_ok=True)
    textures = []
    for name in OUTFITS:
        img = build_texture(name)
        img.save(os.path.join(ASSETS, "textures", "entity", f"kiko_{name}.png"))
        textures.append((name, img))

    icons = {
        "gift_box": gift_icon(),
        "outfit_school": shirt_icon(PAL["school"]["top"], icon_school),
        "outfit_casual1": shirt_icon(PAL["casual1"]["top"], icon_casual1),
        "outfit_casual2": shirt_icon(PAL["casual2"]["top"], icon_casual2),
    }
    for n, img in icons.items():
        img.save(os.path.join(ASSETS, "textures", "item", f"{n}.png"))

    # Ícono del mod, la carita de frente
    hidden = {n for n in PART if n not in ("root", "head", "hair_back", "ponytail", "clip", "right_eye", "left_eye", "right_pupil", "left_pupil")}
    face = render(textures[0][1], "school", view_yaw=0, view_pitch=0, scale=11, size=(128, 128),
                  center=(64, 64 + 55.5 * 11), hidden=hidden)
    icon = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
    ImageDraw.Draw(icon).rounded_rectangle([0, 0, 127, 127], radius=24, fill=c(255, 214, 200))
    icon.alpha_composite(face)
    icon.save(os.path.join(ASSETS, "icon.png"))

    gen_java()
    os.makedirs(os.path.join(ROOT, "blockbench"), exist_ok=True)
    with open(os.path.join(ROOT, "blockbench", "kiko.bbmodel"), "w") as f:
        json.dump(bb_model(textures), f)
    if preview_dir:
        previews(textures, preview_dir)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
