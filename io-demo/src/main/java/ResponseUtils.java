/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

/**
 * @author wangguodong
 */
public class ResponseUtils {
	private final static String CRLF = "\r\n";

	public static String getResponse(String msg) {
		msg = "Server response:<br><br>" + msg.replaceAll(CRLF, "<br>");
		return new StringBuilder("HTTP /1.1 200 ok ").append(CRLF)
				.append("Content-Type:text/html ").append(CRLF)
				.append("Content-Length:" + msg.length()).append(CRLF).append(CRLF)
				.append(msg).toString();
	}
}
