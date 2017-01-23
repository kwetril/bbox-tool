package bbox.ui;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TransformState {
    private int minScalePower;
    private int maxScalePower;
    private int scalePower;
    private int initScalePower;
    private double scaleFactor;
    AffineTransform transform;

    public TransformState(int minScalePower, int maxScalePower, int initScalePower, double scaleFactor) {
        this(minScalePower, maxScalePower, initScalePower, scaleFactor, 0, 0);
    }

    public TransformState(int minScalePower, int maxScalePower, int initScalePower, double scaleFactor,
                          double translateX, double translateY) {
        this.minScalePower = minScalePower;
        this.maxScalePower = maxScalePower;
        this.scalePower = initScalePower;
        this.initScalePower = initScalePower;
        this.scaleFactor = scaleFactor;
        double scale = getScale();
        transform = new AffineTransform();
        transform.scale(scale, scale);
        transform.translate(translateX, translateY);
    }

    public double getScale() {
        return Math.pow(scaleFactor, scalePower);
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void resetTransform() {
        scalePower = initScalePower;
        double scale = getScale();
        transform = new AffineTransform();
        transform.scale(scale, scale);
    }

    public int getScalePower() {
        return scalePower;
    }

    public int getScaleIndex() {
        return scalePower - minScalePower;
    }

    public double getTranslateX() {
        return transform.getTranslateX();
    }

    public double getTranslateY() {
        return transform.getTranslateY();
    }

    public void setScalePower(int scalePower) {
        updateScalePower(scalePower - this.scalePower);
    }

    public void updateScalePower(int value) {
        updateScalePower(value, 0, 0);
    }

    public void updateScalePower(int value, double x, double y) {
        int newScalePower = Math.min(Math.max(scalePower + value, minScalePower), maxScalePower);
        int deltaScalePower = newScalePower - scalePower;
        if (deltaScalePower == 0) {
            return;
        }
        double deltaScale = Math.pow(scaleFactor, deltaScalePower);
        Point2D clickPoint = new Point2D.Double(x, y);
        Point2D oldMapPoint = new Point2D.Double();
        Point2D newMapPoint = new Point2D.Double();
        try {
            transform.inverseTransform(clickPoint, oldMapPoint);
            transform.scale(deltaScale, deltaScale);
            scalePower = newScalePower;
            transform.inverseTransform(clickPoint, newMapPoint);
            transform.translate(newMapPoint.getX() - oldMapPoint.getX(),
                    newMapPoint.getY() - oldMapPoint.getY());
        }
        catch (Exception ex) {
            System.out.printf("Err\n");
        }
    }

    public void updateTranslation(double dx, double dy) {
        double scale = getScale();
        dx /= scale;
        dy /= scale;
        transform.translate(dx, dy);
    }

    public void setTranslation(double dx, double dy) {
        dx -= transform.getTranslateX();
        dy -= transform.getTranslateY();
        double scale = getScale();
        dx /= scale;
        dy /= scale;
        transform.translate(dx, dy);
    }

    public AffineTransform getTransform() {
        return transform;
    }
}

