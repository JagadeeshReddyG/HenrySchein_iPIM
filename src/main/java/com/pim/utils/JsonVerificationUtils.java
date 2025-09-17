package com.pim.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.pim.enums.LogType;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class JsonVerificationUtils {

	// Method to get all Product_Id values from the JSON file
	public static List<String> getAllProductIds(String jsonFilePath) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);

		// JSONPath to get all Product_Id fields from Products array
		String jsonPath = "$.Products[*].Product_Id";

		// Fetch list of Product_Id values
		return ctx.read(jsonPath);

	}

	// Method to read JSON from file and return ReadContext
	public static ReadContext parseJsonFile(String filePath) throws IOException {
		File jsonFile = new File(filePath);
		return JsonPath.parse(jsonFile);
	}

	// Method to fetch data from JSON using JsonPath
	public static <T> T fetchDataFromJson(ReadContext ctx, String jsonPath, Class<T> clazz) {
		return ctx.read(jsonPath, clazz);
	}

	// Method to get Document_Id for given Name and Product_Id
	public static String getMime_Type(String jsonFilePath, String productId, String name) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);

		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].AdditionalProductInfo.AdditionalAssets[?(@.Name=='%s')].Mime-Type", productId, name);

		// Fetch the Document_Id using JsonPath
		List<String> documentIds = fetchDataFromJson(ctx, jsonPath, List.class);

		// Return the first matching Document_Id, or null if none found
		return documentIds.isEmpty() ? null : documentIds.get(0);
	}

	// Method to get Document_Id for given Name and Product_Id
	@SuppressWarnings("unchecked")
	public static String getDocumentIdOfGivenVivaAsset(String jsonFilePath, String productId, String vivaAssetName) throws IOException {
		ReadContext readContext = parseJsonFile(jsonFilePath);

		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].AdditionalProductInfo.AdditionalAssets[?(@.Name=='%s')].Document_Id", productId, vivaAssetName);

		// Fetch the Document_Id using JsonPath
		List<String> documentIds = fetchDataFromJson(readContext, jsonPath, List.class);

		// Return the first matching Document_Id, or null if none found
		return documentIds.isEmpty() ? null : documentIds.get(0);
	}

	public static String getFileNameOfGivenVivaAsset(String jsonFilePath, String productId, String vivaAssetName) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);

		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].AdditionalProductInfo.AdditionalAssets[?(@.Name=='%s')].FileName", productId, vivaAssetName);

		// Fetch the Document_Id using JsonPath
		List<String> documentIds = fetchDataFromJson(ctx, jsonPath, List.class);

		// Return the first matching Document_Id, or null if none found
		return documentIds.isEmpty() ? null : documentIds.get(0);
	}

	public static String getDAM_IdentiferOfGivenMedia_Id(String jsonFilePath, String productId, String mediaId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Media[?(@.Media_Id=='%s')].DAM_Identifer", productId, mediaId);

		// Fetch the Prices using JsonPath
		List<String> DAM_Identifer = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return DAM_Identifer.isEmpty() ? null : DAM_Identifer.get(0);
	}

	public static List<String> getItemMedia_Id(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Media[*].Media_Id", productId);

		// Fetch the Prices using JsonPath
		List<String> Media_Ids = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return Media_Ids.isEmpty() ? null : Media_Ids;
	}


	// Method to get Description for given Product_Id
	public static String getDescriptionsOfAdditionalDescription(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].AdditionalProductInfo.AdditionalDescription[*].Description", productId);
		// Fetch the Descriptions using JsonPath
		List<String> description = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return description.isEmpty() ? null : description.get(0);
	}

	public static String getPriceInsidePrices(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Prices[*].Price", productId);
		// Fetch the Prices using JsonPath
		List<String> price = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return price.isEmpty() ? null : price.get(0);
	}

	public static String getProductNotesInsideDescriptions(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Descriptions[*].Product_Notes", productId);

		// Fetch the productNotes using JsonPath
		List<String> productNotes = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching productNotes, or null if none found
		return productNotes.isEmpty() ? null : productNotes.get(0);
	}

	public static String getAvailability_CodeOfGivenProductID(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Availability_Code", productId);

		// Fetch the Prices using JsonPath
		List<String> DAM_Identifer = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return DAM_Identifer.isEmpty() ? null : DAM_Identifer.get(0);
	}

	public static String getFullDisplayDescriptionOfGivenProductID(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Descriptions[*].Full_Display_Description", productId);

		// Fetch the Prices using JsonPath
		List<String> DAM_Identifer = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return DAM_Identifer.isEmpty() ? null : DAM_Identifer.get(0);
	}

	public static String getDimension_ValueOfGivenDimension_Name(String jsonFilePath, String productId, String DimensionName) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Dimensions[?(@.Dimension_Name=='%s')].Dimension_Value", productId, DimensionName);

		// Fetch the Prices using JsonPath
		List<String> DAM_Identifer = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return DAM_Identifer.isEmpty() ? null : DAM_Identifer.get(0);
	}

	public static Map<String, String> getAll_Dimension_Name_And_Dimension_Values_In_Map(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPathDimName = String.format("$.Products[?(@.Product_Id=='%s')].Dimensions[*].Dimension_Name", productId);

		// Fetch the Prices using JsonPath
		List<String> dimName = fetchDataFromJson(ctx, jsonPathDimName, List.class);

		String jsonPathDimValue = String.format("$.Products[?(@.Product_Id=='%s')].Dimensions[*].Dimension_Value", productId);

		// Fetch the Prices using JsonPath
		List<String> dimValue = fetchDataFromJson(ctx, jsonPathDimValue, List.class);
		// Return the first matching Document_Id, or null if none found

		Map<String, String> map_LA = new HashMap<String, String>();

		for (int i = 0; i < dimName.size(); i++) {
			String LA_Keys = dimName.get(i);

			String LA_Values = dimValue.get(i);

			map_LA.put(LA_Keys, LA_Values);
		}
		return map_LA;
	}

	public static String getManufacturer_IDOfGivenProdictId(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Manufacturer_ID", productId);

		// Fetch the Prices using JsonPath
		List<String> value = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return value.isEmpty() ? null : value.get(0);
	}

	public static String getGTINOfGivenProdictId(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].GTIN[*].GTIN", productId);

		// Fetch the Prices using JsonPath
		List<String> value = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return value.isEmpty() ? null : value.get(0);
	}

	public static List<String> getListOfCatalog_Name_OfGivenProductId(String jsonFilePath, String productId) throws IOException {
		ReadContext ctx = parseJsonFile(jsonFilePath);
		// Construct the JSON path
		String jsonPath = String.format("$.Products[?(@.Product_Id=='%s')].Non_Base-Catalog_to_Product_mapping[*].Catalog_Id", productId);

		// Fetch the Prices using JsonPath
		List<String> value = fetchDataFromJson(ctx, jsonPath, List.class);
		// Return the first matching Document_Id, or null if none found
		return value.isEmpty() ? null : value;
	}


	// Example usage of the getDocumentId method
	public static void main(String[] args) {
//    	int time = 60;
//    	String folderDirectory = ".\\src\\test\\resources\\JSON\\08052025110034454_PIM_EXTRACT.json";
//
//    	List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
//    	for (String jsonFileName : jsonFileNames) {
//    		String jsonFilePath = folderDirectory+jsonFileName;
//		System.out.println(jsonFilePath);
////		String jsonFilePath = ".\\\\src\\\\test\\\\resources\\\\JSON\\\\yourfile.json";
//
//        String productId = "2900095";
//        String VivaAsset = "2900095_2_1109_Constic300300_20161219090616525.png";
////        String media_ID = "8580026_US_Top_03.jpg";
//
////        String productId = "2290225";
//        String Dimension_Name = "Quantity";
//        String media_ID = "2900095_US_front_01.jpg";
//
//
//        try {
//            String documentId = getDocumentIdOfGivenVivaAsset(jsonFilePath, productId, VivaAsset);
//            String mimeType = getMime_Type(jsonFilePath, productId, VivaAsset);
//            String desc = getDescriptionsOfAdditionalDescription(jsonFilePath, productId);
//            String price = getPriceInsidePrices(jsonFilePath, productId);
//            String dam_Identifer = getDAM_IdentiferOfGivenMedia_Id(jsonFilePath,productId, media_ID);
//            String Availcode = getAvailability_CodeOfGivenProductID(jsonFilePath, productId);
//            String fullDisDesc = getFullDisplayDescriptionOfGivenProductID(jsonFilePath, productId);
//
//          String DimensionValue = getDimension_ValueOfGivenDimension_Name(jsonFilePath, productId, Dimension_Name);
//          String manuId = getManufacturer_IDOfGivenProdictId(jsonFilePath, productId);
//          String gtin = getGTINOfGivenProdictId(jsonFilePath, productId);
//          List<String> CatId = getListOfCatalog_Name_OfGivenProductId(jsonFilePath, productId);
//
//
//
////          System.out.println("Found DimensionValue: " + DimensionValue);
//
//            System.out.println("Found Document_Id: " + documentId);
//            System.out.println("Found Mime_Type: " + mimeType);
//            System.out.println("Found desc: " + desc);
//            System.out.println("Found price: " + price);
//            System.out.println("Found DAM_Identifer: " + dam_Identifer);
//            System.out.println("Found Availcode: " + Availcode);
//            System.out.println("Found fullDisDesc: " + fullDisDesc);
//            System.out.println("Found DimensionValue: " + DimensionValue);
//            System.out.println("Found manuId: " + manuId);
//            System.out.println("Found gtin: " + gtin);
//            System.out.println("Found CatId: " + CatId);
//
////            if (documentId != null) {
////                System.out.println("Found Document_Id: " + documentId);
////            } else {
////                System.out.println("Document_Id not found!");
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    	}


		String folderDirectory = "Y:\\US\\Archive\\2025\\06\\";
		String fileName = "16062025100108520_PIM_EXTRACT.json";
		String jsonFilePath = folderDirectory + fileName;

		try {
			// Step 1: Parse the JSON file
			ReadContext ctx = parseJsonFile(jsonFilePath);

			// Step 2: Fetch the first Product_Id
			List<String> productIds = fetchDataFromJson(ctx, "$.Products[*].Product_Id", List.class);
			if (productIds == null || productIds.isEmpty()) {
				System.out.println("No products found.");
				return;
			}
			String firstProductId = productIds.get(0);
			System.out.println("First Product Id: " + firstProductId);

			// Step 3: Fetch all Dimension_Name and Dimension_Value as a map
			Map<String, String> dimensionsMap = getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath, firstProductId);

			// Step 4: Print the Dimension info
			if (dimensionsMap.isEmpty()) {
				System.out.println("No Dimensions found for product id: " + firstProductId);
			} else {
				System.out.println("Dimensions for Product Id: " + firstProductId);
				for (Map.Entry<String, String> entry : dimensionsMap.entrySet()) {
					System.out.println("Dimension Name: " + " Key: " + entry.getKey() + " -> Value: " + entry.getValue());
				}
			}

			// Step 4: Fetch catalog list using your existing method
			List<String> catalogIds = getListOfCatalog_Name_OfGivenProductId(jsonFilePath, firstProductId);

			// Step 4: Print the catalog info
			if (catalogIds == null || catalogIds.isEmpty()) {
				System.out.println("No Non-Base Catalogs found for product id: " + firstProductId);
			} else {
				System.out.println("Non-Base Catalogs for Product Id: " + firstProductId);
				System.out.println("catalogIds: " + catalogIds);
				for (String catalogId : catalogIds) {
					System.out.println(" - Catalog_Id: " + catalogId);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


//    	    // All files Read
//    	    String folderDirectory = ".\\src\\test\\resources\\JSON\\";
//    	    File folder = new File(folderDirectory);
//
//    	    File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
//    	    if (files == null || files.length == 0) {
//    	        System.out.println("No JSON files found in the folder.");
//    	        return;
//    	    }
//
//    	    for (File file : files) {
//    	        String jsonFilePath = file.getAbsolutePath();
//    	        System.out.println("\nReading File: " + file.getName());
//
//    	        try {
//    	            ReadContext ctx = parseJsonFile(jsonFilePath);
//    	            List<String> productIds = fetchDataFromJson(ctx, "$.Products[*].Product_Id", List.class);
//
//    	            if (productIds == null || productIds.isEmpty()) {
//    	                System.out.println("No Product_Id found in " + file.getName());
//    	            } else {
//    	                System.out.println("Product_Ids found:");
//    	                for (String id : productIds) {
//    	                    System.out.println(" - " + id);
//    	                }
//    	            }
//
//    	        } catch (Exception e) {
//    	            System.out.println("Error reading " + file.getName());
//    	            e.printStackTrace();
//    	        }
//    	    }

	}

//	public static String getProductNotesFromIPIM(String jsonContent, String itemNumber) {
//		// Parse JSON content
//		ReadContext ctx = JsonPath.parse(jsonContent);
//
//		// Verify Product_Id matches
//		String idFromJson = ctx.read("$.Product_Id", String.class);
//		if (!idFromJson.equals(itemNumber)) {
//			// use itemNumber here
//			return null;
//		}
//
//		// JsonPath to Product_Notes inside Descriptions
//		String jsonPath = "$.Descriptions[*].Product_Notes";
//
//		// Fetch list of notes
//		List<String> productNotes = ctx.read(jsonPath);
//
//		// Return the first non-empty Product_Notes
//		for (String note : productNotes) {
//			if (note != null && !note.trim().isEmpty()) {
//				return note;
//			}
//		}
//		return null;
//	}


	//Fetch Product Notes from iPIM Json
	public static String getProductNotesFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);

		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;

		// Fetch Product_Notes
		List<String> productNotes = ctx.read("$.Descriptions[*].Product_Notes");

		// Just return first (or null if empty)
		return productNotes.isEmpty() ? null : productNotes.get(0);
	}

	//Fetch Item Media from iPIM Json
	public static List<String> getItemMediaFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;

		// Fetch Item Media
		List<String> mediaIds = ctx.read("$.Media[*].Media_Id");
		return mediaIds == null || mediaIds.isEmpty() ? null : mediaIds;
	}

	//Fetch List Price from iPIM Json
	public static String getListPriceFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		// Fetch List Price
		List<String> prices = ctx.read("$.Prices[*].Price");
		// Return the first Price, or null if the array is empty
		return prices.isEmpty() ? null : prices.get(0);
	}

	public static String getGEP_Full_Displayed_Web_DescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		// Fetch List Price
		List<String> prices = ctx.read("$.Descriptions[*].Full_Display_Description");
		// Return the first Price, or null if the array is empty
		return prices.isEmpty() ? null : prices.get(0);
	}

	public static String getGEP_Abbreviated_Displayed_Web_DescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> abbreviatedDesc = ctx.read("$.Descriptions[*].Abbreviated_Display_Description");
		return abbreviatedDesc.isEmpty() ? null : abbreviatedDesc.get(0);
	}

	public static String getProductDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> productDesc = ctx.read("$.Descriptions[*].Product_Description");
		return productDesc.isEmpty() ? null : productDesc.get(0);
	}

	public static String getSearchDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> searchDesc = ctx.read("$.Descriptions[*].Search_Description");
		return searchDesc.isEmpty() ? null : searchDesc.get(0);
	}

	public static String getDetailDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> detailDesc = ctx.read("$.Descriptions[*].['Detail Description']");
		return detailDesc.isEmpty() ? null : detailDesc.get(0);
	}

	public static String getTechnicalDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> technicalDesc = ctx.read("$.Descriptions[*].Technical_Description");
		return technicalDesc.isEmpty() ? null : technicalDesc.get(0);
	}
	public static String getLookAheadSearchDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> lookAheadSearchDesc = ctx.read("$.Descriptions[*].Look_Ahead_Search_Description");
		return lookAheadSearchDesc.isEmpty() ? null : lookAheadSearchDesc.get(0);
	}

	public static String getExtendedWebDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> extendedWebDesc = ctx.read("$.Descriptions[*].Extended_Web_Description");
		return extendedWebDesc.isEmpty() ? null : extendedWebDesc.get(0);
	}

	public static String getPrintCatalogDescriptionFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> printCatalogDesc = ctx.read("$.Descriptions[*].Print_Catalog_Description");
		return printCatalogDesc.isEmpty() ? null : printCatalogDesc.get(0);
	}

	public static List<String> getMediaURLFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> mediaURL = ctx.read("$.Media[*].URL");
		return mediaURL.isEmpty() ? null : mediaURL;
	}

	public static List<String> getMediaMimeTypFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> mediaMimeType = ctx.read("$.Media[*].MIME_Type");
		return mediaMimeType.isEmpty() ? null : mediaMimeType;
	}
	public static List<String> getDocumentIDFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		List<String> mediaDocumentId = ctx.read("$.Media[*].Document_Id");
		return mediaDocumentId.isEmpty() ? null : mediaDocumentId;
	}
	public static String getGTINFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);
		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		// Fetch GTIN
		List<String> gtin = ctx.read("$.GTIN[*].GTIN");
		return gtin.isEmpty() ? null : gtin.get(0);
	}

	public static String getFederalDrugClassCodeFromIPIM_Json(String jsonContent, String itemNumber) {
		ReadContext ctx = JsonPath.parse(jsonContent);

		// Ensure Product_Id matches
		if (!itemNumber.equals(ctx.read("$.Product_Id", String.class))) return null;
		// fetch Class_Code_Id where Type = FDRL_DRG_CLS_CD
		List<String> classCodeIds = ctx.read("$.Class_Codes_ID[?(@.Type=='FDRL_DRG_CLS_CD')].Class_Code_Id");
		return classCodeIds.isEmpty() ? null : classCodeIds.get(0);
	}

	@FunctionalInterface
	public interface JsonProcessor {
		boolean process(String jsonContent, LocalDateTime rowTime) throws Exception;
	}

	public static void validateAllArrayFieldsPresent(String jsonContent) {
		ReadContext ctx = JsonPath.parse(jsonContent);
// Define required array fields
		String[] requiredArrays = {
				"GTIN",
				"Class_Codes_ID",
				"Dimensions",
				"Descriptions",
				"Item_Category_ID",
				"Global_Messages_ID",
				"Non_Base-Catalog_to_Product_mapping",
				"Item_Classifications",
				"Product_References",
				"Media",
				"Prices",
				"WebPrices",
				"DivisionalPrices",
				"Competitors"
		};
// Check each field
		for (String arrayField : requiredArrays) {
			try {
				Object value = ctx.read("$." + arrayField);
				if (value == null) {
					throw new AssertionError("Missing array field: " + arrayField);
				}
				if (!(value instanceof List)) {
					throw new AssertionError("Field '" + arrayField + "' is not an array");
				}
				System.out.println("Array field present: " + arrayField);
				log(LogType.EXTENTANDCONSOLE, "Array field:: " + arrayField + ":: present");
			} catch (Exception e) {
				throw new AssertionError("Validation failed for array field: " + arrayField, e);
			}
		}
	}
}