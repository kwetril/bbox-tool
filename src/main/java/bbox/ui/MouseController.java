package bbox.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends MouseAdapter {
   BoundingBoxEditor boxEditor;
   MouseAdapter movingTool;

    public MouseController(MouseAdapter movingTool) {
        this.movingTool = movingTool;
    }

    public void setBoxEditor(BoundingBoxEditor boxEditor) {
        this.boxEditor = boxEditor;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (boxEditor != null) {
            boxEditor.mouseMoved(mouseEvent);
        }
        if (!mouseEvent.isConsumed()) {
            movingTool.mouseMoved(mouseEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (boxEditor != null) {
            boxEditor.mousePressed(mouseEvent);
        }
        if (!mouseEvent.isConsumed()) {
            movingTool.mousePressed(mouseEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (boxEditor != null) {
            boxEditor.mouseReleased(mouseEvent);
        }
        if (!mouseEvent.isConsumed()) {
            movingTool.mouseReleased(mouseEvent);
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (boxEditor != null) {
            boxEditor.mouseDragged(mouseEvent);
        }
        if (!mouseEvent.isConsumed()) {
            movingTool.mouseDragged(mouseEvent);
        }
    }
}
