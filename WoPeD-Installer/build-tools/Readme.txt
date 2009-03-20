WoPeD (Workflow Petrinet Designer) is an easy-to-use tool to 
draw, manage, simulate and analyse workflow process definitions 
using an extended Petri net notation called "workflow nets".
   
Copyright (C) 2003-2009		
 
Contact
-------
   
Prof. Dr. Thomas Freytag
Berufsakademie Karlsruhe
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
Java 6.0 or higher on your machine. Otherwise download it from 
http://java.sun.com. 

WoPeD is known to run on Microsoft Windows platforms (2000, XP, Vista), 
on MacOS X and on many Linux distributions. Any reports of successfully 
having installed WoPeD on any other systems are very much appreciated, 
as well as negative experiences.
   
   
Download and Run
----------------
 
Download the latest version via the WoPeD website www.woped.org.
For Microsoft Windows, download WoPeD-install-windows-@wopedversion@.zip, 
unpack it with an archiving tool and run the contained executable 
WoPeD-install.exe. 
Additionally you may want to assign WoPeD.exe as the standard 
application for the file type "pnml" to open a net with WoPeD 
just by double-clicking on the associated PNML document. 
For Linux, download WoPeD-install-linux-@wopedversion@.tgz, unpack and 
decompress it with the tar utility. Then run the contained executable 
WoPeD-install from the command line or desktop. 
In both cases, a setup wizard appears and will guide you through 
the installation process, letting you choose several installation
options like the target folder or the creation of a shortcut on 
your desktop and in the start menu. Note that the creation of 
desktop and start menu shortcuts may not work on all Linux-based 
GUI systems.
For MacOS, download WoPeD-install-macos-@wopedversion@.app.zip and unpack 
it into the target folder of your choice. The rest should be done 
automatically by MacOS. 

That's it!
   

Contained Files
-----------------

Root directory:

  ./Changelog.txt                   (Release info)
  ./License.txt                     (License file)
  ./Readme.txt                      (This file)
  ./WofJava.dll			    	    (Calling interface to Woflan tool - Windows only) 				     
  ./WoPeD.exe                       (Launcher for Windows)
  ./WoPeD                           (Launcher for Linux - alternatively)
  ./WoPeD-classes-@wopedversion@.jar         (WoPeD program classes)
  ./WoPeD.log                       (Error logging, initially not existing)
  ./WoPeDconfig.xml                 (WoPeD property settings, initially not existing)

lib directory
  ./lib/bpelBeans.jar               (BPEL model description library)
  ./lib/confBeans.jar               (Configuration management library)
  ./lib/pnmlBeans.jar               (Petri net model description library)
  ./lib/pnmlBeans_old.jar           (Petri net model description library, old version)
  ./lib/flanagan-@flanaganversion@.jar             (Random number generator library)
  ./lib/jcalendar-@jcalendarversion@.jar             (Calendar utility library)
  ./lib/jsr_@jsrversion@.jar        		(JSR utility library)
  ./lib/jgraph-@jgraphversion@.jar            (Graph drawing library)
  ./lib/log4j-@log4jversion@.jar             (Event logger library)
  ./lib/mysql-connector-@mysqlversion@.jar             (MySQL connector library)
  ./lib/ruddi-@ruddiversion@.jar             (UDDI access library)
  ./lib/stax-@staxversion@.jar             (Stax library)
  ./lib/xbean.jar                   (Bean generation library)

doc directory
  ./doc/html/de                    	(Complete HTML online manual in German - still rudimentary)
  ./doc/html/en                     (Complete HTML online manual in English)
  ./doc/html/images                 (Auxiliary files for online manual)
  ./doc/pdf/de						(PDF formatted manuals in German)
  ./doc/pdf/en						(PDF formatted manuals in English)

nets directory
  ./nets                            (Suggested folder to store your nets)

Uninstaller directory
  ./Uninstaller/uninstaller.jar     (uninstaller Java programm, Windows and Linux only)
	
	
Enjoy it, any feedback will be welcome!

The WoPeD drivers