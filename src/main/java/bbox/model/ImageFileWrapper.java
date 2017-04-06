package bbox.model;

import java.io.File;

/**
 * Created by a.klimovich on 06.04.2017.
 */
public class ImageFileWrapper extends File {
    public ImageFileWrapper(File f) {
        super(f.getAbsolutePath());
    }

    @Override
    public String toString() {
        return getName();
    }
}
