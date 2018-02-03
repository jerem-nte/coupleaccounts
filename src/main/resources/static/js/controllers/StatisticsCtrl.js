coupleAccountsControllers.controller('StatisticsCtrl', ['$scope', '$http', function ($scope, $http) {

	 $scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
	 $scope.data = [300, 500, 100];
	
}]);