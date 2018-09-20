package javax.swing;

import java.util.HashMap;

public class ActionMap {

    private HashMap<String, Action> map = new HashMap<>();

    public ActionMap() {}

    public void put(String key, Action value) {
        map.put(key, value);
    }

    public Action get(String key) {
        return map.get(key);
    }

}
