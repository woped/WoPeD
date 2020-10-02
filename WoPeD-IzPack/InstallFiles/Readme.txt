WoPeD (Workflow Petrinet Designer) is an easy-to-use tool to 
draw, manage, simulate and analyse workflow process definitions 
using an extended Petri net notation called "workflow nets".
   
Copyright (C) 2003-2018		
 
Contact
-------
   
Prof. Dr. Thomas Freytag
DHBW Karlsruhe
D-76231 Karlsruhe
Germany
Phone +49-721-9735-937
   
Website: www.woped.org
Email:   info@woped.org
   
   
Table of Contents
-----------------

  * License Agreement
  * Quick Start
  * Download and Run
  * Contained Files

   
License Agreement
-----------------
  
WoPeD is published under the GNU LESSER GENERAL PUBLIC LICENSE.
See attached file License.txt for more info.
   
      
Quick Start
-----------

This is just a short introductive description. For more information
refer to the online user manual available within WoPeD or on the WoPeD 
website http://www.woped.org.
   
Note: WoPeD is written in Java. It is necessary that you have installed 
Java 8 on your machine. Please note that WoPeD does not yet support the 
latest Java versions 9 and 10. 

WoPeD is known to run on Microsoft Windows platforms, on MacOS X and on 
many Linux distributions. Any reports of successfully having installed 
WoPeD on any other systems are very much appreciated, as well as negative 
experiences.
   
   
Download and Run
----------------
 
Download the latest version via the WoPeD website www.woped.org.

For Microsoft Windows, download WoPeD-install-windows-@wopedversion@.zip, 
unpack it with an archiving tool and run the contained executable 
WoPeD-install.exe.  

For Linux, download WoPeD-install-linux-@wopedversion@.tgz, unpack and 
decompress it with the tar utility. Then launch the contained executable 
WoPeD-install-linux.jar. 

For MacOS, download WoPeD-install-macos-@wopedversion@.dmg, double-click on it
to mount the image and launch the contained package file 
WoPeD-install-macos.pkg. 

In all three cases, a setup wizard appears and will guide you through 
the installation process, letting you choose several installation
options. Note that the creation of desktop and start menu shortcuts 
may not work on all target GUI systems.

You may want to assign WoPeD as the standard application opening PNML 
documents. Under Windows this is simply done in the control panel, under
MacOS it is already enabled in the package properties. Try it.

That's it!
   

Contained Files
-----------------

Root directory:

  ./Changelog.txt                   (Release info)
  ./License.txt                     (License file)
  ./Readme.txt                      (This file)
  ./WoPeD.exe                       (Launcher for Windows)
  ./WoPeD-classes-@wopedversion@.jar         (WoPeD program classes)

lib directory
  Various external JAR libraries to run WoPeD                   
  
doc directory
  ./doc/html/de                     (Complete HTML online manual in German - still rudimentary)
  ./doc/html/en                     (Complete HTML online manual in English)
  ./doc/html/images                 (Auxiliary files for online manual)
  ./doc/pdf/de                      (PDF formatted manuals in German)
  ./doc/pdf/en                      (PDF formatted manuals in English)

nets directory
  ./nets                            (Suggested folder to store your nets)

Uninstaller directory
  ./Uninstaller/uninstaller.jar     (uninstaller Java programm, Windows and Linux only)
	
	
Enjoy it, any feedback will be welcome!

The WoPeD drivers