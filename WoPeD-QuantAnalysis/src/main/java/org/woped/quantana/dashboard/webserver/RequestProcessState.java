package org.woped.quantana.dashboard.webserver;

public class RequestProcessState {
    private boolean stopProcessing;
    
    public RequestProcessState(){
        stopProcessing = false;
    }
    
    public boolean stopProcessing(){
        return stopProcessing;
    }
    
    public void setStopProcessing(boolean stopProcessing){
        this.stopProcessing = stopProcessing;
    }
}
