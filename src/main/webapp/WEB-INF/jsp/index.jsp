<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Schedule to Reddit</title>
<link rel="shortcut icon" type="image/png" th:href="@{/resources/favicon.png}"/>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Schedule to Reddit</a>
    </div>
    
     <p class="navbar-text navbar-right">
        <a href="signup">Sign up</a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </p>
    
  </div><!-- /.container-fluid -->
</nav>
<div class="container">
<h1>Schedule to Reddit</h1>

<br/><br/>
<div class="alert alert-info" th:if="${param.containsKey('msg')}" th:text="${param.msg[0]}">

</div>
<div class="alert alert-danger" th:if="${param.containsKey('error')}">
Invalid username or password
</div>
<div class="alert alert-warning" th:if="${param.containsKey('invalidSession')}">
Session timeout
</div>
<form method="post" action="j_spring_security_check" class="form-horizontal col-sm-6">
  <div class="form-group">
    <label for="username" class="control-label col-sm-3">Username</label>
    <div class="col-sm-9"><input class="form-control" id="username" name="username" placeholder="Username"/></div>
  </div>
  <div class="form-group">
    <label for="password" class="control-label col-sm-3">Password</label>
   <div class="col-sm-9"> <input type="password" class="form-control" id="password" name="password" placeholder="Password"/></div>
  </div>  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <a href="forgetPassword">Forget Password?</a><button type="submit" class="btn btn-default pull-right">Login</button>
  <br/><br/>
  <br/><br/>
    <a href="signup" class="btn btn-primary pull-right">Sign up</a>
  
</form>
</div>		
</body>
</html>