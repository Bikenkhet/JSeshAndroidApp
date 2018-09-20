/*
 * Copyright ou © ou Copr. Serge Rosmorduc (2016) 
 * serge.rosmorduc@cnam.fr

 * Ce logiciel est un programme informatique servant à mettre en place 
 * une base de données linguistique pour l'égyptien ancien.

 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence [CeCILL|CeCILL-B|CeCILL-C] telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package jsesh.search;

import java.util.ArrayList;
import java.util.List;
import jsesh.hieroglyphs.CompositeHieroglyphsManager;
import jsesh.mdc.model.Hieroglyph;
import jsesh.mdc.model.MDCPosition;
import jsesh.mdc.model.ModelElementDeepAdapter;
import jsesh.mdc.model.TopItemList;

/**
 * Search for sign strings.
 * <p>
 * Preprocessing step: create an array with couples (Sign occurrence, position).
 *
 * Currently uses a naive search algorithm. I'm not convinced that boyer-moore
 * is needed or relevant here.
 *
 * @author rosmord
 */
public class SignStringSearchQuery implements MdCSearchQuery {

    private final List<String> search;

    public SignStringSearchQuery(List<String> search) {
        this.search = new ArrayList<>();
        for (String rawCode : search) {
            String normalized = CompositeHieroglyphsManager.getInstance()
                    .getCanonicalCode(rawCode);
            this.search.add(normalized);
        }
    }

    /**
     * Perform the search.
     *
     * <p>
     * Basically, we have a tree and we want to perform a sequential search on
     * it. The easiest way to do this is to linearize the entry.
     *
     * @param items
     * @return
     */
    public List<MDCPosition> doSearch(TopItemList items) {
        ArrayList<MDCPosition> result= new ArrayList<>();
        CodeExtractors codeExtractors= new CodeExtractors();
        List<HieroglyphOccurrence> l = codeExtractors.analyzeQuadrant(items); 
        for (int i= 0; i < l.size(); i++) {
            if (match(l, i))
                result.add(new MDCPosition(items, l.get(i).getPosition()));
        }
        return result;
    }

    /**
     * Check if the search string is found at position pos.
     * @param items
     * @param pos
     * @return 
     */
    private boolean match(List<HieroglyphOccurrence> items, int pos) {
        boolean ok= true;
        int i= 0;
        while (pos < items.size() && i < search.size() && ok) {
            if (! items.get(pos).hasCode(search.get(i))) {
                ok= false;
            }
            pos++;
            i++;
        }
        return ok && i == search.size();
    }

    private static class HieroglyphOccurrence {

        private final String code;
        private final int position;

        public HieroglyphOccurrence(String code, int position) {
            this.code = code;
            this.position = position;
        }

        public String getCode() {
            return code;
        }

        public int getPosition() {
            return position;
        }

        private boolean hasCode(String code) {
            return this.code.equals(code);
        }

        @Override
        public String toString() {
            return "(" + code + ", " + position + ')';
        }
        
        
    }

    /**
     * Extract a normalised list of codes+position...
     */
    private class CodeExtractors extends ModelElementDeepAdapter {

        private List<HieroglyphOccurrence> codes;

        private int position = -1;

        public List<HieroglyphOccurrence> analyzeQuadrant(TopItemList list) {
            this.codes = new ArrayList<>();
            for (int i = 0; i < list.getNumberOfChildren(); i++) {
                this.position = i;
                list.getChildAt(i).accept(this);
            }
            return this.codes;
        }

        @Override
        public void visitHieroglyph(Hieroglyph h) {
            String code = CompositeHieroglyphsManager.getInstance()
                    .getCanonicalCode(h.getCode());
            codes.add(new HieroglyphOccurrence(code, position));
        }
    }
}
