
public class IntegerValue extends Value{
	protected int value; // leaf
	
	public IntegerValue(Integer value) {
		super("int");
		this.value = value;
	}
	
	public String toString() {
		if (this.equals(Parser.FAIL)) {
			return "";
		}
        return ""+ this.value;
    }
    
    public int getValue() {
        return this.value;
    }

}
