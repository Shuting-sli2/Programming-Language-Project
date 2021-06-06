import java.util.ArrayList;
import java.util.List;

public class FunctionValue extends Value{
	Parse IR; // the intermediate representation of the function
	Environment environ; // the environment in which the function is defined
		// As Environment class has parent pointer, so it automatically keeps track of an Environment Stack
		// Ex. the Inner() keeps track of the Environment inside Outer()
	List<String> parameters; // a list of parameters to separate parameters
	List<String> types;
	String return_type;
	boolean has_signature;
	
	public FunctionValue(Parse node, Environment environ) {	
		super("func");
		this.IR = node;
		this.environ = environ;
		this.parameters =  new ArrayList<String>(); 
		this.types =  new ArrayList<String>(); 
		this.return_type = "var";
		this.has_signature = false;
	}
	
	public String toString() {
        return "closure";	
    }

}
