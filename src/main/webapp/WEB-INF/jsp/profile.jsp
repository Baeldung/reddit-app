<html>
<head>

<title>Schedule to Reddit</title>
<link rel="shortcut icon" type="image/png" th:href="@{/resources/favicon.png}"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<link rel="stylesheet" th:href="@{/resources/datetime-picker.css}" />
<link rel="stylesheet" th:href="@{/resources/autocomplete.css}"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
<script th:src="@{/resources/datetime-picker.js}"></script>
<script th:src="@{/resources/validator1.js}"></script>
<script th:src="@{/resources/timezones.full.js}"></script>
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
<h1>Profile Page</h1>
<br/><br/>
<form role="form" data-toggle="validator">
<div class="row">
<input type="hidden" name="id"/>

<div class="form-group">
    <label class="col-sm-3">Email</label>
    <span class="col-sm-9">
    <input name="email" type="email" placeholder="your email" class="form-control" required="required"/>
    <span class="help-block">Provide your email to receive notifications when your scheduled posts are submitted</span>
    </span>
</div>

<div class="col-sm-12"><a href="changePassword" class="btn btn-primary">Change Password</a></div>
<br/><br/> 
<hr/> 
<br/><br/> 

<h1>Edit Default Preferences</h1>
<br/><br/> 
<div class="form-group">
    <label class="col-sm-3">Timezone</label>
    <span class="col-sm-9"><select id="timezone" name="timezone" class="form-control"></select></span>
</div>

<br/><br/> 
<div class="form-group">
    <label class="col-sm-3">Default Subreddit</label>
    <span class="col-sm-9"><input id="sr" name="subreddit" placeholder="Subreddit" class="form-control" /></span>
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
    <div>
        <h2 class="col-sm-12">Default Resubmit Options</h2>
    </div>
    <br/><br/><br/><br/>
    <div th:include="resubmit"/>
    
    <div class="col-sm-12"><button id="submitBut" type="submit" class="btn btn-primary">Save Changes</button></div>
   </div>
</form>

<br/>
<a th:if="${#authentication.name == null}" th:href="/user/authReddit" class="btn btn-primary">Authenticate Reddit</a>

</div>
</body>
<script>
/*<![CDATA[*/

  $(function() {
    $( "#sr" ).autocomplete({
      source: "api/subredditAutoComplete"
    });
    
    loadPref();
    
    $('#timezone').timezones();

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
    	showAlertMessage(error.responseText);
    }); 
}
/*]]>*/  
</script>
</html>