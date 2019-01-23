package com.mjvs.jgsp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	String APP_URL = "http://localhost:4200/login";
	
	// Page Elements.
	@FindBy(id = "username")
	private WebElement username;

	@FindBy(id = "inputPassword")
	private WebElement password;

	@FindBy(css = "button")
	private WebElement loginButton;

	@FindBy(css = ".active")
	private WebElement navbarActiveLink;

	// Driver instance.
	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	public void ensureLoginButtonIsClickable() {
		new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(this.loginButton));

	}

	public WebElement getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username.clear();
		this.username.sendKeys(username);
	}

	public WebElement getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password.clear();
		this.password.sendKeys(password);
	}

	public WebElement getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(WebElement loginButton) {
		this.loginButton = loginButton;
	}
}
