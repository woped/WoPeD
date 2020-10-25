$.urlParam = function(name, url){
    if(!url){
     url = window.location.href;
    }
    var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(url);
    if (!results)
    { 
        return 0; 
    }
    return results[1] || 0;
}

$.docReady = function(downloadText, installationText, startText, useText) {
	//Get URL parameter
	var selectedMaster = decodeURIComponent($.urlParam('selection'));
	
	//Verify selected master item and set detail text corresponding to it
	var detail;
	if( selectedMaster == "installation"){//selectedMaster.search("download") != -1
		detail = installationText
		selectedMaster = $("#installation");
	} else if(selectedMaster == "start"){
		detail = startText;
		selectedMaster = $("#start");
	} else if(selectedMaster == "use"){
		detail = useText;
		selectedMaster = $("#use");
	} else {
		//Either download selected or set download as default
		detail = downloadText; 
		selectedMaster = $("#download");
	}

	//Set color for selected/unselected master items
	$(".MasterItem").css("background", "transparent");
	$(".MasterItem").css("color", "black");
	$(selectedMaster).css("background-color", "orange");
	$(selectedMaster).css("color", "white");

	//Set detail text in html for selected master item
	$( ".Detail" ).html( detail );
};