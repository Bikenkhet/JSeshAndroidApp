package jsesh.mdcDisplayer.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Shader {
	
	/**
	 * Shade an area. Utility method.
	 * To function correctly, this method needs the area to be expressed in page coordinates.
	 * That is, all areas should be drawn using the same reference system.
	 * If it isn't the case, they won't align correctly.
	 * TODO : to avoid too many copies of the area, we might decide that the area will be modified
	 * by the method.
	 * @param area
	 */
	public void shadeArea(Graphics2D g, Area area) {
		// Easy way: paint the area in grey...
		// Complex way : intersect the area with line hatching...

		// Prevent us from messing with the area (not that important currently,
		// but who knows ?)

		area = (Area) area.clone();
		// Let's work in a protected graphic environment.
		Graphics2D tempG = (Graphics2D) g.create();

		// Now we can work in page coordinate for everything...
		// Let's take a rectangle large enough
		Rectangle2D r = area.getBounds2D();
		//System.out.println("SHADE: "+r.getX()+" "+r.getY()+" "+r.getWidth()+" "+r.getHeight());
		// Now, we want to draw lines in this rectangle
		// Those lines will be added to an area
		Area shadingLines = new Area();

		// the minimal possible starting point for a shading line which
		// intersects our area.
		double startx = r.getMinX() - r.getHeight();

		double spacing = 3; // Spacing between lines (move it to
		// drawingSpecification)

		double lineWidth = 0.5; // Normal line width (idem).

		BasicStroke stroke = new BasicStroke((float) lineWidth);

		// Actual start...
		double n = Math.ceil((startx + r.getMaxY()) / spacing);

		// This is the abscissa of the first line we will draw

		double x = n * spacing - r.getMaxY();

		// Add all relevant line segments to the area.
		while (x < r.getMaxX()) {
			//ANDROID
			//Patch to fix lack of good BasicStroke.createStrokedShape(Shape) equivalent
			//Uses rotated rectangles instead of lines
			double length = r.getHeight() + Math.abs(r.getMinX() - r.getMaxX());	//Taxicab distance, extra length shouldn't matter
			//20/40 to fix gap between glyphs, arbitrary size, hopefully not scale dependent
			Rectangle2D rect = new Rectangle.Double(-20, -0.25, length+40, 0.5d);
			shadingLines.transform(AffineTransform.getTranslateInstance(-x, -r.getMaxY()));
			shadingLines.transform(AffineTransform.getRotateInstance(Math.PI/4));
			shadingLines.add(new Area(rect));
			shadingLines.transform(AffineTransform.getRotateInstance(-Math.PI/4));
			shadingLines.transform(AffineTransform.getTranslateInstance(x, r.getMaxY()));
			//End of ANDROID

			x += spacing;
		}
		// The area is ready. Intersect it with the original area
		area.intersect(shadingLines);
		// Draw...
		tempG.setColor(Color.BLACK);
		tempG.fill(area);

		tempG.dispose();
	}
}
