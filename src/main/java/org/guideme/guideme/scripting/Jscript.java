package org.guideme.guideme.scripting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.eclipse.swt.widgets.Display;
import org.guideme.guideme.MainLogic;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.ui.main_shell.MainShell;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.tools.debugger.Main;
import org.mozilla.javascript.Context;

public class Jscript {
	private Guide guide;
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker JSCRIPT_MARKER = MarkerManager.getMarker("JSCRIPT");
	private boolean running;
	private final Main dbg;
	private final ContextFactory cntxFact;

	private static final String IMPLICIT_JAVASCRIPT = """
			//For backwards compatability, maintain a top level logging function called "jscriptLog"
			jscriptLog=debug.log.bind(debug);

			function pageLoad(){};
			""";

	public Jscript(Guide guide) {
		this.guide = guide;

		cntxFact = new ContextFactory();

		dbg = initJavascriptDebugger();

		dbg.attachTo(cntxFact);
	}

	private Main initJavascriptDebugger() {
		Thread dbgThread;
		Main ans = new Main("GuideMe");

		dbgThread = new Thread(
				new JscriptConsole(ans, cntxFact, guide.getSettings().getGlobalScope()));
		dbgThread.setName("JavascriptDebuggerThread");
		dbgThread.start();

		return ans;
	}

	public void updateDebugSettings(AppSettings appSettings) {

		dbg.setBreakOnExceptions(appSettings.getJsDebugError());
		dbg.setBreakOnEnter(appSettings.getJsDebugEnter());
		dbg.setBreakOnReturn(appSettings.getJsDebugExit());
		dbg.setSize(appSettings.getJsDebugWidth(), appSettings.getJsDebugHeight());
		dbg.setSize(appSettings.getJsDebugWidth(), appSettings.getJsDebugHeight());
		dbg.setVisible(appSettings.getJsDebug());
	}

	public class SimpleNodeVisitor implements NodeVisitor {
		private boolean foundCall = false;
		private ArrayList<Object> args = new ArrayList<>();

		@Override
		public boolean visit(AstNode node) {
			if (node == null)
				return false;

			int nodeType = node.getType();
			if (nodeType == Token.CALL && !foundCall) {
				foundCall = true;
				for (AstNode n2 : ((FunctionCall) node).getArguments()) {
					if (guide.getSettings().isConvertArgumentTypes())
						args.add(nodeToObj(n2));
					else
						args.add(n2.toSource());
				}
			}

			return true;
		}

		public List<Object> getArgs() {
			return args;
		}

		private Object nodeToObj(AstNode node) {
			return nodeToObj(node, true);
		}

		private Object nodeToObj(AstNode node, boolean convertName) {
			// TODO: Ideally, there should be a better way to handle this than this
			// obnoxious chain of if/else statements.

			switch (node) {
			case StringLiteral sl:
				return sl.getValue();
			case NumberLiteral nl:
				return nl.getNumber();
			case ArrayLiteral al:
				return al.getElements().toArray();
			case KeywordLiteral kl:
				switch (kl.getType()) {
				case Token.NULL:
					return null;
				case Token.FALSE:
					return false;
				case Token.TRUE:
					return true;
				default:
					return node.toSource();
				}
			case ObjectLiteral ol:
				HashMap<Object, Object> obj = new HashMap<Object, Object>();
				for (ObjectProperty op : ((ObjectLiteral) node).getElements())
					obj.put(nodeToObj(op.getLeft(), false), nodeToObj(op.getRight()));
				return obj;
			case Name name:
				String key = name.getIdentifier();
				if (convertName)
					return guide.getSettings().getScriptVariables().get(key);
				else
					return key;

			default:
				return node.toSource();
			}
		}
	}

	public void exec(UserSettings userSettings, AppSettings appSettings, boolean inPrefGuide,
			MainShell mainShell, OverRide overRide, String javaScriptText, String javaFunction,
			boolean pageloading) {
		running = true;

		// TODO shouldn't this already be done?
		guide.setMainshell(mainShell);

		SwingUtilities.invokeLater(() -> run(userSettings, appSettings, inPrefGuide, overRide,
				javaScriptText, javaFunction, pageloading));
		while (isRunning()) {
			Display.getCurrent().readAndDispatch();
		}
	}

	public void run(UserSettings userSettings, AppSettings appSettings, boolean inPrefGuide,
			OverRide overRide, String javaScriptText, String javaFunction, boolean pageloading) {

		LOGGER.trace(JSCRIPT_MARKER, "Chapter: {}", guide.getSettings().getChapter());
		LOGGER.debug(JSCRIPT_MARKER, "Page: {}", guide.getSettings().getCurrPage());
		LOGGER.debug(JSCRIPT_MARKER, "javaFunction: {}", javaFunction);
		LOGGER.debug(JSCRIPT_MARKER, "pageloading: {}", pageloading);
		LOGGER.trace(JSCRIPT_MARKER, "javaScriptText: {}", javaScriptText);
		if (!guide.getSettings().isGlobalScriptLogged()) {
			LOGGER.trace(JSCRIPT_MARKER, "globalJavaScriptText: " + guide.getGlobaljScript());
			guide.getSettings().setGlobalScriptLogged(true);
		}
		ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
		Debug debug = new Debug(this);
		Map<String, Object> scriptVars;
		scriptVars = guide.getSettings().getScriptVariables();

		Scriptable globalScope = guide.getSettings().getGlobalScope();

		dbg.setScope(globalScope);

		Context cntx = cntxFact.enterContext();
		cntx.setOptimizationLevel(-1);
		cntx.getWrapFactory().setJavaPrimitiveWrap(false);
		if (globalScope.get("GuideObjects", globalScope) != "true") {
			ScriptableObject.putProperty(globalScope, "GuideObjects", "true");
			ScriptableObject.putProperty(globalScope, "comonFunctions", comonFunctions);
			ScriptableObject.putProperty(globalScope, "fileSeparator",
					java.lang.System.getProperty("file.separator"));
			ScriptableObject.putProperty(globalScope, "guide", guide);
			ScriptableObject.putProperty(globalScope, "guideSettings", guide.getSettings());
			ScriptableObject.putProperty(globalScope, "mediaDir", appSettings.getDataDirectory());
			ScriptableObject.putProperty(globalScope, "globalVars",
					MainLogic.getGlobalScriptVariables());
			ScriptableObject.putProperty(globalScope, "debug", debug);

			cntx.evaluateString(globalScope, IMPLICIT_JAVASCRIPT, "internal", 0, null);
		}
		ScriptableObject.putProperty(globalScope, "scriptVars", scriptVars);

		Scriptable parentScope = guide.getSettings().getScope();
		if (parentScope == null) {
			parentScope = cntx.newObject(globalScope);
		}
		parentScope.setParentScope(globalScope);
		updateDebugSettings(appSettings);
		dbg.setScope(parentScope);

		cntx.evaluateString(parentScope, guide.getGlobaljScript(), "globalScript", 1, null);
		guide.getSettings().setScope(parentScope);

		Scriptable scope = cntx.newObject(parentScope);
		scope.setParentScope(parentScope);

		if (!inPrefGuide) {
			UserSettings cloneUS =  new UserSettings(userSettings);
			ScriptableObject.putProperty(globalScope, "userSettings", cloneUS);
		} else {
			ScriptableObject.putProperty(globalScope, "userSettings", userSettings);
		}
		// Deprecated should use guide now
		ScriptableObject.putProperty(scope, "comonFunctions", comonFunctions);
		ScriptableObject.putProperty(scope, "fileSeparator",
				java.lang.System.getProperty("file.separator"));
		ScriptableObject.putProperty(scope, "guide", guide);
		ScriptableObject.putProperty(scope, "mediaDir", appSettings.getDataDirectory());
		// Deprecated should use guide now
		ScriptableObject.putProperty(scope, "guideSettings", guide.getSettings());

		ScriptableObject.putProperty(scope, "scriptVars", scriptVars);
		ScriptableObject.putProperty(scope, "guide", guide);
		// Deprecated should use guide now
		ScriptableObject.putProperty(scope, "guideSettings", guide.getSettings());
		LOGGER.debug(JSCRIPT_MARKER, "Starting ScriptVariables: {}", scriptVars);
		LOGGER.debug(JSCRIPT_MARKER, "Starting Flags {{}}", guide.getSettings().GetFlags());

		if (pageloading) {
			ScriptableObject.putProperty(globalScope, "overRide", overRide);
		}

		try {

			cntx.evaluateString(scope, javaScriptText, "pageScript", 1, null);

			/*
			 * TODO, why the song and dance around javaFunction? Can't we still just call it
			 * as normal javascript?
			 */
			int argStart;
			String javaFunctionFull = javaFunction;
			argStart = javaFunction.indexOf("(");
			if (argStart > -1) {
				javaFunction = javaFunction.substring(0, argStart);
			}
			Object fObj = scope.get(javaFunction, scope);
			if (!(fObj instanceof Function)) {
				fObj = parentScope.get(javaFunction, parentScope);
			}
			if (!(fObj instanceof Function)) {
				fObj = globalScope.get(javaFunction, parentScope);
			}
			if ((fObj instanceof Function fct)) {
				Object[] args;
				IRFactory factory = new IRFactory(new CompilerEnvirons());
				AstRoot rootNode = factory.parse(javaFunctionFull, null, 0);
				SimpleNodeVisitor nodeVisitor = new SimpleNodeVisitor();
				rootNode.visit(nodeVisitor);
				LOGGER.info("AstParseForArguments: {}", nodeVisitor.getArgs().toString());
				args = nodeVisitor.getArgs().toArray();

				fct.call(cntx, scope, scope, args);
			} else {
				LOGGER.error(JSCRIPT_MARKER, " Couldn't find function {}", javaFunction);
				guide.updateJConsole("Couldn't find function " + javaFunction);
			}
		} catch (EvaluatorException ex) {
			/*
			 * TODO, we need much better tracability on where the error came from.
			 */
			LOGGER.error(JSCRIPT_MARKER,
					"JavaScriptError line " + ex.lineNumber() + " column " + ex.columnNumber()
							+ " Source " + ex.lineSource() + " error " + ex.getLocalizedMessage());
			guide.updateJConsole(
					"JavaScriptError line " + ex.lineNumber() + " column " + ex.columnNumber()
							+ " Source " + ex.lineSource() + " error " + ex.getLocalizedMessage());
		} catch (Exception ex) {
			LOGGER.error(JSCRIPT_MARKER, " FileRunScript " + ex.getLocalizedMessage(), ex);
			guide.updateJConsole("FileRunScript " + ex.getLocalizedMessage());
			LOGGER.error(" FileRunScript " + ex.getLocalizedMessage(), ex);
		}
		LOGGER.debug(JSCRIPT_MARKER, "Ending ScriptVariables: {}", scriptVars);
		LOGGER.debug(JSCRIPT_MARKER, "Ending Flags {" + guide.getSettings().GetFlags() + "}");
		Context.exit();

		guide.getSettings().SetFlags(comonFunctions.GetFlags(guide.GetFlags()));
		guide.getSettings().saveSettings();
		MainLogic.saveGlobalScriptVariables();
		if (inPrefGuide) {
			userSettings.saveUserSettings();
		}
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public Guide getGuide() {
		return guide;
	}

}
