coupleAccountsControllers.controller('TransactionsCtrl', ['$scope', '$http', '$q', '$uibModal', '$animate', function ($scope, $http, $q, $uibModal) {

	$scope.loading = true;
	$scope.scope = "0";
	$scope.message = {};
	$scope.defaultSelectedUser = 0;
	$scope.transactions = [];
	$scope.debts = [];
	$scope.label = "";
	$scope.allTransactionSelected = false;
	$scope.alerts = [];
	
	
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
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
					$scope.alerts.push(data);
					$scope.getTransactions();
					$scope.getUserDebt();
				   
					if(data.status == "0") {		   	
						$scope.cleanForm();
					}
		    	}).
		    	error(function(data, status, headers, config) {
		    		$scope.alerts.push(data);
		    	}
		    );
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
 	   		$scope.alerts.push(data);
		   $scope.getTransactions();
		   $scope.getUserDebt();
    	}).
    	error(function(data, status, headers, config) {
    		$scope.alerts.push(data);
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
 	   		$scope.alerts.push(data);
 	   		$scope.getTransactions();
 	   		$scope.getUserDebt();
    	}).
    	error(function(data, status, headers, config) {
    		$scope.alerts.push(data);
    	});
		
	}

	
	$scope.getUserDebt = function() {
		
		var promise = $http.post('/expense/debt').
		success(function(data, status, headers, config) {
			$scope.debts = data;
		}).
		error(function(data, status, headers, config) {
			$scope.alerts.push(data);
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
	
	$scope.showPayTheBill = function() {
		
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: "partials/paythebill.html",
			controller: 'PayTheBillCtrl',
			size: 'lg',
			resolve: {
				debts: function () {
					return $scope.debts;
				}
			}
		});
		
		modalInstance.result.then(function (data) {
			$scope.alerts.push(data);
			$scope.getTransactions();
			$scope.getUserDebt();
		    }, function () {}
		);
	};
	
	$scope.init();
	
}]);




coupleAccountsControllers.controller('PayTheBillCtrl', function ($scope, $http, $uibModalInstance, debts) {

	$scope.debts = debts;
	
	$scope.ok = function () {
		$scope.debts.filter(function(debt){return debt.debt!=0;}).forEach(function(debt) {
			$http.post('/expense/add', {userId:debt.debit.id, label:'Remboursement des dettes', amount: debt.debt, scope:1, currencyId:debt.currency.id}).
				success(function(data, status, headers, config) {
					$uibModalInstance.close(data);
		    	}).
		    	error(function(data, status, headers, config) {
		    		$uibModalInstance.close(data);
		    	}
	    	);
		});
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss();
	};
});