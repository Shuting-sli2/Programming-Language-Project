
import java.util.LinkedList;

public class Parse {
	
    // the name of the parse node
	protected String name;
    // the number of characters into the string we've parsed
	protected int index;
	protected LinkedList<Parse> children;
    
    // constructor: ex. new Parse(0, -1)
    public Parse(String name, int index) {
        this.name = name;
        this.index = index;
    }
    
    // compare Parse with another Parse
    // ex p1.equals(p2)
    public boolean equals(Parse other) {
    	if (other != null && this != null) {
    		return (this.name.equals(other.name)) && (this.index == other.index);
    	} else if (other == null && this != null || other != null && this == null) {
    		return false;
    	} else {
    		return true;
    	}
    }

    
    public String getName() {
        return this.name;
    }
    
    public boolean getVariableTyped() {
        return ((VariableParse)this).typed;
    }
    
    public VariableParse getVariableType() {
        return ((VariableParse)this).type;
    }
    
    public void setStatementTyped() {
        ((StatementParse)this).typed = true;
    }
    
    public boolean getStatementTyped() {
        return ((StatementParse)this).typed;
    }

    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
    	this.index = index;
    }
    
    public int getValue() {
        return ((IntegerParse)this).getValue();
    }
    
    public LinkedList<Parse> getChildren() {
        return ((StatementParse)this).children;
    }


}