import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Value {
// subclass: ClassValue, Environment, FunctionValue, Location, IntegerValue
	// the name of the parse node
	protected String name;

	// constructor: ex. new Parse(0, -1)
	public Value(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public int getValue() {
		return ((IntegerValue) this).getValue();
	}

	public Environment getEnviron() {
		if (this instanceof FunctionValue) {
			return ((FunctionValue) this).environ;
		} else if (this instanceof ClassValue){
			return ((ClassValue) this).environ;
		} else if (this instanceof Location){ //location
			return ((Location) this).stored_place;
		} else { // Environment
			return (Environment) this;
		}
	}
	
	public String getIdentifier() {
		return ((Location)this).identifier;
	}

	public void setObject() {
		((Environment)this).is_object = true;
	}
	public boolean getObject() {
		return ((Environment)this).is_object;
	}

	public List<String> getParameters() {
		return ((FunctionValue) this).parameters;
	}
	
	public List<String> getTypes() {
		return ((FunctionValue) this).types;
	}
	
	public String getReturnType() {
		return ((FunctionValue) this).return_type;
	}
	
	public boolean getHasSignature() {
		return ((FunctionValue) this).has_signature;
	}
	
	public void setHasSignature() {
		((FunctionValue) this).has_signature = true;
	}

	public Parse getIR() {
		if (this instanceof FunctionValue) {
			return ((FunctionValue) this).IR;
		} else {
			return ((ClassValue) this).IR;
		}
	}


	public int addValue(Value other) {
		return ((IntegerValue) this).getValue() + ((IntegerValue) other).getValue();
	}

	public int minusValue(Value other) {
		return ((IntegerValue) this).getValue() - ((IntegerValue) other).getValue();
	}

	public int mulValue(Value other) {
		return ((IntegerValue) this).getValue() * ((IntegerValue) other).getValue();
	}

	public int divValue(Value other) {
		return ((IntegerValue) this).getValue() / ((IntegerValue) other).getValue();
	}

	// ==
	public boolean equals(Value other) {
		if (((IntegerValue) this).getValue() == ((IntegerValue) other).getValue()) {
			return true;
		} else {
			return false;
		}
	}

	// ==, int as argument
	public boolean equals(int other) {
		if (((IntegerValue) this).getValue() == other) {
			return true;
		} else {
			return false;
		}
	}

	// !=
	public boolean notEquals(Value other) {
		if (((IntegerValue) this).getValue() != ((IntegerValue) other).getValue()) {
			return true;
		} else {
			return false;
		}
	}

	// !=, int as argument
	public boolean notEquals(int other) {
		if (((IntegerValue) this).getValue() != other) {
			return true;
		} else {
			return false;
		}
	}

	// <=
	public boolean lessEquals(Value other) {
		if (((IntegerValue) this).getValue() <= ((IntegerValue) other).getValue()) {
			return true;
		} else {
			return false;
		}
	}

	// <=, int as argument
	public boolean lessEquals(int other) {
		if (((IntegerValue) this).getValue() <= other) {
			return true;
		} else {
			return false;
		}
	}

	// >=
	public boolean greaterEquals(Value other) {
		if (((IntegerValue) this).getValue() >= ((IntegerValue) other).getValue()) {
			return true;
		} else {
			return false;
		}
	}

	// >=, int as argument
	public boolean greaterEquals(int other) {
		if (((IntegerValue) this).getValue() >= other) {
			return true;
		} else {
			return false;
		}
	}

	// <
	public boolean less(Value other) {
		if (((IntegerValue) this).getValue() < ((IntegerValue) other).getValue()) {
			return true;
		} else {
			return false;
		}
	}

	// <, int as argument
	public boolean less(int other) {
		if (((IntegerValue) this).getValue() < other) {
			return true;
		} else {
			return false;
		}
	}

	// >
	public boolean more(Value other) {
		if (((IntegerValue) this).getValue() > ((IntegerValue) other).getValue()) {
			return true;
		} else {
			return false;
		}
	}

	// >, int as argument
	public boolean more(int other) {
		if (((IntegerValue) this).getValue() > other) {
			return true;
		} else {
			return false;
		}
	}

}
