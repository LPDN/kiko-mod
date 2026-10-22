"""
Genera los 4 sonidos de Kiko como tonitos sintetizados (nada de voces de anime).
Uso   python tools/gen_sounds.py   (requiere numpy y ffmpeg con libvorbis)
"""
import os
import subprocess
import tempfile
import wave

import numpy as np

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OUT = os.path.join(ROOT, "src", "main", "resources", "assets", "kiko", "sounds")
SR = 44100


def tone(f0, f1, dur, vol=0.5, vibrato=0.0):
    t = np.linspace(0, dur, int(SR * dur), endpoint=False)
    freq = np.linspace(f0, f1, t.size) * (1 + vibrato * np.sin(2 * np.pi * 28 * t))
    phase = 2 * np.pi * np.cumsum(freq) / SR
    wave_ = np.sin(phase) + 0.35 * np.sin(2 * phase) + 0.12 * np.sin(3 * phase)
    env = np.minimum(1, t / 0.01) * np.exp(-t / (dur * 0.55))
    return wave_ * env * vol


def silence(dur):
    return np.zeros(int(SR * dur))


SOUNDS = {
    # "¡pi-pi!" saludo que sube
    "greet": [tone(880, 1180, 0.09), silence(0.04), tone(1050, 1500, 0.13)],
    # arpegio feliz
    "happy": [tone(990, 990, 0.07), tone(1250, 1250, 0.07), tone(1480, 1700, 0.16, vibrato=0.01)],
    # "hmph" que baja
    "sad": [tone(700, 520, 0.12), silence(0.03), tone(560, 380, 0.22, vibrato=0.02)],
    # "¡ay!" corto
    "hurt": [tone(1300, 760, 0.12, vol=0.6)],
}


def main():
    os.makedirs(OUT, exist_ok=True)
    for name, parts in SOUNDS.items():
        data = np.concatenate(parts)
        data = data / max(1e-6, np.abs(data).max()) * 0.8
        pcm = (data * 32767).astype(np.int16)
        with tempfile.NamedTemporaryFile(suffix=".wav", delete=False) as tmp:
            path = tmp.name
        with wave.open(path, "wb") as w:
            w.setnchannels(1)
            w.setsampwidth(2)
            w.setframerate(SR)
            w.writeframes(pcm.tobytes())
        subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", path, "-c:a", "libvorbis", "-q:a", "5",
                        os.path.join(OUT, f"{name}.ogg")], check=True)
        os.remove(path)


if __name__ == "__main__":
    main()
