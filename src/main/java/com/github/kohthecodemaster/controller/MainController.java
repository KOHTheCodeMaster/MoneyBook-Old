package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CardSwipePojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;
import com.github.kohthecodemaster.utils.FileHelper;
import com.github.kohthecodemaster.utils.JsonController;
import com.github.kohthecodemaster.utils.TestingHelper;
import com.github.kohthecodemaster.utils.ValidationHelper;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {

    public static final File accountJsonFile = new File("src/main/resources/stub/accounts.json");
    public static final File creditCardJsonFile = new File("src/main/resources/stub/credit-cards.json");
    public static final File transactionMainJsonFile = new File("src/main/resources/stub/transactions-main.json");
    public static final File transactionForAccJsonFile = new File("src/main/resources/stub/transactions-acc.json");
    public static final File transactionsCardSwipeJsonFile = new File("src/main/resources/stub/transactions-card-swipe.json");
    public static final File processedCardSwipeJsonFile = new File("src/main/resources/stub/transactions-processed-card-swipe.json");
    public static final File tempDir = new File("src/main/resources/temp/");
    public static final File resultDir = new File("src/main/resources/result/");

    //    private static final String strEndDate = "01 Mar 2022";
    private static final String strEndDate = "26 May 2023";
    private static final int TEMP_JSON_FILE_THRESHOLD_LIMIT = 100;

    private Map<String, AccountPojo> accountMap;
    private Map<String, CreditCardPojo> creditCardsMap;
    private List<TransactionPojo> mainTransactionList;
    private Map<String, List<TransactionPojo>> resultMap;

    public void major() {

        init();
        validation();
        process();
//        testing();

    }

    private void validation() {

        boolean isValid = ValidationHelper.validateJsonFiles(accountMap.keySet(), creditCardsMap.keySet(), accountJsonFile, creditCardJsonFile,
                transactionForAccJsonFile, transactionsCardSwipeJsonFile);

        if (!isValid) System.out.println("Json Files Validation Failed.\n");
        else System.out.println("JSON File Validation Complete.");

    }

    private void process() {

        initializeMainTransactionList();

        processMainTransactionList();

        saveItAll();

    }

    private void init() {

        initializeMaps();
        FileHelper.initializeDirs(accountMap, creditCardsMap, tempDir, resultDir);

    }

    private void initializeMaps() {

        //  Initialize Accounts Map
        accountMap = new HashMap<>();
        List<AccountPojo> accountPojoList = AccountPojo.loadAccountPojoListFromJson(accountJsonFile);
        accountPojoList.forEach(accountPojo -> accountMap.put(accountPojo.getName(), accountPojo));

        //  Initialize Cards Map
        creditCardsMap = new HashMap<>();
        List<CreditCardPojo> creditCardPojoList = CreditCardPojo.loadCreditCardPojoListFromJson(creditCardJsonFile);
        creditCardPojoList.forEach(creditCardPojo -> creditCardsMap.put(creditCardPojo.getLast4Digits(), creditCardPojo));

        //  Initialize Result Map
        resultMap = new HashMap<>();

    }

    /**
     * 1. Load transactionsCardSwipeJsonFile and Process Card Swipe List and convert into Transaction List
     * (Source To Target form) & save it as processedCardSwipeJsonFile
     * 2. Initialize & Save transactionMainJsonFile Using transactionForAccJsonFile & processedCardSwipeJsonFile
     */
    private void initializeMainTransactionList() {

        //  Initialize Account Transaction List
        List<TransactionPojo> transactionListForAcc = TransactionPojo.loadTransactionPojoListFromJson(transactionForAccJsonFile);

        //  Initialize Card Swipe List
        List<CardSwipePojo> cardSwipeList = CardSwipePojo.loadCardSwipePojoListFromJson(transactionsCardSwipeJsonFile);
        //  Process Card Swipe List and convert into Transaction List for Cards (Source To Target form)
        List<TransactionPojo> processedTransactionListForCards = CardSwipePojo.processCardSwipePojoList(cardSwipeList);
        JsonController.saveListToJsonFile(processedTransactionListForCards, processedCardSwipeJsonFile);

        //  Merge Transaction List for Account & Cards both into a single Main Transaction List
        this.mainTransactionList = mergeAccAndCardSwipeLists(transactionListForAcc, processedTransactionListForCards);
        JsonController.saveListToJsonFile(mainTransactionList, transactionMainJsonFile);

    }

    /**
     * Merge Transaction List for Account & Cards using following criteria:
     * 1. Add transactions to main list based on the earliest date of transaction.
     * 2. In case of Same Date Txn. -> Account Txn. is prioritized Before Card Swipe Txn.
     */
    private ArrayList<TransactionPojo> mergeAccAndCardSwipeLists(List<TransactionPojo> transactionPojoListForAcc, List<TransactionPojo> transactionPojoListForCards) {

        ArrayList<TransactionPojo> mainTransactionList = new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        int indexForAcc = 0, indexForCards = 0;
        TransactionPojo currentTxnForAcc = transactionPojoListForAcc.get(indexForAcc);
        TransactionPojo currentTxnForCardSwipe = transactionPojoListForCards.get(indexForCards);

        while (currentTxnForAcc != null && currentTxnForCardSwipe != null) {

            LocalDate localDateForAcc = LocalDate.parse(currentTxnForAcc.getDate(), dateTimeFormatter);
            LocalDate localDateForCardSwipe = LocalDate.parse(currentTxnForCardSwipe.getDate(), dateTimeFormatter);

            if (localDateForAcc.isBefore(localDateForCardSwipe) || localDateForAcc.isEqual(localDateForCardSwipe)) {
                addToTransactionList(currentTxnForAcc, mainTransactionList);
                ++indexForAcc;
                currentTxnForAcc = transactionPojoListForAcc.size() - 1 >= indexForAcc
                        ? transactionPojoListForAcc.get(indexForAcc)
                        : null;
            } else if (localDateForAcc.isAfter(localDateForCardSwipe)) {
                addToTransactionList(currentTxnForCardSwipe, mainTransactionList);
                ++indexForCards;
                currentTxnForCardSwipe = transactionPojoListForCards.size() - 1 >= indexForCards
                        ? transactionPojoListForCards.get(indexForCards)
                        : null;
            } else {
                System.out.println("Invalid Scenario");
                throw new RuntimeException("Invalid Date Scenario.");
            }

        }

        if (currentTxnForAcc == null) {
            for (int i = indexForCards; i < transactionPojoListForCards.size(); i++)
                addToTransactionList(transactionPojoListForCards.get(i), mainTransactionList);
        } else {
            for (int i = indexForCards; i < transactionPojoListForAcc.size(); i++)
                addToTransactionList(transactionPojoListForAcc.get(i), mainTransactionList);
        }

        return mainTransactionList;

    }

    private void addToTransactionList(TransactionPojo transactionPojo, List<TransactionPojo> transactionList) {

        //  Update transaction id according to size of the list
        transactionPojo.setTransactionId(transactionList.size() + 1);
        transactionList.add(transactionPojo);

    }

    private void saveItAll() {

        saveTempJsonFiles();
        processTempJsonFilesForResultDir();

    }

    private void saveTempJsonFiles() {

        mainTransactionList.forEach(transactionPojo -> {

            updateResultMap(transactionPojo, transactionPojo.getSourceAccount());
            updateResultMap(transactionPojo, transactionPojo.getTargetAccount());

        });

        //  Save the remaining TransactionPojo lists for each account/card into temp json files.
        resultMap.forEach((accountName, transactionPojoList) ->
                FileHelper.saveListToJsonWithoutThresholdCheck(accountName, transactionPojoList, tempDir));

    }

    private void updateResultMap(TransactionPojo transactionPojo, String accountName) {

        List<TransactionPojo> transactionPojoList = resultMap.computeIfAbsent(accountName, strAccountName -> new ArrayList<>());
        transactionPojoList.add(transactionPojo);

        FileHelper.saveListToJson(accountName, transactionPojoList, tempDir, TEMP_JSON_FILE_THRESHOLD_LIMIT);

    }

    private void processTempJsonFilesForResultDir() {

        // TODO: 05-01-2023 - Merge each account json files into one & re-structure the result directory

    }

    private void testing() {

        testStubFromJson();

    }


    private void testStubFromJson() {

//        TestingHelper.testMainTransactionPojoListFromJson(transactionMainJsonFile);
//        TestingHelper.testAccTransactionPojoListFromJson(transactionForAccJsonFile);
        TestingHelper.testAccountPojoListFromJson(accountJsonFile);
//        TestingHelper.testCreditCardPojoListFromJson(creditCardJsonFile);
//        TestingHelper.testCardSwipePojoListFromJson(transactionsCardSwipeJsonFile);
//        TestingHelper.testCardNameMismatch(creditCardsMap, transactionsCardSwipeJsonFile);
//        TestingHelper.checkDuplicateCardEntries(creditCardJsonFile);

    }

    public void processMainTransactionList() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDate endDate = LocalDate.parse(strEndDate, dateTimeFormatter);

        mainTransactionList = mainTransactionList.stream()
                .filter(transactionPojo -> {
                    //  Keep all transactions whose date is before or equal to endDate.
                    LocalDate transactionDate = LocalDate.parse(transactionPojo.getDate(), dateTimeFormatter);
                    return transactionDate.isBefore(endDate) || transactionDate.isEqual(endDate);
                })
                .collect(Collectors.toList());

        mainTransactionList.forEach(this::processTransaction);

        summary();

    }

    private void summary() {

        BigDecimal netAccountsBalance = BigDecimal.ZERO;
        BigDecimal netCardsBalance = BigDecimal.ZERO;

        System.out.println("\nAccounts Summary:");
        for (Map.Entry<String, AccountPojo> entry : accountMap.entrySet()) {
            String strAccountName = entry.getKey();
            AccountPojo accountPojo = entry.getValue();
            if (accountPojo.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(strAccountName + " -> " + accountPojo.getBalance() + "\t\t Transaction Count: " + accountPojo.getTransactionPojoList().size());
                netAccountsBalance = netAccountsBalance.add(accountPojo.getBalance());
            }
        }

        System.out.println("\nCredit Cards Summary:");
        for (Map.Entry<String, CreditCardPojo> entry : creditCardsMap.entrySet()) {
            String last4Digits = entry.getKey();
            CreditCardPojo creditCardPojo = entry.getValue();
            if (creditCardPojo.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(last4Digits + " -> " + creditCardPojo.getBalance() + "\t\t Transaction Count: " + creditCardPojo.getTransactionPojoList().size());
                netCardsBalance = netCardsBalance.add(creditCardPojo.getBalance());
            }
        }

//        System.out.println("\n======\n");
//        AccountPojo accountPojo = accountMap.get("BillPe Kotak");
//        accountPojo.getTransactionPojoList().forEach(System.out::println);

        System.out.println("\nAcc. Total: " + netAccountsBalance + " | Cards Total: " + netCardsBalance + " | Diff: " + netAccountsBalance.add(netCardsBalance));

    }

    private void processTransaction(TransactionPojo transactionPojo) {

//        System.out.println("Processing - Begin.");

        try {

            AccountPojo sourceAccountPojo = accountMap.get(transactionPojo.getSourceAccount());
            AccountPojo targetAccountPojo = accountMap.get(transactionPojo.getTargetAccount());

            //  Transfer Amount from Source to Target Account based on Account & Credit Card scenario
            if (sourceAccountPojo != null &&
                targetAccountPojo != null) {

                //  Source & Target Account Both are NOT A Credit Card
                AccountPojo.transferBalance(sourceAccountPojo, targetAccountPojo, transactionPojo);

            } else if (sourceAccountPojo == null &&
                       targetAccountPojo != null) {

                //  Source IS A Credit Card  AND  Target Account IS NOT A Credit Card
                CreditCardPojo sourceCreditCardPojo = creditCardsMap.get(transactionPojo.getSourceAccount());//  Last 4 Digits
                AccountPojo.transferBalance(sourceCreditCardPojo, targetAccountPojo, transactionPojo);

            } else if (sourceAccountPojo != null &&
                       targetAccountPojo == null) {

                //  Source Account IS NOT A Credit Card  AND  Target IS A Credit Card
                CreditCardPojo targetCreditCardPojo = creditCardsMap.get(transactionPojo.getTargetAccount());//  Last 4 Digits
                AccountPojo.transferBalance(sourceAccountPojo, targetCreditCardPojo, transactionPojo);

            } else {
                System.out.println("INVALID Scenario - Failed to Process Transaction:\n" + transactionPojo);
                throw new RuntimeException();
            }

        } catch (Exception e) {
            System.out.println("Failed to Process - Transaction Pojo: " + transactionPojo);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        System.out.println("Processing - Completed.");

    }

}
