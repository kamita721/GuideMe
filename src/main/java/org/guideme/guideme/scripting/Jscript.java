package org.guideme.guideme.scripting;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.guideme.guideme.MainLogic;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.ui.main_shell.MainShell;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.tools.debugger.Main;

public class Jscript implements Runnable {
	private static Guide guide;
	private static GuideSettings guideSettings;
	private static UserSettings userSettings;
	private static AppSettings appSettings;
	private static OverRide overRide;
	private static Logger logger = LogManager.getLogger();
	private static final Marker JSCRIPT_MARKER = MarkerManager.getMarker("JSCRIPT");
	private static boolean inPrefGuide;
	private String javaScriptText;
	private String javaFunction;
	private boolean pageloading;
	private boolean running;
	private Main dbg;

	public Jscript(Guide Iguide, UserSettings IuserSettings, AppSettings IappSettings,
			boolean IinPrefGuide, MainShell ImainShell, OverRide IoverRide, String ijavaScriptText,
			String ijavaFunction, boolean ipageloading) {
		super();
		guide = Iguide;
		guideSettings = Iguide.getSettings();
		userSettings = IuserSettings;
		appSettings = IappSettings;
		inPrefGuide = IinPrefGuide;
		guide.setMainshell(ImainShell);
		overRide = IoverRide;
		javaScriptText = ijavaScriptText;
		javaFunction = ijavaFunction;
		pageloading = ipageloading;
		running = true;
	}

	public void changePreSettings(GuideSettings IguideSettings) {
		guideSettings = IguideSettings;
	}

	public static void jscriptLog(String strMessage) {
		logger.info(JSCRIPT_MARKER, strMessage);
		guide.updateJConsole(strMessage);
	}

	public class SimpleNodeVisitor implements NodeVisitor {
		private boolean foundCall = false;
		private ArrayList<Object> args = new ArrayList<Object>();

		@Override
		public boolean visit(AstNode node) {
			if (node == null)
				return false;

			int nodeType = node.getType();
			if (nodeType == Token.CALL && !foundCall) {
				foundCall = true;
				for (AstNode n2 : ((FunctionCall) node).getArguments()) {
					if (guideSettings.isConvertArgumentTypes())
						args.add(nodeToObj(n2));
					else
						args.add(n2.toSource());
				}
			}

			return true;
		}

		public ArrayList<Object> getArgs() {
			return args;
		}

		private Object nodeToObj(AstNode node) {
			return nodeToObj(node, true);
		}

		private Object nodeToObj(AstNode node, boolean convertName) {
			// TODO: Ideally, there should be a better way to handle this than this
			// obnoxious chain of if/else statements.
			if (node instanceof StringLiteral)
				return ((StringLiteral) node).getValue(); // Will not include enclosing quotes.
			else if (node instanceof NumberLiteral)
				return ((NumberLiteral) node).getNumber();
			else if (node instanceof ArrayLiteral) {
				ArrayList<Object> arr = new ArrayList<Object>();
				for (AstNode ae : ((ArrayLiteral) node).getElements())
					arr.add(nodeToObj(ae));
				return arr.toArray();
			} else if (node instanceof KeywordLiteral) {
				int nodeType = node.getType();
				if (nodeType == Token.NULL)
					return null;
				else if (nodeType == Token.TRUE)
					return true;
				else if (nodeType == Token.FALSE)
					return false;
				else
					return node.toSource();
			} else if (node instanceof ObjectLiteral) {
				HashMap<Object, Object> obj = new HashMap<Object, Object>();
				for (ObjectProperty op : ((ObjectLiteral) node).getElements())
					obj.put(nodeToObj(op.getLeft(), false), nodeToObj(op.getRight()));
				return obj;
			} else if (node instanceof Name) {
				String key = ((Name) node).getIdentifier();
				if (convertName)
					return guideSettings.getScriptVariables().get(key);
				else
					return key;
			} else
				return node.toSource();
		}
	}

	public void run() {
		try {
			String javaScriptToRun = javaScriptText;
			// logger.trace(JSCRIPT_MARKER, "Chapter: " + guideSettings.getChapter());
			logger.debug(JSCRIPT_MARKER, "Page: " + guideSettings.getCurrPage());
			logger.debug(JSCRIPT_MARKER, "javaFunction: " + javaFunction);
			logger.debug(JSCRIPT_MARKER, "pageloading: " + pageloading);
			logger.trace(JSCRIPT_MARKER, "javaScriptText: " + javaScriptText);
			if (!guideSettings.isGlobalScriptLogged()) {
				logger.trace(JSCRIPT_MARKER, "globalJavaScriptText: " + guide.getGlobaljScript());
				guideSettings.setGlobalScriptLogged(true);
			}
			ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
			Map<String, Object> scriptVars;
			scriptVars = guideSettings.getScriptVariables();
			ContextFactory cntxFact = new ContextFactory();

			Scriptable globalScope = guideSettings.getGlobalScope();

			if (appSettings.getJsDebug()) {
				dbg = new Main("GuideMe");
				dbg.attachTo(cntxFact);
			}

			Context cntx = cntxFact.enterContext();
			cntx.setOptimizationLevel(-1);
			cntx.getWrapFactory().setJavaPrimitiveWrap(false);

			if (globalScope.get("GuideObjects", globalScope) != "true") {
				ScriptableObject.putProperty(globalScope, "GuideObjects", "true");
				ScriptableObject.putProperty(globalScope, "comonFunctions", comonFunctions);
				ScriptableObject.putProperty(globalScope, "fileSeparator",
						java.lang.System.getProperty("file.separator"));
				ScriptableObject.putProperty(globalScope, "guide", guide);
				ScriptableObject.putProperty(globalScope, "guideSettings", guideSettings);
				ScriptableObject.putProperty(globalScope, "mediaDir",
						appSettings.getDataDirectory());
				ScriptableObject.putProperty(globalScope, "globalVars",
						MainLogic.getGlobalScriptVariables());
				@SuppressWarnings("rawtypes")
				Class[] cArg = new Class[1];
				cArg[0] = String.class;
				java.lang.reflect.Method tjlog = Jscript.class.getMethod("jscriptLog", cArg);
				FunctionObject jlog = new FunctionObject("jscriptLog", tjlog, globalScope);
				ScriptableObject.putProperty(globalScope, "jscriptLog", jlog);
				cArg = null;
				jlog = null;

			}
			ScriptableObject.putProperty(globalScope, "scriptVars", scriptVars);

			Scriptable parentScope = guideSettings.getScope();
			if (parentScope == null) {
				// parentScope = cntx.initStandardObjects();;
				parentScope = cntx.newObject(globalScope);
			}
			parentScope.setParentScope(globalScope);

			if (appSettings.getJsDebug()) {
				dbg.setBreakOnExceptions(appSettings.getJsDebugError());
				dbg.setBreakOnEnter(appSettings.getJsDebugEnter());
				dbg.setBreakOnReturn(appSettings.getJsDebugExit());
				dbg.setScope(parentScope);
				dbg.setSize(appSettings.getJsDebugWidth(), appSettings.getJsDebugHeight());
				dbg.setVisible(true);
				JFrame jfWindow = dbg.getDebugFrame();
				if (appSettings.getMainMonitor() == 1) {
					showOnScreen(1, jfWindow);
				} else {
					showOnScreen(0, jfWindow);
				}
				dbg.setExitAction(new DontExitOnClose());
			}

			cntx.evaluateString(parentScope, guide.getGlobaljScript(), "globalScript", 1, null);
			guideSettings.setScope(parentScope);

			// Scriptable scope = cntx.initStandardObjects();
			// scope.setParentScope(parentScope);;
			Scriptable scope = cntx.newObject(parentScope);
			// scope.setPrototype(parentScope);
			scope.setParentScope(parentScope);

			if (!inPrefGuide) {
				UserSettings cloneUS = userSettings.clone();
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
			ScriptableObject.putProperty(scope, "guideSettings", guideSettings);

			ScriptableObject.putProperty(scope, "scriptVars", scriptVars);
			ScriptableObject.putProperty(scope, "guide", guide);
			// Deprecated should use guide now
			ScriptableObject.putProperty(scope, "guideSettings", guideSettings);
			logger.debug(JSCRIPT_MARKER, "Starting ScriptVariables: " + scriptVars);
			logger.debug(JSCRIPT_MARKER, "Starting Flags {" + guideSettings.getFlags() + "}");

			if (pageloading) {
				ScriptableObject.putProperty(globalScope, "overRide", overRide);
			}

			try {

				cntx.evaluateString(scope, javaScriptToRun, "pageScript", 1, null);

				int argStart;
				int argEnd;
				String argstring = "";
				String[] argArray;
				String javaFunctionFull = javaFunction;
				argStart = javaFunction.indexOf("(");
				argEnd = javaFunction.indexOf(")");
				if (argStart > -1) {
					argstring = javaFunction.substring(argStart + 1, argEnd);
					javaFunction = javaFunction.substring(0, argStart);
				}
				Object fObj = scope.get(javaFunction, scope);
				if (!(fObj instanceof Function)) {
					fObj = parentScope.get(javaFunction, parentScope);
				}
				if ((fObj instanceof Function)) {
					Object args[] = { "" };
					IRFactory factory = new IRFactory(new CompilerEnvirons());
					AstRoot rootNode = factory.parse(javaFunctionFull, null, 0);
					SimpleNodeVisitor nodeVisitor = new SimpleNodeVisitor();
					rootNode.visit(nodeVisitor);
					logger.info("AstParseForArguments: " + nodeVisitor.getArgs().toString());
					args = nodeVisitor.getArgs().toArray();

					Function fct = (Function) fObj;
					fct.call(cntx, scope, scope, args);
				} else {
					logger.error(JSCRIPT_MARKER, " Couldn't find function " + javaFunction);
					guide.updateJConsole("Couldn't find function " + javaFunction);
				}
			} catch (EvaluatorException ex) {
				logger.error(JSCRIPT_MARKER,
						"JavaScriptError line " + ex.lineNumber() + " column " + ex.columnNumber()
								+ " Source " + ex.lineSource() + " error "
								+ ex.getLocalizedMessage());
				guide.updateJConsole("JavaScriptError line " + ex.lineNumber() + " column "
						+ ex.columnNumber() + " Source " + ex.lineSource() + " error "
						+ ex.getLocalizedMessage());
			} catch (Exception ex) {
				logger.error(JSCRIPT_MARKER, " FileRunScript " + ex.getLocalizedMessage(), ex);
				guide.updateJConsole("FileRunScript " + ex.getLocalizedMessage());
				logger.error(" FileRunScript " + ex.getLocalizedMessage(), ex);
			}
			logger.debug(JSCRIPT_MARKER, "Ending ScriptVariables: " + scriptVars);
			logger.debug(JSCRIPT_MARKER, "Ending Flags {" + guideSettings.getFlags() + "}");
			Context.exit();

			if (appSettings.getJsDebug()) {
				dbg.detach();
				dbg.dispose();
			}

			guideSettings.setFlags(comonFunctions.getFlags(guide.getFlags()));
			guideSettings.saveSettings();
			MainLogic.saveGlobalScriptVariables();
			if (inPrefGuide) {
				userSettings.saveUserSettings();
			}
		} catch (Exception ex) {
			logger.error(" FileRunScript " + ex.getLocalizedMessage(), ex);
		}
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public void setOverRide(OverRide overRide) {
		Jscript.overRide = overRide;
	}

	private static class DontExitOnClose implements Runnable {
		@Override
		public void run() {
			// System.exit(0);
		}
	}

	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}

}
