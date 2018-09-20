package java.awt.event;

import android.view.View;

public class KeyEvent {

    private android.view.KeyEvent keyEvent;
    private Object source;

    public KeyEvent(android.view.KeyEvent keyEvent, Object source) {
        this.keyEvent = keyEvent;
        this.source = source;
    }

    public boolean isActionKey() {
        return false;
    }

    public boolean isControlDown() {
        return keyEvent.isCtrlPressed();
    }

    public boolean isMetaDown() {
        return keyEvent.isMetaPressed();
    }

    public boolean isAltDown() {
        return keyEvent.isAltPressed();
    }

    public char getKeyChar() {
        int k = keyEvent.getKeyCode();
        if (k == keyEvent.KEYCODE_DEL) return 8;
        return (char) keyEvent.getUnicodeChar();
    }

    public Object getSource() {

        return source;
    }

}
