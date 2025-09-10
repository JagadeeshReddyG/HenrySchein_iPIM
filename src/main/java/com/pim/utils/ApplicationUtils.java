package com.pim.utils;

import com.pim.constants.Constants;
import com.pim.enums.LogType;

import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationUtils {
	

	CSVUtils csvUtils = new CSVUtils();
	
	public static String getTaxonomyId(String text) {
		String number = "";
		Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(text);
        while(m.find()) {
            number = number + m.group();
        }
    	return number;
	}

	//formatted Output: 3000 - 650 - 40
	public static String getFormattedTaxonomyId(String text) {
		List<String> numbers = new ArrayList<>();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(text);
		while (m.find()) {
			numbers.add(m.group());
		}
		if (numbers.isEmpty()) {
			return "";
		}
		return String.join("-", numbers);
	}

	public static  String getLastWord(String text){
		String lastWord = text.substring(text.lastIndexOf(" ")+1);
		return lastWord;
	}

	public static  String getLastTwoWord(String text){
		String[] words = text.split(" ");
		String lastTwoWords = words[(words.length-2)]+" "+words[(words.length-1)];
		return lastTwoWords;
	}

	public void updateMessageInCSV(String division, String action, String type, String message){
		List<String[]> data = csvUtils.getCSVFileData(Constants.getItemMessagesCSVFilePath());

		for(int i=1; i<data.size(); i++){
			String[] values = data.get(i);
			if(values[0].equalsIgnoreCase(division) &&
					values[1].equalsIgnoreCase(action) &&
					values[2].equalsIgnoreCase(type)){
				data.get(i)[3] = message;
				break;
			}
		}
		csvUtils.writeDataTOCsv(Constants.getTempCSVFilePath(), data);
		csvUtils.deleteFile(Constants.getItemMessagesCSVFilePath());
		csvUtils.renameFile(Constants.getTempCSVFilePath(), Constants.getItemMessagesCSVFilePath());
		csvUtils.deleteFile(Constants.getTempCSVFilePath());
	}

	public String getMessageFromCSV(String division, String action, String type){
		List<String[]> data = csvUtils.getCSVFileData(Constants.getItemMessagesCSVFilePath());
		String message = "";

		for(int i=1; i<data.size(); i++){
			String[] values = data.get(i);
			if(values[0].equalsIgnoreCase(division) &&
					values[1].equalsIgnoreCase(action) &&
					values[2].equalsIgnoreCase(type)){
				 message = data.get(i)[3];
				break;
			}
		}
	return message;
	}
	
	public static String getJDEDescription(String jde) {
		String desc = jde + " " + DateandTimeUtils.getTodaysDate();
		return desc;
	}

}
