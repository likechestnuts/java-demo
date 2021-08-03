/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

/**
 * @author wangguodong
 * @since 2021/8/3
 */
public abstract class NodeDecorator implements TextNode{

	protected  final TextNode target;

	protected NodeDecorator(TextNode target) {
		this.target = target;
	}

	@Override
	public void setText(String text){
		this.target.setText(text);
	}
}
