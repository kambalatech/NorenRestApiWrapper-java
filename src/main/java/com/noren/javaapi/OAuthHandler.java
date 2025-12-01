package com.noren.javaapi;

import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.JSONObject;
import okhttp3.*;

/**
 * Handles OAuth: generate access + refresh token, read/write cred.properties, inject headers.
 */
public class OAuthHandler {

    private String accessToken;
    private String refreshToken;
    private String uid;
    private String accountId;
    private Properties credentials;
    private String credFilePath;

    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");

    public OAuthHandler(String credFilePath) throws IOException {
        this.credFilePath = credFilePath;
        this.credentials = new Properties();
        try (FileInputStream fis = new FileInputStream(credFilePath)) {
            credentials.load(fis);
        }
        this.uid = credentials.getProperty("UID");
        this.accountId = credentials.getProperty("Account_ID");
        this.accessToken = credentials.getProperty("Access_token");
        this.refreshToken = credentials.getProperty("Refresh_token");
    }

    /** Get OAuth URL */
    public String getOAuthURL() {
        return credentials.getProperty("oauth_url") + "?client_id=" + credentials.getProperty("client_id");
    }

    /** Generate Access + Refresh Token */
    public Map<String, String> getAccessToken(String code, String baseUrl, NorenRoutes routes) throws Exception {
        String secretCode = credentials.getProperty("Secret_Code");
        String appKey = credentials.getProperty("client_id");

        String url = credentials.getProperty("base_url") + "/GenAcsTok";

        // SHA-256 hashing same as Python
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String dataToHash = appKey + secretCode + code;
        byte[] hash = digest.digest(dataToHash.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));

        JSONObject payloadJson = new JSONObject();
        payloadJson.put("code", code);
        payloadJson.put("checksum", sb.toString());
        payloadJson.put("uid", uid);

        RequestBody body = RequestBody.create("jData=" + payloadJson.toString(), FORM);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new Exception("Unexpected code " + response);

            JSONObject res = new JSONObject(response.body().string());
            if (res.has("access_token")) {
                accessToken = res.getString("access_token");
                uid = res.getString("USERID");
                accountId = res.getString("actid");

                // Refresh token (optional key)
                if (res.has("refresh_token")) {
                    refreshToken = res.getString("refresh_token");
                }

                // Save back to cred.properties
                credentials.setProperty("Access_token", accessToken);
                if (refreshToken != null) {
                    credentials.setProperty("Refresh_token", refreshToken);
                }
                try (PrintWriter writer = new PrintWriter(new FileWriter(credFilePath))) {
                    for (String key : credentials.stringPropertyNames()) {
                        String value = credentials.getProperty(key);
                        writer.println(key + "=" + value);
                    }
                }


                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("access_token", accessToken);
                tokenMap.put("refresh_token", refreshToken);
                tokenMap.put("uid", uid);
                tokenMap.put("actid", accountId);
                return tokenMap;
            } else {
                throw new Exception("access_token not found in response: " + res.toString());
            }
        }
    }

    /** Inject OAuth Headers */
    public Map<String, String> injectOAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    // --- Getter methods ---
    public String getUid() { return uid; }
    public String getAccountId() { return accountId; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getBaseUrl() { return credentials.getProperty("base_url"); }
    public String getWebsocketUrl() { return credentials.getProperty("websocket_url"); }
}
