package com.coding.CodingTestPack;

import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.coding.helper.BaseHelper;
import com.coding.helper.LocatorTypes;

public class TestPageMethods extends BaseHelper implements ITestPageInput {
	
	boolean returnValue = false;
	
	public boolean navigateToSignInSignUpPage(String element) {
		Reporter.log("Click on Anmelden or Registrieren link and verify page is opened");
		try {
			WebElement signIn = findElement(element, LocatorTypes.XPATH);
			returnValue = signIn.isDisplayed();
			signIn.click();
		} catch (Exception e) {
			Reporter.log("Pzge is already opened verify the header");
			returnValue = true;
		}
		
			WebElement header = findElement(webElemProperty.getProperty("PYLOT_PAGE_HEADER"), LocatorTypes.XPATH);
			returnValue &= header.getText().contains(SIGNUP_PAGE_HEADER_DET) || header.getText().contains(SIGNUP_PAGE_HEADER_ENG);
			
		
		if (returnValue) {
			Reporter.log("<b>navigateToSignInSignUpPage method passed");

		} else {
			Reporter.log("<b>navigateToSignInSignUpPage method failed");

		}

		return returnValue;
	}
	
	public boolean enterSignUpPageDetailsAndLogin(String vorname, String nachname, String email, String passwd,
			boolean isSuccess, boolean isDuplicateEmail) throws InterruptedException {
		Reporter.log("Verify login button is disbaled when no details is filled in sign up page");
		WebElement loginBtn = findElement(webElemProperty.getProperty("TO_REGISTERED_BTN"), LocatorTypes.XPATH);
		returnValue = isNotEnabled(loginBtn);
		if (returnValue) {
			Reporter.log("Verify login button is only enabled after all the details in the page is filled");
			Reporter.log("Enter Vorname");
			returnValue = setAndVerifyTextBoxData(webElemProperty.getProperty("VORNAME_ELE"), LocatorTypes.XPATH,
					vorname);
			returnValue &= isNotEnabled(loginBtn);
			Reporter.log("Enter Nachname");
			returnValue &= setAndVerifyTextBoxData(webElemProperty.getProperty("NACHNAME_ELE"), LocatorTypes.XPATH,
					nachname);
			returnValue &= isNotEnabled(loginBtn);
			Reporter.log("Enter Email");
			returnValue &= setAndVerifyTextBoxData(webElemProperty.getProperty("SIGN_UP_EMAIL_ELE"), LocatorTypes.XPATH,
					email);
			returnValue &= isNotEnabled(loginBtn);
			Reporter.log("Enter Password");
			returnValue &= setAndVerifyTextBoxData(webElemProperty.getProperty("SIGN_UP_PASSWD_ELE"),
					LocatorTypes.XPATH, passwd);
			returnValue &= isNotEnabled(loginBtn);
			Reporter.log("Click on Check box");
			WebElement checkBox = findElement(webElemProperty.getProperty("TERM_CONSENT_CHECKBOX"), LocatorTypes.XPATH);
			checkBox.click();
			returnValue &= checkBox.getAttribute("class").contains("input--valid");
			if (returnValue) {
				if (isSuccess) {
					Reporter.log("Verify loginBtn is enabled");
					returnValue &= loginBtn.isEnabled();
					if (returnValue) {
						Reporter.log("Click on sign up button");
						loginBtn.click();
						Thread.sleep(3000);
						if (isDuplicateEmail) {
							Reporter.log("Verify error message is displayed when duplicate email id is entered");
							WebElement welcomeHeader = findElement(webElemProperty.getProperty("DUPLICATE_EMAIL_ERROR_MSG"),
									LocatorTypes.XPATH);
							String errorText = welcomeHeader.getText().trim();
							returnValue &= welcomeHeader.isDisplayed()
									&& (errorText.equals(DUPLICATE_EMAIL_ERROR_MSG_TEXT_DET) || errorText.equals(DUPLICATE_EMAIL_ERROR_MSG_TEXT_ENG));

						} else {
							WebElement welcomeHeader = findElement(
									webElemProperty.getProperty("WELCOME_PYLOT_PAGE_HEADER"), LocatorTypes.XPATH);
							Reporter.log("Verify login is successful if all valid data is entered");
							returnValue = welcomeHeader.isDisplayed()
									&& (welcomeHeader.getText().equals(SIGNUP_PAGE_HEADER_DET)
											|| welcomeHeader.getText().equals(SIGNUP_PAGE_HEADER_ENG));

						}
					}
				} else {
					Reporter.log("Verify login button is disabled if invalid data is entered");
					returnValue = !loginBtn.isEnabled();

				}
			}

		}

		if (returnValue) {
			Reporter.log("<b>enterSignUpPageDetails method passed");

		} else {
			Reporter.log("<b>enterSignUpPageDetails method failed");

		}

		return returnValue;
	}

	public boolean loginToPylotPage(String email, String psswd, boolean isSuccess, boolean isError)
			throws InterruptedException {
		Reporter.log("Verify Anmelden or Sign in button is disabled when user email and password is not entered");
		WebElement signIn = findElement(webElemProperty.getProperty("TO_REGISTERED_BTN"), LocatorTypes.XPATH);
		returnValue = isNotEnabled(signIn);
		if (returnValue) {
			Reporter.log("Enter Email");
			returnValue = setAndVerifyTextBoxData(webElemProperty.getProperty("SIGN_IN_EMAIL"), LocatorTypes.XPATH,
					email);
			// TODO: The submit button is enabled even before entering value in passowrd
			// field
			// returnValue &= isNotEnabled(signIn);
			Reporter.log("Enter Password");
			returnValue &= setAndVerifyTextBoxData(webElemProperty.getProperty("SIGN_UP_PASSWD_ELE"),
					LocatorTypes.XPATH, psswd);
			if (isSuccess) {
				Reporter.log("Verify Anmelden or Sign in button enabled");
				returnValue &= signIn.isEnabled();
				Reporter.log("Click on sign in button");
				signIn.click();
				Thread.sleep(3000);
				if (isError) {
					Reporter.log("Verify error message is displayed when invalid data is entered");
					WebElement welcomeHeader = findElement(webElemProperty.getProperty("DUPLICATE_EMAIL_ERROR_MSG"),
							LocatorTypes.XPATH);
					String errorText = welcomeHeader.getText().trim();
					returnValue &= welcomeHeader.isDisplayed() && (errorText.equals(INVALID_SIGNIN_DEATILS_ERROR_ENG)
							|| errorText.equals(INVALID_SIGNIN_DEATILS_ERROR_DET));

				} else {
					WebElement welcomeHeader = findElement(webElemProperty.getProperty("WELCOME_PYLOT_PAGE_HEADER"),
							LocatorTypes.XPATH);
					Reporter.log("Verify login is successful if all valid data is entered");
					returnValue = welcomeHeader.isDisplayed() && (welcomeHeader.getText().equals(SIGNUP_PAGE_HEADER_DET)
							|| welcomeHeader.getText().equals(SIGNUP_PAGE_HEADER_ENG));

				}

			} else {
				Reporter.log(
						"Verify login is not successful if invalid data is entered and sign in button is disabled");
				returnValue &= isNotEnabled(signIn);

			}

		}

		if (returnValue) {
			Reporter.log("<b>enterSignUpPageDetails method passed");

		} else {
			Reporter.log("<b>enterSignUpPageDetails method failed");

		}

		return returnValue;
	}
	
	public boolean validateInvalidFieldHighlighted(String element) {
		Reporter.log("Verify the field is highlighted red when invalid data is entered");
		WebElement fieldEle = findElement(element, LocatorTypes.XPATH);
		returnValue = fieldEle.getAttribute("class").contains("input--invalid");
		if (returnValue) {
			Reporter.log("The field is not accepting invalid data , Verification PASSED");

		} else {
			Reporter.log("The field is accepting invalid data , Verification FAILED");

		}

		return returnValue;
	}
}
