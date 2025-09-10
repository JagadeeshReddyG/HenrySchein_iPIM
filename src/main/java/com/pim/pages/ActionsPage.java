package com.pim.pages;

import com.pim.enums.WaitLogic;

import org.openqa.selenium.By;

public class ActionsPage extends BasePage {

	String itemNumber;
	private final By actionsDropdown = By.xpath("//span[contains(text(),\"Actions\")]");
	private final By classifyItem_Actions = By.xpath("//span[text()='Classify item']");
	private final By radioMoveButton = By.xpath("//label[text()='Move']/..");
	private final By radioCopyButton = By.xpath("//label[text()='Copy']/..");
	private final By okButton = By.xpath("//span[text()='OK']/..");
	private final By cancelButton = By.xpath("//span[text()='Cancel']/..");
	private final By closeButton = By.xpath("//div[@class='v-window-closebox']");

	private final By classificationTabList = By.id("detail_classification_table");
	private final By logout = By.xpath("//span[contains(text(),\"Log out\")]");
	private final By exportOption = By.xpath("//span[text()='Export']");

	//For Export Csv
	private final By exportAsCSV = By.xpath("//span[text()='Export as CSV']");
	private final By downloadCSV = By.xpath("//a[contains(text(),'Kathir.Ranga')]");
	//For Export Directly
	private final By exportDirectly = By.xpath("//span[text()='Export directly']");
	private final  By exportDescription = By.xpath("//span[text()='Description Export']");
	private final By downloadExportDescription = By.xpath("//a[contains(text(),'Description Export.csv')]");
	private final By JDEdesOption = By.xpath
			("(//div[@class='v-slot v-slot-Article']//td//div[@class='v-table-caption-container v-table-caption-container-align-left'])[3]");
	
	//For Business rule
	private  String businessRuleXpath = "//span[contains(text(),'${variable}')]";
	private final By Export_completed_successfully = By.xpath("//div[@class='popupContent']/descendant::div[text()='Export completed successfully']");

	//For List of headers
	private final By headersList = By.xpath
			("//div[@class='v-slot v-slot-Article']//td//div[@class='v-table-caption-container v-table-caption-container-align-left']");
//tr[@class='v-table-row v-selected']
	private final By importOption = By.xpath("//span[contains(text(), 'Import')]");
	private final By chooseFileForFileToUpload = By.xpath("//input[@type='hidden']/../div/span[@class='v-button-wrap']");
//	private final By chooseFileForFileToUpload = By.xpath("//input[@class='gwt-FileUpload']");
	private final By IFUItemAssetMappingOption = By.xpath("//div[contains(text(), 'IFU Item Asset Mapping')]");




	public ActionsPage clickOnActionsDropdown(){
		WaitForMiliSec(4000);
		click(actionsDropdown, WaitLogic.CLICKABLE, "ActionsDropdown");
		WaitForMiliSec(2000);
		return this;
	} 
	public ActionsPage clickOnClassifyItem(){
		click(classifyItem_Actions, WaitLogic.CLICKABLE,"ClassifyItem");
		WaitForMiliSec(3000);
		return this;
	}

	public ActionsPage clickOnRadioMoveButton() {
		click(radioMoveButton, WaitLogic.CLICKABLE,"RadioMoveButton");
		return this;
	}

	public ActionsPage clickOnRadioCopyButton() {
		click(radioCopyButton, WaitLogic.CLICKABLE,"RadioCopyButton");
		WaitForMiliSec(8000);
		return this;
	}

	public ActionsPage clickOnOkButton(){
		click(okButton, WaitLogic.CLICKABLE,"OkButton");
		WaitForMiliSec(40000);
		return this;
	}
	public ActionsPage clickOnCancelButton()
	{
		click(cancelButton, WaitLogic.CLICKABLE,"CancelButton");
		return this;
	}
	
	public ActionsPage clickOnJDEDes()
	{
		click(JDEdesOption, WaitLogic.CLICKABLE,"JDEdesOption");
		return this;
	}

	//To select "Data" field and click "Export selected rows" option
	public ActionsPage clickOnExportAsCSVAndDownloadSheet(){
		click(exportOption, WaitLogic.CLICKABLE,"exportButton");
		WaitForMiliSec(2000);
		click(exportAsCSV, WaitLogic.CLICKABLE,"exportAsCSVOption");
		WaitForMiliSec(2000);
		click(exportOption, WaitLogic.CLICKABLE,"exportButtonOnPopUp");
		WaitForMiliSec(2000);
		click(downloadCSV, WaitLogic.CLICKABLE,"linkToDownloadCSV");
		WaitForMiliSec(2000);
		return this;
	}

	//To select "Data" field and click on any type of exports as "Export Directly selected rows"
	public ActionsPage clickOnExportDirectlyAnyOptionsAndDownloadSheet(){
		click(exportOption, WaitLogic.CLICKABLE,"exportButton");
		WaitForMiliSec(2000);
		click(exportDirectly, WaitLogic.CLICKABLE,"exportDirectlyButton");
		WaitForMiliSec(2000);
		click(exportDescription, WaitLogic.CLICKABLE,"exportDescriptionOption");
		WaitForMiliSec(2000);
		click(okButton, WaitLogic.CLICKABLE,"OkButton");
		WaitForMiliSec(4000);
		click(downloadExportDescription, WaitLogic.CLICKABLE,"downloadExportDescriptionExcel");
		WaitForMiliSec(2000);
		return this;
	}

	//To select Workflow rule from Actions
	public ActionsPage SelectBusinessRule(String Rule){
		WaitForMiliSec(2000);
		click(exportOption, WaitLogic.CLICKABLE,"exportButton");
		WaitForMiliSec(2000);
		click(exportDirectly, WaitLogic.CLICKABLE,"exportDirectlyButton");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(businessRuleXpath, Rule), WaitLogic.CLICKABLE, "catalogType");
		WaitForMiliSec(5000);
		click(okButton, WaitLogic.CLICKABLE,"OkButton");
		WaitForMiliSec(2000);
		isVisible(Export_completed_successfully, "Export_completed_successfully_Msg");
		WaitForMiliSec(2000);
		click(closeButton, WaitLogic.CLICKABLE,"CloseButton");
		WaitForMiliSec(2000);
		return this;
	}
	
	public ActionsPage clickOnOkButton1(){
		WaitForMiliSec(10000);
		if(isVisible(okButton,"")) {
		click(okButton, WaitLogic.CLICKABLE,"OkButton");
		}
		WaitForMiliSec(40000);
		return this;
	}
	
	public ActionsPage clickOnImportOption()
	{
		click(importOption, WaitLogic.CLICKABLE,"importOption");
		return this;
	}
	
	public ActionsPage clickOnChooseFileForFileToUpload()
	{
		WaitForMiliSec(5000);
		moveToElementAndClick(chooseFileForFileToUpload, "chooseFileForFileToUpload");

//		try {
//			Runtime.getRuntime()
//					.exec(Javautils.getFilePathToUploadSingleFile(FileUtils.getTempImagePath("Mapping_template.xlsx") + ""));
//		} catch (IOException e) {
//			log(LogType.INFO, e.getMessage());
//		}
//		log(LogType.INFO, "uploaded the image");
//		Robot r;
//		try {
//			r = new Robot();
//			r.keyPress(KeyEvent.VK_CONTROL);
//			r.keyPress(KeyEvent.VK_V);
//			r.keyRelease(KeyEvent.VK_V);
//			r.keyRelease(KeyEvent.VK_CONTROL);
//			r.keyPress(KeyEvent.VK_ENTER);
//			r.keyRelease(KeyEvent.VK_ENTER);
//
//		} catch (AWTException e) {
//			e.printStackTrace();
//		}
//		WaitForMiliSec(10000);
		return this;
	}
	
	public ActionsPage clickOnIFUItemAssetMappingOption() {
		WaitForMiliSec(2000);
		click(IFUItemAssetMappingOption, WaitLogic.CLICKABLE,"IFUItemAssetMappingOption");
		WaitForMiliSec(5000);
		return this;
	}
	
	
	public ActionsPage clickOnStartImport() {
		WaitForMiliSec(2000);
		click(IFUItemAssetMappingOption, WaitLogic.CLICKABLE,"IFUItemAssetMappingOption");
		WaitForMiliSec(5000);
		return this;
	}
}


