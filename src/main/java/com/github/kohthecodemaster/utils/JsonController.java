package com.github.kohthecodemaster.utils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonController {

    public static List<?> parseJsonFileToList(File cookiesJsonFile, Type listType) {

        List<?> resultList = null;

        try (FileReader fr = new FileReader(cookiesJsonFile)) {

            resultList = new Gson().fromJson(fr, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

}
