var merohcBillingControllers = angular.module('merohc.billing.controllers', ['ui.router', 'ui.bootstrap']);

merohcBillingControllers.controller('InvoiceListController',
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

  $scope.selectItem=function(selectedInvoice){
    $scope.selectedItem = selectedInvoice;
    $state.go('billing.invoices.view', { invoiceId: selectedInvoice.id });
  };

  $scope.selectItemById=function(id){
    for (var index in $scope.items) {
      var invoice = $scope.items[index];
      if (invoice.id===id){
        $scope.selectedItem=invoice;
      }
    }
  }

  $scope.isItemSelected=function(selectedInvoice){
    return selectedInvoice===$scope.selectedItem;
  };

  $scope.filter=function(){
    $scope.filteredItems=[];
    for (var index in $scope.items) {
      var invoice = $scope.items[index];
      var regexp = new RegExp($scope.searchTerm,"i");
      if (invoice.name.match(regexp)){
        $scope.filteredItems.push(invoice);
      }
    }
  };

  $scope.updateItem = function(oldItem, newItem) {
    for (var index in $scope.items) {
      var company = $scope.items[index];
      if (company.id===oldItem.id){
        $scope.items[index]=newItem;
      }
    }

    $scope.filter();
  }

  $scope.addItem = function(newItem) {
    $scope.items.push(newItem);
    $scope.filter();
    $state.go('billing.invoices.view', { invoiceId: newItem.id })
  }

  $scope.deleteItem = function(oldItem) {

    for (var index in $scope.items) {
      var invoice = $scope.items[index];
      if (invoice.id===oldItem.id){
        if (index > -1) {
          $scope.items.splice(index, 1);
        }
      }
    }
    $state.go("billing.invoices");
    $scope.refresh();
  }

  $scope.refresh=function(){
    $http.get(merohcConfig.BASE_URL + '/invoice').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

}]);

merohcBillingControllers.controller('InvoiceViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.invoiceId=$state.params.invoiceId;

  $scope.updateInvoice=function(){
    if ($scope.invoiceId){
      $http.get(merohcConfig.BASE_URL + '/invoice/'+$scope.invoiceId).success(function(data){
        $scope.invoice = data;
      });
    }
  };

  $scope.deleteInvoice=function(){
    //FIXME JC 151212 - Should ask confirmation for deletion
    if ($scope.invoiceId){
      $http.delete(merohcConfig.BASE_URL + '/invoice/'+$scope.invoiceId).success(function(data){
        $scope.deleteItem($scope.invoice);
      });
    }
  }

  $scope.editInvoice=function(){
    $state.go('billing.invoices.edit', { invoiceId: $scope.invoiceId });
  }

  $scope.updateInvoice();

}]);

merohcBillingControllers.controller('InvoiceCreateController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.invoice={};

  $scope.invoice.invoiceItems= new Array();

  $scope.saveInvoice = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/invoice/add',
            data    : $scope.invoice,  // pass in data as strings
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $state.go('billing.invoices.view', {invoiceId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('billing.invoices');
  }

  $scope.addInvoiceItem = function(){
    $scope.addItemPanel=true;
  }

  $scope.addItem = function(item){
    $scope.invoice.invoiceItems.push(item);
    $scope.addItemPanel = false;
  }

  $scope.cancelAddItem = function(){
    $scope.item=null;
    $scope.addItemPanel = false;
  }

  $scope.removeItem = function(item){
    var index = $scope.invoice.invoiceItems.indexOf(item);

    if (index > -1) {
        $scope.invoice.invoiceItems.splice(index, 1);
    }
  }

  $scope.updateCategories = function(){
    $http.get(merohcConfig.BASE_URL + '/referential/invoiceCategory').success(function(data){
      $scope.categories = data;
    });

  }

  $scope.openEmitted = function() {
      $scope.popupEmitted.opened = true;
    };

  $scope.popupEmitted = {
      opened: false
    };

  $scope.openDue = function() {
      $scope.popupDue.opened = true;
    };

  $scope.popupDue = {
      opened: false
    };

  $scope.openPayment = function() {
      $scope.popupPayment.opened = true;
    };

  $scope.popupPayment = {
      opened: false
    };

  $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };

   $scope.format = 'dd-MMMM-yyyy'

  $scope.updateCategories();

}]);

merohcBillingControllers.controller('InvoiceEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.invoiceId=$state.params.invoiceId;

  //FIXME datepicker

  $scope.updateInvoice=function(){
    if ($scope.invoiceId){
      $http.get(merohcConfig.BASE_URL + '/invoice/'+$scope.invoiceId).success(function(data){
        $scope.invoice = data;
        $scope.oldInvoice = angular.copy(data);
      });
    }
  };

  $scope.saveInvoice = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/invoice',
            data    : $.param($scope.invoice),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {

            //update invoice and selectedItem in parent scope
            $scope.updateItem($scope.oldInvoice, data);
            $scope.$parent.selectedItem=data;

            $state.go('billing.invoices.view', {invoiceId : $scope.invoiceId});
          });
  };

  $scope.cancel = function(){
    $state.go('billing.invoices.view', {invoiceId : $scope.invoiceId});
  }

  $scope.updateInvoice();

}]);