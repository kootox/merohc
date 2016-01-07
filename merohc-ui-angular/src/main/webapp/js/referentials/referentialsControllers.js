var merohcReferentialsControllers = angular.module('merohc.referentials.controllers', ['ui.router']);

merohcReferentialsControllers.controller('InvoiceCategoryListController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.items = '';

  $scope.filteredItems = $scope.items;

  $scope.searchTerm="";

  $scope.selectedItem=$scope.items[0];

  $scope.targetId="";

  $scope.setTargetId=function(targetId){
    $scope.targetId=targetId;
  }

  $scope.selectItem=function(selectedCategory){
    $scope.selectedItem = selectedCategory;
    $state.go('referentials.invoiceCategory.view', { categoryId: selectedCategory.id });
  };

  $scope.selectItemById=function(id){
    for (var index in $scope.items) {
      var category = $scope.items[index];
      if (category.id===id){
        $scope.selectedItem=category;
      }
    }
  };

  $scope.isItemSelected=function(selectedCategory){
    return selectedCategory===$scope.selectedItem;
  };

  $scope.filter=function(){
    $scope.filteredItems=[];
    for (var index in $scope.items) {
      var category = $scope.items[index];
      var regexp = new RegExp($scope.searchTerm,"i");
      if (category.name.match(regexp)){
        $scope.filteredItems.push(category);
      }
    }
  };

  $scope.updateItem = function(oldItem, newItem) {
    for (var index in $scope.items) {
      var category = $scope.items[index];
      if (category.id===oldItem.id){
        $scope.items[index]=newItem;
      }
    }

    $scope.filter();
  };

  $scope.addItem = function(newItem) {
    $scope.items.push(newItem);
    $scope.filter();
    $state.go('referentials.invoiceCategory.view', { categoryId: newItem.id });
  };

  $scope.deleteItem = function(oldItem) {

    for (var index in $scope.items) {
      var category = $scope.items[index];
      if (category.id===oldItem.id){
        if (index > -1) {
          $scope.items.splice(index, 1);
        }
      }
    }
    $state.go("referentials.invoiceCategory");
    $scope.refresh();
  };

  $scope.refresh=function(){
    $http.get(merohcConfig.BASE_URL + '/referential/invoiceCategory').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

}]);

merohcReferentialsControllers.controller('InvoiceCategoryViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.categoryId=$state.params.categoryId;

  $scope.updateCategory=function(){
    if ($scope.categoryId){
      $http.get(merohcConfig.BASE_URL + '/referential/invoiceCategory/'+$scope.categoryId).success(function(data){
        $scope.category = data;
      });
    }
  };

  $scope.deleteCategory=function(){
    //FIXME JC 151212 - Should ask confirmation for deletion
    if ($scope.categoryId){
      $http.delete(merohcConfig.BASE_URL + '/referential/invoiceCategory/'+$scope.categoryId).success(function(data){
        $scope.deleteItem($scope.category);
      });
    }
  }

  $scope.editCategory=function(){
    $state.go('referentials.invoiceCategory.edit', { categoryId: $scope.categoryId });
  }

  $scope.updateCategory();

}]);

merohcReferentialsControllers.controller('InvoiceCategoryCreateController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.saveCategory = function(){
    //FIXME JC160107 - Change method to PUT and remove add from url when #1 is closed
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/referential/invoiceCategory/add',
            data    : $.param($scope.category),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {

            //update category and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $state.go('referentials.invoiceCategory.view', {categoryId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('referentials.invoiceCategory');
  }

}]);

merohcReferentialsControllers.controller('InvoiceCategoryEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.categoryId=$state.params.categoryId;

  $scope.updateCategory=function(){
    if ($scope.categoryId){
      $http.get(merohcConfig.BASE_URL + '/referential/invoiceCategory/'+$scope.categoryId).success(function(data){
        $scope.category = data;
        $scope.oldCategory = angular.copy(data);
      });
    }
  };

  $scope.saveCategory = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/referential/invoiceCategory',
            data    : $.param($scope.category),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {

            //update category and selectedItem in parent scope
            $scope.updateItem($scope.oldCategory, data);
            $scope.$parent.selectedItem=data;

            $state.go('referentials.invoiceCategory.view', { categoryId: $scope.categoryId});
          });
  };

  $scope.cancel = function(){
    $state.go('referentials.invoiceCategory.view', { categoryId: $scope.categoryId});
  }

  $scope.updateCategory();

}]);