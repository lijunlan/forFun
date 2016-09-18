/**
 * 
 */

$(document).ready(function() {
	var $unfoldBt = $(".unfoldBt");
	$unfoldBt.click(function() {
		$(this).toggleClass("click");
		if ($(this).hasClass("click")) {
			$(this).text("展开");
		} else {
			$(this).text("收起");
		}
		$(this).parent().next().slideToggle("slow", "easeOutExpo");
	});
	$("#closeFloat").click(function() {
		$(this).parent().hide();

	});
	setUpLoginLink();
});
