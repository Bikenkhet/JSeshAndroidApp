package java.awt.font;

import android.graphics.Paint;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

public class TextLayout {

    private String text;
    private Font font;
    private FontRenderContext fontRenderContext;

    public TextLayout(String text, Font font, FontRenderContext fontRenderContext) {
        this.text = text;
        this.font = font;
        this.fontRenderContext = fontRenderContext;
    }

    public Rectangle2D getBounds() {
        return font.getStringBounds(text, fontRenderContext);
    }

    public float getAdvance() {
        Paint p = new Paint();
        p.setTypeface(font.getTypeface());
        return p.measureText(text);
    }

    public double getAscent() {
        Paint p = new Paint();
        p.setTypeface(font.getTypeface());
        return p.getFontMetrics().ascent;
    }
}
