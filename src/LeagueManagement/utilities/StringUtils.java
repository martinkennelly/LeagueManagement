package LeagueManagement.utilities;

public class StringUtils {
    /** Validates user inputted username or password only allowing upper and lowercase A to Z,
     * underscore and hyphen with a minimum length of minLength and max length maxLength
     * @param input User submitted username or password to be validated
     * @param minLength Minimum allowable length string
     * @param maxLength Maximum allowable length string
     * @return boolean, true if string conforms to requirements, otherwise false
     */
    public static boolean validateUsernamePassword(String input, int minLength, int maxLength){
        if (minLength > maxLength || minLength < 1 ) {
            throw new RuntimeException("Incorrect min or max length parameters");
        }
        boolean result = false;
        if (!(input == null)) {
            String pattern = "[a-zA-Z0-9_\\-]{" + minLength + "," + maxLength + "}";
            result = input.matches(pattern);
        }
        return result;
    }

    /** Joins a String array into a single string comma-seperated and returns it
     *
     * @param stringArray String array
     * @return String, combined array, comma-seperated
     */
    public static String joinArrayStringForCSV(String[] stringArray) {
        String joinedString = "";
        if (stringArray != null && stringArray.length > 0) {
            for (int i = 0; i < stringArray.length; i++) {
                joinedString += "\""+ stringArray[i] +"\"" + ",";
            }
        }
        return joinedString.length() > 0 ? joinedString.substring(0,joinedString.length()-1) : joinedString;
    }
}
