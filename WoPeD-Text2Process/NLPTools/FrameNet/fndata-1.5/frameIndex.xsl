<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://framenet.icsi.berkeley.edu">
<xsl:output method="html" />
<!-- This XSL file transforms frameIndex XML into a page with two HTML frames,
     the left frame displaying a list of FN Frames and the right displaying
     FN Frame Reports. There are three modes: the main mode (mode=''),
     which just sets up the two HTML frames in a frameset; the index mode
     (mode='frameIndex'), which displays the list of Frames in the left frame;
     and the blank mode, which displays a blank page in the right frame before
     any FN Frame is requested.
     First the browser transforms the XSL to populate the Javascript with
     XML data (the Frames' names), then the Javascript is executed. -->
<xsl:template match="/fn:frameIndex">
<html>
	<head>
      		<title>Frame Index</title>
		<script type="text/javascript">
        	//<![CDATA[
            // Javascript must go in CDATA blocks to get it
            // through the XSL processor

            // PRIMARY ENTRY POINT
            var currentXMLFile = getURLFileName();
            // get paramaters passed through url
            var mode = gup('mode');
            var frame = gup('frame');
            var banner = gup('banner');

            /* MAIN MODE */
            if (mode == "") {
                // create the frameset and two frames for the Frame Index
                // and set their src as the currentXMLFile but with the
                // mode set as 'frameIndex' for the left frame
                // and 'dispBlank' for the right frame
                var frameset = document.createElement("frameset");
                frameset.setAttribute("cols","20%,*");
                var mFrame = document.createElement("frame");
                mFrame.setAttribute("src",currentXMLFile+"?mode=frameIndex&frame=" + frame + "&banner=" + banner);
                mFrame.setAttribute("name","left");
                frameset.appendChild(mFrame);
                var sFrame = document.createElement("frame");
                sFrame.setAttribute("src",currentXMLFile+"?mode=dispBlank&banner=" + banner);
                sFrame.setAttribute("name","right");
                frameset.appendChild(sFrame);
                document.documentElement.appendChild(frameset);
            }

            /* FRAME INDEX MODE */
            else if (mode == "frameIndex") {
                // create a body for the left frame
                var docBody = document.createElement("body");
                document.documentElement.appendChild(docBody);

                // display title and alphabetical links for the list of Frames
                docBody.innerHTML = "<h1>Frame Index</h1>";
                docBody.innerHTML += "<a href='#A'>A </a><a href='#B'>B </a><a href='#C'>C </a><a href='#D'>D </a><a href='#E'>E </a><a href='#F'>F </a>" +
                                     "<a href='#G'>G </a><a href='#H'>H </a><a href='#I'>I </a><a href='#J'>J </a><a href='#K'>K </a><a href='#L'>L </a>" +
                                     "<a href='#M'>M </a><a href='#N'>N </a><a href='#O'>O </a><a href='#P'>P </a><a href='#Q'>Q </a><a href='#R'>R </a>" +
                                     "<a href='#S'>S </a><a href='#T'>T </a><a href='#U'>U </a><a href='#V'>V </a><a href='#W'>W </a><a href='#X'>X </a>" +
                                     "<a href='#Y'>Y </a><a href='#Z'>Z </a>";
                
                // get the list of Frames and store in a 'p' block
                docBody.innerHTML += "<p id='frames'>";
                var framesHTML = '';
                var p = document.getElementById('frames');
                var curLetter = 'A';
                //]]>
                <xsl:for-each select='fn:frame'>
                    var frName = "<xsl:value-of select='@name' />";
                    //<![CDATA[
                    // set up locations for alphabetical links at the top of the list
                    if (frName.charAt(0).toLowerCase() != curLetter.toLowerCase()) {
                        curLetter = frName.charAt(0);
                        framesHTML += '<a name=\"' + curLetter + '\"/>';
                    }
                    // add link for frName
                    framesHTML += "<a href=\'javascript:openFrame(\"" + escape(frName) + "\")\'>" + frName + "</a>";
                    framesHTML += '<br />';
                    //]]>
                </xsl:for-each>
                //<![CDATA[
                docBody.innerHTML += framesHTML;
                docBody.innerHTML += "</p>";

                // if a Frame was passed in through the url, open it
                if (frame)
                    openFrame(frame);
            }

            /* DISPLAY BLANK */
            else if (mode == "dispBlank") {
                // create an empty body for the right frame
                var docBody = document.createElement("body");
                document.documentElement.appendChild(docBody);

                // if a banner was specified, display it in this blank frame
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
            }

            // load an FN Frame in the right HTML frame
			function openFrame(frName) {
				top.frames[1].location.href = 'frame/' + frName + '.xml?banner=' + banner;
			}
        
            // extract XML file name from URL
            function getURLFileName() {
               var wholeurl = window.location.href;
               var result = wholeurl.replace(/[?].*$/,"");
               return result;
            }

            // get the value of a paramater passed in through the url
            // like in 'frameIndex.xml?mode=frameIndex&frame=Causation&banner='
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
		</script>
	</head>
</html>
</xsl:template>
</xsl:stylesheet>
	
