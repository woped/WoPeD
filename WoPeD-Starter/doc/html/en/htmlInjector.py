import os
import re

JQUERY_LOCATION = '\"../jquery-1.10.2.js\"'
EXTENSIONS = ['.htm','.html']

for file in os.listdir("./"):
	for extension in EXTENSIONS:
	    if file.endswith(extension):
	        #print file
	        f = open("./"+file, "r+")
	        text = f.read()
	        idMatch = text.find("injectedScriptByC4Br") #Check whether the script does already exist
	        if idMatch == -1:
			try:
		        	match = text.find("</head>")
		        	beforeHead = match - 1        
		       		temp = text[match:] #store the content of the html file, starting with </head>
		        	injectingScript = "\n<script id='injectedScriptByC4Br' src=" + JQUERY_LOCATION + "></script>\n"
		        	f.seek(beforeHead) #set file cursor before </head> and write the script. Truncate the rest and rewrite it new to avoid any duplicates
		        	f.write(injectingScript)
		        	f.truncate()
		        	f.write(temp)
		        	f.close()
		        	#print match
			except IOError:
				print "Exception in " + file

print "Finished jQuery injection."

JQUERY_GENERAL_LOCATION = '\"../wopedGeneral.js\"'
EXTENSIONS = ['.htm','.html']

for file in os.listdir("./"):
	for extension in EXTENSIONS:
	    if file.endswith(extension):
	        #print file
	        f = open("./"+file, "r+")
	        text = f.read()
	        idMatch = text.find("injectedGeneralScriptByC4Br") #Check whether the script does already exist
	        if idMatch == -1:
			try:
		        	match = text.find("</head>")
		        	beforeHead = match - 1        
		       		temp = text[match:] #store the content of the html file, starting with </head>
		        	injectingScript = "\n<script id='injectedGeneralScriptByC4Br' src=" + JQUERY_GENERAL_LOCATION + "></script>\n"
		        	f.seek(beforeHead) #set file cursor before </head> and write the script. Truncate the rest and rewrite it new to avoid any duplicates
		        	f.write(injectingScript)
		        	f.truncate()
		        	f.write(temp)
		        	f.close()
		        	#print match
			except IOError:
				print "Exception in " + file

print "Finished jQuery general script injection."