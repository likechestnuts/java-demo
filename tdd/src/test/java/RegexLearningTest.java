/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangguodong
 * @since 2021/7/21
 */
public class RegexLearningTest {

	private String haystack = "The needle shop sells needles";

	private String regex = "(needle)";

	@Test
	public void testHowGroupCountWorks() {
		Matcher matcher = Pattern.compile(regex).matcher(haystack);
		assertEquals(2, matcher.groupCount());
	}

	@Test
	public void testFindStartAndEnd() {
		Matcher matcher = Pattern.compile(regex).matcher(haystack);
		assertTrue(matcher.find());
		assertEquals(4, matcher.start());
		assertEquals(10, matcher.end());
		assertTrue(matcher.find());
		assertEquals(22, matcher.start());
		assertEquals(28, matcher.end());
		assertFalse(matcher.find());
	}
}
