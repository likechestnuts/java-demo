/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public class DivDecorator extends NodeDecorator {

	protected DivDecorator(TextNode target) {
		super(target);
	}

	@Override
	public String getText() {
		return "<div>" + this.target.getText() + "</div>";
	}
}
