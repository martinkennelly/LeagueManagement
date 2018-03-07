package LeagueManagement.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class for performing file or directory manipulations
 */
public class FileUtils {
    /** Checks if a file or directory exists, returns true if it does exists otherwise false.
     *
     * @param file - File object containing the path to the file or directory.
     * @return boolean, true if the file or directory exists otherwise false.
     */
    public static boolean checkExists(File file) {
        boolean exists = false;
        try {
            Path filePath = file.toPath().toRealPath();
            exists = Files.exists(filePath);
        } catch (IOException iox) { }
        return exists;
    }

    /** Check if an array of String paths exists
     *
     * @param names String array of file or directory paths.
     * @return boolean, true if files or directories exists, otherwise false.
     */
    public static boolean checkExists(String[] names) {
        String basePath = ".//";
        boolean success = true;
        for (String name : names) {
            File file = new File(basePath.concat(name));
            if (!checkExists(file)) {
                success = false;
            }
        }
        return success;
    }

    /** Check if a string path to a file or directory exists
     *
     * @param name path to a file or directory
     * @return boolean, true if the file or directory exists, else false.
     */
    public static boolean checkExists(String name) {
        return checkExists(new String[]{name});
    }

    /** Creates a directory or any subdirectories if theyre missing.
     * @param file File with the appropriate directorie path
     * @return boolean, true if directories are created, otherwise false
     */
    public static boolean createDirectory(File file) {
        return file.mkdirs();
    }

    /** Recursively deletes directorys and its contents or individual files. If a file is in use, it will not
     *  be able to delete the file and fails without giving an error.
     * @param file file object pointing to either directory or file
     */
    private static void deleteThing(File file) {
        File[] directoryContents = file.listFiles();
        if (directoryContents != null) {
            for (File each : directoryContents) {
                deleteThing(each);
            }
        }
        file.delete();
    }

    /** Deletes either a file or directory
     *
     * @param file File object pointing to either a directory or file
     * @return boolean, returns true if deletion was successful otherwise false.
     */
    public static boolean delete(File file) {
        deleteThing(file);
        return !file.exists();
    }

    /** Append a line of text to a file. If the file doesnt exists, it creates it.
     *
     * @param file File object descripting the file to append to
     * @param textToAppend String of text to append
     * @return boolean, true if write to file was successful otherwise false.
     */
    public static boolean appendLineToEndOfFile(File file, String textToAppend) {
        boolean success = false;
        if (file != null && textToAppend != null) {
            if (!checkExists(file)) {
                try {
                    file.createNewFile();
                } catch (IOException iox) {
                    throw new RuntimeException("Unable to create file");
                }
            }
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.printf("%s%s", textToAppend,System.lineSeparator());
                printWriter.close();
                fileWriter.close();
                success = true;
            } catch (IOException iox) { }
        }
        return success;
    }

    public static boolean writeFile(File file, String fileData) {
        boolean success = false;
        if (!(file == null || fileData == null)) {
            try {
                FileWriter fileWriter = new FileWriter(file,false);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.printf("%s%s",fileData,System.lineSeparator());
                printWriter.close();
                fileWriter.close();
                success = true;
            } catch (IOException iox) { }
        }
        return success;
    }
}
