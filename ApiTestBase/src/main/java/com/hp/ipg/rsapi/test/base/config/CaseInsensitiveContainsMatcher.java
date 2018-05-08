package com.hp.ipg.rsapi.test.base.config;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Class to override Matcher to support Case Insensitive validation for Contains
 * 
 */
public class CaseInsensitiveContainsMatcher extends TypeSafeMatcher<String> {

	private String expectedString;

	public CaseInsensitiveContainsMatcher(String expectedString) {
		this.expectedString = expectedString;
	}

	/* (non-Javadoc)
	 * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
	 */
	@Override
	protected boolean matchesSafely(String actualString) {
		return actualString.toLowerCase().contains(this.expectedString.toLowerCase());
	}

	/* (non-Javadoc)
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description description) {
		description.appendText("containing string" + "\"" + this.expectedString + "\"");
	}
}
