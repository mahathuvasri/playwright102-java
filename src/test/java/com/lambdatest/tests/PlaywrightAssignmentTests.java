package com.lambdatest.tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PlaywrightAssignmentTests {

    private PlaywrightFactory.PlaywrightContext ctx;
    private Page page;

    @BeforeMethod
    public void setup() {
        ctx = PlaywrightFactory.create(PlaywrightFactory.RunMode.LAMBDATEST);
        page = ctx.page;
    }

    @Test
    public void testScenario1_simpleFormDemo() {
        page.navigate("https://www.lambdatest.com/selenium-playground");
        page.click("text=Simple Form Demo");
        Assert.assertTrue(page.url().contains("simple-form-demo"));
        String message = "Welcome to LambdaTest";
        page.fill("input#user-message", message);
        page.click("button:has-text('Get Checked Value')");
        String actualMessage = page.textContent("#message").trim();
        Assert.assertEquals(actualMessage, message);
    }

    @Test
    public void testScenario2_dragAndDropSlider() {
        page.navigate("https://www.lambdatest.com/selenium-playground");
        page.click("text=Drag & Drop Sliders");
        Locator slider = page.locator("input[type='range']").nth(2);
        for (int i = 0; i < 100; i++) {
            slider.press("ArrowRight");
            String currentValue = slider.evaluate("el => el.value").toString();
            if ("95".equals(currentValue)) {
                break;
            }
        }
        String finalValue = slider.evaluate("el => el.value").toString();
        Assert.assertEquals(finalValue, "95");
    }

    @Test
    public void testScenario3_inputFormSubmit() {
        page.navigate("https://www.lambdatest.com/selenium-playground");
        page.click("text=Input Form Submit");
        page.click("button:has-text(\"Submit\")");
        String validationMessage = page.locator("#name").evaluate("el => el.validationMessage").toString();
        Assert.assertTrue(validationMessage.contains("Please fill out this field"));

        page.fill("#name", "Test User");
        page.fill("#inputEmail4", "test.user@example.com");
        page.fill("#inputPassword4", "Password123!");
        page.fill("#company", "LambdaTest");
        page.fill("#websitename", "https://example.com");

        Locator options = page.locator("select[name='country'] option");
        int count = options.count();
        String selectValue = "";
        for (int i = 0; i < count; i++) {
            String label = options.nth(i).textContent().trim();
            String value = options.nth(i).getAttribute("value");
            System.out.println("Country option: TEXT=" + label + "  VALUE=" + value);
            if (label.equalsIgnoreCase("United States")) {
                selectValue = value;
            }
        }
        if (selectValue.isEmpty()) {
            throw new AssertionError("'United States' not an option in country drop-down!");
        }
        page.selectOption("select[name='country']", selectValue);

        page.fill("#inputCity", "New York");
        page.fill("#inputAddress1", "Address line 1");
        page.fill("#inputAddress2", "Address line 2");
        page.fill("#inputState", "NY");
        page.fill("#inputZip", "10001");
        page.click("button:has-text(\"Submit\")");
        page.waitForTimeout(2000);
        String successText = page.textContent(".success-msg").trim();
        Assert.assertEquals(successText, "Thanks for contacting us, we will get back to you shortly.");
    }

    @AfterMethod
    public void tearDown() {
        PlaywrightFactory.close(ctx);
    }
}
