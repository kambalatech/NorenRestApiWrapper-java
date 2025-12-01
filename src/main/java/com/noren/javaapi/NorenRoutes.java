
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.noren.javaapi;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author itsku
 */


public class NorenRoutes {
    public Map<String, String> routes;
    public static String _host = "http://rama.kambala.co.in:6008/NorenWClient/";

    public NorenRoutes(){
        routes = new HashMap<>() {{
            put("gen_acs_tok", "/GenAcsTok");
            put("logout", "/Logout");
            put("searchscrip", "/SearchScrip");
            put("orderbook", "/OrderBook");
            put("tradebook", "/TradeBook");
            put("placeorder", "/PlaceOrder");
            put("modifyorder", "/ModifyOrder");
            put("cancelorder", "/CancelOrder");
            put("timepriceseries", "/TPSeries");
            put("forgotpassword_OTP", "/FgtPwdOTP");
            put("get_limits", "/Limits");
            put("positionbook", "/PositionBook");
            put("get_quotes", "/GetQuotes");
            put("get_BasketMargin", "/GetBasketMargin");
        }};
    }

    public String get(String key){
        return _host + routes.get(key);
    }
}
