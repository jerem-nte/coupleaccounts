coupleAccountsControllers.controller('EditUserCtrl', function ($scope, $http, $uibModalInstance, user, UserService) {

	$scope.name = user.name;
	$scope.id = user.id;
	
	$scope.ok = function () {
		UserService.editUser($scope.id, $scope.name).then(function(data) {
			$uibModalInstance.close(data);
		}).catch(function(data) {
			$uibModalInstance.close(data);
		});
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss();
	};
});



coupleAccountsControllers.controller('UsersCtrl', ['$scope', '$http', '$uibModal', 'UserService', function ($scope, $http, $uibModal, UserService) {
	
	$scope.message = {};
	$scope.users = [];
	 
	$scope.loadUsers = function() {
		UserService.getUsers().then(function(data) {
			$scope.users = data;
		}).catch(function(data) {
			$scope.message = 'Error getting users';
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