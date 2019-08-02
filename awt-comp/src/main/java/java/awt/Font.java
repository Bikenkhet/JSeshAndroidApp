package java.awt;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class Font implements Serializable {

    public static final int PLAIN       = 0;
    public static final int BOLD        = 1;
    public static final int ITALIC      = 2;



    public static final int TRUETYPE_FONT = 0;

    private Typeface typeface;

    private float size = 1.0f;

    private String name = "Default";

    private String source;

    public Typeface getTypeface() {
        return typeface;
    }




    public static Font createFont(int fontFormat, InputStream fontStream) {

/*
        AssetManager am = context.getApplicationContext().getAssets();

        typeface = Typeface.createFromAsset(am, "fonts/" + "abc.ttf");
*/
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    public static Font fromAssets(AssetManager mgr, String path) throws IOException {
        Font font = new Font();
        font.typeface = Typeface.createFromAsset(mgr, path);
        if (font.typeface == null) throw new IOException();
        font.source = path;
        return font;
    }

    public Font(Typeface typeface) {
        this.typeface = typeface;
    }

    private Font() {

    }

    public Font(String name, int style, int size) {
        /*
        this.name = (name != null) ? name : "Default";
        this.style = (style & ~0x03) == 0 ? style : 0;
        this.size = size;
        this.pointSize = size;
        */

        this.name = name;

        //FIXME TEMP
        //FIXME Not great beyond specific circumstances

        //TODO Needs extending
        Typeface family = Typeface.DEFAULT;
        if (name != null) {
            String fam = name.toLowerCase();
            if (fam.equals("serif")) family = Typeface.SERIF;
        }

        //TODO Needs extending
        int typefaceStyle = Typeface.NORMAL;
        if (style == ITALIC) typefaceStyle = Typeface.ITALIC;
        else if (style == BOLD) typefaceStyle = Typeface.BOLD;
        else if (style == (BOLD | ITALIC)) typefaceStyle = Typeface.BOLD_ITALIC;


        this.typeface = Typeface.create(family, typefaceStyle);

        //this.typeface = Typeface.create(name, Typeface.NORMAL);
        //this.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);   //FIXME !!! INCORRECT STYLE
        //this.typeface = Typeface.create(name, style);   //FIXME !!! INCORRECT STYLE

        this.size = size;

    }

    public Rectangle2D getStringBounds(String text, FontRenderContext renderContext) {
        //FIXME A valid FontRenderContext must be provided and used
        Paint p = new Paint();
        p.setTypeface(typeface);
        Rect rect = new Rect();
        p.getTextBounds(text, 0, text.length(), rect);
        //TODO I don't know why this needs to be halved, but it looks right
        return new Rectangle2D.Float(rect.left, rect.top, 0.5f*(rect.right - rect.left), 0.5f*(rect.bottom - rect.top));
    }

    public Font deriveFont(Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {
        //FIXME This should probably do something
        Font font = this.copy();
        return font;
    }

    public Font deriveFont(float size) {
        Font font = this.copy();
        font.size = size;
        return font;
    }

    public int getSize() {
        return (int)size;
    }

    public float getSize2D() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Font copy() {
        Font font = new Font(Typeface.create(this.typeface, this.typeface.getStyle()));
        font.size = size;
        font.name = name;
        font.source = source;
        return font;
    }

}
