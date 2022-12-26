package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.pojo.TransactionPojo;
import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class MainController {

    public static final File tempJsonFile = new File("src/main/resources/temp/json/sample.json");

    public void major() {

        testing();

    }

    private void testing() {

        testReadingJson();

    }

    private void testReadingJson() {

        List<TransactionPojo> transactionPojoList = loadTransactionPojoListFromJson();

        transactionPojoList.forEach(System.out::println);
        System.out.println("Size: " + transactionPojoList.size());

    }

    private List<TransactionPojo> loadTransactionPojoListFromJson() {

        Type type = new TypeToken<List<TransactionPojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<TransactionPojo> transactionPojoList = (List<TransactionPojo>) JsonController.parseJsonFileToList(
                tempJsonFile,
                type);

        return transactionPojoList;

    }

    private void init() {


    }

}
