/*
 * Created on 20 sept. 2004 by rosmord
 * This code can be distributed under the Gnu Library Public Licence.
 **/
package jsesh.mdc.model.operations;

import java.util.Collections;
import java.util.List;

import jsesh.mdc.model.ModelElement;

/**
 * Insertion represents the action of inserting an element in another one. 
 *   @author rosmord
 *
 */
public class Insertion extends ModelOperation {
	
	private int index;
	private List children;

	/**
	 * 
	 * @param element the container element.
	 * @param index the position in the container.
	 * @param child the element to be inserted.
	 */
	public Insertion(ModelElement element, int index, ModelElement child) {
			super(element);
			this.index= index;
			this.children= Collections.singletonList(child);
	}
	
	/**
	 * Create an insertion object representing the addition of a list of ModelElements in element.
	 * @param element  the container element.
	 * @param index the position where the insertion will take place
	 * @param children the elements to add.
	 */
	
	public Insertion(ModelElement element, int index, List children) {
		super(element);
		this.index= index;
		this.children= children;
	}
	

	/**
	 * @return Returns the children.
	 */
	public List getChildren() {
		return children;
	}
	
	/**
	 * @return the index.
	 */
	public int getIndex() {
		return index;
	}

	/* (non-Javadoc)
	 * @see jsesh.mdc.model.operations.ModelOperation#visitModelOperation(jsesh.mdc.model.operations.ModelOperationVisitor)
	 */
	public void accept(ModelOperationVisitor v) {
		v.visitInsertion(this);		
	}

}
