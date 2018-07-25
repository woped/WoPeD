<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://framenet.icsi.berkeley.edu">
<xsl:output method="html" />
<!-- This XSL file transforms Frame XML into Frame Reports, coloring
     the annotation in the definitions through Javascript called onload.
     First the browser executes the XSL to generate the HTML in the body
     and populates the function 'getFEs', used to store the FEs' color data.
     For the Desktop Report System, some links should not be displayed,
     so the xsl:param 'internalMode' is passed with value 'desktop'. -->
<xsl:param name="internalMode"></xsl:param>
<xsl:template match="/fn:frame">
<html>
	<head>
      		<title><xsl:value-of select='@name'/></title>
		<script type="text/javascript">
        //<![CDATA[
        // Javascript must go in CDATA blocks to get it
        // through the XSL processor

        // PRIMARY ENTRY POINT
        var currentXMLFile = getURLFileName();
        var banner = gup('banner');
        var fes; // store the fes' color info in this global variable
		window.onload = convertDefinitions;

        // called onload, converts all of the FE definitions and
        // the Frame definition into colored text
        function convertDefinitions() {
            sorttable.init();
            // get the FEs color info and store it in 'fes'
            getFEs();
            
            // the FE definitions have ids 'fe#'
            // get the first FE definition
			var fe = document.getElementById('fe1');
			for (var i = 2; fe != null; i++) {
                // get definition text
				var def = fe.innerHTML;
                // color the definition and update the text
				fe.innerHTML = convertDefinition(def);
                // get next definition
				fe = document.getElementById('fe' + i);
			}

            // get the Frame definition and color it
			var defEl = document.getElementById('def');
			var def = defEl.innerHTML;
			defEl.innerHTML = convertDefinition(def);
            
            // if the banner was specified, display it
            if (banner) {
                // create an iframe and load the banner in it
                var loc = window.location;
                var domain = loc.protocol + "//" + loc.host + "/";
                var banFrame = document.getElementById('banner');
                banFrame.setAttribute("src",  domain + unescape(banner));
                banFrame.style.width = '100%';
                banFrame.scrolling = 'no';
                banFrame.style.display = 'block';
                banFrame.style.border = 0;

                // add the banner paramater to all of the links,
                // so the banner is displayed throughout all reports
                var links = document.getElementsByTagName('a');
                for (var i = 0; i < links.length; i++) {
                    var link = links[i].href;
                    if (link.indexOf('?') > 0)
                        links[i].href = link + '&banner=' + banner;
                    else
                        links[i].href = link + '?banner=' + banner;
                }
             }

             // the body is hidden (display='none') while the
             // definition coloring is done so that it is transparent to the user;
             // now display body
             document.getElementById('bodyDelay').style.display = 'block';
		}

        // given an FE or Frame definition, color and format it based on its tags
		function convertDefinition(def) {
            // split the definition on '<', '>', and spaces
            // to get the individual words and tags
			var defWords = def.split(/(&gt;)|(&lt;)|(\s+)/);
			var newDef = ''; // converted definition to be returned
            var tag, prevTag;
            // loop through all of the words and tags
			for (var j = 0; j < defWords.length; j++) {
                // make sure the word exists
                // (the split function generates 'undefined' words)
				if (defWords[j]) {
                    // if its the start of a tag ('<')
                    if (defWords[j].substr(0,4) == '&lt;') {
                        // find the actual tag by skipping empty words
                        do { j++ } while (!defWords[j] || defWords[j] == ''); 
                        // store the tag
                        tag = defWords[j];
                        // if the tag is a start tag (doesn't start with '/')
                        if (tag.charAt(0) != '/') {
                            // skip to close of tag
                            while (!defWords[j] || defWords[j].substr(0,4) != '&gt;') j++;
                            j++;

                            // add different coloring or formatting to the definition
                            // based on the tag's value
                            if (tag == 't') { // target formatting
                                if (prevTag != 'fex')
                                    newDef += '<font style="color: rgb(255, 255, 255); background-color: rgb(0, 0, 0); text-transform:uppercase;">';
                                else // target is an FE
                                    newDef += '<font style="text-transform:uppercase;">';
                            }
                            else if (tag == 'fex') { // FE example coloring
                                // store current position
                                var curj = j;
                                // go back to before the tag's end and find 'name=FE'
                                while (!defWords[j] || defWords[j].substr(0,4) != '&gt;') j--;
                                do { j--; } while(!defWords[j] || defWords[j] == '');
                                var feName = defWords[j].substr(6, defWords[j].length - 7);
                                // return position to after tag
                                j = curj;

                                // add coloring based on feName
                                newDef += getHTMLFontColors(feName);
                            }
                            else if (tag == 'fen') { // FE name coloring
                                // go to the fe name
                                while (!defWords[j] || defWords[j] == '') j++;
                                feName = defWords[j];

                                // add coloring based on feName, followed by the name
                                newDef += getHTMLFontColors(feName) + feName;
                            }
                            else if (tag == 'ex')  { // indent examples
                                newDef += '<br /><tr align="left" valign="top"><div style="margin-left:40px;" ><td>';
                            }
                            else if (tag == 'supp' || tag == 'm') { // italicize supports and 'm'
                                newDef += '<i>';
                            }
                            else if (tag == 'gov') { // bold governors
                                newDef += '<b>';
                            }
                            else if (tag == 'x') { // underline 'x'
                                newDef += '<u>';
                            }
                            prevTag = tag;
                        }
                        else { // tag is a closing tag (starts with '/')
                            // remove the '/'
                            tag = tag.substr(1);                            
                            // skip to close of tag
                            while (!defWords[j] || defWords[j].substr(0,4) != '&gt;') j++;
                            // grab a possible trailing space from the endtag
                            var endTagSpace = defWords[j].substr(4);
                            j++;

                            if (tag == 't' || tag == 'fex' || tag == 'fen') {
                                // for the target and fe coloring, just close the font tag
                                newDef += '</font>';
							}
                            else if (tag == 'ex') { // for examples, close the indenting
                                newDef += '<br /></td></div></tr>';
                            }
                            else if (tag == 'supp' || tag == 'm') { // close italics
                                newDef += '</i>';
                            }
                            else if (tag == 'gov') { // close bold
                                newDef += '</b>';
                            }
                            else if (tag == 'x') { // close underlining
                                newDef += '</u>';
                            }
                            prevTag = '';
                        }
                    }
                    // not a tag, just a word or space
                    else {
                        newDef += defWords[j];
                        tag = '';
                    }
				}
			}
			return newDef + "<br /><br />";
		}

        // look up the FE color info given an FE name or abbreviation
        function getHTMLFontColors(feName) {
            var feFgColor, feBgColor;
            // search for the FE name
            for (var i = 0; i < fes.length; i++) {
                if (feName == fes[i]['name']) {
                    feFgColor = fes[i]['fgColor'];
                    feBgColor = fes[i]['bgColor'];
                    break;
                }
            }

            // if FE name couldnt be found, search for it as an FE abbreviation
			if (!feFgColor || !feBgColor) {
                for (var i = 0; i < fes.length; i++) {
                    if (feName == fes[i]['abbrev']) {
                        feFgColor = fes[i]['fgColor'];
                        feBgColor = fes[i]['bgColor'];
                        break;
                    }
                }
            }

            // return font coloring if found FE colors
            if (feFgColor && feBgColor)
				return '<font style="color: #' + feFgColor + '; background-color: #' + feBgColor + ';">';
			else
				return '';
		}

        // get the FE data through XSL and store in 'fes'
        function getFEs() {
            fes = new Array();
            var i = 0;
            //]]>
			<xsl:for-each select='fn:FE'>
                var fe = new Object();
                fe['name'] = "<xsl:value-of select='@name' />";
                fe['abbrev'] = "<xsl:value-of select='@abbrev' />";
                fe['fgColor'] = "<xsl:value-of select='@fgColor' />";
                fe['bgColor'] = "<xsl:value-of select='@bgColor' />";
                fes[i] = fe;
                i++;
			</xsl:for-each>
			//<![CDATA[
        }

        // extract XML file name from URL
        function getURLFileName() {
           var wholeurl = window.location.href;
           var result = wholeurl.replace(/[?].*$/,"");
           return result;
        }

        // get the value of a paramater passed in through the url
        // like in '...ion.xml?banner='
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

        // ALL OF THE REMAINING JAVASCRIPT IS THIRD-PARTY

        // CROSS-BROWSER SPLIT HELPER FUNCTION, BORROWED FROM ONLINE SOURCE
        // Cross-Browser split code found at http://blog.stevenlevithan.com/archives/cross-browser-split
        /* Cross-Browser Split 1.0.1
        (c) Steven Levithan <stevenlevithan.com>; MIT License
        An ECMA-compliant, uniform cross-browser split method */

        var cbSplit;

        // avoid running twice, which would break `cbSplit._nativeSplit`'s reference to the native `split`
        if (!cbSplit) {

        cbSplit = function (str, separator, limit) {
            // if `separator` is not a regex, use the native `split`
            if (Object.prototype.toString.call(separator) !== "[object RegExp]") {
                return cbSplit._nativeSplit.call(str, separator, limit);
            }

            var output = [],
                lastLastIndex = 0,
                flags = (separator.ignoreCase ? "i" : "") +
                        (separator.multiline  ? "m" : "") +
                        (separator.sticky     ? "y" : ""),
                separator = RegExp(separator.source, flags + "g"), // make `global` and avoid `lastIndex` issues by working with a copy
                separator2, match, lastIndex, lastLength;

            str = str + ""; // type conversion
            if (!cbSplit._compliantExecNpcg) {
                separator2 = RegExp("^" + separator.source + "$(?!\\s)", flags); // doesn't need /g or /y, but they don't hurt
            }

            /* behavior for `limit`: if it's...
            - `undefined`: no limit.
            - `NaN` or zero: return an empty array.
            - a positive number: use `Math.floor(limit)`.
            - a negative number: no limit.
            - other: type-convert, then use the above rules. */
            if (limit === undefined || +limit < 0) {
                limit = Infinity;
            } else {
                limit = Math.floor(+limit);
                if (!limit) {
                    return [];
                }
            }

            while (match = separator.exec(str)) {
                lastIndex = match.index + match[0].length; // `separator.lastIndex` is not reliable cross-browser

                if (lastIndex > lastLastIndex) {
                    output.push(str.slice(lastLastIndex, match.index));

                    // fix browsers whose `exec` methods don't consistently return `undefined` for nonparticipating capturing groups
                    if (!cbSplit._compliantExecNpcg && match.length > 1) {
                        match[0].replace(separator2, function () {
                            for (var i = 1; i < arguments.length - 2; i++) {
                                if (arguments[i] === undefined) {
                                    match[i] = undefined;
                                }
                            }
                        });
                    }

                    if (match.length > 1 && match.index < str.length) {
                        Array.prototype.push.apply(output, match.slice(1));
                    }

                    lastLength = match[0].length;
                    lastLastIndex = lastIndex;

                    if (output.length >= limit) {
                        break;
                    }
                }

                if (separator.lastIndex === match.index) {
                    separator.lastIndex++; // avoid an infinite loop
                }
            }

            if (lastLastIndex === str.length) {
                if (lastLength || !separator.test("")) {
                    output.push("");
                }
            } else {
                output.push(str.slice(lastLastIndex));
            }

            return output.length > limit ? output.slice(0, limit) : output;
        };

        cbSplit._compliantExecNpcg = /()??/.exec("")[1] === undefined; // NPCG: nonparticipating capturing group
        cbSplit._nativeSplit = String.prototype.split;

        } // end `if (!cbSplit)`

        // for convenience...
        String.prototype.split = function (separator, limit) {
            return cbSplit(this, separator, limit);
        };

        /*
          SortTable
          version 2
          7th April 2007
          Stuart Langridge, http://www.kryogenix.org/code/browser/sorttable/

          Instructions:
          Add class="sortable" to any table you'd like to make sortable
          Click on the headers to sort

          Thanks to many, many people for contributions and suggestions.
          Licenced as X11: http://www.kryogenix.org/code/browser/licence.html
          This basically means: do what you want with it.
        */
        
        var stIsIE = /*@cc_on!@*/false;

        sorttable = {
          init: function() {
            // quit if this function has already been called
            if (arguments.callee.done) return;
            // flag this function so we don't do the same thing twice
            arguments.callee.done = true;
            // kill the timer
            //if (_timer) clearInterval(_timer);

            if (!document.createElement || !document.getElementsByTagName) return;

            // MODIFIED to fit our format and include time
            sorttable.DATE_RE = /^(\d\d?)[\/\.-](\d\d?)[\/\.-]((\d\d)?\d\d)[\s](\d\d)[:](\d\d)[:](\d\d)(.*)$/;

            forEach(document.getElementsByTagName('table'), function(table) {
              if (table.className.search(/\bsortable\b/) != -1) {
                sorttable.makeSortable(table);
              }
            });

          },

          makeSortable: function(table) {
            if (table.getElementsByTagName('thead').length == 0) {
              // table doesn't have a tHead. Since it should have, create one and
              // put the first table row in it.
              the = document.createElement('thead');
              the.appendChild(table.rows[0]);
              table.insertBefore(the,table.firstChild);
            }
            // Safari doesn't support table.tHead, sigh
            if (table.tHead == null) table.tHead = table.getElementsByTagName('thead')[0];

            if (table.tHead.rows.length != 1) return; // can't cope with two header rows

            // Sorttable v1 put rows with a class of "sortbottom" at the bottom (as
            // "total" rows, for example). This is B&R, since what you're supposed
            // to do is put them in a tfoot. So, if there are sortbottom rows,
            // for backwards compatibility, move them to tfoot (creating it if needed).
            sortbottomrows = [];
            for (var i=0; i<table.rows.length; i++) {
              if (table.rows[i].className.search(/\bsortbottom\b/) != -1) {
                sortbottomrows[sortbottomrows.length] = table.rows[i];
              }
            }
            if (sortbottomrows) {
              if (table.tFoot == null) {
                // table doesn't have a tfoot. Create one.
                tfo = document.createElement('tfoot');
                table.appendChild(tfo);
              }
              for (var i=0; i<sortbottomrows.length; i++) {
                tfo.appendChild(sortbottomrows[i]);
              }
              delete sortbottomrows;
            }

            // work through each column and calculate its type
            headrow = table.tHead.rows[0].cells;
            for (var i=0; i<headrow.length; i++) {
              // manually override the type with a sorttable_type attribute
              if (!headrow[i].className.match(/\bsorttable_nosort\b/)) { // skip this col
                mtch = headrow[i].className.match(/\bsorttable_([a-z0-9]+)\b/);
                if (mtch) { override = mtch[1]; }
                  if (mtch && typeof sorttable["sort_"+override] == 'function') {
                    headrow[i].sorttable_sortfunction = sorttable["sort_"+override];
                  } else {
                    headrow[i].sorttable_sortfunction = sorttable.guessType(table,i);
                  }
                  // make it clickable to sort
                  headrow[i].sorttable_columnindex = i;
                  headrow[i].sorttable_tbody = table.tBodies[0];
                  dean_addEvent(headrow[i],"click", function(e) {

                  if (this.className.search(/\bsorttable_sorted\b/) != -1) {
                    // if we're already sorted by this column, just
                    // reverse the table, which is quicker
                    sorttable.reverse(this.sorttable_tbody);
                    this.className = this.className.replace('sorttable_sorted',
                                                            'sorttable_sorted_reverse');
                    this.removeChild(document.getElementById('sorttable_sortfwdind'));
                    sortrevind = document.createElement('span');
                    sortrevind.id = "sorttable_sortrevind";
                    sortrevind.innerHTML = stIsIE ? '&nbsp<font face="webdings">5</font>' : '&nbsp;&#x25B4;';
                    this.appendChild(sortrevind);
                    return;
                  }
                  if (this.className.search(/\bsorttable_sorted_reverse\b/) != -1) {
                    // if we're already sorted by this column in reverse, just
                    // re-reverse the table, which is quicker
                    sorttable.reverse(this.sorttable_tbody);
                    this.className = this.className.replace('sorttable_sorted_reverse',
                                                            'sorttable_sorted');
                    this.removeChild(document.getElementById('sorttable_sortrevind'));
                    sortfwdind = document.createElement('span');
                    sortfwdind.id = "sorttable_sortfwdind";
                    sortfwdind.innerHTML = stIsIE ? '&nbsp<font face="webdings">6</font>' : '&nbsp;&#x25BE;';
                    this.appendChild(sortfwdind);
                    return;
                  }

                  // remove sorttable_sorted classes
                  theadrow = this.parentNode;
                  forEach(theadrow.childNodes, function(cell) {
                    if (cell.nodeType == 1) { // an element
                      cell.className = cell.className.replace('sorttable_sorted_reverse','');
                      cell.className = cell.className.replace('sorttable_sorted','');
                    }
                  });
                  sortfwdind = document.getElementById('sorttable_sortfwdind');
                  if (sortfwdind) { sortfwdind.parentNode.removeChild(sortfwdind); }
                  sortrevind = document.getElementById('sorttable_sortrevind');
                  if (sortrevind) { sortrevind.parentNode.removeChild(sortrevind); }

                  this.className += ' sorttable_sorted';
                  sortfwdind = document.createElement('span');
                  sortfwdind.id = "sorttable_sortfwdind";
                  sortfwdind.innerHTML = stIsIE ? '&nbsp<font face="webdings">6</font>' : '&nbsp;&#x25BE;';
                  this.appendChild(sortfwdind);

                    // build an array to sort. This is a Schwartzian transform thing,
                    // i.e., we "decorate" each row with the actual sort key,
                    // sort based on the sort keys, and then put the rows back in order
                    // which is a lot faster because you only do getInnerText once per row
                    row_array = [];
                    col = this.sorttable_columnindex;
                    rows = this.sorttable_tbody.rows;
                    for (var j=0; j<rows.length; j++) {
                      row_array[row_array.length] = [sorttable.getInnerText(rows[j].cells[col]), rows[j]];
                    }
                    /* If you want a stable sort, uncomment the following line */
                    //sorttable.shaker_sort(row_array, this.sorttable_sortfunction);
                    /* and comment out this one */
                    row_array.sort(this.sorttable_sortfunction);

                    tb = this.sorttable_tbody;
                    for (var j=0; j<row_array.length; j++) {
                      tb.appendChild(row_array[j][1]);
                    }

                    delete row_array;
                  });
                }
            }
          },

          guessType: function(table, column) {
            // guess the type of a column based on its first non-blank row
            sortfn = sorttable.sort_alpha;
            for (var i=0; i<table.tBodies[0].rows.length; i++) {
              text = sorttable.getInnerText(table.tBodies[0].rows[i].cells[column]);
              if (text != '') {
                if (text.match(/^-?[£$€]?[\d,.]+%?$/)) {
                  return sorttable.sort_numeric;
                }
                // check for a date: dd/mm/yyyy or dd/mm/yy
                // can have / or . or - as separator
                // can be mm/dd as well
                possdate = text.match(sorttable.DATE_RE)
                if (possdate) {
                    // MODIFIED to just use mmdd format
                    sortfn = sorttable.sort_mmdd;
                }
              }
            }
            return sortfn;
          },

          getInnerText: function(node) {
            // gets the text we want to use for sorting for a cell.
            // strips leading and trailing whitespace.
            // this is *not* a generic getInnerText function; it's special to sorttable.
            // for example, you can override the cell text with a customkey attribute.
            // it also gets .value for <input> fields.

            hasInputs = (typeof node.getElementsByTagName == 'function') &&
                         node.getElementsByTagName('input').length;

            if (node.getAttribute("sorttable_customkey") != null) {
              return node.getAttribute("sorttable_customkey");
            }
            else if (typeof node.textContent != 'undefined' && !hasInputs) {
              return node.textContent.replace(/^\s+|\s+$/g, '');
            }
            else if (typeof node.innerText != 'undefined' && !hasInputs) {
              return node.innerText.replace(/^\s+|\s+$/g, '');
            }
            else if (typeof node.text != 'undefined' && !hasInputs) {
              return node.text.replace(/^\s+|\s+$/g, '');
            }
            else {
              switch (node.nodeType) {
                case 3:
                  if (node.nodeName.toLowerCase() == 'input') {
                    return node.value.replace(/^\s+|\s+$/g, '');
                  }
                case 4:
                  return node.nodeValue.replace(/^\s+|\s+$/g, '');
                  break;
                case 1:
                case 11:
                  var innerText = '';
                  for (var i = 0; i < node.childNodes.length; i++) {
                    innerText += sorttable.getInnerText(node.childNodes[i]);
                  }
                  return innerText.replace(/^\s+|\s+$/g, '');
                  break;
                default:
                  return '';
              }
            }
          },

          reverse: function(tbody) {
            // reverse the rows in a tbody
            newrows = [];
            for (var i=0; i<tbody.rows.length; i++) {
              newrows[newrows.length] = tbody.rows[i];
            }
            for (var i=newrows.length-1; i>=0; i--) {
               tbody.appendChild(newrows[i]);
            }
            delete newrows;
          },

          /* sort functions
             each sort function takes two parameters, a and b
             you are comparing a[0] and b[0] */
          sort_numeric: function(a,b) {
            aa = parseFloat(a[0].replace(/[^0-9.-]/g,''));
            if (isNaN(aa)) aa = 0;
            bb = parseFloat(b[0].replace(/[^0-9.-]/g,''));
            if (isNaN(bb)) bb = 0;
            return aa-bb;
          },
          sort_alpha: function(a,b) {
            if (a[0]==b[0]) return 0;
            if (a[0]<b[0]) return -1;
            return 1;
          },
          sort_mmdd: function(a,b) { // MODIFIED to include time
            mtch = a[0].match(sorttable.DATE_RE);
            y = mtch[3]; d = mtch[2]; m = mtch[1];
            hr = mtch[5]; min = mtch[6]; sec = mtch[7];
            if (m.length == 1) m = '0'+m;
            if (d.length == 1) d = '0'+d;
            dt1 = y+m+d+hr+min+sec;
            mtch = b[0].match(sorttable.DATE_RE);
            y = mtch[3]; d = mtch[2]; m = mtch[1];
            hr = mtch[5]; min = mtch[6]; sec = mtch[7];
            if (m.length == 1) m = '0'+m;
            if (d.length == 1) d = '0'+d;
            dt2 = y+m+d+hr+min+sec;
            if (dt1==dt2) return 0;
            if (dt1<dt2) return -1;
            return 1;
          },

          shaker_sort: function(list, comp_func) {
            // A stable sort function to allow multi-level sorting of data
            // see: http://en.wikipedia.org/wiki/Cocktail_sort
            // thanks to Joseph Nahmias
            var b = 0;
            var t = list.length - 1;
            var swap = true;

            while(swap) {
                swap = false;
                for(var i = b; i < t; ++i) {
                    if ( comp_func(list[i], list[i+1]) > 0 ) {
                        var q = list[i]; list[i] = list[i+1]; list[i+1] = q;
                        swap = true;
                    }
                } // for
                t--;

                if (!swap) break;

                for(var i = t; i > b; --i) {
                    if ( comp_func(list[i], list[i-1]) < 0 ) {
                        var q = list[i]; list[i] = list[i-1]; list[i-1] = q;
                        swap = true;
                    }
                } // for
                b++;

            } // while(swap)
          }
        }

        /* ******************************************************************
           Supporting functions: bundled here to avoid depending on a library
           ****************************************************************** */

        // Dean Edwards/Matthias Miller/John Resig

        /* for Mozilla/Opera9 */
        if (document.addEventListener) {
            document.addEventListener("DOMContentLoaded", sorttable.init, false);
        }

        /* for Internet Explorer */
        /*@cc_on @*/
        /*@if (@_win32)
            document.write("<script id=__ie_onload defer src=javascript:void(0)><\/script>");
            var script = document.getElementById("__ie_onload");
            script.onreadystatechange = function() {
                if (this.readyState == "complete") {
                    sorttable.init(); // call the onload handler
                }
            };
        /*@end @*/


        // written by Dean Edwards, 2005
        // with input from Tino Zijdel, Matthias Miller, Diego Perini

        // http://dean.edwards.name/weblog/2005/10/add-event/

        function dean_addEvent(element, type, handler) {
            if (element.addEventListener) {
                element.addEventListener(type, handler, false);
            } else {
                // assign each event handler a unique ID
                if (!handler.$$guid) handler.$$guid = dean_addEvent.guid++;
                // create a hash table of event types for the element
                if (!element.events) element.events = {};
                // create a hash table of event handlers for each element/event pair
                var handlers = element.events[type];
                if (!handlers) {
                    handlers = element.events[type] = {};
                    // store the existing event handler (if there is one)
                    if (element["on" + type]) {
                        handlers[0] = element["on" + type];
                    }
                }
                // store the event handler in the hash table
                handlers[handler.$$guid] = handler;
                // assign a global event handler to do all the work
                element["on" + type] = handleEvent;
            }
        };
        // a counter used to create unique IDs
        dean_addEvent.guid = 1;

        function removeEvent(element, type, handler) {
            if (element.removeEventListener) {
                element.removeEventListener(type, handler, false);
            } else {
                // delete the event handler from the hash table
                if (element.events && element.events[type]) {
                    delete element.events[type][handler.$$guid];
                }
            }
        };

        function handleEvent(event) {
            var returnValue = true;
            // grab the event object (IE uses a global event object)
            event = event || fixEvent(((this.ownerDocument || this.document || this).parentWindow || window).event);
            // get a reference to the hash table of event handlers
            var handlers = this.events[event.type];
            // execute each event handler
            for (var i in handlers) {
                this.$$handleEvent = handlers[i];
                if (this.$$handleEvent(event) === false) {
                    returnValue = false;
                }
            }
            return returnValue;
        };

        function fixEvent(event) {
            // add W3C standard event methods
            event.preventDefault = fixEvent.preventDefault;
            event.stopPropagation = fixEvent.stopPropagation;
            return event;
        };
        fixEvent.preventDefault = function() {
            this.returnValue = false;
        };
        fixEvent.stopPropagation = function() {
          this.cancelBubble = true;
        }

        // Dean's forEach: http://dean.edwards.name/base/forEach.js
        /*
            forEach, version 1.0
            Copyright 2006, Dean Edwards
            License: http://www.opensource.org/licenses/mit-license.php
        */

        // array-like enumeration
        if (!Array.forEach) { // mozilla already supports this
            Array.forEach = function(array, block, context) {
                for (var i = 0; i < array.length; i++) {
                    block.call(context, array[i], i, array);
                }
            };
        }

        // generic enumeration
        Function.prototype.forEach = function(object, block, context) {
            for (var key in object) {
                if (typeof this.prototype[key] == "undefined") {
                    block.call(context, object[key], key, object);
                }
            }
        };

        // character enumeration
        String.forEach = function(string, block, context) {
            Array.forEach(string.split(""), function(chr, index) {
                block.call(context, chr, index, string);
            });
        };

        // globally resolve forEach enumeration
        var forEach = function(object, block, context) {
            if (object) {
                var resolve = Object; // default
                if (object instanceof Function) {
                    // functions have a "length" property
                    resolve = Function;
                } else if (object.forEach instanceof Function) {
                    // the object implements a custom forEach method so use that
                    object.forEach(block, context);
                    return;
                } else if (typeof object == "string") {
                    // the object is a string
                    resolve = String;
                } else if (typeof object.length == "number") {
                    // the object is array-like
                    resolve = Array;
                }
                resolve.forEach(object, block, context);
            }
        };
		//]]>
		</script>
   	</head>
   	<body>
        <!-- body stored in a div to be displayed after 
             the definitions are done being colored -->
        <div id='bodyDelay' style='display:none;'>
            <iframe id='banner' style='display:none;'></iframe>
            <!-- display navigation link at top right, unless on desktop -->
            <xsl:if test="$internalMode!='desktop'">
                <div style="float:right;">
                    <a href="../luIndex.xml" target='_top'>Lexical Unit Index</a>
                </div>
            </xsl:if>
            <!-- basic frame info: name, definition, semantic type -->
            <h1><xsl:value-of select='@name' /></h1>
            <h3>Definition:</h3>
            <table><tr><td id='def'><xsl:value-of select='fn:definition' /></td></tr></table>
            <xsl:if test="fn:semType/@name!=''">
                <b style='font-size:19px'>Semantic Type: </b>
                <xsl:for-each select='fn:semType'>
                    <xsl:value-of select="@name" />
                    <xsl:if test='position()!=last()'>, </xsl:if>
                </xsl:for-each>
                <br />
            </xsl:if>

            <h3>FEs:</h3>
            <h4>Core:</h4>
            <table border='0' bgcolor='#FFFFFF' cellspacing='0' vspace='0'>
                <xsl:for-each select='fn:FE'>
                    <xsl:sort select='@name' order='ascending' />
                    <xsl:variable name='feNum' select='position()' />
                    <xsl:if test='@coreType="Core"' >
                        <tr>
                            <td valign='top' width='210' cellpadding='4'>
                                <xsl:variable name='fgColor' select='@fgColor' />
                                <xsl:variable name='bgColor' select='@bgColor' />

                                <font style='color:#{$fgColor}; background-color:#{$bgColor}'>
                                    <xsl:value-of select='@name' /> [<xsl:value-of select='@abbrev' />]
                                </font>

                                <xsl:if test="fn:semType/@name!=''">
                                    <br /><b style='font-size:14px'>Semantic Type: </b>
                                    <xsl:for-each select='fn:semType'>
                                        <xsl:value-of select="@name" />
                                        <xsl:if test='position()!=last()'>, </xsl:if>
                                    </xsl:for-each>
                                </xsl:if>

                               <xsl:if test="fn:requiresFE/@name!=''">
                                    <br /><b style='font-size:14px'>Requires: </b>
                                    <xsl:for-each select='fn:requiresFE'>
                                        <xsl:sort select='@name' order='ascending' />
                                        <xsl:value-of select='@name' />
                                        <xsl:if test='position()!=last()'>, </xsl:if>
                                    </xsl:for-each>
                                </xsl:if>
                                <xsl:if test="fn:excludesFE/@name!=''">
                                    <br /><b style='font-size:14px'>Excludes: </b>
                                    <xsl:for-each select='fn:excludesFE'>
                                        <xsl:sort select='@name' order='ascending' />
                                        <xsl:value-of select='@name' />
                                        <xsl:if test='position()!=last()'>, </xsl:if>
                                    </xsl:for-each>
                                </xsl:if>
                            </td>

                            <td align='left' valign='top' id='fe{$feNum}'>
                                <xsl:value-of select='fn:definition' />
                            </td>

                        </tr>
                    </xsl:if>
                </xsl:for-each>
                <tr><td><h4>Non-Core:</h4></td></tr>
                <xsl:for-each select='fn:FE'>
                    <xsl:sort select='@name' order='ascending' />
                    <xsl:variable name='feNum' select='position()' />
                    <xsl:if test='@coreType!="Core"' >
                        <tr>
                            <td valign='top' width='210' cellpadding='4'>
                                <xsl:variable name='fgColor' select='@fgColor' />
                                <xsl:variable name='bgColor' select='@bgColor' />

                                <font style='color:#{$fgColor}; background-color:#{$bgColor}'>
                                    <xsl:value-of select='@name' /> [<xsl:value-of select='@abbrev' />]
                                </font>

                                <xsl:if test="fn:semType/@name!=''">
                                    <br /><b style='font-size:14px'>Semantic Type: </b>
                                    <xsl:for-each select='fn:semType'>
                                        <xsl:value-of select="@name" />
                                        <xsl:if test='position()!=last()'>, </xsl:if>
                                    </xsl:for-each>
                                </xsl:if>
                            </td>
                            <td align='left' valign='top' id='fe{$feNum}'>
                                <xsl:value-of select='fn:definition' />
                            </td>
                        </tr>
                    </xsl:if>
                </xsl:for-each>
            </table>

            <xsl:if test="fn:FEcoreSet">
                <h3>FE Core set(s):</h3>
                <xsl:for-each select="fn:FEcoreSet">
                    &#123;<xsl:for-each select="fn:memberFE">
                        <xsl:sort select='@name' order='ascending' />
                        <xsl:value-of select="@name" />
                        <xsl:if test='position()!=last()'>, </xsl:if>
                    </xsl:for-each>&#125;<xsl:if test='position()!=last()'>, </xsl:if>
                </xsl:for-each>
            </xsl:if>

            <h3>Frame-frame Relations:</h3>
            <p>
                <xsl:for-each select='fn:frameRelation'>
                    <xsl:value-of select='@type' />:
                    <xsl:for-each select='fn:relatedFrame'>
                        <xsl:sort select='.' order='ascending' />
                        <xsl:variable name='rfName' select='current()' />
                        <a href='{$rfName}.xml'>
                            <xsl:value-of select='$rfName' />
                        </a>
                        <xsl:if test='position()!=last()'>, </xsl:if>
                    </xsl:for-each>
                    <br />
                </xsl:for-each>
            </p>

            <h3>Lexical Units:</h3>
            <p><i>
                <xsl:for-each select='fn:lexUnit'>
                    <xsl:sort select='@name' order='ascending' />
                    <xsl:value-of select='@name' />
                    <xsl:if test='position()!=last()'>, </xsl:if>
                </xsl:for-each>
            </i></p>
            <p>Created by <xsl:value-of select='@cBy' /> on <xsl:value-of select='@cDate' /></p>
            <hr />
            <xsl:if test="$internalMode!='desktop'">
                <table class='sortable' cellspacing='10' margin-left='auto' margin-right='auto'>
                    <tr><td><a href="javascript:"><b>Lexical Unit</b></a></td>
                        <td><a href="javascript:"><b>LU Status</b></a></td>
                        <td><a href="javascript:"><b>Lexical Entry Report</b></a></td>
                        <td><a href="javascript:"><b>Annotation Report</b></a></td>
                        <td><a href="javascript:"><b>Annotator ID</b></a></td>
                        <td><a href="javascript:"><b>Created Date</b></a></td></tr>
                    <xsl:for-each select='fn:lexUnit'>
                        <xsl:sort select='@name' order='ascending' />
                        <tr><td><xsl:value-of select='@name' /></td>
                        <td><b><xsl:value-of select='@status' /></b></td>
                        <xsl:variable name='luID' select='@ID' />
                        <td><xsl:if test='@status!="Problem"'>
                            <a href='../lu/lu{$luID}.xml?mode=lexentry' target='_top'>Lexical entry</a>
                        </xsl:if></td>
                        <td><xsl:if test='fn:sentenceCount/@annotated>0 and @status!="Problem"'>
                            <a href='../lu/lu{$luID}.xml?mode=annotation' target='_top'>Annotation</a>
                        </xsl:if></td>
                        <td><xsl:value-of select='@cBy' /></td>
                        <td><xsl:value-of select='@cDate' /></td>
                    </tr></xsl:for-each>
                </table>
            </xsl:if>
            <!-- exclude user-sorting and annotation/lexentry links for desktop reports -->
            <xsl:if test="$internalMode='desktop'">
                <table  cellspacing='10' margin-left='auto' margin-right='auto'>
                    <tr><td><b>Lexical Unit</b></td>
                        <td><b>LU Status</b></td>
                        <td><b>Annotator ID</b></td>
                        <td><b>Created Date</b></td></tr>
                    <xsl:for-each select='fn:lexUnit'>
                        <xsl:sort select='@name' order='ascending' />
                        <tr><td><xsl:value-of select='@name' /></td>
                        <td><b><xsl:value-of select='@status' /></b></td>                        
                        <td><xsl:value-of select='@cBy' /></td>
                        <td><xsl:value-of select='@cDate' /></td>
                    </tr></xsl:for-each>
                </table>
            </xsl:if>
        </div>
	</body>
</html>
</xsl:template>
</xsl:stylesheet>
