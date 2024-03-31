package org.guideme.guideme.util;

import static org.junit.Assert.*;

import org.guideme.generated.model.Page;
import org.guideme.guideme.model.Guide;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

public class PageFilterTest {
	private final String CHAPTER = "default";
	private final PageFilter pageFilter = new PageFilter();
	private final Guide guide = Guide.getGuide();

	@Before
	public void setUp() {
		addShowablePage("showable");
		addUnshowablePage("unshowable");

		for (int i = 1; i <= 10; i++) {
			addShowablePage("page" + i);
		}
		for (int i = 11; i <= 20; i++) {
			addUnshowablePage("page" + i);
		}

		addShowablePage("low-p-1");
		addShowablePage("low-p-2");
		addUnshowablePage("low-p-3");
		addShowablePage("med-p-1");
		addShowablePage("med-p-2");
		addUnshowablePage("med-p-3");
		addShowablePage("high-p-1");
		addShowablePage("high-p-2");
		addUnshowablePage("high-p-3");

		addShowablePage("mult-1-1");
		addShowablePage("mult-1-2");
		addShowablePage("mult-1-3");
		addShowablePage("mult-2-1");
		addShowablePage("mult-2-2");
		addShowablePage("mult-2-3");
	}

	private void addShowablePage(String id) {
		Page page = new Page();
		page.setId(id);
		guide.getChapter(CHAPTER).addPage(page);
	}

	private void addUnshowablePage(String id) {
		// TODO, does anything prevent a tease from setting a flag called "unset"?
		Page page = new Page();
		page.setId(id);
		page.setIfSet("unset");
		guide.getChapter(CHAPTER).addPage(page);
	}

	@Test
	public void testAbsoluteMatchOnMissingPage() {
		assertNull(pageFilter.getSingleMatchingPage("missingPage", guide, CHAPTER));
	}

	@Test
	public void testAbsoluteMatchOnShowablePage() {
		assertEquals("showable", pageFilter.getSingleMatchingPage("showable", guide, CHAPTER));
	}

	@Test
	public void testAbsoluteMatchOnUnshowablePage() {
		assertEquals("unshowable", pageFilter.getSingleMatchingPage("unshowable", guide, CHAPTER));
	}

	@Test
	public void testRangeCanShowFilter() {
		assertEquals(10, pageFilter.getAllMatchingPages("page(1..20)", guide, CHAPTER).size());
		assertNull(pageFilter.getSingleMatchingPage("page(11..20)", guide, CHAPTER));
	}

	@Test
	public void testWildcardCanShowFilter() {
		assertEquals(10, pageFilter.getAllMatchingPages("page*", guide, CHAPTER).size());
		assertNull(pageFilter.getSingleMatchingPage("page11*", guide, CHAPTER));
	}

	@Test
	public void testRegexCanShowFilter() {
		assertEquals(10, pageFilter.getAllMatchingPages("regex:page\\d+", guide, CHAPTER).size());
		assertNull(pageFilter.getSingleMatchingPage("regex:page2\\d+", guide, CHAPTER));
	}

	@Test
	public void testSingleRangeFilter() {
		assertEquals(2, pageFilter.getAllMatchingPages("low-p-(1..3)", guide, CHAPTER).size());
	}

	@Test
	public void testMultipleRangeFilters() {
		assertEquals(6,
				pageFilter.getAllMatchingPages("mult-(1..2)-(1..3)", guide, CHAPTER).size());
	}

	@Test
	public void testWildcardFilter() {
		assertEquals(2, pageFilter.getAllMatchingPages("low-p-*", guide, CHAPTER).size());
		assertEquals(2, pageFilter.getAllMatchingPages("med-p-*", guide, CHAPTER).size());
		assertEquals(2, pageFilter.getAllMatchingPages("high-p-*", guide, CHAPTER).size());
		assertEquals(6, pageFilter.getAllMatchingPages("*-p-*", guide, CHAPTER).size());
	}

	@Test
	public void testWildcardMustMatchAtLeastOneChar() {
		assertNull(pageFilter.getSingleMatchingPage("showable*", guide, CHAPTER));
		assertNull(pageFilter.getSingleMatchingPage("*showable", guide, CHAPTER));
		assertNull(pageFilter.getSingleMatchingPage("*showable*", guide, CHAPTER));
		assertEquals("showable", pageFilter.getSingleMatchingPage("showabl*", guide, CHAPTER));
		assertEquals("showable", pageFilter.getSingleMatchingPage("*howable", guide, CHAPTER));
		assertEquals("showable", pageFilter.getSingleMatchingPage("*howabl*", guide, CHAPTER));
	}

	@Test
	public void testRegexFilter() {
		assertEquals(4,
				pageFilter.getAllMatchingPages("regex:(low|med)-p-\\d", guide, CHAPTER).size());
	}

	@Test
	public void testRegexFilterUsingFullStringMatching() {
		assertNull(pageFilter.getSingleMatchingPage("regex:page", guide, CHAPTER));
		assertEquals("page1", pageFilter.getSingleMatchingPage("regex:page1", guide, CHAPTER));
	}

	@Test
	public void testInvalidRegexFilter() {
		assertNull(pageFilter.getSingleMatchingPage("regex:(missing_parenthesis", guide, CHAPTER));
	}

	@Test
	public void testRandomSelection() {
		HashSet<String> valid = new HashSet<>(2);
		valid.add("page1");
		valid.add("page2");
		valid.add("page3");
		valid.add("page4");
		valid.add("page5");

		assertTrue(valid.contains(pageFilter.getSingleMatchingPage("page(1..5)", guide, CHAPTER)));
	}
}
