package com.pim.pages;

import org.openqa.selenium.By;

public class ImagePreviewTab extends BasePage{
	private final By zoomBar = By.xpath("//div[contains(text(),'Zoom')]/ancestor::div[@class='v-horizontallayout v-layout v-horizontal v-widget']");
    private final By downloadIcon = By.xpath("//div[@class='v-slot v-slot-hpmw-mediaDownloadLink']");
    
    public boolean isZoomBarPresent() {
    	return isVisible(zoomBar,"Zoom");
    }
    
    public boolean isDownloadIconPresent() {
    	return isVisible(downloadIcon,"download icon");
    }
	
}
