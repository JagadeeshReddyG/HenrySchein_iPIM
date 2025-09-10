package com.pim.factories;

import com.pim.constants.Constants;
import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;

public final class ExplicitWaitFactory {

   private ExplicitWaitFactory() {
   }


   public static WebElement performExplicitWait(WaitLogic waitstrategy, By by) {
      WebElement element = null;
      if (waitstrategy == WaitLogic.CLICKABLE) {
         element = new WebDriverWait(DriverManager.getDriver(), Constants.getExplicitWait())
               .until(ExpectedConditions.elementToBeClickable(by));
      } else if (waitstrategy == WaitLogic.PRESENCE) {
         element = new WebDriverWait(DriverManager.getDriver(), Constants.getExplicitWait())
               .until(ExpectedConditions.presenceOfElementLocated(by));
      } else if (waitstrategy == WaitLogic.VISIBLE) {
         element = new WebDriverWait(DriverManager.getDriver(), Constants.getExplicitWait())
               .until(ExpectedConditions.visibilityOfElementLocated(by));
      } else if (waitstrategy == WaitLogic.NONE) {
         element = DriverManager.getDriver().findElement(by);
      }
      else if (waitstrategy == WaitLogic.INVISIBLE) {
		  new WebDriverWait(DriverManager.getDriver(), Constants.getExplicitWait())
				.until(ExpectedConditions.invisibilityOf(DriverManager.getDriver().findElement(by)));

	}
      return element;
   }

   public static WebElement performExplicitWait(WaitLogic waitstrategy, By by, int timeout) {
      WebElement element = null;
      if (waitstrategy == WaitLogic.CLICKABLE) {
         element = new WebDriverWait(DriverManager.getDriver(), timeout)
               .until(ExpectedConditions.elementToBeClickable(by));
      } else if (waitstrategy == WaitLogic.PRESENCE) {
         element = new WebDriverWait(DriverManager.getDriver(), timeout)
               .until(ExpectedConditions.presenceOfElementLocated(by));
      } else if (waitstrategy == WaitLogic.VISIBLE) {
         element = new WebDriverWait(DriverManager.getDriver(), timeout)
               .until(ExpectedConditions.visibilityOfElementLocated(by));
      } else if (waitstrategy == WaitLogic.NONE) {
         element = DriverManager.getDriver().findElement(by);
      }
      else if (waitstrategy == WaitLogic.INVISIBLE) {
		  new WebDriverWait(DriverManager.getDriver(), timeout)
				.until(ExpectedConditions.invisibilityOf(DriverManager.getDriver().findElement(by)));

	}
      return element;
   }
   
   public static boolean invisibilityOfAnElement(WaitLogic waitstrategy, By by, int timeout) {
	   boolean flag = false;
	   if (waitstrategy == WaitLogic.INVISIBLE) {
	         flag = new WebDriverWait(DriverManager.getDriver(), timeout)
	               .until(ExpectedConditions.invisibilityOfElementLocated(by));
	      }
	   return flag;
   }


}