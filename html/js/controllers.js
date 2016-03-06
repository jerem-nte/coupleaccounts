var coupleAccountsControllers = angular.module('coupleAccountsControllers', []);

coupleAccountsControllers.controller('TransactionsCtrl', ['$scope', '$http', function ($scope, $http) {


	$scope.scope = "0";
	$scope.message = {};
	$scope.defaultSelectedUser = 0;
	$scope.transactions = [];
	$scope.checkedTransactions = [];
	$scope.debts = [];
	$scope.label = "";
	
	
	$scope.initUserList = function() {	
		$http.get("/user/listhtmlselect").success(function(response) {
			$scope.users = response;
			$scope.user = response[0].value;
		});
	}
	
	$scope.initCurrencyList = function() {	
		$http.get("/currency/listhtmlselect").success(function(response) {
			$scope.currencies = response;
			$scope.currency = response[0].value;
		});
	}
  
  	$scope.getTransactions = function() {		
		$http.get("/expense/list/notarchived").success(function(response) {$scope.transactions = response;});
  	}
  	
	$scope.addTransaction = function(isValid) {
			
		console.log("addTransaction(" + $scope.user + "," + $scope.label + "," + $scope.amount + "," + $scope.scope + "," + $scope.currency + ")" + isValid );
		
		if(isValid) {
			
		$http.post('/expense/add', {userId:$scope.user, label:$scope.label, amount:$scope.amount, scope:$scope.scope, currencyId:$scope.currency}).
	 	   	success(function(data, status, headers, config) {
			   $scope.message.msg = data.message;
			   $scope.message.status = data.status;		   
			   $scope.getTransactions();
			   $scope.getUserDebt();
			   
			   if(data.status == "0") {		   	
				   $scope.cleanForm();
			   }
	    	}).
	    	error(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
	    	});
			
		}
		
	}
	
	$scope.deleteTransaction = function(trsId) {
			
		console.log("deleteTransaction(" + trsId + ")");
		
		$http.post('/expense/delete', {trsId:trsId}).
	 	   	success(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
			   $scope.getTransactions();
			   $scope.getUserDebt();
	    	}).
	    	error(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
	    	});
	}
	
	$scope.archiveTransaction = function(trsId) {
		
		console.log("archiveTransaction(" + trsId + ")");
		
		$http.post('/expense/archive', {trsId:trsId}).
	 	   	success(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
			   $scope.getTransactions();
			   $scope.getUserDebt();
	    	}).
	    	error(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
	    	});
	}
	
	$scope.getUserDebt = function() {
		
		$http.post('/expense/debt').
 	   	 success(function(data, status, headers, config) {
		    $scope.debts = data;
    	 }).
     	 error(function(data, status, headers, config) {
		   $scope.message.msg = data.message;
		   $scope.message.status = data.status;
    	 });
	}
	
	$scope.cleanForm = function() {
		
		$scope.initUserList();
		$scope.label = "";
		$scope.amount = "";
	}
	
	
	$scope.initUserList();
	$scope.initCurrencyList();
	$scope.getTransactions();
	$scope.getUserDebt();
	
	
}]);


coupleAccountsControllers.controller('ArchivesCtrl', ['$scope', '$http', function ($scope, $http) {

	$scope.message = {};
	$scope.transactions = [];
  
  	$scope.getTransactions = function() {		
		$http.get("/expense/list/archived").success(function(response) {$scope.transactions = response;});
  	}
 
	$scope.deleteTransaction = function(trsId) {
			
		console.log("deleteTransaction(" + trsId + ")");
		
		$http.post('/expense/delete', {trsId:trsId}).
	 	   	success(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
			   $scope.getTransactions();
			   $scope.getUserDebt();
	    	}).
	    	error(function(data, status, headers, config) {
 			   $scope.message.msg = data.message;
 			   $scope.message.status = data.status;
	    	});
	}
	
	
	
	
	$scope.getTransactions();
	
	
}]);
