<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script th:src="@{/resources/validator1.js}"></script>
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
<h1>Add new Site</h1>
<br/>
<form th:action="@{/sites}" method="post" role="form" data-toggle="validator">
<div class="row">
<div class="form-group">
    <label class="col-sm-3">Site Name</label>
    <span class="col-sm-9"><input name="name" placeholder="name" class="form-control" required="required" data-minlength="3"/></span>
</div>
<br/><br/>
<div class="form-group">
    <label class="col-sm-3">Feed Url</label>
    <span class="col-sm-9"><input id="url" name="url" type="url" placeholder="e.g. (http://www.baeldung.com/feed/)" class="form-control" required="required" data-minlength="3"/></span>
</div>
<br/><br/>  

<br/><br/>
    <button type="submit" id="submitBut" class="btn btn-primary">Add Site</button>
</div>
</form>
</div>
<script>
$("#submitBut").click(function(event) {
	event.preventDefault();
	addSite();
});

function addSite(){
	 var data = {};
	    $('form').serializeArray().map(function(x){data[x.name] = x.value;});
	    console.log(JSON.stringify(data));
	 $.ajax({
	    url: 'sites',
	    data: JSON.stringify(data),
	    type: 'POST',
	    contentType:'application/json',
	    success: function(result) {
	        window.location.href="mysites";
	    },
	    error: function(error) {
	        alert(error.responseText);
	    }   
	 }); 
}
</script>
</body>
</html>