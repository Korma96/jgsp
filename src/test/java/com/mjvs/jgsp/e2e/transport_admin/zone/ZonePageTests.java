package com.mjvs.jgsp.e2e.transport_admin.zone;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.Assert.*;

// Tests depend on each other, execute only all tests together
// and don`t change their execution order !!!
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class ZonePageTests
{
    private final String ZoneHomePage = "http://localhost:4200/transport/zone";
    private final String NewZoneName = "nova_zona";
    private final String RenamedNewZoneName = "nova_zona123";
    private final String GradskeLinijeZoneName = "gradske_linije";
    private final String PrigradskeLinijeZoneName = "prigradske_linije";
    private final int Timeout = 10;
    private final String ZoneButtonCss = ".firsttt";
    private final String ZoneRemoveButtonCss = ".seconddd";
    private final String LineButtonCss = ".lineButton";
    private final String LineManipulateButtonCss = ".manipulateButton";
    private final String LineName = "1";
    private static WebDriver browser;

    @BeforeClass
    public static void SetupSelenium() {
        //instantiate browser
        System.setProperty("webdriver.chrome.driver", "D:\\Storage\\chromedriver.exe");
        browser = new ChromeDriver();
        //maximize window
        browser.manage().window().maximize();
    }

    @Test
    public void TestA_ShowAllZones()
    {
        browser.navigate().to("http://localhost:4200/transport");
        // wait for page to load
        WaitUntilZonePageIsShown();

        assertEquals("We have 3 zones in e2e.sql file",3, GetNumberOfZones());
    }

    @Test
    public void TestB_AddZone() {
        // Arrange
        browser.navigate().to("http://localhost:4200/transport/add_zone");
        WebElement addZoneInput = browser.findElement(By.id("zone_name_input"));
        assertTrue(addZoneInput.isDisplayed());
        WebElement addZoneButton = browser.findElement(By.id("zone_name_button"));
        assertTrue(addZoneButton.isDisplayed());

        // Act
        // try to add zone with existing name
        addZoneInput.sendKeys(GradskeLinijeZoneName);
        addZoneButton.click();
        // error is shown and url is unchanged
        assertNotEquals(ZoneHomePage, browser.getCurrentUrl());
        addZoneInput.clear();
        addZoneInput.sendKeys("nova_zona");
        addZoneButton.click();

        // Assert
        WaitUntilZonePageIsShown();
        assertEquals(4, GetNumberOfZones());
        assertNotNull(GetZoneButtonByName(NewZoneName));
    }

    @Test
    public void TestC_UpdateZoneNameAndLines() throws Exception
    {
        browser.navigate().to(ZoneHomePage);
        WaitUntilZonesAppear();
        WebElement zoneButton = GetZoneButtonByName(GradskeLinijeZoneName);
        assertNotNull(zoneButton);
        // go to update zone page
        zoneButton.click();

        // remove line from zone
        WaitUntilZoneLinesAppear();
        assertNull(GetAddLineButtonByName(LineName));
        WebElement removeLineButton = GetRemoveLineButtonByName(LineName);
        assertNotNull(removeLineButton);
        removeLineButton.click();
        WaitUntilRemainingLinesAppear();
        assertNotNull(GetAddLineButtonByName(LineName));

        // add line to newly created zone
        browser.navigate().to(ZoneHomePage);
        WaitUntilZonesAppear();
        WebElement newZoneButton = GetZoneButtonByName(NewZoneName);
        assertNotNull(newZoneButton);
        newZoneButton.click();
        WaitUntilAddOrUpdateZonePageIsShown();

        WaitUntilRemainingLinesAppear();
        assertNull(GetRemoveLineButtonByName(LineName));
        WebElement lineButton = GetAddLineButtonByName(LineName);
        assertNotNull(lineButton);
        lineButton.click();
        WaitUntilZoneLinesAppear();
        assertNull(GetAddLineButtonByName(LineName));
        assertNotNull(GetRemoveLineButtonByName(LineName));

        // try to rename zone
        WebElement renameZoneInput = browser.findElement(By.id("zone_name_input"));
        renameZoneInput.clear();
        renameZoneInput.sendKeys(PrigradskeLinijeZoneName);
        WebElement renameZoneButton = browser.findElement(By.id("zone_rename_button"));
        renameZoneButton.click();
        assertNotEquals(ZoneHomePage, browser.getCurrentUrl());

        // rename zone
        renameZoneInput.clear();
        renameZoneInput.sendKeys(RenamedNewZoneName);
        renameZoneButton.click();
        WaitUntilZonePageIsShown();
        WaitUntilZonesAppear();
        assertNotNull(GetZoneButtonByName(RenamedNewZoneName));
        assertNull(GetZoneButtonByName(NewZoneName));
    }

    @Test
    public void TestD_DeleteZone() throws Exception
    {
        // remove newly created zone, so its line(s) will be released
        browser.navigate().to(ZoneHomePage);
        WaitUntilZonesAppear();
        WebElement removeZoneButton = GetRemoveZoneButtonByName(RenamedNewZoneName);
        assertNotNull(removeZoneButton);
        removeZoneButton.click();
        Thread.sleep(3000);
        WaitUntilZonesAppear();
        assertEquals(3, GetNumberOfZones());

        // return line to zone
        WebElement zoneButton = GetZoneButtonByName(GradskeLinijeZoneName);
        assertNotNull(zoneButton);
        zoneButton.click();
        WaitUntilAddOrUpdateZonePageIsShown();
        WaitUntilRemainingLinesAppear();
        assertNull(GetRemoveLineButtonByName(LineName));
        WebElement lineButton = GetAddLineButtonByName(LineName);
        assertNotNull(lineButton);
        lineButton.click();
        Thread.sleep(3000);
        WaitUntilZoneLinesAppear();
        assertNotNull(GetRemoveLineButtonByName(LineName));
        assertNull(GetAddLineButtonByName(LineName));
    }

    private int GetNumberOfZones()
    {
        return browser.findElements(By.cssSelector(ZoneButtonCss)).size();
    }

    private WebElement GetZoneButtonByName(String zoneName)
    {
        List<WebElement> zoneButtons = browser.findElements(By.cssSelector(ZoneButtonCss));
        for(WebElement zoneButton : zoneButtons) {
            if(zoneButton.getAttribute("textContent").trim().equals(zoneName)){
                return zoneButton;
            }
        }
        return null;
    }

    private WebElement GetRemoveZoneButtonByName(String zoneName)
    {
        List<WebElement> zoneButtons = browser.findElements(By.cssSelector(ZoneButtonCss));
        List<WebElement> zoneRemoveButtons = browser.findElements(By.cssSelector(ZoneRemoveButtonCss));
        for(int i = 0; i<zoneButtons.size(); i++) {
            if(zoneButtons.get(i).getAttribute("textContent").trim().equals(zoneName)){
                return zoneRemoveButtons.get(i);
            }
        }
        return null;
    }

    private WebElement GetRemoveLineButtonByName(String lineName)
    {
        List<WebElement> lineButtons = browser.findElement(By.id("zone_lines_table"))
                                                .findElements(By.cssSelector(LineButtonCss));
        List<WebElement> lineRemoveButtons = browser.findElement(By.id("zone_lines_table"))
                                                .findElements(By.cssSelector(LineManipulateButtonCss));
        for(int i=0; i<lineButtons.size(); i++){
            if(lineButtons.get(i).getAttribute("textContent").trim().equals(lineName)){
                return lineRemoveButtons.get(i);
            }
        }
        return null;
    }

    private WebElement GetAddLineButtonByName(String lineName)
    {
        List<WebElement> lineButtons = browser.findElement(By.id("remaining_lines_table"))
                .findElements(By.cssSelector(LineButtonCss));
        List<WebElement> lineAddButtons = browser.findElement(By.id("remaining_lines_table"))
                .findElements(By.cssSelector(LineManipulateButtonCss));
        for(int i=0; i<lineButtons.size(); i++){
            if(lineButtons.get(i).getAttribute("textContent").trim().equals(lineName)){
                return lineAddButtons.get(i);
            }
        }
        return null;
    }

    private void WaitUntilZonePageIsShown()
    {
        new WebDriverWait(browser, Timeout)
                .until(ExpectedConditions.elementSelectionStateToBe(By.id("table_zones"), false));
    }

    private void WaitUntilAddOrUpdateZonePageIsShown()
    {
        new WebDriverWait(browser, Timeout)
                .until(ExpectedConditions.elementSelectionStateToBe(
                        By.id("zone_name_input"), false));
    }

    private void WaitUntilZonesAppear()
    {
        GetWaiter().until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return browser.findElement(By.cssSelector(ZoneButtonCss));
            }
        });
    }

    private void WaitUntilRemainingLinesAppear()
    {
        GetWaiter().until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return browser.findElement(By.id("remaining_lines_table"))
                        .findElement(By.cssSelector(LineButtonCss));
            }
        });
    }

    private void WaitUntilZoneLinesAppear()
    {
        GetWaiter().until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return browser.findElement(By.id("zone_lines_table"))
                        .findElement(By.cssSelector(LineButtonCss));
            }
        });
    }

    private Wait<WebDriver> GetWaiter()
    {
        return new FluentWait<>(browser)
                .withTimeout(10, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    @AfterClass
    public static void CloseSelenium() {
        // Shutdown the browser
        browser.quit();
    }
}
