package com.technology.jep.jepria.auto.conditions;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ExpectedConditions {
  
  /**
   * Проверка отображения одного из Web-элементов, представленных списком ID
   * @param args
   * @return true, если одно из элементов отображается
   */
  public static ExpectedCondition<Boolean> oneOfElementsLocatedVisible(By... args) {
    final List<By> byes = Arrays.asList(args);
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        for (By by : byes) {
          WebElement el;
          try {
            el = driver.findElement(by);
          } catch (Exception r) {
            continue;
          }
          if (el.isDisplayed()) {
            return true;
          }
        }

        return false;
      }
    };
  }
  
  /**
   * Проверка выполнения одного из условий, представленных списком ConditionChecker-ов 
   * @param args - список ConditionChecker-ов
   * @return true, если одно из условий выполнено
   */
//  public static ExpectedCondition<Boolean> atLeastOneOfConditionIsSatisfied(ConditionChecker... args) {
//    final List<ConditionChecker> checkers = Arrays.asList(args);
//    return new ExpectedCondition<Boolean>() {
//      @Override
//      public Boolean apply(WebDriver driver) {
//        for (ConditionChecker checker : checkers) {
//          if(checker.isSatisfied()) {
//            return true;
//          }
//        }
//
//        return false;
//      }
//    };
//  }

  public static ExpectedCondition<ConditionChecker> atLeastOneOfConditionIsSatisfied(ConditionChecker... args) {
    final List<ConditionChecker> checkers = Arrays.asList(args);
    return new ExpectedCondition<ConditionChecker>() {
      @Override
      public ConditionChecker apply(WebDriver driver) {
        for (ConditionChecker checker : checkers) {
          if(checker.isSatisfied()) {
            return checker;
          }
        }

        return null;
      }
    };
  }
}
