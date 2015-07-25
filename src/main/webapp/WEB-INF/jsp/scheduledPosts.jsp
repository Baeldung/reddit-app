<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/plug-ins/1.10.7/integration/bootstrap/3/dataTables.bootstrap.css"/>
<script th:src="@{/resources/moment.min.js}"></script>
<script th:src="@{/resources/moment-timezone-with-data.js}"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/plug-ins/1.10.7/integration/bootstrap/3/dataTables.bootstrap.js"></script>

</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>My Scheduled Posts</h1>
<table class="table table-bordered">
<thead>
<tr>
<th>Post title</th>
<th>Submission Date (<span id="timezone" sec:authentication="principal.user.preference.timezone">UTC</span>)</th>
<th>Status</th>
<th>Resubmit Attempts left</th>
<th>Actions</th>
</tr>
</thead>
    
</table>

</div>
<script th:inline="javascript">
/*<![CDATA[*/
           
$(document).ready(function() {
    $('table').dataTable( {
        "processing": true,
        "searching":false,
        "lengthMenu": [ 2, 5, 10, 20, 50 ],
        "columnDefs": [
                       { "name": "title",   "targets": 0 },
                       { "name": "submissionDate",  "targets": 1 },
                       { "name": "submissionResponse", "targets": 2 },
                       { "name": "noOfAttempts",  "targets": 3 },
                       { "targets": 4, "data": "id",
                    	    "render": function ( data, type, full, meta ) {
                    	        return '<a class="btn btn-warning" href="editPost/'+data+
                                '">Edit</a> <a href="#" class="btn btn-danger" onclick="confirmDelete('+data
                                +') ">Delete</a>';
                    	      }}
                     ],
                     "columns": [
                                 { "data": "title" },
                                 { "data": "submissionDate" },
                                 { "data": "submissionResponse" },
                                 { "data": "noOfAttempts" }
                             ],
        "serverSide": true,
        "ajax": "api/scheduledPosts"
    } );
} );

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