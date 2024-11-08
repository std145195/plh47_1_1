import java.util.Arrays;
import java.util.Random;

public class erotima_1 {
    // Ορίζουμε τις ελάχιστες και μέγιστες τιμές για το μέγεθος του password
    private static final int PASSWORD_MIN = 8;   // Ελάχιστο μήκος password
    private static final int PASSWORD_MAX = 16;  // Μέγιστο μήκος password
    // Δημιουργία αντικειμένου Random για την παραγωγή τυχαίων αριθμών
    private static final Random RANDOM = new Random();
    // Συνολικός αριθμός χαρακτήρων (26 γράμματα για μικρά και κεφαλαία)
    private static final int TOTAL_CHAR_COUNT = 26;

    public static void main(String[] args) {
        // Ορισμός τυχαίας τιμής για το n, μεταξύ 20 και 25 (δημιουργούμε πίνακα 2^n passwords)
        int n = RANDOM.nextInt(6) + 20;
        // Δημιουργία passwords με μέγεθος 2^n
        String[] passwords = generatePasswords((int) Math.pow(2, n));
        // Debugging: εκτύπωση του n και του πλήθους των passwords (προαιρετικά)
        // System.out.println("\nΤο n είναι:" + n);  // Fixme: αφαιρέστε για παραγωγή
        // System.out.println("Οι γραμμές/passwords του πίνακα είναι:" + passwords.length);

        // Ορισμός των διαφόρων αριθμών νημάτων (1, 2, 4, 8)
        int[] threadCounts = {1, 2, 4, 8};

        // Επανάληψη για κάθε πλήθος νημάτων
        for (int threadCount : threadCounts) {
            System.out.println("\n--- Εκτέλεση με " + threadCount + " νήματα ---");

            // Καταγραφή του χρόνου έναρξης
            long startTime = System.currentTimeMillis();

            // Δημιουργία πίνακα για την καταμέτρηση χαρακτήρων (52 θέσεις, 26 μικρά και 26 κεφαλαία)
            int[] mainCounter = new int[52];

            // Δημιουργία πίνακα νημάτων με το πλήθος των νημάτων
            ProcessThread[] threads = new ProcessThread[threadCount];
            // Διαίρεση του πίνακα passwords σε υποπίνακες με βάση τον αριθμό νημάτων
            int subarrays = passwords.length / threadCount;

            // Δημιουργία και εκκίνηση των νημάτων
            for (int i = 0; i < threadCount; i++) {
                int start = i * subarrays;  // Αρχή του υποπίνακα για το νήμα
                int end = (i + 1) * subarrays;  // Τέλος του υποπίνακα για το νήμα
                if (i == threadCount - 1) {  // Αν είναι το τελευταίο νήμα, παίρνει τα υπόλοιπα
                    end = passwords.length;
                }

                // Δημιουργία νήματος που αναλαμβάνει μέρος των passwords
                threads[i] = new ProcessThread(Arrays.copyOfRange(passwords, start, end));
                threads[i].start();  // Εκκίνηση νήματος
            }

            // Αναμονή για την ολοκλήρωση όλων των νημάτων (join)
            for (ProcessThread thread : threads) {
                try {
                    thread.join();  // Αναμονή για το τέλος του νήματος
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Συγχώνευση των αποτελεσμάτων από όλα τα νήματα
            for (ProcessThread processThread : threads) {
                for (int j = 0; j < mainCounter.length; j++) {
                    mainCounter[j] += processThread.getCounter()[j];  // Προσθήκη αποτελεσμάτων από κάθε νήμα
                }
            }

            // Καταγραφή του χρόνου τερματισμού
            long endTime = System.currentTimeMillis();
            // Υπολογισμός του χρόνου εκτέλεσης σε χιλιοστά του δευτερολέπτου (ms)
            double xiliosta = (endTime - startTime);
            // Εκτύπωση του χρόνου εκτέλεσης
            System.out.printf("Χρόνος εκτέλεσης: %.0f ms\n", xiliosta);
            System.out.println("-------------------------------------------");

            // Εκτύπωση των συχνοτήτων των χαρακτήρων
            System.out.printf("%-20s %-20s\n", "Κεφαλαία", "Μικρά");
            System.out.println("-------------------------------------------");

            // Επανάληψη για κάθε γράμμα (0-25 για μικρά, 26-51 για κεφαλαία)
            for (int i = 0; i < 26; i++) {
                char uppercase = (char) ('A' + i);  // Κεφαλαίο γράμμα
                char lowercase = (char) ('a' + i);  // Μικρό γράμμα

                // Εκτύπωση κεφαλαίου και μικρού γράμματος μαζί με τις συχνότητές τους
                System.out.printf("%-2c %-10d %5s %-2c %-10d\n",
                        uppercase, mainCounter[i + 26], "", lowercase, mainCounter[i]);
            }
            System.out.println("-------------------------------------------");
        }
    }

    // Δημιουργεί έναν πίνακα με τυχαία passwords
    private static String[] generatePasswords(int size) {
        String[] passwords = new String[size];
        for (int i = 0; i < size; i++) {
            passwords[i] = randomPassword();  // Δημιουργία τυχαίου password για κάθε θέση
        }
        return passwords;
    }

    // Δημιουργεί ένα τυχαίο password
    private static String randomPassword() {
        // Επιλογή τυχαίου μήκους για το password (από 8 έως 16 χαρακτήρες)
        int size = RANDOM.nextInt(PASSWORD_MAX - PASSWORD_MIN + 1) + PASSWORD_MIN;
        // Χρήση StringBuilder για τη δημιουργία του password
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            // Τυχαία επιλογή μικρού ή κεφαλαίου γράμματος
            if (RANDOM.nextBoolean()) {
                char c = (char) ('a' + RANDOM.nextInt(26));  // Μικρό γράμμα
                sb.append(c);
            } else {
                char c = (char) ('A' + RANDOM.nextInt(26));  // Κεφαλαίο γράμμα
                sb.append(c);
            }
        }
        return sb.toString();  // Επιστροφή του τυχαίου password
    }

    // Νήμα που επεξεργάζεται ένα τμήμα του πίνακα passwords και μετράει τους χαρακτήρες
    static class ProcessThread extends Thread {
        private final String[] passwords;
        // Πίνακας για την καταμέτρηση χαρακτήρων (52 θέσεις για μικρά και κεφαλαία)
        private final int[] counter = new int[TOTAL_CHAR_COUNT * 2];

        public ProcessThread(String[] passwords) {
            this.passwords = passwords;  // Ορίζουμε τα passwords που θα επεξεργαστεί το νήμα
        }

        @Override
        public void run() {
            // Επανάληψη για κάθε password που επεξεργάζεται το νήμα
            for (String password : passwords) {
                // Επανάληψη για κάθε χαρακτήρα του password
                for (char c : password.toCharArray()) {
                    // Αν είναι μικρό γράμμα, αυξάνουμε την αντίστοιχη θέση στον πίνακα
                    if (c >= 'a' && c <= 'z') {
                        int index = c - 'a';
                        counter[index]++;
                    }
                    // Αν είναι κεφαλαίο γράμμα, αυξάνουμε την αντίστοιχη θέση
                    else if (c >= 'A' && c <= 'Z') {
                        int index = (c - 'A') + TOTAL_CHAR_COUNT;
                        counter[index]++;
                    }
                }
            }
        }

        // Μέθοδος για να επιστρέψει το αποτέλεσμα του counter για αυτό το νήμα
        public int[] getCounter() {
            return counter;
        }
    }
}