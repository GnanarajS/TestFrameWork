package com.hp.ipg.rsapi.test.base.config;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Class to override Matcher to support Case Insensitive validation for Starts With
 * 
 */
public class CaseInsensitiveStartsWithMatcher extends TypeSafeMatcher<String> {

	private String expectedString;

	public CaseInsensitiveStartsWithMatcher(String expectedString) {
		this.expectedString = expectedString;
	}

	/* (non-Javadoc)
	 * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
	 */
	@Override
	protected boolean matchesSafely(String actualString) {
		return actualString.toLowerCase().startsWith(this.expectedString.toLowerCase());
	}

	/* (non-Javadoc)
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description description) {
		description.appendText("starts with string" + "\"" + this.expectedString + "\"");
	}

}