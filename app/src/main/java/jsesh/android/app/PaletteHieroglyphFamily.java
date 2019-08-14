package jsesh.android.app;

public class PaletteHieroglyphFamily {

    public final int id;
    private final String familyCode;
    private final String familyName;

    private final boolean isNormalCode;

    public PaletteHieroglyphFamily(int id, String familyCode, String familyName) {
        this.id = id;
        isNormalCode = familyCode == null;
        this.familyCode = isNormalCode ? "" : familyCode;
        this.familyName = familyName;
    }

    @Override
    public String toString() {
        if (familyCode.equals("")) return familyName;
        else return familyCode + ". " + familyName;
    }

    public String getFamilyCode() {
        return familyCode;
    }

}
