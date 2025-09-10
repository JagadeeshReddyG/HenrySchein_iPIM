package com.pim.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;

public class AllCatalogsPage extends BasePage {

	// All Catalogs tab Locator
	public By tabAllCatalogs = By.xpath("//div[text()='All Catalogs']");
	public By tabAllCanadianCatalogs = By.xpath("//div[text()='All Canadian Catalogs']");

	// All Catalogs Attributes Locators for fetching the values

	public By fieldMedicalCatalogList = By.xpath("//span[contains(text(), 'Medical Catalog List:')]/../../..//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");

	public By fieldSpecialMarketsCatalogList = By.xpath("//span[contains(text(), 'Special Markets Catalog List:')]/../../..//div[@class='v-horizontallayout v-layout v-horizontal v-widget hpmw-buttonGroup v-horizontallayout-hpmw-buttonGroup']");

	public By fieldZahnCatalogList = By.xpath("//span[contains(text(), 'Zahn Catalog List:')]/../../..//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");
	public By fieldDentalCatalogList = By.xpath("//span[contains(text(), 'Dental Catalog List:')]/../../..//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");
	
	public By fieldMedicalCatalogListForCA = By.xpath("(//td//div[@class='v-customcomponent v-widget v-readonly hpmw-detailFormLabel v-customcomponent-hpmw-detailFormLabel v-has-width'])[2]//div[@class='v-slot v-slot-hpmw-valueLabel']//div");

	public By fieldDentalCatalogListUI = By.xpath("//span[text()='Dental Catalog List:']/ancestor::tr//*[contains(@class,'v-label')]");
	public By fieldMedicalCatalogListUI = By.xpath("//span[text()='Medical Catalog List:']/ancestor::tr//*[contains(@class,'v-label')]");
	public By fieldZahnCatalogListUI = By.xpath("//span[text()='Zahn Catalog List:']/ancestor::tr//*[contains(@class,'v-label')]");
	public By fieldSpecialMarketsCatalogListUI = By.xpath("//span[text()='Special Markets Catalog List:']/ancestor::tr//*[contains(@class,'v-label')]");

	// catalog field values dropdown
	public By FieldDropDown = By.xpath("//table//div[@class='v-filterselect-button']");


	// Adding all the SpecialMarketsCatalogrules to the list
	public List<String> getAllSpecialMarketsCatalogList() {
		WaitForMiliSec(5000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(fieldSpecialMarketsCatalogList);

		List<String> listSpecialmarketsCatalogRules = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String ruleName = catalogRules.getText().trim();
			listSpecialmarketsCatalogRules.add(ruleName);
		}
		return listSpecialmarketsCatalogRules;

	}

	// Adding all the MedicalCatalogrules to the list
	public List<String> getAllMedicalCatalogList() {
        WaitForMiliSec(5000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(fieldMedicalCatalogList);
		List<String> listMedicalCatalogRules = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String ruleName = catalogRules.getText().trim();
			listMedicalCatalogRules.add(ruleName);
		}

		return listMedicalCatalogRules;

	}
	
	// Adding all the MedicalCatalogrules to the list for CA catalog
		public List<String> getAllMedicalCatalogListForCA() {

			List<WebElement> listSize = DriverManager.getDriver().findElements(fieldMedicalCatalogListForCA);
			List<String> listMedicalCatalogRules = new ArrayList<String>();
			for (WebElement catalogRules : listSize) {
				String ruleName = catalogRules.getText().trim();
				listMedicalCatalogRules.add(ruleName);
			}

			return listMedicalCatalogRules;

		}
	
	// Adding all the ZahnCatalogrules to the list

	public List<String> getAllZahnCatalogList() {

		List<WebElement> listSize = DriverManager.getDriver().findElements(fieldZahnCatalogList);
		List<String> listZahnCatalogRules = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String ruleName = catalogRules.getText().trim();
			listZahnCatalogRules.add(ruleName);
		}
		return listZahnCatalogRules;
	}

	public boolean isCatalogFieldVisible(String fieldname) {
		By by = null;
		try {
			by = (By) AllCatalogsPage.class.getField(fieldname).get(AllCatalogsPage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname);
	}

	public boolean isCatalogFieldEditable(String fieldname, String visibilityElement) {

		By by = null;

		try {
			by = (By) AllCatalogsPage.class.getField(fieldname).get(AllCatalogsPage.this);
			click(by, WaitLogic.CLICKABLE, fieldname);
			by = (By) AllCatalogsPage.class.getField(visibilityElement).get(AllCatalogsPage.this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (isVisible(by, fieldname + visibilityElement, 5));
	}

	// Adding all the DentalCatalogrules to the list
	public List<String> getAllDentalCatalogList() {
		BasePage.WaitForMiliSec(3000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(fieldDentalCatalogList);
		List<String> listDentalCatalogRules = new ArrayList<String>();
		for (WebElement catalogRules : listSize) {
			String ruleName = catalogRules.getText().trim();
			listDentalCatalogRules.add(ruleName);
		}
		return listDentalCatalogRules;
	}

}