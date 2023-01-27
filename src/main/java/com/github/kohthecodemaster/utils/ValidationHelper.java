package com.github.kohthecodemaster.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ValidationHelper {


    public static boolean validateJsonFiles(Set<String> accountSet, Set<String> creditCardSet, File... jsonFiles) {

        boolean isValid = true;
        List<File> invalidJsonFiles = new ArrayList<>();

        Arrays.stream(jsonFiles).forEach(file -> {
            if (!file.isFile()) invalidJsonFiles.add(file);
        });

        if (!invalidJsonFiles.isEmpty()) {
            System.out.println("Json Files Validation Failed.\n" +
                               "Following Json Files are missing:\n");
            invalidJsonFiles.forEach(file -> System.out.println(file.getAbsolutePath()));
            return false;
        }

        /*
            accounts.json
            credit-cards.json
            transactions-acc.json
            transactions-card-swipe.json
         */


        for (File file : jsonFiles) {
            try {

                String fileName = file.getName();
                switch (fileName) {
                    case "accounts.json":
                        isValid &= validateAccountJsonFile(file);
                        break;
                    case "credit-cards.json":
                        break;
                    case "transactions-acc.json":
                        isValid &= validateTransactionAccJsonFile(file, accountSet, creditCardSet);
                        break;
                    case "transactions-card-swipe.json":
                        break;
                }

            } catch (Exception e) {
                System.out.println("Exception - " + e.getMessage());
                e.printStackTrace();
            }

        }

        return isValid;
    }

    private static boolean validateTransactionAccJsonFile(File file, Set<String> accountSet, Set<String> creditCardSet) throws FileNotFoundException {

        boolean isValid = true;
        System.out.println("Validating - " + file.getName());

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {
        }.getType();

        ArrayList<Map<String, String>> list = gson.fromJson(new FileReader(file), type);

        boolean validKeys;
        String key1 = "Id";
        String key2 = "Date";
        String key3 = "Source Account";
        String key4 = "Target Account";
        String key5 = "Category";
        String key6 = "Amount";
        String key7 = "Note";
        String key8 = "Credit Card Description";
        String key9 = "Narration";

        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            String[] keys = map.keySet().toArray(new String[0]);
            validKeys = keys[0].equals(key1) &&
                        keys[1].equals(key2) &&
                        keys[2].equals(key3) &&
                        keys[3].equals(key4) &&
                        keys[4].equals(key5) &&
                        keys[5].equals(key6) &&
                        keys[6].equals(key7) &&
                        keys[7].equals(key8) &&
                        keys[8].equals(key9);
            if (!validKeys) {
                System.out.println("\nValidation Failed for entry [" + (i + 1) + "] - Keys Mismatch.\n" +
                                   "Expected Keys -> " + key1 + ", " + key2 + ", " + key3 +
                                   key4 + ", " + key5 + ", " + key6 + ", " +
                                   key7 + ", " + key8 + ", " + key9 + "\n" +
                                   "Found Keys -> " + keys[0] + ", " + keys[1] + ", " + keys[2] +
                                   keys[3] + ", " + keys[4] + ", " + keys[5] + ", " +
                                   keys[6] + ", " + keys[7] + ", " + keys[8] + "\n"
                );
                isValid = false;
                continue;
            }

            //  Validating Id
            try {
                Integer.parseInt(map.get(keys[0]));
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Failed for entry [" + (i + 1) + "] - Invalid Id.\n" +
                                   "Expected Id should be an Integer value.\n" +
                                   "Found Id -> " + keys[0]);
                e.printStackTrace();
                isValid = false;
            }

            //  Validating date
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            try {
                dateTimeFormatter.parse(map.get(keys[1]));
            } catch (DateTimeParseException e) {
                System.out.println("\nValidation Failed for entry [" + (i + 1) + "] - Invalid Date.\n" +
                                   "Expected Date Format -> " + dateTimeFormatter + "\n" +
                                   "Found Date -> " + keys[0]);

                isValid = false;
            }

            //  Validate Source Account
            String sourceAccount = map.get(keys[2]);
            if (!accountSet.contains(sourceAccount) && !creditCardSet.contains(sourceAccount)) {
                System.out.println("\nValidation Failed for entry [" + (i + 1) + "] - Invalid Source Account.\n" +
                                   "Found Source Account -> " + sourceAccount);
                isValid = false;
            }

            //  Validate Target Account
            String targetAccount = map.get(keys[3]);
            if (!accountSet.contains(targetAccount) && !creditCardSet.contains(targetAccount)) {
                System.out.println("\nValidation Failed for entry [" + (i + 1) + "] - Invalid Target Account.\n" +
                                   "Found Target Account -> " + targetAccount);
                isValid = false;
            }

            //  Skipped Validating Category as it's a String

            //  Validating Amount
            try {
                Double.parseDouble(map.get(keys[5]));
            } catch (NumberFormatException e) {
                System.out.println("\nValidation Failed for entry [" + (i + 1) + "] - Invalid Amount.\n" +
                                   "Expected Amount should be an Integer value.\n" +
                                   "Found Amount -> " + map.get(keys[5]));
                isValid = false;
            }

            //  Skipped Validating Note, Credit Card Description & Narration as they're all String

        }

        if (isValid) System.out.println(file.getName() + " - Validation Complete.");

        return isValid;
    }

    private static boolean validateAccountJsonFile(File file) throws FileNotFoundException {

        System.out.println("Validating - " + file.getName());

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {
        }.getType();

        ArrayList<Map<String, String>> list = gson.fromJson(new FileReader(file), type);

        boolean validKeys;
        String key1 = "Id";
        String key2 = "Name";
        String key3 = "Balance";

        for (Map<String, String> map : list) {
            String[] keys = map.keySet().toArray(new String[0]);
            validKeys = keys[0].equals(key1) &&
                        keys[1].equals(key2) &&
                        keys[2].equals(key3);
            if (!validKeys) {
                System.out.println("Validation Failed - Keys Mismatch.\n" +
                                   "Expected Keys -> " + key1 + ", " + key2 + ", " + key3 + "\n" +
                                   "Found Keys -> " + keys[0] + ", " + keys[1] + ", " + keys[2] + "\n"
                );
                return false;
            }
        }

        System.out.println(file.getName() + " - Validation Complete.");
        return true;

    }
}
