package api.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener
{
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;

	public void onStart(ITestContext testContext) 
	{
	    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // Time Stamp
	    repName = "Test-Report-" + timeStamp + ".html";

	    sparkReporter = new ExtentSparkReporter(".\\Reports\\" + repName); // Specify location of the Report
	    
	    sparkReporter.config().setDocumentTitle("RestAssured AutomationProject"); // Title of report
	    sparkReporter.config().setReportName("Pet Store Users API"); // Name of the report
	    sparkReporter.config().setTheme(Theme.DARK);
	    
	    extent = new ExtentReports();
	    extent.attachReporter(sparkReporter);

	    extent.setSystemInfo("Application", "Pet Store Users API");
	    extent.setSystemInfo("Operating System", System.getProperty("os.name"));
	    extent.setSystemInfo("User Name", System.getProperty("user.name"));
	    extent.setSystemInfo("Environment", "QA");
	    extent.setSystemInfo("user", "Siva");
	}
	
	public void onTestSuccess(ITestResult result) 
	{
	    test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups()); //To display groups in report
        test.log(Status.PASS,result.getName()+" Got Executed Successfully");
	}

	public void onTestFailure(ITestResult result) 
	{
	    test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());

        test.log(Status.FAIL, result.getName() + " Test got Failed");
        test.log(Status.INFO, result.getThrowable().getMessage());
	}
	
	public void onTestSkipped(ITestResult result)
	{
	    test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, result.getName() + " Test got Skipped");
        test.log(Status.INFO, result.getThrowable().getMessage());
	}
	
	public void onFinish(ITestContext testContext)
	{
		extent.flush();
	        
	    String pathOfExtentReport = System.getProperty("user.dir")+"\\Reports\\"+repName;
	    File extentReport = new File(pathOfExtentReport);
	    try 
	    {
	       Desktop.getDesktop().browse(extentReport.toURI());
	    } catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }	
	}
}
