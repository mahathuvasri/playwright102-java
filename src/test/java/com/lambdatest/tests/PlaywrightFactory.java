package com.lambdatest.tests;

import com.microsoft.playwright.*;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PlaywrightFactory {

    public enum RunMode {
        LOCAL,
        LAMBDATEST
    }

    public static class PlaywrightContext {
        public Playwright playwright;
        public Browser browser;
        public Page page;
    }

    public static PlaywrightContext create(RunMode mode) {
        PlaywrightContext ctx = new PlaywrightContext();
        ctx.playwright = Playwright.create();

        if (mode == RunMode.LOCAL) {
            ctx.browser = ctx.playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );
            ctx.page = ctx.browser.newPage();
        } else if (mode == RunMode.LAMBDATEST) {
            Map<String, Object> capabilities = new HashMap<>();
            capabilities.put("browserName", "Chrome");
            capabilities.put("browserVersion", "latest");

            Map<String, Object> ltOptions = new HashMap<>();
            ltOptions.put("platform", "Windows 11");
            ltOptions.put("build", "Playwright 102 Java Assignment");
            ltOptions.put("name", "Playwright Certification Tests");
            ltOptions.put("project", "Playwright 102 Certification");

            ltOptions.put("user", Config.LT_USERNAME);
            ltOptions.put("accessKey", Config.LT_ACCESS_KEY);

            ltOptions.put("network", true);
            ltOptions.put("video", true);
            ltOptions.put("console", "info");
            ltOptions.put("timezone", "Asia/Kolkata");

            capabilities.put("LT:Options", ltOptions);

            String capsJson = new Gson().toJson(capabilities);
            String encodedCaps = URLEncoder.encode(capsJson, StandardCharsets.UTF_8);
            String cdpUrl = Config.LT_CDP_URL + encodedCaps;

            ctx.browser = ctx.playwright.chromium().connect(cdpUrl);

            BrowserContext context = ctx.browser.newContext();
            ctx.page = context.newPage();
        }

        return ctx;
    }

    public static void close(PlaywrightContext ctx) {
        if (ctx != null) {
            if (ctx.browser != null) {
                ctx.browser.close();
            }
            if (ctx.playwright != null) {
                ctx.playwright.close();
            }
        }
    }
}
