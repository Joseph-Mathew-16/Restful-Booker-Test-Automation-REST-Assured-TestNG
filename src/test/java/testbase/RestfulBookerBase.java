package testbase;

import dataproviders.CSVFileReader;

public class RestfulBookerBase extends TestBase {

    public Object[][] csvReader(String folderPath, String fileName) {
        return new CSVFileReader().csvReader(this, folderPath, fileName);
    }
}
