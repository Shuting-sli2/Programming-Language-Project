import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassValue extends Value{

	Parse IR; // the intermediate representation
	Environment environ; // the environment class is defined in 
	
	public ClassValue(Parse node, Environment environ) {	
		super("class");
		this.IR = node; // IR of body
		this.environ = environ;
	}
	
	public String toString() {
        return "class";	
    }
	
	
}

