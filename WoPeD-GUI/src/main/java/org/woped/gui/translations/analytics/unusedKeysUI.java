package org.woped.gui.translations.analytics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * The UI for analytic tools.
 */
@SuppressWarnings("serial")
public class unusedKeysUI extends JFrame implements TableModelListener  {

	/** current selected locale. */
	private Locale currLocale = Locale.ROOT;

    /** data list. */
    private ArrayList<String> dataList;

    /** The table. */
    private JTable table;

    /** The deletion list. */
    private HashSet<String> deletionList = new HashSet<String>();

    /** The ribbon UI. */
    private AnalysisRibbonBar ribbon = null;

    /**
     * Instantiates a new unusedKeysUI.
     */
    public unusedKeysUI() {
    	super("messages.properties Analyze-Tool");
    	createFrame();
    }

    /**
     * Instantiates a unusedKeysUI with data.
     *
     * @param data the data in JTable
     */
    public unusedKeysUI(ArrayList<String> data) {
        super("messages.properties Analyze-Tool");
        createFrame();
        dataList = data;
    }

    /**
     * Creates the frame.
     */
    private void createFrame() {
        setLayout(new BorderLayout());
        setSize(500, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //Ribbon instead of Toolbar
        ribbon = new AnalysisRibbonBar(this);
        //hide taskbar
        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createEmptyBorder(-25, 0, 0, 0));
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(ribbon,BorderLayout.CENTER);
        table = new JTable();
        add(containerPanel, BorderLayout.NORTH );
        add(new JScrollPane(table), BorderLayout.CENTER);

	}

	/**
	 * Close window.
	 */
	public void closeWindow() {
    	this.setVisible(false);
    	System.exit(0);
    }

	/**
	 * Sets the locale to en.
	 */
	public void setLocaleEN() {
    	this.currLocale = Locale.ROOT;
    }

	/**
	 * Sets the locale to de.
	 */
	public void setLocaleDE() {
    	this.currLocale = Locale.GERMAN;
    }

	/**
	 * Do analysis action.
	 */
	public void doAct() {
		ArrayList<File> files = new ArrayList<File>();
		String workingDir = System.getProperty("user.dir");
		workingDir = AnalysisTools.substringBeforeLastSeperator(workingDir, "/");

		ResourceBundle labels = ResourceBundle.getBundle("org.woped.gui.translations.Messages", currLocale);
		AnalysisTools.keyList = Collections.list(labels.getKeys());

		AnalysisTools.listFiles(workingDir, files);


			for (File file : files){
				AnalysisTools.checkUsageInFileExtend(file);
			}

		HashSet<String> usedKeySet = new HashSet<String>(AnalysisTools.usedKeys);
		AnalysisTools.keyList.removeAll(usedKeySet);
		dataList = AnalysisTools.keyList;


		Object[][] data = null;
		Object[] columnNames = null;
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        	   @Override
        	   public Class<?> getColumnClass(int columnIndex) {
        	      return getValueAt(0, columnIndex).getClass();
        	   }
        	};
        model.setColumnIdentifiers(new String[] { "mark for deletion", "Key" });
        model.setRowCount(dataList.size());
        int row = 0;
        for (String data2 : dataList) {
            model.setValueAt(new Boolean(false), row, 0);
            model.setValueAt(data2, row, 1);
            row++;
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getModel().addTableModelListener(this);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new SelectAllHeader(table, 0));
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        TableModel model = (TableModel)e.getSource();
        Boolean selected = (Boolean)model.getValueAt(row, 0);
        String key = (String) model.getValueAt(row, 1);
        if(selected){
        	deletionList.add(key);
        }
        else{
        	deletionList.remove(key);
        }
	}

	/**
	 * Delete selected keys.
	 */
	public void deleteKeys() {
		String workingDir = System.getProperty("user.dir");
		String file = workingDir + "/src/org/woped/gui/translations/";
		if(currLocale == Locale.GERMAN){
			file = file + "Messages_de.properties";
		}
		else if(currLocale  == Locale.ROOT){
			file = file + "Messages.properties";
		}
		for (String key : deletionList) {
			AnalysisTools.removeKeyFromFile(file, key);
		}
	}

	/**
	 * Copy keys to clipboard.
	 */
	public void copyToClipboard() {
		String stringSelection = "";
		for (String key : deletionList) {
			stringSelection += (key +"\n");
		}
		StringSelection selection = new StringSelection(stringSelection);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		JOptionPane.showMessageDialog(this, "Selection copied to clipboard");

	}
}
 @SuppressWarnings("serial")
class MyTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );
        return comp;
    }
}
 @SuppressWarnings("serial")
class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        boolean status = Boolean.parseBoolean(table.getModel().getValueAt(row, 1).toString());
        setSelected(status);
        table.repaint();
        return this;
    }
}
 @SuppressWarnings("serial")
class SelectAllHeader extends JToggleButton implements TableCellRenderer {

     private static final String ALL = "✓ Select all";
     private static final String NONE = "✓ Select none";
     private JTable table;
     private TableModel tableModel;
     private JTableHeader header;
     private TableColumnModel tcm;
     private int targetColumn;
     private int viewColumn;

     public SelectAllHeader(JTable table, int targetColumn) {
         super(ALL);
         this.table = table;
         this.tableModel = table.getModel();
         if (tableModel.getColumnClass(targetColumn) != Boolean.class) {
             throw new IllegalArgumentException("Boolean column required.");
         }
         this.targetColumn = targetColumn;
         this.header = table.getTableHeader();
         this.tcm = table.getColumnModel();
         this.applyUI();
         this.addItemListener(new ItemHandler());
         header.addMouseListener(new MouseHandler());
         tableModel.addTableModelListener(new ModelHandler());
     }

     @Override
     public Component getTableCellRendererComponent(
         JTable table, Object value, boolean isSelected,
         boolean hasFocus, int row, int column) {
         return this;
     }

     private class ItemHandler implements ItemListener {

         @Override
         public void itemStateChanged(ItemEvent e) {
             boolean state = e.getStateChange() == ItemEvent.SELECTED;
             setText((state) ? NONE : ALL);
             for (int r = 0; r < table.getRowCount(); r++) {
                 table.setValueAt(state, r, viewColumn);
             }
         }
     }

     @Override
     public void updateUI() {
         super.updateUI();
         applyUI();
     }

     private void applyUI() {
         this.setFont(UIManager.getFont("TableHeader.font"));
         this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
         this.setBackground(UIManager.getColor("TableHeader.background"));
         this.setForeground(UIManager.getColor("TableHeader.foreground"));
     }

     private class MouseHandler extends MouseAdapter {

         @Override
         public void mouseClicked(MouseEvent e) {
             viewColumn = header.columnAtPoint(e.getPoint());
             int modelColumn = tcm.getColumn(viewColumn).getModelIndex();
             if (modelColumn == targetColumn) {
                 doClick();
             }
         }
     }

     private class ModelHandler implements TableModelListener {

         @Override
         public void tableChanged(TableModelEvent e) {
             if (needsToggle()) {
                 doClick();
                 header.repaint();
             }
         }
     }

     private boolean needsToggle() {
         boolean allTrue = true;
         boolean allFalse = true;
         for (int r = 0; r < tableModel.getRowCount(); r++) {
             boolean b = (Boolean) tableModel.getValueAt(r, targetColumn);
             allTrue &= b;
             allFalse &= !b;
         }
         return allTrue && !isSelected() || allFalse && isSelected();
     }
 }
