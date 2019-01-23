package com.mjvs.jgsp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PassengerPage {
    // Driver instance.
    private WebDriver driver;

    @FindBy(id = "nav-buy-ticket-tab")
    private WebElement buyTicketTab;

    @FindBy(id = "nav-show-tickets-tab")
    private WebElement showTicketsTab;

    @FindBy(id = "nav-change-account-type-tab")
    private WebElement changeAccountTypeTab;


    public PassengerPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getBuyTicketTab() {
        return buyTicketTab;
    }

    public void setBuyTicketTab(WebElement buyTicketTab) {
        this.buyTicketTab = buyTicketTab;
    }

    public WebElement getShowTicketsTab() {
        return showTicketsTab;
    }

    public void setShowTicketsTab(WebElement showTicketsTab) {
        this.showTicketsTab = showTicketsTab;
    }

    public WebElement getChangeAccountTypeTab() {
        return changeAccountTypeTab;
    }

    public void setChangeAccountTypeTab(WebElement changeAccountTypeTab) {
        this.changeAccountTypeTab = changeAccountTypeTab;
    }

    public void ensureIsClickableBuyTicketTab() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(buyTicketTab));
    }

    public void ensureIsClickableChangeAccountTypeTab() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(changeAccountTypeTab));
    }

    public void ensureIsClickableShowTicketsTab() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(showTicketsTab));
    }
}
