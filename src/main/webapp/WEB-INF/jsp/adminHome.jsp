<html>
<head>

<title>Schedule to Reddit</title>
<link rel="shortcut icon" type="image/png" th:href="@{/resources/favicon.png}"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>

</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" th:href="@{/adminHome}">Schedule to Reddit</a>
    </div>
    
     <p class="navbar-text navbar-right">Logged in as 
        <b><span sec:authentication="principal.username">Bob</span></b>&nbsp;&nbsp;&nbsp;
        <a th:href="@{/logout}">Logout</a>&nbsp;&nbsp;&nbsp;
    </p>
  </div><!-- /.container-fluid -->
</nav>
<div class="container">
        <h1>Welcome, <small><span sec:authentication="principal.username">Bob</span></small></h1>
        <br/>
        <a href="users" class="btn btn-primary">Display Users List</a>

        <br/><br/>
        
        <a href="./" class="btn btn-primary">Go to Scheduling Home</a>
</div>
</body>
</html>