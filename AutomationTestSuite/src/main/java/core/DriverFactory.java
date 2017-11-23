package core;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lib.Common;
import util.Log;
import util.Log.Priority;

public class DriverFactory {

	private static WebDriver driver = null;
	private static Log log = new Log(DriverFactory.class);
	

	@SuppressWarnings("unchecked")
	public static AndroidDriver<MobileElement> getMobDriver(){
		return (AndroidDriver<MobileElement>) driver;
	}

	public static WebDriver getWebDriver(){
		return driver;
	}
	
	protected static void setDriver(String platform) {
		try {
			 if(driver == null) {
				 if(platform.equalsIgnoreCase("ANDROID")) {
					File path = new File(Globals.GC_APK_LOC+Common.getGlobalParam("APK_FILE_NAME"));
					DesiredCapabilities dsAnd = new DesiredCapabilities();
					dsAnd.setCapability("platformName",Common.getGlobalParam("PLATFORM_NAME"));
					dsAnd.setCapability("deviceName", Common.getGlobalParam("DEVICE_NAME"));
					dsAnd.setCapability("appPackage", Common.getGlobalParam("APP_PACKAGE"));
					dsAnd.setCapability("appActivity", Common.getGlobalParam("APP_ACTIVITY"));
					dsAnd.setCapability("newCommandTimeout", Integer.parseInt(Common.getGlobalParam("SESSION_TIMEOUT")));
					dsAnd.setCapability("app", path.getAbsolutePath());
					
					driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"),dsAnd);
				 }else if(platform.equalsIgnoreCase("IOS")) {
					 
				 }else if(platform.equalsIgnoreCase("chrome")){
					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//"
													 +Common.getGlobalParam("ChromeDriverPath"));
					ChromeOptions options = new ChromeOptions();
					options.setExperimentalOption("prefs", chromePrefs);
					options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation")); 
                    options.addArguments("test-type");
                    options.addArguments("start-maximized");
                    options.addArguments("disable-infobars");
                    options.addArguments("chrome.switches","--disable-extensions");
					DesiredCapabilities cap = DesiredCapabilities.chrome();
					cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap.setCapability(ChromeOptions.CAPABILITY, options);
					options.addArguments("--start-maximized");
						
					driver = new ChromeDriver(cap);
				 }else {
					 throw new Exception("Unknown Mobile Platform Type :"+platform);
				 }
			 }
		}catch(Exception e) {
			log.Report(Priority.ERROR, "Unable to initialize appium driver :"+e.getMessage());		
		}
	}
	
}
