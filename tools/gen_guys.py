"""
Los NPC hombres (v3.3). Seis compañeros con su propio oficio, ropa y personalidad.
Usan el cuerpo de hombre de gen_kids.py y la misma técnica de capas (piel, pelo, ojos y ropa pintables),
pero cada uno trae sus colores fijos y sus accesorios.

  Toño   el ranchero      sombrero vaquero, camisa de cuadros, paliacate
  Leo    el pescador      gorro de lana, chaleco impermeable, botas de hule
  Dante  el guardia       peto de hierro, hombreras y capa
  Iván   el minero        casco con lámpara, overol y guantes
  Emi    el músico        chamarra de cuero y guitarra en la espalda
  Beto   el comerciante   chaleco, morral cruzado y gorrito

Uso   python tools/gen_guys.py carpeta_preview
"""
import math
import os
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_kids as gk  # noqa: E402
import gen_girls as gg  # noqa: E402
import gen_assets as g  # noqa: E402

c = g.c
T = gk.T
SK, SK_S = gk.SK, gk.SK_S
HR, HR_D, HR_L = gk.HR, gk.HR_D, gk.HR_L
C1, C1_S, C1_D = gk.C1, gk.C1_S, gk.C1_D
C2, C2_S, C2_D = gk.C2, gk.C2_S, gk.C2_D
SH, SH_D = gk.SH, gk.SH_D
WHITE = gk.WHITE
CLEAR = None
solid, pattern = gg.solid, gk.pattern


def man(name, stubble_unused=False, shirt=None, arm_upper=None, arm_lower=None, legs=None, shins=None, feet=None, extra_body=()):
    """Cuerpo base de hombre (hombros anchos, sin curvas). Regresa G y dónde quedan los pies."""
    G = gg.Girl(name)
    G.part("root", None, (0, 0, 0))
    face_extra = None   # v3.3.1 sin barba de puntitos (se veía sucio)
    gg.build_head(G, 50, SK, HR, HR_D, HR_L, gk.EY, gk.EY_D, gk.EY_L, gk.PUPIL, gk.BANGS_BOY, {}, blush=None, extra_face=face_extra)
    gk.short_hair(G)
    tone_down_mouths(G)
    # v3.3.1 ojos de chico: más chicos, con blanco a los lados y sin pestañas de anime
    def guy_eye(x, y, w, h):
        # v3.3.6 el ojo es blanco (3 renglones) y lo que se mueve es el iris completo de 1x3 encima,
        # así cuando mira de reojo se desliza sobre lo blanco sin dejar pedazos pintados atrás
        return SK if y == 0 else WHITE

    def guy_iris(x, y, w, h):
        return gk.EY_D if y == 1 else gk.EY
    for side in ("right", "left"):
        G.paint[f"{side}_eye"] = {"front": guy_eye}
        G.paint[f"{side}_pupil"] = {"front": guy_iris}
        pupil = next(p for p in G.parts if p["name"] == f"{side}_pupil")
        for cb in pupil["cubes"]:
            cb["size"] = (1, 3, 0)
    shirt = shirt or {"*": C1, "right": C1_S, "left": C1_S, "top": C1, "bottom": C1_S}
    G.part("body", "root", (0, 48, 0), cubes=[
        gk.neck(G),
        G.cube("chest", (-5, 38, -2.5), (10, 10, 5), 0.55, shirt),
        G.cube("waist", (-4.5, 33, -2.25), (9, 5, 4), 0.5, solid(C1, C1_S)),
        G.cube("hips", (-4.5, 28, -2.5), (9, 5, 5), 0.6, solid(C2, C2_S)),
    ] + list(extra_body))
    f = gk.limbs(G, 7.1, (47.5, 11, 3), 10, 2.6, (14, 4, 29), (13, 3), 2, arm_rot=0.08,
                 upper=(arm_upper or solid(C1, C1_S), 0.65),
                 lower=(arm_lower or pattern(g.rows((3, g.shade_sides(C1, C1_S)), (4, C1_D), (9, SK))), 0.4),
                 legp=(legs or solid(C2, C2_S), 0.8), shinp=(shins or pattern(g.shade_sides(C2, C2_S)), 0.6),
                 footp=(feet or pattern(g.rows((0, SH), (1, SH_D))), 0.4))
    return G, f


def tone_down_mouths(G):
    """Las bocas de las chicas traen labios rojitos; a ellos se les ponen de un tono más natural."""
    def soft(col):
        if col is None or len(col) < 4 or col[3] != 255:
            return col
        r, gg_, b = col[:3]
        if r > 140 and gg_ < 110 and b < 120:          # rojo de labios
            return T(gk.SKIN, 150)
        return col

    def wrap(pnt):
        if callable(pnt):
            return lambda x, y, w, h, pnt=pnt: soft(pnt(x, y, w, h))
        return soft(pnt)
    for p in G.parts:
        if p["name"].startswith("mouth_"):
            for cb in p["cubes"]:
                pnts = G.paint.get(cb["id"])
                if pnts:
                    G.paint[cb["id"]] = {k: wrap(v) for k, v in pnts.items()}


def plaid_c1(x, y, w, h):
    if x % 4 == 1 or y % 4 == 1:
        return C1_D
    return C1 if (x // 2 + y // 2) % 2 else C1_S


# ------------------------------------------------------------------
def tono():
    BROWN, BROWN_D, BAND = c(150, 104, 62), c(118, 80, 46), c(70, 46, 30)
    BOOT, BOOT_D = c(110, 70, 40), c(80, 50, 28)
    RED = c(196, 50, 50)
    G, feet = man("tono", shirt={"*": plaid_c1, "front": lambda x, y, w, h: WHITE if (x == w // 2 and y % 3 == 1 and h == 10) else plaid_c1(x, y, w, h)},
                  arm_upper=pattern(plaid_c1), arm_lower=pattern(g.rows((2, C1_D), (9, SK))),
                  shins=pattern(g.rows((4, g.shade_sides(C2, C2_S)), (12, g.shade_sides(BOOT, BOOT_D)))),
                  feet=pattern(g.rows((0, BOOT), (1, BOOT_D))))
    body = next(p for p in G.parts if p["name"] == "body")
    body["cubes"] += [
        G.cube("bandana", (-3, 46.5, -3), (6, 2, 6), 0.35, pattern(lambda x, y, w, h: c(240, 230, 220) if (x + y) % 4 == 0 else RED)),
        G.cube("belt", (-4.7, 32.6, -2.7), (9, 1, 5), 0.7, {"*": BAND, "front": lambda x, y, w, h: c(220, 190, 90) if 3 <= x <= 5 else BAND}),
    ]
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("hat_brim", (-8, 60.2, -8), (16, 1, 16), 0, {"*": BROWN_D, "top": lambda x, y, w, h: BROWN_D if (x in (0, w - 1) or y in (0, h - 1)) else BROWN}),
        G.cube("hat_crown", (-4.5, 61.2, -4.5), (9, 4, 9), 0.1, pattern(g.rows((0, BAND), (3, BROWN)))),
        G.cube("hat_dent", (-3.5, 64.8, -2), (7, 1, 4), 0, pattern(BROWN_D)),
    ])
    return G, feet


def leo():
    YEL, YEL_D = c(244, 196, 40), c(206, 158, 20)
    NAVY = c(40, 54, 96)
    BOOT, BOOT_D = c(56, 100, 64), c(40, 76, 48)
    stripe = lambda x, y, w, h: NAVY if y % 2 == 1 else WHITE
    G, feet = man("leo", shirt={"*": stripe, "right": stripe, "left": stripe},
                  arm_upper=pattern(stripe), arm_lower=pattern(g.rows((1, stripe), (9, SK))),
                  shins=pattern(g.rows((3, g.shade_sides(C2, C2_S)), (12, g.shade_sides(BOOT, BOOT_D)))),
                  feet=pattern(g.rows((0, BOOT), (1, BOOT_D))))
    body = next(p for p in G.parts if p["name"] == "body")
    body["cubes"] += [
        # Chaleco impermeable abierto
        G.cube("vest", (-4.8, 32, -2.8), (10, 16, 5), 0.8, {
            "front": lambda x, y, w, h: CLEAR if 3 <= x <= 6 and y < 13 else (YEL_D if x in (2, 7) else YEL),
            "*": YEL, "right": YEL_D, "left": YEL_D, "top": CLEAR, "bottom": YEL_D}),
        G.cube("pocket_r", (-4.5, 35, -3.9), (2, 2, 1), 0, pattern(YEL_D)),
        G.cube("pocket_l", (2.5, 35, -3.9), (2, 2, 1), 0, pattern(YEL_D)),
    ]
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("beanie", (-5.5, 58.8, -5.5), (11, 3, 11), 0.45, pattern(lambda x, y, w, h: c(30, 42, 80) if y == 2 else (NAVY if x % 2 else c(50, 66, 110)))),
    ])
    return G, feet


def dante():
    STEEL, STEEL_D, STEEL_L = c(176, 182, 190), c(128, 134, 144), c(220, 224, 230)
    CAPE, CAPE_D = c(150, 34, 44), c(110, 22, 32)
    G, feet = man("dante",
                  arm_upper=solid(STEEL, STEEL_D), arm_lower=pattern(g.rows((5, g.shade_sides(C1, C1_S)), (9, c(90, 60, 40)))),
                  shins=pattern(g.rows((2, g.shade_sides(C2, C2_S)), (12, g.shade_sides(STEEL, STEEL_D)))),
                  feet=pattern(g.rows((0, STEEL_D), (1, c(70, 70, 76)))))
    body = next(p for p in G.parts if p["name"] == "body")
    body["cubes"] += [
        G.cube("breastplate", (-5, 36, -2.8), (10, 12, 5), 0.85, {
            "front": lambda x, y, w, h: STEEL_L if (x == w // 2 and y > 0) else (STEEL_D if y == h - 1 or x in (0, w - 1) else STEEL),
            "*": STEEL, "right": STEEL_D, "left": STEEL_D, "top": STEEL_L, "bottom": STEEL_D}),
        G.cube("tabard", (-2.5, 26, -3.9), (5, 10, 1), 0.1, {"*": C1, "front": lambda x, y, w, h: c(240, 200, 80) if abs(x - 2) + abs(y - 4) == 2 or (x, y) == (2, 4) else C1}),
        G.cube("cape", (-5, 22, 3.0), (10, 26, 1), 0.3, {"*": CAPE, "back": lambda x, y, w, h: CAPE_D if x % 3 == 0 else CAPE, "front": CAPE_D}),
        G.cube("belt", (-4.8, 32.5, -2.9), (10, 1, 5), 0.9, {"*": c(70, 50, 34), "front": lambda x, y, w, h: c(220, 190, 90) if x == w // 2 else c(70, 50, 34)}),
    ]
    for side, sx in (("right", -1), ("left", 1)):
        arm = next(p for p in G.parts if p["name"] == f"{side}_arm")
        arm["cubes"].append(G.cube(f"{side}_pauldron", (7.1 * sx - 2.5, 44.5, -2.5), (5, 3, 5), 0.3,
                                   {"*": STEEL, "top": STEEL_L, "bottom": STEEL_D}))
    return G, feet


def ivan():
    HARD, HARD_D = c(250, 200, 40), c(214, 160, 20)
    LAMP = c(255, 250, 200)
    GLOVE = c(150, 110, 70)
    BOOT, BOOT_D = c(60, 52, 48), c(40, 34, 32)

    def overall(x, y, w, h):
        if h == 10 and y >= 4 and 2 <= x <= w - 3:
            return C2_D if x in (2, w - 3) else C2
        if h == 10 and x in (2, w - 3):
            return C2_D
        return C1
    G, feet = man("ivan", shirt={"*": C1, "front": overall, "back": overall, "right": C1_S, "left": C1_S},
                  arm_lower=pattern(g.rows((2, C1_D), (7, SK), (9, GLOVE))),
                  shins=pattern(g.rows((5, g.shade_sides(C2, C2_S)), (12, g.shade_sides(BOOT, BOOT_D)))),
                  feet=pattern(g.rows((0, BOOT), (1, BOOT_D))))
    body = next(p for p in G.parts if p["name"] == "body")
    for cb in body["cubes"]:
        if cb["id"] == "waist":
            G.paint["waist"] = solid(C2, C2_S)
    body["cubes"] += [
        G.cube("tool_belt", (-4.8, 30.5, -2.8), (10, 2, 5), 0.85, {"*": c(110, 76, 44), "front": lambda x, y, w, h: c(160, 160, 166) if x in (2, 7) else c(110, 76, 44)}),
    ]
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("hard_dome", (-5.5, 59.6, -5.5), (11, 3, 11), 0.4, {"*": HARD, "bottom": HARD_D}),
        G.cube("hard_brim", (-5.5, 59.3, -8.6), (11, 1, 3), 0, pattern(HARD_D)),
        G.cube("hard_lamp", (-1, 60.2, -7.1), (2, 2, 1), 0, {"*": c(150, 150, 150), "front": LAMP}),
    ])
    return G, feet


def emi():
    LEATHER, LEATHER_D = c(44, 40, 46), c(30, 28, 32)
    WOOD, WOOD_D = c(180, 110, 56), c(130, 76, 36)
    G, feet = man("emi",
                  arm_upper=solid(LEATHER, LEATHER_D), arm_lower=pattern(g.rows((5, g.shade_sides(LEATHER, LEATHER_D)), (6, c(200, 200, 206)), (9, SK))),
                  feet=pattern(g.rows((0, WHITE), (1, c(200, 200, 206)))))
    body = next(p for p in G.parts if p["name"] == "body")
    body["cubes"] += [
        G.cube("jacket", (-4.8, 32, -2.8), (10, 16, 5), 0.8, {
            "front": lambda x, y, w, h: CLEAR if 3 <= x <= 6 else (c(200, 200, 206) if (y == 3 and x in (1, 8)) else LEATHER),
            "*": LEATHER, "right": LEATHER_D, "left": LEATHER_D, "top": CLEAR, "bottom": LEATHER_D}),
        # Guitarra en la espalda (cuerpo, boca y mástil)
        G.cube("guitar_body", (-3, 27, 3.4), (6, 8, 2), 0.1, {"*": WOOD_D, "back": lambda x, y, w, h: c(40, 26, 18) if (x in (2, 3) and y in (3, 4)) else WOOD}),
        G.cube("guitar_neck", (-0.5, 35, 3.9), (1, 13, 1), 0, pattern(c(90, 56, 30))),
        G.cube("guitar_strap", (-4.6, 38, -2.9), (1, 10, 6), 0.2, pattern(c(150, 40, 50))),
    ]
    # Pelo un poco más largo con colita
    G.part("hair_back", "head", (0, 56, 6.2), cubes=[
        G.cube("hair_back", (-5.5, 51, 5.6), (11, 5, 2), 0, {"back": lambda x, y, w, h: HR_D if x % 3 == 1 else HR, "*": HR_D, "top": HR})])
    G.part("ponytail", "head", (0, 53, 7), cubes=[
        G.cube("tail", (-1, 47, 6.6), (2, 5, 2), 0.25, pattern(lambda x, y, w, h: HR_D if y % 2 else HR))])
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("earring", (5.1, 53.5, -0.5), (1, 1, 1), 0, pattern(c(230, 200, 90)))])
    return G, feet


def beto():
    VEST, VEST_D = c(110, 64, 140), c(80, 44, 104)
    BAG, BAG_D = c(150, 100, 56), c(110, 72, 38)
    G, feet = man("beto", shirt={"*": C1, "front": lambda x, y, w, h: C1_D if (x == w // 2 and h == 10 and y > 1) else C1, "right": C1_S, "left": C1_S})
    body = next(p for p in G.parts if p["name"] == "body")
    body["cubes"] += [
        G.cube("vest", (-4.8, 33, -2.8), (10, 15, 5), 0.8, {
            "front": lambda x, y, w, h: CLEAR if 4 <= x <= 5 else (c(230, 200, 90) if (x in (3, 6) and y in (4, 8)) else VEST),
            "*": VEST, "right": VEST_D, "left": VEST_D, "top": CLEAR, "bottom": VEST_D}),
        # Morral cruzado
        G.cube("strap", (-0.5, 33, -3.8), (1, 15, 1), 0.1, pattern(BAG_D)),
        G.cube("satchel", (3.6, 28, -2.5), (3, 5, 5), 0.2, {"*": BAG, "top": BAG_D, "right": lambda x, y, w, h: BAG_D if y == 1 else BAG}),
        G.cube("belt", (-4.7, 32.6, -2.7), (9, 1, 5), 0.7, pattern(c(60, 44, 32))),
    ]
    G.part("spikes", "head", (0, 50, 0), cubes=[
        G.cube("curl1", (-4.5, 61, -3.5), (3, 2, 3), 0.3, solid(HR, HR_D)),
        G.cube("curl2", (-1, 61.4, -1), (3, 2, 3), 0.3, solid(HR, HR_D)),
        G.cube("curl3", (2, 61, -3), (3, 2, 3), 0.3, solid(HR, HR_D)),
        G.cube("curl4", (-2, 61, 2), (4, 2, 3), 0.3, solid(HR, HR_D)),
    ])
    return G, feet


#          nombre  constructor  alto   piel                pelo               ojos               ropa 1             ropa 2            zapatos
GUYS = [
    ("tono", tono, 1.88, (204, 146, 106), (84, 56, 38), (110, 70, 40), (70, 120, 200), (64, 88, 136), (110, 70, 40)),
    ("leo", leo, 1.82, (255, 226, 208), (236, 204, 120), (70, 120, 200), (250, 250, 250), (140, 120, 90), (56, 100, 64)),
    ("dante", dante, 1.92, (112, 74, 50), (46, 40, 46), (226, 156, 40), (150, 34, 44), (60, 60, 70), (70, 70, 76)),
    ("ivan", ivan, 1.80, (228, 180, 146), (46, 40, 46), (64, 44, 34), (150, 154, 160), (220, 120, 50), (60, 52, 48)),
    ("emi", emi, 1.76, (255, 226, 208), (240, 120, 150), (146, 104, 206), (240, 240, 236), (40, 40, 46), (250, 250, 250)),
    ("beto", beto, 1.84, (160, 108, 74), (84, 56, 38), (70, 160, 90), (240, 236, 220), (90, 74, 60), (60, 50, 50)),
]
INFO = {
    "tono": ("Toño · el ranchero", "tranquilo, chambeador, de pocas palabras"),
    "leo": ("Leo · el pescador", "relajado, bromista, siempre huele a mar"),
    "dante": ("Dante · el guardia", "serio, protector, leal hasta el final"),
    "ivan": ("Iván · el minero", "terco, curioso, ama las cuevas"),
    "emi": ("Emi · el músico", "soñador, coqueto, siempre cantando"),
    "beto": ("Beto · el comerciante", "platicador, astuto, regatea todo"),
}


def build_all():
    out = []
    for name, fn, h, skin, hair, eye, c1, c2, shoe in GUYS:
        G, feet = fn()
        G = gk.finish(G, feet)
        G.name, G.height = name, h
        gg.use(G)
        tex = gk.colorize(gg.texture(G), skin, hair, eye, c1, c2, shoe)
        out.append((G, tex))
    return out


def main(out):
    os.makedirs(out, exist_ok=True)
    built = build_all()
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 15)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 12)
    ppb = 150
    W, H = 230, 340
    # 1) Todos juntos (de frente y de espaldas)
    sheet = Image.new("RGBA", (W * len(built), H * 2 + 50), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    for i, (G, tex) in enumerate(built):
        for j, vy in enumerate((0.35, math.pi + 0.4)):
            gg.use_no_pack(G)
            img = g.render(tex, G.name, view_yaw=vy, view_pitch=-0.1, scale=ppb * G.height / G.units, size=(W, H - 10),
                           center=(W / 2, H - 20), hidden=gg.PREVIEW_HIDDEN)
            sheet.alpha_composite(img, (i * W, 46 + j * H))
        d.text((i * W + 8, 6), INFO[G.name][0], fill=(255, 210, 120), font=font)
        d.text((i * W + 8, 26), INFO[G.name][1], fill=(190, 190, 200), font=small)
    for j in range(2):
        y = 46 + j * H + H - 20 - ppb * 1.8
        for x in range(0, W * len(built), 12):
            d.line([(x, y), (x + 6, y)], fill=(120, 200, 255), width=1)
    sheet.convert("RGB").save(os.path.join(out, "chicos_juntos.png"))
    # 2) Caras
    CW = 230
    faces = Image.new("RGBA", (CW * len(built), CW + 30), (40, 42, 54, 255))
    d = ImageDraw.Draw(faces)
    for i, (G, tex) in enumerate(built):
        gg.use_no_pack(G)
        keep = {n for n in g.PART if n in ("root", "head") or gg._under(n, "head")}
        hidden = {n for n in g.PART if n not in keep} | gg.PREVIEW_HIDDEN
        hb = g.PART["head"]["pivot"][1]
        img = g.render(tex, G.name, view_yaw=0.3, view_pitch=0.0, scale=13, size=(CW, CW), center=(CW / 2, CW / 2 + (hb + 5.5) * 13),
                       hidden=hidden)
        faces.alpha_composite(img, (i * CW, 30))
        d.text((i * CW + 8, 6), INFO[G.name][0], fill=(255, 210, 120), font=font)
    faces.convert("RGB").save(os.path.join(out, "chicos_caras.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/guys")
