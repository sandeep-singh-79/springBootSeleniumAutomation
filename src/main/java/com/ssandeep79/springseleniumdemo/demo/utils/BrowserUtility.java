package com.ssandeep79.springseleniumdemo.demo.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.devtools.v126.network.model.RequestId;
import org.openqa.selenium.devtools.v126.network.model.Response;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Utility class for browser-specific operations
 * Provides methods for cookie management, storage access, console logs, and network monitoring
 */
public class BrowserUtility {
    private static final Logger logger = LoggerFactory.getLogger(BrowserUtility.class);
    
    private final WebDriver driver;
    private DevTools devTools;
    private final Map<RequestId, Response> networkResponses = new ConcurrentHashMap<>();
    
    public BrowserUtility(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Initializes DevTools for Chrome or Edge browsers
     * Required for network monitoring and console log capturing
     */
    public void initDevTools() {
        if (driver instanceof HasDevTools) {
            try {
                devTools = ((HasDevTools) driver).getDevTools();
                devTools.createSession();
                logger.info("DevTools session initialized successfully");
            } catch (Exception e) {
                logger.error("Failed to initialize DevTools session", e);
            }
        } else {
            logger.warn("This WebDriver doesn't support DevTools");
        }
    }
    
    /**
     * Gets all cookies from the current browser session
     * @return set of all cookies
     */
    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }
    
    /**
     * Gets a specific cookie by name
     * @param name cookie name
     * @return the cookie if found, null otherwise
     */
    public Cookie getCookie(String name) {
        return driver.manage().getCookieNamed(name);
    }
    
    /**
     * Adds a new cookie
     * @param name cookie name
     * @param value cookie value
     * @param path cookie path
     * @param domain cookie domain (null for current domain)
     * @param expiry cookie expiration date (null for session cookie)
     * @param isSecure whether the cookie is secure (HTTPS only)
     */
    public void addCookie(String name, String value, String path, String domain, Date expiry, boolean isSecure) {
        Cookie.Builder cookieBuilder = new Cookie.Builder(name, value);
        
        if (path != null) {
            cookieBuilder.path(path);
        }
        
        if (domain != null) {
            cookieBuilder.domain(domain);
        }
        
        if (expiry != null) {
            cookieBuilder.expiresOn(expiry);
        }
        
        if (isSecure) {
            cookieBuilder.isSecure(true);
        }
        
        driver.manage().addCookie(cookieBuilder.build());
    }
    
    /**
     * Deletes a specific cookie by name
     * @param name cookie name
     */
    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }
    
    /**
     * Deletes all cookies in the current domain
     */
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }
    
    /**
     * Gets an item from local storage
     * @param key item key
     * @return item value or null if not found
     */
    public String getLocalStorageItem(String key) {
        try {
            return (String) ((JavascriptExecutor) driver).executeScript(
                    "return window.localStorage.getItem(arguments[0]);", key);
        } catch (Exception e) {
            logger.error("Failed to get local storage item: {}", key, e);
            return null;
        }
    }
    
    /**
     * Sets an item in local storage
     * @param key item key
     * @param value item value
     */
    public void setLocalStorageItem(String key, String value) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
        } catch (Exception e) {
            logger.error("Failed to set local storage item: {}", key, e);
        }
    }
    
    /**
     * Removes an item from local storage
     * @param key item key
     */
    public void removeLocalStorageItem(String key) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "window.localStorage.removeItem(arguments[0]);", key);
        } catch (Exception e) {
            logger.error("Failed to remove local storage item: {}", key, e);
        }
    }
    
    /**
     * Clears all items in local storage
     */
    public void clearLocalStorage() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        } catch (Exception e) {
            logger.error("Failed to clear local storage", e);
        }
    }
    
    /**
     * Gets an item from session storage
     * @param key item key
     * @return item value or null if not found
     */
    public String getSessionStorageItem(String key) {
        try {
            return (String) ((JavascriptExecutor) driver).executeScript(
                    "return window.sessionStorage.getItem(arguments[0]);", key);
        } catch (Exception e) {
            logger.error("Failed to get session storage item: {}", key, e);
            return null;
        }
    }
    
    /**
     * Sets an item in session storage
     * @param key item key
     * @param value item value
     */
    public void setSessionStorageItem(String key, String value) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "window.sessionStorage.setItem(arguments[0], arguments[1]);", key, value);
        } catch (Exception e) {
            logger.error("Failed to set session storage item: {}", key, e);
        }
    }
    
    /**
     * Clears all items in session storage
     */
    public void clearSessionStorage() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        } catch (Exception e) {
            logger.error("Failed to clear session storage", e);
        }
    }
    
    /**
     * Gets browser console logs
     * @return list of log entries
     */
    public List<LogEntry> getConsoleLogs() {
        try {
            LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
            return logs.getAll();
        } catch (Exception e) {
            logger.error("Failed to get console logs", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Gets error-level console logs
     * @return list of error log entries
     */
    public List<LogEntry> getConsoleErrors() {
        try {
            return getConsoleLogs().stream()
                    .filter(log -> log.getLevel().toString().equals("SEVERE"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to filter console errors", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Starts monitoring network traffic
     * Requires DevTools to be initialized first
     */
    public void startNetworkMonitoring() {
        if (devTools == null) {
            logger.warn("DevTools not initialized. Call initDevTools() first.");
            return;
        }
        
        try {
            networkResponses.clear();
            
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            
            devTools.addListener(Network.responseReceived(), responseReceived -> {
                networkResponses.put(responseReceived.getRequestId(), responseReceived.getResponse());
            });
            
            logger.info("Network monitoring started");
        } catch (Exception e) {
            logger.error("Failed to start network monitoring", e);
        }
    }
    
    /**
     * Stops network traffic monitoring
     */
    public void stopNetworkMonitoring() {
        if (devTools == null) {
            return;
        }
        
        try {
            devTools.send(Network.disable());
            logger.info("Network monitoring stopped");
        } catch (Exception e) {
            logger.error("Failed to stop network monitoring", e);
        }
    }
    
    /**
     * Gets all captured network responses
     * @return map of request IDs to response objects
     */
    public Map<RequestId, Response> getNetworkResponses() {
        return Collections.unmodifiableMap(networkResponses);
    }
    
    /**
     * Gets HTTP responses by status code
     * @param statusCode HTTP status code to filter by
     * @return list of matching responses
     */
    public List<Response> getResponsesByStatusCode(int statusCode) {
        return networkResponses.values().stream()
                .filter(response -> response.getStatus() == statusCode)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets network errors (4xx and 5xx responses)
     * @return list of error responses
     */
    public List<Response> getNetworkErrors() {
        return networkResponses.values().stream()
                .filter(response -> response.getStatus() >= 400)
                .collect(Collectors.toList());
    }
    
    /**
     * Sets browser dimension to simulate specific device
     * @param width viewport width
     * @param height viewport height
     * @param deviceName name of the device for logging
     */
    public void setDeviceDimension(int width, int height, String deviceName) {
        try {
            Dimension dimension = new Dimension(width, height);
            driver.manage().window().setSize(dimension);
            logger.info("Browser dimension set to {} x {} ({})", width, height, deviceName);
        } catch (Exception e) {
            logger.error("Failed to set browser dimension", e);
        }
    }
    
    /**
     * Sets default browser timeout values
     * @param implicitWait implicit wait timeout in seconds
     * @param pageLoad page load timeout in seconds
     * @param script script execution timeout in seconds
     */
    public void setBrowserTimeouts(int implicitWait, int pageLoad, int script) {
        try {
            driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(pageLoad, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(script, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Failed to set browser timeouts", e);
        }
    }
    
    /**
     * Takes a screenshot of the current browser window
     * @return screenshot as byte array, or null if failed
     */
    public byte[] takeScreenshot() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }
    
    /**
     * Clears browser data (cookies, storage)
     */
    public void clearBrowserData() {
        deleteAllCookies();
        clearLocalStorage();
        clearSessionStorage();
    }
    
    /**
     * Gets current page performance timing metrics using Navigation Timing API
     * @return map of timing metrics
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPerformanceMetrics() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (Map<String, Object>) js.executeScript(
                    "return window.performance.timing");
        } catch (Exception e) {
            logger.error("Failed to get performance metrics", e);
            return Collections.emptyMap();
        }
    }
    
    /**
     * Checks if specific feature is enabled in the browser
     * @param featureName name of the feature to check
     * @return true if feature is enabled
     */
    public boolean isFeatureEnabled(String featureName) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script = String.format(
                    "return '%s' in window || 'has%s' in window.navigator;",
                    featureName, featureName.substring(0, 1).toUpperCase() + featureName.substring(1));
            return (Boolean) js.executeScript(script);
        } catch (Exception e) {
            logger.error("Failed to check feature: {}", featureName, e);
            return false;
        }
    }
}
