var merohcCRMControllers = angular.module('merohc.crm.controllers', ['ui.router']);

merohcCRMControllers.controller('CompanyListController',
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
    $state.go('crm.companies.view', { companyId: newItem.id })
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

  //get back emails for the company
  $scope.updateEmails=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+$scope.companyId+'/email').success(function(data){
        $scope.emails = data;
      });
    }
  };

  //get back addresses for the company
  $scope.updateAddresses=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+$scope.companyId+'/address').success(function(data){
        $scope.addresses = data;
      });
    }
  };

  //get back phones for the company
  $scope.updatePhones=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+$scope.companyId+'/phone').success(function(data){
        $scope.phones = data;
      });
    }
  };

  //get back notes for the company
  /*$scope.updateNotes=function(){
    if ($scope.companyId){
      $http.get('services/v1/Note?field=target&value='+$scope.companyId).success(function(data){
        $scope.notes = data;
      });
    }
  };*/

  //get back contacts for the company
  $scope.updateContacts=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+encodeURIComponent($scope.companyId)+'/contact').success(function(data){
        $scope.contacts = data;
      });
    }
  };

  //get back invoices for the company
  /*$scope.updateInvoices=function(){
    if ($scope.companyId){
      $http.get('services/v1/Invoice?field=payer&value='+$scope.companyId).success(function(data){
        $scope.invoices = data;
      });
    }
  };*/

  $scope.updateCompany();
  $scope.updateEmails();
  $scope.updateAddresses();
  $scope.updatePhones();
  //$scope.updateNotes();
  $scope.updateContacts();
  /*$scope.updateInvoices();*/

}]);

merohcCRMControllers.controller('CompanyCreateController', ['$scope', '$http', '$state', 'merohc-config',function ($scope, $http, $state, merohcConfig) {

  $scope.saveCompany = function(){
    $http.post(merohcConfig.BASE_URL + '/company/add',
               $.param($scope.company),  // pass in data as strings
               {headers : { 'Content-Type': 'application/x-www-form-urlencoded' }}  // set the headers so angular passing info as form data (not request payload)
         )
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $state.go('crm.companies.view', {companyId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('crm.companies');
  }

}]);

merohcCRMControllers.controller('CompanyEditController', ['$scope', '$http', '$state', 'merohc-config',function ($scope, $http, $state, merohcConfig) {

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

            $state.go('crm.companies.view', {companyId : $scope.companyId});
          });
  };

  $scope.cancel = function(){
    $state.go('crm.companies.view', {companyId : $scope.companyId});
  }

  $scope.updateCompany();

}]);

merohcCRMControllers.controller('ContactCreateController', ['$scope', '$http', '$state', '$stateParams', 'merohc-config',function ($scope, $http, $state, $stateParams, merohcConfig) {

  $scope.saveContact = function(){
    var param = { contact : $scope.contact };
    $http({
            method      : 'PUT',
            url         : merohcConfig.BASE_URL + '/company/'+ $stateParams.companyId +'/contact',
            data        : $scope.contact
         })
          .success(function(data) {
            $state.go('crm.companies.viewContact', { companyId: $state.params.companyId, contactId: data.id });
          });
  };

  $scope.cancel = function(){
    $state.go('crm.companies.view', { companyId: $stateParams.companyId });
  }

}]);

merohcCRMControllers.controller('ContactEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.saveContact = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/contact',
            data    : $.param($scope.contact),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
         })
          .success(function(data) {
            if ($scope.companyId) {
              $state.go('crm.companies.viewContact', { companyId: $state.params.companyId, contactId: $state.params.contactId });
            } else {
              //FIXME JC151227 - update parent list
              $state.go('crm.contacts.view', {contactId: $state.params.contactId });
            }
          });
  };

  $scope.updateContact=function(){
      if ($scope.contactId){
        $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId).success(function(data){
          $scope.contact = data;
        });
      }
    };

  $scope.cancel = function(){
    if ($scope.companyId) {
      $state.go('crm.companies.viewContact', { companyId: $state.params.companyId, contactId: $state.params.contactId });
    } else {
      $state.go('crm.contacts.view', {contactId: $state.params.contactId });
    }
  }

  $scope.updateContact();

}]);

merohcCRMControllers.controller('ContactDetailController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.companyId=$state.params.companyId;
  $scope.contactId=$state.params.contactId;

  $scope.updateContact=function(){
    if ($scope.companyId && $scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId).success(function(data){
        $scope.contact = data;
      });
    }
  };

  //get back emails for the contact
  $scope.updateEmails=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId+'/email').success(function(data){
        $scope.emails = data;
      });
    }
  };

  //get back addresses for the contact
  $scope.updateAddresses=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId+'/address').success(function(data){
        $scope.addresses = data;
      });
    }
  };

  //get back phones for the contact
  $scope.updatePhones=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId+'/phone').success(function(data){
        $scope.phones = data;
      });
    }
  };

  $scope.deleteContact=function(){
    //FIXME JC 151216 - Should ask confirmation for deletion
    if ($scope.companyId && $scope.contactId){
      $http.delete(merohcConfig.BASE_URL + '/contact/'+$scope.contactId).success(function(data){
        $state.go('crm.companies.view', {companyId : $scope.companyId});
      });
    }
  }

  $scope.editContact=function(){
    $state.go('crm.companies.editContact', { companyId: $scope.companyId, contactId: $scope.contactId });
  }

  $scope.updateContact();
  $scope.updateEmails();
  $scope.updateAddresses();
  $scope.updatePhones();

}]);

merohcCRMControllers.controller('EmailCreateController', ['$scope', '$http', '$state', '$stateParams', 'merohc-config',function ($scope, $http, $state, $stateParams, merohcConfig) {

  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.saveEmail = function(){
    if ($scope.contactId != null || $scope.companyId == null){
      $http({
        method  : 'PUT',
        url     : merohcConfig.BASE_URL + '/contact/'+ $scope.contactId +'/email',
        data    : $scope.email  // pass in data as strings
      })
      .success(function(data) {
        if ($scope.company==null){
          $state.go('crm.contacts.viewEmail', { contactId: $state.params.contactId, emailId: data.id });
        } else {
          $state.go('crm.companies.viewEmail', { companyId: $state.params.companyId, emailId: data.id });
        }
      });
    } else {
      $http({
        method  : 'PUT',
        url     : merohcConfig.BASE_URL + '/company/'+ $stateParams.companyId +'/email',
        data    : $scope.email
      })
      .success(function(data) {
        $state.go('crm.companies.viewEmail', { companyId: $state.params.companyId, emailId: data.id });
      });
    }
  };

  $scope.cancel = function(){
    if ($scope.contactId!=null){
      $state.go('crm.companies.viewContact', { companyId: $stateParams.companyId, contactId: $scope.contactId });
    } else {
      $state.go('crm.companies.view', { companyId: $stateParams.companyId });
    }
  }

}]);

merohcCRMControllers.controller('EmailViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.companyId=$state.params.companyId;
  $scope.contactId=$state.params.contactId;
  $scope.emailId=$state.params.emailId;

  $scope.updateEmail=function(){
    if ($scope.emailId){
      $http.get(merohcConfig.BASE_URL + '/email/'+$scope.emailId).success(function(data){
        $scope.email = data;
      });
    }
  };

  $scope.deleteEmail=function(){
    //FIXME JC 151216 - Should ask confirmation for deletion
    if ($scope.emailId){
      $http.delete(merohcConfig.BASE_URL + '/email/'+$scope.emailId).success(function(data){
        if ($scope.companyId){
          $state.go('crm.companies.view', {companyId : $scope.companyId});
        } else {
          $state.go('crm.contacts.view', {contactId : $scope.contactId});
        }
      });
    }
  }

  $scope.editEmail=function(){
    if ($scope.companyId){
      $state.go('crm.companies.editEmail', { companyId: $scope.companyId, emailId: $scope.emailId });
    } else {
      $state.go('crm.contacts.editEmail', { contactId: $scope.contactId, emailId: $scope.emailId });
    }
  }

  $scope.updateEmail();

}]);

merohcCRMControllers.controller('EmailEditController', ['$scope', '$http', '$state', 'merohc-config',function ($scope, $http, $state, merohcConfig) {

  $scope.emailId=$state.params.emailId;
  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.saveEmail = function(){
    $http({
      method  : 'POST',
      url     : merohcConfig.BASE_URL + '/email',
      data    : $.param($scope.email),  // pass in data as strings
      headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    })
    .success(function(data) {
      if ($scope.companyId){
        $state.go('crm.companies.viewEmail', { companyId: $scope.companyId, emailId: $scope.emailId });
      } else {
        $state.go('crm.contacts.viewEmail', { contactId: $scope.contactId, emailId: $scope.emailId });
      }
    });
  };

  $scope.updateEmail=function(){
    if ($scope.emailId){
      $http.get(merohcConfig.BASE_URL + '/email/'+$scope.emailId).success(function(data){
        $scope.email = data;
      });
    }
  };

  $scope.cancel = function(){
    if ($scope.companyId){
      $state.go('crm.companies.viewEmail', { companyId: $scope.companyId, emailId: $scope.emailId });
    } else {
      $state.go('crm.contacts.viewEmail', { contactId: $scope.contactId, emailId: $scope.emailId });
    }
  }

  $scope.updateEmail();

}]);

merohcCRMControllers.controller('AddressCreateController',
    ['$scope', '$http', '$state', '$stateParams', 'merohc-config',
    function ($scope, $http, $state, $stateParams, merohcConfig) {

  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.saveAddress = function(){
    if ($scope.contactId!=null){
      $http({
        method  : 'PUT',
        url     : merohcConfig.BASE_URL + '/contact/'+ $scope.contactId +'/address',
        data    : $scope.address
      })
      .success(function(data) {
        if ($scope.company==null){
          $state.go('crm.contacts.viewAddress', { contactId: $state.params.contactId, addressId: data.id });
        } else {
          $state.go('crm.companies.viewAddress', { companyId: $state.params.companyId, addressId: data.id });
        }
      });
    } else {
      $http({
        method  : 'PUT',
        url     : merohcConfig.BASE_URL + '/company/'+ $stateParams.companyId +'/address',
        data    : $scope.address
      })
      .success(function(data) {
        $state.go('crm.companies.viewAddress', { companyId: $state.params.companyId, addressId: data.id });
      });
    }
  };

  $scope.cancel = function(){
    if ($scope.contactId!=null){
      $state.go('crm.companies.viewContact', { companyId: $stateParams.companyId, contactId: $scope.contactId });
    } else {
      $state.go('crm.companies.view', { companyId: $stateParams.companyId });
    }
  }

}]);

merohcCRMControllers.controller('AddressViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.companyId=$state.params.companyId;
  $scope.contactId=$state.params.contactId;
  $scope.addressId=$state.params.addressId;

  $scope.updateAddress=function(){
    if ($scope.companyId && $scope.addressId){
      $http.get(merohcConfig.BASE_URL + '/address/'+$scope.addressId).success(function(data){
        $scope.address = data;
      });
    }
  };

  $scope.deleteAddress=function(){
    //FIXME JC 151216 - Should ask confirmation for deletion
    if ($scope.companyId && $scope.addressId){
      $http.delete(merohcConfig.BASE_URL + '/address/'+$scope.addressId).success(function(data){
        if ($scope.companyId){
          $state.go('crm.companies.view', {companyId : $scope.companyId});
        } else {
          $state.go('crm.contacts.view', {contactId : $scope.contactId});
        }
      });
    }
  }

  $scope.editAddress=function(){
      if ($scope.companyId){
        $state.go('crm.companies.editAddress', { companyId: $scope.companyId, addressId: $scope.addressId });
      } else {
        $state.go('crm.contacts.editAddress', { contactId: $scope.contactId, addressId: $scope.addressId });
      }
  }

  $scope.updateAddress();

}]);

merohcCRMControllers.controller('AddressEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.addressId=$state.params.addressId;
  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.saveAddress = function(){
    $http({
      method  : 'POST',
      url     : merohcConfig.BASE_URL + '/address',
      data    : $.param($scope.address),  // pass in data as strings
      headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    })
    .success(function(data) {
      if ($scope.companyId){
        $state.go('crm.companies.viewAddress', { companyId: $scope.companyId, addressId: $scope.addressId });
      } else {
        $state.go('crm.contacts.viewAddress', { contactId: $scope.contactId, addressId: $scope.addressId });
      }
    });
  };

  $scope.updateAddress=function(){
      if ($scope.addressId){
        $http.get(merohcConfig.BASE_URL + '/address/'+$scope.addressId).success(function(data){
          $scope.address = data;
        });
      }
    };

  $scope.cancel = function(){
    if ($scope.companyId){
      $state.go('crm.companies.viewAddress', { companyId: $scope.companyId, addressId: $scope.addressId });
    } else {
      $state.go('crm.contacts.viewAddress', { contactId: $scope.contactId, addressId: $scope.addressId });
    }
  }

  $scope.updateAddress();

}]);

merohcCRMControllers.controller('PhoneCreateController',
    ['$scope', '$http', '$state', '$stateParams', 'merohc-config',
    function ($scope, $http, $state, $stateParams, merohcConfig) {

  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.savePhone = function(){
    if ($scope.contactId!=null){
      $http({
        method  : 'PUT',
        url     : merohcConfig.BASE_URL + '/contact/'+ $scope.contactId +'/phone',
        data    : $scope.phone
      })
      .success(function(data) {
        if ($scope.company==null){
          $state.go('crm.contacts.viewPhone', { contactId: $state.params.phoneId, phoneId: data.id });
        } else {
          $state.go('crm.companies.viewPhone', { companyId: $state.params.phoneId, phoneId: data.id });
        }
      });
    } else {
      $http({
        method  : 'PUT',
        url     : merohcConfig.BASE_URL + '/company/'+ $stateParams.companyId +'/phone',
        data    : $scope.phone
      })
      .success(function(data) {
        $state.go('crm.companies.viewPhone', { companyId: $state.params.companyId, phoneId: data.id });
      });
    }
  };

  $scope.cancel = function(){
    if ($scope.contactId!=null){
      $state.go('crm.companies.viewContact', { companyId: $stateParams.companyId, contactId: $scope.contactId });
    } else {
      $state.go('crm.companies.view', { companyId: $stateParams.companyId });
    }
  }

}]);

merohcCRMControllers.controller('PhoneViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.companyId=$state.params.companyId;
  $scope.contactId=$state.params.contactId;
  $scope.phoneId=$state.params.phoneId;

  $scope.updatePhone=function(){
    if ($scope.companyId && $scope.phoneId){
      $http.get(merohcConfig.BASE_URL + '/phone/'+$scope.phoneId).success(function(data){
        $scope.phone = data;
      });
    }
  };

  $scope.deletePhone=function(){
    //FIXME JC 151216 - Should ask confirmation for deletion
    if ($scope.companyId && $scope.phoneId){
      $http.delete(merohcConfig.BASE_URL + '/phone/'+$scope.phoneId).success(function(data){
        if ($scope.companyId){
          $state.go('crm.companies.view', {companyId : $scope.companyId});
        } else {
          $state.go('crm.contacts.view', {contactId : $scope.contactId});
        }
      });
    }
  }

  $scope.editPhone=function(){
    if ($scope.companyId){
      $state.go('crm.companies.editPhone', { companyId: $scope.companyId, phoneId: $scope.phoneId });
    } else {
      $state.go('crm.contacts.editPhone', { contactId: $scope.contactId, phoneId: $scope.phoneId });
    }
  }

  $scope.updatePhone();

}]);

merohcCRMControllers.controller('PhoneEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.phoneId=$state.params.phoneId;
  $scope.contactId=$state.params.contactId;
  $scope.companyId=$state.params.companyId;

  $scope.savePhone = function(){
    $http({
      method  : 'POST',
      url     : merohcConfig.BASE_URL + '/phone',
      data    : $.param($scope.phone),  // pass in data as strings
      headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    })
    .success(function(data) {
      if ($scope.companyId){
        $state.go('crm.companies.viewPhone', { companyId: $scope.companyId, phoneId: $scope.phoneId });
      } else {
        $state.go('crm.contacts.viewPhone', { contactId: $scope.contactId, phoneId: $scope.phoneId });
      }
    });
  };

  $scope.updatePhone=function(){
    if ($scope.phoneId){
      $http.get(merohcConfig.BASE_URL + '/phone/'+$scope.phoneId).success(function(data){
        $scope.phone = data;
      });
    }
  };

  $scope.cancel = function(){
    if ($scope.companyId){
      $state.go('crm.companies.viewPhone', { companyId: $scope.companyId, phoneId: $scope.phoneId });
    } else {
      $state.go('crm.contacts.viewPhone', { contactId: $scope.contactId, phoneId: $scope.phoneId });
    }
  }

  $scope.updatePhone();

}]);

merohcCRMControllers.controller('ContactListController', ['$scope', '$http', '$state', 'merohc-config' , function ($scope, $http, $state, merohcConfig) {

  $scope.items = '';

  $scope.filteredItems = $scope.items;

  $scope.searchTerm="";

  $scope.selectedItem=$scope.items[0];

  $scope.targetId="";

  $scope.setTargetId=function(targetId){
    $scope.targetId=targetId;
  }

  $scope.selectItem=function(selectedContact){
    $scope.selectedItem = selectedContact;
    $state.go('crm.contacts.view', { contactId: selectedContact.id });
  };

  $scope.selectItemById=function(id){
    for (var index in $scope.items) {
      var contact = $scope.items[index];
      if (contact.id===id){
        $scope.selectedItem=contact;
      }
    }
  }

  $scope.isItemSelected=function(selectedContact){
    return selectedContact===$scope.selectedItem;
  };

  $scope.filter=function(){
    $scope.filteredItems=[];
    for (var index in $scope.items) {
      var contact = $scope.items[index];
      var regexp = new RegExp($scope.searchTerm,"i");
      if (contact.firstName.match(regexp)){
        $scope.filteredItems.push(contact);
      }
    }
  };

  $scope.updateItem = function(oldItem, newItem) {
    for (var index in $scope.items) {
      var contact = $scope.items[index];
      if (contact.id===oldItem.id){
        $scope.items[index]=newItem;
      }
    }

    $scope.filter();
  }

  $scope.addItem = function(newItem) {
    $scope.items.push(newItem);
    $scope.filter();
    $state.go('crm.contacts.view', { contactId: newItem.id })
  }

  $scope.deleteItem = function(oldItem) {

    for (var index in $scope.items) {
      var contact = $scope.items[index];
      if (contact.id===oldItem.id){
        if (index > -1) {
          $scope.items.splice(index, 1);
        }
      }
    }
    $state.go("crm.contacts");
    $scope.refresh();
  }

  $scope.refresh=function(){
    $http.get(merohcConfig.BASE_URL + '/contact').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

}]);

merohcCRMControllers.controller('ContactViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.contactId=$state.params.contactId;

  $scope.updateContact=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId).success(function(data){
        $scope.contact = data;
        $scope.updateCompany();
      });
    }
  };

  $scope.deleteContact=function(){
    //FIXME JC 151212 - Should ask confirmation for deletion
    if ($scope.contactId){
      $http.delete(merohcConfig.BASE_URL + '/contact/'+$scope.contactId).success(function(data){
        $scope.deleteItem($scope.contact);
      });
    }
  }

  $scope.editContact=function(){
    $state.go('crm.contacts.edit', { contactId: $scope.contactId });
  }

  //get back emails for the contact
  $scope.updateEmails=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId+'/email').success(function(data){
        $scope.emails = data;
      });
    }
  };

  //get back addresses for the contact
  $scope.updateAddresses=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId+'/address').success(function(data){
        $scope.addresses = data;
      });
    }
  };

  //get back phones for the contact
  $scope.updatePhones=function(){
    if ($scope.contactId){
      $http.get(merohcConfig.BASE_URL + '/contact/'+$scope.contactId+'/phone').success(function(data){
        $scope.phones = data;
      });
    }
  };

  //get back contact's company
  $scope.updateCompany=function(){
    if ($scope.companyId){
      $http.get(merohcConfig.BASE_URL + '/company/'+$scope.contact.companyId).success(function(data){
        $scope.company = data;
      });
    }
  };

  $scope.updateContact();
  $scope.updateEmails();
  $scope.updateAddresses();
  $scope.updatePhones();

}]);
