<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://framenet.icsi.berkeley.edu">
<xsl:output method="html" />
<!-- This XSL file transforms lexUnit XML into either an Annotation Report or
     a Lexical Entry Report based on the mode specified in the url
     (i.e. '?mode=lexentry'). First the browser executes the XSL to generate
     a large HTML/Javascript file populated with XML data, then the Javascript
     is executed to create HTML.
     For the Lexical Entry Report, there are three modes: the main mode
     (mode='lexentry'), which sets up the HTML frameset and frames; the top
     frame mode (mode='lexentrytop'), which displays the LU info and links to
     sentences; and the bottom frame sentence mode (mode='sentence'), which
     displays annotated sentences;
     For the Desktop Report System, the mode is passed
     as an xsl:parm; the xsl:param 'internalMode' is set to 'Desktop'; and
     any annotationSets to be displayed by the Lexical Entry Report are passed
     through xsl:param 'annotationSets'.
     Some of the code is very similar to or the same as the code in fullText.xsl.
     A different approach is used here though, in which all of the XSL is processed
     at once (creating a large HTML/Javascript file), whereas the fulltext
     processes the XSL in blocks to avoid making too large of a HTML/Javascript
     file and to speed up the report's loading. The lexUnit XSL doesn't use this
     approach because it slows down the opening of multiple sentences at a time
     and it wasn't compatible with the desktop software, where LU reports are
     displayed but not fulltext reports. -->
<xsl:param name='mode'>specifiedInUrl</xsl:param>
<xsl:param name='annotationSets'></xsl:param>
<xsl:param name='internalMode'></xsl:param>
<xsl:template match="/fn:lexUnit">
	<html>
		<head>
			<title><xsl:value-of select='@frame'/>.<xsl:value-of select='@name'/></title>
			<style type="text/css">
				table {
					border-width: medium medium medium medium;
					border-spacing: 2px;
					border-style: outset outset outset outset;
					border-color: gray gray gray gray;
					border-collapse: separate;
					background-color: white;
				}
				table th {
					border-width: 1px 1px 1px 1px;
					padding: 1px 1px 1px 1px;
					border-style: inset inset inset inset;
					border-color: gray gray gray gray;
					background-color: white;
					-moz-border-radius: 0px 0px 0px 0px;
				}
				table td {
					border-width: 1px 1px 1px 1px;
					padding: 1px 1px 1px 5px;
					border-style: inset inset inset inset;
					border-color: gray gray gray gray;
					background-color: white;
					-moz-border-radius: 0px 0px 0px 0px;
				}
				table.fes td.feName {
					width: 300px;
				}
				table.fes td.feType {
					width: 180px;
				}

                <!-- The following spans are used for coloring and marking
                     sentences in the Annotation Reports and sentence frame. -->
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
				<xsl:for-each select="fn:header/fn:frame/fn:FE">
					span.<xsl:value-of select='../../../@frame'/>-<xsl:value-of select='@name'/> {
						color: #<xsl:value-of select='@fgColor'/>;
						background-color: #<xsl:value-of select='@bgColor'/>;
					}
					span.<xsl:value-of select='../../../@frame'/>-<xsl:value-of select='@abbrev'/> {
						color: #<xsl:value-of select='@fgColor'/>;
						background-color: #<xsl:value-of select='@bgColor'/>;
					}
				</xsl:for-each>
			</style>
			<script type="text/javascript">
				//<![CDATA[
                // Javascript must go in CDATA blocks to get it
                // through the XSL processor

				// GLOBAL VARIABLES
				var currentXMLFile = getURLFileName();
				//]]>
				var frameName = "<xsl:value-of select='@frame'/>";

                <!-- PRIMARY ENTRY POINT -->
                <xsl:if test="$mode='specifiedInUrl'">
                    //<![CDATA[
                    var mode = gup('mode');
                    // location of the banner for the public website
                    // is passed in as a parameter through the url
                    var banner = gup('banner');
                    //]]>
                </xsl:if>
                <xsl:if test="$mode!='specifiedInUrl'">
                    <!-- For Desktop Report System -->
                    var mode = "<xsl:value-of select='$mode' />";
                    var banner = '';
                </xsl:if>
                //<![CDATA[
                /* ANNOTATION MODE */
				// if mode isn't specified, display Annotation Report
				if (mode == "" || mode == "annotation") {
                    // create the HTML body
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
                    //]]>
                    <!-- don't display navigation links for desktop reports -->
                    <xsl:if test="$internalMode!='desktop'">
                        //<![CDATA[
                        // create navigation links at top right of report
                        var linkDiv = navBar();
                        document.body.appendChild(linkDiv);
                        //]]>
                    </xsl:if>
                    //<![CDATA[
                    // create the headings for the Annotation Report
                    var bodyHeading = document.createElement("h1");
                    bodyHeading.innerHTML = "Annotation";
                    var bodyTitle = document.createElement("h1");
					//]]>
					bodyTitle.innerHTML = "<xsl:value-of select='@name'/>";
					//<![CDATA[
                    document.body.appendChild(bodyHeading);
					document.body.appendChild(bodyTitle);

                    // create the FE Table
					var feTableDiv = document.createElement("div");
					feTableDiv.setAttribute("id","feTable");
					var feLegend = document.createElement("div");
					feLegend.setAttribute("class","fes");
					var rowStr;
					var tableContent = "<tr><th>Frame Element</th><th>Core Type</th></tr>";
					//]]>
					<xsl:for-each select="fn:header/fn:frame/fn:FE">
						//<![CDATA[
						rowStr = "<tr>";
						rowStr += "<td class='feName'><span class='";
						//]]>
						rowStr += "<xsl:value-of select='../../../@frame'/>-<xsl:value-of select='@name'/>";
						//<![CDATA[
						rowStr += "'>";
						//]]>
						rowStr += "<xsl:value-of select='@name'/>";
						//<![CDATA[
						rowStr += "</span></td><td class='feType'>";
						//]]>
						rowStr += "<xsl:value-of select='@type'/>";
						//<![CDATA[
						rowStr += "</td></tr>";
						tableContent += rowStr;
						//]]>
					</xsl:for-each>
					//<![CDATA[
					feLegend.innerHTML = "<table>" + tableContent + "</table>";
					feTableDiv.appendChild(feLegend);
					document.body.appendChild(feTableDiv);

                    // turn colors on and set up the toggle colors link
                    var colors = 1;
                    //]]>
                    <!-- don't display toggle colors link for desktop reports
                         because it can't be handled by the html/javascript api used -->
                    <xsl:if test="$internalMode!='desktop'">
                        //<![CDATA[
                        var buttonDiv = document.createElement("div");
                        buttonDiv.setAttribute("id","buttons");
                        // 'blur' code is to get rid of a dotted-line box around the link
                        buttonDiv.innerHTML = "<br /><a onFocus='if(this.blur)this.blur();' id='annotColorLink'" +
                            "href='javascript:toggleAnnotationColor()'>Turn Colors Off</a><br />";
                        document.body.appendChild(buttonDiv);
                        //]]>
                    </xsl:if>
                    //<![CDATA[

                    // set up the divs for displaying sentences
                    var mainDivColor = document.createElement("div");
                    var mainDivNoColor = document.createElement("div");
                    mainDivColor.style.display = "block"; // display color div
                    mainDivNoColor.style.display = "none"; // hide no color div
                    document.body.appendChild(mainDivColor);
                    document.body.appendChild(mainDivNoColor);

                    // get sentences, color and mark them up, and add them to
                    // mainDivColor and mainDivNoColor
                    showAnnotation();

                    Array.prototype.contains = function ( needle ) {
                       for (i in this) {
                           if (this[i] == needle) return true;
                       }
                       return false;
                    }
                    
                    var labelsCByDiv = document.createElement("div");
                    var cBys = new Array();
                    var cBy = '';
                    //]]>
                    <xsl:for-each select="fn:subCorpus/fn:sentence/fn:annotationSet/fn:layer[@name='FE'or@name='Target']/fn:label/@cBy">
                        <xsl:sort select='.' order='ascending' />
                        cBy = "<xsl:value-of select='.' />";
                        //<![CDATA[
                        if (!cBys.contains(cBy))
                            cBys.push(cBy);
                        //]]>
                    </xsl:for-each>
                    //<![CDATA[
                    labelsCByDiv.innerHTML = '<b>Annotator ID(s): </b>' + cBys.join(', ');
                    document.body.appendChild(labelsCByDiv);

                    // called by toggle colors link
                    function toggleAnnotationColor() {
                        colors = 1 - colors;
                        if (colors == 1) {
                            document.getElementById('annotColorLink').innerHTML = "Turn Colors Off";
                            mainDivColor.style.display = "block"; // display color div
                            mainDivNoColor.style.display = "none"; // hide no color div
                        } else {
                            document.getElementById('annotColorLink').innerHTML = "Turn Colors On";
                            mainDivColor.style.display = "none"; // hide color div
                            mainDivNoColor.style.display = "block"; // display no color div
                        }
                    }
				} 
                
                /* LEXICAL ENTRY MODE */
                else if (mode == "lexentry") {
                    // create the frameset and two frames for the Lexical Entry
                    // Report and set their src as the currentXMLFile but with the
                    // mode set as 'lexentrytop' for the top frame
                    // and 'sentence' for the bottom frame
					var frameset = document.createElement("frameset");
					frameset.setAttribute("rows","60%,*");
					var mFrame = document.createElement("frame");
					mFrame.setAttribute("src",currentXMLFile+"?mode=lexentrytop&banner=" + banner);
					mFrame.setAttribute("name","top");
					frameset.appendChild(mFrame);
					var sFrame = document.createElement("frame");
					sFrame.setAttribute("src",currentXMLFile+"?mode=sentence");
					sFrame.setAttribute("name","bottom");
					frameset.appendChild(sFrame);
					document.documentElement.appendChild(frameset);
				}

                /* LEXICAL ENTRY MODE TOP FRAME */
                else if (mode == "lexentrytop") {
                    // track the sentences added to the bottom frame in an array
					var sentIDs = new Array();

                    // create a body for this frame
					var frameBody = document.createElement("body");
					document.documentElement.appendChild(frameBody);

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
                        frameBody.appendChild(banFrame);
                    }
                    //]]>
                    <!-- don't display navigation links for desktop reports -->
                    <xsl:if test="$internalMode!='desktop'">
                        //<![CDATA[
                        // create navigation links at top right of report
                        var linkDiv = navBar();
                        document.body.appendChild(linkDiv);
                        //]]>
                    </xsl:if>
                    //<![CDATA[
                    // put all of the lexical entry data in a div with id 'main'
					var mainDiv = document.createElement("div");
					mainDiv.setAttribute("id","main");
					document.body.appendChild(mainDiv);

                    // get the lexical entry data and display in mainDiv
					showLexEntry("main");

					// entry point for adding a sentence to the bottom pane
                    // when user clicks a sentence count link
					function addSent(){
						for (var i=0; i < addSent.arguments.length; i++ ) {
							var sent_id = addSent.arguments[i];
							if (addSentID(sent_id)) {
                                // pass the sentID to the transformAndAdd function
                                // in the bottom sentence frame
								top.frames[1].transformAndAdd(sent_id);
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

                /* LEXICAL ENTRY MODE BOTTOM SENTENCE FRAME */
                else if (mode == "sentence") {
                    // turn colors on
					var colors = 1;

                    // create divs for the colored and marked up sentences
					var sentDivC_On = document.createElement('div'); // colors
					var sentDivC_Off = document.createElement('div'); // no colors

					// generate the body of the document
					var docBody = document.createElement("body");
					document.documentElement.appendChild(docBody);
                    //]]>
                    <!-- don't display toggle colors link or clear sentences link for desktop reports
                         because it can't be handled by the html/javascript api used -->
                    <xsl:if test="$internalMode!='desktop'">
                        //<![CDATA[
                        var buttonDiv = document.createElement("div");
                        buttonDiv.setAttribute("id","buttons");
                        // 'blur' code is to get rid of a dotted-line box around the link
                        buttonDiv.innerHTML += "<a onFocus='if(this.blur)this.blur();'" +
                            "href='javascript:clearSents()'>Clear Sentences</a>";
                        buttonDiv.innerHTML += "<a onFocus='if(this.blur)this.blur();' style='padding:15px;' id='colorLink'" +
                            "href='javascript:toggleColor()'>Turn Colors Off</a><br /><br />";
                        document.body.appendChild(buttonDiv);
                        //]]>
                    </xsl:if>
                    //<![CDATA[
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
                            //]]>
                            <xsl:if test="$internalMode!='desktop'">
                                //<![CDATA[
                                document.getElementById('colorLink').innerHTML = "Turn Colors Off";
                                //]]>
                            </xsl:if>
                            //<![CDATA[
							sentDivC_On.style.display = "block"; // display color div
							sentDivC_Off.style.display = "none"; // hide no color div
						} else {
                            //]]>
                            <xsl:if test="$internalMode!='desktop'">
                                //<![CDATA[
                                document.getElementById('colorLink').innerHTML = "Turn Colors On";
                                //]]>
                            </xsl:if>
                            //<![CDATA[
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
						top.frames[0].sentIDs = new Array();
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

                    // color and mark up the sentences, then display them
                    // called by the top frame when a user clicks a sentence count link
					function transformAndAdd(sentId) {
                        // color and mark up sentence with ID 'sentId'
						sent = getSentenceAnnotation(sentId);
						colorSent = sent[0];
						noColorSent = sent[1];
						
                        // before the sentence, insert the [X] link,
                        // or nothing for the desktop reports
                        var pretext = "";
                        //]]>
                        <xsl:if test="$internalMode!='desktop'">
                            //<![CDATA[
                            pretext = "[<a href='javascript:removeSent(" + sentId + ")'>X</a>] ";
                            //]]>
                        </xsl:if>
                        //<![CDATA[
                        // load the constructed text into two divs, one with color
                        // and one with just mark up
						var newDivC_On = document.createElement('div');
						newDivC_On.setAttribute('id', 'sent' + sentId + 'C_On');
						newDivC_On.innerHTML = pretext + colorSent;

						var newDivC_Off = document.createElement('div');
						newDivC_Off.setAttribute('id', 'sent' + sentId + 'C_Off');
						newDivC_Off.innerHTML = pretext + noColorSent;

                        // add the divs for this sentence to their respective
                        // parent sentence divs
						sentDivC_On.appendChild(newDivC_On);
						sentDivC_Off.appendChild(newDivC_Off);

                        showSents();
					}

                    //]]>
                    <!-- the desktop report system passes in annotation set ids
                         through an XSL paramater, since the bottom frame isn't
                         a real html frame but just another Java panel below the top frame -->
                    <xsl:if test="$annotationSets!=''">
                        var annoSets = "<xsl:value-of select='$annotationSets' />";
                        //<![CDATA[
                        function addSent(annoIds) {
                            for (var i=0; i < annoIds.length; i++ ) {
                                var sent_id = annoIds[i];
                                transformAndAdd(sent_id);
                            }
                        }
                        var annoSetIds = annoSets.split(',');
                        addSent(annoSetIds);
                        //]]>
                    </xsl:if>
                    //<![CDATA[
				}
                                
                /* GENERAL FUNCTIONS */

				// extract XML file name from URL
				function getURLFileName() {
					var wholeurl = window.location.href;
					var result = wholeurl.replace(/[?].*$/,"");
					return result;
				}

                // get the value of a paramater passed in through the url
                // like in '...lu3.xml?mode=lexentry&banner='
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
                    var luLink = document.createElement("a");
                    // create Annotation link for Lexical Entry Report
                    if (mode == "lexentrytop") {
                        luLink.setAttribute("href",currentXMLFile+"?mode=annotation&banner=" + banner);
                        luLink.setAttribute("target","_top");
                        luLink.innerHTML = "Annotation";
                    }
                    // create Lexical Entry link for Annotation Report
                    else if (mode == "" || mode == "annotation") {
                        luLink.setAttribute("href",currentXMLFile+"?mode=lexentry&banner=" + banner);
                        luLink.setAttribute("target","_top");
                        luLink.innerHTML = "Lexical Entry";
                    }
                    // create Frame link
                    var frameLink = document.createElement("a");
                    frameLink.setAttribute("href","../frameIndex.xml?frame=" + frameName + "&banner=" + banner);
                    frameLink.setAttribute("target","_top");
                    frameLink.innerHTML = frameName;
					linkDiv.appendChild(luLink);
					linkDiv.innerHTML += "<font style='padding:5px' />";
					linkDiv.appendChild(frameLink);
					return linkDiv;
				}

                // used by Annotation Report to display all sentences
				function showAnnotation(targetDivId) {
                    // get the annotation for all of the sentences
                    var sentenceAnnot = getSentenceAnnotation("all");
                    // update the color and no color divs
					mainDivColor.innerHTML = sentenceAnnot[0];
                    mainDivNoColor.innerHTML = sentenceAnnot[1];
				}

                //* get the sentence annotation in the form of a colored sentence
                // and a marked up sentence
                //* sentId is actually an annotationSet ID or 'all'
                //* used by the Annotation Report and in the sentence frame of the
                // Lexical Entry Report
				function getSentenceAnnotation(sentId) {
					var sent;
					var sentOrig;					
					var charLabelMap;
					var rank2, rank3;
					var sc_name;
					// [colorSent, noColorSent]
					var finalStr = ["", ""];
					//]]>
					var frameName = "<xsl:value-of select='@frame'/>";
					//<![CDATA[

                    // create an unordered list of all the subcorpora for Annotation Report
					if (sentId == "all") {
						finalStr[0] += "<ul>";
                        finalStr[1] += "<ul>";
                    }
					//]]>
					<xsl:for-each select="fn:subCorpus">
						//<![CDATA[
                        // display subcorpora for Annotation Report
						if (sentId == "all") {
							//]]>
							sc_name = "<xsl:value-of select='@name' />";
							//<![CDATA[
							ind = sc_name.indexOf("-");
							// cuts off the material before the first "-" in the sc name: not always desirable
							sc_name = sc_name.substring(ind+1, sc_name.length);
							finalStr[0] += "<li>"+sc_name+"</li><ol>";
                            finalStr[1] += "<li>"+sc_name+"</li><ol>";
						}

                        // populate maps of annotation labels to char positions
                        // in the sentence for each layer of annotation
                        // and for both colors and no colors
						//]]>
						<xsl:for-each select="fn:sentence/fn:annotationSet[@status='MANUAL'or@status='AUTO_EDITED'or@status='AUTO_APP']">
                            <!-- annotationSet ID that may match passed in 'sentId' -->
							var cursentId = "<xsl:value-of select='@ID' />";
                            <!-- the actual Sentence ID, not annotationSet ID -->
                            var actualsentId = "<xsl:value-of select='../@ID' />";
							//<![CDATA[
							if (sentId == "all" || cursentId == sentId) {
                            	// have to escape all quotes in the sentOrig first, using XSL template at bottom
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
													"<span class='" + frameName + "-" + labelName + "'>", false);
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
											"<span class='" + frameName + "-" + labelName + "'>", true);
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
								if (sentId == "all") {
									finalStr[0] += "<li>";
                                    finalStr[1] += "<li>";
                                }
								
								// color
								finalStr[0] += sent[0][0];
								var invisSpan = "<br /><span class='invisible'>"
								if (rank2) {
                                    //]]>
                                    <xsl:if test="$internalMode!='desktop'">
                                        //<![CDATA[
                                        // display differences between annotation/lexentry
                                        if (sentId == "all")
                                            finalStr[0] += invisSpan + sent[1][0] + "</span>";
                                        else
                                            finalStr[0] += invisSpan + "[X] " + sent[1][0] + "</span>";
                                        //]]>
                                    </xsl:if>
                                    <xsl:if test="$internalMode='desktop'">
                                        //<![CDATA[
                                        finalStr[0] += invisSpan + sent[1][0] + "</span>";
                                        //]]>
                                    </xsl:if>
                                    //<![CDATA[
								}
								if (rank3) {									
									//]]>
                                    <xsl:if test="$internalMode!='desktop'">
                                        //<![CDATA[
                                        // display differences between annotation/lexentry
                                        if (sentId == "all")
                                            finalStr[0] += invisSpan + sent[2][0] + "</span>";
                                        else
                                            finalStr[0] += invisSpan + "[X] " + sent[2][0] + "</span>";
                                        //]]>
                                    </xsl:if>
                                    <xsl:if test="$internalMode='desktop'">
                                        //<![CDATA[
                                        finalStr[0] += invisSpan + sent[2][0] + "</span>";
                                        //]]>
                                    </xsl:if>
                                    //<![CDATA[
								}
                                //]]>
                                <xsl:if test="$internalMode='desktop'">
                                    //<![CDATA[
                                    finalStr[0] += " <a href='" + actualsentId + "'>Edit</a>";
                                    //]]>
                                </xsl:if>
                                //<![CDATA[

                                // no color
                                finalStr[1] += sent[0][1];
                                if (rank2) {
                                    if (sentId == "all")
                                        finalStr[1] += invisSpan + sent[1][1] + "</span>";
                                    else
                                        finalStr[1] += invisSpan + "[X] " + sent[1][1] + "</span>";
                                }
                                if (rank3) {
                                    if (sentId == "all")
                                        finalStr[1] += invisSpan + sent[2][1] + "</span>";
                                    else
                                        finalStr[1] += invisSpan + "[X] " + sent[2][1] + "</span>";
                                }
								
								if (sentId == "all") {
									finalStr[0] += "</li>";
                                    finalStr[1] += "</li>";
                                }
							}
							//]]>
						</xsl:for-each>
						//<![CDATA[
						if (sentId == "all") {
							finalStr[0] += "</ol>";
                            finalStr[1] += "</ol>";
                        }
						//]]>
					</xsl:for-each>
					//<![CDATA[
					if (sentId == "all") {
						finalStr[0] += "</ul>";
                        finalStr[1] += "</ul>";
                    }
					return finalStr;
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

                // the link on sentence counts to add sentences to the bottom
                // sentence frame depends on the internal mode
                function getAddSentLink(idList) {
                    //]]>
                    <!-- outside the desktop, call the javascript function addSent -->
                    <xsl:if test="$internalMode!='desktop'">
                        //<![CDATA[
                        return "<a href='javascript:addSent(" + idList + ")'>";
                        //]]>
                    </xsl:if>
                    <!-- inside the desktop, just link to the list of sentences
                         and the desktop java code will handle the rest -->
                    <xsl:if test="$internalMode='desktop'">
                        //<![CDATA[
                        return "<a href='" + idList + "'>";
                        //]]>
                    </xsl:if>
                    //<![CDATA[
                }

                // get the Lexical Entry data for the top frame of the Lexical Entry Report
				function showLexEntry(targetDivId) {
                    // get basic LU info
					//]]>
					var luName = "<xsl:value-of select='@name' />";
					var frName = "<xsl:value-of select='@frame' />";
					var luDef = "<xsl:value-of select='normalize-space(fn:definition)' />";
                    var incFE = "<xsl:value-of select='@incorporatedFE' />";
                    var luSemType = "";
                    <xsl:for-each select='fn:semType'>
                        luSemType += "<xsl:value-of select="@name" />";
                        <xsl:if test='position()!=last()'>
                            luSemType += ", ";
                        </xsl:if>
                    </xsl:for-each>

                    //<![CDATA[
                    // get lists of support words
                    var governors = "";
                    var supports = "";
                    var controllers = "";
                    // get governor data
                    //]]>
                    <xsl:for-each select="fn:valences/fn:governor[@type='Gov']">
                        governors += "<xsl:value-of select='@lemma' />";
                        <xsl:if test='position()!=last()'>
                            governors += ", ";
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:for-each select="fn:valences/fn:governor[@type='Sup']">
                        supports += "<xsl:value-of select='@lemma' />";
                        <xsl:if test='position()!=last()'>
                            supports += ", ";
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:for-each select="fn:valences/fn:governor[@type='Ctrlr']">
                        controllers += "<xsl:value-of select='@lemma' />";
                        <xsl:if test='position()!=last()'>
                            controllers += ", ";
                        </xsl:if>
                    </xsl:for-each>
					//<![CDATA[
                    // create a div storing the lu info and support words
					var luInfoDiv = document.createElement('div');
					luInfoDiv.innerHTML = "<h1>Lexical Entry</h1>" +
                                             "<h1>" + luName + "</h1>" +
											 "<h3>Frame: " + frName + "</h3>" +
											 "<h4>Definition:</h4>" + luDef + "<br />";
                    if (luSemType != "")
                        luInfoDiv.innerHTML += "<br /><b>Semantic Type: </b>" +
                                                  luSemType + "<br />";
                    if (governors.length > 0)
                        luInfoDiv.innerHTML += "<br />" + "<b>Governor(s):</b> " + governors;
                    if (supports.length > 0)
                        luInfoDiv.innerHTML += "<br />" + "<b>Support(s):</b> " + supports;
                    if (controllers.length > 0)
                        luInfoDiv.innerHTML += "<br />" + "<b>Controller(s):</b> " + controllers;

                    if (incFE) {
                        if (governors.length > 0 || supports.length > 0 || controllers.length > 0)
                            luInfoDiv.innerHTML += "<br />";
                        luInfoDiv.innerHTML += "<br /><b>Incorporated FE: </b>" +
                                               "<span class='" + frName + "-" + incFE + "'>" + incFE + "</span>";
                    }
                    
                    luInfoDiv.innerHTML += "<h3>Frame Elements and Their Syntactic Realizations</h3>" +
											 "The Frame Elements for this word sense are (with realizations):";

                    // FE Realizations
					var colors = new Array();
					var fes = new Array();
					var finalStr = "<p />";
					finalStr += "<table class='feReal'>";
					finalStr += "<tr><th>Frame Element</th><th>Number Annotated</th><th>Realization(s)</th></tr>";
					var sum = 0;
					var curFE = "";
					//]]>
					<xsl:for-each select="fn:valences/fn:FERealization">
						curFE = "<xsl:value-of select='fn:FE/@name' />";
						curSum = "<xsl:value-of select='@total' />";
						idList = "";
						<xsl:for-each select=".//fn:annoSet">
							idList += "\"<xsl:value-of select='@ID' />\",";
						</xsl:for-each>
						//<![CDATA[
						idList = idList.substring(0,idList.length-1);
						finalStr += "<tr><td><span class='" + frName + "-" + curFE + "'>" + curFE +
							"</span></td>";
						finalStr += "<td>(" + getAddSentLink(idList) +
							curSum + "</a>)</td>";
						finalStr += "<td>";
						//]]>
						<xsl:for-each select='fn:pattern'>
							fe = "<xsl:value-of select='fn:valenceUnit/@FE' />";
							pt = "<xsl:value-of select='fn:valenceUnit/@PT' />";
							gf = "<xsl:value-of select='fn:valenceUnit/@GF' />";
							//<![CDATA[
							if (gf == ""){
								gf = "--";
							}
							//]]>
							sum = "<xsl:value-of select='@total' />";
							idList = "";
							<xsl:for-each select="fn:annoSet">
								idList += "\"<xsl:value-of select='@ID' />\",";
							</xsl:for-each>
							//<![CDATA[
							idList = idList.substring(0,idList.length-1);
							finalStr += pt + "." + gf;
							finalStr += " (" + getAddSentLink(idList) +
								sum + "</a>)<br />";
							//]]>
						</xsl:for-each>
						//<![CDATA[
						finalStr += "</td></tr>";
						//]]>
					</xsl:for-each>
					//<![CDATA[
					finalStr += "</table>";

					var feTable = document.createElement('div');
					feTable.setAttribute('id', 'feTable');
					feTable.innerHTML = finalStr;

					// FE Group Realizations
					// need to find out what is the maximum number of FEs in any realization group is
                    var maxFEGroupSize = 0;
					//]]>
					<xsl:for-each select="fn:valences/fn:FEGroupRealization">
						<xsl:sort select="count(fn:FE)" order="descending"/>
						<xsl:if test="position() = 1">
							maxFEGroupSize = <xsl:value-of select="count(fn:FE)"/>;
						</xsl:if>
					</xsl:for-each>
					//<![CDATA[
					finalStr = "<h3>Valence Patterns:</h3>";
					finalStr += "These frame elements occur in the following syntactic patterns: <p />";
					finalStr += "<table class='feGReal'>";
					finalStr += "<tr><th>Number Annotated</th><th colspan='" + maxFEGroupSize +
						"'>Patterns</th></tr>";
					//]]>
					var numFEs;
					<xsl:for-each select="fn:valences/fn:FEGroupRealization">
						idList = "";
						<xsl:for-each select=".//fn:annoSet">
							idList += "\"<xsl:value-of select='@ID' />\",";
						</xsl:for-each>
						//<![CDATA[
						idList = idList.substring(0,idList.length-1);
						//]]>
						curSum = "<xsl:value-of select='@total'/>";
						//<![CDATA[
						finalStr += "<tr><td class='totals'>";
						finalStr += getAddSentLink(idList) + curSum + "</a> TOTAL";
						finalStr += "</td>";
						var j = 0;
						numFEs = 0;
						//]]>
						<xsl:for-each select="fn:FE">
							//<![CDATA[
							numFEs++;
							//]]>
							fe = "<xsl:value-of select='@name' />";
							//<![CDATA[
							finalStr += "<td><span class='"+frName+"-"+fe+"'>"+fe+"</span></td>";
							//]]>
						</xsl:for-each>
						//<![CDATA[
						for (var i = numFEs; i < maxFEGroupSize; i++){
							finalStr += "<td><br /></td>";
						}
						finalStr += "</tr>";
						//]]>
						<xsl:for-each select="fn:pattern">
							idList = "";
							<xsl:for-each select="fn:annoSet">
								idList += "\"<xsl:value-of select='@ID' />\",";
							</xsl:for-each>
							idList = idList.substring(0,idList.length-1);
							sum = "<xsl:value-of select="@total"/>";
							//<![CDATA[
							finalStr += "<tr><td class='totals'> (";
							finalStr += getAddSentLink(idList) + sum + "</a>";
							finalStr += ") </td>";
							numFEs = 0;
							//]]>
							<xsl:for-each select="fn:valenceUnit">
								//<![CDATA[
								numFEs++;
								//]]>
								pt = "<xsl:value-of select='@PT' />";
								gf = "<xsl:value-of select='@GF' />";
								//<![CDATA[
								if (gf == ""){
									gf = "--";
								}
								finalStr += "<td>" + pt + "<br />" + gf + "</td>";
								//]]>
							</xsl:for-each>
							//<![CDATA[
							for (var i = numFEs; i < maxFEGroupSize; i++){
								finalStr += "<td><br /></td>";
							}
							finalStr += "</tr>";
							//]]>
						</xsl:for-each>
					</xsl:for-each>
					//<![CDATA[
					finalStr += "</table>";

					var patternTable = document.createElement('div');
					patternTable.setAttribute('id', 'patternTable');
					patternTable.innerHTML = finalStr;

					var targetDiv = document.getElementById(targetDivId);
					targetDiv.appendChild(luInfoDiv);
					targetDiv.appendChild(feTable);
					targetDiv.appendChild(patternTable);
				}
				//]]>
			</script>
	</head>
  </html>
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
