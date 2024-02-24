import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Test {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		//test
		Context ctx = Context.enter();
		Scriptable globalScope = ctx.initSafeStandardObjects();
		
		@SuppressWarnings("rawtypes")
		Class[] cArg = new Class[1];
		cArg[0] = String.class;
		java.lang.reflect.Method tjlog = Inner.class.getMethod("log", cArg);
		
		Inner inner = new Inner();
		
		Scriptable logScope = ctx.newObject(globalScope);
		logScope.setPrototype(globalScope);
		logScope.setParentScope(null);
		logScope.put("this", logScope, inner);
		globalScope.put("this", globalScope, inner);
		FunctionObject jlog = new FunctionObject("log", tjlog, logScope);
		ScriptableObject.putProperty(globalScope, "log", jlog);
		
		
		Object ans = ctx.evaluateString(globalScope, "log(this)", null, 0, null);
		System.err.println(ans);
	}
	
}

class Inner{
	public static  void log(String s) {
		System.out.println(s);
	}
	
}