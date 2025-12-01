/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.noren.javaapi;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author itsku
 */

public class NorenRequests {
    OkHttpClient client = new OkHttpClient();
    NorenRoutes routes = new NorenRoutes();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public NorenRequests(String host){
        NorenRoutes._host = host;
    }

    
    // Encode URL parameter safely
    
    
    public String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    
    // Perform GET requests
            
            
    public String run(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
          try (Response response = client.newCall(builder.build()).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    
    // Perform POST requests
    
    String post(String url, JSONObject jsnObj, Map<String,String> headers) {
        String json = "jData=" + jsnObj.toString();
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder().url(url).post(body);

        if(headers != null) {
            for(Map.Entry<String,String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body().string();
        } catch(IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    
    
    // Utility: SHA-256 hashing (if needed)
    
     public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return input;
        }
    }
}
