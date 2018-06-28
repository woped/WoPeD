package org.woped.starter.osxMenu;

import java.awt.Window;
import java.lang.reflect.Method;

import org.woped.core.utilities.LoggerManager;
import org.woped.starter.Constants;

public class OSXFullscreen {
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void enableOSXFullscreen(Window window) {
		try {
			Class FsUtil = Class.forName("com.apple.eawt.FullScreenUtilities");
			Class parameters[] = new Class[]{Window.class, Boolean.TYPE};
			Method method = FsUtil.getMethod("setWindowCanFullScreen", parameters);
			method.invoke(FsUtil, window, true);
			LoggerManager.info(Constants.GUI_LOGGER, "Enabled Mac fullscreen support.");
		} catch (ClassNotFoundException e1) {
		} catch (Exception e) {
			LoggerManager.info(Constants.GUI_LOGGER, "Failed to enable Mac Fullscreen: "+e);
		}
	}
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void toggleOSXFullscreen(Window window) {
		try {
			Class appClass = Class.forName("com.apple.eawt.Application");
 
			Method method = appClass.getMethod("getApplication");
			Object appInstance = method.invoke(appClass);
 
			Class parameters[] = new Class[]{Window.class};
			method = appClass.getMethod("requestToggleFullScreen", parameters);
			method.invoke(appInstance, window);
		} catch (ClassNotFoundException e1) {
		} catch (Exception e) {
			LoggerManager.info(Constants.GUI_LOGGER, "Failed to toggle Mac Fullscreen: "+e);
			}
	}
}
