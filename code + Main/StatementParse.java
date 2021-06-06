
import java.util.LinkedList;

// Two new member variables
// 1. String name;
// 2. LinkedList<Parse> children;

public class StatementParse extends Parse{
	protected LinkedList<Parse> children;
	boolean typed;
	// tree structure
	
	public StatementParse(String name, int index) {	
		//Parse in video
		// would not allow constructor name "Parse"
		super(name, index);	// "print", "declare", "assign"
		// TODO Auto-generated constructor stub
		this.children = new LinkedList<Parse>();
		this.typed = false;
	}
	
	public boolean equals(Parse other) {
		return (this.name == other.name) && (this.index == other.getIndex());
		// video: this.value == other.value
	}
	
	
	// recursively prints the entire parse
	public String toString() {
		//|| (this.equals(new StatementParse("sequence", 0)))
		if (this.equals(Parser.FAIL)) {
			return "";
		}
        String result = "";
        result += "(";
        result += this.name;
        for (Parse child: this.children) {
        	result += " " + child.toString();
        }
        result += ")";
        return result;	
    }
    
    
}
