"""
Chicas nuevas (diseño). Genera vistas previas de las 3 compañeras nuevas junto a Kiko:
  - Maru, la herrera (alta, atlética, hombros anchos)
  - Lumi, la tímida lectora (más bajita y delgadita)
  - Nadi, la exploradora (delgada, piernas largas)
Por ahora solo hace las imágenes (layouts). Cuando se aprueben, cada una pasa al mod con su propio modelo.

Uso   python tools/gen_girls.py carpeta_preview
Mismas unidades que Kiko: 61.75 unidades = 1.8 bloques (la altura del jugador).
"""
import math
import os
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_assets as g  # noqa: E402

KIKO_FACE_SOURCE = list(g.PARTS)   # piezas de Kiko (de aquí se copian bocas, sonrojo, sudor, etc.)

c = g.c
CLEAR = None
UNITS_PER_BLOCK = 61.75 / 1.8


def tone(col, k):
    return g.tone(col, k)


# ------------------------------------------------------------------
# Geometría
# ------------------------------------------------------------------
class Girl:
    def __init__(self, name, height=1.8):
        self.name = name
        self.height = height          # bloques en el juego (el render la escala)
        self.parts = []
        self.paint = {}      # id de cubo -> painters

    def part(self, name, parent, pivot, rot=(0, 0, 0), cubes=()):
        self.parts.append(dict(name=name, parent=parent, pivot=pivot, rot=rot, cubes=list(cubes)))

    def cube(self, cid, frm, size, dil=0.0, painters=None):
        if painters is not None:
            self.paint[cid] = painters
        return dict(id=cid, frm=frm, size=size, dil=dil)


def solid(main, shade=None, light=None, round_front=0.0, front=None, back=None, top=None, bottom=None):
    """Painter genérico con volumen: luz al frente, orillas en sombra."""
    shade = shade or tone(main, 0.86)
    light = light or tone(main, 1.08)
    side = g.shade_sides(main, shade)
    f = front if front is not None else (g.roundish(main, strength=round_front) if round_front else side)
    return {"front": f, "back": back if back is not None else side, "right": side, "left": side,
            "top": top if top is not None else light, "bottom": bottom if bottom is not None else shade}


def pattern(fn):
    """Painter igual en todas las caras."""
    return {"*": fn}


def eye_painters(iris, iris_d, iris_l, lash=c(60, 34, 44)):
    def front(x, y, w, h):
        if y == 0:
            return lash
        if y == 1 and x == 1:
            return c(255, 255, 255)
        if y == 1:
            return iris_d
        if y == 3:
            return iris_l
        return iris
    return {"front": front}


def build_head(G, base_y, skin, hair, hair_d, hair_l, iris, iris_d, iris_l, pupil, bangs, mouth, blush=None,
               extra_face=None):
    """Cabeza 10x11x10 con cara pintada (nariz, boca, cachetes) y ojos en piezas aparte."""
    skin_s = tone(skin, 0.93)

    def face(x, y, w, h):
        if extra_face:
            col = extra_face(x, y)
            if col is not None:
                return col
        if y <= 1:
            return hair
        if y == 7 and x == 5:
            return skin_s
        if blush and y == 8 and x in (1, 2, 7, 8):
            return blush
        return skin
    G.part("head", "root", (0, base_y, 0), cubes=[
        G.cube("head", (-5, base_y, -5), (10, 11, 10), 0,
               {"front": face, "top": hair, "right": hair, "left": hair, "back": hair, "bottom": skin_s}),
        G.cube("hair_shell", (-5, base_y, -5), (10, 11, 10), 0.75, hair_shell(hair, hair_d, hair_l, bangs)),
    ])
    ez = g.EYE_Z
    for side, x0 in (("right", -4), ("left", 1)):
        G.part(f"{side}_eye", "head", (x0 + 1.5, base_y + 3, ez), cubes=[
            G.cube(f"{side}_eye", (x0, base_y + 3, ez), (3, 4, 0), 0, eye_painters(iris, iris_d, iris_l))])
        G.part(f"{side}_pupil", f"{side}_eye", (x0 + 1.5, base_y + 3, ez - 0.15), cubes=[
            G.cube(f"{side}_pupil", (x0 + 1, base_y + 3, ez - 0.15), (1, 2, 0), 0, {"front": pupil})])
        G.part(f"{side}_eye_closed", "head", (x0 + 1.5, base_y + 4, ez), cubes=[
            G.cube(f"{side}_eye_closed", (x0, base_y + 4, ez), (3, 1, 0), 0, {"front": c(60, 34, 44)})])
        G.part(f"{side}_eye_happy", "head", (x0 + 1.5, base_y + 4, ez), cubes=[
            G.cube(f"{side}_eye_happy", (x0, base_y + 4, ez), (3, 2, 0), 0,
                   {"front": lambda x, y, w, h: c(60, 34, 44) if (y == 0 and x == 1) or (y == 1 and x != 1) else CLEAR})])
    for side, x0 in (("right", -4), ("left", 1)):
        G.part(f"{side}_brow", "head", (x0 + 1.5, base_y + 7.5, ez - 0.05), cubes=[
            G.cube(f"{side}_brow", (x0, base_y + 7, ez - 0.05), (3, 1, 0), 0, {"front": hair_d})])
    # Bocas y extras de la cara: las mismas piezas que Kiko (así funcionan todas sus expresiones)
    dy = base_y - 50     # las piezas de Kiko están en su cabeza (base 50); se recorren si la cabeza está más abajo
    for p in KIKO_FACE_SOURCE:
        if p["name"].startswith("mouth_") or p["name"] in ("blush", "sweat", "tears", "anger"):
            cubes = []
            for cb in p["cubes"]:
                frm = (cb["frm"][0], cb["frm"][1] + dy, cb["frm"][2])
                cubes.append(G.cube(cb["id"], frm, cb["size"], cb["dil"],
                                    g.painters_for(cb["id"], "school", g.PAL["school"])))
            pv = (p["pivot"][0], p["pivot"][1] + dy, p["pivot"][2])
            G.part(p["name"], "head", pv, p["rot"], cubes)


def hair_shell(hair, hair_d, hair_l, bangs):
    def front(x, y, w, h):
        if y in bangs and x in bangs[y]:
            if y == 1 and x in (2, 6):
                return hair_l
            return hair_d if y >= 2 else hair
        if x in (0, 9) and y <= 9:
            return hair_d if y >= 7 else hair
        return CLEAR

    def side_r(x, y, w, h):
        if y >= 6 and x >= w - 4:
            return CLEAR if y >= 8 or x >= w - 2 else hair_d
        return hair_d if y >= 9 or (x % 3 == 1 and y > 2) else hair

    def side_l(x, y, w, h):
        if y >= 6 and x <= 3:
            return CLEAR if y >= 8 or x <= 1 else hair_d
        return hair_d if y >= 9 or (x % 3 == 1 and y > 2) else hair

    def top(x, y, w, h):
        return hair_l if x == 3 and 1 <= y <= 8 else (hair_d if x in (0, 9) else hair)

    def back(x, y, w, h):
        return hair_d if y >= 9 or x % 3 == 2 else hair
    return {"front": front, "top": top, "right": side_r, "left": side_l, "back": back, "bottom": CLEAR}


# ------------------------------------------------------------------
# Cuerpo con curvas por capas, igual que el de Kiko (busto y glúteos redondeados)
# ------------------------------------------------------------------
def curves(G, top, bottom, center=None, bust_scale=1.0, glute_scale=1.0, chest=None, waist=None, hips=None, inner=None):
    """Cambia el torso de G por el de Kiko: pecho, cintura, cadera y busto/glúteos en capas.
    top/bottom = color de la ropa de arriba/abajo. center(x, y, w, h) pinta algo al centro (botones, blusa)."""
    body = next(p for p in G.parts if p["name"] == "body")
    keep = [cb for cb in body["cubes"] if cb["id"] not in ("bust", "bust_low", "glutes", "chest", "waist", "hips")]
    top_s, bot_s = tone(top, 0.86), tone(bottom, 0.86)

    def with_center(base):
        def f(x, y, w, h):
            if center:
                col = center(x, y, w, h)
                if col is not None:
                    return col
            return g.at(base, x, y, w, h)
        return f

    cubes = [
        G.cube("chest", *(chest or ((-4.5, 39, -2.5), (9, 9, 5), 0.45)),
               solid(top, top_s, front=with_center(g.shade_sides(top, top_s)))),
        G.cube("waist", *(waist or ((-4, 34, -2), (8, 5, 4), 0.35)), solid(top, top_s)),
        G.cube("hips", *(hips or ((-5, 28, -2.5), (10, 6, 5), 0.8)), solid(bottom, bot_s)),
    ]
    k = bust_scale
    for cid, frm, size, dil in g.BUST_CUBES:
        base = re_base(cid)
        frm = (frm[0], frm[1], frm[2] * 1.0 if base in ("bust_upper", "bust_slope") else -2.5 + (frm[2] + 2.5) * k)
        if base == "bust":
            pnt = solid(top, top_s, front=with_center(g.roundish(top, strength=0.6)))
        elif base == "bust_lobe":
            left_lobe = cid.startswith("left_")
            lobe = g.roundish(top, strength=0.75)

            def lobe_front(x, y, w, h, lobe=lobe, left_lobe=left_lobe):
                # inner = la orilla de en medio (donde se abre el cárdigan, por ejemplo)
                if inner is not None and x == (0 if left_lobe else w - 1):
                    return inner
                return g.at(lobe, x, y, w, h)
            pnt = {"front": lobe_front, "top": tone(top, 1.08),
                   "bottom": tone(top, 0.8), "*": tone(top, 0.9)}
        elif base == "bust_low":
            pnt = {"*": tone(top, 0.8), "bottom": tone(top, 0.78)}
        else:
            pnt = {"*": top, "top": tone(top, 1.12), "front": with_center(lambda x, y, w, h: tone(top, 1.05))}
        cubes.append(G.cube(cid, frm, size, dil, pnt))
    k = glute_scale
    for cid, frm, size, dil in g.GLUTE_CUBES:
        base = re_base(cid)
        frm = (frm[0], frm[1], 2.0 + (frm[2] - 2.0) * k)
        if base == "glute_lobe":
            pnt = {"back": g.roundish(bottom), "top": tone(bottom, 1.06), "bottom": tone(bottom, 0.8), "*": tone(bottom, 0.9)}
        elif base == "glutes":
            pnt = {"*": bottom, "back": g.roundish(bottom, strength=0.6), "bottom": tone(bottom, 0.75)}
        else:
            pnt = {"*": tone(bottom, 0.85), "bottom": tone(bottom, 0.78)}
        cubes.append(G.cube(cid, frm, size, dil, pnt))
    body["cubes"] = cubes + keep


def re_base(cid):
    for pre in ("right_", "left_"):
        if cid.startswith(pre):
            return cid[len(pre):]
    return cid


# ------------------------------------------------------------------
# Maru, la herrera
# ------------------------------------------------------------------
def maru():
    G = Girl("maru")
    SK = c(216, 162, 124)
    HAIR, HAIR_D, HAIR_L = c(58, 54, 62), c(38, 36, 42), c(232, 120, 48)   # carbón con puntas naranja
    TANK, TANK_S = c(70, 74, 82), c(54, 58, 66)
    APRON, APRON_S, APRON_D = c(146, 92, 52), c(118, 72, 40), c(90, 54, 30)
    PANTS, PANTS_S = c(86, 92, 62), c(68, 74, 48)
    BOOT, BOOT_S = c(70, 50, 38), c(50, 36, 28)
    WRAP = c(232, 222, 200)
    METAL = c(200, 204, 210)
    G.part("root", None, (0, 0, 0))
    bangs = {0: range(10), 1: range(10), 2: [0, 1, 3, 4, 6, 8, 9], 3: [0, 9]}
    mouth = {(4, 9): c(150, 60, 64), (5, 9): c(150, 60, 64), (6, 8): c(150, 60, 64)}  # media sonrisa segura
    build_head(G, 50, SK, HAIR, HAIR_D, HAIR_L, c(70, 160, 90), c(40, 110, 60), c(140, 210, 140), c(30, 60, 36),
               bangs, mouth, blush=None)
    # Pelo corto y parado, con picos
    G.part("spikes", "head", (0, 50, 0), cubes=[
        G.cube("spike1", (-4, 61, -3), (3, 2, 3), 0.3, solid(HAIR, HAIR_D, HAIR_L)),
        G.cube("spike2", (0, 61.5, -1), (3, 2, 3), 0.3, solid(HAIR, HAIR_D, HAIR_L)),
        G.cube("spike3", (-1.5, 61, 2), (4, 2, 3), 0.3, solid(HAIR, HAIR_D, HAIR_L)),
        G.cube("tips", (-5.5, 59.5, -5.8), (11, 1, 1), 0.2, pattern(lambda x, y, w, h: HAIR_L if x % 3 == 0 else HAIR)),
    ])
    # Goggles en la frente
    G.part("goggles", "head", (0, 50, 0), cubes=[
        G.cube("goggle_band", (-5.6, 60.2, -5.6), (11, 1, 11), 0.15, pattern(c(60, 46, 36))),
        G.cube("goggle_r", (-4, 59.9, -6.4), (3, 2, 1), 0.1, {"*": c(150, 120, 70), "front": lambda x, y, w, h: c(255, 196, 110) if (x, y) == (0, 0) else c(214, 130, 50)}),
        G.cube("goggle_l", (1, 59.9, -6.4), (3, 2, 1), 0.1, {"*": c(150, 120, 70), "front": lambda x, y, w, h: c(255, 196, 110) if (x, y) == (0, 0) else c(214, 130, 50)}),
    ])
    # Torso atlético: hombros anchos, cintura menos marcada
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("chest", (-5, 39, -2.5), (10, 9, 5), 0.55, solid(TANK, TANK_S, front=lambda x, y, w, h: SK if y == 0 and 3 <= x <= 6 else (TANK_S if x in (0, w - 1) else TANK))),
        G.cube("bust", (-4.5, 40.6, -5.0), (9, 4, 2), 0.35, solid(TANK, TANK_S, round_front=0.6)),
        G.cube("bust_low", (-4, 40.0, -4.4), (8, 1, 2), 0.2, solid(tone(TANK, 0.8))),
        G.cube("waist", (-4.5, 34, -2.25), (9, 5, 4), 0.5, solid(TANK, TANK_S)),
        G.cube("hips", (-5, 28, -2.5), (10, 6, 5), 0.7, solid(PANTS, PANTS_S)),
        G.cube("glutes", (-4.5, 28.4, 2.0), (9, 4, 2), 0.5, solid(PANTS, PANTS_S, back=g.roundish(PANTS, strength=0.6))),
        G.cube("belt", (-5.2, 33.2, -2.8), (10, 1, 5), 0.8, {"*": c(60, 44, 32), "front": lambda x, y, w, h: METAL if x == w // 2 else c(60, 44, 32)}),
        G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": tone(SK, 0.93), "front": SK}),
        # Mandil de cuero: del pecho a las rodillas, con bolsa
        G.cube("apron", (-4.8, 18, -4.3), (10, 16, 1), 0.2, {
            "front": lambda x, y, w, h: APRON_D if (y in (11, 12) and 2 <= x <= 6) or x in (0, w - 1) else (APRON_S if y >= h - 2 else APRON),
            "*": APRON_S, "top": APRON}),
    ])
    curves(G, TANK, PANTS, chest=((-4.75, 39, -2.5), (9, 9, 5), 0.6), waist=((-4, 34, -2), (8, 5, 4), 0.42),
           center=lambda x, y, w, h: SK if (h == 9 and y == 0 and 3 <= x <= 5) else None)
    # Mandil de la cintura para abajo (así se ve su figura con el top)
    # Brazos fuertes (4 de ancho), descubiertos con vendas en los antebrazos
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (6.9 * sx, 47.5, 0), rot=(0, 0, 0.1 * -sx), cubes=[
            G.cube(f"{side}_arm", (6.9 * sx - 1.5, 37, -1.5), (3, 11, 3), 0.5,
                   solid(SK, tone(SK, 0.9), top=TANK, front=g.shade_sides(SK, tone(SK, 0.9))))])
        G.part(f"{side}_forearm", f"{side}_arm", (6.9 * sx, 37.5, 0), cubes=[
            G.cube(f"{side}_forearm", (6.9 * sx - 1.5, 27, -1.5), (3, 10, 3), 0.3,
                   pattern(g.rows((3, lambda x, y, w, h: tone(WRAP, 0.9) if (x + y) % 3 == 0 else WRAP), (9, SK))))])
    # Piernas fuertes con botas pesadas
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (2.75 * sx, 29, 0), cubes=[
            G.cube(f"{side}_thigh", (2.75 * sx - 2, 15, -2), (4, 14, 4), 0.85,
                   solid(PANTS, PANTS_S, front=lambda x, y, w, h: PANTS_S if (x == 1 and 5 <= y <= 8) else PANTS))])
        G.part(f"{side}_shin", f"{side}_leg", (2.75 * sx, 15, 0), cubes=[
            G.cube(f"{side}_shin", (2.75 * sx - 1.5, 2, -1.5), (3, 13, 3), 0.65,
                   pattern(g.rows((5, g.shade_sides(PANTS, PANTS_S)), (6, BOOT_S), (12, g.shade_sides(BOOT, BOOT_S))))),
            G.cube(f"{side}_foot", (2.75 * sx - 2, 0, -3), (4, 2, 5), 0.35, pattern(g.rows((0, BOOT), (1, BOOT_S)))),
        ])
    return G


# ------------------------------------------------------------------
# Lumi, la tímida lectora (más bajita)
# ------------------------------------------------------------------
def lumi():
    G = Girl("lumi", height=1.64)
    SK = c(252, 232, 222)
    HAIR, HAIR_D, HAIR_L = c(184, 162, 222), c(150, 128, 194), c(216, 200, 244)    # lavanda
    CARD, CARD_S, CARD_D = c(240, 226, 196), c(220, 204, 172), c(196, 178, 146)     # cárdigan crema
    BLOUSE = c(250, 250, 252)
    SKIRT, SKIRT_S, SKIRT_D = c(62, 78, 128), c(48, 62, 104), c(36, 46, 82)
    TIGHT, TIGHT_S = c(70, 60, 78), c(56, 48, 64)
    SHOE, SHOE_S = c(96, 54, 50), c(70, 38, 36)
    FRAME = c(206, 150, 140)   # lentes color oro rosa
    G.part("root", None, (0, 0, 0))
    hb = 50     # mismas proporciones que Kiko, pero en el juego se dibuja al 90 % (más bajita, no cabezona)
    bangs = {0: range(10), 1: range(10), 2: range(10), 3: [0, 1, 3, 4, 5, 6, 8, 9], 4: [0, 9]}   # fleco largo
    mouth = {(4, 9): c(196, 110, 116), (5, 9): c(196, 110, 116)}   # boquita chiquita
    build_head(G, hb, SK, HAIR, HAIR_D, HAIR_L, c(110, 140, 196), c(70, 96, 150), c(170, 196, 236), c(40, 56, 96),
               bangs, mouth, blush=c(255, 184, 190))
    # Lentes redondos grandes (marco encima de los ojos)
    def frame(x, y, w, h):
        edge = y in (0, h - 1) or x in (0, w - 1)
        return FRAME if edge else CLEAR
    G.part("glasses", "head", (0, hb, 0), cubes=[
        G.cube("lens_r", (-4.5, hb + 2.5, g.EYE_Z - 0.3), (4, 5, 0), 0, {"front": frame}),
        G.cube("lens_l", (0.5, hb + 2.5, g.EYE_Z - 0.3), (4, 5, 0), 0, {"front": frame}),
        G.cube("bridge", (-0.5, hb + 5.5, g.EYE_Z - 0.3), (1, 1, 0), 0, {"front": FRAME}),
    ])
    # Pelo largo y lacio hasta los muslos + trenza de lado con moño
    G.part("hair_back", "head", (0, hb + 6, 6.2), cubes=[
        G.cube("hair_back", (-5.5, 25, 5.6), (11, 31, 2), 0, {
            "back": lambda x, y, w, h: CLEAR if (y == h - 1 and x % 2 == 0) else (HAIR_D if x % 3 == 1 else HAIR),
            "*": HAIR_D, "top": HAIR})])
    G.part("ponytail", "head", (-5.6, hb + 4, -1), cubes=[
        G.cube("braid", (-6.4, hb - 13, -2.2), (2, 15, 2), 0.2, pattern(lambda x, y, w, h: HAIR_D if y % 2 == 0 else HAIR)),
        G.cube("ribbon", (-6.9, hb - 14, -2.7), (3, 1, 3), 0.1, pattern(c(150, 200, 236))),
    ])
    G.part("clip", "head", (0, hb, 0), cubes=[
        G.cube("clip", (2.5, hb + 8.4, -6.0), (2, 2, 1), 0, {"*": c(255, 236, 160), "front": lambda x, y, w, h: c(255, 236, 160) if (x, y) != (1, 0) else CLEAR})])
    # Torso delgadito, hombros angostos
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("chest", (-4, 39, -2), (8, 9, 4), 0.45, solid(CARD, CARD_S, front=lambda x, y, w, h: BLOUSE if 3 <= x <= 4 else (CARD_D if x in (2, 5) else CARD))),
        G.cube("bust", (-3.8, 40.8, -4.2), (8, 3, 2), 0.3, solid(CARD, CARD_S, round_front=0.5,
               front=lambda x, y, w, h: BLOUSE if 3 <= x <= 4 else (CARD_D if x in (2, 5) else CARD))),
        G.cube("waist", (-3.5, 34, -1.75), (7, 5, 4), 0.35, solid(CARD, CARD_S, front=lambda x, y, w, h: CARD_D if x in (2, 4) else CARD)),
        G.cube("hips", (-4.5, 28, -2.25), (9, 6, 4), 0.6, solid(SKIRT, SKIRT_S)),
        G.cube("cardigan_hem", (-4.6, 28.5, -2.5), (9, 5, 5), 0.6, {"*": lambda x, y, w, h: CARD_D if y == h - 1 else CARD, "top": CLEAR, "bottom": CARD_D,
                                                                     "front": lambda x, y, w, h: CLEAR if 3 <= x <= 5 else (CARD_D if y == h - 1 else CARD)}),
        G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": tone(SK, 0.93), "front": SK}),
        G.cube("collar", (-2, 47.6, -2.7), (4, 1, 1), 0.15, pattern(BLOUSE)),
    ])
    curves(G, CARD, SKIRT, bust_scale=0.85, glute_scale=0.85,
           chest=((-4.25, 39, -2.25), (9, 9, 4), 0.4), waist=((-3.75, 34, -1.75), (8, 5, 4), 0.3),
           hips=((-4.75, 28, -2.25), (10, 6, 4), 0.7),
           center=lambda x, y, w, h: BLOUSE if (w // 2 - 1 <= x <= w // 2) else (CARD_D if x in (w // 2 - 2, w // 2 + 1) else None),
           inner=BLOUSE)
    # Falda larga plisada (debajo de la rodilla)
    G.part("lumi_skirt", "body", (0, 34, 0), cubes=[
        G.cube("skirt", (-5.5, 14, -3.2), (11, 14, 7), 0.4, {
            "*": lambda x, y, w, h: SKIRT_D if x % 3 == 0 else (SKIRT_S if y >= h - 1 else SKIRT), "top": SKIRT, "bottom": SKIRT_D}),
    ])
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (6.0 * sx, 47.5, 0), rot=(0, 0, 0.06 * -sx), cubes=[
            G.cube(f"{side}_arm", (6.0 * sx - 1.5, 37, -1.5), (3, 11, 3), 0.15, solid(CARD, CARD_S))])
        G.part(f"{side}_forearm", f"{side}_arm", (6.0 * sx, 37.5, 0), cubes=[
            G.cube(f"{side}_forearm", (6.0 * sx - 1.5, 27, -1.5), (3, 10, 3), 0.1,
                   pattern(g.rows((6, g.shade_sides(CARD, CARD_S)), (7, CARD_D), (9, SK))))])
    # Libro en la mano izquierda
    G.part("book", "left_forearm", (6.0, 29, 0), cubes=[
        G.cube("book", (4.7, 26, -4.2), (3, 5, 4), 0, {"*": c(170, 60, 70), "top": c(244, 238, 220), "bottom": c(244, 238, 220),
                                                         "right": c(244, 238, 220), "front": c(150, 46, 56)})])
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (2.4 * sx, 29, 0), cubes=[
            G.cube(f"{side}_thigh", (2.4 * sx - 1.5, 15, -1.5), (3, 14, 3), 0.6, solid(TIGHT, TIGHT_S))])
        G.part(f"{side}_shin", f"{side}_leg", (2.4 * sx, 15, 0), cubes=[
            G.cube(f"{side}_shin", (2.4 * sx - 1.5, 2, -1.5), (3, 13, 3), 0.45, solid(TIGHT, TIGHT_S)),
            G.cube(f"{side}_foot", (2.4 * sx - 1.5, 0, -2.5), (3, 2, 4), 0.3,
                   {"*": g.rows((0, SHOE), (1, SHOE_S)), "top": lambda x, y, w, h: SHOE if y >= 1 else TIGHT}),
        ])
    return G


# ------------------------------------------------------------------
# Nadi, la exploradora
# ------------------------------------------------------------------
def nadi():
    G = Girl("nadi")
    SK = c(200, 146, 108)
    FRECK = c(176, 118, 84)
    HAIR, HAIR_D, HAIR_L = c(120, 72, 44), c(92, 52, 30), c(164, 108, 66)      # castaño
    TOP, TOP_S = c(64, 156, 150), c(48, 128, 124)                              # blusa verde azulado
    JACKET, JACKET_S, JACKET_D = c(196, 176, 126), c(170, 150, 102), c(140, 120, 78)
    SHORTS, SHORTS_S = c(116, 92, 62), c(94, 74, 48)
    BOOT, BOOT_S = c(104, 66, 40), c(76, 46, 28)
    SOCK = c(236, 230, 214)
    SCARF = c(206, 64, 54)
    G.part("root", None, (0, 0, 0))
    bangs = {0: range(10), 1: range(10), 2: [0, 1, 2, 5, 8, 9], 3: [0, 9]}
    mouth = {(3, 9): c(160, 70, 70), (4, 10): c(160, 70, 70), (5, 10): c(160, 70, 70), (6, 9): c(160, 70, 70)}  # sonrisa
    build_head(G, 50, SK, HAIR, HAIR_D, HAIR_L, c(170, 140, 60), c(120, 96, 36), c(214, 196, 110), c(70, 52, 20),
               bangs, mouth, blush=None,
               extra_face=lambda x, y: FRECK if (y == 7 and x in (2, 7)) or (y == 8 and x in (3, 6)) else None)  # pecas
    # Sombrero de exploradora con pluma
    G.part("nadi_hat", "head", (0, 50, 0), cubes=[
        G.cube("hat_brim", (-8, 60.2, -8), (16, 1, 16), 0, {"*": JACKET_S, "top": lambda x, y, w, h: JACKET_S if (x + y) % 5 == 0 else JACKET}),
        G.cube("hat_crown", (-5, 61.2, -5), (10, 3, 10), 0.2, pattern(g.rows((0, JACKET), (1, c(80, 56, 36)), (2, JACKET)))),
        G.cube("feather", (4.4, 61.5, -1), (1, 4, 2), 0, pattern(lambda x, y, w, h: c(236, 236, 236) if y < 3 else SCARF)),
    ])
    # Pelo a los hombros + trenza larga en la espalda
    G.part("hair_back", "head", (0, 56, 6.2), cubes=[
        G.cube("hair_back", (-5.5, 45, 5.6), (11, 11, 2), 0, {"back": lambda x, y, w, h: HAIR_D if x % 3 == 1 else HAIR, "*": HAIR_D, "top": HAIR})])
    G.part("ponytail", "head", (0, 47, 7), cubes=[
        G.cube("braid", (-1, 30, 6.4), (2, 16, 2), 0.25, pattern(lambda x, y, w, h: HAIR_D if y % 2 == 0 else HAIR)),
        G.cube("braid_tie", (-1.5, 30, 5.9), (3, 1, 3), 0, pattern(SCARF)),
    ])
    # Torso delgado
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("chest", (-4.5, 39, -2.5), (9, 9, 5), 0.45, solid(TOP, TOP_S)),
        G.cube("bust", (-4, 40.6, -5.1), (8, 4, 2), 0.35, solid(TOP, TOP_S, round_front=0.6)),
        G.cube("bust_low", (-3.5, 40.0, -4.5), (7, 1, 2), 0.2, solid(tone(TOP, 0.8))),
        G.cube("waist", (-4, 34, -2), (8, 5, 4), 0.3, solid(TOP, TOP_S)),
        G.cube("hips", (-4.8, 28, -2.5), (10, 6, 5), 0.65, solid(SHORTS, SHORTS_S)),
        G.cube("glutes", (-4.3, 28.4, 2.0), (9, 4, 2), 0.45, solid(SHORTS, SHORTS_S, back=g.roundish(SHORTS, strength=0.6))),
        G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": tone(SK, 0.93), "front": SK}),
        # Chamarra abierta (tapa los lados, se ve la blusa al frente)
        G.cube("jacket", (-4.6, 33, -2.7), (9, 15, 5), 0.75, {
            "front": lambda x, y, w, h: CLEAR if 3 <= x <= 5 else (JACKET_D if x in (2, 6) or (y in (8, 9) and x in (0, 1, 7, 8)) else JACKET),
            "back": lambda x, y, w, h: JACKET_S if x % 4 == 0 else JACKET, "right": JACKET_S, "left": JACKET_S,
            "top": CLEAR, "bottom": JACKET_D}),
        # Cinturón con bolsitas
        G.cube("belt", (-5, 33, -2.8), (10, 1, 5), 0.85, pattern(c(84, 56, 34))),
        G.cube("pouch_r", (-5.6, 30.5, -2.2), (2, 3, 2), 0.3, solid(c(120, 82, 50))),
        G.cube("pouch_l", (3.6, 30.5, -2.2), (2, 3, 2), 0.3, solid(c(120, 82, 50))),
        # Paliacate rojo al cuello
        G.cube("scarf", (-3, 46.5, -3), (6, 2, 6), 0.3, pattern(lambda x, y, w, h: tone(SCARF, 0.85) if (x + y) % 3 == 0 else SCARF)),
        # Mochila con cobija enrollada (más atrás, por los glúteos)
        G.cube("backpack", (-3.5, 35, 3.2), (7, 9, 3), 0.2, solid(c(96, 110, 70), c(76, 90, 54))),
        G.cube("bedroll", (-4.5, 44, 3.6), (9, 2, 2), 0.3, pattern(lambda x, y, w, h: c(180, 60, 50) if x % 3 else c(150, 46, 40))),
    ])
    curves(G, TOP, SHORTS)
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (6.6 * sx, 47.5, 0), rot=(0, 0, 0.08 * -sx), cubes=[
            G.cube(f"{side}_arm", (6.6 * sx - 1.5, 37, -1.5), (3, 11, 3), 0.55, solid(JACKET, JACKET_S))])
        G.part(f"{side}_forearm", f"{side}_arm", (6.6 * sx, 37.5, 0), cubes=[
            G.cube(f"{side}_forearm", (6.6 * sx - 1.5, 27, -1.5), (3, 10, 3), 0.1,
                   pattern(g.rows((2, JACKET_D), (8, SK), (9, c(84, 56, 34)))))])   # mangas arremangadas y pulsera
    # Piernas largas: shorts, piernas descubiertas, calcetas y botas altas
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (2.6 * sx, 29, 0), cubes=[
            G.cube(f"{side}_thigh", (2.6 * sx - 2, 15, -2), (4, 14, 4), 0.65,
                   pattern(g.rows((3, g.shade_sides(SHORTS, SHORTS_S)), (13, g.shade_sides(SK, tone(SK, 0.92))))))])
        G.part(f"{side}_shin", f"{side}_leg", (2.6 * sx, 15, 0), cubes=[
            G.cube(f"{side}_shin", (2.6 * sx - 1.5, 2, -1.5), (3, 13, 3), 0.5,
                   pattern(g.rows((1, g.shade_sides(SK, tone(SK, 0.92))), (3, SOCK), (12, lambda x, y, w, h: BOOT_S if (y % 2 == 0 and x == w // 2) else BOOT)))),
            G.cube(f"{side}_foot", (2.6 * sx - 1.5, 0, -2.5), (3, 2, 4), 0.4, pattern(g.rows((0, BOOT), (1, BOOT_S)))),
        ])
    return G


# ------------------------------------------------------------------
# Rina, la enfermera (tranquila, cariñosa)
# ------------------------------------------------------------------
def rina():
    G = Girl("rina")
    SK = c(250, 228, 214)
    HAIR, HAIR_D, HAIR_L = c(192, 208, 226), c(146, 164, 190), c(226, 234, 246)     # plateado azulado
    SCRUB, SCRUB_S = c(132, 206, 196), c(104, 178, 168)                            # uniforme verde agua
    COAT, COAT_S, COAT_D = c(250, 250, 250), c(226, 230, 234), c(200, 206, 212)     # bata blanca
    CROSS = c(80, 180, 120)                                                         # cruz verde (no roja)
    SHOE, SHOE_S = c(236, 236, 240), c(196, 198, 206)
    G.part("root", None, (0, 0, 0))
    bangs = {0: range(10), 1: range(10), 2: [0, 1, 2, 3, 6, 7, 8, 9], 3: [0, 1, 8, 9]}   # fleco de lado
    build_head(G, 50, SK, HAIR, HAIR_D, HAIR_L, c(214, 110, 140), c(160, 70, 100), c(244, 170, 196), c(110, 40, 64),
               bangs, {}, blush=c(255, 196, 200))
    # Cofia con cruz verde
    G.part("rina_cap", "head", (0, 50, 0), cubes=[
        G.cube("cap", (-3.5, 60.6, -5.6), (7, 2, 3), 0.15, {"*": COAT, "front": lambda x, y, w, h: CROSS if (x == 3 or (y == 0 and 2 <= x <= 4)) else COAT}),
    ])
    # Pelo largo con coleta baja y un mechón al frente
    G.part("hair_back", "head", (0, 56, 6.2), cubes=[
        G.cube("hair_back", (-5.5, 45, 5.6), (11, 11, 2), 0, {"back": lambda x, y, w, h: HAIR_D if x % 3 == 1 else HAIR, "*": HAIR_D, "top": HAIR})])
    G.part("ponytail", "head", (0, 44, 5), cubes=[
        G.cube("tail_top", (-1.5, 40, 3.6), (3, 6, 3), 0.1, pattern(lambda x, y, w, h: HAIR_D if x == 0 else HAIR)),
        G.cube("low_tail", (-1.5, 27, 3.0), (3, 13, 2), 0.2, pattern(lambda x, y, w, h: HAIR_D if x == 0 else HAIR)),
        G.cube("tail_tie", (-2, 40, 3.2), (4, 1, 3), 0, pattern(CROSS)),
    ])
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": tone(SK, 0.93), "front": SK}),
        # Bata abierta encima del uniforme, larga hasta la rodilla
        G.cube("coat", (-4.7, 20, -2.8), (9, 28, 5), 0.75, {
            "front": lambda x, y, w, h: CLEAR if 3 <= x <= 5 and y < 20 else (COAT_D if x in (2, 6) else COAT),
            "back": lambda x, y, w, h: COAT_S if x == w // 2 and y > 12 else COAT, "right": COAT_S, "left": COAT_S,
            "top": CLEAR, "bottom": COAT_D}),
        G.cube("badge", (-4.2, 42, -3.9), (2, 1, 1), 0, pattern(CROSS)),
        G.cube("pocket", (2, 30, -3.9), (2, 2, 1), 0, pattern(COAT_D)),
    ])
    curves(G, SCRUB, SCRUB, center=lambda x, y, w, h: tone(SCRUB, 0.85) if (h == 9 and y <= 2 and x == w // 2) else None)
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (6.6 * sx, 47.5, 0), rot=(0, 0, 0.07 * -sx), cubes=[
            G.cube(f"{side}_arm", (6.6 * sx - 1.5, 37, -1.5), (3, 11, 3), 0.55, solid(COAT, COAT_S))])
        G.part(f"{side}_forearm", f"{side}_arm", (6.6 * sx, 37.5, 0), cubes=[
            G.cube(f"{side}_forearm", (6.6 * sx - 1.5, 27, -1.5), (3, 10, 3), 0.4,
                   pattern(g.rows((6, g.shade_sides(COAT, COAT_S)), (7, COAT_D), (9, SK))))])
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (2.75 * sx, 29, 0), cubes=[
            G.cube(f"{side}_thigh", (2.75 * sx - 2, 15, -2), (4, 14, 4), 0.85, solid(SCRUB, SCRUB_S))])
        G.part(f"{side}_shin", f"{side}_leg", (2.75 * sx, 15, 0), cubes=[
            G.cube(f"{side}_shin", (2.75 * sx - 1.5, 2, -1.5), (3, 13, 3), 0.55, solid(SCRUB, SCRUB_S)),
            G.cube(f"{side}_foot", (2.75 * sx - 1.5, 0, -2.5), (3, 2, 4), 0.35, pattern(g.rows((0, SHOE), (1, SHOE_S)))),
        ])
    return G


# ------------------------------------------------------------------
# Tami, la chef (alegre, ruidosa, vive para cocinar)
# ------------------------------------------------------------------
def tami():
    G = Girl("tami")
    SK = c(222, 176, 140)
    HAIR, HAIR_D, HAIR_L = c(222, 120, 54), c(184, 88, 36), c(250, 166, 90)        # naranja jengibre
    JACKET, JACKET_S, JACKET_D = c(252, 250, 246), c(228, 226, 220), c(200, 198, 190)
    BUTTON = c(60, 56, 60)
    SCARF = c(210, 52, 52)
    APRON, APRON_S = c(150, 44, 52), c(118, 32, 40)
    PANTS_A, PANTS_B = c(40, 40, 46), c(30, 30, 35)                                # pantalón negro de chef
    SHOE, SHOE_S = c(44, 40, 44), c(30, 28, 30)
    G.part("root", None, (0, 0, 0))
    bangs = {0: range(10), 1: range(10), 2: [0, 1, 4, 5, 8, 9], 3: [0, 9]}
    build_head(G, 50, SK, HAIR, HAIR_D, HAIR_L, c(150, 96, 50), c(104, 62, 30), c(200, 150, 96), c(60, 36, 20),
               bangs, {}, blush=c(240, 150, 140),
               extra_face=lambda x, y: c(200, 150, 110) if (y == 8 and x in (2, 7)) else None)
    # Gorro de chef alto
    G.part("tami_hat", "head", (0, 50, 0), cubes=[
        G.cube("toque_band", (-5, 60.4, -5), (10, 2, 10), 0.4, solid(JACKET, JACKET_S)),
        G.cube("toque_puff", (-5.5, 62.4, -5.5), (11, 4, 11), 0.3, {"*": lambda x, y, w, h: JACKET_S if x % 3 == 1 else JACKET, "top": JACKET}),
    ])
    # Chongo atrás
    G.part("hair_back", "head", (0, 56, 6.2), cubes=[
        G.cube("hair_back", (-5.5, 50, 5.6), (11, 6, 2), 0, {"back": lambda x, y, w, h: HAIR_D if x % 3 == 1 else HAIR, "*": HAIR_D, "top": HAIR})])
    G.part("ponytail", "head", (0, 55, 7), cubes=[
        G.cube("bun", (-2, 53, 6.8), (4, 4, 3), 0.3, solid(HAIR, HAIR_D, HAIR_L)),
    ])
    G.part("body", "root", (0, 48, 0), cubes=[
        G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": tone(SK, 0.93), "front": SK}),
        G.cube("neckerchief", (-3, 46.5, -3), (6, 2, 6), 0.35, pattern(lambda x, y, w, h: tone(SCARF, 0.85) if (x + y) % 3 == 0 else SCARF)),
        # Mandil negro largo, de la cintura a la rodilla
        G.cube("apron", (-4.8, 18, -4.2), (10, 16, 1), 0.2, {
            "front": lambda x, y, w, h: APRON_S if x in (0, w - 1) else (c(236, 210, 200) if (y == 5 and 3 <= x <= 6) else APRON), "*": APRON_S}),
        G.cube("apron_tie", (-5.2, 33.2, -2.8), (10, 1, 5), 0.8, pattern(APRON)),
    ])
    def buttons(x, y, w, h):
        # Filipina cruzada: dos filas de botones
        if h == 9 and x in (w // 2 - 2, w // 2 + 2) and y in (2, 5, 8):
            return BUTTON
        return None
    curves(G, JACKET, PANTS_B, center=buttons)
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (6.6 * sx, 47.5, 0), rot=(0, 0, 0.08 * -sx), cubes=[
            G.cube(f"{side}_arm", (6.6 * sx - 1.5, 37, -1.5), (3, 11, 3), 0.55, solid(JACKET, JACKET_S))])
        G.part(f"{side}_forearm", f"{side}_arm", (6.6 * sx, 37.5, 0), cubes=[
            G.cube(f"{side}_forearm", (6.6 * sx - 1.5, 27, -1.5), (3, 10, 3), 0.15,
                   pattern(g.rows((2, JACKET_D), (9, SK))))])        # mangas arremangadas
    # Cucharón en la mano derecha
    G.part("ladle", "right_forearm", (-6.6, 29, 0), cubes=[
        G.cube("ladle_stick", (-7.1, 22, -3.5), (1, 8, 1), 0, pattern(c(170, 170, 176))),
        G.cube("ladle_cup", (-7.6, 20.5, -4), (2, 2, 2), 0, pattern(c(150, 150, 158))),
    ])
    checks = lambda x, y, w, h: PANTS_B if x in (0, w - 1) else PANTS_A
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (2.75 * sx, 29, 0), cubes=[
            G.cube(f"{side}_thigh", (2.75 * sx - 2, 15, -2), (4, 14, 4), 0.85, pattern(checks))])
        G.part(f"{side}_shin", f"{side}_leg", (2.75 * sx, 15, 0), cubes=[
            G.cube(f"{side}_shin", (2.75 * sx - 1.5, 2, -1.5), (3, 13, 3), 0.55, pattern(g.rows((10, checks), (12, SHOE)))),
            G.cube(f"{side}_foot", (2.75 * sx - 1.5, 0, -2.5), (3, 2, 4), 0.35, pattern(g.rows((0, SHOE), (1, SHOE_S)))),
        ])
    return G


# ------------------------------------------------------------------
# v3.0: ropa para cambiarles. Cada chica tiene
#   (base)    su ropa de trabajo
#   casual    ropa de diario
#   wedding   vestido de novia (se desbloquea al casarse)
# Se arma quitando la ropa de la chica base (se queda su cabeza y su pelo) y vistiéndola de nuevo
# con sus mismas proporciones.
# ------------------------------------------------------------------
PROPS = {
    #        piel                 brazo x  giro   pierna x  muslo  curvas
    "maru": (c(216, 162, 124), 6.9, 0.10, 2.75, 4, dict(chest=((-4.75, 39, -2.5), (9, 9, 5), 0.6), waist=((-4, 34, -2), (8, 5, 4), 0.42))),
    "lumi": (c(252, 232, 222), 6.0, 0.06, 2.4, 3, dict(bust_scale=0.85, glute_scale=0.85, chest=((-4.25, 39, -2.25), (9, 9, 4), 0.4),
                                                        waist=((-3.75, 34, -1.75), (8, 5, 4), 0.3), hips=((-4.75, 28, -2.25), (10, 6, 4), 0.7))),
    "nadi": (c(200, 146, 108), 6.6, 0.08, 2.6, 4, {}),
    "rina": (c(250, 228, 214), 6.6, 0.07, 2.75, 4, {}),
    "tami": (c(222, 176, 140), 6.6, 0.08, 2.75, 4, {}),
}
BASE_BUILDERS = {}          # se llena en main (maru, lumi, ...)
# Piezas de trabajo que se quitan al cambiarse
WORK_PARTS = {"goggles", "lumi_skirt", "book", "nadi_hat", "rina_cap", "tami_hat", "ladle"}
CLOTHES = {"body", "right_arm", "left_arm", "right_forearm", "left_forearm", "right_leg", "left_leg", "right_shin", "left_shin"}


def undress(name, suffix):
    G = BASE_BUILDERS[name]()
    G.parts = [p for p in G.parts if p["name"] not in CLOTHES | WORK_PARTS]
    G.name = f"{name}_{suffix}"
    return G


def neck(G, sk):
    return G.cube("neck", (-1.5, 48, -1.5), (3, 2, 3), 0, {"*": tone(sk, 0.93), "front": sk})


def arms(G, name, upper, forearm, extra=()):
    sk, ax, rot = PROPS[name][0], PROPS[name][1], PROPS[name][2]
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_arm", "root", (ax * sx, 47.5, 0), rot=(0, 0, rot * -sx), cubes=[
            G.cube(f"{side}_arm", (ax * sx - 1.5, 37, -1.5), (3, 11, 3), upper[1], upper[0])])
        G.part(f"{side}_forearm", f"{side}_arm", (ax * sx, 37.5, 0), cubes=[
            G.cube(f"{side}_forearm", (ax * sx - 1.5, 27, -1.5), (3, 10, 3), forearm[1], forearm[0])])


def legs(G, name, thigh, shin, foot):
    lx, tw = PROPS[name][3], PROPS[name][4]
    sw = 3
    for side, sx in (("right", -1), ("left", 1)):
        G.part(f"{side}_leg", "root", (lx * sx, 29, 0), cubes=[
            G.cube(f"{side}_thigh", (lx * sx - tw / 2, 15, -tw / 2), (tw, 14, tw), thigh[1], thigh[0])])
        G.part(f"{side}_shin", f"{side}_leg", (lx * sx, 15, 0), cubes=[
            G.cube(f"{side}_shin", (lx * sx - sw / 2, 2, -sw / 2), (sw, 13, sw), shin[1], shin[0]),
            G.cube(f"{side}_foot", (lx * sx - sw / 2, 0, -2.5), (sw, 2, 4), foot[1], foot[0]),
        ])


def skirt_part(G, pname, top_y, bottom_y, main, shade, dark, pleats=3, wide=11, deep=7, dil=0.4, painter=None):
    p = painter or (lambda x, y, w, h: dark if x % pleats == 0 else (shade if y >= h - 1 else main))
    G.part(pname, "body", (0, top_y, 0), cubes=[
        G.cube(pname, (-wide / 2, bottom_y, -deep / 2 + 0.3), (wide, top_y - bottom_y, deep), dil,
               {"*": p, "top": main, "bottom": dark})])


def plaid(a, b, line):
    return lambda x, y, w, h: line if (x % 4 == 1 or y % 4 == 1) else (b if (x // 2 + y // 2) % 2 else a)


# ---------------- casual ----------------
def casual_maru():
    G = undress("maru", "casual")
    sk = PROPS["maru"][0]
    RED, RED_D, BLACK = c(178, 44, 48), c(130, 30, 34), c(40, 34, 38)
    JEAN, JEAN_S = c(64, 88, 136), c(48, 68, 110)
    BOOT, BOOT_S = c(96, 64, 42), c(70, 46, 30)
    fl = plaid(RED, RED_D, BLACK)
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G, sk),
        G.cube("belt", (-5.2, 33.2, -2.8), (10, 1, 5), 0.8, {"*": c(70, 50, 34), "front": lambda x, y, w, h: c(210, 200, 170) if x == w // 2 else c(70, 50, 34)}),
        G.cube("collar", (-2.5, 47.4, -2.9), (5, 1, 1), 0.2, pattern(RED_D)),
    ])
    curves(G, RED, JEAN, **PROPS["maru"][5],
           center=lambda x, y, w, h: (c(236, 232, 224) if x == w // 2 and y % 3 == 1 else fl(x, y, w, h)) if h >= 4 else fl(x, y, w, h))
    arms(G, "maru", (pattern(fl), 0.5), (pattern(g.rows((2, RED_D), (9, sk))), 0.3))
    legs(G, "maru", (solid(JEAN, JEAN_S, front=lambda x, y, w, h: JEAN_S if x == 1 and 4 <= y <= 9 else JEAN), 0.85),
         (pattern(g.rows((6, g.shade_sides(JEAN, JEAN_S)), (12, g.shade_sides(BOOT, BOOT_S)))), 0.65),
         (pattern(g.rows((0, BOOT), (1, BOOT_S))), 0.35))
    return G


def casual_lumi():
    G = undress("lumi", "casual")
    sk = PROPS["lumi"][0]
    HOOD, HOOD_S, HOOD_D = c(198, 176, 236), c(170, 148, 210), c(146, 124, 186)
    SK_A, SK_B, SK_L = c(70, 76, 120), c(58, 62, 104), c(132, 120, 176)
    SOCK = c(246, 246, 250)
    LOAF, LOAF_S = c(110, 66, 50), c(80, 46, 36)
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G, sk),
        G.cube("hoodie_hood", (-4, 44, 2.0), (8, 4, 3), 0.4, solid(HOOD, HOOD_S)),
        G.cube("pocket", (-3, 34.5, -3.3), (6, 2, 1), 0.1, {"*": HOOD_D, "front": lambda x, y, w, h: HOOD_S if 1 <= x <= 4 else HOOD_D}),
        G.cube("strings", (-1.5, 43, -3.6), (3, 3, 0), 0, {"front": lambda x, y, w, h: c(250, 250, 250) if x in (0, 2) else None}),
        G.cube("hoodie_hem", (-4.8, 30.8, -2.6), (10, 3, 5), 0.55, {"*": lambda x, y, w, h: HOOD_D if y == h - 1 else HOOD, "top": None}),
    ])
    curves(G, HOOD, SK_A, **PROPS["lumi"][5])
    skirt_part(G, "casual_skirt", 31, 19, SK_A, SK_B, SK_B, painter=plaid(SK_A, SK_B, SK_L), wide=11, deep=7, dil=0.35)
    arms(G, "lumi", (solid(HOOD, HOOD_S), 0.3), (pattern(g.rows((7, g.shade_sides(HOOD, HOOD_S)), (8, HOOD_D), (9, sk))), 0.25))
    # Libro en la mano (sin el libro no es Lumi)
    G.part("book", "left_forearm", (6.0, 29, 0), cubes=[
        G.cube("book", (4.7, 26, -4.2), (3, 5, 4), 0, {"*": c(70, 110, 170), "top": c(244, 238, 220), "bottom": c(244, 238, 220),
                                                         "right": c(244, 238, 220), "front": c(56, 90, 150)})])
    legs(G, "lumi", (pattern(g.rows((5, g.shade_sides(sk, tone(sk, 0.93))), (13, g.shade_sides(sk, tone(sk, 0.93))))), 0.6),
         (pattern(g.rows((0, sk), (10, g.shade_sides(SOCK, c(222, 222, 230))), (12, SOCK))), 0.45),
         ({"*": g.rows((0, LOAF), (1, LOAF_S)), "top": LOAF}, 0.3))
    return G


def casual_nadi():
    G = undress("nadi", "casual")
    sk = PROPS["nadi"][0]
    SUN, SUN_S, SUN_D = c(250, 210, 80), c(226, 182, 56), c(200, 156, 40)
    DEN, DEN_S, DEN_D = c(92, 128, 180), c(72, 104, 152), c(56, 84, 126)
    SNK, SNK_S = c(244, 244, 246), c(200, 200, 210)
    flower = lambda x, y, w, h: c(255, 250, 240) if (x * 3 + y * 5) % 17 == 0 else None
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G, sk),
        # Chamarra de mezclilla corta, abierta
        G.cube("jacket", (-4.6, 37, -2.7), (9, 11, 5), 0.75, {
            "front": lambda x, y, w, h: CLEAR if 3 <= x <= 5 else (DEN_D if x in (2, 6) or y == h - 1 else DEN),
            "back": lambda x, y, w, h: DEN_D if y == h - 1 else DEN, "right": DEN_S, "left": DEN_S, "top": CLEAR, "bottom": DEN_D}),
    ])
    curves(G, SUN, SUN, center=lambda x, y, w, h: flower(x, y, w, h))
    skirt_part(G, "casual_skirt", 31, 18, SUN, SUN_S, SUN_D, pleats=4, wide=12, deep=8, dil=0.45,
               painter=lambda x, y, w, h: flower(x, y, w, h) or (SUN_D if x % 4 == 0 else (SUN_S if y >= h - 1 else SUN)))
    arms(G, "nadi", (solid(DEN, DEN_S), 0.55), (pattern(g.rows((2, DEN_D), (8, sk), (9, c(84, 56, 34)))), 0.1))
    legs(G, "nadi", (pattern(g.shade_sides(sk, tone(sk, 0.92))), 0.65),
         (pattern(g.rows((9, g.shade_sides(sk, tone(sk, 0.92))), (11, c(250, 250, 250)), (12, SNK))), 0.5),
         (pattern(g.rows((0, SNK), (1, SNK_S))), 0.4))
    return G


def casual_rina():
    G = undress("rina", "casual")
    sk = PROPS["rina"][0]
    CARD, CARD_S, CARD_D = c(244, 232, 210), c(222, 208, 182), c(198, 182, 154)
    BLOUSE = c(176, 210, 240)
    SKIRT, SKIRT_S, SKIRT_D = c(150, 196, 180), c(122, 170, 154), c(100, 146, 132)
    FLAT, FLAT_S = c(236, 214, 200), c(206, 180, 164)
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G, sk),
        G.cube("collar", (-2, 47.6, -2.7), (4, 1, 1), 0.15, pattern(BLOUSE)),
        G.cube("cardigan_hem", (-4.6, 31.5, -2.5), (9, 3, 5), 0.6, {"*": lambda x, y, w, h: CARD_D if y == h - 1 else CARD, "top": CLEAR, "bottom": CARD_D,
                                                                     "front": lambda x, y, w, h: CLEAR if 3 <= x <= 5 else (CARD_D if y == h - 1 else CARD)}),
    ])
    curves(G, CARD, SKIRT, center=lambda x, y, w, h: BLOUSE if (w // 2 - 1 <= x <= w // 2) else (CARD_D if x in (w // 2 - 2, w // 2 + 1) else None),
           inner=BLOUSE)
    skirt_part(G, "casual_skirt", 31, 8, SKIRT, SKIRT_S, SKIRT_D, pleats=3, wide=11, deep=7, dil=0.45)
    arms(G, "rina", (solid(CARD, CARD_S), 0.45), (pattern(g.rows((7, g.shade_sides(CARD, CARD_S)), (8, CARD_D), (9, sk))), 0.35))
    legs(G, "rina", (pattern(g.shade_sides(sk, tone(sk, 0.93))), 0.6),
         (pattern(g.shade_sides(sk, tone(sk, 0.93))), 0.45),
         (pattern(g.rows((0, FLAT), (1, FLAT_S))), 0.3))
    return G


def casual_tami():
    G = undress("tami", "casual")
    sk = PROPS["tami"][0]
    WHITE, NAVY = c(250, 250, 248), c(44, 58, 108)
    DEN, DEN_S, DEN_D = c(84, 116, 170), c(66, 94, 144), c(50, 74, 116)
    SNK, SNK_S = c(214, 52, 52), c(170, 36, 36)
    stripe = lambda x, y, w, h: NAVY if y % 2 == 1 else WHITE

    def bib(x, y, w, h):
        if h >= 9 and y >= 4 and 2 <= x <= w - 3:
            return DEN_D if x in (2, w - 3) else (c(240, 200, 80) if (y == 4 and x in (3, w - 4)) else DEN)
        if h >= 9 and x in (2, w - 3):
            return DEN_D            # tirantes
        return stripe(x, y, w, h)
    G.part("body", "root", (0, 48, 0), cubes=[neck(G, sk)])
    curves(G, WHITE, DEN, center=bib)
    skirt_part(G, "casual_skirt", 31, 19, DEN, DEN_S, DEN_D, pleats=5, wide=11, deep=7, dil=0.4,
               painter=lambda x, y, w, h: DEN_D if (x % 5 == 0 or y == h - 1) else (c(240, 200, 80) if (y == 3 and x in (2, 8)) else DEN))
    arms(G, "tami", (pattern(stripe), 0.5), (pattern(g.rows((1, stripe), (9, sk))), 0.15))
    legs(G, "tami", (pattern(g.shade_sides(sk, tone(sk, 0.93))), 0.85),
         (pattern(g.rows((9, g.shade_sides(sk, tone(sk, 0.93))), (11, WHITE), (12, SNK))), 0.55),
         (pattern(g.rows((0, SNK), (1, c(250, 250, 250)))), 0.35))
    return G


# ---------------- boda ----------------
SATIN, SATIN_S, SATIN_D = c(252, 250, 246), c(232, 228, 222), c(208, 204, 198)
LACE = c(222, 216, 226)
VEIL = c(250, 250, 252)


def wedding(name, crown=(c(255, 196, 210), c(255, 236, 150), c(150, 200, 130))):
    """Vestido de novia: corpiño de encaje, mangas abombadas, falda amplia a media pierna, velo y corona de flores."""
    G = undress(name, "wedding")
    sk, ax = PROPS[name][0], PROPS[name][1]
    hb = next(p for p in G.parts if p["name"] == "head")["pivot"][1]
    lace = lambda x, y, w, h: LACE if (x + y) % 3 == 0 and y >= 1 else None
    G.part("body", "root", (0, 48, 0), cubes=[
        neck(G, sk),
        G.cube("sash", (-4.4, 33.4, -2.6), (9, 1, 5), 0.7, pattern(crown[0])),
        G.cube("bow", (-1.5, 33, 2.9), (3, 2, 1), 0.2, pattern(crown[0])),
        # Falda en dos capas, amplia
        G.cube("gown_upper", (-5.5, 22, -3.6), (11, 12, 8), 0.5,
               {"*": lambda x, y, w, h: SATIN_S if x % 3 == 0 else SATIN, "top": SATIN, "bottom": SATIN_D}),
        G.cube("gown_lower", (-6.5, 7, -4.6), (13, 15, 10), 0.6,
               {"*": lambda x, y, w, h: SATIN_D if y >= h - 1 else (LACE if (y >= h - 4 and (x + y) % 2 == 0) else (SATIN_S if x % 3 == 0 else SATIN)),
                "top": SATIN, "bottom": SATIN_D}),
    ])
    curves(G, SATIN, SATIN, center=lace, inner=None, **PROPS[name][5])
    # Mangas abombadas y guantes largos
    sleeve = solid(SATIN, SATIN_S)
    arms(G, name, (sleeve, 0.85), (pattern(g.rows((0, SATIN_S), (9, g.shade_sides(SATIN, SATIN_S)))), 0.2))
    legs(G, name, (pattern(g.shade_sides(SATIN, SATIN_S)), 0.6),
         (pattern(g.shade_sides(c(246, 240, 236), c(226, 220, 216))), 0.4),
         (pattern(g.rows((0, SATIN), (1, SATIN_D))), 0.3))
    # Corona de flores y velo largo con orilla de encaje
    flowers = [(-4.5, -5.6), (-2.0, -5.9), (0.5, -5.9), (3.0, -5.6), (-5.6, -3), (4.6, -3), (-5.6, 0), (4.6, 0)]
    cubes = []
    for i, (fx, fz) in enumerate(flowers):
        col = crown[i % 3]
        cubes.append(G.cube(f"crown_{i}", (fx, hb + 11.3, fz), (1, 1, 1), 0.3,
                            {"*": col, "top": lambda x, y, w, h, col=col: c(255, 255, 255) if (x + y) % 2 == 0 else col}))
    # El velo va pegadito atrás de la cabeza (o del pelo largo) y cae hasta la espalda
    back_z = 5.9
    for p in G.parts:
        if p["name"] == "hair_back":
            for cb in p["cubes"]:
                back_z = max(back_z, cb["frm"][2] + cb["size"][2] + 0.15)

    def veil(shade):
        def f(x, y, w, h):
            if y >= h - 2:
                return LACE if (x + y) % 2 == 0 else None
            if y == 0:
                return crown[0] if x % 3 == 1 else VEIL
            return tone(VEIL, shade if x % 3 == 0 else 1.0)
        return f
    cubes.append(G.cube("veil", (-5.5, hb - 14, back_z), (11, 25, 0), 0, {"back": veil(0.94), "front": veil(0.9)}))
    G.part("veil", "head", (0, hb, 0), cubes=cubes)
    return G


OUTFIT_BUILDERS = {
    "maru": [casual_maru, lambda: wedding("maru", (c(232, 120, 48), c(255, 236, 150), c(150, 200, 130)))],
    "lumi": [casual_lumi, lambda: wedding("lumi", (c(200, 176, 240), c(255, 250, 250), c(150, 200, 236)))],
    "nadi": [casual_nadi, lambda: wedding("nadi", (c(250, 210, 80), c(206, 64, 54), c(150, 200, 130)))],
    "rina": [casual_rina, lambda: wedding("rina", (c(150, 216, 196), c(255, 255, 255), c(200, 220, 255)))],
    "tami": [casual_tami, lambda: wedding("tami", (c(240, 120, 70), c(255, 236, 150), c(214, 52, 52)))],
}


# ------------------------------------------------------------------
# Piezas que el modelo de Java (KikoModel) espera aunque esta chica no las use
# ------------------------------------------------------------------
REQUIRED_EMPTY = [
    ("hair_back", "head", (0, 56, 6.2)), ("ponytail", "head", (6, 58.5, 0.5)), ("clip", "head", (0, 50, 0)),
    ("skirt", "body", (0, 34, 0)), ("dress", "skirt", (0, 34, 0)), ("hood", "body", (0, 48, 0)),
    ("scarf", "body", (0, 48, 0)), ("hat", "head", (0, 50, 0)), ("hardhat", "head", (0, 50, 0)),
    ("beanie", "head", (0, 50, 0)),
]


def complete(G):
    names = {p["name"] for p in G.parts}
    for name, parent, pivot in REQUIRED_EMPTY:
        if name not in names:
            G.part(name, parent, pivot)
            names.add(name)
    # El orden importa para el Java: cada padre antes que sus hijos
    order, placed = [], set()
    pending = list(G.parts)
    while pending:
        for p in list(pending):
            if p["parent"] is None or p["parent"] in placed:
                order.append(p)
                placed.add(p["name"])
                pending.remove(p)
    G.parts = order
    return G


# ------------------------------------------------------------------
# Textura y render (reusa el motor de gen_assets)
# ------------------------------------------------------------------
def use(G):
    g.PARTS = G.parts
    g.PART = {p["name"]: p for p in G.parts}
    g.pack()


def texture(G):
    img = Image.new("RGBA", (g.TEX, g.TEX), (0, 0, 0, 0))
    px = img.load()
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
                        if col is not None:
                            px[fx + xx, fy + yy] = col
    return img


def kiko_render(view_yaw, scale, size, center):
    g.PARTS = KIKO_PARTS
    g.PART = {p["name"]: p for p in KIKO_PARTS}
    tex = g.build_texture("casual2")
    return g.render(tex, "casual2", view_yaw=view_yaw, scale=scale, size=size, center=center)


def girl_render(G, tex, view_yaw, scale, size, center, pitch=-0.1):
    use_no_pack(G)
    k = G.height / 1.8   # el modelo mide 61.75 unidades; se escala a su altura
    return g.render(tex, G.name, view_yaw=view_yaw, view_pitch=pitch, scale=scale * k, size=size, center=center,
                    hidden=PREVIEW_HIDDEN)


PREVIEW_HIDDEN = {"right_eye_closed", "left_eye_closed", "right_eye_happy", "left_eye_happy",
                  "blush", "sweat", "tears", "anger"} | {f"mouth_{m}" for m in g.MOUTHS if m != "neutral"}


def use_no_pack(G):
    g.PARTS = G.parts
    g.PART = {p["name"]: p for p in G.parts}


KIKO_PARTS = list(g.PARTS)


def main(out):
    os.makedirs(out, exist_ok=True)
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 22)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 15)
    girls = [complete(maru()), complete(lumi()), complete(nadi()), complete(rina()), complete(tami())]
    BASE_BUILDERS.update(maru=maru, lumi=lumi, nadi=nadi, rina=rina, tami=tami)
    variants = {G.name: [complete(fn()) for fn in OUTFIT_BUILDERS[G.name]] for G in girls}
    export(girls + [V for vs in variants.values() for V in vs])
    texs = {}
    for G in girls + [V for vs in variants.values() for V in vs]:
        use(G)
        texs[G.name] = texture(G)
    outfit_sheet(girls, variants, texs, out)

    info = {
        "maru": ("Maru · la herrera", "1.80 bloques · atlética, hombros anchos"),
        "lumi": ("Lumi · la tímida lectora", "1.64 bloques · bajita y delgadita"),
        "nadi": ("Nadi · la exploradora", "1.80 bloques · delgada, piernas largas"),
        "rina": ("Rina · la enfermera", "1.80 bloques · tranquila y cariñosa"),
        "tami": ("Tami · la chef", "1.80 bloques · alegre y ruidosa"),
    }

    # 1) Todas juntas con Kiko y la línea de altura del jugador
    scale = 6.6
    W, H = 300, 520
    N = len(girls) + 1
    sheet = Image.new("RGBA", (W * N, H + 60), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    ground = H + 20
    for i, (label, fn) in enumerate([("Kiko", None)] + [(info[G.name][0], G) for G in girls]):
        if fn is None:
            r = kiko_render(0.35, scale, (W, H), (W / 2, H - 20))
        else:
            r = girl_render(fn, texs[fn.name], 0.35, scale, (W, H), (W / 2, H - 20))
        sheet.alpha_composite(r, (i * W, 40))
        d.text((i * W + 10, 8), label, fill=(255, 210, 120), font=small)
    for blocks, col, lab in ((1.8, (120, 200, 255), "jugador 1.8"), (1.64, (200, 160, 255), "Lumi 1.64")):
        y = 40 + H - 20 - blocks * UNITS_PER_BLOCK * scale
        for x in range(0, W * N, 12):
            d.line([(x, y), (x + 6, y)], fill=col, width=2)
        d.text((W * N - 110, y - 18), lab, fill=col, font=small)
    sheet.convert("RGB").save(os.path.join(out, "chicas_juntas.png"))

    # 2) Vueltas de cada una
    views = [("frente", 0.0), ("3/4", 0.55), ("perfil", -math.pi / 2 + 0.02), ("espalda 3/4", math.pi + 0.55)]
    for G in girls:
        sh = Image.new("RGBA", (W * 4, H + 70), (40, 42, 54, 255))
        d = ImageDraw.Draw(sh)
        for j, (lab, vy) in enumerate(views):
            r = girl_render(G, texs[G.name], vy, scale, (W, H), (W / 2, H - 20))
            sh.alpha_composite(r, (j * W, 60))
            d.text((j * W + W // 2 - 30, 40), lab, fill="white", font=small)
        d.text((10, 8), info[G.name][0], fill=(255, 210, 120), font=font)
        d.text((380, 12), info[G.name][1], fill=(200, 200, 210), font=small)
        sh.convert("RGB").save(os.path.join(out, f"chica_{G.name}.png"))

    # 3) Caras de cerca
    CW, CH = 300, 300
    faces = Image.new("RGBA", (CW * len(girls), CH + 30), (40, 42, 54, 255))
    d = ImageDraw.Draw(faces)
    for i, G in enumerate(girls):
        use_no_pack(G)
        head = g.PART["head"]
        hb = head["pivot"][1]
        keep = {n for n in g.PART if n == "root" or n == "head" or _under(n, "head")}
        hidden = {n for n in g.PART if n not in keep} | PREVIEW_HIDDEN
        r = g.render(texs[G.name], G.name, view_yaw=0.3, view_pitch=0.0, scale=17, size=(CW, CH),
                     center=(CW / 2, CH / 2 + (hb + 5.5) * 17), hidden=hidden)
        faces.alpha_composite(r, (i * CW, 30))
        d.text((i * CW + 10, 6), info[G.name][0], fill=(255, 210, 120), font=small)
    faces.convert("RGB").save(os.path.join(out, "chicas_caras.png"))


def outfit_sheet(girls, variants, texs, out):
    """Una fila por chica: trabajo, casual y boda (de frente y de 3/4)."""
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 16)
    W, H, scale = 230, 440, 5.6
    labels = ["trabajo", "casual", "boda"]
    for G in girls:
        looks = [G] + variants[G.name]
        sh = Image.new("RGBA", (W * 6, H + 40), (40, 42, 54, 255))
        d = ImageDraw.Draw(sh)
        for i, V in enumerate(looks):
            for j, vy in enumerate((0.0, math.pi + 0.5)):
                r = girl_render(V, texs[V.name], vy if j == 0 else vy, scale, (W, H), (W / 2, H - 16))
                sh.alpha_composite(r, ((i * 2 + j) * W, 34))
            d.text((i * 2 * W + 12, 8), f"{G.name.capitalize()} · {labels[i]}", fill=(255, 210, 120), font=small)
        sh.convert("RGB").save(os.path.join(out, f"ropa_{G.name}.png"))


def export(girls):
    """Texturas al mod y el código Java de los modelos (client/model/GirlModels.java)."""
    tex_dir = os.path.join(g.ASSETS, "textures", "entity")
    methods = []
    for G in girls:
        use(G)
        texture(G).save(os.path.join(tex_dir, f"{G.name}.png"))
        body = g.gen_java_body(G.parts, {p["name"]: p for p in G.parts})
        methods.append(f"""    /** {G.name.capitalize()} ({G.height} bloques en el juego). */
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
 * Modelos de Maru, Lumi y Nadi. Los genera tools/gen_girls.py (no editar a mano).
 * Tienen las mismas piezas que Kiko (cabeza, ojos, bocas, brazos con codo, piernas con rodilla...),
 * así que usan la misma clase KikoModel y todas sus animaciones y expresiones.
 */
@SuppressWarnings("unused")
public final class GirlModels {
    private GirlModels() {}

""" + "\n".join(methods) + "}\n"
    with open(os.path.join(os.path.dirname(g.MODEL_JAVA), "GirlModels.java"), "w") as f:
        f.write(java)


def _under(name, ancestor):
    p = g.PART[name]["parent"]
    while p:
        if p == ancestor:
            return True
        p = g.PART[p]["parent"]
    return False


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else ".")
