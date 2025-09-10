package com.pim.utils;
import static com.pim.reports.FrameworkLogger.log;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

import com.pim.enums.LogType;

public class ImageDownloader {
    public static void downloadImage(String imageUrl, String outputPath) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void downloadImageFromGivenURLInGivenPath(String URL, String pathWithImageName) {
        try {
            String imageUrl = URL;  // Replace with actual image URL
            String outputPath = pathWithImageName;
            downloadImage(imageUrl, outputPath);
    		log(LogType.EXTENTANDCONSOLE, "Image downloaded to " + outputPath); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void createFolderInGivenPath(String folderPath) {
    	File folder = new File(folderPath);
    	  // Check if the folder already exists
        if (!folder.exists()) {
            // Create the folder
            boolean created = folder.mkdirs();

            if (created) {
        		log(LogType.EXTENTANDCONSOLE,"Folder created successfully."); 
            } else {
        		log(LogType.EXTENTANDCONSOLE,"Failed to create the folder."); 
            }
        } else {
    		log(LogType.EXTENTANDCONSOLE,"Folder already exists."); 
        }
    }
    
}
