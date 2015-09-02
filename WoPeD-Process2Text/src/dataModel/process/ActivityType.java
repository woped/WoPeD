package dataModel.process;

import java.util.HashMap;
import java.util.Map;

public class ActivityType {
	
	public static final int NONE = 0;


    public static final Map<String, Integer> TYPE_MAP = new HashMap<String ,Integer >(){
        {
            put("None", 0);
            put("Manual", 1);
            put("User", 0);
            put("Subprocess",2);
        }
    };
	
}
