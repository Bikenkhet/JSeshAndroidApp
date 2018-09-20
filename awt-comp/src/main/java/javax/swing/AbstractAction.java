package javax.swing;

import java.util.HashMap;

public abstract class AbstractAction implements Action {



    boolean enabled;

    private HashMap<String, Object> values = new HashMap<String, Object>();


    public void putValue(String key, Object value) {
        values.put(key, value);
    }

    public Object getValue(String key) {
        return values.get(key);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public AbstractAction() {
        //this.NAME = "";
    }

    public AbstractAction(String name) {
        //this.NAME = name;
    }

    public AbstractAction(String name, Icon icon) {
        //this.NAME = name;
    }

}
