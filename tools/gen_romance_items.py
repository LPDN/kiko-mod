"""
Íconos (16x16) y modelos del ramo, el anillo y el Anillo del Abismo (v3.0).
Uso   python tools/gen_romance_items.py [carpeta_preview]
"""
import json
import os
import sys

from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "kiko")
OUT = (44, 28, 30)


def img():
    return Image.new("RGBA", (16, 16), (0, 0, 0, 0))


def put(im, x, y, c):
    if 0 <= x < 16 and 0 <= y < 16:
        im.putpixel((x, y), c + (255,))


def bouquet():
    im = img()
    # Papel en cono
    for y in range(8, 16):
        half = max(0, (15 - y) // 2 + 1)
        for x in range(8 - half, 8 + half):
            put(im, x, y, (246, 214, 226) if (x + y) % 3 else (232, 190, 206))
        put(im, 8 - half - 1, y, OUT)
        put(im, 8 + half, y, OUT)
    # Listón
    for x in range(5, 11):
        put(im, x, 11, (214, 60, 96))
    put(im, 4, 12, (214, 60, 96)); put(im, 11, 12, (214, 60, 96))
    # Hojitas y flores
    for x, y in ((3, 7), (12, 7), (2, 6), (13, 5)):
        put(im, x, y, (90, 160, 70))
    flowers = [(5, 4, (230, 60, 80)), (9, 3, (255, 214, 80)), (11, 6, (190, 120, 230)), (3, 6, (255, 255, 255)),
               (7, 6, (255, 140, 170)), (7, 2, (120, 170, 240)), (11, 2, (255, 160, 60)), (3, 2, (240, 110, 160)),
               (9, 6, (255, 255, 255)), (5, 6, (255, 214, 80))]
    for fx, fy, col in flowers:
        for dx, dy in ((0, 0), (1, 0), (0, 1), (1, 1)):
            put(im, fx + dx, fy + dy, col)
        put(im, fx, fy, tuple(min(255, v + 40) for v in col))
        put(im, fx + 1, fy + 1, tuple(int(v * 0.8) for v in col))
    return im


def ring(band, band_l, band_d, gem, gem_l, gem_d):
    im = img()
    # Aro
    cx, cy = 7.5, 10.0
    for y in range(16):
        for x in range(16):
            d = ((x - cx) / 5.2) ** 2 + ((y - cy) / 4.4) ** 2
            if 0.45 < d < 1.0:
                col = band_l if (y < cy and x < cx) else (band_d if y > cy + 1 else band)
                put(im, x, y, col)
            elif 1.0 <= d < 1.35 and y > 5:
                put(im, x, y, OUT)
    # Piedra arriba
    for y in range(1, 6):
        for x in range(5, 11):
            if abs(x - 7.5) + abs(y - 3.2) < 3.3:
                put(im, x, y, gem_l if (x < 8 and y < 3) else (gem_d if y >= 4 else gem))
    put(im, 6, 2, (255, 255, 255))
    for x in (6, 9):
        put(im, x, 5, band_d)
    return im


def family_book():
    """Libro de familia: pasta rosa con un corazón."""
    im = img()
    for y in range(2, 15):
        for x in range(3, 13):
            col = (226, 120, 150) if x > 3 else (180, 80, 110)
            if y in (2, 14) or x == 12:
                col = (150, 60, 90)
            put(im, x, y, col)
    for y in range(3, 14):
        put(im, 12, y, (246, 238, 220))      # hojas
    for x, y in ((6, 6), (7, 6), (9, 6), (10, 6), (5, 7), (6, 7), (7, 7), (8, 7), (9, 7), (10, 7), (11, 7),
                 (6, 8), (7, 8), (8, 8), (9, 8), (10, 8), (7, 9), (8, 9), (9, 9), (8, 10)):
        put(im, x - 1, y, (255, 240, 120))
    for y in range(2, 15):
        put(im, 2, y, OUT)
        put(im, 13, y, OUT)
    for x in range(2, 14):
        put(im, x, 1, OUT)
        put(im, x, 15, OUT)
    return im


def main(preview=None):
    icons = {
        "bouquet": bouquet(),
        "family_book": family_book(),
        "ring": ring((236, 190, 70), (255, 230, 140), (186, 136, 40), (180, 236, 250), (240, 252, 255), (110, 190, 220)),
        "abyss_ring": ring((52, 74, 96), (96, 150, 170), (28, 40, 54), (40, 200, 210), (170, 255, 250), (10, 110, 130)),
    }
    for n, im in icons.items():
        im.save(os.path.join(ASSETS, "textures", "item", f"{n}.png"))
        with open(os.path.join(ASSETS, "models", "item", f"{n}.json"), "w") as f:
            json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": f"kiko:item/{n}"}}, f, indent=2)
    if preview:
        os.makedirs(preview, exist_ok=True)
        sheet = Image.new("RGBA", (len(icons) * 144, 144), (60, 62, 74, 255))
        for i, im in enumerate(icons.values()):
            sheet.alpha_composite(im.resize((128, 128), Image.NEAREST), (i * 144 + 8, 8))
        sheet.save(os.path.join(preview, "anillos.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
