package util.reporter;

import java.util.Vector;
import org.testng.ITestResult;


public class Reporter implements IReporter{

	Vector<IReporter> reporter;
	
	/**
	 * <h1>Reporter</h1> Reporter constructor initialize HTML or PDF reporter objects and them to the Vector
	 * 
	 * @param String rptType
	 */
	public Reporter(String rptType){
		reporter =  new Vector<IReporter>();
	
		switch(rptType){
			case "PDF" : 
			case "HTML" : 
				reporter.add(new HTMLReporter("HTMLReport.html"));break;	
			default: 
				reporter.add(new HTMLReporter("filepath"));break;
		}	
	}
	
	/**
	 * <h1>StartTest</h1> it starts the test reporting
	 * 
	 * @param String testName
	 * @return void
	 */
	@Override
	public synchronized void StartTest(String testName) {
		for(IReporter rep : reporter){
			rep.StartTest(testName);
		}
	}

	/**
	 * <h1>Log</h1> it logs step status, step name, step details, if capture screen
	 * 
	 * @param Status logStatus
	 * @param String stepName
	 * @param String stepDetails
	 * @param boolean captureScreen
	 * @return void
	 */
	@Override
	public synchronized void Log(Status logStatus, String stepName, String stepDetails, boolean captureScreen) {
		for(IReporter rep : reporter){
			rep.Log(logStatus,stepName,stepDetails,captureScreen);
		}
	}

	/**
	 * <h1>EndTest</h1> after logging various steps, EndTest is called to stop further logging of steps
	 * 
	 * @param ITestResult result
	 * @return void
	 */
	@Override
	public synchronized void EndTest(ITestResult result) {
		for(IReporter rep : reporter){
			rep.EndTest(result);
		}
	}

	/**
	 * <h1>FlushReport</h1> after end test, everything which is logged gets written to the report
	 * 
	 * @return void
	 */
	@Override
	public synchronized void FlushReport() {
		for(IReporter rep : reporter){
			rep.FlushReport();
		}
	}

	/**
	 * <h1>CloseReport</h1> after flush, all resources gets cleaned
	 * 
	 * @return void
	 */
	@Override
	public void CloseReport() {
		for(IReporter rep : reporter){
			rep.CloseReport();
		}	
	}
	


	


}
