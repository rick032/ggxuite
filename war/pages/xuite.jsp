<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>fill Xuite account</title>
		<link href="../css/south-street/jquery-ui-1.10.1.custom.css" rel="stylesheet">
		<script src="../js/jquery-1.9.1.js"></script>
		<script src="../js/jquery-ui-1.10.1.custom.js"></script>
	</head>
	<script>
		$(document).ready(function() {
			var apiKey = $("#apiKey"), secretKey = $("#secretKey"), form = $('#form1');
			$("#send").click(function() {
				if (apiKey.val() && secretKey.val()) {
					//form.attr("action", "http://my.xuite.net/service/account/authorize.php?response_type=token&client_id=" + apiKey.val() + "&redirect_uri=rick032.no-ip.org:80/xuite/add&scope=read write&state=&invoke_call=")
					//form.submit();

					$.ajax({
						type : "GET",
						url : "../xuite/add",
						data : {
							apiKey : apiKey.val(),
							secretKey : secretKey.val(),
							oAuth : $("#oAuth").val()
						},
						success : function(msg) {
							$("#message").html(msg);
						},
						error : function(msg) {
							alert(msg);
						}
					});
				}
			})
		});
	</script>
	<body>
		<h1>Xuite !</h1>
		<!--<form id="form1" action="" target="_blank" method="post">-->
			<div>
				<table>
					<tr>
						<td colspan="2" style="font-weight: bold;">Please input these columns.</td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><label for="API-KEY">API-KEY:</label></td>
						<td>
						<input type="text" id="apiKey" name="apiKey" value="207aadf5f03e3e5f232094e2b00664bd"/>
						</td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><label for="SECRET-KEY">SECRET-KEY:</label></td>
						<td>
						<input type="text" id="secretKey" name="secretKey" value="6339926682" />
						</td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><label for="oAuth">oAuth:</label></td>
						<td>
						<input type="text" id="oAuth" name="oAuth" value="XOA12a741cd5799882265a3db22d504c375"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<button id="send" name="send">
							取得OAUTH
						</button></td>
					</tr>
				</table>
				<div id="message" name="message"></div>
			</div>
		<!--</form>-->
	</body>
</html>
