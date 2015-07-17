<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>

</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>All Users</h1>
<br/>
<br/>
<table class="table table-bordered">
<thead>
<tr>
<th>Username</th>
<th>Roles</th>
<th>Actions</th>
</tr>
</thead>

</table>
</div>
<div class="modal fade" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Modify User Roles</h4>
      </div>
      <div class="modal-body">
        <input type="hidden" name="id" id="userId"/>
        <div id="allRoles"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" onclick="modifyUserRoles()">Save changes</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<script>
/*<![CDATA[*/
           
$(function(){
	var userRoles="";
	$.get("user", function(data){
        $.each(data, function( index, user ) {
        	userRoles = extractRolesName(user.roles);
            $('.table').append('<tr><td>'+user.username+'</td><td>'+userRoles+
              '</td><td><a href="#" class="btn btn-warning" onclick="showEditModal('+user.id+',\''+userRoles+'\')">Modify User Roles</a> </td></tr>');
        });
    });
});

function extractRolesName(roles){
	var result ="";
	$.each(roles, function( index, role ) {
        result+= role.name+" ";
    });
	return result;
}

function showEditModal(userId, roleNames){
	$("#userId").val(userId);
	$.get("user/roles", function(data){
        $.each(data, function( index, role ) {
        	if(roleNames.indexOf(role.name) != -1){
                $('#allRoles').append('<input type="checkbox" name="roleIds" value="'+role.id+'" checked/> '+role.name+'<br/>')
        	} else{
        		$('#allRoles').append('<input type="checkbox" name="roleIds" value="'+role.id+'" /> '+role.name+'<br/>')
        	}
        });
        $("#myModal").modal();
    });
}

function modifyUserRoles(){
	var roles = [];
    $.each($("input[name='roleIds']:checked"), function(){            
    	roles.push($(this).val());
    }); 
    if(roles.length == 0){
    	alert("Error, at least select one role");
    	return;
    }
    
    $.ajax({
        url: "user/"+$("#userId").val()+"?roleIds="+roles.join(","),
        type: 'PUT',
        contentType:'application/json'
            
    }).done(function() {
        window.location.href="users";
    })
    .fail(function(error) {
        alert(error.responseText);
    }); 
}

/*]]>*/
</script>
</body>
</html>