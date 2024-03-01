package org.guideme.guideme.ui.debug_shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.guideme.guideme.model.Button;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.model.Page;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.ui.main_shell.MainShell;

import com.snapps.swt.SquareButton;

public class DebugShell {
	private ComonFunctions comonFuctions;
	private Shell shell = null;
	private Display myDisplay;
	private static Logger logger = LogManager.getLogger();
	private Combo pagesCombo;
	private Guide guide;
	private Text txtText;
	private Text txtScript;
	private Text txtScriptConsole;
	private Text txtVarKey;
	private Text txtVarValue;
	private MainShell mainShell;
	private Composite varComp;
	private ScrolledComposite varScrlComp;
	private TabFolder tabFolder;
	private Table varTable;
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private static DebugShell debugShell;
	private boolean keepShellOpen;
	private DebugTab[] debugTabs;
	private MainTab mainTab;

	public static synchronized DebugShell getDebugShell() {
		if (debugShell == null) {
			debugShell = new DebugShell();
		}
		return debugShell;
	}

	protected DebugShell() {
		super();
	}

	public void createShell(final Display display, MainShell mainshell) {
		logger.trace("Enter createShell");
		keepShellOpen = true;
		comonFuctions = ComonFunctions.getComonFunctions();
		AppSettings appSettings = AppSettings.getAppSettings();
		ResourceBundle displayText = appSettings.getDisplayText();

		// Create the main UI elements
		myDisplay = display;

		guide = Guide.getGuide();
		mainShell = mainshell;
		shell = new Shell(myDisplay);
		shell.setText("GuideMe Debug Shell");
		shell.setSize(appSettings.getJsDebugWidth(), appSettings.getJsDebugHeight());
		shell.addListener(SWT.Close, event -> {
			shell.setVisible(false);
			event.doit = !keepShellOpen;
		});

		FormLayout layout = new FormLayout();
		shell.setLayout(layout);

		pagesCombo = new Combo(shell, SWT.DROP_DOWN);
		pagesCombo.addSelectionListener(new DebugSelectListener());

		// Set the layout and how it responds to screen resize
		FormData pagesComboFormData = new FormData();
		pagesComboFormData.top = new FormAttachment(0, 0);
		pagesComboFormData.left = new FormAttachment(0, 2);
		pagesComboFormData.right = new FormAttachment(80, -2);
		pagesCombo.setLayoutData(pagesComboFormData);

		SquareButton btnGo = new SquareButton(shell, SWT.PUSH);
		btnGo.setText(displayText.getString("DebugShellButtonGo"));
		FormData btnGoFormData = new FormData();
		btnGoFormData.top = new FormAttachment(0, 0);
		btnGoFormData.right = new FormAttachment(90, -2);
		btnGoFormData.left = new FormAttachment(80, 0);
		btnGo.setLayoutData(btnGoFormData);
		btnGo.addSelectionListener(new GoButtonListener());

		SquareButton btnCurrent = new SquareButton(shell, SWT.PUSH);
		btnCurrent.setText(displayText.getString("DebugShellButtonReset"));
		FormData btnCurrentFormData = new FormData();
		btnCurrentFormData.top = new FormAttachment(0, 0);
		btnCurrentFormData.right = new FormAttachment(100, -2);
		btnCurrentFormData.left = new FormAttachment(90, 0);
		btnCurrent.setLayoutData(btnCurrentFormData);
		btnCurrent.addSelectionListener(new CurrentButtonListener());

		tabFolder = new TabFolder(shell, SWT.NONE);
		FormLayout mainlayout = new FormLayout();
		tabFolder.setLayout(mainlayout);
		FormData mainCompFormData = new FormData();
		mainCompFormData.top = new FormAttachment(pagesCombo, 0);
		mainCompFormData.left = new FormAttachment(0, 0);
		mainCompFormData.right = new FormAttachment(100, 0);
		mainCompFormData.bottom = new FormAttachment(100, 0);
		tabFolder.setLayoutData(mainCompFormData);

		mainTab = new MainTab(tabFolder, appSettings);
		debugTabs = new DebugTab[] { mainTab };

		// Text Tab
		TabItem tabText = new TabItem(tabFolder, SWT.NONE);
		tabText.setText(displayText.getString("DebugShellTabText"));

		txtText = new Text(tabFolder,
				SWT.LEFT + SWT.MULTI + SWT.WRAP + SWT.READ_ONLY + SWT.V_SCROLL);
		FormData lblTexctFormData = new FormData();
		lblTexctFormData.top = new FormAttachment(0, 0);
		lblTexctFormData.left = new FormAttachment(0, 0);
		lblTexctFormData.right = new FormAttachment(100, 0);
		lblTexctFormData.bottom = new FormAttachment(100, 0);
		txtText.setLayoutData(lblTexctFormData);

		tabText.setControl(txtText);

		// Script Tab
		TabItem tabScript = new TabItem(tabFolder, SWT.NONE);
		tabScript.setText("JavaScript");

		txtScript = new Text(tabFolder,
				SWT.LEFT + SWT.MULTI + SWT.WRAP + SWT.READ_ONLY + SWT.V_SCROLL);
		FormData txtScriptFormData = new FormData();
		txtScriptFormData.top = new FormAttachment(0, 0);
		txtScriptFormData.left = new FormAttachment(0, 0);
		txtScriptFormData.right = new FormAttachment(100, 0);
		txtScriptFormData.bottom = new FormAttachment(100, 0);
		txtScript.setLayoutData(txtScriptFormData);

		tabScript.setControl(txtScript);

		// Variables Tab
		TabItem tabVariables = new TabItem(tabFolder, SWT.NONE);
		tabVariables.setText(displayText.getString("DebugShellTabVariables"));

		varScrlComp = new ScrolledComposite(tabFolder, SWT.V_SCROLL | SWT.H_SCROLL);

		varComp = new Composite(varScrlComp, SWT.NONE);
		FormLayout varlayout = new FormLayout();
		varComp.setLayout(varlayout);
		FormData varCompFormData = new FormData();
		varCompFormData.top = new FormAttachment(0, 0);
		varCompFormData.left = new FormAttachment(0, 0);
		varCompFormData.right = new FormAttachment(100, 0);
		varCompFormData.bottom = new FormAttachment(100, 0);
		varComp.setLayoutData(varCompFormData);

		varTable = new Table(varComp, SWT.NONE);
		varTable.setLinesVisible(true);
		varTable.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		varTable.setLayoutData(data);
		String[] titles = { displayText.getString("DebugShellTableName"),
				displayText.getString("DebugShellTableValue") };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(varTable, SWT.NONE);
			column.setText(titles[i]);
		}
		FormData varTableFormData = new FormData();
		varTableFormData.top = new FormAttachment(0, 0);
		varTableFormData.left = new FormAttachment(0, 0);
		varTableFormData.right = new FormAttachment(100, 0);
		varTable.setLayoutData(varTableFormData);
		varTable.addSelectionListener(new VarTableListener());

		txtVarKey = new Text(varComp, SWT.LEFT + SWT.SINGLE + SWT.BORDER);
		FormData txtVarKeyFormData = new FormData();
		txtVarKeyFormData.top = new FormAttachment(varTable, 0);
		txtVarKeyFormData.left = new FormAttachment(0, 0);
		txtVarKeyFormData.right = new FormAttachment(30, -2);
		txtVarKey.setLayoutData(txtVarKeyFormData);

		txtVarValue = new Text(varComp, SWT.LEFT + SWT.SINGLE + SWT.BORDER);
		FormData txtVarValueFormData = new FormData();
		txtVarValueFormData.top = new FormAttachment(varTable, 0);
		txtVarValueFormData.left = new FormAttachment(30, 0);
		txtVarValueFormData.right = new FormAttachment(90, -2);
		txtVarValue.setLayoutData(txtVarValueFormData);

		SquareButton btnSet = new SquareButton(varComp, SWT.PUSH);
		btnSet.setText(displayText.getString("DebugShellButtonSetValue"));
		FormData btnSetFormData = new FormData();
		btnSetFormData.top = new FormAttachment(varTable, 0);
		btnSetFormData.right = new FormAttachment(100, -2);
		btnSetFormData.left = new FormAttachment(90, 0);
		btnSet.setLayoutData(btnSetFormData);
		btnSet.addSelectionListener(new SetButtonListener());

		varScrlComp.setContent(varComp);
		varScrlComp.setAlwaysShowScrollBars(true);
		varScrlComp.setExpandHorizontal(true);
		varScrlComp.setExpandVertical(true);
		tabVariables.setControl(varScrlComp);

		// Jscript console Tab
		TabItem tabConsole = new TabItem(tabFolder, SWT.NONE);
		tabConsole.setText("JavaScript Console");

		txtScriptConsole = new Text(tabFolder,
				SWT.LEFT + SWT.MULTI + SWT.WRAP + SWT.READ_ONLY + SWT.V_SCROLL);
		FormData txtScriptConsoleFormData = new FormData();
		txtScriptConsoleFormData.top = new FormAttachment(0, 0);
		txtScriptConsoleFormData.left = new FormAttachment(0, 0);
		txtScriptConsoleFormData.right = new FormAttachment(100, 0);
		txtScriptConsoleFormData.bottom = new FormAttachment(100, 0);
		txtScriptConsole.setLayoutData(txtScriptConsoleFormData);

		tabConsole.setControl(txtScriptConsole);

		varComp.layout();
		varScrlComp.layout();
		tabFolder.layout();
		shell.layout();
		shell.open();
		shell.setVisible(false);
		logger.trace("Exit createShell");
	}

	class VarTableListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			TableItem tmpItem;
			tmpItem = (TableItem) e.item;
			txtVarKey.setText(tmpItem.getText(0));
			txtVarValue.setText(tmpItem.getText(1));
			super.widgetSelected(e);
		}

	}

	class GoButtonListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				String strPage;
				strPage = pagesCombo.getItem(pagesCombo.getSelectionIndex());
				mainShell.displayPage(strPage);
			} catch (Exception ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}

	}

	class SetButtonListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				String flags = comonFuctions.getFlags(guide.getFlags());
				Map<String, Object> scriptVars = guide.getSettings().getScriptVariables();

				Color color = myDisplay.getSystemColor(SWT.COLOR_YELLOW);

				if (txtVarKey.getText().equals("Flags")) {
					List<String> flagsarray = new ArrayList<>();
					comonFuctions.setFlags(txtVarValue.getText(), flagsarray);
					guide.setFlags(flagsarray);
					flags = comonFuctions.getFlags(guide.getFlags());
				} else {
					scriptVars.put(txtVarKey.getText(), txtVarValue.getText());
					guide.getSettings().setScriptVariables(scriptVars);
					scriptVars = guide.getSettings().getScriptVariables();
				}
				varTable.removeAll();

				TableItem item = new TableItem(varTable, SWT.NONE);
				item.setBackground(color);
				item.setText(0, "Flags");
				item.setText(1, flags);

				for (Entry<String, Object> entry : scriptVars.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					item = new TableItem(varTable, SWT.NONE);
					item.setBackground(color);
					item.setText(0, key);
					item.setText(1, value);
				}

				for (int i = 0; i < 2; i++) {
					varTable.getColumn(i).pack();
				}

			} catch (Exception ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}

	}

	class CurrentButtonListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				String strPage;
				strPage = guide.getSettings().getCurrPage();
				setPage(strPage, true);
			} catch (Exception ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}

	}

	class DebugSelectListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				String strPage;
				strPage = pagesCombo.getItem(pagesCombo.getSelectionIndex());
				setPage(strPage, false);
			} catch (Exception ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}

	}

	public void clearPagesCombo() {
		try {
			this.pagesCombo.removeAll();
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

	public void addPagesCombo(String page) {
		try {
			this.pagesCombo.add(page);
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

	public void setPage(String page, boolean currPage) {
		Page dispPage;
		if (currPage) {
			int currPageIndex = this.pagesCombo.indexOf(page);
			this.pagesCombo.select(currPageIndex);
		}
		dispPage = guide.getChapters().get("default").getPages().get(page);
		StringBuilder txtBuilder = new StringBuilder();
		for (int i = 0; i < dispPage.getTextCount(); i++) {
			if (dispPage.getText(i).canShow(guide.getFlags())) {
				txtBuilder.append(dispPage.getText(i).getText());
			}
		}
		txtText.setText(txtBuilder.toString());

		for (DebugTab tab : debugTabs) {
			tab.onPageChange(dispPage, currPage);
		}

		// Java Script
		txtScript.setText(dispPage.getjScript());

		tabFolder.layout();
		tabFolder.pack();
		tabFolder.update();
		shell.layout();
		varScrlComp.setMinSize(varComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	public void loadChapter(Chapter chapter) {
		if (chapter == null) {
			return;
		}
		for (Page page : chapter.getPages().values()) {
			addPagesCombo(page.getId());
		}
		setPage(guide.getCurrPage(), true);
	}

	public void updateJConsole(String logText) {
		String conText = txtScriptConsole.getText();
		String conDelim = txtScriptConsole.getLineDelimiter();
		if (txtScriptConsole.getLineCount() > 100) {
			int lastLine = conText.lastIndexOf(conDelim);
			lastLine = conText.lastIndexOf(conDelim, lastLine - 1);
			conText = conText.substring(0, lastLine);
		}
		conText = logText + conDelim + conText;
		txtScriptConsole.setText(conText);
	}

	public void clearJConsole() {
		txtScriptConsole.setText("");
	}

	public void refreshVars() {
		try {
			Color color = myDisplay.getSystemColor(SWT.COLOR_YELLOW);
			Map<String, Object> treeMap = new TreeMap<>(guide.getSettings().getScriptVariables());
			String flags = comonFuctions.getFlags(guide.getFlags());

			varTable.removeAll();

			TableItem item = new TableItem(varTable, SWT.NONE);
			item.setBackground(color);
			item.setText(0, "Flags");
			item.setText(1, flags);

			for (Entry<String, Object> entry : treeMap.entrySet()) {
				String key = entry.getKey();
				String value;
				Object objVal = entry.getValue();
				if (objVal != null) {
					value = comonFunctions.getVarAsString(objVal);
				} else {
					value = "null";
				}
				item = new TableItem(varTable, SWT.NONE);
				item.setBackground(color);
				item.setText(0, key);
				item.setText(1, value);
			}

			for (int i = 0; i < 2; i++) {
				varTable.getColumn(i).pack();
			}
			tabFolder.layout();
			tabFolder.pack();
			tabFolder.update();
			shell.layout();
			varScrlComp.setMinSize(varComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

	public void closeShell() {
		try {
			keepShellOpen = false;
			shell.close();
		} catch (Exception ex) {
			logger.error("close shell " + ex.getLocalizedMessage(), ex);
		}
	}

	public void showDebug() {
		if (shell == null) {
			createShell(myDisplay, mainShell);
		} else {
			shell.setVisible(!shell.getVisible());
			if (shell.isVisible()) {
				shell.setActive();
			}
		}
	}

	public void setKeepShellOpen(Boolean keepShellOpen) {
		this.keepShellOpen = keepShellOpen;
	}

	public void addOverrideButton(Button button) {
		mainTab.addOverrideButton(button);
		shell.layout();
		varScrlComp.setMinSize(varComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public void addOverrideButtons(List<Button> button) {
		for(Button b : button) {
			addOverrideButton(b);
		}
		
	}
}
