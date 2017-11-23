package lib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import core.Globals;
import util.Log;
import util.Log.Priority;

public class Common {
	private static Map<String, String> globalParam = new LinkedHashMap<String, String>();
	private static Log log = new Log(Common.class);
	private static Properties OR = null;

	/**
	 * <h1>getGlobalParam</h1> getGlobalParam gets global access to the
	 * configuration properties defined in TestConfig.properties
	 * 
	 * @param String
	 *            paramName
	 * @return String
	 */
	public static String getGlobalParam(String paramName) {
		if (!globalParam.isEmpty() && globalParam.containsKey(paramName.toUpperCase())) {
			return globalParam.get(paramName.toUpperCase());
		}
		return Globals.GC_EMPTY;
	}
	
	/**
	 * <h1>loadCustomOR</h1> loads custom object repository
	 *
	 * @return void
	 */
	public static void loadCustomOR() throws FileNotFoundException, IOException {
		OR = new Properties();
		OR.load(new FileInputStream(Globals.GC_CUSTOM_OR+Common.getGlobalParam("AUT")+".properties"));
	}
	
	/**
	 * <h1>getElement</h1> gets xpath as string from custom objec
	 * repository
	 *
	 * @param String
	 *            locatorKey
	 * @param String...
	 *            dynamicAttribute
	 * @return String
	 */
	public static String getElement(String locatorKey, String... dynamicAttribute) throws Exception {
		String locator = null;
		if (OR.isEmpty() || OR == null)
			throw new Exception("Custom OR is not loaded");
		try {
			if (dynamicAttribute.length == 0) {
				return OR.getProperty(locatorKey);
			} else {
				locator = OR.getProperty(locatorKey);
				for (String loc : dynamicAttribute) {
					locator = locator.replaceFirst("@@Value@@", loc);
				}
			}
		} catch (Exception e) {
			throw new Exception("Locator " + locatorKey + " not found");
		}
		return locator;
	}

	/**
	 * <h1>setGlobalParam</h1> setGlobalParam updates the global configuration
	 * value at runtime
	 * 
	 * @param String
	 *            parameterName
	 * @param String
	 *            parameterValue
	 * @return void
	 */
	public static void setGlobalParam(String parameterName, String parameterValue) {
		if (!globalParam.containsKey(parameterValue)) {
			globalParam.put(parameterName.trim().toUpperCase(), parameterValue);
		}
	}

	/**
	 * <h1>loadConfigProperty</h1> loadConfigProperty loads
	 * TestConfig.properties into global map
	 * 
	 * @param String
	 *            configPath
	 * @return void
	 */
	public static void loadConfigProperty(String configPath) {
		Properties prop = new Properties();
		InputStream ins = null;
		if (globalParam.isEmpty()) {
			try {
				ins = new FileInputStream(configPath);
				log.Report(Priority.INFO, "reading " + configPath + " file");
				prop.load(ins);
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String val = prop.getProperty(key);
					globalParam.put(key.toString().trim().toUpperCase(), val);
					log.Report(Priority.DEBUG, "setting @globalParam with key :" + key + " -- value :" + val);
				}
				globalParam.remove(Globals.GC_EMPTY);
			} catch (IOException ex) {
				log.Report(Priority.ERROR, "Unable to read Config :" + ex.getMessage());
			} catch (Exception e) {
				log.Report(Priority.ERROR, "Unable to read Config :" + e.getMessage());
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						log.Report(Priority.ERROR, "Unable to read Config :" + e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * <h1>shortenedStackTrace</h1> generates short stack trace to be reported
	 * in HTML report
	 * 
	 * @param Throwable
	 *            t
	 * @param int
	 *            maxLines
	 * @return String
	 */
	public static String shortenedStackTrace(Throwable t, int maxLines) {
		String str = logIt(t);
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		String[] lines = writer.toString().split("\n");
		StringBuilder sb = new StringBuilder();
		sb.append("[" + str + "]\n");
		for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
			sb.append(lines[i]).append("\n");
		}
		return sb.toString();
	}

	/**
	 * <h1>logIt</h1> generates detailed log for the thrown exception
	 * 
	 * @param Throwable
	 *            t
	 * @return Strings
	 */
	private static String logIt(Throwable e1) {
		StackTraceElement[] stacktrace = e1.getStackTrace();
		List<String> res = new ArrayList<String>();
		String finalTrace = "";
		for (int i = 0; i < stacktrace.length; i++) {
			StackTraceElement e = stacktrace[i];
			String methodName = e.getMethodName();

			if (e.getClassName().startsWith("pageobject")) {
				res.add("Class " + e.getClassName() + " -- Method " + methodName + " @ line "
						+ stacktrace[i].getLineNumber());
			}
		}
		for (String str : res) {
			finalTrace = finalTrace + str + " ;";
		}
		return finalTrace;
	}
}
