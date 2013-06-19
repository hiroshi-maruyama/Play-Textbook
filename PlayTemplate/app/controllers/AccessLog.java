package controllers;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import play.mvc.Http.Request;

public class AccessLog {

    private static Map<String, Long> access_log = new HashMap<String, Long>();
    
    public static void access_start() {
        access_log.put(Auth.connected(), Calendar.getInstance().getTimeInMillis());
    }
    
    public static void access_end(Request request) {
        StringBuilder sb = new StringBuilder();
        
        // TIME
        Long end = Calendar.getInstance().getTimeInMillis();
        sb.append(access_log.get(Auth.connected())).append("\t");
        sb.append(end).append("\t");
        sb.append(end - access_log.get(Auth.connected())).append("\t");
        
        // IP
        sb.append(request.remoteAddress).append("\t");
        
        // user
        sb.append(Auth.connected()).append("\t");
        
        // action
        sb.append(request.action);
        
        if(request.method.equals("GET")){
            sb.append("?").append(request.querystring);
        }
        
        System.out.println(sb.toString());
    }
}