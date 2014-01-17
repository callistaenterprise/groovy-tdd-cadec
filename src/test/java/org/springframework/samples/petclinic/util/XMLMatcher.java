package org.springframework.samples.petclinic.util;

import java.util.List;
import java.util.Map;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.w3c.dom.Document;

public class XMLMatcher {
	static {
		XMLUnit.setIgnoreWhitespace(true);
	}

	public static TypeSafeMatcher<String> isSimilarTo(final String expectedXML) {
		return new TypeSafeMatcher<String>() {
			private DetailedDiff diff;
			private Exception thrownException;

			public void describeTo(Description desc) {
				desc.appendText(expectedXML);
			}

			@Override
			protected void describeMismatchSafely(String actualXML,
					Description mismatchDescription) {
				try {
					appendMismatchDescription(actualXML, diff, thrownException,
							mismatchDescription);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean matchesSafely(String actualXML) {
				diff = null;

				try {
					diff = new DetailedDiff(new Diff(expectedXML, actualXML));
				} catch (Exception exception) {
					thrownException = exception;
					return false;
				}

				return diff.similar();
			}
		};
	}

    /**
     * Parse the expected and actual content strings as XML and assert that the
     * two are "similar" -- i.e. they contain the same elements and attributes
     * regardless of order.
     *
     * <p>Use of this method assumes the
     * <a href="http://xmlunit.sourceforge.net/">XMLUnit<a/> library is available.
     *
     * @param expected the expected XML content
     * @param actual the actual XML content
     *
     * @see MockMvcResultMatchers#xpath(String, Object...)
     * @see MockMvcResultMatchers#xpath(String, Map, Object...)
     */
    public static void assertXmlEqual(String expected, String actual) throws Exception {

            XMLUnit.setIgnoreWhitespace(true);
            XMLUnit.setIgnoreComments(true);
            XMLUnit.setIgnoreAttributeOrder(true);

            Document control = XMLUnit.buildControlDocument(expected);
            Document test = XMLUnit.buildTestDocument(actual);
            Diff diff = new Diff(control, test);
            if (!diff.similar()) {
                    AssertionErrors.fail("Body content " + diff.toString());
            }
    }

	// --------------------
	// Helper methods
	// --------------------

	private static void appendMismatchDescription(String actualXML,
			DetailedDiff diff, Exception thrownException,
			Description mismatchDescription) {
		mismatchDescription.appendText(actualXML);

		if (thrownException != null) {
			appendException(thrownException, mismatchDescription);
		}

		appendDifferences(diff, mismatchDescription);
	}

	private static void appendDifferences(DetailedDiff diff,
			Description mismatchDescription) {
		if (diff != null) {
			mismatchDescription.appendText("\nDiff: " + diff.toString());

			List<Difference> differences = diff.getAllDifferences();
			for (Difference difference : differences) {
				mismatchDescription.appendText("\nDiff: "
						+ difference.getDescription());
			}
		}
	}

	private static void appendException(Exception thrownException,
			Description mismatchDescription) {
		mismatchDescription.appendText("\nException: "
				+ thrownException.getMessage());
	}

}