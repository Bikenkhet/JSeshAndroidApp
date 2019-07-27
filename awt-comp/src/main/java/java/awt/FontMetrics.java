package java.awt;

import android.graphics.Paint;

import java.io.Serializable;

public class FontMetrics implements Serializable {

    protected Font font;

    private Paint paint;

    public FontMetrics(Font font) {
        this.font = font;

        paint = new Paint();
        paint.setTypeface(font.getTypeface());
        paint.setTextSize(font.getSize2D());

    }

    public int getAscent() {
        //FIXME Negative very dubious, but it makes line number position better
        return -(int)paint.getFontMetrics().ascent;
    }

}
