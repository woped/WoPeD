package processtotext.ws;

import java.util.ArrayList;
import java.util.List;

import worldModel.Action;



public class InterpetWorldModel {

	public List<String> getTextTrans(List<Action> actions) {
		String regex = null;
		List<String>  output= new ArrayList<String>();
		for (Action action : actions) {

			String input = action.toString();

			if (!input.equals("Action - Dummy Node")) {

				regex = input.replace("Action - ", "");
				if (regex.indexOf("PP") != -1) {
					regex = regex.substring(0, regex.indexOf("\n\tPP"));
				}
				
				output.add(regex);
			}
		}

		return output;
	}

}
