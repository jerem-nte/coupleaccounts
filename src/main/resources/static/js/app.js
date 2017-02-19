var coupleAccountsApp = angular.module('coupleAccountsApp', ['ngRoute', 'coupleAccountsControllers', 'chart.js', 'ngAnimate', 'ui.bootstrap', 'ui-notification']);

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
      	                        when('/users', {
      	                          templateUrl: 'partials/users.html',
      	                          controller: 'UsersCtrl'
      	                        }).
      	                        when('/statistics', {
      	                          templateUrl: 'partials/statistics.html',
      	                          controller: 'StatisticsCtrl'
      	                        }).
      	                        otherwise({
      	                          redirectTo: '/'
      	                        });
                          	}]);

var coupleAccountsControllers = angular.module('coupleAccountsControllers', []);