package pageobjects.ali;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lib.Data;
import lib.Mob;
import util.Log;
import util.Log.Priority;

public class CategoryListPage {

	@FindBy(xpath = ".//android.widget.TextView[contains(@resource-id,'tv_pic_text_mix_item_category_name')]")
	private List<WebElement> catList;
	
	@FindBy(id = "com.alibaba.aliexpresshd:id/menu_overflow")
	private WebElement rightMenu;
	
	@FindBy(xpath = ".//android.widget.ListView/android.widget.RelativeLayout[1]")
	private WebElement HomeMenu;
	
	private AndroidDriver<MobileElement> driver = null;
	private Log log = null;
	private static LinkedHashSet<String> appCategory = new LinkedHashSet<String>();

	public CategoryListPage(AndroidDriver<MobileElement> driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		log = new Log(CategoryListPage.class);
		if(!Mob.isMobElementsDisplayed(catList,true)) {
			throw new IllegalStateException("This is not AliExpress Home page");
		}
	}

	/*
	 * Verify if all category items are present as expected
	*/
	public boolean verifyCategoryList() {
		List<String> cat = new ArrayList<String>();
		int count = 0; boolean verifyCat = true;
	
		getCategoryList();
		cat.addAll(appCategory);

		for (LinkedHashMap<String, String> mp : Data.getTestDataForSingleItr()) {
			if (!mp.get("CATEGORIES").equals(cat.get(count))) {
				log.Report(Priority.ERROR,"Item : "+mp.get("CATEGORIES")+" is not present in actual category list");
				verifyCat = false;break;			}
			log.Report(Priority.INFO,"Item : "+mp.get("CATEGORIES")+" is present in actual category list as expected");
			count++;
		}
		return verifyCat;
	}
	
	/*
	 * Retrieving category list from the application 
	*/
	private void getCategoryList() {
		Mob.waitForElements(catList);

		List<WebElement> list = catList;
		int listSize = list.size();
		int count = 0;

		for (WebElement ele : list) {
			count++;
			if (appCategory.contains(list.get(list.size() - 1).getText()))
				break;
			appCategory.add(ele.getText());
			System.out.println(ele.getText());
			log.Report(Priority.INFO,"Item listed in Category list " + ele.getText());
			if (count == listSize) {
				Mob.ScrollDown(driver);
				getCategoryList();
			}
		}
	}
	
	/*
	 * Navigating to Home	*/
	public HomePage navToHome() {
		if (Mob.isMobElementDisplayed(rightMenu)) {
			Mob.clickOnElement(rightMenu);
			Mob.waitForElement(HomeMenu);
			Mob.clickOnElement(HomeMenu);
		}
		return new HomePage(driver);
	}

}
