package com.technology.jep.test.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public class ScreenshotMaker implements ITestListener {

  public ScreenshotMaker() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void onFinish(ITestContext result) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onStart(ITestContext result) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTestFailure(ITestResult result) {
    
    File imageFile = ((TakesScreenshot) WebDriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
    String failureImageFileName = result.getMethod().getMethodName()
        + new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime()) + ".png";
            File failureImageFile = new File(".\\test-output\\screenshots\\"+failureImageFileName);
    try {
      FileUtils.copyFile(imageFile, failureImageFile);
    } catch (IOException e) {
      System.out.println("Can't save screenshot " + failureImageFileName);
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTestStart(ITestResult result) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTestSuccess(ITestResult result) {
    // TODO Auto-generated method stub

  }

}
