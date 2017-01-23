package bbox.model;

import java.awt.*;
import java.awt.geom.Point2D;

public class BoundingBox {
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;

    public BoundingBox(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public Point getTopLeftPoint() {
        return new Point(x, y);
    }

    public Point getTopRightPoint() {
        return new Point(x + width, y);
    }

    public Point getBottomLeftPoint() {
        return new Point(x, y + height);
    }

    public Point getBottomRightPoint() {
        return new Point(x + width, y + height);
    }

    public static BoundingBox fromCorners(String name, Point pt1, Point pt2) {
        int minx = Math.min(pt1.x, pt2.x);
        int miny = Math.min(pt1.y, pt2.y);
        int maxx = Math.max(pt1.x, pt2.x);
        int maxy = Math.max(pt1.y, pt2.y);
        return new BoundingBox(name, minx, miny, maxx - minx, maxy - miny);
    }

    public BoundingBox updateFromCorners(Point pt1, Point pt2) {
        x = Math.min(pt1.x, pt2.x);
        y = Math.min(pt1.y, pt2.y);
        width = Math.max(pt1.x, pt2.x) - x;
        height = Math.max(pt1.y, pt2.y) - y;
        return this;
    }
}
