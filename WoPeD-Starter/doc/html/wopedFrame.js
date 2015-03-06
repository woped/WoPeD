$(document).ready (function() {
	$('#loadIndex').click ( function () {
		var loc = new String(location.href);
		var index = loc.lastIndexOf("/");
		loc = loc.substring(0, index + 1);
		loc = loc + "index.htm";
		$('.content').attr('src', loc);
	});
	
	$('#loadGlossary').click ( function () {
		var loc = new String(location.href);
		var index = loc.lastIndexOf("/");
		loc = loc.substring(0, index + 1);
		loc = loc + "contents.htm";
		$('.content').attr('src', loc);
	});
});