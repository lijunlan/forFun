/**
 * 
 */
function sendUpMsg(data, onReceive) {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/AppFactoryService/up",
		data : data,
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : onReceive
	});
}

function sendPassageMsg() {
	var msg = $("#input_passage").val();
	if (msg == "")
		return;
	var data = "{'style':'news','method':'upload_message','message':'" + msg
			+ "'}";
	sendUpMsg(data, function(data, textStatu) {
		var dataObj = eval("(" + data + ")");
		alert(dataObj.state);
	});
}