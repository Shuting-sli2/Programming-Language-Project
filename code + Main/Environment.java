import java.util.HashMap;

public class Environment extends Value {
	HashMap<String, Value> map;	// For storing variables, could be Integer/Function
	HashMap<String, String> type_map;	// For storing variables, could be Integer/Function
	Environment parent;
	boolean is_object; // if is_object is true --> a member function
	
	public Environment() {
		super("obj");
        this.map = new HashMap<String, Value>();
        this.type_map = new HashMap<String, String>();
        this.parent = null;
        this.is_object = false; 
	}
	
	public Environment(String name, Value value) {
        // this.parent = ?
		super("obj");
        this.map = new HashMap<String, Value>();
        this.type_map = new HashMap<String, String>();
        this.map.put(name,value);
        this.type_map.put(name,"var");
        this.parent = this;
        this.is_object = false;
	}
	

	
	// Is there need to keep this method? 
	public boolean equals(Environment other) {
    	if (other != null && this != null) {
    		return (this.map.equals(other.map)) && (this.parent.equals(other.parent));
    	} else if (other == null && this != null || other != null && this == null) {
    		return false;
    	} else {
    		return true;
    	}
    }
	
	public boolean getIsObject() {
		return this.is_object;
	}
	
	
	public String toString() {
        return "obj";	
    }

		
}
