/**
 * 
 */
function getUserInfo() {
	j16.ajax({
		cache : true,
		type : "POST",
		url : "/AppFactoryService/up",
		data : "{'style':'own','method':'get_own_info'}",
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data, textStatu) {
			var dataObj = eval("(" + data + ")");
			//TODO
			var html = "<span class=\"nav-head\" style=\"background:url("
				+ dataObj.iconUrl
				+") center\">"
				+"	<t style='background:url("
				+ dataObj.iconUrl
				+") top center'></t>" 
				+"<b style='background: url("
				+ dataObj.iconUrl
				+") bottom center'></b>" 
				+"</span>";
			j16("#icon").html(html);
			j16("#ownName").html(dataObj.ownName);
			j16("#discription").html(dataObj.discription);
		}
	});
}

function sendUpMsg(data, onReceive) {
	j16.ajax({
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

function sendImageMsg() {
	var msg = editor_image.html();
	msg = BASE64.encoder(msg);
	var title = j16("#imageTitle").val();
	var url = j16("#imageUrl").val();
	if (msg == "" || url == "" || title == "")
		return;
	var data = "{'style':'news','method':'upload_image_message','title':'"
			+ title + "','message':'" + msg + "','url':'" + url + "'}";
	sendUpMsg(data, function(data, textStatu) {
		var dataObj = eval("(" + data + ")");
		alert(dataObj.state);
		location.reload(true);
	});
}

function sendPassageMsg() {
	try {
		var msg = editor_text.html();
		msg = BASE64.encoder(msg);
		var title = j16("#textTitle").val();
		if (msg == "" || title == "")
			return;
		var data = "{'style':'news','method':'upload_message','title':'"
				+ title + "','message':'" + msg + "'}";
		sendUpMsg(data, function(data, textStatu) {
			var dataObj = eval("(" + data + ")");
			alert(dataObj.state);
			location.reload(true);
		});
	} catch (e) {
		alert(e);
	}
}

function transmit(index, item_id) {
	j16
			.ajax({
				cache : true,
				type : "POST",
				url : "/AppFactoryService/down",
				data : "{'style':'news','method':'upload_recommend_message','item_id':'"
						+ item_id + "'}",
				async : false,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data, textStatu) {
					try {
						j16("#news_" + index).remove();
					} catch (e) {
						alert(e);
					}
				}
			});
}

function upClick(item_id) {
	j16.ajax({
		cache : true,
		type : "POST",
		url : "/AppFactoryService/down",
		data : "{'style':'news','method':'upload_action','item_id':'" + item_id
				+ "'}",
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data, textStatu) {
		}
	});
}

function get_recommend_news() {
	j16
			.ajax({
				cache : true,
				type : "POST",
				url : "/AppFactoryService/down",
				data : "{'style':'news','method':'down_recommend_message','num':'10'}",
				async : false,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data, textStatu) {
					var json = eval("(" + data + ")");
					if (getValueFromJson(json, "state") == "error") {
						alert(getValueFromJson(json, "msg"));
						return;
					}
					var inhtml = "";
					j16
							.each(
									json,
									function(index, value) {
										inhtml = inhtml
												+ "<li id='"
												+ "news_"
												+ index
												+ "' style='margin-left: -40px'>"
												+ "<div class='div-inline' style='width: 180px; height: 120px;' align='center'>"
												+ "<img class='img-rounded' alt='' src='"
												+ value.face
												+ "' style='width: 100px; height: 100px; margin-top: 10px'>"
												+ "</div>"
												+ "<div class='div-inline' style='width: 570px; height: 120px' align='center'>"
												+ "	<p style='color: #fff; font-size: 20px; margin-top: 45px; font-family: 黑体; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; word-break: keep-all; display: block;'>"
												+ "		<a href='"
												+ value.url
												+ "' onclick=\"upClick('"
												+ value.item_id
												+ "')\" target=\"_Blank\" style='color: #fff; text-decoration: none; overflow: hidden;'>"
												+ value.title
												+ "</a>"
												+ "	</p>"
												+ "</div>"
												+ "<div class='div-inline' style='width: 220px; height: 120px' align='center'>"
												+ "	<button onclick=\"transmit("
												+ index
												+ ","
												+ "'"
												+ value.item_id
												+ "'"
												+ ")\" type='submit' class='btn pull-right' style='background: #2eb398; color: #ffffff; margin-top: 40px; margin-right: 70px'>Transmit</button>"
												+ "</div>" + "</li>";
									});
					j16("#recommend_news").html(inhtml);
				}
			});
}