var coupleAccountsControllers = angular.module('coupleAccountsControllers', []);

coupleAccountsControllers.controller('TransactionsCtrl', ['$scope', '$http', function ($scope, $http) {


	$scope.scope = "0";
	$scope.message = {};
	$scope.defaultSelectedUser = 0;
	$scope.transactions = [];
	$scope.debts = [];
	$scope.label = "";
	$scope.allTransactionSelected = false;
	
	$scope.selectAllTransactions = function () {
        // Loop through all the entities and set their isChecked property
        for (var i = 0; i < $scope.transactions.length; i++) {
            $scope.transactions[i].isChecked = $scope.allTransactionSelected;
        }
    };
    
    $scope.selectTransaction = function () {
        // If any entity is not checked, then uncheck the "allItemsSelected" checkbox
        for (var i = 0; i < $scope.transactions.length; i++) {
            if (!$scope.transactions[i].isChecked) {
                $scope.allTransactionSelected = false;
                return;
            }
        }

        //If not the check the "allItemsSelected" checkbox
        $scope.allTransactionSelected = true;
    };
	
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
	
	$scope.deleteSelectedTransaction = function(trs) {
		
		var selectedIds = [];
		
		angular.forEach(trs, function (item) {
            if(item.isChecked) {
            	selectedIds.push(item.id);
            }
        });

		console.log("deleteSelectedTransaction("+selectedIds+")");
		
		$http.post('/expense/delete', {ids:selectedIds}).
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
	
	$scope.archiveSelectedTransaction = function(trs) {
		
		var selectedIds = [];
		
		angular.forEach(trs, function (item) {
            if(item.isChecked) {
            	selectedIds.push(item.id);
            }
        });

		console.log("archiveSelectedTransaction("+selectedIds+")");
		
		$http.post('/expense/archive', {ids:selectedIds}).
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

	$scope.page = 1;
	$scope.pageMax = 1;
	$scope.message = {};
	$scope.transactions = [];
	$scope.allTransactionSelected = false;
	
	$scope.nextPage = function () {
		if($scope.page==$scope.pagemax)
			return;
		
		$scope.page++;
		$scope.getTransactions();
    };
    $scope.previousPage = function () {
    	if($scope.page==1)
			return;
    	
		$scope.page--;
		$scope.getTransactions();
		
    };    
    
    $scope.getPageMax = function() {		
		$http.get("/expense/list/archived/pagemax").
		success(function(response) {
			$scope.pagemax = response;
		});
  	};
	
	$scope.selectAllTransactions = function () {
        // Loop through all the entities and set their isChecked property
        for (var i = 0; i < $scope.transactions.length; i++) {
            $scope.transactions[i].isChecked = $scope.allTransactionSelected;
        }
    };
    
    $scope.selectTransaction = function () {
        // If any entity is not checked, then uncheck the "allItemsSelected" checkbox
        for (var i = 0; i < $scope.transactions.length; i++) {
            if (!$scope.transactions[i].isChecked) {
                $scope.allTransactionSelected = false;
                return;
            }
        }

        //If not the check the "allItemsSelected" checkbox
        $scope.allTransactionSelected = true;
    };
	
	
  	$scope.getTransactions = function() {	
  		$scope.getPageMax();
  		
		$http.get("/expense/list/archived",{params:{page:$scope.page}}).
		success(function(response) {
			$scope.transactions = response;
		});
  	}
 
  	$scope.deleteSelectedTransaction = function(trs) {
		
		var selectedIds = [];
		
		angular.forEach(trs, function (item) {
            if(item.isChecked) {
            	selectedIds.push(item.id);
            }
        });

		console.log("deleteSelectedTransaction("+selectedIds+")");
		
		$http.post('/expense/delete', {ids:selectedIds}).
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


coupleAccountsControllers.controller('StatisticsCtrl', ['$scope', '$http', function ($scope, $http) {

	 $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
	  $scope.series = ['Series A', 'Series B'];
	  $scope.data = [
	    [65, 59, 80, 81, 56, 55, 40],
	    [28, 48, 40, 19, 86, 27, 90]
	  ];
	
}]);
