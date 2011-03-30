import java.util.Collection;
import java.util.HashMap;
import java.util.*;

public class MySystem
{
	public static final Map<String, Integer> COMPONENTS;
    static {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
		aMap.put("HUMIDITY SENSOR", 44);
		aMap.put("TEMPERTURE SENSOR", 55);
		aMap.put("DOOR SENSOR", EventIDs.DOOR_ID);
		aMap.put("WIDNOW SENSOR", EventIDs.WINDOW_ID);
		aMap.put("MOTION SENSOR", EventIDs.MOTION_ID);
		aMap.put("FIRE SENSOR", EventIDs.FIRE_ID);
		aMap.put("SPRINKLER SENSOR", EventIDs.SPRINKLER_ID);
		//aMap.put("SECURITY MONITOR", EventIDs.SECURITY_MONITOR_ID);
		//aMap.put("MAINTENANCE MONITOR", EventIDs.MAINTENANCE_MONITOR_ID);
		//aMap.put("SECURITY CONTROLLER", EventIDs.SECURITY_CONTROLLER_ID);
		//aMap.put("FIRE CONTROLLER", EventIDs.FIREALARM_CONTROLLER_ID);
		//aMap.put("SPRINKLER CONTROLLER", EventIDs.SPRINKLER_CONTROLLER_ID);
        COMPONENTS = Collections.unmodifiableMap(aMap);
    }
}
