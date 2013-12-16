/** OSXHelper class provides methods to set the OSX look and feel for specific JComponents on mac platform 
 *
 * @author Florian Adolf
 */

package org.woped.core.utilities;

import java.awt.Font;
import javax.swing.JButton;
import org.apache.batik.util.Platform;

public class OSXHelper {
	
	/** macToggleButton(JButton button) gets a JButton as input parameter and performs different style
	 * operations such as button styling for OSX, removing button icon and parsing HTML styles on the button
	 * label. It also sets the label font to the Mac OSX standard font. 
	 * 
	 * @param button			The JButton element the style should applied on.
	 */

	// Optimize buttons as Mac OSX toggle buttons and remove the icon on it if
	// set.
	public static void macToggleButton(JButton button) {
		if (Platform.isOSX) {
			button.putClientProperty("JComponent.sizeVariant", "regular");
			// set standard OSX toggle style
			button.putClientProperty("JButton.buttonType", "textured");
			// set OSX system font
			button.setFont(new Font("Lucia Grande", Font.PLAIN, 13));
			// delete icon if set
			button.setIcon(null);
			// remove HTML tags if set on the text
			if (!button.getText().isEmpty())
				button.setText(button.getText().replaceAll("<[^>]+>", ""));
		}

	}

}
