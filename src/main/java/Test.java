import org.guideme.guideme.settings.GuideSettings;

public class Test {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		GuideSettings gs = new GuideSettings("Test");
		gs.loadSettings("/ram/test.state");

	}
}
