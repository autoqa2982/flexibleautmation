package util.reporter;

public class ReportManager {

	private static Reporter report  = new Reporter("HTML");
	
	/**
	 * <h1>GetReporter</h1> used for returning current IReporter object  
	 * @return IReporter
	 */
	public synchronized static IReporter GetReporter() {	
		return report;
	}

	
}
