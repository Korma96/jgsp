package com.mjvs.jgsp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class ShowTicketsTab {
    private WebDriver driver;

    @FindBy(css = "table tbody tr")
    private List<WebElement> rows;

    @FindBy(xpath = "(//table[1]/tbody/tr)[last()]/td")
    private List<WebElement> tdTagsFromLastTrTag;

    @FindBy(className = "toast-message")
    private List<WebElement> toastrs;


    public ShowTicketsTab(WebDriver driver) {
        this.driver = driver;
    }

    public int getNumOfTickets() {
        return rows.size() - 1; // -1 zbog zaglavlja
    }

    public List<WebElement> getTdTagsFromLastTrTag() {
        return tdTagsFromLastTrTag;
    }

    public void setTdTagsFromLastTrTag(List<WebElement> tdTagsFromLastTrTag) {
        this.tdTagsFromLastTrTag = tdTagsFromLastTrTag;
    }

    public List<WebElement> getTickets() {
        int size = rows.size();
        if (size > 0) return rows.subList(1, size); // svi redovi bez zaglavlja

        return rows;
    }

    public void ensureIsVisibleTickets() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfAllElements(rows));
    }

    public void ensureIsClickableButton(WebElement button) {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(button));
    }

    public void ensureIsInvisibleToastrs() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfAllElements(toastrs));
    }

    public void ensureIsInvisibleButton(WebElement button) {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOf(button));
    }

    public List<WebElement> getButtons(WebElement lastTd) {
        return lastTd.findElements(By.tagName("button"));
    }

    public void ensureTextNotToBePresentInElement(WebElement td, String text) {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(td, text)));
    }


    public boolean isActivateButton(WebElement button) {
        return button.getAttribute("class").contains("btn-success");
    }

    public boolean isDownloadButton(WebElement button) {
        return button.getAttribute("class").contains("btn-primary");
    }

    public boolean isRemoveButton(WebElement button) {
        return button.getAttribute("class").contains("btn-danger");
    }
    public boolean isDisabledButton(WebElement button) {
        return button.getAttribute("class").contains("btn-secondary");
    }
}
