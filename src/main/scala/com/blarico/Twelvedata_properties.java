package com.blarico;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Twelvedata_properties {
    Properties p = new Properties();
    OutputStream os;

    void run(){
        try {
            os = new FileOutputStream("application.properties");
            p.setProperty("topic","twelveData");
            p.setProperty("apikey","0f23bd092f644011889de648fde28d90");
            p.setProperty("format","JSON");
            p.setProperty("symbol","EUR/USD");
            p.setProperty("bootstrap.servers","localhost:9092");
            p.store(os,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
