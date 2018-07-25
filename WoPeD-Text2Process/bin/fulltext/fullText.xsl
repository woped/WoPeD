<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://framenet.icsi.berkeley.edu">
<xsl:output method="html" />
<!-- This XSL file transforms fullText XML into FullText Reports.
     First the browser executes the default XSL block ($mode='') to generate a
     HTML/Javascript file with three modes: the main mode (mode=''), which sets
     up the HTML frameset and frames; the top frame mode (mode='document'), which
     displays the Document text with some annotation and links to sentences; and
     the bottom frame sentence mode (mode='sentence'), which displays annotated
     sentences.
     The document mode and sentence mode call the XSLT processor on the
     'document', 'createdBy', and 'sentence' XSL blocks and evaluate
     the resulting Javascript. -->
<xsl:param name='mode'></xsl:param>
<xsl:param name='sentId'></xsl:param>
<xsl:template match="/fn:fullTextAnnotation">
<xsl:if test="$mode=''">
	<html>
		<head>
			<title><xsl:value-of select='fn:header/fn:corpus/@name'/> - <xsl:value-of select='fn:header/fn:corpus/fn:document/@description'/></title>
			<style>
                <!-- The following spans are used for coloring and marking
                     sentences in the sentence frame. -->
				span.Target {
					color: #FFFFFF;
					background-color: #000000;
				}
				span.italic {
					font-style: italic;
				}
				span.Gov {
					font-weight: bold;
				}
				span.X {
					text-decoration: underline;
				}
				span.invisible {
					color: #FFFFFF;
					background-color: #FFFFFF;
				}
                span.NER {
                    color: #000000;
                    background-color: #FFFF00;
                }
                span.Mention {
                    color: #000000;
                    background-color: #FF69B4;
                }
			</style>
			<script type="text/javascript">
                //<![CDATA[
				// GLOBAL VARIABLES
				var currentXMLFile = getURLFileName();
                //]]>
                var corpName = "<xsl:value-of select='fn:header/fn:corpus/@name' />";
                var docName = "<xsl:value-of select='fn:header/fn:corpus/fn:document/@description' />";
                <xsl:variable name="xslLoc" select="/processing-instruction('xml-stylesheet')" />
                var xslLoc = <xsl:value-of select="substring-after($xslLoc, 'href=')" />;
                //<![CDATA[
				var mode = gup('mode');
                var banner = gup('banner');

                // PRIMARY ENTRY POINT

                /* MAIN MODE */
				if (mode == "") {
                    // create the frameset and two frames and set their src as
                    // the currentXMLFile but with the
                    // mode set as 'document' for the top frame
                    // and 'sentence' for the bottom frame
					var frameset = document.createElement("frameset");
					frameset.setAttribute("rows","60%,*");
					var mFrame = document.createElement("frame");
					mFrame.setAttribute("src",currentXMLFile+"?mode=document&banner=" + banner);
					mFrame.setAttribute("name","top");
					frameset.appendChild(mFrame);
					var sFrame = document.createElement("frame");
					sFrame.setAttribute("src",currentXMLFile+"?mode=sentence");
					sFrame.setAttribute("name","bottom");
					frameset.appendChild(sFrame);
					document.documentElement.appendChild(frameset);
				}

                /* TOP FRAME DOCUMENT MODE */
                else if (mode == "document") {
                    // track the sentences added to the bottom frame in an array
					var sentIDs = new Array();

                    // create a body for this frame
					var docBody = document.createElement("body");
					document.documentElement.appendChild(docBody);

                    // if a banner was specified, display it
                    if (banner) {
                        // create an iFrame and load the banner in it
                        var loc = window.location;
                        var domain = loc.protocol + "//" + loc.host + "/";
                        var banFrame = document.createElement("iframe");
                        banFrame.setAttribute("src",  domain + unescape(banner));
                        banFrame.style.width = '100%';
                        banFrame.scrolling = 'no';
                        banFrame.style.border = 0;
                        docBody.appendChild(banFrame);
                    }

                    var linkDiv = navBar();
                    var bodyTitle = document.createElement("h1");

                    bodyTitle.innerHTML = corpName + " - " + docName;
                    document.body.appendChild(linkDiv);
                    document.body.appendChild(bodyTitle);

                    // put the document text in a div with id 'main'
					var mainDiv = document.createElement("div");
					mainDiv.setAttribute("id","main");
					document.body.appendChild(mainDiv);

                    var createdByDiv = document.createElement("div");
                    createdByDiv.setAttribute("id", "cby");
                    document.body.appendChild(createdByDiv);

                    // load the xml and xsl in the top frame
                    var xml, xsl;
                    var xmlLoader, xslLoader;
                    if (window.ActiveXObject) { // Internet Explorer
                        // just load the xml and xsl directly
                        xml = CreateXMLFileParser(currentXMLFile);
                        xsl = CreateXMLFileParser(xslLoc);
                        setTimeout("showDocumentText()", 1);
                        showLabelsCreatedBy();
                    } else { // Firefox, Safari, etc.
                        // perform a GET to get the xml
                        // (Safari can't load the xml directly the same
                        // way that IE and Firefox can)
                        xmlLoader = CreateAJAXObject();
                        xslLoader = CreateAJAXObject();
                        xmlLoader.onreadystatechange = xmlReceive;
                        xmlLoader.open("GET", currentXMLFile, true);
                        xmlLoader.send(null);
                    }

                    mainDiv.innerHTML = "Loading document...";

					// entry point for adding a sentence to the bottom pane
                    // when user clicks a target link for a sentence
					function addSent(){
						for (var i=0; i < addSent.arguments.length; i++ ) {
							var sent_id = addSent.arguments[i];
							if (addSentID(sent_id)) {
                                // pass the sentID to the getSentenceAnnotation function
                                // in the bottom sentence frame
								top.frames[1].getSentenceAnnotation(sent_id);
							}
						}
					}

                    // add a sentID to the array sentIDs
                    function addSentID(sent_id) {
						if (hasSentID(sent_id) < 0) {
							sentIDs.push(sent_id);
							return true;
						}
						else
							return false;
					}

                    // check if the sentID has already been added to the
                    // bottom sentence frame (is stored in sentIDs)
					function hasSentID(sent_id){
						for (var i = 0; i < sentIDs.length; i++)
							if (sentIDs[i] == sent_id)
								return i;
							return -1;
					}

					// remove a sentID from the array sentIDs
					function removeSentID(sent_id) {
						var pos = hasSentID(sent_id);
						if (pos >= 0) {
							if (pos < sentIDs.length - 1) {
								sentIDs[pos] = sentIDs.pop();
							} else {
								sentIDs.pop();
							}
							return true;
						}
						else {
							// Shouldn't get here
							return false;
						}
					}
				}

                /* BOTTOM FRAME SENTENCE MODE */
                else if (mode == "sentence") {
                    // turn colors on
					var colors = 1;

                    // create divs for the colored and marked up sentences
					var sentDivC_On = document.createElement('div'); // colors
					var sentDivC_Off = document.createElement('div'); // no colors

					// generate the body of the document
					var docBody = document.createElement("body");
					document.documentElement.appendChild(docBody);

                    // display toggle colors link and clear sentences link
					var buttonDiv = document.createElement("div");
					buttonDiv.setAttribute("id","buttons");
                    // blur part is to get rid of a dotted-line box around the link
					buttonDiv.innerHTML += "<a onFocus='if(this.blur)this.blur();'" +
                        "href='javascript:clearSents()'>Clear Sentences</a>";
					buttonDiv.innerHTML += "<a onFocus='if(this.blur)this.blur();' style='padding:15px;' id='colorLink'" +
						"href='javascript:toggleColor()'>Turn Colors Off</a><br /><br />";
                    document.body.appendChild(buttonDiv);

                    // create a div for the display of sentences
					var sentsDiv = document.createElement("div");
					sentsDiv.setAttribute("id","sents");
					sentsDiv.appendChild(sentDivC_On);
					sentsDiv.appendChild(sentDivC_Off);
					document.body.appendChild(sentsDiv);

					function toggleColor() {
						colors = 1 - colors;
						showSents();
					}

					function showSents() {
						if (colors == 1) {
							document.getElementById('colorLink').innerHTML = "Turn Colors Off";
							sentDivC_On.style.display = "block"; // display color div
							sentDivC_Off.style.display = "none"; // hide no color div
						} else {
							document.getElementById('colorLink').innerHTML = "Turn Colors On";
							sentDivC_On.style.display = "none"; // hide color div
							sentDivC_Off.style.display = "block"; // display no color div
						}
					}

                    // used by the clear sentences link
					function clearSents() {
                        // delete html in sentence divs
						sentDivC_On.innerHTML = "";
						sentDivC_Off.innerHTML = "";
                        // reset the sentIDs array in the top frame
						top.frames[0].sentIDs = Array();
					}

                    // used by the [X] links to the left of sentences
					function removeSent(sent_id) {
                        // get the child of the sentence divs where the sentence
                        // with the given sentID is stored
						var nodeC_On = getChild(sentDivC_On, 'sent' + sent_id + 'C_On');
						var nodeC_Off = getChild(sentDivC_Off, 'sent' + sent_id + 'C_Off');

                        // if the children were found, remove them
						if (nodeC_On && nodeC_Off) {
							sentDivC_On.removeChild(nodeC_On);
							sentDivC_Off.removeChild(nodeC_Off);
                            // also update the sentIDs array in the top frame
							top.frames[0].removeSentID(sent_id);
						}
					}

                    // find the child of an object by id
					function getChild(object, id){
						for (var i = 0; object.childNodes.item(i); i++){
							if (object.childNodes.item(i).id == id){
								return object.childNodes.item(i);
							}
						}
						return null;
					}

                    // get the sentence annotation in the form of a colored sentence
                    // and a marked up sentence for a given sentID
                    function getSentenceAnnotation(sentId) {
                        var parameters = new Object();
                        parameters['mode'] = 'sentence';
                        parameters['sentId'] = sentId;

                        var newDivC_On = document.createElement('div');
                        newDivC_On.setAttribute('id', 'sent' + sentId + 'C_On');
                        var newDivC_Off = document.createElement('div');
                        newDivC_Off.setAttribute('id', 'sent' + sentId + 'C_Off');

                        // add the divs for this sentence to their respective
                        // parent sentence divs
                        sentDivC_On.appendChild(newDivC_On);
                        sentDivC_Off.appendChild(newDivC_Off);

                        XSLTTransform(top.frames[0].xsl, top.frames[0].xml, parameters);
                    }
				}

                /* GENERAL FUNCTIONS */

				// extract XML file name from URL
				function getURLFileName() {
				   var wholeurl = window.location.href;
				   var result = wholeurl.replace(/[?].*$/,"");
				   return result;
				}

                // get the value of a paramater passed in through the url
                // like in '...__work.xml?mode=sentence&banner='
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

                // create the navigation links at the top right of the report
                function navBar() {
					var linkDiv = document.createElement("div");
                    // float the link div to the right
					linkDiv.style.cssFloat = "right"; // Firefox, Safari
                    linkDiv.style.styleFloat = "right"; // IE

                    // create luIndex link
                    var luIndexLink = document.createElement("a");
                    luIndexLink.setAttribute("href","../luIndex.xml?banner=" + banner);
                    luIndexLink.setAttribute("target","_top");
                    luIndexLink.innerHTML = "Lexical Unit Index";

                    // create frameIndex link
                    var frameIndexLink = document.createElement("a");
                    frameIndexLink.setAttribute("href","../frameIndex.xml?banner=" + banner);
                    frameIndexLink.setAttribute("target","_top");
                    frameIndexLink.innerHTML = "Frame Index";

                    linkDiv.appendChild(luIndexLink);
					linkDiv.innerHTML += "<font style='padding:5px;' />";
					linkDiv.appendChild(frameIndexLink);
					return linkDiv;
				}

                // get the text of the document with some markup and links to sentences
                // for the top frame
				function showDocumentText() {
                    var parameters = new Object();
                    parameters['mode'] = 'document';
                    XSLTTransform(xsl, xml, parameters);
                }

                // get the list of annotators for this document's labels
                function showLabelsCreatedBy() {
                    var parameters = new Object();
                    parameters['mode'] = 'createdby';
                    XSLTTransform(xsl, xml, parameters);
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
                        xslLoader.open("GET", xslLoc, true);
                        xslLoader.send(null);
                    }
                }

                // borrowed from http://www.mindlence.com/WP/?page_id=224
                function xslReceive() {
                    if (xslLoader.readyState == 4) {
                        xsl = CreateXMLStringParser(xslLoader.responseText);
                        showDocumentText();
                        showLabelsCreatedBy()
                    }
                }

                // borrowed from http://www.mindlence.com/WP/?page_id=224
               function XSLTTransform(XSLStyleSheet, XMLData, Parameters) {
                    var output;
                    if (window.ActiveXObject) { // Internet Explorer
                        if (Parameters != "") {//Loop through the parameters and apply each to the XSLT Processor
                            for ($Index in Parameters) {
                                var paramNode = XSLStyleSheet.selectSingleNode("//xsl:param[@name='" + $Index + "']");
                                paramNode.text = Parameters[$Index];
                            }
                        }

                        output = XMLData.transformNode(XSLStyleSheet);
                    } else if (document.implementation && document.implementation.createDocument) { // Firefox, Safari, etc.
                        XSLTProc = new XSLTProcessor();
                        XSLTProc.importStylesheet(XSLStyleSheet);

                        if (Parameters != "") {//Loop through the parameters and apply each to the XSLT Processor
                            for ($Index in Parameters) {
                                XSLTProc.setParameter("", $Index, Parameters[$Index]);
                            }
                        }

                        var TransformDoc = XSLTProc.transformToFragment(XMLData, document);
                        output = TransformDoc.firstChild.wholeText;
                    } else {
                        window.alert("Browser does not support XSLT.");
                        return false;
                    }
                    output = output.replace(/&lt;/g, "<");
                    output = output.replace(/&gt;/g, ">");
                    eval(output);
                }

                Array.prototype.contains = function ( needle ) {
                   for (i in this) {
                       if (this[i] == needle) return true;
                   }
                   return false;
                }

                // insert a value for a key into a javascript object
				function insertMapValue(map, key, value, toTheEnd) {
					if (!map[key])
						map[key] = value;
					else if (toTheEnd) // insert at end
						map[key] += value;
					else // insert at beginning
						map[key] = value + map[key];
				}

                // go through the sentence text and insert the labels (span classes)
				function applyLabelsToSent(charLabelMap, sent, sentOrig, noColor) {
					// noColor is 0 or 1
					var inTarget; // tracks whether in target tag
					for (var i = 0; i < sentOrig.length + 1; i++) {
						// get labels for this char at i
						var charLabels = [charLabelMap[0][noColor]["" + i],
										  charLabelMap[1][noColor]["" + i],
										  charLabelMap[2][noColor]["" + i]]
						// rank 1
						if (charLabels[0]) {
							var targetIndex = charLabels[0].indexOf("<TARGET>");
							if (targetIndex != -1) {
								// found target tag
								inTarget = !inTarget;
								// remove special target tag
								charLabels[0] = charLabels[0].substring(0, targetIndex) +
									charLabels[0].substring(targetIndex + 8, charLabels[0].length);
							}
							sent[0][noColor] += charLabels[0];
						}

						// close and open invisible span for ranks 2 and 3
						for (var r = 1; r < 3; r++) {
							if (charLabels[r]) {
								if (charLabels[r] == "</span>" || charLabels[r] == "]")
									sent[r][noColor] += charLabels[r] + "<span class='invisible'>";
								else
									sent[r][noColor] += "</span>" + charLabels[r];
							}
						}

                        // have to check 1 past the length but don't add characters past the length
                        if (i < sentOrig.length) {
                            var sentChar = sentOrig.charAt(i);
                            if (inTarget)
                                sentChar = sentChar.toUpperCase();
                            sent[0][noColor] += sentChar;
                            sent[1][noColor] += sentChar;
                            sent[2][noColor] += sentChar;
                        }
					}
					if (charLabelMap[0][noColor]["itype"])
						sent[0][noColor] += charLabelMap[0][noColor]["itype"];
					return sent;
				}
                //]]>
            </script>
        </head>
    </html>
</xsl:if>
<xsl:if test="$mode='document'">
    //<![CDATA[
    var mainContent, sentOrig, sent, curParag, parag;
    mainContent = "";
    parag = 0;
    //]]>
    <xsl:for-each select="fn:sentence">
        <!-- have to escape all quotes in the sent first, using template at bottom -->
        <xsl:variable name="processSent">
            <xsl:call-template name="cleanQuote">
            <xsl:with-param name="string">
                <xsl:value-of select='normalize-space(fn:text)' />
            </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        sentOrig = "<xsl:value-of select='$processSent' />";
        curParag = "<xsl:value-of select='@paragNo' />";
        //<![CDATA[
        if (parag != curParag) {
            if (parag != 0) {
                mainContent += "</p>";
            }
            parag = curParag;
            mainContent += "<p>" + parag + ". ";
        }
        sent = "";
        var charLabelMap = new Object();
        var start, end, id;
        //]]>
        <!-- get the NER, WSL, and Target labels -->
        <xsl:for-each select="fn:annotationSet/fn:layer[@name='NER'or@name='WSL'or@name='Target']/fn:label">
            <xsl:sort select="../@name" order="ascending" />
            start = "<xsl:value-of select='@start' />";
            end = <xsl:value-of select='@end' /> + 1 + ""; <!-- have to add 1 the way char values work out -->
            id = "<xsl:value-of select='../../@ID'/>";
            <xsl:if test="../@name='NER'">
                //<![CDATA[
                insertMapValue(charLabelMap, start, "<span class='NER'>", true);
                insertMapValue(charLabelMap, end, "</span>", true);
                //]]>
            </xsl:if>
            <xsl:if test="../@name='WSL' and (@name='NT' or @name='Nonrelational')">
                //<![CDATA[
                insertMapValue(charLabelMap, start, "<i>", true);
                insertMapValue(charLabelMap, end, "</i>", true);
                //]]>
            </xsl:if>
            <xsl:if test="../@name='WSL' and @name='Mention'">
                //<![CDATA[
                insertMapValue(charLabelMap, start, "<span class='Mention'>", true);
                insertMapValue(charLabelMap, end, "</span>", true);
                //]]>
            </xsl:if>
            <xsl:if test="../@name='Target' and @name='Target' and ../../@frameName">
                frame = "<xsl:value-of select='../../@frameName' />";
                <xsl:if test="../../@status = 'MANUAL'">
                    //<![CDATA[
                    insertMapValue(charLabelMap, start, "<TARGET><a href='javascript:addSent(" + id + ")'>", true);
                    insertMapValue(charLabelMap, end, "<TARGET></a><sub>" + frame + "</sub>", true);
                    //]]>
                </xsl:if>
                <xsl:if test="../../@status = 'UNANN'">
                    //<![CDATA[
                    insertMapValue(charLabelMap, start, "<TARGET>", true);
                    insertMapValue(charLabelMap, end, "<TARGET><sub>" + frame + "</sub>", true);
                    //]]>
                </xsl:if>
            </xsl:if>
        </xsl:for-each>
        //<![CDATA[
        // apply the labels to the sentence
        var inTarget = false;
        for (var i = 0; i < sentOrig.length + 1; i++) {
            // get label for this char at i
            var charLabel = charLabelMap["" + i];
            if (charLabel) {
                var targetIndex = charLabel.indexOf("<TARGET>");
                if (targetIndex != -1) {
                    // found target tag
                    inTarget = !inTarget;
                    // remove special target tag
                    charLabel = charLabel.substring(0, targetIndex) +
                        charLabel.substring(targetIndex + 8, charLabel.length);
                }
                sent += charLabel;
            }

            // have to check 1 past the length but don't add characters past the length
            if (i < sentOrig.length) {
                var sentChar = sentOrig.charAt(i);
                if (inTarget)
                    sentChar = sentChar.toUpperCase();
                sent += sentChar;
            }
        }
        mainContent += sent + " ";
        //]]>
    </xsl:for-each>
    //<![CDATA[
    document.getElementById('main').innerHTML = mainContent;
    //]]>
</xsl:if>
<xsl:if test="$mode='createdby'">
    //<![CDATA[
    var cBys = new Array();
    var cBy = '';
    //]]>
    <xsl:for-each select="fn:sentence/fn:annotationSet/fn:layer[@name='FE'or@name='Target']/fn:label/@cBy">
        <xsl:sort select='.' order='ascending' />
        cBy = "<xsl:value-of select='.' />";
        //<![CDATA[
        if (!cBys.contains(cBy))
            cBys.push(cBy);
        //]]>
    </xsl:for-each>
    //<![CDATA[
    document.getElementById('cby').innerHTML += '<b>Annotator ID(s): </b>' + cBys.join(', ');
    //]]>
</xsl:if>
<xsl:if test="$mode='sentence'">
    //<![CDATA[
    var sent;
    var sentOrig;
    var charLabelMap;
    var rank2, rank3;
    var sc_name;
    // [colorSent, noColorSent]
    var finalStr = ["", ""];
    //]]>
    var sentId = "<xsl:value-of select='$sentId' />";
    var frameName = "<xsl:value-of select='@frame'/>";
    <xsl:for-each select="//fn:sentence/fn:annotationSet[@ID=$sentId]">
        var cursentId = "<xsl:value-of select='@ID' />";
        //<![CDATA[
        // have to escape all quotes in the sent[0] first, using template at bottom
        //]]>
        <xsl:variable name="processSent">
            <xsl:call-template name="cleanQuote">
            <xsl:with-param name="string">
                <xsl:value-of select='normalize-space(../fn:text)' />
            </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        sentOrig = "<xsl:value-of select='$processSent' />";
        //<![CDATA[
        // [colorSent, noColorSent] for each rank 1-3
        sent = [["", ""], ["", ""], ["", ""]];
        // simulate 6 maps (for each rank 1-3 and for color/noColor)
        // with simple javascript object properties
        charLabelMap = [[new Object(), new Object()],
            [new Object(), new Object()], [new Object(), new Object()]];
        rank2 = false; // assume no rank 2 at first
        rank3 = false; // assume no rank 3 at first
        //]]>

        <!-- get labels and insert into charLabelMap -->
        <xsl:for-each select="fn:layer[@name='FE'or@name='Target'or@name='Noun'or@name='Adj'or@name='Verb']/fn:label">
            <xsl:sort select="../@name" order="ascending" />
            <!-- ^ make sure Target layer always comes last so it can be overridden by an FE -->
            labelName = "<xsl:value-of select='@name' />";
            fgColor = "<xsl:value-of select='@fgColor' />";
            bgColor = "<xsl:value-of select='@bgColor' />";
            rank = <xsl:value-of select='../@rank' />;
            //<![CDATA[
            if (rank == 2)
                rank2 = true;
            else if (rank == 3)
                rank3 = true;
            else if (rank > 3) {
                // shouldn't happen: no case yet, but script could be edited to accomodate
                sentOrig += " ERROR: LABEL " + labelName + " HAS RANK > 3. THIS IS NOT " +
                    "HANDLED YET (NO SUCH CASE WHEN SCRIPT WAS WRITTEN BUT COULD BE ADDED)";
            }

            // get the label data and store it in charLabelMap
            //]]>
            <xsl:if test="@start">
                start = "<xsl:value-of select='@start' />";
                <!-- add 1 (just the way char values work out later) and make it a string -->
                end = <xsl:value-of select='@end' /> + 1 + "";
                <xsl:if test="../@name = 'Target'">
                    //<![CDATA[
                    // color
                    insertMapValue(charLabelMap[0][0], start, "<TARGET><span class='Target'>", false);
                    insertMapValue(charLabelMap[0][0], end, "<TARGET></span>", true);
                    // no color
                    insertMapValue(charLabelMap[0][1], start, "<TARGET><span class='italic'>", true);
                    insertMapValue(charLabelMap[0][1], end, "<TARGET></span><sup>Target</sup>", false);
                    //]]>
                </xsl:if>
                <xsl:if test="../@name = 'FE'">
                    //<![CDATA[
                    if (rank < 4) { // should always be true
                        // color
                        insertMapValue(charLabelMap[rank-1][0], start,
                            "<span style='color:#" + fgColor + ";background-color:#" + bgColor + ";'>", false);
                        insertMapValue(charLabelMap[rank-1][0], end, "</span>", true);

                        // no color
                        insertMapValue(charLabelMap[rank-1][1], start,
                            "[<sub>" + labelName + "</sub>", true);
                        insertMapValue(charLabelMap[rank-1][1], end, "]", false);
                    }
                    //]]>
                </xsl:if>
                <xsl:if test="../@name = 'Noun'">
                    //<![CDATA[
                    // color
                    if (labelName == "Gov")
                        insertMapValue(charLabelMap[0][0], start, "<span class='Gov'>", false);
                    else if (labelName == "X")
                        insertMapValue(charLabelMap[0][0], start, "<span class='X'>", false);
                    insertMapValue(charLabelMap[0][0], end, "</span>", true);

                    // no color
                    if (labelName == "Gov") {
                        insertMapValue(charLabelMap[0][1], start, "[", false);
                        insertMapValue(charLabelMap[0][1], end, "]<sup>Gov</sup>", true);
                    }
                    else if (labelName == "X") {
                        insertMapValue(charLabelMap[0][1], start, "{", false);
                        insertMapValue(charLabelMap[0][1], end, "}<sup>X</sup>", true);
                    }
                    //]]>
                </xsl:if>
                <xsl:if test="@name = 'Supp' and (../@name = 'Noun' or ../@name='Verb' or ../@name='Adj')">
                    //<![CDATA[
                    // color
                    insertMapValue(charLabelMap[0][0], start, "<span class='italic'>", false);
                    insertMapValue(charLabelMap[0][0], end, "</span>", true);

                    // no color
                    insertMapValue(charLabelMap[0][1], start, "[", false);
                    insertMapValue(charLabelMap[0][1], end, "]<sup>Supp</sup>", true);
                    //]]>
                </xsl:if>
                <xsl:if test="@name = 'Ctrlr' and (../@name = 'Noun' or ../@name='Verb' or ../@name='Adj')">
                    //<![CDATA[
                    // color
                    insertMapValue(charLabelMap[0][0], start, "<span class='italic'>", false);
                    insertMapValue(charLabelMap[0][0], end, "</span>", true);

                    // no color
                    insertMapValue(charLabelMap[0][1], start, "[", false);
                    insertMapValue(charLabelMap[0][1], end, "]<sup>Ctrlr</sup>", true);
                    //]]>
                </xsl:if>
            </xsl:if>
            <xsl:if test="not(@start)">
                itype = "<xsl:value-of select='@itype' />";
                //<![CDATA[
                // color
                insertMapValue(charLabelMap[0][0], "itype",
                    "<span style='color:#" + fgColor + ";background-color:#" + bgColor + ";'>", true);
                insertMapValue(charLabelMap[0][0], "itype", itype + "</span>", true);

                // no color
                insertMapValue(charLabelMap[0][1], "itype",
                    "[<sub>" + labelName + "</sub>" + itype + "]", true);
                //]]>
            </xsl:if>
        </xsl:for-each>

        //<![CDATA[
        // apply color labels in charLabelMap to sent
        sent = applyLabelsToSent(charLabelMap, sent, sentOrig, 0);

        // apply non-color labels in charLabelMap to sent
        sent = applyLabelsToSent(charLabelMap, sent, sentOrig, 1);

        // construct finalStr from sent
        // color
        finalStr[0] += sent[0][0];
        var invisSpan = "<br /><span class='invisible'>"
        if (rank2)
            finalStr[0] += invisSpan + "[X] " + sent[1][0] + "</span>";
        if (rank3)
            finalStr[0] += invisSpan + "[X] " + sent[2][0] + "</span>";

        // no color
        finalStr[1] += sent[0][1];
        if (rank2)
            finalStr[1] += invisSpan + "[X] " + sent[1][1] + "</span>";
        if (rank3)
            finalStr[1] += invisSpan + "[X] " + sent[2][1] + "</span>";
        //]]>
    </xsl:for-each>
    //<![CDATA[
    colorSent = finalStr[0];
    noColorSent = finalStr[1];

    // before the sentence, insert the [X] link
    pretext = "[<a href='javascript:removeSent(" + sentId + ")'>X</a>] ";

    // load the constructed text into two divs, one with color
    // and one with just mark up
    document.getElementById('sent' + sentId + 'C_On').innerHTML += pretext + colorSent;
    document.getElementById('sent' + sentId + 'C_Off').innerHTML += pretext + noColorSent;

    showSents();
    //]]>
</xsl:if>
</xsl:template>

<!-- borrowed code for escaping quotes in strings -->
<xsl:template name="cleanQuote">
	<xsl:param name="string" />
	<xsl:if test="contains($string, '&#x22;')">
		<xsl:value-of select="substring-before($string, '&#x22;')" />\"<xsl:call-template name="cleanQuote">
                <xsl:with-param name="string">
					<xsl:value-of select="substring-after($string, '&#x22;')" />
                </xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	<xsl:if test="not(contains($string, '&#x22;'))">
		<xsl:value-of select="$string" />
	</xsl:if>
</xsl:template>
</xsl:stylesheet>
