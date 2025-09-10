package com.pim.pages;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.reports.ExtentLogger;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;

public class MainMenu extends BasePage {
	ProductDetailSearchPage pdp = new ProductDetailSearchPage();

	private final By Structures = By.xpath("//span[normalize-space()='Structures']/..");
	private final By Catalogs = By.xpath("//span[normalize-space()='Catalogs']/..");
	private final By Tasks = By.xpath("//span[normalize-space()='Tasks']/..");
	private final By Queries = By.xpath("//span[normalize-space()='Queries']/..");
	private final By Search = By.xpath("//span[normalize-space()='Search']/..");
	private final By MenuRefresh = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-refresh']");
	private final By Media = By.xpath("(//span[contains(text(),'Media')])[1]/..");

	public StructureSubMenu clickStructuresMenu() {
		click(Structures, WaitLogic.CLICKABLE, "StructuresIcon");
		return new StructureSubMenu();
	}

	public CatalogSubMenu clickCatalogsMenu() {
		WaitForMiliSec(5000);
		click(Catalogs, WaitLogic.CLICKABLE, "CatalogsIcon");
		return new CatalogSubMenu();
	}

	public TasksSubMenu clickTasksMenu() {
		click(Tasks, WaitLogic.CLICKABLE, "TasksIcon");
		return new TasksSubMenu();
	}

	public QueriesSubMenu clickQueriesMenu() {
		//WaitForMiliSec(5000);
		click(Queries, WaitLogic.CLICKABLE, "QueriesIcon");
		return new QueriesSubMenu();
	}

	public SearchSubMenu clickSearchMenu() {
		WaitForMiliSec(15000);
		click(Search, WaitLogic.CLICKABLE, "SearchIcon");
		WaitForMiliSec(5000);
		return new SearchSubMenu();
	}

	public MainMenu clickRefreshMenuIcon() {
		click(MenuRefresh, WaitLogic.CLICKABLE, "Refresh menu");
		WaitForMiliSec(5000);
		return this;
	}
	
	public MediaSubMenu clickMediaMenu() {
		pdp.waitUntilBufferingIconDisappear();
		click(Media, WaitLogic.CLICKABLE, "Media Sub Menu");
		return new MediaSubMenu(); 
	}

}
