package com.pim.constants;

import com.pim.enums.ConfigProperties;
import com.pim.utils.PropertyFileRead;

import java.time.LocalDate;

public final class Constants {


	private Constants() {
	}

	private static final String RUNMODE = "local";
	public static final String LOCALE ="US";
//	private static final String RUNMODE = System.getProperty("runmode");
//	public static final String LOCALE = System.getProperty("locale");
	
	private static final int EXPLICIT_WAIT = 60;
	private static final String RESOURCES_PATH = System.getProperty("user.dir") + "/src/test/resources";
	private static final String PROPERTY_FILE_PATH = RESOURCES_PATH + "/config/";
	private static final String EXTENT_REPORT_FOLDER_PATH = System.getProperty("user.dir") + "/extent-test-output/";
	private static String extendReportFilePath = "";
	private static final String JSON_CONFIG_FILE_PATH = RESOURCES_PATH + "/config/config.json";
	private static final String EXCEL_PATH = RESOURCES_PATH + "\\excel\\Test.xlsx";
	private static final String DAM_EXCEL_PATH = RESOURCES_PATH + "\\excel\\";
	private static final String LOGINID_DATA_SHEET = "UserId";
	private static final String TEXT_PATH = RESOURCES_PATH + "\\excel\\WebDescription.txt";
	private static final String TEXT_PATH_FOR_SEO = RESOURCES_PATH + "\\excel\\SEO.txt";
	private static final String FilepathToUpload= System.getProperty("user.dir") + "\\src\\test\\resources\\canadaImages\\image7.jpeg";
	private static final String command = RESOURCES_PATH + "/canadaImages/FileUpload.exe "  + "\"" +  FilepathToUpload + "\"";
	private static final String FilepathToUploadSec= System.getProperty("user.dir") + "\\src\\test\\resources\\canadaImages\\SecondImage.png";
	private static final String commandSec = RESOURCES_PATH + "/canadaImages/FileUpload.exe "  + "\"" +  FilepathToUploadSec + "\"";
	private static final String FilepathToUploadThird= System.getProperty("user.dir") + "\\src\\test\\resources\\canadaImages\\ThirdImage.png";
	private static final String commandThird = RESOURCES_PATH + "/canadaImages/FileUpload.exe "  + "\"" +  FilepathToUploadThird + "\"";
	private static final String CSV_PATH = RESOURCES_PATH + "\\csv\\ItemMessages.csv";
	private static final String TEMP_CSV_PATH = RESOURCES_PATH + "\\csv\\Temp.csv";

	private static final String TARGET_PATH = "C:\\PIM\\DownloadDamImages\\";

	private static final String DAM_IMAGE_PATH = System.getProperty("user.dir") +  "\\src\\test\\resources\\DAMImages\\";
    private static final String HOT_FOLDER_LOCATION = "\\\\USCOLOMDMQA05\\IMMVolume\\BufferVolume\\Hotfolders\\1\\Photo_Studio_";
    private static final String STIBO_FOLDER_LOCATION = "\\\\USNYMEFS01.us.hsi.local\\corpshare\\MDM\\DAM\\Stibo\\QA\\";
    private static final String DAM_IMAGES_TEMP_PATH = System.getProperty("user.dir") +  "\\src\\test\\resources\\DAMImages\\temp\\";
    private static final String SITE_CORE_FOLDER_LOCATION = "\\\\USNYMEFS01.us.hsi.local\\corpshare\\MDM\\DAM\\Sitecore\\QA\\Archive\\";
    private static final String PLANETPRESS = "\\\\USCOLOMDMQA05.us.hsi.local\\PlanetPress\\";
    private static final String MARKETING_AUTOMATION_LOCATION = "\\\\USNYMEFS01.us.hsi.local\\corpshare\\MDM\\DAM\\Marketo\\QA\\";
	private static final String UK_HOT_FOLDER_PATH = "\\\\uscolomdmqa05.us.hsi.local\\IMMVolume\\BufferVolume\\Hotfolders\\1\\UK Folder\\in\\";
    private static final String SITE_CORE_PATH_UK = "\\\\USCOLOMDMQA05.us.hsi.local\\Sitecore\\UK\\";
    private static final String MASS_UPLOAD_LOCATION = "\\\\USCOLOMDMQA05.us.hsi.local\\IMMVolume\\BufferVolume\\Hotfolders\\1\\Mass_Upload\\";
    private static final String MASS_UPLOAD_PHOTOSTUDIO_LOCATION = "\\\\USCOLOMDMQA05.us.hsi.local\\IMMVolume\\BufferVolume\\Hotfolders\\1\\Mass_Upload\\";
	private static final String JSON_FILE_PATH = "\\\\Uscolomdmqa02\\";
	private static final String JSON_FILE_PATH_LOCAL = RESOURCES_PATH + "\\JSON\\" + LOCALE + "\\";

	public static String getPropertyFilePath() {
		return PROPERTY_FILE_PATH + "config.properties";
	}
	
	public static String getDatabasePropertyFilePath() {
		return PROPERTY_FILE_PATH + LOCALE + "/Database.properties";
	}

	public static int getExplicitWait() {
		return EXPLICIT_WAIT;
	}

	public static String getJsonconfigfilepath() {
		return JSON_CONFIG_FILE_PATH;
	}

	/**
	 * @return If Override reports value in the property file is no,then the
	 *         timestamp will be appended
	 */
	public static String getExtentReportFolderPath() {
		return EXTENT_REPORT_FOLDER_PATH;
	}

	private static String createReportPath() {
		if (PropertyFileRead.getPropValue(ConfigProperties.OVERRIDEREPORTS).equalsIgnoreCase("no")) {
			return EXTENT_REPORT_FOLDER_PATH + System.currentTimeMillis() + "/index.html";
		} else {
			return EXTENT_REPORT_FOLDER_PATH + "/index.html";
		}
	}

	public static String getExtentReportFilePath() {
		if (extendReportFilePath.isEmpty()) {
			extendReportFilePath = createReportPath();
		}
		return extendReportFilePath;
	}

	public static String getExcelPath() {
		return EXCEL_PATH;
	}
	
	public static String getDamExcelPath() {
		return DAM_EXCEL_PATH + LOCALE + "\\DamTestData.xlsx";
	}

	public static String getLoginDataSheet() {
		return LOGINID_DATA_SHEET;
	}
	
	public static String getTextFile() {
		return TEXT_PATH;
	}
	public static String getTextFileForSEO() {
		return TEXT_PATH_FOR_SEO;
	}

	public static String getCanadaImageFilePath() {
		System.out.println(command);
		return command;
	}

	
	public static String getFilePathToUpload() {
		System.out.println(command);
		return command;
	}
	
	public static String getItemMessagesCSVFilePath(){
		return CSV_PATH;
	}

	public static String getTempCSVFilePath(){
		return TEMP_CSV_PATH;

	}
	public static String getRunMode(){
		return RUNMODE;
	}
	
	public static String getTargetPath() {
		return TARGET_PATH;
	}
	

	public static String getDamImagePath() {
		return DAM_IMAGE_PATH;
	}
	
	public static String getDivision() {
		return LOCALE;
	}
	
	public static String getHotfolder() {
		return HOT_FOLDER_LOCATION + LOCALE + "\\in\\";
	}
	
	public static String getHotfolderForUS() {
		return HOT_FOLDER_LOCATION + "US" + "\\in\\";
	}
	public static String getStibofolder() {
		return STIBO_FOLDER_LOCATION;
	}
	
	public static String getImageTempPath() {
		return DAM_IMAGES_TEMP_PATH;
	}
	
	public static String getSiteCorefolder() {
		return SITE_CORE_FOLDER_LOCATION;
	}
	
	public static String planetPressFolder() {
		return PLANETPRESS + LOCALE + "\\";
	}
	
	public static String getMarketingAutomationFolder() {
		return MARKETING_AUTOMATION_LOCATION;
	}
	
	public static String getUKHotFolder() {
		return UK_HOT_FOLDER_PATH;
	}
	
	public static String getUKSiteCorePath(){
		return SITE_CORE_PATH_UK;
	}
	
	public static String getMassUploadPath() {
		return MASS_UPLOAD_LOCATION + LOCALE + "_P_Mass_Upload\\in\\";
	}
	
	public static String getMassUploadPhotostudiPath() {
		return MASS_UPLOAD_PHOTOSTUDIO_LOCATION + LOCALE + "_P_Mass_Upload_Photo_Studio\\in\\";
	}	
	
	public static String getJSONFilePath_Local() {
		return JSON_FILE_PATH_LOCAL;
	}	
	
	public static String getJSONFilePath_Archive() {
		LocalDate currentDate = LocalDate.now();
		String year = String.valueOf(currentDate.getYear());
		String month = String.format("%02d", currentDate.getMonthValue());
		return "Y:\\US\\Archive\\" + year + "\\" + month + "\\";
	}
	public static String getJSONFilePath_ArchiveCA() {
		LocalDate currentDate = LocalDate.now();
		String year = String.valueOf(currentDate.getYear());
		String month = String.format("%02d", currentDate.getMonthValue());
		return "Y:\\CA\\Archive\\" + year + "\\" + month + "\\";
	}
	
}