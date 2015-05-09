package pk.edu.lums;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class App {
	private static final int WAIT_30 = 30;

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver",
				"E:\\Documents\\LUMS\\2015-Spring-Network Security\\Project\\chromedriver.exe");
		WebDriver driver = new ChromeDriver(caps());
		driver.manage().timeouts().pageLoadTimeout(WAIT_30, TimeUnit.SECONDS);
		driver.get("http://google.com.pk/");
		// try {
		// Thread.sleep(WAIT_30 * 1000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		// WebDriverWait wait = new WebDriverWait(driver, WAIT_30);
		// wait.until(ExpectedConditions.)
		Logs logs = driver.manage().logs();
		// LogEntries logEntries1 = logs.get(LogType.BROWSER);
		// LogEntries logEntries2 = logs.get(LogType.CLIENT);
		// LogEntries logEntries3 = logs.get(LogType.DRIVER);
		LogEntries logEntries4 = logs.get(LogType.PERFORMANCE);
		// LogEntries logEntries5 = logs.get(LogType.PROFILER);
		// LogEntries logEntries6 = logs.get(LogType.SERVER);

		// Nothing returned from the following 2
		// printLogs(logEntries1);
		// printLogs(logEntries2);
		// printLogs(logEntries3);
		printLogs(logEntries4);
		try {
			printLogReceived(logEntries4);
			printLogSent(logEntries4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// printLogs(logEntries5);
		// printLogs(logEntries6);

		System.out.println("==============================================");
		System.out.println("Hello World!");
		driver.close();
		driver.quit();
	}

	private static DesiredCapabilities caps() {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		LoggingPreferences logPrefs = new LoggingPreferences();
		// logPrefs.enable(LogType.BROWSER, Level.FINEST);
		// logPrefs.enable(LogType.CLIENT, Level.FINEST);
		// logPrefs.enable(LogType.DRIVER, Level.FINEST);
		logPrefs.enable(LogType.PERFORMANCE, Level.FINEST);
		// logPrefs.enable(LogType.PROFILER, Level.FINEST);
		// logPrefs.enable(LogType.SERVER, Level.FINEST);
		caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("-incognito");
		caps.setCapability(ChromeOptions.CAPABILITY, options);
		return caps;
	}

	private static void printLogs(LogEntries logEntries) {
		System.out.println("==============================================");

		for (LogEntry logEntry : logEntries) {
			System.out.println(logEntry.toString());
		}
	}

	private static void printLogReceived(LogEntries logEntries)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("==============================================");

		for (LogEntry logEntry : logEntries) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode readValue = null;
			readValue = mapper.readValue(logEntry.getMessage(), JsonNode.class);
			if ("Network.responseReceived".equals(readValue.get("message")
					.get("method").asText().trim())) {
				System.out.print(readValue.get("message").get("params")
						.get("timestamp").asText());
				System.out.print(" ");
				System.out.println(readValue.get("message").get("params")
						.get("response").get("url").asText());
			}
		}
	}

	private static void printLogSent(LogEntries logEntries)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("==============================================");

		for (LogEntry logEntry : logEntries) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode readValue = null;
			readValue = mapper.readValue(logEntry.getMessage(), JsonNode.class);
			if ("Network.requestWillBeSent".equals(readValue.get("message")
					.get("method").asText().trim())) {
				System.out.print(readValue.get("message").get("params")
						.get("timestamp").asText());
				System.out.print(" ");
				System.out.println(readValue.get("message").get("params")
						.get("request").get("url").asText());
			}
		}
	}
}
