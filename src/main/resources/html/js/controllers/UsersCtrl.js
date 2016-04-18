coupleAccountsControllers.controller('EditUserCtrl', function ($scope, $http, $uibModalInstance, user) {

	$scope.name = user.name;
	$scope.id = user.id;
	
	$scope.ok = function () {
		$http.get('/user/edit', {params:{id:$scope.id, name:$scope.name}}).
 	   	success(function(data, status, headers, config) {
 	   		$uibModalInstance.close(data);
    	}).
    	error(function(data, status, headers, config) {
    		$uibModalInstance.close(data);
    	});
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss();
	};
});



coupleAccountsControllers.controller('UsersCtrl', ['$scope', '$http', '$uibModal', function ($scope, $http, $uibModal) {
	
	$scope.message = {};
	$scope.users = [];
	 
	$scope.loadUsers = function() {
		$http.get("/user/list",{params:{page:$scope.page}}).
			success(function(response) {
				$scope.users = response;
			});
	}
	
	$scope.showEditUser = function(user) {
		
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: "partials/edituser.html",
			controller: 'EditUserCtrl',
			size: 'sm',
			resolve: {
				user: function () {
					return user;
				}
			}
		});
		
		modalInstance.result.then(function (data) {
			$scope.message = data;
			$scope.loadUsers();
		    }, function () {
		      console.log("Closed edit user modal");
		    }
		);
	};
	
	$scope.loadUsers();
	
}]);