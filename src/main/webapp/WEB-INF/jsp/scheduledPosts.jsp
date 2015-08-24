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
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

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

<div class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Detailed Status</h4>
      </div>
      <div class="modal-body">
        
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</div>
<script th:inline="javascript">
/*<![CDATA[*/
           
$(document).ready(function() {
    $('table').dataTable( {
        "processing": true,
        "searching":false,
        "columnDefs": [
                       { "name": "title",   "targets": 0},
                       { "name": "date",  "targets": 1 },
                       { "targets": 2, "data": "status","width":"20%","orderable": false,
                           "render": function ( data, type, full, meta ) {
                               return data+' <a href="#" onclick="showDetailedStatus(\''+full.detailedStatus+'\' )">More Details</a>';
                             }
                       },
                       { "name": "noOfAttempts",  "targets": 3},
                       { "targets": 4, "data": "id",
                    	    "render": function ( data, type, full, meta ) {
                    	        return '<a class="btn btn-warning" href="editPost/'+data+
                                '">Edit</a> <a href="#" class="btn btn-danger" onclick="confirmDelete('+data
                                +') ">Delete</a>';
                    	      }}
                     ],
                     "columns": [
                                 { "data": "title" },
                                 { "data": "date" },
                                 { "data": "status" },
                                 { "data": "noOfAttempts" }
                             ],
        "serverSide": true,
        "ajax": function(data, callback, settings) {
            $.get('api/scheduledPosts', {
                size: data.length,
                page: (data.start/data.length),
                sortDir: data.order[0].dir,
                sort: data.columns[data.order[0].column].name
            }, function(res,textStatus, request) {
            	var pagingInfo = request.getResponseHeader('PAGING_INFO');
            	var total = pagingInfo.split(",")[0].split("=")[1];
                callback({
                    recordsTotal: total,
                    recordsFiltered: total,
                    data: res
                });
            });
        }
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

function showDetailedStatus(detailedStatus){
	$(".modal-body").html(detailedStatus.replace(/---/g, '<br/>'));
	$('.modal').modal();
}

/*]]>*/
</script>
</body>
</html>