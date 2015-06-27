<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<link rel="stylesheet" th:href="@{/resources/datetime-picker.css}"/>
<link rel="stylesheet" th:href="@{/resources/autocomplete.css}"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script th:src="@{/resources/datetime-picker.js}"></script>
<script th:src="@{/resources/validator.js}"></script>
<style type="text/css">
.btn.disabled{
background-color: #ddd;
border-color: #ddd;
}

.btn.disabled:hover{
background-color: #ddd;
border-color: #ddd;
}
</style>
</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>Schedule Post to Reddit 
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
  Load from My Sites
</button>
</h1>
<form action="#" method="post" role="form" data-toggle="validator">
<div class="row">
<div class="form-group">
    <label class="col-sm-3">Title</label>
    <span class="col-sm-9"><input name="title" placeholder="title" class="form-control" required="required" data-minlength="3"/></span>
</div>
<br/><br/>
<div class="form-group">
    <label class="col-sm-3">Url</label>
    <span class="col-sm-9"><input name="url" type="url" placeholder="url" class="form-control" required="required" data-minlength="3"/></span>
</div>
<br/><br/>  
<div class="form-group">
    <label class="col-sm-3">Subreddit</label>
    <span class="col-sm-9"><input id="sr" name="subreddit" placeholder="Subreddit (e.g. kitten)"  class="form-control" required="required" data-minlength="3"/></span>
</div>
<br/><br/>
<div>
<label class="col-sm-3">Send replies to my inbox</label> 
 <span class="col-sm-9">
<input type="checkbox" name="sendReplies" value="true"/> </span> 
</div>
<br/><br/>
<div>
<span class="col-sm-2"><a href="#" class="btn btn-default" onclick="checkIfAlreadySubmitted()">Check if already submitted</a></span>
<span class="col-sm-1"></span>
<span class="col-sm-9"><span id="checkResult" class="alert alert-info" style="display:none"></span></span>
</div>
<br/><br/>
<hr/>

<div class="form-group">
    <label class="col-sm-3">Resubmit If:</label>
    
    <span class="col-sm-2">Votes didn't exceed </span>
    <span class="col-sm-1">
    <input type="number" class="form-control input-sm"  name="minScoreRequired"/>
    </span>
    
    <span class="col-sm-3">within &nbsp;&nbsp;
    <select name="timeInterval">
        <option value="0" >None</option>
        <option value="45" >45 minutes</option>
        <option value="60" >1 hour</option>
        <option value="120" >2 hours</option>
      </select>
    </span>
    
    <span class="col-sm-3">try resubmitting &nbsp;&nbsp;
    <select name="noOfAttempts">
        <option value="0" >No</option>
        <option value="2" >2</option>
        <option value="3" >3</option>
        <option value="4" >4</option>
        <option value="5" >5</option>
      </select>
      &nbsp;&nbsp; times.
    </span>
    
    
</div>
<br/>

<div class="form-group">
    <label class="col-sm-3">&nbsp;</label>
    
    <span class="col-sm-2">Minimum Upvote Ratio</span>
    <span class="col-sm-1">
    <input type="number" class="form-control input-sm" value="98" name="minUpvoteRatio" data-min="0" data-max="100"  required="required"/>
    </span>
    
    <span class="col-sm-3">keep If Has Comments &nbsp;&nbsp;    
    <input type="checkbox" name="keepIfHasComments" value="true"/>
    </span>
    
    <span class="col-sm-3">Delete If Consume Attempts &nbsp;&nbsp;    
    <input type="checkbox" name="deleteAfterLastAttempt" value="true"/>
    </span>
    
    
</div>
<br/><br/>
<br/><br/>

<div>
<label class="col-sm-3">Submission Date (<span sec:authentication="principal.preference.timezone">UTC</span>)</label>
<div class="col-sm-5"><input id="date" name="date" class="form-control" readonly="readonly"/></div><div class="col-sm-4"><a class="btn btn-default" onclick="togglePicker()" style="font-size:16px;padding:8px 12px"><i class="glyphicon glyphicon-calendar"></i></a></div>
    <script type="text/javascript">
    /*<![CDATA[*/
        $(function(){
            $('*[name=date]').appendDtpicker({"inline": true});
            $('*[name=date]').handleDtpicker('hide');
        });
        var toggle = "show";
        function togglePicker(){
        	$('*[name=date]').handleDtpicker(toggle);
        	toggle = toggle=="show"?"hide":"show";
        }
        /*]]>*/
    </script>
</div>
<br/><br/>

    
   <div class="col-sm-12"> <button id="submitBut" type="submit" class="btn btn-primary">Schedule</button></div>
   </div>
</form>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Load from My Sites</h4>
      </div>
      <div class="modal-body">
        <div class="dropdown col-sm-6">
		  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
		    Choose Site <span class="caret"></span>
		  </button>
		  <ul id="siteList" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
		  </ul>
		</div>
		
        <div class="dropdown col-sm-6">
          <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-expanded="true">
            Choose Article <span class="caret"></span>
          </button>
          <ul id="articleList" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2">
          </ul>
        </div>
        
        <br/>
        <br/>
       <button class="btn btn-primary" onclick="load()" data-dismiss="modal">Load</button>
      </div>
    </div>
  </div>
</div>




<script>
/*<![CDATA[*/
  $(function() {
    $( "#sr" ).autocomplete({
      source: "api/subredditAutoComplete"
    });
    
    $("input[name='url'],input[name='subreddit']").focus(function (){
        $("#checkResult").hide();
    });
    
    loadPref();
    
  });
  
  function loadPref(){
      $.get("user/preference", function (data){
          $.each(data, function(key, value) {
              if(value == true && key!= "id"){
                  $('*[name="'+key+'"]')[0].checked=true;
              }
              if(value != false){
                  $('*[name="'+key+'"]').val(value);
              }
          });
      });
  }
  
  $('#myModal').on('shown.bs.modal', function () {
	  if($("#siteList").children().length > 0)
		  return;
	  $.get("sites", function(data){
		 $.each(data, function( index, site ) {
			  $("#siteList").append('<li><a href="#" onclick="loadArticles('+site.id+',\''+site.name+'\')">'+site.name+'</a></li>')
		 });
	  });
  });
  
  
  function loadArticles(siteID,siteName){
	  $("#dropdownMenu1").html(siteName);
	  $.get("sites/articles?id="+siteID, function(data){
		  $("#articleList").html('');
		  $("#dropdownMenu2").html('Choose Article <span class="caret"></span>');
	      $.each(data, function( index, article ) {
	          $("#articleList").append('<li><a href="#" onclick="chooseArticle(\''+article.title+'\',\''+article.link+'\')"><b>'+article.title+'</b> <small>'+new Date(article.publishDate).toUTCString()+'</small></li>')
	      });
	 }).fail(function(error){
		 console.log(error);
		 alert(error.responseText);
	 });
  }
  
  var title = "";
  var link = "";
  
  function chooseArticle(selectedTitle,selectedLink){
	  $("#dropdownMenu2").html(selectedTitle);
	  title=selectedTitle;
	  link = selectedLink;
  }
  
  function load(){
      $("input[name='title']").val(title);
      $("input[name='url']").val(link);
  }
  /*]]>*/ 
</script>
  
<script>
/*<![CDATA[*/
function checkIfAlreadySubmitted(){
    var url = $("input[name='url']").val();
    var sr = $("input[name='subreddit']").val();
    console.log(url);
    if(url.length >2 && sr.length > 2){
        $.get("api/posts",{url: url, sr: sr}, function(data){
            var result = JSON.parse(data);
            if(result.length == 0){
                $("#checkResult").show().html("Not submitted before");
            }else{
                $("#checkResult").show().html('Already submitted <b><a target="_blank" href="http://www.reddit.com'+result[0].data.permalink+'">here</a></b>');
            }
        });
    }
    else{
        $("#checkResult").show().html("Too short url and/or subreddit");
    }
}           
/*]]>*/          
</script>

<script>
/*<![CDATA[*/
$("#submitBut").click(function(event) {
    event.preventDefault();
    schedulePost();
});

function schedulePost(){
    var data = {};
    $('form').serializeArray().map(function(x){data[x.name] = x.value;});
    console.log(JSON.stringify(data));
 $.ajax({
    url: 'api/scheduledPosts?date='+$("#date").val(),
    data: JSON.stringify(data),
    type: 'POST',
    contentType:'application/json',
    success: function(result) {
        window.location.href="scheduledPosts";
    },
    error: function(error) {
        alert(error.responseText);
    }   
}); 
}
/*]]>*/  
</script>
</body>
</html>