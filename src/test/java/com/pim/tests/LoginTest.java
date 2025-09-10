package com.pim.tests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.pages.LoginPage;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;


public final class LoginTest extends BaseTest {

    private LoginTest(){
    }

    @PimFrameworkAnnotation(category = {CategoryType.SMOKE}, module = Modules.HOMEPAGE)
    @Test(description = "test1")
    public void LoginTest(){
        LoginPage lp = new LoginPage();
        String title = lp.enterUserName("kathir.ranga")
                .enterPassword("MSD-Technology")
                .clickLoginButton().clickLogoutButton().getTitle();
        Assertions.assertThat(title).isEqualTo("Informatica MDM – Product 360");
    }
}
