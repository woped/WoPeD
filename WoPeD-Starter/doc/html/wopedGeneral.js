$(document).ready (function() {
	$('a').click ( function( e ){
		var href = $(this).attr('href').toLowerCase();
		if(href.indexOf('http') >= 0){
			e.preventDefault();
			window.open(href);
		}
		
		var slashIndex = window.location.toString().indexOf('/de/'); 
		var backslashIndex = window.location.toString().indexOf('\\de\\');
		if(slashIndex >= 0 || backslashIndex >= 0 ){
			var languageReplacement;
			if (slashIndex >= 0){
				var newhref = '../en/'.concat($(this).attr('href'));
				$('a[href]').attr('href', newhref);
			} else {
				$(this).attr('href') = '..\\en\\'.concat($(this).attr('href'));
			}
		}
	});
});