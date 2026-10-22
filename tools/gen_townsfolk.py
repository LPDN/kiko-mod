"""
Gente del pueblo (v3.4). Modelos con capas pintables (piel, pelo, ojos, ropa 1, ropa 2, zapatos) igual que los hijos,
así cada NPC sale con rasgos y colores al azar. Varios cortes de pelo, ropa y accesorios para que no se repitan.

  Hombres   camisa · granjero (sombrero y overol) · chaleco y gorra · panadero (mandil) · abuelito (suéter y lentes)
  Mujeres   vestido · chongo y mandil · melena con pantalón · trenza y overol · abuelita (rebozo y lentes)
  Niños     niño · niña · chavo · chava (los mismos de los hijos)

Uso   python tools/gen_townsfolk.py carpeta_preview
"""
import math
import os
import random
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_assets as g  # noqa: E402
import gen_girls as gg  # noqa: E402
import gen_kids as gk  # noqa: E402
import gen_guys as gy  # noqa: E402

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
STRAW, STRAW_D = c(232, 206, 130), c(196, 166, 96)
FRAME = c(50, 40, 40)


def body(G):
    return next(p for p in G.parts if p["name"] == "body")


def glasses(G):
    ez = g.EYE_Z - 0.35
    frame = lambda x, y, w, h: FRAME if (y in (0, h - 1) or x in (0, 4, 5, 9)) and not (x in (4, 5) and y != 1) else CLEAR
    G.part("npc_glasses", "head", (0, 50, 0), cubes=[G.cube("glasses", (-5, 52.5, ez), (10, 4, 0), 0, {"front": frame})])


def straw_hat(G, band=C1_D):
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("straw_brim", (-8, 60.2, -8), (16, 1, 16), 0, {"*": STRAW_D, "top": lambda x, y, w, h: STRAW_D if (x + y) % 5 == 0 else STRAW}),
        G.cube("straw_top", (-4.5, 61.2, -4.5), (9, 3, 9), 0.1, pattern(g.rows((0, band), (2, STRAW))))])


def cap(G):
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("cap_top", (-5.5, 59.5, -5.5), (11, 2, 11), 0.45, {"*": C2_D, "top": C2}),
        G.cube("cap_brim", (-4.5, 59.6, -9), (9, 1, 4), 0, pattern(C2_D))])


# ------------------------------------------------------------------
# Hombres (el cuerpo de los chicos, con ojos de chico)
# ------------------------------------------------------------------
def man_shirt():
    G, f = gy.man("man_shirt")
    return G, f


def man_farmer():
    def overall(x, y, w, h):
        if h == 10 and y >= 4 and 2 <= x <= w - 3:
            return C2_D if x in (2, w - 3) else C2
        if h == 10 and x in (2, w - 3):
            return C2_D
        return C1
    G, f = gy.man("man_farmer", shirt={"*": C1, "front": overall, "back": overall, "right": C1_S, "left": C1_S})
    G.paint["waist"] = solid(C2, C2_S)
    straw_hat(G)
    return G, f


def man_vest():
    G, f = gy.man("man_vest")
    body(G)["cubes"].append(G.cube("vest", (-4.8, 33, -2.8), (10, 15, 5), 0.8, {
        "front": lambda x, y, w, h: CLEAR if 4 <= x <= 5 else (WHITE if (x in (3, 6) and y in (4, 8)) else C2),
        "*": C2, "right": C2_S, "left": C2_S, "top": CLEAR, "bottom": C2_S}))
    cap(G)
    return G, f


def man_baker():
    G, f = gy.man("man_baker")
    body(G)["cubes"].append(G.cube("apron", (-4, 26, -3.4), (8, 18, 1), 0.2, {
        "*": WHITE, "front": lambda x, y, w, h: c(226, 226, 230) if y == 6 else WHITE}))
    return G, f


def man_elder():
    G, f = gy.man("man_elder", shirt={"*": C1, "front": lambda x, y, w, h: (C1_D if (x == w // 2 and h == 10) else
                                                                          (WHITE if (x == w // 2 + 1 and h == 10 and y % 3 == 1) else C1)),
                                      "right": C1_S, "left": C1_S})
    glasses(G)
    return G, f


# ------------------------------------------------------------------
# Mujeres (cuerpo de adulta de los hijos; las mismas proporciones y nada más)
# ------------------------------------------------------------------
def woman_base(name, hair_bottom=38, skirt=True, skirt_len=15, top=None, bust=1.0):
    G = gg.Girl(name)
    G.part("root", None, (0, 0, 0))
    gk.head(G, gk.BANGS_GIRL)
    if hair_bottom:
        gk.hair_long(G, hair_bottom)
    G.part("body", "root", (0, 48, 0), cubes=[gk.neck(G)])
    gg.curves(G, C1, C1, bust_scale=bust, center=top or (lambda x, y, w, h: WHITE if (h == 9 and y == 0 and 3 <= x <= 5) else None))
    if skirt:
        G.part("kid_skirt", "body", (0, 31, 0), cubes=[
            G.cube("kid_skirt", (-5.5, 31 - skirt_len, -3.2), (11, skirt_len, 7), 0.45,
                   {"*": lambda x, y, w, h: C2_D if x % 3 == 0 else (C2_S if y >= h - 1 else C2), "top": C2, "bottom": C2_D})])
        legp = pattern(g.shade_sides(SK, SK_S))
        shinp = pattern(g.shade_sides(SK, SK_S))
    else:
        legp = solid(C2, C2_S)
        shinp = pattern(g.shade_sides(C2, C2_S))
    f = gk.limbs(G, 6.6, (47.5, 11, 3), 10, 2.75, (14, 4, 29), (13, 3), 2,
                 upper=(solid(C1, C1_S), 0.5), lower=(pattern(g.rows((5, g.shade_sides(C1, C1_S)), (6, C1_D), (9, SK))), 0.35),
                 legp=(legp, 0.6), shinp=(shinp, 0.45), footp=(pattern(g.rows((0, SH), (1, SH_D))), 0.3))
    return G, f


def bun(G):
    G.part("ponytail", "head", (0, 58, 6), cubes=[
        G.cube("bun", (-2, 57, 5.4), (4, 4, 3), 0.3, solid(HR, HR_D))])


def woman_dress():
    return woman_base("woman_dress")


def woman_bun():
    G, f = woman_base("woman_bun", hair_bottom=None)
    bun(G)
    body(G)["cubes"].append(G.cube("apron", (-3.5, 20, -3.6), (7, 20, 1), 0.2, {
        "*": WHITE, "front": lambda x, y, w, h: c(240, 200, 210) if (y == 1 or y == h - 2) else WHITE}))
    return G, f


def woman_bob():
    G, f = woman_base("woman_bob", hair_bottom=49, skirt=False)
    return G, f


def woman_braid():
    def overall(x, y, w, h):
        return C2 if (h <= 6 and 2 <= x <= w - 3) else None
    G, f = woman_base("woman_braid", hair_bottom=None, skirt=False, top=overall)
    gk.ponytail(G, 14)
    straw_hat(G)
    return G, f


def woman_elder():
    G, f = woman_base("woman_elder", hair_bottom=None, skirt_len=18)
    bun(G)
    glasses(G)
    body(G)["cubes"].append(G.cube("shawl", (-5.2, 41, -3), (10, 7, 6), 0.7, {
        "*": lambda x, y, w, h: C2_D if (x + y) % 4 == 0 else C2, "top": C2, "bottom": CLEAR}))
    return G, f


# ------------------------------------------------------------------
# Guardias (v3.8). Armadura de hierro con colores fijos (no se pintan con los rasgos), casco con nariguera y
# un penacho que cambia con el rango. El juego prende el penacho según el rango usando la ranura de peinado
#   rango 0 recluta (sin penacho) · 1 guardia (penacho blanco) · 2 sargento (penacho azul) · 3 capitán (penacho rojo alto y dorado)
# ------------------------------------------------------------------
IRON, IRON_D, IRON_L = c(176, 178, 186), c(118, 120, 132), c(216, 218, 224)
GOLD, GOLD_D = c(230, 190, 62), c(178, 138, 40)
LEATHER, LEATHER_D = c(104, 68, 42), c(70, 46, 28)
PL_WHITE, PL_BLUE, PL_RED = c(240, 240, 244), c(52, 86, 196), c(200, 40, 44)


def guard_armor(G, ax, lx, female):
    def plate(x, y, w, h):
        if y == 0 or x in (0, w - 1):
            return IRON_D
        if x in (w // 2 - 1, w // 2):
            return IRON_L
        return IRON
    cw = 10
    body(G)["cubes"].append(G.cube("breastplate", (-5, 37, -2.9 if not female else -3.4), (cw, 10, 5 if not female else 6), 0.95, {
        "*": IRON, "front": plate, "back": lambda x, y, w, h: IRON_D if y == 0 else IRON, "right": IRON_D, "left": IRON_D,
        "top": IRON_D, "bottom": CLEAR}))
    body(G)["cubes"].append(G.cube("belt", (-5, 34, -2.6), (10, 2, 5), 0.85, {
        "*": LEATHER, "front": lambda x, y, w, h: GOLD if x in (4, 5) else LEATHER, "top": LEATHER_D, "bottom": LEATHER_D}))
    body(G)["cubes"].append(G.cube("tassets", (-5, 29, -2.8), (10, 4, 5), 0.9, {
        "*": IRON_D, "front": lambda x, y, w, h: IRON_L if x in (4, 5) else IRON, "top": CLEAR, "bottom": CLEAR}))
    for side, sx in (("right", -1), ("left", 1)):
        arm = next(p for p in G.parts if p["name"] == f"{side}_arm")
        arm["cubes"].append(G.cube(f"{side}_pauldron", (ax * sx - 2.5, 43.5, -2.5), (5, 4, 5), 0.6, {
            "*": IRON, "top": IRON_L, "bottom": CLEAR, "front": IRON_D, "back": IRON_D}))
        fa = next(p for p in G.parts if p["name"] == f"{side}_forearm")
        fa["cubes"].append(G.cube(f"{side}_bracer", (ax * sx - 1.5, 28.5, -1.5), (3, 5, 3), 0.55, {
            "*": IRON_D, "top": IRON, "bottom": IRON}))
        sh = next(p for p in G.parts if p["name"] == f"{side}_shin")
        sh["cubes"].append(G.cube(f"{side}_greave", (lx * sx - 1.5, 5, -1.5), (3, 8, 3), 0.6, {
            "*": IRON, "front": lambda x, y, w, h: IRON_L if x == 1 else IRON, "top": CLEAR, "bottom": CLEAR}))


def guard_helmet(G):
    G.part("guard_helmet", "head", (0, 50, 0), cubes=[
        G.cube("helm_dome", (-5, 55.5, -5), (10, 5, 10), 1.05, {
            "*": IRON, "top": IRON_L, "bottom": CLEAR,
            "front": lambda x, y, w, h: IRON_D if y == h - 1 else IRON}),
        G.cube("helm_rim", (-5, 54.8, -5), (10, 1, 10), 1.1, {"*": IRON_D, "top": CLEAR, "bottom": CLEAR}),
        G.cube("helm_nasal", (-0.5, 53.5, -5.9), (1, 5, 1), 0.2, {"*": IRON_D, "front": IRON, "top": CLEAR, "bottom": CLEAR})])
    # Penachos por rango (van en las ranuras de peinado)
    G.part("hs_long", "head", (0, 50, 0), cubes=[
        G.cube("plume_1", (-1, 61.4, -4), (2, 3, 8), 0.3, {"*": PL_WHITE, "top": c(255, 255, 255)})])
    G.part("hs_curly", "head", (0, 50, 0), cubes=[
        G.cube("plume_2", (-1, 61.4, -4.5), (2, 4, 9), 0.35, {"*": PL_BLUE, "top": c(90, 130, 230)}),
        G.cube("plume_2b", (-5.5, 54.8, -5.5), (11, 1, 11), 0.95, {"*": GOLD_D, "top": GOLD, "bottom": CLEAR})])
    G.part("hs_quiff", "head", (0, 50, 0), cubes=[
        G.cube("plume_3", (-1, 61.4, -4.5), (2, 6, 9), 0.4, {"*": PL_RED, "top": c(240, 90, 90)}),
        G.cube("plume_3b", (-5.5, 54.8, -5.5), (11, 1, 11), 0.95, {"*": GOLD, "top": c(250, 214, 100), "bottom": CLEAR}),
        G.cube("plume_3c", (-1.5, 60.2, -1.5), (3, 1, 3), 0.4, {"*": GOLD, "top": GOLD})])


def guard_man():
    G, f = gy.man("guard_man")
    guard_armor(G, 7.1, 2.6, False)
    guard_helmet(G)
    return G, f


def guard_woman():
    G, f = woman_base("guard_woman", hair_bottom=None, skirt=False, bust=0.3)
    gk.ponytail(G, 14)
    guard_armor(G, 6.6, 2.75, True)
    guard_helmet(G)
    return G, f


# ------------------------------------------------------------------
# Peinados extra de los hombres (v3.4). Van en todos los modelos de hombre y el juego prende uno según el NPC
#   0 normal (el corto de siempre) · 1 greñas · 2 chinitos · 3 copete
# Con sombrero o gorra solo salen 0 y 1 (los otros se meterían en el sombrero)
# ------------------------------------------------------------------
HAIR_STYLES = ["hs_long", "hs_curly", "hs_quiff"]


def hair_styles(G):
    def locks(x, y, w, h):
        if y == h - 1 and x % 2 == 1:
            return CLEAR
        return HR_D if x % 3 == 1 else HR
    G.part("hs_long", "head", (0, 50, 0), cubes=[
        G.cube("hs_long_back", (-5.5, 51, 4.8), (11, 8, 1), 0.35, {"*": HR_D, "back": locks, "top": HR}),
        G.cube("hs_long_r", (-5.9, 52, 1), (1, 6, 4), 0.25, {"*": HR_D, "right": locks, "left": locks}),
        G.cube("hs_long_l", (4.9, 52, 1), (1, 6, 4), 0.25, {"*": HR_D, "right": locks, "left": locks})])

    def curls(x, y, w, h):
        k = (x * 3 + y * 5) % 7
        return HR_L if k == 0 else (HR_D if k in (3, 5) else HR)
    G.part("hs_curly", "head", (0, 50, 0), cubes=[
        G.cube("hs_curly_top", (-5.5, 59.2, -5.3), (11, 3, 11), 0.55, {"*": curls, "bottom": CLEAR}),
        G.cube("hs_curly_front", (-5, 58.6, -6.1), (10, 1, 1), 0.4, {"*": curls})])
    G.part("hs_quiff", "head", (0, 50, 0), cubes=[
        G.cube("hs_quiff_base", (-3.5, 60.4, -5.8), (7, 2, 6), 0.35, {"*": HR_D, "top": HR, "front": lambda x, y, w, h: HR_L if (x == 2 and y == 0) else HR}),
        G.cube("hs_quiff_tip", (-3, 61.9, -6.8), (6, 1, 3), 0.3, {"*": HR, "front": HR_L, "top": HR_L})])


ADULTS = [
    ("man_shirt", man_shirt, "m", False), ("man_farmer", man_farmer, "m", False), ("man_vest", man_vest, "m", False),
    ("man_baker", man_baker, "m", False), ("man_elder", man_elder, "m", True),
    ("woman_dress", woman_dress, "f", False), ("woman_bun", woman_bun, "f", False), ("woman_bob", woman_bob, "f", False),
    ("woman_braid", woman_braid, "f", False), ("woman_elder", woman_elder, "f", True),
    ("guard_man", guard_man, "m", False), ("guard_woman", guard_woman, "f", False),
]
KIDS = [("child_boy", lambda: gk.child(False)), ("child_girl", lambda: gk.child(True)),
        ("teen_boy", lambda: gk.teen(False)), ("teen_girl", lambda: gk.teen(True))]

NAMES_M = ["Ramón", "Julio", "Pedro", "Chuy", "Memo", "Lalo", "Paco", "Rigo", "Tavo", "Nacho", "Beni", "Toñito", "Chencho", "Fer"]
NAMES_F = ["Lupita", "Rosa", "Carmen", "Mari", "Chela", "Paty", "Lety", "Vero", "Nena", "Juana", "Tere", "Male", "Sofi", "Cata"]
JOBS = {"man_farmer": "granjero", "woman_braid": "granjera", "man_baker": "panadero", "woman_bun": "cocinera",
        "man_vest": "tendero", "man_shirt": "leñador", "woman_dress": "costurera", "woman_bob": "maestra",
        "man_elder": "abuelito", "woman_elder": "abuelita"}
GRAYS = [(236, 232, 222), (192, 208, 226), (170, 170, 176)]


def build(name, fn):
    G, f = fn() if name not in dict(KIDS) else (fn(), None)
    if name.startswith("man"):
        hair_styles(G)
    if f is not None:
        G = gk.finish(G, f)
    G.name = name
    return G


def random_npc(rng, model, elder=False):
    skin = rng.choice(gk.SKINS)
    natural = [gk.HAIRS[i] for i in (0, 1, 2, 3, 4, 5, 7)]
    hair = rng.choice(GRAYS) if elder else rng.choice(natural if rng.random() < 0.8 else gk.HAIRS[8:])
    eye = rng.choice(gk.EYES)
    c1, c2 = rng.sample(gk.CLOTHES, 2)
    shoe = rng.choice(gk.SHOES)
    return skin, hair, eye, c1, c2, shoe


HEIGHTS = {"man_elder": 1.75, "woman_elder": 1.62, "guard_man": 1.95, "guard_woman": 1.8}


def height(name):
    return HEIGHTS.get(name, 1.85 if name.startswith("man") else 1.72)


def export():
    """Texturas (en la carpeta de los hijos, se pintan igual) y client/model/TownModels.java."""
    tex_dir = os.path.join(g.ASSETS, "textures", "entity", "kid")
    methods, units = [], []
    for name, fn, _, _ in ADULTS:
        G = build(name, fn)
        G.units = 61.75      # alto de la cabeza (sin contar sombreros ni copete, si no se encogen)
        gg.use(G)
        gg.texture(G).save(os.path.join(tex_dir, f"{name}.png"))
        body = g.gen_java_body(G.parts, {p["name"]: p for p in G.parts})
        methods.append(f"""    /** {name} ({height(name)} bloques, {G.units:.2f} unidades de alto). */
    public static TexturedModelData {name}() {{
        ModelData data = new ModelData();
{body}
        return TexturedModelData.of(data, {g.TEX}, {g.TEX});
    }}
""")
        units.append(f'        java.util.Map.entry("{name}", {G.units:.3f}f)')
    java = """package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Gente del pueblo (v3.4). Los genera tools/gen_townsfolk.py (no editar a mano).
 * Texturas pintables como las de los hijos (textures/entity/kid/), así cada vecino sale con sus colores.
 * Los niños del pueblo usan los modelos de KidModels.
 */
@SuppressWarnings("unused")
public final class TownModels {
    private TownModels() {}

    /** Alto de cada modelo en unidades (para escalarlo a su altura en bloques). */
    public static final java.util.Map<String, Float> UNITS = java.util.Map.ofEntries(
""" + ",\n".join(units) + """);

""" + "\n".join(methods) + "}\n"
    with open(os.path.join(os.path.dirname(g.MODEL_JAVA), "TownModels.java"), "w") as f:
        f.write(java)


def hair_preview(out):
    """Los 4 peinados de hombre lado a lado."""
    G = build("man_shirt", man_shirt)
    rng = random.Random(5)
    W, H = 200, 260
    sheet = Image.new("RGBA", (W * 8, H), (40, 42, 54, 255))
    for j, col in enumerate([random_npc(rng, "man_shirt") for _ in range(2)]):
        gg.use(G)
        tex = gk.colorize(gg.texture(G), *col)
        gg.use_no_pack(G)
        for i in range(4):
            hidden = set(gg.PREVIEW_HIDDEN) | {n for k, n in enumerate(HAIR_STYLES) if k != i - 1}
            keep = {n for n in g.PART if n in ("root", "head") or gg._under(n, "head")}
            hidden |= {n for n in g.PART if n not in keep}
            hb = g.PART["head"]["pivot"][1]
            img = g.render(tex, G.name, view_yaw=0.6, view_pitch=-0.05, scale=11, size=(W, H),
                           center=(W / 2, H / 2 + (hb + 5.5) * 11), hidden=hidden)
            sheet.alpha_composite(img, ((j * 4 + i) * W, 0))
    sheet.convert("RGB").save(os.path.join(out, "peinados.png"))


def guard_preview(out):
    """Los dos guardias con los 4 rangos."""
    os.makedirs(out, exist_ok=True)
    rng = random.Random(8)
    W, H, ppb = 200, 330, 150
    sheet = Image.new("RGBA", (W * 4, H * 2), (40, 42, 54, 255))
    hs = ["hs_long", "hs_curly", "hs_quiff"]
    for r, (name, fn) in enumerate((("guard_man", guard_man), ("guard_woman", guard_woman))):
        G = build(name, fn)
        G.units = 61.75
        col = random_npc(rng, name)
        for rank in range(4):
            gg.use(G)
            tex = gk.colorize(gg.texture(G), *col)
            gg.use_no_pack(G)
            hidden = (set(gg.PREVIEW_HIDDEN) - set(hs)) | {n for i, n in enumerate(hs) if i != rank - 1}
            img = g.render(tex, G.name, view_yaw=0.5, view_pitch=-0.08, scale=ppb * height(name) / G.units, size=(W, H - 10),
                           center=(W / 2, H - 20), hidden=hidden)
            sheet.alpha_composite(img, (rank * W, r * H))
    sheet.convert("RGB").save(os.path.join(out, "guardias.png"))


def main(out):
    os.makedirs(out, exist_ok=True)
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 14)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 12)
    rng = random.Random(34)
    models = {n: build(n, fn) for n, fn, _, _ in ADULTS}
    for n, fn in KIDS:
        models[n] = fn()
        models[n].name = n
    heights = {"child_boy": 1.1, "child_girl": 1.1, "teen_boy": 1.55, "teen_girl": 1.55}
    # 20 vecinos: 12 adultos (todos los modelos sin repetir los primeros 10), 2 abuelitos ya incluidos, 8 niños
    picks = [a[0] for a in ADULTS] + ["man_shirt", "woman_dress"] + ["child_boy", "child_girl", "teen_boy", "teen_girl",
                                                                     "child_girl", "child_boy", "teen_girl", "teen_boy"]
    W, H, ppb = 190, 300, 130
    cols = 5
    sheet = Image.new("RGBA", (W * cols, (H + 40) * 4), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    used = set()
    for i, name in enumerate(picks):
        G = models[name]
        elder = name.endswith("elder")
        col = random_npc(rng, name, elder)
        gg.use(G)
        tex = gk.colorize(gg.texture(G), *col)
        gg.use_no_pack(G)
        h = heights.get(name, 1.62 if elder else (1.85 if name.startswith("man") else 1.72))
        img = g.render(tex, G.name, view_yaw=0.35, view_pitch=-0.1, scale=ppb * h / G.units, size=(W, H - 10),
                       center=(W / 2, H - 20), hidden=gg.PREVIEW_HIDDEN)
        x, y = (i % cols) * W, (i // cols) * (H + 40)
        sheet.alpha_composite(img, (x, y + 34))
        male = name.startswith("man") or name.endswith("boy")
        pool = [n for n in (NAMES_M if male else NAMES_F) if n not in used]
        nm = rng.choice(pool)
        used.add(nm)
        job = JOBS.get(name, {"child_boy": "niño", "child_girl": "niña", "teen_boy": "chavo", "teen_girl": "chava"}.get(name, ""))
        d.text((x + 8, y + 6), nm, fill=(255, 210, 120), font=font)
        d.text((x + 8, y + 22), job, fill=(190, 190, 200), font=small)
    sheet.convert("RGB").save(os.path.join(out, "vecinos_pueblo.png"))


if __name__ == "__main__":
    if "--guards" in sys.argv:
        guard_preview(sys.argv[sys.argv.index("--guards") + 1])
    elif "--export" in sys.argv:
        export()
        print("exportado")
    else:
        main(sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/town")
        hair_preview(sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/town")
