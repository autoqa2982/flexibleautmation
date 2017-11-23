package core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lib.Common;
import util.Log;
import util.Log.Priority;

public class TestBase {

	protected static LinkedHashMap<Integer, LinkedHashMap<String, String>> testDataObj = null;
	protected static LinkedHashMap<Integer, List<LinkedHashMap<String, String>>> singleItrAllTetsDataObj = null;
	private static Map<Long,String> manualTCName = new HashMap<Long,String>();
	private Log log = new Log(TestBase.class);
	
	protected AndroidDriver<MobileElement> mobDriver(){
		return DriverFactory.getMobDriver();
	}
	
	protected WebDriver webDriver(){
		return DriverFactory.getWebDriver();
	}
	
	@DataProvider
	public Object[][] setData(ITestNGMethod method, ITestContext test){
		String tcClassPkgNm = Globals.GC_EMPTY;
		String tcName = Globals.GC_EMPTY;
		IGetData getData = null;
		Object[][] dataProviderObj = null;
	
		try{
			tcClassPkgNm = method.getRealClass().getPackage().getName();
			tcName = test.getName();
			manualTCName.put(Thread.currentThread().getId(), tcName);
			log.Report(Priority.INFO,"Setting data provider with TestData "
			                  +Common.getGlobalParam("TESTDATA_FORMAT"));
		    getData = new GetXLData();
		 
		    testDataObj = getData.getTestDataMap(tcClassPkgNm, tcName);
		    if (testDataObj != null) {
		    	dataProviderObj = new Object[testDataObj.size()][2];
				int count = 0;
				
				for(Integer itr : testDataObj.keySet()){
					dataProviderObj[count][0] = itr;
					dataProviderObj[count][1] = null;
					count++;
				}	
				return dataProviderObj;
			} 
		}catch(Exception e){
			log.Report(Priority.ERROR, "Unable to initialize DataProvider :"+e.getMessage());
		}
		return new Object[][] {{}}; // As null cannot be returned in DataProvider	
	}

	@DataProvider
	public Object[][] setAllDataAtSingleItr(ITestNGMethod method, ITestContext test){
		String tcClassPkgNm = Globals.GC_EMPTY;
		String tcName = Globals.GC_EMPTY;
		IGetData getData = null;
		Object[][] dataProviderObj = null;
	
		try{
			tcClassPkgNm = method.getRealClass().getPackage().getName();
			tcName = test.getName();
			manualTCName.put(Thread.currentThread().getId(), tcName);
			log.Report(Priority.INFO,"Setting data provider with TestData "
			                  +Common.getGlobalParam("TESTDATA_FORMAT"));
		    if(Common.getGlobalParam("TESTDATA_FORMAT").equals("XLSX")
		       ||Common.getGlobalParam("TESTDATA_FORMAT").equals("XLS")){
		       getData = new GetXLData();
		    }

		    singleItrAllTetsDataObj = getData.getAllDataForSingleItr(tcClassPkgNm, tcName);
		    if (singleItrAllTetsDataObj != null) {
		    	dataProviderObj = new Object[singleItrAllTetsDataObj.size()][2];
				int count = 0;
				
				for(Integer itr : singleItrAllTetsDataObj.keySet()){
					dataProviderObj[count][0] = itr;
					dataProviderObj[count][1] = null;
					count++;
				}	
				return dataProviderObj;
			} 
		}catch(Exception e){
			log.Report(Priority.ERROR, "Unable to initialize DataProvider :"+e.getMessage());
		}
		return new Object[][] {{}}; // As null cannot be returned in DataProvider	
	}
}
