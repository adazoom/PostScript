package src;

import structure5.*;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;

public class Interpreter {
	private static boolean debug = false;
	// create a table that stores defined values
	private SymbolTable table = new SymbolTable();
	// implement stack
	private Stack<Token> stack = new StackList<Token>();

	/**
	 * Main method that is called first. Handles an iterator of Tokens and
	 * passes them to handler
	 * 
	 * @param cmd
	 */
	public void interpretLine(Iterator<Token> cmd) {
		Token t;
		while (cmd.hasNext()) {
			// get the token
			t = cmd.next();
			if (debug)
				System.out.println("Token " + t);
			else {
				handler(t);
			}
		}
	}

	/**
	 * Method that proceeds the List of Tokens and passes them to main interepter
	 * @param token
	 */
	private void proceed(List<Token> token) {
		Reader read = new Reader(token);
		interpretLine(read);
	}
	/**
	 * The manager function that handles all of the input operations
	 * @param t
	 */
	private void handler(Token t) {
		// check whether the t is a procedure NOTE:happens again in case input contains if
		if (t.isProcedure()) {
			// if its just a procedure with no if operator then we push it on stack
			stack.push(t);
		}
		// if we are dealing with symbols, which include all the def commands
		// we need to store the new defined variables in table
		if (t.isSymbol()) {
			String token = t.getSymbol();
			// check whether command starts with slash
			if (t.getSymbol().charAt(0) == '/') {
				//store tokens that are given until we encounter def
				stack.push(t);
			}
			// check if the command ends with def
			else if (token.equals("def")) {
				// store symbol and its value for table
				Token number = stack.pop();
				String name = stack.pop().getSymbol().substring(1);
				// store new variables in the table
				table.add(name, number);
			}
			// if we call table values
			else if (table.contains(token)) {
				//if the value of a symbol is a procedure itself, we should proceed it
				if (table.get(token).isProcedure()) {
					proceed(table.get(token).getProcedure());
				} else //just push the value on the stack
					stack.push(table.get(token));
			} else {//operation is a command or do nothing
				// handle the command and break
				command(token);
				//break;
			}
		}
		// ghostscript only accepts numbers and booleans, therefore my stack accepts only numbers and booleans
		if (t.isNumber() || t.isBoolean()) {
			stack.push(t);
		}
	}

	/**
	 * Method to handle all the commands
	 */
	private void command(String token) {

		switch (token) {
		case "pstack": 	pstack();
			break;
		case "quit": 	System.exit(0);
			break;
		case "exch":	exch();
			break;
		case "dup":		dup();
			break;
		case "pop":		if (!stack.empty())//check if there is something to pop
							stack.pop();
			break;
		case "sub":		sub();
			break;
		case "add":		add();
			break;
		case "mul":		mul();
			break;
		case "div":		div();
			break;
		case "eq":		stack.push(new Token(eq()));
			break;
		case "ne":		stack.push(new Token(ne()));
			break;
		case "lt":		stack.push(new Token(lt()));
			break;
		case "gt":		stack.push(new Token(gt()));
			break;
		case "not":		not();
			break;
		case "ptable":	System.out.println(table.toString());
			break;
		case "if":		ifOperand();
			break;
		}
	}
	/**
	 * Method to perform "if" operation
	 */
	private void ifOperand() {
		//first store the procedure and then the boolean
		Token prevProc = stack.pop();
		Token prevToken = stack.pop();
		// check whether the condition is true
		if (prevToken.isBoolean() && prevToken.getBoolean() == true) {
			proceed(prevProc.getProcedure());
		}
	}

	/**
	 * Method to perform "not" command
	 */
	private void not() {
		try {
			// pop token
			Token x = stack.pop();
			if (x.isBoolean()) {
				stack.push(new Token(!x.getBoolean()));
			} else if (x.isNumber()) {
				Double z = -1 * (x.getNumber() + 1);
				stack.push(new Token(z));
			}
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least one token!");
		}
	}

	/**
	 * Method to check "greater than"
	 */
	private boolean gt() {
		try {
			// pop tokens and compare
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.isNumber() && y.isNumber()) {
				if (y.getNumber() < x.getNumber())
					return true;
			} else {
				stack.push(x);
				stack.push(y);
				System.out
						.println("Can only perform this command with two numbers!");
			}
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least two tokens!");
		}
		return false;
	}

	/**
	 * Method to check "less than"
	 */
	private boolean lt() {
		try {
			// pop tokens and compare
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.isNumber() && y.isNumber()) {
				if (y.getNumber() > x.getNumber())
					return true;
			} else {
				stack.push(x);
				stack.push(y);
				System.out
						.println("Can only perform this command with two numbers!");
			}
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least two tokens!");
		}
		return false;
	}

	/**
	 * Method to check if two tokens are not equal
	 */
	private boolean ne() {
		try {
			// pop tokens and compare
			Token y = stack.pop();
			Token x = stack.pop();
			if (!x.equals(y))
				return true;
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least two tokens!");
		}
		return false;
	}

	/**
	 * Method to check if two tokens are equal
	 */
	private boolean eq() {
		try {
			// pop tokens and compare
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.equals(y))
				return true;
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least two tokens!");
		}
		return false;
	}

	/**
	 * Method to substract two tokens
	 */
	private void sub() {
		if (stack.size() > 1) {
			// popy,popx,pushx-y
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.isNumber() && y.isNumber()) {
				Token z = new Token(x.getNumber() - y.getNumber());
				stack.push(z);
			} else {
				stack.push(x);
				stack.push(y);
				System.out
						.println("Can only perform this command with two numbers!");
			}
		} else {
			System.out.println("The stack must have at least two numbers!");
		}

	}

	/**
	 * Method to add two tokens
	 */
	private void add() {
		if (stack.size() > 1) {
			// popy,popx,pushx+y
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.isNumber() && y.isNumber()) {
				Token z = new Token(x.getNumber() + y.getNumber());
				stack.push(z);
			} else {
				stack.push(x);
				stack.push(y);
				System.out
						.println("Can only perform this command with two numbers!");
			}
		} else {
			System.out.println("The stack must have at least two numbers!");
		}
	}

	/**
	 * Method to multiply two tokens
	 */
	private void mul() {
		if (stack.size() > 1) {
			// popy,popx,push xy
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.isNumber() && y.isNumber()) {
				Token z = new Token(x.getNumber() * y.getNumber());
				stack.push(z);
			} else {
				stack.push(x);
				stack.push(y);
				System.out
						.println("Can only perform this command with two numbers!");
			}
		} else {
			System.out.println("The stack must have at least two numbers!");
		}
	}

	/**
	 * Method to divide two tokens
	 */
	private void div() {
		if (stack.size() > 1) {
			// popy,popx,push x/y
			Token y = stack.pop();
			Token x = stack.pop();
			if (x.isNumber() && y.isNumber()) {
				Token z = new Token(x.getNumber() / y.getNumber());
				stack.push(z);
			} else {
				stack.push(x);
				stack.push(y);
				System.out
						.println("Can only perform this command with two numbers!");
			}
		} else {
			System.out.println("The stack must have at least two numbers!");
		}
	}

	/**
	 * Method to print the stack
	 */
	private void pstack() {
		Enumeration<Token> e = stack.elements();
		while (e.hasMoreElements())
			System.out.println(e.nextElement().toString());
	}

	/**
	 * Method to exchange last two tokens on the stack
	 */
	private void exch() {
		try {
			// pop y, pop x
			Token y = stack.pop();
			Token x = stack.pop();
			// push y, push x
			stack.push(y);
			stack.push(x);
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least two tokens!");
		}

	}

	/**
	 * Method to duplicate the last token on stack
	 */
	private void dup() {
		try {
			// pop x, push x, push x
			Token x = stack.pop();
			stack.push(x);
			stack.push(x);
		} catch (NullPointerException e) {
			System.out.println("The stack must have at least one token!");
		}
	}

	/**
	 * Prompt prints out a size of non empty stack
	 */
	private void printPrompt() {
		if (stack.size() > 0)
			System.out.print("PS" + "<" + stack.size() + "> ");
		else
			System.out.print("PS> ");

	}

	public static void main(String[] args) {

		Interpreter inter = new Interpreter();

		Scanner sc = new Scanner(System.in);

		inter.printPrompt();

		while (sc.hasNextLine()) {

			inter.interpretLine(new Reader(sc.nextLine()));
			inter.printPrompt();

		}

	}
}
