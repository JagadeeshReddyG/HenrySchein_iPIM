package com.pim.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.pim.constants.Constants;
import com.pim.enums.ConfigProperties;
import com.pim.exceptions.PropertyFileHandleException;

public class DatabasePropertyFileRead {
	private DatabasePropertyFileRead() {
	}
	
	private static Properties property = new Properties();
	private static final Map<String, String> CONFIG_MAP = new HashMap<String, String>();
	
	static {
		try {
			FileInputStream file = new FileInputStream(Constants.getDatabasePropertyFilePath());
			property.load(file);
			for (Map.Entry<Object, Object> entry : property.entrySet()) {
				CONFIG_MAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()).trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}


	public static String getDatabasePropValue(ConfigProperties key) {
		
		if (Objects.isNull(CONFIG_MAP.get(key.name().toLowerCase()))) {
			throw new PropertyFileHandleException(
					"Property name " + key + " is not found. Please check config Properties");
		}
		return CONFIG_MAP.get(key.name().toLowerCase());
	}

}
