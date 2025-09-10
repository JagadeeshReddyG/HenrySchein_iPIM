package com.pim.pages;
import com.aventstack.extentreports.ExtentTest;
import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;

import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.ArrayList;
import java.util.List;


public class GlobalAttributePage extends BasePage{

	private final By Global_Attribute_Tab               = By.xpath("(//div[@class='v-captiontext' and contains(text(),'Global Attribute')]");
	private final By Status                             = By.xpath("//*[contains(text(),'Status')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	public By HSI_Item_Number                  = By.xpath("(//*[contains(text(),'HSI Item Number')]/../../..//td[@class='v-formlayout-contentcell'])[1]//div[contains(@class,'v-csslayout v-layout v-widget')]//div");

	public  By HSI_Item_NumberDropDown  = By.xpath("//table//div[@class='v-filterselect-button']");
	private final By Variation_Group_Leader                 = By.xpath("//*[contains(text(),'Variation Group Leader')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	public  By JDE_Description                 = By.xpath("//span[contains(text(),'JDE Description')]/ancestor::td/following-sibling::td//div[contains(@class,'v-label')]");
	public  By JDE_Strength                       = By.xpath("//*[contains(text(),'JDE Strength')]/../../..//td[@class='v-formlayout-contentcell']");
	public  By WCS_Description                 = By.xpath("//*[contains(text(),'WCS Description')]/../../..//td[@class='v-formlayout-contentcell']");
	public  By WCS_Strength                       = By.xpath("//*[contains(text(),'WCS Strength')]/../../..//td[@class='v-formlayout-contentcell']");
	public  By JDE_Size                          = By.xpath("//*[contains(text(),'JDE Size')]/../../..//td[@class='v-formlayout-contentcell']");
	private final By Supplier_Code                    = By.xpath("//span[contains(text(),'Supplier C')]/ancestor::td/following-sibling::td//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");
	private final By Supplier_CodeDropDown =By.xpath("//span[normalize-space()='Supplier Code:\r\n"
			+ ":']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");
	private final By Supplier_Name							 = By.xpath("//*[contains(text(),'Supplier Name')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Manufacturer_Code						 = By.xpath("//span[contains(text(),'Manufacturer Code:')]/ancestor::td/following-sibling::td//div[contains(@class,'v-label')]");
	private final By Manufacturer_Name						 = By.xpath("//*[contains(text(),'Manufacturer Name')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Alternate_Part_Number					 = By.xpath("//*[contains(text(),'Alternate Part Number')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Manufacturer_Part_Number				 = By.xpath("//span[contains(text(),'Manufacturer Part Number')]//ancestor::tr/td[3]//div[2]");
	public final By Component_UOM							 = By.xpath("//*[contains(text(),'Component UOM')]/../../..//td[@class='v-formlayout-contentcell']");
	public final By Shipping_UOM							 = By.xpath("//*[contains(text(),'Shipping UOM')]/../../..//td[@class='v-formlayout-contentcell']");
	private final By Country_of_Origin						 = By.xpath("//*[contains(text(),'Country of Origin')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Stocking_Type_Code						 = By.xpath("//*[contains(text(),'Stocking Type Code')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Stocking_Type_Description				 = By.xpath("//*[contains(text(),'Stocking Type Description')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Line_Type_Code							 = By.xpath("//span[contains(text(),'Line Type Code')]/ancestor::td/following-sibling::td//div[contains(@class,'v-label')]");
	private final By Line_Type_Description					 = By.xpath("//*[contains(text(),'Line Type Description')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Language								 = By.xpath("//*[contains(text(),'Language')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	public final By JDE_French_Description					 = By.xpath("//*[contains(text(),'JDE French Description')]/../../..//td[@class='v-formlayout-contentcell']");
	public final By JDE_French_Description_textField		 = By.xpath("//div//input[@class='v-textfield v-widget hpmw-singleline-textfield v-textfield-hpmw-singleline-textfield v-textfield-focus']");
	private final By Label									 = By.xpath("//*[contains(text(),'Label')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Family_Set_Leader						 = By.xpath("//*[contains(text(),'Family Set Leader')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Classification							 = By.xpath("//span[contains(text(),'Classification')]/ancestor::td/following-sibling::td//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");
	private final By Brand_Name								 = By.xpath("//*[contains(text(),'Brand Name')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Item_Status_Code						 = By.xpath("//*[contains(text(),'Item Status Code')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Item_Status_Description				 = By.xpath("//*[contains(text(),'Item Status Description')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	public final By UOM_Product							 = By.xpath("//*[contains(text(),'UOM Product')]/../../..//td[@class='v-formlayout-contentcell']");
	private final By Case_Quantity							 = By.xpath("//*[contains(text(),'Case Quantity')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Case_Factor							 = By.xpath("//*[contains(text(),'Case Factor')]/../../..//td[@class='v-formlayout-contentcell'])[1] ");
	private final By Secondary_UOM							 = By.xpath("//*[contains(text(),'Secondary UOM')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Tax_Description						 = By.xpath("//*[contains(text(),'Tax Description')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Hazardous_Class_Code					 = By.xpath("//span[contains(text(),'Hazardous Class')]/ancestor::td/following-sibling::td//div[2]");
	private final By Hazardous_Class_Description			 = By.xpath("//*[contains(text(),'Hazardous Class Description')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	public final By FDA_Listed_Kit_Flag					 = By.xpath("//*[contains(text(),'FDA Listed Kit Flag')]/../../..//td[@class='v-formlayout-contentcell']");
	private final By Federal_Drug_Internal_Class_Code	= By.xpath("//span[contains(text(),'Federal Drug Internal')]/ancestor::td/following-sibling::td//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");
	private final By Federal_Drug_Class_Internal_Description = By.xpath("//*[contains(text(),'Federal Drug Class Internal Description')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Buyer_Code                          = By.xpath("//*[contains(text(),'Buyer Code')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Buyer_Name                          = By.xpath("//*[contains(text(),'Buyer Name')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Last_changed_by                   = By.xpath("//*[contains(text(),'Last changed by')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Last_changed_on                   = By.xpath("//*[contains(text(),'Last changed on')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By Product_Name                     = By.xpath("//*[contains(text(),'Product Name')]/../../..//td[@class='v-formlayout-contentcell'])[1]");
	private final By GlobalAttributesList                    = By.xpath("//td[@class='v-formlayout-contentcell']//div//div//div//div//div");
	public By FieldDropDown = By.xpath("//table//div[@class='v-filterselect-button']");
    private final By familyAttribure = By.xpath("(//span[contains(text(),'Family Set Leader:')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");

	public String getSuppliercode() {
		String suppliercode = getStringValues(Supplier_Code,WaitLogic.VISIBLE,"get supplier code");
		return suppliercode;
	}

	public String getManufacturercode() {
		String manufacturercode = getStringValues(Manufacturer_Code,WaitLogic.VISIBLE,"get manufacturer code");
		return manufacturercode;
	}

	public String getHazardousClassCode(){
		String hazardouscode = getStringValues(Hazardous_Class_Code,WaitLogic.VISIBLE,"get hazardous code");
		return hazardouscode;
	}

	public String getClassificationCode(){
		String classificationcode = getStringValues(Classification,WaitLogic.VISIBLE,"get classification code");
		return classificationcode;
	}

	public String getJdeDescription(){
		String jdedescription = getStringValues(JDE_Description,WaitLogic.VISIBLE,"get jde description");
		return jdedescription;
	}
	
	public String getJdeSize(){
		String jdeSize = getStringValues(JDE_Size, WaitLogic.VISIBLE, "get jde description");
		return jdeSize;
	}
	
	public String convertEAIntoEachForJdeSize(String jdeSize){
		String jdedescription = "Each";
		if(jdeSize.equals("EA")) {
			return jdedescription;
		}
		else {
			return jdeSize;
		}
	}

	public String getLineTypeCode()  {
		String linetypecode = getStringValues(Line_Type_Code,WaitLogic.VISIBLE,"line type code");
		return linetypecode;
	}

	public String getManufacturerPartNumber()  {
		String manufacturerPartNumber = getStringValues(Manufacturer_Part_Number,WaitLogic.VISIBLE,"get manufacturepartnumber code");
		return manufacturerPartNumber.trim()+" ";
	}
	
	public String getManufacturerPartNumberWithoutSpace()  {
		String manufacturerPartNumber = getStringValues(Manufacturer_Part_Number,WaitLogic.VISIBLE,"get manufacturepartnumber code");
		return manufacturerPartNumber.trim();
	}
	public boolean isGlobalAttributeFieldEditable(String fieldname, String visibilityElement) {

		By by = null;
		
		try {
			by = (By) GlobalAttributePage.class.getField(fieldname).get(GlobalAttributePage.this);
			click(by, WaitLogic.CLICKABLE, fieldname);
			by = (By) GlobalAttributePage.class.getField(visibilityElement).get(GlobalAttributePage.this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (isClickable(by, fieldname + visibilityElement, 5));
	}

	public String getHSI_Item_Number() {
		String hsiItemNumber = getStringValues(HSI_Item_Number,WaitLogic.VISIBLE,"get hsiItemNumber");
		return hsiItemNumber;
	}

	//To check visibility for all mandatory Global Attributes is Visible

	public boolean isAllAttributesVisible(String fieldName) {
		By by = null;
		try {
			by = (By) GlobalAttributePage.class.getField(fieldName).get(GlobalAttributePage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldName);
	}


	public String getFdac()  {
		String fdac = getStringValues(Federal_Drug_Internal_Class_Code,WaitLogic.VISIBLE,"fdac");
		return fdac;
	}

	public boolean isFrenchDescriptionDisabled() {
		boolean iselementEnable=true;
		iselementEnable = isClickable(JDE_French_Description,"JDE French Description");
		return iselementEnable;
	}

	public boolean isFrenchDescriptionReadOnly(String fieldname, String visibilityElement) {

		By by = null;

		try {
			by = (By) GlobalAttributePage.class.getField(fieldname).get(GlobalAttributePage.this);
			click(by, WaitLogic.CLICKABLE, fieldname);
			by = (By) GlobalAttributePage.class.getField(visibilityElement).get(GlobalAttributePage.this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (isVisible(by, fieldname + visibilityElement, 5));
	}

	public int checkFrenchDescriptionFieldlength()
	{
		String abc = getTextValue(JDE_French_Description,WaitLogic.VISIBLE,"Frech field");
		int length = abc.length();
		return length;
	}


	public GlobalAttributePage EnterFrenchDescriptionManually(String description) {
		click(JDE_French_Description, WaitLogic.CLICKABLE, "JDE_French_Description");
		WaitForMiliSec(2000);
		clearField(JDE_French_Description_textField, WaitLogic.CLICKABLE, "JDE_French_Description_textField");
		WaitForMiliSec(2000);
		click(JDE_French_Description, WaitLogic.CLICKABLE, "JDE_French_Description");
		WaitForMiliSec(2000);
		sendKeys(JDE_French_Description_textField, description, WaitLogic.VISIBLE, "JDE_French_Description");
		WaitForMiliSec(2000);
		return new GlobalAttributePage();
	}
	
	public String getFamilyAttribute() {
		return getTextValue(familyAttribure,WaitLogic.VISIBLE,"Family Attribute");
	}


}



