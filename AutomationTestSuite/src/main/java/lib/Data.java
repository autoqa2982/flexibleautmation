package lib;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Data {

	private static Map<Long, HashMap<String, String>> globalTestdata = 
			               new HashMap<Long, HashMap<String, String>>();
	
	private static Map<Long, List<LinkedHashMap<String, String>>> globalTestdataExtItr = 
            			   new HashMap<Long, List<LinkedHashMap<String, String>>>();

	/**
	 * <h1>setVal</h1> loads test data and tags every data set extracted from data sheet to the iteration number 
	 * 
	 * @param HashMap<String, String> td
	 * @return void

	 */
	@SuppressWarnings("unused")
	private static void setVal(HashMap<String, String> td){
		globalTestdata.put(Thread.currentThread().getId(), td);
	}
	
	/**
	 * <h1>setTestDataForSingleItr</h1> loads test data and tags all data set extracted from data sheet to a single iteration number
	 * 
	 * @param HashMap<String, String> td
	 * @return void
	 */
	@SuppressWarnings("unused")
	private static void setTestDataForSingleItr(List<LinkedHashMap<String, String>> td){
		globalTestdataExtItr.put(Thread.currentThread().getId(), td);
	}
	
	/**
	 * <h1>getVal</h1> gets testdata parameter value
	 * 
	 * @param String paramName
	 * @return String
	 */
	public static String getVal(String paramName){
		String value = "";
		try{
			if (globalTestdata.get(Thread.currentThread().getId()).containsKey(paramName.toUpperCase().trim())) {
				if (globalTestdata.get(Thread.currentThread().getId()).get(paramName.trim().toUpperCase()).length() > 0)
					value = globalTestdata.get(Thread.currentThread().getId()).get(paramName.trim().toUpperCase());
			} 
		}catch(Exception e){
			// do nothing
		}
		return value.trim();
	}
	
	/**
	 * <h1>getTestDataForSingleItr</h1> gets testdata map list tag to single itr
	 * 
	 * @return List<LinkedHashMap<String, String>> 
	 */
	public static List<LinkedHashMap<String, String>> getTestDataForSingleItr(){
		try{
			if (globalTestdataExtItr.get(Thread.currentThread().getId())!=null) {
				return globalTestdataExtItr.get(Thread.currentThread().getId());
			} 
		}catch(Exception e){
			// do nothing
		}
		return null;
	}
}
