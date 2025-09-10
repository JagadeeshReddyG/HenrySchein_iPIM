package com.pim.pages;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;
import org.openqa.selenium.By;

public class CanadaTranslationPage extends BasePage {
    public final By JDE_Frech_Description				 = By.xpath("//*[contains(text(),'JDE French Description')]/../../../../div[contains(@class,'v-customcomponent')]");
    public final By classification_code				 = By.xpath("//*[contains(text(),'Classification Code')]/../../../../div[contains(@class,'v-customcomponent')]//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");
    public final By Translation_From					 = By.xpath("//*[contains(text(),'Translating From')]/../../../../div[contains(@class,'v-customcomponent')]");
    public final By Translation_To						 = By.xpath("//*[contains(text(),'Translating To')]/../../../../div[contains(@class,'v-customcomponent')]");
    public final By JDE_Description					 = By.xpath("//*[contains(text(),'JDE Description')]/../../../../div[contains(@class,'v-customcomponent')]");
    public final By LeftSide_Division					  = By.xpath("(//*[contains(text(),'Division')]/../../..//div[contains(@class,'v-select v-widget')])[1]//option[1]");
    public final By RightSide_Division					  = By.xpath("(//*[contains(text(),'Division')]/../../..//div[contains(@class,'v-select v-widget')])[2]");
    public final By LeftSide_Division_text					  = By.xpath("(//*[contains(text(),'Division')]/../../..//div[contains(@class,'v-select v-widget')])[1]//option[1]");
    public final By RightSide_Division_text					  = By.xpath("(//*[contains(text(),'Division')]/../../..//div[contains(@class,'v-select v-widget')])[2]//option[1]");
    public final By Left_Langaugae						 = By.xpath("(//*[contains(text(),'Language')]/../../..//div[contains(@class,'v-select v-widget')])[1]");
    public final By Left_Langaugae_text						 = By.xpath("(//*[contains(text(),'Language')]/../../..//div[contains(@class,'v-select v-widget')])[1]//option[2]");
    public final By Right_Langaugae						 = By.xpath("(//*[contains(text(),'Language')]/../../..//div[contains(@class,'v-select v-widget')])[2]");
    public final By Right_Langaugae_text					 = By.xpath("(//*[contains(text(),'Language')]/../../..//div[contains(@class,'v-select v-widget')])[2]////option[2]");
    public final By Left_Langaugae_dropdown						     = By.xpath("(//select[@class='v-select-select' ]/option[@value='2'])[3]");
    public final By Right_Langaugae_dropdown						 = By.xpath("(//select[@class='v-select-select' ]/option[@value='2'])[4]");
    public final By Full_Display_Description_LeftSide				= By.xpath("(//*[contains(text(),'Full Display Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Full_Display_Description_RightSide				= By.xpath("(//*[contains(text(),'Full Display Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Full_Display_Description_RightSide_textField	= By.xpath("//div//input[@class='v-textfield v-widget hpmw-singleline-textfield v-textfield-hpmw-singleline-textfield v-textfield-focus']");
    public final By Detail_Extended_Description_LeftSide	= By.xpath("(//*[contains(text(),'Detailed or Extended Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Detail_Extended_Description_RightSide	= By.xpath("(//*[contains(text(),'Detailed or Extended Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Abbreviated_Display_Description_LeftSide	= By.xpath("(//*[contains(text(),'Abbreviated Display Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Abbreviated_Display_Description_RightSide	= By.xpath("(//*[contains(text(),'Abbreviated Display Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Abbreviated_Display_Description_RightSide_textField	= By.xpath("//div//input[@class='v-textfield v-widget hpmw-singleline-textfield v-textfield-hpmw-singleline-textfield v-textfield-focus']");
    public final By Look_Ahead_SearchDescription_LeftSide	= By.xpath("(//*[contains(text(),'Look Ahead Search Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Look_Ahead_SearchDescription_English	= By.xpath("(//*[contains(text(),'Look Ahead Search Description')])[1]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    public final By Look_Ahead_SearchDescription_RightSide	= By.xpath("(//*[contains(text(),'Look Ahead Search Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Detail_Extended_Description_English	= By.xpath("(//*[contains(text(),'Detailed or Extended Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Detail_Extended_Description_French	= By.xpath("(//*[contains(text(),'Detailed or Extended Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Search_Description_LeftSide			= By.xpath("(//*[contains(text(),'Search Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Search_Description_RightSide			= By.xpath("(//*[contains(text(),'Search Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Search_Description_French			= By.xpath("(//*[contains(text(),'Search Description')])[4]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    public final By ProductOvreview_English				= By.xpath("(//*[contains(text(),'Product Overview / Technical Specification Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By ProductOvreview_French				= By.xpath("(//*[contains(text(),'Product Overview / Technical Specification Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Extended_WebDescription_English		= By.xpath("(//*[contains(text(),'Extended Web Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Extended_WebDescription_French		= By.xpath("(//*[contains(text(),'Extended Web Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By ProductOvreview_LeftSide				= By.xpath("(//*[contains(text(),'Product Overview / Technical Specification Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By ProductOvreview_RightSide				= By.xpath("(//*[contains(text(),'Product Overview / Technical Specification Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    public final By Extended_WebDescription_LeftSide		= By.xpath("(//*[contains(text(),'Extended Web Description')]/../../..//div[contains(@class,'v-panel-content')])[1]");
    public final By Extended_WebDescription_RightSide		= By.xpath("(//*[contains(text(),'Extended Web Description')]/../../..//div[contains(@class,'v-panel-content')])[2]");
    private final String tabSearchxpath = "//span[normalize-space()='${variable}']";
    public By FieldDropDown = By.xpath("//table//div[@class='v-filterselect-button']");

    private final By EnglishDivisionDropDown = By.xpath("(//select[@class='v-select-select'])[1]");
    private final By FrenchDivisionDropDown = By.xpath("(//select[@class='v-select-select'])[2]");
    
    //English Search description
    public final By EnglishSearchDescription= By.xpath("(//*[contains(text(),'Search Description')])[3]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    public final By EnglishSearchDescriptionInputValue= By.xpath("(//*[contains(text(),'Search Description')])[3]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']//input");

    //English Extended Web Description
    public final By EnglishExtendedWebDescription= By.xpath("(//*[contains(text(),'Extended Web Description')])[1]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    public final By EnglishExtendedWebDescriptionInputvalue= By.xpath("(//*[contains(text(),'Extended Web Description')])[1]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']//input");
    
    
    //English Extended Description
    public final By EnglishExtendeDescription= By.xpath("(//*[contains(text(),'Detailed or Extended Description')])[1]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    public final By EnglishExtendedDescriptionInputvalue= By.xpath("(//*[contains(text(),'Detailed or Extended Description')])[1]/../..//div[@class='v-panel v-widget light v-panel-light v-has-width']//input");


    
    //Clicking on English Discription Division
    
    public CanadaTranslationPage EnglishDivisionDropdown(String label) {
        dropdown(EnglishDivisionDropDown, label, WaitLogic.VISIBLE, "labelDropdown");
       WaitForMiliSec(3000);
        return this;
    }
    public CanadaTranslationPage FrenchDivisionDropdown(String label) {
    	WaitForMiliSec(3000);
    	click(FrenchDivisionDropDown, WaitLogic.CLICKABLE, "FrenchDivisionDropDown");
        dropdown(FrenchDivisionDropDown, label, WaitLogic.VISIBLE, "labelDropdown");
        WaitForMiliSec(3000);
        return this;
    }
	
    public boolean isCanadaTranslationFieldVisible(String fieldname) {
        By by = null;
        try {
            by = (By) CanadaTranslationPage.class.getField(fieldname).get(CanadaTranslationPage.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isVisible(by, fieldname);
    }

    public boolean isCanadaTranslationFieldEditable(String fieldname) {
        By by = null;
        try {
            by = (By) CanadaTranslationPage.class.getField(fieldname).get(CanadaTranslationPage.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEnabled(by, fieldname);
    }

  //For Canada translation field "Field Editable"
  	public boolean isCanadaTranslationFieldEditable(String fieldname, String visibilityElement) {

    
    		By by = null;

		try {
			by = (By) CanadaTranslationPage.class.getField(fieldname).get(CanadaTranslationPage.this);
			click(by, WaitLogic.CLICKABLE, fieldname);
			by = (By) CanadaTranslationPage.class.getField(visibilityElement).get(CanadaTranslationPage.this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (isVisible(by, fieldname + visibilityElement, 5));
	}
  	public String ElementValues()
    {
  	    	String ElementValue = getStringValues(LeftSide_Division,WaitLogic.CLICKABLE, "languageTypeBox");
  	    	return ElementValue;
  	    
    }


     public String compareElementValues(String fieldname)
    {
        WaitLogic waitstrategy = null;
        By by = null;
        try {
            by = (By) CanadaTranslationPage.class.getField(fieldname).get(CanadaTranslationPage.this);
            System.out.println(by);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTextValue(by,waitstrategy, fieldname);
    }
     
     public String compareElementAttributeValues(String fieldname)
     {
         WaitLogic waitstrategy = null;
         By by = null;
         try {
             by = (By) CanadaTranslationPage.class.getField(fieldname).get(CanadaTranslationPage.this);
             System.out.println(by);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return getAttributeValues(by,waitstrategy, fieldname);
     }


    //To get text of english description from canada translation Tab
    public String getFullDisplayEnglishDescription(){
        String description = getStringValues(Full_Display_Description_LeftSide,WaitLogic.CLICKABLE, "getEnglishDescription");
        return description;
    }
    
    public String getFullDisplayDescription1() {
		String FullDisplayDescription = getStringValues(Full_Display_Description_RightSide,WaitLogic.VISIBLE,"get Full Display Description");
		return FullDisplayDescription;
	}
	
	
    public String getAbbreviatedDisplayDescription1() {
		String AbbreviatedDisplayDescription = getStringValues(Abbreviated_Display_Description_RightSide,WaitLogic.VISIBLE,"get Abbreviated Display Description");
		return AbbreviatedDisplayDescription;
	}

	
	public CanadaTranslationPage EnterFullDisplayDescriptionManually1(String description) {
		click(Full_Display_Description_RightSide, WaitLogic.CLICKABLE, "Full_Display_Description");
        WaitForMiliSec(2000);
        clearField(Full_Display_Description_RightSide_textField, WaitLogic.CLICKABLE, "Full_Display_Description");
        WaitForMiliSec(2000);
        click(Full_Display_Description_RightSide, WaitLogic.CLICKABLE, "Full_Display_Description");
        WaitForMiliSec(2000);
        sendKeys(Full_Display_Description_RightSide_textField, description, WaitLogic.VISIBLE, "Full_Display_Description");
        WaitForMiliSec(2000);
       	return new CanadaTranslationPage();

		}
	
	public CanadaTranslationPage EnterAbbreviatedDisplayDescription1(String description) {
		click(Abbreviated_Display_Description_RightSide, WaitLogic.CLICKABLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
        clearField(Abbreviated_Display_Description_RightSide_textField, WaitLogic.CLICKABLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
        click(Abbreviated_Display_Description_RightSide, WaitLogic.CLICKABLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
        sendKeys(Abbreviated_Display_Description_RightSide_textField, description, WaitLogic.VISIBLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
       	return new CanadaTranslationPage();

		}
    
    public String getFullDisplayDescription() {
		String FullDisplayDescription = getStringValues(Full_Display_Description_RightSide,WaitLogic.VISIBLE,"get Full Display Description");
		return FullDisplayDescription;
	}
	
	
    public String getAbbreviatedDisplayDescription() {
		String AbbreviatedDisplayDescription = getStringValues(Abbreviated_Display_Description_RightSide,WaitLogic.VISIBLE,"get Abbreviated Display Description");
		return AbbreviatedDisplayDescription;
	}

	
	public CanadaTranslationPage EnterFullDisplayDescriptionManually(String description) {
		click(Full_Display_Description_RightSide, WaitLogic.CLICKABLE, "Full_Display_Description");
        WaitForMiliSec(2000);
        clearField(Full_Display_Description_RightSide_textField, WaitLogic.CLICKABLE, "Full_Display_Description");
        WaitForMiliSec(2000);
        click(Full_Display_Description_RightSide, WaitLogic.CLICKABLE, "Full_Display_Description");
        WaitForMiliSec(4000);
        sendKeys(Full_Display_Description_RightSide_textField, description, WaitLogic.VISIBLE, "Full_Display_Description");
        WaitForMiliSec(2000);
       	return new CanadaTranslationPage();

		}
	
	public CanadaTranslationPage EnterAbbreviatedDisplayDescription(String description) {
		WaitForMiliSec(2000);
		click(Abbreviated_Display_Description_RightSide, WaitLogic.CLICKABLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
        clearField(Abbreviated_Display_Description_RightSide_textField, WaitLogic.CLICKABLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
        click(Abbreviated_Display_Description_RightSide, WaitLogic.CLICKABLE, "Abbreviated_Display_Description");
        WaitForMiliSec(4000);
        sendKeys(Abbreviated_Display_Description_RightSide_textField, description, WaitLogic.VISIBLE, "Abbreviated_Display_Description");
        WaitForMiliSec(2000);
       	return new CanadaTranslationPage();


		}
    
	public CanadaTranslationPage searchDescription (String searchDescription) {
		click(EnglishSearchDescription, WaitLogic.CLICKABLE, "EnglishSearchDescription");
		selectTextViaKeyboard();
		deleteSelected();
		clearField(EnglishSearchDescriptionInputValue, WaitLogic.CLICKABLE, "EnglishSearchDescriptionInputValue");
		WaitForMiliSec(3000);
		click(EnglishSearchDescription, WaitLogic.CLICKABLE, "EnglishSearchDescription");
		WaitForMiliSec(2000);
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(searchDescription + " " + DateandTimeUtils.getTodaysDate());
		WaitForMiliSec(2000);
		return this;

	}
	
	public CanadaTranslationPage ExtendedWebDescription (String ExtendedWebDescription) {
		click(EnglishExtendedWebDescription, WaitLogic.CLICKABLE, "EnglishExtendedWebDescription");
		selectTextViaKeyboard();
		deleteSelected();
		clearField(EnglishExtendedWebDescriptionInputvalue, WaitLogic.CLICKABLE, "EnglishExtendedWebDescriptionInputvalue");
		WaitForMiliSec(3000);
		click(EnglishExtendedWebDescription, WaitLogic.CLICKABLE, "EnglishExtendedWebDescription");
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(ExtendedWebDescription + " " + DateandTimeUtils.getTodaysDate());
		WaitForMiliSec(2000);
		return this;

	}
	
	public CanadaTranslationPage ExtendedDescription (String ExtendedDescription) {
		WaitForMiliSec(3000);
		scrollToElement(EnglishExtendeDescription, "EnglishExtendeDescription");
		WaitForMiliSec(3000);
		click(EnglishExtendeDescription, WaitLogic.CLICKABLE, "EnglishExtendeDescription");
		selectTextViaKeyboard();
		deleteSelected();
		WaitForMiliSec(5000);
		sendKeys(EnglishExtendedDescriptionInputvalue, ExtendedDescription+ " " + DateandTimeUtils.getTodaysDate(), WaitLogic.VISIBLE,
				"EnglishExtendedDescriptionInputvalue");
		WaitForMiliSec(3000);
		return this;

	}
	
	public String getDetailedorExtendedDescriptionValueFrench() {
		WaitForMiliSec(5000);
		String DetailedorExtendedDescriptionValueFrench = getStringValues(Detail_Extended_Description_French, WaitLogic.VISIBLE, "get LookAheadSearchDescription Value");
		if (DetailedorExtendedDescriptionValueFrench.contains("No content")) {
			return DetailedorExtendedDescriptionValueFrench.replace(DetailedorExtendedDescriptionValueFrench, "");
		}
		return DetailedorExtendedDescriptionValueFrench.trim();
	}
	
	public String getDetailedorExtendedDescriptionValue() {
		WaitForMiliSec(5000);
		String DetailedorExtendedDescriptionValue = getStringValues(Detail_Extended_Description_English, WaitLogic.VISIBLE, "get LookAheadSearchDescription Value");
		if (DetailedorExtendedDescriptionValue.contains("No content")) {
			return DetailedorExtendedDescriptionValue.replace(DetailedorExtendedDescriptionValue, "");
		}
		return DetailedorExtendedDescriptionValue.trim();
	}
	
	public String getProductOverviewOrTechnicalSpecificationDescriptionValue() {
		WaitForMiliSec(5000);
		String getProductOverviewOrTechnicalSpecificationDescriptionValue = getStringValues(ProductOvreview_English, WaitLogic.VISIBLE, "get ProductOverviewOrTechnicalSpecificationDescription Value");
		if (getProductOverviewOrTechnicalSpecificationDescriptionValue.contains("No content")) {
			return getProductOverviewOrTechnicalSpecificationDescriptionValue.replace(getProductOverviewOrTechnicalSpecificationDescriptionValue, "");
		}
		return getProductOverviewOrTechnicalSpecificationDescriptionValue.trim();
	}
	
	public String getProductOverviewOrTechnicalSpecificationDescriptionValueFrench() {
		WaitForMiliSec(5000);
		String getProductOverviewOrTechnicalSpecificationDescriptionValueFrench = getStringValues(ProductOvreview_French, WaitLogic.VISIBLE, "get ProductOverviewOrTechnicalSpecificationDescription Value");
		if (getProductOverviewOrTechnicalSpecificationDescriptionValueFrench.contains("No content")) {
			return getProductOverviewOrTechnicalSpecificationDescriptionValueFrench.replace(getProductOverviewOrTechnicalSpecificationDescriptionValueFrench, "");
		}
		return getProductOverviewOrTechnicalSpecificationDescriptionValueFrench.trim();
	}
	
	public String getSearchDescriptionValue() {
		WaitForMiliSec(5000);
		String getSearchDescriptionValue = getStringValues(EnglishSearchDescription, WaitLogic.VISIBLE, "get EnglishSearchDescription Value");
		if (getSearchDescriptionValue.contains("No content")) {
			return getSearchDescriptionValue.replace(getSearchDescriptionValue, "");
		}
		return getSearchDescriptionValue.trim();
	}
	
	public String getSearchDescriptionValueFrench() {
		WaitForMiliSec(5000);
		String getSearchDescriptionValueFrench = getStringValues(Search_Description_French, WaitLogic.VISIBLE, "get EnglishSearchDescription Value");
		if (getSearchDescriptionValueFrench.contains("No content")) {
			return getSearchDescriptionValueFrench.replace(getSearchDescriptionValueFrench, "");
		}
		return getSearchDescriptionValueFrench.trim();
	}
	
	public String getExtendedWebDescriptionValue() {
		WaitForMiliSec(5000);
		String DetailedorExtendedDescriptionValue = getStringValues(Extended_WebDescription_English, WaitLogic.VISIBLE, "get LookAheadSearchDescription Value");
		if (DetailedorExtendedDescriptionValue.equals("No content")) {
			return DetailedorExtendedDescriptionValue.replace(DetailedorExtendedDescriptionValue, "");
		}
		return DetailedorExtendedDescriptionValue.trim();
	}
	
	public String getExtendedWebDescriptionValueFrench() {
		WaitForMiliSec(5000);
		String DetailedorExtendedDescriptionValueFrench = getStringValues(Extended_WebDescription_French, WaitLogic.VISIBLE, "get LookAheadSearchDescription Value");
		if (DetailedorExtendedDescriptionValueFrench.equals("No content")) {
			return DetailedorExtendedDescriptionValueFrench.replace(DetailedorExtendedDescriptionValueFrench, "");
		}
		return DetailedorExtendedDescriptionValueFrench.trim();
	}
	
	//To get classification code
	public String getClassificationCode(){
		String classificationCodeDivision = getStringValues(classification_code,WaitLogic.VISIBLE,"get classification code");
		return classificationCodeDivision;
	}
}