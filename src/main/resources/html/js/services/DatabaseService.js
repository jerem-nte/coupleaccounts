angular.module('coupleAccountsApp').factory('DatabaseService', [ '$route', '$rootScope', '$http', function($route, $rootScope, $http) {
	return {
		getConfiguration : function() {
			return $http.get("/database/config").then(function(response) {
				return response.data;
			});
		},
		
		testConfiguration : function(configuration) {
			return $http.post('/database/test', {configuration:configuration}).then(function(response) {
				return response.data;
			});
		}
		
	};
} ]);