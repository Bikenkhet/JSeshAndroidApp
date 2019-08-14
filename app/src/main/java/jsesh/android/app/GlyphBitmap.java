package jsesh.android.app;

import android.graphics.Bitmap;

import jsesh.android.graphics.ImageIconFactory;

public class GlyphBitmap {

    private Bitmap bitmap;
    public final String code;

    public GlyphBitmap(String code) {
        this.code = code;
    }

    public synchronized Bitmap getBitmap() {
        if (bitmap == null) {
            bitmap = ImageIconFactory.getInstance().buildGlyphImage(code);
        }
        return bitmap;
    }

}
