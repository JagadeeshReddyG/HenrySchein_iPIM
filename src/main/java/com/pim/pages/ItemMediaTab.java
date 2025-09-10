package com.pim.pages;

import com.pim.driver.DriverManager;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.pim.reports.FrameworkLogger.log;

public class ItemMediaTab extends BasePage{
	ProductDetailSearchPage productdetailpage = new ProductDetailSearchPage();

	private final String imagesRows = "//tbody//tr[contains(@class,'v-table-row')]//td//div[text()='${variable}']";
	public final By imagesRowsforMSDS = By.xpath("//tbody//tr[contains(@class,'v-table-row')]//td//div[contains(text(),'.jpg')]");
	private final By imageDeleteIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-delete']");
	private final By imageDeleteYesButton = By.xpath("//span[contains(text(),'Yes')]");
	private final String shotType = "//div[contains(text(),'${variable}')]/ancestor::td/preceding-sibling::td[@class='v-table-cell-content v-table-cell-content-ArticleMediaAssetMapType_MediaAssetType']//div";
	private final String RegionValueOfGivenImage = "//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td[@class='v-table-cell-content v-table-cell-content-ArticleMediaAssetMapType_TypeOfInformation']//div";
	private final String PrimaryIndicatorValueOfGivenImage = "//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td[@class='v-table-cell-content v-table-cell-content-ArticleMediaAssetMapType_Category']//div";
    private final By editIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-edit']");
    private final By primaryIndicatortextfield = By.xpath("//span[contains(text(),'Primary Indicator:')]/ancestor::tr/td[@class='v-formlayout-contentcell']//input");
	private final By popupCloseIcon = By.xpath("//div[@class='v-window-closebox']");
	private final String deliverTochannel = "//div[contains(text(),'${variable}')]/ancestor::div[@class='v-scrollable v-table-body-wrapper v-table-body']/preceding-sibling::div//td//a[contains(text(),'Deliver')]";
    private final String thumbNailImage = "//div[contains(text(),'${variable}')]/ancestor::td/preceding-sibling::td//div[@class='v-embedded v-widget hpmw-table-thumbnail-container v-embedded-hpmw-table-thumbnail-container v-embedded-image']//img";
	private final String primaryIndicatorvalue = "//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td[@class='v-table-cell-content v-table-cell-content-ArticleMediaAssetMapType_Category']//div";
	private final String URIvalue = "//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td[@class='v-table-cell-content v-table-cell-content-ArticleMediaAssetMapType_UniformResourceIdentifier']//div";

	private final String mediaProviderValue = "//div[text()='${variable}']/../..//td[@class='v-table-cell-content v-table-cell-content-ArticleDomainType_Res_Text100_01']//div";
	private final String categoryValue = "//div[text()='${variable}']/../..//td[@class='v-table-cell-content v-table-cell-content-ArticleDomainType_Std_Text100_07']//div";
	private final String AssetIDValue = "//div[text()='${variable}']/../..//td[@class='v-table-cell-content v-table-cell-content-ArticleDomainType_Std_LK_Text100_01']//div";
	private final By AllAssetFileNameValue = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ArticleDomainType_Std_Text100_09']//div");

    private final By msdsFileName = By.xpath("//div[contains(text(),'MSDS')]/ancestor::td[contains(@class,'MediaAssetType')]/following-sibling::td[contains(@class,'Type_Name')]//div");
    private final By AllImageName = By.xpath("//td[@class='v-table-cell-content v-table-cell-content-ArticleMediaAssetMapType_Name']/div[@class='v-table-cell-wrapper']");

	ProductDetailSearchPage pdp = new ProductDetailSearchPage();
	public ItemMediaTab deletePresentImages(String imageName) {
		By imagePresent = getElementByReplaceText(imagesRows, imageName);
		productdetailpage.waitUntilBufferingIconDisappear();
		while(isVisible(imagePresent, "image status is Ready", 5)) {
			click(imagePresent,WaitLogic.CLICKABLE, "click on image row");
			WaitForMiliSec(3000);
			click(imageDeleteIcon, WaitLogic.CLICKABLE, "click Image Delete icon");
			WaitForMiliSec(3000); 
			click(imageDeleteYesButton, WaitLogic.CLICKABLE,"click Image Delete Yes Button");
			productdetailpage.waitUntilBufferingIconDisappear();
			WaitForMiliSec(3000); 
		}
		return this;
	}
	
	public ItemMediaTab deletePresentImages1(String imageName) {
		By imagePresent = getElementByReplaceText(imagesRows, imageName);
		productdetailpage.waitUntilBufferingIconDisappear();
		if(isVisible(imagePresent, "image status is Ready", 5)) {
			click(imagePresent,WaitLogic.CLICKABLE, "click on image row");
			WaitForMiliSec(3000);
			click(imageDeleteIcon, WaitLogic.CLICKABLE, "click Image Delete icon");
			WaitForMiliSec(3000); 
			click(imageDeleteYesButton, WaitLogic.CLICKABLE,"click Image Delete Yes Button");
			WaitForMiliSec(5000); 
		}
		return this;
	}
	
	public boolean isImagePresent(String imageName) {
		WaitForMiliSec(9000);
		productdetailpage.waitUntilBufferingIconDisappear();
		By imagePresent = getElementByReplaceText(imagesRows, imageName);
		return isVisible(imagePresent, "image status is Ready", 5);
	}
	
	/**
	 * Name of the method: getShotType
	 * Description: method to get shot type
	 * Author:Manisha
	 * Parameters: ImageName 
	 */
	public String getShotType(String imageName) {
		String shottype = getTextValue(getElementByReplaceText(shotType, imageName),WaitLogic.VISIBLE, "shot type");
		return shottype;
	}
	
	/**
	 * Name of the method: clickItemAsset
	 * Description: method to click item asset
	 * Author:Manisha
	 * Parameters: Image Name
	 */
	public ItemMediaTab clickItemAsset(String imageName) {
		By imageAsset = getElementByReplaceText(imagesRows, imageName);
		click(imageAsset, WaitLogic.CLICKABLE, "Image Asset");
		return this;
	}
	
	/**
	 * Name of the method: clickEditIcon
	 * Description: method to click edit icon for asset
	 * Author:Manisha
	 * Parameters:  
	 */
	public ItemMediaTab clickEditIcon() {
		click(editIcon, WaitLogic.CLICKABLE, "edit icon");
		return this;
	}
	
	/**
	 * Name of the method: primaryIndicatortextfield
	 * Description: method to get Attribute of primary indicator text field
	 * Author:Manisha
	 * Parameters:  
	 */
	public String primaryIndicatortextfield() {
		String primaryindicator = getAttributeValues(primaryIndicatortextfield,WaitLogic.VISIBLE, "Primary Indicator text field", "class");
		return primaryindicator;
	}
	
	/**
	 * Name of the method: clickPopupCloseIcon
	 * Description: method to close popup
	 * Author:Manisha
	 * Parameters:  
	 */
	public ItemMediaTab clickPopupCloseIcon() {
		click(popupCloseIcon,WaitLogic.CLICKABLE,"close icon");
		return this;
	}
	
	/**
	 * Name of the method: isDeliverToPresent
	 * Description: method to verify deliver To present in PIM or not
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public boolean isDeliverToPresent(String imagename) {
		return isVisible(getElementByReplaceText(deliverTochannel, imagename), "Deliver To channel");
	}
	
	/**
	 * Name of the method: getthumbNailImage
	 * Description: method to get image attribute
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public String getthumbNailImage(String imageName) {
		return getAttributeValues(getElementByReplaceText(thumbNailImage, imageName),WaitLogic.VISIBLE, "Thubnail image","src");
	}
	
	/**
	 * Name of the method: getPrimaryIndicatorValue
	 * Description: method to get primary indicator value
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public String getPrimaryIndicatorValue(String imageName) {
		String primaryindicator = getTextValue(getElementByReplaceText(primaryIndicatorvalue, imageName),WaitLogic.VISIBLE, "shot type");
		return primaryindicator;
	}
	
	/**
	 * Name of the method: getMSDSFileName
	 * Description: method to get msds file name
	 * Author:Manisha
	 * Parameters: 
	 */
	public String getMSDSFileName() {
		return getTextValue(msdsFileName,WaitLogic.VISIBLE, "MSDS File Name");
	}
	
	/**
	 * Name of the method: isImageFileVisible
	 * Description: method to verify is image is visible
	 * Author:Manisha
	 * Parameters: fieldname
	 */
	public boolean isImageFileVisible(String fieldname) {
		By by = null;
		try {
			by = (By) ItemMediaTab.class.getField(fieldname).get(ItemMediaTab.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname);
	}
	
	public String getPrimaryIndicatorValueOfGivenImage(String imageName) {
		String text = getTextValue(getElementByReplaceText(PrimaryIndicatorValueOfGivenImage, imageName),WaitLogic.VISIBLE, "PrimaryIndicatorValueOfGivenImage");
		return text;
	}
	
	public String getRegionValueOfGivenImage(String imageName) {
		String text = getTextValue(getElementByReplaceText(RegionValueOfGivenImage, imageName),WaitLogic.VISIBLE, "RegionValueOfGivenImage");
		return text;
	}
	
	public List<String> getAllImageName() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AllImageName);
		List<String> actualProductAssetName = new ArrayList<>();
		for (WebElement webElement : element) {
			WaitForMiliSec(1000);
			actualProductAssetName.add(webElement.getText());
		}
		return actualProductAssetName;
	}
	
	public String getURIUnionResourceIdentifierValue(String imageName) {
		String primaryindicator = getTextValue(getElementByReplaceText(URIvalue, imageName),WaitLogic.VISIBLE, "URIUnionResourceIdentifierValue");
		return primaryindicator;
	}
	
	public String getProviderNameFromMediaProviderTab(String imageName) {
		String providerValue = getTextValue(getElementByReplaceText(mediaProviderValue, imageName),WaitLogic.VISIBLE, "mediaProviderValue");
		return providerValue;
	}
	
	public String getCategoryNameFromMediaProviderTab(String imageName) {
		String providerValue = getTextValue(getElementByReplaceText(categoryValue, imageName),WaitLogic.VISIBLE, "mediaProviderValue");
		return providerValue;
	}
	
	public List<String> getAllAssetFileNameFromMediaProviderTab() {
		WaitForMiliSec(5000);
		List<WebElement> element = DriverManager.getDriver().findElements(AllAssetFileNameValue);
		List<String> actualProductAssetName = new ArrayList<>();
		for (WebElement webElement : element) {
			WaitForMiliSec(1000);
			actualProductAssetName.add(webElement.getText());
		}
		return actualProductAssetName;
	}
	
	public String getAssetIDFromMediaProviderTab(String imageName) {
		String value = getTextValue(getElementByReplaceText(AssetIDValue, imageName),WaitLogic.VISIBLE, "AssetIDValue");
		return value;
	}
	
	public boolean isImagePresent2Times(String imageName) {
		productdetailpage.waitUntilBufferingIconDisappear();
		By imagePresent = getElementByReplaceText(imagesRows, imageName);
		List<WebElement> imageNames = DriverManager.getDriver().findElements(imagePresent);
		int sizeOfList = imageNames.size();
		log(LogType.INFO, sizeOfList+" times image name is present");
		boolean condition = sizeOfList>1;
		return condition;
	}
}
