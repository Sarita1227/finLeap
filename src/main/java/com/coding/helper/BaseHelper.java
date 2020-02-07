package com.coding.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.coding.driver.DriverManager;
import com.thoughtworks.selenium.Wait;


public class BaseHelper{

//	protected WebDriverWait webDriverWait = new WebDriverWait(DriverManager.driver, time);
	
	LogDetails logger = new LogDetails();
	public static final Properties webElemProperty = new Properties();

	
	
	/**
	 * Read element from property file
	 */
	public static void getElementPath(String fileName) {
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new FileReader((System.getProperty("user.dir")) + "/inputSheets/" + fileName));
			webElemProperty.load(reader);
			reader.close();	
		} catch(IOException e) {
			e.getMessage();
		}
		
	}
	
	public static Properties getWebElementProperties() {
		return webElemProperty;
	}
	
	/**
	 * Click on element javascript code
	 * @param element
	 */
	public void clickAt(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor)DriverManager.driver;
		executor.executeScript("arguments[0].click();", element);
	}
	
	
	/**
	 * Select Dropdown by index
	 * @param element
	 * @param index
	 */
	public void selectDropdownByIndex(String element, int index, LocatorTypes type) {
		pageDropDown(element, type).selectByIndex(index);
	}
	
	/**
	 * Select dropdown by value
	 * @param element
	 * @param value
	 */
	public void selectDropdownByValue(String element, String value, LocatorTypes type) {
		pageDropDown(element, type).selectByVisibleText(value);
	}
	
	public void enterData(String element, String value, LocatorTypes type) {
		findElement(element, type).sendKeys(value);
		
	}
	
	public boolean setAndVerifyTextBoxData(String element, LocatorTypes type, String value) {
		boolean returnValue = false;
		WebElement textBoxEle = findElement(element, type);
		returnValue = textBoxEle.isEnabled();
		if (returnValue) {
			textBoxEle.clear();
			textBoxEle.sendKeys(value);
			textBoxEle.sendKeys(Keys.TAB);
			returnValue = textBoxEle.getAttribute("value").equals(value);
		}
		return returnValue;

	}

	public Select pageDropDown(String elementName, LocatorTypes type) {
		WebElement element = findElement(elementName, type);
		return new Select(element);
	}
	
	public boolean isElementNotPresent(String element, long timeUnit) {
		try {
		WebDriverWait wait = new WebDriverWait(DriverManager.driver, timeUnit);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element)));
		return false;
		} catch (NoSuchElementException e) {
		return true;
		}
	}
		
	public void waitForElementToAppear(int time, String element) {
		WebDriverWait wait = new WebDriverWait(DriverManager.driver, time);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
	}

	/**
	 * Follow a common pattern to 
	 * @param locatorName
	 * @return
	 */
	public WebElement findElement(String locatorName, LocatorTypes type) {
		WebElement element = null;
		switch (type) {
		case CSS:
			element = DriverManager.driver.findElement(By.cssSelector((locatorName)));
			break;
		case XPATH:
			element = DriverManager.driver.findElement(By.xpath((locatorName)));
			break;
		case ID:
			element = DriverManager.driver.findElement(By.id((locatorName)));
			break;
		case CLASSNAME:
			element = DriverManager.driver.findElement(By.className((locatorName)));
			break;
		case LINKTEXT:
			element = DriverManager.driver.findElement(By.linkText((locatorName)));
			break;
		case NAME:
			element = DriverManager.driver.findElement(By.name((locatorName)));
			break;
		case PARTIALLINKTEXT:
			element = DriverManager.driver.findElement(By.partialLinkText((locatorName)));
			break;
		case TAGNAME:
			element = DriverManager.driver.findElement(By.tagName((locatorName)));
			break;
		default:
			element = DriverManager.driver.findElement(By.xpath((locatorName)));
			break;
		}

		return element;
	}
	
	/**
	 * Find list of webElements
	 * @param locatorName
	 * @param type
	 * @return
	 */
	public List<WebElement> findElements(String locatorName, LocatorTypes type) {
		List<WebElement> element = null;
		switch (type) {
		case CSS:
			element = DriverManager.driver.findElements(By.cssSelector((locatorName)));
			break;
		case XPATH:
			element = DriverManager.driver.findElements(By.xpath((locatorName)));
			break;
		case ID:
			element = DriverManager.driver.findElements(By.id((locatorName)));
			break;
		case CLASSNAME:
			element = DriverManager.driver.findElements(By.className((locatorName)));
			break;
		case LINKTEXT:
			element = DriverManager.driver.findElements(By.linkText((locatorName)));
			break;
		case NAME:
			element = DriverManager.driver.findElements(By.name((locatorName)));
			break;
		case PARTIALLINKTEXT:
			element = DriverManager.driver.findElements(By.partialLinkText((locatorName)));
			break;
		case TAGNAME:
			element = DriverManager.driver.findElements(By.tagName((locatorName)));
			break;
		default:
			element = DriverManager.driver.findElements(By.xpath((locatorName)));
			break;
		}

		return element;
	}
	
	    
	public void waitForElementVisibility(String element, int time) {
		WebDriverWait wait = new WebDriverWait(DriverManager.driver, time);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element)));

	}
	
	public void waitForPageLoad() {
		while (true) {
			String pageStatus = (String) ((JavascriptExecutor) DriverManager.driver)
					.executeScript("return document.readyState");
			if (pageStatus.equals("complete")) {
				break;
			}
		}
	} 

	public void takeScreenshot() throws Exception {
		File screenshot = ((TakesScreenshot) DriverManager.driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File("/Screenshot"));
	}
	
	/**
	 * Wait till the element disappears
	 * @param locator
	 * @param type
	 * @return
	 */
	public ExpectedCondition<Boolean> absenceOfElementLocated(
		      final String locator, final LocatorTypes type) {
		    return new ExpectedCondition<Boolean>() {
		      public Boolean apply(WebDriver driver) {
		        try {
		          findElement(locator, type);
		          return false;
		        } catch (NoSuchElementException e) {
		          return true;
		        } catch (StaleElementReferenceException e) {
		          return true;
		        }
		      }

		      @Override
		      public String toString() {
		        return "element to not being present: " + locator;
		      }

		    };
		  }
	
	public boolean isNotEnabled(WebElement element) {
		return element.getAttribute("disabled").contains("true");
	}
}
