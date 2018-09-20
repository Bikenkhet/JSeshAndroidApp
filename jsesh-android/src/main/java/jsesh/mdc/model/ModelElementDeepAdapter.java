/*
 * Created on 1 nov. 2004
 *
 * This file is distributed under the LGPL.
 */
package jsesh.mdc.model;

/**
 * A convenience base class form ModelElementVisitors.
 * 
 * <p>Visit methods for non abstract classes call the methods for
 * their abstract parents, which in turn call the methods for their parents,
 * which call visitDefault.
 * <p> visitDefault call <code>accept</code> on
 * all children.</p>
 * 
 * <p> This architecture allows you to customize your visitor at the precision you want.
 * 
 * @author S. Rosmorduc
 *
 */
public class ModelElementDeepAdapter implements ModelElementVisitor {

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementAdapter#visitDefault(jsesh.mdc.model.ModelElement)
	 */
	public void visitDefault(ModelElement t) {
		for (int i=0; i < t.getNumberOfChildren(); i++) {
			t.getChildAt(i).accept(this);
		}
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitAlphabeticText(jsesh.mdc.model.AlphabeticText)
	 */
        @Override
	public void visitAlphabeticText(AlphabeticText t) {
		visitBasitItem(t);
		
	}

	/**
	 * @param t
	 */
	public void visitBasitItem(BasicItem t) {
		visitTopItem(t);
	}

	/**
	 * @param t
	 */
	public void visitTopItem(TopItem t) {
		visitDefault(t);
	}

	/* (non-Javadoc)
  	 * @see jsesh.mdc.model.ModelElementVisitor#visitBasicItemList(jsesh.mdc.model.BasicItemList)
	 */
        @Override
	public void visitBasicItemList(BasicItemList l) {
		visitDefault(l);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitCadrat(jsesh.mdc.model.Cadrat)
	 */
        @Override
	public void visitCadrat(Cadrat c) {
		visitBasitItem(c);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitCartouche(jsesh.mdc.model.Cartouche)
	 */
        @Override
	public void visitCartouche(Cartouche c) {
		visitInnerGroup(c);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitHBox(jsesh.mdc.model.HBox)
	 */
        @Override
	public void visitHBox(HBox b) {
		visitDefault(b);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitHieroglyph(jsesh.mdc.model.Hieroglyph)
	 */
        @Override
	public void visitHieroglyph(Hieroglyph h) {
		visitInnerGroup(h);
	}

	/*
	 * 
	 */
	public void visitInnerGroup(InnerGroup g) {
		visitHorizontalListElement(g);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitHRule(jsesh.mdc.model.HRule)
	 */
        @Override
	public void visitHRule(HRule h) {
		visitTopItem(h);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitLigature(jsesh.mdc.model.Ligature)
	 */
        @Override
	public void visitLigature(Ligature l) {
		visitInnerGroup(l);
	}

        @Override
	public void visitComplexLigature(ComplexLigature ligature) {
		visitHorizontalListElement(ligature);	
	}
	
	public void visitHorizontalListElement(HorizontalListElement elt) {
		visitDefault(elt);
	}
	
	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitModifier(jsesh.mdc.model.Modifier)
	 */
        @Override
	public void visitModifier(Modifier mod) {
		visitDefault(mod);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitModifierList(jsesh.mdc.model.ModifiersList)
	 */
        @Override
	public void visitModifierList(ModifiersList l) {
		visitDefault(l);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitOverwrite(jsesh.mdc.model.Overwrite)
	 */
        @Override
	public void visitOverwrite(Overwrite o) {
		visitInnerGroup(o);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitPhilology(jsesh.mdc.model.Philology)
	 */
        @Override
	public void visitPhilology(Philology p) {
		visitInnerGroup(p);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitSubCadrat(jsesh.mdc.model.SubCadrat)
	 */
        @Override
	public void visitSubCadrat(SubCadrat c) {
		visitInnerGroup(c);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitSuperScript(jsesh.mdc.model.Superscript)
	 */
        @Override
	public void visitSuperScript(Superscript s) {
		visitTopItem(s);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitTabStop(jsesh.mdc.model.TabStop)
	 */
        @Override
	public void visitTabStop(TabStop t) {
		visitTopItem(t);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitTopItemList(jsesh.mdc.model.TopItemList)
	 */
        @Override
	public void visitTopItemList(TopItemList t) {
		visitDefault(t);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitLineBreak(jsesh.mdc.model.LineBreak)
	 */
        @Override
	public void visitLineBreak(LineBreak b) {
		visitTopItem(b);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitPageBreak(jsesh.mdc.model.PageBreak)
	 */
        @Override
	public void visitPageBreak(PageBreak b) {
		visitTopItem(b);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitAbsoluteGroup(jsesh.mdc.model.AbsoluteGroup)
	 */
        @Override
	public void visitAbsoluteGroup(AbsoluteGroup g) {
		visitInnerGroup(g);
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElementVisitor#visitZoneStart(jsesh.mdc.model.ZoneStart)
	 */
        @Override
	public void visitZoneStart(ZoneStart start) {
		visitTopItem(start);
	}
	
        @Override
	public void visitTabbing(Tabbing tabbing) {
		visitTopItem(tabbing);
	}

        @Override
	public void visitTabbingClear(TabbingClear tabbingClear) {
		visitTopItem(tabbingClear);
	}
}
