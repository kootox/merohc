var merohcCRMControllers = angular.module('merohc.crm.controllers', ['ui.router']);

merohcCRMControllers.controller('CompanyListController', ['$scope', '$http', '$location', '$state', 'merohc-config' , function ($scope, $http, $location, $state, merohcConfig) {

  $scope.items = '';

  $scope.filteredItems = $scope.items;

  $scope.searchTerm="";

  $scope.selectedItem=$scope.items[0];

  $scope.targetId="";

  $scope.setTargetId=function(targetId){
    $scope.targetId=targetId;
  }

  $scope.selectItem=function(selectedCompany){
    $scope.selectedItem = selectedCompany;
    $state.go('crm.companies.view', { companyId: selectedCompany.id });
  };

  $scope.selectItemById=function(id){
    for (var index in $scope.items) {
      var company = $scope.items[index];
      if (company.id===id){
        $scope.selectedItem=company;
      }
    }
  }

  $scope.isItemSelected=function(selectedCompany){
    return selectedCompany===$scope.selectedItem;
  };

  $scope.filter=function(){
    $scope.filteredItems=[];
    for (var index in $scope.items) {
      var company = $scope.items[index];
      var regexp = new RegExp($scope.searchTerm,"i");
      if (company.name.match(regexp)){
        $scope.filteredItems.push(company);
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
  }

  $scope.deleteItem = function(oldItem) {

    for (var index in $scope.items) {
      var company = $scope.items[index];
      if (company.id===oldItem.id){
        if (index > -1) {
          $scope.items.splice(index, 1);
        }
      }
    }
    $state.go("crm.companies");
    $scope.refresh();
  }

  $scope.refresh=function(){
    $http.get(merohcConfig.BASE_URL + '/company').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

}]);

merohcCRMControllers.controller('CompanyDetailController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.companyId=$state.params.companyId;

  $scope.updateCompany=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+encodeURIComponent($scope.companyId)).success(function(data){
        $scope.company = data;
      });
    }
  };

  $scope.deleteCompany=function(){
    //FIXME JC 151212 - Should ask confirmation for deletion
    if ($scope.companyId){
      $http.delete(merohcConfig.BASE_URL + '/company/'+encodeURIComponent($scope.companyId)).success(function(data){
        $scope.deleteItem($scope.company);
      });
    }
  }

  $scope.editCompany=function(){
    $state.go('crm.companies.edit', { companyId: $scope.companyId });
  }

  //get back contact details for the company
  /*$scope.updateContactDetails=function(){
    if ($scope.companyId){
      $http.get('services/v1/ContactDetails?field=target&value='+$scope.companyId).success(function(data){
        $scope.contactDetails = data;
      });
    }
  };

  //get back notes for the company
  $scope.updateNotes=function(){
    if ($scope.companyId){
      $http.get('services/v1/Note?field=target&value='+$scope.companyId).success(function(data){
        $scope.notes = data;
      });
    }
  };

  //get back employees for the company
  $scope.updateEmployees=function(){
    if ($scope.companyId){
      $http.get('services/v1/Employee?field=company&value='+$scope.companyId).success(function(data){
        $scope.employees = data;
      });
    }
  };

  //get back invoices for the company
  $scope.updateInvoices=function(){
    if ($scope.companyId){
      $http.get('services/v1/Invoice?field=payer&value='+$scope.companyId).success(function(data){
        $scope.invoices = data;
      });
    }
  };*/

  $scope.updateCompany();
  /*$scope.updateContactDetails();
  $scope.updateNotes();
  $scope.updateEmployees();
  $scope.updateInvoices();*/

}]);

merohcCRMControllers.controller('CompanyCreateController', ['$scope', '$http', '$window', 'merohc-config',function ($scope, $http, $window, merohcConfig) {

  $scope.saveCompany = function(){
    $http({
            method  : 'PUT',
            url     : merohcConfig.BASE_URL + '/company/add',
            data    : $.param($scope.company),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $window.history.back();
          });
  };

  $scope.cancel = function(){
    $window.history.back();
  }

}]);

merohcCRMControllers.controller('CompanyEditController', ['$scope', '$http', '$window', '$state', 'merohc-config',function ($scope, $http, $window, $state, merohcConfig) {

  $scope.companyId=$state.params.companyId;

  $scope.updateCompany=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+encodeURIComponent($scope.companyId)).success(function(data){
        $scope.company = data;
        $scope.oldCompany = angular.copy(data);
      });
    }
  };

  $scope.saveCompany = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/company',
            data    : $.param($scope.company),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.updateItem($scope.oldCompany, data);
            $scope.$parent.selectedItem=data;

            $state.go('^')
          });
  };

  $scope.cancel = function(){
    $window.history.back();
  }

  $scope.updateCompany();

}]);

merohcCRMControllers.controller('PersonListController', function ($scope, $http, $location, $state) {

  $scope.items = '';

  $scope.filteredItems = $scope.items;

  $scope.searchTerm="";

  $scope.selectedItem=$scope.items[0];

  $scope.targetId="";

  $scope.setTargetId=function(targetId){
    $scope.targetId=targetId;
  }

  $scope.selectItem=function(selectedPerson){
    $scope.selectedItem = selectedPerson;
    $state.go('crm.persons.view', { personId: selectedPerson.id });
  };

  $scope.selectItemById=function(id){
    for (var index in $scope.items) {
      var company = $scope.items[index];
      if (company.id===id){
        $scope.selectedItem=company;
      }
    }
  }

  $scope.isItemSelected=function(selectedCompany){
    return selectedCompany===$scope.selectedItem;
  };

  $scope.filter=function(){
    $scope.filteredItems=[];
    for (var index in $scope.items) {
      var company = $scope.items[index];
      var regexp = new RegExp($scope.searchTerm,"i");
      if (company.Person.firstName.match(regexp)||company.Person.lastName.match(regexp)){
        $scope.filteredItems.push(company);
      }
    }
  };

  $scope.updateItem = function(oldItem, newItem) {
    for (var index in $scope.items) {
      var company = $scope.items[index];
      if (company===oldItem){
        $scope.items[index]=newItem;
      }
    }

    $scope.filter();
  }

  $scope.addItem = function(newItem) {
    $scope.items.push(newItem);
    $scope.filter();
  }

  $scope.deleteItem = function(oldItem) {
    var index = $scope.items.indexOf(oldItem);

    if (index > -1) {
        $scope.items.splice(index, 1);
    }

    $scope.filter();
  }

  $scope.refresh=function(){
    $http.get('services/v1/Person').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

});

merohcCRMControllers.controller('PersonCreateController', function ($scope, $http, $window) {

  $scope.saveCompany = function(){
    $http({
            method  : 'PUT',
            url     : 'companies/add',
            data    : $.param($scope.company),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $window.history.back();
          });
  };

  $scope.cancel = function(){
    $window.history.back();
  }

});

merohcCRMControllers.controller('PersonDetailController', function ($scope, $http, $state){

  $scope.personId=$state.params.personId;

  $scope.updatePerson=function(){
      if ($scope.personId){
        $http.get('services/v1/Person/'+$scope.personId).success(function(data){
          $scope.person = data;
        });
      }
    };

  //get back contact details for the person
  $scope.updateContactDetails=function(){
    if ($scope.personId){
      $http.get('services/v1/ContactDetails?field=target&value='+$scope.personId).success(function(data){
        $scope.contactDetails = data;
      });
    }
  };

  //get back notes for the person
  $scope.updateNotes=function(){
    if ($scope.personId){
      $http.get('services/v1/Note?field=target&value='+$scope.personId).success(function(data){
        $scope.notes = data;
      });
    }
  };

  //get back employees for the person
  $scope.updateEmployees=function(){
    if ($scope.personId){
      $http.get('services/v1/Employee?field=company&value='+$scope.personId).success(function(data){
        $scope.employees = data;
      });
    }
  };

  $scope.updatePerson();
  $scope.updateContactDetails();
  $scope.updateNotes();
  $scope.updateEmployees();

});

merohcCRMControllers.controller('NoteController', function ($scope, $http, $state){

  $scope.noteId=$state.params.noteId;

  $scope.updateNote=function(){
    if ($scope.noteId){
      $http.get('services/v1/Note/'+$scope.noteId).success(function(data){
        $scope.note = data;
      });
    }
  };

  $scope.updateNote();
});

merohcCRMControllers.controller('ContactDetailsController', function ($scope, $http, $state){

  $scope.contactDetailsId=$state.params.contactDetailsId;

  $scope.updateContactDetails=function(){
    if ($scope.contactDetailsId){
      $http.get('services/v1/ContactDetails/'+$scope.contactDetailsId).success(function(data){
        $scope.contactDetails = data;
      });
    }
  };

  $scope.updateContactDetails();
});