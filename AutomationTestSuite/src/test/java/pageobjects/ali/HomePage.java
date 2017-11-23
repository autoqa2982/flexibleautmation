package pageobjects.ali;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import common.Lib;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lib.Mob;
import util.Log;
import util.Log.Priority;

public class HomePage {
	
	@FindBy(xpath=".//android.widget.TextView[@text='Categories']/..")
	private WebElement widCategories;
	
	@FindBy(id="com.alibaba.aliexpresshd:id/search_hint")
	private WebElement globalSearchHome;
	
	@FindBy(id="com.alibaba.aliexpresshd:id/tv_tv2")
	private WebElement searchOverLay;
	
	@FindBy(id="com.alibaba.aliexpresshd:id/abs__search_src_text")
	private WebElement globalSearchText;
	
	@FindBy(xpath=".//android.widget.TextView[@resource-id='android:id/text1']")
	private List<WebElement> srchResultList; 
	
	@FindBy(xpath=".//android.widget.TextView[@resource-id='com.alibaba.aliexpresshd:id/tv_product_list_tagged_title']")
	private WebElement eleShirtTitle;
	
	@FindBy(xpath=".//android.widget.TextView[@resource-id='com.alibaba.aliexpresshd:id/tv_productsummary_price']")
	private WebElement eleShirtPrice;
	
	@FindBy(xpath=".//android.widget.TextView[@resource-id='com.alibaba.aliexpresshd:id/tv_product_list_feedback_rating_score']")
	private WebElement eleShirtRating;
	

	private AndroidDriver<MobileElement> driver = null;
	private Log log = null;

	public HomePage(AndroidDriver<MobileElement> driver) {
		this.driver = driver;
		PageFactory.initElements(driver,this);
		log = new Log(HomePage.class);
		if(!Mob.isMobElementDisplayed(widCategories,true)) {
			throw new IllegalStateException("This is not AliExpress Home page");
		}
	}
	
	/*
	 * Navigate to AliExpress Category List
	*/
	public CategoryListPage navigateToCategoryList() {
		if (Mob.isMobElementDisplayed(widCategories, true)) {
			Mob.clickOnElement(widCategories);
		}
		return new CategoryListPage(driver);
	}

	/*
	 * Performing global search for AliExpress
	*/
	public boolean performGlobalSearch(String val) {
		log.Report(Priority.INFO, "Performing global search for item "+ val);
		Mob.waitForElement(globalSearchHome); Mob.clickOnElement(globalSearchHome);
		Mob.waitForElement(searchOverLay); Mob.clickOnElement(searchOverLay);
		Mob.waitForElement(globalSearchText); Mob.clickOnElement(globalSearchText);
		Mob.setTextToTextBox(globalSearchText, val);
		if(globalSearchText.getText().equals(val)) return true;
		return false;
	}

	/*
	 * Verify search result for Global search
	*/
	public boolean verifySearchResult(String phoneDet) {
		log.Report(Priority.INFO, "Verify search result for " + phoneDet);
		List<WebElement> res = srchResultList;
		Mob.clickOnElement(res.get(0));

		Mob.waitForElement(eleShirtTitle);
		String itemTitle = eleShirtTitle.getText();
		String itemPrice = eleShirtPrice.getText();
		String itemRating = eleShirtRating.getText();

		log.Report(Priority.INFO, "Verify Product details for : item " + itemTitle);
		if (itemTitle.toLowerCase().contains(phoneDet.toLowerCase())
				&& Lib.checkPattern("[+-]?([0-9]*[.])?[0-9]+", itemPrice.replaceAll("[^\\d.]+|\\.(?!\\d)", ""))
				&& Lib.checkPattern("[+-]?([0-9]*[.])?[0-9]+", itemRating)) {
			return true;
		} 
		return false;
	}

}
