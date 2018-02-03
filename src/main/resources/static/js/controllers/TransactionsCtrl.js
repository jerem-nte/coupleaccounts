coupleAccountsControllers.controller('TransactionsCtrl', ['$scope', '$http', '$q', '$uibModal', '$animate', 'ExpenseService', 'UserService', 'Notification', function ($scope, $http, $q, $uibModal, $animate, ExpenseService, UserService, Notification) {

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
		var promise = UserService.getUserList().then(function(response) {
			$scope.users = response;
			$scope.user = response[0].value;
		});
		return promise;
	}
	
	$scope.initCurrencyList = function() {	
		var promise = ExpenseService.getCurrencyList().then(function(response) {
			$scope.currencies = response;
			$scope.currency = response[0].value;
		});
		return promise;
	}
  
  	$scope.getTransactions = function() {		
  		var promise = ExpenseService.getExpenses().then(function(response) {$scope.transactions = response;});
  		return promise;
  	}
  	
	$scope.addTransaction = function(isValid) {
			
		console.log("addTransaction(" + $scope.user + "," + $scope.label + "," + $scope.amount + "," + $scope.scope + "," + $scope.currency + ")" + isValid );
		
		if(isValid) {
			
			ExpenseService.isExpenseExist($scope.user, $scope.amount, $scope.scope, $scope.currency).then(function(exist) {
    			if(exist) {
    				Notification({message: 'A similar expense already exist', positionY: 'bottom', positionX: 'center'}, 'warning');
    			}
    			
    			ExpenseService.addExpense($scope.user, $scope.label, $scope.amount, $scope.scope, $scope.currency).then(function(data) {
					Notification({message: data.content, positionY: 'bottom', positionX: 'center'}, data.status == 0 ? 'success' : 'warning');
					$scope.getTransactions();
					$scope.getUserDebt();
				   
					if(data.status == "0") {		   	
						$scope.cleanForm();
					}
		    	}).catch(function(data) {
		    		Notification.error({message: data.content, positionY: 'bottom', positionX: 'center'});
		    	});
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
		
		ExpenseService.deleteExpenses(selectedIds).then(function(data) {
			Notification({message: data.content, positionY: 'bottom', positionX: 'center'}, data.status == 0 ? 'success' : 'warning');
			$scope.getTransactions();
			$scope.getUserDebt();
    	}).catch(function(data) {
    		Notification.error({message: data.content, positionY: 'bottom', positionX: 'center'});
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
		
		ExpenseService.archiveExpenses(selectedIds).then(function(data) {
			Notification({message: data.content, positionY: 'bottom', positionX: 'center'}, data.status == 0 ? 'success' : 'warning');
 	   		$scope.getTransactions();
 	   		$scope.getUserDebt();
    	}).catch(function(data) {
    		Notification.error({message: data.content, positionY: 'bottom', positionX: 'center'});
    	});
		
	}

	
	$scope.getUserDebt = function() {
		
		var promise = ExpenseService.getUserDebt().then(function(data) {
			$scope.debts = data;
		}).catch(function(data) {
			Notification.error({message: data.content, positionY: 'bottom', positionX: 'center'});
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
		}).catch(function() {
			$scope.loading = false;
			Notification.error({message: "Error loading data, please contact your system administrator", positionY: 'bottom', positionX: 'center'});
		});
	}
	
	$scope.showPayTheBill = function(userDebt) {
		
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: "partials/paythebill.html",
			controller: 'PayTheBillCtrl',
			size: 'lg',
			resolve: {
				debt: function () {
					return userDebt;
				}
			}
		});
		
		modalInstance.result.then(function (data) {
			$scope.getTransactions();
			$scope.getUserDebt();
		    }, function () {}
		);
	};
	
	$scope.init();
	
}]);




coupleAccountsControllers.controller('PayTheBillCtrl', function ($scope, $http, $uibModalInstance, debt, ExpenseService) {

	$scope.debt = debt;
	
	$scope.ok = function () {
		if(debt.debt!=0) {
			ExpenseService.addExpense(debt.debit.id, 'Remboursement des dettes', debt.debt, '1', debt.currency.id).then(function(data) {
				$uibModalInstance.close(data);
	    	}).catch(function(data) {
	    		$uibModalInstance.close(data);
	    	});
		}
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss();
	};
});