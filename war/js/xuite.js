$(document).ready(function() {
	var apiKey = $("#apiKey"), secretKey = $("#secretKey"), oAuth = $("#oAuth"), form = $('#form1'), message = $("#message"),content = $("#content");

	$("#sendToOauth").click(function() {

		if ($.trim(oAuth.val())) {
			return alert("The oAuth has value already.");
		}
		if (apiKey.val()) {
			form.attr("action", "http://my.xuite.net/service/account/authorize.php?response_type=token&client_id=" + apiKey.val() + "&redirect_uri=http://"+window.location.host+"/xuite/getoauth&scope=read write&state=&invoke_call=")
			form.submit();
		} else {
			alert("Please key in API-KEY");
		}
	})
	$("#send").click(function() {
		if (apiKey.val() && secretKey.val() && oAuth.val()) {

			message.animate({
				opacity : 0.25,
				left : '50',
				height : '20'
			}, 1000, function() {
				$(this).text("It's getting File list.Please wait");
				$.ajax({
					type : "GET",
					url : "../xuite/add",
					data : {
						apiKey : apiKey.val(),
						secretKey : secretKey.val(),
						oAuth : oAuth.val()
					},
					success : function(msg) {
						content.val(msg);
						$("#message").text('');
					},
					error : function(msg) {
						alert(msg);
					}
				});
			});

		} else {
			alert("Please Key in all columns.");
		}
	})

	$("#getOldFileList").click(function() {
		if (apiKey.val()) {
			$(this).text("It's getting old File list.Please wait");
			$.ajax({
				type : "GET",
				url : "../xuite/getoldfile",
				data : {
					apiKey : apiKey.val()
				},
				success : function(rep) {
					var rep = eval('(' + rep+ ')');
					if(rep.msg){
						return alert(rep.msg);
					}					
					secretKey.val(rep.secretKey);
					oAuth.val(rep.oAuth);
					content.val(rep.content);				
				},
				error : function(msg) {
					alert(msg);
				}
			});
		} else {
			alert("Please key in API-KEY");
		}
	})
});
