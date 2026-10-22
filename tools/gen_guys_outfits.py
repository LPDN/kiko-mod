"""
Ropa de los chicos (v3.3). Trabajo (gen_guys), casual y boda (aquí), verano e invierno (gen_seasons).
También exporta todo al mod
  - client/model/GuyModels.java       los 6 chicos x 5 ropas
  - client/model/SeasonModels.java    las 5 chicas en verano e invierno
  - textures/entity/<nombre>[_ropa].png

Uso   python tools/gen_guys_outfits.py carpeta_preview [--export]
"""
import math
import os
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_assets as g  # noqa: E402
import gen_girls as gg  # noqa: E402
import gen_kids as gk  # noqa: E402
import gen_guys as gy  # noqa: E402
import gen_seasons as gs  # noqa: E402

c = g.c
WHITE = gk.WHITE
C1, C1_S, C1_D = gk.C1, gk.C1_S, gk.C1_D
SK, SK_S = gk.SK, gk.SK_S
pattern, solid = gg.pattern, gg.solid

#           casual: playera          pantalón           zapatos            boda: saco          pantalón            zapatos           corbata
OUTFIT_COLORS = {
    "tono": (((240, 236, 226), (70, 96, 150), (110, 70, 40)), ((32, 32, 36), (32, 32, 36), (40, 30, 24), (196, 40, 48))),
    "leo": (((130, 200, 230), (226, 214, 180), (240, 240, 240)), ((240, 240, 236), (240, 240, 236), (200, 160, 110), (110, 180, 230))),
    "dante": (((40, 40, 46), (50, 60, 90), (60, 50, 44)), ((36, 44, 80), (36, 44, 80), (30, 30, 34), (150, 34, 44))),
    "ivan": (((220, 120, 50), (100, 100, 108), (60, 52, 48)), ((110, 112, 120), (110, 112, 120), (30, 30, 34), (230, 140, 50))),
    "emi": (((240, 240, 236), (40, 40, 46), (250, 250, 250)), ((190, 170, 230), (190, 170, 230), (250, 250, 250), (40, 40, 46))),
    "beto": (((70, 150, 100), (200, 184, 150), (90, 60, 40)), ((110, 30, 48), (110, 30, 48), (30, 30, 34), (230, 196, 80))),
}

GOLD, SILVER = c(230, 196, 90), c(214, 218, 224)


def keep_extras(G, name, base):
    """Lo que es parte de él y no de la ropa (el arete de Emi; el sombrero de Toño lo pone cada ropa)."""
    if name == "emi":
        G.part("guy_hat", "head", (0, 50, 0), cubes=[G.cube("earring", (5.1, 53.5, -0.5), (1, 1, 1), 0, pattern(GOLD))])


# ------------------------------------------------------------------
# Casual
# ------------------------------------------------------------------
def emblem(name):
    """Un dibujito en la playera de cada quien."""
    if name == "emi":   # estrellita rosa
        return lambda x, y: c(240, 120, 160) if (x, y) in {(4, 3), (5, 3), (3, 4), (4, 4), (5, 4), (6, 4), (4, 5), (5, 5), (4, 2), (5, 2)} else None
    if name == "leo":   # anclita
        return lambda x, y: c(40, 54, 96) if (x in (4, 5) and 2 <= y <= 6) or (y == 6 and 3 <= x <= 6) or (y == 3 and 3 <= x <= 6) else None
    if name == "beto":  # botoncitos de polo
        return lambda x, y: C1_D if (x in (4, 5) and y <= 2) or (y == 0 and 3 <= x <= 6) else None
    if name == "ivan":  # rayas del suéter
        return lambda x, y: C1_D if y in (2, 3) else None
    return lambda x, y: None


def guy_casual(name, fn):
    G, _ = fn()
    G = gs.strip_guy(G)
    em = emblem(name)
    tee_front = lambda x, y, w, h: (em(x, y) if h == 10 else None) or C1
    shirt = {"*": C1, "front": tee_front, "right": C1_S, "left": C1_S, "top": C1, "bottom": C1_S}
    jeans = lambda x, y, w, h: gk.C2_S if x in (0, w - 1) else gk.C2
    feet = gs.guy_body(G, shirt,
                       pattern(g.rows((4, g.shade_sides(C1, C1_S)), (10, g.shade_sides(SK, SK_S)))),
                       pattern(g.shade_sides(SK, SK_S)),
                       pattern(jeans), pattern(g.rows((10, jeans), (12, g.shade_sides(gk.C2_D, gk.C2_D)))),
                       pattern(g.rows((0, gk.SH), (1, gk.SH_D))))
    keep_extras(G, name, fn)
    if name == "tono":      # el sombrero no se lo quita
        BROWN, BROWN_D, BAND = c(150, 104, 62), c(118, 80, 46), c(70, 46, 30)
        G.part("guy_hat", "head", (0, 50, 0), cubes=[
            G.cube("hat_brim", (-8, 60.2, -8), (16, 1, 16), 0, {"*": BROWN_D, "top": BROWN}),
            G.cube("hat_crown", (-4.5, 61.2, -4.5), (9, 4, 9), 0.1, pattern(g.rows((0, BAND), (3, BROWN)))),
            G.cube("hat_dent", (-3.5, 64.8, -2), (7, 1, 4), 0, pattern(BROWN_D))])
    return G, feet


# ------------------------------------------------------------------
# Boda (traje; Toño de charro)
# ------------------------------------------------------------------
def guy_wedding(name, fn):
    G, _ = fn()
    G = gs.strip_guy(G)
    tie = c(*OUTFIT_COLORS[name][1][3])
    J, J_S, J_D = C1, C1_S, C1_D
    trim = SILVER if name == "tono" else (GOLD if name in ("dante", "beto") else J_D)

    def chest_front(x, y, w, h):
        if h == 10:
            if 3 <= x <= 6:                             # camisa blanca y corbata (o moño)
                if name == "tono":
                    return tie if y in (0, 1) and 3 <= x <= 6 else WHITE
                return tie if x in (4, 5) and y >= 1 else WHITE
            if x in (2, 7):
                return J_D                              # solapas
            if name == "tono" and x in (1, 8) and y % 3 == 1:
                return trim                             # botonadura de plata del charro
            return J
        # cintura: saco cerrado con botones
        if x == w // 2 and y in (1, 3):
            return trim if name != "tono" else J_D
        return J
    shirt = {"*": J, "front": chest_front, "right": J_S, "left": J_S, "top": J, "bottom": J_S}
    feet = gs.guy_body(G, shirt,
                       solid(J, J_S),
                       pattern(g.rows((7, g.shade_sides(J, J_S)), (8, WHITE), (9, SK))),
                       pattern(lambda x, y, w, h: gk.C2_S if x in (0, w - 1) else gk.C2),
                       pattern(g.shade_sides(gk.C2, gk.C2_S)),
                       pattern(g.rows((0, gk.SH), (1, gk.SH_D))))
    body = next(p for p in G.parts if p["name"] == "body")
    # Flor en la solapa
    body["cubes"].append(G.cube("boutonniere", (2.6, 44.5, -3.2), (1, 1, 1), 0.15, pattern(c(255, 250, 250))))
    keep_extras(G, name, fn)
    if name == "tono":
        BLK, BLK_D = c(30, 30, 34), c(20, 20, 24)
        G.part("guy_hat", "head", (0, 50, 0), cubes=[
            G.cube("hat_brim", (-8.5, 60.2, -8.5), (17, 1, 17), 0, {"*": BLK_D, "top": lambda x, y, w, h: SILVER if (x in (1, w - 2) or y in (1, h - 2)) else BLK}),
            G.cube("hat_crown", (-4.5, 61.2, -4.5), (9, 5, 9), 0.1, pattern(g.rows((0, SILVER), (1, BLK)))),
        ])
    return G, feet


# ------------------------------------------------------------------
# Todo junto
# ------------------------------------------------------------------
OUTFITS = ["", "_casual", "_wedding", "_summer", "_winter"]
LABELS = ["trabajo", "casual", "boda", "verano", "invierno"]


def build_guy_outfits(name, fn, h, skin, hair, eye, c1, c2, shoe):
    """Regresa [(G, textura)] en el orden de OUTFITS."""
    cas, wed = OUTFIT_COLORS[name]
    sc = gs.GUY_SEASON[name]
    builds = [
        (fn(), (c1, c2, shoe)),
        (guy_casual(name, fn), cas),
        (guy_wedding(name, fn), wed[:3]),
        (gs.guy_summer(fn), (sc[0], sc[1], (150, 100, 60))),
        (gs.guy_winter(fn), (sc[2], sc[4], sc[3])),
    ]
    out = []
    for i, ((G, feet), (a, b, s)) in enumerate(builds):
        if name == "emi" and i >= 3:
            keep_extras(G, name, fn) if not any(p["name"] == "guy_hat" for p in G.parts) else None
        G = gk.finish(G, feet)
        G.name, G.height = name + OUTFITS[i], h
        gg.use(G)
        tex = gk.colorize(gg.texture(G), skin, hair, eye, a, b, s)
        out.append((G, tex))
    return out


def all_guys():
    return {row[0]: build_guy_outfits(*row) for row in gy.GUYS}


def girl_seasons():
    gg.BASE_BUILDERS.update(maru=gg.maru, lumi=gg.lumi, nadi=gg.nadi, rina=gg.rina, tami=gg.tami)
    out = {}
    for name in ["maru", "lumi", "nadi", "rina", "tami"]:
        res = []
        for suf, fn in (("_summer", gs.girl_summer), ("_winter", gs.girl_winter)):
            G = gg.complete(fn(name))
            G.name = name + suf
            G.height = gg.Girl(name).height if hasattr(gg.Girl(name), "height") else 1.8
            gg.use(G)
            res.append((G, gg.texture(G)))
        out[name] = res
    return out


def preview(guys, out):
    os.makedirs(out, exist_ok=True)
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 15)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 12)
    ppb, W, H = 120, 170, 270
    # Casual y boda (de frente y de espaldas)
    sheet = Image.new("RGBA", (W * 4 * 2 + 20, (H + 26) * 3), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    for i, row in enumerate(gy.GUYS):
        name = row[0]
        looks = guys[name]
        bx, by = (i % 2) * (W * 4 + 20), (i // 2) * (H + 26)
        d.text((bx + 8, by + 4), gy.INFO[name][0], fill=(255, 210, 120), font=font)
        for j, (idx, yaw) in enumerate(((1, 0.35), (1, math.pi + 0.4), (2, 0.35), (2, math.pi + 0.4))):
            G, tex = looks[idx]
            sheet.alpha_composite(gs.render_one(G, tex, ppb, W, H, yaw), (bx + j * W, by + 22))
            if yaw < 1:
                d.text((bx + j * W + W // 2 - 20, by + 22), LABELS[idx], fill=(200, 200, 210), font=small)
    sheet.convert("RGB").save(os.path.join(out, "chicos_casual_boda.png"))


def export(guys, girls):
    tex_dir = os.path.join(g.ASSETS, "textures", "entity")
    header = """package com.jaciel.kiko.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

"""
    def method(G):
        body = g.gen_java_body(G.parts, {p["name"]: p for p in G.parts})
        return f"""    /** {G.name} ({G.height} bloques, {getattr(G, 'units', 61.75):.2f} unidades). */
    public static TexturedModelData {G.name}() {{
        ModelData data = new ModelData();
{body}
        return TexturedModelData.of(data, {g.TEX}, {g.TEX});
    }}
"""
    model_dir = os.path.dirname(g.MODEL_JAVA)
    # Chicos
    methods, units = [], []
    for name, looks in guys.items():
        for G, tex in looks:
            tex.save(os.path.join(tex_dir, f"{G.name}.png"))
            methods.append(method(G))
        units.append(f'        "{name}", {looks[0][0].units:.3f}f')
    java = header + """/**
 * Los 6 chicos (v3.3) con sus 5 ropas (trabajo, casual, boda, verano, invierno).
 * Los genera tools/gen_guys_outfits.py (no editar a mano). Usan la misma clase KikoModel que las chicas.
 */
@SuppressWarnings("unused")
public final class GuyModels {
    private GuyModels() {}

    // La cabeza de todos llega a 61.75 unidades (igual que las chicas), así que KikoRenderer los escala igual.

""" + "\n".join(methods) + "}\n"
    open(os.path.join(model_dir, "GuyModels.java"), "w").write(java)
    # Chicas en verano e invierno
    methods = []
    for name, looks in girls.items():
        for G, tex in looks:
            tex.save(os.path.join(tex_dir, f"{G.name}.png"))
            methods.append(method(G))
    java = header + """/**
 * Ropa de verano e invierno de las chicas (v3.3). La genera tools/gen_guys_outfits.py (no editar a mano).
 */
@SuppressWarnings("unused")
public final class SeasonModels {
    private SeasonModels() {}

""" + "\n".join(methods) + "}\n"
    open(os.path.join(model_dir, "SeasonModels.java"), "w").write(java)


if __name__ == "__main__":
    guys = all_guys()
    preview(guys, sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/guys")
    if "--export" in sys.argv:
        export(guys, girl_seasons())
        print("exportado")
