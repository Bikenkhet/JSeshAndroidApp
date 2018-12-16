package jsesh.android.graphics;

import android.graphics.Matrix;
import android.graphics.Path;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class GraphicsUtils {

    public static Path getPathFromIterator(PathIterator iterator) {

        Path path = new Path();

        float[] coords = new float[6];
        int type;
        while (!iterator.isDone()) {

            type = iterator.currentSegment(coords);

            switch (type) {
                case PathIterator.SEG_MOVETO:
                    path.moveTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    path.lineTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    path.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    path.cubicTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    path.close();
                    break;
                default:
                    break;
            }

            iterator.next();

        }

        return path;

    }

    public static Matrix getMatrixFromAffineTransform(AffineTransform Tx) {

        double[] affineArray = new double[6];
        Tx.getMatrix(affineArray);
        Matrix matrix = new Matrix();

        matrix.setValues(new float[]{
                (float)affineArray[0],
                (float)affineArray[2],
                (float)affineArray[4],
                (float)affineArray[1],
                (float)affineArray[3],
                (float)affineArray[5],
                0.0f,
                0.0f,
                1.0f
        });

        return matrix;

    }

}
