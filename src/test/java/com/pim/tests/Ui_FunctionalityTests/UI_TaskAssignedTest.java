package com.pim.tests.Ui_FunctionalityTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TasksSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.pim.reports.FrameworkLogger.log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UI_TaskAssignedTest extends BaseTest {
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	StructureSubMenu structuresubmenu = new StructureSubMenu();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ClassificationsPage classificationpage = new ClassificationsPage();
	TasksSubMenu tasksSubMenu = new TasksSubMenu();

	private UI_TaskAssignedTest() {
	}

	// UI_061-Verify that user is able to view the list of tasks assigned to them
	@PimFrameworkAnnotation(module = Modules.UIFUCTIONAL_TASK, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UI_Functional_Task)
	@Test(description = "UI_061 | Verify that user is able to view the list of tasks assigned to them", groups = { "US",
			"pim","UIFUCTIONAL_TASK" }, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatUserIsAbleToViewThelistofTasksAssignedToThem(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickTasksMenu()./*
												 * enterSearchTextToFilter(map.get("SearchText")).clickFilterButton() .
												 */SelectFirstUserGroup();
		log(LogType.INFO, "Verifying clicking on Filter button and selecting the first user");
		Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();

	}
}
