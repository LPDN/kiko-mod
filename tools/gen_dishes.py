"""
Texturas (16x16) y modelos de los platillos de cocina.
Uso   python tools/gen_dishes.py [carpeta_preview]
"""
import json
import math
import os
import sys

from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "kiko")

OUT = (40, 26, 18)
BOWL, BOWL_L, BOWL_D = (150, 98, 56), (186, 128, 76), (104, 66, 36)
PLATE, PLATE_D = (238, 238, 232), (190, 190, 186)


def blank():
    return Image.new("RGBA", (16, 16), (0, 0, 0, 0))


def px(img, x, y, c):
    if 0 <= x < 16 and 0 <= y < 16:
        img.putpixel((x, y), c + (255,) if len(c) == 3 else c)


def bowl(img, soup, soup_l):
    """Plato hondo de madera y la superficie del caldo."""
    # Superficie (elipse) filas 6 a 9
    for y in range(6, 10):
        half = {6: 4, 7: 6, 8: 6, 9: 5}[y]
        for x in range(8 - half, 8 + half):
            px(img, x, y, soup_l if y == 6 or (y == 7 and x < 8) else soup)
    # Borde
    for x in range(2, 14):
        px(img, x, 10, BOWL_L)
    px(img, 1, 9, BOWL_D); px(img, 14, 9, BOWL_D)
    px(img, 1, 8, OUT); px(img, 14, 8, OUT)
    for x in range(3, 13):
        px(img, x, 5, OUT)
    px(img, 2, 6, OUT); px(img, 13, 6, OUT); px(img, 1, 7, OUT); px(img, 14, 7, OUT)
    # Cuerpo del plato
    for y, (a, b) in {11: (2, 14), 12: (3, 13), 13: (4, 12)}.items():
        for x in range(a, b):
            px(img, x, y, BOWL if x > a + 1 else BOWL_L)
        px(img, a - 1, y, OUT); px(img, b, y, OUT)
    for x in range(5, 11):
        px(img, x, 14, BOWL_D)
    for x in range(4, 12):
        px(img, x, 15, OUT)
    px(img, 1, 10, OUT); px(img, 14, 10, OUT)


def guisado():
    img = blank()
    bowl(img, (110, 58, 30), (140, 80, 44))
    for x, y in ((5, 7), (9, 8), (11, 7)):
        px(img, x, y, (232, 120, 34))          # zanahoria
    for x, y in ((7, 8), (8, 8), (4, 8), (10, 6)):
        px(img, x, y, (86, 40, 26))            # carne
    px(img, 6, 6, (110, 160, 60))
    return img


def caldo():
    img = blank()
    bowl(img, (222, 172, 70), (246, 206, 110))
    for x, y in ((6, 7), (7, 7), (7, 8), (6, 8)):
        px(img, x, y, (236, 210, 170))         # pieza de pollo
    px(img, 5, 7, (200, 160, 110))
    for x, y in ((10, 7), (11, 8), (4, 8)):
        px(img, x, y, (90, 160, 60))           # cilantro
    px(img, 9, 8, (232, 120, 34))
    return img


def ramen():
    img = blank()
    bowl(img, (236, 200, 150), (250, 226, 186))
    for x in range(4, 12):
        if x % 2 == 0:
            px(img, x, 8, (250, 236, 170))
            px(img, x + 1, 7, (250, 236, 170))
    px(img, 9, 7, (250, 250, 250)); px(img, 10, 7, (250, 250, 250)); px(img, 10, 8, (244, 180, 40))   # huevo
    for x, y in ((5, 6), (6, 7), (11, 8)):
        px(img, x, y, (90, 170, 70))           # cebollín
    # Palillos
    for i in range(6):
        px(img, 10 + i // 2, 5 - i, (200, 150, 90))
        px(img, 12 + i // 2, 5 - i, (170, 120, 70))
    return img


def pure():
    img = blank()
    bowl(img, (240, 220, 150), (250, 236, 180))
    for x in range(5, 11):
        px(img, x, 5, (250, 236, 180))
    for x in range(6, 10):
        px(img, x, 4, (252, 242, 200))
    for x in range(5, 11):
        px(img, x, 3 if 6 <= x <= 9 else 4, OUT) if x in (5, 10) else None
    px(img, 7, 4, (255, 220, 70)); px(img, 8, 4, (255, 220, 70))    # mantequilla
    px(img, 7, 3, (230, 190, 50))
    px(img, 4, 5, OUT); px(img, 11, 5, OUT)
    for x in range(6, 10):
        px(img, x, 3, OUT) if x not in (7,) else None
    return img


def crema():
    img = blank()
    bowl(img, (234, 124, 40), (248, 156, 70))
    for x, y in ((6, 7), (7, 7), (8, 8), (9, 8), (10, 7)):
        px(img, x, y, (250, 240, 226))         # chorrito de crema
    px(img, 5, 8, (110, 170, 60))
    return img


def huevos():
    img = blank()
    # Plato
    for y in range(6, 14):
        half = int(round(7 * math.sqrt(max(0, 1 - ((y - 9.8) / 4.3) ** 2))))
        for x in range(8 - half, 8 + half):
            px(img, x, y, PLATE if y < 12 else PLATE_D)
        if half:
            px(img, 8 - half - 1, y, OUT); px(img, 8 + half, y, OUT)
    for x in range(3, 13):
        px(img, x, 14, OUT)
    for x in range(4, 12):
        px(img, x, 5, OUT)
    # Tortilla y salsa
    for y in range(7, 12):
        for x in range(3, 13):
            if (x - 8) ** 2 / 25 + (y - 9.2) ** 2 / 6 < 1:
                px(img, x, y, (224, 190, 120))
    for x, y in ((4, 9), (5, 10), (11, 9), (10, 10), (8, 11), (7, 7)):
        px(img, x, y, (196, 46, 40))
    # Dos huevos
    for cx, cy in ((6, 8), (10, 9)):
        for dx in range(-1, 2):
            for dy in range(-1, 2):
                px(img, cx + dx, cy + dy, (252, 252, 248))
        px(img, cx, cy, (250, 180, 30))
    px(img, 9, 7, (90, 160, 60))
    return img


def concha():
    img = blank()
    cx, cy = 7.5, 8.5
    for y in range(16):
        for x in range(16):
            d = ((x - cx) / 6.5) ** 2 + ((y - cy) / 5.5) ** 2
            if d < 1:
                top = y < 9.5
                col = (250, 214, 226) if top else (214, 150, 80)
                if top and ((x + y) % 3 == 0 or (x - y) % 3 == 0):
                    col = (232, 170, 192)
                if not top and y == 10:
                    col = (236, 180, 110)
                px(img, x, y, col)
            elif d < 1.28:
                px(img, x, y, OUT)
    return img


def pay():
    img = blank()
    # Molde
    for y in range(6, 14):
        half = int(round(7 * math.sqrt(max(0, 1 - ((y - 9.5) / 4.0) ** 2))))
        for x in range(8 - half, 8 + half):
            px(img, x, y, (226, 168, 84))
        if half:
            px(img, 8 - half - 1, y, OUT); px(img, 8 + half, y, OUT)
    for x in range(3, 13):
        px(img, x, 14, OUT); px(img, x, 5, OUT)
    # Relleno rojo y enrejado
    for y in range(7, 13):
        for x in range(3, 13):
            if (x - 8) ** 2 / 20 + (y - 9.6) ** 2 / 6 < 1:
                px(img, x, y, (196, 40, 60) if (x + y) % 2 else (170, 28, 48))
                if x % 3 == 0 or y % 3 == 0:
                    px(img, x, y, (244, 196, 110))
    for x in range(3, 13):
        px(img, x, 13, (176, 116, 50))
    return img


DISHES = {"guisado": guisado, "caldo_de_pollo": caldo, "ramen": ramen, "pure_de_papa": pure,
          "crema_de_zanahoria": crema, "huevos_rancheros": huevos, "concha": concha, "pay_de_frutas": pay}


def main(preview=None):
    tex_dir = os.path.join(ASSETS, "textures", "item")
    model_dir = os.path.join(ASSETS, "models", "item")
    imgs = []
    for name, fn in DISHES.items():
        img = fn()
        img.save(os.path.join(tex_dir, f"{name}.png"))
        with open(os.path.join(model_dir, f"{name}.json"), "w") as f:
            json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": f"kiko:item/{name}"}}, f, indent=2)
        imgs.append(img)
    for egg in ("rina_spawn_egg", "tami_spawn_egg"):
        with open(os.path.join(model_dir, f"{egg}.json"), "w") as f:
            json.dump({"parent": "minecraft:item/template_spawn_egg"}, f, indent=2)
    if preview:
        os.makedirs(preview, exist_ok=True)
        sheet = Image.new("RGBA", (len(imgs) * 18 * 8, 18 * 8), (60, 62, 74, 255))
        for i, im in enumerate(imgs):
            sheet.alpha_composite(im.resize((128, 128), Image.NEAREST), (i * 144 + 8, 8))
        sheet.save(os.path.join(preview, "platillos.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
