"""
Cama matrimonial de Kiko (Fase 4). Genera
  - blockstates/kiko_bed.json y los 4 modelos (cabecera/pies x izquierda/derecha)
  - texturas 16x16 (madera, sábana, cobija con estrellitas, almohada) e ícono del ítem
  - loot table, receta y el tag de camas
  - vista previa (si le pasas una carpeta)
Uso   python tools/gen_bed.py [carpeta_preview]
"""
import json
import math
import os
import sys

from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "kiko")
DATA = os.path.join(ROOT, "src", "main", "resources", "data")


def c(r, g, b, a=255):
    return (r, g, b, a)


WOOD, WOOD_D, WOOD_L = c(214, 150, 128), c(178, 112, 98), c(236, 184, 162)
SHEET, SHEET_S = c(250, 247, 244), c(228, 222, 220)
BLANKET, BLANKET_D, BLANKET_L = c(255, 140, 128), c(226, 104, 100), c(255, 176, 160)
STAR = c(255, 214, 80)
PILLOW, PILLOW_S, PILLOW_STRIPE = c(255, 252, 250), c(232, 226, 228), c(255, 170, 168)


# ------------------------------------------------------------------
# Texturas
# ------------------------------------------------------------------
def tex_wood():
    img = Image.new("RGBA", (16, 16), WOOD)
    px = img.load()
    for y in range(16):
        for x in range(16):
            if y % 4 == 3:
                px[x, y] = WOOD_D                     # tablas
            elif (x * 7 + y * 3) % 11 == 0:
                px[x, y] = WOOD_L                     # vetas
            elif (x + (y // 4) * 5) % 9 == 0:
                px[x, y] = WOOD_D
    return img


def tex_sheet():
    img = Image.new("RGBA", (16, 16), SHEET)
    px = img.load()
    for y in range(16):
        for x in range(16):
            if (x + y) % 7 == 0:
                px[x, y] = SHEET_S
    return img


def tex_blanket():
    img = Image.new("RGBA", (16, 16), BLANKET)
    px = img.load()
    for y in range(16):
        for x in range(16):
            if y in (0, 15):
                px[x, y] = BLANKET_D
            elif (x + y) % 6 == 0:
                px[x, y] = BLANKET_L                 # tejido
    # Estrellitas como su pasador
    for sx, sy in ((3, 4), (11, 10), (12, 3), (5, 12)):
        for dx, dy in ((0, 0), (1, 0), (-1, 0), (0, 1), (0, -1)):
            px[(sx + dx) % 16, (sy + dy) % 16] = STAR
    return img


def tex_pillow():
    img = Image.new("RGBA", (16, 16), PILLOW)
    px = img.load()
    for y in range(16):
        for x in range(16):
            if x in (0, 15) or y in (0, 15):
                px[x, y] = PILLOW_S
            elif y in (6, 9):
                px[x, y] = PILLOW_STRIPE
    return img


def icon():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.rectangle([1, 3, 2, 14], fill=WOOD_D)           # cabecera
    d.rectangle([3, 7, 14, 11], fill=SHEET)
    d.rectangle([7, 6, 14, 11], fill=BLANKET)
    d.rectangle([3, 5, 6, 7], fill=PILLOW)
    d.rectangle([3, 9, 6, 10], fill=PILLOW)
    d.point((10, 8), fill=STAR)
    d.rectangle([3, 12, 14, 12], fill=WOOD)
    d.rectangle([3, 13, 3, 14], fill=WOOD_D)
    d.rectangle([14, 13, 14, 14], fill=WOOD_D)
    return img


TEXTURES = {"wood": tex_wood, "sheet": tex_sheet, "blanket": tex_blanket, "pillow": tex_pillow}


# ------------------------------------------------------------------
# Modelos (mirando al norte: la cabecera está en z = 0)
# Para la mitad izquierda la orilla de afuera es x = 0; para la derecha, x = 16
# ------------------------------------------------------------------
def box(frm, to, tex, faces=("north", "south", "east", "west", "up", "down")):
    return dict(frm=frm, to=to, tex=tex, faces=faces)


def elements(part, side):
    # Base y colchón no tocan la cabecera ni la piecera (para que no parpadeen las caras)
    z0, z1 = (2, 16) if part == "head" else (0, 14)
    els = [
        box((0, 3, z0), (16, 6, z1), "wood"),          # base
        box((0, 6, z0), (16, 9, z1), "sheet"),         # colchón
    ]
    if part == "head":
        els += [
            box((0, 0, 0), (16, 14, 2), "wood"),       # cabecera
            box((0, 14, 0), (16, 16, 3), "wood"),      # remate
            box((3, 9, 3), (13, 11.5, 9), "pillow"),   # almohada
            box((0, 9, 9), (16, 10, 16), "blanket"),   # cobija (mitad)
            box((0, 10, 9), (16, 10.6, 10.2), "blanket"),  # doblez de la cobija
        ]
        drape_z = (9, 16)
        leg = None
    else:
        els += [
            box((0, 0, 14), (16, 11, 16), "wood"),     # piecera
            box((0, 9, 0), (16, 10, 14), "blanket"),
        ]
        drape_z = (0, 14)
    # La cobija cuelga por la orilla de afuera, y una patita en la esquina de afuera
    if side == "left":
        els.append(box((-0.6, 4, drape_z[0]), (0, 10, drape_z[1]), "blanket"))
        els.append(box((0, 0, 7 if part == "head" else 1), (2, 3, 9 if part == "head" else 3), "wood"))
    else:
        els.append(box((16, 4, drape_z[0]), (16.6, 10, drape_z[1]), "blanket"))
        els.append(box((14, 0, 7 if part == "head" else 1), (16, 3, 9 if part == "head" else 3), "wood"))
    return els


def face_uv(e, face):
    (x0, y0, z0), (x1, y1, z1) = e["frm"], e["to"]
    cl = lambda v: max(0.0, min(16.0, v))
    if face in ("north", "south"):
        u0, u1, v0, v1 = x0, x1, 16 - y1, 16 - y0
    elif face in ("east", "west"):
        u0, u1, v0, v1 = z0, z1, 16 - y1, 16 - y0
    else:
        u0, u1, v0, v1 = x0, x1, z0, z1
    u0, u1, v0, v1 = cl(u0), cl(u1), cl(v0), cl(v1)
    if u1 - u0 < 0.5:
        u1 = min(16, u0 + 1)
    if v1 - v0 < 0.5:
        v1 = min(16, v0 + 1)
    return [round(u0, 2), round(v0, 2), round(u1, 2), round(v1, 2)]


def model_json(part, side):
    els = []
    for e in elements(part, side):
        els.append({
            "from": list(e["frm"]), "to": list(e["to"]),
            "faces": {f: {"uv": face_uv(e, f), "texture": "#" + e["tex"]} for f in e["faces"]},
        })
    return {
        "parent": "block/block",
        "ambientocclusion": False,
        "textures": {"particle": "kiko:block/kiko_bed_wood", **{t: f"kiko:block/kiko_bed_{t}" for t in TEXTURES}},
        "elements": els,
    }


def blockstate_json():
    rot = {"north": 0, "east": 90, "south": 180, "west": 270}
    variants = {}
    for f, y in rot.items():
        for part in ("head", "foot"):
            for side in ("left", "right"):
                v = {"model": f"kiko:block/kiko_bed_{part}_{side}"}
                if y:
                    v["y"] = y
                variants[f"facing={f},part={part},side={side}"] = v
    return {"variants": variants}


# ------------------------------------------------------------------
# Vista previa: la cama completa (4 bloques) en 3/4
# ------------------------------------------------------------------
def preview(out_dir, texs):
    quads = []
    # Mirando al norte: izquierda en x 0..16, derecha en x 16..32; cabecera z 0..16, pies z 16..32
    blocks = [("head", "left", 0, 0), ("head", "right", 16, 0), ("foot", "left", 0, 16), ("foot", "right", 16, 16)]
    yaw, pitch, scale = math.radians(215), math.radians(-32), 9.0
    cy, sy, cp, sp = math.cos(yaw), math.sin(yaw), math.cos(pitch), math.sin(pitch)

    def proj(p):
        x, y, z = p[0] - 16, p[1], p[2] - 16
        x, z = x * cy - z * sy, x * sy + z * cy                   # yaw
        y, z = y * cp - z * sp, y * sp + z * cp                   # pitch
        return x, -y, z

    for part, side, ox, oz in blocks:
        for e in elements(part, side):
            (x0, y0, z0), (x1, y1, z1) = e["frm"], e["to"]
            x0, x1, z0, z1 = x0 + ox, x1 + ox, z0 + oz, z1 + oz
            img = texs[e["tex"]]
            corners = {
                "up": [(x0, y1, z0), (x1, y1, z0), (x1, y1, z1), (x0, y1, z1)],
                "south": [(x0, y1, z1), (x1, y1, z1), (x1, y0, z1), (x0, y0, z1)],
                "north": [(x1, y1, z0), (x0, y1, z0), (x0, y0, z0), (x1, y0, z0)],
                "east": [(x1, y1, z1), (x1, y1, z0), (x1, y0, z0), (x1, y0, z1)],
                "west": [(x0, y1, z0), (x0, y1, z1), (x0, y0, z1), (x0, y0, z0)],
            }
            shade = {"up": 1.0, "south": 0.85, "north": 0.7, "east": 0.78, "west": 0.78}
            for f, cs in corners.items():
                pts = [proj(p) for p in cs]
                ax, ay = pts[1][0] - pts[0][0], pts[1][1] - pts[0][1]
                bx, by = pts[3][0] - pts[0][0], pts[3][1] - pts[0][1]
                if ax * by - ay * bx >= 0:
                    continue
                n = 8
                px = img.load()
                for i in range(n):
                    for j in range(n):
                        def lerp(a, b, t):
                            return tuple(a[k] + (b[k] - a[k]) * t for k in range(3))
                        def at(u, v):
                            top = lerp(cs[0], cs[1], u)
                            bot = lerp(cs[3], cs[2], u)
                            return proj(lerp(top, bot, v))
                        q = [at(i / n, j / n), at((i + 1) / n, j / n), at((i + 1) / n, (j + 1) / n), at(i / n, (j + 1) / n)]
                        col = px[min(15, int((i + 0.5) / n * 16)), min(15, int((j + 0.5) / n * 16))]
                        col = tuple(int(ch * shade[f]) for ch in col[:3]) + (255,)
                        quads.append((sum(p[2] for p in q) / 4, q, col))
    quads.sort(key=lambda q: q[0], reverse=True)
    W, H = 520, 400
    out = Image.new("RGBA", (W, H), (40, 42, 54, 255))
    d = ImageDraw.Draw(out)
    for _, q, col in quads:
        d.polygon([(W / 2 + p[0] * scale, H / 2 + 40 + p[1] * scale) for p in q], fill=col, outline=col)
    out.save(os.path.join(out_dir, "kiko_cama.png"))


def main(preview_dir=None):
    texs = {name: fn() for name, fn in TEXTURES.items()}
    for name, img in texs.items():
        img.save(os.path.join(ASSETS, "textures", "block", f"kiko_bed_{name}.png"))
    icon().save(os.path.join(ASSETS, "textures", "item", "kiko_bed.png"))

    def dump(path, obj):
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, "w", encoding="utf-8") as f:
            json.dump(obj, f, indent=2)

    for part in ("head", "foot"):
        for side in ("left", "right"):
            dump(os.path.join(ASSETS, "models", "block", f"kiko_bed_{part}_{side}.json"), model_json(part, side))
    dump(os.path.join(ASSETS, "blockstates", "kiko_bed.json"), blockstate_json())
    dump(os.path.join(ASSETS, "models", "item", "kiko_bed.json"),
         {"parent": "minecraft:item/generated", "textures": {"layer0": "kiko:item/kiko_bed"}})
    dump(os.path.join(DATA, "kiko", "loot_table", "blocks", "kiko_bed.json"), {
        "type": "minecraft:block",
        "pools": [{
            "rolls": 1,
            "entries": [{"type": "minecraft:item", "name": "kiko:kiko_bed"}],
            "conditions": [
                {"condition": "minecraft:survives_explosion"},
                {"condition": "minecraft:block_state_property", "block": "kiko:kiko_bed",
                 "properties": {"part": "head", "side": "left"}},
            ],
        }],
    })
    dump(os.path.join(DATA, "kiko", "recipe", "kiko_bed.json"), {
        "type": "minecraft:crafting_shapeless",
        "category": "misc",
        "ingredients": [{"tag": "minecraft:beds"}, {"tag": "minecraft:beds"}, {"item": "minecraft:pink_dye"}],
        "result": {"id": "kiko:kiko_bed", "count": 1},
    })
    dump(os.path.join(DATA, "minecraft", "tags", "block", "beds.json"), {"replace": False, "values": ["kiko:kiko_bed"]})
    if preview_dir:
        os.makedirs(preview_dir, exist_ok=True)
        preview(preview_dir, texs)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
