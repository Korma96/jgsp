package com.mjvs.jgsp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class ChangeAccountTypeTab {
    private WebDriver driver;

    @FindBy(css = "#id_current_passenger_type b")
    private WebElement currentPassengerType;

    @FindBy(id = "id_passenger_type")
    private WebElement passengerType;

    @FindBy(id = "id_confirmation")
    private WebElement confirmation; // <input type="file"/>

    @FindBy(xpath = "//button[contains(.,'Send request for changing')]")
    private WebElement sendRequestForChangingButton;

    @FindBy(className = "toast-message")
    private List<WebElement> toastrs;


    public ChangeAccountTypeTab(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getCurrentPassengerType() {
        return currentPassengerType;
    }

    public void setCurrentPassengerType(WebElement currentPassengerType) {
        this.currentPassengerType = currentPassengerType;
    }

    public WebElement getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(WebElement passengerType) {
        this.passengerType = passengerType;
    }

    public WebElement getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation.clear();
        this.confirmation.sendKeys(confirmation);
    }

    public WebElement getSendRequestForChangingButton() {
        return sendRequestForChangingButton;
    }

    public void setSendRequestForChangingButton(WebElement sendRequestForChangingButton) {
        this.sendRequestForChangingButton = sendRequestForChangingButton;
    }

    public List<WebElement> getToastrs() {
        return toastrs;
    }

    public void setToastrs(List<WebElement> toastrs) {
        this.toastrs = toastrs;
    }

    public void ensureIsVisibleCurrentPassengerType() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(currentPassengerType));
    }

    public void ensureIsVisiblePassengerType() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(passengerType));
    }

    public void ensureIsVisibleConfirmation() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(confirmation));
    }

    public void ensureIsClickableSendRequestForChangingButton() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(sendRequestForChangingButton));
    }

    public void ensureIsVisibleToastrs() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfAllElements(toastrs));
    }

    public void ensureIsInvisibleToastrs() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfAllElements(toastrs));
    }
}
