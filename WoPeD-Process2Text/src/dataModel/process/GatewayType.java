package dataModel.process;

import java.util.HashMap;
import java.util.Map;

public class GatewayType {
	
	public static final int XOR = 0;
	public static final int OR = 1;
	public static final int AND = 2;
	public static final int EVENT = 3;
	
	public static final Map<String, Integer> TYPE_MAP = new HashMap<String ,Integer >(){
        {
            put("Exclusive_Databased_Gateway", 0);
            put("Inclusive_Databased_Gateway", 1);
            put("ParallelGateway", 2);
            put("InclusiveGateway",1);
            put("AND_Gateway",2);
            put("EventbasedGateway",3);
        }
    };

}
