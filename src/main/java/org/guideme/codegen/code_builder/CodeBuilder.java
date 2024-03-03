package org.guideme.codegen.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeBuilder {	
	private static Logger logger = LogManager.getLogger();

	private final StringBuilder builder = new StringBuilder();

	private final String[] packageName;
	private final String className;
	private final Set<String> imports = new HashSet<>();

	private int indentLevel = 0;

	public CodeBuilder(String[] packageName, String className) {
		this.packageName = packageName;
		this.className = className;
		
		logger.info("Entering CodeBuilder for {}.{}", getPackageName(), className);
	}

	public void addLine(String fmt, Object... args) {
		String toAdd = fmt.formatted(args);
		/* Adjust the indentation specifically for this line. */
		int lineDeindent = 0;
		for (char c : toAdd.toCharArray()) {
			/*
			 * When a line starts with a close bracket, we want to de-indent it immediatly.
			 * Even though the indent is added back after the line, we still subtract it
			 * again during normal indent processing.
			 */
			if (c == '}') {
				lineDeindent++;
			}
		}

		/*
		 * Standard coding style is that case statements are written at the parent
		 * scopes indentation level. The case body still gets indented, as this applies
		 * to the current line only.
		 */
		if (toAdd.startsWith("case")) {
			lineDeindent++;
		}
		indentLevel -= lineDeindent;

		checkIndent();

		for (int i = 0; i < indentLevel; i++) {
			builder.append('\t');
		}
		// We'll see these close brackets again a few lines down.
		indentLevel += lineDeindent;

		builder.append(toAdd);
		builder.append('\n');
		for (char c : toAdd.toCharArray()) {
			if (c == '{') {
				indentLevel++;
			}
			if (c == '}') {
				indentLevel--;
				checkIndent();
			}
		}
	}

	public <T extends ImportProvider> void addInterfaceImports(Collection<T> providers) {
		for (ImportProvider provider : providers) {
			imports.addAll(provider.getInterfaceImports());
		}
	}

	public <T extends ImportProvider> void addClassImports(Collection<T> providers) {
		for (ImportProvider provider : providers) {
			imports.addAll(provider.getClassImports());
		}
	}

	public void addImport(String imp) {
		imports.add(imp);
	}

	public void addLine() {
		builder.append('\n');
	}

	public void reduceIndent() {
		indentLevel--;
		checkIndent();
	}

	public void increaseIndent() {
		indentLevel++;
	}

	private void checkIndent() {
		if (indentLevel < 0) {
			throw new IllegalStateException("Negative indentation");
		}
	}

	private String getPackageName() {
		return String.join(".", packageName);
	}

	@Override
	public String toString() {
		StringBuilder ans = new StringBuilder();
		ans.append("package " + getPackageName() + ";");
		ans.append('\n');
		for (String s : imports) {
			ans.append("import " + s + ";");
		}
		ans.append(builder);
		return builder.toString();
	}

	public void generate(File srcRoot) throws IOException {
		File dir = srcRoot;
		for (String compontent : packageName) {
			dir = new File(dir, compontent);
		}
		dir.mkdirs();
		File outputFile = new File(dir, className + ".java");
		try (FileOutputStream os = new FileOutputStream(outputFile)) {
			os.write(toString().getBytes());
		}
	}

}
