/**
 * 
 */
package finalproject;


/**
 * @author jeremiemartinez
 *
 */
public class WeightedDocument extends GenericDocument{
	
	private VectorTermSpace vector;
	
	public WeightedDocument(int id, String title) {
		super(id, title);
	}

	public VectorTermSpace getVector() {
		return vector;
	}

	public void setVector(VectorTermSpace vector) {
		this.vector = vector;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof WeightedDocument)
			if (this.getId() == ((GenericDocument)o).getId()){
				return true;
			} else{
				return (this.getVector()) == ((WeightedDocument)o).getVector();
			}
		else
			return false;
	}
	
	
}
