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
    <tr th:each="post  : ${posts.getContent()}">
        <td th:text="${post.getTitle()}"></td>
        <td th:text="${post.getSubmissionDate()}"></td>
        <td th:text="${post.getSubmissionResponse()}"></td>
        <td th:if="${post.getNoOfAttempts() > 0}" th:text="${post.getNoOfAttempts()}"></td>
        <td th:unless="${post.getNoOfAttempts() > 0}">-</td>
        <td>
            <a th:href="@{/editPost/{id}(id=${post.getId()})}" class="btn btn-warning" >Edit</a>
            <a href="#" class="btn btn-danger" th:onclick="'javascript:confirmDelete(\'' +${post.getId()}+ '\') '">Delete</a>
        </td>
    </tr>
</table>

<br/>

<nav th:if="${posts.getTotalPages() > 1 }">
    <ul class='pagination'>
      <li class="active">
        <a href="#" onclick="loadPage(1)">1</a>
      </li>
      <li th:each="i : ${#numbers.sequence( 2, posts.getTotalPages())}" >
        <a href="#" th:onclick="'javascript:loadPage(\'' +${i}+ '\') '"  th:text="${i}">1</a>
      </li>
    </ul>
</nav>
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

function loadPage(page){
	$('.pagination').children().removeClass('active');
	$('.pagination').children().eq(page-1).addClass('active');
	$('table').children().not(':first').remove();
	var attempt = '-';
	$.get("api/scheduledPosts?page="+(page-1), function(data){
		$.each(data, function( index, post ) {
		     attempt = post.noOfAttempts<1? '-':post.noOfAttempts;
			$('.table').append('<tr><td>'+post.title+'</td><td>'+
					post.submissionDate+'</td><td>'+post.submissionResponse+'</td><td>'+
					attempt+'</td><td> <a class="btn btn-warning" href="/reddit-scheduler/editPost/'+post.id+
					'">Edit</a> <a href="#" class="btn btn-danger" onclick="confirmDelete('+post.id
							+') ">Delete</a> </td></tr>');
		});
	});
}

/*]]>*/
</script>
</body>
</html>