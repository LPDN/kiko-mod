"""
Ropa de verano y de invierno para todos (como la de Kiko), por ahora solo vistas previas.
  Chicas  verano = vestido ligero con sombrero de paja · invierno = abrigo largo, bufanda y gorro
  Chicos  verano = camisa floreada, shorts y huaraches · invierno = chamarra acolchada, bufanda y gorro

Uso   python tools/gen_seasons.py carpeta_preview
"""
import math
import os
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_girls as gg  # noqa: E402
import gen_kids as gk  # noqa: E402
import gen_guys as gy  # noqa: E402
import gen_assets as g  # noqa: E402

c = g.c
tone = g.tone
CLEAR = None
STRAW, STRAW_D = c(240, 218, 150), c(206, 180, 112)

# ------------------------------------------------------------------
# Chicas
# ------------------------------------------------------------------
#        vestido de verano     listón del sombrero   abrigo de invierno    bufanda / gorro
GIRL_SEASON = {
    "maru": ((226, 96, 74), (250, 236, 200), (118, 82, 56), (232, 120, 48)),
    "lumi": ((204, 186, 238), (150, 200, 236), (238, 228, 208), (184, 162, 222)),
    "nadi": ((150, 206, 168), (206, 64, 54), (76, 112, 72), (206, 64, 54)),
    "rina": ((176, 214, 240), (250, 250, 250), (180, 194, 214), (132, 206, 196)),
    "tami": ((250, 244, 236), (214, 52, 52), (190, 50, 50), (250, 250, 250)),
}


def sun_hat(G, ribbon):
    hb = 50
    G.part("sun_hat", "head", (0, hb, 0), cubes=[
        G.cube("sun_brim", (-8, hb + 10.4, -8), (16, 1, 16), 0, {"*": STRAW_D, "top": lambda x, y, w, h: STRAW_D if (x + y) % 4 == 0 else STRAW}),
        G.cube("sun_crown", (-4.5, hb + 11.4, -4.5), (9, 3, 9), 0.1, gg.pattern(g.rows((0, ribbon), (2, STRAW)))),
        G.cube("sun_flower", (3.5, hb + 11.4, -5.2), (2, 2, 1), 0, gg.pattern(c(255, 250, 250))),
    ])


def beanie(G, col, pom=None):
    hb = 50
    G.part("winter_hat", "head", (0, hb, 0), cubes=[
        G.cube("w_beanie", (-5.5, hb + 8.4, -5.5), (11, 4, 11), 0.55,
               {"*": lambda x, y, w, h: tone(col, 0.85) if y == 3 else (col if x % 2 else tone(col, 0.92)), "top": col}),
        G.cube("w_pom", (-1, hb + 12.8, -1), (2, 2, 2), 0.3, gg.pattern(pom or c(250, 250, 250))),
    ])


def scarf(G, col):
    stripe = lambda x, y, w, h: c(250, 250, 250) if (x + y) % 4 == 0 else col
    return [G.cube("w_scarf", (-3.2, 46.2, -3.2), (6, 2, 6), 0.5, gg.pattern(stripe)),
            G.cube("w_scarf_tail", (1.2, 39, -4.2), (2, 7, 1), 0.2, gg.pattern(stripe))]


def girl_summer(name):
    dress, ribbon, _, _ = [c(*v) for v in GIRL_SEASON[name]]
    G = gg.undress(name, "summer")
    sk = gg.PROPS[name][0]
    D, D_S, D_D = dress, tone(dress, 0.88), tone(dress, 0.76)
    dots = (lambda x, y, w, h: ribbon if (x + 2 * y) % 5 == 0 else None) if name == "tami" else (lambda x, y, w, h: None)
    G.part("body", "root", (0, 48, 0), cubes=[gg.neck(G, sk),
                                             G.cube("strap_r", (-3.5, 45, -2.9), (1, 3, 1), 0, gg.pattern(D_S)),
                                             G.cube("strap_l", (2.5, 45, -2.9), (1, 3, 1), 0, gg.pattern(D_S)),
                                             G.cube("sash", (-4.3, 33.4, -2.5), (9, 1, 5), 0.6, gg.pattern(ribbon))])
    gg.curves(G, D, D, center=lambda x, y, w, h: sk if (h == 9 and y <= 1) else dots(x, y, w, h), **gg.PROPS[name][5])
    gg.skirt_part(G, "season_skirt", 32, 17, D, D_S, D_D, pleats=4, wide=12, deep=8, dil=0.5,
                  painter=lambda x, y, w, h: dots(x, y, w, h) or (D_D if y == h - 1 else (D_S if x % 4 == 0 else D)))
    gg.arms(G, name, (gg.pattern(g.shade_sides(sk, tone(sk, 0.92))), 0.4), (gg.pattern(g.shade_sides(sk, tone(sk, 0.92))), 0.3))
    gg.legs(G, name, (gg.pattern(g.shade_sides(sk, tone(sk, 0.93))), 0.6), (gg.pattern(g.shade_sides(sk, tone(sk, 0.93))), 0.45),
            (gg.pattern(g.rows((0, c(176, 120, 74)), (1, c(140, 90, 54)))), 0.3))
    sun_hat(G, ribbon)
    return G


def girl_winter(name):
    _, _, coat, acc = [c(*v) for v in GIRL_SEASON[name]]
    G = gg.undress(name, "winter")
    G.parts = [p for p in G.parts if p["name"] not in ("spikes",)]
    sk = gg.PROPS[name][0]
    C, C_S, C_D = coat, tone(coat, 0.86), tone(coat, 0.72)
    BOOT, BOOT_D = c(96, 64, 44), c(70, 46, 30)
    TIGHT = c(60, 54, 66)
    buttons = lambda x, y, w, h: C_D if (h == 9 and x in (w // 2 - 2, w // 2 + 1) and y in (2, 5, 8)) else None
    G.part("body", "root", (0, 48, 0), cubes=[gg.neck(G, sk)] + scarf(G, acc) + [
        G.cube("coat_belt", (-4.4, 33.4, -2.6), (9, 1, 5), 0.7, gg.pattern(C_D))])
    gg.curves(G, C, C, center=buttons, **gg.PROPS[name][5])
    gg.skirt_part(G, "season_skirt", 33, 16, C, C_S, C_D, pleats=5, wide=11, deep=7, dil=0.55,
                  painter=lambda x, y, w, h: C_D if (y == h - 1 or x == w // 2) else (C_S if x % 5 == 0 else C))
    gg.arms(G, name, (gg.solid(C, C_S), 0.6), (gg.pattern(g.rows((6, g.shade_sides(C, C_S)), (7, C_D), (9, acc))), 0.45))
    gg.legs(G, name, (gg.pattern(g.shade_sides(TIGHT, tone(TIGHT, 0.85))), 0.6),
            (gg.pattern(g.rows((3, g.shade_sides(TIGHT, tone(TIGHT, 0.85))), (4, c(240, 236, 226)), (12, g.shade_sides(BOOT, BOOT_D)))), 0.55),
            (gg.pattern(g.rows((0, BOOT), (1, BOOT_D))), 0.4))
    beanie(G, acc)
    return G


# ------------------------------------------------------------------
# Chicos
# ------------------------------------------------------------------
#        camisa de verano     shorts               chamarra de invierno   bufanda / gorro      pantalón de invierno
GUY_SEASON = {
    "tono": ((240, 140, 60), (200, 186, 150), (120, 84, 56), (196, 50, 50), (64, 88, 136)),
    "leo": ((80, 190, 210), (240, 236, 220), (40, 54, 96), (244, 196, 40), (90, 90, 100)),
    "dante": ((240, 240, 236), (60, 60, 70), (60, 62, 72), (150, 34, 44), (40, 40, 46)),
    "ivan": ((90, 170, 110), (120, 110, 90), (220, 120, 50), (60, 60, 66), (70, 70, 80)),
    "emi": ((240, 150, 180), (40, 40, 46), (40, 40, 46), (240, 150, 180), (70, 74, 90)),
    "beto": ((240, 196, 70), (110, 64, 140), (110, 64, 140), (240, 196, 70), (90, 74, 60)),
}


def strip_guy(G):
    G.parts = [p for p in G.parts if p["name"] not in gg.CLOTHES | {"guy_hat"}]
    return G


def guy_body(G, shirt_painter, arm_upper, arm_lower, legs, shins, feet, extra=()):
    sk = gk.SK
    C1, C1_S = gk.C1, gk.C1_S
    G.part("body", "root", (0, 48, 0), cubes=[
        gk.neck(G),
        G.cube("chest", (-5, 38, -2.5), (10, 10, 5), 0.55, shirt_painter),
        G.cube("waist", (-4.5, 33, -2.25), (9, 5, 4), 0.5, shirt_painter),
        G.cube("hips", (-4.5, 28, -2.5), (9, 5, 5), 0.6, gg.solid(gk.C2, gk.C2_S)),
    ] + list(extra))
    return gk.limbs(G, 7.1, (47.5, 11, 3), 10, 2.6, (14, 4, 29), (13, 3), 2, arm_rot=0.08,
                    upper=(arm_upper, 0.65), lower=(arm_lower, 0.4), legp=(legs, 0.8), shinp=(shins, 0.6), footp=(feet, 0.4))


def guy_summer(fn):
    G, _ = fn()
    G = strip_guy(G)
    C1, C1_S, C1_D = gk.C1, gk.C1_S, gk.C1_D
    flower = lambda x, y, w, h: c(255, 250, 240) if (x * 3 + y * 5) % 13 == 0 else (C1_D if (x * 3 + y * 5) % 13 == 6 else C1)
    shirt = {"*": flower, "front": lambda x, y, w, h: C1_D if (x == w // 2 and h == 10) else flower(x, y, w, h), "right": C1_S, "left": C1_S}
    sk = gk.SK
    feet = guy_body(G, shirt, gg.pattern(g.rows((4, flower), (10, g.shade_sides(sk, gk.SK_S)))), gg.pattern(g.shade_sides(sk, gk.SK_S)),
                    gg.pattern(g.rows((6, g.shade_sides(gk.C2, gk.C2_S)), (13, g.shade_sides(sk, gk.SK_S)))),
                    gg.pattern(g.shade_sides(sk, gk.SK_S)), gg.pattern(g.rows((0, c(150, 100, 60)), (1, c(110, 72, 40)))))
    # Lentes de sol
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("shades", (-4.5, 53.5, g.EYE_Z - 0.4), (9, 2, 0), 0, {"front": lambda x, y, w, h: c(30, 30, 36) if x != 4 else c(60, 60, 66)})])
    return G, feet


def guy_winter(fn):
    G, _ = fn()
    G = strip_guy(G)
    G.parts = [p for p in G.parts if p["name"] not in ("spikes",)]
    C1, C1_S, C1_D = gk.C1, gk.C1_S, gk.C1_D
    quilt = lambda x, y, w, h: C1_D if y % 3 == 2 else C1
    shirt = {"*": quilt, "front": lambda x, y, w, h: c(200, 200, 206) if x == w // 2 else quilt(x, y, w, h), "right": quilt, "left": quilt}
    BOOT, BOOT_D = c(96, 64, 44), c(70, 46, 30)
    feet = guy_body(G, shirt, gg.pattern(quilt), gg.pattern(g.rows((6, quilt), (7, C1_D), (9, gk.SH))),
                    gg.solid(gk.C2, gk.C2_S), gg.pattern(g.rows((4, g.shade_sides(gk.C2, gk.C2_S)), (12, g.shade_sides(BOOT, BOOT_D)))),
                    gg.pattern(g.rows((0, BOOT), (1, BOOT_D))))
    body = next(p for p in G.parts if p["name"] == "body")
    body["cubes"] += [G.cube("puffer", (-5.2, 31, -2.9), (10, 2, 6), 0.8, gg.pattern(C1_D))]
    body["cubes"] += scarf(G, gk.SH)
    G.part("guy_hat", "head", (0, 50, 0), cubes=[
        G.cube("w_beanie", (-5.5, 58.4, -5.5), (11, 4, 11), 0.6, {"*": lambda x, y, w, h: gk.SH_D if y == 3 else gk.SH, "top": gk.SH}),
        G.cube("w_pom", (-1, 62.8, -1), (2, 2, 2), 0.3, gg.pattern(c(250, 250, 250)))])
    return G, feet


# ------------------------------------------------------------------
def render_one(G, tex, ppb, W, H, yaw=0.35):
    gg.use_no_pack(G)
    return g.render(tex, G.name, view_yaw=yaw, view_pitch=-0.1, scale=ppb * G.height / getattr(G, "units", 61.75),
                    size=(W, H - 10), center=(W / 2, H - 20), hidden=gg.PREVIEW_HIDDEN)


def main(out):
    os.makedirs(out, exist_ok=True)
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 15)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 12)
    gg.BASE_BUILDERS.update(maru=gg.maru, lumi=gg.lumi, nadi=gg.nadi, rina=gg.rina, tami=gg.tami)
    ppb, W, H = 120, 170, 270
    labels = ["trabajo", "verano", "invierno"]

    # Chicas
    girls = ["maru", "lumi", "nadi", "rina", "tami"]
    sheet = Image.new("RGBA", (W * 3 * 2 + 20, (H + 26) * 3), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    for i, name in enumerate(girls):
        looks = [gg.complete(gg.BASE_BUILDERS[name]()), gg.complete(girl_summer(name)), gg.complete(girl_winter(name))]
        bx, by = (i % 2) * (W * 3 + 20), (i // 2) * (H + 26)
        d.text((bx + 8, by + 4), name.capitalize(), fill=(255, 210, 120), font=font)
        for j, G in enumerate(looks):
            gg.use(G)
            tex = gg.texture(G)
            sheet.alpha_composite(render_one(G, tex, ppb, W, H), (bx + j * W, by + 22))
            d.text((bx + j * W + W // 2 - 24, by + 6), labels[j], fill=(200, 200, 210), font=small)
    sheet.convert("RGB").save(os.path.join(out, "temporadas_chicas.png"))

    # Chicos
    sheet = Image.new("RGBA", (W * 3 * 2 + 20, (H + 26) * 3), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    for i, (name, fn, h, skin, hair, eye, c1, c2, shoe) in enumerate(gy.GUYS):
        sc = GUY_SEASON[name]
        variants = [(fn(), (c1, c2, shoe)), (guy_summer(fn), (sc[0], sc[1], (150, 100, 60))), (guy_winter(fn), (sc[2], sc[4], sc[3]))]
        bx, by = (i % 2) * (W * 3 + 20), (i // 2) * (H + 26)
        d.text((bx + 8, by + 4), gy.INFO[name][0], fill=(255, 210, 120), font=font)
        for j, ((G, feet), (a, b, s)) in enumerate(variants):
            G = gk.finish(G, feet)
            G.name, G.height = name, h
            gg.use(G)
            tex = gk.colorize(gg.texture(G), skin, hair, eye, a, b, s)
            sheet.alpha_composite(render_one(G, tex, ppb, W, H), (bx + j * W, by + 22))
            d.text((bx + j * W + W // 2 - 24, by + 22), labels[j], fill=(200, 200, 210), font=small)
    sheet.convert("RGB").save(os.path.join(out, "temporadas_chicos.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/seasons")
