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
<th>Submission Date</th>
<th>Status</th>
<th>Resubmit Attempts left</th>
<th>Actions</th>
</tr>
</thead>
    <tr th:each="post  : ${posts.getContent()}">
        <td th:text="${post.getTitle()}"></td>
        <td th:text="${#calendars.format(post.getSubmissionDate(),'dd MMMM yyyy  HH:mm z')}"></td>
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
      <li th:each="i : ${#numbers.sequence( 1, posts.getTotalPages())}" th:class="${i-1 == posts.getNumber()}? active : none">
        <a href="#" th:if="${i-1 == posts.getNumber()}" th:text="${i}">1</a>
        <a th:unless="${i-1 == posts.getNumber()}" th:href="@{scheduledPosts(page=${i},size=${posts.getSize()})}"><span th:text="${i}">1</span></a>
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
/*]]>*/
</script>
</body>
</html>