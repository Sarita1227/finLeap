package com.coding.CodingTestPack;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.coding.driver.DriverManager;
import com.coding.helper.BaseHelper;
import com.coding.helper.LaunchWebUrl;
import com.coding.helper.LocatorTypes;

public class TestPageTest extends BaseHelper implements ITestPageInput {
	
	boolean testResult = false;
	LaunchWebUrl launchUrl = new LaunchWebUrl();
	TestPageMethods testPageMethods = new TestPageMethods();
	
	/**
	 * Initializing unique credential for valid testing
	 */
	String vorname = RandomStringUtils.randomAlphabetic(6);
	String nachname = RandomStringUtils.randomAlphabetic(6);
	String email = RandomStringUtils.randomAlphabetic(6) + "@test.com";
	String passwd = (RandomStringUtils.randomAlphabetic(1)).toUpperCase() + RandomStringUtils.randomAlphabetic(5) + "@"
			+ RandomStringUtils.randomNumeric(3);

	public TestPageTest() {
		getElementPath("pageElements.properties");
		
	}
	
	@BeforeTest
	public void beforeTest() {
		try {
			testResult = launchUrl.loadAppUrl();
		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		
		if (testResult) {
			Reporter.log("Before Test method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("Before Test method failed");
			Assert.fail();
		}
	}

	@AfterMethod
	public void afterMethod() throws InterruptedException {
		try {
			WebElement profile = findElement(webElemProperty.getProperty("PROFILE_BTN"), LocatorTypes.XPATH);
			if(profile.isDisplayed()) {
				profile.click();
				WebElement logout = findElement(webElemProperty.getProperty("LOGOUT_BTN"), LocatorTypes.XPATH);
				if(logout.isDisplayed()) {
					logout.click();
					Thread.sleep(3000);
				}
			}
		} catch (Exception e) {
			DriverManager.driver.navigate().refresh();
			waitForPageLoad();
			Thread.sleep(3000);
		}
	}

	@AfterTest
	public void closeDriver() throws IOException {
		DriverManager.driver.quit();
	}
	
	/**
	 * TC01 test with valid sign up details
	 * Enter valid login details
	 * 
	 */
	@Test(priority = 1, alwaysRun = true)
	public void TC01_SignUpWithCorrectAndUniqueData() {
		try {
			
			testResult = testPageMethods.navigateToSignInSignUpPage(webElemProperty.getProperty("REGISTRATION_LINK"));
			testResult &= testPageMethods.enterSignUpPageDetailsAndLogin(vorname, nachname, email, passwd, true, false);

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC01_SignUpWithCorrectAndUniqueData method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC01_SignUpWithCorrectAndUniqueData method failed");
			Assert.fail();
		}

	}

	
	/**
	 * TC02 test with valid login details
	 * Enter valid login details
	 * 
	 */
	@Test(priority = 2, alwaysRun = true)
	public void TC02_SignInWithValidCredential() {
		try {
			testResult = testPageMethods.loginToPylotPage(email, passwd, true, false);

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC02_SignInWithValidCredential method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC02_SignInWithValidCredential method failed");
			Assert.fail();
		}

	}

	
	/**
	 * TC03 test with different invalid email id and password combination
	 * Enter valid login details
	 * 
	 */
	@Test(priority = 3, alwaysRun = true)
	public void TC03_SignInWithInvalidEmailPasswd() {
		try {
			testResult = testPageMethods.loginToPylotPage(INVALID_EMAIL, passwd, false, false);
			testResult &= testPageMethods.validateInvalidFieldHighlighted(webElemProperty.getProperty("SIGN_IN_EMAIL"));

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC03_SignInWithInvalidEmailPasswd method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC03_SignInWithInvalidEmailPasswd method failed");
			Assert.fail();
		}

	}

	/**
	 * TC04 test with valid sign up details
	 * Enter valid login details
	 * 
	 */
	@DataProvider(name="SignUpValidation")
    public Object[][] getDataForSignUp(){
    return new Object[][] 
    	{
            { THREE_DIGIT_ALFASPC, VALID_NACHNAME, VALID_TEST_EMAIL, VALID_PASSWD, webElemProperty.getProperty("VORNAME_ELE")},
            { NUMERIC_INPUT, VALID_NACHNAME, VALID_TEST_EMAIL, VALID_PASSWD, webElemProperty.getProperty("VORNAME_ELE") },
            { VALID_VORNAME, THREE_DIGIT_ALFASPC, VALID_TEST_EMAIL, VALID_PASSWD, webElemProperty.getProperty("NACHNAME_ELE")},
            { VALID_VORNAME, NUMERIC_INPUT, VALID_TEST_EMAIL, VALID_PASSWD, webElemProperty.getProperty("NACHNAME_ELE")}, 
            {VALID_VORNAME, VALID_NACHNAME, INVALID_EMAIL, VALID_PASSWD, webElemProperty.getProperty("SIGN_UP_EMAIL_ELE")},
            {VALID_VORNAME, VALID_NACHNAME, VALID_TEST_EMAIL, INVALID_EMAIL, webElemProperty.getProperty("SIGN_UP_PASSWD_ELE")}
        };

    }
	@Test(priority = 4, alwaysRun = true, dataProvider="SignUpValidation")
	public void TC04_SignUpWithInvalidData(String firstname, String lastname, String mailId, String password, String element) {
		try {
			
			testResult = testPageMethods.navigateToSignInSignUpPage(webElemProperty.getProperty("REGISTRATION_LINK"));
			testResult &= testPageMethods.enterSignUpPageDetailsAndLogin(firstname, lastname, mailId, password, false, false);
			testResult &= testPageMethods.validateInvalidFieldHighlighted(element);

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC04_SignUpWithInvalidData method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC04_SignUpWithInvalidData method failed");
			Assert.fail();
		}

	}
	
	/**
	 * TC05 test with valid sign up details
	 * Enter valid login details
	 * 
	 */
	@Test(priority = 5, alwaysRun = true)
	public void TC05_SignUpWithDuplicateEmail() {
		try {
			testResult = testPageMethods.navigateToSignInSignUpPage(webElemProperty.getProperty("REGISTRATION_LINK"));
			testResult &= testPageMethods.enterSignUpPageDetailsAndLogin(vorname, nachname, email, passwd, true, true);

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC05_SignUpWithDuplicateEmail method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC05_SignUpWithDuplicateEmail method failed");
			Assert.fail();
		}

	}
	
	@DataProvider(name="EmailPasswdValidation")
    public Object[][] getLoginData(){
    return new Object[][] 
    	{
            { email, INVALID_PASSWD},
            { UNREGISTERED_EMAIL, passwd },
        };

    }
	@Test(priority = 6, alwaysRun = true, dataProvider="EmailPasswdValidation")
	public void TC06_SignInWithInvalidCredential(String emailId, String password) {
		try {
			testResult = testPageMethods.navigateToSignInSignUpPage(webElemProperty.getProperty("SIGN_IN_LINK"));
			testResult &= testPageMethods.loginToPylotPage(emailId, password, true, true);

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC06_SignInWithInvalidCredential method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC06_SignInWithInvalidCredential method failed");
			Assert.fail();
		}

	}
}
