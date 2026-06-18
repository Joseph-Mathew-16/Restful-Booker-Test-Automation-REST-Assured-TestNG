package utilities;

import org.testng.Assert;
import org.testng.Reporter;
import testbase.TestBase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private static Properties properties;
    public String url;
    public int retryLogicMaxRetries;
    public int reportLogLevel;

    /**
     * Constructor for the Configuration class. It loads the configuration
     * properties from the configuration.properties file and initializes the static
     * variables with the corresponding values.
     */
    public Configuration(TestBase testBase) {

        properties = new Properties();
        try {
            properties.load(new FileInputStream("./src/test/resources/configuration.properties"));
            url = properties.getProperty("url");

            retryLogicMaxRetries = Integer.parseInt(properties.getProperty("retry-logic.max-retries"));

            reportLogLevel = Integer.parseInt(properties.getProperty("report.log-level"));
        } catch (FileNotFoundException fileNotFoundException) {
            String errorMessage = "configuration.properties file could not be opened because the file does not exist or due to some other reason.";
            //TODO: Change reporter method.
            Reporter.log(errorMessage, true);
            Assert.fail(errorMessage, fileNotFoundException);
        } catch (IOException ioException) {
            String errorMessage = "An error occurred when reading from the input stream for configuration.properties files.";
            //TODO: Change reporter method.
            Reporter.log(errorMessage, true);
            Assert.fail(errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            String errorMessage = "An error occurred due to the input stream for configuration.properties file containing a malformed Unicode escape sequence";
            //TODO: Change reporter method.
            Reporter.log(errorMessage, true);
            Assert.fail(errorMessage, illegalArgumentException);
        } catch (NullPointerException nullPointerException) {
            String errorMessage = "An error occured as the input stream for configuration.properties file is null.";
            //TODO: Change reporter method.
            Reporter.log(errorMessage, true);
            Assert.fail(errorMessage, nullPointerException);
        }
    }
}

