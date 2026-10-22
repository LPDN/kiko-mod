"""
Camas matrimoniales de colores (v3.3.1). Una por casa para que no se repitan.
Misma forma que la cama de Kiko (gen_bed.py), otra madera, otra cobija y otro dibujo.
Genera texturas, modelos, blockstates, íconos, loot tables, recetas (2 camas + tinte) y el tag de camas.

Uso   python tools/gen_bed_variants.py [carpeta_preview]
"""
import json
import os
import sys

from PIL import Image, ImageDraw

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_bed as gb  # noqa: E402

c = gb.c

#  id         casa de   madera (base, oscura, clara)                     cobija (base, oscura, clara)                 dibujo     acento            raya almohada     tinte
VARIANTS = [
    ("bed_forge", "Maru", (c(92, 64, 44), c(64, 44, 30), c(120, 86, 60)), (c(190, 50, 44), c(140, 30, 30), c(226, 90, 60)), "plaid", c(30, 26, 28), c(190, 50, 44), "red_dye"),
    ("bed_moon", "Lumi", (c(214, 200, 150), c(184, 168, 120), c(232, 222, 182)), (c(150, 120, 210), c(110, 86, 170), c(186, 160, 236)), "moons", c(250, 244, 200), c(150, 120, 210), "purple_dye"),
    ("bed_trail", "Nadi", (c(114, 84, 52), c(84, 60, 36), c(140, 106, 70)), (c(90, 140, 80), c(60, 106, 56), c(130, 180, 110)), "mountains", c(236, 230, 210), c(90, 140, 80), "green_dye"),
    ("bed_clinic", "Rina", (c(236, 230, 222), c(206, 200, 192), c(250, 248, 244)), (c(132, 206, 196), c(96, 170, 160), c(176, 230, 222)), "crosses", c(250, 250, 250), c(132, 206, 196), "cyan_dye"),
    ("bed_kitchen", "Tami", (c(176, 120, 74), c(140, 90, 54), c(204, 150, 100)), (c(240, 140, 50), c(200, 104, 30), c(250, 236, 220)), "checker", c(250, 236, 220), c(240, 140, 50), "orange_dye"),
    ("bed_ranch", "Toño", (c(110, 80, 50), c(80, 56, 34), c(140, 104, 68)), (c(150, 104, 62), c(110, 72, 40), c(220, 196, 150)), "plaid", c(220, 196, 150), c(196, 50, 50), "brown_dye"),
    ("bed_sailor", "Leo", (c(220, 216, 206), c(190, 186, 176), c(240, 238, 232)), (c(40, 54, 96), c(28, 38, 70), c(250, 250, 250)), "stripes", c(250, 250, 250), c(40, 54, 96), "blue_dye"),
    ("bed_guard", "Dante", (c(70, 70, 76), c(50, 50, 56), c(100, 100, 108)), (c(150, 34, 44), c(110, 22, 32), c(180, 60, 66)), "emblem", c(230, 196, 90), c(150, 34, 44), "gray_dye"),
    ("bed_mine", "Iván", (c(96, 72, 48), c(70, 52, 34), c(124, 96, 64)), (c(110, 112, 120), c(80, 82, 90), c(140, 142, 150)), "gems", c(250, 200, 40), c(250, 200, 40), "yellow_dye"),
    ("bed_music", "Emi", (c(46, 34, 30), c(30, 22, 20), c(66, 50, 44)), (c(60, 40, 80), c(40, 26, 56), c(90, 66, 116)), "notes", c(240, 150, 180), c(240, 150, 180), "magenta_dye"),
    ("bed_merchant", "Beto", (c(162, 130, 78), c(130, 100, 56), c(190, 156, 100)), (c(60, 140, 90), c(40, 104, 64), c(100, 180, 120)), "coins", c(240, 200, 70), c(240, 200, 70), "lime_dye"),
]


def stamp(px, x, y, pts, col):
    for dx, dy in pts:
        px[(x + dx) % 16, (y + dy) % 16] = col


def blanket(base, dark, light, kind, accent):
    img = Image.new("RGBA", (16, 16), base)
    px = img.load()
    for y in range(16):
        for x in range(16):
            col = base
            if kind == "plaid":
                if x % 6 in (0, 1) and y % 6 in (0, 1):
                    col = accent
                elif x % 6 in (0, 1) or y % 6 in (0, 1):
                    col = dark
            elif kind == "stripes":
                col = light if (y // 2) % 2 == 0 else base
            elif kind == "checker":
                col = light if ((x // 4) + (y // 4)) % 2 == 0 else base
            elif (x + y) % 6 == 0:
                col = light
            if y in (0, 15):
                col = dark
            px[x, y] = col
    if kind == "moons":
        for sx, sy in ((4, 4), (11, 10)):
            stamp(px, sx, sy, [(0, -1), (-1, 0), (-1, 1), (0, 2), (1, -1), (1, 2)], accent)
        for sx, sy in ((12, 3), (4, 12)):
            px[sx, sy] = accent
    elif kind == "mountains":
        for sx in (2, 9):
            stamp(px, sx, 9, [(0, 0), (1, -1), (2, -2), (3, -1), (4, 0), (2, -3)], accent)
    elif kind == "crosses":
        for sx, sy in ((4, 4), (11, 11)):
            stamp(px, sx, sy, [(0, 0), (1, 0), (-1, 0), (0, 1), (0, -1)], accent)
    elif kind == "emblem":
        stamp(px, 8, 8, [(0, -2), (-1, -1), (1, -1), (-2, 0), (2, 0), (-1, 1), (1, 1), (0, 2)], accent)
    elif kind == "gems":
        for sx, sy, col in ((4, 4, accent), (11, 9, c(90, 220, 220)), (6, 12, c(220, 60, 60))):
            stamp(px, sx, sy, [(0, 0), (1, 0), (0, 1), (1, 1)], col)
    elif kind == "notes":
        for sx, sy in ((4, 5), (11, 10)):
            stamp(px, sx, sy, [(0, 0), (1, 0), (1, -1), (1, -2), (1, -3), (2, -3)], accent)
    elif kind == "coins":
        for sx, sy in ((4, 4), (11, 10), (12, 3)):
            stamp(px, sx, sy, [(0, 0), (1, 0), (0, 1), (1, 1)], accent)
    return img


def wood(base, dark, light):
    img = Image.new("RGBA", (16, 16), base)
    px = img.load()
    for y in range(16):
        for x in range(16):
            if y % 4 == 3:
                px[x, y] = dark
            elif (x * 7 + y * 3) % 11 == 0:
                px[x, y] = light
            elif (x + (y // 4) * 5) % 9 == 0:
                px[x, y] = dark
    return img


def pillow(stripe):
    img = Image.new("RGBA", (16, 16), gb.PILLOW)
    px = img.load()
    for y in range(16):
        for x in range(16):
            if x in (0, 15) or y in (0, 15):
                px[x, y] = gb.PILLOW_S
            elif y in (6, 9):
                px[x, y] = stripe
    return img


def icon(woodc, blanketc, accent):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.rectangle([1, 3, 2, 14], fill=woodc[1])
    d.rectangle([3, 7, 14, 11], fill=gb.SHEET)
    d.rectangle([7, 6, 14, 11], fill=blanketc[0])
    d.rectangle([3, 5, 6, 7], fill=gb.PILLOW)
    d.rectangle([3, 9, 6, 10], fill=gb.PILLOW)
    d.point((10, 8), fill=accent)
    d.rectangle([3, 12, 14, 12], fill=woodc[0])
    d.rectangle([3, 13, 3, 14], fill=woodc[1])
    d.rectangle([14, 13, 14, 14], fill=woodc[1])
    return img


def dump(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)


def main(preview_dir=None):
    A, D = gb.ASSETS, gb.DATA
    previews = []
    for vid, owner, woodc, blanketc, kind, accent, stripe, dye in VARIANTS:
        texs = {"wood": wood(*woodc), "sheet": gb.tex_sheet(), "blanket": blanket(*blanketc, kind, accent), "pillow": pillow(stripe)}
        for name, img in texs.items():
            img.save(os.path.join(A, "textures", "block", f"{vid}_{name}.png"))
        icon(woodc, blanketc, accent).save(os.path.join(A, "textures", "item", f"{vid}.png"))
        for part in ("head", "foot"):
            for side in ("left", "right"):
                m = gb.model_json(part, side)
                m["textures"] = {"particle": f"kiko:block/{vid}_wood", **{t: f"kiko:block/{vid}_{t}" for t in texs}}
                dump(os.path.join(A, "models", "block", f"{vid}_{part}_{side}.json"), m)
        bs = gb.blockstate_json()
        for v in bs["variants"].values():
            v["model"] = v["model"].replace("kiko:block/kiko_bed_", f"kiko:block/{vid}_")
        dump(os.path.join(A, "blockstates", f"{vid}.json"), bs)
        dump(os.path.join(A, "models", "item", f"{vid}.json"),
             {"parent": "minecraft:item/generated", "textures": {"layer0": f"kiko:item/{vid}"}})
        dump(os.path.join(D, "kiko", "loot_table", "blocks", f"{vid}.json"), {
            "type": "minecraft:block",
            "pools": [{"rolls": 1, "entries": [{"type": "minecraft:item", "name": f"kiko:{vid}"}],
                       "conditions": [{"condition": "minecraft:survives_explosion"},
                                      {"condition": "minecraft:block_state_property", "block": f"kiko:{vid}",
                                       "properties": {"part": "head", "side": "left"}}]}]})
        dump(os.path.join(D, "kiko", "recipe", f"{vid}.json"), {
            "type": "minecraft:crafting_shapeless", "category": "misc",
            "ingredients": [{"tag": "minecraft:beds"}, {"tag": "minecraft:beds"}, {"item": f"minecraft:{dye}"}],
            "result": {"id": f"kiko:{vid}", "count": 1}})
        previews.append(texs)
    dump(os.path.join(D, "minecraft", "tags", "block", "beds.json"),
         {"replace": False, "values": ["kiko:kiko_bed"] + [f"kiko:{v[0]}" for v in VARIANTS]})
    if preview_dir:
        os.makedirs(preview_dir, exist_ok=True)
        sheet = Image.new("RGBA", (16 * 4 * 8 + 20, len(VARIANTS) * 72), (40, 42, 54, 255))
        d = ImageDraw.Draw(sheet)
        for i, (v, texs) in enumerate(zip(VARIANTS, previews)):
            for j, n in enumerate(("wood", "blanket", "pillow")):
                sheet.paste(texs[n].resize((64, 64), Image.NEAREST), (120 + j * 70, i * 72 + 4))
            d.text((4, i * 72 + 28), v[1], fill=(255, 210, 120))
        sheet.save(os.path.join(preview_dir, "camas.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
