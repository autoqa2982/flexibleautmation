package core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import lib.Common;
import lib.Data;
import util.Log;
import util.Log.Priority;
import util.reporter.ReportManager;
import util.reporter.Status;

public class TestListener extends TestBase implements ITestListener, ISuiteListener, IInvokedMethodListener {
	
	private Log log = new Log(TestListener.class);
	private Map<String,String> failedTCMap = null;
	String failedTCDet = "";
	
	public void onStart(ISuite suite) {
		try {		
			Common.loadConfigProperty(Globals.GC_TESTCONFIGLOC 
					                + Globals.GC_CONFIGFILEANDSHEETNAME 
					                + ".properties");
			
			if((!System.getProperty("aut").equals(null)) & (!System.getProperty("browserName").equals(null))){
				System.out.println(System.getProperty("aut")+"--"+System.getProperty("browserName")) ;
				Common.setGlobalParam("AUT", System.getProperty("aut"));
				Common.setGlobalParam("BROWSER", System.getProperty("browserName"));
			}
			log.Report(Priority.INFO, "Test Configuration initialized successfully");
		} catch (Exception e) {
			log.Report(Priority.ERROR, e.getMessage());
		}
	}

	public void onStart(ITestContext test) {
			try {
				log.Report(Priority.INFO,
						" ******************* Execution started for " + test.getName() + "******************");
				
				if(Common.getGlobalParam("BROWSER").equalsIgnoreCase("android")){
					if(!mobDriver().getWindowHandle().isEmpty()){
						log.Report(Priority.INFO,
								"Web Driver instance found to be active for the Test Case :" + test.getName());
					}
				}else{
					if (!webDriver().getWindowHandle().isEmpty()) {
						log.Report(Priority.INFO,
								"Web Driver instance found to be active for the Test Case :" + test.getName());
					}
				}
			} catch (Exception t) {
				try {
					log.Report(Priority.INFO, "Web Driver instance found to be inactive for the Test Case :"
							+ test.getName() + " ,hence re-initiating");

					DriverFactory.setDriver(Common.getGlobalParam("BROWSER"));
					log.Report(Priority.INFO,
							"Web Driver instance re-initiated " + "successfully the Test Case :" + test.getName());
				} catch (Exception e) {
					log.Report(Priority.ERROR, "Failed to re-initialize Web Driver :" + e.getMessage());
				}
			}
	}

	public void onTestStart(ITestResult result) { }

	public void onTestSuccess(ITestResult result) { }

	public void onTestFailure(ITestResult result) {
		// capturing failed TCs
		failedTCMap = new LinkedHashMap<String,String>();
		failedTCMap.put(result.getTestContext().getName(),
				        result.getMethod().getQualifiedName().substring(0, result.getMethod().getQualifiedName().lastIndexOf("."))
				        +"|"+ result.getMethod().getMethodName());
	}

	public void onTestSkipped(ITestResult result) {	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }

	public void onFinish(ITestContext context) {
		try{		
		//	if(driver() != null) driver().quit();	
		}catch(Exception e){
			log.Report(Priority.ERROR, "Exception :" + e.getLocalizedMessage());
		}
	}

	public void onFinish(ISuite suite) {
		log.Report(Priority.INFO, "Close browser for suite :" + suite.getName());
		if(Common.getGlobalParam("BROWSER").equalsIgnoreCase("android")){
		   if(mobDriver()!=null) mobDriver().quit();
		}else{
		   if(webDriver()!=null) webDriver().quit();
		}

		ReportManager.GetReporter().CloseReport();
		log.Report(Priority.INFO, "Extent report closed");
	}

	@Override
	public synchronized void afterInvocation(IInvokedMethod method, ITestResult result) {
		if (method.isTestMethod()) {
			// Handling test exceptions and errors
			if (result.getThrowable() != null) {
				result.setStatus(ITestResult.FAILURE);
				log.Report(Priority.ERROR, "Failing test " + result.getName() + "exception occcured , Exception :"
														+ Common.shortenedStackTrace(result.getThrowable(), 10));
				ReportManager.GetReporter().Log(Status.FAIL,"Exception thrown", 
						                                  Common.shortenedStackTrace(result.getThrowable(), 5), true);
			}
			
			// Extent end test
			ReportManager.GetReporter().EndTest(result);
			log.Report(Priority.INFO, "Extent report end test for " + result.getName());
		
			// Sync testng run result with extent rpt
			if (result.getStatus() == ITestResult.FAILURE) {
				log.Report(Priority.INFO, "Setting testng report to fail in case of faliure  " + result.getName());
				result.getTestContext().getFailedTests().addResult(result, method.getTestMethod());
				result.getTestContext().getPassedTests().removeResult(method.getTestMethod());
			}
	
			ReportManager.GetReporter().FlushReport();
			log.Report(Priority.INFO, "Flushing report for " + result.getName());
		}
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult result) { 
		if (method.isTestMethod()) {
			log.Report(Priority.INFO, "Start extent report for " + result.getName());	
			ReportManager.GetReporter().StartTest(method.getTestResult().getTestContext().getName());
	
			// Setting global data
			try {
				if(testDataObj!=null){
					// Setting single iteration data
					log.Report(Priority.INFO, "Setting Global Data Map " + result.getName());
					Method m = Data.class.getDeclaredMethod("setVal", HashMap.class);
					m.setAccessible(true);
					m.invoke(null, testDataObj.get(result.getParameters()[0]));
				}
				
				if(singleItrAllTetsDataObj!=null){
					log.Report(Priority.INFO, "Setting Global Data Map " + result.getName());
					Method m = Data.class.getDeclaredMethod("setTestDataForSingleItr", List.class);
					m.setAccessible(true);
					m.invoke(null, singleItrAllTetsDataObj.get(result.getParameters()[0]));
				}
			} catch (Exception e) {
				log.Report(Priority.ERROR,
					"Setting Global Data Map Failed for " + result.getName() + " exception " + e.getMessage());
			}
		}
	}	

}