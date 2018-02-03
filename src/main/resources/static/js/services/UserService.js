angular.module('coupleAccountsApp').factory('UserService', [ '$route', '$rootScope', '$http', function($route, $rootScope, $http) {
	return {
		getUsers : function() {
			return $http.get("/user/list").then(function(response) {
				return response.data;
			});
		},
		
		editUser : function (id, name) {
			return $http.get('/user/edit', {params:{id:id, name:name}}).then(function(response) {
				return response.data;
			});
		},
		
		getUserList : function(ids) {
			return $http.get("/user/listhtmlselect").then(function(response) {
				return response.data;
			});
		}
	};
} ]);