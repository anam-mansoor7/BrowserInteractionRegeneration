package pk.edu.lums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.lang3.NotImplementedException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Loader {
	private WebDriver driver;

	public Loader() {
		super();
		System.setProperty("webdriver.chrome.driver", Constants.BASE_PATH
				+ "chromedriver.exe");
		driver = new ChromeDriver(capabilities());
		driver.manage().timeouts()
				.pageLoadTimeout(Constants.WAIT_30, TimeUnit.SECONDS);
	}

	private DesiredCapabilities capabilities() {
		DesiredCapabilities capability = DesiredCapabilities.chrome();
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.FINEST);
		capability.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("-incognito");
		capability.setCapability(ChromeOptions.CAPABILITY, options);
		return capability;
	}

	public void load(String url) {
		driver.get(url);
		waitForLoad(driver);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void waitFor(ExpectedCondition cond, Long seconds) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(cond);
	}

	private void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(pageLoadCondition);
	}

	public void quit() {
		driver.close();
		driver.quit();
	}

	public List<Node> getAutomatedCalls() {
		Logs logs = driver.manage().logs();
		LogEntries logEntries4 = logs.get(LogType.PERFORMANCE);

		return parseSentRequestLogs(logEntries4);
	}

	private List<Node> parseSentRequestLogs(LogEntries logEntries) {

		List<LogEntry> allEntries = logEntries.getAll();

		List<Node> nodes = new ArrayList<Node>(allEntries.size());

		try {

			for (LogEntry logEntry : allEntries) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode readValue;

				readValue = mapper.readValue(logEntry.getMessage(),
						JsonNode.class);

				if ("Network.requestWillBeSent".equals(readValue.get("message")
						.get("method").asText().trim())) {

					String timestamp = readValue.get("message").get("params")
							.get("timestamp").asText();
					String url = readValue.get("message").get("params")
							.get("request").get("url").asText();

					nodes.add(new Node(url, Double.valueOf(timestamp)
							.longValue()));
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return nodes;
	}

	// TODO get the WebElement out as well. Put the elements out directly.
	// TODO there would be common elements in these elements and clickable.
	public List<Node> getAnchors() {

		List<WebElement> anchors = driver.findElements(By.tagName("a"));
		String currentUrl = driver.getCurrentUrl();

		List<Node> nodes = new ArrayList<Node>(anchors.size());

		Long timestamp = 0l;
		for (WebElement anchor : anchors) {
			String url = anchor.getAttribute("href");
			if (url != null && currentUrl != null && !url.startsWith("http")) {
				String mid = currentUrl.endsWith("/") || url.startsWith("/") ? ""
						: "/";
				url = currentUrl + mid + url;
			}

			nodes.add(new Node(url, ++timestamp));
		}

		return nodes;
	}

	public List<Node> getClickables() {
		throw new NotImplementedException(
				"This would be added in 2nd iteration");
	}
}
