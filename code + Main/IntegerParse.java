
// original parse class become this IntegerParse class
public class IntegerParse extends Parse{
	protected int value; // leaf
	protected int index;
	
	public IntegerParse(int value, int index) {
		// Parse in the video
		// use Parse instead of IntegerParse
		// TODO Auto-generated constructor stub
		super("int", index);
		this.value = value;
	}
	
	public boolean equals(IntegerParse other) {
		return (this.value == other.getValue()) && (this.index == other.getIndex());
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
