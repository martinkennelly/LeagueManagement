package LeagueManagement.utilities;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

/**
 * Class for salt + hashing encoding and decoding passwords. Reasons for salt is to protect
 * against a rainbow table attack.
 * Robbed it from somewhere but dont know where, must get reference. Have used it in test programs
 */
public class Password {
    private final static int iterations = 20 * 2000;
    private final static int saltLen = 20;
    private final static int desiredKeyLen = 256;

    /** Calculates salted PRNG hash of the given parameter password.
     *
     * @param password Password to be salted + hashed
     * @return String of salt + hash ready to be stored in our collection of user names + passwords
     * @throws Exception
     */
    public static String getSaltedHash(String password) throws Exception {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Can not hash an empty password");
        }
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        return Base64.encodeBase64String(salt) + "$" + hash(password,salt);
    }

    /** Calculates a hash of the parameter password
     *
     * @param password Password to be hashed
     * @param salt Protects against dictionary /rowbow table attacks
     * @return  string base64 representation of the hash
     * @throws Exception
     */
    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length()==0) {
            throw new IllegalArgumentException("Empty Passwords are not acceptable");
        }
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(),salt,iterations,desiredKeyLen));
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * Method for checking a string password against a salted hash to see if it matches. If it does, the method
     * will return true.
     * @param password Password to be checked against our salted hash
     * @param storedSaltedHash Stored salted hash
     * @return boolean true if password is corrent, otherwise false
     * @throws Exception
     */
    public static boolean checkPassword(String password, String storedSaltedHash) throws Exception{
        String[] saltAndPass = storedSaltedHash.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException("The salt and hash that was saved is causing an error");
        }
        String hashOfInput = hash(password,Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }

    public static void main(String[] args) throws Exception{
        //System.out.println(getSaltedHash("adminadmin"));
        System.out.println(checkPassword("adminadmin","TPtJ2sW0I8gdV/9uwIHy1D22hBA=$Pjzh5halkeldb19Bdgg1AoJATdOZFCQC5pOR6rM6J/w="));
    }
}
