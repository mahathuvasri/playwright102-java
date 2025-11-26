package com.lambdatest.tests;

public class Config {

    public static final String LT_USERNAME = System.getenv("LT_USERNAME");

    public static final String LT_ACCESS_KEY = System.getenv("LT_ACCESS_KEY");

    // Base CDP URL for LambdaTest Playwright
    public static final String LT_CDP_URL = "wss://cdp.lambdatest.com/playwright?capabilities=";
}
