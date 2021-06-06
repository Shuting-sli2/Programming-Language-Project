import java.util.LinkedList;

public class VariableParse extends Parse{
	 	VariableParse type;
		boolean typed;
	    
		public VariableParse(String name, int index) {	
			super(name, index);
			this.typed = false;
		}
		
		public boolean equals(Parse other) {
			return (this.name == other.name) && (this.index == other.getIndex());
			// video: this.value == other.value
		}
		
		// only difference from StatementParse is toString method
		public String toString() {
			if (this.equals(Parser.FAIL)) {
				return "";
			}
	        return "" + this.name;
		}


}
