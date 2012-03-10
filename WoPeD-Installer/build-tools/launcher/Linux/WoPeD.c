/*
 * Copyright (c) 2004 Alexis Nagy
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char jarName[20]; 
char javaPath[20];
char currPath[512];
int  foundJRE = 0;

/*
 * This method is searching for a JRE in predefined paths. It returns 1 as soon as 
 * a JRE has been found and 0 if no JRE has been found in any of the paths.
 */
int javasearch()
{
    char java[7][20];
    int i;
   
    // define paths to search in:
    strcpy(java[0], "/usr/bin/java");
    strcpy(java[1], "/bin/java");
    strcpy(java[2], "/sbin/java");
    strcpy(java[3], "/usr/bin/local/java");
    strcpy(java[4], "/usr/sbin/java");
    strcpy(java[5], "/usr/java/bin/java");
    strcpy(java[6], "~/bin");
        
    for (i = 0; i < 7; i++) // has to be as big as the array
    {
        FILE *javatest;
        
        javatest = fopen(java[i],"r");
        
        if (javatest)
        {
            // found JRE
            strcpy(javaPath, java[i]);
            strcat(javaPath, " -jar ");
            foundJRE = 1;
	        fclose(javatest);
	    return 1;
        }
    }

    
    strcpy("java -jar ", javaPath);
    return 0;
}

int main(int argc, char* args[])
{
    int i;
    char cmd[1024];

    strcpy(currPath, args[0]);
    strcpy(jarName, "/WoPeD.jar");

    // search for JRE
    if (javasearch() == 0)
    {
        printf("%s \n", "Sorry, but I could not find your JRE. \
			\nMaybe it's in your $ENV, trying ... \n\
			If this does not work please install a Java Runtime Environment \
			or call WoPeD install manually with java -jar %s", jarName);
		return 0;
    }
    
    
    for (i = strlen(currPath)-1; i >= 0 && currPath[i] != '/'; i--);

    currPath[i] = '\0';

    strcpy(cmd, "");
    strcat(cmd, javaPath);
    strcat(cmd, " ");
    strcat(cmd, currPath);
    strcat(cmd, jarName); 
	for (i = 1; i < argc; i++) {
		strcat(cmd, " ");
		strcat(cmd, args[i]);
	}

    system(cmd);

    printf("%s\n", cmd);
    
    return 1;
}
