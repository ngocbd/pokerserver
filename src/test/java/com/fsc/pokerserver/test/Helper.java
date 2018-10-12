package com.fsc.pokerserver.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class Helper {


    public static int getStatusCodeFromUrl(String url) throws IOException{
        return Jsoup.connect(url).method(Connection.Method.GET).execute().statusCode();

    }
}
