/**
 * 
 */

function login() {
	var email = $("#email").val();
	var password = $("#password").val();
	password = encryptedStr(
			password,
			"9b12eab350d5560dc5f729b8b985a641d94f13006d246117d5a0586b7f3064e512baf409bedc7006a6c11acad2417dca4fc07ccf2f4c46b3cd4643c89955523fe6d7cddcec88ab90e3f1d35a58d0e33024ab17987eedb781b8ad86231d900ddccf38541e4be24ac0930e060785b86c0da680bf22728e7fba65fa0f9225d8c829");
	$.ajax({
		cache : true,
		type : "POST",
		url : "/IvyProService/manage",
		data : "{'style':'user','method':'login','email':'" + email
				+ "','password':'" + password + "'}",
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data, textStatu) {
			var json = eval("(" + data + ")");
			if (getValueFromJson(json, "state") == "success") {
				var mark = getValueFromJson(json, "mark");
				setCookie("mark", mark);
				self.location = 'home.html';
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