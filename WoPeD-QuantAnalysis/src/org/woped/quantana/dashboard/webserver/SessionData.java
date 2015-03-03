package org.woped.quantana.dashboard.webserver;

import java.util.HashMap;

public class SessionData {
    private HashMap<String,Object> content;
    public SessionData(){
        content = new HashMap<String,Object>();
    }
    public Object getObject(String name){
       return content.get(name);
    }
    public void addObject(String name, Object value){
        content.put(name, value);
    }
}
