<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Test Page</title>
</head>
<body>
	<form id="form1" action="/AppFactoryService/AppUpLoadService"
		method="post">
		<div class="form-group">
			<label>内容</label><input name="message" id="exampleInputEmail1" />
		</div>
		<div class="form-group">
			<label>类型</label><input name="type" id="exampleInputPassword1" />
		</div>
	</form>
	<button onclick="getMessage()">GET BY LAST ID</button>
	<button onclick="test()">Submit</button>
	<button onclick="down()">Get</button>
	<button onclick="comment()">UpLoadComment</button>
	<button onclick="downcomment()">getComment</button>
	<button onclick="getuserall()">getUserAll</button>
	<button onclick="getuseroffline()">getUserOnline</button>
	<button onclick="deletepassage()">deletePassage</button>
	<button onclick="deletecomment()">deleteComment</button>
	<button onclick="login()">login</button>
	<button onclick="register()">register</button>
	<button onclick="changeUserInfo()">changeUserInfo</button>
	<button onclick="praise()">praise</button>
	<button onclick="checkPraise()">checkPraise</button>
	<button onclick="delPraise()">delPraise</button>
	<button onclick="sendMsg()">sendMsg</button>
	<button onclick="getMsg()">getMsg</button>
	<button onclick="delMsg()">delMsg</button>
	<button onclick="delManyMsg()">delManyMsg</button>
	<button onclick="getRecentMsg()">getRecentMsg</button>
	<button onclick="getIpInfo()">IpInfo</button>
	<script src="js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">
		function getIpInfo() {
			$.ajax({
				cache : true,
				type : "POST",
				url : "/AppFactoryService/down",
				data : "{'style':'user','method':'get_all_user_analyze_info'}",
				async : false,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data, textStatu) {
					var dataObj = eval("(" + data + ")");
					alert(dataObj.state);
				}
			});
		}
		function delManyMsg() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'message','method':'delete_many_msg','own_user_id':'8','another_user_id':'13'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function getRecentMsg() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'message','method':'get_recent_list','user_id':'4','page':'1','formulate_time':'true'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function delPraise() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'cancel_praise','user_id':'8','passage_id':'62','type':'passage'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function sendMsg() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/up",
						data : "{'style':'message','method':'upload_msg','from_user_id':'4','to_user_id':'5','content':'我在说话啦啦啦啦啦啦啦'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function delMsg() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'message','method':'delete_msg','from_user_id':'4','to_user_id':'5','message_id':'1'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function getMsg() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'message','method':'get_msg_list','own_user_id':'4','other_user_id':'5','refresh_time':'1409660272875','formulate_time':'true'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function checkPraise() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'check_praise','user_id':'8','type':'passage','passage_id':'62'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function praise() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'praise','user_id':'8','type':'passage','passage_id':'62'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function changeUserInfo() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'change_info','user_id':'4','age':'12','introduce':'oewjofjefooofjwo金额非问哦Joe我','email':'345693031@qq.com'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function getMessage() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'news','method':'get_message_list','last_passage_id':'6','formulate_time':'true'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function register() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'register','username':'123456','password':'123456','email':'123','name':'999'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function login() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'login','username':'123456','password':'123456'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function deletecomment() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'comment','method':'delete_comment','comment_id':'1','user_id':'1','passage_id':'1'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function deletepassage() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'news','method':'delete_message','passage_id':'1'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function getuseroffline() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'get_user_list','page':'1','user_type':'online'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function getuserall() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'user','method':'get_user_list','page':'1','user_type':'all'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function downcomment() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'comment','method':'get_comment_list','page':'1','id_type':'passage','formulate_time':'true','passage_id':'1','user_id':'1'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function comment() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/up",
						data : "{'style':'comment','method':'upload_comment','content':'"
								+ $(exampleInputEmail1).val()
								+ "','passage_id':'1','user_id':'1'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(item, value) {
								alert(value);
							});
						}
					});
		}
		function down() {
			$
					.ajax({
						cache : true,
						type : "POST",
						url : "/AppFactoryService/down",
						data : "{'style':'news','method':'get_message_list','page':'1','formulate_time':'true'}",
						async : false,
						error : function(request) {
							alert("Connection error");
						},
						success : function(data, textStatu) {
							var dataObj = eval("(" + data + ")");
							$.each(dataObj, function(index, value) {
								alert(index + ":" + value.message);
							});
						}
					});
		}
		function test() {
			$.ajax({
				cache : true,
				type : "POST",
				url : "/AppFactoryService/up",
				data : "{'style':'news','method':'upload_message','message':'"
						+ $(exampleInputEmail1).val() + "'}",
				async : false,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data, textStatu) {
					var dataObj = eval("(" + data + ")");
					$.each(dataObj, function(item, value) {
						alert(value);
					});
				}
			});
		}
	</script>
</body>
</html>

