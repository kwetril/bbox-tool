package bbox.model;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;


public class BoundingBoxLoader {
    public static void storeBoxForImage(File imageFile, BoundingBox boundingBox) {
        File bboxFile = boundingBoxFileFromImageFile(imageFile);
        try(PrintWriter out = new PrintWriter(bboxFile)  ){
            out.println(boxToString(boundingBox));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static BoundingBox loadBoxForImage(File imageFile) {
        File bboxFile = boundingBoxFileFromImageFile(imageFile);
        if (bboxFile.exists()) {
            try {
                return boxFromString(Files.lines(bboxFile.toPath()).findFirst().get());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static File boundingBoxFileFromImageFile(File imageFile) {
        File folder = imageFile.getParentFile();
        String imageName = imageFile.getName();
        String boxName = "bbox-" + imageName.split("\\.")[0] + ".txt";
        File bboxFile = new File(folder, boxName);
        return bboxFile;
    }

    private static String boxToString(BoundingBox bbox) {
        Rectangle rect = bbox.getRectangle();
        return String.format("%s %s %s %s", rect.x, rect.y, rect.width, rect.height);
    }

    private static BoundingBox boxFromString(String str) {
        String[] values = str.split(" ");
        return new BoundingBox("", Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                Integer.parseInt(values[2]), Integer.parseInt(values[3]));
    }
}
