/*
 * Created on 5 juil. 2005 by rosmord
 *
 * TODO document the file BitmapStreamGraphics.java
 *
 * This file is distributed along the GNU Lesser Public License (LGPL)
 * author : rosmord
 */
package jsesh.android.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Toast;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.util.Properties;

import jsesh.android.AndroidUtils;

/**
 * A StreamGraphics2D for bitmaps files.
 *
 * @author rosmord
 */
public class BitmapStreamGraphics extends CanvasGraphics {

    //OutputStream out;
    Context context;
    String name;
    String format;
    Bitmap image;
    CanvasGraphics proxy;

    public BitmapStreamGraphics(Context context, String name, Dimension2D dim,
                                String format, boolean transparency) {
        super();
        this.format = format;
        this.name = name;
        this.context = context;

        image = Bitmap.createBitmap((int) Math.ceil(dim.getWidth()), (int) Math
                .ceil(dim.getHeight()), Bitmap.Config.ARGB_8888);
        if (transparency) image.setHasAlpha(true);
        this.setCanvas(new Canvas(image));
        proxy = this;
        //GraphicsUtils.antialias(proxy);
    }

    public void fillWith(Color color) {
        Color oldColor= proxy.getBackground();
        proxy.setBackground(color);
        proxy.clearRect(0, 0, image.getWidth(), image.getHeight());
        proxy.setBackground(oldColor);
    }

    public void dispose() {
        if (proxy != null) {
            proxy = null;
            final String url = CapturePhotoUtils.insertImage(context.getContentResolver(), image, name, "", format.equals("png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG);
            image = null;
            name = null;

            AndroidUtils.getActivity(context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (url != null)
                        Toast.makeText(context, "Saved to gallery.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Failed to save to gallery. Ensure permissions are enabled.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * jsesh.graphics.wmfexport.BaseGraphics2D#setProperties(java.util.Properties
     * )
     */
    public void setProperties(Properties properties) {

    }
}
