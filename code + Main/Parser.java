
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

// each Parse function returns a parse, either Integer or Statement parse

public class Parser {
	static Parse FAIL = new IntegerParse(0, 0);

	// the main function
	public Parse parse(String str, String term) {
		Parse parse = parse(str, 0, term);
		if (parse.equals(Parser.FAIL)) {
			System.out.println(str + "\nfail\n\n");
			return null;
		} else if (parse.getIndex() < str.length()) {
			System.out.println(str + "\n\n---------------------\nSUBSTRING LEFT: \n" + str.substring(parse.getIndex()));
			System.out.println(
					"\n--------------------------------------------------" + "\nindex parsed: " + parse.getIndex());
			System.out.println("\nstr length: " + str.length());
			System.out.println("\ndid not parse the entire string");
			return null;
		} else {
			return parse;
		}
	}

	private Parse parse(String str, int index, String term) {
		if (term.equals("integer")) {
			return this.parse_integer(str, index);
		} else if (term.equals("addition") || term.equals("subtraction") || term.equals("add_sub")
				|| term.equals("math")) {
			return this.parse_add_sub_expression(str, index);
		} else if (term.equals("parenthesis")) {
			return this.parse_parenthesis(str, index);
		} else if (term.equals("operand")) {
			return this.parse_operand(str, index);
		} else if (term.equals("opt_space")) {
			return this.parse_opt_space(str, index);
		} else if (term.equals("multiplication") || term.equals("division") || term.equals("mul_div")) {
			return this.parse_mul_div_expression(str, index);
		} else if (term.equals("req_space")) {
			return this.parse_req_space(str, index);
		} else if (term.equals("print")) {
			return this.parse_print_statement(str, index);
		} else if (term.equals("statement")) {
			return this.parse_statement(str, index);
		} else if (term.equals("expression")) {
			return this.parse_expression(str, index);
		} else if (term.equals("program")) {
			return this.parse_program(str, index);
		} else if (term.equals("space")) {
			return this.parse_space(str, index);
		} else if (term.equals("comment")) {
			return this.parse_comment(str, index);
		} else if (term.equals("identifier")) {
			return this.parse_identifier(str, index);
		} else if (term.equals("identifier_first_char")) {
			return this.parse_identifier_first_char(str, index);
		} else if (term.equals("identifier_char")) {
			return this.parse_identifier_char(str, index);
		} else if (term.equals("location")) {
			return this.parse_location(str, index);
		} else if (term.equals("declaration")) {
			return this.parse_declaration_statement(str, index);
		} else if (term.equals("assignment")) {
			return this.parse_assignment_statement(str, index);
		} else if (term.equals("expression_statement")) {
			return this.parse_expression_statement(str, index);
		} else if (term.equals("if_else_statement")) {
			return this.parse_if_else_statement(str, index);
		} else if (term.equals("if_statement")) {
			return this.parse_if_statement(str, index);
		} else if (term.equals("while_statement")) {
			return this.parse_while_statement(str, index);
		} else if (term.equals("or_expression")) {
			return this.parse_or_expression(str, index);
		} else if (term.equals("and_expression")) {
			return this.parse_and_expression(str, index);
		} else if (term.equals("optional_not_expression")) {
			return this.parse_optional_not_expression(str, index);
		} else if (term.equals("not_expression")) {
			return this.parse_not_expression(str, index);
		} else if (term.equals("comp_expression")) {
			return this.parse_comp_expression(str, index);
		} else if (term.equals("comp_operator")) {
			return this.parse_comp_operator(str, index);
		} else if (term.equals("location")) {
			return this.parse_location(str, index);
		} else if (term.equals("return_statement")) {
			return this.parse_return_statement(str, index);
		} else if (term.equals("function_call")) {
			return this.parse_function_call(str, index);
		} else if (term.equals("arguments")) {
			return this.parse_arguments(str, index);
		} else if (term.equals("function")) {
			return this.parse_function(str, index);
		} else if (term.equals("parameters")) {
			return this.parse_parameters(str, index);
		} else if (term.equals("class")) {
			return this.parse_class(str, index);
		} else if (term.equals("call_member_expression")) {
			return this.parse_call_member_expression(str, index);
		} else if (term.equals("call_member")) {
			return this.parse_call_member(str, index);
		} else if (term.equals("member")) {
			return this.parse_member(str, index);
		} else if (term.equals("type")) {
			return this.parse_type(str, index);
		} else if (term.equals("parameter")) {
			return this.parse_parameter(str, index);
		} else if (term.equals("return_type")) {
			return this.parse_return_type(str, index);
		} else {
			throw new AssertionError("Unexpected term " + term);
		}
	}

	// program = opt_space ( statement opt_space )*
	// return parse = sequence
	private Parse parse_program(String str, int index) {
		StatementParse sequence = new StatementParse("sequence", index);
		// opt_space
		Parse parse = this.parse(str, index, "opt_space");
		sequence.setIndex(parse.getIndex());
		index = parse.getIndex();

		// ( statement opt_space )*
		while (index < str.length() && !parse.equals(Parser.FAIL)) {
			// PARSE STATEMENT
			parse = this.parse(str, index, "statement");
			if (parse.equals(Parser.FAIL)) {
				break;
			}
			index = parse.getIndex();
			sequence.children.add(parse);
			// PARSE OPT_SPACE
			if (index >= str.length()) {
				break;
			}
			Parse parse2 = this.parse(str, parse.getIndex(), "opt_space");
			index = parse2.getIndex();
		}

		// System.out.println("index is " + index);
		sequence.setIndex(index);
		// System.out.println(sequence);
		return sequence;
	}

	// expression_statement
	private Parse parse_statement(String str, int index) {
		Parse parse = this.parse(str, index, "declaration");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		parse = this.parse(str, index, "assignment");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}

		parse = this.parse(str, index, "if_else_statement");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}

		parse = this.parse(str, index, "if_statement");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}

		parse = this.parse(str, index, "while_statement");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}

		parse = this.parse(str, index, "return_statement");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		// System.out.println(str + "not return statement" +"\n\n\n");

		parse = this.parse(str, index, "print");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}

		// System.out.println(str + "not print statement");
		// return parse if its an expression
		parse = this.parse(str, index, "expression_statement");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		// else fail
		return Parser.FAIL;
	}

	// "if" opt_space "(" opt_space expression opt_space ")" opt_space "{" opt_space
	// program opt_space "}" opt_space "else" opt_space "{" opt_space program
	// opt_space "}";
	private Parse parse_if_statement(String str, int index) {
		String strTemp = str.substring(index);
		if (!strTemp.startsWith("if")) {
			// System.out.println(str + "does not start with var");
			return Parser.FAIL;
		}
		index += 2;

		Parse space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		if (str.charAt(index) != '(') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// expression
		Parse lhs = this.parse(str, index, "expression");
		if (lhs.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = lhs.getIndex();

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// ")"
		if (str.charAt(index) != ')') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "{"
		if (str.charAt(index) != '{') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// program
		// find the substring between "{" and "}
		String t = "";
		int t_index = index;

		Stack<String> stack = new Stack<String>(); // stack to keep track of ‘}’
		stack.push("{");
		while (t_index < str.length()) {
			// System.out.println(str + " temp_index " +temp_index + " charAt(temp_index) "
			// + str.charAt(temp_index));

			if (str.charAt(t_index) == '{') {
				stack.push("{");
			}
			if (str.charAt(t_index) == '}') {
				stack.pop();
				if (stack.empty()) {
					break;
				}
			}
			t += str.charAt(t_index);
			t_index++;

		}
		if (!stack.empty()) {
			return Parser.FAIL;
		}
		// System.out.println(str + "\nprogram: " +t);

		Parse rhs = this.parse(t, 0, "program");
		if (rhs.equals(Parser.FAIL)) {
			// System.out.println(str + " program " + index);
			return Parser.FAIL;
		}
		index = t_index;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "}"
		if (str.charAt(index) != '}') {
			return Parser.FAIL;
		}
		index++;

		StatementParse parent = new StatementParse("if", index);
		parent.children.add(lhs);
		parent.children.add(rhs);
		lhs = parent;

		return lhs;

	}

	// "if" opt_space "(" opt_space expression opt_space ")" opt_space "{" opt_space
	// program opt_space "}"
	// opt_space "else" opt_space "{" opt_space program opt_space "}";
	private Parse parse_if_else_statement(String str, int index) {
		String strTemp = str.substring(index);

		// "if"
		if (!strTemp.startsWith("if")) {
			// System.out.println(str + "does not start with var");
			return Parser.FAIL;
		}
		index += 2;

		// opt_space
		Parse space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "("
		if (str.charAt(index) != '(') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// expression
		Parse lhs = this.parse(str, index, "expression");
		if (lhs.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = lhs.getIndex();

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// ")"
		if (str.charAt(index) != ')') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "{"
		if (str.charAt(index) != '{') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// program: the substring between "{" and "}
		String temp = "";
		int temp_index = index;

		Stack<String> stack = new Stack<String>(); // stack to keep track of ‘}’
		stack.push("{");
		while (temp_index < str.length()) {
			// System.out.println(str + " temp_index " +temp_index + " charAt(temp_index) "
			// + str.charAt(temp_index));
			// check for comment
			Parse comment = this.parse(str, temp_index, "comment");
			if (!comment.equals(Parser.FAIL)) {
				temp_index = comment.getIndex();
			}
			if (str.charAt(temp_index) == '{') {
				stack.push("{");
			}
			if (str.charAt(temp_index) == '}') {
				stack.pop();
				if (stack.empty()) {
					break;
				}
			}
			temp += str.charAt(temp_index);
			temp_index++;
		}
		if (!stack.empty()) {
			return Parser.FAIL;
		}

		Parse middle = this.parse(temp, 0, "program");
		if (middle.equals(Parser.FAIL)) {
			// System.out.println(str + " program fails" + index);
			return Parser.FAIL;
		}
		index = temp_index;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "}"
		if (str.charAt(index) != '}') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "else"
		String substring = str.substring(index);
		if (!substring.startsWith("else")) {
			return Parser.FAIL;
		}
		index += 4;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();
		// "{"
		if (str.charAt(index) != '{') {
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// program
		// find the substring between "{" and "}

		String t = "";
		int t_index = index;
		// use stack
		// push "{"s in
		// pop "{" out when there is "}"
		// break out loop when stack is empty

		while (t_index < str.length()) {
			// System.out.println(str + " temp_index: " + t_index + " charAt(temp_index): "
			// + str.charAt(t_index));
			// check for comment
			Parse comment = this.parse(str, t_index, "comment");
			if (!comment.equals(Parser.FAIL)) {
				t_index = comment.getIndex();
			}
			if (str.charAt(t_index) == '{') {
				stack.push("{");
			}
			if (str.charAt(t_index) == '}') {
				if (stack.empty()) {
					break;
				}
				stack.pop();
			}
			t += str.charAt(t_index);
			t_index++;
		}

		if (!stack.empty()) {
			// System.out.println("stack is not cleared");
			return Parser.FAIL;
		}
		// System.out.println(t);
		Parse rhs = this.parse(t, 0, "program");
		if (rhs.equals(Parser.FAIL)) {
			// System.out.println(str + " program " + index);
			return Parser.FAIL;
		}
		index = t_index;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "}"
		if (str.charAt(index) != '}') {
			return Parser.FAIL;
		}
		index++;

		StatementParse parent = new StatementParse("ifelse", index);
		parent.children.add(lhs);
		parent.children.add(middle);
		parent.children.add(rhs);

		lhs = parent;

		return lhs;
	}

	// "while" opt_space "(" opt_space expression opt_space ")" opt_space "{"
	// opt_space program opt_space "}";
	private Parse parse_while_statement(String str, int index) {
		// System.out.println(str + " index " + index);
		String strTemp = str.substring(index);
		if (!strTemp.startsWith("while")) {
			return Parser.FAIL;
		}
		index += 5;

		Parse space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println(str + " opt_space " + index);
			return Parser.FAIL;
		}
		index = space.getIndex();

		if (str.charAt(index) != '(') {
			// System.out.println(str + " ( " + index);
			return Parser.FAIL;
		}
		index++;

		// expression
		Parse lhs = this.parse(str, index, "expression");
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println(str + " expression " + index);
			return Parser.FAIL;
		}
		index = lhs.getIndex();

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println(str + " opt_space " + index);
			return Parser.FAIL;
		}
		index = space.getIndex();

		// ")"
		if (str.charAt(index) != ')') {
			// System.out.println(str + " ) " + index);
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println(str + " opt_space " + index);
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "{"
		if (str.charAt(index) != '{') {
			// System.out.println(str + " { " + index);
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println(str + " opt_space " + index);
			return Parser.FAIL;
		}
		index = space.getIndex();

		// program
		// find the substring between "{" and "}
		String temp = "";
		int temp_index = index;
		// use stack
		// push "{"s in
		// pop "{" out when there is "}"
		// break out loop when stack is empty
		Stack<String> stack = new Stack<String>(); // stack to keep track of ‘}’
		stack.push("{");
		while (temp_index < str.length()) {
			// check for comment
			Parse comment = this.parse(str, temp_index, "comment");
			if (!comment.equals(Parser.FAIL)) {
				temp_index = comment.getIndex();
			}

			if (str.charAt(temp_index) == '{') {
				stack.push("{");
			}
			if (str.charAt(temp_index) == '}') {

				stack.pop();
				if (stack.empty()) {
					break;
				}
			}
			temp += str.charAt(temp_index);
			temp_index++;
		}
		if (!stack.empty()) {
			return Parser.FAIL;
		}
		// System.out.println(temp);
		// temp = program
		Parse rhs = this.parse(temp, 0, "program");
		if (rhs.equals(Parser.FAIL)) {
			// System.out.println("\n" + str + " while, program " + index);
			// System.out.println("\n" + str.substring(index));
			return Parser.FAIL;
		}
		index = temp_index;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println(str + " opt_space " + index);
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "}"
		if (str.charAt(index) != '}') {
			// System.out.println(str + " } " + index);
			return Parser.FAIL;
		}
		index++;
		// System.out.println("lhs "+ lhs.toString());
		// System.out.println("rhs "+ rhs.toString());
		StatementParse parent = new StatementParse("while", index);
		// System.out.println(parent.name);
		// System.out.println(str + "\n\n" + parent.children);
		// System.out.println(parent.toString());
		// System.out.println("lhs "+ lhs.toString());
		// System.out.println("rhs "+ rhs.toString());
		parent.children.add(lhs);
		parent.children.add(rhs);
		lhs = parent;

		return lhs;
	}

	// GRAMMAR: "ret" req_space expression opt_space ";";
	// TREE STRUCTURE:
	// parent node: "ret" StatementParse
	// child node: Parse returned by parse_expression
	private Parse parse_return_statement(String str, int index) {
		// "ret"
		String strTemp = str.substring(index);
		// System.out.println(index);
		// System.out.println("strTemp: >"+strTemp +"\n\n\n");
		if (!strTemp.startsWith("ret")) {
			return Parser.FAIL;
		}
		index += 3;
		// req_space
		Parse parse = this.parse(str, index, "req_space");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = parse.getIndex();
		// expression
		Parse expression = this.parse(str, index, "expression");
		// System.out.println(expression);
		if (expression.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = expression.getIndex();
		// opt_space
		Parse space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = space.getIndex();
		// ";"
		if (str.charAt(index) != ';') {
			return Parser.FAIL;
		}
		index++;
		// set child
		StatementParse ret = new StatementParse("return", parse.getIndex());
		ret.children.add(expression);
		// System.out.println(index);
		// System.out.println(ret);
		ret.setIndex(index);
		return ret;
	}

	// "var" req_space assignment_statement (old version)

	// "var" req_space identifier opt_space "=" opt_space expression opt_space ";";
	// (current version)

	// parent: declare StatementParse
	// child1: parse returned by identifier, without "lookup"
	// child2: parse returned by expression
	private Parse parse_declaration_statement(String str, int index) {
		StatementParse declare = new StatementParse("declare", index);
		String strTemp = str.substring(index);

		int temp_index = 0;
		// index is not 0
		// System.out.println("\n---place1----\n" + strTemp.substring(0));

		Parse type = this.parse(strTemp, temp_index, "type");
		if (type.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		temp_index += type.getIndex();
		// add child only if type isn't "var"
		if (!type.name.equals("var")) {
			declare.children.add(type);
		}

		// System.out.println("\n---place_var----\n" + strTemp.substring(temp_index));
		type = this.parse(strTemp, temp_index, "req_space");
		if (type.equals(Parser.FAIL)) {
			// System.out.println("no req space");
			return Parser.FAIL;
		}
		temp_index = type.getIndex();
		// System.out.println("\n---place_req_space----\n" +
		// strTemp.substring(temp_index));
		// identifier opt_space "=" opt_space expression opt_space ";"
		Parse lhs = this.parse(strTemp, temp_index, "identifier"); // its child
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println("identifier fails");
			return Parser.FAIL;
		}
		// System.out.println("lhs "+ lhs);
		lhs = lhs.getChildren().get(0);
		temp_index = lhs.getIndex();
		// System.out.println("\n---place_identifier----\n" +
		// strTemp.substring(temp_index));

		Parse dummy = this.parse(strTemp, temp_index, "opt_space");
		if (dummy.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		temp_index = dummy.getIndex();
		// System.out.println("\n---place_opt_space----\n" +
		// strTemp.substring(temp_index));

		if (strTemp.charAt(temp_index) != '=') {
			// System.out.println("missing =");
			return Parser.FAIL;
		}
		temp_index++;
		// System.out.println("\n---place_==----\n" + strTemp.substring(temp_index));

		dummy = this.parse(strTemp, temp_index, "opt_space");
		if (dummy.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		temp_index = dummy.getIndex();
		// System.out.println("\n---place_opt_space_expression----\n" +
		// strTemp.substring(temp_index));

		Parse rhs = this.parse(strTemp, temp_index, "expression");
		if (rhs.equals(Parser.FAIL)) {
			// System.out.println("expression fails");
			return Parser.FAIL;
		}
		temp_index = rhs.getIndex();

		// System.out.println("\n---place_after_expression----\n" +
		// strTemp.substring(temp_index));
		// System.out.println("rhs: " +rhs);

		dummy = this.parse(strTemp, temp_index, "opt_space");
		if (rhs.equals(Parser.FAIL)) {
			// System.out.println("opt_space fails");
			return Parser.FAIL;
		}
		temp_index = dummy.getIndex();
		// System.out.println("\n---place_opt_space_before;----\n" +
		// strTemp.substring(temp_index));

		if (strTemp.charAt(temp_index) != ';') {
			// System.out.println(str.substring(index));
			// System.out.println("MISSING ;\n--------------------");
			return Parser.FAIL;
		}
		temp_index++;
		index += temp_index;

		// System.out.println("\n---place_after;strTemp----\n" +
		// strTemp.substring(temp_index));
		// System.out.println("\n---place_after;str----\n" + str.substring(index));
		// System.out.println("\n---place_after;temp_index----\n" + temp_index);

		declare.setIndex(index);
		// System.out.println("lhs before return: "+ lhs);
		// System.out.println("rhs before return: "+ rhs);
		declare.children.add(lhs);
		declare.children.add(rhs);
		return declare;
	}

	// location opt_space "=" opt_space expression opt_space ";"
	private Parse parse_assignment_statement(String str, int index) {
		Parse parse = this.parse(str, index, "location");
		if (parse.equals(Parser.FAIL)) {
			// System.out.println("location fails: " + str.substring(index));
			return Parser.FAIL;
		}
		index = parse.getIndex();
		// System.out.println(str + "assign"+ index); // index should be 5 here
		// parse = "lookup- > identifier"
		if (parse.name.equals("lookup")) {
			parse.name = "varloc";
		}
		Parse parse2 = this.parse(str, index, "opt_space");
		if (parse2.equals(Parser.FAIL)) {
			// System.out.println(str + "opt space fails");
			return Parser.FAIL;
		}
		index = parse2.getIndex();
		parse.setIndex(index);

		// System.out.println(str + "assign"+ index); // index should be 6 here

		if (str.charAt(index) != '=') {
			// System.out.println("missing =" + str.substring(index));
			return Parser.FAIL;
		}
		index++; // '=' takes one index
		parse.setIndex(index);
		// System.out.println(str + "assign"+ index); // index should be 7 here after
		// '='

		parse2 = this.parse(str, index, "opt_space");
		index = parse2.getIndex();
		// parse.setIndex(index);

		Parse parse_r = this.parse(str, index, "expression");
		if (parse_r.equals(Parser.FAIL)) {
			// System.out.println(str + "expression fails in parse_assignment_statement");
			return Parser.FAIL;
		}

		index = parse_r.getIndex();
		// System.out.println(str + "assign "+"expression" +index); // index should be 9
		// here

		parse2 = this.parse(str, index, "opt_space");
		index = parse2.getIndex();

		// System.out.println(str + "assign "+" after last opt space" +index); // index
		// should be 9 here
		parse_r.setIndex(index);
		// System.out.println(str + ", " + index);
		// already fails here

		if (index >= str.length() || index < 0 || str.charAt(index) != ';') {
			// System.out.println("Missing ;" + str.substring(index));
			return Parser.FAIL;
		}
		index++; // ';' takes one index
		parse_r.setIndex(index);

		// System.out.println(str + "assign"+ index);//before ';', should be 9

		StatementParse assign = new StatementParse("assign", index);
		// System.out.println(str + "assign"+ index); // after ';', should be 10
		assign.children.add(parse);
		assign.children.add(parse_r);
		return assign;
	}

	// original: location = identifier;
	// location = identifier ( "." identifier )*;

	// parent: member -> memloc
	// child 1: lhs
	// child 2: parses from new identifier
	private Parse parse_location(String str, int index) {
		// return parse if identifier works
		Parse lhs = this.parse(str, index, "identifier");
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println("identifier fails in parse_location" + "
			// str.substring(index): " +str.substring(index));
			return Parser.FAIL;
		}
		index = lhs.getIndex();
		// else, convert node to varloc node
		if (lhs.name.equals("lookup")) {
			lhs.name = "varloc";
		}

		StatementParse parent = new StatementParse("memloc", index);
		Parse rhs = new Parse("", index);
		// ( "." identifier )*;
		while (index < str.length() && !rhs.equals(Parser.FAIL)) {
			// "."
			if (str.charAt(index) != '.') {
				break;
			}
			index++;

			// identifier
			rhs = this.parse(str, index, "identifier");
			if (rhs.equals(Parser.FAIL)) {
				// System.out.println("inner identifier fails in parse_location " +
				// str.substring(index));
				break;
			}
			rhs = rhs.getChildren().get(0);
			// System.out.println(rhs);
			index = rhs.getIndex();
			parent.children.add(lhs);
			parent.children.add(rhs);
			lhs = parent;
			lhs.setIndex(index);
		}
		return lhs;
	}

	// type = "func" || "int" || "var";
	private Parse parse_type(String str, int index) {
		String parsed = "";
		if (((index + 3 < str.length()) && (str.substring(index, index + 4).equals("func")))) {
			parsed += str.substring(index, index + 4);
			// System.out.println(parsed);
			index += 4;
			// System.out.println(index + " func");
		} else if ((index + 2 < str.length()) && ((str.substring(index, index + 3).equals("var")))) {
			parsed += str.substring(index, index + 3);
			// System.out.println(parsed);
			index += 3;
			// System.out.println(index + " var || int");
		} else if ((index + 2 < str.length()) && ((str.substring(index, index + 3).equals("int")))) {
			parsed += str.substring(index, index + 3);
			// System.out.println(parsed);
			index += 3;
		} else {
			return Parser.FAIL;
		}
		VariableParse result = new VariableParse(parsed, index);
		// System.out.println(result);
		return result;
	}

	// print_statement: "print" req_space expression opt_space ";"
	private Parse parse_print_statement(String str, int index) {
		// Check to make sure the word is print otherwise it should fail
		// Gets the string to start at the index (for the purpose of program
		String strTemp = str.substring(index);
		// Creates a variable with the original index
		int indexTemp = index;
		if (strTemp.startsWith("print")) {
			// check for req_space with index of 5 for the word "print"
			Parse parse = this.parse(strTemp, 5, "req_space");
			// if theres no space, the parse fails
			if (parse.equals(Parser.FAIL)) {
				// System.out.println(str + "fails at print-req_space");
				return Parser.FAIL;
			}
			// then check for the expression
			// index after req_space should be 14
			// now 7

			// System.out.println(str + " index after req space: " +parse.getIndex());

			// req_space did not count the index for comments

			parse = this.parse(strTemp, parse.getIndex(), "expression");
			if (parse.equals(Parser.FAIL)) {
				// System.out.println(str.substring(index));
				// System.out.println("fails at print- expression");
				return Parser.FAIL;
			}
			// System.out.println("result is " + parse);
			// I forgot that opt_space always returned a result of 0

			// then checks for the opt_space
			// index before opt_space
			// index = parse.getIndex();

			// int result = parse.getValue();

			// System.out.println(parse.getIndex());
			index = parse.getIndex();
			Parse parse2 = this.parse(strTemp, parse.getIndex(), "opt_space");
			// Fail if there is no ; at the end
			// I got stuck here and i realized its because the string wasn't long enough
			// to check for another index
			// System.out.println(str.length());
			// System.out.println(index);

			if (strTemp.length() <= parse2.getIndex()) {
				// System.out.println(str + "fails at print-strTemp.length() <= index");
				return Parser.FAIL;
			} else if (strTemp.charAt(parse2.getIndex()) != ';') {
				// System.out.println(str + parse2.getIndex());
				// System.out.println(str + "fails at print-not end with ;");
				return Parser.FAIL;
			}
			index = parse2.getIndex();
			// add one to the index for semi colon
			// And add the original index back into print
			index += indexTemp + 1;
			// System.out.println(new Parse(result, index));
			parse.setIndex(index);
			StatementParse print = new StatementParse("print", index);
			print.children.add(parse);
			return print;
		} else {
			// System.out.println(str + "fails at print statement doesn't start with
			// print");
			return Parser.FAIL;
		}
	}

	// expression_statement = expression opt_space ";";
	private Parse parse_expression_statement(String str, int index) {
		Parse parse = this.parse(str, index, "expression");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = parse.getIndex();

		Parse parse2 = this.parse(str, index, "opt_space");
		if (parse2.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}

		index = parse2.getIndex();
		if (index >= str.length()) {
			return Parser.FAIL;
		}

		if (str.charAt(index) != ';') {
			// System.out.println(";;;;;;;;;;;;;;;;;;\n" + str.substring(index) +
			// "\n-----------------\n");
			// System.out.println(";");
			return Parser.FAIL;
		}

		index++; // ';' takes one index
		parse.setIndex(index);
		return parse;
	}

	// expression = add_sub_expression
	private Parse parse_expression(String str, int index) {
		Parse parse = this.parse(str, index, "or_expression");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		// not equals parser.fail
		index = parse.getIndex();
		// System.out.println(" \n" + str + "\nindex: " + index);
		return parse;
	}

	// and_expression ( opt_space "||" opt_space and_expression )*;
	private Parse parse_or_expression(String str, int index) {
		Parse lhs = this.parse(str, index, "and_expression");
		if (lhs.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = lhs.getIndex();

		// (opt_space ("*" | "/") opt_space operand? )*
		while (index < str.length() && !lhs.equals(Parser.FAIL)) {
			// 1st opt_space
			Parse parent = this.parse(str, index, "opt_space");

			if (parent.getIndex() >= str.length()) {
				parent = Parser.FAIL;
				break;
			}
			index = parent.getIndex();
			// "||"
			if (index + 1 >= str.length() || !str.substring(index, index + 2).equals("||")) {
				// System.out.println(str + "\n no || in or expression");
				// System.out.println(str + "\n"+ index + ":" + str.charAt(index));
				parent = Parser.FAIL;
				break;
			}

			// 2nd opt space
			parent.setIndex(this.parse(str, index + 2, "opt_space").getIndex());

			// optional_not_expression
			Parse rhs = this.parse(str, parent.getIndex(), "and_expression");

			// ((0 == (!1)) && (0+1))

			if (rhs.equals(Parser.FAIL)) {
				// System.out.println(str + "\n second add expression fails in or expression");
				// System.out.println(str + "\n"+ index + ":" + str.charAt(index));
				rhs = Parser.FAIL;
				break;
			}

			// create the mul/div node
			StatementParse or = new StatementParse("||", rhs.getIndex());
			or.children.add(lhs);
			or.children.add(rhs);
			lhs = or;

			index = rhs.getIndex();
			lhs.setIndex(index);
		}
		return lhs;
	}

	// optional_not_expression ( opt_space "&&" opt_space optional_not_expression)*;
	private Parse parse_and_expression(String str, int index) {

		// 0 == (! 1) && 0+1

		Parse lhs = this.parse(str, index, "optional_not_expression");
		// System.out.println(str + "\n and expression-optional_not_expression ");
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println(" \n" + str + "\ncomp_expression -- addsub");
			// System.out.println(" \n" + str + "\n" + index + ": "+ str.charAt(index));
			return Parser.FAIL;
		}
		index = lhs.getIndex();

		// (opt_space "&&" opt_space optional_not_expression )*
		while (index < str.length() && !lhs.equals(Parser.FAIL)) {
			// 1st opt_space
			Parse parent = this.parse(str, index, "opt_space");
			if (parent.getIndex() >= str.length()) {
				parent = Parser.FAIL;
				break;
			}
			index = parent.getIndex();

			// "&&"
			// System.out.println(" \n" + str + "\n" + index);
			if (index + 1 >= str.length() || !str.substring(index, index + 2).equals("&&")) {
				parent = Parser.FAIL;
				break;
			}

			// 2nd opt space
			parent.setIndex(this.parse(str, index + 2, "opt_space").getIndex());

			// optional_not_expression
			Parse rhs = this.parse(str, parent.getIndex(), "optional_not_expression");
			if (rhs.equals(Parser.FAIL)) {
				rhs = Parser.FAIL;
				break;
			}

			// create && node
			StatementParse and = new StatementParse("&&", rhs.getIndex());
			and.children.add(lhs);
			and.children.add(rhs);
			lhs = and;
			index = lhs.getIndex();
		}
		return lhs;
	}

	// comp_expression| not_expression;
	private Parse parse_optional_not_expression(String str, int index) {
		// System.out.println(str + " index " + index);
		Parse parse = this.parse(str, index, "comp_expression");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		// System.out.println(str + " not comp_expression");
		parse = this.parse(str, index, "not_expression");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		return Parser.FAIL;
	}

	private Parse parse_not_expression(String str, int index) {
		if (index > str.length() || str.charAt(index) != '!') {
			return Parser.FAIL;
		} else {
			index++;
			StatementParse exclamation = new StatementParse("!", index);

			exclamation.setIndex(this.parse(str, exclamation.getIndex(), "opt_space").getIndex());

			Parse child = this.parse(str, index, "comp_expression");
			if (child.equals(Parser.FAIL)) {
				return Parser.FAIL;
			}
			index = child.getIndex();
			exclamation.setIndex(index);
			exclamation.children.add(child);

			return exclamation;
		}
	}

	// 0 == 0
	// add_sub_expression (opt_space comp_operator opt_space add_sub_expression)?;
	private Parse parse_comp_expression(String str, int index) {

		// add_sub_expression
		Parse lhs = this.parse(str, index, "add_sub");
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println(" \n" + str + "lhs fails");
			// System.out.println(" \n" + str + "\n" + index + ": "+ str.charAt(index));
			return Parser.FAIL;
		}
		index = lhs.getIndex();
		int count = 0;
		// (opt_space comp_operator opt_space add_sub_expression)?
		while (index < str.length() && !lhs.equals(Parser.FAIL) && count < 1) {
			// opt_space
			Parse opt_space = this.parse(str, index, "opt_space");
			if (opt_space.getIndex() >= str.length()) {
				opt_space = Parser.FAIL;
				break;
			}
			// comp_operator as parent
			index = opt_space.getIndex();
			Parse comp = this.parse(str, index, "comp_operator"); // returns StatementParse
			if (comp.equals(Parser.FAIL)) {
				comp = Parser.FAIL;
				break;
			}
			// System.out.println(comp);

			// opt_space
			comp.setIndex(this.parse(str, comp.getIndex(), "opt_space").getIndex());
			// System.out.println(str + " index "+ comp.getIndex());

			// add_sub
			Parse rhs = this.parse(str, comp.getIndex(), "add_sub");
			if (rhs.equals(Parser.FAIL)) {
				rhs = Parser.FAIL;
				break;
			}
			// System.out.println(" \n" + str + "\n rhs: "+ rhs);
			comp.setIndex(rhs.getIndex());

			// tree structure
			if (!opt_space.equals(Parser.FAIL) && !comp.equals(Parser.FAIL) && !rhs.equals(Parser.FAIL)) {
				StatementParse parent = new StatementParse(comp.name, comp.getIndex());
				parent.children.add(lhs);
				parent.children.add(rhs);
				lhs = parent;
				index = lhs.getIndex();
			}
			// System.out.println(str + "\n" + lhs);
			count++;
		}
		return lhs;
	}

	// "=="||"!="||"<="||">="|| "<"|| ">";
	private Parse parse_comp_operator(String str, int index) {
		String parsed = "";
		if (((index + 1 < str.length()) && (str.substring(index, index + 2).equals("==")
				|| str.substring(index, index + 2).equals("!=") || str.substring(index, index + 2).equals("<=")
				|| str.substring(index, index + 2).equals(">=")))) {
			parsed += str.substring(index, index + 2);
			index += 2;
		} else if ((index < str.length()) && (str.charAt(index) == '<') || (str.charAt(index) == '>')) {
			parsed += str.charAt(index);
			index++;
		} else {
			// System.out.println(str + "\n " + index + ": " + str.charAt(index));
			// System.out.println("\n\n"+ str + "\n fails comp_operator");
			return Parser.FAIL;
		}
		return new StatementParse(parsed, index);
	}

	// add_sub_expression = mul_div_expression ( opt_space ("+"|"-") opt_space
	// mul_div_expression )*;
	private Parse parse_add_sub_expression(String str, int index) {
		// mul_div_expression
		// System.out.println(str + " index "+ index);
		Parse lhs = this.parse(str, index, "multiplication");
		if (lhs.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = lhs.getIndex();

		// System.out.println(str + "\nindex: "+ index);
		while (index < str.length() && !lhs.equals(Parser.FAIL)) {
			// 1st opt_space

			Parse rhs = this.parse(str, index, "opt_space");

			if (rhs.getIndex() >= str.length()) {
				rhs = Parser.FAIL;
				break;
			}

			// "+" | "-"
			String operation = "";
			if (str.charAt(rhs.getIndex()) == '+') {
				operation = "add";
			} else if (str.charAt(rhs.getIndex()) == '-') {
				operation = "sub";
			} else {
				rhs = Parser.FAIL;
				break;
			}

			// 2nd opt_space
			rhs.setIndex(this.parse(str, rhs.getIndex() + 1, "opt_space").getIndex());

			// operand?
			// 0 operand
			if (rhs.getIndex() >= str.length() || rhs.getIndex() < 0) {
				break;
			}

			// mul_div_expression as rhs node
			// parse = this.parse(str, parse.getIndex(), "operand"); // out of range
			rhs = this.parse(str, rhs.getIndex(), "multiplication");
			if (rhs.equals(Parser.FAIL)) {
				// System.out.println("not an operand");
				rhs = Parser.FAIL; // Im not sure we need this statement, if parse already equals
				break; // Parser.FAIL we shouldnt need to set it equal again.
			}

			// create the plus node
			if (operation.equals("add")) {
				StatementParse parent = new StatementParse("+", rhs.index);
				parent.children.add(lhs);
				parent.children.add(rhs);
				lhs = parent;
			} else {
				StatementParse parent = new StatementParse("-", rhs.index);
				parent.children.add(lhs);
				parent.children.add(rhs);
				lhs = parent;
			}
			index = lhs.getIndex();

		}
		// System.out.println(str + "\nindex: "+ index);
		// return lhs;
		return lhs;
	}

	// opt_space call_expression ( opt_space ("*" | "/") opt_space
	// call_expression?)*

	private Parse parse_mul_div_expression(String str, int index) {
		// opt_space
		Parse lhs = this.parse(str, index, "opt_space");
		index = lhs.getIndex();

		// call_expression
		lhs = this.parse(str, index, "call_member_expression");
		if (lhs.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}

		// function(parameters 1 2 3 ... 11)

		index = lhs.getIndex();

		// (opt_space ("*" | "/") opt_space call_expression? )*
		while (index < str.length() && !lhs.equals(Parser.FAIL)) {
			// 1st opt_space
			Parse rhs = this.parse(str, index, "opt_space");

			if (rhs.getIndex() >= str.length()) {
				rhs = Parser.FAIL;
				break;
			}

			// "*" | "/"
			String operation = "";
			if (str.charAt(rhs.getIndex()) == '*') {
				operation = "mul";
			} else if (str.charAt(rhs.getIndex()) == '/') {
				operation = "div";
			} else {
				rhs = Parser.FAIL;
				break;
			}

			// 2nd opt space
			// update index again
			rhs.setIndex(this.parse(str, rhs.getIndex() + 1, "opt_space").getIndex());
			// operand?

			rhs = this.parse(str, rhs.getIndex(), "call_member_expression");
			if (rhs.equals(Parser.FAIL)) {
				// System.out.println("fails in mul div_call_expression");
				rhs = Parser.FAIL;
				break;
			}
			// create the mul/div node
			if (operation.equals("mul")) {
				StatementParse parent = new StatementParse("*", rhs.index);
				parent.children.add(lhs);
				parent.children.add(rhs);
				lhs = parent;
			} else {
				StatementParse parent = new StatementParse("/", rhs.index);
				parent.children.add(lhs);
				parent.children.add(rhs);
				lhs = parent;
			}
			index = lhs.getIndex();
		}
		// System.out.println(str + "\nmul_div_loop_index: " + index);
		return lhs;
	}

	// operand (opt_space function_call )*; (old version)
	// operand ( opt_space call_member)*; (current version)
	// TREE STRUCTURE:
	// parent node: "call" StatementParse/ "member" StatementParse
	// lhs child: Parse returned by operand
	// rhs child(s): Parse returned by function_call/member: arguments/identifiers
	private Parse parse_call_member_expression(String str, int index) {
		// operand
		Parse lhs = this.parse(str, index, "operand");
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println("operand fails. " + str.substring(index));
			return Parser.FAIL;
		}
		index = lhs.getIndex();
		// (opt_space function_call)*;
		while (index < str.length() && !lhs.equals(Parser.FAIL)) {
			// opt_space
			Parse parent = this.parse(str, index, "opt_space");

			if (parent.getIndex() >= str.length()) {
				parent = Parser.FAIL;
				break;
			}
			index = parent.getIndex();

			// function_call (old version)
			// call_member (current version)
			Parse rhs = this.parse(str, index, "call_member");
			if (rhs.equals(Parser.FAIL)) {
				// System.out.println("function call fails" + str.substring(index));
				rhs = Parser.FAIL;
				break;
			}

			index = rhs.getIndex();
			// create the "call" node
			// or "member" node if VariableParse
			StatementParse member_or_call = new StatementParse("", index);
			if (rhs instanceof VariableParse) {
				member_or_call = new StatementParse("member", index);
			} else {
				member_or_call = new StatementParse("call", index);
			}
			member_or_call.children.add(lhs);
			member_or_call.children.add(rhs);
			lhs = member_or_call;
		}
		return lhs;
	}

	private Parse parse_call_member(String str, int index) {
		Parse parse = this.parse(str, index, "function_call");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		parse = this.parse(str, index, "member");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}

		return Parser.FAIL;
	}

	// member = "." opt_space identifier;
	// identifier: "lookup" StatementParse - identifier_name VariableParse
	// return parse returned by identifier.children.(0)
	private Parse parse_member(String str, int index) {
		if (str.charAt(index) != '.') {
			return Parser.FAIL;
		} else {
			index++;
			// opt_space
			Parse space = this.parse(str, index, "opt_space");
			index = space.getIndex();

			// identifier
			String identifier_name = "";
			Parse parse = this.parse(str, index, "identifier");
			if (parse.equals(Parser.FAIL)) {
				return Parser.FAIL;
			}
			identifier_name += parse.toString().substring(8, parse.toString().length() - 1);
			index = parse.getIndex();
			VariableParse identifier = new VariableParse(identifier_name, index);
			return identifier;
		}
	}

	// GRAMMAR: "(" opt_space arguments opt_space ")";
	// return Parse returned by parse_arguments
	private Parse parse_function_call(String str, int index) {
		if (str.charAt(index) != '(') {
			// System.out.println("(");
			return Parser.FAIL;
		}
		// opt_space
		Parse parse = this.parse(str, index, "opt_space");
		index = parse.getIndex();

		// arguments
		parse = this.parse(str, index + 1, "arguments");
		// arguments fail
		if (parse.equals(Parser.FAIL)) {
			System.out.println("arguments");
			return Parser.FAIL;
		}

		// opt_space
		parse.setIndex(this.parse(str, parse.getIndex(), "opt_space").getIndex());

		if (str.charAt(parse.getIndex()) != ')') {
			System.out.println(")");
			return Parser.FAIL;
		}

		parse.setIndex(parse.getIndex() + 1);
		return parse;
	}

	// GRAMMAR: arguments = (expression opt_space ( "," opt_space expression
	// opt_space )* )?;
	// TREE STRUCTURE:
	// parent node: "arguments" (StatementParse)
	// child(s): argument(s) (IntegerParse/StatementParse)
	private Parse parse_arguments(String str, int index) {
		// ( )?
		int count = 0;
		// build arguments node
		StatementParse arguments = new StatementParse("arguments", index);
		while (index < str.length() && count < 1) {
			// expression
			Parse expression = this.parse(str, index, "expression");
			if (expression.equals(Parser.FAIL)) {
				break;
			}
			index = expression.getIndex();

			// opt_space
			Parse space = this.parse(str, index, "opt_space");
			if (space.equals(Parser.FAIL)) {
				return Parser.FAIL;
			}
			index = space.getIndex();

			// set child
			// IntegerParse argument = new IntegerParse(expression.getValue(), index);
			arguments.children.add(expression);

			// ( "," opt_space expression opt_space )*
			while (index < str.length() && !expression.equals(Parser.FAIL)) {
				// ","
				if (str.charAt(index) != ',') {
					break;
				}
				index++;
				Boolean comma = true;

				// opt_space
				space = this.parse(str, index, "opt_space");
				if (space.equals(Parser.FAIL)) {
					if (comma) {
						return Parser.FAIL;
					}
					break;
				}
				index = space.getIndex();

				// expression
				expression = this.parse(str, index, "expression");
				if (expression.equals(Parser.FAIL)) {
					break;
				}
				index = expression.getIndex();

				// opt_space
				space = this.parse(str, index, "opt_space");
				if (space.equals(Parser.FAIL)) {
					break;
				}
				index = space.getIndex();

				// set child
				// argument = new IntegerParse(expression.getValue(), index);
				arguments.children.add(expression);
			}
			count++;
		}
		arguments.setIndex(index);
		return arguments;

	}

	// operand = integer | paren | identifier
	private Parse parse_operand(String str, int index) {
		// System.out.println(index);
		// System.out.println(str);
		Parse parse = this.parse(str, index, "class");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		parse = this.parse(str, index, "parenthesis");
		if (!parse.equals(Parser.FAIL)) {
			// System.out.println(str.substring(index) + "paren");
			return parse;
		}
		// System.out.println(str.substring(index) + "not paren");
		parse = this.parse(str, index, "function");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		// System.out.println("function fails");

		parse = this.parse(str, index, "identifier");
		// System.out.println(str.substring(index));
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		// System.out.println("not identifier");

		parse = this.parse(str, index, "integer");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		return Parser.FAIL;
	}

	// class: "class" opt_space "{" ( opt_space declaration_statement )* opt_space
	// "}";
	private Parse parse_class(String str, int index) {
		// "class"
		StatementParse class_lang = new StatementParse("class", index);
		String strTemp = str.substring(index);
		if (!strTemp.startsWith("class")) {
			return Parser.FAIL;
		}
		index += 5;

		// opt_space
		Parse parse = this.parse(str, index, "opt_space");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = parse.getIndex();

		// "{"
		if (str.charAt(index) != '{') {
			return Parser.FAIL;
		}
		index++;

		// find the substring between "{" and "}
		String t = "";
		int t_index = index;

		Stack<String> stack = new Stack<String>(); // stack to keep track of ‘}’
		stack.push("{");
		while (t_index < str.length()) {
			// System.out.println(str + " temp_index " +temp_index + " charAt(temp_index) "
			// + str.charAt(temp_index));
			if (str.charAt(t_index) == '{') {
				stack.push("{");
			}
			if (str.charAt(t_index) == '}') {
				stack.pop();
				if (stack.empty()) {
					break;
				}
			}
			t += str.charAt(t_index);
			t_index++;
		}
		if (!stack.empty()) {
			return Parser.FAIL;
		}

		// t <- [index, t_index]
		// substr_idx <- [0, t_index-index]
		// t = ( opt_space declaration_statement )* opt_space
		int substr_idx = 0;
		while (substr_idx < (t_index - index) && !parse.equals(Parser.FAIL)) {
			// PARSE OPT_SPACE
			Parse space = this.parse(t, substr_idx, "opt_space");
			substr_idx = space.getIndex();

			// PARSE declaration_statement
			parse = this.parse(t, substr_idx, "declaration");
			if (parse.equals(Parser.FAIL)) {
				break;
			}
			substr_idx = parse.getIndex();
			class_lang.children.add(parse);

			// opt_space
			parse = this.parse(t, substr_idx, "opt_space");
			if (parse.equals(Parser.FAIL)) {
				return Parser.FAIL;
			}
			substr_idx = parse.getIndex();
		}
		index += substr_idx;

		// opt_space
		parse = this.parse(str, index, "opt_space");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = parse.getIndex();

		// "}"
		if (str.charAt(index) != '}') {
			return Parser.FAIL;
		}
		index++;
		class_lang.setIndex(index);
		return class_lang;
	}

	// paren = "(" opt_space expression opt_space ")";
	private Parse parse_parenthesis(String str, int index) {
		// System.out.println("parenthesis index: " + index);
		// System.out.println("\nSUBSTRING: "+str.substring(index)+"\n");
		// System.out.println(index);
		// System.out.println(str.length());
		if (index > str.length() || str.charAt(index) != '(') {
			return Parser.FAIL;
		}
		index++;
		// opt_space
		Parse parse = this.parse(str, index, "opt_space");
		index = parse.getIndex();

		// expression
		parse = this.parse(str, index, "expression");
		if (parse.equals(Parser.FAIL)) {
			// System.out.println("Expression Fails");
			return Parser.FAIL;
		}
		// System.out.println("EXPRESSION");

		// opt_space
		parse.setIndex(this.parse(str, parse.getIndex(), "opt_space").getIndex());

		if (str.charAt(parse.getIndex()) != ')') {
			return Parser.FAIL;
		}
		parse.setIndex(parse.getIndex() + 1);
		return parse;
	}

	// GRAMMAR: function = "func" opt_space "(" opt_space parameters opt_space ")"
	// (opt_space return_type )? opt_space "{" opt_space statements opt_space "}";
	// opt_space "{" opt_space program opt_space "}";
	// TREE STURCTURE:
	// parent node: "function" StatementParse
	// child1: Parse returned by parse_parameters
	// child2: Parse returned by parse_program
	private Parse parse_function(String str, int index) {
		StatementParse parent = new StatementParse("function", index);
		// "func"
		String strTemp = str.substring(index);
		if (!strTemp.startsWith("func")) {
			// System.out.println("\n\n" + strTemp + "\n\n");
			// System.out.println("func");
			return Parser.FAIL;
		}
		index += 4;

		// opt_space
		Parse space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println("opt space");
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "("
		if (str.charAt(index) != '(') {
			// System.out.println("(");
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			System.out.println("opt space");
			return Parser.FAIL;
		}
		index = space.getIndex();

		// parameters
		Parse lhs = this.parse(str, index, "parameters");
		if (lhs.equals(Parser.FAIL)) {
			// System.out.println("\nparse_function-parameters fail. ");
			// System.out.println("\n" + index);
			// System.out.println("\n" + str.substring(index));
			return Parser.FAIL;
		}
		index = lhs.getIndex();
		// System.out.println(str.substring(index));
		int parameter_size = lhs.getChildren().size();
		StatementParse signature = new StatementParse("signature", index);
		if (lhs.getStatementTyped()) {
			// System.out.println("parameters node is typed");
			// System.out.println("parameters node is typed");
			for (int i = 0; i < parameter_size; i++) {
				signature.children.add(lhs.getChildren().get(i).getVariableType());
			}
			parent.children.add(signature);
		} else {
			// System.out.println("parameters node is not typed");
		}
		// System.out.println(signature);

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println("opt space");
			return Parser.FAIL;
		}
		index = space.getIndex();

		// ")"
		if (str.charAt(index) != ')') {
			// System.out.println(str.substring(index) + ")");
			return Parser.FAIL;
		}
		index++;

		// (opt_space return_type)?
		int count = 0;
		while (index < str.length() && !lhs.equals(Parser.FAIL) && count < 1) {
			// opt_space
			Parse opt_space = this.parse(str, index, "opt_space");
			if (opt_space.equals(Parser.FAIL)) {
				return Parser.FAIL;
			}
			index = opt_space.getIndex();

			Parse type = this.parse(str, index, "return_type");
			boolean no_specification = false; // type specified
			if (type.equals(Parser.FAIL)) {
				// System.out.println("return type fails");
				no_specification = true;
				type = new VariableParse("var", index);
			}
			index = type.getIndex();

			if (!no_specification) { // return type specifies return types
				if (parent.getChildren().size() == 0) {
					// no signature --> no parameter specifies type
					if (parameter_size > 0) {
						for (int i = 0; i < parameter_size; i++) {
							signature.children.add(new VariableParse("var", index));
						}
					}
					signature.children.add(type);
					parent.children.add(signature);
					// if has parameters
				} else {
					parent.getChildren().get(0).getChildren().add(type);
				}
			} else {
				// function specifies no return types
				if (parent.getChildren().size() != 0) { // has signature before
					parent.getChildren().get(0).getChildren().add(type);
				}
			}
			count++;
		}

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println("space");
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "{"
		if (str.charAt(index) != '{') {
			// System.out.println("{");
			return Parser.FAIL;
		}
		index++;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			// System.out.println("space");
			return Parser.FAIL;
		}
		index = space.getIndex();

		// program
		// find the substring between "{" and "}
		String t = "";
		int t_index = index;

		Stack<String> stack = new Stack<String>(); // stack to keep track of ‘}’
		stack.push("{");
		while (t_index < str.length()) {
			// System.out.println(str + " temp_index " +temp_index + " charAt(temp_index) "
			// + str.charAt(temp_index));
			if (str.charAt(t_index) == '{') {
				stack.push("{");
			}
			if (str.charAt(t_index) == '}') {
				stack.pop();
				if (stack.empty()) {
					break;
				}
			}
			t += str.charAt(t_index);
			t_index++;

		}
		if (!stack.empty()) {
			// System.out.println("stack not empty");
			return Parser.FAIL;
		}

		// t is the String for program

		Parse rhs = this.parse(t, 0, "program");
		if (rhs.equals(Parser.FAIL)) {
			// System.out.println(t);
			System.out.println("fails statement");
			return Parser.FAIL;
		}
		index = t_index;

		// opt_space
		space = this.parse(str, index, "opt_space");
		if (space.equals(Parser.FAIL)) {
			System.out.println("opt_Space");
			return Parser.FAIL;
		}
		index = space.getIndex();

		// "}"
		if (str.charAt(index) != '}') {
			System.out.println("}");
			return Parser.FAIL;
		}
		index++;
		parent.setIndex(index);
		parent.children.add(lhs);
		parent.children.add(rhs);
		lhs = parent;
		return lhs;
	}

	// GRAMMAR: (identifier opt_space ( "," opt_space identifier opt_space )* )?;
	// opt_space)* )?
	// TREE STRUCTURE:
	// parent node: "parameters" (StatementParse)
	// child(s): identifier_name (VariableParse)
	private Parse parse_parameters(String str, int index) {
		// ( )?
		int count = 0;
		// build parameters node
		StatementParse parameters = new StatementParse("parameters", index);
		while (index < str.length() && count < 1) {
			// identifier
			Parse parameter = this.parse(str, index, "parameter");
			if (parameter.equals(Parser.FAIL)) {
				break;
			}
			index = parameter.getIndex();

			// set child
			parameters.children.add(parameter);
			if (parameter.getVariableTyped()) {
				// System.out.println("parameter typed");
				parameters.setStatementTyped();
			} else {
				// System.out.println("parameter not typed\n");
			}

			// opt_space
			Parse space = this.parse(str, index, "opt_space");
			if (space.equals(Parser.FAIL)) {
				break;
			}
			index = space.getIndex();

			// ("," opt_space identifier opt_space)*
			while (index < str.length() && !parameter.equals(Parser.FAIL)) {
				// ","
				if (str.charAt(index) != ',') {
					// System.out.println(str.substring(index)+"no comma");
					break;
				}
				Boolean comma = true;
				index++;
				// opt_space
				space = this.parse(str, index, "opt_space");
				if (space.equals(Parser.FAIL)) {
					break;
				}
				index = space.getIndex();
				// identifier
				parameter = this.parse(str, index, "parameter");
				if (parameter.equals(Parser.FAIL)) {
					if (comma) {
						System.out.println("parameter fails");
						return Parser.FAIL;
					}
					break;
				}
				index = parameter.getIndex();
				// opt_space
				space = this.parse(str, index, "opt_space");
				if (space.equals(Parser.FAIL)) {
					break;
				}
				index = space.getIndex();
				// set child
				parameters.children.add(parameter);
				if (parameter.getVariableTyped()) {
					parameters.setStatementTyped();
				}
			}
			count++;
		}
		// System.out.println(parameters.children);
		for (int i = 0; i < parameters.children.size(); i++) {
			// System.out.println(parameters.children.get(i).getVariableType());
		}
		parameters.setIndex(index);
		return parameters;
	}

	// parameter = ( type req_space )? identifier;
	private Parse parse_parameter(String str, int index) {
		// (type req_space)?
		int count = 0;
		boolean typed = true;
		Parse type = new VariableParse("", index);
		while (index < str.length() && !type.equals(Parser.FAIL) && count < 1) {
			// type
			type = this.parse(str, index, "type");
			// System.out.println(str.substring(index));
			// System.out.println(type.name);
			if (type.equals(Parser.FAIL)) { // no specification of "var"/"func"/"int"
				typed = false;
				type = new VariableParse("var", index);
				break;
			}
			index = type.getIndex();
			// req_space
			Parse req_space = this.parse(str, index, "req_space");
			if (req_space.equals(Parser.FAIL)) {
				break;
			}
			index = req_space.getIndex();
			count++;
		}

		// identifier
		String identifier_name = "";
		Parse identifier_parse = this.parse(str, index, "identifier");
		if (identifier_parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		identifier_name += identifier_parse.toString().substring(8, identifier_parse.toString().length() - 1);
		index = identifier_parse.getIndex();

		VariableParse parameter = new VariableParse(identifier_name, index);

		parameter.type = new VariableParse(type.name, index);
		if (typed) {
			parameter.typed = true;
		}

		// System.out.println(parameter+ " has type: "+ parameter.type.name);
		return parameter;

	}

	private Parse parse_return_type(String str, int index) {
		if (str.charAt(index) != '-' || str.charAt(index + 1) != '>') {
			return Parser.FAIL;
		}
		index += 2;
		Parse parse = this.parse(str, index, "opt_space");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = parse.getIndex();

		parse = this.parse(str, index, "type");
		if (parse.equals(Parser.FAIL)) {
			return Parser.FAIL;
		}
		index = parse.getIndex();
		String name = parse.getName();
		VariableParse return_type = new VariableParse(name, index);
		return return_type;
	}

	// GRAMMAR: identifier = identifier_first_char ( identifier_char )*;
	// NOTE: identifier cannot be a keyword: print, var, if, else, while, func, ret,
	// class, int, bool, string
	// TREE STRUCTURE:
	// parent node: "lookup"
	// child: identifier_name
	private Parse parse_identifier(String str, int index) {
		// TODO Auto-generated method stub
		// concatenate digits in the form of string
		String parsed = "";
		Parse parse = this.parse(str, index, "identifier_first_char");
		if (parse.equals(Parser.FAIL)) {
			// System.out.println(str + "first char fails");
			return Parser.FAIL;
		}
		parsed += parse.getName();
		index = parse.getIndex();

		while (index < str.length() && !parse.equals(Parser.FAIL)) {
			parse = this.parse(str, index, "identifier_char");
			if (!parse.equals(Parser.FAIL)) {
				parsed += parse.getName();
				index = parse.getIndex();

			}
		}
		if (parsed.equals("print") || parsed.equals("var") || parsed.equals("if") || parsed.equals("else")
				|| parsed.equals("while") || parsed.equals("func") || parsed.equals("ret") || parsed.equals("class")
				|| parsed.equals("int") || parsed.equals("bool") || parsed.equals("string")) {
			// System.out.println(parsed);
			return Parser.FAIL;
		}

		StatementParse lookup = new StatementParse("lookup", index);
		VariableParse var_name = new VariableParse(parsed, index);

		lookup.children.add(var_name);
		// System.out.println(lookup.name + ", " +lookup.children);
		return lookup;
	}

	// identifier_first_char = ALPHA| "_";
	private Parse parse_identifier_first_char(String str, int index) {
		String parsed = "";
		if (index < str.length() && (Character.isLetter(str.charAt(index)) || str.charAt(index) == '_')) {
			parsed += str.charAt(index);
			index++;
		} else {
			// System.out.println(str + ", " + index + ", " + str.charAt(index));
			return Parser.FAIL;
		}
		return new StatementParse(parsed, index);
	}

	// identifier_char = ALNUM| "_";
	private Parse parse_identifier_char(String str, int index) {
		String parsed = "";
		if (index < str.length() && (Character.isLetter(str.charAt(index)) || Character.isDigit(str.charAt(index))
				|| str.charAt(index) == '_')) {
			parsed += str.charAt(index);
			index++;
		} else {
			return Parser.FAIL;
		}
		return new StatementParse(parsed, index);
	}

	// integer = (digit)+
	// repeat digit 1 or more times
	private Parse parse_integer(String str, int index) {
		// TODO Auto-generated method stub
		// concatenate digits in the form of string
		String parsed = "";
		// System.out.println(str + " index: "+ index);
		while (index < str.length() && index >= 0 && Character.isDigit(str.charAt(index))) {
			parsed += str.charAt(index);
			index++;
		}

		if (parsed.equals("")) {
			return Parser.FAIL;
		}

		// convert String to int
		return new IntegerParse(Integer.parseInt(parsed), index);
		// only IntegerParse has values
		// integers are always leaves
	}

	// opt_space = (space)*;
	// 4*5/2
	private Parse parse_opt_space(String str, int index) { // index = 0
		if (index >= str.length()) {
			return Parser.FAIL;
		}
		Parse parse = new Parse("", index);
		while (index < str.length() && index >= 0 && (!parse.equals(Parser.FAIL))) {
			parse = this.parse(str, index, "space");
			if (!parse.equals(Parser.FAIL)) {
				index = parse.getIndex();
			}
			// System.out.println(str + "index: " + parse.getIndex());
		}
		return new Parse("", index);
	}

	// req_space = ( space )+;
	// not tested?
	private Parse parse_req_space(String str, int index) {
		// return FAIL parser if there is not at least one space
		if (this.parse(str, index, "space").equals(Parser.FAIL)) {
			return Parser.FAIL;
		} else {
			// Then check for the rest of the spaces
			while (index < str.length() && (!this.parse(str, index, "space").equals(Parser.FAIL))) {
				index = this.parse(str, index, "space").getIndex();
			}
			return new Parse("", index);
		}
	}

	private Parse parse_space(String str, int index) {
		Parse parse = this.parse(str, index, "comment");
		if (!parse.equals(Parser.FAIL)) {
			return parse;
		}
		// parse = FAIL
		if (str.charAt(index) == '\n' || str.charAt(index) == ' ' || str.charAt(index) == '\t') {
			return new Parse("", index + 1);
		}
		// and str.charAt(index) != '\n'||str.charAt(index) != ' '
		return Parser.FAIL; // so it gets here, returns fail
		// should return fail if neither (comment/" "/"\n")
	}

	private Parse parse_comment(String str, int index) {
		// System.out.println(str + ": " + index);
		if (str.charAt(index) != '#') {
			return Parser.FAIL;
		} else {
			index++;
			while (index < str.length() && str.charAt(index) != '\n') {
				// System.out.println(str + ": " +index + ", " + str.charAt(index));
				index++;
			}
			// okay i think I messed up with index
			// System.out.println(str + ": " + index);
			return new Parse("", index);
		}
	}

	/*
	 * private static void test(Parser parser, String str, String term, Parse
	 * expected) { Parse actual = parser.parse(str, term); if (actual == null) {
	 * throw new AssertionError("Got null when parsing \"" + str + "\""); } if
	 * (!actual.equals(expected)) { throw new AssertionError("Parsing \"" + str +
	 * "\"; expected " + expected + " but got " + actual); } }
	 */

	private static void test(Parser parser, String str, String term, String expected, int index) {
		Parse actual = parser.parse(str, term);
		if (actual == null) {
			throw new AssertionError("Got null when parsing \"" + str + "\"");
		}
		if (!actual.toString().equals(expected)) {
			throw new AssertionError("Parsing \"" + str + "\"; expected " + expected + " but got " + actual);
		}
		if (actual.getIndex() != (index)) {
			throw new AssertionError("Parsing \"" + str + "\"; expected " + expected + " but got " + actual);
		}
	}

	public static void test() {
		Parser parser = new Parser();
		// Testing req space with first opt_space in add changed to req_space
		/*
		 * These tests all passed when add_sub_expression was changed to req_space I
		 * changed it back so our other tests would work I also had to use math instead
		 * of multiplication or divison beacuse the req_space was in add_sub_expression
		 * test(parser, " 1 + 2", "addition", new Parse(3, 6)); test(parser,
		 * "      1 + 2", "addition", new Parse(3, 11)); test(parser, "1 + 2",
		 * "addition", Parser.FAIL); test(parser, " 3 - 2", "subtraction", new Parse(1,
		 * 6)); test(parser, "      3 - 2", "subtraction", new Parse(1, 11));
		 * test(parser, "3 - 2", "subtraction", Parser.FAIL); test(parser, " 5 * 5",
		 * "math", new Parse(25, 6)); test(parser, "      5 * 5", "math", new Parse(25,
		 * 11)); test(parser, "5 * 5", "math", Parser.FAIL); test(parser, " 6 / 3",
		 * "math", new Parse(2, 6)); test(parser, "      6 / 3", "math", new Parse(2,
		 * 11)); test(parser, "6 / 3", "math", Parser.FAIL);
		 */

		/*
		 * // integer test test(parser, "3", "expression", "3", 1); test(parser, "3*3",
		 * "expression", "(* 3 3)", 3); test(parser, "2*3*1*6", "expression",
		 * "(* (* (* 2 3) 1) 6)", 7); test(parser, "2+2+2", "expression",
		 * "(+ (+ 2 2) 2)", 5); test(parser, "2*2+2", "expression", "(+ (* 2 2) 2)", 5);
		 * test(parser, "2+2*2", "expression", "(+ 2 (* 2 2))", 5);
		 */
		/*
		 * Parse var = parser.parse("(2+2)", "expression");
		 * System.out.println(var.toString()); System.out.println(var.getIndex());
		 * System.out.println(var.equals(Parser.FAIL));
		 */

		/*
		 * test(parser, "(2+2)", "expression", "(+ 2 2)", 5); test(parser, "2*(2+2)",
		 * "expression", "(* 2 (+ 2 2))", 7); test(parser, "0", "expression", "0", 1);
		 * test(parser, "100", "expression", "100", 3); test(parser, "3-", "expression",
		 * "3", 1);
		 * 
		 * test(parser, "2021", "expression", "2021", 4); test(parser, "2020+2021",
		 * "expression", "(+ 2020 2021)", 9);
		 * 
		 * test(parser, "0+0", "expression", "(+ 0 0)", 3); test(parser, "0+0+0+0+0",
		 * "expression", "(+ (+ (+ (+ 0 0) 0) 0) 0)", 9); test(parser, "42+0",
		 * "expression", "(+ 42 0)", 4); test(parser, "0+42", "expression", "(+ 0 42)",
		 * 4); test(parser, "123+234+345", "expression", "(+ (+ 123 234) 345)", 11);
		 * 
		 * // test(parser, "(0)", "parenthesis", "0", 3); test(parser, "3 ",
		 * "expression", "3", 1); test(parser, "3 + 4", "expression", "(+ 3 4)", 5);
		 * test(parser, "4 +    (1 +2+3)+ 5", "expression", "(+ (+ 4 (+ (+ 1 2) 3)) 5)",
		 * 18); test(parser, "3   +   4+(5+6)+9   ", "expression",
		 * "(+ (+ (+ 3 4) (+ 5 6)) 9)", 17);
		 * 
		 * // division passed test(parser, "1 + 2 * 6 / 4", "expression",
		 * "(+ 1 (/ (* 2 6) 4))", 13); test(parser, "2/1", "expression", "(/ 2 1)", 3);
		 * test(parser, "1/2", "expression", "(/ 1 2)", 3); test(parser, "5/2  ",
		 * "expression", "(/ 5 2)", 3); test(parser, "0    /   1", "expression",
		 * "(/ 0 1)", 10); test(parser, "9 / (2 / 1)", "expression", "(/ 9 (/ 2 1))",
		 * 11);
		 * 
		 * // subtraction passed test(parser, "2-1", "expression", "(- 2 1)", 3);
		 * test(parser, "(100-10) - 1", "expression", "(- (- 100 10) 1)", 12);
		 * test(parser, "100-(10 - 1)", "expression", "(- 100 (- 10 1))", 12);
		 * 
		 * // sign at the end passed test(parser, "3++", "expression", "3", 1);
		 * test(parser, "1+1-", "expression", "(+ 1 1)", 3); test(parser, "1+1+-",
		 * "expression", "(+ 1 1)", 3); test(parser, "1***", "expression", "1", 1);
		 * test(parser, "1+", "expression", "1", 1); test(parser, "1-", "expression",
		 * "1", 1); test(parser, "1///", "expression", "1", 1); test(parser,
		 * "1- - - + ", "expression", "1", 1); test(parser, "1+ 1    -", "expression",
		 * "(+ 1 1)", 4); test(parser, "2+2*2//*", "expression", "(+ 2 (* 2 2))", 5);
		 * 
		 * // testing trailing space // space as trailing space test(parser, "1     ",
		 * "expression", "1", 1); test(parser, "1***   ", "expression", "1", 1);
		 * test(parser, "9 / (2 / 1)  ", "expression", "(/ 9 (/ 2 1))", 11);
		 * test(parser, "3 + 4                  ", "expression", "(+ 3 4)", 5); //
		 * comment as trailing space test(parser, "1+1#hello", "expression", "(+ 1 1)",
		 * 3); test(parser, "2*3*1*6#hello", "expression", "(* (* (* 2 3) 1) 6)", 7);
		 * test(parser, "(0)#hello", "expression", "0", 3); // new line as trailing
		 * space test(parser, "1+1\n", "expression", "(+ 1 1)", 3); test(parser,
		 * "2020+2021\n", "expression", "(+ 2020 2021)", 9); test(parser, "5/2\n",
		 * "expression", "(/ 5 2)", 3); test(parser, "1+\n", "expression", "1", 1);
		 * 
		 * // able to parse leading space // regular space as leading space /* Parse one
		 * = parser.parse("     1", "program"); System.out.println(one.toString());
		 * System.out.println(one.getIndex());
		 * System.out.println(one.equals(Parser.FAIL));
		 */

		// test(parser, " 1", "expression", "1", 6);
		// test(parser, " 1/2", "expression", "(/ 1 2)", 8);

		/*
		 * // new line as leading space test(parser, "\n1+1", "expression", "(+ 1 1)",
		 * 4); test(parser, "\n     1/2", "expression", "(/ 1 2)", 9); test(parser,
		 * "     \n3++", "expression", "3", 7); // comment as leading space test(parser,
		 * "#hello\n1+1", "expression", "(+ 1 1)", 10); test(parser,
		 * "#hello\n123+234+345", "expression", "(+ (+ 123 234) 345)", 18); test(parser,
		 * "#comment\n0", "expression", "0", 10);
		 * 
		 * // able to parse space between digit and parenthesis test(parser,
		 * "(   0+0   )", "expression", "(+ 0 0)", 11); test(parser,
		 * "(   (100-10) - 1    )", "expression", "(- (- 100 10) 1)", 21); test(parser,
		 * "(   4 +    (1 +2+3)+ 5 )", "expression", "(+ (+ 4 (+ (+ 1 2) 3)) 5)", 24);
		 * test(parser, "(   0   )", "expression", "0", 9);
		 * 
		 * Parse fail = parser.parse("var 1stBday = 100;", "program"); // Parser.FAIL //
		 * System.out.println(fail.toString()); // should be "" //
		 * System.out.println(fail.getIndex()); // should be ""
		 * 
		 * // testing when print should fail test(parser, "", "print", "", -1);
		 * test(parser, "p", "print", "", -1); test(parser, "pr", "print", "", -1);
		 * test(parser, "pri", "print", "", -1); test(parser, "prin", "print", "", -1);
		 * test(parser, "print", "print", "", -1); test(parser, "print ", "print", "",
		 * -1); test(parser, " print", "print", "", -1); test(parser, " print 2+2",
		 * "print", "", -1); test(parser, "print", "print", "", -1); test(parser,
		 * "print", "print", "", -1); test(parser, "print", "print", "", -1);
		 * test(parser, "print", "print", "", -1);
		 * 
		 * test(parser, "print 2+2", "print", "", -1); test(parser, "print2+2;",
		 * "print", "", -1); test(parser, "print 1~2;", "print", "", -1);
		 * 
		 * // testing only space, should all fail test(parser, "", "expression", "",
		 * -1); // fail test(parser, "\n", "expression", "", -1); test(parser,
		 * "#hello\n", "expression", "", -1); test(parser, "#     \n", "expression", "",
		 * -1);
		 * 
		 * // testing when print should work test(parser, "print 2+2; ", "print",
		 * "(print (+ 2 2))", 10); test(parser, "print 2+2;  hello", "print",
		 * "(print (+ 2 2))", 10);
		 * 
		 * test(parser, "print 9+1;", "print", "(print (+ 9 1))", 10); test(parser,
		 * "print 6;", "print", "(print 6)", 8); test(parser, "print 9*2;", "print",
		 * "(print (* 9 2))", 10);
		 * 
		 * test(parser, "print 9+1*5 ;", "print", "(print (+ 9 (* 1 5)))", 13);
		 * test(parser, "print 9/2 * 4   ;", "print", "(print (* (/ 9 2) 4))", 17);
		 * test(parser, "print        (8*8    + 2  - 3-1) / 2  ;", "print",
		 * "(print (/ (- (- (+ (* 8 8) 2) 3) 1) 2))", 39);
		 * 
		 * // Test expression, statement, and program test(parser, "2+2", "expression",
		 * "(+ 2 2)", 3); test(parser, "2*2+1", "expression", "(+ (* 2 2) 1)", 5);
		 * test(parser, "print 3+2;", "statement", "(print (+ 3 2))", 10); test(parser,
		 * "4*5/2", "statement", "", -1);
		 * 
		 * Parse p = parser.parse("print 3+2;    hi", "program"); // index = 14 //
		 * System.out.println(p.toString()); // System.out.println(p.getIndex()); //
		 * System.out.println(p.equals(Parser.FAIL));
		 * 
		 * /* Parse print_3 = parser.parse("print 3;", "program");
		 * System.out.println(print_3.toString());
		 * System.out.println(print_3.getIndex());
		 * System.out.println(print_3.equals(Parser.FAIL));
		 */
		// print 3 failed
		// failed where?
		/*
		 * test(parser, "print 3;", "program", "(sequence (print 3))", 8);
		 * 
		 * /* Parse mul_statements = parser.parse("print 3+3;   print 6+8;", "program");
		 * System.out.println(mul_statements.toString()); // s expression incomplete //
		 * why incomplete? System.out.println(mul_statements.getIndex()); // should be
		 * 23 instead of 13 //System.out.println(mul_statements.equals(Parser.FAIL));
		 * 
		 */
		/*
		 * test(parser, "print 3+3;   print 6+8;", "program",
		 * "(sequence (print (+ 3 3)) (print (+ 6 8)))", 23); test(parser,
		 * "print 1 + 1;", "program", "(sequence (print (+ 1 1)))", 12);
		 * 
		 * // testing string // test(parser, "b", "expression", "(lookup (b))", 1); //
		 * test(parser, "hello", "expression", "(lookup (hello))", 5);
		 * 
		 * // testing variables test(parser, "var n = 3;", "declaration",
		 * "(declare n 3)", 10);
		 * 
		 * // "(declare (n) 3)" // "(declare n 3)"
		 * 
		 * test(parser, "var n = 3;", "program", "(sequence (declare n 3))", 10);
		 * 
		 * // Parse var = parser.parse("var n = 3;", "program"); //
		 * System.out.println(var.toString()); // System.out.println(var.getIndex());
		 * 
		 * test(parser, "var n = 3; var x = 3;", "program",
		 * "(sequence (declare n 3) (declare x 3))", 21);
		 * 
		 * Parse vars = parser.parse("var n = 3; var x = 3;", "program"); //
		 * System.out.println(vars.toString()); // System.out.println(vars.getIndex());
		 * 
		 * Parse actual = parser.parse("print #hello\n1-1;", "program"); //
		 * System.out.println(actual.equals(Parser.FAIL)); // false, should not fail
		 * 
		 * // debug "print #hello\n1-1;" fails at where /*
		 * System.out.println(actual.name); // print
		 * System.out.println(actual.getChildren()); // print
		 * System.out.println(actual.toString()); // (print (- 1 1))
		 * System.out.println(actual.getIndex());
		 */

		/*
		 * Parse actual = parser.parse("print         ;", "program");
		 * System.out.println(actual.equals(Parser.FAIL));
		 * System.out.println(actual.name); System.out.println(actual.toString());
		 * System.out.println(actual.getIndex());
		 */

		// fail to parse

		// (print (- 1 1))
		// where it returns fail?
		/*
		 * Parse a = parser.parse("print (5 - (5 + (5 * (5 / 5))));", "program"); //
		 * System.out.println(a.equals(Parser.FAIL)); // true //
		 * System.out.println(a.name); // System.out.println(a.toString()); //
		 * System.out.println(a.getIndex()); /* Parse ifelse =
		 * parser.parse("var ifelse = 0;", "program");
		 * System.out.println(ifelse.toString()); System.out.println(ifelse.getIndex());
		 */
		/*
		 * Parse ifelse =
		 * parser.parse("var num1 = 1;\nvar num2 = 2;\nprint (num1 + 3) * num2;",
		 * "program"); // System.out.println(ifelse.toString()); //
		 * System.out.println(ifelse.getIndex());
		 * 
		 * Parse assign =
		 * parser.parse("var num = 33;print num;num = (9 + 1) * 2;print num;",
		 * "program"); // System.out.println(assign.toString()); //
		 * System.out.println(assign.getIndex());
		 * 
		 * Parse f = parser.parse("var 1stBday = 100;", "program");
		 * 
		 * 
		 * // Parser.FAIL // System.out.println(f.toString()); // should be ""
		 * 
		 * // What s expression should Parser return for Parser.FAIL? // return empty
		 * string
		 * 
		 * /*
		 * 
		 * System.out.println(fail.getIndex());
		 * System.out.println(fail.equals(Parser.FAIL));
		 */
		/*
		 * Parse semi = parser.parse("var num = 0", "program"); // Parser.FAIL
		 * System.out.println(semi.toString()); // should be ""
		 * 
		 * 
		 * Parse aftersemi = parser.parse("var num = 0;;;;;;;;;", "program"); //
		 * Parser.FAIL System.out.println(semi.toString()); // should be ""
		 * 
		 * 
		 * Parse s = parser.parse("print a; #2\n\n", "program"); // "printa; #2\n\n"
		 * System.out.println(s.toString()); System.out.println(s.equals(Parser.FAIL));
		 * 
		 */
		/*
		 * //Parse switch_var = parser.
		 * parse("# switch variables\nvar a = 2;\nprint a;	#2\n\nvar b = 3;\nprint b; #3 \n\nvar temp = b;\nprint temp; #3\n\nb = a;\nprint b; #2\n\na = temp;\nprint a; #3\nprint temp; #3"
		 * , "program"); Parse switch_var =
		 * parser.parse("# switch variables\nvar a = 2;\nprint a;", "program");
		 * //#2\n\nvar b = 3;\nprint b; #3 \n\nvar temp = b;\nprint temp; #3\n\nb =
		 * a;\nprint b; #2\n\na = temp;\nprint a; #3\nprint temp; #3
		 * 
		 * System.out.println(switch_var.toString());
		 * System.out.println(switch_var.equals(Parser.FAIL));
		 */
		/*
		 * Parse loop = parser.parse("var x = 0;\n" + "while (x < 3) {\n" +
		 * "    x = x + 1;\n" + "}\n" + "print x;", "program");
		 * System.out.println(loop.toString()); System.out.println(loop.getIndex());
		 * System.out.println(loop.equals(Parser.FAIL));
		 * 
		 */
		/*
		 * Parse loop2 = parser.parse("var x = 0;\n" +
		 * "while (!x) {   # should loop once\n" + "    var y = 0;\n" +
		 * "    while (y < 3) {     # should loop 3 times\n" + "        print y;\n" +
		 * "        y = y + 1;\n" + "    }\n" +
		 * "    x = y;      # assign location from outer, value from inner that has also been reassigned by the loop\n"
		 * + "}", "program");
		 */

		// System.out.println(loop2.toString());
		// System.out.println(loop2.getIndex());
		// System.out.println(loop2.equals(Parser.FAIL));

		// precedence: paren, muldiv, addsub, compare, not, and, or

		// System.out.println(three.toString());
		// System.out.println(three.getIndex());
		// System.out.println(three.equals(Parser.FAIL));

		Parse six = parser.parse("# tests for function return types and differently typed variables of the same name using closures\n"
				+ "\n"
				+ "func a = func() -> int{\n"
				+ "	int a = 4;\n"
				+ "	ret a;\n"
				+ "};\n"
				+ "", "program");

		System.out.println(six.toString());

	}

	public static void main(String[] args) {
		test();
	}

}
