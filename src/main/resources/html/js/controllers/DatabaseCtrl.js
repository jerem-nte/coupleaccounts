coupleAccountsControllers.controller('DatabaseCtrl', ['$scope', '$http', function ($scope, $http) {
	
	$scope.message = {};
	$scope.configuration = {};
	
	
	
	$scope.load = function() {
		console.log("Load database configuration");
		$http.get("/database/config").success(function(response) {$scope.configuration = response;});
	};
	
	$scope.testConnection = function() {
		
		$http.post('/database/update', {configuration:$scope.configuration}).
			success(function(data, status, headers, config) {
				$scope.message = data;
	    	}).
	    	error(function(data, status, headers, config) {
	    		$scope.message = data;
	    	}
	    );
	}
	
	$scope.load();
	
}]);