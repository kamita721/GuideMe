package org.guideme.guideme.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.debugger.Main;

/*
 * Backend for the console pane of the javascript debug window.
 * 
 * The debug window itself provides a console window with an input and output stream.
 * This class simply uses those streams to provide interactivity.
 * 
 * To allow for multi-line input, we keep track of our depth in nested brackets. We additionally
 * keep track of quotations so that we can exclude brackets within them.
 * If we reach the end of a line, and our not inside of a block, then we emit the accumulated string for evaluation.
 */
public class JscriptConsole implements Runnable {
	StringBuilder cmdAccumulator;
	int depth;
	boolean withinQuotation;
	char quoteChar;

	private final BufferedReader stdin;
	private final PrintStream stdout;
	private final PrintStream stderr;
	private final ContextFactory cntxFact;
	private final Scriptable scope;

	private boolean shutdown = false;

	private Logger logger = LogManager.getLogger();

	public JscriptConsole(Main debugger, ContextFactory cntxFact, Scriptable scope) {
		this.cmdAccumulator = new StringBuilder();

		this.stdin = new BufferedReader(new InputStreamReader(debugger.getIn()));
		this.stdout = new PrintStream(debugger.getOut());
		this.stderr = new PrintStream(debugger.getErr());
		this.cntxFact = cntxFact;
		this.scope = scope;
	}

	@Override
	public void run() {
		printHeader();
		printPrompt();
		while (!shutdown) {
			String str;
			try {
				str = stdin.readLine();
			} catch (IOException e) {
				stderr.print("An unexpected error occured.");
				e.printStackTrace(stderr);
				e.printStackTrace();
				break;
			}
			cmdAccumulator.append(str);
			updateNesting(str);
			if (depth <= 0 && !withinQuotation) {
				exec(cmdAccumulator.toString());
				cmdAccumulator = new StringBuilder();
			}
		}
	}

	private void printHeader() {
		stdout.print("Javascript console:\n");
		stdout.print("Type \"debugger\" to inspect the global scope.");
		// TODO, print usage info.
	}

	private void printPrompt() {
		stdout.print("\n> ");
		stdout.flush();
	}

	private void exec(String str) {
		str = str.strip();
		try (Context cntx = cntxFact.enterContext()) {
			Object ans = cntx.evaluateString(scope, str, "console", 0, null);
			stdout.print(ans.toString());
		} catch (EcmaError e) {
			stderr.print(e.getMessage());
		} catch (Exception e) {
			stderr.print("Internal error: " + e.getMessage());
			logger.warn("Internal error while evaluating console javascript: {}", str, e);
		} finally {
			printPrompt();
		}
	}

	private void updateNesting(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '"', '\'':
				if (withinQuotation && quoteChar == c) {
					withinQuotation = false;
				} else if (!withinQuotation) {
					withinQuotation = true;
					quoteChar = c;
				}
				break;
			case '{':
				if (!withinQuotation) {
					depth++;
				}
				break;
			case '}':
				if (!withinQuotation) {
					depth--;
				}
				break;
			default:
				break;
			}
		}
	}
}
