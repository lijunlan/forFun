/**
 * 
 */

var page = 0;

$(document).ready(function() {
	if (getCookie("mark") == null) {
		self.location = 'login.html';
		return;
	}
	getPage();
	setUpLoginLink();
	getOrders();
});

function getPage() {
	var p = $_GET('page');
	if (p == null || p == "") {
		return;
	} else {
		page = p;
	}
}

function addPage() {
	changePage(page + 1);
}

function subPage() {
	changePage(page - 1);
}

function changePage(p) {
	if (p >= 0)
		page = p;
	else
		return;
	getOrders();
}

function getOrders() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/IvyProService/manage",
		data : "{'style':'order','method':'get_list','mark':'"
				+ getCookie("mark") + "','page':'" + page + "'}",
		async : false,
		error : function(request) {
			// alert("Connection error");
		},
		success : function(data, textStatu) {
			var json = eval("(" + data + ")");
			if (getValueFromJson(json, "state") == "error") {
				alert(getValueFromJson(json, "msg"));
				return;
			}
			var inhtml = "";
			$.each(json, function(index, value) {
				inhtml = inhtml + "<tr>" + "<td>" + (index + 1) + "</td>"
						+ "<td>" + value.id + "</td>" + "<td>" + value.category
						+ "</td>" + "<td>" + value.title + "</td>" + "<td>"
						+ value.createDate + "</td>" + "<td>" + value.isEnd
						+ "</td>" + "</tr>";
			});
			$("#orderlist").html(inhtml);
			if (page <= 3) {
				$("#b1").html(1);
				$("#b1").attr("href", "javascript:changePage(1)");
				$("#b2").html(2);
				$("#b2").attr("href", "javascript:changePage(2)");
				$("#b3").html(3);
				$("#b3").attr("href", "javascript:changePage(3)");
				$("#b4").html(4);
				$("#b4").attr("href", "javascript:changePage(4)");
				$("#b5").html(5);
				$("#b5").attr("href", "javascript:changePage(5)");
			} else {
				$("#b1").html(page - 2);
				$("#b1").attr("href",
						"javascript:changePage(" + (page - 2) + ")");
				$("#b2").html(page - 1);
				$("#b2").attr("href",
						"javascript:changePage(" + (page - 1) + ")");
				$("#b3").html(page);
				$("#b3").attr("href", "javascript:changePage(" + page + ")");
				$("#b4").html(page + 1);
				$("#b4").attr("href",
						"javascript:changePage(" + (page + 1) + ")");
				$("#b5").html(page + 2);
				$("#b5").attr("href",
						"javascript:changePage(" + (page + 2) + ")");
			}
		}
	});
}