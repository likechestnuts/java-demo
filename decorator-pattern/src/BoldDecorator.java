/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class BoldDecorator extends NodeDecorator {
	protected BoldDecorator(TextNode target) {
		super(target);
	}

	@Override
	public String getText() {
		return "<b>" + target.getText() + "</b>";
	}
}
