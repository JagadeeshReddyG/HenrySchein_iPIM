
package com.pim.utils;

import com.pim.constants.Constants;
import com.pim.enums.ConfigProperties;
import com.pim.exceptions.InvalidPathForPropertyFileException;
import com.pim.exceptions.PropertyFileHandleException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class PropertyFileRead {
	private PropertyFileRead() {
	}

	private static Properties property = new Properties();
	private static final Map<String, String> CONFIG_MAP = new HashMap<String, String>();
	static {
		try {
			FileInputStream file = new FileInputStream(Constants.getPropertyFilePath());
			property.load(file);
			for (Map.Entry<Object, Object> entry : property.entrySet()) {
				CONFIG_MAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()).trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static String getPropValue(ConfigProperties key) {
		if (Objects.isNull(CONFIG_MAP.get(key.name().toLowerCase()))) {
			throw new PropertyFileHandleException(
					"Property name " + key + " is not found. Please check config Properties");
		}
		return CONFIG_MAP.get(key.name().toLowerCase());
	}
}
