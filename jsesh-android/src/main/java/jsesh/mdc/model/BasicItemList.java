package jsesh.mdc.model;

import jsesh.mdc.interfaces.BasicItemListInterface;

/**
 * BasicItemList : an auxiliary class, used as <em>implementation</em> for all elements
 * which contain basic items.
 * 
 * As such, a BasicItemList does not constitute an element.
 * 
 * @author rosmord
 *
 *This code is published under the GNU LGPL.
 */

public class BasicItemList
    extends EmbeddedModelElement
	implements BasicItemListInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6889728940705626342L;

	public BasicItemList() {
	}

	public void addBasicItem(BasicItem basicItem) {
		addChild(basicItem);
	}
	
	public void addBasicItemAt(int id, BasicItem basicItem) {
		addChildAt(id, basicItem);
	}

        @Override
	public void accept(ModelElementVisitor v) {
		v.visitBasicItemList(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
        @Override
	public String toString() {
		return getChildrenAsString();
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElement#compareToAux(jsesh.mdc.model.ModelElement)
	 */
        @Override
	public int compareToAux(ModelElement e) {
		return compareContents(e);
	}
	

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElement#deepCopy()
	 */
        @Override
	public BasicItemList deepCopy() {
		BasicItemList r= new BasicItemList();
		copyContentTo(r);
		return r; 
	}
	
	@Override
	public HorizontalListElement buildHorizontalListElement() {
		return new SubCadrat(deepCopy());
	}
	
	/* (non-Javadoc)
	 * @see jsesh.mdc.model.ModelElement#buildTopItem()
	 */
        @Override
	public TopItem buildTopItem() {
		Cadrat c= new Cadrat();
		HBox b= new HBox();
		SubCadrat sub= new SubCadrat((BasicItemList) this.deepCopy());
		b.addHorizontalListElement(sub);
		c.addHBox(b);
		return c;
	}
} // end BasicItemList
