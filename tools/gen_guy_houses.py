"""
Casas de los chicos (v3.3). Usa el mismo motor de gen_structures.py.

  tono_ranch    el rancho de Toño (cabaña de troncos, porche, corral con vacas, milpa con agua)
  leo_shack     la palapa de Leo (casita de playa, estanque con muelle, redes y barriles)
  dante_tower   la torre de Dante (torre de piedra de dos pisos con almenas, campana y armadura)
  ivan_mine     la casa de Iván (cabaña de piedra, entrada a la mina con rieles, minerales de adorno)
  emi_studio    el estudio de Emi (casa oscura con tocadiscos, bloques musicales, amatistas y escenario)
  beto_shop     la tienda de Beto (casa con puesto al frente, toldo de colores, barriles y mostrador)

Cada casa trae a su chico adentro y una cama matrimonial para que duerma ahí.
Uso   python tools/gen_guy_houses.py [carpeta_preview]
"""
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_structures as gs  # noqa: E402
from gen_structures import Build  # noqa: E402

gs.COLORS.update({
    "oak_planks": (162, 130, 78), "stripped_oak_log": (176, 144, 90), "oak_stairs": (162, 130, 78), "oak_slab": (162, 130, 78),
    "oak_door": (150, 120, 70), "spruce_log": (80, 60, 36), "dark_oak_stairs": (70, 46, 24), "dark_oak_slab": (70, 46, 24),
    "dark_oak_door": (70, 46, 24), "dark_oak_fence": (66, 44, 24), "stripped_dark_oak_log": (96, 72, 48),
    "birch_stairs": (214, 200, 150), "birch_slab": (214, 200, 150), "birch_fence": (214, 200, 150), "birch_log": (220, 216, 206),
    "stripped_birch_log": (200, 180, 130), "light_blue_terracotta": (114, 110, 138), "cyan_wool": (20, 140, 150),
    "blue_wool": (50, 60, 160), "white_concrete": (220, 222, 224), "sand": (220, 210, 160), "sandstone": (216, 204, 156),
    "stone_brick_stairs": (122, 122, 122), "stone_brick_slab": (122, 122, 122), "stone_brick_wall": (122, 122, 122),
    "mossy_stone_bricks": (104, 120, 96), "polished_andesite": (136, 138, 136), "bell": (230, 190, 60),
    "target": (220, 200, 190), "red_wall_banner": (160, 40, 40), "iron_door": (190, 190, 190),
    "cobbled_deepslate": (80, 80, 84), "deepslate_bricks": (70, 70, 74), "cobblestone_stairs": (110, 110, 110),
    "cobblestone_slab": (110, 110, 110), "cobblestone_wall": (110, 110, 110), "rail": (140, 120, 90), "minecart": (120, 120, 126),
    "iron_ore": (150, 136, 124), "gold_ore": (170, 150, 90), "coal_ore": (100, 100, 100), "copper_ore": (150, 120, 100),
    "diamond_ore": (120, 170, 170), "redstone_ore": (150, 90, 90), "emerald_block": (40, 190, 90),
    "amethyst_block": (150, 100, 200), "amethyst_cluster": (180, 130, 220), "purple_carpet": (130, 60, 170),
    "black_carpet": (30, 30, 34), "purple_wool": (130, 60, 170), "magenta_wool": (190, 70, 180), "yellow_wool": (240, 200, 50),
    "red_wool": (170, 40, 40), "green_wool": (80, 120, 40), "lime_wool": (120, 190, 40), "orange_wool": (230, 120, 30),
    "farmland": (100, 64, 40), "hay_block": (200, 170, 50), "oak_fence_gate": (160, 130, 80), "cauldron": (70, 70, 74),
    "smithing_table": (60, 50, 60), "fletching_table": (180, 160, 120), "lectern": (160, 120, 70),
    "melon": (110, 160, 50), "pumpkin": (220, 130, 30), "beehive": (210, 170, 80), "oxeye_daisy": (240, 240, 230),
    "dandelion": (240, 220, 60), "sugar_cane": (140, 190, 90), "tall_grass": (90, 150, 60),
})


def lift(b, dy):
    """Sube toda la casa dy bloques (para que el estanque pueda ser hondo)."""
    b.blocks = {(x, y + dy, z): v for (x, y, z), v in b.blocks.items() if y + dy < b.size[1]}
    b.entities = [(x, y + dy, z, e, extra) for (x, y, z, e, extra) in b.entities]


def box(b, X0, X1, Z0, Z1, TOP, wall, corner=None, base=None):
    """Paredes de una casa (y troncos en las esquinas)."""
    for y in range(1, TOP + 1):
        blk = base if (base and y == 1) else wall
        for x in range(X0, X1 + 1):
            b.set(x, y, Z0, blk)
            b.set(x, y, Z1, blk)
        for z in range(Z0, Z1 + 1):
            b.set(X0, y, z, blk)
            b.set(X1, y, z, blk)
    if corner:
        for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1)):
            b.log(x, 1, z, x, TOP, z, corner, "y")


def ceiling(b, X0, X1, Z0, Z1, TOP, plank, beam):
    """Techo plano por dentro (así no se ve el hueco del tejado y las lámparas cuelgan de algo) con vigas."""
    for x in range(X0 + 1, X1):
        for z in range(Z0 + 1, Z1):
            b.set(x, TOP + 1, z, plank)
    for z in range(Z0 + 2, Z1 - 1, 3):
        b.log(X0 + 1, TOP + 1, z, X1 - 1, TOP + 1, z, beam, "x")


def chair(b, x, z, block, facing):
    b.stairs(x, 1, z, block, facing)


def potted(b, x, y, z, plant):
    b.set(x, y, z, "potted_" + plant)


def windows(b, spots, block="glass_pane"):
    for x, y, z in spots:
        b.fill(x, y, z, x, y + 1, z, block)


# ------------------------------------------------------------------
def tono_ranch():
    b = Build("tono_ranch", (17, 13, 17))
    b.ground(spots=("coarse_dirt", "dirt_path"), chance=0.08)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 9, 6, 13, 4
    b.fill(X0, 0, Z0, X1, 0, Z1, "spruce_planks")
    box(b, X0, X1, Z0, Z1, TOP, "spruce_planks", corner="spruce_log", base="cobblestone")
    for z in (Z0, Z1):
        b.log(X0 + 1, TOP, z, X1 - 1, TOP, z, "stripped_spruce_log", "x")
    b.door(5, 1, Z0, "spruce_door", "south")
    windows(b, [(3, 2, Z0), (7, 2, Z0), (X0, 2, 9), (X0, 2, 11), (X1, 2, 9), (X1, 2, 11), (5, 2, Z1)])
    b.gable(0, 10, Z0 - 1, Z1 + 1, TOP + 1, "spruce_stairs", "spruce_slab", "spruce_planks", ends=(Z0, Z1))
    # Porche
    b.fill(X0, 0, 3, X1, 0, Z0 - 1, "spruce_planks")
    for x in (X0, X1):
        b.fence(x, 1, 3)
        b.fence(x, 2, 3)
        b.fence(x, 3, 3)
    for x in range(X0, X1 + 1):
        b.slab(x, 4, 3, "spruce_slab")
        b.slab(x, 4, 4, "spruce_slab")
        b.slab(x, 4, 5, "spruce_slab")
    b.stairs(3, 1, 4, "spruce_stairs", "north")
    b.lantern(5, 3, 4)
    b.set(8, 1, 4, "hay_block", axis="y")
    # Adentro: recámara al fondo a la derecha, cocinita a la izquierda, mesa con sillas al centro
    ceiling(b, X0, X1, Z0, Z1, TOP, "spruce_planks", "stripped_spruce_log")
    b.fill(3, 1, 8, 6, 1, 11, "brown_carpet")
    b.kiko_bed(8, 11, 1, "south", block="kiko:bed_ranch")
    b.barrel(6, 1, 12, "up")
    potted(b, 6, 2, 12, "dandelion")
    b.set(2, 1, 12, "smoker", facing="east", lit="false")
    b.set(2, 1, 11, "furnace", facing="east", lit="false")
    b.set(2, 1, 10, "crafting_table")
    b.chest(2, 1, 8, "east", "kiko:chests/tono_ranch")
    b.barrel(2, 1, 7, "east", "kiko:chests/tono_ranch")
    b.barrel(2, 2, 7, "east")
    b.table(4, 1, 10, "spruce")
    chair(b, 4, 9, "spruce_stairs", "north")
    chair(b, 4, 11, "spruce_stairs", "south")
    b.set(4, 2, 10, "candle", candles="2", lit="false", waterlogged="false")
    b.set(8, 1, 7, "hay_block", axis="y")
    b.set(7, 1, 7, "composter", level="3")
    b.set(8, 2, 7, "lantern", hanging="false", waterlogged="false")
    b.lantern(4, TOP, 8)
    b.lantern(6, TOP, 10)
    # Corral con vacas
    CX0, CX1, CZ0, CZ1 = 11, 16, 6, 13
    for x in range(CX0, CX1 + 1):
        b.fence(x, 1, CZ0, "oak_fence")
        b.fence(x, 1, CZ1, "oak_fence")
    for z in range(CZ0, CZ1 + 1):
        b.fence(CX0, 1, z, "oak_fence")
        b.fence(CX1, 1, z, "oak_fence")
    b.set(CX0, 1, 9, "oak_fence_gate", facing="east", open="false", in_wall="false", powered="false")
    # Bebedero y heno al centro, lejos de la cerca (si no, las vacas se suben y se salen)
    b.set(13, 1, 9, "water_cauldron", level="3")
    b.set(14, 1, 10, "hay_block", axis="y")
    b.girl(12, 1, 7, "minecraft:cow")
    b.girl(15, 1, 12, "minecraft:cow")
    # Milpa con agua al centro
    for x in range(11, 17):
        for z in range(0, 5):
            if (x, z) == (13, 2):
                b.set(x, 0, z, "water", level="0")
            else:
                b.set(x, 0, z, "farmland", moisture="7")
                crop = ["wheat", "carrots", "potatoes"][(x + z) % 3]
                b.set(x, 1, z, crop, age=str(3 + (x * z) % 5 if crop == "wheat" else (x + z) % 8))
    b.fill(5, 0, 0, 5, 0, 2, "dirt_path")
    b.girl(5, 1, 9, "kiko:tono")
    return b, TOP + 1


def leo_shack():
    b = Build("leo_shack", (15, 14, 16))
    b.ground(block="sand", spots=("sand",), chance=0.0)
    b.clear()
    X0, X1, Z0, Z1, TOP = 2, 10, 7, 13, 4
    b.fill(X0, 0, Z0, X1, 0, Z1, "birch_planks")
    box(b, X0, X1, Z0, Z1, TOP, "stripped_birch_log", corner="birch_log")
    for x in range(X0 + 1, X1):
        b.set(x, 1, Z0, "light_blue_terracotta")
        b.set(x, 1, Z1, "light_blue_terracotta")
    b.door(6, 1, Z0, "birch_door", "south")
    windows(b, [(4, 2, Z0), (8, 2, Z0), (X0, 2, 10), (X1, 2, 10), (6, 2, Z1)])
    b.gable(1, 11, Z0 - 1, Z1 + 1, TOP + 1, "birch_stairs", "birch_slab", "birch_planks", ends=(Z0, Z1))
    # Estanque con muelle al frente
    for x in range(1, 13):
        for z in range(0, 5):
            if not (x in (1, 12) or z == 0):
                b.set(x, 0, z, "water", level="0")
            else:
                b.set(x, 0, z, "sand")
    b.fill(6, 0, 1, 7, 0, 6, "spruce_planks")
    b.fence(6, 1, 1)
    b.fence(7, 1, 1)
    b.lantern(6, 2, 1, hanging=False)
    b.barrel(5, 1, 5, "up", "kiko:chests/leo_shack")
    b.set(3, 1, 2, "lily_pad")
    b.set(10, 1, 3, "lily_pad")
    b.girl(9, 1, 2, "minecraft:boat", {"Type": gs.String("birch")})
    # Adentro: cama al fondo, rincón de pesca, mesita con sillas
    ceiling(b, X0, X1, Z0, Z1, TOP, "birch_planks", "stripped_birch_log")
    b.fill(4, 1, 9, 6, 1, 11, "light_blue_carpet")
    b.kiko_bed(9, 11, 1, "south", block="kiko:bed_sailor")
    b.barrel(7, 1, 12, "up")
    b.set(7, 2, 12, "sea_pickle", pickles="3", waterlogged="false")
    b.chest(3, 1, 12, "east", "kiko:chests/leo_shack")
    b.barrel(3, 1, 11, "east")
    b.barrel(3, 1, 8, "east")
    b.barrel(3, 2, 8, "east")
    potted(b, 3, 3, 8, "fern")
    b.set(9, 1, 8, "water_cauldron", level="3")
    b.set(8, 1, 8, "dried_kelp_block")
    b.set(8, 2, 8, "sea_pickle", pickles="2", waterlogged="false")
    b.table(5, 1, 10, "birch")
    chair(b, 5, 9, "birch_stairs", "north")
    chair(b, 5, 11, "birch_stairs", "south")
    b.lantern(5, TOP, 9)
    b.lantern(8, TOP, 11)
    b.girl(6, 1, 9, "kiko:leo")
    lift(b, 2)
    # Estanque hondo (3 bloques) y tierra debajo de lo demás
    for x in range(b.size[0]):
        for z in range(b.size[2]):
            top = b.get(x, 2, z)
            if top == "water":
                b.set(x, 1, z, "water", level="0")
                b.set(x, 0, z, "water", level="0")
            elif top in ("spruce_planks",) and 1 <= z <= 4:
                # el muelle va sobre postes
                if (x + z) % 3 == 0:
                    b.log(x, 0, z, x, 1, z, "spruce_log", "y")
                else:
                    b.set(x, 1, z, "water", level="0")
                    b.set(x, 0, z, "water", level="0")
            else:
                b.set(x, 0, z, "sandstone" if top == "sand" else "dirt")
                b.set(x, 1, z, "sand" if top == "sand" else "dirt")
    return b, TOP + 3


def leo_beach():
    """La palapa de Leo en la playa (v3.3.4). Sobre pilotes, sin piso ni estanque: no toca la arena de alrededor."""
    b = Build("leo_beach", (13, 17, 14))
    # La arena queda en y = 2 (start_height -3): los pilotes se entierran 2 bloques y el piso queda 2 arriba
    X0, X1, Z0, Z1, F = 2, 10, 4, 11, 5          # F = piso de la casa
    TOP = F + 4
    # Pilotes
    for x in (X0, 6, X1):
        for z in (Z0, 8, Z1):
            b.log(x, 0, z, x, F - 1, z, "spruce_log", "y")
    for x in (4, 8):
        b.log(x, 0, 2, x, F - 1, 2, "spruce_log", "y")
    # Piso, terraza y escalones
    b.fill(X0, F, Z0, X1, F, Z1, "birch_planks")
    b.fill(4, F, 2, 8, F, 3, "spruce_planks")
    for x in range(4, 9):
        if x != 6:
            b.fence(x, F + 1, 2)
    b.fence(4, F + 1, 3)
    b.fence(8, F + 1, 3)
    b.stairs(6, F - 2, 0, "spruce_stairs", "south")
    b.stairs(6, F - 1, 1, "spruce_stairs", "south")
    b.lantern(4, F + 2, 2, hanging=False)
    b.lantern(8, F + 2, 2, hanging=False)
    # Paredes
    for y in range(F + 1, TOP + 1):
        for x in range(X0, X1 + 1):
            b.set(x, y, Z0, "stripped_birch_log", axis="x")
            b.set(x, y, Z1, "stripped_birch_log", axis="x")
        for z in range(Z0, Z1 + 1):
            b.set(X0, y, z, "stripped_birch_log", axis="z")
            b.set(X1, y, z, "stripped_birch_log", axis="z")
    for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1)):
        b.log(x, F + 1, z, x, TOP, z, "birch_log", "y")
    for x in range(X0 + 1, X1):
        b.set(x, F + 1, Z0, "light_blue_terracotta")
        b.set(x, F + 1, Z1, "light_blue_terracotta")
    b.fill(X0 + 1, F + 1, Z0 + 1, X1 - 1, TOP, Z1 - 1, "air")
    b.door(6, F + 1, Z0, "birch_door", "south")
    for x, y, z in ((4, F + 2, Z0), (8, F + 2, Z0), (X0, F + 2, 8), (X1, F + 2, 8), (6, F + 2, Z1)):
        b.fill(x, y, z, x, y + 1, z, "glass_pane")
    b.gable(1, 11, Z0 - 1, Z1 + 1, TOP + 1, "birch_stairs", "birch_slab", "birch_planks", ends=(Z0, Z1))
    for x in range(X0 + 1, X1):
        for z in range(Z0 + 1, Z1):
            b.set(x, TOP + 1, z, "birch_planks")
    b.log(X0 + 1, TOP + 1, 8, X1 - 1, TOP + 1, 8, "stripped_birch_log", "x")
    # Adentro
    b.fill(4, F + 1, 6, 6, F + 1, 8, "light_blue_carpet")
    b.kiko_bed(9, 9, F + 1, "south", block="kiko:bed_sailor")
    b.barrel(7, F + 1, 10, "up")
    b.set(7, F + 2, 10, "sea_pickle", pickles="3", waterlogged="false")
    b.chest(3, F + 1, 10, "east", "kiko:chests/leo_shack")
    b.barrel(3, F + 1, 9, "east", "kiko:chests/leo_shack")
    b.barrel(3, F + 1, 5, "east")
    b.barrel(3, F + 2, 5, "east")
    b.set(9, F + 1, 5, "water_cauldron", level="3")
    b.set(8, F + 1, 5, "dried_kelp_block")
    b.table(5, F + 1, 7, "birch")
    b.stairs(5, F + 1, 6, "birch_stairs", "north")
    b.stairs(5, F + 1, 8, "birch_stairs", "south")
    b.lantern(5, TOP, 6)
    b.lantern(8, TOP, 9)
    b.girl(6, F + 1, 7, "kiko:leo")
    return b, TOP


def dante_tower():
    b = Build("dante_tower", (13, 15, 13))
    b.ground(spots=("gravel", "coarse_dirt"), chance=0.1)
    b.clear()
    X0, X1, Z0, Z1 = 3, 9, 3, 9
    b.fill(X0, 0, Z0, X1, 0, Z1, "polished_andesite")
    box(b, X0, X1, Z0, Z1, 9, "stone_bricks")
    b.mix(X0, 1, Z0, X1, 1, Z0, [("mossy_stone_bricks", 1), ("stone_bricks", 3), ("cracked_stone_bricks", 1)])
    b.fill(X0 + 1, 1, Z0 + 1, X1 - 1, 9, Z1 - 1, "air")
    # Piso de arriba
    b.fill(X0 + 1, 5, Z0 + 1, X1 - 1, 5, Z1 - 1, "spruce_planks")
    b.fill(X0 + 1, 5, Z1 - 1, X0 + 1, 5, Z1 - 1, "air")
    for y in range(1, 6):
        b.set(X0 + 1, y, Z1 - 1, "ladder", facing="east", waterlogged="false")
    # Techo de almenas
    b.fill(X0, 10, Z0, X1, 10, Z1, "stone_bricks")
    for x in range(X0, X1 + 1):
        for z in range(Z0, Z1 + 1):
            if (x in (X0, X1) or z in (Z0, Z1)) and (x + z) % 2 == 0:
                b.set(x, 11, z, "stone_bricks")
    b.set(6, 11, 6, "bell", attachment="floor", facing="south", powered="false")
    b.fill(X0 + 1, 10, Z1 - 1, X0 + 1, 10, Z1 - 1, "air")
    for y in range(6, 11):
        b.set(X0 + 1, y, Z1 - 1, "ladder", facing="east", waterlogged="false")
    # Puerta y troneras
    b.door(6, 1, Z0, "spruce_door", "south")
    b.set(6, 3, Z0, "stone_brick_slab", type="top", waterlogged="false")
    for x, y, z in ((X0, 2, 6), (X1, 2, 6), (6, 7, Z0), (X0, 7, 6), (X1, 7, 6), (6, 7, Z1)):
        b.set(x, y, z, "iron_bars", north="false", south="false", east="false", west="false", waterlogged="false")
    for z in (Z0 - 1,):
        b.set(5, 3, z, "red_wall_banner", facing="north")
        b.set(7, 3, z, "red_wall_banner", facing="north")
    # Abajo armería, arriba su cama
    b.armor_stand(4, 1, 4, ["minecraft:iron_boots", "minecraft:iron_leggings", "minecraft:iron_chestplate", "minecraft:iron_helmet"], yaw=225.0)
    b.chest(8, 1, 4, "west", "kiko:chests/dante_tower")
    b.barrel(8, 1, 5, "west", "kiko:chests/dante_tower")
    b.set(8, 1, 8, "smithing_table")
    b.set(8, 1, 7, "grindstone", face="floor", facing="west")
    b.set(4, 1, 6, "target", power="0")
    b.lantern(6, 4, 6)
    b.kiko_bed(6, 7, 6, "south", block="kiko:bed_guard")
    b.set(4, 6, 4, "barrel", facing="up", open="false")
    b.set(4, 7, 4, "lantern", hanging="false", waterlogged="false")
    b.lantern(6, 9, 6)
    # v3.3.2 más cositas: tapete rojo, mesa con silla, librero y armas en la pared
    b.fill(5, 1, 5, 7, 1, 7, "red_carpet")
    b.table(7, 1, 6, "spruce")
    chair(b, 7, 7, "spruce_stairs", "south")
    b.set(7, 2, 6, "candle", candles="1", lit="false", waterlogged="false")
    b.fill(5, 6, 4, 7, 6, 5, "red_carpet")
    b.fill(8, 6, 5, 8, 7, 7, "bookshelf")
    b.set(8, 6, 4, "chest", None, facing="west", type="single", waterlogged="false")
    b.set(4, 6, 5, "smithing_table")
    # Postes con antorchas al frente y camino
    b.fill(6, 0, 0, 6, 0, 2, "gravel")
    for x in (4, 8):
        b.set(x, 1, 1, "cobblestone_wall", up="true", north="none", south="none", east="none", west="none", waterlogged="false")
        b.lantern(x, 2, 1, hanging=False)
    b.girl(6, 1, 6, "kiko:dante")
    return b, 5


def ivan_mine():
    b = Build("ivan_mine", (16, 13, 14))
    b.ground(spots=("gravel", "coarse_dirt", "cobblestone"), chance=0.15)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 9, 4, 11, 4
    b.fill(X0, 0, Z0, X1, 0, Z1, "cobbled_deepslate")
    box(b, X0, X1, Z0, Z1, TOP, "cobblestone", corner="stripped_dark_oak_log")
    b.door(5, 1, Z0, "spruce_door", "south")
    windows(b, [(3, 2, Z0), (7, 2, Z0), (X0, 2, 8), (X1, 2, 7), (5, 2, Z1)])
    b.gable(0, 10, Z0 - 1, Z1 + 1, TOP + 1, "cobblestone_stairs", "cobblestone_slab", "cobblestone", ends=(Z0, Z1))
    # Adentro: taller a la izquierda, minerales de colección al frente, cama al fondo
    ceiling(b, X0, X1, Z0, Z1, TOP, "spruce_planks", "stripped_dark_oak_log")
    b.fill(3, 1, 7, 5, 1, 9, "gray_carpet")
    b.kiko_bed(8, 9, 1, "south", block="kiko:bed_mine")
    b.barrel(6, 1, 10, "up")
    b.set(6, 2, 10, "lantern", hanging="false", waterlogged="false")
    b.set(2, 1, 6, "furnace", facing="east", lit="false")
    b.set(2, 1, 7, "blast_furnace", facing="east", lit="false")
    b.set(2, 1, 8, "crafting_table")
    b.barrel(2, 1, 9, "east", "kiko:chests/ivan_mine")
    b.chest(2, 1, 10, "east", "kiko:chests/ivan_mine")
    b.set(3, 1, 5, "grindstone", face="floor", facing="south")
    for i, ore in enumerate(["iron_ore", "gold_ore", "diamond_ore", "emerald_ore"]):
        b.set(7 + i % 2, 1 + i // 2, 5, ore)
    b.set(8, 1, 6, "redstone_ore", lit="false")
    b.table(4, 1, 8, "spruce")
    chair(b, 4, 7, "spruce_stairs", "north")
    chair(b, 4, 9, "spruce_stairs", "south")
    b.set(4, 2, 8, "candle", candles="1", lit="false", waterlogged="false")
    b.lantern(4, TOP, 6)
    b.lantern(6, TOP, 9)
    # Entrada a la mina (un arco de troncos con rieles que bajan)
    for z in (5, 9):
        b.log(12, 1, z, 12, 3, z, "dark_oak_log", "y")
        b.log(15, 1, z, 15, 3, z, "dark_oak_log", "y")
    for z in range(5, 10):
        b.log(12, 4, z, 15, 4, z, "stripped_dark_oak_log", "x")
    b.fill(12, 1, 6, 15, 3, 8, "cobblestone")
    b.fill(13, 1, 6, 14, 3, 8, "air")
    b.fill(13, 0, 4, 13, 0, 9, "gravel")
    for z in range(3, 10):
        b.set(13, 1, z, "rail", shape="north_south", waterlogged="false")
    b.girl(13, 1, 4, "minecraft:minecart")
    b.lantern(14, 3, 5)
    b.set(15, 1, 3, "coal_ore")
    b.set(15, 1, 2, "iron_ore")
    b.set(11, 1, 2, "cobblestone")
    b.fill(5, 0, 0, 5, 0, 3, "gravel")
    b.girl(5, 1, 7, "kiko:ivan")
    return b, TOP + 1


def emi_studio():
    b = Build("emi_studio", (15, 13, 16))
    b.ground(spots=("coarse_dirt",), chance=0.05)
    b.clear()
    X0, X1, Z0, Z1, TOP = 2, 11, 7, 14, 4
    b.fill(X0, 0, Z0, X1, 0, Z1, "dark_oak_planks")
    box(b, X0, X1, Z0, Z1, TOP, "dark_oak_planks", corner="stripped_dark_oak_log", base="deepslate_bricks")
    b.door(6, 1, Z0, "dark_oak_door", "south")
    windows(b, [(4, 2, Z0), (9, 2, Z0), (X0, 2, 10), (X1, 2, 10), (X1, 2, 12), (7, 2, Z1)], "purple_stained_glass_pane")
    b.gable(1, 12, Z0 - 1, Z1 + 1, TOP + 1, "dark_oak_stairs", "dark_oak_slab", "dark_oak_planks", ends=(Z0, Z1))
    # Adentro: rincón de instrumentos, sillón, alfombra y su cama al fondo
    ceiling(b, X0, X1, Z0, Z1, TOP, "dark_oak_planks", "stripped_dark_oak_log")
    b.fill(5, 1, 9, 8, 1, 11, "purple_carpet")
    b.fill(6, 1, 10, 7, 1, 10, "magenta_carpet")
    b.kiko_bed(10, 12, 1, "south", block="kiko:bed_music")
    b.barrel(8, 1, 13, "up")
    b.set(8, 2, 13, "amethyst_cluster", facing="up", waterlogged="false")
    b.set(3, 1, 13, "jukebox", has_record="false")
    for i, inst in enumerate(["guitar", "bass", "harp", "bell"]):
        b.set(3, 1, 9 + i, "note_block", instrument=inst, note=str(4 + i * 3), powered="false")
    b.chest(3, 1, 8, "east", "kiko:chests/emi_studio")
    chair(b, 5, 13, "dark_oak_stairs", "south")
    chair(b, 6, 13, "dark_oak_stairs", "south")
    b.set(10, 1, 8, "amethyst_block")
    b.set(10, 2, 8, "amethyst_cluster", facing="up", waterlogged="false")
    b.barrel(10, 1, 9, "west", "kiko:chests/emi_studio")
    b.set(10, 2, 9, "purple_candle", candles="3", lit="false", waterlogged="false")
    b.lantern(5, TOP, 10)
    b.lantern(8, TOP, 11)
    # Escenario al frente con luces
    b.fill(3, 1, 2, 10, 1, 5, "dark_oak_slab", type="bottom", waterlogged="false")
    for x in (3, 10):
        b.fence(x, 2, 2, "dark_oak_fence")
        b.fence(x, 3, 2, "dark_oak_fence")
        b.lantern(x, 4, 2, hanging=False)
    b.set(6, 2, 3, "note_block", instrument="guitar", note="12", powered="false")
    b.set(7, 2, 3, "jukebox", has_record="false")
    b.fill(6, 0, 0, 7, 0, 1, "dirt_path")
    b.girl(6, 1, 10, "kiko:emi")
    return b, TOP + 1


def beto_shop():
    b = Build("beto_shop", (15, 13, 16))
    b.ground(spots=("coarse_dirt", "dirt_path"), chance=0.1)
    b.clear()
    X0, X1, Z0, Z1, TOP = 2, 11, 7, 14, 4
    b.fill(X0, 0, Z0, X1, 0, Z1, "oak_planks")
    box(b, X0, X1, Z0, Z1, TOP, "oak_planks", corner="stripped_oak_log", base="cobblestone")
    b.door(6, 1, Z0, "oak_door", "south")
    windows(b, [(4, 2, Z0), (9, 2, Z0), (X0, 2, 10), (X1, 2, 10), (7, 2, Z1)])
    b.gable(1, 12, Z0 - 1, Z1 + 1, TOP + 1, "oak_stairs", "oak_slab", "oak_planks", ends=(Z0, Z1))
    # Adentro: bodega de barriles, escritorio de cuentas, librero y su cama al fondo
    ceiling(b, X0, X1, Z0, Z1, TOP, "oak_planks", "stripped_oak_log")
    b.fill(5, 1, 9, 8, 1, 11, "green_carpet")
    b.fill(6, 1, 10, 7, 1, 10, "lime_carpet")
    b.kiko_bed(10, 12, 1, "south", block="kiko:bed_merchant")
    b.barrel(8, 1, 13, "up")
    potted(b, 8, 2, 13, "red_tulip")
    for z in range(8, 14):
        b.barrel(3, 1, z, "east", "kiko:chests/beto_shop" if z % 2 else None)
        if z % 2 == 0:
            b.barrel(3, 2, z, "east")
    b.fill(4, 1, 13, 6, 2, 13, "bookshelf")
    b.set(10, 1, 8, "emerald_block")
    b.chest(10, 1, 9, "west", "kiko:chests/beto_shop")
    b.set(10, 1, 10, "cartography_table")
    b.set(9, 1, 8, "lectern", facing="south", has_book="false", powered="false")
    b.table(6, 1, 10, "oak")
    chair(b, 6, 9, "oak_stairs", "north")
    chair(b, 6, 11, "oak_stairs", "south")
    b.set(6, 2, 10, "candle", candles="2", lit="false", waterlogged="false")
    b.lantern(5, TOP, 9)
    b.lantern(8, TOP, 11)
    # Puesto al frente: mostrador y toldo de rayas
    for x in range(3, 11):
        b.slab(x, 1, 3, "oak_slab", "top")
    b.barrel(3, 1, 4, "up")
    b.barrel(10, 1, 4, "up")
    b.set(5, 2, 3, "melon")
    b.set(8, 2, 3, "carved_pumpkin", facing="south")
    for x in (3, 10):
        b.fence(x, 2, 2, "oak_fence")
        b.fence(x, 3, 2, "oak_fence")
    for x in range(3, 11):
        for z in (1, 2, 3):
            b.set(x, 4, z, "yellow_wool" if x % 2 else "red_wool")
    b.lantern(6, 3, 2)
    b.fill(6, 0, 0, 7, 0, 6, "dirt_path")
    b.girl(8, 1, 10, "kiko:beto")
    return b, TOP + 1


BUILDERS = [tono_ranch, leo_shack, dante_tower, ivan_mine, emi_studio, beto_shop]

LOOT = {
    "tono_ranch": [("minecraft:wheat", 3, 9, 10), ("minecraft:bread", 2, 5, 8), ("minecraft:carrot", 2, 6, 6),
                   ("minecraft:potato", 2, 6, 6), ("minecraft:wheat_seeds", 3, 9, 6), ("minecraft:lead", 1, 2, 4),
                   ("minecraft:saddle", 1, 1, 2), ("minecraft:milk_bucket", 1, 1, 3), ("minecraft:egg", 2, 6, 5),
                   ("minecraft:golden_carrot", 1, 3, 2)],
    "leo_shack": [("minecraft:cod", 2, 6, 8), ("minecraft:salmon", 2, 5, 8), ("minecraft:fishing_rod", 1, 1, 4),
                  ("minecraft:kelp", 3, 9, 6), ("minecraft:string", 2, 6, 5), ("minecraft:nautilus_shell", 1, 1, 2),
                  ("minecraft:prismarine_shard", 1, 4, 3), ("minecraft:name_tag", 1, 1, 1), ("minecraft:cooked_cod", 2, 4, 6)],
    "dante_tower": [("minecraft:arrow", 6, 16, 10), ("minecraft:iron_ingot", 2, 5, 8), ("minecraft:bread", 2, 4, 6),
                    ("minecraft:iron_sword", 1, 1, 3), ("minecraft:shield", 1, 1, 3), ("minecraft:bow", 1, 1, 3),
                    ("minecraft:iron_helmet", 1, 1, 2), ("minecraft:golden_apple", 1, 1, 1), ("minecraft:torch", 4, 12, 6)],
    "ivan_mine": [("minecraft:coal", 4, 12, 10), ("minecraft:raw_iron", 2, 6, 8), ("minecraft:raw_copper", 3, 8, 6),
                  ("minecraft:raw_gold", 1, 3, 4), ("minecraft:redstone", 3, 8, 5), ("minecraft:lapis_lazuli", 2, 6, 4),
                  ("minecraft:iron_pickaxe", 1, 1, 3), ("minecraft:torch", 6, 16, 8), ("minecraft:diamond", 1, 1, 1),
                  ("minecraft:rail", 4, 10, 4)],
    "emi_studio": [("minecraft:music_disc_cat", 1, 1, 2), ("minecraft:music_disc_blocks", 1, 1, 2), ("minecraft:music_disc_far", 1, 1, 2),
                   ("minecraft:note_block", 1, 3, 5), ("minecraft:amethyst_shard", 2, 6, 6), ("minecraft:string", 2, 6, 6),
                   ("minecraft:cookie", 3, 8, 6), ("minecraft:experience_bottle", 1, 3, 4), ("minecraft:goat_horn", 1, 1, 1),
                   ("kiko:bouquet", 1, 1, 2)],
    "beto_shop": [("minecraft:emerald", 2, 6, 10), ("minecraft:bread", 2, 4, 6), ("minecraft:apple", 2, 5, 6),
                  ("minecraft:paper", 3, 8, 6), ("minecraft:book", 1, 3, 4), ("minecraft:compass", 1, 1, 2),
                  ("minecraft:map", 1, 1, 3), ("minecraft:gold_ingot", 1, 3, 4), ("kiko:gift_box", 1, 1, 3),
                  ("minecraft:melon_slice", 3, 8, 5)],
}


def worldgen(names):
    for n in names:
        gs.dump(f"kiko/worldgen/template_pool/{n}.json", {
            "fallback": "minecraft:empty",
            "elements": [{"weight": 1, "element": {"element_type": "minecraft:single_pool_element",
                                                    "location": f"kiko:{n}", "projection": "rigid",
                                                    "processors": "minecraft:empty"}}]})
        gs.dump(f"kiko/worldgen/structure/{n}.json", {
            "type": "minecraft:jigsaw",
            "biomes": "#kiko:has_structure/girl_home",
            "step": "surface_structures",
            "spawn_overrides": {},
            "terrain_adaptation": "beard_thin",
            "start_pool": f"kiko:{n}",
            "size": 1,
            "start_height": {"absolute": -3 if n == "leo_shack" else -1},
            "project_start_to_heightmap": "WORLD_SURFACE_WG",
            "max_distance_from_center": 80,
            "use_expansion_hack": False})
        gs.dump(f"kiko/loot_table/chests/{n}.json", gs.loot_table(LOOT[n]))
    # La palapa de Leo también sale seguido en las playas (su propia estructura y set)
    b, _ = leo_beach()
    b.save()
    gs.dump("kiko/worldgen/template_pool/leo_beach.json", {
        "fallback": "minecraft:empty",
        "elements": [{"weight": 1, "element": {"element_type": "minecraft:single_pool_element",
                                                "location": "kiko:leo_beach", "projection": "rigid",
                                                "processors": "minecraft:empty"}}]})
    gs.dump("kiko/worldgen/structure/leo_shack_beach.json", {
        "type": "minecraft:jigsaw",
        "biomes": "#kiko:has_structure/beach_home",
        "step": "surface_structures",
        "spawn_overrides": {},
        "terrain_adaptation": "none",
        "start_pool": "kiko:leo_beach",
        "size": 1,
        "start_height": {"absolute": -3},
        "project_start_to_heightmap": "WORLD_SURFACE_WG",
        "max_distance_from_center": 80,
        "use_expansion_hack": False})
    gs.dump("kiko/tags/worldgen/biome/has_structure/beach_home.json", {"replace": False, "values": ["minecraft:beach"]})
    gs.dump("kiko/worldgen/structure_set/beach_homes.json", {
        "structures": [{"structure": "kiko:leo_shack_beach", "weight": 1}],
        "placement": {"type": "minecraft:random_spread", "spacing": 14, "separation": 5, "salt": 90412377}})
    # Su propio set (no le quita lugar a las casas de las chicas)
    gs.dump("kiko/worldgen/structure_set/guy_homes.json", {
        "structures": [{"structure": f"kiko:{n}", "weight": 1} for n in names],
        "placement": {"type": "minecraft:random_spread", "spacing": 32, "separation": 10, "salt": 51783207}})


def main(preview_dir=None):
    builds = [fn() for fn in BUILDERS]
    for b, _ in builds:
        b.save()
    worldgen([b.name for b, _ in builds])
    if preview_dir:
        os.makedirs(preview_dir, exist_ok=True)
        gs.preview(builds, preview_dir)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
