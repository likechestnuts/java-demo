import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TestTemplateTest {


	private static Template template;

	@BeforeAll
	public static void setUp() {
		template = new Template("${one},${two},${three}");
		template.set("one", "1");
		template.set("two", "2");
		template.set("three", "3");
	}


	@Test
	public void multipleVariables() {
		assertTemplateEvaluatesTo("1,2,3");
	}


	@Test
	public void unknownVariableAreIgnored() {
		template.set("doesnotexist", "Hi");
		assertTemplateEvaluatesTo("1,2,3");
	}

	@Test
	public void missingValueRaisesException() {
		try {
			new Template("${foo}").evaluate();
			fail("evaluate() should throw an exception if a variables was left without a value");
		} catch (MissingValueException exception) {
			assertEquals("No value for ${foo}", exception.getMessage());
		}
	}

//	@Test
//	public void  variablesGetProcessedJustOnce(){
//		template.set("one","${one}");
//		template.set("two","${two}");
//		template.set("three","${three}");
//		assertTemplateEvaluatesTo("${one},${two},${three}");
//	}
	private void assertTemplateEvaluatesTo(String expected) {
		assertEquals(expected, template.evaluate());
	}

}