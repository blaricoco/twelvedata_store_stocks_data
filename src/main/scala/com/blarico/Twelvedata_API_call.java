package com.blarico;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Properties;

public class Twelvedata_API_call {


    public String getStocks(Properties p){

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest
                    .get("https://api.twelvedata.com/time_series?interval=1min&symbol="+p.getProperty("symbol")+"&format="+p.getProperty("symbol")+"&apikey="+p.getProperty("apikey"))
                    .header("Cookie", "__cfduid=d38f7dde9c7a26ce6fed228bcb35255a41593440950")
                    .asString();
            System.out.println(response.getBody());
            return response.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "";
    }
}
