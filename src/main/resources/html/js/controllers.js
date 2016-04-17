var coupleAccountsControllers = angular.module('coupleAccountsControllers', []);

coupleAccountsControllers.controller('TransactionsCtrl', ['$scope', '$http', '$q', function ($scope, $http, $q) {

	$scope.loading = true;
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
		var promise = $http.get("/user/listhtmlselect").success(function(response) {
			$scope.users = response;
			$scope.user = response[0].value;
		});
		return promise;
	}
	
	$scope.initCurrencyList = function() {	
		var promise = $http.get("/currency/listhtmlselect").success(function(response) {
			$scope.currencies = response;
			$scope.currency = response[0].value;
		});
		return promise;
	}
  
  	$scope.getTransactions = function() {		
  		var promise = $http.get("/expense/list/notarchived").success(function(response) {$scope.transactions = response;});
  		return promise;
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
		
		var promise = $http.post('/expense/debt').
 	   	 success(function(data, status, headers, config) {
		    $scope.debts = data;
    	 }).
     	 error(function(data, status, headers, config) {
		   $scope.message.msg = data.message;
		   $scope.message.status = data.status;
    	 });
		
		return promise;
	}
	
	$scope.cleanForm = function() {
		
		$scope.initUserList();
		$scope.label = "";
		$scope.amount = "";
	}
	
	$scope.init = function() {
		
		var promises = [];
		
		promises.push($scope.initUserList());
		promises.push($scope.initCurrencyList());
		promises.push($scope.getTransactions());
		promises.push($scope.getUserDebt());
		
		$q.all(promises).then(function() {
			$scope.loading = false;
		});
	}
	
	$scope.init();
	
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



coupleAccountsControllers.controller('StatisticsCtrl', ['$scope', '$http', function ($scope, $http) {

	 $scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
	 $scope.data = [300, 500, 100];
	
}]);
