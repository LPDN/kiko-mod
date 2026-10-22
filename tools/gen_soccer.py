"""Pelota de futbol (v3.2.1): textura de la entidad (cubo 8x8x8 en 32x16) e ícono del ítem."""
import os, json
from PIL import Image

ROOT = os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources", "assets", "kiko")
W = (245, 245, 240, 255); W2 = (220, 220, 214, 255); B = (30, 30, 34, 255); B2 = (60, 60, 66, 255)

# Cara de 8x8, pentágono negro al centro y esquinitas negras (así al juntar caras se ve como balón)
FACE = [
    "B..WW..B",
    "..WWWW..",
    "WWWBBWWW",
    "WWBBBBWW",
    "WWBBBBWW",
    "WWWBBWWW",
    "..WWWW..",
    "B..WW..B",
]

def face(img, ox, oy, shade=False):
    for y, row in enumerate(FACE):
        for x, c in enumerate(row):
            if c == "B":
                col = B2 if (x + y) % 3 == 0 else B
            elif c == ".":
                col = W2
            else:
                col = W2 if shade and y > 5 else W
            img.putpixel((ox + x, oy + y), col)

def entity():
    img = Image.new("RGBA", (32, 16), (0, 0, 0, 0))
    face(img, 8, 0)            # arriba
    face(img, 16, 0, True)     # abajo
    for ox in (0, 8, 16, 24):  # lados
        face(img, ox, 8, True)
    p = os.path.join(ROOT, "textures", "entity", "soccer_ball.png")
    os.makedirs(os.path.dirname(p), exist_ok=True)
    img.save(p)
    return img

def item():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    cx, cy, r = 7.5, 7.5, 6.6
    patches = [(7.5, 7.5, 2.2), (3.4, 5.0, 1.4), (11.6, 5.0, 1.4), (4.4, 11.8, 1.4), (10.6, 11.8, 1.4), (7.5, 2.0, 1.0)]
    for y in range(16):
        for x in range(16):
            d = ((x - cx) ** 2 + (y - cy) ** 2) ** 0.5
            if d > r + 0.4:
                continue
            col = W
            for px, py, pr in patches:
                if ((x - px) ** 2 + (y - py) ** 2) ** 0.5 <= pr:
                    col = B
            # sombra abajo a la derecha y contorno
            if col == W and (x - cx) + (y - cy) > 4.5:
                col = W2
            if d > r - 0.6:
                col = (70, 70, 76, 255)
            img.putpixel((x, y), col)
    p = os.path.join(ROOT, "textures", "item", "soccer_ball.png")
    img.save(p)
    m = os.path.join(ROOT, "models", "item", "soccer_ball.json")
    json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "kiko:item/soccer_ball"}}, open(m, "w"), indent=2)
    return img

if __name__ == "__main__":
    a = entity(); b = item()
    prev = Image.new("RGBA", (32 * 8 + 16 * 8 + 16, 16 * 8), (40, 44, 52, 255))
    prev.paste(a.resize((256, 128), Image.NEAREST), (0, 0))
    prev.paste(b.resize((128, 128), Image.NEAREST), (272, 0), b.resize((128, 128), Image.NEAREST))
    prev.save("/tmp/claude-0/soccer_preview.png")
    print("ok")
