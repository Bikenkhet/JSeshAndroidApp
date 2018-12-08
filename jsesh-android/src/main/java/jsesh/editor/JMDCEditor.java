package jsesh.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.logging.Logger;

import javax.swing.ActionMap;

import jsesh.android.AndroidUtils;
import jsesh.android.R;
import jsesh.android.graphics.CanvasGraphics;
import jsesh.editor.caret.MDCCaret;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.constants.TextDirection;
import jsesh.mdc.constants.TextOrientation;
import jsesh.mdc.model.MDCPosition;
import jsesh.mdc.model.operations.ModelOperation;
import jsesh.mdcDisplayer.draw.ViewDrawer;
import jsesh.mdcDisplayer.layout.MDCEditorKit;
import jsesh.mdcDisplayer.layout.SimpleViewBuilder;
import jsesh.mdcDisplayer.mdcView.MDCView;
import jsesh.mdcDisplayer.preferences.DrawingSpecification;
import jsesh.mdcDisplayer.preferences.PageLayout;
import jsesh.search.MdCSearchQuery;
import jsesh.swing.utils.GraphicsUtils;

/**
 * TODO: document your custom view class.
 */
public class JMDCEditor extends View {

    private GestureDetectorCompat gestureDetector;

    public JMDCEditor(Context context) {
        super(context);
        init(null, 0);
    }

    public JMDCEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public JMDCEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.JMDCEditor, defStyle, 0);

        String mdcText = a.getString(R.styleable.JMDCEditor_mdcText);
        if (mdcText == null) mdcText = "";

        boolean editable = a.getBoolean(R.styleable.JMDCEditor_editable, true);
        setEditable(editable);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(isFocusableInTouchMode());
                setFocusableInTouchMode(true);
                requestFocus();
            }
        });

        //JMDCEditor init

        //Prevent blur on scaling
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        this.setBackgroundColor(Color.WHITE);

        editorInit(mdcText);

        gestureDetector = new GestureDetectorCompat(this.getContext(), eventListener);
        gestureDetector.setIsLongpressEnabled(true);

    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
        AndroidUtils.getActivity(getContext()).getMenuInflater().inflate(R.menu.jmdceditor_menu, menu);

        final JMDCEditor editor = this;
        final int length = menu.size();
        for (int index = 0; index < length; index++) {
            final MenuItem menuItem = menu.getItem(index);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.editor_select) {
                        editor.moveMarkToMouse(eventListener.longPressLocation);
                        editor.requestFocus();
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Fill background
        {
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.WHITE);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
        }
        paintComponent(CanvasGraphics.create(canvas));
    }

    //VIEW side


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Dimension d = getPreferredSize();

        if (widthMode == MeasureSpec.UNSPECIFIED) d.width = Math.max(d.width, widthSize);
        if (heightMode == MeasureSpec.UNSPECIFIED) d.height = Math.max(d.height, heightSize);

        setMeasuredDimension(d.width, d.height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if (event.getAction() == MotionEvent.ACTION_MOVE) eventListener.onMove(event);
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {

            //TODO This currently uses the system keyboard, support for a custom in-app JSesh keyboard will be added

            showKeyboard();


        }


    }

    public void showKeyboard() {
        if (!isEditable()) return;
        InputMethodManager imm =(InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //TODO Option to propagate?
            hideKeyboard();
        }
        return false;
    }


    //WORKING

    //TEMP way
    private KeyListener keyListener;
    public void addKeyListener(KeyListener event) {
        keyListener = event;
    }

    //This should not be here
    private SparseArray<SparseArray<String>> acceleratorMap = getAcceleratorMap();
    private ActionMap actionMap;

    public void setActionMap(ActionMap actionMap) {
        this.actionMap = actionMap;
    }

    private SparseArray<SparseArray<String>> getAcceleratorMap() {
        int NONE = 0;
        int SHIFT = KeyEvent.META_SHIFT_ON;
        int CTRL = KeyEvent.META_CTRL_ON;
        int CTRL_SHIFT = KeyEvent.META_CTRL_ON + KeyEvent.META_SHIFT_ON;

        SparseArray<SparseArray<String>> acc = new SparseArray<SparseArray<String>>();
        SparseArray<String> n = new SparseArray<String>();
        SparseArray<String> s = new SparseArray<String>();
        SparseArray<String> c = new SparseArray<String>();
        SparseArray<String> cs = new SparseArray<String>();
        acc.put(NONE, n);
        acc.put(SHIFT, s);
        acc.put(CTRL, c);
        acc.put(CTRL_SHIFT, cs);

        n.put(KeyEvent.KEYCODE_DPAD_LEFT, ActionsID.GO_LEFT);
        n.put(KeyEvent.KEYCODE_DPAD_RIGHT, ActionsID.GO_RIGHT);
        n.put(KeyEvent.KEYCODE_DPAD_DOWN, ActionsID.GO_DOWN);
        n.put(KeyEvent.KEYCODE_DPAD_UP, ActionsID.GO_UP);
        n.put(KeyEvent.KEYCODE_ENTER, ActionsID.NEW_LINE);

        s.put(KeyEvent.KEYCODE_DPAD_LEFT, ActionsID.EXPAND_SELECTION_LEFT);
        s.put(KeyEvent.KEYCODE_DPAD_RIGHT, ActionsID.EXPAND_SELECTION_RIGHT);
        s.put(KeyEvent.KEYCODE_DPAD_DOWN, ActionsID.EXPAND_SELECTION_DOWN);
        s.put(KeyEvent.KEYCODE_DPAD_UP, ActionsID.EXPAND_SELECTION_UP);


        c.put(KeyEvent.KEYCODE_G, ActionsID.GROUP_VERTICAL);
        c.put(KeyEvent.KEYCODE_H, ActionsID.GROUP_HORIZONTAL);


        return acc;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isEditable()) return false;

        int flags = event.getMetaState();
        int actionMask = KeyEvent.META_CTRL_ON + KeyEvent.META_META_ON + KeyEvent.META_ALT_ON;
        int mask = actionMask + KeyEvent.META_SHIFT_ON;
        int mods = flags & mask;

        SparseArray<String> sa = acceleratorMap.get(mods);
        if (sa != null) {
            String id = sa.get(keyCode);
            if (id != null) {
                ActionListener listener = actionMap.get(id);
                if (listener != null) {
                    listener.actionPerformed(null);
                    return true;
                }
            }
        }

        keyListener.keyTyped(new java.awt.event.KeyEvent(event, this));
        return true;

    }




    //JMDCEditor

    public void repaint() {
        //FIXME ?
        invalidate();
    }

    public void revalidate() {
        //FIXME
        requestLayout();
    }

    public void scrollRectToVisible(Rectangle r) {
        //FIXME
    }



    //SPECIFIC

    /**
     *
     */
    private static final long serialVersionUID = -5312716856062578743L;
    private static final int BOTTOM_MARGIN = 5;
    private JMDCModelEditionListener mdcModelEditionListener;
    /**
     * Strategy to build a view.
     *
     */
    // SimpleViewBuilder builder;
    /**
     * Debugging of view placement.
     */
    private boolean debug = false;
    /**
     * Strategy to draw a view. (we have just decided to build the view builder
     * on demand, and not to keep it. But the drawer contains some information,
     * and in particular a cache of views).
     */
    protected ViewDrawer drawer;
    /**
     * The current view we maintain.
     */
    MDCView documentView;
    /**
     * Display scale for this window.
     */
    private double scale;
    /**
     * Updates the view.
     *
     */
    private MDCViewUpdater viewUpdater;
    /**
     * Deals with events that occur on this object :
     */
    MDCEditorEventsListener eventListener;
    /**
     * Basic Informations about drawing : fonts to use, line width, etc...
     *
     */
    JMDCEditorWorkflow workflow;
    /**
     * The object responsible for building transferable for the clipboard.
     */
    //MDCModelTransferableBroker mdcModelTransferableBroker = new SimpleMDCModelTransferableBroker();   //TEMP
    /**
     * States that the caret has changed since last redraw.
     * <p>
     * The purpose of this variable is to ensure that scrolls to the caret
     * position are only done <em>after</em> view updates.
     */
    private boolean caretChanged = true;
    private boolean editable = true;
    // FIXME : choose a reasonable method to share drawing specifications.
    private DrawingSpecification drawingSpecifications = MDCEditorKit
            .getBasicMDCEditorKit().getDrawingSpecifications();

    private boolean drawLimits = false;

    public void editorInit(String mdcText) {
        //FIXME Temp for demonstration
        HieroglyphicTextModel hieroglyphicTextModel = new HieroglyphicTextModel();
        try {
            hieroglyphicTextModel.setMDCCode(mdcText);
        } catch (MDCSyntaxError mdcSyntaxError) {}
        editorInit(hieroglyphicTextModel);
    }

    public void editorInit(HieroglyphicTextModel data) {
        //setBackground(Color.WHITE);
        drawer = new ViewDrawer();
        //drawer.setCached(true);
        drawer.setCached(false);
        setScale(2.0);
        // simpleDrawingSpecification= new SimpleDrawingSpecifications();
        // The use of an interface for the view builder may have been
        // some kind of over-engineering.
        // builder = new SimpleViewBuilder();
        documentView = null;
        workflow = new JMDCEditorWorkflow(data);

        mdcModelEditionListener = new JMDCModelEditionListener();
        workflow.addMDCModelListener(mdcModelEditionListener);
        // setRequestFocusEnabled(true);
        setFocusable(true);
        viewUpdater = new MDCViewUpdater(this);

        setScale(10.0);

        eventListener = new MDCEditorEventsListener(this);
        new MDCEditorKeyManager(this);
    }

    public void setHieroglyphiTextModel(
            HieroglyphicTextModel hieroglyphicTextModel) {
        workflow.setHieroglyphicTextModel(hieroglyphicTextModel);
        invalidateView();
    }

    public void addCodeChangeListener(MDCModelEditionListener l) {
        workflow.addMDCModelListener(l);
    }

    public void deleteCodeChangeListener(MDCModelEditionListener l) {
        workflow.deleteCodeChangeListener(l);
    }

    /**
     * Returns the code buffer associated with this editor.
     *
     * @return the code buffer associated with this editor.
     */
    public String getCodeBuffer() {
        return workflow.getCurrentCode().toString();
    }

    /**
     * @return the model.
     *
     */
    public HieroglyphicTextModel getHieroglyphicTextModel() {
        return workflow.getHieroglyphicTextModel();
    }

    /**
     * returns the cursor associated with this object, or null if none. The
     * simple displayer has no cursor. Only its editor subclass has.
     *
     * @return the caret.
     */
    protected MDCCaret getMDCCaret() {
        return workflow.getCaret();
    }

    //@Override
    public Dimension getPreferredSize() {
        if (getHieroglyphicTextModel() == null) {
            return new Dimension(600, 600);
        } else {
            MDCView v = getView();
            if (v.getWidth() == 0 || v.getHeight() == 0) {
                v.setWidth(14);
                v.setHeight(14);
            }
            return new Dimension((int) (scale * v.getWidth()), BOTTOM_MARGIN
                    + (int) (scale * v.getHeight()));
        }
    }

    /**
     * @return the scale
     *
     */
    public double getScale() {
        return scale;
    }

    /**
     * Returns a MDCView of the current hieroglyphicTextModel. Build it if
     * necessary.
     *
     * @return the view for the model.
     */
    public MDCView getView() {
        if (documentView == null) {
            documentView = new SimpleViewBuilder().buildView(
                    getHieroglyphicTextModel().getModel(),
                    getDrawingSpecifications());
            revalidate();
            if (debug) {
                System.out.println(documentView);
            }
        }
        return documentView;
    }

    /**
     * Return the workflow, which allows manipulation of the underlaying
     * hieroglyphicTextModel.
     *
     * @return Returns the workflow.
     */
    public JMDCEditorWorkflow getWorkflow() {
        return workflow;
    }

    /**
     * Moves cursor to screen position p.
     *
     * @param p a point in screen position.
     */
    protected void moveCursorToMouse(Point p) {

        Point clickPoint = (Point) p.clone();
        // the display scale is none of the drawer's business (currently, that
        // is.)
        clickPoint.x = (int) (clickPoint.x / getScale());
        clickPoint.y = (int) (clickPoint.y / getScale());
        // drawer.getPositionForPoint(getView(), clickPoint);
        MDCPosition pos = drawer.getPositionForPoint(getView(), clickPoint,
                getDrawingSpecifications());
        if (pos != null) {
            workflow.setCursor(pos);
        }
    }

    /**
     * Moves mouse to screen position p.
     *
     * @param p
     */
    protected void moveMarkToMouse(Point p) {
        Point clickPoint = (Point) p.clone();
        // the display scale is none of the drawer's business (currently, that
        // is.)
        clickPoint.x = (int) (clickPoint.x / getScale());
        clickPoint.y = (int) (clickPoint.y / getScale());
        // drawer.getPositionForPoint(getView(), clickPoint);
        MDCPosition pos = drawer.getPositionForPoint(getView(), clickPoint,
                getDrawingSpecifications());
        workflow.setMark(pos);
    }

    /**
     * Preliminary operation for drawing this component.
     *
     * @param g
     */
    protected void drawBaseComponent(Graphics g) {
//        CanvasGraphics cg = (CanvasGraphics) g;
//        cg.setBackground(java.awt.Color.WHITE);
//        cg.clear();
        /*super.paintComponent(g);
        Insets insets = getInsets();
        g.clipRect(insets.left, insets.top, getWidth() - insets.left
                - insets.right, getHeight() - insets.top - insets.bottom);*/


//        g.clipRect(getPaddingLeft(), getPaddingTop(),
//                getWidth() - getPaddingLeft() - getPaddingRight(),
//                getHeight() - getPaddingTop() - getPaddingBottom());
    }

    //@Override
    protected void paintComponent(Graphics g) {
        drawBaseComponent(g);
//        GraphicsDevice[] devs = GraphicsEnvironment
//                .getLocalGraphicsEnvironment()
//                .getScreenDevices();
        Graphics2D g2d = (Graphics2D) g;
        GraphicsUtils.antialias(g2d);
        g2d.scale(scale, scale);

        // Either there are no page format specification (in which case there is
        // only
        // one infinitie page).
        PageLayout pageLayout = getDrawingSpecifications().getPageLayout();
        if (drawLimits && pageLayout.hasPageFormat()) {
            // IMPROVE THIS...
            g2d.setColor(java.awt.Color.RED);
            g2d.draw(pageLayout.getDrawingRectangle());
        }

        drawer.setClip(true);
        drawer.drawViewAndCursor(g2d, getView(), getMDCCaret(),
                getDrawingSpecifications());
        if (caretChanged) {
            // Disarm caret change updates.
            caretChanged = false;
            // Show the cursor.
            Rectangle r = getPointerRectangle();
            if (!g.getClipBounds().contains(r)) {
                // canDraw= false;
                // Let's get some space around the cursor :
                r.height += 4;
                r.width += 4;
                r.x -= 2;
                r.y -= 2;
                //SwingUtilities.invokeLater(new VisibilityScroller(r));    //TEMP
                requestRectangleOnScreen(new Rect(r.x, r.y, r.x + r.width, r.y + r.height), true);
            }
        }

    }


    public java.util.List<MDCPosition> doSearch(MdCSearchQuery query) {
        return getHieroglyphicTextModel().doSearch(query);
    }

    /*
     * Auxiliary class, used to redraw the window when the cursor is out of the
     * visible frame.
     */
    private class VisibilityScroller implements Runnable {

        Rectangle r;

        public VisibilityScroller(Rectangle r) {
            this.r = r;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        public void run() {
            scrollRectToVisible(r);
            repaint();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#print(java.awt.Graphics)
     */
    public void print(Graphics g) {
        drawer.setClip(false);
        drawer.draw((Graphics2D) g, getView(), getDrawingSpecifications());
    }

    /**
     * @param b
     *
     */
    public void setDebug(boolean b) {
        debug = b;
    }

    /**
     * @param d
     *
     */
    public void setScale(double d) {
        scale = d;
        if (drawer.isCached()) {
            drawer.flushCache();
        }
        getDrawingSpecifications().setGraphicDeviceScale(scale);
        repaint();
        revalidate();
    }


    public void setInsertPosition(int insertPosition) {
        // TODO : this is WAAYYY too convoluted.
        MDCPosition mdcPosition= getHieroglyphicTextModel().buildPosition(insertPosition);
        getWorkflow().setCursor(mdcPosition);
    }

    /**
     * @return the current insertion position.
     */
    public int getInsertPositiont() {
        return workflow.getCaret().getInsert().getIndex();
    }

    /**
     * Returns a rectangle describing the current cursor position.
     *
     * The rectangle coordinates are given in screen coordinates, not model
     * coordinates.
     *
     * @return a rectangle describing the current cursor position.
     */
    Rectangle getPointerRectangle() {
        Rectangle2D r1 = drawer.getRectangleAroundPosition(getView(), workflow
                        .getCaret().getInsert().getPosition(),
                getDrawingSpecifications());
        int w = (int) (r1.getWidth() * getScale());
        int h = (int) (r1.getHeight() * getScale());
        if (w < 2) {
            w = 2;
        }
        if (h < 2) {
            h = 2;
        }
        Rectangle r = new Rectangle((int) (r1.getX() * getScale()),
                (int) (r1.getY() * getScale()), w, h);
        return r;
    }

    /**
     * Chooses between lines and columns for main text. Should change
     * orientation in sub zones when a zone system is created.
     *
     * @param orientation
     */
    public void setTextOrientation(TextOrientation orientation) {
        getDrawingSpecifications().setTextOrientation(orientation);
        invalidateView();
    }

    /**
     * Choose between right-to-left and left-to-right text direction.
     *
     */
    public void setMDCTextDirection(TextDirection direction) {
        getDrawingSpecifications().setTextDirection(direction);
        invalidateView();
    }

    public TextOrientation getTextOrientation() {
        return getDrawingSpecifications().getTextOrientation();
    }

    /**
     * Choose between right-to-left and left-to-right text direction.
     *
     */
    public TextDirection getMDCTextDirection() {
        return getDrawingSpecifications().getTextDirection();
    }

    /**
     * Returns a copy of the specifications attached to the current window.
     */
    public DrawingSpecification getDrawingSpecifications() {
        DrawingSpecification result = drawingSpecifications;
        return result;
    }

    /**
     * @param drawingSpecifications The drawingSpecifications to set.
     */
    public void setDrawingSpecifications(
            DrawingSpecification drawingSpecifications) {
        this.drawingSpecifications = drawingSpecifications;
        drawingSpecifications.setGraphicDeviceScale(scale);
        // TODO : remove me after...
        PageLayout p = drawingSpecifications.getPageLayout();
        p.setPageFormat(new PageFormat());
        drawingSpecifications.setPageLayout(p);

        invalidateView();
    }

    public void invalidateView() {
        documentView = null;
        if (drawer.isCached()) {
            drawer.flushCache();
        }
        revalidate();
        //repaint(); //     ???
    }

    public char getCurrentSeparator() {
        return getWorkflow().getCurrentSeparator();
    }

    private class JMDCModelEditionListener implements MDCModelEditionListener {

        private static final String CLASS_FULL_NAME = "jsesh.editor.JMDCEditor";

        /*
         * (non-Javadoc)
         *
         * @see jsesh.editor.MDCModelEditionListener#textEdited
         * (jsesh.mdc.model.operations.ModelOperation)
         */
        public void textEdited(ModelOperation op) {
            op.accept(viewUpdater);
            Logger.getLogger(CLASS_FULL_NAME).fine("Text edited");
            caretChanged = true;
            // FIXME : only call revalidate if the dimensions have changed.
            revalidate();
            repaint();
        }

        /*
         * (non-Javadoc)
         *
         * @see jsesh.editor.MDCModelEditionListener#textChanged()
         */
        public void textChanged() {
            Logger.getLogger(CLASS_FULL_NAME).fine("Text changed");
            documentView = null;
            // repaint();
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * jsesh.mdcDisplayer.draw.MDCCaretChangeListener#caretChanged(jsesh
         * .mdcDisplayer.draw.MDCCaret)
         */
        public void caretChanged(MDCCaret caret) {
            Logger.getLogger(CLASS_FULL_NAME).fine("Caret changed");
            caretChanged = true;
            repaint();
        }

        /*
         * (non-Javadoc)
         *
         * @see jsesh.editor.MDCModelEditionListener#codeChanged
         * (java.lang.StringBuffer)
         */
        public void codeChanged(StringBuffer code) {
            // NO-OP.
        }

        public void separatorChanged() {
            // NO-OP
        }

        /*
         * (non-Javadoc)
         *
         * @see jsesh.editor.MDCModelEditionListener#focusGained
         * (java.lang.StringBuffer)
         */
        public void focusGained(StringBuffer code) {
            // NO-OP.
        }

        /*
         * (non-Javadoc)
         *
         * @see jsesh.editor.MDCModelEditionListener#focusLost(
         */
        public void focusLost() {
            // NO-OP.
        }
    }

    /**
     * Paste the content of the clipboard into this editor, at the insert
     * position.
     *
     * <p>
     * Currently supports cut and paste from other JSesh editors, and plain text
     * from other editors. It might be interesting to propose some kind of
     * switch to paste plain text as Manuel de Codage code later.
     *
     * <p>
     * Other project : we should also support html in a basic way.
     */
    public void paste() {

//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//        try {
//            Transferable t = clipboard.getContents(this);
//            if (t != null) {
//                if (t.isDataFlavorSupported(JSeshPasteFlavors.ListOfTopItemsFlavor)) {
//                    ListOfTopItems l = (ListOfTopItems) t
//                            .getTransferData(JSeshPasteFlavors.ListOfTopItemsFlavor);
//                    workflow.insertElements(l);
//                } else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                    String string = (String) t
//                            .getTransferData(DataFlavor.stringFlavor);
//                    workflow.insertElement(new AlphabeticText('l', string));
//                }
//            }
//        } catch (IllegalStateException exception) {
//            exception.printStackTrace();
//        } catch (UnsupportedFlavorException exception) {
//            exception.printStackTrace();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }


    }

    /**
     * Copy the selected area in this editor into the clipboard.
     */
    public void copy() {

//        TopItemList top = getWorkflow().getSelectionAsTopItemList();
//        MDCModelTransferable transferable = mdcModelTransferableBroker
//                .buildTransferable(top);
//        Toolkit.getDefaultToolkit().getSystemClipboard()
//                .setContents(transferable, null);
    }

//    public void copy(DataFlavor[] dataFlavors) {
//        TopItemList top = getWorkflow().getSelectionAsTopItemList();
//        MDCModelTransferable transferable = mdcModelTransferableBroker
//                .buildTransferable(top, dataFlavors);
//        Toolkit.getDefaultToolkit().getSystemClipboard()
//                .setContents(transferable, null);
//
//    }

    /**
     * Cut the selected area.
     */
    public void cut() {
        copy();
        getWorkflow().removeSelectedText();
    }

//    /**
//     * Sets a class which will be used to build model transferable for cut and
//     * paste.
//     *
//     * @param mdcModelTransferableBroker The mdcModelTransferableBroker to set.
//     */
//    public void setMdcModelTransferableBroker(
//            MDCModelTransferableBroker mdcModelTransferableBroker) {
//        this.mdcModelTransferableBroker = mdcModelTransferableBroker;
//    }

    public void clearText() {
        setMDCText("");
    }

    /**
     * Sets the content of this element, giving it a text in manuel de codage
     * format.
     *
     * @param mdcText
     * @throws RuntimeException encapsulating a MDCSyntaxError
     */
    public void setMDCText(String mdcText) {
        try {
            getWorkflow().setMDCCode(mdcText);
            invalidateView();
        } catch (MDCSyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    public String getMDCText() {
        return getWorkflow().getMDCCode();
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Does the component keep a cache of rendered groups, sacrifying memory for
     * speed ?
     *
     * @return
     */
    public boolean isCached() {
        return drawer.isCached();
    }

    /**
     * Chose if the component will sacrifice memory for speed.
     *
     * @param c
     */
    public void setCached(boolean c) {
        drawer.setCached(c);
    }

    public void showShadingPopup() {
//        ShadingMenuBuilder menuBuilder = new ShadingMenuBuilder() {
//            protected Action buildAction(int shadingCode, String mdcLabel) {
//                return new EditorShadeAction(JMDCEditor.this, shadingCode,
//                        mdcLabel);
//            }
//        };
//        JPopupMenu shadingPopup = menuBuilder.buildPopup();
//
//        // Create specific actions for this popup ?????
//        // (the labels for these one is too long).
//        // P.S. Only shade zone seems necessary. Unshade is not as useful.
//        shadingPopup.add(getActionMap().get(ActionsID.SHADE_ZONE));
//        shadingPopup.add(getActionMap().get(ActionsID.UNSHADE_ZONE));
//
//        Rectangle r = getPointerRectangle();
//
//        shadingPopup.show(this, (int) r.getCenterX(), (int) r.getCenterY());

    }

    /**
     * Has the current document some unsaved changes ?
     *
     * @return
     */
    public boolean mustSave() {
        return workflow.mustSave();
    }

    public boolean hasSelection() {
        return workflow.getCaret().hasSelection();
    }

    /**
     * Insert one sign with a given code.
     *
     * @param code
     */
    public void insert(String code) {
        workflow.addSign(code);
    }

    /**
     * Should <em>all</em> small signs be vertically centered
     *
     * @param center
     */
    public void setSmallSignsCentered(boolean center) {
        getDrawingSpecifications().setSmallSignsCentered(center);
    }

    /**
     * are <em>all</em> small signs be vertically centered
     *
     * @return true if it is the case.
     */
    public boolean isSmallSignsCentered() {
        return getDrawingSpecifications().isSmallSignsCentered();
    }

    /**
     * Temporary method for signs justification.
     *
     * @return true if lines are justified.
     */
    public boolean isJustified() {
        return getDrawingSpecifications().isJustified();
    }









}
