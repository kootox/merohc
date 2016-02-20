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

  $scope.addInvoiceItemPanel = function(){
    $scope.addItemPanel=true;
  }

  $scope.addInvoiceItem = function(item){
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

  $scope.updateProjects = function(){
    $http.get(merohcConfig.BASE_URL + '/project').success(function(data){
      $scope.projects = data;
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
  $scope.updateProjects();

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
            $scope.updateItem($scope.oldInvoice, data);
            $scope.$parent.selectedItem=data;

            $state.go('billing.invoices.view', {invoiceId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('billing.invoices.view', {invoiceId : $scope.invoiceId});
  }

  $scope.updateInvoice();

  $scope.addInvoiceItemPanel = function(){
    $scope.addItemPanel=true;
  }

  $scope.addInvoiceItem = function(item){
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

  $scope.updateProjects = function(){
    $http.get(merohcConfig.BASE_URL + '/project').success(function(data){
      $scope.projects = data;
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
  $scope.updateProjects();

}]);

merohcBillingControllers.controller('BillListController',
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

  $scope.selectItem=function(selectedBill){
    $scope.selectedItem = selectedBill;
    $state.go('billing.bills.view', { billId: selectedBill.id });
  };

  $scope.selectItemById=function(id){
    for (var index in $scope.items) {
      var invoice = $scope.items[index];
      if (invoice.id===id){
        $scope.selectedItem=invoice;
      }
    }
  }

  $scope.isItemSelected=function(selectedBill){
    return selectedBill===$scope.selectedItem;
  };

  $scope.filter=function(){
    $scope.filteredItems=[];
    for (var index in $scope.items) {
      var bill = $scope.items[index];
      var regexp = new RegExp($scope.searchTerm,"i");
      if (bill.name.match(regexp)){
        $scope.filteredItems.push(bill);
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
    $state.go('billing.bills.view', { billId: newItem.id })
  }

  $scope.deleteItem = function(oldItem) {

    for (var index in $scope.items) {
      var bill = $scope.items[index];
      if (bill.id===oldItem.id){
        if (index > -1) {
          $scope.items.splice(index, 1);
        }
      }
    }
    $state.go("billing.bills");
    $scope.refresh();
  }

  $scope.refresh=function(){
    $http.get(merohcConfig.BASE_URL + '/bill').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

}]);

merohcBillingControllers.controller('BillViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.billId=$state.params.billId;

  $scope.updateBill=function(){
    if ($scope.billId){
      $http.get(merohcConfig.BASE_URL + '/bill/'+$scope.billId).success(function(data){
        $scope.bill = data;

        //calculate totalAmount
        $scope.totalAmount=0;
        $scope.bill.billItems.forEach(function(item){
          $scope.totalAmount = $scope.totalAmount + item.amount;
        });
      });
    }
  };

  $scope.updateCategories = function(){
    $http.get(merohcConfig.BASE_URL + '/referential/billCategory').success(function(data){
      $scope.categories = data;
    });
  }

  $scope.deleteBill=function(){
    //FIXME jcouteau 20160220 - Should ask confirmation for deletion
    if ($scope.billId){
      $http.delete(merohcConfig.BASE_URL + '/bill/'+$scope.invoiceId).success(function(data){
        $scope.deleteItem($scope.bill);
      });
    }
  }

  $scope.editBill=function(){
    $state.go('billing.bills.edit', { billId: $scope.billId });
  }

  $scope.updateBill();
  $scope.updateCategories();

}]);

merohcBillingControllers.controller('BillCreateController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.bill={};

  $scope.bill.billItems= new Array();

  $scope.saveBill = function(){
    $http({
            method  : 'PUT',
            url     : merohcConfig.BASE_URL + '/bill',
            data    : $scope.bill,  // pass in data as strings
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $state.go('billing.bills.view', {billId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('billing.bills');
  }

  $scope.addBillItemPanel = function(){
    $scope.addItemPanel=true;
  }

  $scope.addBillItem = function(item){
    for (var index in $scope.categories) {
      var category = $scope.categories[index];
      if (category.id===item.billCategoryId){
        item.billCategoryName=category.name;
      }
    }

    $scope.bill.billItems.push(item);
    $scope.addItemPanel = false;
  }

  $scope.cancelAddItem = function(){
    $scope.item=null;
    $scope.addItemPanel = false;
  }

  $scope.removeItem = function(item){
    var index = $scope.bill.billItems.indexOf(item);

    if (index > -1) {
        $scope.bill.billItems.splice(index, 1);
    }
  }

  $scope.updateCategories = function(){
    $http.get(merohcConfig.BASE_URL + '/referential/billCategory').success(function(data){
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

merohcBillingControllers.controller('BillEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.billId=$state.params.billId;

  $scope.updateBill=function(){
    if ($scope.billId){
      $http.get(merohcConfig.BASE_URL + '/bill/'+$scope.billId).success(function(data){
        $scope.bill = data;
        $scope.oldBill = angular.copy(data);
      });
    }
  };

  $scope.saveBill = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/bill',
            data    : $scope.bill
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.updateItem($scope.oldBill, data);
            $scope.$parent.selectedItem=data;

            $state.go('billing.bills.view', {billId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('billing.bills.view', {billId : $scope.billId});
  }

  $scope.updateBill();

  $scope.addBillItemPanel = function(){
    $scope.addItemPanel=true;
  }

  $scope.addBillItem = function(item){
    for (var index in $scope.categories) {
      var category = $scope.categories[index];
      if (category.id===item.billCategoryId){
        item.billCategoryName=category.name;
      }
    }

    $scope.bill.billItems.push(item);
    $scope.addItemPanel = false;
  }

  $scope.cancelAddItem = function(){
    $scope.item=null;
    $scope.addItemPanel = false;
  }

  $scope.removeItem = function(item){
    var index = $scope.bill.billItems.indexOf(item);

    if (index > -1) {
        $scope.bill.billItems.splice(index, 1);
    }
  }

  $scope.updateCategories = function(){
    $http.get(merohcConfig.BASE_URL + '/referential/billCategory').success(function(data){
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