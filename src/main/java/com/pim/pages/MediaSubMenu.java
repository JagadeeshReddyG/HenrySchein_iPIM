package com.pim.pages;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.pim.driver.DriverManager;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import com.pim.factories.ExplicitWaitFactory;

public class MediaSubMenu extends BasePage{
	ProductDetailSearchPage pdp = new ProductDetailSearchPage();


	private final By dropdown = By.xpath("//div[@class='v-filterselect-button']");
	private final By multiMediaDocument = By.xpath("//span[contains(text(),'Multimedia document')]");
	private final String libraryNodes = "//div[contains(text(),'${variable}')]//span[@class='v-treetable-treespacer v-treetable-node-closed']";
	private final String subCategories = "//div[contains(text(),'${variable}')]";
	private final String dropdownIconOfGivenText = "//div[@class='v-table-cell-wrapper' and contains(text(),'${variable}')]/span";
	private final String givenTextOptions = "//div[@class='v-table-cell-wrapper' and contains(text(),'${variable}')]/span";
	private final By filterIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-search v-menubar-menuitem-unchecked']");
	private final By closeFilterIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-search v-menubar-menuitem-checked']");
	private final By filterByTextfield = By.xpath("//input[@id='document_search_field']");
	private final By filterByFilterButton = By.xpath("//span[contains(text(),'Filter')]/..");
	private final String givenImageDivision = "	//div[@class='v-label v-widget hpmw-documentCaption v-label-hpmw-documentCaption hpmw-documentCaptionBottom v-label-hpmw-documentCaptionBottom v-has-width' and contains(text(),'${variable}')]";
	private final String downloadIconOnGivenImage = "//div[@class='v-link v-widget hpmw-media-action-download v-link-hpmw-media-action-download hpmw-media-action-count2 v-link-hpmw-media-action-count2']/a";
	private final By firstImageDiv = By.xpath("//div[@class='v-csslayout v-layout v-widget hpmw-documentBox v-csslayout-hpmw-documentBox']");
	private final By firstImageInUsageList = By.xpath("//tbody//tr[contains(@class,'v-table-row')]//td//div[text()='1']");
	private final By imageDeleteIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-remove']");
	private final By deleteConfirmationIcon = By.xpath("//span[contains(text(),'Yes')]");
	private final By noOnDeleteConfirmationIcon = By.xpath("//span[contains(text(),'No')]");
	private final By closeIcon = By.xpath("//div[@class='popupContent']/descendant::div[@class='v-window-closebox']");
	private final By deleteErrorIcon = By.xpath("//span[contains(text(),'OK')]");
	private final By allAssetsNumberInUsageList = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ArticleType_SupplierAltAid']/div");
	private final String assetDivInUsageList = "//td[@class='v-table-cell-content v-table-cell-content-ArticleType_SupplierAltAid']/div[contains(text(),'${variable}')]";
	private final By usageList = By.xpath("//div[text()='Usage list']");
	private final By getItemAssetName = By.xpath("//div[contains(text(), '.jpg')]");

	//Media Tab
	private final By fileNameTextField = By.xpath("(//span[contains(text(),'File-name')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");
    private final By shotTypeTextField = By.xpath("(//span[contains(text(),'Shot-Type')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");
    private final By serializationTextField = By.xpath("(//span[contains(text(),'Serialization')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");
	private final By imageInfoIcon = By.xpath("//button[contains(@class,'mediaOpenInfo')]");
	private final By documentIdentificationNoInPopup = By.xpath("//*[contains(text(),'Document identifier')]//ancestor::tr//td[3]");
	private final By resolutonValueInPopup = By.xpath("//*[contains(text(),'Resolution')]//ancestor::tr//td[3]");



	public String getFileNameFromMediaTab() {
		String fileNameValue = getStringValues(fileNameTextField, WaitLogic.VISIBLE, "get File Name Value");
		return fileNameValue.trim();
	}

	public String getSerializationValueFromMediaTab() {
		String serializationValue = getStringValues(serializationTextField, WaitLogic.VISIBLE, "get Serialization Value");
		return serializationValue.trim();
	}

	public String getShotTypeValueFromMediaTab() {
		String shotTypeValue = getStringValues(shotTypeTextField, WaitLogic.VISIBLE, "get Serialization Value");
		return shotTypeValue.trim();
	}

	public void clickImageInfoIconFromMediaTab() {
		click(imageInfoIcon, WaitLogic.CLICKABLE, "Info Icon");
	}

	public MediaSubMenu selectMultiMediaDocument() {
		pdp.waitUntilBufferingIconDisappear();
		click(dropdown, WaitLogic.CLICKABLE, "Media dropdown");
		click(multiMediaDocument, WaitLogic.CLICKABLE, "multi media document");
		return this;
	}

	public MediaSubMenu clickMediaLibraryNodes(String nodes) {
		click(getElementByReplaceText(libraryNodes, nodes), WaitLogic.CLICKABLE, nodes + " ");
		return this;
	}

	public MediaSubMenu clickSubCategory(String subcategory) {
		click(getElementByReplaceText(subCategories, subcategory), WaitLogic.CLICKABLE, subcategory + " ");
		return this;
	}

	public MediaSubMenu expandDropdownIconOfGivenText(String dropdownText) {
		click(getElementByReplaceText(dropdownIconOfGivenText, dropdownText), WaitLogic.CLICKABLE, dropdownText);
		return this;
	}

	public MediaSubMenu expandDropdownAfterCheckingFileType(String fileNameWithType) {
		if(fileNameWithType.contains(".pdf")) {
			click(getElementByReplaceText(dropdownIconOfGivenText, "Materials"), WaitLogic.CLICKABLE, "Materials");
		}
		else {
			click(getElementByReplaceText(dropdownIconOfGivenText, "Images"), WaitLogic.CLICKABLE, "Images");
		}
		return this;
	}

	public MediaSubMenu clickOnGivenTextOptions(String text) {
		click(getElementByReplaceText(givenTextOptions, text), WaitLogic.CLICKABLE, text);
		return this;
	}

	public MediaSubMenu clickOnFilterIcon() {
		pdp.waitUntilBufferingIconDisappear();
		moveToElementAndClick(filterIcon, "filterIcon");
		pdp.waitUntilBufferingIconDisappear();
		return this;
	}

	public MediaSubMenu closeFilterIcon() {
		BasePage.WaitForMiliSec(1000);
		moveToElementAndClick(closeFilterIcon, "filterIcon");
		BasePage.WaitForMiliSec(1000);
		return this;
	}

	public MediaSubMenu passTextOnFilterByTextfield(String text) {
		click(filterByTextfield, WaitLogic.CLICKABLE, "filterByTextfield");
		sendKeys(filterByTextfield, text, WaitLogic.CLICKABLE, "filterIcon");
		return this;
	}

	public MediaSubMenu clickOnFilterByFilterButton() {
		click(filterByFilterButton, WaitLogic.CLICKABLE, "filterByFilterButton");
		pdp.waitUntilBufferingIconDisappear();
		return this;
	}

	public MediaSubMenu clickFirstItem() {
		BasePage.WaitForMiliSec(10000);
		click(firstImageDiv, WaitLogic.CLICKABLE, "First Image");
		BasePage.WaitForMiliSec(1000);
		return this;
	}

	public MediaSubMenu clickOnDownloadButtonOnGivenImage(String imageName) {
		moveToElementAndClick(getElementByReplaceText(givenImageDivision, imageName), imageName);
		moveToElementAndClick(getElementByReplaceText(downloadIconOnGivenImage, imageName), "downloadIconOnGivenImage");
		BasePage.WaitForMiliSec(5000);

		return this;
	}

	public MediaSubMenu clickFirstItemInUsageList() {
//		while(isVisible(firstImageInUsageList, "image status is Ready", 2)) {
		click(firstImageInUsageList, WaitLogic.CLICKABLE, "firstImageInUsageList");
		BasePage.WaitForMiliSec(1000);
//		}
		return this;
	}

	public MediaSubMenu clickOnGivenAssetInUsageList(String assetNum) {
		click(getElementByReplaceText(assetDivInUsageList, assetNum), WaitLogic.CLICKABLE, "firstImageInUsageList");
		BasePage.WaitForMiliSec(1000);
		return this;
	}


	public MediaSubMenu deleteAllItemsInUsageList() {
		click(firstImageInUsageList, WaitLogic.CLICKABLE, "firstImageInUsageList");
		BasePage.WaitForMiliSec(1000);
		return this;
	}

	public MediaSubMenu clickDeleteIcon() {
		click(imageDeleteIcon, WaitLogic.CLICKABLE, "imageDeleteIcon");
		BasePage.WaitForMiliSec(1000);
		return this;
	}

	public MediaSubMenu clickDeleteConfirmationIcon() {
		click(deleteConfirmationIcon, WaitLogic.CLICKABLE, "deleteConfirmationIcon");
		pdp.waitUntilBufferingIconDisappear();
		BasePage.WaitForMiliSec(2000);
		return this;
	}

	public MediaSubMenu clickOnUsageList(){
		click(usageList, WaitLogic.CLICKABLE, "usageList");
		BasePage.WaitForMiliSec(1000);
		return this;
	}

	public MediaSubMenu clickNoOnDeleteConfirmationIcon() {
		click(noOnDeleteConfirmationIcon, WaitLogic.CLICKABLE, "noOnDeleteConfirmationIcon");
		pdp.waitUntilBufferingIconDisappear();
		BasePage.WaitForMiliSec(2000);
		return this;
	}

	public MediaSubMenu clickPopCloseIcon() {
		click(closeIcon, WaitLogic.CLICKABLE, "closeIcon");
		pdp.waitUntilBufferingIconDisappear();
		BasePage.WaitForMiliSec(2000);
		return this;
	}
	public MediaSubMenu clickOkOnErrorIcon() {
		click(deleteErrorIcon, WaitLogic.CLICKABLE, "deleteErrorIcon");
		pdp.waitUntilBufferingIconDisappear();
		BasePage.WaitForMiliSec(2000);
		return this;
	}



//	public ArrayList<String> getAssetNamesInUsageList() {
//		ArrayList<String> assetList = new ArrayList<String>();
//		List<WebElement> allAssetList = DriverManager.getDriver().findElements(allAssetsNumberInUsageList);
//
//		for(int i=0; i<allAssetList.size(); i++) {
//			String assetNum = allAssetList.get(i).getText();
//			assetList.add(assetNum);
//		}
//		return assetList;
//	}

	public ArrayList<String> getAssetNamesInUsageList() {
	    ArrayList<String> assetList = new ArrayList<>();

	    try {
	        // Wait until at least one element is present and visible
	    	ExplicitWaitFactory.performExplicitWait(WaitLogic.VISIBLE, allAssetsNumberInUsageList);

	        List<WebElement> allAssetElements = DriverManager.getDriver().findElements(allAssetsNumberInUsageList);

	        for (WebElement element : allAssetElements) {
	            String assetNum = element.getText().trim();
	            if (!assetNum.isEmpty()) {
	                assetList.add(assetNum);
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("Unable to retrieve asset names in usage list: " + e.getMessage());
	    }

	    return assetList;
	}



	public String getAssetName() {
		WaitForMiliSec(3000);
		String assetName = getStringValues(getItemAssetName, WaitLogic.VISIBLE, "get assetName");
		return assetName;
	}

	public String ExtractAssetItemNumber() {
        String assetName = getAssetName();

        if (assetName.length() >= 7 && assetName.matches("^\\d{7}_.*")) {
            String itemNumber = assetName.substring(0, 7);
            return itemNumber;
        } else {
            throw new IllegalArgumentException("Invalid asset name format: " + assetName);
        }
    }

	// MediaSubMenu.java

	public MediaSubMenu deleteAsset(String asset, String docId) {
	    try {
	        clickOnGivenAssetInUsageList(asset);
	        clickDeleteIcon();
	        clickDeleteConfirmationIcon();
	        BasePage.WaitForMiliSec(30000);
	    } catch (Exception e) {
	        log(LogType.ERROR, "Error while deleting asset [" + asset + "] for docId: " + docId);
	        handleErrorPopup(docId);
	    }
	    return this;
	}

	public MediaSubMenu handleErrorPopup(String docId) {
	    try {
	        clickPopCloseIcon();
	        //clickOkOnErrorIcon();
	        clickNoOnDeleteConfirmationIcon();
	    } catch (Exception e) {
	        log(LogType.ERROR, "Error popup handling failed for docId: " + docId);
	    }
	    return this;
	}

	public MediaSubMenu minimizeIfVisible(String docId) {
	    try {
	    	pdp.minimizeProductDetailTab();
	    } catch (Exception e) {
	        System.out.println("Could not minimize product detail tab for doc ID: " + docId);
	    }
	    return this;
	}


}
