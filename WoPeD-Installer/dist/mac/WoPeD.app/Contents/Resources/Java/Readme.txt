WoPeD (Workflow Petrinet Designer) is an easy-to-use tool to 
draw, manage, simulate and analyse workflow process definitions 
using an extended Petri net notation called "workflow nets".
   
Copyright (C) 2003-2013		
 
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
Java 6.0 or newer on your machine.  

WoPeD is known to run on Microsoft Windows platforms, on MacOS X and on 
many Linux distributions. Any reports of successfully having installed 
WoPeD on any other systems are very much appreciated, as well as negative 
experiences.
   
   
Download and Run
----------------
 
Download the latest version via the WoPeD website www.woped.org.

For Microsoft Windows, download WoPeD-install-windows-3.0.2.zip, 
unpack it with an archiving tool and run the contained executable 
WoPeD-install.exe.  

For Linux, download WoPeD-install-linux-3.0.2.tgz, unpack and 
decompress it with the tar utility. Then launch the contained executable 
WoPeD-install. 

For MacOS, download WoPeD-install-macos-3.0.2.pkg, move 
it into the target folder of your choice and launch the package file. 

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
  ./WoPeD                           (Launcher for Linux - alternatively)
  ./WoPeD-classes-3.0.2.jar         (WoPeD program classes)

lib directory
  ./lib/antlrworks-1.4.0.jar 
  ./lib/bpelBeans.jar                
  ./lib/flamingo-7.0.0.jar 
  ./lib/flanagan-1.0.0.jar           
  ./lib/jbpt-0.1.0.jar
  ./lib/jcalendar-1.3.2.jar          
  ./lib/jcommon-1.0.15.jar           
  ./lib/jfreechart-1.0.12.jar        
  ./lib/jgraph-5.10.2.jar             
  ./lib/jgraphx-1.5.1.jar             
  ./lib/jsr-1.7.3.jar             
  ./lib/log4j-1.2.8.jar    
  ./lib/metricsBeans.jar             
  ./lib/mysqlconnector-5.1.5.jar
  ./lib/pnmlBeans.jar                
  ./lib/pnmlBeans_old.jar           
  ./lib/ruddi-1.0.0.jar
  ./lib/ssj-2.1.3.jar  
  ./lib/stax-1.2.0.jar              
  ./lib/tablelayout-0.8.6.jar 
  ./lib/trident-1.2.0.jar 
  ./lib/xmlbeans-2.3.0.jar                   
  
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