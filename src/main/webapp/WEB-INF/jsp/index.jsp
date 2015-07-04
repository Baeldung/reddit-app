<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Schedule to Reddit</title>
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
<div class="alert alert-danger" th:if="${param.containsKey('error')}">
Invalid username or password
</div>
<form class="form-inline" method="post" action="j_spring_security_check">
  <div class="form-group">
    <label class="sr-only" for="username">Username</label>
    <input class="form-control" id="username" name="username" placeholder="Username"/>
  </div>
  <div class="form-group">
    <label class="sr-only" for="password">Password</label>
    <input type="password" class="form-control" id="password" name="password" placeholder="Password"/>
  </div>
  
  <button type="submit" class="btn btn-default">Login</button>
</form>
<br/><br/>
<a href="signup" class="btn btn-primary">Sign up</a>
<br/><br/>
<a href="redditLogin" class="btn btn-primary">Login with Reddit</a>
</div>		
</body>
</html>