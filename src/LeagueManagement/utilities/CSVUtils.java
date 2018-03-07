package LeagueManagement.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CSVUtils {
    private static final char DEFAULT_SEPERATOR = ',';

    public static String[] parseLine(String line){
        Pattern matchPattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
        String[] splitLine = matchPattern.split(line);
        for (int i = 0; i < splitLine.length; i++){
            splitLine[i] = splitLine[i].replaceAll("\"","");
        }
        return splitLine;
    }

    /** Replace a line in a csv file referenced by column number and a value to distinguist the row.
     *
     * @param file File object where file is locaed
     * @param lineToReplace Line that will replace the line that is found. Make sure this is comma seperated.
     * @param id Value to distinquish the row(s) to replace.
     * @param colNoForId Column number to replace. From one to how many columns exist in the csv file.
     * @return boolean, true if a line was found to match the criteria and also that it was successfully replaced, otherwise false.
     * @throws FileNotFoundException
     */
    public static boolean replaceLine(File file, String lineToReplace, String id, int colNoForId) throws FileNotFoundException {
        boolean isLineFound = false;
        boolean isLineFoundCurrentLine = false;
        Scanner scanner;
        String[] lineSplit;
        String totalFile = "";
        if (file != null && FileUtils.checkExists(file) && lineToReplace != null && colNoForId > 0 && id != null) {
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                lineSplit = CSVUtils.parseLine(scanner.nextLine());
                if (colNoForId <= lineSplit.length) {
                    if (lineSplit[colNoForId - 1].equalsIgnoreCase(id)) {
                        isLineFound = true;
                        isLineFoundCurrentLine = true;
                    }
                }
                if (isLineFoundCurrentLine) {
                    totalFile += lineToReplace + System.lineSeparator();
                    isLineFoundCurrentLine = false;

                } else {
                    totalFile += StringUtils.joinArrayStringForCSV(lineSplit) + System.lineSeparator();
                }
            }
        }
        return isLineFound ? FileUtils.writeFile(file,totalFile) : false;
    }

    /** Reads in CSV File and returns a multidimential arraylist. Set forceSameRowLength to
     *  true if all rows must have the same amount of columns.
     * @param file file object points to the csv file.
     * @param forceSameRowLength boolean, true if all rows must have the same amounts of columns
     * @return 2-d arraylist of csv file
     */
    public static ArrayList<ArrayList<String>> readInCSV(File file, boolean forceSameRowLength) {
        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
        Scanner scanner;
        if (FileUtils.checkExists(file)) {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    ArrayList<String> rowArray = new ArrayList<String>();
                    String[] parsedLine = CSVUtils.parseLine(scanner.nextLine());
                    rowArray.addAll(Arrays.asList(parsedLine));
                    table.add(rowArray);
                }
                scanner.close();
            } catch (FileNotFoundException fnex) {}
            if (forceSameRowLength && table.size() > 0) {
                int rowSize = table.get(0).size();
                int otherRowSize;
                for (int row = 1; row < table.size(); row++) {
                    otherRowSize = table.get(row).size();
                    if (rowSize != otherRowSize) {
                        throw new RuntimeException("Row sizes are not consistent in data");
                    }
                }
            }
        }
        return table;
    }
}
