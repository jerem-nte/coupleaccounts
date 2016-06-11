coupleAccountsControllers.controller('DatabaseCtrl', ['$scope', '$http', 'DatabaseService', function ($scope, $http, DatabaseService) {
	
	$scope.message = {};
	$scope.configuration = {};
	
	
	$scope.load = function() {
		DatabaseService.getConfiguration().then(function(data) {
			$scope.configuration = data;
		}).catch(function(data) {
			$scope.message = data;
		});
	};
	
	$scope.testConnection = function() {
		
		DatabaseService.testConfiguration($scope.configuration).then(function(data) {
			$scope.message = data;
		}).catch(function(data) {
			$scope.message = data;
		})
	}
	
	$scope.load();
	
}]);