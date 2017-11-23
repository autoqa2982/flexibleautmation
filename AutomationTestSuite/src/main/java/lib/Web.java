package lib;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import core.DriverFactory;
import util.Log;
import util.Log.Priority;

public class Web {

	private static Log log = new Log(Web.class);

	/**
	 * <h1>isWebElementDisplayed</h1> checks if the element is present in DOM,
	 * ideally useful if the DOM is refreshed before finding the element.
	 * 
	 * @param WebDriver
	 *            driver
	 * @param String
	 *            xpath
	 * @param boolean...
	 *            waitForElement
	 * @return boolean
	 */
	public static boolean isWebElementDisplayed(WebDriver driver, String xpath, boolean... waitForElement) {
		try {
			return isWebElementDisplayed(driver.findElement(By.xpath(xpath)), waitForElement);
		} catch (Exception e) {
			// do nothing when element not present
			log.Report(Priority.ERROR, "Error " + Common.shortenedStackTrace(e, 10));
		}
		return false;
	}

	/**
	 * <h1>isWebElementDisplayed</h1> checks if the element is present in DOM,
	 * also can wait with explicit timeout for the element to be displayed
	 * 
	 * @param WebElement
	 *            element
	 * @param boolean...
	 *            waitForElement
	 * @return boolean
	 */
	public static boolean isWebElementDisplayed(WebElement element, boolean... waitForElement) {
		boolean blnElementDisplayed = false;
		try {
			try {
				if (waitForElement.length > 0) {
					if (waitForElement[0] == true) {
						Web.waitForElement(element);
					}
				}
			} catch (Exception e) {
				// Do nothing
			}
			blnElementDisplayed = element.isDisplayed();
		} catch (Exception e) {
			log.Report(Priority.ERROR,
					"WebElement " + getLocString(element) + "Error " + Common.shortenedStackTrace(e, 10));
			blnElementDisplayed = false;
		}
		return blnElementDisplayed;
	}

	/**
	 * <h1>isWebElementsDisplayed</h1> checks if list of elements are present in
	 * DOM, also can wait with explicit timeout configured for the elements to
	 * be displayed
	 * 
	 * @param List<WebElement>
	 *            elements
	 * @param boolean...
	 *            waitForElement
	 * @return boolean
	 */
	public static boolean isWebElementsDisplayed(List<WebElement> elements, boolean... waitForElement) {
		boolean blnElementDisplayed = false;
		try {
			log.Report(Priority.INFO, "check if WebElements " + getLocString(elements.get(0)) + " is displayed");
			try {
				if (waitForElement.length > 0) {
					if (waitForElement[0] == true) {
						Web.waitForElements(elements);
					}
				}
			} catch (Exception e) {
				// Do nothing
			}
			try {
				if ((new WebDriverWait(DriverFactory.getWebDriver(),
						Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT")))
								.ignoring(StaleElementReferenceException.class)
								.until(ExpectedConditions.visibilityOfAllElements(elements))
								.size() == elements.size())) {
					blnElementDisplayed = true;
				}
			} catch (Exception e) {
				log.Report(Priority.ERROR, "Error " + Common.shortenedStackTrace(e, 10));
			}
		} catch (NoSuchElementException e) {
			log.Report(Priority.INFO,
					"WebElement " + getLocString(elements.get(0)) + "Error " + Common.shortenedStackTrace(e, 10));
			blnElementDisplayed = false;
		}
		return blnElementDisplayed;
	}

	/**
	 * <h1>setTextToTextBox</h1> sets text to the text box if displayed in DOM
	 * 
	 * @param WebElement
	 *            textBoxField
	 * @param CharSequence
	 *            valueToSet
	 * @return String
	 */
	public static String setTextToTextBox(WebElement textBoxField, CharSequence valueToSet) {
		String fieldTextValue = "";
		log.Report(Priority.INFO,
				"Set text to input box " + getLocString(textBoxField) + " with value : " + valueToSet);
		if (Web.isWebElementDisplayed(textBoxField)) {
			textBoxField.clear();
			textBoxField.sendKeys(valueToSet);
			textBoxField.click();
			fieldTextValue = textBoxField.getAttribute("value");
		}
		log.Report(Priority.INFO,
				"Value set to input box " + getLocString(textBoxField) + " with value : " + fieldTextValue);
		return fieldTextValue;
	}

	/**
	 * <h1>clickOnElement</h1> clicks on element if displayed in DOM
	 * 
	 * @param WebElement
	 *            clickableElement
	 * @return boolean
	 */
	public static boolean clickOnElement(WebElement clickableElement) {
		boolean success = false;
		log.Report(Priority.INFO, "Click on element " + getLocString(clickableElement));
		if (Web.isWebElementDisplayed(clickableElement)) {
			clickableElement.click();
			success = true;
		}
		log.Report(Priority.INFO, "Click on element status" + success);
		return success;
	}

	/**
	 * <h1>waitForElement</h1> waits for element with explicit timeout
	 * configured
	 * 
	 * @param WebElement
	 *            element
	 * @return void
	 */
	public static void waitForElement(WebElement element) {
		try {
			log.Report(Priority.INFO, "Wait for element " + getLocString(element));
			(new WebDriverWait(DriverFactory.getWebDriver(),
					Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT"))))
							.ignoring(StaleElementReferenceException.class)
							.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			log.Report(Priority.ERROR, "Error " + Common.shortenedStackTrace(e, 10));
		}
	}

	/**
	 * <h1>waitForElement</h1> waits for element with parameterized timeout
	 * 
	 * @param WebElement
	 *            element
	 * @param int
	 *            timeout
	 * @return void
	 */
	public static void waitForElement(WebElement element, int timeout) {
		try {
			log.Report(Priority.INFO, "Wait for element " + getLocString(element) + " with timeout " + timeout);
			(new WebDriverWait(DriverFactory.getWebDriver(), timeout))
					.ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			log.Report(Priority.ERROR, "Error " + Common.shortenedStackTrace(e, 10));
		}
	}

	/**
	 * <h1>waitForElements</h1> waits for list of elements with explicit timeout
	 * configured
	 * 
	 * @param List<WebElement>
	 *            elements
	 * @return void
	 */
	public static void waitForElements(List<WebElement> elements) {
		try {
			log.Report(Priority.INFO, "Wait for elements " + getLocString(elements.get(0)));
			(new WebDriverWait(DriverFactory.getWebDriver(),
					Long.parseLong(Common.getGlobalParam("objectSyncTimeout"))))
							.ignoring(StaleElementReferenceException.class)
							.until(ExpectedConditions.visibilityOfAllElements(elements));
		} catch (Exception e) {
			log.Report(Priority.ERROR, "Error " + Common.shortenedStackTrace(e, 10));
		}
	}

	/**
	 * <h1>selectItem</h1> selects item from dropddown by visible text
	 * 
	 * @param WebElement
	 *            ele
	 * @param String
	 *            item
	 * @return boolean
	 */
	public static boolean selectItem(WebElement ele, String item) {
		Select sel = new Select(ele);
		sel.getFirstSelectedOption();
		for (WebElement el : sel.getOptions()) {
			if (el.getText().contains(item)) {
				sel.selectByVisibleText(el.getText());
				return true;
			}
		}
		return false;
	}

	/**
	 * <h1>getSelectedItemfromList</h1> returns the first selected item from
	 * dropdown
	 * 
	 * @param WebElement
	 *            ele
	 * @return String
	 */
	public static String getSelectedItemfromList(WebElement ele) {
		String returnVal = "";
		Select sel = new Select(ele);
		returnVal = sel.getFirstSelectedOption().getText();
		return returnVal;
	}

	/**
	 * <h1>getAllListOptions</h1> returns the list of all options available in
	 * dropdown
	 * 
	 * @param WebElement
	 *            ele
	 * @return List<WebElement
	 */
	public static List<WebElement> getAllListOptions(WebElement ele) {
		List<WebElement> selOptions = null;
		Select sel = new Select(ele);
		selOptions = sel.getOptions();
		return selOptions;
	}

	/**
	 * <h1>getSelectItems</h1> returns the list of all selected options in
	 * dropdown
	 * 
	 * @param WebElement
	 *            ele
	 * @return List<String>
	 */
	public static List<String> getSelectItems(WebElement ele) {
		List<String> listItems = new ArrayList<String>();
		Select sel = new Select(ele);
		for (WebElement e : sel.getOptions()) {
			listItems.add(e.getText());
		}
		if (listItems.isEmpty())
			return null;
		return listItems;
	}

	/**
	 * <h1>openNewWindow</h1> opens new browser window
	 * 
	 * @param WebDriver
	 *            driver
	 * @return void
	 */
	public static void openNewWindow(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.open();");
	}

	/**
	 * <h1>GetElementValue</h1> get text of an element
	 * 
	 * @param WebElement
	 *            Element
	 * @return String
	 */
	public static String GetElementValue(WebElement Element) {
		if (Web.isWebElementDisplayed(Element, true)) {
			log.Report(Priority.INFO, "Get WebElement value");
			return Element.getText().trim();
		}
		return null;
	}

	/**
	 * <h1>SwitchToFrame</h1> switch driver focus to iframe
	 * 
	 * @param WebDriver
	 *            driver
	 * @param WebElement
	 *            frm
	 * @return void
	 */
	public static void SwitchToFrame(WebDriver driver, WebElement frm) {
		driver.switchTo().frame(frm);
	}

	/**
	 * <h1>SwitchToDefaultContent</h1> switch driver focus back to default
	 * content
	 * 
	 * @param WebDriver
	 *            driver
	 * @return void
	 */
	public static void SwitchToDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * <h1>WebScrollToElement</h1> performs page scroll to an element
	 * 
	 * @param WebDriver
	 *            driver
	 * @param WebElement
	 *            ele
	 * @return void
	 */
	public static void WebScrollToElement(WebDriver driver, WebElement ele) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
	}

	/**
	 * <h1>getLocString</h1> converts an element to a xpath string, used to
	 * loggers to log details
	 * 
	 * @param WebElement
	 *            ele
	 * @return String
	 */
	public static String getLocString(WebElement ele) {
		if (ele != null) {
			if (ele.toString().contains("Proxy element for: DefaultElementLocator")) {
				return ele.toString().split("By.")[1];
			} else {
				return ele.toString().split("->")[1];
			}
		}
		return "";
	}

}
