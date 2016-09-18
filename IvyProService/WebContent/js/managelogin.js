/**
 * 
 */

function login() {
	var username = $("#username").val();
	var password = $("#password").val();
	$.ajax({
		cache : true,
		type : "POST",
		url : "/IvyProService/manage",
		data : "{'style':'manager','method':'login','username':'" + username
				+ "','password':'" + password + "'}",
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data, textStatu) {
			var json = eval("(" + data + ")");
			if (getValueFromJson(json, "state") == "success") {
				var managerId = getValueFromJson(json, "managerId");
				setCookie("managerId", managerId);
				self.location = 'manager.html';
			} else {
				alert(getValueFromJson(json, "msg"));
				// self.location = 'home.html';
			}
		}
	});
}

function jump() {
	self.location = 'register.html';
}