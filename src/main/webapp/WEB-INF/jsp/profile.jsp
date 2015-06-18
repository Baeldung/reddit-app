<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<link rel="stylesheet" th:href="@{/resources/datetime-picker.css}" />
<link rel="stylesheet" th:href="@{/resources/autocomplete.css}"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
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
<h1>Edit Default Preferences</h1>
<br/><br/>
<form role="form" data-toggle="validator">
<div class="row">
<input type="hidden" name="id"/>

<div class="form-group">
    <label class="col-sm-3">Email</label>
    <span class="col-sm-9">
    <input name="email" type="email" placeholder="your email" class="form-control" required="required" data-minlength="10"/>
    <span class="help-block">Provide your email to receive notifications when your scheduled posts are submitted</span>
    </span>
</div>

<br/><br/>  
<br/><br/>  
<div class="form-group">
    <label class="col-sm-3">Default Subreddit</label>
    <span class="col-sm-9"><input id="sr" name="subreddit" placeholder="Subreddit" class="form-control" required="required" data-minlength="3"/></span>
</div>
<br/><br/>
<div>
<label  class="col-sm-3">Send replies to my inbox</label>  
<span class="col-sm-9"> 
<input type="checkbox" name="sendReplies" value="true"/>
</span> 
</div>
<br/><br/>
<hr/>
<br/>
<div class="form-group">
    <label class="col-sm-3">Default Resubmit Options:</label>
    
    <span class="col-sm-2">Votes didn't exceed </span>
    <span class="col-sm-1">
    <input type="number" class="form-control input-sm"  name="minScoreRequired" required="required"/>
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
    <input  type="checkbox" name="deleteAfterLastAttempt" value="true"/>
    </span>
    
    
</div>
    
    <div class="col-sm-12"><button id="submitBut" type="submit" class="btn btn-primary">Save Changes</button></div>
   </div>
</form>
</div>
</body>
<script>
/*<![CDATA[*/

  $(function() {
    $( "#sr" ).autocomplete({
      source: "api/subredditAutoComplete"
    });
    loadPref();
  });
  
  function loadPref(){
	  $.get("user/preference", function (data){
		  $.each(data, function(key, value) {
			  if(value == true){
				  $('*[name="'+key+'"]')[0].checked=true;
			  }
			  if(value != false){
		          $('*[name="'+key+'"]').val(value);
			  }
		  });
		  console.log($('*[name="timeInterval"]').val());
	  });
  }
  /*]]>*/  
</script>

<script>
/*<![CDATA[*/
$("#submitBut").click(function(event) {
    event.preventDefault();
    editPref();
});

function editPref(){
    var data = {};
	$('form').serializeArray().map(function(x){data[x.name] = x.value;});
    console.log(JSON.stringify(data));
	$.ajax({
        url: "user/preference/"+$('input[name="id"]').val(),
        data: JSON.stringify(data),
        type: 'PUT',
        contentType:'application/json'
        	
    }).done(function() {
        window.location.href = "./";
    })
    .fail(function(error) {
    	alert(error.responseText);
    }); 
}
/*]]>*/  
</script>
</html>