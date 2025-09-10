package com.pim.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class CSVUtils {

    public List<String[]> getCSVFileData(String filename){
        List<String[]> data = null;
        try {
            FileReader ItemMessageFile = new FileReader(new File(filename));
            CSVReader read = new CSVReader(ItemMessageFile);
            data = read.readAll();
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Issue while reading CSV file "+ filename);
        }

        return data;
    }

    public void writeDataTOCsv(String filename, List<String[]> data){
        FileWriter file = null;
        try {
            file = new FileWriter(new File(filename));
            CSVWriter writer = new CSVWriter(file);
            writer.writeAll(data);
            writer.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Issue while writing CSV file "+ filename);
        }
    }

    public void deleteFile(String filename){
        File file = new File(filename);
        file.delete();
    }

    public void renameFile(String currentFilename, String newFilename){
        File currentFile = new File(currentFilename);
        File newFile = new File(newFilename);
        currentFile.renameTo(newFile);
    }


}