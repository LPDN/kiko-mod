"""
Casas de las chicas. Genera las estructuras .nbt, su worldgen (estructura, pool, set, bioma),
los loot tables de sus cofres y una vista previa.

  maru_forge     la forja de Maru (techo de dos aguas de pizarra, chimenea, yunque, alto horno, armadura)
  lumi_library   la biblioteca de Lumi (dos pisos, mesa de encantamientos con libreros, jardín con estanque)
  nadi_camp      el campamento de Nadi (tienda grande, fogata, tendedero, mesa de cartografía)
  rina_clinic    la clínica de Rina (camillas, cruz verde, soporte para pociones)
  tami_kitchen   la fonda de Tami (cocina con ahumadores, mesas, terraza y huerto)

Cada casa trae a su chica adentro y una cama de Kiko para que duerma ahí.
Uso   python tools/gen_structures.py [carpeta_preview]      (necesita nbtlib)
"""
import json
import math
import os
import random
import sys

import nbtlib
from nbtlib.tag import Byte, Compound, Double, Float, Int, List, String

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA = os.path.join(ROOT, "src", "main", "resources", "data")
DATA_VERSION = 3955  # 1.21.1

DIRS = {"north": (0, -1), "south": (0, 1), "east": (1, 0), "west": (-1, 0)}
CW = {"north": "east", "east": "south", "south": "west", "west": "north"}

# Bloques que cuentan como "pared" para que las cercas y los vidrios se conecten
SOLID_HINTS = ("planks", "_log", "_wood", "bricks", "stone", "cobblestone", "wool", "terracotta", "concrete",
               "quartz", "bookshelf", "glass", "deepslate", "andesite", "granite", "diorite")
NOT_SOLID = ("slab", "stairs", "pane", "fence", "wall", "door", "carpet", "button", "plate", "banner", "bars")


class Build:
    def __init__(self, name, size):
        self.name = name
        self.size = size
        self.blocks = {}       # (x, y, z) -> (nombre, props, nbt)
        self.entities = []
        self.rng = random.Random(name)

    # --- básicos ---
    def set(self, x, y, z, block, nbt=None, **props):
        if not (0 <= x < self.size[0] and 0 <= y < self.size[1] and 0 <= z < self.size[2]):
            return
        name = block if ":" in block else "minecraft:" + block
        self.blocks[(x, y, z)] = (name, {k: str(v).lower() for k, v in props.items()}, nbt)

    def get(self, x, y, z):
        b = self.blocks.get((x, y, z))
        return b[0].split(":")[-1] if b else None

    def fill(self, x0, y0, z0, x1, y1, z1, block, **props):
        for x in range(min(x0, x1), max(x0, x1) + 1):
            for y in range(min(y0, y1), max(y0, y1) + 1):
                for z in range(min(z0, z1), max(z0, z1) + 1):
                    self.set(x, y, z, block, **props)

    def clear(self):
        """Limpia todo lo de arriba del piso (si no, quedan árboles y tierra adentro)."""
        self.fill(0, 1, 0, self.size[0] - 1, self.size[1] - 1, self.size[2] - 1, "air")

    def ground(self, block="grass_block", spots=("coarse_dirt",), chance=0.12):
        for x in range(self.size[0]):
            for z in range(self.size[2]):
                if spots and self.rng.random() < chance:
                    self.set(x, 0, z, self.rng.choice(spots))
                else:
                    self.set(x, 0, z, block, **({"snowy": "false"} if block == "grass_block" else {}))

    def mix(self, x0, y0, z0, x1, y1, z1, choices):
        """Llena con una mezcla al azar [(bloque, peso), ...] para que no se vea plano."""
        total = sum(w for _, w in choices)
        for x in range(min(x0, x1), max(x0, x1) + 1):
            for y in range(min(y0, y1), max(y0, y1) + 1):
                for z in range(min(z0, z1), max(z0, z1) + 1):
                    r = self.rng.random() * total
                    for blk, w in choices:
                        r -= w
                        if r <= 0:
                            self.set(x, y, z, blk)
                            break

    # --- piezas ---
    def stairs(self, x, y, z, block, facing, half="bottom"):
        self.set(x, y, z, block, facing=facing, half=half, shape="straight", waterlogged="false")

    def slab(self, x, y, z, block, kind="bottom"):
        self.set(x, y, z, block, type=kind, waterlogged="false")

    def log(self, x0, y0, z0, x1, y1, z1, block, axis):
        self.fill(x0, y0, z0, x1, y1, z1, block, axis=axis)

    def pane(self, x, y, z, block="glass_pane"):
        self.set(x, y, z, block, north="false", south="false", east="false", west="false", waterlogged="false")

    def fence(self, x, y, z, block="spruce_fence"):
        self.set(x, y, z, block, north="false", south="false", east="false", west="false", waterlogged="false")

    def door(self, x, y, z, block, facing, hinge="left"):
        for half, dy in (("lower", 0), ("upper", 1)):
            self.set(x, y + dy, z, block, facing=facing, half=half, hinge=hinge, open="false", powered="false")

    def lantern(self, x, y, z, hanging=True):
        self.set(x, y, z, "lantern", hanging=hanging, waterlogged="false")

    def chest(self, x, y, z, facing, loot):
        self.set(x, y, z, "chest", Compound({"id": String("minecraft:chest"), "LootTable": String(loot)}),
                 facing=facing, type="single", waterlogged="false")

    def barrel(self, x, y, z, facing="up", loot=None):
        nbt = Compound({"id": String("minecraft:barrel")})
        if loot:
            nbt["LootTable"] = String(loot)
        self.set(x, y, z, "barrel", nbt, facing=facing, open="false")

    def bed(self, x, y, z, color, facing):
        """Cama normal de 1x2. (x, z) = pies; la cabecera va hacia `facing`."""
        dx, dz = DIRS[facing]
        nbt = Compound({"id": String("minecraft:bed")})
        self.set(x, y, z, f"{color}_bed", nbt, facing=facing, part="foot", occupied="false")
        self.set(x + dx, y, z + dz, f"{color}_bed", nbt, facing=facing, part="head", occupied="false")

    def kiko_bed(self, fx, fz, y, facing, block="kiko:kiko_bed"):
        """Cama de Kiko 2x2. (fx, fz) = pies de la mitad izquierda; la cabecera va hacia `facing`."""
        hx, hz = DIRS[facing]
        rx, rz = DIRS[CW[facing]]
        for part, ox, oz in (("foot", 0, 0), ("head", hx, hz)):
            for side, sx, sz in (("left", 0, 0), ("right", rx, rz)):
                self.set(fx + ox + sx, y, fz + oz + sz, block, facing=facing, part=part, side=side, occupied="false")

    def table(self, x, y, z, wood="spruce"):
        self.fence(x, y, z, f"{wood}_fence")
        self.set(x, y + 1, z, f"{wood}_pressure_plate", powered="false")

    def leaves(self, x, y, z, block="oak_leaves"):
        self.set(x, y, z, block, persistent="true", distance="1", waterlogged="false")

    def gable(self, x0, x1, z0, z1, y, stair, ridge, fill, ends, axis="z"):
        """
        Techo de dos aguas. Con axis="z" la cumbrera corre a lo largo de z (las caídas van hacia x0 y x1);
        con axis="x" corre a lo largo de x. `ends` son las paredes de los extremos que se rellenan con `fill`.
        """
        a0, a1 = (x0, x1) if axis == "z" else (z0, z1)
        b0, b1 = (z0, z1) if axis == "z" else (x0, x1)
        lo_face, hi_face = ("east", "west") if axis == "z" else ("south", "north")
        i = 0
        while a0 + i <= a1 - i:
            lo, hi, yy = a0 + i, a1 - i, y + i
            for b in range(b0, b1 + 1):
                def put(a, fn, *args):
                    if axis == "z":
                        fn(a, yy, b, *args)
                    else:
                        fn(b, yy, a, *args)
                if lo == hi:
                    put(lo, self.slab, ridge)
                else:
                    put(lo, self.stairs, stair, lo_face)
                    put(hi, self.stairs, stair, hi_face)
            for e in ends:
                for a in range(lo + 1, hi):
                    if axis == "z":
                        self.set(a, yy, e, fill)
                    else:
                        self.set(e, yy, a, fill)
            i += 1
        # Aleros: escalones de cabeza (al revés) debajo de la orilla para que se vea grueso
        for b in range(b0, b1 + 1):
            for a, face in ((a0, lo_face), (a1, hi_face)):
                if axis == "z":
                    self.stairs(a, y - 1, b, stair, face, "top")
                else:
                    self.stairs(b, y - 1, a, stair, face, "top")

    def girl(self, x, y, z, entity, extra=None):
        self.entities.append((x + 0.5, y, z + 0.5, entity, extra))

    def armor_stand(self, x, y, z, items, yaw=180.0):
        armor = [Compound({"id": String(i), "count": Int(1)}) if i else Compound({}) for i in items]
        self.entities.append((x + 0.5, y, z + 0.5, "minecraft:armor_stand",
                              {"ArmorItems": List[Compound](armor), "Rotation": List[Float]([Float(yaw), Float(0)])}))

    # --- cercas y vidrios conectados ---
    def _connects(self, me, other):
        if other is None or other == "air":
            return False
        if "fence" in me and ("fence" in other):
            return True
        if ("pane" in me or me == "iron_bars") and ("pane" in other or other == "iron_bars"):
            return True
        if any(n in other for n in NOT_SOLID):
            return False
        return any(h in other for h in SOLID_HINTS)

    def autoconnect(self):
        for (x, y, z), (name, props, nbt) in list(self.blocks.items()):
            short = name.split(":")[-1]
            if not (short.endswith("_fence") or short.endswith("pane") or short == "iron_bars"):
                continue
            for d, (dx, dz) in DIRS.items():
                props[d] = "true" if self._connects(short, self.get(x + dx, y, z + dz)) else "false"

    def save(self):
        self.autoconnect()
        palette, index = [], {}
        blocks = []
        for (x, y, z), (name, props, nbt) in sorted(self.blocks.items(), key=lambda kv: (kv[0][1], kv[0][2], kv[0][0])):
            key = (name, tuple(sorted(props.items())))
            if key not in index:
                index[key] = len(palette)
                entry = Compound({"Name": String(name)})
                if props:
                    entry["Properties"] = Compound({k: String(v) for k, v in props.items()})
                palette.append(entry)
            b = Compound({"pos": List[Int]([Int(x), Int(y), Int(z)]), "state": Int(index[key])})
            if nbt is not None:
                b["nbt"] = nbt
            blocks.append(b)
        ents = []
        for (x, y, z, eid, extra) in self.entities:
            nbt = Compound({"id": String(eid), "PersistenceRequired": Byte(1)})
            for k, v in (extra or {}).items():
                nbt[k] = v
            ents.append(Compound({
                "pos": List[Double]([Double(x), Double(y), Double(z)]),
                "blockPos": List[Int]([Int(math.floor(x)), Int(math.floor(y)), Int(math.floor(z))]),
                "nbt": nbt,
            }))
        root = Compound({
            "DataVersion": Int(DATA_VERSION),
            "size": List[Int]([Int(v) for v in self.size]),
            "palette": List[Compound](palette),
            "blocks": List[Compound](blocks),
            "entities": List[Compound](ents),
        })
        path = os.path.join(DATA, "kiko", "structure", f"{self.name}.nbt")
        os.makedirs(os.path.dirname(path), exist_ok=True)
        nbtlib.File(root).save(path, gzipped=True)


# ------------------------------------------------------------------
# La forja de Maru (paredes 11 x 9), frente hacia z = 0
# ------------------------------------------------------------------
def maru_forge():
    b = Build("maru_forge", (13, 13, 12))
    b.ground(spots=("coarse_dirt", "gravel"))
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 11, 1, 9, 4
    # Piso de piedra y un camino de grava al frente
    b.mix(X0, 0, Z0, X1, 0, Z1, [("stone_bricks", 6), ("cobblestone", 2), ("polished_andesite", 2), ("cracked_stone_bricks", 1)])
    b.mix(4, 0, 0, 8, 0, 0, [("gravel", 2), ("cobblestone", 1)])
    # Paredes: piedra abajo, madera oscura arriba, marco de troncos
    for y in range(1, TOP + 1):
        blk = "cobblestone" if y <= 2 else "dark_oak_planks"
        b.fill(X0, y, Z0, X1, y, Z0, blk)
        b.fill(X0, y, Z1, X1, y, Z1, blk)
        b.fill(X0, y, Z0, X0, y, Z1, blk)
        b.fill(X1, y, Z0, X1, y, Z1, blk)
    b.mix(X0, 1, Z0, X1, 1, Z0, [("cobblestone", 3), ("mossy_cobblestone", 1)])
    for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1), (6, Z0), (6, Z1), (X0, 5), (X1, 5)):
        b.log(x, 1, z, x, TOP, z, "stripped_dark_oak_log", "y")
    b.log(X0 + 1, TOP, Z0, X1 - 1, TOP, Z0, "stripped_dark_oak_log", "x")
    b.log(X0 + 1, TOP, Z1, X1 - 1, TOP, Z1, "stripped_dark_oak_log", "x")
    # Portón grande del taller
    b.fill(3, 1, Z0, 5, 3, Z0, "air")
    b.fill(7, 1, Z0, 9, 3, Z0, "air")
    b.log(6, 1, Z0, 6, 3, Z0, "stripped_dark_oak_log", "y")
    # Ventanas con barrotes a los lados
    for z in (3, 7):
        b.fill(X0, 2, z, X0, 3, z, "iron_bars")
        b.fill(X1, 2, z, X1, 3, z, "iron_bars")
    # Techo de pizarra a dos aguas
    b.gable(0, 12, 0, 10, TOP + 1, "deepslate_tile_stairs", "deepslate_tile_slab", "dark_oak_planks", ends=(Z0, Z1))
    b.log(X0 + 1, TOP + 1, 5, X1 - 1, TOP + 1, 5, "stripped_dark_oak_log", "x")   # viga de adentro
    # Fogón: alto horno, horno y caldero de lava debajo de la chimenea
    b.set(8, 1, 8, "blast_furnace", facing="north", lit="false")
    b.set(9, 1, 8, "furnace", facing="north", lit="false")
    b.set(10, 1, 8, "lava_cauldron")
    b.fill(10, 2, 8, 10, 11, 8, "bricks")
    b.fill(8, 2, 8, 9, 2, 8, "bricks")
    b.set(10, 12, 8, "campfire", facing="north", lit="true", signal_fire="false", waterlogged="false")
    b.set(10, 1, 6, "water_cauldron", level="3")                              # para templar
    # Zona de trabajo
    b.set(6, 1, 4, "anvil", facing="east")
    b.set(8, 1, 4, "smithing_table")
    b.set(4, 1, 4, "grindstone", face="floor", facing="north")
    b.set(2, 1, 2, "crafting_table")
    b.chest(2, 1, 3, "east", "kiko:chests/maru_forge")
    b.barrel(2, 1, 4, "east", "kiko:chests/maru_forge")
    b.barrel(2, 2, 4, "east")
    b.set(10, 1, 2, "stonecutter", facing="west")
    # Rejilla de herramientas: estante de troncos con cadenas
    b.log(7, 3, 8, 7, 3, 8, "stripped_dark_oak_log", "x")
    b.set(6, 3, 8, "chain", axis="y", waterlogged="false")
    b.armor_stand(6, 1, 7, ["minecraft:iron_boots", "minecraft:iron_leggings", "minecraft:iron_chestplate", "minecraft:iron_helmet"])
    # Lámparas colgando de la viga
    b.lantern(4, TOP, 5)
    b.lantern(8, TOP, 5)
    # Su cama en la esquina de atrás
    b.kiko_bed(4, 7, 1, "south", block="kiko:bed_forge")
    b.set(2, 1, 8, "spruce_trapdoor", facing="east", half="bottom", open="false", powered="false", waterlogged="false")
    # Afuera: leña bajo el alero y carbón
    b.log(12, 1, 3, 12, 2, 6, "oak_log", "z")
    b.set(0, 1, 2, "coal_block")
    b.set(0, 1, 3, "barrel", Compound({"id": String("minecraft:barrel")}), facing="up", open="false")
    b.girl(6, 1, 6, "kiko:maru")
    return b, 5


# ------------------------------------------------------------------
# La biblioteca de Lumi (paredes 9 x 9, dos pisos) con jardín al frente
# ------------------------------------------------------------------
def lumi_library():
    b = Build("lumi_library", (13, 15, 17))
    b.ground(spots=("coarse_dirt",), chance=0.04)
    b.clear()
    X0, X1, Z0, Z1 = 2, 10, 6, 14
    F2, TOP = 5, 8
    b.fill(X0, 0, Z0, X1, 0, Z1, "spruce_planks")
    b.fill(X0 + 1, 0, Z0 + 1, X1 - 1, 0, Z1 - 1, "dark_oak_planks")
    for y in range(1, TOP + 1):
        blk = "stone_bricks" if y == 1 else "birch_planks"
        for x in range(X0, X1 + 1):
            b.set(x, y, Z0, blk)
            b.set(x, y, Z1, blk)
        for z in range(Z0, Z1 + 1):
            b.set(X0, y, z, blk)
            b.set(X1, y, z, blk)
    for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1)):
        b.log(x, 1, z, x, TOP, z, "dark_oak_log", "y")
    # Viga entre pisos por fuera
    b.log(X0 + 1, F2, Z0, X1 - 1, F2, Z0, "dark_oak_log", "x")
    b.log(X0 + 1, F2, Z1, X1 - 1, F2, Z1, "dark_oak_log", "x")
    b.log(X0, F2, Z0 + 1, X0, F2, Z1 - 1, "dark_oak_log", "z")
    b.log(X1, F2, Z0 + 1, X1, F2, Z1 - 1, "dark_oak_log", "z")
    # Puerta y ventanas
    b.door(6, 1, Z0, "spruce_door", "south")
    b.set(6, 3, Z0, "dark_oak_log", axis="x")
    for x in (4, 8):
        b.fill(x, 2, Z0, x, 3, Z0, "glass_pane")
        b.fill(x, 2, Z1, x, 3, Z1, "glass_pane")
        b.set(x, 1, Z0 - 1, "flowering_azalea_leaves", persistent="true", distance="1", waterlogged="false")
    for z in (8, 12):
        b.fill(X0, 2, z, X0, 3, z, "glass_pane")
        b.fill(X1, 2, z, X1, 3, z, "glass_pane")
    for x in (4, 8):
        b.fill(x, 6, Z0, x, 7, Z0, "glass_pane")
    b.fill(6, 6, Z1, 6, 7, Z1, "glass_pane")
    b.fill(X0, 6, 10, X0, 7, 10, "glass_pane")
    b.fill(X1, 6, 10, X1, 7, 10, "glass_pane")
    # Segundo piso con hoyo para la escalera
    b.fill(X0 + 1, F2, Z0 + 1, X1 - 1, F2, Z1 - 1, "spruce_planks")
    b.fill(9, 1, 13, 9, F2, 13, "ladder", facing="north", waterlogged="false")
    # Planta baja: su recámara y sala de lectura
    b.kiko_bed(4, 12, 1, "south", block="kiko:bed_moon")
    b.fill(5, 1, 8, 7, 1, 10, "light_blue_carpet")
    b.set(6, 1, 9, "purple_carpet")
    b.fill(9, 1, 7, 9, 2, 10, "bookshelf")
    b.table(7, 1, 12)
    b.set(7, 2, 12, "candle", candles="2", lit="true", waterlogged="false")
    b.stairs(7, 1, 11, "spruce_stairs", "north")
    b.chest(3, 1, 7, "east", "kiko:chests/lumi_library")
    b.set(3, 1, 9, "potted_azure_bluet")
    b.set(3, 1, 10, "potted_blue_orchid")
    b.set(9, 1, 11, "potted_allium")
    b.lantern(4, 4, 8)
    b.lantern(8, 4, 11)
    # Segundo piso: la mesa de encantamientos rodeada de libreros (a dos de distancia, como debe ser)
    # Se sube por la escalera de mano del fondo y se entra al círculo por el hueco del frente
    cx, cz, fy = 6, 10, F2 + 1
    for dx in range(-2, 3):
        for dz in range(-2, 3):
            if max(abs(dx), abs(dz)) == 2 and not (dx == 0 and dz == -2):
                b.fill(cx + dx, fy, cz + dz, cx + dx, fy + 1, cz + dz, "bookshelf")
    b.fill(cx - 1, F2, cz - 1, cx + 1, F2, cz + 1, "purple_wool")      # tapete pintado en el piso (la alfombra tapa los libreros)
    b.set(cx, fy, cz, "enchanting_table")
    b.set(3, fy, 10, "lectern", facing="east", has_book="false", powered="false")
    b.chest(3, fy, 13, "east", "kiko:chests/lumi_library")
    b.set(3, fy, 7, "potted_allium")
    b.lantern(6, TOP, 10)
    # Techo a dos aguas
    b.gable(1, 11, Z0 - 1, Z1 + 1, TOP + 1, "spruce_stairs", "spruce_slab", "birch_planks", ends=(Z0, Z1))
    b.set(6, TOP + 2, Z0, "glass_pane")
    b.log(6, TOP + 1, Z0 + 1, 6, TOP + 1, Z1 - 1, "dark_oak_log", "z")      # viga para el farol
    # Jardín: camino, flores, estanque, banca y cerca
    b.fill(6, 0, 0, 6, 0, Z0 - 1, "dirt_path")
    b.fill(5, 0, Z0 - 1, 7, 0, Z0 - 1, "dirt_path")
    flowers = ["allium", "blue_orchid", "cornflower", "lily_of_the_valley", "oxeye_daisy", "azure_bluet", "pink_tulip"]
    for x in list(range(1, 5)) + list(range(8, 12)):
        for z in range(1, 4):
            if b.rng.random() < 0.7:
                b.set(x, 1, z, b.rng.choice(flowers))
    b.fill(9, 0, 1, 10, 0, 2, "water", level="0")
    b.fill(9, 1, 1, 10, 1, 2, "air")
    b.set(9, 1, 1, "lily_pad")
    b.stairs(2, 1, 4, "spruce_stairs", "north")
    b.stairs(3, 1, 4, "spruce_stairs", "north")
    for x in range(0, 13):
        if x != 6:
            b.fence(x, 1, 0, "spruce_fence")
    for z in range(0, Z0 + 1):
        b.fence(0, 1, z, "spruce_fence")
        b.fence(12, 1, z, "spruce_fence")
    # v3.4.1 cerca completa hasta la casa
    for x in (1, 11):
        b.fence(x, 1, Z0, "spruce_fence")
    # Entrada abierta con el caminito (v3.4.3 sin puerta)
    b.fence(0, 2, 0, "spruce_fence")
    b.lantern(0, 3, 0, hanging=False)
    b.fence(12, 2, 0, "spruce_fence")
    b.lantern(12, 3, 0, hanging=False)
    # Arbolito de azalea en la esquina
    b.log(11, 1, 4, 11, 2, 4, "oak_log", "y")
    for dx in range(-1, 2):
        for dz in range(-1, 2):
            b.leaves(11 + dx, 3, 4 + dz, "flowering_azalea_leaves" if (dx + dz) % 2 else "azalea_leaves")
    b.leaves(11, 4, 4, "flowering_azalea_leaves")
    b.girl(5, 1, 8, "kiko:lumi")
    return b, F2


# ------------------------------------------------------------------
# El campamento de Nadi (13 x 13), tienda grande con espacio para la cama
# ------------------------------------------------------------------
def nadi_camp():
    b = Build("nadi_camp", (15, 7, 15))
    b.ground(spots=("coarse_dirt", "podzol"), chance=0.18)
    b.clear()
    # Tienda: 7 de ancho, 7 de fondo, 4 de alto, abierta al frente (z = 3)
    TZ0, TZ1 = 3, 9
    rows = [(1, 2, 8, "green_wool"), (2, 3, 7, "green_wool"), (3, 4, 6, "lime_wool")]
    for z in range(TZ0, TZ1 + 1):
        for y, lo, hi, wool in rows:
            b.set(lo, y, z, wool)
            b.set(hi, y, z, wool)
        b.set(5, 4, z, "white_wool")
    for y, lo, hi, wool in rows:
        b.fill(lo + 1, y, TZ1, hi - 1, y, TZ1, "green_wool")               # pared de atrás
    b.set(3, 1, TZ0, "green_wool")                                         # las solapas de la entrada
    b.set(7, 1, TZ0, "green_wool")
    b.set(4, 2, TZ0, "green_wool")
    b.set(6, 2, TZ0, "green_wool")
    b.set(5, 3, TZ0, "lime_wool")
    # Estacas
    for x, z in ((1, 2), (9, 2), (1, 10), (9, 10)):
        b.fence(x, 1, z, "spruce_fence")
    # Adentro: tapete, cama, cofre, barril y farol colgando de la cumbrera
    b.fill(3, 0, TZ0 + 1, 7, 0, TZ1 - 1, "spruce_planks")
    b.fill(3, 1, TZ0 + 1, 7, 1, TZ1 - 1, "brown_carpet")
    b.fill(4, 1, TZ0 + 1, 6, 1, TZ0 + 2, "orange_carpet")
    b.kiko_bed(5, 7, 1, "south", block="kiko:bed_trail")
    b.chest(7, 1, 8, "west", "kiko:chests/nadi_camp")
    b.barrel(3, 1, 8, "east", "kiko:chests/nadi_camp")
    b.set(3, 1, 7, "potted_fern")
    b.lantern(5, 3, 6)
    # Fogata con troncos para sentarse
    b.fill(10, 0, 4, 12, 0, 6, "coarse_dirt")
    b.set(11, 1, 5, "campfire", facing="north", lit="true", signal_fire="false", waterlogged="false")
    b.log(10, 1, 3, 12, 1, 3, "stripped_spruce_log", "x")
    b.log(13, 1, 4, 13, 1, 6, "stripped_spruce_log", "z")
    b.log(10, 1, 7, 11, 1, 7, "stripped_spruce_log", "x")
    # Tendedero: dos postes con hilo y unas banderas como ropa
    for x in (10, 14):
        b.fence(x, 1, 11, "spruce_fence")
        b.fence(x, 2, 11, "spruce_fence")
        b.set(x, 3, 11, "spruce_fence", north="false", south="false", east="false", west="false", waterlogged="false")
    b.fill(11, 3, 11, 13, 3, 11, "tripwire", attached="false", disarmed="true", powered="false",
           north="false", south="false", east="true", west="true")
    for x, col in ((11, "white"), (12, "light_blue"), (13, "orange")):
        b.set(x, 2, 11, f"{col}_wall_banner", Compound({"id": String("minecraft:banner")}), facing="south")
    # Mesa de cartografía, barriles y pacas
    b.set(11, 1, 9, "cartography_table")
    b.barrel(12, 1, 9, "up", "kiko:chests/nadi_camp")
    b.set(12, 2, 9, "barrel", Compound({"id": String("minecraft:barrel")}), facing="up", open="false")
    b.set(13, 1, 9, "loom", facing="west")
    b.fill(0, 1, 12, 1, 1, 13, "hay_block", axis="y")
    b.set(0, 2, 12, "hay_block", axis="y")
    # Poste con farol en la entrada y arbustos
    b.fence(1, 1, 1, "spruce_fence")
    b.fence(1, 2, 1, "spruce_fence")
    b.lantern(1, 3, 1, hanging=False)
    for x, z in ((13, 13), (0, 6), (8, 13)):
        b.set(x, 1, z, "sweet_berry_bush", age="3")
    for x, z in ((4, 12), (6, 13), (12, 1), (2, 13)):
        b.set(x, 1, z, b.rng.choice(["dandelion", "poppy", "short_grass", "fern"]))
    b.girl(10, 1, 5, "kiko:nadi")
    return b, 3


# ------------------------------------------------------------------
# La clínica de Rina (paredes 11 x 9), techo plano con barandal
# ------------------------------------------------------------------
def rina_clinic():
    b = Build("rina_clinic", (13, 9, 12))
    b.ground(spots=("coarse_dirt",), chance=0.04)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 11, 2, 10, 5
    for x in range(X0, X1 + 1):
        for z in range(Z0, Z1 + 1):
            b.set(x, 0, z, "smooth_quartz" if (x + z) % 2 == 0 else "light_gray_terracotta")
    for y in range(1, TOP + 1):
        blk = "white_terracotta" if y != 4 else "cyan_terracotta"
        for x in range(X0, X1 + 1):
            b.set(x, y, Z0, blk)
            b.set(x, y, Z1, blk)
        for z in range(Z0, Z1 + 1):
            b.set(X0, y, z, blk)
            b.set(X1, y, z, blk)
    for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1)):
        b.fill(x, 1, z, x, TOP, z, "quartz_pillar", axis="y")
    # Puerta doble, cruz verde arriba
    b.door(6, 1, Z0, "birch_door", "south")
    b.fill(6, 3, Z0, 6, 5, Z0, "green_concrete")
    b.set(5, 4, Z0, "green_concrete")
    b.set(7, 4, Z0, "green_concrete")
    # Ventanas
    for x in (2, 3, 9, 10):
        b.fill(x, 2, Z0, x, 3, Z0, "glass_pane")
    for z in (4, 5, 7, 8):
        b.fill(X0, 2, z, X0, 3, z, "glass_pane")
        b.fill(X1, 2, z, X1, 3, z, "glass_pane")
    for x in (4, 8):
        b.fill(x, 2, Z1, x, 3, Z1, "glass_pane")
    # Techo plano con luces y barandal
    b.fill(X0, TOP + 1, Z0, X1, TOP + 1, Z1, "smooth_quartz")
    for x, z in ((4, 5), (8, 5), (4, 8), (8, 8)):
        b.set(x, TOP + 1, z, "sea_lantern")
    for x in range(X0, X1 + 1):
        b.slab(x, TOP + 2, Z0, "smooth_quartz_slab")
        b.slab(x, TOP + 2, Z1, "smooth_quartz_slab")
    for z in range(Z0, Z1 + 1):
        b.slab(X0, TOP + 2, z, "smooth_quartz_slab")
        b.slab(X1, TOP + 2, z, "smooth_quartz_slab")
    # Toldo verde agua sobre la puerta
    for x in range(4, 9):
        b.slab(x, 3, Z0 - 1, "prismarine_brick_slab")
    # Camillas con cortina
    b.bed(9, 1, 4, "white", "east")
    b.bed(9, 1, 7, "white", "east")
    b.fill(9, 1, 6, 10, 2, 6, "white_stained_glass_pane")
    b.set(9, 1, 3, "potted_azalea_bush")
    b.set(10, 1, 9, "potted_fern")
    # Botiquín: soporte de pociones, caldero, cofre, gabinetes
    b.set(2, 1, 8, "brewing_stand", Compound({"id": String("minecraft:brewing_stand")}),
          has_bottle_0="false", has_bottle_1="false", has_bottle_2="false")
    b.set(2, 1, 9, "water_cauldron", level="3")
    b.chest(3, 1, 9, "north", "kiko:chests/rina_clinic")
    b.barrel(2, 1, 7, "east", "kiko:chests/rina_clinic")
    b.barrel(2, 2, 7, "east")
    b.slab(2, 2, 8, "smooth_quartz_slab")
    # Recepción con bancas de espera
    b.fill(2, 1, 5, 4, 1, 5, "smooth_quartz_slab", type="top", waterlogged="false")
    b.set(3, 2, 5, "potted_lily_of_the_valley")
    b.stairs(2, 1, 3, "birch_stairs", "north")
    b.stairs(3, 1, 3, "birch_stairs", "north")
    # Su cama atrás, detrás de un biombo
    b.kiko_bed(6, 8, 1, "south", block="kiko:bed_clinic")
    b.fill(4, 1, 7, 7, 2, 7, "white_stained_glass_pane")
    b.set(7, 1, 9, "white_carpet")
    # Al frente: camino y azaleas
    b.fill(6, 0, 0, 6, 0, 1, "smooth_stone")
    for x in (2, 3, 9, 10):
        b.set(x, 1, 1, "flowering_azalea" if x % 2 else "azalea")
    b.girl(6, 1, 5, "kiko:rina")
    return b, TOP + 1


# ------------------------------------------------------------------
# La fonda de Tami (paredes 13 x 9) con terraza al frente y huerto atrás
# ------------------------------------------------------------------
def tami_kitchen():
    b = Build("tami_kitchen", (15, 12, 16))
    b.ground(spots=("coarse_dirt",), chance=0.06)
    b.clear()
    X0, X1, Z0, Z1, TOP = 1, 13, 3, 11, 4
    b.mix(X0, 0, Z0, X1, 0, Z1, [("spruce_planks", 3), ("stripped_spruce_wood", 1)])
    b.fill(X0 + 1, 0, 8, X1 - 1, 0, Z1 - 1, "polished_andesite")             # piso de la cocina
    for y in range(1, TOP + 1):
        blk = "bricks" if y <= 2 else "white_terracotta"
        for x in range(X0, X1 + 1):
            b.set(x, y, Z0, blk)
            b.set(x, y, Z1, blk)
        for z in range(Z0, Z1 + 1):
            b.set(X0, y, z, blk)
            b.set(X1, y, z, blk)
    for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1), (7, Z1), (5, Z0), (9, Z0), (X0, 7), (X1, 7)):
        b.log(x, 1, z, x, TOP, z, "spruce_log", "y")
    # Puerta doble y ventanales al frente
    b.door(7, 1, Z0, "spruce_door", "south")
    for x in (2, 3, 4, 10, 11, 12):
        b.fill(x, 2, Z0, x, 3, Z0, "glass_pane")
    for z in (5, 9):
        b.fill(X0, 2, z, X0, 3, z, "glass_pane")
        b.fill(X1, 2, z, X1, 3, z, "glass_pane")
    # Techo de teja a dos aguas (cumbrera a lo largo)
    b.gable(0, 14, 2, 12, TOP + 1, "brick_stairs", "brick_slab", "spruce_planks", ends=(X0, X1), axis="x")
    b.log(X0 + 1, TOP + 1, 7, X1 - 1, TOP + 1, 7, "stripped_spruce_log", "x")
    b.lantern(4, TOP, 7)
    b.lantern(10, TOP, 7)
    # Cocina al fondo: ahumadores, horno, fogón, fregadero, alacena
    b.set(2, 1, 10, "smoker", facing="north", lit="false")
    b.set(3, 1, 10, "furnace", facing="north", lit="false")
    b.set(4, 1, 10, "smoker", facing="north", lit="false")
    b.barrel(5, 1, 10, "up", "kiko:chests/tami_kitchen")
    b.set(6, 1, 10, "water_cauldron", level="3")
    b.chest(8, 1, 10, "north", "kiko:chests/tami_kitchen")
    b.set(9, 1, 10, "crafting_table")
    b.barrel(10, 1, 10, "up")
    b.set(11, 1, 10, "campfire", facing="north", lit="true", signal_fire="false", waterlogged="false")
    b.fill(11, 2, 10, 11, 11, 10, "bricks")
    b.set(12, 1, 10, "composter", level="0")
    # Repisa con cosas encima
    b.fill(2, 3, 10, 6, 3, 10, "spruce_slab", type="top", waterlogged="false")
    b.set(3, 4, 10, "potted_red_tulip")
    b.set(5, 4, 10, "candle", candles="2", lit="false", waterlogged="false")
    # Barra de la cocina con pastel
    b.fill(4, 1, 8, 9, 1, 8, "smooth_stone_slab", type="top", waterlogged="false")
    b.set(4, 1, 8, "barrel", Compound({"id": String("minecraft:barrel")}), facing="up", open="false")
    b.set(6, 2, 8, "cake", bites="0")
    b.set(8, 2, 8, "potted_cactus")
    # Comedor: dos mesas con sillas
    b.fill(2, 1, 4, 4, 1, 6, "red_carpet")
    b.fill(9, 1, 4, 11, 1, 6, "red_carpet")
    for tx in (3, 10):
        b.table(tx, 1, 5)
        b.stairs(tx - 1, 1, 5, "spruce_stairs", "west")
        b.stairs(tx + 1, 1, 5, "spruce_stairs", "east")
    # Su cama en la esquina
    b.kiko_bed(12, 8, 1, "south", block="kiko:bed_kitchen")
    # Terraza al frente
    b.fill(1, 0, 0, 13, 0, 2, "spruce_planks")
    b.fill(7, 0, 0, 7, 0, 2, "stripped_spruce_wood")
    for tx in (3, 11):
        b.table(tx, 1, 1)
        b.stairs(tx - 1, 1, 1, "spruce_stairs", "west")
        b.stairs(tx + 1, 1, 1, "spruce_stairs", "east")
    for x in (1, 13):
        b.fence(x, 1, 0, "spruce_fence")
        b.fence(x, 2, 0, "spruce_fence")
        b.lantern(x, 3, 0, hanging=False)
    # Huerto atrás con canal de agua
    for x in range(2, 13):
        for z in (13, 14):
            if x == 7:
                b.set(x, 0, z, "water", level="0")
            else:
                b.set(x, 0, z, "farmland", moisture="7")
                crop = ("wheat", "carrots", "potatoes", "beetroots")[(x - 2) // 3 % 4]
                b.set(x, 1, z, crop, age="3" if crop == "beetroots" else "7")
    for x in range(1, 14):
        b.fence(x, 1, 15, "oak_fence")
    for z in range(13, 16):
        b.fence(1, 1, z, "oak_fence")
        b.fence(13, 1, z, "oak_fence")
    b.girl(7, 1, 6, "kiko:tami")
    return b, TOP + 1


# ------------------------------------------------------------------
# La cabañita de Kiko (paredes 9 x 8) con porche y jardín al frente (v3.1)
# ------------------------------------------------------------------
def kiko_cottage():
    b = Build("kiko_cottage", (13, 12, 15))
    b.ground(spots=("coarse_dirt",), chance=0.04)
    b.clear()
    X0, X1, Z0, Z1, TOP = 2, 10, 5, 12, 4
    b.fill(X0, 0, Z0, X1, 0, Z1, "cherry_planks")
    b.fill(X0 + 1, 0, Z0 + 1, X1 - 1, 0, Z1 - 1, "birch_planks")
    for y in range(1, TOP + 1):
        blk = "stone_bricks" if y == 1 else "white_terracotta"
        for x in range(X0, X1 + 1):
            b.set(x, y, Z0, blk)
            b.set(x, y, Z1, blk)
        for z in range(Z0, Z1 + 1):
            b.set(X0, y, z, blk)
            b.set(X1, y, z, blk)
    for x, z in ((X0, Z0), (X1, Z0), (X0, Z1), (X1, Z1)):
        b.log(x, 1, z, x, TOP, z, "stripped_cherry_log", "y")
    b.log(X0 + 1, TOP, Z0, X1 - 1, TOP, Z0, "stripped_cherry_log", "x")
    b.log(X0 + 1, TOP, Z1, X1 - 1, TOP, Z1, "stripped_cherry_log", "x")
    # Puerta, ventanas redondeadas y jardineras
    b.door(6, 1, Z0, "cherry_door", "south")
    for x in (4, 8):
        b.fill(x, 2, Z0, x, 3, Z0, "glass_pane")
        b.set(x, 1, Z0 - 1, "potted_pink_tulip" if x == 4 else "potted_allium")
    for z in (7, 10):
        b.fill(X0, 2, z, X0, 3, z, "glass_pane")
        b.fill(X1, 2, z, X1, 3, z, "glass_pane")
    b.fill(6, 2, Z1, 6, 3, Z1, "glass_pane")
    # Techo rosita de cerezo
    b.gable(1, 11, Z0 - 1, Z1 + 1, TOP + 1, "cherry_stairs", "cherry_slab", "cherry_planks", ends=(Z0, Z1))
    b.set(6, TOP + 2, Z0, "glass_pane")
    b.log(X0 + 1, TOP + 1, 8, X1 - 1, TOP + 1, 8, "stripped_cherry_log", "x")
    b.lantern(4, TOP, 8)
    b.lantern(8, TOP, 8)
    # Adentro: su cama, su ropero (barriles), tocador, mesita con pastel y su rincón de música
    b.kiko_bed(9, 10, 1, "south")
    b.fill(4, 1, 7, 8, 1, 9, "pink_carpet")
    b.set(6, 1, 8, "white_carpet")
    b.barrel(3, 1, 11, "east", "kiko:chests/kiko_cottage")
    b.barrel(3, 2, 11, "east")
    b.chest(3, 1, 10, "east", "kiko:chests/kiko_cottage")
    b.set(3, 1, 9, "jukebox", has_record="false")
    b.set(3, 1, 8, "note_block", instrument="bell", note="12", powered="false")
    b.set(3, 2, 8, "potted_cherry_sapling")
    b.table(6, 1, 11, "cherry")
    b.set(6, 2, 11, "cake", bites="1")
    b.stairs(5, 1, 11, "cherry_stairs", "west")
    b.stairs(7, 1, 11, "cherry_stairs", "east")
    b.set(9, 1, 6, "bookshelf")
    b.set(9, 2, 6, "potted_red_tulip")
    b.set(3, 1, 6, "crafting_table")
    # Porche con barandal y faroles
    b.fill(X0, 0, 2, X1, 0, Z0 - 1, "cherry_planks")
    for x in range(X0, X1 + 1):
        if x not in (5, 6, 7):
            b.fence(x, 1, 2, "cherry_fence")
    b.fence(X0, 2, 2, "cherry_fence")
    b.lantern(X0, 3, 2, hanging=False)
    b.fence(X1, 2, 2, "cherry_fence")
    b.lantern(X1, 3, 2, hanging=False)
    b.stairs(3, 1, 4, "cherry_stairs", "north")
    b.stairs(9, 1, 4, "cherry_stairs", "north")
    # Jardín con camino y flores
    b.fill(6, 0, 0, 6, 0, 1, "dirt_path")
    flowers = ["pink_tulip", "allium", "peony", "oxeye_daisy", "poppy", "cornflower", "lily_of_the_valley"]
    for x in list(range(0, 5)) + list(range(8, 13)):
        for z in range(0, 2):
            if b.rng.random() < 0.6:
                fl = b.rng.choice(flowers)
                if fl == "peony":
                    b.set(x, 1, z, "peony", half="lower")
                    b.set(x, 2, z, "peony", half="upper")
                else:
                    b.set(x, 1, z, fl)
    # Arbolito de cerezo al lado
    tx, tz = 12, 2
    for dx in range(-1, 1):
        for dz in range(-1, 2):
            for dy in (3, 4):
                b.set(tx + dx, dy, tz + dz, "cherry_leaves", persistent="true", distance="1", waterlogged="false")
    b.set(tx, 5, tz, "cherry_leaves", persistent="true", distance="1", waterlogged="false")
    b.log(tx, 1, tz, tx, 3, tz, "cherry_log", "y")
    b.girl(6, 1, 7, "kiko:kiko")
    return b, TOP + 1


# ------------------------------------------------------------------
# Worldgen y loot
# ------------------------------------------------------------------
def dump(rel, obj):
    path = os.path.join(DATA, rel)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2)


def potion(pid, item="minecraft:potion"):
    return (item, 1, 2, 4, [{"function": "minecraft:set_potion", "id": pid}])


LOOT = {
    "maru_forge": [("minecraft:iron_ingot", 2, 6, 10), ("minecraft:coal", 3, 9, 10), ("minecraft:gold_ingot", 1, 3, 4),
                   ("minecraft:iron_nugget", 4, 12, 6), ("minecraft:iron_pickaxe", 1, 1, 2), ("minecraft:iron_sword", 1, 1, 2),
                   ("minecraft:shield", 1, 1, 2), ("minecraft:diamond", 1, 1, 1), ("minecraft:bread", 2, 4, 6)],
    "lumi_library": [("minecraft:book", 2, 5, 10), ("minecraft:paper", 3, 8, 8), ("minecraft:writable_book", 1, 1, 3),
                     ("minecraft:lapis_lazuli", 3, 9, 6), ("minecraft:experience_bottle", 1, 4, 5), ("minecraft:apple", 1, 3, 6),
                     ("minecraft:feather", 1, 3, 4), ("minecraft:candle", 1, 3, 4)],
    "nadi_camp": [("minecraft:map", 1, 2, 6), ("minecraft:compass", 1, 1, 4), ("minecraft:spyglass", 1, 1, 2),
                  ("minecraft:cooked_salmon", 2, 5, 8), ("minecraft:bread", 2, 4, 8), ("minecraft:string", 2, 6, 6),
                  ("minecraft:torch", 4, 12, 8), ("minecraft:emerald", 1, 3, 3), ("minecraft:name_tag", 1, 1, 1)],
    "rina_clinic": [potion("minecraft:healing"), potion("minecraft:regeneration"),
                    potion("minecraft:healing", "minecraft:splash_potion"), ("minecraft:golden_apple", 1, 1, 2),
                    ("minecraft:glistering_melon_slice", 1, 3, 5), ("minecraft:honey_bottle", 1, 3, 6),
                    ("minecraft:glass_bottle", 2, 5, 8), ("minecraft:sugar", 2, 5, 6), ("minecraft:paper", 2, 5, 6),
                    ("minecraft:ghast_tear", 1, 1, 1), ("minecraft:milk_bucket", 1, 1, 3)],
    "kiko_cottage": [("kiko:gift_box", 1, 2, 4), ("minecraft:cookie", 3, 8, 8), ("minecraft:cake", 1, 1, 3),
                     ("kiko:outfit_school", 1, 1, 2), ("kiko:outfit_casual1", 1, 1, 2), ("kiko:outfit_casual2", 1, 1, 2),
                     ("minecraft:pink_dye", 2, 5, 6), ("minecraft:pink_petals", 2, 6, 6), ("minecraft:emerald", 1, 3, 4),
                     ("minecraft:music_disc_cat", 1, 1, 1), ("minecraft:music_disc_mellohi", 1, 1, 1), ("kiko:bouquet", 1, 1, 2)],
    "tami_kitchen": [("minecraft:bread", 2, 5, 8), ("minecraft:cookie", 3, 8, 6), ("minecraft:pumpkin_pie", 1, 2, 5),
                     ("minecraft:egg", 2, 6, 6), ("minecraft:sugar", 2, 6, 6), ("minecraft:wheat", 3, 9, 6),
                     ("minecraft:potato", 2, 6, 6), ("minecraft:carrot", 2, 6, 6), ("minecraft:bowl", 2, 5, 5),
                     ("kiko:guisado", 1, 2, 3), ("kiko:ramen", 1, 2, 3), ("kiko:concha", 1, 3, 4),
                     ("kiko:pay_de_frutas", 1, 2, 3), ("kiko:huevos_rancheros", 1, 2, 3)],
}


def loot_table(entries):
    out = []
    for e in entries:
        n, lo, hi, w = e[:4]
        fns = [{"function": "minecraft:set_count", "count": {"type": "minecraft:uniform", "min": lo, "max": hi}}]
        if len(e) > 4:
            fns += e[4]
        out.append({"type": "minecraft:item", "name": n, "weight": w, "functions": fns})
    return {"type": "minecraft:chest", "pools": [{"rolls": {"type": "minecraft:uniform", "min": 3, "max": 6}, "entries": out}]}


def worldgen(names):
    for n in names:
        dump(f"kiko/worldgen/template_pool/{n}.json", {
            "fallback": "minecraft:empty",
            "elements": [{"weight": 1, "element": {"element_type": "minecraft:single_pool_element",
                                                    "location": f"kiko:{n}", "projection": "rigid",
                                                    "processors": "minecraft:empty"}}]})
        dump(f"kiko/worldgen/structure/{n}.json", {
            "type": "minecraft:jigsaw",
            "biomes": "#kiko:has_structure/girl_home",
            "step": "surface_structures",
            "spawn_overrides": {},
            "terrain_adaptation": "beard_thin",
            "start_pool": f"kiko:{n}",
            "size": 1,
            "start_height": {"absolute": -1},
            "project_start_to_heightmap": "WORLD_SURFACE_WG",
            "max_distance_from_center": 80,
            "use_expansion_hack": False})
        dump(f"kiko/loot_table/chests/{n}.json", loot_table(LOOT[n]))
    dump("kiko/worldgen/structure_set/girl_homes.json", {
        "structures": [{"structure": f"kiko:{n}", "weight": 1} for n in names],
        "placement": {"type": "minecraft:random_spread", "spacing": 32, "separation": 10, "salt": 74135921}})
    dump("kiko/tags/worldgen/biome/has_structure/girl_home.json", {"replace": False, "values": [
        "minecraft:plains", "minecraft:sunflower_plains", "minecraft:meadow", "minecraft:forest",
        "minecraft:birch_forest", "minecraft:flower_forest", "minecraft:savanna", "minecraft:taiga",
        "minecraft:cherry_grove", "minecraft:old_growth_birch_forest"]})


# ------------------------------------------------------------------
# Vista previa (cubitos en isométrico, por fuera y sin techo)
# ------------------------------------------------------------------
COLORS = {
    "stone_bricks": (122, 122, 122), "cracked_stone_bricks": (112, 112, 112), "cobblestone": (110, 110, 110),
    "mossy_cobblestone": (96, 116, 88), "polished_andesite": (136, 138, 136), "stripped_dark_oak_log": (96, 72, 48),
    "dark_oak_planks": (70, 46, 24), "dark_oak_log": (60, 46, 30), "deepslate_tile_stairs": (58, 58, 64),
    "deepslate_tile_slab": (58, 58, 64), "bricks": (150, 82, 64), "brick_stairs": (150, 82, 64), "brick_slab": (150, 82, 64),
    "campfire": (230, 140, 40), "blast_furnace": (90, 90, 96), "lava_cauldron": (230, 110, 30), "water_cauldron": (70, 110, 200),
    "furnace": (100, 100, 100), "smoker": (96, 84, 72), "anvil": (64, 64, 70), "smithing_table": (60, 50, 60),
    "grindstone": (140, 140, 140), "stonecutter": (120, 120, 120), "crafting_table": (150, 100, 60),
    "chest": (160, 110, 50), "barrel": (130, 92, 52), "lantern": (250, 210, 120), "chain": (70, 70, 80),
    "iron_bars": (150, 150, 156), "coal_block": (30, 30, 30), "spruce_trapdoor": (110, 80, 50),
    "kiko:kiko_bed": (255, 140, 128), "spruce_planks": (114, 84, 52), "birch_planks": (214, 200, 150),
    "oak_log": (110, 86, 52), "glass_pane": (190, 220, 235), "spruce_door": (100, 74, 44), "birch_door": (220, 210, 170),
    "spruce_stairs": (104, 76, 46), "spruce_slab": (104, 76, 46), "bookshelf": (140, 96, 60), "purple_carpet": (130, 60, 170),
    "light_blue_carpet": (110, 180, 220), "enchanting_table": (90, 40, 60), "lectern": (160, 120, 70),
    "grass_block": (110, 160, 70), "coarse_dirt": (120, 86, 60), "podzol": (100, 70, 40), "gravel": (130, 124, 120),
    "dirt_path": (150, 124, 70), "green_wool": (80, 120, 40), "lime_wool": (120, 190, 40), "white_wool": (236, 236, 236),
    "stripped_spruce_log": (140, 110, 70), "stripped_spruce_wood": (140, 110, 70), "spruce_log": (80, 60, 36),
    "cartography_table": (120, 90, 60), "loom": (150, 120, 90), "hay_block": (200, 170, 50),
    "spruce_fence": (100, 74, 44), "oak_fence": (160, 130, 80), "spruce_fence_gate": (100, 74, 44),
    "brown_carpet": (110, 72, 40), "orange_carpet": (230, 120, 30), "red_carpet": (170, 40, 40), "white_carpet": (236, 236, 236),
    "white_wall_banner": (240, 240, 240), "light_blue_wall_banner": (110, 180, 220), "orange_wall_banner": (230, 120, 30),
    "tripwire": (220, 220, 220), "sweet_berry_bush": (60, 110, 60), "water": (60, 100, 200), "lily_pad": (40, 130, 40),
    "smooth_quartz": (236, 230, 222), "smooth_quartz_slab": (236, 230, 222), "quartz_pillar": (236, 230, 222),
    "white_terracotta": (210, 180, 162), "cyan_terracotta": (86, 92, 92), "light_gray_terracotta": (136, 106, 98),
    "light_gray_concrete": (126, 126, 116),
    "green_concrete": (72, 140, 60), "sea_lantern": (200, 230, 230), "prismarine_brick_slab": (100, 170, 150),
    "white_bed": (236, 236, 236), "white_stained_glass_pane": (240, 240, 240), "brewing_stand": (110, 100, 80),
    "smooth_stone": (160, 160, 160), "smooth_stone_slab": (160, 160, 160), "azalea": (100, 140, 50),
    "flowering_azalea": (180, 120, 170), "azalea_leaves": (90, 130, 50), "flowering_azalea_leaves": (170, 110, 160),
    "cake": (240, 220, 200), "composter": (110, 80, 50), "farmland": (100, 64, 40),
    "wheat": (210, 190, 80), "carrots": (230, 130, 40), "potatoes": (190, 180, 80), "beetroots": (160, 40, 50),
    "candle": (240, 220, 160), "cherry_planks": (226, 170, 170), "stripped_cherry_log": (220, 150, 150), "cherry_log": (70, 40, 50),
    "cherry_stairs": (226, 170, 170), "cherry_slab": (226, 170, 170), "cherry_door": (226, 170, 170), "cherry_fence": (210, 150, 150),
    "cherry_leaves": (240, 170, 200), "pink_carpet": (240, 150, 180), "jukebox": (110, 70, 50), "note_block": (100, 66, 46),
    "peony": (230, 170, 220), "potted_cherry_sapling": (150, 90, 60), "cherry_pressure_plate": (226, 170, 170), "spruce_pressure_plate": (104, 76, 46), "ladder": (150, 120, 70),
}
FLAT = {"carpet": 0.08, "slab": 0.5, "pressure_plate": 0.06, "lily_pad": 0.04, "tripwire": 0.04, "trapdoor": 0.2}
SMALL = ("potted_", "lantern", "candle", "cake", "fence", "flower", "allium", "orchid", "tulip", "daisy", "bluet",
         "cornflower", "lily_of_the_valley", "dandelion", "poppy", "grass", "fern", "bush", "chain", "brewing", "banner",
         "wheat", "carrots", "potatoes", "beetroots", "ladder", "bars", "pane", "azalea")


def render(b, W, H, cut=None, s=14):
    from PIL import Image, ImageDraw
    img = Image.new("RGB", (W, H), (40, 42, 54))
    d = ImageDraw.Draw(img)
    sx, sy, sz = b.size
    cx, cy = W / 2 - (sx - sz) * s * 0.87 / 2, H - 30 - (sx + sz) * s * 0.5

    def iso(x, y, z):
        return (cx + (x - z) * s * 0.87, cy + (x + z) * s * 0.5 - y * s)

    cells = []
    for (x, y, z0), (name, props, _) in b.blocks.items():
        short = name.split(":")[-1] if not name.startswith("kiko:") else name
        if short == "air" or (cut is not None and y > cut):
            continue
        z = sz - 1 - z0      # el frente (z = 0) queda viendo hacia la cámara
        col = COLORS.get(short, (200, 0, 200))
        if short.startswith("potted_"):
            col = (150, 90, 60)
        elif short not in COLORS and any(k in short for k in ("orchid", "allium", "tulip", "daisy", "bluet", "cornflower",
                                                               "lily_of", "dandelion", "poppy")):
            col = (220, 120, 200)
        elif short in ("short_grass", "fern"):
            col = (90, 150, 60)
        h = 1.0
        for k, v in FLAT.items():
            if k in short and not (k == "slab" and props.get("type") == "double"):
                h = v
        y0 = y + (0.5 if props.get("type") == "top" and "slab" in short else 0)
        inset = 0.3 if any(k in short for k in SMALL) else 0.0
        cells.append((x, y0, z, h, col, inset))
    for x, y, z, h, col, ins in sorted(cells, key=lambda c: (c[0] + c[2], c[1])):
        a, bb = x + ins, x + 1 - ins
        c0, c1 = z + ins, z + 1 - ins
        hh = h * (0.6 if ins and h == 1.0 else 1.0)
        top = [iso(a, y + hh, c0), iso(bb, y + hh, c0), iso(bb, y + hh, c1), iso(a, y + hh, c1)]
        left = [iso(a, y + hh, c1), iso(bb, y + hh, c1), iso(bb, y, c1), iso(a, y, c1)]
        right = [iso(bb, y + hh, c0), iso(bb, y + hh, c1), iso(bb, y, c1), iso(bb, y, c0)]
        d.polygon(left, fill=tuple(int(v * 0.72) for v in col))
        d.polygon(right, fill=tuple(int(v * 0.86) for v in col))
        d.polygon(top, fill=col, outline=tuple(int(v * 0.6) for v in col))
    for (x, y, z, eid, _) in b.entities:
        if eid == "minecraft:armor_stand":
            continue
        px, py = iso(x, y, sz - z)
        d.ellipse([px - 6, py - 32, px + 6, py - 20], fill=(255, 210, 120))
        d.rectangle([px - 5, py - 20, px + 5, py], fill=(255, 170, 90))
    return img


def preview(builds, out):
    from PIL import Image, ImageDraw, ImageFont
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 18)
    W, H = 520, 520
    for b, cut in builds:
        sheet = Image.new("RGB", (W * 2, H + 36), (40, 42, 54))
        sheet.paste(render(b, W, H), (0, 36))
        sheet.paste(render(b, W, H, cut=cut - 1), (W, 36))
        d = ImageDraw.Draw(sheet)
        d.text((10, 8), f"{b.name}   por fuera", fill=(255, 210, 120), font=font)
        d.text((W + 10, 8), "sin techo", fill=(200, 200, 210), font=font)
        sheet.save(os.path.join(out, f"casa_{b.name}.png"))


def main(preview_dir=None):
    builds = [kiko_cottage(), maru_forge(), lumi_library(), nadi_camp(), rina_clinic(), tami_kitchen()]
    for b, _ in builds:
        b.save()
    worldgen([b.name for b, _ in builds])
    if preview_dir:
        os.makedirs(preview_dir, exist_ok=True)
        preview(builds, preview_dir)


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else None)
