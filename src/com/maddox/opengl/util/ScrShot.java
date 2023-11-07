package com.maddox.opengl.util;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.maddox.TexImage.TexImage;
import com.maddox.il2.engine.Config;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.Provider;
import com.maddox.opengl.gl;
import com.maddox.rts.HomePath;
import com.maddox.sas1946.il2.util.CommonTools;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ScrShot {

    public void grab() {
        this.doGrab(null);
    }

    public void grab(String fileName) {
        this.doGrab(fileName);
    }
    
    private void doGrab(String fileName) {
        String fileNameTga = fileName == null ? this.scrName(TYPE_TGA) : SCREENSHOTS_DIR + fileName + types[TYPE_TGA];
        String fileNameJpg = fileName == null ? this.scrName(TYPE_JPG) : SCREENSHOTS_DIR + fileName + types[TYPE_JPG];
        String glProvider = Provider.GLname();
        if ((glProvider != null) && (glProvider.toLowerCase().startsWith("opengl")))
        {
            this.getScreenShot(this.screenshotType, fileNameTga, fileNameJpg, (int) (this.jpgQuality * 100F));
            return;
        }
        this.dx = GLContext.getCurrent().width();
        this.dy = GLContext.getCurrent().height();
        if (this.img == null) this.img = new TexImage();
        this.img.set(this.dx, this.dy, GL_RGB);
        gl.ReadPixels(0, 0, this.dx, this.dy, GL_RGB, GL_UNSIGNED_BYTE, this.img.image);
        switch (this.screenshotType) {
            case TYPE_TGA:
                grabDX_TGA(fileNameTga);
                break;
            case TYPE_JPG:
                grabDX_JPG(fileNameJpg);
                break;
            case TYPE_BOTH:
                grabDX_TGA(fileNameTga);
                grabDX_JPG(fileNameJpg);
                break;
            default:
                grabDX_TGA(fileNameTga);
        }
    }

    public void grabDX_TGA(String fileName) {
//        System.out.println("grabDX_TGA Screenshot: " + fileName);
        try {
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(0);
            os.write(0);
            os.write(2);
            os.write(new byte[5]);
            os.write(0);
            os.write(0);
            os.write(0);
            os.write(0);
            os.write((short) this.dx);
            os.write((short) (this.dx >> 8));
            os.write((short) this.dy);
            os.write((short) (this.dy >> 8));
            os.write((byte) (this.img.BPP * 8));
            os.write(0);
            this.swapRB();
            os.write(this.img.image, 0, this.dx * this.dy * this.img.BPP);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void grabDX_JPG(String fileName) {
//        System.out.println("grabDX_JPG Screenshot: " + fileName);
        if (this.screenshotType != TYPE_JPG) this.swapRB();
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            DataBufferByte dbb = new DataBufferByte(this.img.image, this.dx * this.dy * 3, 0);
            WritableRaster wr = Raster.createInterleavedRaster(dbb, this.dx, this.dy, this.dx * 3, 3, new int[] { 0, 1, 2 }, null);

            ComponentColorModel ccm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8 }, false, false, 1, 0);

            BufferedImage bi = new BufferedImage(ccm, wr, false, null);
            for (int i = 0; i < this.dy / 2 - 1; i++) {
                for (int j = 0; j < this.dx; j++) {
                    int k = bi.getRGB(j, i);
                    int m = bi.getRGB(j, this.dy - i - 1);
                    bi.setRGB(j, this.dy - i - 1, k);
                    bi.setRGB(j, i, m);
                }
            }
            JPEGImageEncoder jie = JPEGCodec.createJPEGEncoder(os);
            JPEGEncodeParam jep = jie.getDefaultJPEGEncodeParam(bi);
            jep.setQuality(this.jpgQuality, true);
            jie.encode(bi, jep);
            os.flush();
            bi.flush();
            bi = null;
        } catch (FileNotFoundException fnfe) {
            System.out.println("Image " + fileName + " (jpg file) could not be created.");
            fnfe.printStackTrace();
        } catch (ImageFormatException ife) {
            System.out.println("Image Format Exception while trying to create a " + fileName + " jpg file");
            ife.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("IOException while trying to create a " + fileName + " jpg file");
            ioe.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException ioe) {
                System.out.println("Unable to close stream while trying to create a " + fileName + " jpg file");
                ioe.printStackTrace();
            }
        }
    }

    private void swapRB()
    {
      int i = this.dx * 3;
      for (int j = 0; j < this.dy; j++) {
        for (int k = 0; k < i; k += 3)
        {
          int m = this.img.image(k, j);
          int n = this.img.image(k + 2, j);
          this.img.image(k, j, n);
          this.img.image(k + 2, j, m);
        }
      }
    }
    
    private String scrName(int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, CommonTools.getTimeZoneBiasMinutes());
        String str = SCREENSHOTS_DIR + this.shortDate.format(calendar.getTime()) + types[type];
        str = str.replace('\\', '-');

        str = str.replace(':', '-');
        str = str.replace('*', '-');
        str = str.replace('?', '-');
        str = str.replace('"', '-');
        str = str.replace('<', '-');
        str = str.replace('>', '-');
        str = str.replace('|', '-');
        
//        if (this.screenshotType == TYPE_BOTH || this.screenshotType == type) System.out.println("Screenshot: " + str);

        return str;
    }
    
    public ScrShot(String s) {
        this.shortDate = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss.SSS");
        this.screenshotType = Config.cur.ini.get("game", "ScreenshotType", 1, 0, 2);
        this.jpgQuality = Config.cur.ini.get("game", "jpgQuality", 1.0F, 0.0F, 1.0F);
        this.scrDir = new File(HomePath.toFileSystemName(SCREENSHOTS_DIR, 0));
        if (!this.scrDir.exists()) this.scrDir.mkdirs();
    }

    private native void getScreenShot(int i, String s, String s1, int j);

    private int dx;
    private int dy;
    private TexImage img = null;
    
    private static final int GL_BGR = 0x80E0; // 32992
    private static final int GL_RGB = 0x1907; // 6407
    private static final int GL_UNSIGNED_BYTE = 0x1401; // 5121
    
    private static final int TYPE_TGA = 0;
    private static final int TYPE_JPG = 1;
    private static final int TYPE_BOTH = 2;

    private DateFormat          shortDate;
    private int                 screenshotType;
    private float               jpgQuality;
    private static final String TGA             = ".tga";
    private static final String JPG             = ".jpg";
    private static final String[] types= {TGA, JPG};
    private static final String SCREENSHOTS_DIR = "MyScreenShots/";
    private File                scrDir;
}
