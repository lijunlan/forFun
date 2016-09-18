/**
 * 
 */
var j16 = jQuery.noConflict();
j16(document).ready(function() {
	if (getCookie("mark") == null) {
		self.location = 'login.html';
		return;
	}
	setUpLoginLink();
});

// Convert divs to queue widgets when the DOM is ready
j16(function() {
	j16("#uploader").pluploadQueue({
		// General settings
		runtimes : 'html5,flash,silverlight,html4',
		url : '/IvyProService/upfile',
		max_file_size : '10mb',
		unique_names : true,
		multiple_queues : false,
		// 一次只能选择一个文件
		multi_selection : false,
		dragdrop : true,
		chunk_size : '10mb',
		// Specify what files to browse for
		filters : [ {
			title : "word",
			extensions : "doc,docx"
		} ],
		// Flash settings
		flash_swf_url : 'js/plupload/js/plupload.flash.swf',
		// Silverlight settings
		silverlight_xap_url : 'js/plupload/js/plupload.silverlight.xap'
	});
	var upl = j16("#uploader").pluploadQueue();
	upl.bind('FileUploaded', function(up, file, res) {
		try {
			var json = jQuery.parseJSON(res['response']);
			j16('#url').val(json['url']);
		} catch (e) {
			alert(e);
		}
		// alert(file.name + " uploaded!");
		// a
	});

	j16("#uploader").pluploadQueue();
	upl.bind('FilesAdded', function(up, files) {
		if (up.files.length > 1) {
			alert("您只能上传一个文件！");
			up.removeFile(files[0]);
		}
	});

	upl.bind('BeforeUpload', function(up, file) {
		// filename : getCookie("id");
	});

	j16('#form')
			.submit(
					function(e) {
						var uploader = $('#uploader').pluploadQueue();
						alert(uploader.files.length);
						if (uploader.files.length > 0
								&& uploader.files.length < 2) {
							// When all files are uploaded submit form
							uploader
									.bind(
											'StateChanged',
											function() {
												if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
													j16('form')[1].submit();
												}
											});
							uploader.start();
						} else if (uploader.files.length > 0) {
							alert("only one file is allowed!");
						} else {
							alert('you should choose one file!');
						}
						return false;
					});
});

function changeOrder() {

	var link = j16("#url").val();
	var id = j16("#orderId").val();
	if (link == "" || id == "") {
		alert("请将信息填写完整");
		return;
	}
	j16.ajax({
		cache : true,
		type : "POST",
		url : "/IvyProService/manage",
		data : "{'style':'order','method':'change','link':'" + link
				+ "','id':'" + id + "','mark':'" + getCookie("mark") + "'}",
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data, textStatu) {
			var json = eval("(" + data + ")");
			if (getValueFromJson(json, "state") == "success") {
				self.location = 'showorder.html';
			} else {
				alert(getValueFromJson(json, "msg"));
			}
		}
	});
}
