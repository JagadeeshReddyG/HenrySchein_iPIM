package com.pim.tests.Middleware;

import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.pim.enums.ConfigProperties;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.PropertyFileRead;

public class MiddlewareBaseClassTest {
	Map<String, List> data;

	@BeforeTest
	public void establishing_db_connection() {
		DatabaseUtilitiies.dbSetup(PropertyFileRead.getPropValue(ConfigProperties.db_url), PropertyFileRead.getPropValue(ConfigProperties.db_user), PropertyFileRead.getPropValue(ConfigProperties.db_password));
	}


	@AfterTest
	public void closing_db_connection() {
		DatabaseUtilitiies.closingConnection();
	}


}
