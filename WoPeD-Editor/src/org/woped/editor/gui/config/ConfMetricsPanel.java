package org.woped.editor.gui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.woped.config.metrics.WoPeDMetricsConfiguration;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

@SuppressWarnings("serial")
public class ConfMetricsPanel extends AbstractConfPanel
{
	private JPanel         	metricsPanel    					= null;
    private JCheckBox 		useMetricsCheckBox					= null;
    private JPanel			metricsDisplayPanel 				= null;
    private JSpinner		variableDecimalPlacesSpinner 		= null;
    private JSpinner		algorithmDecimalPlacesSpinner		= null;		
    private JCheckBox		useAlgorithmHighlightingCheckBox	= null;
    private JPanel			metricsBuilderPanel					= null;
    private JCheckBox		showNamesInBuilderCheckBox			= null;
    private JCheckBox		showAdvancedErrorMessagesCheckBox  = null;

    /**
     * Constructor for ConfMetricsPanel
     */
    public ConfMetricsPanel(String name)
    {
        super(name);
        initialize();
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration()
    {
        if (useMetricsCheckBox.isSelected() != ConfigurationManager.getConfiguration().isUseMetrics())
        {
            JOptionPane.showMessageDialog(this, Messages.getString("Configuration.Metrics.Dialog.Restart.Message"), Messages.getString("Configuration.Metrics.Dialog.Restart.Title"),
                    JOptionPane.INFORMATION_MESSAGE);
            ConfigurationManager.getConfiguration().setUseMetrics(useMetricsCheckBox.isSelected());
        
            // Load metrics configuration if check box is selected and 
            // we do not have a non-static metrics configuration loaded yet
            if (useMetricsCheckBox.isSelected() && !ConfigurationManager.hasNonStaticMetricsConfiguration()) {
            	WoPeDMetricsConfiguration metricsConfig = new WoPeDMetricsConfiguration();
            	metricsConfig.initConfig();
            	ConfigurationManager.setMetricsConfiguration(metricsConfig);
            }
        
            ConfigurationManager.getConfiguration().setAlgorithmDecimalPlaces(Integer.parseInt(algorithmDecimalPlacesSpinner.getModel().getValue().toString()));
            ConfigurationManager.getConfiguration().setVariableDecimalPlaces(Integer.parseInt(variableDecimalPlacesSpinner.getModel().getValue().toString()));
            ConfigurationManager.getConfiguration().setUseAlgorithmHighlighting(useAlgorithmHighlightingCheckBox.isSelected());
            ConfigurationManager.getConfiguration().setShowNamesInBuilder(showNamesInBuilderCheckBox.isSelected());
            ConfigurationManager.getConfiguration().setShowAdvancedErrorMessages(showAdvancedErrorMessagesCheckBox.isSelected());
        }
        
    	return true;
    }

    /**
     * @see AbstractConfPanel#readConfiguration()
     */
    public void readConfiguration()
    {
    	if (ConfigurationManager.getConfiguration().isUseMetrics())
    		useMetricsCheckBox.setSelected(true);
    	else
    		useMetricsCheckBox.setSelected(false);
    	
    	variableDecimalPlacesSpinner.setValue(ConfigurationManager.getConfiguration().getVariableDecimalPlaces());
    	algorithmDecimalPlacesSpinner.setValue(ConfigurationManager.getConfiguration().getAlgorithmDecimalPlaces());
    	useAlgorithmHighlightingCheckBox.setSelected(ConfigurationManager.getConfiguration().isUseAlgorithmHighlighting());
    	showNamesInBuilderCheckBox.setSelected(ConfigurationManager.getConfiguration().isShowNamesInBuilder());
    	showAdvancedErrorMessagesCheckBox.setSelected(ConfigurationManager.getConfiguration().isShowAdvancedErrorMessages());
    }

    private void initialize()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getMetricsPanel(), c);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(getMetricsDisplayPanel(), c);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 2;
        contentPanel.add(getMetricsBuilderPanel(), c);
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 3;
       		 
        contentPanel.add(new JPanel(), c);
        setMainPanel(contentPanel);
    }

	class CheckboxListener implements ItemListener{					

		public void itemStateChanged(ItemEvent ie) {
			JCheckBox jcb = (JCheckBox) ie.getSource();
			getMetricsDisplayPanel().setVisible(jcb.isSelected());
			getMetricsBuilderPanel().setVisible(jcb.isSelected());
		}
	}

    // ################## GUI COMPONENTS #################### */

    private JCheckBox getUseMetricsCheckBox() {
        if (useMetricsCheckBox == null)
        {
        	useMetricsCheckBox = new JCheckBox(Messages.getString("Configuration.Metrics.UseMetrics.Label"));
        	useMetricsCheckBox.setMinimumSize(new Dimension(100, 20));
        	useMetricsCheckBox.setToolTipText(Messages.getString("Configuration.Metrics.UseMetrics.Tooltip"));
            CheckboxListener cbl = new CheckboxListener();
            useMetricsCheckBox.addItemListener(cbl);
        }
        
        return useMetricsCheckBox;
    }
    
    private JPanel getMetricsPanel() {
        if (metricsPanel == null)
        {
        	metricsPanel = new JPanel();
        	metricsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            metricsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Metrics.Panel.MetricsUsage.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            metricsPanel.add(getUseMetricsCheckBox(), c);

        }
        return metricsPanel;
    }
    
    private JPanel getMetricsDisplayPanel() {
    	if (metricsDisplayPanel == null) {
    		metricsDisplayPanel = new JPanel();
    		
    		metricsDisplayPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            metricsDisplayPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Metrics.Panel.MetricsDisplay.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));
            
            c.insets = new Insets(0, 4, 0, 0);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            metricsDisplayPanel.add(new JLabel(Messages.getString("Configuration.Metrics.VariableDecimalPlaces.Label")), c);
            c.weightx = 1;
            c.gridx = 10;
            c.gridy = 0;
            metricsDisplayPanel.add(getVariableDecimalPlacesSlider(), c);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            metricsDisplayPanel.add(new JLabel(Messages.getString("Configuration.Metrics.AlgorithmDecimalPlaces.Label")), c);
            c.weightx = 1;
            c.gridx = 10;
            c.gridy = 1;
            metricsDisplayPanel.add(getAlgorithmDecimalPlacesSlider(), c);
            
            c.insets = new Insets(0, 0, 0, 0);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            metricsDisplayPanel.add(getUseAlgorithmHighlightingCheckBox(), c);
    	}
    	
    	metricsDisplayPanel.setVisible(getUseMetricsCheckBox().isSelected());
    	return metricsDisplayPanel;
    }

	private JSpinner getVariableDecimalPlacesSlider() {
		if (variableDecimalPlacesSpinner == null) {
			variableDecimalPlacesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
			((JSpinner.DefaultEditor)variableDecimalPlacesSpinner.getEditor()).getTextField().setEditable(false);
			((JSpinner.DefaultEditor)variableDecimalPlacesSpinner.getEditor()).getTextField().setEnabled(true);
		}
		return variableDecimalPlacesSpinner;
	}

	private JSpinner getAlgorithmDecimalPlacesSlider() {
		if (algorithmDecimalPlacesSpinner == null) {
			algorithmDecimalPlacesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
			((JSpinner.DefaultEditor)algorithmDecimalPlacesSpinner.getEditor()).getTextField().setEditable(false);
			((JSpinner.DefaultEditor)algorithmDecimalPlacesSpinner.getEditor()).getTextField().setEnabled(true);
		}
		return algorithmDecimalPlacesSpinner;
	}
	
	private JCheckBox getUseAlgorithmHighlightingCheckBox() {
		if(useAlgorithmHighlightingCheckBox == null) 
			useAlgorithmHighlightingCheckBox = new JCheckBox(Messages.getString("Configuration.Metrics.UseAlgorithmHighlighting"));
			
		return useAlgorithmHighlightingCheckBox;
	}
	
	private JPanel getMetricsBuilderPanel() {
		if (metricsBuilderPanel == null)
        {
			metricsBuilderPanel = new JPanel();
        	metricsBuilderPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            metricsBuilderPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Metrics.Panel.MetricsBuilder.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            metricsBuilderPanel.add(getShowNamesInBuilderCheckBox(), c);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            metricsBuilderPanel.add(getShowAdvancedErrorMessagesCheckBox(), c);

        }
    	metricsBuilderPanel.setVisible(getUseMetricsCheckBox().isSelected());
        return metricsBuilderPanel;
	}
	
	private JCheckBox getShowNamesInBuilderCheckBox() {
		if(showNamesInBuilderCheckBox == null) 
			showNamesInBuilderCheckBox = new JCheckBox(Messages.getString("Configuration.Metrics.ShowNamesInBuilder"));
			
		return showNamesInBuilderCheckBox;
	}
	
	private JCheckBox getShowAdvancedErrorMessagesCheckBox() {
		if(showAdvancedErrorMessagesCheckBox == null) 
			showAdvancedErrorMessagesCheckBox = new JCheckBox(Messages.getString("Configuration.Metrics.ShowAdvancedErrorMessages"));
			
		return showAdvancedErrorMessagesCheckBox;
	}
}