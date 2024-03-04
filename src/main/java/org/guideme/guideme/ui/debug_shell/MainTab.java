package org.guideme.guideme.ui.debug_shell;

import java.util.Collection;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.guideme.generated.model.Button;
import org.guideme.generated.model.Page;
import org.guideme.guideme.settings.AppSettings;

public class MainTab implements DebugTab {
	private final Composite content;
	private final TabItem tabItem;
	private Table btnTable;
	private final TabFolder container;

	/*
	 * When adding a data table, we need to know what the bottom most widget we
	 * already have is, so that we can properly position the new one.
	 */
	private Composite bottomWidget;

	public MainTab(TabFolder container, AppSettings appSettings) {
		this.container = container;

		tabItem = new TabItem(container, SWT.NONE);
		tabItem.setText(appSettings.getDisplayText().getString("DebugShellTabMain"));

		content = new Composite(container, SWT.SHADOW_NONE);
		FormLayout tbllayout = new FormLayout();
		content.setLayout(tbllayout);
		FormData tableCompFormData = new FormData();
		tableCompFormData.top = new FormAttachment(0, 0);
		tableCompFormData.left = new FormAttachment(0, 0);
		tableCompFormData.right = new FormAttachment(100, 0);
		tableCompFormData.bottom = new FormAttachment(100, 0);
		content.setLayoutData(tableCompFormData);

		tabItem.setControl(content);
	}

	@Override
	public Composite getContent() {
		return content;
	}

	@Override
	public void onPageChange(Page dispPage, boolean currPage) {
		for (Control kid : content.getChildren()) {
			kid.dispose();
		}

		bottomWidget = content;

		constructTable(new String[] { "Page", "set", "unset", "If Set", "If not set" },
				new Page[] { dispPage }, page -> new String[] { page.getId(), page.getSet(),
						page.getUnSet(), page.getIfSet(), page.getIfNotSet() });

		btnTable = constructTable(
				new String[] { "Button", "target", "jScript", "set", "unset", "If Set",
						"If not set", "image", "hotkey" },
				dispPage.getButtons(),
				btn -> new String[] { btn.getText(), btn.getTarget(), btn.getJScript(),
						btn.getSet(), btn.getUnSet(), btn.getIfSet(), btn.getIfNotSet(),
						btn.getImage(), btn.getHotkey() });
		constructTable(
				new String[] { "Delay", "style", "target", "jScript", "startWith", "set", "unset",
						"If Set", "If not set" },
				dispPage.getDelays(),
				delay -> new String[] { String.valueOf(delay.getDelaySec()), delay.getStyle(),
						delay.getTarget(), delay.getJscript(),
						Integer.toString(delay.getStartWith()), delay.getSet(), delay.getUnSet(),
						delay.getIfSet(), delay.getIfNotSet() });
		constructTable(new String[] { "Image", "If Set", "If not set" }, dispPage.getImages(),
				img -> new String[] { img.getId(), img.getIfSet(), img.getIfNotSet() });
		constructTable(
				new String[] { "audio", "target", "jScript", "startAt", "stopAt", "repeat",
						"If Set", "If not set" },
				dispPage.getAudios(),
				audio -> new String[] { audio.getId(), audio.getTarget(), audio.getJscript(),
						audio.getStartAt(), audio.getStopAt(), audio.getRepeat(), audio.getIfSet(),
						audio.getIfNotSet() });
		constructTable(
				new String[] { "video", "target", "jScript", "startAt", "stopAt", "repeat",
						"If Set", "If not set" },
				dispPage.getVideos(),
				video -> new String[] { video.getId(), video.getTarget(), video.getJscript(),
						video.getStartAt(), video.getStopAt(), video.getRepeat(),
						video.getIfSet(), video.getIfNotSet() });
		constructTable(
				new String[] { "metronome", "If Set", "If Unset", "Resolution", "Loops", "Rhythm" },
				dispPage.getMetronomes(),
				metronome -> new String[] { String.valueOf(metronome.getBpm()),
						metronome.getIfSet(), metronome.getIfNotSet(),
						String.valueOf(metronome.getResolution()),
						String.valueOf(metronome.getLoops()), metronome.getRhythm() });

		content.pack();
	}

	public void addOverrideButton(Button button) {
		Color color = content.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		TableItem item = new TableItem(btnTable, SWT.NONE);
		item.setBackground(color);
		item.setText(0, button.getText());
		item.setText(1, button.getTarget());
		item.setText(2, button.getJScript());
		item.setText(3, button.getSet());
		item.setText(4, button.getUnSet());
		item.setText(5, button.getIfSet());
		item.setText(6, button.getIfNotSet());
		item.setText(7, button.getImage());
		item.setText(8, button.getHotkey());
		container.layout();
		container.pack();
		container.update();
	}

	/**
	 * Construct a table that shows information about the provided arguements.
	 * 
	 * It is expected that each invocation of getter returns an array the same size
	 * as headers, and with corresponding elements, however this is not enforced.
	 * 
	 * @param <T>     - Type of object being introspected
	 * @param headers - A human readable list of field names being shown
	 * @param objs    - The objects being instrospected
	 * @param getter  - Function that returns an array of human readable
	 *                information.
	 * @return
	 */
	private <T> Table constructTable(String[] headers, T[] objs, Function<T, String[]> getter) {
		Color dataColor = content.getDisplay().getSystemColor(SWT.COLOR_YELLOW);

		Table ans = new Table(content,
				SWT.HIDE_SELECTION | SWT.NO_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		ans.setLinesVisible(true);
		ans.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		ans.setLayoutData(data);
		for (int i = 0; i < headers.length; i++) {
			TableColumn column = new TableColumn(ans, SWT.NONE);
			column.setText(headers[i]);
		}

		for (T obj : objs) {
			TableItem item = new TableItem(ans, SWT.NONE);
			item.setBackground(dataColor);
			item.setText(getter.apply(obj));
		}
		for (int i = 0; i < ans.getColumnCount(); i++) {
			ans.getColumn(i).pack();
		}

		FormData formData = new FormData();
		formData.top = new FormAttachment(bottomWidget, 10);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		Point size = ans.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		formData.width = size.x;
		ans.setSize(size);
		ans.setLayoutData(formData);
		ans.pack();
		bottomWidget = ans;

		return ans;
	}
	
	@SuppressWarnings("unchecked")
	private <T> Table constructTable(String[] headers, Collection<T> objs, Function<T, String[]> getter) {
		return constructTable(headers, (T[])objs.toArray(), getter);
	}
}
