import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Interpreter {
	private static final String Value = null;
	String output;
	Environment curr_environment; // HashMap<String, Value> map;
	// the 3 member variables for returning from function:
	Boolean returning; // returning or not, default to no (return_value is null)
	Value return_value; // default to null
	Integer n_th_function_call; // keep track of the number of function call
								// default to 0, error when returning while n_th_function_call == 0
	String classs; // Keep track of the class instances

	// evaluate parse, and returns a String of output
	public Interpreter() {
		this.output = "";
		this.curr_environment = new Environment(); // this.curr_environment is address, not null
		// parent Environment is null by default in Environment Class
		// initialize for returning from function
		this.n_th_function_call = 0;
		this.returning = false;
		this.return_value = new IntegerValue(0);
		this.classs = "";
	}

	// execute
	// should catches error messages
	public String execute(Parse node) {
		// System.out.println("node:" + node);
		/*
		if (this.returning) {
			System.out.println("returning is true");
		} else {
			System.out.println("returning is false");
		}*/
		
		// System.out.println(this.curr_environment.map);
		// System.out.println(this.curr_environment.parent);
		if (node.equals(Parser.FAIL)) {
			return "syntax error";
		}
		try {
			exec(node);
		} catch (RuntimeException e) {
			//System.out.println("error: " + ">>>" + e.toString() + "<<<");
			if (e.toString().equals("java.lang.RuntimeException: runtime error: divide by zero")) {
				output += "runtime error: divide by zero";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: variable already defined")) {
				output += "runtime error: variable already defined";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: undefined variable")) {
				output += "runtime error: undefined variable";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: math operation on functions")) {
				output += "runtime error: math operation on functions";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: returning outside function")) {
				output += "runtime error: returning outside function";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: duplicate parameter")) {
				output += "runtime error: duplicate parameter";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: calling a non-function")) {
				this.n_th_function_call = 0;
				this.returning = false;
				output += "runtime error: calling a non-function";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: argument mismatch")) {
				this.n_th_function_call = 0;
				this.returning = false;
				output += "runtime error: argument mismatch";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: member of non-object")) {
				output += "runtime error: member of non-object";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: undefined member")) {
				output += "runtime error: undefined member";
			} else if (e.toString().equals("java.lang.RuntimeException: runtime error: type mismatch")) {
				this.n_th_function_call = 0;
				this.returning = false;
				output += "runtime error: type mismatch";
			}
		}
		// System.out.println("Debugging: "+ this.curr_environment.map);
		// System.out.println("Debugging: "+ node+"\n"+ this.return_value + "," +
		// this.n_th_function_call +", " + this.curr_environment.map + "\n");
		return this.output;
	}

	public void exec(Parse node) {
		// System.out.println("Debugging_NODE: "+ node);
		// System.out.println("node name: "+ node.name);
		// System.out.println("node children: "+ node.getChildren());
		if (node.name.equals("sequence")) {
			this.execute_sequence(node);
		} else if (node.name.equals("print")) {
			this.execute_print(node);
		} else if (node.name.equals("assign")) {
			this.execute_assign(node);
		} else if (node.name.equals("lookup")) {
			this.execute_lookup(node);
		} else if (node.name.equals("varloc")) {
			this.execute_varloc(node);
		} else if (node.name.equals("if")) {
			this.execute_if(node);
		} else if (node.name.equals("ifelse")) {
			this.execute_ifelse(node);
		} else if (node.name.equals("while")) {
			this.execute_while(node);
		} else if (node.name.equals("call")) { // calling function
			this.execute_function_call(node);
		} else if (node.name.equals("return")) { // returning function
			this.execute_return(node);
		} else if (node.name.equals("function")) { // when a function is defined
			this.execute_function(node);
		} else if (node.name.equals("declare")) {
			this.execute_declare(node);
		} else if (node.name.equals("class")) {
			this.execute_class(node);
		} else if (node.name.equals("memloc")) {
			this.execute_memloc(node);
		} else if (node.name.equals("member")) {
			this.execute_member(node);
		} else {
			this.execute_math_expression(node);
		}
	}

	public void execute_sequence(Parse node) {
		// System.out.println("EXECUTE_SEQUENCE");
		for (Parse child : node.getChildren()) { // children: LinkedList<Parse>
			this.exec(child);

			if (this.returning) {
				// System.out.println("this.returning is true;");
			} else {
				//System.out.println("this.returning is false;");
			}

			if (this.returning) {
				break;
			}
		}
	}

	// declare int a 5
	// declare a 5
	public void execute_declare(Parse node) {
		// System.out.println("\n\nDEBUGGING execute_declare.");
		// System.out.println("parse: " + node);

		int n_child = node.getChildren().size();
		String var_name = "";
		Value result = new Value("");
		String type = "var";
		if (n_child == 3) {
			type = node.getChildren().get(0).name;
			var_name = node.getChildren().get(1).name;		
			result = this.eval(node.getChildren().get(2)); // eval_class			
			if (this.curr_environment.map.containsKey(var_name)) {
				throw new RuntimeException("runtime error: variable already defined");
			}
			if (type.equals("var")) {
				// do nothing
			} else if (!type.equals(result.name)) {
				System.out.println("error in declare node when child = 3.");
				throw new RuntimeException("runtime error: type mismatch");
			}
			// else type matches, add to 2 maps
		} else { // child == 2
			// System.out.println(n_child);
			var_name = node.getChildren().get(0).name;
			result = this.eval(node.getChildren().get(1));
			if (this.curr_environment.map.containsKey(var_name)) {
				throw new RuntimeException("runtime error: variable already defined");
			}
		}
		// System.out.println(result);
		this.curr_environment.map.put(var_name, result);
		this.curr_environment.type_map.put(var_name, type);
		// System.out.println(this.curr_environment.map);
		// System.out.println(this.curr_environment.type_map);
	}

	// ( assign (varloc a) (function ( parameters ) ( sequence ) ) ) )
	public void execute_assign(Parse node) { // "assign"
		Value new_val = this.eval(node.getChildren().get(1));

		Value location = this.eval(node.getChildren().get(0)); // eval "varloc" -> Location
		Environment env = location.getEnviron();
		// System.out.println("ASSIGN_LOCATION_curr_environment: " +
		// this.curr_environment.map);
		// System.out.println("ASSIGN_LOCATION_environment(1): " + env.map);

		// check for variable's type stored in the map
		// v.s.
		// value's type
		String variable_name = "";
		if (node.getChildren().get(0).getChildren().size() == 1) { // member
			variable_name = node.getChildren().get(0).getChildren().get(0).name;
		} else { // variable
			variable_name = node.getChildren().get(0).getChildren().get(1).name;
		}
		String value_type = new_val.name;

		String variable_type = env.type_map.get(variable_name);

		/*
		 * System.out.println(variable_name); System.out.println(new_val);
		 * System.out.println(variable_type); System.out.println(value_type);
		 */
		if (variable_type.equals("var")) {
			// let it do anything
		} else if (!variable_type.equals(value_type)) {
			System.out.println("error in assign node");
			throw new RuntimeException("runtime error: type mismatch");
		}
		env.map.put(variable_name, new_val);

		// System.out.println("ASSIGN_LOCATION_environment(2): " + env.map);
	}

	public void execute_lookup(Parse node) {
		this.eval(node);
	}

	public void execute_varloc(Parse node) {
		this.eval(node);
	}

	public void execute_memloc(Parse node) {
		this.eval(node);
	}

	public void execute_member(Parse node) {
		this.eval(node);
	}

	public void execute_function(Parse node) {
		this.eval(node);
	}

	public void execute_class(Parse node) {
		this.eval(node);
	}

	// consider function truthiness
	public void execute_if(Parse node) {
		//System.out.println("DEBUGGING execute_if: "+node + "\n" + this.eval(node.getChildren().get(0)));

		Value condition = this.eval(node.getChildren().getFirst());

		if (condition.name.equals("func")
				||condition.name.equals("class") 
				|| condition.name.equals("obj") 
				|| condition.notEquals(0)) { // if																		// is
																													// met
			this.push_environ();
			this.exec(node.getChildren().get(1)); // math expression
			this.pop_environ();
		}
		//System.out.println(this.curr_environment.map);
		//System.out.println(this.curr_environment.type_map);
	}

	// consider function truthiness
	public void execute_ifelse(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		if (lhs.name.equals("func") || lhs.name.equals("class") || lhs.name.equals("obj") || lhs.notEquals(0)) { // if																								// met
			// System.out.println("ifelse environment1: " + this.curr_environment.map);
			this.push_environ();
			// System.out.println("ifelse node's middle children: " +
			// node.getChildren().get(1));
			this.exec(node.getChildren().get(1)); // math expression
			// System.out.println("ifelse environment2: " + this.curr_environment.map);
			this.pop_environ();
		} else {
			// System.out.println("else");
			this.push_environ();
			this.exec(node.getChildren().get(2)); // math expression
			this.pop_environ();
		}
	}

	// consider function truthiness
	public void execute_while(Parse node) {
		//System.out.println("hi");
		Value condition = this.eval(node.getChildren().get(0));
		// remember to check if a function has already returned
		while (!this.returning && condition.name.equals("func")
				|| condition.name.equals("class") || condition.name.equals("obj") || condition.notEquals(0)
				
				) { // while condition is met
			this.push_environ();
			this.exec(node.getChildren().get(1)); // math expression
			// System.out.println(condition);
			this.pop_environ();
			condition = this.eval(node.getChildren().get(0));
			if (this.returning) {
				break;
			}
		}
		//System.out.println(condition);
	}

	public void execute_return(Parse node) { // eval_"ret"_node
		
		// Check if returning outside a function
		if (this.n_th_function_call == 0) {
			throw new RuntimeException("runtime error: returning outside function");
		}
		// System.out.println("execute_return: "+node.getChildren().get(0));
		// return node's child:
		// (call (lookup a) (arguments (lookup b) (lookup c)))

		// Value ret = this.eval(node.getChildren().get(0));
		// System.out.println("return environment: " + this.curr_environment.map);
		this.return_value = this.eval(node.getChildren().get(0));
		// System.out.println("Hi");
		this.returning = true;
	}

	public void execute_function_call(Parse node) {
		this.eval(node);
	}

	public void execute_parameters(Parse node) {
		this.eval(node);
	}

	public void execute_arguments(Parse node) {
		this.eval(node);
	}

	public void execute_print(Parse node) {
		//System.out.println("hi");
		// System.out.println("\nPRINT NODE\n" + node + "\n");
		Environment temp = this.curr_environment;
		// System.out.println("\nPRINT NODE\n" + node + "\n");
		Value childOfPrint = this.eval(node.getChildren().getFirst());
		// System.out.println("\nPRINT NODE\n" + node + "\n");
		// System.out.println("print environment: " + this.curr_environment.map);
		// System.out.println("print b: " + childOfPrint);
		if (childOfPrint == null) {
			// System.out.print("null child of print");
			this.output += "0";
		} else {
			this.output += childOfPrint;
		}
		this.output += "\n";
	}

	public void execute_math_expression(Parse node) {
		this.eval(node);
	}

	public void push_environ() {
		Environment last = new Environment();
		last.parent = this.curr_environment;
		this.curr_environment = last;
	}

	public void pop_environ() {
		this.curr_environment = this.curr_environment.parent;
	}

	public Value eval(Parse node) {
		// System.out.println(node.name);
		if (node.name.equals("int")) {
			return this.eval_int(node);
		} else if (node.name.equals("call")) { // call both function & class
			return this.eval_function_call(node);
		} else if (node.name.equals("+")) {
			return this.eval_plus(node);
		} else if (node.name.equals("-")) {
			return this.eval_minus(node);
		} else if (node.name.equals("*")) {
			return this.eval_mul(node);
		} else if (node.name.equals("/")) {
			return this.eval_div(node);
		} else if (node.name.equals("==")) { // "=="| "!="| "<="| ">="| "<"| ">";
			return this.eval_equal(node);
		} else if (node.name.equals("!=")) {
			return this.eval_not_equal(node);
		} else if (node.name.equals("<=")) {
			return this.eval_less_than_equal(node);
		} else if (node.name.equals(">=")) {
			return this.eval_more_than_equal(node);
		} else if (node.name.equals("<")) {
			return this.eval_less_than(node);
		} else if (node.name.equals(">")) {
			return this.eval_more_than(node);
		} else if (node.name.equals("lookup")) {
			return this.eval_lookup(node);
		} else if (node.name.equals("varloc")) {
			return this.eval_varloc(node);
		} else if (node.name.equals("memloc")) {
			return this.eval_memloc(node);
		} else if (node.name.equals("member")) {
			return this.eval_member(node);
		} else if (node.name.equals("!")) {
			return this.eval_exclamation(node);
		} else if (node.name.equals("||")) {
			return this.eval_or(node);
		} else if (node.name.equals("&&")) {
			return this.eval_and(node);
		} else if (node.name.equals("function")) {
			return this.eval_function(node);
		} else if (node.name.equals("class")) {
			return this.eval_class(node);
		} else {
			throw new AssertionError("Unexpected term " + node.name);
		}
	}

	// eval series
	// add left child and right child
	//
	public Value eval_plus(Parse node) {
		// System.out.println(node.getChildren().getFirst());
		// System.out.println(node.getChildren().get(1));
		// System.out.println(output);

		// Value's +/-/*/..
		// check if adding functions
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}
		int result = lhs.addValue(rhs);
		return new IntegerValue(result);
	}

	// minus right child from left child
	public Value eval_minus(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}
		int result = lhs.minusValue(rhs);
		return new IntegerValue(result);
	}

	public Value eval_mul(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}

		int result = lhs.mulValue(rhs);
		return new IntegerValue(result);
	}

	public Value eval_div(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}

		if (rhs.equals(0)) {
			// System.out.println("0");
			throw new RuntimeException("runtime error: divide by zero");
		}

		int result = lhs.divValue(rhs);
		return new IntegerValue(result);
	}

	// "=="
	// https://www.baeldung.com/java-xor-operator
	// A ^ B
	// A ^ B
	//
	public Value eval_equal(Parse node) {
		// eval once

		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));
		// System.out.println(lhs);
		// System.out.println(rhs);
		// System.out.println(lhs.name);
		// System.out.println(rhs.name);

		// eval lookup -> Environment Value

		if ((lhs.name.equals("func") && !rhs.name.equals("func"))
				|| (lhs.name.equals("class") && !rhs.name.equals("class"))
				|| (lhs.name.equals("obj") && !rhs.name.equals("obj"))
				|| (!lhs.name.equals("func") && rhs.name.equals("func"))
				|| (!lhs.name.equals("class") && rhs.name.equals("class"))
				|| (!lhs.name.equals("obj") && rhs.name.equals("obj"))) {
			// System.out.println("one of the child is a function/class instance");
			// one of the child is a function instance
			return new IntegerValue(0);

		} else if ((lhs.name.equals("func") && rhs.name.equals("func"))
				|| (lhs.name.equals("class") && rhs.name.equals("class"))
				|| (lhs.name.equals("obj") && rhs.name.equals("obj"))) {
			// both children are function instances
			// System.out.println("function vs function");

			// System.out.println("lhs: "+
			// node.getChildren().getFirst().getChildren().getFirst().name);
			// System.out.println("rhs: "+
			// node.getChildren().get(1).getChildren().get(0).name);
			if (lhs == rhs) {
				return new IntegerValue(1);
			} else {
				return new IntegerValue(0);
			}

		} else {
			// int v.s. int
			// System.out.println("int vs int");
			if (lhs.equals(rhs)) {
				return new IntegerValue(1);
			} else {
				return new IntegerValue(0);
			}
		}
	}

	// "!="
	public Value eval_not_equal(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if ((lhs.name.equals("func") && !rhs.name.equals("func"))
				|| (lhs.name.equals("class") && !rhs.name.equals("class"))
				|| (lhs.name.equals("obj") && !rhs.name.equals("obj"))
				|| (!lhs.name.equals("func") && rhs.name.equals("func"))
				|| (!lhs.name.equals("class") && rhs.name.equals("class"))
				|| (!lhs.name.equals("obj") && rhs.name.equals("obj"))) {
			return new IntegerValue(1);
		} else if (lhs.name.equals("func") && rhs.name.equals("func")
				|| lhs.name.equals("class") && rhs.name.equals("class")
				|| lhs.name.equals("obj") && rhs.name.equals("obj")) {

			if (lhs == rhs) {
				return new IntegerValue(0);
			} else {
				return new IntegerValue(1);
			}

		} else {
			if (lhs.equals(rhs)) {
				return new IntegerValue(0);
			} else {
				return new IntegerValue(1);
			}
		}

	}

	// <=
	public Value eval_less_than_equal(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}

		if (lhs.lessEquals(rhs)) {
			return new IntegerValue(1);
		} else {
			return new IntegerValue(0);
		}
	}

	// >=
	public Value eval_more_than_equal(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}

		if (lhs.greaterEquals(rhs)) {
			return new IntegerValue(1);
		} else {
			return new IntegerValue(0);
		}
	}

	// <
	public Value eval_less_than(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}

		if (lhs.less(rhs)) {
			return new IntegerValue(1);
		} else {
			return new IntegerValue(0);
		}
	}

	// >
	public Value eval_more_than(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));
		if (lhs.name.equals("func") || rhs.name.equals("func") || lhs.name.equals("class") || rhs.name.equals("class")
				|| lhs.name.equals("obj") || rhs.name.equals("obj")) {
			throw new RuntimeException("runtime error: math operation on functions");
		}

		if (lhs.more(rhs)) {
			return new IntegerValue(1);
		} else {
			return new IntegerValue(0);
		}
	}

	// ||
	// function truthiness
	public Value eval_or(Parse node) {
		Value lhs = this.eval(node.getChildren().getFirst());
		if (lhs.name.equals("func") || lhs.name.equals("class") || lhs.name.equals("obj") || lhs.notEquals(0)) {
			return new IntegerValue(1);
		} else if (this.eval(node.getChildren().get(1)).name.equals("func")
				|| this.eval(node.getChildren().get(1)).name.equals("class")
				|| this.eval(node.getChildren().get(1)).name.equals("obj")
				|| this.eval(node.getChildren().get(1)).notEquals(0)) {
			return new IntegerValue(1);
		} else {
			return new IntegerValue(0);
		}
	}

	// &&
	// function truthiness
	public Value eval_and(Parse node) {

		Value lhs = this.eval(node.getChildren().getFirst());
		Value rhs = this.eval(node.getChildren().get(1));

		if (lhs.name.equals("func") && rhs.name.equals("func") || lhs.name.equals("class") && rhs.name.equals("class")
				|| lhs.name.equals("obj") && rhs.name.equals("obj")) {
			return new IntegerValue(1);
		} else {
			if (lhs.name.equals("func") || lhs.name.equals("class") || lhs.name.equals("obj")) { // function v.s.
																									// int
				if (rhs.equals(0)) {
					return new IntegerValue(0);
				} else {
					return new IntegerValue(1);
				}
			} else if (rhs.name.equals("func") || rhs.name.equals("class") || rhs.name.equals("obj")) { // int
																										// v.s.function
				if (lhs.equals(0)) {
					return new IntegerValue(0);
				} else {
					return new IntegerValue(1);
				}
			} else { // int v.s. int
				if (lhs.notEquals(0) && rhs.notEquals(0)) {
					// both are functions
					return new IntegerValue(1);
				} else {
					// only one is function
					return new IntegerValue(0);
				}
			}
		}
	}

	// !
	// function truthiness
	public Value eval_exclamation(Parse node) {
		Value child = this.eval(node.getChildren().getFirst());

		if (child.name.equals("func") || child.name.equals("class") || child.name.equals("obj")) {
			// System.out.println("hi");
			return new IntegerValue(0);
		}
		if (child.equals(0)) {
			return new IntegerValue(1);
		} else {
			return new IntegerValue(0);
		}
	}

	public Value eval_int(Parse node) {
		return new IntegerValue(node.getValue());
	}

	public Value eval_varloc(Parse node) {
		return eval_varloc(node, 0, this.curr_environment);
	}

	private Value eval_varloc(Parse node, Integer n_th_parent, Environment env) {

		if (env == null) {
			// System.out.println("hi");
			throw new RuntimeException("runtime error: undefined variable");
		}
		if (env.map.containsKey(node.getChildren().get(0).name)) { // if the keyset of the environment contains the //
			return new Location(env, node.getChildren().get(0).name); // variable name
		} else {
			return eval_varloc(node, n_th_parent + 1, env.parent);
		}
	}

	// (memloc (varloc foo) bar)
	public Value eval_memloc(Parse node) { // "memloc"
		// this.eval(first child) -> returns Location

		// varloc foo returns the location of where foo is stored
		// than we get foo, see if it is an object?
		Value location = this.eval(node.getChildren().get(0)); // returns Location
		Environment env = location.getEnviron();
		String identifier = location.getIdentifier();
		// System.out.println(identifier);

		if (!env.map.containsKey(identifier)) {
			System.out.println("memloc1");
			throw new RuntimeException("runtime error: undefined member");
		}

		if (env.map.get(identifier).name.equals("class")) {
			throw new RuntimeException("runtime error: member of non-object");
		}
		Environment env_of_member = env.map.get(identifier).getEnviron(); // an environment
		// System.out.println(env_of_member.map);

		String member = node.getChildren().get(1).name;
		if (!env_of_member.map.containsKey(member)) {
			System.out.println("memloc2");
			throw new RuntimeException("runtime error: undefined member");
		}
		return new Location(env_of_member, member);
	}

	public Value eval_lookup(Parse node) {
		// System.out.println(node);
		return eval_lookup(node, this.curr_environment);
	}

	private Value eval_lookup(Parse node, Environment env) {
		if (env == null) {
			throw new RuntimeException("runtime error: undefined variable");
		}
		if (env.map.containsKey(node.getChildren().get(0).name)) {
			// if the keyset of the environment contains the variable name
			// System.out.println(env.map.get(node.getChildren().get(0).name));
			return env.map.get(node.getChildren().get(0).name); // return the stored value of the identifier key
		} else {
			return eval_lookup(node, env.parent);
		}
	}

	// (member (lookup c) numerator)
	// lhs child returns an object(Value: Environment)

	// (member (lookup v) value)
	public Value eval_member(Parse node) {
		// runtime error: member of non-object

		Value obj = this.eval(node.getChildren().get(0));
		if (!obj.name.equals("obj")) {
			throw new RuntimeException("runtime error: member of non-object");
		}
		Environment env = obj.getEnviron();
		if (!env.is_object) {
			throw new RuntimeException("runtime error: member of non-object");
		}
		// System.out.println(env.map);

		if (!env.map.containsKey(node.getChildren().get(1).name)) {
			System.out.println("member");
			throw new RuntimeException("runtime error: undefined member");
		}
		return env.map.get(node.getChildren().get(1).name);
	}

	// (function (parameters n) (sequence (print (lookup n))))
	public Value eval_function(Parse node) { // "function" node
		/*
		 System.out.println("\n\nDEBUGGING Eval_function.");
		 System.out.println("parse: " + node); // (function (parameters n) (sequenc (print (lookup n)))) System.out.println("parse.index:  "+ node.getIndex());//
		 System.out.println("node:  "+ node.name);// function
		 System.out.println("node.children:  " + node.getChildren()); //null
		 */

		// System.out.println("parse: " + node);
		// when a function is defined, a new instance of Closure is created
		FunctionValue closure = new FunctionValue(node, this.curr_environment);
		Set<String> duplicate_check = new HashSet<String>();
		// System.out.println("Debugging_eval_function_closure: " + closure);
		int closure_n_child = node.getChildren().size(); // func node has how many children

		if (closure_n_child == 3) {
			// 3 children: child0=signature, child1=parameters, child2=program
			// has (signature)
			
			Parse signature_child = node.getChildren().get(0);
			Parse parameter_child = node.getChildren().get(1);
			Parse program_child = node.getChildren().get(2);
			
			
			closure.setHasSignature();
			int parameters_num = parameter_child.getChildren().size();
			// loop through (parameters) node
			for (int i = 0; i < parameters_num; i++) {
				if (duplicate_check.contains(parameter_child.getChildren().get(i).name)) {
					throw new RuntimeException("runtime error: duplicate parameter");
				} else {
					duplicate_check.add(parameter_child.getChildren().get(i).name);
				}
				closure.parameters.add(parameter_child.getChildren().get(i).name);

				// loop through signature to add type to closure's types list
				String type = signature_child.getChildren().get(i).name;
				closure.types.add(type);
			}
			int position_of_return_type = parameters_num;
			String return_type = signature_child.getChildren().get(position_of_return_type).name;
			closure.return_type = return_type;
			//sSystem.out.println(closure.return_type);
		} else {
			// 2 children: child0=parameters, child2=program
			// no signature
			for (int i = 0; i < node.getChildren().get(0).getChildren().size(); i++) {
				if (duplicate_check.contains(node.getChildren().get(0).getChildren().get(i).name)) {
					// System.out.println("Debugging_eval_function: runtime error: duplicate
					// parameter");
					throw new RuntimeException("runtime error: duplicate parameter");
				} else {
					duplicate_check.add(node.getChildren().get(0).getChildren().get(i).name);
				}
				closure.parameters.add(node.getChildren().get(0).getChildren().get(i).name);
				// default all type to "var"
				closure.types.add("var");
			}
		}
		// System.out.println(closure.types +"," + closure.parameters);
		// System.out.println(closure.return_type);
		return closure;
	}

	public Value eval_class(Parse node) { // "function" node
		ClassValue class_value = new ClassValue(node, this.curr_environment);
		return class_value;
	}

	public Value eval_function_call(Parse node) {
		// System.out.println("\n\nDEBUGGING Eval_function_call.");
		// System.out.println("parse: " + node); // (function (parameters n) (sequence
		// (print (lookup n))))
		// System.out.println("parse.index: "+ node.getIndex());
		// System.out.println("node: "+ node.name);// function
		// System.out.println("node.children: " + node.getChildren()); //null
		Value closure = this.eval(node.getChildren().get(0)); // prints "closure"
		// System.out.println(closure);

		if (closure.name.equals("class")) {
			// return Environment if class
			// ENVIRONMENT: where all the members are stored.
			// store member(String name)-value pair into the environment's map
			// where you execute the body of a class
			if (node.getChildren().get(1).getChildren().size() != 0) {
				throw new RuntimeException("runtime error: argument mismatch");
			}

			// 1. save curr_environment to a variable
			Environment temp = this.curr_environment; // main environment

			// 2. set this.curr_environment to the closure's environment
			this.curr_environment = closure.getEnviron();

			// 3. push a new environment to the stack
			this.push_environ(); // this is the environment that should be returned

			// 4. execute the body of class
			if (closure.getIR().getChildren().size() != 0) {
				for (int i = 0; i < closure.getIR().getChildren().size(); i++) {
					this.exec(closure.getIR().getChildren().get(i)); // not sure
				}
			}
			// a bunch of declaration statements
			// store members in this environment's map

			// 5. store returned environment
			Value ret = this.curr_environment;
			ret.setObject();

			// 6. after the function is done, set the current environment back
			this.curr_environment = temp;

			return ret;

		} else if (!closure.name.equals("func")) {
			throw new RuntimeException("runtime error: calling a non-function");
		} // else, FUNCTION.

		// System.out.println("\n\nplace 0: "+ this.n_th_function_call);
		this.n_th_function_call++;
		// System.out.println("place 1: "+ this.n_th_function_call);
		// 1. eval each argument from left to right in current environment

		ArrayList<Value> arguments = new ArrayList<Value>();
		int argument_size = node.getChildren().get(1).getChildren().size();

		// when calling functions: need to check if a function is a member
		if (closure.getEnviron().is_object) {
			// if member function
			// if a member function, it automatically takes the object(environment) as the
			// first argument
			// checking for argument mismatch: check after inclusion of object as the first
			// argument
			arguments.add(closure.getEnviron());
		}

		argument_size += arguments.size();

		if (argument_size != closure.getParameters().size()) {
			// System.out.println(argument_size);
			// System.out.println(closure.getParameters().size());
			// System.out.println("runtime error: argument mismatch");
			// System.out.println(node);
			throw new RuntimeException("runtime error: argument mismatch");
		}

		for (int i = 0; i < node.getChildren().get(1).getChildren().size(); i++) {
			arguments.add(this.eval(node.getChildren().get(1).getChildren().get(i)));
		}

		// System.out.println(arguments);
		// 2. save curr_environment to a variable
		Environment temp = this.curr_environment; // main environment
		// System.out.println("Main environment(1): "+this.curr_environment.map);

		// 3. set this.curr_environment to the closure's environment
		this.curr_environment = closure.getEnviron();
		// System.out.println("Closure's outter environment: " +
		// this.curr_environment.map);

		// 4. push a new environment to the stack
		this.push_environ();
		// System.out.println("Closure's inner environment: " +
		// this.curr_environment.map);

		// 5. add all parameters-arguments pairs to that environment
		// System.out.println("STEP 5");
		// check for type mismatch before assignment

		String argument_type = "";
		String parameter_type = "";
		for (int i = 0; i < arguments.size(); i++) {
			// parameter, argument pair
			argument_type = arguments.get(i).name;
			parameter_type = closure.getTypes().get(i);
			if (parameter_type.equals("var")) {
				// yes, let's assign
			} else if (!parameter_type.equals(argument_type)) {
				System.out.println("parameter and argument type mismatch");
				System.out.println("argument_type:     " + argument_type);
				System.out.println("parameter_type:     " + parameter_type);
				throw new RuntimeException("runtime error: type mismatch");
			}
			this.curr_environment.map.put(closure.getParameters().get(i), arguments.get(i));
			this.curr_environment.type_map.put(closure.getParameters().get(i), closure.getTypes().get(i));
		}

		// 6. execute the body of the function

		// this is because closure's IR now has three children, with signature node
		// body is child2 now.

		if (closure.getHasSignature()) { // has three children
			// System.out.println("3");
			this.exec(closure.getIR().getChildren().get(2)); // execute(sequence)
		} else { // has 2 children
			// System.out.println("2");
			this.exec(closure.getIR().getChildren().get(1)); // execute(sequence)
		}

		// IR = function's body
		// same for Class

		// 7. after the function is done, set the current environment back
		// System.out.println("STEP 7");
		this.curr_environment = temp;

		// store this.returning + this.return value
		Value ret = this.return_value;
		// System.out.println("Return value: " + ret);
		String real_return_type = ret.name;

		// System.out.println("real return type:" + real_return_type);
		// System.out.println("expected return type:" + closure.getReturnType());

		if (closure.getReturnType().equals("var")) {
			// everything is fine
		} else if (!closure.getReturnType().equals(real_return_type)) {
			System.out.println("function return type mismatch");
			throw new RuntimeException("runtime error: type mismatch");
		}

		// reset return variables before returning
		this.returning = false;
		// should go back to where function is called
		this.return_value = new IntegerValue(0);
		// System.out.println("place 2: "+ this.n_th_function_call);
		this.n_th_function_call--;

		// System.out.println("place 3: "+ this.n_th_function_call);
		// System.out.println("return_value: "+ ret);
		// System.out.println("Main environment(2): "+ this.curr_environment.map);
		return ret;
	}

	// <TYPE>.
	// Errors only in 1)variable assignment 2)function return type

	// in eval_FUNCTION: loop through (signature), to know type for
	// parameters/arguments, and return type
	// add to type map in the closure.

	// in eval_function_CALL: type mismatch
	// when wrong type of argument(kinda assignment)
	// wrong type of return type

	// create a seperate map of <Variable, type>
	// add "var" for untyped variables as well
	// let it do anything if it is "var"
	// check before variable assignment/declaration, if value matches type

	// runtime error: type mismatch

}
