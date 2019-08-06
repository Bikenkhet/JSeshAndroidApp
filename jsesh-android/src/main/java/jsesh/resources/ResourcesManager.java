/*
 * Created on 3 oct. 2004 by rosmord
 * This code can be distributed under the Gnu Library Public Licence.
 **/
package jsesh.resources;

import android.content.Context;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

//import javax.swing.ImageIcon;

import org.qenherkhopeshef.utils.PlatformDetection;

/**
 * A container for various resources (like fonts).
 *
 * @author rosmord
 *
 */
public class ResourcesManager {

    static private ResourcesManager instance;

    private Font transliterationFont;

    private Font unicodeFont;
    /**
     * Name of the file (in the package ResourcesManager) which contains the
     * definitions for the various icons. A global convention shall be that
     * inactive icons will have " inactive" in their names.
     */
    public static String ICONDEFINITIONS = "/jseshResources/iconsDefinitions.txt";

//    /**
//     * A map linking symbolic names to icons.
//     */
//    private Map<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();

    private static Context context;

    public static void setContext(Context context) {
        if (ResourcesManager.context == null) {
            System.out.println("Setting context: "+context);
            ResourcesManager.context = context;
        }
    }


    private ResourcesManager() {
        loadFonts();
        loadIcons();
    }

    private void loadIcons() {
//        try {         //FIXME ???
//            BufferedReader r = null;
//            try {
//                String s;
//                r = new BufferedReader(new InputStreamReader(
//                        ResourcesManager.class
//                                .getResourceAsStream(ICONDEFINITIONS)));
//                while ((s = r.readLine()) != null) {
//                    String[] data = s.split(":");
//                    iconMap.put(data[0], new ImageIcon(ResourcesManager.class
//                            .getResource("/jseshResources/" + data[1])));
//                }
//            } finally {
//                if (r != null) {
//                    r.close();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void loadFonts() {
        transliterationFont = loadStreamFont("jseshResources/fonts/MDCTranslitLC.ttf", Font.ITALIC);
        transliterationFont = transliterationFont.deriveFont(12f);

        unicodeFont = loadStreamFont("jseshResources/fonts/EgyptoSerif.ttf", Font.PLAIN);
        unicodeFont = unicodeFont.deriveFont(12f);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Font loadStreamFont(String resourceName, int defaultStyle) {
        InputStream in = null;
        Font result = null;
        try {
            if (false) throw new FontFormatException("NOT IMPLEMENTED");    //ANDROID  Just to keep the exception
            try {
//                in = ResourcesManager.class.getResourceAsStream(resourceName);
//                if (in == null) {
//                    throw new FileNotFoundException("file not found "
//                            + resourceName);
//                }
//                result = Font.createFont(Font.TRUETYPE_FONT, in);
                result = Font.fromAssets(context.getAssets(), resourceName);
                HashMap map = new HashMap();
                //map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
                //map.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
                result = result.deriveFont(map);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (FontFormatException e) {
            System.err.println("problem with font file format " + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("problem with font loading " + e);
            e.printStackTrace();
        }
        if (result == null) {
            result = new Font(null, defaultStyle, 1);
        }
        return result;
    }

    static public ResourcesManager getInstance() {
        if (instance == null) {
            instance = new ResourcesManager();
        }
        return instance;
    }

    /**
     * @return the font for translitteration.
     */
    public Font getTransliterationFont() {
        return transliterationFont;
    }

    public Reader getLigatureData() {
//        Reader in;        //ANDROID - Should the resources be in a jar?
//        in = new InputStreamReader(ResourcesManager.class
//                .getResourceAsStream("/jseshResources/data/ligatures.txt"));
//        return in;
        Reader in = null;
        try {
            in = new InputStreamReader(openAsset("/jseshResources/data/ligatures.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public Reader getDemoData() {
        try {
            Reader in;
            in = new InputStreamReader(ResourcesManager.class
                    .getResourceAsStream("/jseshResources/data/allmdc.gly"),
                    "UTF-8");
            return in;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);// Should not happen.
        }
    }

    /**
     * Returns the directory used for the database and other working files.
     *
     * @return the directory
     */
    public File getUserPrefsDirectory() {
        File result = new File(System.getProperty("user.home"));
        if (PlatformDetection.getPlatform() == PlatformDetection.MACOSX) {
            result = new File(result, "Library");
            result = new File(result, "Preferences");
            result = new File(result, "JSesh");
            //System.err.println("Looking for " + result);
        } else if (PlatformDetection.getPlatform() == PlatformDetection.WINDOWS) {
            result = new File(result, "JSeshData");
        } else { // Unix
            result = new File(result, ".jsesh");
        }
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

//    public ImageIcon getIcon(String symbolicName) {
//        if (iconMap.containsKey(symbolicName)) {
//            return iconMap.get(symbolicName);
//        } else {
//            return iconMap.get("Dummy");
//        }
//    }

    /**
     * Return the unicode font. (this is a testing feature)
     *
     * @return the unicodeFont
     */
    public Font getUnicodeFont() {
        return unicodeFont;
    }



    //ANDROID - Put somewhere else?

    public static FileInputStream createFileInputStream(File file) throws IOException {
        return createFileInputStream(file.getPath());
    }

    public static FileInputStream createFileInputStream(String filename) throws IOException {
        //System.out.println("Creating FileInputStream");
        if (filename.startsWith("/")) filename = filename.substring(1);
        return context.getAssets().openFd(filename).createInputStream();
    }

    public static InputStream openAsset(String filename) throws IOException {
        if (filename.startsWith("/")) filename = filename.substring(1);
        return context.getAssets().open(filename);
    }

    public static InputStream openAssetInPackage(String filename) throws IOException {
        String callerClassName = (new Exception()).getStackTrace()[1].getClassName();
        String packageName = callerClassName.substring(0, callerClassName.lastIndexOf('.'));
        String filePath = packageName.replace('.', '/') + "/" + filename;
        return openAsset(filePath);
    }

}
