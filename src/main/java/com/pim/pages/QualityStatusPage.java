package com.pim.pages;

import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import com.pim.driver.DriverManager;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;

public class QualityStatusPage extends BasePage {

	Javautils javautils = new Javautils();
	private final By qualityStatusTab = By.xpath("//div[contains(text(),'Quality status')]");
	private final By RefreshButtoninQualityStatus = By
			.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-refresh']");
	private final By rule = By.xpath("//div/a[text()='Rule']");
	private final By arrowUp = By.xpath(
			"//td[@class='v-table-header-cell-asc v-table-header-sortable']//div[@class='v-table-sort-indicator']");
	private final By arrowDown = By.xpath(
			"//td[@class='v-table-header-cell-desc v-table-header-sortable']//div[@class='v-table-sort-indicator']");
	private final By status = By.xpath("//div/a[text()='Status']");
	private final By message = By.xpath("//div/a[text()='Message']");
	private final By channels = By.xpath("//div/a[text()='Channels']");
	private final By lastExecution = By.xpath("//div/a[text()='Last execution']");
	private final By qsColumnSelectorTab = By
			.xpath("//div[@id='detail_quality_status']//div[@class='v-table-column-selector']");
	private final By inputField = By.xpath("//input[@type='text']");
	private final By filterSelectButton = By.xpath("//div[@id='dataquality_channel_combobox']//div");
	private final By allChannels = By.xpath("//span[text()='All channels']");
	private final By catalogRules = By.xpath("//span[text()='Catalog Rules']");
	private final By dentalRules = By.xpath("//span[text()='Dental Rules']");
	private final By globalRules = By.xpath("//span[text()='Global Rules']");
	private final By localAttrMandatoryCheck = By.xpath("//span[text()='Local Attribute Mandatory Check']");
	private final By medicalRules = By.xpath("//span[text()='Medical Rules']");
	private final By specialMarketRules = By.xpath("//span[text()='Special Market Rules']");
	private final By zahnRules = By.xpath("//span[text()='Zahn Rules']");
	private final By ruleColumSelectorTab = By.xpath("//span/div[text()='Rule']");
	private final By statusColumSelectorTab = By.xpath("//span/div[text()='Status']");
	private final By channelsColumSelectorTab = By.xpath("//span/div[text()='Channels']");
	private final By messageColumSelectorTab = By.xpath("//span/div[text()='Message]");
	private final By ignoreColumSelectorTab = By.xpath("//span/div[text()='Ignore']");
	private final By ignoredOnColumSelectorTab = By.xpath("//span/div[text()='Ignored on']");
	private final By ignoredByColumSelectorTab = By.xpath("//span/div[text()='Ignored by']");
	private final By lastExecColumSelectorTab = By.xpath("//span/div[text()='Last execution']");
	private final By DescriptionColumSelectorTab = By.xpath("//span/div[text()='Description']");
	private final By scoreColumSelectorTab = By.xpath("//span/div[text()='Score']");
	private final By ignore = By.xpath("//div/a[text()='Ignore']");
	private final By ignoredOn = By.xpath("//div/a[text()='Ignored on']");
	private final By ignoreBy = By.xpath("//div/a[text()='Ignore by']");
	private final By description = By.xpath("//div/a[text()='Description']");
	private final By score = By.xpath("//div/a[text()='Score']");
	private final By MaximizeButton = By.xpath(
			"//div[@class='v-button v-widget hpmw-detail-view-focus-btn v-button-hpmw-detail-view-focus-btn hpmw-expand-all-button v-button-hpmw-expand-all-button']//span[@class='v-button-wrap']");
	private final By MinimizeButton = By.xpath("//div//span[@class='v-button-wrap']");
	private final By buttonFocusMode = By.xpath("(//span[@class='v-button-wrap'])[2]");
	private final By tabLastExecution = By.xpath(
			"//div[@class='v-table-caption-container v-table-caption-container-align-left']//a[text()='Last execution']");
	private final String ruleTypeXpath = "//span[text()='${variable}']";
	private final String todaysDate = DateandTimeUtils.getTodaysDateForQualityStatus();
	private final String ruleListSizeXpath = ("//table[@class='v-table-table']/tbody/tr/td[6]/div[contains(text(),"
			+ "\'" + todaysDate + "\'"
			+ ")]/preceding::div[contains(text(),'${variable}')]/preceding::div[@class='v-table-cell-wrapper'][1]");
	private final String ruleListXpath = (" //table[@class='v-table-table']/tbody/tr/td[6]/preceding::div[contains(text(),'${variable}')]/preceding::div[@class='v-table-cell-wrapper'][1]");
    private final By primaryAssetRuleMessage = By.xpath("//div[contains(text(),'Primary Asset Check')]/ancestor::td/following-sibling::td//div[contains(text(),'The Item does not have a Default primary asset.  Please select a Default primary asset.')]");
	

	// To Select Catalog Rules From the Channel DropDown
	public QualityStatusPage selectRuleTypeFromDropDown(String ruleType) {
		click(filterSelectButton, WaitLogic.CLICKABLE, "Channel DropDown");
		WaitForMiliSec(1000);
		click(getElementByReplaceText(ruleTypeXpath, ruleType), WaitLogic.CLICKABLE, "RuleType");
		WaitForMiliSec(2000);
		return this;
	}

	// To Sort the last Execution Tab to latest Date

	public QualityStatusPage sortRulestByLastExecution() {
		WaitForMiliSec(3000);
		click(tabLastExecution, WaitLogic.CLICKABLE, "Last Execution");
		WaitForMiliSec(3000);
		click(tabLastExecution, WaitLogic.CLICKABLE, "Last Execution");
		WaitForMiliSec(3000);
		return this;
	}

	public QualityStatusPage sortRulestByStatus() {
//		WaitForMiliSec(3000);
//		click(status, WaitLogic.CLICKABLE, "status");
		WaitForMiliSec(3000);
		click(status, WaitLogic.CLICKABLE, "status");
		WaitForMiliSec(3000);
		return this;
	}

	// To get all the rule Names run based on CurrentDate and ruleStatus

	public List<String> getRuleNamesByRuleStatus(String ruleStatus) {
		List<String> al_ActualRuleNames = new ArrayList<>();
		int counter = 0;
		do {
			WaitForMiliSec(30000);
			click(RefreshButtoninQualityStatus, WaitLogic.CLICKABLE, "Refresh button");
			try {
				al_ActualRuleNames.addAll(getRuleResultForCurrentDate(ruleStatus));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
		} while (counter <= 10 &&  al_ActualRuleNames.isEmpty());
		return al_ActualRuleNames;
	}

	private Set<String> getRuleResultForCurrentDate(String ruleStatus) {

		WaitForMiliSec(5000);
		System.out.println(getElementByReplaceText(ruleListSizeXpath, ruleStatus));
		WaitForMiliSec(5000);
		Set<String> al_ActualRuleNames = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			List<WebElement> listOfRulesWebElements = DriverManager.getDriver()
					.findElements(getElementByReplaceText(ruleListSizeXpath, ruleStatus));

			for (int j = 0; j < listOfRulesWebElements.size(); j++) {
				String ruleName = listOfRulesWebElements.get(j).getText();
				al_ActualRuleNames.add(ruleName);

				if (j == listOfRulesWebElements.size() - 1) {
					System.out.println("Scroll Start");
					WebElement element = listOfRulesWebElements.get(j);
					((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
							element);
					WaitForMiliSec(5000);
					System.out.println("Scroll Done");
				}
			}

		}
		System.out.println(al_ActualRuleNames.size());
		System.out.println(al_ActualRuleNames);
		 return al_ActualRuleNames;

	}
	
	public List<String> getRuleNamesByRuleStatusInQualityStatusForItemCreation(String ruleStatus) {
		List<String> al_ActualRuleNames = new ArrayList<>();
		int counter = 0;
		do {
			WaitForMiliSec(30000);
			try {
				al_ActualRuleNames.addAll(getRuleResultInQualityStatus(ruleStatus));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
		} while (counter <= 20 &&  al_ActualRuleNames.isEmpty());
		return al_ActualRuleNames;
	}
	
	private Set<String> getRuleResultInQualityStatus(String ruleStatus) {

		WaitForMiliSec(5000);
		System.out.println(getElementByReplaceText(ruleListXpath, ruleStatus));
		WaitForMiliSec(5000);
		Set<String> al_ActualRuleNames = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			List<WebElement> listOfRulesWebElements = DriverManager.getDriver()
					.findElements(getElementByReplaceText(ruleListXpath, ruleStatus));

			for (int j = 0; j < listOfRulesWebElements.size(); j++) {
				String ruleName = listOfRulesWebElements.get(j).getText();
				al_ActualRuleNames.add(ruleName);

				if (j == listOfRulesWebElements.size() - 1) {
					System.out.println("Scroll Start");
					WebElement element = listOfRulesWebElements.get(j);
					((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
							element);
					WaitForMiliSec(5000);
					System.out.println("Scroll Done");
				}
			}

		}
		System.out.println(al_ActualRuleNames.size());
		System.out.println(al_ActualRuleNames);
		 return al_ActualRuleNames;

	}

	/*
	 * public List<String> getRuleNamesByRuleStatus1(String ruleStatus) {
	 * WaitForMiliSec(12000); List<WebElement> listOfRulesWebElements =
	 * DriverManager.getDriver()
	 * .findElements(getElementByReplaceText(ruleListSizeXpath, ruleStatus));
	 * List<String> al_ActualRuleNames = new ArrayList<>(); WaitForMiliSec(5000);
	 * for (WebElement listOfRulesWebElement : listOfRulesWebElements) { String
	 * ruleName = listOfRulesWebElement.getText(); al_ActualRuleNames.add(ruleName);
	 * } System.out.println(al_ActualRuleNames); return al_ActualRuleNames; }
	 * 
	 */

	// sorting the rule

	public void sortRule() {
		click(rule, WaitLogic.CLICKABLE, "rule is sorted");
		WaitForMiliSec(2000);
	}

	public QualityStatusPage maximizeQualityStatusTab() {
		WaitForMiliSec(2000);
		click(MaximizeButton, WaitLogic.CLICKABLE, "Maximize button clicked");
		WaitForMiliSec(2000);
		return this;
	}

	public QualityStatusPage minimizeQualityStatusTab() {
		WaitForMiliSec(2000);
		click(MinimizeButton, WaitLogic.CLICKABLE, "Minimize button clicked");
		WaitForMiliSec(2000);
		return this;
	}
	
	public boolean getRuleMessage() {
		boolean message = isVisible(primaryAssetRuleMessage,"Primary Asset Failed Message");
		return message;
	}

}
