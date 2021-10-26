import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.NoSuchElementException;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class HeadHunterBot {
    private final static String KEY_DRIVER = "webdriver.chrome.driver";
    private final static String VALUE_DRIVER = "C:\\Users\\comp\\IdeaProjects\\chromedriver.exe";   // необходимо указать путь до chromedriver.exe
    private final static String URL = "https://hh.ru/account/login?backurl=%2F";
    private final static String LOGIN = "********";                     // указываем логин и
    private final static String PASSWORD = "********";                  // пароль от личного кабинета на hh.ru
    private final static String VACANCY = "Java junior";                // указываем желаемую вакансию
    private final static String MESSAGE = "Привет! Обращаюсь к тебе посредством искусственного интеллекта (назовём его Василием), разработанного собственноручно:)\n" +
            "Василий авторизуется в моем личном кабинете на hh.ru и осуществляет поиск и отклик на вакансии с должностью Java-разработчик(junior).\n" +
            "Рассчитываю на плодотворное сотрудничество.\n" +
            "Спасибо за внимание:)\n" +
            "\n" +
            "*Буду признателен за обратную связь и в том случае, если вдруг Василий откликнется на неподходящую вакансию, с целью избежать подобных ситуаций в будущем.";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty(KEY_DRIVER, VALUE_DRIVER);
        ChromeDriver driver = new ChromeDriver();
        driver.get(URL);

        driver.findElement(By.id("HH-React-Root")).findElement(By.className("bloko-link-switch")).click();
        driver.findElement(By.className("bloko-form-item"))
                .findElement(By.className("bloko-input"))
                .sendKeys(LOGIN);

        List<WebElement> elements;
        elements = driver.findElements(By.className("bloko-form-item"));
        elements.get(1).findElement(By.className("bloko-input"))
                .sendKeys(PASSWORD);

        driver.findElement(with(By.className("bloko-button")).below(driver.findElement(By.className("bloko-form-item")))).click();

        Thread.sleep(1000);
        driver.findElement(By.className("bloko-input")).sendKeys(VACANCY + Keys.ENTER);
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("#HH-React-Root > div > div > div.sticky-container > div:nth-child(1)" +
                " > div > div > div.search-filters > div.search-filters__sorts > div:nth-child(2) > div > div")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("body > div.bloko-drop.bloko-drop_menu.bloko-drop_theme-light.bloko-drop_layer-above-content" +
                ".bloko-drop_flexible.bloko-drop_fullscreen-on-xs.bloko-drop_clickable.bloko-drop_bottom > div > div > div:nth-child(5)")).click();

        Thread.sleep(1000);
        driver.findElement(By.cssSelector("body > div.HH-Supernova-Search-Container.supernova-navi-search-wrapper.supernova-navi-search-wrapper_expanded" +
                ".supernova-navi-search-wrapper_search-page > div.supernova-navi-search.HH-SearchVacancyDropClusters-SearchForm > div > div.supernova-navi-search-columns" +
                " > div > div > form > div > div.supernova-search-group__input > div > span > a")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("body > div.HH-MainContent.HH-Supernova-MainContent > div > div.main-content > div.bloko-columns-wrapper" +
                " > div > div.bloko-column.bloko-column_xs-4.bloko-column_s-8.bloko-column_m-8.bloko-column_l-12.bloko-column_container > div > form" +
                " > div:nth-child(6) > div > div.bloko-column.bloko-column_xs-4.bloko-column_s-8.bloko-column_m-5.bloko-column_l-8 > div > div > div" +
                " > div.region-select.region-select_list > div > input")).sendKeys("Москва");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("#submit-bottom")).click();

        List<WebElement> jobs;
        jobs = driver.findElements(By.className("vacancy-serp-item"));
        System.out.println(jobs.size());

        boolean javaJunior = false;
        for (WebElement element : jobs) {
            String s = element.findElement(By.className("bloko-link")).getText();

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            javaJunior = checkingTheString(s);
            if (javaJunior == true) {
                System.out.println(s);
                Thread.sleep(1000);
                try {
                    element.findElement(By.className("bloko-button")).click();
                    Thread.sleep(1000);
                    driver.findElement(By.className("bloko-modal")).findElement(By.className("bloko-link-switch")).click();
                    Thread.sleep(1000);
                    driver.findElement(By.className("bloko-modal")).findElement(By.className("bloko-textarea")).sendKeys(MESSAGE);
                    Thread.sleep(1000);
                    driver.findElement(By.className("bloko-modal")).findElement(By.className("bloko-form-spacer")).findElement(By.className("bloko-button")).click();
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    System.out.println("Такого элемента нет или вы уже откликнулись");
                }
            }
            javaJunior = false;
        }
        driver.quit();
    }

    private static boolean checkingTheString(String s) {
        String s1 = s.toLowerCase();
        if (s1.contains("junior java") || s1.contains("java junior") || s1.contains("junior java-developer")
                || s1.contains("junior java-разработчик") || s1.contains("java (junior)")
                || s1.contains("java(junior)") || s1.contains("java-разработчик(junior)")
                || s1.contains("java разработчик(junior)") || s1.contains("младший разработчик java")
                || s1.contains("младший java") || s1.contains("программист java(junior)")) return true;
        else return false;
    }
}