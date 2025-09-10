package com.pim.tests.webDescriptionTests;

import java.util.Map;

import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.TextFileUtils;

public class WebFullDisplayDescriptionTest extends BaseTest{

	private WebFullDisplayDescriptionTest() {

	}

	WebDescription webdescription = new WebDescription();

	//MDLW_072
	@PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Web_Description)
	@Test(description = "MDLW_072 | Verify whether description updated for web description gets updated in respective databases for Pre-requisit", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","WEB_DESCRIPTION"})
	public void verify_full_description_in_DB(Map<String, String> map) {
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		String fulldescription = webdescription.getFullDisplayDescription();
		TextFileUtils.writeTextFile(fulldescription);

	}


}
