package com.coding.helper;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import com.coding.driver.DriverManager;
import com.coding.driver.DriverManagerFactory;
import com.coding.driver.DriverType;

public class LaunchWebUrl extends BaseHelper{

	DriverManager webDr;
		
	@SuppressWarnings("static-access")
	public boolean loadAppUrl() throws Exception {
		boolean returnValue = false;
		webDr = DriverManagerFactory.getManager(DriverType.CHROME);
		DriverManager.driver = webDr.getDriver();
		Reporter.log("Launching Page URL");
		DriverManager.driver.navigate().to("http://sandbox.cockpit.pylot.de/");
		waitForPageLoad();
		waitForElementToAppear(60, webElemProperty.getProperty("PYLOT_PAGE_HEADER"));
		Reporter.log("Verify the Url is loaded successfully");
		WebElement loginPageELement = findElement(webElemProperty.getProperty("PYLOT_PAGE_HEADER"), LocatorTypes.XPATH);
		Reporter.log("Verify Login page is displayed");
		returnValue = loginPageELement.isDisplayed();
		returnValue &= loginPageELement.getText().equals("Willkommen bei Pylot!");
		
		if (returnValue) {
			Reporter.log("Successfully loaded the Page URL");
			Assert.assertTrue(returnValue);
		} else {
			Reporter.log("URL loading method failedL");
			Assert.fail();
		}
		return returnValue;

	}

}
