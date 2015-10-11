<html>
<head>

<title>Schedule to Reddit</title>
<link rel="shortcut icon" type="image/png" th:href="@{/resources/favicon.png}"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
<link rel="stylesheet" th:href="@{/resources/ng-table.min.css}"/>

<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-resource.min.js"></script>

<script th:src="@{/resources/ng-table.min.js}"></script>
<script th:src="@{/resources/mainCtrl.js}"></script>

</head>
<body>
<div th:include="header"/>

<div class="container" ng-app="myApp" ng-controller="mainCtrl">
<h1>My RSS Feeds</h1>
<br/>
<a th:href="@{/feedForm}" class="btn btn-primary">Add New RSS Feed</a>
<br/>
<br/>

<table ng-table="tableParams" class="table table-bordered table-striped table-condensed">
      <tr ng-repeat="row in $data track by row.id">
        <td data-title="'Name'" sortable="'name'">{{row.name}}</td>
        <td data-title="'Feed URL'" sortable="'url'">{{row.url}}</td>
        <td data-title="'Actions'" ><a href="#" class="btn btn-danger" ng-click="confirmDelete(row.id) ">Delete</a></td>
      </tr>
    </table>
</div>
</body>
</html>