package com.pim.pages;

import org.openqa.selenium.By;

import com.pim.enums.WaitLogic;


public class TasksSubMenu extends BasePage {
	
	private final By search = By.xpath("//input[contains(@class,'taskContext-filter')]");
	private final By filterButton= By.xpath("//div//span[contains(@class,'v-menubar-menuitem v-menubar-menuitem-hpmw-filter')]");
	private final By SelectFirstGroup= By.xpath("//td[contains(@class,'v-table-cell-content v-table-cell-content-hpmw-task-tree-cell-usergroup')]//div[contains(@class,'v-label v-widget v-has-width')]");
	public By TasksList= By.xpath("//*[@id='task_table']//tr[contains(@class,'v-table-row')]");
	private final By taskName = By.xpath("(//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width'])[1]");
	private final By AcceptTask = By.xpath("//span[text()='Accept']");
	private final String SepcificUserCodeXpath = "//td[@class='v-table-cell-content v-table-cell-content-hpmw-task-tree-cell-usergroup']//*[contains(text(),'${variable}')]";
    private final String notAcceptedTasks = "//tr//td//div[contains(text(),'${variable}')]";
    private final String assetTask ="//div[contains(text(),'${variable}')]";
	private final By AcceptTaskContent = By.xpath("//span[text()='Accept task content']");
    
	public TasksSubMenu enterSearchTextToFilter(String searchText) {

		click(search, WaitLogic.PRESENCE, "search");
		sendKeys(search, searchText, WaitLogic.PRESENCE, "searchText");
		WaitForMiliSec(3000);
		return this;
	}

	public TasksSubMenu clickFilterButton() {
		click(filterButton, WaitLogic.PRESENCE, "filterButton");
		WaitForMiliSec(3000);
		return this;
	}
	public TasksSubMenu SelectFirstUserGroup() 
	{
		WaitForMiliSec(20000);
		click(SelectFirstGroup, WaitLogic.CLICKABLE, "SelectFirstGroup");
		WaitForMiliSec(5000);
		return this;
	}
	public TasksSubMenu SelectSpecificUserGroup(String specificUser) 
	{
		WaitForMiliSec(20000);
		click(getElementByReplaceText(SepcificUserCodeXpath, specificUser), WaitLogic.CLICKABLE,"specificUser");
		WaitForMiliSec(5000);
		return this;
	}
	public boolean isTaskPresent(String fieldName) {
		By by = null;
		try {
			by = (By) TasksSubMenu.class.getField(fieldName).get(TasksSubMenu.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldName);
	}

	//To get Task Name
	public String getTaskName(){
		String taskNameDescription = getStringValues(taskName,WaitLogic.CLICKABLE, "getTaskName");
		return taskNameDescription;
	}

	//To accept task
	public TasksSubMenu AcceptTask()
	{
		WaitForMiliSec(20000);
		click(AcceptTask, WaitLogic.CLICKABLE, "clickOnAcceptTask");
		WaitForMiliSec(20000);
		return this;
	}
	
	/**
	 * Name of the method: clickOnNotAcceptedTask
	 * Description: method to click on task
	 * Author:Manisha
	 * Parameters: name of the task
	 */
	public TasksSubMenu clickOnNotAcceptedTask(String task) {
		click(getElementByReplaceText(notAcceptedTasks, task), WaitLogic.CLICKABLE, task);
		return this;
	}
	
	/**
	 * Name of the method: assetTaskrow
	 * Description: method to verify task row is visible 
	 * Author:Manisha
	 * Parameters: itemnumber
	 */
	public boolean assetTaskrow(String itemNumber) {
		return isVisible(getElementByReplaceText(assetTask, itemNumber), "asset task");
	}

	public TasksSubMenu clickOnAssetTaskRow(String itemNumber) {
		click(getElementByReplaceText(assetTask, itemNumber), WaitLogic.CLICKABLE, "asset task");
		return this;
	}
	
	//To accept task content
	public TasksSubMenu AcceptTaskContent()
	{
		WaitForMiliSec(20000);
		click(AcceptTaskContent, WaitLogic.CLICKABLE, "clickOnAcceptTaskContent");
		WaitForMiliSec(20000);
		return this;
	}
	
}
	
