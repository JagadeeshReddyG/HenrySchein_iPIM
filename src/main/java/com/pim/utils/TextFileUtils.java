package com.pim.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.pim.constants.Constants;

public class TextFileUtils {

	private TextFileUtils() {

	}

	//writing in txt file
	public static void writeTextFile(String description) {
		try {
			// create a writer
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.getTextFile()));

			// write text to file
			bw.write(description);

			// close the writer
			bw.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//reading the txt file
	public static String readTextFile(){

		String str = "";
		// Creating an object of BufferedReader class
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(Constants.getTextFile()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Declaring a string variable
		String st;

		try {
			while ((st = br.readLine()) != null) { 
				str = str+st;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;


	}
	
	//writing in txt file
	public static void writeTextFileforSEO(String description) {
		try {
			// create a writer
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.getTextFileForSEO()));

			// write text to file
			bw.write(description);

			// close the writer
			bw.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//reading the txt file
	public static String readTextFileForSEO(){

		String str = "";
		// Creating an object of BufferedReader class
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(Constants.getTextFileForSEO()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Declaring a string variable
		String st;

		try {
			while ((st = br.readLine()) != null) { 
				str = str+st;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;


	}
}
