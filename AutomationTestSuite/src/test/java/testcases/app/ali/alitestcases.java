package testcases.app.ali;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import core.TestBase;
import lib.Common;
import lib.Data;
import pageobjects.ali.CategoryListPage;
import pageobjects.ali.HomePage;
import pageobjects.ali.SplashPage;
import util.reporter.ReportManager;
import util.reporter.Status;

public class alitestcases extends TestBase{
	
	HomePage home = null;
	CategoryListPage catList = null;
	SplashPage splash = null;	
	
	/*
	 * Verify_AliExpress_Category_List_Items
	*/
	@Test(dataProvider = "setAllDataAtSingleItr")
	public void Auto_TC001(int itr, Object o) {
		splash = new SplashPage(mobDriver());
		home = splash.closeHomeOverLay();
		catList = home.navigateToCategoryList();
		if(catList.verifyCategoryList()) {
		   ReportManager.GetReporter().Log(Status.PASS,"Verify if all expected AliExpress category items are a part of Actual List"
				                            ,"Verification successfull", false);
		}else {
		   ReportManager.GetReporter().Log(Status.FAIL,"Verify if all expected AliExpress category items are a part of Actual List"
				                            ,"Verification failed", true);
		}
		catList.navToHome();
	}
	
	/*
	 * Verify_AliExpress_Global_Search_Result
	*/
	@Test(dataProvider = "setData")
	public void Auto_TC002(int itr, Object o){	
		home = new HomePage(mobDriver());
		
		if(home.performGlobalSearch(Data.getVal("SearchText"))) {
		   ReportManager.GetReporter().Log(Status.PASS,"Verify if Global Search performed successfully for the text "
		              +Data.getVal("SearchText"),"Verification successfull", false); 
		}else {
		   ReportManager.GetReporter().Log(Status.FAIL,"Verify if Global Search performed successfully for the text "
		              +Data.getVal("SearchText"),"Verification failed", true);
		}
		
		if(home.verifySearchResult(Data.getVal("VerifyText"))) {
			ReportManager.GetReporter().Log(Status.PASS, "Verify item title, price and rating present for " 
		                     + Data.getVal("VerifyText")," Item details are verified successfully",false);
		}else {
			ReportManager.GetReporter().Log(Status.FAIL, "Verify item title, price and rating present for " 
							+ Data.getVal("VerifyText")," Item details are verified successfully",true);
		}
	}
	
	@AfterSuite
	public void cleanAPP() {
		mobDriver().removeApp(Common.getGlobalParam("APP_PACKAGE"));
	}
	
}
