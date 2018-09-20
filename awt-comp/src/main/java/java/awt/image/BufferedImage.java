package java.awt.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import jsesh.android.graphics.CanvasGraphics;

public class BufferedImage extends Image {

    private CanvasGraphics graphics;
    private Canvas canvas;
    private Bitmap bitmap;


    public static final int TYPE_INT_RGB = 1;
    public static final int TYPE_INT_ARGB = 2;


    public BufferedImage(int width, int height, int imageType) {

        //TODO Use imageType

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(width, height, conf);
        canvas = new Canvas(bitmap);

    }

    public Graphics getGraphics() {
        return createGraphics();
    }

    public Graphics2D createGraphics() {
        //TODO IMPORTANT Do this properly
        return graphics;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public void flush() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }


}
