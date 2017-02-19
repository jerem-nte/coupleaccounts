coupleAccountsControllers.controller('ArchivesCtrl', ['$scope', '$http', 'ExpenseService', function ($scope, $http, ExpenseService) {

	$scope.page = 1;
	$scope.pageMax = 1;
	$scope.message = {};
	$scope.transactions = [];
	$scope.allTransactionSelected = false;
	$scope.loading = true;
	
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
  		$scope.loading = true;
  		
  		ExpenseService.getArchivedExpenses($scope.page).then(function(data) {
  			$scope.transactions = data.expenses;
			$scope.pageMax = data.pageMax;
			$scope.loading = false;
  		}).catch(function(data) {
  			$scope.loading = false;
  			$scope.message = {status:1, content:"Cannot get archived expenses"};
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
		
		ExpenseService.deleteExpenses(selectedIds).then(function(data) {
 	   		$scope.message = data;
 	   		$scope.getTransactions();
    	}).catch(function(data) {
    		$scope.message = data;
    	});
	}
	
	$scope.getTransactions();
	
}]);