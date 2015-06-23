<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<script th:src="@{/resources/moment.min.js}"></script>
<script th:src="@{/resources/moment-timezone-with-data.js}"></script>
</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>My Scheduled Posts</h1>
<table class="table table-bordered">
<thead>
<tr>
<th>Post title</th>
<th>Submission Date (<span id="timezone" sec:authentication="principal.preference.timezone">UTC</span>)</th>
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
<script th:inline="javascript">
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
					convertDate(post.submissionDate)+'</td><td>'+post.submissionResponse+'</td><td>'+
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

function convertDate(date){
	var serverTimezone = [[${#dates.format(#calendars.createToday(), 'z')}]];
    var serverDate = moment.tz(date, serverTimezone);
	var clientDate = serverDate.clone().tz($("#timezone").html());
	var myformat = "YYYY-MM-DD HH:mm";
	return clientDate.format(myformat);
}

/*]]>*/
</script>
</body>
</html>