package bbox.ui;

import bbox.model.BoundingBox;
import bbox.model.BoundingBoxLoader;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BoundingBoxFrame extends JFrame {
    private ImagePanel imagePanel;
    private JLabel statusLabel;
    private JButton newBoxButton;
    private JButton saveBoxButton;
    private File lastOpenedFile;

    public BoundingBoxFrame() {
        setTitle("Bounding Box Tool");
        setMinimumSize(new Dimension(800, 600));
        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);

        imagePanel = new ImagePanel();
        add(imagePanel, BorderLayout.CENTER);


        add(createStatusPanel(), BorderLayout.SOUTH);
        add(createToolPanel(), BorderLayout.EAST);
    }

    private void openImage(File imageFile) {
        try {
            BufferedImage currentImage = ImageIO.read(imageFile);
            BoundingBox bbox = BoundingBoxLoader.loadBoxForImage(imageFile);
            imagePanel.setImage(currentImage, bbox);
            newBoxButton.setEnabled(true);
            saveBoxButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openImageMenuItem = new JMenuItem("Open");
        openImageMenuItem.addActionListener(actionEvent -> {
            JFileChooser imageChooser = new JFileChooser();
            if (lastOpenedFile != null) {
                imageChooser.setCurrentDirectory(lastOpenedFile.getParentFile());
            }
            FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            imageChooser.addChoosableFileFilter(imageFilter);
            imageChooser.setAcceptAllFileFilterUsed(false);
            int result = imageChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                lastOpenedFile = imageChooser.getSelectedFile();
                openImage(lastOpenedFile);
            }
        });
        fileMenu.add(openImageMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JPanel createToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        newBoxButton = new JButton("New box");
        newBoxButton.addActionListener(actionEvent -> {
            imagePanel.newBoundingBox();
        });
        newBoxButton.setEnabled(false);
        toolPanel.add(newBoxButton);
        saveBoxButton = new JButton("Save");
        saveBoxButton.addActionListener(actionEvent -> {
            if (imagePanel.getActiveBox() != null) {
                saveBoundingBox(lastOpenedFile, imagePanel.getActiveBox());
            }
        });
        saveBoxButton.setEnabled(false);
        toolPanel.add(saveBoxButton);
        return toolPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    private void saveBoundingBox(File imageFile, BoundingBox boundingBox) {
        BoundingBoxLoader.storeBoxForImage(imageFile, boundingBox);
    }
}
