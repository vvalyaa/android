import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.CRC32;

public class Main {
    private static final int MAX_PASSWORD_LENGTH = 20; // Максимальная длина пароля (слова + числа).
    private static final int MAX_NUMBER = 9999; // Максимальное число для численной части пароля.

    private static final String DICTIONARY_FILE = "10k-most-common.txt"; // Путь к файлу со словарем.

    public static void main(String[] args) {
        long targetHash = 0x0BA02B6E1L; // Замените на ваш CRC32 хеш исходного пароля.

        crackPassword(targetHash);
    }

    private static void crackPassword(long targetHash) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY_FILE))) {
            String word;
            while ((word = reader.readLine()) != null) {
                for (int number = 1; number <= MAX_NUMBER; number++) {
                    String password = word + number;
                    long passwordHash = getPasswordHash(password);

                    if (passwordHash == targetHash) {
                        System.out.println("Password found: " + password);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Password not found.");
    }

    private static long getPasswordHash(String password) {
        CRC32 crc32 = new CRC32();
        crc32.update(password.getBytes());
        return crc32.getValue();
    }
}
