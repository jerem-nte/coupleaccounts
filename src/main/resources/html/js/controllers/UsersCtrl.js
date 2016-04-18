coupleAccountsControllers.controller('EditUserController', ['$scope', '$http', 'userParam', 'close', function($scope, $http, userParam, close) {

	$scope.edituser = userParam;
	
	$scope.close = function(result) {
		
		if(result) {
			$http.get('/user/edit', {params:{id:$scope.edituser.id, name:$scope.edituser.name}}).
	 	   	success(function(data, status, headers, config) {
	 	   		close(data, 500); // close, but give 500ms for bootstrap to animate
	    	}).
	    	error(function(data, status, headers, config) {
	    		close(data, 500);
	    	});
		} else {
			close(result, 500);
		}
		
	};
}]);



coupleAccountsControllers.controller('UsersCtrl', ['$scope', '$http', 'ModalService', function ($scope, $http, ModalService) {
	
	$scope.message = {};
	$scope.users = [];
	 
	$http.get("/user/list",{params:{page:$scope.page}}).
		success(function(response) {
			$scope.users = response;
		});
	
	$scope.showEditUser = function(user) {

		ModalService.showModal({
			templateUrl: "partials/edituser.html",
			controller: "EditUserController",
			inputs: {userParam: user}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				$scope.message.msg = result.message;
				$scope.message.status = result.status;
			});
		});

	 };
}]);