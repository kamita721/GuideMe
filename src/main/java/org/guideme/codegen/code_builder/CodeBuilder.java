package org.guideme.codegen.code_builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CodeBuilder {
	private final StringBuilder builder = new StringBuilder();

	private int indentLevel = 0;

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
			}else {
				break;
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

	@Override
	public String toString() {
		return builder.toString();
	}

	public void generate(File srcRoot, String[] packagePath, String fileName) throws IOException {
		File dst = srcRoot;
		for (String packageComponent : packagePath) {
			dst = new File(dst, packageComponent);
		}
		dst.mkdirs();
		dst = new File(dst, fileName + ".java");
		try(FileOutputStream os= new FileOutputStream(dst)){
			os.write(toString().getBytes());
		}
	}

}
