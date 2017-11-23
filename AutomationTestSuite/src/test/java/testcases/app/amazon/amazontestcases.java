package testcases.app.amazon;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import core.TestBase;
import lib.Common;
import lib.Data;
import pageobjects.amazon.CartPage;
import pageobjects.amazon.HomePage;
import pageobjects.amazon.ProductDetailPage;
import pageobjects.amazon.SearchResultPage;
import util.reporter.ReportManager;
import util.reporter.Status;

public class amazontestcases extends TestBase{

	HomePage home = null;
	SearchResultPage result = null;
	ProductDetailPage prod = null;
	CartPage cart = null;
	
	@BeforeTest
	public void launchApp() {
		if(webDriver().getTitle().isEmpty()) {
		   webDriver().get(Common.getGlobalParam("URL_"+Common.getGlobalParam("TEST_ENV")));
		}
	}
	
	/*
	 * Verify_Added_Amazon_Items_in_Cart
	*/
	@Test(dataProvider="setData")
	public void Auto_TC001(int itr, Object o){
		home = new HomePage(webDriver());
		home.addProduct(Data.getVal("Category"), Data.getVal("Product"));
		
		result = home.performSearch();
		if(result.verifySearchResult(Data.getVal("Product"))) {
		   ReportManager.GetReporter().Log(Status.PASS,"Verify if search of "+Data.getVal("Product")+" is successfull", 
				                           "Product search is successfull", false);
		} else {
		   ReportManager.GetReporter().Log(Status.FAIL,"Verify if search of "+Data.getVal("Product")+" is successfull", 
                    					   "Product search failed", true);
		}
		
		prod  = result.selectResult();	
		if(prod.verifyExpectedProdDisplayed(Data.getVal("Product"))) {
			 ReportManager.GetReporter().Log(Status.PASS,"Verify if expected product "+Data.getVal("Product")
			         +" is displayed in Product details page", "Verification successfull", false);
		} else {
			 ReportManager.GetReporter().Log(Status.FAIL,"Verify if expected product "+Data.getVal("Product")
	         		+" is displayed in Product details page", "Verification failed", true);
		}
		
		prod.addToCart(Data.getVal("Quantity"));
		
		cart = prod.navigateToCart();
		if(cart.verifyAdded(Data.getVal("Product"))) {
			 ReportManager.GetReporter().Log(Status.PASS,"Verify if expected product "+Data.getVal("Product")
	         +" is displayed in Cart page", "Verification successfull", false);
		} else {
			 ReportManager.GetReporter().Log(Status.PASS,"Verify if expected product "+Data.getVal("Product")
	         +" is displayed in Cart page", "Verification failed", true);
		}
	}
	
}
