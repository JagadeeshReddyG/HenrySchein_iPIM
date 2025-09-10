package com.pim.utils;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;

import com.google.common.base.CharMatcher;
import com.pim.enums.ConfigProperties;
import com.pim.enums.LogType;

public class Javautils {
	public Javautils() {
	}

	public List<String> readMultipleValuesFromExcel(String datafromexcel) {
		List<String> list = new ArrayList<>();
		if (datafromexcel.contains(",")) {
			String[] multipledata = datafromexcel.split(",");
			for (String data : multipledata) {
				list.add(data);
			}
		} else {
			list.add(datafromexcel);
		}
		return list;
	}

	public static Object compareList(List<String> list1, List<String> list2) {

		Object value = true;
		List<String> temp = new ArrayList<>();
		for (String object : list1) {

			if (!list2.contains(object)) {
				temp.add(object);
			}
		}
		if (!temp.isEmpty()) {
			temp.add(0, "Below Values are not found during comparision");
			value = temp;
		}

		return value;

	}

	public static void validateFirstLetterOfEveryWordIsInUppercase(String str) {
		String words[] = str.split(" ");
		for (int i = 0; i < words.length; i++) {
			String s = words[i];
			log(LogType.EXTENTANDCONSOLE, s.charAt(0)+"");
			if(s.charAt(0)>=65 && s.charAt(0)<=90 || s.charAt(0)>=97 && s.charAt(0)<=122) {
			Assert.assertEquals(true, Character.isUpperCase(s.charAt(0)));
			}
			log(LogType.EXTENTANDCONSOLE, "validating first letter of every word is in Uppercase-----Assert passed");
		}
	}

	public static void validateFirstLetterOfEveryWordIsInLowercase(String str) {

		String words[] = str.split(" ");
		for (int i = 0; i < words.length; i++) {
			String s = words[i];
			log(LogType.EXTENTANDCONSOLE, s.charAt(0)+"");
			if(s.charAt(0)>=65 && s.charAt(0)<=90 || s.charAt(0)>=97 && s.charAt(0)<=122) {
			Assert.assertEquals(true, Character.isLowerCase(s.charAt(0)));
			}
			log(LogType.EXTENTANDCONSOLE, "validating first letter of every word is in Lowercase-----Assert passed");
		}
	}

	public static void validateSpecialCharacterAvailability(String str) {

		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(str);
		System.out.println("\nYour string capture value is : " + str);
		int count = 0;

		while (match.find()) {

			count = count + 1;

			System.out.println("\tposition " + match.start() + ": " + str.charAt(match.start()));

		}

		System.out.println("\nThere are " + count + " special characters");
		Assert.assertEquals(true, count >= 1);
		log(LogType.EXTENTANDCONSOLE, "validating special characters availability  -----Assert passed");
	}

	public static void validateSpecialCharacterNon_Availability(String str) {

		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(str);
		System.out.println("\nYour string capture value is : " + str);
		int count = 0;

		while (match.find()) {

			count = count + 1;

			System.out.println("\tposition " + match.start() + ": " + str.charAt(match.start()));

		}

		System.out.println("\nThere are " + count + " special characters");
		Assert.assertEquals(true, count == 0);
		log(LogType.EXTENTANDCONSOLE, "validating special characters Non-Availability  -----Assert passed");
	}

	public static void validateMaximumLengthOf512Characters(String str) {

		int stringLength = str.length();
		System.out.println("maxm_length: " + stringLength);
		Assert.assertEquals(false, stringLength > 512);
		log(LogType.EXTENTANDCONSOLE, "validating maximum length of 512 characters   -----Assert passed");

	}

	public static void validateLengthOfNotEqualTo512Characters(String str) {

		int stringLength = str.length();
		System.out.println("maxm_length: " + stringLength);
		Assert.assertEquals(true, stringLength < 512);
		log(LogType.EXTENTANDCONSOLE, "validating maximum length Not equal to 512 characters   -----Assert passed");

	}

	public static void validateMaximumLengthOf80Characters(String str) {

		int stringLength = str.length();
		System.out.println("maxm_length: " + stringLength);
		Assert.assertEquals(80, stringLength);
		log(LogType.EXTENTANDCONSOLE, "validating maximum length of 80 characters   -----Assert passed");

	}

	public static void validateLengthOfNotEqualTo80Characters(String str) {

		int stringLength = str.length();
		System.out.println("maxm_length: " + stringLength);
		Assert.assertEquals(true, stringLength <= 80);
		log(LogType.EXTENTANDCONSOLE, "validating maximum length Not equal to 80 characters   -----Assert passed");

	}

	public static void selectAllAndDeletingThroughKeboard() {
		Robot r;
		try {
			r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_A);
			r.keyRelease(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_BACK_SPACE);
		} catch (AWTException e) {

			e.printStackTrace();
		}
	}

	// spilting based on space.
	public static String[] splitbasedonspace(String str) {
		String[] spiltedstring = str.split(" ");
		return spiltedstring;
	}

	// replacing underscore by space
	public static String changeunderscoretospace(String str) {
		return str.replaceAll("_", " ");
	}
	
	//Random Numbers generate
	public static int randomNumber () {
        double doubleRandomNumber = Math.random() * 10;
        int randomNumber = (int)doubleRandomNumber;
        return randomNumber;
    }

	//To return first character of the string
	public static String getFirstLetter(String text){
		String first = text.substring(0, 1);
		return  first;
	}
	public static String getFilePathToUploadSingleFile(String FilepathToUpload) {
		String RESOURCES_PATH=System.getProperty("user.dir") + "\\src\\test\\resources";
		String command=RESOURCES_PATH + "\\canadaImages\\FileUpload.exe "  + "\"" +  FilepathToUpload + "\"";
		System.out.println(command);
		return command;
	}
	
	/**
	 * Name of the method: getIntValueFromString
	 * Description: Method to convert string value to int
	 * Author:Manisha
	 * Parameters: String value
	 */
	public static int getIntValueFromString(String value) {
		int intValue = Integer.parseInt(value);
		return intValue;
	}
	
	
	/**
	 * Name of the method: getIntValueFromString
	 * Description: Method to convert string value to int
	 * Author:Darshan
	 * Parameters: String value
	 */
	public static String getIntegerValueFromString(String value) {
		String intValue = CharMatcher.digit().retainFrom(value);
		return intValue;
	}
	
	/**
	 * Name of the method: spiltbasedonundrscore
	 * Description: Method to split the string based on underscore
	 * Author:Manisha
	 * Parameters: String value
	 */
	public static String[] spiltbasedonundrscore(String data){
		String[] multipledata = data.split("_");
		return multipledata;
	}
	
	/**
	 * Name of the method: verifyStringContainOnlyDigit
	 * Description: Method to verify that string contains only digits
	 * Author:Manisha
	 * Parameters: String value
	 */
	public static boolean verifyStringContainOnlyDigit(String data) {
		// contains only digits
        String regex = "[0-9]+";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // using Pattern.matcher()
        Matcher m = p.matcher(data);
        return m.matches();
	}
	
	/**
	 * Name of the method: generateSevenDigitRandomNumber
	 * Description: Method to generate seven digit random number starting with 3 
	 * Author:Manisha
	 * Parameters: 
	 */
	public static int generateSevenDigitRandomNumber() {
//		Random random = new Random();
		int randomnumber = /**2000000 +**/ ThreadLocalRandom.current().nextInt(1000000, 9999999);
		return randomnumber;
	}
	
	/**
	 * Name of the method: getStringFromIntValue
	 * Description: Method to convert int value to string
	 * Author:Manisha
	 * Parameters: int value
	 */
	public static String getStringFromIntValue(int value) {
		String string = Integer.toString(value);
		return string;
	}
	
	/**
	 * Name of the method: getIntegerFromString
	 * Description: Method to convert string value to int
	 * Author:Manisha
	 * Parameters: string value
	 */
	public static String getIntegerFromString(String value) {
		String number = CharMatcher.digit().retainFrom(value);
		return number;
	}

	/**
	 * Name of the method: generateRandomNumberAndVerifyInDB
	 * Description: Method to generate seven digit random number and verify whether this number is exists on PIM/DB
	 * Author:Manisha
	 * Parameters: 
	 */
	public static int generateRandomNumberAndVerifyInDB() {
		int randomnumber = 0;
		DatabaseUtilitiies.dbSetup(PropertyFileRead.getPropValue(ConfigProperties.db_url), PropertyFileRead.getPropValue(ConfigProperties.db_user), PropertyFileRead.getPropValue(ConfigProperties.db_password));
		boolean flag = true;
		while(flag) {
			randomnumber = generateSevenDigitRandomNumber();
			ResultSet res = DatabaseUtilitiies.getQueryResult("SELECT ITEMCODE as code FROM [ECom_Build].[dbo].[itemmast] where ITEMCODE ='"+randomnumber+"' UNION  SELECT ITEMNO as code FROM [Extract].[dbo].[PIX20] where ITEMNO = '"+randomnumber+"' UNION  SELECT ITEMCODE as code FROM [ECom_build_CA].[dbo].[ItemExt] where ITEMCODE = '"+randomnumber+"'");
			try {
				if (!res.isBeforeFirst() ) {    
					System.out.println("No data"); 
					flag=false;
				}
			} catch (SQLException e) {
				log(LogType.INFO, e.getMessage());
				e.printStackTrace();
			} 
		}
		log(LogType.EXTENTANDCONSOLE, randomnumber + " random");
		DatabaseUtilitiies.closingConnection();
		return randomnumber;
	}
	
	/**
	 * Name of the method: getItemNumberforCAFromDB
	 * Description: Method to get item code which is existing in US DB but not in CA DB
	 * Author:Manisha
	 * Parameters: 
	 */
	public static String getItemNumberforCAFromDB() {
		String item =null;
		DatabaseUtilitiies.dbSetup(PropertyFileRead.getPropValue(ConfigProperties.db_url), PropertyFileRead.getPropValue(ConfigProperties.db_user), PropertyFileRead.getPropValue(ConfigProperties.db_password));
		//DatabaseUtilitiies.dbSetup("jdbc:sqlserver://uscolomdmqa01.us.hsi.local", "QADemo", "QADemo2018");
		ResultSet res = DatabaseUtilitiies.getQueryResult("select top(1) ITEMCODE from [ECom_Build].[dbo].[itemmast] where IMAGE is not null and LEN(IMAGE) > 14 and ITEMCODE not in (select ITEMCODE from [ECom_Build_CA].[dbo].[itemmast]) ORDER BY NEWID()");
		try {
			while(res.next()) {
			item = res.getString("ITEMCODE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item;
	}	
	
	/**
	 * Name of the method: spiltbasedonnospace
	 * Description: Method to split the string based on on space
	 * Author:Manisha
	 * Parameters: String value
	 */
	public static String[] spiltbasedoncolon(String data){
		String[] multipledata = data.split(":");
		return multipledata;
	}
	
	/**
	 * Name of the method: isNumeric
	 * Description: Method to verify the number
	 * Author:Manisha
	 * Parameters: Numeric
	 */
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
}

