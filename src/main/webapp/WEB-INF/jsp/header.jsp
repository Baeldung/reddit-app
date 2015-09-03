<div>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" th:href="@{/}">Schedule to Reddit</a>
    </div>
    
     <p class="navbar-text navbar-right">Logged in as 
        <b><a th:href="@{/profile}" sec:authentication="principal.username">Bob</a></b>&nbsp;&nbsp;&nbsp;
        <a th:href="@{/logout}">Logout</a>&nbsp;&nbsp;&nbsp;
    </p>
    
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a th:href="@{/mysites}">My RSS Feeds</a></li>
        <li><a th:href="@{/scheduledPosts}">My Scheduled Posts</a></li>
        <li><a th:href="@{/post}">Post to Reddit</a></li>
        <li><a th:href="@{/postSchedule}">Schedule Post to Reddit</a></li>
      </ul>
      
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div id="errorAlert" class="alert alert-danger" style="display:none;margin:10px;">
<button type="button" class="close" data-dismiss="alert" aria-label="Close">
  <span aria-hidden="true">x</span>
</button>
<span id="alertContent"></span>
</div>

<div id="loading-layer" style="position:absolute;left:0;top:0;width:100%;height:100%;background-color:rgba(230,230,230,0.5);display:none;z-index:2000;">
<div id="loading-image" style="position:absolute;left:45%;top:45%;">
<img th:src="@{/resources/spin.gif}"/>
</div>
</div>
<script>
$(document).ajaxStart(function() {
    $("#loading-layer").show();
    $("#errorAlert").hide();
    $(".btn").attr("disabled", true);
});
$(document).ajaxComplete(function() {
	$("#loading-layer").hide();
    $(".btn").removeAttr("disabled");
});

function showAlertMessage(msg){
	$("#alertContent").html(msg);
	$("#errorAlert").show();
}
</script>
</div>