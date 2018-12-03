package jsesh.hieroglyphs;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.prefs.Preferences;

/**
 * Place holder for the default hieroglyphic font manager. The default system
 * currently uses both internal hieroglyphic fonts and a directory based system.
 * 
 * <p>
 * Could perhaps be observable.
 * 
 * @author rosmord
 * 
 */
public class DefaultHieroglyphicFontManager implements HieroglyphicFontManager {
	private static final String GLYPH_DIRECTORY = "glyphDirectory";

	static private DefaultHieroglyphicFontManager instance = null;

	private CompositeHieroglyphicFontManager composite;

	private DirectoryHieroglyphicFontManager directoryManager;

	private ResourcesHieroglyphicFontManager resourcesHieroglyphicFontManager;

	private DefaultHieroglyphicFontManager() {
		composite = new CompositeHieroglyphicFontManager();
		directoryManager = new DirectoryHieroglyphicFontManager(new File(""));
		composite.addHieroglyphicFontManager(directoryManager);
		resourcesHieroglyphicFontManager = new ResourcesHieroglyphicFontManager(
				"/jseshGlyphs");
		composite.addHieroglyphicFontManager(resourcesHieroglyphicFontManager);
		composite
				.addHieroglyphicFontManager(new MemoryHieroglyphicFontManager());
		initDirectory();
	}

	public static synchronized DefaultHieroglyphicFontManager getInstance() {
		if (instance == null)
			instance = new DefaultHieroglyphicFontManager();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jsesh.hieroglyphs.CompositeHieroglyphicFontManager#addHieroglyphicFontManager
	 * (jsesh.hieroglyphs.HieroglyphicFontManager)
	 */
	public void addHieroglyphicFontManager(HieroglyphicFontManager manager) {
		composite.addHieroglyphicFontManager(manager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jsesh.hieroglyphs.CompositeHieroglyphicFontManager#get(java.lang.String)
	 */
	public ShapeChar get(String code) {
		String newCode = code;
		// TODO Awfull patch for now. This should move to another class. The
		// font manager should
		// associate glyphs codes to drawings ;
		// a code manager should associate mdc codes to glyphs codes.
		if (!GardinerCode.isCanonicalCode(code))
			newCode = CompositeHieroglyphsManager.getInstance()
					.getCanonicalCode(code);
		return composite.get(newCode);
	}

	public ShapeChar getSmallBody(String code) {
		String newCode = code;
		// TODO Awfull patch for now. This should move to another class. The
		// font manager should
		// associate glyphs codes to drawings ;
		// a code manager should associate mdc codes to glyphs codes.
		if (!GardinerCode.isCanonicalCode(code))
			newCode = CompositeHieroglyphsManager.getInstance()
					.getCanonicalCode(code);
		return composite.getSmallBody(newCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsesh.hieroglyphs.CompositeHieroglyphicFontManager#getCodes()
	 */
	public Set<String> getCodes() {
		return composite.getCodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsesh.hieroglyphs.CompositeHieroglyphicFontManager#hasNewSigns()
	 */
	public boolean hasNewSigns() {
		return composite.hasNewSigns();
	}

    public SortedSet<String> getSimilarCodes(String code) {
	    return composite.getSimilarCodes(code);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsesh.hieroglyphs.DirectoryHieroglyphicFontManager#getDirectory()
	 */
	public File getDirectory() {
		return directoryManager.getDirectory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jsesh.hieroglyphs.DirectoryHieroglyphicFontManager#setDirectory(java.
	 * io.File)
	 */
	public void setDirectory(File directory) {
		Preferences preferences = Preferences.userNodeForPackage(this
				.getClass());
		directoryManager.setDirectory(directory);
		if (directory != null) {
			String path = directory.getAbsolutePath();
			try {
				path = directory.getCanonicalPath();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			preferences.put(GLYPH_DIRECTORY, path);
		} else {
			preferences.remove(GLYPH_DIRECTORY);
		}
	}

	private void initDirectory() {
		try {
			Preferences preferences = Preferences.userNodeForPackage(this
					.getClass());
			String dirPath = preferences.get(GLYPH_DIRECTORY, "");
			directoryManager.setDirectory(new File(dirPath));
		} catch (javax.xml.parsers.FactoryConfigurationError e) {
			e.printStackTrace();
		}
	}

	public void insertNewSign(String text, ShapeChar shapeChar) {
		directoryManager.insertNewSign(text, shapeChar);
	}
}
