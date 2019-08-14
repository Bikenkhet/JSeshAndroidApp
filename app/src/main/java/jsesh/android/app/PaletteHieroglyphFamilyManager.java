package jsesh.android.app;

import android.content.Context;

import java.util.Arrays;

class PaletteHieroglyphFamilyManager {

    final private PaletteHieroglyphFamily[] families;

    private int currentID = 0;

    PaletteHieroglyphFamilyManager(Context context) {

        families = new PaletteHieroglyphFamily[] {
            createFamily(null,  context.getString(R.string.family_latest_signs)),
            createFamily(null,  context.getString(R.string.family_user_palette)),
            createFamily("A",   context.getString(R.string.family_a)),
            createFamily("B",   context.getString(R.string.family_b)),
            createFamily("C",   context.getString(R.string.family_c)),
            createFamily("D",   context.getString(R.string.family_d)),
            createFamily("E",   context.getString(R.string.family_e)),
            createFamily("F",   context.getString(R.string.family_f)),
            createFamily("G",   context.getString(R.string.family_g)),
            createFamily("H",   context.getString(R.string.family_h)),
            createFamily("I",   context.getString(R.string.family_i)),
            createFamily("K",   context.getString(R.string.family_k)),
            createFamily("L",   context.getString(R.string.family_l)),
            createFamily("M",   context.getString(R.string.family_m)),
            createFamily("N",   context.getString(R.string.family_n)),
            createFamily("O",   context.getString(R.string.family_o)),
            createFamily("P",   context.getString(R.string.family_p)),
            createFamily("Q",   context.getString(R.string.family_q)),
            createFamily("R",   context.getString(R.string.family_r)),
            createFamily("S",   context.getString(R.string.family_s)),
            createFamily("T",   context.getString(R.string.family_t)),
            createFamily("U",   context.getString(R.string.family_u)),
            createFamily("V",   context.getString(R.string.family_v)),
            createFamily("W",   context.getString(R.string.family_w)),
            createFamily("X",   context.getString(R.string.family_x)),
            createFamily("Y",   context.getString(R.string.family_y)),
            createFamily("Z",   context.getString(R.string.family_z)),
            createFamily("Aa",   context.getString(R.string.family_aa)),
            createFamily("Ff",   context.getString(R.string.family_ff)),
            createFamily("NU",   context.getString(R.string.family_nu)),
            createFamily("NL",   context.getString(R.string.family_nl)),
            createFamily(null,  context.getString(R.string.family_all)),
            createFamily(null,  context.getString(R.string.family_tall_narrow)),
            createFamily(null,  context.getString(R.string.family_low_broad)),
            createFamily(null,  context.getString(R.string.family_low_narrow)),
        };

    }

    private PaletteHieroglyphFamily createFamily(String familyCode, String familyName) {
        PaletteHieroglyphFamily p = new PaletteHieroglyphFamily(currentID, familyCode, familyName);
        currentID++;
        return p;
    }

    PaletteHieroglyphFamily[] getFamilies() {
        return Arrays.copyOf(families, families.length);
    }

}
