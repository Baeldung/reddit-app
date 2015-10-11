var app = angular.module('myApp', ["ngTable","ngResource"]);
app.controller('mainCtrl', function($scope,NgTableParams,$resource) {


    $scope.feed = $resource("api/myFeeds/:feedId",{feedId:'@id'});
    $scope.tableParams = new NgTableParams({}, {
      getData: function(params) {
    	  var queryParams = {page:params.page()-1 , size:params.count()};
    	  var sortingProp = Object.keys(params.sorting());
          if(sortingProp.length == 1){
        	  queryParams["sort"] = sortingProp[0];
        	  queryParams["sortDir"] = params.sorting()[sortingProp[0]];
          }
        return $scope.feed.query(queryParams, function(data, headers) {
          var totalRecords = headers("PAGING_INFO").split(",")[0].split("=")[1];
          params.total(totalRecords);
          console.log(params.total());
          return data;
        }).$promise;
      }
    });
    
	$scope.confirmDelete = function(id){
		if (confirm("Do you really want to delete this site?") == true) {
			$scope.feed.delete({feedId:id}, function(){
				$scope.tableParams.reload();
			});
			
    	} 
	}
    
    
});