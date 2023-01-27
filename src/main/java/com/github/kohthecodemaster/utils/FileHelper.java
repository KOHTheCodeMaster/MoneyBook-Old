package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;
import stdlib.utils.KOHFilesUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileHelper {

    public static void saveListToJson(String accountName, List<TransactionPojo> transactionPojoList, File tempDir, int thresholdLimit) {

        //  Save the list to Json File ONLY when the size is greater than TEMP_JSON_FILE_THRESHOLD_LIMIT
        if (transactionPojoList.size() > thresholdLimit) {
            File tempJsonFile = new File(tempDir.getAbsolutePath() + "/" + accountName, accountName + " - " + System.currentTimeMillis() + ".json");
            JsonController.saveListToJsonFile(transactionPojoList, tempJsonFile);
            transactionPojoList.clear();
        }

    }

    public static void saveListToJsonWithoutThresholdCheck(String accountName, List<TransactionPojo> transactionPojoList, File tempDir) {

        File tempJsonFile = new File(tempDir.getAbsolutePath() + "/" + accountName, accountName + " - " + System.currentTimeMillis() + ".json");
        JsonController.saveListToJsonFile(transactionPojoList, tempJsonFile);
        transactionPojoList.clear();

    }

    public static void initializeDirs(Map<String, AccountPojo> accountMap, Map<String, CreditCardPojo> creditCardsMap, File... dirs) {

        try {

            for (File dir : dirs) {
                if (dir.isDirectory()) deleteDirectoryRecursively(dir);
                Files.createDirectory(dir.toPath());
            }

            for (Map.Entry<String, AccountPojo> entry : accountMap.entrySet()) {
                String accountName = entry.getKey();
                //  Create accountName dir inside tempDir/
                Files.createDirectory(Path.of(dirs[0].getAbsolutePath() + "/" + accountName));
            }

            for (Map.Entry<String, CreditCardPojo> entry : creditCardsMap.entrySet()) {
                String cardLast4Digits = entry.getKey();
                //  Create cardLast4Digits dir inside tempDir/
                Files.createDirectory(Path.of(dirs[0].getAbsolutePath() + "/" + cardLast4Digits));
            }

        } catch (IOException e) {
            System.out.println("Failed To Initialize Directories.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private static void deleteDirectoryRecursively(File dirToBeDeleted) {

        Path dirPath = dirToBeDeleted.toPath().resolve(dirToBeDeleted.getAbsolutePath());

        try (Stream<Path> pathStream = Files.walk(dirPath)) {

            //  Sort the stream in descending order so that Files (Not Dirs.) will be deleted first
            pathStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(KOHFilesUtil::deleteFileNow);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
