import org.guideme.guideme.settings.GuideSettings;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Test {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		GuideSettings gs = new GuideSettings("Test");
		gs.loadSettings("/ram/test.state");

	}
}
