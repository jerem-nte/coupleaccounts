var coupleAccountsApp = angular.module('coupleAccountsApp', ['ngRoute', 'coupleAccountsControllers']);

coupleAccountsApp.config(['$routeProvider',
                          function($routeProvider) {
                          	$routeProvider.
      	                        when('/', {
      	                          templateUrl: 'partials/expenses.html',
      	                          controller: 'TransactionsCtrl'
      	                        }).
      	                        when('/archives', {
      	                          templateUrl: 'partials/archives.html',
      	                          controller: 'ArchivesCtrl'
      	                        }).
      	                        otherwise({
      	                          redirectTo: '/'
      	                        });
                          	}]);