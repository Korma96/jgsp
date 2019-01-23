package com.mjvs.jgsp.e2e.tests;

import static org.junit.Assert.*;

import com.mjvs.jgsp.e2e.pages.*;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PassengerPageTest {
    private WebDriver browser;
    private String baseUrl = "http://localhost:4200";

    private LoginPage loginPage;
    private PassengerPage passengerPage;
    private BuyTicketTab buyTicketTab;
    private ShowTicketsTab showTicketsTab;
    private ModalDilaog modalDilaog;
    private ChangeAccountTypeTab changeAccountTypeTab;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    // selektovanje button-a sa nekim tekstom u sebi
    //String xpathStr = String.format("//button[contains(.,'%s')]", nekiTekst);
    //driver.findElement(By.xpath(xpathStr));

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/mjvs/jgsp/e2e/driver/chromedriver.exe");
        browser = new ChromeDriver();

        browser.manage().window().maximize();
        browser.navigate().to(baseUrl + "/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        passengerPage = PageFactory.initElements(browser, PassengerPage.class);
        buyTicketTab = PageFactory.initElements(browser, BuyTicketTab.class);
        showTicketsTab = PageFactory.initElements(browser, ShowTicketsTab.class);
        modalDilaog = PageFactory.initElements(browser, ModalDilaog.class);
        changeAccountTypeTab = PageFactory.initElements(browser, ChangeAccountTypeTab.class);

        loginPage.setUsername("steva");
        loginPage.setPassword("steva");
        loginPage.ensureLoginButtonIsClickable();
        loginPage.getLoginButton().click();
    }

    @Test
    public void buyTicket() {
        // Odlazimo na tab show tickets da bismo videli koliko ima starih ticket-a pre kupovine novih
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        int oldNumOfTickets = showTicketsTab.getNumOfTickets();

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo tram za transport i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTransport();
        new Select(buyTicketTab.getTransport()).selectByVisibleText("tram");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Nije moguce dobiti cenu jer trenutno ne postoji cenovnik za tram
        buyTicketTab.ensureIsVisibleToastrError();
        assertEquals("The price has not been successfully delivered!",
                buyTicketTab.getToastrEror().getText().trim());
        modalDilaog.ensureIsVisiblePrice();
        assertEquals("Please wait...", modalDilaog.getPrice().getText().trim());
        // Gasimo dialog za potvrdu kupovine
        modalDilaog.ensureIsClickableCancelPurchaseButton();
        modalDilaog.getCancelPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();

        // Izaberemo metro za transport i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTransport();
        new Select(buyTicketTab.getTransport()).selectByVisibleText("metro");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Nije moguce dobiti cenu jer trenutno ne postoji cenovnik za metro
        buyTicketTab.ensureIsVisibleToastrError();
        assertEquals("The price has not been successfully delivered!",
                buyTicketTab.getToastrEror().getText().trim());
        modalDilaog.ensureIsVisiblePrice();
        assertEquals("Please wait...", modalDilaog.getPrice().getText().trim());
        // Gasimo dialog za potvrdu kupovine
        modalDilaog.ensureIsClickableCancelPurchaseButton();
        modalDilaog.getCancelPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();

        // Izaberemo metro za transport i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleDate();
        buyTicketTab.getDate().clear(); // simuliramo prazno polje za date
        // ovo radimo da bi ngbDatepicker izgubio fokus
        //**********************************************
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();
        //**********************************************
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Nije moguce dobiti cenu jer trenutno ne postoji cenovnik za metro
        buyTicketTab.ensureIsVisibleToastrError();
        assertEquals("You did not select a date!",
                buyTicketTab.getToastrEror().getText().trim());

        // Izaberemo metro za transport i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleDate();
        buyTicketTab.setDate("25.sdsdsd.2030."); // simuliramo pogresan date
        // ovo radimo da bi ngbDatepicker izgubio fokus
        //**********************************************
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();
        //**********************************************
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Nije moguce dobiti cenu jer trenutno ne postoji cenovnik za metro
        buyTicketTab.ensureIsVisibleToastrError();
        assertEquals("Not valid date!",
                buyTicketTab.getToastrEror().getText().trim());

        // Izaberemo metro za transport i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleDate();
        buyTicketTab.setDate("20.07.2005."); // simuliramo date koji je vec prosao
        // ovo radimo da bi ngbDatepicker izgubio fokus
        //**********************************************
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();
        //**********************************************
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Nije moguce dobiti cenu jer trenutno ne postoji cenovnik za metro
        buyTicketTab.ensureIsVisibleToastrError();
        assertEquals("The selected date has already passed!",
                buyTicketTab.getToastrEror().getText().trim());

        // Izaberemo bus za transport i zonu prigradske_linije, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTransport();
        new Select(buyTicketTab.getTransport()).selectByVisibleText("bus");
        buyTicketTab.ensureIsVisibleZone();
        new Select(buyTicketTab.getZone()).selectByVisibleText("prigradske_linije");
        LocalDate date = LocalDate.now();
        buyTicketTab.ensureIsVisibleDate();
        buyTicketTab.setDate(date.getDayOfMonth() + "."+date.getMonthValue()+"."+date.getYear()+".");
        // ovo radimo da bi ngbDatepicker izgubio fokus
        //**********************************************
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();
        //**********************************************
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        int newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo za ticketType monthly, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("monthly");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo za ticketType yearly, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("yearly");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo za zonu medjumesne_linije, kliknemo na Ticket for line, za liniju izaberemo 60,
        // za ticketType onetime, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleZone();
        new Select(buyTicketTab.getZone()).selectByVisibleText("medjumesne_linije");
        buyTicketTab.ensureIsClickableTicketForLine();
        buyTicketTab.getTicketForLine().click();
        buyTicketTab.ensureIsVisibleLine();
        new Select(buyTicketTab.getLine()).selectByVisibleText("60");
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("onetime");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo za ticketType daily, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("daily");
        date = LocalDate.now();
        int day = date.getDayOfMonth();
        if(day < YearMonth.of(date.getYear(), date.getMonthValue()).lengthOfMonth()) day++;
        buyTicketTab.ensureIsVisibleDate();
        buyTicketTab.setDate(day + "."+date.getMonthValue()+"."+date.getYear()+".");
        // ovo radimo da bi ngbDatepicker izgubio fokus
        //**********************************************
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();
        //**********************************************
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo za ticketType monthly, za month sledeci mesec u odnosu na trenutni, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("monthly");
        buyTicketTab.ensureIsVisibleMonth();
        int month = LocalDate.now().getMonthValue();
        if(month != 12) month++; // hocemo sledeci mesec
        String monthStr = Month.of(month).name();
        monthStr = monthStr.charAt(0) + monthStr.substring(1).toLowerCase();
        new Select(buyTicketTab.getMonth()).selectByVisibleText(monthStr);
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;

        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Izaberemo za ticketType yearly, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("yearly");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Odlazimo na tab show tickets da bismo videli koliko ima ticket-a nakon kupovine jednog ticket-a
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        showTicketsTab.ensureIsVisibleTickets();
        newNumOfTickets = showTicketsTab.getNumOfTickets();
        assertEquals(newNumOfTickets, oldNumOfTickets + 1);
        oldNumOfTickets = newNumOfTickets;
    }

    @Test
    public void showTickets() {
        // Prebacujemo se na tab buy ticket
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();

        // Najpre kupimo par karata da bismo mogli testirati activate, download i remove ticket u show tickets tabu

        // Izaberemo za zonu prigradske_linije, kliknemo na Ticket for line, za liniju izaberemo 60,
        // za ticketType onetime, i pokusamo kupiti ticket
        buyTicketTab.ensureIsVisibleZone();
        new Select(buyTicketTab.getZone()).selectByVisibleText("prigradske_linije");
        buyTicketTab.ensureIsClickableTicketForLine();
        buyTicketTab.getTicketForLine().click();
        buyTicketTab.ensureIsVisibleTicketType();
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("onetime");
        buyTicketTab.ensureIsVisibleLine();
        new Select(buyTicketTab.getLine()).selectByVisibleText("35");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        // Kupimo jos jednu
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        buyTicketTab.ensureIsVisibleTicketType();
        //kupujemo kartu za danasnji datum, jer ce ona odmah poceti da vazi, i ocekujemo da necemo smeti da je obrisemo
        new Select(buyTicketTab.getTicketType()).selectByVisibleText("daily");
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();
        //Zatim pokusavamo kupiti kartu za sutrasnji datum, sem ako je danas vec poslednji dan u tekucem mesecu,
        //jer za kartu koju pocinje da vazi od sutra, ocekujemo da cemo moci da je obrisemo
        LocalDate date = LocalDate.now();
        int day = date.getDayOfMonth();
        if(day < YearMonth.of(date.getYear(), date.getMonthValue()).lengthOfMonth()) day++;
        buyTicketTab.ensureIsVisibleDate();
        buyTicketTab.setDate(day + "."+date.getMonthValue()+"."+date.getYear()+".");
        // ovo radimo da bi ngbDatepicker izgubio fokus
        //**********************************************
        passengerPage.ensureIsClickableBuyTicketTab();
        passengerPage.getBuyTicketTab().click();
        //**********************************************
        buyTicketTab.ensureIsClickableBuyTicketButton();
        buyTicketTab.getBuyTicketButton().click();
        // Otvorio se dodatni dialog za pregled i potvrdu kupovine ticket-a
        modalDilaog.ensureIsClickableConfirmPurchaseButton();
        modalDilaog.getConfirmPurchaseButton().click();
        modalDilaog.ensureIsInvisibleDialog();

        // Odlazimo na tab show tickets da bismo testirali activate, download i remove ticket
        passengerPage.ensureIsClickableShowTicketsTab();
        passengerPage.getShowTicketsTab().click();
        List<WebElement> tickets = showTicketsTab.getTickets();

        //showTicketsTab.ensureIsInVisibleToastr();

        int numOfTickets = showTicketsTab.getNumOfTickets();
        int oldNumOfTuckets = numOfTickets;

        WebElement tr;
        for(int i = 0; i < tickets.size(); i++) {
            showTicketsTab.ensureIsInvisibleToastrs();
            tr = tickets.get(i);
            List<WebElement> tds = tr.findElements(By.tagName("td"));
            WebElement lastTd = tds.get(tds.size() - 1);
            List<WebElement> buttons = showTicketsTab.getButtons(lastTd); // [activate, download, remove]

            String startDateAndTimeStr = tds.get(1).getText().trim();
            String endDateAndTimeStr = tds.get(2).getText().trim();

            assertTrue(showTicketsTab.isDownloadButton(buttons.get(1)));
            showTicketsTab.ensureIsClickableButton(buttons.get(1)); // download
            //buttons.get(1).click();

            if(startDateAndTimeStr.equals("not yet activated") && endDateAndTimeStr.equals("not yet activated")) {
                assertTrue(showTicketsTab.isRemoveButton(buttons.get(2)));
                showTicketsTab.ensureIsClickableButton(buttons.get(2)); // remove
                assertTrue(showTicketsTab.isActivateButton(buttons.get(0)));
                showTicketsTab.ensureIsClickableButton(buttons.get(0)); // activate
                buttons.get(0).click();
                showTicketsTab.ensureTextNotToBePresentInElement(tds.get(1), "not yet activated");
                showTicketsTab.ensureTextNotToBePresentInElement(tds.get(2), "not yet activated");
                assertTrue(!tds.get(1).getText().trim().equals("not yet activated")
                        && !tds.get(2).getText().trim().equals("not yet activated"));
                assertTrue(showTicketsTab.isDownloadButton(buttons.get(1)));
                showTicketsTab.ensureIsClickableButton(buttons.get(1)); // download
                //buttons.get(1).click();
                showTicketsTab.ensureIsInvisibleButton(buttons.get(2));
            }
            else {
                LocalDateTime startDateAndTime = LocalDateTime.parse(startDateAndTimeStr,dateTimeFormatter);
                LocalDateTime endDateAndTime = LocalDateTime.parse(endDateAndTimeStr, dateTimeFormatter);

                LocalDateTime currentDateTime = LocalDateTime.now();
                // 10 sec lufta
                if(startDateAndTime.isAfter(currentDateTime.minusSeconds(10))
                        || currentDateTime.isAfter(endDateAndTime)) {
                    showTicketsTab.ensureIsClickableButton(buttons.get(2)); // remove
                    assertTrue(showTicketsTab.isRemoveButton(buttons.get(2)));
                    buttons.get(2).click();
                    showTicketsTab.ensureIsInvisibleButton(buttons.get(2));
                    numOfTickets = showTicketsTab.getNumOfTickets();
                    assertEquals(oldNumOfTuckets - 1, numOfTickets);
                    oldNumOfTuckets = numOfTickets;
                }
                else {
                    assertFalse(buttons.get(2).getAttribute("class").contains("btn-danger")); // remove dugme disable-ovano
                    assertTrue(buttons.get(2).getAttribute("class").contains("btn-secondary"));
                }
            }

        }
    }

    @Test
    public void changeAccountType() throws UnsupportedEncodingException {
        passengerPage.ensureIsClickableChangeAccountTypeTab();
        passengerPage.getChangeAccountTypeTab().click();

        changeAccountTypeTab.ensureIsVisibleCurrentPassengerType();
        String currentPassengerType = changeAccountTypeTab.getCurrentPassengerType().getText().trim();

        String[] passengerTypes = {"STUDENT", "PENSIONER"};
        String selectedPassengerType;
        if(currentPassengerType.equals(passengerTypes[0])) selectedPassengerType = passengerTypes[1];
        else selectedPassengerType = passengerTypes[0];

        changeAccountTypeTab.ensureIsVisiblePassengerType();
        new Select(changeAccountTypeTab.getPassengerType()).selectByVisibleText(selectedPassengerType);

        String imageRelativePath = "/images/confirmation.jpg";
        String absolutePath = new File(URLDecoder.decode(getClass().getResource(imageRelativePath).getFile(),"UTF-8"))
                                    .getAbsolutePath();
        System.out.println(absolutePath);
        changeAccountTypeTab.setConfirmation(absolutePath);

        changeAccountTypeTab.ensureIsInvisibleToastrs();

        changeAccountTypeTab.ensureIsClickableSendRequestForChangingButton();
        changeAccountTypeTab.getSendRequestForChangingButton().click();

        changeAccountTypeTab.ensureIsVisibleToastrs();
        assertEquals("Successfully changed account type!",
                changeAccountTypeTab.getToastrs().get(0).getText().trim());


    }

    @After
    public void tearDown() throws Exception {
        // Shutdown the browser
        //if(browser != null) browser.quit();
    }
}
