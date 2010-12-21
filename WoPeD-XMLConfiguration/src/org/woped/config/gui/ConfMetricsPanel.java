package org.woped.config.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.woped.config.metrics.WoPeDMetricsConfiguration;
import org.woped.core.config.ConfigurationManager;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.translations.Messages;

@SuppressWarnings("serial")
public class ConfMetricsPanel extends AbstractConfPanel
{
	private JPanel         	metricsPanel    				= null;
    private JCheckBox 		useMetricsCheckBox				= null;
    private JPanel			metricsDisplayPanel 			= null;
    private JSpinner		variableDecimalPlacesSpinner 	= null;
    private JSpinner		algorithmDecimalPlacesSpinner	= null;

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
        ConfigurationManager.getConfiguration().setUseMetrics(useMetricsCheckBox.isSelected());
        
        // Load metrics configuration
        if (useMetricsCheckBox.isSelected()) {
        	WoPeDMetricsConfiguration metricsConfig = new WoPeDMetricsConfiguration(ConfigurationManager.isApplet());
        	metricsConfig.initConfig();
			ConfigurationManager.setMetricsConfiguration(metricsConfig);
        }
        
        ConfigurationManager.getConfiguration().setAlgorithmDecimalPlaces(Integer.parseInt(algorithmDecimalPlacesSpinner.getModel().getValue().toString()));
        ConfigurationManager.getConfiguration().setVariableDecimalPlaces(Integer.parseInt(variableDecimalPlacesSpinner.getModel().getValue().toString()));
       
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
    	
    	//variableDecimalPlacesSpinner.set(ConfigurationManager.getConfiguration().getVariableDecimalPlaces());
    	algorithmDecimalPlacesSpinner.setValue(ConfigurationManager.getConfiguration().getAlgorithmDecimalPlaces());
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
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 2;
        contentPanel.add(new JPanel(), c);
        setMainPanel(contentPanel);
    }

    // ################## GUI COMPONENTS #################### */

    private JCheckBox getUseMetricsCheckBox() {
        if (useMetricsCheckBox == null)
        {
        	useMetricsCheckBox = new JCheckBox("<html>" + Messages.getString("Configuration.Metrics.UseMetrics.Label") + "</html>");
        	useMetricsCheckBox.setMinimumSize(new Dimension(100, 20));
        	useMetricsCheckBox.setToolTipText("<html>" + Messages.getString("Configuration.Metrics.UseMetrics.Tooltip") + "</html>");
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
            c.gridy = 3;
            metricsDisplayPanel.add(new JLabel(Messages.getString("Configuration.Metrics.AlgorithmDecimalPlaces.Label")), c);
            c.weightx = 1;
            c.gridx = 10;
            c.gridy = 3;
            metricsDisplayPanel.add(getAlgorithmDecimalPlacesSlider(), c);
    	}
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
}