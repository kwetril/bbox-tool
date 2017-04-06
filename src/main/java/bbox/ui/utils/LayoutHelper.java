package bbox.ui.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by a.klimovich on 03.04.2017.
 */
public class LayoutHelper {
    public static void setWidth(JComponent component, int width) {
        Dimension size;
        size = component.getPreferredSize();
        size.width = width;
        component.setPreferredSize(size);
        size = component.getMinimumSize();
        size.width = width;
        component.setMinimumSize(size);
        size = component.getMaximumSize();
        size.width = width;
        component.setMaximumSize(size);
    }
}
