package dataproviders;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;
import utilities.Step;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.List;

public class CSVFileReader {

    /**
     * Reads a CSV file from a specified folder within the test-data folder in resources.
     * Returns a 2-dimensional Object array suitable for a TestNG DataProvider.
     *
     * @param folderPath The subfolder within test-data.
     * @param fileName   The CSV file name.
     * @return 2D Object array of CSV data (skips header).
     */
    public Object[][] csvReader(String folderPath, String fileName) {

        // Prepares the file path. Also sanitizes the path using .normalize() method.
        String path = Paths.get("src", "test", "resources", "test-data", folderPath, fileName).normalize().toString();

        try {
            // Reads the CSV file.
            Reader reader = new FileReader(path);
            CSVParser csvParser = CSVParser.builder().setReader(reader).setFormat(CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).get()).get();

            List<CSVRecord> records = csvParser.getRecords();
            // Checks if the file is empty.
            if (records.size() == 0) {
                String errorMessage = "The test data CSV file is empty. Folder path: " + folderPath + ". File name: " + fileName + ".";
                Step.step(errorMessage);
            }

            // Store the data from the test data file into the Object array.
            int rowCount = records.size();
            int columnCount = records.getFirst().size();
            Object[][] data = new Object[rowCount][columnCount];
            for (int rowIteration = 0; rowIteration < rowCount; rowIteration++) {
                for (int columnIteration = 0; columnIteration < columnCount; columnIteration++) {
                    data[rowIteration][columnIteration] = records.get(rowIteration).get(columnIteration);
                }
            }
            return data;


        } catch (FileNotFoundException fileNotFoundException) {
            String errorMessage = "The specified test data CSV file either does not exist, is a directory instead of a file, or can't be read for some other reason. Folder path: " + folderPath + ". File name: " + fileName + ".";
            Step.step(errorMessage);
            Assert.fail(errorMessage, fileNotFoundException);
        } catch (IOException ioException) {
            String errorMessage = "An IO exception occurred while parsing the test data CSV file. Folder path: " + folderPath + ". File name: " + fileName + ".";
            Step.step(errorMessage);
            Assert.fail(errorMessage, ioException);
        }

        // This code should be inaccessible. An error is placed in case of any unexpected scenarios.
        String errorMessage = "An unexpected error appeared while reading test data CSV file. Folder path: " + folderPath + ". File name: " + fileName + ".";
        Step.step(errorMessage);
        Assert.fail(errorMessage);
        return null;
    }

}
