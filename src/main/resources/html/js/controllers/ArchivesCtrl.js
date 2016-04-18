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