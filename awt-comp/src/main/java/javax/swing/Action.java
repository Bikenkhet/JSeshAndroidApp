package javax.swing;

import java.awt.event.ActionListener;

public interface Action extends ActionListener {

    public static final String SMALL_ICON = "SmallIcon";

    static String NAME = "";

    public static final String ACCELERATOR_KEY = "AcceleratorKey";

    public Object getValue(String key);

}
