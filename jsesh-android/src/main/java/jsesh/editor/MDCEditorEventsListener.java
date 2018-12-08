package jsesh.editor;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.awt.Point;

public class MDCEditorEventsListener extends GestureDetector.SimpleOnGestureListener implements View.OnFocusChangeListener {

    private JMDCEditor editor;

    boolean dragging = false;

    boolean longPress = false;

    Point longPressLocation;

    MDCEditorEventsListener(JMDCEditor editor) {
        this.editor = editor;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //TODO
    }

    public void onMove(MotionEvent e) {
        if (longPress) {
            if (!dragging) {
                editor.getWorkflow().setMarkToCursor();
            }
            this.editor.moveCursorToMouse(new Point((int) e.getX(), (int) e.getY()));
            dragging = true;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        /*
        if (longPress) {
            if (!dragging) {
                editor.getWorkflow().setMarkToCursor();
            }
            this.editor.moveCursorToMouse(new Point((int) e2.getX(), (int) e2.getY()));
        }
        dragging = true;*/
        return true;

    }

    @Override
    public boolean onDown(MotionEvent e) {
        int action = 0;
        if (dragging) {
            dragging = false;
        }

        if (e.getAction() == MotionEvent.ACTION_MOVE) {

        }
        else if (e.getAction() == MotionEvent.ACTION_UP) {
            //if (longPress) {
            //    longPress = false;
            //    setDisallowParentScroll(false);
            //}
        }

        //FIXME
        /*
        if (e.getButton() == MouseEvent.BUTTON1) {
            action = 1;
            if ((e.isShiftDown()))
                action = 2;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            action = 2;
        }
        */
        action = 1;
/*
        if (action == 1) {
            this.editor.moveCursorToMouse(new Point((int) e.getX(), (int) e.getY()));
            getWorkflow().clearMark();
            editor.requestFocus();
        } else if (action == 2) {
            this.editor.moveCursorToMouse(new Point((int) e.getX(), (int) e.getY()));
            editor.requestFocus();
        }
*/
        editor.showKeyboard();

        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.editor.moveCursorToMouse(new Point((int) e.getX(), (int) e.getY()));
        getWorkflow().clearMark();
        editor.requestFocus();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //editor.getWorkflow().setMark(editor.getWorkflow().getCaret().getInsertPosition());
        //this.editor.moveMarkToMouse(new Point((int) e.getX(), (int) e.getY()));
        //editor.requestFocus();

        editor.showContextMenu();
        longPressLocation = new Point((int) e.getX(), (int) e.getY());

        //longPress = true;
        //setDisallowParentScroll(true);

    }

    private void setDisallowParentScroll(boolean parentScroll) {
        ViewParent parent = editor.getParent();
        if (parent instanceof ScrollView || parent instanceof HorizontalScrollView) {
            parent.requestDisallowInterceptTouchEvent(parentScroll);
            parent = parent.getParent();
            if (parent instanceof ScrollView || parent instanceof HorizontalScrollView) {
                parent.requestDisallowInterceptTouchEvent(parentScroll);
            }
        }
    }

    private JMDCEditorWorkflow getWorkflow() {
        return editor.workflow;
    }

}
