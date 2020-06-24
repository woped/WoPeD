package org.woped.metrics.builder;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.woped.gui.translations.Messages;
import org.woped.metrics.exceptions.CalculateFormulaException;
import org.woped.metrics.exceptions.NestedCalculateFormulaException;

/**
 * @author Tobias Lorentz
 * This UI displays all error which were found by the consistency check
 *
 */
public class MetricsBuilderErrorFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3222238220054903505L;
	/**
	 *  This Variable stores the Error from the consistency check
	 *  Special Case: this is a object of the NestedCalculateFormulaException and contains multiple Exceptions
	 */
	private CalculateFormulaException e;

	/**
	 * @param e Syntax-Error
	 * @throws HeadlessException
	 */
	public MetricsBuilderErrorFrame(CalculateFormulaException e)
			throws HeadlessException {
		super();
		this.e = e;
		this.createUI();
		this.setTitle(Messages.getString("Metrics.Builder.ErrorFrame.FormuaErrors")); //$NON-NLS-1$
		setVisible(true);
		setSize(getPreferredSize());
		this.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
		pack();
	}

	/**
	 * This Method creates the UI
	 */
	private void createUI() {
		this.setLayout(new BorderLayout());
		this.add(
				new JLabel(Messages.getString("Metrics.Builder.ErrorFrame.Description")), //$NON-NLS-1$
				BorderLayout.NORTH);

		if (e instanceof NestedCalculateFormulaException) {
			String[] columnTitles = { Messages.getString("Metrics.Builder.ErrorFrame.Error") }; //$NON-NLS-1$
			DefaultTableModel tableModel = new DefaultTableModel(
					((NestedCalculateFormulaException) e)
							.getExceptionsAsTable(),
					columnTitles) {
				// To set all Cells read-only
				private static final long serialVersionUID = 2451154203730538735L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};
			JTable errorList = new JTable(tableModel);
			this.add(new JScrollPane(errorList), BorderLayout.CENTER);
		} else {
			this.add(new JLabel(e.getLocalizedMessage()), BorderLayout.CENTER);
		}
	}

}
