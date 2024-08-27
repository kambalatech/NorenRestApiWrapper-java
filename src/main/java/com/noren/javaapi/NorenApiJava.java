/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.noren.javaapi;

//package okhttp3.guide;
import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import com.noren.javaapi.NorenCallback;
import com.noren.javaapi.NorenWebsocketClient;

/**
 *
 * @author itsku
 */
public class NorenApiJava {    
    String _host;
    String _websocketURL;
    NorenRequests _api;
    NorenWebsocketClient _wsclient;
    public NorenApiJava(String host,String websocketURL){
        _host = host;
        _websocketURL=websocketURL;
        _api = new NorenRequests(host);
    }
    private String _userid;    
    private String _actid;
    private String _key;
    
    public String login(String userid,String password, String twoFA, String vendor_code, String api_secret, String imei) {
        String url = _api.routes.get("authorize");
        JSONObject jsonObject = new JSONObject();
        
        String passwordsha = _api.sha256(password);        
        String appkey = userid + "|" + api_secret;
        String appkeysha = _api.sha256(appkey);
        
        jsonObject.put("source", "API");
        jsonObject.put("apkversion", "1.0.0");
        jsonObject.put("uid", userid);
        jsonObject.put("pwd", passwordsha);
        jsonObject.put("factor2", twoFA);
        jsonObject.put("vc", vendor_code);
        jsonObject.put("appkey", appkeysha);
        jsonObject.put("imei", imei);

        String response = _api.post(url, jsonObject);
        
        JSONObject jsonResp = new JSONObject(response);
        
        String stat = jsonResp.getString("stat").toString();
        if("Ok".equals(stat))
        {
            _userid = userid;
            _actid = userid;
            _key = jsonResp.getString("susertoken").toString();
        }
        
        return response;   
    }
    
    public JSONObject search(String exchange, String searchtext){
        String url = _api.routes.get("searchscrip");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("exch", exchange);
        jsonObject.put("stext", _api.encodeValue(searchtext));
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;   
    }
    public JSONArray get_order_book(){
        String url = _api.routes.get("orderbook");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        
        String response = _api.post(url, _key, jsonObject);
        System.out.println(response);
        if(response.charAt(0) == '[')
        {
            JSONArray jsonResp = new JSONArray(response);
            return jsonResp;
        }
        return null;       
    }
    
    public JSONObject forgotpassword_OTP(String userid, String pan){
        String url = _api.routes.get("forgotpassword_OTP");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", userid);
        jsonObject.put("pan",pan);
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;   
    }
    
        public JSONObject get_quotes(String userid, String exch,String token){
        String url = _api.routes.get("get_quotes");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", userid);
        jsonObject.put("exch",exch);
        jsonObject.put("token",token);
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;   
    }
        
     public JSONObject get_limits(String uid, String actid){
        String url = _api.routes.get("get_limits");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", uid);
        jsonObject.put("actid",actid);
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;   
    }
     
    public JSONArray get_trade_book(){
        String url = _api.routes.get("tradebook");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("actid", _actid);
        String response = _api.post(url, _key, jsonObject);
        System.out.println(response);
        if(response.charAt(0) == '[')
        {
            JSONArray jsonResp = new JSONArray(response);
            return jsonResp;
        }
        return null;         
    }
    
    public JSONArray get_position_book(){
        String url = _api.routes.get("positionbook");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("actid", _actid);
        String response = _api.post(url, _key, jsonObject);
        System.out.println(response);
        if(response.charAt(0) == '[')
        {
            JSONArray jsonResp = new JSONArray(response);
            return jsonResp;
        }
        return null;         
    }
    
    public JSONObject get_Basket_Margin(MainData basket){
        String url = _api.routes.get("get_BasketMargin");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("actid", _actid);
        jsonObject.put("exch", basket.exch);
        jsonObject.put("tsym", basket.tsym);
        jsonObject.put("qty", Integer.toString(basket.qty));
        jsonObject.put("prc", String.valueOf(basket.prc));
        jsonObject.put("prd", basket.prd);
        jsonObject.put("trantype", basket.trantype);
        jsonObject.put("prctyp", basket.prctyp);
        if(basket.basketlists.isEmpty()== false){
           
          jsonObject.put("basketlists", basket.toString());     
        }
           
           
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;        
    }
    
    public JSONObject place_order(String buy_or_sell,String product_type,
                    String exchange,String tradingsymbol,Integer quantity,Integer discloseqty,
                    String price_type,Double price,String remarks,Double trigger_price,
                    String retention, String amo,Double bookloss_price,Double bookprofit_price,Double trail_price){
        String url = _api.routes.get("placeorder");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("ordersource","API");
        jsonObject.put("actid"  ,_actid);
        jsonObject.put("trantype",buy_or_sell);
        jsonObject.put("prd"     ,product_type);
        jsonObject.put("exch"    ,exchange);
        jsonObject.put("tsym"    ,_api.encodeValue(tradingsymbol));
        jsonObject.put("qty"     ,Integer.toString(quantity));
        jsonObject.put("dscqty"  ,Integer.toString(discloseqty));
        jsonObject.put("prctyp"  ,price_type);
        jsonObject.put("prc"     ,Double.toString(price));
        if(null != trigger_price)
            jsonObject.put("trgprc"  ,Double.toString(trigger_price));
        if(null == retention)
            retention = "DAY";
        jsonObject.put("ret"     ,retention);
        jsonObject.put("remarks" ,remarks);
        if(null != amo)
            jsonObject.put("amo"     ,amo);

        //if cover order or high leverage order
        if (bookloss_price != null)
            jsonObject.put("blprc"  ,Double.toString(bookloss_price));

        //trailing price
        if (trail_price != null)
            jsonObject.put("trailprc"  ,Double.toString(trail_price));

        //book profit of bracket order
        if (bookprofit_price != null)
            jsonObject.put("bpprc"  ,Double.toString(bookprofit_price));
                
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;

    }
            
    public JSONObject modify_order(String orderno,String exchange,String tradingsymbol,Integer newquantity,
                    String newprice_type,Double newprice,Double newtrigger_price,Double bookloss_price, Double bookprofit_price , Double trail_price){
        String url = _api.routes.get("modifyorder");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("ordersource","API");
        jsonObject.put("actid"  ,_actid);
        jsonObject.put("norenordno"  ,orderno);
        jsonObject.put("exch"  ,exchange);
        jsonObject.put("tsym"  ,_api.encodeValue(tradingsymbol));
        jsonObject.put("qty"  ,Integer.toString(newquantity));
        jsonObject.put("prctyp"  ,newprice_type);
        jsonObject.put("prc"  ,Double.toString(newprice));

        if (newprice_type.equals( "SL-LMT") || "SL-MKT".equals(newprice_type)){        
            if (newtrigger_price != null)
                jsonObject.put("trgprc"  ,Double.toString(newtrigger_price));
            else                
                return null;
            
        }
        //if cover order or high leverage order
        if (bookloss_price != null)
            jsonObject.put("blprc"  ,Double.toString(bookloss_price));
        
        //trailing price
        if (trail_price != null)
            jsonObject.put("trailprc"  ,Double.toString(trail_price));
        
        //book profit of bracket order   
        if (bookprofit_price != null)
            jsonObject.put("bpprc"  ,Double.toString(bookprofit_price));
        
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp;     
     }
    public JSONObject cancel_order(String orderno){
        String url = _api.routes.get("cancelorder");
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("ordersource","API");
        jsonObject.put("norenordno",orderno);
        
        String response = _api.post(url, _key, jsonObject);
        JSONObject jsonResp = new JSONObject(response);
        return jsonResp; 
    }
    
    public JSONArray get_time_price_series(String exchange, String token, String starttime, Object endtime, Object interval){
        String url = _api.routes.get("timepriceseries");
        if (starttime == null) {
            starttime = Long.toString(System.currentTimeMillis()/ 1000);
        }
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("uid", _userid);
        jsonObject.put("exch", exchange);
        jsonObject.put("token", token);
        jsonObject.put("st", starttime);
        if (endtime != null) {
            jsonObject.put("et", endtime);
        }
        if (interval != null) {
            jsonObject.put("intrv", interval);
        }
        
     String response = _api.post(url, _key, jsonObject);
        System.out.println(response);
        if(response.charAt(0) == '[')
        {
            JSONArray jsonResp = new JSONArray(response);
            return jsonResp;
        }
        return null;         
    }
    public  void startwebsocket(NorenCallback appcallback) {
        try {
            // Create WebSocket client instance and establish the connection
            _wsclient = new NorenWebsocketClient(new URI(_websocketURL),appcallback);
            _wsclient.connectBlocking(); // Wait until the connection is established
      
            // Send a message to the WebSocket server for login
            _wsclient.send("{\"uid\":\"" + _userid + "\",\"actid\":\"" + _userid + "\",\"source\":\"API\",\"susertoken\":\"" + _key + "\",\"t\":\"c\"}");

        } catch (Exception e) {
            System.err.println("Error during WebSocket connection and message sending: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public  void subscribe(String value) {
        try {
            
            // Send a message to the WebSocket server for subscription feed
           _wsclient.send("{\"t\":\"t\", \"k\":\"" + value + "\"}");
        } catch (Exception e) {
            System.err.println("Error during WebSocket connection and message sending: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public  void unsubscribe(String value) {
        try {
            
            // Send a message to the WebSocket server for subscription feed
           _wsclient.send("{\"t\":\"u\", \"k\":\"" + value + "\"}");
        } catch (Exception e) {
            System.err.println("Error during WebSocket connection and message sending: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
}

    

    //public static void main(String[] args) {
    //    System.out.println("Hello and Welcom to Noren World!");
    //    NorenApiJava api = new NorenApiJava("http://kurma.kambala.co.in:9959/NorenWClient/");
        
    //    String response = api.login("MOBKUMAR", "Zxc@1234", "01-01-1970", "IDART_DESK", "12be8cef3b1758f5", "java-");
    //    System.out.println(response);
            
    //    JSONObject search_reply = api.search("NSE", "TCS"); 
    //    System.out.println(search_reply.toString());
        
    //    JSONObject reply = api.place_order("B","I", "NSE", "CANBK-EQ", 1, 0, "L", 220.0, "java", null, null, null, null, null, null); 
    //    System.out.println(reply.toString());
        
    //    JSONArray book  = api.get_order_book(); 
    //    System.out.println(book.toString());
        
    //    book = api.get_trade_book(); 
    //    if(book != null)
    //        System.out.println(book.toString());       
    //}   

