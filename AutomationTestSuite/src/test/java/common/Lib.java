package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lib.Common;
import lib.Mob;
import util.Log;
import util.Log.Priority;


public class Lib {

	private static Log log = new Log(Lib.class);

	public static void NavigateToHome(AndroidDriver<MobileElement> driver) {
		try {
			if(Mob.isMobElementDisplayed(driver,"id",Common.getElement("rightMenu"), true)) {
				   Mob.clickOnElement(driver.findElement(By.id(Common.getElement("rightMenu"))));
				   Mob.waitForElement(driver.findElement(By.xpath(Common.getElement("menuHome"))));
				   Mob.clickOnElement(driver.findElement(By.xpath(Common.getElement("menuHome"))));
				}		
		}catch(Exception e) {
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
	}
	
	public static boolean checkPattern(String pattern, String text){
		try{
			Pattern patrn = Pattern.compile(pattern);
		    Matcher matcher = patrn.matcher(text.trim());
		    boolean matches = matcher.matches();   
			return matches;
		}catch(Exception e){
			log.Report(Priority.ERROR, Common.shortenedStackTrace(e, 10));
		}
		return false;
	}
	
	public static void NavigateBack(AndroidDriver<MobileElement> driver) {
		log.Report(Priority.INFO, "Android navigating back");
		driver.navigate().back();
	}
	
	
}
