package jsesh.android.graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class CanvasGraphics extends Graphics2D {

    //TEMP
    private int nest;

    private Canvas canvas;
    private Paint paint;
    private Paint backgroundPaint;

    private Color color;
    private Color background;

    private AffineTransform affineTransform;

    private Matrix matrix;

    private float scaleX = 1;

    private RenderingHints renderingHints;

    private Stroke stroke;

    private Font font;

    public static CanvasGraphics create(Canvas canvas) {

        //System.out.println("Creating graphics");

        CanvasGraphics canvasGraphics = new CanvasGraphics(canvas);

        canvasGraphics.nest = canvas.getSaveCount();

        return canvasGraphics;

    }

    public CanvasGraphics() {}

    public CanvasGraphics(Canvas canvas) {
        setCanvas(canvas);
    }

    public void setCanvas(Canvas canvas) {

        this.canvas = canvas;
        paint = new Paint();
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);

        //TODO Check defaults (Graphics2D / swing)
        color = Color.BLACK;
        background = Color.WHITE;

        affineTransform = new AffineTransform();

        matrix = new Matrix();

        renderingHints = new RenderingHints(null);

        //TODO This should be handled through rendering hints
        paint.setAntiAlias(true);

    }

    public void clear() {
        drawRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }




    @Override
    public void draw(Shape s) {

        PathIterator iterator = s.getPathIterator(null);
        Path path = GraphicsUtils.getPathFromIterator(iterator);

        Matrix inv = new Matrix();
        matrix.invert(inv);
        canvas.concat(inv);
        path.transform(matrix);

        paint.setStrokeWidth(paint.getStrokeWidth()*scaleX);
        paint.setStyle(Paint.Style.STROKE);


        canvas.drawPath(path, paint);
        canvas.concat(matrix);
        paint.setStrokeWidth(paint.getStrokeWidth()/scaleX);

    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void drawString(String str, int x, int y) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void drawString(String str, float x, float y) {
        paint.setStyle(Paint.Style.FILL);
        //Use stroke width 0 for drawing text, it seems Graphics2D doesn't use it for text, but
        //canvas does?
        if (paint.getStrokeWidth() != 0) paint.setStrokeWidth(0);
        float tempStrokeWidth = paint.getStrokeWidth();
        canvas.drawText(str, x, y, paint);
        if (tempStrokeWidth != 0) paint.setStrokeWidth(tempStrokeWidth);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void dispose() {
        //Check if it is already disposed; this is needed because some graphics seem to be disposed more than once
        if (canvas != null) {
            for (int i=0;i<nest-canvas.getSaveCount();i++) canvas.save();
            //System.out.println("RESTORING:"+canvas.getSaveCount()+"|"+nest);
            if (nest > 2) canvas.restore();

            //FOR GC purposes, hopefully
            canvas = null;
        }
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void fill(Shape s) {
        PathIterator iterator = s.getPathIterator(null);
        Path path = GraphicsUtils.getPathFromIterator(iterator);

        Matrix inv = new Matrix();
        matrix.invert(inv);
        canvas.concat(inv);
        path.transform(matrix);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

        canvas.concat(matrix);
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setStroke(Stroke s) {
        stroke = s;
        if (stroke instanceof BasicStroke) {
            paint.setStrokeWidth(((BasicStroke) stroke).getLineWidth());
        }
    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        renderingHints = (RenderingHints) hints;
    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public RenderingHints getRenderingHints() {
        return (RenderingHints) renderingHints.clone();
    }

    @Override
    public Graphics create() {

        CanvasGraphics canvasGraphics = new CanvasGraphics(canvas);

        canvasGraphics.nest = nest + 1;

        canvasGraphics.paint = new Paint(this.paint);
        canvasGraphics.backgroundPaint = new Paint(this.backgroundPaint);

        canvasGraphics.color = this.color;
        canvasGraphics.background = this.background;

        canvasGraphics.affineTransform = (AffineTransform) affineTransform.clone();
        canvasGraphics.matrix = new Matrix(matrix);

        canvasGraphics.scaleX = scaleX;

        //TODO Is this ok for copying? Won't work if original is used before dispose
        //TODO Seems alright with fix in dispose
        canvas.save();
        //System.out.println("SAVING:"+canvas.getSaveCount() + "|"+canvasGraphics.nest);


        return canvasGraphics;

    }

    @Override
    public void translate(int x, int y) {
        affineTransform.translate(x, y);
        matrix.preTranslate(x, y);
        canvas.translate(x, y);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color c) {
        if (color.getRGB() != c.getRGB()) {
            color = c;
            paint.setColor(c.getRGB());
        }
    }

    @Override
    public void setPaintMode() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    @Override
    public void setXORMode(Color c1) {
        //TODO Not actually right
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
    }

    @Override
    public Font getFont() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
        paint.setTextSize(font.getSize2D());
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return null;
    }

    @Override
    public Rectangle getClipBounds() {
        Rect rect = canvas.getClipBounds();
        return new Rectangle(rect.left, rect.top, rect.width(), rect.height());
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        canvas.clipRect(x, y, x + width, y + height);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        //TODO Check
        canvas.clipRect(x, y, x + width, y + height, Region.Op.REPLACE);
    }

    @Override
    public Shape getClip() {
        //TODO No equivalent?
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void setClip(Shape clip) {
        canvas.clipPath(GraphicsUtils.getPathFromIterator(clip.getPathIterator(null)), Region.Op.REPLACE);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, width, height, paint);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        canvas.drawRect(x, y, width, height, backgroundPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(x, y, width, height, arcWidth, arcHeight, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(x, y, width, height, arcWidth, arcHeight, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(x, y, width, height, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void fillOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(x, y, width, height, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        paint.setStyle(Paint.Style.STROKE);
        //TODO useCenter?
        canvas.drawArc(x, y, width, height, startAngle, arcAngle, true, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        paint.setStyle(Paint.Style.FILL);
        //TODO useCenter?
        canvas.drawArc(x, y, width, height, startAngle, arcAngle, true, paint);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void translate(double tx, double ty) {
        affineTransform.translate(tx, ty);
        matrix.preTranslate((float)tx, (float)ty);
        canvas.translate((float)tx, (float)ty);
    }

    @Override
    public void rotate(double theta) {
        affineTransform.rotate(theta);
        matrix.preRotate((float)(theta*180.0/Math.PI));
        canvas.rotate((float)(theta*180.0/Math.PI));
    }

    @Override
    public void rotate(double theta, double x, double y) {
        affineTransform.rotate(theta, x, y);
        matrix.preRotate((float)(theta*180.0/Math.PI), (float)x, (float)y);
        canvas.rotate((float)(theta*180.0/Math.PI), (float)x, (float)y);
    }

    @Override
    public void scale(double sx, double sy) {
        affineTransform.scale(sx, sy);
        matrix.preScale((float)sx, (float)sy);
        canvas.scale((float)sx, (float)sy);
        scaleX*=sx;
    }

    @Override
    public void shear(double shx, double shy) {
        affineTransform.shear(shx, shy);
        //FIXME Matrix
        canvas.scale((float)shx, (float)shy);
    }

    @Override
    public void transform(AffineTransform Tx) {
        //TODO Not this
        affineTransform.concatenate(Tx);
        Matrix m = GraphicsUtils.getMatrixFromAffineTransform(Tx);
        matrix.preConcat(m);
        canvas.concat(m);

    }

    @Override
    public void setTransform(AffineTransform Tx) {
        //TODO Not this
        affineTransform.setTransform(Tx);
        //FIXME Matrix
        canvas.setMatrix(GraphicsUtils.getMatrixFromAffineTransform(Tx));
    }

    @Override
    public AffineTransform getTransform() {
        return affineTransform;
    }

    @Override
    public void setBackground(Color color) {
        if (color.getRGB() != background.getRGB()) {
            background = color;
            backgroundPaint.setColor(color.getRGB());
        }
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void clip(Shape s) {
        canvas.clipPath(GraphicsUtils.getPathFromIterator(s.getPathIterator(null)));
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        FontRenderContext f;
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override public String toString() {
        return getClass().getName();
    }

    private class CanvasFontRenderContext extends FontRenderContext {

        AffineTransform affineTransform;


        @Override
        public AffineTransform getTransform() {
            return affineTransform;
        }
    }

}