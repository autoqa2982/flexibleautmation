package pageobjects.ali;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lib.Mob;

public class SplashPage {
	
	@FindBy(id="com.alibaba.aliexpresshd:id/iv_close_poplayer")
	private WebElement btnClosePopUpHome;
	
	private AndroidDriver<MobileElement>  driver = null;
	
	public SplashPage(AndroidDriver<MobileElement>  driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		if(!Mob.isMobElementDisplayed(btnClosePopUpHome,true)) {
			throw new IllegalStateException("This is not AliExpress Splash Home page");
		}
	}
	
	/*
	 * close splash pop up 
	*/
	public HomePage closeHomeOverLay() {
		Mob.waitForElement(btnClosePopUpHome);	
		if (Mob.isMobElementDisplayed(btnClosePopUpHome, true)) {
			Mob.clickOnElement(btnClosePopUpHome);
		}
		return new HomePage(driver);
	}
}
