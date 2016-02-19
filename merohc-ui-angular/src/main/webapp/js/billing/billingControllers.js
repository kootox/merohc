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

        //calculate totalAmount
        $scope.totalAmount=0;
        $scope.invoice.invoiceItems.forEach(function(item){
          $scope.totalAmount = $scope.totalAmount + item.amount;
        });
      });
    }
  };

  $scope.updateCategories = function(){
    $http.get(merohcConfig.BASE_URL + '/referential/invoiceCategory').success(function(data){
      $scope.categories = data;
    });
  }

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
  $scope.updateCategories();

}]);

merohcBillingControllers.controller('InvoiceCreateController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.invoice={};

  $scope.invoice.invoiceItems= new Array();

  $scope.saveInvoice = function(){
    $http({
            method  : 'PUT',
            url     : merohcConfig.BASE_URL + '/invoice',
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
    for (var index in $scope.categories) {
      var category = $scope.categories[index];
      if (category.id===item.invoiceCategoryId){
        item.invoiceCategoryName=category.name;
      }
    }

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

  $scope.updateCompanies = function(){
    $http.get(merohcConfig.BASE_URL + '/company').success(function(data){
      $scope.companies = data;
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
  $scope.updateCompanies();

}]);

merohcBillingControllers.controller('InvoiceEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.invoiceId=$state.params.invoiceId;

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
    $state.go('billing.invoices.view', {invoiceId : $scope.invoiceId});
  }

  $scope.updateInvoice();

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

  $scope.updateCompanies = function(){
    $http.get(merohcConfig.BASE_URL + '/company').success(function(data){
      $scope.companies = data;
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

  $scope.updateCompanies();

}]);