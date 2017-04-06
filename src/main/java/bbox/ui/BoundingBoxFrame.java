package bbox.ui;

import bbox.model.BoundingBox;
import bbox.model.BoundingBoxLoader;
import bbox.model.ImageFileWrapper;
import bbox.ui.utils.LayoutHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoundingBoxFrame extends JFrame {
    private ImagePanel imagePanel;
    private JLabel statusLabel;
    private JButton newBoxButton;
    private JButton saveBoxButton;
    private JList imagesList;
    private DefaultListModel listModel;
    private File lastOpenedFile;
    private File[] openedFiles;

    public BoundingBoxFrame() {
        setTitle("Bounding Box Tool");
        setIconImage(getLogoImage());
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
            imageChooser.setMultiSelectionEnabled(true);
            int result = imageChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                openedFiles = imageChooser.getSelectedFiles();
                lastOpenedFile = openedFiles[0];
                listModel.clear();
                for (File openedFile : openedFiles) {
                    listModel.addElement(new ImageFileWrapper(openedFile));
                }
                imagesList.setSelectedIndex(0);
            }
        });
        fileMenu.add(openImageMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JPanel createToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
        LayoutHelper.setWidth(toolPanel, 250);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        newBoxButton = new JButton("New box");
        newBoxButton.addActionListener(actionEvent -> imagePanel.newBoundingBox());
        newBoxButton.setEnabled(false);
        newBoxButton.setAlignmentX(CENTER_ALIGNMENT);
        LayoutHelper.setWidth(newBoxButton, 230);
        box.add(newBoxButton);

        box.add(Box.createRigidArea(new Dimension(0, 10)));

        saveBoxButton = new JButton("Save");
        saveBoxButton.addActionListener(actionEvent -> {
            if (imagePanel.getActiveBox() != null) {
                saveBoundingBox(lastOpenedFile, imagePanel.getActiveBox());
            }
        });
        saveBoxButton.setEnabled(false);
        saveBoxButton.setAlignmentX(CENTER_ALIGNMENT);
        LayoutHelper.setWidth(saveBoxButton, 230);
        box.add(saveBoxButton);

        box.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel imagesListLabel = new JLabel("List of images:");
        imagesListLabel.setAlignmentX(CENTER_ALIGNMENT);
        LayoutHelper.setWidth(imagesListLabel, 230);
        box.add(imagesListLabel);

        box.add(Box.createRigidArea(new Dimension(0, 5)));

        listModel = new DefaultListModel();
        imagesList = new JList(listModel);
        imagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        imagesList.setVisibleRowCount(5);
        imagesList.setLayoutOrientation(JList.VERTICAL);
        imagesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                JList list = (JList) e.getSource();
                File selectedFile = (File) list.getSelectedValue();
                if (selectedFile != null) {
                    lastOpenedFile = selectedFile;
                    openImage(lastOpenedFile);
                }
            }
        });
        JScrollPane listScroller = new JScrollPane(imagesList);
        LayoutHelper.setWidth(listScroller, 230);
        box.add(listScroller);

        toolPanel.add(box, BorderLayout.CENTER);
        return toolPanel;
    }

    private Image getLogoImage() {
        ImageIcon img = new ImageIcon(getClass().getResource("/logo.png"));
        return img.getImage();
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
