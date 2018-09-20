package java.awt;

import android.graphics.Paint;

import java.io.Serializable;

public class FontMetrics implements Serializable {

    protected Font font;

    private Paint paint;

    protected FontMetrics(Font font) {
        this.font = font;

        paint = new Paint();
        paint.setTypeface(font.getTypeface());


    }

    public int getAscent() {
        return (int)paint.getFontMetrics().ascent;
    }

}
