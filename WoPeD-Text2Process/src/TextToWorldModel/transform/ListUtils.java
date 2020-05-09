/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package TextToWorldModel.transform;

import java.util.ArrayList;
import java.util.List;

import worldModel.Actor;
import worldModel.ExtractedObject;

public class ListUtils {
	
	/**
	 * @param list
	 * @return ArrayList<ExtractedObjects>
	 */
	public static ArrayList<ExtractedObject> toExtractedObjects(List<Actor> list) {
		ArrayList<ExtractedObject> _result = new ArrayList<ExtractedObject>();
		for(Actor a:list) {
			_result.add(a);
		}
		return _result;
	}

	/**
	 * @param string
	 * @return
	 */
	public static List<String> getList(String... string) {
		ArrayList<String> _result = new ArrayList<String>();
		for(String s:string) {
			_result.add(s);
		}
		return _result;
	}

}
