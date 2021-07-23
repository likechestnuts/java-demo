/*
 * Copyright (c) 2021, wangguodong. All rights reserved.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangguodong
 * @since 2021/7/16
 */
public class Template {


	public static final Pattern PATTERN = Pattern.compile(".*\\$\\{.+\\}.*");

	private Map<String, String> variables;

	private String templateTest;

	public Template(String templateTest) {
		this.variables = new HashMap<>();
		this.templateTest = templateTest;
	}

	public void set(String name, String value) {
		this.variables.put(name, value);
	}

	public String evaluate() {
		String result = replaceVariables();
		checkForMissingValues(result);
		return result;
	}

	private String replaceVariables() {
		String result = templateTest;
		for (Map.Entry<String, String> entry : variables.entrySet()) {
			String regex = "\\$\\{" + entry.getKey() + "\\}";
			result = result.replaceAll(regex, entry.getValue());
		}
		return result;
	}

	private void checkForMissingValues(String result) {
		Matcher matcher = PATTERN.matcher(result);
		if (matcher.find()) {
			throw new MissingValueException("No value for " + matcher.group());
		}
	}

}
