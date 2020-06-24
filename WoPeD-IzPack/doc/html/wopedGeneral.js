$(document).ready (function() {
	$('a').click ( function( e ){
		var href = $(this).attr('href').toLowerCase();
		if(href.indexOf('http') >= 0){
			e.preventDefault();
			window.open(href);
		} else {
            //Check whether change of location is necessary (only necessary in german index and contents files)
		    if ((window.location.toString().indexOf("index.htm") >= 0 || window.location.toString().indexOf("contents.htm") >= 0)
                && ($(this).attr('href').indexOf("en") < 0)) {
                var slashIndex = window.location.toString().indexOf('/de/');
                var backslashIndex = window.location.toString().indexOf('\\de\\');
                if (slashIndex >= 0 || backslashIndex >= 0) {
                    var languageReplacement;
                    if (slashIndex >= 0) {
                        var newhref = '../en/'.concat($(this).attr('href'));
                        $(this).attr('href', newhref);
                    } else {
                        $(this).attr('href') = '..\\en\\'.concat($(this).attr('href'));
                    }
                }
		    }
		}
	});
});