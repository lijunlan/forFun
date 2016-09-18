/**
 * 
 */

var page = 0;

$(document).ready(function() {
	if (getCookie("managerId") == null) {
		self.location = 'managelogin.html';
		return;
	}
	getPage();
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

function endOrder(orderId) {
	$
			.ajax({
				cache : true,
				type : "POST",
				url : "/IvyProService/manage",
				data : "{'style':'order','method':'order_done','id':'"
						+ orderId + "'}",
				async : false,
				error : function(request) {
					// alert("Connection error");
				},
				success : function(data, textStatu) {
					self.location = 'manager.html?page=' + page;
				}
			});
}

function getOrders() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/IvyProService/manage",
		data : "{'style':'order','method':'get_list','page':'" + page + "'}",
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
						+ "<td>" + value.id + "</td>" + "<td>"
						+ value.userEmail + "</td>" + "<td>" + value.category
						+ "</td>" + "<td>" + value.title + "</td>" + "<td>"
						+ value.request + "</td>" + "<td>" + value.createDate
						+ "</td>" + "<td><a href='" + value.link
						+ "'>下载文书</a></td>" + "<td>" + value.isEnd + "</td>";
				if (value.isEnd == "完成") {
					inhtml = inhtml + "<td>完成</td>" + "</tr>";
				} else if (value.isEnd == "处理中") {
					inhtml = inhtml + "<td><a href='javascript:endOrder("
							+ value.id + ")'>结束订单</a></td>" + "</tr>";
				} else {
					+"<td><a href='#'>出错</a></td>" + "</tr>";
				}
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