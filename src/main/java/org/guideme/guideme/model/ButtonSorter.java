package org.guideme.guideme.model;

import java.util.Comparator;

import org.guideme.generated.model.GlobalButton;
import org.guideme.generated.model.Button;

public class ButtonSorter implements Comparator<Button> {

	private int getMetaSort(Button b) {
		if (b instanceof GlobalButton gb) {
			switch(gb.getPlacement()) {
			case BOTTOM:
				return 1;
			case TOP:
				return -1;
			}
		}
		return 0;
	}

	private int getSort(Button b) {
		return b.getSortOrder();
	}

	@Override
	public int compare(Button o1, Button o2) {
		int sort1 = getMetaSort(o1);
		int sort2 = getMetaSort(o2);
		if(sort1 == sort2) {
			sort1 = getSort(o1);
			sort2 = getSort(o2);
		}
		return sort1 - sort2;
	}

}
