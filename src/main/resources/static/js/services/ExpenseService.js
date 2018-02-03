angular.module('coupleAccountsApp').factory('ExpenseService', [ '$route', '$rootScope', '$http', function($route, $rootScope, $http) {
	return {
		
		getExpenses : function() {
			return $http.get("/expense/list/notarchived").then(function(response) {
				return response.data;
			});
		},
		
		getArchivedExpenses : function(page) {
			return $http.get("/expense/list/archived",{params:{page:page}}).then(function(response) {
				return response.data;
			});
		},
		
		addExpense : function(userId, label, amount, scope, currencyId) {
			return $http.post('/expense/add', {userId:userId, label:label, amount:amount, scope:scope, currencyId:currencyId}).then(function(response) {
				return response.data;
			});
		},
		
		isExpenseExist : function(userId, amount, scope, currencyId) {
			return $http.post('/expense/exist', {userId:userId, amount:amount, scope:scope, currencyId:currencyId}).then(function(response) {
				return response.data;
			});
		},
		
		deleteExpenses : function(ids) {
			return $http.post('/expense/delete', ids).then(function(response) {
				return response.data;
			});
		},
		
		archiveExpenses : function(ids) {
			return $http.post('/expense/archive', ids).then(function(response) {
				return response.data;
			});
		},
		
		getCurrencyList : function(ids) {
			return $http.get("/currency/listhtmlselect").then(function(response) {
				return response.data;
			});
		},
		
		getUserDebt : function(ids) {
			return $http.post("/expense/debt").then(function(response) {
				return response.data;
			});
		}
	};
} ]);