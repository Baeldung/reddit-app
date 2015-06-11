<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>

</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>My Scheduled Posts</h1>
<table class="table table-bordered">
<thead>
<tr>
<th>Post title</th>
<th>Submission Date (<span th:text="${#dates.format(#calendars.createToday(), 'z')}">UTC</span>)</th>
<th>Status</th>
<th>Resubmit Attempts left</th>
<th>Actions</th>
</tr>
</thead>
    
</table>

<br/>
  <button id="prev" type="button" class="btn btn-default" onclick="loadPrev()">Previous</button>
  <button id="next" type="button" class="btn btn-default" onclick="loadNext()">Next</button>

</div>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script>
/*<![CDATA[*/
function confirmDelete(id) {
    if (confirm("Do you really want to delete this post?") == true) {
    	deletePost(id);
    } 
}

function deletePost(id){
	$.ajax({
	    url: 'api/scheduledPosts/'+id,
	    type: 'DELETE',
	    success: function(result) {
	    	window.location.href="scheduledPosts"
	    }
	});
}

var currentPage = 0;

$(function(){
	loadPage(0);
});

function loadNext(){
	loadPage(currentPage+1);
	$("#next").blur();
}

function loadPrev(){
    loadPage(currentPage-1);
    $("#prev").blur();
}

function loadPage(page){
	currentPage = page;
	$('table').children().not(':first').remove();
	if(page == 0){
		$('#prev').hide();
	}else{
		$('#prev').show();
	}
	var attempt = '-';
	$.get("api/scheduledPosts?page="+page, function(data){
		$.each(data, function( index, post ) {
		     attempt = post.noOfAttempts<1? '-':post.noOfAttempts;
			$('.table').append('<tr><td>'+post.title+'</td><td>'+
					post.submissionDate+'</td><td>'+post.submissionResponse+'</td><td>'+
					attempt+'</td><td> <a class="btn btn-warning" href="/reddit-scheduler/editPost/'+post.id+
					'">Edit</a> <a href="#" class="btn btn-danger" onclick="confirmDelete('+post.id
							+') ">Delete</a> </td></tr>');
		});
		if(data.length == 0){
			$('#next').hide();
		}else{
			$('#next').show();
		}
	});
}

/*]]>*/
</script>
</body>
</html>