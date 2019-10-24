package com.example.demo;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonUtils {


    private static Gson gson = new Gson();

    private static String getKey(String v) {

        if (!Strings.isNullOrEmpty(v)) {
            JsonObject jsonObject = gson.fromJson(v, JsonObject.class);

            System.out.println(">>>>>>>>>>>>   " + jsonObject.get("itemId").toString());
            return jsonObject.get("itemId").toString();
        } else return "a";
    }



}
