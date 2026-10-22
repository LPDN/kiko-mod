"""
Bebidas de Tami (v3.6). Íconos 16x16 de cada bebida, del vaso, del jarrito y de la jarra de agua fresca.
Por ahora solo genera la vista previa.

Uso   python tools/gen_drinks.py carpeta_preview
"""
import os
import sys

from PIL import Image, ImageDraw, ImageFont

GLASS = (210, 236, 246, 170)
GLASS_D = (150, 190, 210, 220)
GLASS_L = (250, 255, 255, 230)
OUT = (40, 40, 48, 255)
CLAY, CLAY_D, CLAY_L = (178, 96, 58, 255), (134, 66, 38, 255), (206, 128, 84, 255)


def shade(c, k):
    return tuple(max(0, min(255, int(v * k))) for v in c[:3]) + (255,)


def bottle(liquid, label, cap=(200, 40, 40, 255)):
    """Botella de refresco (la de vidrio de toda la vida) con etiqueta."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    # cuerpo
    for y in range(5, 15):
        w = 3 if y < 7 else 4 if y < 9 else 3 if y < 11 else 4
        for x in range(8 - w, 8 + w):
            px[x, y] = liquid if 1 < y else GLASS
        px[8 - w, y] = shade(liquid, 0.7)
        px[8 + w - 1, y] = shade(liquid, 0.8)
        px[8 - w + 1, y] = shade(liquid, 1.25)
    # cuello y corcholata
    for y in range(2, 5):
        for x in (7, 8):
            px[x, y] = GLASS_D if y < 4 else shade(liquid, 0.9)
    for x in range(6, 10):
        px[x, 1] = cap
    px[6, 1] = shade(cap, 0.7)
    # etiqueta
    for y in (9, 10):
        for x in range(5, 11):
            px[x, y] = label
    px[7, 9] = (250, 250, 250, 255)
    px[8, 10] = (250, 250, 250, 255)
    # contorno de abajo
    for x in range(5, 11):
        px[x, 15] = shade(liquid, 0.55)
    return img


def can():
    """Lata de energética blanca (diseño propio: rayo azul y ojito verde de chupacabras)."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    W, W_S, W_D = (244, 244, 248, 255), (214, 216, 224, 255), (176, 180, 192, 255)
    MET = (190, 196, 206, 255)
    for y in range(2, 15):
        for x in range(5, 11):
            px[x, y] = W_D if x == 10 else (W_S if x == 9 else W)
    for x in range(5, 11):
        px[x, 1] = MET
        px[x, 2] = (150, 156, 168, 255)
        px[x, 14] = MET
        px[x, 15] = (150, 156, 168, 255)
    px[7, 1] = (120, 126, 140, 255)          # la abridera
    # rayo azul
    for (x, y) in ((8, 4), (7, 5), (7, 6), (6, 7), (7, 7), (8, 7), (8, 8), (7, 9), (7, 10), (6, 11)):
        px[x, y] = (60, 170, 230, 255)
    # ojito verde
    px[6, 4] = (110, 230, 120, 255)
    px[9, 11] = (110, 230, 120, 255)
    return img


def cup(liquid, foam=None, straw=None, tall=False):
    """Vaso de vidrio (agua fresca o licuado)."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    top = 2 if tall else 4
    for y in range(top, 15):
        inset = 0 if y < 8 else 1 if y < 12 else 1
        x0, x1 = 4 + inset, 11 - inset
        for x in range(x0, x1 + 1):
            if x in (x0, x1):
                px[x, y] = GLASS_D
            elif y == top:
                px[x, y] = GLASS_L
            elif y == top + 1:
                px[x, y] = foam or shade(liquid, 1.15)
            else:
                px[x, y] = liquid if (x + y) % 5 else shade(liquid, 1.12)
        px[x0 + 1, y] = shade(GLASS_L, 1) if y > top + 1 else px[x0 + 1, y]
    for x in range(6, 10):
        px[x, 15] = GLASS_D
    if straw:
        for i in range(5):
            px[10 + (i // 2), top - 1 - i // 1 if top - 1 - i >= 0 else 0] = straw
    return img


def mug(liquid, foam=None, steam=True):
    """Jarrito de barro con algo calientito."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    for y in range(6, 15):
        x0, x1 = (3, 11) if y < 13 else (4, 10)
        for x in range(x0, x1 + 1):
            px[x, y] = CLAY_D if x in (x0, x1) else (CLAY_L if x == x0 + 1 else CLAY)
        if y == 9:
            for x in range(x0 + 1, x1):
                px[x, y] = (236, 220, 180, 255) if x % 2 else CLAY_L     # rayita pintada
    for x in range(4, 11):
        px[x, 6] = foam or liquid
        px[x, 7] = shade(liquid, 0.85)
    # asa
    for y in range(8, 12):
        px[12, y] = CLAY_D
    px[13, 9] = CLAY_D
    px[13, 10] = CLAY_D
    if steam:
        for (x, y) in ((6, 4), (7, 3), (6, 2), (9, 4), (10, 3), (9, 2)):
            px[x, y] = (240, 240, 245, 150)
    return img


def empty_cup():
    img = cup((0, 0, 0, 0))
    px = img.load()
    for y in range(5, 15):
        for x in range(5, 11):
            if px[x, y][3] and px[x, y] not in (GLASS_D,):
                px[x, y] = GLASS
    return img


def empty_mug():
    img = mug(CLAY_D, foam=CLAY_D, steam=False)
    return img


def pitcher(liquid):
    """Jarra de vidrio (bloque) con agua fresca."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    for y in range(3, 15):
        x0, x1 = (4, 11) if y > 4 else (5, 10)
        for x in range(x0, x1 + 1):
            if x in (x0, x1):
                px[x, y] = GLASS_D
            elif y < 6:
                px[x, y] = GLASS
            else:
                px[x, y] = liquid if (x * 3 + y) % 7 else shade(liquid, 1.15)
    # pico y asa
    px[3, 4] = GLASS_D
    for y in range(6, 12):
        px[12, y] = GLASS_D
    px[13, 7], px[13, 10] = GLASS_D, GLASS_D
    for x in range(5, 11):
        px[x, 15] = GLASS_D
    # rodajas de fruta flotando
    px[6, 7] = (250, 250, 250, 255)
    px[9, 8] = (250, 250, 250, 255)
    return img


DRINKS = [
    # (grupo, nombre, ícono, receta, efecto)
    ("Refrescos", "Kiko-Cola", lambda: bottle((70, 30, 20, 255), (200, 40, 40, 255)),
     "botella de agua + azúcar + cacao", "velocidad II 30 s, luego tantito cansado"),
    ("Refrescos", "Naranjiko", lambda: bottle((240, 130, 30, 255), (40, 120, 200, 255), cap=(40, 120, 200, 255)),
     "botella de agua + azúcar + zanahoria", "velocidad 60 s y quita el hambre"),
    ("Refrescos", "Fresquiko de limón", lambda: bottle((150, 210, 70, 255), (250, 230, 60, 255), cap=(60, 150, 60, 255)),
     "botella de agua + azúcar + hojas", "resistencia al fuego 40 s"),
    ("Aguas frescas", "Horchata", lambda: cup((236, 226, 206, 255), foam=(250, 246, 236, 255)),
     "jarra con trigo + azúcar + leche", "cura 2 corazones, rinde 4 vasos"),
    ("Aguas frescas", "Jamaica", lambda: cup((150, 20, 50, 255)),
     "jarra con flores rojas + azúcar", "regeneración 15 s, rinde 4 vasos"),
    ("Aguas frescas", "Agua de sandía", lambda: cup((240, 90, 110, 255), foam=(250, 150, 160, 255)),
     "jarra con 4 rebanadas de sandía", "cura 3 corazones, rinde 4 vasos"),
    ("Calientitas", "Café de olla", lambda: mug((80, 46, 24, 255)),
     "jarrito + cacao + azúcar + palito", "prisa minera 90 s"),
    ("Calientitas", "Chocolate caliente", lambda: mug((96, 54, 34, 255), foam=(196, 150, 110, 255)),
     "jarrito + cacao + leche", "no te congelas 3 min y regeneración"),
    ("Calientitas", "Champurrado", lambda: mug((120, 70, 60, 255), foam=(150, 96, 80, 255)),
     "jarrito + trigo + cacao", "llena bastante"),
    ("Calientitas", "Té de manzanilla", lambda: mug((220, 190, 80, 255), foam=(236, 210, 110, 255)),
     "jarrito + 2 margaritas", "cura y te da sueñito (duermes antes)"),
    ("Energéticas", "Chupacabras Blanco", can,
     "lata + azúcar + polvo de piedra luminosa", "fuerza, velocidad y regeneración 1 min, luego hambre"),
    ("Licuados", "Licuado de fresa", lambda: cup((230, 110, 140, 255), foam=(250, 180, 200, 255), straw=(240, 240, 240, 255), tall=True),
     "vaso + bayas dulces + leche", "regeneración II 10 s"),
    ("Licuados", "Licuado de manzana", lambda: cup((236, 210, 150, 255), foam=(250, 236, 200, 255), straw=(80, 200, 120, 255), tall=True),
     "vaso + manzana + leche", "absorción y llena"),
]

CONTAINERS = [
    ("Lata vacía", can, "3 pepitas de hierro en V, salen 2 latas"),
    ("Vaso", empty_cup, "3 vidrios en U, se regresa al tomar"),
    ("Jarrito de barro", empty_mug, "3 ladrillos en U, se regresa al tomar"),
    ("Jarra de agua fresca", lambda: pitcher((150, 20, 50, 255)), "bloque, se llena con 4 vasos"),
]


def preview(out):
    os.makedirs(out, exist_ok=True)
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 17)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 13)
    big = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 22)
    S = 6
    CW, CH = 330, 150
    cols = 4
    rows = (len(DRINKS) + cols - 1) // cols + 1
    sheet = Image.new("RGBA", (CW * cols + 20, CH * rows + 90), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    d.text((14, 12), "Bebidas de Tami (v3.6)", fill=(255, 210, 120), font=big)
    d.text((14, 44), "Tami las hace mejor (duran el doble). Las demás también te las preparan con los ingredientes.",
           fill=(190, 190, 200), font=small)
    for i, (grp, name, fn, rec, eff) in enumerate(DRINKS + [(None, n, f, r, "") for n, f, r in CONTAINERS]):
        x, y = 10 + (i % cols) * CW, 80 + (i // cols) * CH
        d.rounded_rectangle([x, y, x + CW - 12, y + CH - 12], 10, fill=(52, 54, 70, 255))
        icon = fn().resize((16 * S, 16 * S), Image.NEAREST)
        sheet.alpha_composite(icon, (x + 10, y + 18))
        tx = x + 16 * S + 22
        d.text((tx, y + 14), name, fill=(255, 220, 140), font=font)
        if grp:
            d.text((tx, y + 36), grp, fill=(150, 160, 180), font=small)
        lines = [rec] + ([eff] if eff else [])
        yy = y + 60
        for ln in lines:
            words, cur = ln.split(), ""
            for w in words:
                if d.textlength(cur + " " + w, font=small) > CW - 16 * S - 46:
                    d.text((tx, yy), cur.strip(), fill=(220, 220, 230), font=small)
                    yy += 17
                    cur = ""
                cur += " " + w
            d.text((tx, yy), cur.strip(), fill=(220, 220, 230) if ln == rec else (140, 220, 160), font=small)
            yy += 21
    sheet.convert("RGB").save(os.path.join(out, "bebidas.png"))


ICONS = {
    "kiko_cola": DRINKS[0][2], "naranjiko": DRINKS[1][2], "fresquiko": DRINKS[2][2],
    "horchata": DRINKS[3][2], "jamaica": DRINKS[4][2], "agua_sandia": DRINKS[5][2],
    "cafe_de_olla": DRINKS[6][2], "chocolate_caliente": DRINKS[7][2], "champurrado": DRINKS[8][2],
    "te_manzanilla": DRINKS[9][2], "chupacabras_blanco": DRINKS[10][2],
    "licuado_fresa": DRINKS[11][2], "licuado_manzana": DRINKS[12][2],
    "cup": empty_cup, "mug": empty_mug, "empty_can": can,
    "pitcher": lambda: jar_icon(None, None),
    "pitcher_horchata": lambda: jar_icon(PITCH_LIQ["pitcher_horchata"], "canela"),
    "pitcher_jamaica": lambda: jar_icon(PITCH_LIQ["pitcher_jamaica"], "flor"),
    "pitcher_agua_sandia": lambda: jar_icon(PITCH_LIQ["pitcher_agua_sandia"], "sandia"),
}
PITCH_LIQ = {"pitcher": None, "pitcher_horchata": (238, 228, 204, 235), "pitcher_jamaica": (150, 22, 52, 235),
             "pitcher_agua_sandia": (240, 96, 116, 235)}
DECO = {"pitcher": None, "pitcher_horchata": "canela", "pitcher_jamaica": "flor", "pitcher_agua_sandia": "sandia"}


def jar_side(liquid, deco, level=4):
    """Cara de la jarra de vidrio puesta en el suelo (con transparencia). level 1..4 = qué tan llena."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    glass = (205, 232, 244, 70)
    edge = (140, 186, 208, 230)
    top = 1
    fill_h = 0 if liquid is None else max(2, int(14 * level / 4))
    liq_top = 15 - fill_h
    for y in range(16):
        for x in range(16):
            if x in (0, 15) or y == 15 or y == 0:
                px[x, y] = edge
            elif liquid is not None and y >= liq_top:
                c = liquid
                if y == liq_top:
                    c = shade(liquid, 1.25)
                elif (x * 3 + y * 5) % 11 == 0:
                    c = shade(liquid, 1.1)
                px[x, y] = c[:3] + (liquid[3],)
            else:
                px[x, y] = glass
    # reflejo del vidrio
    for y in range(2, 13):
        px[2, y] = (255, 255, 255, 190) if px[2, y][3] < 200 or y > liq_top else (255, 255, 255, 120)
        px[3, y] = (255, 255, 255, 110)
    # decoración visible solo si hay líquido hasta ahí
    def put(x, y, c):
        if 1 <= x <= 14 and liq_top < y < 15:
            px[x, y] = c
    if deco == "canela":
        BR, BR2 = (150, 84, 40, 255), (190, 120, 66, 255)
        for i in range(8):
            put(5 + i // 1 if i < 6 else 11, 12 - i, BR if i % 2 else BR2)
        for i in range(7):
            put(11 - i, 13 - i // 1, BR2 if i % 2 else BR) if False else None
        for (x, y) in ((8, 11), (9, 12), (10, 13), (6, 13), (12, 12), (7, 9)):
            put(x, y, (252, 248, 236, 255))
    elif deco == "flor":
        for (cx, cy) in ((6, 11), (11, 8), (9, 13)):
            for (dx, dy) in ((0, -1), (1, 0), (0, 1), (-1, 0), (1, -1), (-1, 1)):
                put(cx + dx, cy + dy, (232, 60, 90, 255))
            put(cx, cy, (255, 220, 90, 255))
    elif deco == "sandia":
        for (cx, cy) in ((6, 12), (11, 9)):
            for dx in range(-2, 3):
                for dy in range(0, 2 - abs(dx) // 2 + 1):
                    put(cx + dx, cy + dy, (255, 120, 140, 255))
            for dx in range(-2, 3):
                put(cx + dx, cy + 2 - abs(dx) // 2, (70, 160, 70, 255))
            put(cx - 1, cy, (40, 20, 24, 255))
            put(cx + 1, cy + 1, (40, 20, 24, 255))
    return img


def jar_lid(liquid):
    img = Image.new("RGBA", (16, 16), (150, 100, 60, 255))
    px = img.load()
    for y in range(16):
        for x in range(16):
            if (x + y * 2) % 5 == 0:
                px[x, y] = (128, 84, 48, 255)
    return img


def jar_icon(liquid, deco):
    """Ícono 16x16 de la jarra (con asa, pico y tapa)."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    E = (110, 160, 186, 255)
    G = (205, 232, 244, 120)
    for y in range(4, 15):
        for x in range(3, 11):
            if x in (3, 10) or y == 14:
                px[x, y] = E
            elif liquid is not None and y >= 7:
                c = shade(liquid, 1.25) if y == 7 else liquid
                px[x, y] = c[:3] + (255,)
            else:
                px[x, y] = G
    for x in range(4, 10):                                    # cuello
        px[x, 3] = E
    px[3, 3], px[10, 3] = (0, 0, 0, 0), (0, 0, 0, 0)
    for x in range(5, 9):                                     # tapa
        px[x, 2] = (150, 100, 60, 255)
        px[x, 1] = (178, 124, 78, 255)
    px[2, 4], px[2, 3] = E, E                                 # pico
    for y in range(5, 12):                                    # asa
        px[11, y] = E
    px[12, 6], px[12, 10] = E, E
    for y in range(5, 13):
        px[4, y] = (255, 255, 255, 200) if px[4, y][3] > 0 and y != 7 else px[4, y]
    if liquid is not None:
        W = (252, 248, 236, 255)
        if deco == "canela":
            px[6, 12], px[7, 11], px[8, 10] = (150, 84, 40, 255), (190, 120, 66, 255), (150, 84, 40, 255)
            px[5, 9] = W
        elif deco == "flor":
            for (x, y) in ((5, 10), (6, 9), (6, 11), (7, 10), (8, 12)):
                px[x, y] = (232, 60, 90, 255)
            px[6, 10] = (255, 220, 90, 255)
        elif deco == "sandia":
            for x in range(5, 9):
                px[x, 11] = (255, 120, 140, 255)
                px[x, 12] = (70, 160, 70, 255)
            px[6, 11] = (40, 20, 24, 255)
    return img


def export(root):
    import json
    a = os.path.join(root, "src/main/resources/assets/kiko")
    d = os.path.join(root, "src/main/resources/data/kiko")
    for sub in ("textures/item", "textures/block", "models/item", "models/block", "blockstates"):
        os.makedirs(os.path.join(a, sub), exist_ok=True)
    os.makedirs(os.path.join(d, "loot_table/blocks"), exist_ok=True)
    for name, fn in ICONS.items():
        fn().save(os.path.join(a, "textures/item", name + ".png"))
        json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "kiko:item/" + name}},
                  open(os.path.join(a, "models/item", name + ".json"), "w"), indent=1)
    # bloques de jarra, un modelo por nivel de llenado
    jar_lid(None).save(os.path.join(a, "textures/block", "pitcher_lid.png"))
    for name, liq in PITCH_LIQ.items():
        levels = range(5) if liq is not None else [0]
        states = {}
        for lv in levels:
            tex = "%s_%d" % (name, lv)
            jar_side(liq, DECO[name], lv if lv else 4).save(os.path.join(a, "textures/block", tex + ".png"))
            F = lambda n: {f: {"texture": n} for f in ("north", "south", "east", "west", "up", "down")}
            model = {"textures": {"side": "kiko:block/" + tex, "lid": "kiko:block/pitcher_lid", "particle": "kiko:block/" + tex},
                     "elements": [
                         {"from": [4, 0, 4], "to": [12, 9, 12], "faces": F("#side")},
                         {"from": [5, 9, 5], "to": [11, 11, 11], "faces": F("#side")},
                         {"from": [6, 11, 6], "to": [10, 12.5, 10], "faces": F("#lid")},
                         {"from": [12, 1.5, 7], "to": [14, 3, 9], "faces": F("#side")},
                         {"from": [13, 3, 7], "to": [14, 7.5, 9], "faces": F("#side")},
                         {"from": [12, 7.5, 7], "to": [14, 9, 9], "faces": F("#side")},
                         {"from": [3, 8, 7], "to": [4.5, 10.5, 9], "faces": F("#side")}]}
            json.dump(model, open(os.path.join(a, "models/block", tex + ".json"), "w"), indent=1)
            states["level=%d" % lv] = {"model": "kiko:block/" + tex}
        if liq is not None:
            states["level=0"] = states["level=1"]
        else:
            for lv in range(1, 5):
                states["level=%d" % lv] = states["level=0"]
        json.dump({"variants": states}, open(os.path.join(a, "blockstates", name + ".json"), "w"), indent=1)
        for old in ("textures/block/%s.png" % name, "models/block/%s.json" % name):
            try:
                os.remove(os.path.join(a, old))
            except OSError:
                pass
        json.dump({"type": "minecraft:block", "pools": [{"rolls": 1, "entries": [{"type": "minecraft:item", "name": "kiko:pitcher"}],
                                                          "conditions": [{"condition": "minecraft:survives_explosion"}]}]},
                  open(os.path.join(d, "loot_table/blocks", name + ".json"), "w"), indent=1)


if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "export":
        export(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
        sys.exit(0)
    preview(sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/drinks")
