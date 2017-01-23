package bbox.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class TransformableCanvas extends JPanel {
    protected TransformState transformState;

    public TransformableCanvas(int minScalePower, int maxScalePower, int initScalePower, double scaleFactor) {
        this.transformState = new TransformState(minScalePower, maxScalePower, initScalePower, scaleFactor);
    }

    public void addMouseScalingTool() {
        addMouseWheelListener(mouseWheelEvent -> {
            transformState.updateScalePower(-mouseWheelEvent.getWheelRotation(),
                    mouseWheelEvent.getX(), mouseWheelEvent.getY());
            repaint();
        });
    }

    public MouseAdapter getMouseMovingTool() {
        MouseAdapter adapter = new MouseAdapter() {
            private int x;
            private int y;

            @Override
            public void mouseMoved(MouseEvent e) {
                Point2D source = new Point2D.Float(e.getX(), e.getY());
                Point2D destination = convertWindowPoint(source);
                System.out.printf("Mouse x: %s; y: %s; Img: (%s, %s)\n",
                        e.getX(), e.getY(), destination.getX(), destination.getY());
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                x = mouseEvent.getX();
                y = mouseEvent.getY();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                int dx = x - mouseEvent.getX();
                int dy = y - mouseEvent.getY();
                transformState.updateTranslation(dx, dy);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int dx = mouseEvent.getX() - x;
                int dy = mouseEvent.getY() - y;
                x = mouseEvent.getX();
                y = mouseEvent.getY();
                transformState.updateTranslation(dx, dy);
                repaint();
            }
        };
        return adapter;
    }

    public Point2D convertWindowPoint(Point2D point) {
        Point2D result = new Point2D.Float();
        try {
            transformState.transform.inverseTransform(point, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.transform(transformState.getTransform());
    }
}

