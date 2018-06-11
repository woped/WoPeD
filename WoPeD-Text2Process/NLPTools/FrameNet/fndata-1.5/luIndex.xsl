<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://framenet.icsi.berkeley.edu">
<xsl:output method="html" />
<!-- This XSL file transforms luIndex XML into a page with alphabetical
     links for listing LUs and a search box that finds LUs that start with
     a user's query. It has two completely distinct sets of Javascript code,
     specified by the xsl:param 'dynamic': dynamic mode is used by the Dynamic Web
     Report System and normal mode is used in all other cases.
     The dynamic mode's alphabetical links and search box load new luIndex
     XML files with the 'startsWith' paramater passed through the URL. XSL is
     used to populate the Javascript with the XML data then the Javascript is
     executed.
     The normal mode's alphabetical links and search box transform parts of the
     XML file by calling the XSL processor directly in the Javascript
     with the xsl:param 'runXSL' on and the xsl:param 'startsWith' specifying
     which LUs to list. The result of the XSL processor is displayed on the page. -->
<xsl:param name='runXSL'></xsl:param>
<xsl:param name='startsWith'></xsl:param>
<xsl:param name='dynamic'>false</xsl:param>
<xsl:template match="/fn:luIndex">

<!-- this block is only executed when the normal mode calls the XSL processor -->
<xsl:if test='$runXSL!=""'>
    <!-- get a list of the LUs that start with 'startsWith' -->
    <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
    <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
    <xsl:for-each select='fn:lu[starts-with(translate(@name, $lcletters, $ucletters), $startsWith)]'>
        <xsl:variable name='name' select='@name' />
        <xsl:variable name='id' select='@ID' />
        <xsl:variable name='frName' select='@frameName' />
        <xsl:variable name='status' select='@status' />
        <xsl:variable name='hasAnno' select='@hasAnnotation' />
        <li><xsl:value-of select='$name' /><xsl:text> </xsl:text>(<a href='frameIndex.xml?frame={$frName}'><xsl:value-of select='$frName' /></a>)<xsl:text> </xsl:text>
        <b><xsl:value-of select='$status' /></b><xsl:text> </xsl:text>
        <xsl:if test='$status!="Problem"'>
            <a href='lu/lu{$id}.xml?mode=lexentry'>Lexical Entry</a>
             <xsl:if test='$hasAnno="true"'>
                <xsl:text> </xsl:text><a href='lu/lu{$id}.xml?mode=annotation'>Annotation</a>
            </xsl:if>
        </xsl:if>
        </li>
    </xsl:for-each>
</xsl:if>

<!-- the main display block -->
<xsl:if test='$runXSL=""'>
<html>
	<head>
      	<title>Lexical Unit Index</title>
		<script type="text/javascript">

        <!-- NORMAL MODE -->
        <xsl:if test='$dynamic="false"'>
            //<![CDATA[
            // Javascript must go in CDATA blocks to get it
            // through the XSL processor

            // PRIMARY ENTRY POINT
            var currentXMLFile = getURLFileName();
			var banner = gup('banner');

            // the xml and xsl must be loaded in 'setup' once the page is loaded
            // so they can be used to display lists of LUs with 'runXSL'
			var xml;
			var xsl;
            var xmlLoader;
            var xslLoader;

            window.onload = setup;
            function setup() {
                // if a banner was specified, display it
                if (banner) {
                    var loc = window.location;
                    var domain = loc.protocol + "//" + loc.host + "/";
                    var banFrame = document.getElementById('banner');
                    banFrame.setAttribute("src",  domain + unescape(banner));
                    banFrame.style.width = '100%';
                    banFrame.scrolling = 'no';
                    banFrame.style.display = 'block';
                    banFrame.style.border = 0;

                    // update frameIndex link with banner
                    document.getElementById('frameIndexLink').href = 'frameIndex.xml?banner=' + banner;
                }
				
				if (window.ActiveXObject) { // Internet Explorer
                    // just load the xml and xsl directly
					xml = CreateXMLFileParser('luIndex.xml');
					xsl = CreateXMLFileParser('luIndex.xsl');					
				} else { // Firefox, Safari, etc.
                    // perform a GET to get the xml
                    // (Safari can't load the xml directly the same
                    // way that IE and Firefox can)
					xmlLoader = CreateAJAXObject();
					xslLoader = CreateAJAXObject();
					xmlLoader.onreadystatechange = xmlReceive;
					xmlLoader.open("GET", "luIndex.xml", true);
					xmlLoader.send(null);
				}
            }

            // called when a user clicks an alphabetical link or performs a search
            function getLUs(option) {
                var compare;
                // get the div to display the list of LUs in
                var luDiv = document.getElementById('lus');
                var lusHTML = '';

                // display a title for the list of LUs being displayed
                if (option == "search") {
                    // get what the user searched for
                    compare = document.getElementById('search').value;
                    lusHTML = '<h1>Search: ' + compare + '</h1>';
                } else {
                    compare = option;
                    if (compare == "")
                        lusHTML = '<h1>All</h1>';
                    else
                        lusHTML = '<h1>' + compare + '</h1>';
                }

                luDiv.innerHTML = lusHTML;

                // going to call the XSL processor with 'runXSL' on
                var parameters = new Object();
                parameters['runXSL'] = 'true';

                // create a list object to store the LUs in
                var luList = document.createElement('ul');
                luList.setAttribute('id', 'lulist');
                luDiv.appendChild(luList);
                if (compare != '#') {
                    parameters['startsWith'] = compare.toUpperCase(); // xsl compares to upper case lu names
                    if (xml && xsl) // make sure xml and xsl were loaded
                        XSLTTransform(xsl, xml, 'lulist', parameters);
                } else { // special case for numbers to get all of them
                    for (var i = 0; i < 10; i++) {
                        parameters['startsWith'] = i + "";
                        if (xml && xsl)
                            XSLTTransform(xsl, xml, 'lulist', parameters);
                    }
                }

                // have to update LU links if a banner was specified
                if (banner) {
                    var luLinks = luDiv.getElementsByTagName('a');
                    for (var i = 0; i < luLinks.length; i++)
                        luLinks[i].href += "&banner=" + banner;
                }
            }

            // borrowed from http://www.mindlence.com/WP/?page_id=224
            // only called for Firefox, Safari, etc.
            function CreateXMLStringParser(XMLString) {
                try { // Firefox, Safari, etc.
                    var xmlParser = new DOMParser();
                    var xmlDoc = xmlParser.parseFromString(XMLString, "text/xml");
                } catch(Err) { // Internet Explorer (shouldn't be reached)
                    try {
                        var xmlDoc= new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async="false";
                        xmlDoc.loadXML(XMLString);
                    } catch(Err) {
                        window.alert("Browser does not support XML parsing.");
                        return false;
                    }
                }

                return xmlDoc;
            }

            // borrowed from http://www.mindlence.com/WP/?page_id=224
            // only called for Internet Explorer
			function CreateXMLFileParser(XMLFile) {
				var xmlDoc;
				try { // Firefox, Safari, etc. (should be skipped)
					xmlDoc = document.implementation.createDocument("", "", null);
					xmlDoc.load(XMLFile);
				} catch(Err) { // Internet Explorer
					try {
						xmlDoc= new ActiveXObject("MSXML2.FreeThreadedDomDocument");
						xmlDoc.async = "false";
						xmlDoc.load(XMLFile);
					} catch(Err) {
						window.alert("Browser does not support XML parsing.");
						return false;
					}
				} 
				return xmlDoc;
			}

            // borrowed from http://www.mindlence.com/WP/?page_id=224
            function CreateAJAXObject() {
                try { // Firefox, Opera, and Safari
                    AJAXObj = new XMLHttpRequest();
                } catch (err) { // Internet Explorer
                    try {
                        AJAXObj = new ActiveXObject("Msxml2.XMLHTTP");
                    } catch (err) {
                        try {
                            AJAXObj = new ActiveXObject("Microsoft.XMLHTTP");
                        } catch (err) {
                            alert("Your browser does not support AJAX!");
                        }
                    }
                }

                return AJAXObj;
            }

            // borrowed from http://www.mindlence.com/WP/?page_id=224
            function xmlReceive() {
                if (xmlLoader.readyState == 4) {
                    xml = CreateXMLStringParser(xmlLoader.responseText);

                    xslLoader.onreadystatechange = xslReceive;
                    xslLoader.open("GET", "luIndex.xsl", true);
                    xslLoader.send(null);
                }
            }

            // borrowed from http://www.mindlence.com/WP/?page_id=224
            function xslReceive() {
                if (xslLoader.readyState == 4)
                    xsl = CreateXMLStringParser(xslLoader.responseText);
            }

            // borrowed from http://www.mindlence.com/WP/?page_id=224
           function XSLTTransform(XSLStyleSheet, XMLData, InsertElementID, Parameters) {
                if (window.ActiveXObject) { // Internet Explorer
                    var XSLTCompiled = new ActiveXObject("MSXML2.XSLTemplate");
                    XSLTCompiled.stylesheet = XSLStyleSheet.documentElement;
                    // create XSL-processor
                    var XSLTProc = XSLTCompiled.createProcessor();
                    XSLTProc.input = XMLData;

                    if (Parameters != "") {//Loop through the parameters and apply each to the XSLT Processor
                        for ($Index in Parameters) {
                            XSLTProc.addParameter($Index, Parameters[$Index]);
                        }
                    }

                    XSLTProc.transform();

                    var outDiv = document.createElement('div');
                    outDiv.innerHTML = XSLTProc.output;

                    document.getElementById(InsertElementID).appendChild(outDiv);
                } else if (document.implementation && document.implementation.createDocument) { // Firefox, Safari, etc.
                    XSLTProc = new XSLTProcessor();
                    XSLTProc.importStylesheet(XSLStyleSheet);

                    if (Parameters != "") {//Loop through the parameters and apply each to the XSLT Processor
                        for ($Index in Parameters) {
                            XSLTProc.setParameter("", $Index, Parameters[$Index]);
                        }
                    }

                    TransformDoc = XSLTProc.transformToFragment(XMLData, document);
                    document.getElementById(InsertElementID).appendChild(TransformDoc);
                } else {
                    window.alert("Browser does not support XSLT.");
                    return false;
                }
            }

            // extract XML file name from URL
            function getURLFileName() {
               var wholeurl = window.location.href;
               var result = wholeurl.replace(/[?].*$/,"");
               return result;
            }

            // get the value of a paramater passed in through the url
            // like in 'luIndex.xml?banner='
            function gup(name) {
                name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
                var regexS = "[\\?&]"+name+"=([^&#]*)";
                var regex = new RegExp( regexS );
                var results = regex.exec( window.location.href );
                if( results == null )
                    return "";
                else
                    return results[1];
            }
            //]]>
        </xsl:if>

        <!-- DYNAMIC MODE -->
        <xsl:if test='$dynamic="true"'>
            //<![CDATA[
            // PRIMARY ENTRY POINT
            // the XML file this code is run on contains only the
            // LUs that the user requested
            // (not a list of all the LUs like in normal mode)
            // so all of the LUs in the file should be displayed onload
            window.onload = displayAllLUs;
            function displayAllLUs() {
            	var luDiv = document.getElementById('lus');
                var lusHTML = '<ul>';
                //]]>
                <xsl:for-each select="fn:lu">
                    var name = "<xsl:value-of select='@name' />";
                    var frameName = "<xsl:value-of select='@frameName' />";
                    var status = "<xsl:value-of select='@status' />";
                    var id = "<xsl:value-of select='@ID' />";
                    var hasAnnotation = "<xsl:value-of select='@hasAnnotation' />";
                    //<![CDATA[
                    lusHTML += "<li>";
                    lusHTML += name + " ";
                    lusHTML += "(<a href='frameIndex.xml?frame=" + frameName + "'>" + frameName + "</a>) ";
                    lusHTML += "<b>" + status + "</b> ";
                    if (status.toLowerCase() != "problem") {
                        lusHTML += "<a href='lu/lu" + id + ".xml?mode=lexentry'>Lexical entry</a> ";
                        if (hasAnnotation == "true")
                            lusHTML += "<a href='lu/lu" + id + ".xml?mode=annotation'>Annotation</a>";
                    }
                    lusHTML += "</li>";
                    //]]>
                </xsl:for-each>
                //<![CDATA[
                lusHTML += '</ul>';
                luDiv.innerHTML = lusHTML;
            }

            // called when user clicks alphabetical link or performs a search
            function getLUs(option) {
                var startsWith;
                if (option == "search")
                    startsWith = document.getElementById('search').value;
                else
                    startsWith = option;

                // request a new luIndex XML file from the Java Servlet
                // containing only the LUs that start with 'startsWith'
                window.location.href = "luIndex.xml?startsWith=" + escape(startsWith);
            }
            //]]>
        </xsl:if>
        //<![CDATA[

        // used by both normal and dynamic mode to allow the user
        // to press <enter> key to perform a search
		function searchKeyPress(e) {
			// look for window.event in case event isn't passed in
			if (window.event) { e = window.event; }
			if (e.keyCode == 13) { // <enter> key
					document.getElementById('searchButton').click();
			}
        }
		//]]>
		</script>
	</head>
	<body>
        <iframe id='banner' style='display:none;'></iframe>
        <div style="float:right;">
            <a id='frameIndexLink' href="frameIndex.xml">Frame Index</a>
        </div>
        <h1>FrameNet Index of Lexical Units</h1><p>This page is an index to alphabetical lists of the names of the lexical units (LUs). </p><p>Each LU name is followed by the part of speech, the name of the relevant frame, and its status.  If a lexical unit has the status "Finished_initial" (meaning it was annotated in FN2) or "FN1_sent" (meaning annotated in FN1), it will be followed by links to the HTML files for the lexical entry and the annotated sentences. Lexical units on which work has not been completed may have only a link for the lexical entry, or no link at all. The lexical entry provdes two tables with information about the LU:Frame Elements and their Syntactic Realizations; and Valence Patterns.</p><p>
        <input type='text' id='search' onkeypress='searchKeyPress(event);' /><button type='reset' id='searchButton' onClick='getLUs("search")'>Search</button><br />
        | <a href='javascript:getLUs("#")'>#</a>
        | <a href='javascript:getLUs("A")'>A</a>
        | <a href='javascript:getLUs("B")'>B</a>
        | <a href='javascript:getLUs("C")'>C</a>
        | <a href='javascript:getLUs("D")'>D</a>
        | <a href='javascript:getLUs("E")'>E</a>
        | <a href='javascript:getLUs("F")'>F</a>
        | <a href='javascript:getLUs("G")'>G</a>
        | <a href='javascript:getLUs("H")'>H</a>
        | <a href='javascript:getLUs("I")'>I</a>
        | <a href='javascript:getLUs("J")'>J</a>
        | <a href='javascript:getLUs("K")'>K</a>
        | <a href='javascript:getLUs("L")'>L</a>
        | <a href='javascript:getLUs("M")'>M</a>
        | <a href='javascript:getLUs("N")'>N</a>
        | <a href='javascript:getLUs("O")'>O</a>
        | <a href='javascript:getLUs("P")'>P</a>
        | <a href='javascript:getLUs("Q")'>Q</a>
        | <a href='javascript:getLUs("R")'>R</a>
        | <a href='javascript:getLUs("S")'>S</a>
        | <a href='javascript:getLUs("T")'>T</a>
        | <a href='javascript:getLUs("U")'>U</a>
        | <a href='javascript:getLUs("V")'>V</a>
        | <a href='javascript:getLUs("W")'>W</a>
        | <a href='javascript:getLUs("X")'>X</a>
        | <a href='javascript:getLUs("Y")'>Y</a>
        | <a href='javascript:getLUs("Z")'>Z</a>
        | <a href='javascript:getLUs("")'>All</a> |</p>
	<div id="lus"></div>
	</body>
</html>
</xsl:if>
</xsl:template>
</xsl:stylesheet>
