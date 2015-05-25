<div>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" th:href="@{/home.html}">Schedule to Reddit</a>
    </div>
    
     <p class="navbar-text navbar-right">Logged in as 
        <b><span sec:authentication="principal.username">Bob</span></b>&nbsp;&nbsp;&nbsp;
        <a th:href="@{/logout}">Logout</a>&nbsp;&nbsp;&nbsp;
    </p>
    
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a th:href="@{/mysites}">My Sites</a></li>
        <li><a th:href="@{/posts}">My Scheduled Posts</a></li>
        <li><a th:href="@{/post}">Post to Reddit</a></li>
        <li><a th:href="@{/postSchedule}">Schedule Post to Reddit</a></li>
      </ul>
      
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div id="loading-image" style="position:absolute;left:45%;top:45%;display:none;z-index:2000;">
<img th:src="@{/resources/spin.gif}"/>
</div>
<script>
$(document).ajaxStart(function() {
    $("#loading-image").show();
});
$(document).ajaxComplete(function() {
	$("#loading-image").hide();
});
</script>
</div>