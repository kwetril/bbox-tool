package bbox;

import bbox.ui.BoundingBoxFrame;

import javax.swing.*;

public class BoundingBoxTool {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            BoundingBoxFrame frame = new BoundingBoxFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
