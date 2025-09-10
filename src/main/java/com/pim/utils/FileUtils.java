package com.pim.utils;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.awt.Robot;
import java.nio.file.Files;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import com.pim.constants.Constants;
import com.pim.enums.LogType;


public class FileUtils {

	/**
	 * Name of the method: getFileType
	 * Description: Method to return type of file
	 * Author:Manisha
	 * Parameters: File path
	 */
	public static String getFileType(String file) {
		String fileExtension=null;
		String fileName = new File(file).getName();
		int fileIndex = fileName.lastIndexOf('.');
		if((fileIndex > 0))
		{
			fileExtension = fileName.substring(fileIndex + 1);
		}
		return fileExtension;
	}

	/**
	 * Name of the method: getImagePixelValue
	 * Description: Method to return pixel of file
	 * Author:Manisha
	 * Parameters: File path
	 */
	public static int getImagePixelValue(String file) {
		File input = new File(file);
		BufferedImage image = null;
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int height = image.getHeight();
		int width = image.getWidth();
		return height*width;	
	}
	
	/**
	 * Name of the method: Text size
	 * Description: Method to return Text size of file
	 * Author:Darshan
	 * Parameters: File path
	 */
	public static int gettextSize(String file) {
		File input = new File(file);

         int fileSize = (int) input.length();
         System.out.format("The size of the file: %d bytes", fileSize);
         return fileSize;   
    }
	
	/**
	 * Name of the method: uploadFile
	 * Description: Method to upload the file
	 * Author:Manisha
	 * Parameters: File path
	 */
	public static void uploadFile(String file) {
		Robot rb ;
		try {
			rb = new Robot();
			StringSelection str = new StringSelection(file);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
			rb.delay(250);
			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_ENTER);
			//rb.delay(90);
			rb.keyRelease(KeyEvent.VK_ENTER);

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Name of the method: getFileName
	 * Description: Method to return name of file Name
	 * Author:Manisha
	 * Parameters: File Name
	 */
	public static String getFileName(String file) {
		String[] filefullname = file.split("\\.");
		return filefullname[0];
	}
	
	/**
	 * Name of the method: copyFile
	 * Description: Method will copy from one location to another
	 * Author:Manisha
	 * Parameters: source and destination folder
	 */
	public static void renameAndCopyFile(String src, String dest) {
		Path result = null;
		try {
			result = Files.copy(Paths.get(src), Paths.get(dest));
		} catch (IOException e) {
			log(LogType.INFO,"Exception while moving file: " + e.getMessage());
			e.printStackTrace();
		}
		if(result != null) {
			log(LogType.INFO,"File moved successfully.");
		}else{
			log(LogType.INFO,"File movement failed.");
		}
	}

	/**
	 * Name of the method: deleteExistingFile
	 * Description: Method will delete the file if exists
	 * Author:Manisha
	 * Parameters: source path of the file
	 */
	public static void deleteExistingFile(String path) {
		try {
			Files.deleteIfExists(Paths.get(path));
		} catch (IOException e) {
			log(LogType.INFO,"Exception while deleting file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Name of the method: validImageInSizePath
	 * Description: method for good image path
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public static String validImageInSizePath(String imagename) {
		String path = Constants.getDamImagePath() + Constants.getDivision() +"\\" + getImageName(imagename);
		//String path = Constants.getDamImagePath() + Constants.getDivision() +"\\"+ imagename + "";
		return path;
	}

	/**
	 * Name of the method: photoStudioPath
	 * Description: method for photostudio path
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public static String photoStudioPath(String imagename) {
		String path = Constants.getHotfolder() + getImageName(imagename);
		return path;
	}
	
	public static String photoStudioPath_ForUS(String imagename) {
		String path = Constants.getHotfolderForUS() + getImageName(imagename);
		return path;
	}
	/**
	 * Name of the method: Planetpress
	 * Description: method for Planetpress path
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public static String planetPressPath(String imagename) {
		String path = Constants.planetPressFolder() + getImageName(imagename);
		return path;
	}

	/**
	 * Name of the method: invalidImageInSizePath
	 * Description: method for bad image path
	 * Author:Manisha
	 * Parameters: imagename and imagecategory
	 */
	public static String invalidImageInSizePath(String imagecategory, String imagename) {
		String path = Constants.getDamImagePath() + Constants.getDivision() +"\\"+ getImageCategory(imagecategory) + getImageName(imagename);
		return path;
	}
	
	/**
	 * Name of the method: getImageName
	 * Description: method for get image name from testdata
	 * Author:Manisha
	 * Parameters: imagename 
	 */
	public static String getImageName(String imagename) {
		return imagename + "";
	}
	
	/**
	 * Name of the method: getImageCategory
	 * Description: method for get image category form testdata
	 * Author:Manisha
	 * Parameters: imagecategory
	 */
	public static String getImageCategory(String imagecategory) {
		return imagecategory + "\\";
	}
	
	/**
	 * Name of the method: getTempImagePath
	 * Description: method for get path of image from temp folder
	 * Author:Manisha
	 * Parameters: imagecategory
	 */
	public static String getTempImagePath(String imagename) {
		String path = Constants.getImageTempPath() + getImageName(imagename);
		return path;
	}
	
	/**
	 * Name of the method: checkFileExists
	 * Description: method for checking
	 * Author:Manisha
	 * Parameters: image path
	 */
	public static void checkFileExists(String filepath) {
		//File file = new File(filepath); 
		Path path = Paths.get(filepath);
		if(Files.exists(path)) {
			log(LogType.INFO, "Image path exists");
		}else {
			log(LogType.INFO, "Image path not exists");
		}		
	}
	
	/**
	 * Name of the method: getNonProductImage
	 * Description: method to get Non Product Image
	 * Author:Manisha
	 * Parameters: imagename and imagecategory
	 */
	public static String getNonProductImage(String imagecategory, String imagename) {
		String path = Constants.getDamImagePath() + Constants.getDivision() +"\\"+ getImageCategory(imagecategory) + getImageName(imagename);
		return path;
	}
	
	/**
	 * Name of the method: deleteSpecificFile
	 * Description: method to delete file in network location
	 * Author:Darshan
	 * Parameters: filepath and filename
	 */
	public static void deleteSpecificFile(String filepath, String fileName) {

		File file = new File(filepath +"\\"+ fileName);

		try {
			if (file.delete()) {
				System.out.println("File deleted successfully");
			} else {
				System.out.println("Failed to delete the file");
			}
		} catch (NullPointerException e) {
			//throw new NullPointerForFileDownloadException("File is not founded in  folder");
		}
		return;
	}
	
	/**
	 * Name of the method: readindingAllFilesFromFolder
	 *  Description: Navigating to folder and Reading all the files from folder
	 *  Author:Darshan 
	 * Parameters: FilePath
	 */
	public static List<String> readindingFileNameFromFolder(String filepath) {
		File folder = new File(filepath);
		File[] listOfFiles = folder.listFiles();
		List<String> FileName = new ArrayList<String>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileName.add(file.getName());
			}
		}
		return FileName;
	}
	
	/**
	 * Name of the method: getStiboNonProductPath
	 * Description: method to get Stibo non product path
	 * Author:Manisha
	 * Parameters: 
	 */
	public static String getStiboNonProductPath() {
		String path = Constants.getStibofolder() + "Non-Product";
		return path;
	}
	
	/**
	 * Name of the method: getStiboProductPath
	 * Description: method to get Stibo product path
	 * Author:Manisha
	 * Parameters: 
	 */
	public static String getStiboProductPath() {
		String path = Constants.getStibofolder() + "Product";
		return path;
	}
	
	/**
	 * Name of the method: getSiteCorepath
	 * Description: method to get SiteCore path
	 * Author:Manisha
	 * Parameters: 
	 */
	public static String getSiteCorepath() {
		return Constants.getSiteCorefolder();
	}
	
	/**
	 * Name of the method: getMarketingAutomationPath
	 * Description: method to get Marketing Automation path
	 * Author:Manisha
	 * Parameters: 
	 */
	public static String getMarketingAutomationPath() {
		return Constants.getMarketingAutomationFolder();
	}
	
	/**
	 * Name of the method: photoStudioPathForUK
	 * Description: method for photostudio path for UK
	 * Author:Manisha
	 * Parameters: imagename
	 */
	public static String photoStudioPathForUK(String imagename) {
		String path = Constants.getUKHotFolder() + getImageName(imagename);
		return path;
	}

	
	/**
	 * Name of the method: getUKSiteCorePath
	 * Description: method get UK Site Core Path 
	 * Author:Manisha
	 * Parameters: 
	 */
	public static String getUKSiteCorePath(String product_nonproduct, String original_derivative) {
		return Constants.getUKSiteCorePath() + product_nonproduct + "\\" + original_derivative;
	}


	public static List<String> DownloadingDocumentsWithFileType() {
	File folder = new File(Constants.getTargetPath());
	File[] listOfFiles = folder.listFiles();
	List<String> extention = new ArrayList<String>();
	for (int i = 0; i < listOfFiles.length; i++) {
	  if (listOfFiles[i].isFile()) {
		  String FilesName = listOfFiles[i].getName();
		   extention.add(getFileType(FilesName));
	    System.out.println("File " + listOfFiles[i].getName());
	  } else if (listOfFiles[i].isDirectory()) {
	    System.out.println("Directory " + listOfFiles[i].getName());
	  }
	}
	return extention;
	
	}
	
	public static String downloadedtSDSFilePath(String FileName) {
		String SDSPath = Constants.getTargetPath() +"\\" + FileName;
		return SDSPath;
		
	}
	
	
	public static String massUploadPath(String imagename) {
		String path = Constants.getMassUploadPath() + getImageName(imagename);
		return path;
	}
	
	public static String massUploadPhotostudioPath(String imagename) {
		String path = Constants.getMassUploadPhotostudiPath() + getImageName(imagename);
		return path;
	}
	
    public static  List<String> getRequiredFileNamesFromGivenFolder(String directoryPath,int minusMin) {
        // Define the given time (for example, 30 minutes ago)
        LocalDateTime userGivenTime = LocalDateTime.now().minusMinutes(minusMin); // Modify as needed
 
        // Fetch JSON file names
        List<String> jsonFiles = getRequiredFilesModifiedAfter(directoryPath, userGivenTime);
 
        // Print the matching file names
        for (String fileName : jsonFiles) {
        	String differentFileNameWithPath = directoryPath+fileName;
            System.out.println(differentFileNameWithPath);
            System.out.println("");
        }
        
        return jsonFiles;
    }
 
    public static List<String> getRequiredFilesModifiedAfter(String directoryPath, LocalDateTime userGivenTime) {
        List<String> jsonFilesModifiedAfterGivenTime = new ArrayList<>();
 
        // Convert LocalDateTime to Instant for comparison
        Instant userGivenTimeInInstantFormat = userGivenTime.atZone(ZoneId.systemDefault()).toInstant();
 
        // Create a File object for the directory
        File file = new File(directoryPath);
        File[] listOfJsonFiles = file.listFiles((dir, name) -> name.endsWith(".json"));
 
        if (listOfJsonFiles != null) {
            for (File jsonFiles : listOfJsonFiles) {
                // Check the last modified date
                Instant lastModifiedFileTime = Instant.ofEpochMilli(jsonFiles.lastModified());
                if (lastModifiedFileTime.isAfter(userGivenTimeInInstantFormat)) {
                    // Add matching file name to the list
                	jsonFilesModifiedAfterGivenTime.add(jsonFiles.getName());
                }
            }
        }
 
        return jsonFilesModifiedAfterGivenTime;
    }
    
//    public static void imageComparision(String damImage, String escheinImage) {
//		String expectedImagePath = "C:\\Users\\Imran.Ansari\\Downloads\\ImageCompare\\"+damImage;
//		String actualImagePath = "C:\\Users\\Imran.Ansari\\Downloads\\ImageCompare\\"+escheinImage;
//		
//		File expectedImageFile = new File(expectedImagePath);
//		File actualImageFile = new File(actualImagePath);
//		
//		BufferedImage expectedImage;
//		try {
//			expectedImage = ImageIO.read(expectedImageFile);
//
//			BufferedImage actualImage = ImageIO.read(actualImageFile);
//
//			ImageDiffer imgDiff = new ImageDiffer();
//
//			ImageDiff diff = imgDiff.makeDiff(expectedImage, actualImage);
//
//			if (diff.hasDiff()) {
//				System.out.println("Images are not same");
//			}
//			else {
//				System.out.println("Images are same");
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

    
    public static void storeCurrentTimeInJsonTimesPropertiesFile(String keyForPropertiesFile) { 
    	LocalDateTime currentTime = LocalDateTime.now(); 
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	Properties properties = new Properties(); 
    	// Load existing properties if the file exists 
    	try (FileInputStream input = new FileInputStream(Constants.getJSONFilePath_Local() + "jsonTimes.properties")) { 
    		properties.load(input); 
    	} catch (IOException e) { 
    	// File might not exist yet, this is okay 
    	} 
    	// Set the new property value 
    	properties.setProperty(keyForPropertiesFile, currentTime.format(formatter));
    	// Save properties to the file 
    	try (FileOutputStream output = new FileOutputStream(Constants.getJSONFilePath_Local() + "jsonTimes.properties")) { 
    		properties.store(output, "Saved Date-Time Properties"); 
    		System.out.println("Current date-time saved in properties file.");
    	} catch (IOException e) {
    		e.printStackTrace(); 
    	} 
    }

    public static int calculateTimeDifference(String keyForPropertiesFile) { 
    	Properties properties = new Properties();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	try 
    	(FileInputStream input = new FileInputStream(Constants.getJSONFilePath_Local()+"jsonTimes.properties")) { 
    		properties.load(input); 
    	} catch (IOException e) {
    		e.printStackTrace(); 
    	} 
    	String oldDateTimeStr = properties.getProperty(keyForPropertiesFile, LocalDateTime.now().format(formatter));
		System.out.println("The saved time is " + oldDateTimeStr +"" );
    	LocalDateTime oldDateTime = LocalDateTime.parse(oldDateTimeStr, formatter);
    	LocalDateTime currentDateTime = LocalDateTime.now();
    	long differenceInMinutes = Duration.between(oldDateTime, currentDateTime).toMinutes();
		System.out.println("The saved time is " + oldDateTime +"" );
		System.out.println("The difference between the saved date-time and the current date-time is " + differenceInMinutes + " minutes.");
    	return (int) differenceInMinutes;
    }

	public static String getSavedTimestamp(String keyForPropertiesFile) {
		Properties properties = new Properties();
		String timestamp = null;
		try (FileInputStream input = new FileInputStream(Constants.getJSONFilePath_Local() + "jsonTimes.properties")) {
			properties.load(input);
			// Fetch value for given key
			timestamp = properties.getProperty(keyForPropertiesFile);

			if (timestamp == null || timestamp.trim().isEmpty()) {
				throw new RuntimeException("No timestamp found for key: " + keyForPropertiesFile);
			}

			System.out.println("The saved timestamp for key [" + keyForPropertiesFile + "] is " + timestamp);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return timestamp;
	}


    
}

