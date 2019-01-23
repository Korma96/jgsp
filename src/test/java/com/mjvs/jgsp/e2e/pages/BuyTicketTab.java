package com.mjvs.jgsp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BuyTicketTab {
    private WebDriver driver;

    @FindBy(id = "id_transport_type")
    private WebElement transport;

    @FindBy(id = "id_zone")
    private WebElement zone;

    @FindBy(css = "label[for='color-zone']")
    private WebElement ticketForZone;

    @FindBy(css = "label[for='color-line']")
    private WebElement ticketForLine;

    @FindBy(id = "id_line")
    private WebElement line;

    @FindBy(id = "id_ticket_type")
    private WebElement ticketType;

    @FindBy(css = "input[placeholder='dd.mm.yyyy.']")
    private WebElement date;

    @FindBy(css = "select[name='month']")
    private WebElement month;

    // @FindBy(xpath = "//button[contains(.,'Buy ticket')]")
    @FindBy(id = "id_buy_ticket")
    private WebElement buyTicketButton;

    @FindBy(css = "div.toast-message")
    private WebElement toastrEror;

    public BuyTicketTab(WebDriver driver) {
        this.driver = driver;
    }


    public WebElement getTransport() {
        return transport;
    }

    public void setTransport(WebElement transport) {
        this.transport = transport;
    }

    public WebElement getZone() {
        return zone;
    }

    public void setZone(WebElement zone) {
        this.zone = zone;
    }

    public WebElement getLine() {
        return line;
    }

    public WebElement getTicketForZone() {
        return ticketForZone;
    }

    public void setTicketForZone(WebElement ticketForZone) {
        this.ticketForZone = ticketForZone;
    }

    public WebElement getTicketForLine() {
        return ticketForLine;
    }

    public void setTicketForLine(WebElement ticketForLine) {
        this.ticketForLine = ticketForLine;
    }

    public void setLine(WebElement line) {
        this.line = line;
    }

    public WebElement getTicketType() {
        return ticketType;
    }

    public void setTicketType(WebElement ticketType) {
        this.ticketType = ticketType;
    }

    public WebElement getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.clear();
        this.date.sendKeys(date);
    }

    public WebElement getMonth() {
        return month;
    }

    public void setMonth(WebElement month) {
        this.month = month;
    }

    public WebElement getBuyTicketButton() {
        return buyTicketButton;
    }

    public void setBuyTicketButton(WebElement buyTicketButton) {
        this.buyTicketButton = buyTicketButton;
    }

    public WebElement getToastrEror() {
        return toastrEror;
    }

    public void setToastrEror(WebElement toastrEror) {
        this.toastrEror = toastrEror;
    }

    public void ensureIsVisibleTransport() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(transport));
    }

    public void ensureIsClickableTicketForZone() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(ticketForZone));
    }

    public void ensureIsClickableTicketForLine() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(ticketForLine));
    }

    public void ensureIsClickableBuyTicketButton() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(buyTicketButton));
    }

    public void ensureIsVisibleToastrError() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(toastrEror));
    }

    public void ensureIsVisibleZone() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(zone));
    }

    public void ensureIsVisibleLine() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(line));
    }

    public void ensureIsVisibleTicketType() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(ticketType));
    }

    public void ensureIsVisibleDate() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(date));
    }

    public void ensureIsVisibleMonth() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(month));
    }

}
