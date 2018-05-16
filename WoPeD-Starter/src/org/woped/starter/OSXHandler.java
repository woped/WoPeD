/**
 * OSXHandler.java
 *
 * Created on 06-Jul-2016 10:42:57
 *
 * Abstract: Hooks existing preferences/about/quit functionality from an existing Java app into
 * handlers for the Mac OS X application menu.
 *
 * Uses a Proxy object to dynamically implement the java.awt.desktop.xxxHandler interfaces and register
 * it with the com.apple.eawt.Application object. This allows the complete project to be both built
 * and run on any platform without any stubs or placeholders. Useful for developers looking to
 * implement Mac OS X features while supporting multiple platforms with minimal impact.
 *
 * Version: 0.9
 */

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Christopher Tipper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.woped.starter;

import java.lang.reflect.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ctipper
 */
public class OSXHandler implements InvocationHandler {

    protected Object targetObject;
    protected Method targetMethod;
    protected String proxySignature;

    static Object macOSXApplication;

    private static final Logger logger = Logger.getLogger(OSXHandler.class.getName());

    /**
     * Each OSXHandler has the name of the EAWT method it intends to listen for (handleAbout, for
     * example), the Object that will ultimately perform the task, and the Method to be called on
     * that Object
     */
    protected OSXHandler(String proxySignature, Object target, Method handler) {
        this.proxySignature = proxySignature;
        this.targetObject = target;
        this.targetMethod = handler;
    }

    /**
     * Pass this method an Object and Method equipped to perform application shutdown logic The
     * QuitResponse may be used to respond to a request to quit the application.
     */
    public static void setQuitHandler(Object target, Method quitHandler) {
        OSXHandler adapter = new OSXHandler("handleQuitRequestWith", target, quitHandler) {

            public boolean callTarget(Object appleEvent, Object response) {
                if (appleEvent != null) {
                    try {
                        this.targetMethod.invoke(this.targetObject, new Object[] { appleEvent, response });
                    } catch (IllegalAccessException | IllegalArgumentException | SecurityException |
                             InvocationTargetException ex) {
                        logger.severe("Mac OS X Adapter could not talk to EAWT:");
                    }
                }
                return true;
            }
        };
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            if (macOSXApplication == null) {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
            Class<?> quitHandlerClass = Class.forName("java.awt.desktop.QuitHandler");
            Method addHandlerMethod = applicationClass.getDeclaredMethod("setQuitHandler", new Class<?>[] { quitHandlerClass });
            // Create a proxy object around this handler that can be reflectively added as an Apple AppEvent handler
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXHandler.class.getClassLoader(), new Class<?>[] { quitHandlerClass }, adapter);
            addHandlerMethod.invoke(macOSXApplication, new Object[] { osxAdapterProxy });
        } catch (ClassNotFoundException cnfe) {
            logger.log(Level.SEVERE, "This version of Mac OS X does not support the Apple EAWT. ApplicationEvent handling has been disabled ({0})", cnfe.getMessage());
            cnfe.printStackTrace();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // Likely a NoSuchMethodException or an IllegalAccessException loading/invoking eawt.Application methods
            logger.severe("Mac OS X Adapter could not talk to EAWT:");
            logger.severe(ex.toString());
        }
    }

    /**
     * Pass this method an Object and Method equipped to display application info. They will be
     * called when the About menu item is selected from the application menu
     */
    public static void setAboutHandler(Object target, Method aboutHandler) {
        OSXHandler adapter = new OSXHandler("handleAbout", target, aboutHandler) {

            public boolean callTarget(Object appleEvent) {
                if (appleEvent != null) {
                    try {
                        this.targetMethod.invoke(this.targetObject, new Object[] { appleEvent });
                    } catch (IllegalAccessException | IllegalArgumentException | SecurityException |
                             InvocationTargetException ex) {
                        logger.severe("Mac OS X Adapter could not talk to EAWT:");
                    }
                }
                return true;
            }
        };
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            if (macOSXApplication == null) {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
            Class<?> aboutHandlerClass = Class.forName("java.awt.desktop.AboutHandler");
            Method addHandlerMethod = applicationClass.getDeclaredMethod("setAboutHandler", new Class<?>[] { aboutHandlerClass });
            // Create a proxy object around this handler that can be reflectively added as an Apple AppEvent handler
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXHandler.class.getClassLoader(), new Class<?>[] { aboutHandlerClass }, adapter);
            addHandlerMethod.invoke(macOSXApplication, new Object[] { osxAdapterProxy });
        } catch (ClassNotFoundException cnfe) {
            logger.log(Level.SEVERE, "This version of Mac OS X does not support the Apple EAWT. ApplicationEvent handling has been disabled ({0})", cnfe.getMessage());
            cnfe.printStackTrace();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // Likely a NoSuchMethodException or an IllegalAccessException loading/invoking eawt.Application methods
            logger.severe("Mac OS X Adapter could not talk to EAWT:");
            logger.severe(ex.toString());
        }
    }

    /**
     * Pass this method an Object and a Method equipped to display application options. They will be
     * called when the Preferences menu item is selected from the application menu
     */
    public static void setPreferencesHandler(Object target, Method prefsHandler) {
        OSXHandler adapter = new OSXHandler("handlePreferences", target, prefsHandler) {

            public boolean callTarget(Object appleEvent) {
                if (appleEvent != null) {
                    try {
                        this.targetMethod.invoke(this.targetObject, new Object[] { appleEvent });
                    } catch (IllegalAccessException | IllegalArgumentException | SecurityException |
                             InvocationTargetException ex) {
                        logger.severe("Mac OS X Adapter could not talk to EAWT:");
                    }
                }
                return true;
            }
        };
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            if (macOSXApplication == null) {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
            Class<?> prefsHandlerClass = Class.forName("java.awt.desktop.PreferencesHandler");
            Method addHandlerMethod = applicationClass.getDeclaredMethod("setPreferencesHandler", new Class<?>[] { prefsHandlerClass });
            // Create a proxy object around this handler that can be reflectively added as an Apple AppEvent handler
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXHandler.class.getClassLoader(), new Class<?>[] { prefsHandlerClass }, adapter);
            addHandlerMethod.invoke(macOSXApplication, new Object[] { osxAdapterProxy });
        } catch (ClassNotFoundException cnfe) {
            logger.log(Level.SEVERE, "This version of Mac OS X does not support the Apple EAWT. ApplicationEvent handling has been disabled ({0})", cnfe.getMessage());
            cnfe.printStackTrace();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // Likely a NoSuchMethodException or an IllegalAccessException loading/invoking eawt.Application methods
            logger.severe("Mac OS X Adapter could not talk to EAWT:");
            logger.severe(ex.toString());
        }
    }

    /**
     * Pass this method an Object and a Method equipped to handle document events from the Finder.
     * Documents are registered with the Finder via the CFBundleDocumentTypes dictionary in the
     * application bundle's Info.plist
     */
    public static void setFileHandler(Object target, Method fileHandler) {
        OSXAdapter adapter = new OSXAdapter("openFiles", target, fileHandler) {

            // Override OSXHandler.callTarget to send information on the file to be opened
            public boolean callTarget(Object appleEvent) {
                if (appleEvent != null) {
                    try {
                        Method getFilenameMethod = appleEvent.getClass().getMethod("getFiles", (Class[]) null);
                        @SuppressWarnings("rawtypes")
						java.util.List files = (java.util.List) getFilenameMethod.invoke(appleEvent, (Object[]) null);
                        this.targetMethod.invoke(this.targetObject, new Object[] { files });
                    } catch (IllegalAccessException | IllegalArgumentException |
                             NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                        logger.severe("Mac OS X Adapter could not talk to EAWT:");
                    }
                }
                return true;
            }
        };
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            if (macOSXApplication == null) {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
            Class<?> filesHandlerClass = Class.forName("java.awt.desktop.OpenFilesHandler");
            Method addHandlerMethod = applicationClass.getDeclaredMethod("setOpenFileHandler", new Class<?>[] { filesHandlerClass });
            // Create a proxy object around this handler that can be reflectively added as an Apple AppEvent handler
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXHandler.class.getClassLoader(), new Class<?>[] { filesHandlerClass }, adapter);
            addHandlerMethod.invoke(macOSXApplication, new Object[] { osxAdapterProxy });
        } catch (ClassNotFoundException cnfe) {
            logger.log(Level.SEVERE, "This version of Mac OS X does not support the Apple EAWT. ApplicationEvent handling has been disabled ({0})", cnfe.getMessage());
            cnfe.printStackTrace();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // Likely a NoSuchMethodException or an IllegalAccessException loading/invoking eawt.Application methods
            logger.severe("Mac OS X Adapter could not talk to EAWT:");
            logger.severe(ex.toString());
        }
    }

    /**
     * InvocationHandler implementation This is the entry point for our proxy object; it is called
     * every time an AppEvent method is invoked
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // All of the AppEvent methods are void; return null regardless of what happens
        if (isCorrectMethod(method, args)) {
            if (args.length == 1) {
                callTarget(args[0]);
            } else {
                callTarget(args[0], args[1]);
            }
        }
        return null;
    }

    /**
     * Override this method to perform any operations on the event that comes with the various
     * callbacks See setFileHandler above for an example
     */
    public boolean callTarget(Object appleEvent) throws InvocationTargetException, IllegalAccessException {
        Object result = targetMethod.invoke(targetObject, (Object[]) null);
        if (result == null) {
            return true;
        }
        return Boolean.valueOf(result.toString());
    }

    /**
     * Override this method to perform any operations on the event that comes with the various
     * callbacks See setQuitHandler above for an example
     */
    public boolean callTarget(Object appleEvent, Object response) throws InvocationTargetException, IllegalAccessException {
        Object result = targetMethod.invoke(targetObject, (Object[]) null);
        if (result == null) {
            return true;
        }
        return Boolean.valueOf(result.toString());
    }

    /**
     * Compare the method that was called to the intended method when the OSXHandler instance was
     * created (e.g. handleAbout, handleQuitRequestWith, openFiles etc.)
     */
    protected boolean isCorrectMethod(Method method, Object[] args) {
        return (targetMethod != null && proxySignature.equals(method.getName()) && args.length > 0);
    }

}