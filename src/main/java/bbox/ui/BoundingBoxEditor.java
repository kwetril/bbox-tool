package bbox.ui;

import bbox.model.BoundingBox;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class BoundingBoxEditor extends MouseAdapter {
    ImagePanel imagePanel;
    BoundingBox bbox;
    private Point startPoint;
    private Point endPoint;
    private boolean isActive;

    public BoundingBoxEditor(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        isActive = false;
    }

    public void setBoundingBox(BoundingBox bbox) {
        this.bbox = bbox;
    }

    public void activate() {
        isActive = true;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point2D mousePoint = imagePanel.convertWindowPoint(mouseEvent.getPoint());
        if (isActive) {
            bbox = null;
            startPoint = null;
            endPoint = null;
            startPoint = new Point((int)mousePoint.getX(), (int)mousePoint.getY());
            endPoint = new Point(startPoint);
            bbox = BoundingBox.fromCorners("", startPoint, endPoint);
            imagePanel.boxCreated(bbox);
            mouseEvent.consume();
        } else if (bbox != null) {
            double minDistance = 10000;
            Point newStartPoint = null;
            Point newEndPoint = null;

            Point pt = bbox.getBottomLeftPoint();
            double distance = mousePoint.distance(pt);
            if (distance < minDistance) {
                minDistance = distance;
                newEndPoint = pt;
                newStartPoint = bbox.getTopRightPoint();
            }

            pt = bbox.getBottomRightPoint();
            distance = mousePoint.distance(pt);
            if (distance < minDistance) {
                minDistance = distance;
                newEndPoint = pt;
                newStartPoint = bbox.getTopLeftPoint();
            }

            pt = bbox.getTopRightPoint();
            distance = mousePoint.distance(pt);
            if (distance < minDistance) {
                minDistance = distance;
                newEndPoint = pt;
                newStartPoint = bbox.getBottomLeftPoint();
            }

            pt = bbox.getTopLeftPoint();
            distance = mousePoint.distance(pt);
            if (distance < minDistance) {
                minDistance = distance;
                newEndPoint = pt;
                newStartPoint = bbox.getBottomRightPoint();
            }

            if (minDistance < 10) {
                isActive = true;
                startPoint = newStartPoint;
                endPoint = newEndPoint;
                mouseEvent.consume();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (isActive) {
            isActive = false;
            mouseEvent.consume();
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (isActive) {
            Point2D point = imagePanel.convertWindowPoint(mouseEvent.getPoint());
            endPoint = new Point((int)point.getX(), (int)point.getY());
            bbox.updateFromCorners(startPoint, endPoint);
            imagePanel.boxUpdated(bbox);
            mouseEvent.consume();
        }
    }
}
