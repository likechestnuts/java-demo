/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wangguodong
 * @since 2021/7/23
 */
public class TestTemplateParse {

	@Test
	public void emptyTemplateRenderAsEmptyString() {
		List<String> segments = parse("");
		assertSegments(segments,"");
	}

	@Test
	public void templateWithOnlyPlainText() {
		List<String> segments = parse("plain text only");
		assertSegments(segments,"plain text only");
	}

	@Test
	public void parsingMultipleVariables(){
	    List<String> segments = parse("${a}:${b}:${c}");
		assertSegments(segments, "${a}", ":", "${b}", ":", "${c}");
	}

	private void assertSegments(List<? extends Object> actual, Object... expected) {
		assertEquals(expected.length, actual.size(), "有一个片段");
		assertEquals(Arrays.asList(expected), actual);
	}

	private List<String> parse(String template) {
		return new TemplateParse().parse(template);
	}

}
