package com.pim.tests.Middleware;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.*;
import com.pim.pages.BasePage;
import com.pim.queries.ItemMessages_Queries;
import com.pim.utils.*;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import com.pim.utils.Javautils;
import static com.pim.reports.FrameworkLogger.log;

public class Middleware_Item_MessagesTest extends MiddlewareBaseClassTest {

	Javautils javautils = new Javautils();
	ApplicationUtils appUtils = new ApplicationUtils();

	//MDLW_027
		@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.SMOKE)
		@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
		@Test(description = "MDLW_027, MDLW_037 | Verify assigned dental division error message updated in  respective table ", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,
			groups = {"SMOKE","US","middleware"})
		public void verifyItemErrorMessageForDentalDivision(Map<String, String> map) {
			List<Map<String, String>> assignedDentalDivisionalErrorMessageFromEcomDB = null;
			List<Map<String, String>> assignedDentalDivisionalErrorMessageFromFSC_EZTable = null;
			List<Map<String, String>> assignedDentalDivisionalErrorMessageFromExtractDB = null;
			BasePage.WaitForMiliSec(5000);


			String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
			String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

			assignedDentalDivisionalErrorMessageFromExtractDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
					Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));
			
			BasePage.WaitForMiliSec(5000);

			boolean ExtractDBFlag = false;
			for(Map<String, String> assignedErrorExtract : assignedDentalDivisionalErrorMessageFromExtractDB) {
				Assertions.assertThat(assignedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
				if(assignedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedErrorExtract.get("Record Type").equalsIgnoreCase("U") &&
						assignedErrorExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
							ExtractDBFlag = true;
					}
				};
			Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
			log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


			assignedDentalDivisionalErrorMessageFromEcomDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
			boolean EcomBuildFlag = false;
			for(Map<String, String> assignedErrorECOM : assignedDentalDivisionalErrorMessageFromEcomDB) {
				Assertions.assertThat(assignedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
				if(assignedErrorECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
						assignedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
							EcomBuildFlag = true;
				};
			}
			Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
			log(LogType.INFO,"Added message is getting added in middleware ecomm db");

			assignedDentalDivisionalErrorMessageFromFSC_EZTable= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
			boolean FSC_EZTable = false;
			for(Map<String, String> assignedErrorFSC_EZ : assignedDentalDivisionalErrorMessageFromFSC_EZTable) {
				Assertions.assertThat(assignedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
				if(assignedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
				assignedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
					FSC_EZTable = true;
				};
			}
			Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
			log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


			//MDLW_037
			ExtractDBFlag = false;
			for(Map<String, String> dissociatedErrorExtract : assignedDentalDivisionalErrorMessageFromExtractDB) {
				Assertions.assertThat(dissociatedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
				if(dissociatedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
						dissociatedErrorExtract.get("Record Type").equalsIgnoreCase("D") &&
						dissociatedErrorExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
					ExtractDBFlag = true;
				}
			};
			Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
			log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

			EcomBuildFlag = false;
			for(Map<String, String> disassociatedErrorECOM : assignedDentalDivisionalErrorMessageFromEcomDB) {
				Assertions.assertThat(disassociatedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
				if(disassociatedErrorECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
						disassociatedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
					EcomBuildFlag = true;
				};
			}
			Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
			log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

			FSC_EZTable = false;
			for(Map<String, String> dissociatedErrorFSC_EZ : assignedDentalDivisionalErrorMessageFromFSC_EZTable) {
				Assertions.assertThat(dissociatedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
				if(dissociatedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
						dissociatedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
					FSC_EZTable = true;
				};
			}
			Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
			log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");
		}


	//MDLW_028
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_028, MDLW_038 | Verify assigned Medical division error message updated in  respective table ", priority = 1, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"US","middleware"})
	public void verifyItemErrorMessageForMedicalDivision(Map<String, String> map) {
		List<Map<String, String>> assignedMedicalDivisionalErrorMessageFromEcomDB = null;
		List<Map<String, String>> assignedMedicalDivisionalErrorMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedMedicalDivisionalErrorMessageFromExtractDB = null;

		BasePage.WaitForMiliSec(5000);

		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedMedicalDivisionalErrorMessageFromExtractDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));
		
		BasePage.WaitForMiliSec(5000);


		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedErrorExtract : assignedMedicalDivisionalErrorMessageFromExtractDB) {
			Assertions.assertThat(assignedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedErrorExtract.get("Record Type").equalsIgnoreCase("U") &&
					assignedErrorExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedMedicalDivisionalErrorMessageFromEcomDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedErrorECOM : assignedMedicalDivisionalErrorMessageFromEcomDB) {
			Assertions.assertThat(assignedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedErrorECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");

		assignedMedicalDivisionalErrorMessageFromFSC_EZTable= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
		boolean FSC_EZTable = false;
		for(Map<String, String> assignedErrorFSC_EZ : assignedMedicalDivisionalErrorMessageFromFSC_EZTable) {
			Assertions.assertThat(assignedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(assignedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


		//MDLW_038
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedErrorExtract : assignedMedicalDivisionalErrorMessageFromExtractDB) {
			Assertions.assertThat(dissociatedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedErrorExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedErrorExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedErrorECOM : assignedMedicalDivisionalErrorMessageFromEcomDB) {
			Assertions.assertThat(disassociatedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedErrorECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

		FSC_EZTable = false;
		for(Map<String, String> dissociatedErrorFSC_EZ : assignedMedicalDivisionalErrorMessageFromFSC_EZTable) {
			Assertions.assertThat(dissociatedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(dissociatedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					dissociatedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");
	}
	
	//MDLW_029
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_029 , MDLW_039 | Verify assigned Zahn division error message updated in  respective table ", priority = 2, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyItemErrorMessageForZahnDivision(Map<String, String> map) {
		List<Map<String, String>> assignedZahnDivisionalErrorMessageFromEcomDB = null;
		List<Map<String, String>> assignedZahnDivisionalErrorMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedZahnDivisionalErrorMessageFromExtractDB = null;


		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedZahnDivisionalErrorMessageFromExtractDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedErrorExtract : assignedZahnDivisionalErrorMessageFromExtractDB) {
			Assertions.assertThat(assignedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedErrorExtract.get("Record Type").equalsIgnoreCase("U") &&
					assignedErrorExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedZahnDivisionalErrorMessageFromEcomDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedErrorECOM : assignedZahnDivisionalErrorMessageFromEcomDB) {
			Assertions.assertThat(assignedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedErrorECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");

		assignedZahnDivisionalErrorMessageFromFSC_EZTable= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
		boolean FSC_EZTable = false;
		for(Map<String, String> assignedErrorFSC_EZ : assignedZahnDivisionalErrorMessageFromFSC_EZTable) {
			Assertions.assertThat(assignedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(assignedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


		//MDLW_039
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedErrorExtract : assignedZahnDivisionalErrorMessageFromExtractDB) {
			Assertions.assertThat(dissociatedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedErrorExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedErrorExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedErrorECOM : assignedZahnDivisionalErrorMessageFromEcomDB) {
			Assertions.assertThat(disassociatedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedErrorECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

		FSC_EZTable = false;
		for(Map<String, String> dissociatedErrorFSC_EZ : assignedZahnDivisionalErrorMessageFromFSC_EZTable) {
			Assertions.assertThat(dissociatedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(dissociatedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					dissociatedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");
	}

	//MDLW_030, MDLW_040
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_030, MDLW_040 | Verify assigned Special market division error message updated in  respective table ", priority = 3, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyItemErrorMessageForSpecialMarketDivision(Map<String, String> map) {
		List<Map<String, String>> assignedSpecialMarketDivisionalErrorMessageFromEcomDB = null;
		List<Map<String, String>> assignedSpecialMarketDivisionalErrorMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedSpecialMarketDivisionalErrorMessageFromExtractDB = null;


		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedSpecialMarketDivisionalErrorMessageFromExtractDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedErrorExtract : assignedSpecialMarketDivisionalErrorMessageFromExtractDB) {
			Assertions.assertThat(assignedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedErrorExtract.get("Record Type").equalsIgnoreCase("I") &&
					assignedErrorExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedSpecialMarketDivisionalErrorMessageFromEcomDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedErrorECOM : assignedSpecialMarketDivisionalErrorMessageFromEcomDB) {
			Assertions.assertThat(assignedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedErrorECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("I")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");


		//MDLW_040
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedErrorExtract : assignedSpecialMarketDivisionalErrorMessageFromExtractDB) {
			Assertions.assertThat(dissociatedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedErrorExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedErrorExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedErrorECOM : assignedSpecialMarketDivisionalErrorMessageFromEcomDB) {
			Assertions.assertThat(disassociatedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedErrorECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

	}

	//MDLW_031, MDLW_041
		@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
		@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
		@Test(description = "MDLW_031, MDLW_041 | Verify assigned Global error message updated in  respective table ", priority = 4, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
		public void verifyErrorGlobalMessage(Map<String, String> map) {
			List<Map<String, String>> assignedGlobalErrorMessageFromEcomDB = null;
			List<Map<String, String>> assignedGlobalErrorMessageFromFSC_EZTable = null;
			List<Map<String, String>> assignedGlobalErrorMessageFromExtractDB = null;


			String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
			String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

			assignedGlobalErrorMessageFromExtractDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
					Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));

			boolean ExtractDBFlag = false;
			for(Map<String, String> assignedErrorExtract : assignedGlobalErrorMessageFromExtractDB) {
				Assertions.assertThat(assignedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
				if(assignedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
						assignedErrorExtract.get("Record Type").equalsIgnoreCase("U") &&
						assignedErrorExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
					ExtractDBFlag = true;
				}
			};
			Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
			log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


			assignedGlobalErrorMessageFromEcomDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
			boolean EcomBuildFlag = false;
			for(Map<String, String> assignedErrorECOM : assignedGlobalErrorMessageFromEcomDB ) {
				Assertions.assertThat(assignedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
				if(assignedErrorECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
						assignedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
					EcomBuildFlag = true;
				};
			}
			Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
			log(LogType.INFO,"Added message is getting added in middleware ecomm db");

			assignedGlobalErrorMessageFromFSC_EZTable= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
			boolean FSC_EZTable = false;
			for(Map<String, String> assignedErrorFSC_EZ : assignedGlobalErrorMessageFromFSC_EZTable) {
				Assertions.assertThat(assignedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
				if(assignedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
						assignedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
					FSC_EZTable = true;
				};
			}
			Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
			log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


			//MDLW_041
			ExtractDBFlag = false;
			for(Map<String, String> dissociatedErrorExtract : assignedGlobalErrorMessageFromExtractDB) {
				Assertions.assertThat(dissociatedErrorExtract).containsKeys("Message Code", "Record Type", "Division");
				if(dissociatedErrorExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
						dissociatedErrorExtract.get("Record Type").equalsIgnoreCase("D") &&
						dissociatedErrorExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
					ExtractDBFlag = true;
				}
			};
			Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
			log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

			EcomBuildFlag = false;
			for(Map<String, String> disassociatedErrorECOM : assignedGlobalErrorMessageFromEcomDB) {
				Assertions.assertThat(disassociatedErrorECOM).containsKeys("SQL_STATUS", "MSGCODE");
				if(disassociatedErrorECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
						disassociatedErrorECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
					EcomBuildFlag = true;
				};
			}
			Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
			log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

			FSC_EZTable = false;
			for(Map<String, String> dissociatedErrorFSC_EZ : assignedGlobalErrorMessageFromFSC_EZTable) {
				Assertions.assertThat(dissociatedErrorFSC_EZ).containsKeys("MSGCODE", "Status_Type");
				if(dissociatedErrorFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
						dissociatedErrorFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
					FSC_EZTable = true;
				};
			}
			Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
			log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");

		}
		
    //MDLW_032, MDLW_042
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_032, MDLW_042 | Verify assigned dental division warning message updated in  respective table ", priority = 5, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyItemWarningMessageForDentalDivision(Map<String, String> map) {
		List<Map<String, String>> assignedDentalDivisionalWarningMessageFromEcomDB = null;
		List<Map<String, String>> assignedDentalDivisionalWarningMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedDentalDivisionalWarningMessageFromExtractDB = null;


		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		BasePage.WaitForMiliSec(5000);
		assignedDentalDivisionalWarningMessageFromExtractDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));
		BasePage.WaitForMiliSec(5000);

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedWarningExtract : assignedDentalDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(assignedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedWarningExtract.get("Record Type").equalsIgnoreCase("U") &&
					assignedWarningExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedDentalDivisionalWarningMessageFromEcomDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedWarningECOM : assignedDentalDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(assignedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedWarningECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");

		assignedDentalDivisionalWarningMessageFromFSC_EZTable= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
		boolean FSC_EZTable = false;
		for(Map<String, String> assignedWarningFSC_EZ : assignedDentalDivisionalWarningMessageFromFSC_EZTable) {
			Assertions.assertThat(assignedWarningFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(assignedWarningFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


		//MDLW_042
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedWarningExtract : assignedDentalDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(dissociatedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedWarningExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedWarningExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedWarningECOM : assignedDentalDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(disassociatedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedWarningECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

		FSC_EZTable = false;
		for(Map<String, String> dissociatedWarningFSC_EZ : assignedDentalDivisionalWarningMessageFromFSC_EZTable) {
			Assertions.assertThat(dissociatedWarningFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(dissociatedWarningFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					dissociatedWarningFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");
	}

	//MDLW_033, MDLW_043
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_033, MDLW_043 | Verify assigned medical division warning message updated in  respective table ", priority = 6, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyItemWarningMessageForMedicalDivision(Map<String, String> map) {
		List<Map<String, String>> assignedMedicalDivisionalWarningMessageFromEcomDB = null;
		List<Map<String, String>> assignedMedicalDivisionalWarningMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedMedicalDivisionalWarningMessageFromExtractDB = null;

		BasePage.WaitForMiliSec(10000);

		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedMedicalDivisionalWarningMessageFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));
		
		BasePage.WaitForMiliSec(10000);

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedWarningExtract : assignedMedicalDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(assignedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedWarningExtract.get("Record Type").equalsIgnoreCase("U") &&
					assignedWarningExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedMedicalDivisionalWarningMessageFromEcomDB= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedWarningECOM : assignedMedicalDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(assignedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedWarningECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");

		assignedMedicalDivisionalWarningMessageFromFSC_EZTable= DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
		boolean FSC_EZTable = false;
		for(Map<String, String> assignedWarningFSC_EZ : assignedMedicalDivisionalWarningMessageFromFSC_EZTable) {
			Assertions.assertThat(assignedWarningFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(assignedWarningFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


		//MDLW_043
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedWarningExtract : assignedMedicalDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(dissociatedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedWarningExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedWarningExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedWarningECOM : assignedMedicalDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(disassociatedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedWarningECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

		FSC_EZTable = false;
		for(Map<String, String> dissociatedWarningFSC_EZ : assignedMedicalDivisionalWarningMessageFromFSC_EZTable) {
			Assertions.assertThat(dissociatedWarningFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(dissociatedWarningFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					dissociatedWarningFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");
	}


	//MDLW_034, MDLW_044
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_034, MDLW_044 | Verify assigned zahn division warning message updated in  respective table ", priority = 7, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyItemWarningMessageForZahnDivision(Map<String, String> map) {
		List<Map<String, String>> assignedZahnDivisionalWarningMessageFromEcomDB = null;
		List<Map<String, String>> assignedZahnDivisionalWarningMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedZahnDivisionalWarningMessageFromExtractDB = null;


		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedZahnDivisionalWarningMessageFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedWarningExtract : assignedZahnDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(assignedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedWarningExtract.get("Record Type").equalsIgnoreCase("I") &&
					assignedWarningExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedZahnDivisionalWarningMessageFromEcomDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedWarningECOM : assignedZahnDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(assignedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedWarningECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("I")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");


		//MDLW_044
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedWarningExtract : assignedZahnDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(dissociatedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedWarningExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedWarningExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedWarningECOM : assignedZahnDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(disassociatedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedWarningECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");
	}


	//MDLW_035, MDLW_045
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_035, MDLW_045 | Verify assigned special market division warning message updated in  respective table ", priority = 8, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyItemWarningMessageForSpecialMarketDivision(Map<String, String> map) {
		List<Map<String, String>> assignedSpecialMarketDivisionalWarningMessageFromEcomDB = null;
		List<Map<String, String>> assignedSpecialMarketDivisionalWarningMessageFromExtractDB = null;


		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedSpecialMarketDivisionalWarningMessageFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedWarningExtract : assignedSpecialMarketDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(assignedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedWarningExtract.get("Record Type").equalsIgnoreCase("U") &&
					assignedWarningExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedSpecialMarketDivisionalWarningMessageFromEcomDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedWarningECOM : assignedSpecialMarketDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(assignedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedWarningECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");



		//MDLW_045
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedWarningExtract : assignedSpecialMarketDivisionalWarningMessageFromExtractDB) {
			Assertions.assertThat(dissociatedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedWarningExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedWarningExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedWarningECOM : assignedSpecialMarketDivisionalWarningMessageFromEcomDB) {
			Assertions.assertThat(disassociatedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedWarningECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

		

	}

	//MDLW_036, MDLW_046
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.Valid_Failure)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "MDLW_036, MDLW_046 | Verify assigned Global warning message updated in  respective table ", priority = 9, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","middleware"})
	public void verifyWarningGlobalMessage(Map<String, String> map) {
		List<Map<String, String>> assignedGlobalWarningMessageFromEcomDB = null;
		List<Map<String, String>> assignedGlobalWarningMessageFromFSC_EZTable = null;
		List<Map<String, String>> assignedGlobalWarningMessageFromExtractDB = null;


		String addedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"));
		String deletedMessage = appUtils.getMessageFromCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"));

		assignedGlobalWarningMessageFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllErrorAndWarningItemMssgByItemCodeQuery_Extract_table(map.get("ItemNumber")), DataBaseColumnName.Division.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Message_Code.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Record_Type.name()));

		boolean ExtractDBFlag = false;
		for(Map<String, String> assignedWarningExtract : assignedGlobalWarningMessageFromExtractDB) {
			Assertions.assertThat(assignedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(assignedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					assignedWarningExtract.get("Record Type").equalsIgnoreCase("U") &&
					assignedWarningExtract.get("Message Code").equalsIgnoreCase(addedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Updated item message is not reflecting in Extract DB");
		log(LogType.INFO,"Added Message is getting updated in middleware Extract DB");


		assignedGlobalWarningMessageFromEcomDB = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_Messages_InAllDivisionForItemCodeQuery_Ecom_build_table(map.get("ItemNumber")),  DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.SQL_STATUS.name());
		boolean EcomBuildFlag = false;
		for(Map<String, String> assignedWarningECOM : assignedGlobalWarningMessageFromEcomDB) {
			Assertions.assertThat(assignedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(assignedWarningECOM.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("U")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Added message is getting added in middleware ecomm db");

		assignedGlobalWarningMessageFromFSC_EZTable = DatabaseUtilitiies.getMultipleQueryResult(ItemMessages_Queries.getAllItemError_Warning_MessagesInAllDivisionForItemCodeQuery_FSC_EZ_table(map.get("ItemNumber")), DataBaseColumnName.MSGCODE.name(), DataBaseColumnName.Status_Type.name());
		boolean FSC_EZTable = false;
		for(Map<String, String> assignedWarningFSC_EZ : assignedGlobalWarningMessageFromFSC_EZTable) {
			Assertions.assertThat(assignedWarningFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(assignedWarningFSC_EZ.get("MSGCODE").equalsIgnoreCase(addedMessage) &&
					assignedWarningFSC_EZ.get("Status_Type").equalsIgnoreCase("I")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Updated item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Added Message is getting updated in middleware FSC EZ Table");


		//MDLW_046
		ExtractDBFlag = false;
		for(Map<String, String> dissociatedWarningExtract : assignedGlobalWarningMessageFromExtractDB) {
			Assertions.assertThat(dissociatedWarningExtract).containsKeys("Message Code", "Record Type", "Division");
			if(dissociatedWarningExtract.get("Division").equalsIgnoreCase(map.get("DivisionType")) &&
					dissociatedWarningExtract.get("Record Type").equalsIgnoreCase("D") &&
					dissociatedWarningExtract.get("Message Code").equalsIgnoreCase(deletedMessage)){
				ExtractDBFlag = true;
			}
		};
		Assert.assertTrue(ExtractDBFlag, "Deleted item message is not reflecting in Extract DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Extract DB");

		EcomBuildFlag = false;
		for(Map<String, String> disassociatedWarningECOM : assignedGlobalWarningMessageFromEcomDB) {
			Assertions.assertThat(disassociatedWarningECOM).containsKeys("SQL_STATUS", "MSGCODE");
			if(disassociatedWarningECOM.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					disassociatedWarningECOM.get("SQL_STATUS").equalsIgnoreCase("D")){
				EcomBuildFlag = true;
			};
		}
		Assert.assertTrue(EcomBuildFlag, "Deleted item message is not reflecting in Ecomm DB");
		log(LogType.INFO,"Deleted Message is getting updated in middleware Ecomm DB");

		FSC_EZTable = false;
		for(Map<String, String> dissociatedWarningFSC_EZ : assignedGlobalWarningMessageFromFSC_EZTable) {
			Assertions.assertThat(dissociatedWarningFSC_EZ).containsKeys("MSGCODE", "Status_Type");
			if(dissociatedWarningFSC_EZ.get("MSGCODE").equalsIgnoreCase(deletedMessage) &&
					dissociatedWarningFSC_EZ.get("Status_Type").equalsIgnoreCase("D")){
				FSC_EZTable = true;
			};
		}
		Assert.assertTrue(FSC_EZTable, "Deleted item message is not reflecting in FSC EZ Table");
		log(LogType.INFO,"Deleted Message is getting updated in middleware FSC EZ Table");

	}


}