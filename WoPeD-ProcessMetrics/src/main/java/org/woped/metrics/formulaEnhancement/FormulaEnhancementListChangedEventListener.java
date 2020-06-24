/**
 * 
 */
package org.woped.metrics.formulaEnhancement;

import java.util.EventListener;

/**
 * @author Tobias Lorentz
 *
 */
public interface FormulaEnhancementListChangedEventListener extends
		EventListener {
	void formulaEnhancementListChanged( FormulaEnhancementListChangedEvent e);

}
