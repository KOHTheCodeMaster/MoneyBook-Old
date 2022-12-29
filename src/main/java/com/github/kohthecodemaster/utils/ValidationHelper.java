package com.github.kohthecodemaster.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValidationHelper {


    public static void validateJsonFiles(File... jsonFiles) {

        List<File> missingFiles = new ArrayList<>();

        Arrays.stream(jsonFiles).forEach(file -> {
            if (!file.isFile()) missingFiles.add(file);
        });

        if (!missingFiles.isEmpty()) {
            System.out.println("Json Files Validation Failed.\n" +
                               "Following Json Files are missing:\n");
            missingFiles.forEach(file -> System.out.println(file.getAbsolutePath()));
        }

        /*
            accounts.json
            credit-cards.json
            transactions-acc.json
            transactions-card-swipe.json
         */


        Arrays.stream(jsonFiles).forEach(file -> {

            try {

                String fileName = file.getName();
                switch (fileName) {
                    case "accounts.json":
                        validateAccountJsonFile(file);
                        break;
                    case "credit-cards.json":
                        break;
                    case "transactions-acc.json":
                        break;
                    case "transactions-card-swipe.json":
                        break;
                }

            } catch (Exception e) {
                System.out.println("Exception - " + e.getMessage());
                e.printStackTrace();
            }

        });

    }

    private static void validateAccountJsonFile(File file) throws FileNotFoundException {

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
                return;
            }
        }

        System.out.println(file.getName() + " - Validation Complete.");

    }
}
