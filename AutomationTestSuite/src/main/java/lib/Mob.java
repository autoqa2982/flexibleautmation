package lib;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import core.DriverFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import util.Log;
import util.Log.Priority;


public class Mob {
	
	private static Log log = new Log(Mob.class);

	public static void ScrollDown(AndroidDriver<MobileElement> driver) {
		Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.5;
		int scrollStart = screenHeightStart.intValue();
		Double screenHeightEnd = dimensions.getHeight() * 0.2;
		int scrollEnd = screenHeightEnd.intValue();
		
		new TouchAction(driver).press(0, scrollStart).waitAction()
		.moveTo(0, scrollEnd).release().perform();
	}
	
	/**
	 * <h1>isMobElementDisplayed</h1> checks if the element is present in screen,
	 * ideally useful if the Screen is refreshed before finding the element.
	 * 
	 * @param AndroidDriver<MobileElement> driver 
	 * @param String
	 *            xpath
	 * @param boolean...
	 *            waitForElement
	 * @return boolean
	
	*/public static boolean isMobElementDisplayed(AndroidDriver<MobileElement> driver , String locatorCat,String locPath, boolean... waitForElement) {
		log.Report(Priority.INFO,"Check if Mob element displayed , Element : "+locPath);
		try {
			switch(locatorCat.toLowerCase()) {
				case "xpath" : return isMobElementDisplayed(driver.findElement(By.xpath(locPath)), waitForElement); 
				case "id" : return isMobElementDisplayed(driver.findElement(By.id(locPath)), waitForElement);
				default : return false;
			}
		} catch (Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
		return false;
	}
	
	/**
	 * <h1>isMobElementDisplayed</h1> checks if the element is present in screen
	 * 
	 * @param WebElement
	 *            element
	 * @param boolean...
	 *            waitForElement
	 * @return boolean
	 */
	public static boolean isMobElementDisplayed(WebElement element, boolean... waitForElement) {
		log.Report(Priority.INFO,"Check if Mob element displayed , Element : "+getLocString(element));
		boolean blnElementDisplayed = false;
		try {
			try {
				if (waitForElement.length > 0) {
					if (waitForElement[0] == true) {
						Mob.waitForElement(element);
					}
				}
			} catch (Exception e) {
				log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
			}
			blnElementDisplayed = element.isDisplayed();
		} catch (Exception e) {
			blnElementDisplayed = false;
		}
		return blnElementDisplayed;
	}
	
	public static boolean isMobElementsDisplayed(List<WebElement> eles, boolean... waitForElement) {
		
		boolean blnElementDisplayed = false;
		try {
			try {
				if (waitForElement.length > 0) {
					if (waitForElement[0] == true) {
						Mob.waitForElements(eles);
					}
				}
			} catch (Exception e) {
				log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
			}
			blnElementDisplayed = eles.get(0).isDisplayed();
		} catch (Exception e) {
			blnElementDisplayed = false;
		}
		return blnElementDisplayed;
	}
	
	/**
	 * <h1>waitForElementByID</h1> waits for element with parameterized timeout
	 * 
	 * @param WebElement
	 *            element
	 * @param int
	 *            timeout
	 * @return void
	 */
	public static void waitForElementByID(String id, int timeout) {
		log.Report(Priority.INFO,"Wait for Element by ID: "+id);
		try {
			(new WebDriverWait(DriverFactory.getMobDriver(), timeout)).ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.elementToBeClickable(By.id(id)));
		} catch (Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}

	public static void waitForElementByXPATH(String xpath, int timeout) {
		log.Report(Priority.INFO,"Wait for Element by Xpath: "+xpath);
		try {
			(new WebDriverWait(DriverFactory.getMobDriver(), timeout)).ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		} catch (Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
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
		log.Report(Priority.INFO,"Wait for Element : "+getLocString(element));
		try {
			(new WebDriverWait(DriverFactory.getMobDriver(),
					Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT"))))
							.ignoring(NoSuchElementException.class)
							.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}
	
	public static void waitForElement(String element) {
		log.Report(Priority.INFO,"Wait for Element : "+element);
		try {
			(new WebDriverWait(DriverFactory.getMobDriver(),
					Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT"))))
							.ignoring(NoSuchElementException.class)
							.until(ExpectedConditions.elementToBeClickable(By.xpath(element)));
		} catch (Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}
	
	public static void waitForElements(List<WebElement> eles) {
		try {
			(new WebDriverWait(DriverFactory.getMobDriver(),
					Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT"))))
							.ignoring(NoSuchElementException.class)
							.until(ExpectedConditions.visibilityOfAllElements(eles));
		} catch (Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}
	
	public static void setTextToTextBox(WebElement textBoxField, String valueToSet) {
		log.Report(Priority.INFO,"Set text to : "+getLocString(textBoxField)+" with val : "+ valueToSet);
		try {
			if (isMobElementDisplayed(textBoxField)) {
				textBoxField.clear();
				textBoxField.sendKeys(valueToSet);
				textBoxField.click();
			}
		}catch(Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}
	
	
	/**
	 * <h1>clickOnElement</h1> clicks on element if displayed in screen
	 * 
	 * @param WebElement
	 *            clickableElement
	 */
	public static void clickOnElement(WebElement clickableElement) {
		log.Report(Priority.INFO,"Click on element : "+getLocString(clickableElement));
		try {
			waitForElement(clickableElement);
			clickableElement.click();	
		}catch(Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}
	
	/**
	 * <h1>getLocString</h1> converts an element to a xpath string, used to
	 * loggers to log details
	 * 
	 * @param MobileElement
	 *            ele
	 * @return String
	 */
	public static String getLocString(WebElement ele) {
		try {
			if (ele != null) {
				if (ele.toString().contains("Proxy element for: DefaultElementLocator")) {
					return ele.toString().split("By.")[1];
				} else {
					return ele.toString().split("->")[1];
				}
			}
		}catch(Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
		return "";
	}

}
