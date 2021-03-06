package com.siaivo.shipments.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;



public class JsonReader {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static Double getRateEUR (String url) {
        double rateEUR =0;
        InputStream is = null;
        try {
            is = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return rateEUR;
        }
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = null;
            try {
                jsonText = readAll(rd);
            } catch (IOException e) {
                e.printStackTrace();
                return rateEUR;
            }
            JSONArray jsonarray = new JSONArray(jsonText);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("cc").equals("EUR")) {
                    rateEUR = jsonObject.getDouble("rate");
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return rateEUR;
    }

    // public static Double getRateUSD (String url) throws IOException{
    //     double rateUSD =0;
    //     try (InputStream is = new URL(url).openStream()) {
    //         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
    //         String jsonText = readAll(rd);
    //         JSONArray jsonarray = new JSONArray(jsonText);
    //         for (int i = 0; i < jsonarray.length(); i++) {
    //             JSONObject jsonObject = jsonarray.getJSONObject(i);
    //             if (jsonObject.getString("cc").equals("USD")) {
    //                 rateUSD = jsonObject.getDouble("rate");
    //             }
    //         }
    //     }
    //     return rateUSD;
    // }

    public static Double getRateUSD (String url) {
        double rateUSD  =0;
        InputStream is = null;
        try {
            is = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return rateUSD;
        }
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = null;
            try {
                jsonText = readAll(rd);
            } catch (IOException e) {
                e.printStackTrace();
                return rateUSD ;
            }
            JSONArray jsonarray = new JSONArray(jsonText);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                if (jsonObject.getString("cc").equals("USD")) {
                    rateUSD = jsonObject.getDouble("rate");
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return rateUSD ;
    }
}
