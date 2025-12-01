
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.noren.javaapi;

import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.noren.javaapi.NorenCallback;
import com.noren.javaapi.NorenWebsocketClient;
import java.util.Map;

/**
 *
 * @author itsku
 */


public class NorenApiJava {

    private String _host;
    private String _websocketURL;
    private NorenRequests _api;
    private NorenWebsocketClient _wsclient;
    private OAuthHandler oauth;

    public NorenApiJava(String host, String websocketURL, OAuthHandler oauth) {
        _host = host;
        _websocketURL = websocketURL;
        _api = new NorenRequests(host);
        this.oauth = oauth;
    }

    // --- REST API Methods ---

    public JSONObject search(String exchange, String searchtext) {
        String url = _api.routes.get("searchscrip");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("exch", exchange);
        jsonObject.put("stext", _api.encodeValue(searchtext));

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }
    
       // --- Added APIs from old version ---

    public JSONObject forgotpassword_OTP(String userid, String pan){
        String url = _api.routes.get("forgotpassword_OTP");
                JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", userid);
        jsonObject.put("pan", pan);

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONObject get_Basket_Margin(MainData basket){
        String url = _api.routes.get("get_BasketMargin");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("actid", oauth.getAccountId());
        jsonObject.put("exch", basket.exch);
        jsonObject.put("tsym", basket.tsym);
        jsonObject.put("qty", Integer.toString(basket.qty));
        jsonObject.put("prc", String.valueOf(basket.prc));
        jsonObject.put("prd", basket.prd);
        jsonObject.put("trantype", basket.trantype);
        jsonObject.put("prctyp", basket.prctyp);

        if (!basket.basketlists.isEmpty()) {
            jsonObject.put("basketlists", basket.toString());
        }

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONArray get_order_book() {
        String url = _api.routes.get("orderbook");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        System.out.println(response);
        if (response.charAt(0) == '[') return new JSONArray(response);
        return null;
    }

    public JSONArray get_trade_book() {
        String url = _api.routes.get("tradebook");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("actid", oauth.getAccountId());

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        System.out.println(response);
        if (response.charAt(0) == '[') return new JSONArray(response);
        return null;
    }

    public JSONArray get_position_book() {
        String url = _api.routes.get("positionbook");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("actid", oauth.getAccountId());

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        if (response.charAt(0) == '[') return new JSONArray(response);
        return null;
    }

    public JSONObject get_limits() {
        String url = _api.routes.get("get_limits");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("actid", oauth.getAccountId());

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONObject get_quotes(String exch, String token) {
        String url = _api.routes.get("get_quotes");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("exch", exch);
        jsonObject.put("token", token);

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONObject place_order(String buy_or_sell, String product_type,
                                  String exchange, String tradingsymbol, Integer quantity, Integer discloseqty,
                                  String price_type, Double price, String remarks, Double trigger_price,
                                  String retention, String amo, Double bookloss_price, Double bookprofit_price, Double trail_price) {

        String url = _api.routes.get("placeorder");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("ordersource", "API");
        jsonObject.put("actid", oauth.getAccountId());
        jsonObject.put("trantype", buy_or_sell);
        jsonObject.put("prd", product_type);
        jsonObject.put("exch", exchange);
        jsonObject.put("tsym", _api.encodeValue(tradingsymbol));
        jsonObject.put("qty", Integer.toString(quantity));
        jsonObject.put("dscqty", Integer.toString(discloseqty));
        jsonObject.put("prctyp", price_type);
        jsonObject.put("prc", Double.toString(price));
        if (trigger_price != null) jsonObject.put("trgprc", Double.toString(trigger_price));
        jsonObject.put("ret", retention == null ? "DAY" : retention);
        jsonObject.put("remarks", remarks);
        if (amo != null) jsonObject.put("amo", amo);
        if (bookloss_price != null) jsonObject.put("blprc", Double.toString(bookloss_price));
        if (bookprofit_price != null) jsonObject.put("bpprc", Double.toString(bookprofit_price));
        if (trail_price != null) jsonObject.put("trailprc", Double.toString(trail_price));

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONObject modify_order(String orderno, String exchange, String tradingsymbol, Integer newquantity,
                                   String newprice_type, Double newprice, Double newtrigger_price,
                                   Double bookloss_price, Double bookprofit_price, Double trail_price) {

        String url = _api.routes.get("modifyorder");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("ordersource", "API");
        jsonObject.put("actid", oauth.getAccountId());
        jsonObject.put("norenordno", orderno);
        jsonObject.put("exch", exchange);
        jsonObject.put("tsym", _api.encodeValue(tradingsymbol));
        jsonObject.put("qty", Integer.toString(newquantity));
        jsonObject.put("prctyp", newprice_type);
        jsonObject.put("prc", Double.toString(newprice));

        if ("SL-LMT".equals(newprice_type) || "SL-MKT".equals(newprice_type)) {
            if (newtrigger_price != null) jsonObject.put("trgprc", Double.toString(newtrigger_price));
            else return null;
        }

        if (bookloss_price != null) jsonObject.put("blprc", Double.toString(bookloss_price));
        if (trail_price != null) jsonObject.put("trailprc", Double.toString(trail_price));
        if (bookprofit_price != null) jsonObject.put("bpprc", Double.toString(bookprofit_price));

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONObject cancel_order(String orderno) {
        String url = _api.routes.get("cancelorder");
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("ordersource", "API");
        jsonObject.put("norenordno", orderno);

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        return new JSONObject(response);
    }

    public JSONArray get_time_price_series(String exchange, String token, String starttime, Object endtime, Object interval) {
        String url = _api.routes.get("timepriceseries");
        if (starttime == null) starttime = Long.toString(System.currentTimeMillis() / 1000);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uid", oauth.getUid());
        jsonObject.put("exch", exchange);
        jsonObject.put("token", token);
        jsonObject.put("st", starttime);
        if (endtime != null) jsonObject.put("et", endtime);
        if (interval != null) jsonObject.put("intrv", interval);

        String response = _api.post(url, jsonObject, oauth.injectOAuthHeaders());
        if (response.charAt(0) == '[') return new JSONArray(response);
        return null;
    }

    // --- WebSocket Methods ---
    public void startWebSocket(NorenCallback appcallback) {
        try {
            _wsclient = new NorenWebsocketClient(new URI(_websocketURL), appcallback);
            _wsclient.connectBlocking();

            JSONObject loginMsg = new JSONObject();
            loginMsg.put("uid", oauth.getUid());
            loginMsg.put("actid", oauth.getAccountId());
            loginMsg.put("source", "API");
            loginMsg.put("t", "a"); //c
            loginMsg.put("accesstoken", oauth.getAccessToken());
            //loginMsg.put("Authorization", "Bearer " + oauth.getAccessToken());

            _wsclient.send(loginMsg.toString());

        } catch (Exception e) {
            System.err.println("WebSocket connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void subscribe(String value) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("t", "t");
            msg.put("k", value);
            _wsclient.send(msg.toString());
        } catch (Exception e) {
            System.err.println("WebSocket subscribe error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unsubscribe(String value) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("t", "u");
            msg.put("k", value);
            _wsclient.send(msg.toString());
        } catch (Exception e) {
            System.err.println("WebSocket unsubscribe error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

}

