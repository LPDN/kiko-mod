"""
Cuerpos distintos (v3.4.2). No cambia los modelos, en el juego se ensancha o adelgaza el torso, los hombros y las piernas
(ModelPart xScale/zScale y la posición de brazos y piernas), igual que aquí en la vista previa.

  BODIES   tipo -> (ancho torso, fondo torso, hombros +-, ancho piernas, cadera +-)
"""
import os
import random
import sys

from PIL import Image, ImageDraw, ImageFont

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_assets as g  # noqa: E402
import gen_girls as gg  # noqa: E402
import gen_kids as gk  # noqa: E402
import gen_townsfolk as gt  # noqa: E402

BODIES = {
    "normal": (1.0, 1.0, 0.0, 1.0, 0.0),
    "slim": (0.88, 0.9, -0.5, 0.86, -0.3),
    "athletic": (1.12, 1.06, 0.9, 1.08, 0.2),
    "soft": (1.08, 1.1, 0.3, 1.1, 0.4),
    "full": (1.2, 1.2, 0.7, 1.18, 0.7),
}
GIRLS = {"kiko": "normal", "maru": "athletic", "lumi": "slim", "nadi": "slim", "rina": "soft", "tami": "full"}
LABELS = {"normal": "normal", "slim": "delgada(o)", "athletic": "atlética(o)", "soft": "suave", "full": "llenita(o)"}


def pose_for(kind):
    w, d, sh, lw, hip = BODIES[kind]
    p = {"body": {"xs": w, "zs": d},
         "right_arm": {"dx": -sh}, "left_arm": {"dx": sh},
         "right_leg": {"xs": lw, "zs": lw, "dx": -hip}, "left_leg": {"xs": lw, "zs": lw, "dx": hip}}
    return p


def main(out):
    os.makedirs(out, exist_ok=True)
    font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 15)
    small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 12)
    girls = [gg.complete(fn()) for fn in (gg.maru, gg.lumi, gg.nadi, gg.rina, gg.tami)]
    W, H, scale = 230, 470, 6.0
    # 1) Las chicas, antes (arriba) y ahora (abajo)
    sheet = Image.new("RGBA", (W * 6, H * 2 + 60), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    entries = [("kiko", None)] + [(G.name, G) for G in girls]
    for i, (name, G) in enumerate(entries):
        for row, kind in enumerate(("normal", GIRLS[name])):
            pose = pose_for(kind)
            if G is None:
                g.PARTS = gg.KIKO_PARTS
                g.PART = {p["name"]: p for p in gg.KIKO_PARTS}
                tex = g.build_texture("casual2")
                img = g.render(tex, "casual2", view_yaw=0.35, scale=scale, size=(W, H), center=(W / 2, H - 20), pose=pose)
            else:
                gg.use(G)
                tex = gg.texture(G)
                gg.use_no_pack(G)
                k = G.height / 1.8
                img = g.render(tex, G.name, view_yaw=0.35, view_pitch=-0.1, scale=scale * k, size=(W, H), center=(W / 2, H - 20),
                               hidden=gg.PREVIEW_HIDDEN, pose=pose)
            sheet.alpha_composite(img, (i * W, 40 + row * H))
        d.text((i * W + 10, 8), name.capitalize(), fill=(255, 210, 120), font=font)
        d.text((i * W + 10, 24), "ahora " + LABELS[GIRLS[name]], fill=(190, 190, 200), font=small)
    d.text((6, 44), "antes", fill=(150, 150, 160), font=small)
    d.text((6, 44 + H), "ahora", fill=(150, 150, 160), font=small)
    sheet.convert("RGB").save(os.path.join(out, "cuerpos_chicas.png"))

    # 2) Vecinos con los 5 cuerpos
    rng = random.Random(9)
    picks = [("woman_dress", k) for k in BODIES] + [("man_shirt", k) for k in BODIES]
    sheet = Image.new("RGBA", (W * 5, (H - 40) * 2 + 40), (40, 42, 54, 255))
    d = ImageDraw.Draw(sheet)
    models = {"woman_dress": gt.build("woman_dress", gt.woman_dress), "man_shirt": gt.build("man_shirt", gt.man_shirt)}
    cols = {m: gt.random_npc(rng, m) for m in models}
    for i, (m, kind) in enumerate(picks):
        G = models[m]
        gg.use(G)
        tex = gk.colorize(gg.texture(G), *cols[m])
        gg.use_no_pack(G)
        h = gt.height(m)
        hidden = set(gg.PREVIEW_HIDDEN) | set(gt.HAIR_STYLES)
        img = g.render(tex, G.name, view_yaw=0.35, view_pitch=-0.1, scale=scale * h / 1.8 * 61.75 / G.units * (G.units / 61.75),
                       size=(W, H - 40), center=(W / 2, H - 60), hidden=hidden, pose=pose_for(kind))
        x, y = (i % 5) * W, (i // 5) * (H - 40) + 30
        sheet.alpha_composite(img, (x, y))
        d.text((x + 10, y - 22 if i < 5 else y + 4), LABELS[kind], fill=(255, 210, 120), font=font)
    sheet.convert("RGB").save(os.path.join(out, "cuerpos_vecinos.png"))


if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else "/tmp/claude-0/bodies")
