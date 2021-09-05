<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Login | Register</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous" />
</head>

<body>
	<div class="container">

		<br>
		<h1>Login:</h1>
		<br>

		<form method="POST" action="http://localhost:8080/coursework/do/login">
			Username: <input type="text" name="username" value="" class="btn btn-light"/> ----
			Password: <input type="password" name="password" value="" class="btn btn-light"/>
			<input type="submit" value="Log in" class="btn btn-primary"/>
		</form>

		<br>
		<h1>Register:</h1>
		<br>
		<form method="POST" action="http://localhost:8080/coursework/do/register">
			Username: <input type="text" name="username" value="" class="btn btn-light"/> ----
			Password: <input type="password" name="password" value="" class="btn btn-light"/>
			<input type="submit" value="Register" class="btn btn-primary"/>
		</form>

	</div>
</body>
</html>
