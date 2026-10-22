"""
El pueblo (v3.4). Estructura de piezas (jigsaw) como las aldeas normales: se arma sola al generar el mundo.

  plaza          plaza con fuente, faroles, bancas, campana y jardineras (la pieza de inicio)
  street         calle recta (sigue el terreno) con un lugar para casa de cada lado
  street_cross   cruce con salidas a los lados
  street_end     fin de calle con banquita y flores
  house_small    casa chica (pareja y a veces un niño)
  house_medium   casa mediana (pareja y uno o dos niños)
  house_large    casa grande con porche (pareja y de uno a tres niños)
  house_elders   casa de los abuelitos con jardín
  huerto         milpa con agua, espantapájaros y composta
  corral         corral con vacas y borregos, heno y bebedero
  tree / well    adornos para lugares que sobran
  soccer         cancha con porterías y pelota (sale pegada a la plaza, una por pueblo)

Todo sale en tres maderas según el bioma
  oak      planicies y bosques (paredes de roble, tejas de ladrillo)
  spruce   taigas (abeto, techo oscuro, base de piedra)
  acacia   sabanas (paredes de terracota blanca, techo de acacia)

Las casas traen a los vecinos (kiko:townsfolk) con su papel (papá, mamá, niño, abuelitos) y la probabilidad de salir.
Uso   python tools/gen_town.py [carpeta_preview]
"""
import math
import os
import random
import sys

from nbtlib.tag import Compound, Float, Int, String

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_structures as gs  # noqa: E402
import gen_guy_houses as gh  # noqa: E402
from gen_structures import Build  # noqa: E402

WOODS = {
    "oak": dict(wood="oak", log="oak_log", wall="oak_planks", frame="stripped_oak_log", base="cobblestone",
                roof=("brick_stairs", "brick_slab", "bricks"), floor="spruce_planks", leaves="oak_leaves",
                pave=[("stone_bricks", 5), ("cobblestone", 2), ("andesite", 2), ("cracked_stone_bricks", 1)],
                beds=["red", "yellow", "light_blue", "lime", "pink", "orange"], loot="minecraft:chests/village/village_plains_house"),
    "spruce": dict(wood="spruce", log="spruce_log", wall="spruce_planks", frame="stripped_spruce_log", base="stone_bricks",
                   roof=("dark_oak_stairs", "dark_oak_slab", "dark_oak_planks"), floor="oak_planks", leaves="spruce_leaves",
                   pave=[("cobblestone", 5), ("mossy_cobblestone", 2), ("stone", 2), ("gravel", 1)],
                   beds=["red", "blue", "green", "brown", "cyan", "white"], loot="minecraft:chests/village/village_taiga_house"),
    "acacia": dict(wood="acacia", log="acacia_log", wall="white_terracotta", frame="acacia_log", base="sandstone",
                   roof=("acacia_stairs", "acacia_slab", "acacia_planks"), floor="acacia_planks", leaves="acacia_leaves",
                   pave=[("smooth_sandstone", 5), ("cut_sandstone", 2), ("sandstone", 2), ("terracotta", 1)],
                   beds=["orange", "yellow", "red", "magenta", "lime", "brown"], loot="minecraft:chests/village/village_savanna_house"),
}

gs.COLORS.update({
    "kiko:notice_board": (180, 130, 80), "red_wool": (170, 40, 40), "yellow_wool": (240, 200, 50), "lime_wool": (120, 190, 40),
    "light_blue_wool": (110, 180, 220), "melon": (110, 160, 50), "pumpkin": (220, 130, 30), "loom": (150, 120, 90),
    "potted_cornflower": (80, 110, 220), "kiko:crib": (240, 200, 210),
    "jigsaw": (150, 124, 70), "dirt": (134, 96, 67), "acacia_log": (100, 94, 86), "stripped_acacia_log": (174, 92, 58), "acacia_planks": (168, 90, 50),
    "acacia_stairs": (168, 90, 50), "acacia_slab": (168, 90, 50), "acacia_fence": (168, 90, 50), "acacia_door": (168, 90, 50),
    "acacia_leaves": (90, 140, 40), "acacia_fence_gate": (168, 90, 50), "acacia_pressure_plate": (168, 90, 50),
    "acacia_trapdoor": (168, 90, 50), "oak_trapdoor": (150, 120, 70), "oak_leaves": (70, 130, 40), "spruce_leaves": (60, 96, 60),
    "oak_pressure_plate": (162, 130, 78), "dark_oak_planks": (70, 46, 24), "sandstone": (216, 204, 156),
    "smooth_sandstone": (222, 210, 164), "cut_sandstone": (214, 200, 150), "terracotta": (152, 94, 68), "andesite": (136, 136, 136),
    "stone": (125, 125, 125), "red_bed": (170, 40, 40), "yellow_bed": (240, 200, 50), "light_blue_bed": (110, 180, 220),
    "lime_bed": (120, 190, 40), "pink_bed": (240, 150, 180), "orange_bed": (230, 120, 30), "blue_bed": (50, 60, 160),
    "green_bed": (80, 120, 40), "brown_bed": (110, 72, 40), "cyan_bed": (20, 140, 150), "magenta_bed": (190, 70, 180),
    "poppy": (200, 40, 40), "cornflower": (80, 110, 220), "azure_bluet": (220, 230, 240), "red_tulip": (220, 60, 50),
    "flower_pot": (150, 90, 60), "white_concrete": (220, 222, 224), "carved_pumpkin": (220, 130, 30), "sheep": (230, 230, 230),
    "short_grass": (90, 150, 60), "bell": (230, 190, 60), "water_cauldron": (70, 110, 200), "stone_brick_wall": (122, 122, 122),
    "mossy_cobblestone_wall": (96, 116, 88), "sandstone_wall": (216, 204, 156), "cobblestone_wall": (110, 110, 110),
    "oak_wall_sign": (162, 130, 78), "yellow_carpet": (240, 200, 50), "green_carpet": (80, 120, 40), "blue_carpet": (50, 60, 160),
    "light_gray_carpet": (150, 150, 150), "potted_red_tulip": (220, 60, 50), "potted_poppy": (200, 40, 40),
    "potted_dandelion": (240, 220, 60), "potted_fern": (90, 150, 60), "potted_cactus": (80, 140, 60),
    "bricks": (150, 82, 64), "rose_bush": (200, 40, 50), "lilac": (200, 150, 210), "sunflower": (240, 210, 40),
})

# Vecinos que salen en cada casa: (x, z, papel, probabilidad)
NPC = "kiko:townsfolk"


def npc(b, x, y, z, role, chance=1.0):
    b.girl(x, y, z, NPC, {"TownRole": String(role), "TownChance": Float(chance)})


def jig(b, x, y, z, facing, name, target="minecraft:empty", pool="minecraft:empty", final="minecraft:air"):
    """Bloque jigsaw (conector). Siempre en la orilla de la pieza viendo hacia afuera."""
    b.set(x, y, z, "jigsaw", Compound({
        "id": String("minecraft:jigsaw"), "name": String(name), "target": String(target), "pool": String(pool),
        "final_state": String(final), "joint": String("rollable"),
        "placement_priority": Int(0), "selection_priority": Int(0)}), orientation=f"{facing}_up")
    b.jigs.append((x, y, z, facing, name, target, pool, final))


def front(b, x):
    """Conector de enfrente de casas y lotes, al ras del piso (así quedan a la altura de la calle)."""
    blk = b.blocks.get((x, 0, 0))
    name = blk[0] if blk else "minecraft:grass_block"
    if name.endswith("grass_block"):
        name = "minecraft:grass_block"
    jig(b, x, 0, 0, "north", "kiko:house_front", final=name)


def new(name, size, W):
    b = Build(name, size)
    b.jigs = []
    b.W = W
    return b


def flowers(b, spots):
    kinds = ["poppy", "dandelion", "cornflower", "azure_bluet", "red_tulip", "oxeye_daisy", "allium"]
    for i, (x, z) in enumerate(spots):
        b.set(x, 1, z, kinds[(x * 3 + z * 5 + i) % len(kinds)])


def lamp(b, x, z, W, h=3):
    for y in range(1, h):
        b.fence(x, y, z, f"{W['wood']}_fence")
    b.set(x, h, z, "lantern", hanging="false", waterlogged="false")


# ------------------------------------------------------------------
# Casas: cascarón común (frente hacia z = 0, la calle)
# ------------------------------------------------------------------
def shell(b, W, X0, X1, Z0, Z1, TOP, door_x):
    wd = W["wood"]
    b.fill(X0, 0, Z0, X1, 0, Z1, W["floor"])
    gh.box(b, X0, X1, Z0, Z1, TOP, W["wall"], corner=W["log"], base=W["base"])
    for z in (Z0, Z1):
        b.log(X0 + 1, TOP, z, X1 - 1, TOP, z, W["frame"], "x")
    b.door(door_x, 1, Z0, f"{wd}_door", "south")
    stair, slab, full = W["roof"]
    b.gable(X0 - 1, X1 + 1, Z0 - 1, Z1 + 1, TOP + 1, stair, slab, full, ends=(Z0, Z1))
    gh.ceiling(b, X0, X1, Z0, Z1, TOP, W["floor"], W["frame"])
    # Tapanco relleno (si queda hueco y oscuro ahí salen zombis)
    i = 0
    while X0 - 1 + i <= X1 + 1 - i:
        for x in range(X0 + i, X1 + 1 - i):
            for z in range(Z0, Z1 + 1):
                if b.get(x, TOP + 1 + i, z) in (None, "air"):
                    b.set(x, TOP + 1 + i, z, full)
        i += 1
    # Camino de la calle a la puerta y escalón
    b.fill(door_x, 0, 0, door_x, 0, Z0 - 1, "dirt_path")
    # Contraventanas junto a las ventanas del frente
    return wd


def window_row(b, spots):
    for x, y, z in spots:
        b.pane(x, y, z)


def table(b, W, x, z):
    b.table(x, 1, z, W["wood"])


def house_small(W, tag):
    b = new(f"town/{tag}/house_small", (9, 11, 10), W)
    b.ground(chance=0.06)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 7, 2, 8, 3
    wd = shell(b, W, X0, X1, Z0, Z1, TOP, 4)
    window_row(b, [(2, 2, Z0), (6, 2, Z0), (X0, 2, 5), (X1, 2, 5), (4, 2, Z1)])
    beds = W["beds"]
    # Cama de la pareja (dos camas juntas) y la del niño
    b.bed(5, 1, 6, beds[0], "south")
    b.bed(6, 1, 6, beds[0], "south")
    b.bed(2, 1, 6, beds[1], "south")
    b.set(3, 1, 7, "barrel", facing="up", open="false")
    gh.potted(b, 3, 2, 7, "red_tulip")
    b.set(4, 1, 7, "kiko:crib", facing="north")      # v3.5 para cuando llegue un bebé
    b.set(2, 1, 3, "crafting_table")
    b.set(6, 1, 3, "furnace", facing="south", lit="false")
    b.chest(2, 1, 4, "east", W["loot"])
    b.fill(3, 1, 4, 5, 1, 5, "white_carpet" if wd != "acacia" else "orange_carpet")
    b.lantern(4, TOP, 5)
    flowers(b, [(1, 0), (2, 0), (6, 0), (7, 0)])
    flowers(b, [(1, 1), (7, 1)])
    front(b, 4)
    npc(b, 4, 1, 4, "dad")
    npc(b, 5, 1, 5, "mom")
    npc(b, 3, 1, 5, "kid", 0.3)
    return b


def house_medium(W, tag):
    b = new(f"town/{tag}/house_medium", (11, 12, 12), W)
    b.ground(chance=0.06)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 9, 2, 10, 4
    wd = shell(b, W, X0, X1, Z0, Z1, TOP, 5)
    window_row(b, [(3, 2, Z0), (7, 2, Z0), (X0, 2, 4), (X0, 2, 8), (X1, 2, 4), (X1, 2, 8), (5, 2, Z1), (3, 3, Z0), (7, 3, Z0)])
    beds = W["beds"]
    b.bed(7, 1, 8, beds[2], "south")
    b.bed(8, 1, 8, beds[2], "south")
    b.bed(2, 1, 8, beds[3], "south")
    b.bed(4, 1, 8, beds[4], "south")
    b.set(3, 1, 9, "barrel", facing="up", open="false")
    gh.potted(b, 3, 2, 9, "dandelion")
    b.set(6, 1, 9, "bookshelf")
    b.set(5, 1, 9, "kiko:crib", facing="north")
    # Cocina y comedor
    b.set(2, 1, 3, "furnace", facing="south", lit="false")
    b.set(3, 1, 3, "smoker", facing="south", lit="false")
    b.set(2, 1, 4, "crafting_table")
    b.chest(8, 1, 3, "south", W["loot"])
    b.barrel(8, 1, 4, "west")
    table(b, W, 5, 5)
    gh.chair(b, 4, 5, f"{wd}_stairs", "east")
    gh.chair(b, 6, 5, f"{wd}_stairs", "west")
    b.set(5, 2, 5, "candle", candles="3", lit="false", waterlogged="false")
    b.fill(4, 1, 6, 6, 1, 7, "red_carpet")
    b.lantern(5, TOP, 4)
    b.lantern(5, TOP, 8)
    b.fill(0, 0, 0, 10, 0, 1, "grass_block", snowy="false")
    b.fill(5, 0, 0, 5, 0, 1, "dirt_path")
    flowers(b, [(1, 1), (2, 1), (3, 1), (7, 1), (8, 1), (9, 1)])
    front(b, 5)
    npc(b, 5, 1, 4, "dad")
    npc(b, 6, 1, 6, "mom")
    npc(b, 3, 1, 6, "kid", 0.6)
    npc(b, 4, 1, 7, "kid", 0.3)
    return b


def house_large(W, tag):
    b = new(f"town/{tag}/house_large", (11, 12, 16), W)
    b.ground(chance=0.06)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 9, 4, 14, 4
    wd = shell(b, W, X0, X1, Z0, Z1, TOP, 5)
    window_row(b, [(3, 2, Z0), (7, 2, Z0), (X0, 2, 6), (X0, 2, 8), (X0, 2, 12), (X1, 2, 6), (X1, 2, 8), (X1, 2, 12),
                   (3, 2, Z1), (7, 2, Z1)])
    # Porche con techito y barandal
    b.fill(X0, 0, 1, X1, 0, Z0 - 1, W["floor"])
    for x in (X0, X1):
        for y in (1, 2, 3):
            b.fence(x, y, 1, f"{wd}_fence")
    for x in range(X0, X1 + 1):
        for z in (1, 2, 3):
            b.slab(x, TOP, z, W["roof"][1])
    for x in (2, 3, 7, 8):
        b.fence(x, 1, 1, f"{wd}_fence")
    gh.chair(b, 3, 3, f"{wd}_stairs", "north")
    gh.chair(b, 7, 3, f"{wd}_stairs", "north")
    b.lantern(5, TOP - 1, 2)
    b.set(5, 0, 0, "dirt_path")
    # Pared de la recámara (z = 10) con su puerta
    for x in range(X0 + 1, X1):
        for y in range(1, TOP + 1):
            b.set(x, y, 10, W["wall"])
    b.door(5, 1, 10, f"{wd}_door", "south")
    beds = W["beds"]
    b.bed(7, 1, 12, beds[0], "south")
    b.bed(8, 1, 12, beds[0], "south")
    b.bed(2, 1, 12, beds[1], "south")
    b.bed(3, 1, 12, beds[2], "south")
    b.bed(4, 1, 12, beds[5], "south")
    b.set(6, 1, 13, "barrel", facing="up", open="false")
    b.set(5, 1, 13, "kiko:crib", facing="north")
    gh.potted(b, 6, 2, 13, "fern")
    b.lantern(5, TOP, 12)
    # Sala, cocina y comedor
    b.set(2, 1, 5, "furnace", facing="south", lit="false")
    b.set(3, 1, 5, "smoker", facing="south", lit="false")
    b.set(4, 1, 5, "crafting_table")
    b.chest(8, 1, 5, "south", W["loot"])
    b.set(8, 1, 6, "bookshelf")
    b.set(8, 2, 6, "bookshelf")
    b.set(8, 1, 7, "bookshelf")
    table(b, W, 4, 8)
    table(b, W, 5, 8)
    gh.chair(b, 3, 8, f"{wd}_stairs", "east")
    gh.chair(b, 6, 8, f"{wd}_stairs", "west")
    gh.chair(b, 4, 7, f"{wd}_stairs", "south")
    gh.chair(b, 5, 7, f"{wd}_stairs", "south")
    b.set(4, 2, 8, "candle", candles="2", lit="false", waterlogged="false")
    gh.potted(b, 2, 1, 9, "poppy")
    b.lantern(5, TOP, 6)
    front(b, 5)
    npc(b, 5, 1, 6, "dad")
    npc(b, 6, 1, 7, "mom")
    npc(b, 3, 1, 11, "kid")
    npc(b, 4, 1, 11, "kid", 0.6)
    npc(b, 6, 1, 11, "kid", 0.3)
    return b


def house_elders(W, tag):
    b = new(f"town/{tag}/house_elders", (11, 11, 12), W)
    b.ground(chance=0.04)
    b.clear()
    X0, X1, Z0, Z1, TOP = 2, 8, 4, 10, 3
    wd = shell(b, W, X0, X1, Z0, Z1, TOP, 5)
    window_row(b, [(3, 2, Z0), (7, 2, Z0), (X0, 2, 7), (X1, 2, 7), (5, 2, Z1)])
    # Jardín del frente con cerca baja, mecedoras y flores
    for x in range(0, 11):
        if x != 5:
            b.fence(x, 1, 1, f"{wd}_fence")
    for z in range(2, Z0 + 1):
        b.fence(0, 1, z, f"{wd}_fence")
        b.fence(10, 1, z, f"{wd}_fence")
    for x in (1, 9):
        b.fence(x, 1, Z0, f"{wd}_fence")
    # Entrada abierta con el caminito (v3.4.3 sin puerta, se veía rara)
    flowers(b, [(1, 2), (2, 2), (8, 2), (9, 2)])
    b.set(1, 1, 3, "rose_bush", half="lower")
    b.set(1, 2, 3, "rose_bush", half="upper")
    b.set(9, 1, 3, "lilac", half="lower")
    b.set(9, 2, 3, "lilac", half="upper")
    gh.chair(b, 3, 3, f"{wd}_stairs", "north")
    gh.chair(b, 7, 3, f"{wd}_stairs", "north")
    # Adentro: cama de los dos, librero, tejido, chimenea
    beds = W["beds"]
    b.bed(6, 1, 8, beds[5], "south")
    b.bed(7, 1, 8, beds[5], "south")
    b.set(3, 1, 9, "bookshelf")
    b.set(4, 1, 9, "bookshelf")
    b.set(3, 1, 5, "furnace", facing="south", lit="false")
    b.set(3, 1, 6, "loom", facing="east")
    b.chest(7, 1, 5, "south", W["loot"])
    table(b, W, 4, 7)
    gh.chair(b, 5, 7, f"{wd}_stairs", "west")
    gh.potted(b, 4, 2, 7, "cactus" if wd == "acacia" else "red_tulip")
    b.fill(4, 1, 5, 6, 1, 6, "green_carpet")
    b.lantern(5, TOP, 6)
    front(b, 5)
    npc(b, 5, 1, 6, "grandpa")
    npc(b, 6, 1, 6, "grandma")
    return b


# ------------------------------------------------------------------
# Lotes (también van en los lugares de las calles)
# ------------------------------------------------------------------
def huerto(W, tag):
    b = new(f"town/{tag}/huerto", (11, 6, 11), W)
    b.ground(chance=0.05)
    b.clear()
    crops = ["wheat", "carrots", "potatoes", "beetroots"]
    for x in range(1, 10):
        for z in range(2, 10):
            if x == 5:
                b.set(x, 0, z, "water")
                continue
            b.set(x, 0, z, "farmland", moisture="7")
            crop = crops[(x // 2 + z // 3) % 4]
            age = (x * 7 + z * 3) % (8 if crop != "beetroots" else 4)
            b.set(x, 1, z, crop, age=str(age))
    # Espantapájaros
    b.fence(5, 1, 6, f"{W['wood']}_fence")
    b.set(5, 0, 6, "dirt")
    b.set(5, 2, 6, "hay_block", axis="y")
    b.set(5, 3, 6, "carved_pumpkin", facing="north")
    b.set(0, 1, 2, "composter", level="4")
    b.barrel(10, 1, 2, "up")
    b.fill(0, 0, 0, 10, 0, 1, "grass_block", snowy="false")
    b.fill(5, 0, 0, 5, 0, 1, "dirt_path")
    lamp(b, 0, 10, W)
    lamp(b, 10, 10, W)
    front(b, 5)
    return b


def corral(W, tag):
    b = new(f"town/{tag}/corral", (11, 6, 12), W)
    b.ground(spots=("coarse_dirt",), chance=0.25)
    b.clear()
    wd = W["wood"]
    X0, X1, Z0, Z1 = 0, 10, 1, 11
    for x in range(X0, X1 + 1):
        b.fence(x, 1, Z0, f"{wd}_fence")
        b.fence(x, 1, Z1, f"{wd}_fence")
    for z in range(Z0, Z1 + 1):
        b.fence(X0, 1, z, f"{wd}_fence")
        b.fence(X1, 1, z, f"{wd}_fence")
    b.set(5, 1, Z0, f"{wd}_fence_gate", facing="north", open="false", in_wall="false", powered="false")
    # Tejabán al fondo con heno
    # Heno adentro del tejabán pero NO pegado a la cerca (si no, los animales se suben y se salen)
    b.set(2, 1, 9, "hay_block", axis="y")
    b.set(3, 1, 9, "hay_block", axis="y")
    b.set(2, 2, 9, "hay_block", axis="y")
    for x in (1, 4):
        for y in (1, 2, 3):
            b.fence(x, y, 9, f"{wd}_fence")
    for x in range(0, 6):
        for z in range(8, 12):
            b.slab(x, 4, z, W["roof"][1])
    # Bebedero y comida al centro (lejos de la cerca para que no se salgan)
    b.set(5, 1, 6, "water_cauldron", level="3")
    b.set(6, 1, 6, "hay_block", axis="y")
    b.set(5, 0, 0, "dirt_path")
    b.girl(3, 1, 4, "minecraft:cow")
    b.girl(7, 1, 8, "minecraft:cow")
    b.girl(7, 1, 3, "minecraft:sheep")
    b.girl(3, 1, 7, "minecraft:sheep")
    b.girl(8, 1, 5, "minecraft:chicken")
    front(b, 5)
    return b


def tree(W, tag):
    b = new(f"town/{tag}/tree", (7, 9, 7), W)
    b.ground(chance=0.1)
    b.clear()
    b.log(3, 1, 3, 3, 4, 3, W["log"], "y")
    for y, r in ((4, 2), (5, 2), (6, 1), (7, 0)):
        for x in range(3 - r, 4 + r):
            for z in range(3 - r, 4 + r):
                if abs(x - 3) + abs(z - 3) <= r + 1 and b.get(x, y, z) in (None, "air"):
                    b.leaves(x, y, z, W["leaves"])
    gh.chair(b, 1, 1, f"{W['wood']}_stairs", "north")
    gh.chair(b, 5, 1, f"{W['wood']}_stairs", "north")
    flowers(b, [(1, 4), (5, 5), (2, 5)])
    front(b, 3)
    return b


def well(W, tag):
    b = new(f"town/{tag}/well", (7, 7, 7), W)
    b.ground(chance=0.1)
    b.clear()
    b.fill(1, 0, 1, 5, 0, 5, W["pave"][0][0])
    for x in range(2, 5):
        for z in range(2, 5):
            b.set(x, 1, z, W["base"])
    b.set(3, 1, 3, "water")
    b.set(3, 0, 3, "water")
    for x, z in ((2, 2), (4, 2), (2, 4), (4, 4)):
        b.fence(x, 2, z, f"{W['wood']}_fence")
        b.fence(x, 3, z, f"{W['wood']}_fence")
    for x in range(2, 5):
        for z in range(2, 5):
            b.slab(x, 4, z, W["roof"][1])
    b.set(3, 3, 3, "lantern", hanging="true", waterlogged="false")
    flowers(b, [(1, 1), (5, 1), (1, 5), (5, 5)])
    front(b, 3)
    return b


# ------------------------------------------------------------------
# Calles (siguen el terreno). Conector de atrás "street_in", de enfrente "street_out", lados "house_slot".
# ------------------------------------------------------------------
def street_base(b, L):
    for x in range(5):
        for z in range(L):
            b.set(x, 0, z, "dirt_path" if 1 <= x <= 3 else "grass_block", **({"snowy": "false"} if not 1 <= x <= 3 else {}))


def street(W, tag):
    L = 12
    b = new(f"town/{tag}/street", (5, 5, L), W)
    street_base(b, L)
    for z in range(L):
        if (z * 7) % 5 == 0:
            b.set(2, 0, z, "coarse_dirt")
    lamp(b, 0, 2, W)
    lamp(b, 4, 9, W)
    jig(b, 2, 0, 0, "north", "kiko:street_in", final="minecraft:dirt_path")
    jig(b, 2, 0, L - 1, "south", "kiko:street_out", "kiko:street_in", f"kiko:town/{tag}/streets", "minecraft:dirt_path")
    jig(b, 0, 0, 6, "west", "kiko:house_slot", "kiko:house_front", f"kiko:town/{tag}/houses", "minecraft:grass_block")
    jig(b, 4, 0, 6, "east", "kiko:house_slot", "kiko:house_front", f"kiko:town/{tag}/houses", "minecraft:grass_block")
    return b


def street_cross(W, tag):
    b = new(f"town/{tag}/street_cross", (5, 5, 5), W)
    for x in range(5):
        for z in range(5):
            b.set(x, 0, z, "dirt_path")
    lamp(b, 0, 0, W)
    jig(b, 2, 0, 0, "north", "kiko:street_in", final="minecraft:dirt_path")
    jig(b, 2, 0, 4, "south", "kiko:street_out", "kiko:street_in", f"kiko:town/{tag}/streets", "minecraft:dirt_path")
    jig(b, 0, 0, 2, "west", "kiko:street_out", "kiko:street_in", f"kiko:town/{tag}/streets", "minecraft:dirt_path")
    jig(b, 4, 0, 2, "east", "kiko:street_out", "kiko:street_in", f"kiko:town/{tag}/streets", "minecraft:dirt_path")
    return b


def street_end(W, tag):
    b = new(f"town/{tag}/street_end", (5, 5, 4), W)
    street_base(b, 4)
    b.set(1, 0, 3, "grass_block", snowy="false")
    b.set(3, 0, 3, "grass_block", snowy="false")
    b.set(2, 0, 3, "coarse_dirt")
    gh.chair(b, 2, 3, f"{W['wood']}_stairs", "south")
    flowers(b, [(1, 3), (3, 3), (0, 2), (4, 2)])
    lamp(b, 0, 3, W)
    lamp(b, 4, 3, W)
    jig(b, 2, 0, 0, "north", "kiko:street_in", final="minecraft:dirt_path")
    return b


# ------------------------------------------------------------------
# Plaza (pieza de inicio). Salidas a calles al norte, este y sur; al oeste la cancha.
# ------------------------------------------------------------------
def stall(b, W, x0, back, front, wool, goods):
    """Puestito de 3 de ancho. Atrás los postes (y ahí se para el vendedor), enfrente el mostrador, arriba el toldo."""
    wd = W["wood"]
    for x in (x0, x0 + 2):
        for y in (1, 2):
            b.fence(x, y, back, f"{wd}_fence")
    for x in range(x0, x0 + 3):
        for z in (back, front):
            b.set(x, 3, z, wool if (x + z) % 2 == 0 else "white_wool")
    for i, x in enumerate(range(x0, x0 + 3)):
        b.barrel(x, 1, front, "up")
        g = goods[i]
        if g.startswith("potted_"):
            b.set(x, 2, front, g)
        elif g == "cake":
            b.set(x, 2, front, "cake", bites="0")
        elif g in ("barrel", "smoker", "loom", "crafting_table"):
            pass
        elif g == "hay_block":
            b.set(x, 2, front, "hay_block", axis="y")
        else:
            b.set(x, 2, front, g)


def plaza(W, tag):
    S = 17
    b = new(f"town/{tag}/plaza", (S, 8, S), W)
    b.ground(chance=0)
    b.clear()
    b.mix(0, 0, 0, S - 1, 0, S - 1, W["pave"])
    c = S // 2
    # Fuente: aro de piedra con agua y un pilar al centro que echa agua
    wall = {"oak": "stone_brick_wall", "spruce": "mossy_cobblestone_wall", "acacia": "sandstone_wall"}[tag]
    rim = {"oak": "stone_bricks", "spruce": "mossy_cobblestone", "acacia": "cut_sandstone"}[tag]
    for x in range(c - 3, c + 4) if tag != "spruce" else []:
        for z in range(c - 3, c + 4):
            edge = x in (c - 3, c + 3) or z in (c - 3, c + 3)
            corner = x in (c - 3, c + 3) and z in (c - 3, c + 3)
            if corner:
                continue
            if edge:
                b.set(x, 1, z, rim)
                b.set(x, 0, z, rim)
            else:
                b.set(x, 0, z, "water")
    if tag != "spruce":
        for y in (1, 2):
            b.set(c, y, c, rim)
        b.set(c, 3, c, wall, up="true", north="none", south="none", east="none", west="none", waterlogged="false")
        b.set(c, 4, c, "water")
        # El agua ya va escurriendo (si no, sale como cubo quieto hasta que algo la actualiza)
        for dx, dz in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            b.set(c + dx, 4, c + dz, "water", level="1")
            for y in (1, 2, 3):
                b.set(c + dx, y, c + dz, "water", level="8")
    else:
        # En la taiga hace frío: fogata con troncos alrededor para sentarse
        for x in range(c - 1, c + 2):
            for z in range(c - 1, c + 2):
                b.set(x, 0, z, "cobblestone")
        b.set(c, 1, c, "campfire", lit="true", signal_fire="false", facing="north", waterlogged="false")
        for x in range(c - 1, c + 2):
            b.log(x, 1, c - 3, x, 1, c - 3, "stripped_spruce_log", "x")
            b.log(x, 1, c + 3, x, 1, c + 3, "stripped_spruce_log", "x")
        for z in range(c - 1, c + 2):
            b.log(c - 3, 1, z, c - 3, 1, z, "stripped_spruce_log", "z")
            b.log(c + 3, 1, z, c + 3, 1, z, "stripped_spruce_log", "z")
    # Faroles en las esquinas, bancas viendo a la fuente, jardineras
    for x, z in ((2, 2), (S - 3, 2), (2, S - 3), (S - 3, S - 3)):
        lamp(b, x, z, W, h=4)
    # v3.5.1 solo la banca de en medio (las de los lados tapaban los puestos)
    for x in [c] if tag != "spruce" else []:
        gh.chair(b, x, c - 5, f"{W['wood']}_stairs", "north")
        gh.chair(b, x, c + 5, f"{W['wood']}_stairs", "south")
    for z in range(c - 2, c + 3, 2) if tag != "spruce" else []:
        gh.chair(b, c - 5, z, f"{W['wood']}_stairs", "west")
        gh.chair(b, c + 5, z, f"{W['wood']}_stairs", "east")
    for x, z in ((0, 0), (S - 1, 0), (0, S - 1), (S - 1, S - 1), (1, 0), (0, 1), (S - 2, 0), (S - 1, 1),
                 (0, S - 2), (1, S - 1), (S - 1, S - 2), (S - 2, S - 1)):
        b.set(x, 0, z, "grass_block", snowy="false")
    flowers(b, [(0, 0), (1, 0), (0, 1), (S - 1, 0), (S - 2, 0), (S - 1, 1), (0, S - 1), (1, S - 1), (0, S - 2),
                (S - 1, S - 1), (S - 2, S - 1), (S - 1, S - 2)])
    # Campana del pueblo
    b.fence(c + 4, 1, c + 4, f"{W['wood']}_fence")
    b.set(c + 4, 2, c + 4, "bell", attachment="floor", facing="north", powered="false")
    # v3.8 dos guardias de la plaza (hombre o mujer al azar, con su rango)
    npc(b, c + 3, 1, c + 5, "guard")
    npc(b, c + 5, 1, c + 3, "guard")
    # v3.5 mercadito: 4 puestos con toldo (los domingos los vecinos con oficio venden ahí)
    for (x0, back, front), wool, goods in (((4, 1, 2), "red_wool", ["melon", "pumpkin", "hay_block"]),
                                          ((10, 1, 2), "yellow_wool", ["cake", "barrel", "smoker"]),
                                          ((4, S - 2, S - 3), "lime_wool", ["potted_poppy", "potted_dandelion", "potted_cornflower"]),
                                          ((10, S - 2, S - 3), "light_blue_wool", ["loom", "barrel", "crafting_table"])):
        stall(b, W, x0, back, front, wool, goods)
    # Tablero de encargos arriba del borde de la fuente (en la taiga junto a la fogata), así no estorba
    if tag != "spruce":
        b.set(c, 2, c + 3, "kiko:notice_board", facing="south")
    else:
        b.set(c, 1, c + 4, "kiko:notice_board", facing="south")
    # Conectores
    pave_final = "minecraft:" + W["pave"][0][0]
    jig(b, c, 0, 0, "north", "kiko:plaza_exit", "kiko:street_in", f"kiko:town/{tag}/streets_first", pave_final)
    jig(b, c, 0, S - 1, "south", "kiko:plaza_exit", "kiko:street_in", f"kiko:town/{tag}/streets_first", pave_final)
    jig(b, S - 1, 0, c, "east", "kiko:plaza_exit", "kiko:street_in", f"kiko:town/{tag}/streets_first", pave_final)
    jig(b, 0, 0, c, "west", "kiko:plaza_lot", "kiko:lot_front", f"kiko:town/{tag}/big_lots", pave_final)
    # Todo sube uno: abajo va la base (tierra, y piedra bajo la fuente para que no se salga el agua)
    gh.lift(b, 1)
    b.jigs = [(x, y + 1, z) + tuple(rest) for (x, y, z, *rest) in b.jigs]
    for x in range(S):
        for z in range(S):
            b.set(x, 0, z, rim if (abs(x - c) <= 3 and abs(z - c) <= 3 and tag != "spruce") else "dirt")
    return b


# ------------------------------------------------------------------
# Cancha de futbol (pegada a la plaza). Porterías de cerca con 3 de hueco (las que reconoce la pelota).
# ------------------------------------------------------------------
def soccer(W, tag):
    # Lo corto (15) pegado a la plaza y lo largo (23) hacia afuera, así no les quita lugar a las casas de las calles
    LX, LZ = 15, 23
    b = new(f"town/{tag}/soccer", (LX, 6, LZ), W)
    b.ground(chance=0)
    b.clear()
    wd = W["wood"]
    mx, mz = LX // 2, LZ // 2
    for x in range(LX):
        for z in range(LZ):
            line = (x in (1, LX - 2) and 1 <= z <= LZ - 2) or (z in (1, LZ - 2) and 1 <= x <= LX - 2) or \
                   (z == mz and 1 <= x <= LX - 2)
            circle = abs(math.hypot(x - mx, z - mz) - 2.6) < 0.5
            b.set(x, 0, z, "white_concrete" if (line or circle) else "grass_block", **({} if (line or circle) else {"snowy": "false"}))
    for gz in (1, LZ - 2):
        for x in (mx - 2, mx + 2):
            b.fence(x, 1, gz, f"{wd}_fence")
            b.fence(x, 2, gz, f"{wd}_fence")
        for x in range(mx - 2, mx + 3):
            b.fence(x, 3, gz, f"{wd}_fence")
    # Bancas a un lado y faroles en las esquinas
    for z in range(5, LZ - 5, 3):
        gh.chair(b, LX - 1, z, f"{wd}_stairs", "east")
    for x, z in ((0, 0), (LX - 1, 0), (0, LZ - 1), (LX - 1, LZ - 1)):
        lamp(b, x, z, W, h=4)
    b.girl(mx, 1, mz, "kiko:soccer_ball")
    jig(b, mx, 0, 0, "north", "kiko:lot_front", final="minecraft:grass_block")
    return b


# ------------------------------------------------------------------
# Pools, estructuras y sets
# ------------------------------------------------------------------
def element(loc, proj="rigid"):
    return {"element_type": "minecraft:single_pool_element", "location": loc, "projection": proj, "processors": "minecraft:empty"}


POOLS = {
    # pool: ([(pieza, peso, proyección)], fallback)
    "plaza": ([("plaza", 1, "rigid")], "minecraft:empty"),
    "streets_first": ([("street", 1, "terrain_matching")], "street_ends"),
    "streets": ([("street", 6, "terrain_matching"), ("street_cross", 2, "terrain_matching"), ("street_end", 1, "terrain_matching")], "street_ends"),
    "street_ends": ([("street_end", 1, "terrain_matching")], "minecraft:empty"),
    "houses": ([("house_small", 4, "rigid"), ("house_medium", 4, "rigid"), ("house_large", 2, "rigid"), ("house_elders", 2, "rigid"),
                ("huerto", 1, "rigid"), ("corral", 1, "rigid"), ("well", 1, "rigid")], "decor"),
    "decor": ([("tree", 2, "rigid"), ("well", 1, "rigid")], "minecraft:empty"),
    "big_lots": ([("soccer", 1, "rigid")], "minecraft:empty"),
}

BIOMES = {
    "oak": ["minecraft:plains", "minecraft:sunflower_plains", "minecraft:meadow", "minecraft:forest", "minecraft:flower_forest",
            "minecraft:birch_forest"],
    "spruce": ["minecraft:taiga", "minecraft:snowy_taiga", "minecraft:snowy_plains", "minecraft:old_growth_pine_taiga",
               "minecraft:old_growth_spruce_taiga"],
    "acacia": ["minecraft:savanna", "minecraft:savanna_plateau"],
}


def worldgen():
    for tag in WOODS:
        for pool, (elems, fb) in POOLS.items():
            gs.dump(f"kiko/worldgen/template_pool/town/{tag}/{pool}.json", {
                "fallback": fb if fb.startswith("minecraft:") else f"kiko:town/{tag}/{fb}",
                "elements": [{"weight": w, "element": element(f"kiko:town/{tag}/{n}", proj)} for n, w, proj in elems]})
        gs.dump(f"kiko/worldgen/structure/town_{tag}.json", {
            "type": "minecraft:jigsaw",
            "biomes": f"#kiko:has_structure/town_{tag}",
            "step": "surface_structures",
            "spawn_overrides": {},
            "terrain_adaptation": "beard_thin",
            "start_pool": f"kiko:town/{tag}/plaza",
            "size": 4,
            "start_height": {"absolute": -2},
            "project_start_to_heightmap": "WORLD_SURFACE_WG",
            "max_distance_from_center": 80,
            "use_expansion_hack": False})
        gs.dump(f"kiko/tags/worldgen/biome/has_structure/town_{tag}.json", {"replace": False, "values": BIOMES[tag]})
    gs.dump("kiko/worldgen/structure_set/towns.json", {
        "structures": [{"structure": f"kiko:town_{t}", "weight": 1} for t in WOODS],
        "placement": {"type": "minecraft:random_spread", "spacing": 44, "separation": 14, "salt": 63410925,
                      "exclusion_zone": {"other_set": "minecraft:villages", "chunk_count": 6}}})


BUILDERS = [plaza, street, street_cross, street_end, house_small, house_medium, house_large, house_elders,
            huerto, corral, tree, well, soccer]


# ------------------------------------------------------------------
# v3.9 Escuela (la construyen los vecinos con TownGrowth) y adornos de feria para la plaza
# ------------------------------------------------------------------
def school(W, tag):
    b = new(f"town/{tag}/school", (15, 13, 16), W)
    b.ground(chance=0.04)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 13, 3, 14, 5
    wd = shell(b, W, X0, X1, Z0, Z1, TOP, 7)
    window_row(b, [(3, 2, Z0), (4, 2, Z0), (10, 2, Z0), (11, 2, Z0), (3, 3, Z0), (4, 3, Z0), (10, 3, Z0), (11, 3, Z0),
                   (X0, 2, 6), (X0, 2, 9), (X0, 2, 12), (X1, 2, 6), (X1, 2, 9), (X1, 2, 12),
                   (X0, 3, 6), (X0, 3, 9), (X0, 3, 12), (X1, 3, 6), (X1, 3, 9), (X1, 3, 12)])
    # Pizarrón al fondo (verde, con su repisa de gis)
    for x in range(4, 11):
        for y in (2, 3):
            b.set(x, y, Z1 - 1, "green_terracotta")
        b.slab(x, 1, Z1 - 1, "smooth_stone_slab")
    # Escritorio de la maestra
    b.table(7, 1, Z1 - 3, wd)
    gh.chair(b, 7, Z1 - 2, f"{wd}_stairs", "south")
    b.set(8, 1, Z1 - 3, "lectern", facing="north", has_book="false", powered="false")
    gh.potted(b, 6, 2, Z1 - 3, "red_tulip")
    # Pupitres en 3 filas de 4 (bancos viendo hacia el pizarrón)
    for z in (5, 7, 9):
        for x in (3, 5, 9, 11):
            b.table(x, 1, z, wd)
            gh.chair(b, x, z - 1, f"{wd}_stairs", "north")
    # Libreros y mapas a los lados
    for z in (5, 6, 10, 11):
        b.set(X0 + 1, 1, z, "bookshelf")
        b.set(X1 - 1, 1, z, "bookshelf")
    b.set(X1 - 1, 2, 8, "cartography_table")
    b.set(X0 + 1, 1, 8, "kiko:notice_board", facing="east")
    b.lantern(4, TOP, 7)
    b.lantern(10, TOP, 7)
    b.lantern(7, TOP, 11)
    # Asta con la bandera (verde, blanco y rojo) y la campana de la entrada
    for y in range(1, 7):
        b.fence(1, y, 1, f"{wd}_fence")
    for i, col in enumerate(("green_wool", "white_wool", "red_wool")):
        b.set(2 + i, 6, 1, col)
        b.set(2 + i, 5, 1, col)
    flowers(b, [(10, 0), (11, 0), (12, 0), (10, 1), (12, 1)])
    front(b, 7)
    return b


FIESTA_COLORS = ["pink", "orange", "yellow", "lime", "light_blue", "magenta", "red", "cyan"]


def fiesta(b, S=17):
    """Adornos de feria sobre la plaza (los pone y los quita el juego). Postes en las esquinas y papel picado
    de vidrios de colores cruzando en X y por las orillas, con faroles en los postes."""
    wd = b.W["wood"]
    posts = [(0, 0), (S - 1, 0), (0, S - 1), (S - 1, S - 1), (S // 2, 0), (S // 2, S - 1), (0, S // 2), (S - 1, S // 2)]
    for x, z in posts:
        for y in range(1, 6):
            b.fence(x, y, z, f"{wd}_fence")
        b.set(x, 6, z, "lantern", hanging="false", waterlogged="false")

    def line(a, c):
        (x0, z0), (x1, z1) = a, c
        n = max(abs(x1 - x0), abs(z1 - z0))
        for i in range(1, n):
            x = x0 + round((x1 - x0) * i / n)
            z = z0 + round((z1 - z0) * i / n)
            col = FIESTA_COLORS[(x + z) % len(FIESTA_COLORS)]
            b.set(x, 5, z, f"{col}_stained_glass_pane")
    for a, c in (((0, 0), (S - 1, 0)), ((0, S - 1), (S - 1, S - 1)), ((0, 0), (0, S - 1)), ((S - 1, 0), (S - 1, S - 1)),
                 ((S // 2, 0), (S // 2, S - 1)), ((0, S // 2), (S - 1, S // 2))):
        line(a, c)
    # Mesas con pasteles y comida junto a la fuente
    for x, z in ((3, 6), (13, 6), (3, 10), (13, 10)):
        b.set(x, 1, z, "white_wool")
        b.set(x, 2, z, "candle_cake", lit="false")


def build_all():
    out = {}
    for tag, W in WOODS.items():
        out[tag] = {}
        for fn in BUILDERS:
            b = fn(W, tag)
            out[tag][fn.__name__] = b
    return out


# ------------------------------------------------------------------
# Vista previa: arma un pueblo de mentiritas con las mismas reglas del jigsaw (2D) y lo pinta desde arriba
# ------------------------------------------------------------------
ROT = {"north": 0, "east": 1, "south": 2, "west": 3}
DIRV = {"north": (0, -1), "east": (1, 0), "south": (0, 1), "west": (-1, 0)}
NAMES = ["north", "east", "south", "west"]


def rot_dir(d, r):
    return NAMES[(ROT[d] + r) % 4]


def rot_pt(x, z, r, sx, sz):
    """Gira (x, z) dentro de una pieza de sx por sz, r cuartos de vuelta en el sentido del reloj."""
    for _ in range(r % 4):
        x, z = sz - 1 - z, x
        sx, sz = sz, sx
    return x, z


def simulate(pieces, tag, seed, depth_max=4):
    rng = random.Random(seed)
    placed = []   # (b, r, ox, oz, sx, sz)

    def footprint(b, r):
        sx, sz = b.size[0], b.size[2]
        return (sz, sx) if r % 2 else (sx, sz)

    def overlaps(ox, oz, sx, sz):
        for (_, _, px, pz, psx, psz) in placed:
            if ox < px + psx and px < ox + sx and oz < pz + psz and pz < oz + sz:
                return True
        return abs(ox) > 80 or abs(oz) > 80 or abs(ox + sx) > 80 or abs(oz + sz) > 80

    start = pieces["plaza"]
    placed.append((start, 0, 0, 0, start.size[0], start.size[2]))
    queue = [(placed[0], 0)]
    while queue:
        (b, r, ox, oz, sx, sz), depth = queue.pop(0)
        for (jx, jy, jz, facing, name, target, pool, final) in b.jigs:
            if target == "minecraft:empty":
                continue
            wx, wz = rot_pt(jx, jz, r, b.size[0], b.size[2])
            wx, wz = wx + ox, wz + oz
            f = rot_dir(facing, r)
            ax, az = wx + DIRV[f][0], wz + DIRV[f][1]
            pool_name = pool.split("/")[-1]
            elems, fb = POOLS[pool_name]
            options = []
            if depth != depth_max:
                bag = [n for n, w, _ in elems for _ in range(w)]
                rng.shuffle(bag)
                seen = set()
                options = [n for n in bag if not (n in seen or seen.add(n))]
            fbname = fb.split("/")[-1]
            if not fb.startswith("minecraft:"):
                options += [n for n, _, _ in POOLS[fbname][0]]
            done = False
            for n in options:
                cb = pieces[n]
                for (cx, cy, cz, cf, cname, _, _, _) in cb.jigs:
                    if cname != target:
                        continue
                    want = NAMES[(ROT[f] + 2) % 4]
                    rr = (ROT[want] - ROT[cf]) % 4
                    csx, csz = footprint(cb, rr)
                    lx, lz = rot_pt(cx, cz, rr, cb.size[0], cb.size[2])
                    nox, noz = ax - lx, az - lz
                    if overlaps(nox, noz, csx, csz):
                        continue
                    entry = (cb, rr, nox, noz, csx, csz)
                    placed.append(entry)
                    queue.append((entry, depth + 1))
                    done = True
                    break
                if done:
                    break
    return placed


def top_colors(b):
    """Color visto desde arriba de cada columna (x, z) de la pieza."""
    best = {}
    for (x, y, z), (name, props, _) in b.blocks.items():
        short = name.split(":")[-1]
        if short == "air":
            continue
        if short == "jigsaw":
            short = "dirt_path"
        if (x, z) not in best or y > best[(x, z)][0]:
            best[(x, z)] = (y, short)
    return best


def preview(builds, out):
    from PIL import Image, ImageDraw, ImageFont
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 16)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 12)
    os.makedirs(out, exist_ok=True)
    # 1) Un pueblo armado de cada madera, visto desde arriba
    P = 5
    tiles = []
    for tag, seed in (("oak", 4), ("spruce", 11), ("acacia", 23)):
        placed = simulate(builds[tag], tag, seed)
        minx = min(p[2] for p in placed) - 2
        minz = min(p[3] for p in placed) - 2
        maxx = max(p[2] + p[4] for p in placed) + 2
        maxz = max(p[3] + p[5] for p in placed) + 2
        img = Image.new("RGB", ((maxx - minx) * P, (maxz - minz) * P + 30), (96, 140, 70) if tag != "acacia" else (150, 150, 80))
        d = ImageDraw.Draw(img)
        npcs = houses = 0
        for (b, r, ox, oz, sx, sz) in placed:
            cols = top_colors(b)
            for (x, z), (y, short) in cols.items():
                col = gs.COLORS.get(short, (200, 0, 200))
                if short.startswith("potted_"):
                    col = (150, 90, 60)
                wx, wz = rot_pt(x, z, r, b.size[0], b.size[2])
                px, pz = (wx + ox - minx) * P, (wz + oz - minz) * P + 30
                d.rectangle([px, pz, px + P - 1, pz + P - 1], fill=col)
            for (x, y, z, eid, extra) in b.entities:
                if eid != NPC:
                    continue
                ch = float(extra["TownChance"])
                npcs += ch
                wx, wz = rot_pt(int(x), int(z), r, b.size[0], b.size[2])
                px, pz = (wx + ox - minx) * P + P // 2, (wz + oz - minz) * P + 30 + P // 2
                d.ellipse([px - 3, pz - 3, px + 3, pz + 3], fill=(255, 220, 120), outline=(60, 40, 20))
            if b.name.split("/")[-1].startswith("house"):
                houses += 1
        d.text((8, 6), f"{tag}   {houses} casas   ~{round(npcs)} vecinos", fill=(255, 255, 255), font=font)
        tiles.append(img)
    W = sum(t.width for t in tiles) + 20 * (len(tiles) - 1)
    H = max(t.height for t in tiles)
    sheet = Image.new("RGB", (W, H), (40, 42, 54))
    x = 0
    for t in tiles:
        sheet.paste(t, (x, 0))
        x += t.width + 20
    sheet.save(os.path.join(out, "pueblo_arriba.png"))
    # 2) Las casas por fuera y sin techo (roble)
    keep = [("house_small", 3), ("house_medium", 4), ("house_large", 4), ("house_elders", 3), ("huerto", 3), ("corral", 3),
            ("plaza", 3), ("soccer", 3)]
    for tag in WOODS:
        d = os.path.join(out, tag)
        os.makedirs(d, exist_ok=True)
        items = []
        for k, cut in keep:
            b = builds[tag][k]
            b.full_name, b.name = b.name, k
            items.append((b, cut))
        gs.preview(items, d)
        for b, _ in items:
            b.name = b.full_name


def main(preview_dir=None):
    builds = build_all()
    for tag in builds:
        for b in builds[tag].values():
            b.save()
    # v3.9 la escuela no sale al generar el pueblo, la construyen los vecinos
    for tag, W in WOODS.items():
        school(W, tag).save()
    worldgen()
    if preview_dir:
        preview(builds, preview_dir)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
