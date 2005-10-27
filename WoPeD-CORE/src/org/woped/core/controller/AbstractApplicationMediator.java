/*
 * Created on 19.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.core.controller;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IConfiguration;
import org.woped.core.config.LoggerManager;
import org.woped.core.gui.IUserInterface;

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractApplicationMediator implements IViewListener {
	private HashMap viewControllerMap = null;

	private VEPController vepController = null;

	private IUserInterface ui = null;

	public AbstractApplicationMediator(IUserInterface ui, IConfiguration conf) {
		viewControllerMap = new HashMap();
		this.ui = ui;
		LoggerManager.info(Constants.CORE_LOGGER, "START INIT Application");
		boolean confOK = true;
		if (conf != null) {
			// Init Configuration
			confOK = conf.initConfig();
		}
		// use system look and feel
		try {
			if (!confOK) {
				// This should NEVER happen!
				LoggerManager.fatal(Constants.CORE_LOGGER,
						"Could really not load any configuration.");
				LoggerManager.info(Constants.CORE_LOGGER,
						"EXIT WoPeD - LOGGING DEACTIVATED");
				JOptionPane.showMessageDialog(null,
						"Could really not load any configuration!",
						"Init Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			} else {
				ConfigurationManager.setConfiguration(conf);
				if (ConfigurationManager.getConfiguration().getLookAndFeel() != null) {
					UIManager.setLookAndFeel(ConfigurationManager
							.getConfiguration().getLookAndFeel());
				} else {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					ConfigurationManager.getConfiguration().setLookAndFeel(
							UIManager.getSystemLookAndFeelClassName());
				}
			}
			LoggerManager.debug(Constants.CORE_LOGGER, "Look-And-Feel set.");
		} catch (Exception e) {
			LoggerManager.error(Constants.CORE_LOGGER,
					"Could not set System Look-And-Feel"
							+ ConfigurationManager.getConfiguration()
									.getLookAndFeel());
		}
	}

	public static IViewController createViewController(String className,
			String id) {
		Class[] argsClass = null;
		Object[] args = null;

		argsClass = new Class[] { String.class };
		args = new Object[] { id, };
		try {

			// Creating the ViewController via Reflection
			ClassLoader classLoader = AbstractApplicationMediator.class
					.getClassLoader();
			Class theClass = classLoader.loadClass(className);

			IViewController vc = null;
			Constructor constructor = null;
			if (argsClass != null) {
				constructor = theClass.getConstructor(argsClass);
				vc = (IViewController) constructor.newInstance(args);
			} else {
				vc = (IViewController) theClass.newInstance();
			}

			return vc;
		} catch (Exception e1) {
			LoggerManager.error(Constants.CORE_LOGGER,
					"Could not create the ViewController (ID:" + id + ")"
							+ e1.getMessage());
			return null;
		}
	}

	/**
	 * Returns the reference to the controller of ViewEventProcessors (VEP),
	 * which holds a map of VEPs, where the key is the major code of the event
	 * to be handled.
	 * 
	 * @return
	 */
	public VEPController getVepController() {
		if (vepController == null) {
			vepController = new VEPController();
		}
		return vepController;
	}

	public void removeViewController(IViewController viewController) {
		viewControllerMap.remove(viewController.getId());
	}

	public void fireViewEvent(AbstractViewEvent viewevent) {
		viewEventPerformed(viewevent);
	}

	public final void viewEventPerformed(AbstractViewEvent viewevent) {
		processViewEvent(viewevent);
	}

	public void processViewEvent(AbstractViewEvent viewevent) {
		AbstractEventProcessor vep;
		if ((vep = (AbstractEventProcessor) getVepController().lookup(
				viewevent.getType())) != null)
			vep.processViewEvent(viewevent);
	}

	public void addViewController(IViewController viewController) {
		viewController.addViewListener(this);
		viewControllerMap.put(viewController.getId(), viewController);
	}

	public IViewController getViewController(String id) {
		return (IViewController) viewControllerMap.get(id);
	}

	public Map getViewControllers() {
		return viewControllerMap;
	}

	public IUserInterface getUi() {
		return ui;
	}

	public void setUi(IUserInterface ui) {
		this.ui = ui;
	}
}
