package com.maddox.il2.objects.sounds;

import com.maddox.JGP.Point3d;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class SfxExplosion {
    private static SoundPreset crashTank;
    private static SoundPreset crashAir;
    private static SoundPreset explodeZenitka;
    private static SoundPreset explodeBullet;
    private static SoundPreset explodeShell;
    private static SoundPreset explodeBig;
    private static SoundPreset explodeMiddle;
    private static SoundPreset explodeGiant;

    private static boolean init() {
        if (SfxExplosion.crashTank != null) {
            return true;
        }
        try {
            SfxExplosion.crashTank = new SoundPreset("crash.tank");
            SfxExplosion.crashAir = new SoundPreset("crash.air");
            SfxExplosion.explodeZenitka = new SoundPreset("explode.zenitka");
            SfxExplosion.explodeBullet = new SoundPreset("explode.bullet");
            SfxExplosion.explodeShell = new SoundPreset("explode.shell");
            SfxExplosion.explodeBig = new SoundPreset("explode.big");
            SfxExplosion.explodeGiant = new SoundPreset("explode.giant");
            SfxExplosion.explodeMiddle = new SoundPreset("explode.middle");
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static void crashTank(Point3d pos, int usr) {
        if (!SfxExplosion.init()) {
            return;
        }
        SoundFX soundFX = new SoundFX(SfxExplosion.crashTank);
        soundFX.setPosition(pos);
        soundFX.play();
    }

    public static void crashAir(Point3d pos, int usr) {
        if (!SfxExplosion.init()) {
            return;
        }
        SoundFX soundFX = new SoundFX(SfxExplosion.crashAir);
        soundFX.setPosition(pos);
        soundFX.setUsrFlag(usr);
        soundFX.play();
    }

    public static void crashParts(Point3d pos, int usr) {
    }

    public static void zenitka(Point3d pos, int usr) {
        if (!SfxExplosion.init()) {
            return;
        }

        SoundFX soundFX = new SoundFX(SfxExplosion.explodeZenitka);
        soundFX.setPosition(pos);
        soundFX.setUsrFlag(usr > 1 ? 1 : 0);
        soundFX.play();
    }

    public static void shell(Point3d pos, int usr, float chargeMass, int type, float radius) {
        if (!SfxExplosion.init() || (chargeMass < 0.001F)) {
            return;
        }
        SoundFX soundFX;
        if (chargeMass < 0.02F) {
//            System.out.println("SfxExplosion soundFX = new SoundFX(SfxExplosion.explodeBullet)");
            soundFX = new SoundFX(SfxExplosion.explodeBullet);
        } else if (chargeMass < 0.5F) {
//            System.out.println("SfxExplosion soundFX = new SoundFX(SfxExplosion.explodeShell)");
            soundFX = new SoundFX(SfxExplosion.explodeShell);
        } else if (chargeMass < 50.0F) {
//            System.out.println("SfxExplosion soundFX = new SoundFX(SfxExplosion.explodeMiddle)");
            soundFX = new SoundFX(SfxExplosion.explodeMiddle);
        } else if (chargeMass < Float.MAX_VALUE) {
//            System.out.println("SfxExplosion soundFX = new SoundFX(SfxExplosion.explodeBig)");
            soundFX = new SoundFX(SfxExplosion.explodeBig);
        } else {
//          System.out.println("SfxExplosion soundFX = new SoundFX(SfxExplosion.explodeGiant)");
          soundFX = new SoundFX(SfxExplosion.explodeGiant);
        }

        soundFX.setPosition(pos);
        soundFX.setUsrFlag(usr);
//        System.out.println("SfxExplosion playing Sound...");
        soundFX.play();
//        System.out.println("SfxExplosion ...Sound playing.");
    }

    public static void building(Point3d pos, int usr, float[] size) {
    }

    public static void bridge(Point3d posBegin, Point3d posEnd, float len) {
    }

    public static void wagon(Point3d posBegin, Point3d posEnd, float len, int type) {
    }
}
