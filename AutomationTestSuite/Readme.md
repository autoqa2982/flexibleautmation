# Automation Test Suite Quick Start guide
---------------------------------------------------------

This is quick start guide for a TestNG based automation test framework. It supports both mobile and web application automation requirements, as an example Amazon web and AliExpress mobile app test cases are included within the project. This guide is prepared keeping Windows 7 and above in mind.

# User Guide
---------------------------------------------------------

## Features included

1. TestNG driven
2. Mavenised dependency management
3. Adaptable for both mobile and web app automation requirements
4. Common Web app interaction library
5. Common Mobile app interaction library
6. External Test Configuration
7. Easy way of selecting desired testcase sets to run
8. Utility implemented for Excel interaction and generating TestNG run xml
9. Included scalablity to extract data from other data sources
10. Easy access to data sets for multiple iterations 
11. Independent core and test modules
12. Maven support for compiling and running tests 
13. Generates sophisticated test report in html
14. Powerfull logging facility
15. Scalable to Keyword driven framework

## Environment requirement

1. JAVA JDK
2. Maven
3. Android studio 
4. NodeJS
5. .Net framework
6. GIT bash


## Project installation

```
$ git clone https://souvik2984@bitbucket.org/souvik2984/automationtestsuite.git
```

## Updating TestConfig.properties
Update TestConfig.properties in order to execute tests in mobile or web app. There are various configuration properties available, however following are the basic properties that needs to be updated. 

Maven sure-fire-plugin can also be used in order update these at run time 


```
# Application Under Test
AUT=ALI

#BROWSER = FIREFOX / CHROME / IE / ANDROID
BROWSER =ANDROID

#URL
URL_PROD = https://www.amazon.com/

# DEVICE CAPABILITIES
PLATFORM_NAME=Android
APP_PACKAGE= com.alibaba.aliexpresshd
APP_ACTIVITY = com.alibaba.aliexpresshd.module.home.MainActivity
SESSION_TIMEOUT= 3000
DEVICE_NAME= oneplus5
APK_FILE_NAME =AliExpress.apk

```
## Executing test cases
Test cases can be executed with  below commandfrom  command line interface, however for convenience .bat files are included as described down.

AliExpress mobile app and Amazon web app test cases are added as a part of the project. In order to execute AliExpress test user can trigger following or run ExecuteAndroidTest.bat

Note : To execute mobile app test suite DEVICE_NAME= oneplus5 must be updated
```
mvn clean test -Daut=ALI -DbrowserName=ANDROID -DsuiteXmlFile=RunXML/AndroidTest.xml
```
similarly for Amazon test suite ExecuteWebTest.bat should be executed

Note : To execute mobile app test suite 

1) Device must be connected to adb service
2) Appium server must be up and running



## Report generation
HTML reports are generated out of the box post execution under TestReport directory. Extent report API has been used in order to generate custom reports, every test execution generates new time stamped directory under TestReport/HTMLReport with report artifacts.


# Developers Guide
---------------------------------------------------------
The framework has been separated between core and test components. This creates a greater level of abstraction for the developers as they dont have to bother about core functioning of the system and still will be able use driver instance, test configurations, data sets, logger and reporters.

## Test Config update 
Test case detailes are kept in RunOrder sheet (RunOrder_Amazon.xlsx).
The framework can support multiple projects simultaneously, and project details can be updated by updating AUT parameter in TestConfig.properties
```
# Application Under Test
AUT=AMAZON
```
Depending on the AUT parameter, corresponding RunOrder or TestData files gets picked up.

## Test case management
Test case detailes are kept in RunOrder sheet (RunOrder_Amazon.xlsx). RunOrder sheet requires following parameters
1. Manual Test Name
2. Automation Test Name
3. Automation Test Class path
4. Run Status

util.DriverXMLGenerator is a main class and needs to be  executed in order to get the run details from RunOrder sheet and generate TestNG run xml

```
	<test name="Verify_Added_Amazon_Items_in_Cart">
        <classes>
            <class name="testcases.app.amazon.amazontestcases">
                <methods>
                    <include name="Auto_TC001"/>
                </methods>
            </class>
        </classes>
    </test>
```

## Test case implementation
Test developers needs to create TestNG test extending to TestBase and implement @Test methods as specified in RunOrder sheet under same class path

```
	public class alitestcases extends TestBase{
	
	/*
	 * Verify_AliExpress_Category_List_Items
	*/
	@Test(dataProvider = "setAllDataAtSingleItr")
	public void Auto_TC001(int itr, Object o) {
		splash = new SplashPage(mobDriver());
		home = splash.closeHomeOverLay();
	}
```

## Accessing test data
Test data is maintained in TestData file, depending on AUT parameter in TestConfig.properties corressponding data file is accessed.

Note: package name mentioned in TestNG.xml classpath is the correspinding data sheet, therefore in example below 'amazon' is sheet name in TestData_AMAZON.xlsx file

```
	<class name="testcases.app.amazon.amazontestcases">
```

setData and setAllDataAtSingleItr are DataProvider methods. TestNG test cases can choose to use setData or setAllDataAtSingleItr as DataProvider depending upon following requirements.

Option A : Using setData as DataProvider, tags every data set extracted from data sheet to the iteration number 

```
	public class alitestcases extends TestBase{
	@Test(dataProvider = "setData")
	public void Auto_TC002(int itr, Object o){	
		home = new HomePage(mobDriver());	
	}
```

Option B : Using setAllDataAtSingleItr as DataProvider, tags all data set extracted from data sheet to a single iteration number 

```
	/*
	 * Verify_AliExpress_Category_List_Items
	*/
	@Test(dataProvider = "setAllDataAtSingleItr")
	public void Auto_TC001(int itr, Object o) {
		splash = new SplashPage(mobDriver());
		home = splash.closeHomeOverLay();
	}
```

## Accessing mobile or web driver instance
Depending upon Test Configuration, respective driver gets instantiated. In order to access the driver from test class user should access
mobDriver() or webDriver()

```
	@Test(dataProvider = "setAllDataAtSingleItr")
	public void Auto_TC001(int itr, Object o) {

	splash = new SplashPage(mobDriver());
	
	}
```

```
	@Test(dataProvider = "setAllDataAtSingleItr")
	public void Auto_TC001(int itr, Object o) {

	splash = new SplashPage(webDriver());
	
	}
```

## Page Objects
Test cases can be written by following Page Object Model pattern and then accessed withing test methods.

```
	public class HomePage {
	
	@FindBy(xpath=".//android.widget.TextView[@text='Categories']/..")
	private WebElement widCategories;
	
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
```

## Report logger
Report logger can be accessed as following

```
 ReportManager.GetReporter().Log(logStatus, stepName, stepDetails, captureScreen);
```
Report system is implemented to be in sync with ITestResult, therefore the final test status is fully dependent on custom report status.





