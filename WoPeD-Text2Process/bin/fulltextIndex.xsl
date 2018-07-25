<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://framenet.icsi.berkeley.edu">
<xsl:output method="html" />
<!-- This XSL file transforms fulltextIndex XML into a page
     listing Corpora, which expand to list their Documents. The
     expanding links are handled by Javascript.
     First the XSL transforms the body then some Javascript
     is executed onload. -->
<xsl:template match="/fn:fulltextIndex">
<html>
	<head>
		<title>Full Text Index</title>
		<style>
			.mC {width:500px; margin:5px; float:left;}
			.mH {color:blue; cursor:pointer; font-weight:bold;}
			.mL {display:none; margin-bottom:10px; font-weight:bold;}
			.mO {margin-left:10px; display:block;}
		</style>
		<script type="text/javascript">
		//<![CDATA[
        // Javascript must go in CDATA blocks to get it
        // through the XSL processor

        // called when a user clicks a Corpus name
		function toggleMenu(objID) {
			if (!document.getElementById) return;
			var ob = document.getElementById(objID).style;
            // display or hide the div containing Documents under the clicked Corpus
			ob.display = (ob.display == 'block') ? 'none' : 'block';
		}

        // PRIMARY ENTRY POINT
        var currentXMLFile = getURLFileName();
        var banner = gup('banner');

        window.onload = escapeURLs;
        // called onload to escape links to Documents with
        // characters that must be escaped like '%'
        function escapeURLs() {
            // get all Document links
            var docLinks = document.getElementsByTagName('a');
            for (var i = 0; i < docLinks.length; i++) {
                var curHref = docLinks[i].href;
                var fulltextI = curHref.indexOf('fulltext/');
                // escape characters in the Document's name
                docLinks[i].href = curHref.substring(0, fulltextI) + escape(curHref.substring(fulltextI));
            }

            // if a banner was specified, display it
            if (banner) {
                // create an iFrame and load the banner in it
                var loc = window.location;
                var domain = loc.protocol + "//" + loc.host + "/";
               // document.write(domain);
                var banFrame = document.getElementById('banner');
                banFrame.setAttribute("src",  domain + unescape(banner));
                banFrame.style.width = '100%';
                banFrame.scrolling = 'no';
                banFrame.style.display = 'block';
                banFrame.style.border = 0;

                // add the banner paramater to the Document links
                for (var i = 0; i < docLinks.length; i++)
                    docLinks[i].href += "?banner=" + banner;
            }
        }

        // extract XML file name from URL
        function getURLFileName() {
           var wholeurl = window.location.href;
           var result = wholeurl.replace(/[?].*$/,"");
           return result;
        }

        // get the value of a paramater passed in through the url
        // like in 'fulltextIndex.xml?banner='
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
	<body>
        <iframe id='banner' style='display:none;'></iframe>
        <h1>Full Text Index</h1>
		<b>Choose a Corpus/Document.</b>
		<br />
		<div class='mC'>
		<xsl:for-each select='fn:corpus'>
            <xsl:sort select='@name' order='ascending' />
			<xsl:variable name='menuNum' select='position()' />
			<xsl:variable name='corpName' select='@name' />
			<div class='mH' onclick="toggleMenu('menu{$menuNum}')">
				+ <xsl:value-of select='$corpName' />
			</div>
			<div id='menu{$menuNum}' class='mL'>
				<xsl:for-each select='fn:document'>
                    <xsl:sort select='@description' order='ascending' />
					<xsl:variable name='docName' select='@description' />
					<a class='mO' href='fulltext/{$corpName}__{$docName}.xml'>
						<xsl:value-of select='$docName' />
					</a>
				</xsl:for-each>
			</div>
		</xsl:for-each>
		</div>
	</body>
</html>
</xsl:template>
</xsl:stylesheet>
