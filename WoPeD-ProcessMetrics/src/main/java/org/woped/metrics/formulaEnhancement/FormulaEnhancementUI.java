package org.woped.metrics.formulaEnhancement;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.woped.gui.translations.Messages;

/**
 * @author Tobias Lorentz
 *
 */
public class FormulaEnhancementUI extends JFrame implements
		FormulaEnhancementListChangedEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3311752156025982287L;
	private String[] columnTitles = { Messages.getString("Metrics.FormulaEnhancements.UI.FormulaID"), Messages.getString("Metrics.FormulaEnhancements.UI.ActualFormula"), //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("Metrics.FormulaEnhancements.UI.EnhancedFormula") }; //$NON-NLS-1$
	private JTable formulaList;
	private JPanel buttonPanel;
	private JButton acceptButton;
	private JButton acceptAllButton;
	private JButton clearListButton;
	private JButton closeButton;

	public FormulaEnhancementUI() {
		super();
		setLayout(new BorderLayout());
		
		//Build the UI
		createUI();
		
		//Register the Listener
		FormulaEnhancementList.getInstance().addFormulaEnhancementListChangedEventListener(this);
		
		setVisible(true);
		setSize(getPreferredSize());
		this.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
	}

	private void createUI() {
		DefaultTableModel tableModel = new DefaultTableModel(
				FormulaEnhancementList.getInstance()
						.getEnhancementListAsArray(), columnTitles) {
			// To set all Cells read-only
			private static final long serialVersionUID = 2451154203730538735L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};
		formulaList = new JTable(tableModel);
		this.add(new JScrollPane(formulaList), BorderLayout.CENTER);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4));
		this.add(buttonPanel, BorderLayout.SOUTH);

		acceptButton = new JButton(Messages.getString("Metrics.FormulaEnhancements.UI.AcceptEnhancements")); //$NON-NLS-1$
		this.buttonPanel.add(acceptButton);
		acceptButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//read the selected row number
				int i = formulaList.getSelectedRow();
				
				//There is no row selected
				if(i == -1)
					return;
				try {
					FormulaEnhancementList.getInstance().acceptEnhancement(i);
				} catch (ArrayIndexOutOfBoundsException e) {
					return;
				}
			}
			
		});
		
		acceptAllButton = new JButton(Messages.getString("Metrics.FormulaEnhancements.UI.AcceptAllEnhancements")); //$NON-NLS-1$
		this.buttonPanel.add(acceptAllButton);
		acceptAllButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FormulaEnhancementList.getInstance().acceptAllEnhancements();
			}
			
		});


		clearListButton = new JButton(Messages.getString("Metrics.FormulaEnhancements.UI.ClearList")); //$NON-NLS-1$
		this.buttonPanel.add(clearListButton);
		clearListButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FormulaEnhancementList.getInstance().clearList();
			}

		});

		closeButton = new JButton(Messages.getString("Metrics.FormulaEnhancements.UI.Close")); //$NON-NLS-1$
		this.buttonPanel.add(closeButton);
		closeButton.addActionListener(new closeButtonActionListener(this));	

	}

	@Override
	public void formulaEnhancementListChanged(
			FormulaEnhancementListChangedEvent e) {
		final DefaultTableModel model = (DefaultTableModel) formulaList
				.getModel();
		model.setDataVector(FormulaEnhancementList.getInstance()
				.getEnhancementListAsArray(), columnTitles);
		model.fireTableDataChanged();
	}
	
	
	/**
	 * @author Tobias Lorentz
	 * This class is the Button-Listener for the close button
	 * It stores a reference to the formulaEnhancementUI for deregister the listener
	 * before closing the ui
	 */
	public class closeButtonActionListener implements ActionListener{
		
		private FormulaEnhancementUI feui;
		
		public closeButtonActionListener(FormulaEnhancementUI feui) {
			super();
			this.feui = feui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//deregstering the ui at the datacontainer
			FormulaEnhancementList.getInstance().removeFormulaEnhancementListChangedEventListener(feui);
			
			//Closing the UI
			feui.dispose();			
		}
		
	}
	

}
