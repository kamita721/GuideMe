
/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Browser example snippet: modify DOM (executing javascript)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Test {

	private static Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) {
		try {
			fail();
		}catch(Exception e) {
			logger.error("fail {}", "foo", 1, "hello",  e);
		}
	}
	
	public static void fail() {
		throw new RuntimeException("E");
	}
}