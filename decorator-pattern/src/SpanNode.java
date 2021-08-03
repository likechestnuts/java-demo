/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class SpanNode implements TextNode {
	private String text;

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return this.text;
	}
}
