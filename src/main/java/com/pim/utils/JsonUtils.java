package com.pim.utils;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pim.enums.ConfigProperties;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

    private static Map<String, String> CONFIGMAP;

    private JsonUtils() {

    }

//    static {
//        try {
//            CONFIGMAP = new ObjectMapper().readValue(new File(Constants.getJsonconfigfilepath()),
//                    new TypeReference<HashMap<String,String>>() {});
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static String get(ConfigProperties key) throws Exception {
        if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key.name().toLowerCase()))) {
            throw new Exception("Property name " + key + " is not found. Please check config.properties");
        }
        return CONFIGMAP.get(key.name().toLowerCase());
    }
    
    private static JsonObject jsonObject;
    
    // Constructor to load JSON from file
    public JsonUtils(String filePath) throws IOException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(filePath));
        if (jsonElement.isJsonObject()) {
            jsonObject = jsonElement.getAsJsonObject();
        }
    }

    // Get all Values Present Inside Parent Key
    public JsonArray getValuesInsideParentKey(String parentKey) {
        return jsonObject.getAsJsonArray(parentKey);
    }
    
    // Get Child String Value
    public String requiredChildStringValue(JsonObject jsonObject, String childKey) {
    	JsonElement stringValues = jsonObject.has(childKey) ? jsonObject.get(childKey) : null;
        if (stringValues != null) {
            return stringValues.getAsString();
            }
        return null;
    }
    
    // Get Specific String Value Present Inside Child Array
    public String requiredValueInsideChildArray(JsonObject jsonObject, String childKey, String subChildKey) {
    	JsonArray arrayValues = jsonObject.has(childKey) ? jsonObject.getAsJsonArray(childKey) : null;
        if (arrayValues != null) {
            for (JsonElement descriptionElement : arrayValues) {
                JsonObject arrayValue = descriptionElement.getAsJsonObject();
                if (arrayValue.has(subChildKey)) {
                    return arrayValue.get(subChildKey).getAsString();
                }
            }
        }
        return null;
    }
    
    // Get Specific String Value Present Inside Sub Child Array
    public String requiredValueInsideSubChildArray(JsonObject jsonObject, String childKey, String subChildKey, String subSubChildKey) {
    	JsonArray valueInsideChild = jsonObject.has(childKey) ? jsonObject.getAsJsonArray(childKey) : null;

    	for (JsonElement childArrayValue : valueInsideChild) {
			JsonObject childObject = childArrayValue.getAsJsonObject();
			String value = requiredValueInsideChildArray(childObject, subChildKey, subSubChildKey);
	    	return value;
    	}
    	return null;
    }
    
    
    
    public static void getAnySubChildArrayValueFromJson(String filePath, String targetItemNumber, String parentKey, String childKey, String subChildKey) {
    	String requiredValueInsideChildArr;
    	try {
        	JsonUtils jsonUtils = new JsonUtils(filePath);

            // Get all data from the JSON
            JsonArray allData = jsonUtils.getValuesInsideParentKey(parentKey);
            
            if (allData != null) {
                boolean productFound = false;  // Flag to check if the product was found
                
                for (JsonElement productElement : allData) {
                    JsonObject jsonObject = productElement.getAsJsonObject();

                    // Get the Product ID
                    String productId = jsonObject.get("Product_Id").getAsString();

                    // Check if the current product ID matches the target
                    if (productId.equals(targetItemNumber)) {
                        productFound = true;  // Set flag to true when product is found
                        System.out.println("Product ID = " + productId);

                        // Call getProductDescription for Full_Display_Description
                        requiredValueInsideChildArr = jsonUtils.requiredValueInsideChildArray(jsonObject, childKey, subChildKey);

                        // Check if Full_Display_Description exists
                        if (requiredValueInsideChildArr != null) {
                            System.out.println(targetItemNumber + " = " + requiredValueInsideChildArr);
                        } else {
                            System.out.println(subChildKey + " not found for product " + productId);
                        }
                        break;
                    }
                }
                if (!productFound) {
                    System.out.println("Product with ID " + targetItemNumber + " not found.");
                }
            } else {
                System.out.println("No products found in the JSON.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void getAnyChildStringValueFromJson(String filePath, String targetItemNumber, String parentKey, String childKey) {
    	String requiredStringValue;
    	try {
        	JsonUtils jsonUtils = new JsonUtils(filePath);
            // Get all data from the JSON
            JsonArray allData = jsonUtils.getValuesInsideParentKey(parentKey);
            if (allData != null) {
                boolean productFound = false;  // Flag to check if the product was found
                
                for (JsonElement productElement : allData) {
                    JsonObject jsonObject = productElement.getAsJsonObject();
                    // Get the Product ID
                    String productId = jsonObject.get("Product_Id").getAsString();

                    // Check if the current product ID matches the target
                    if (productId.equals(targetItemNumber)) {
                        productFound = true;  // Set flag to true when product is found
                        System.out.println("Product ID = " + productId);

                        // Call getProductDescription for Full_Display_Description
                        requiredStringValue = jsonUtils.requiredChildStringValue(jsonObject, childKey);

                        // Check if Full_Display_Description exists
                        if (requiredStringValue != null) {
                            System.out.println(childKey + " for "+targetItemNumber+" = "+requiredStringValue);
                        } else {
                            System.out.println(childKey + " not found for product " + productId);
                        }
                        break;
                    }
                }
                
                if (!productFound) {
                    System.out.println("Product with ID " + targetItemNumber + " not found.");
                }

            } else {
                System.out.println("No products found in the JSON.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void getDataFromJSONFilesUpdatedUnderGivenTime(String folderDirectoryPath, String itemNumber ) {
//    	String folderDirectoryPath = "C:\\WORKING FOLDER\\Demo Projects\\Demo\\src\\test\\resources\\config\\";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, 20);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
    		getAnySubChildArrayValueFromJson(resourceFilePath, itemNumber, "Products", "Descriptions", "Full_Display_Description");
    		getAnyChildStringValueFromJson(resourceFilePath, itemNumber, "Products", "Display_Price");


    		getAnySubChildArrayValueFromJson(resourceFilePath, "7773857", "Products", "Descriptions", "Full_Display_Description");
    		getAnySubChildArrayValueFromJson(resourceFilePath, "7773857", "Products", "Item_Category_ID", "Category_Id");
    		getAnyChildStringValueFromJson(resourceFilePath, "7773857", "Products", "Display_Price");
    	}
    	
	}
    
    public static void getAdditionalAssets_Data_Inside_AdditionalProductInfo_FromJSONFilesUpdatedUnderGivenTime(int time, String itemNumber, String folderDirectoryPath) {
//    	String folderDirectoryPath = "C:\\WORKING FOLDER\\Demo Projects\\Demo\\src\\test\\resources\\config\\";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
//    		getAnySubChildArrayValueFromJson(resourceFilePath, itemNumber, "Products", "Media", "MIME_Type");
//    		getAnySubChildArrayValueFromJson(resourceFilePath, itemNumber, "Products", "AdditionalProductInfo", "AdditionalDescription");
//    		getAnySubChildArrayValueFromJson(resourceFilePath, itemNumber, "Products", "Prices", "Price");
//    		getAnyChildStringValueFromJson(resourceFilePath, itemNumber, "Products", "Display_Price");

    	}
    	
	}
    
    
    
    
    
    

    
    public static String getAnySubSubChildArrayValueFromJson(String filePath, String targetItemNumber, String parentKey, String childKey, String subChildKey, String subSubChildArray) {
    	String productId="";
    	String requiredValueInsideChildArr;
    	try {
        	JsonUtils jsonUtils = new JsonUtils(filePath);

            // Get all data from the JSON
            JsonArray allData = jsonUtils.getValuesInsideParentKey(parentKey);
            
            if (allData != null) {
                boolean productFound = false;  // Flag to check if the product was found
                
                for (JsonElement productElement : allData) {
                    JsonObject jsonObject = productElement.getAsJsonObject();

                    // Get the Product ID
                    productId = jsonObject.get("Product_Id").getAsString();

                    // Check if the current product ID matches the target
                    if (productId.equals(targetItemNumber)) {
                        productFound = true;  // Set flag to true when product is found
                        System.out.println("Product_ID = " + productId+ " is available in JSON File.");

                        requiredValueInsideChildArr = jsonUtils.requiredValueInsideSubChildArray(jsonObject, childKey, subChildKey, subSubChildArray);

                        // Check if Full_Display_Description exists
                        if (requiredValueInsideChildArr != null) {
                            System.out.println(targetItemNumber + " = " + requiredValueInsideChildArr);
                        } else {
                            System.out.println(subSubChildArray + " not found for product " + productId);
                        }
                        break;
                    }
                }
                if (!productFound) {
                    System.out.println("Product with ID " + targetItemNumber + " not found.");
                }
            } else {
                System.out.println("No products found in the JSON.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productId;

    }
    public static void getNameOfAssetFromJSONOfGivenAsset() {
    	String folderDirectoryPath = ".\\src\\test\\resources\\JSON\\";
    	int time = 20;
//    	String name="";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
    	String name = getAnySubSubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "AdditionalProductInfo", "AdditionalDescription", "Name");
//    	getAnySubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "Prices", "UOM");
    	System.out.println(name);
    	}
//    	return name;
    }
    
    public static void getProductIDOfAssetFromJSONOfGivenAsset() {
    	String folderDirectoryPath = ".\\src\\test\\resources\\JSON\\";
    	int time = 20;
//    	String name="";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
//    	String name = getAnySubSubChildArrayValueFromJson(folderDirectoryPath, "8580026", "Products", "AdditionalProductInfo", "AdditionalDescription", "Name");
//    	getAnySubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "Prices", "UOM");
//    	System.out.println(name);
    		getAnyChildStringValueFromJson(resourceFilePath, "8580026", "Products", "Base_Product_Id");
    	}
//    	return name;
    }
    
    public static void getFullDisplayDesc_OfAssetFromJSONOfGivenAsset() {
    	String folderDirectoryPath = ".\\src\\test\\resources\\JSON\\";
    	int time = 20;
//    	String name="";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
//    	String name = getAnySubSubChildArrayValueFromJson(folderDirectoryPath, "8580026", "Products", "AdditionalProductInfo", "AdditionalDescription", "Name");
//    	getAnySubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "Prices", "UOM");
//    	System.out.println(name);
    		getAnySubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "Descriptions", "Full_Display_Description");
    	}
//    	return name;
    }
    
    public static void getURLInsideMedia_OfAssetFromJSONOfGivenAsset() {
    	String folderDirectoryPath = ".\\src\\test\\resources\\JSON\\";
    	int time = 20;
//    	String name="";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
//    	String name = getAnySubSubChildArrayValueFromJson(folderDirectoryPath, "8580026", "Products", "AdditionalProductInfo", "AdditionalDescription", "Name");
//    	getAnySubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "Prices", "UOM");
//    	System.out.println(name);
    		getAnySubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "Media", "URL");
    	}
//    	return name;
    }
    
    //Working fine
    public static void getDescription_UnderAdditionalDescription_UnderAdditionalProductInfo_OfAssetFromJSONOfGivenAsset() {
    	String folderDirectoryPath = ".\\src\\test\\resources\\JSON\\";
    	int time = 200;
//    	String name="";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
    		getAnySubSubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "AdditionalProductInfo", "AdditionalDescription", "Description");
    	}
//    	return name;
    }
    
    // Not Working
    public static String getDocumentID_UnderAdditionalAsset_UnderAdditionalProductInfo_OfAssetFromJSONOfGivenAsset() {
    	String folderDirectoryPath = ".\\src\\test\\resources\\JSON\\";
    	int time = 200;
    	String name="";
    	FileUtils fileUtils = new FileUtils();
    	List<String> filesNameList = fileUtils.getRequiredFileNamesFromGivenFolder(folderDirectoryPath, time);
    	for(int i = 0; i<filesNameList.size(); i++) {
    		String fileName = filesNameList.get(i).trim();
    		String resourceFilePath = folderDirectoryPath+fileName;
    		getAnySubSubChildArrayValueFromJson(resourceFilePath, "8580026", "Products", "AdditionalProductInfo", "AdditionalAssets", "Document_Id");
    	}
    	return name;
    }
}