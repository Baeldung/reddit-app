<html>
<head>

<title>Schedule to Reddit</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>

</head>
<body>
<div th:include="header"/>

<div class="container">
<h1>My Sites</h1>
<br/>
<a th:href="@{/siteForm}" class="btn btn-primary">Add New Site</a>
<br/>
<br/>
<table class="table table-bordered">
<thead>
<tr>
<th>Site Name</th>
<th>Feed URL</th>
<th>Actions</th>
</tr>
</thead>
    <tr th:each="site  : ${sites}">
        <td th:text="${site.getName()}"></td>
        <td th:text="${site.getUrl()}"></td>
        <td>
            <a href="#" class="btn btn-danger" th:onclick="'javascript:confirmDelete(\'' +${site.getId()}+ '\') '">Delete</a>
        </td>
    </tr>
</table>
</div>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script>
/*<![CDATA[*/
function confirmDelete(id) {
    if (confirm("Do you really want to delete this site?") == true) {
        deleteSite(id);
    } 
}

function deleteSite(id){
    $.ajax({
        url: 'sites/'+id,
        type: 'DELETE',
        success: function(result) {
            window.location.href="sites"
        }
    });
}
/*]]>*/
</script>
</body>
</html>