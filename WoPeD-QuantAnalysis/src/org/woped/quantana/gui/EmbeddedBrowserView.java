package org.woped.quantana.gui;
/*
import java.awt.Dialog;
import java.awt.Image;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.woped.gui.translations.Messages;

public class EmbeddedBrowserView extends JFrame {
	private static int serverPort;
	private static boolean bIsAbleToUseJfx = false;
	private static boolean bAutoUpdate = false;

	public EmbeddedBrowserView(int port, boolean bAutoUpdate2) throws JFXUsageNotSupported {

		
		super();
		
		EmbeddedBrowserView.bAutoUpdate = bAutoUpdate2;
		try{
			if(bIsAbleToUseJfx == true){
				this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
				
				this.setTitle("Dashboard");
		
				final JFXPanel fxPanel = new JFXPanel();
				this.setSize(1024, 800);
				this.add(fxPanel);
				serverPort = port;
				this.setAlwaysOnTop(true);
				this.setIconImage(Messages.getImageIcon("Application").getImage());
				
				Platform.runLater(new Runnable() { // this will run initFX as
					// JavaFX-Thread
					@Override
					public void run() {
						initFX(fxPanel);
					}
				});
			}
			else{
				//JFXUsageNotSupported ex = new JFXUsageNotSupported();
				throw(new Exception());
			}
		}
		catch(Exception e){

			bIsAbleToUseJfx = false;
			this.removeAll();
			Platform.exit();
			JFXUsageNotSupported ex = new JFXUsageNotSupported();
			throw(ex);
		}

		
	}
	
	public EmbeddedBrowserView(int port) throws JFXUsageNotSupported {
		this(port, false);
	}

	private static final long serialVersionUID = 1L;

	private static void initFX(final JFXPanel fxPanel) {

		WebView webView = new WebView();

		// Obtain the webEngine to navigate
		WebEngine webEngine = webView.getEngine();
		webEngine.load("http://localhost:" + serverPort
				+ "/getrequest/?action=showdashboardFull&AutoUpdate=" + Boolean.toString(bAutoUpdate));
		fxPanel.setScene(new Scene(webView));

	}

}
*/
