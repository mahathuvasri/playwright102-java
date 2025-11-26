package com.lambdatest.tests;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class PlaywrightSmokeTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        page = browser.newPage();
    }

    @Test
    public void openHomepage() {
        page.navigate("https://www.lambdatest.com/selenium-playground");
        Assert.assertTrue(
                page.title().toLowerCase().contains("selenium"),
                "Page title validation failed!"
        );
    }

    @AfterMethod
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
