package com.ssandeep79.springseleniumdemo.demo.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;

/**
 * Advanced utility class for WebElement interactions
 * Provides enhanced element handling capabilities beyond basic Selenium functions
 */
public class WebElementUtility {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private final Actions actions;

    @Value("${element.wait.timeout:30}")
    private int elementWaitTimeout;

    @Value("${element.wait.poll.interval:500}")
    private int elementWaitPollInterval;

    public WebElementUtility(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    /**
     * Creates a custom fluent wait instance with specified timeout and polling interval
     * @param timeoutInSeconds timeout duration in seconds
     * @param pollingIntervalInMillis polling interval in milliseconds
     * @return configured Wait instance
     */
    public Wait<WebDriver> createFluentWait(int timeoutInSeconds, int pollingIntervalInMillis) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Waits for an element to be clickable and clicks it with retry mechanism
     * @param element the WebElement to click
     * @param retries number of retry attempts if StaleElementReferenceException occurs
     */
    public void clickWithRetry(WebElement element, int retries) {
        int attempts = 0;
        while (attempts < retries) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == retries) {
                    throw e;
                }
            }
        }
    }

    /**
     * Performs drag and drop operation between two elements
     * @param source source element
     * @param target target element
     */
    public void dragAndDrop(WebElement source, WebElement target) {
        wait.until(ExpectedConditions.visibilityOf(source));
        wait.until(ExpectedConditions.visibilityOf(target));
        actions.dragAndDrop(source, target).build().perform();
    }

    /**
     * Performs hover action on an element
     * @param element the element to hover over
     */
    public void hoverOverElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        actions.moveToElement(element).build().perform();
    }

    /**
     * Scrolls element into view using JavaScript
     * @param element the element to scroll to
     */
    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Highlights an element temporarily for debugging or demonstration
     * @param element the element to highlight
     * @param durationMs time in milliseconds to keep the highlight
     */
    public void highlightElement(WebElement element, long durationMs) {
        String originalStyle = element.getAttribute("style");
        js.executeScript(
                "arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');",
                element
        );

        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                element, originalStyle
        );
    }

    /**
     * Waits for an element to have a specific attribute value
     * @param element the element to check
     * @param attribute the attribute name
     * @param value expected attribute value
     * @param timeoutInSeconds timeout in seconds
     * @return true if the attribute has the expected value within timeout
     */
    public boolean waitForElementAttributeValue(WebElement element, String attribute, 
                                               String value, int timeoutInSeconds) {
        try {
            return createFluentWait(timeoutInSeconds, elementWaitPollInterval)
                    .until(driver -> value.equals(element.getAttribute(attribute)));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Waits for any of the specified elements to be visible
     * @param elements list of elements to check
     * @param timeoutInSeconds timeout in seconds
     * @return first visible element found or null if none became visible
     */
    public WebElement waitForAnyElementVisible(List<WebElement> elements, int timeoutInSeconds) {
        Wait<WebDriver> fluentWait = createFluentWait(timeoutInSeconds, elementWaitPollInterval);
        
        try {
            return fluentWait.until(driver -> {
                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed()) {
                            return element;
                        }
                    } catch (StaleElementReferenceException | NoSuchElementException e) {
                        // Continue to next element
                    }
                }
                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
    }

    /**
     * Executes a custom JavaScript on the page
     * @param script the JavaScript code to execute
     * @param args arguments to the script
     * @return result of the JavaScript execution
     */
    public Object executeJavaScript(String script, Object... args) {
        return js.executeScript(script, args);
    }

    /**
     * Handles shadow DOM elements
     * @param hostElement the host element containing shadow root
     * @param cssSelector the CSS selector within shadow DOM
     * @return the found WebElement inside shadow DOM
     */
    public WebElement findElementInShadowDOM(WebElement hostElement, String cssSelector) {
        Object shadowRoot = js.executeScript("return arguments[0].shadowRoot", hostElement);
        if (shadowRoot instanceof WebElement) {
            return ((WebElement) shadowRoot).findElement(By.cssSelector(cssSelector));
        }
        throw new NoSuchElementException("Could not access shadow root or find element: " + cssSelector);
    }

    /**
     * Types text character by character with delay to simulate human typing
     * @param element element to type into
     * @param text text to type
     * @param delayMillis delay between keystrokes in milliseconds
     */
    public void typeWithDelay(WebElement element, String text, long delayMillis) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Waits for page to load completely via JavaScript readyState
     * @param timeoutInSeconds maximum time to wait
     */
    public void waitForPageLoad(int timeoutInSeconds) {
        Wait<WebDriver> fluentWait = createFluentWait(timeoutInSeconds, 100);
        
        fluentWait.until(driver -> js.executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Gets computed CSS value of an element
     * @param element the element to check
     * @param cssProperty the CSS property name
     * @return computed CSS value
     */
    public String getComputedStyle(WebElement element, String cssProperty) {
        return (String) js.executeScript(
                "return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);",
                element, cssProperty
        );
    }

    /**
     * Checks if element is in viewport
     * @param element the element to check
     * @return true if element is in viewport
     */
    public boolean isElementInViewport(WebElement element) {
        return (Boolean) js.executeScript(
                "var rect = arguments[0].getBoundingClientRect(); " +
                        "return (rect.top >= 0 && rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth));",
                element
        );
    }

    /**
     * Performs right-click on an element
     * @param element the element to right-click
     */
    public void rightClick(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        actions.contextClick(element).build().perform();
    }

    /**
     * Performs double-click on an element
     * @param element the element to double-click
     */
    public void doubleClick(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        actions.doubleClick(element).build().perform();
    }

    /**
     * Safely gets text from an element handling NoSuchElementException
     * @param element the element to get text from
     * @return the element's text or empty string if not found
     */
    public String safeGetText(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).getText();
        } catch (TimeoutException | NoSuchElementException e) {
            return "";
        }
    }

    /**
     * Waits for jQuery to complete all animations and AJAX requests
     * @param timeoutInSeconds maximum time to wait
     * @return true if jQuery is loaded and not active
     */
    public boolean waitForJQueryToComplete(int timeoutInSeconds) {
        try {
            return createFluentWait(timeoutInSeconds, 100).until(driver -> {
                Boolean jQueryDefined = (Boolean) js.executeScript("return typeof jQuery != 'undefined'");
                if (Boolean.TRUE.equals(jQueryDefined)) {
                    Boolean jQueryActive = (Boolean) js.executeScript("return jQuery.active == 0");
                    return Boolean.TRUE.equals(jQueryActive);
                }
                return true; // If jQuery is not defined, we don't need to wait for it
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
}
