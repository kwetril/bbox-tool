package bbox.ui;

import bbox.model.BoundingBox;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImagePanel extends TransformableCanvas implements IBoundingBoxUpdateListener {
    BufferedImage image;
    BoundingBox activeBox;
    BoundingBoxEditor boundingBoxEditor;
    MouseController mouseController;

    public ImagePanel() {
        super(-6, 4, -2, 1.5);
        activeBox = null;
        addMouseScalingTool();
        MouseAdapter movingTool = getMouseMovingTool();
        mouseController = new MouseController(movingTool);
        boundingBoxEditor = new BoundingBoxEditor(this);
        mouseController.setBoxEditor(boundingBoxEditor);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);
    }

    public void setImage(BufferedImage image, BoundingBox box) {
        transformState.resetTransform();
        this.image = image;
        this.activeBox = box;
        boundingBoxEditor.setBoundingBox(box);
        repaint();
    }

    public void newBoundingBox() {
        boundingBoxEditor.activate();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (image != null) {
            graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        }
        if (activeBox != null) {
            graphics2D.setStroke(new BasicStroke(5));

            graphics2D.setColor(Color.RED);
            Rectangle rect = activeBox.getRectangle();
            graphics2D.drawRect(rect.x, rect.y, rect.width, rect.height);

            graphics2D.setColor(Color.BLUE);
            graphics2D.fillOval(activeBox.getTopLeftPoint().x - 4, activeBox.getTopLeftPoint().y - 4, 8, 8);
            graphics2D.fillOval(activeBox.getTopRightPoint().x - 4, activeBox.getTopRightPoint().y - 4, 8, 8);
            graphics2D.fillOval(activeBox.getBottomLeftPoint().x - 4, activeBox.getBottomLeftPoint().y - 4, 8, 8);
            graphics2D.fillOval(activeBox.getBottomRightPoint().x - 4, activeBox.getBottomRightPoint().y - 4, 8, 8);
        }
    }

    public BoundingBox getActiveBox() {
        return activeBox;
    }

    @Override
    public void boxCreated(BoundingBox box) {
        activeBox = box;
        repaint();
    }

    @Override
    public void boxUpdated(BoundingBox box) {
        activeBox = box;
        repaint();
    }
}
