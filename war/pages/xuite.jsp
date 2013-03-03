<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		
		<meta http-equiv="content-language" content="zh-tw">
		<title>Xuite To SMG By Rick032!</title>		
		<script src="../js/jquery-1.9.1.min.js"></script>	
		<link rel="stylesheet" href="../css/xuite.css">
		<script src="../js/xuite.js" ></script>
	</head>	
	<body>
		<table style="position:relative;text-align: left; width: 946px; height: 32px;" border="0" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td style="width: 250px; text-align: center; vertical-align: top;float: left;"> </td>
					<td style="vertical-align: top; width: 690px; text-align: left;float: right;"><h1>Xuite To SMG By Rick032!</h1>
					<form id="form1" target="_blank" method="post">
						<div>
							<table>
								<tr>
									<td colspan="2" style="font-weight: bold;"><a href="../pages/teach.html" target="_blank">教學</a></td>
								</tr>
								<tr>
									<td colspan="2" style="font-weight: bold;">Please input these columns.</td>
								</tr>
								<tr>
									<td style="font-weight: bold;"><label for="API-KEY">API-KEY:</label></td>
									<td>
									<input type="text" id="apiKey" name="apiKey" value=""/><input type="button" id="getOldFileList" name="getOldFileList" value="Get old File List"/>
									</td>
								</tr>
								<tr>
									<td style="font-weight: bold;"><label for="SECRET-KEY">SECRET-KEY:</label></td>
									<td>
									<input type="text" id="secretKey" name="secretKey" value="" />
									</td>
								</tr>
								<tr>
									<td style="font-weight: bold;"><label for="oAuth">oAuth:</label></td>
									<td>
									<input type="text" id="oAuth" name="oAuth" value="${oAuth}"/>
									<input type="button" id="sendToOauth" name="sendToOauth" value="Get oAuth"/>
									</td>
								</tr>
								<tr>
									<td colspan="2">
									<input type="button" id="send" name="send" value="Get File List" />
									</td>
								</tr>
							</table>
							<div class="msg" id="message" name="message">

							</div>
							<textarea name="content" id="content" rows="20" cols="100">
			
							</textarea>
						</div>
					</form>
					<address>
						Author: Rick032 - <a href="mailto:rick032@msn.com?subject=Xuite to SMG">email to me</a>
					</address></td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
