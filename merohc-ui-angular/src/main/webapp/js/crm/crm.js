var merohcCRM = angular.module('merohc.crm', ['ui.router', 'merohc.crm.controllers']);

merohcCRM.config(
  [        '$stateProvider', '$urlRouterProvider',
  function ($stateProvider,   $urlRouterProvider) {
    $stateProvider
      .state('crm', {
        abstract: true,
        templateUrl: 'crm/partials/crm.html',
        url: '/crm'
      })

      .state('crm.companies', {
        url: '/companies',
        templateUrl: 'crm/partials/companies.list.html',
        controller:'CompanyListController'
      })

      .state('crm.companies.add', {
        url: '/add',
        templateUrl: 'crm/partials/companyCreateCard.html',
        controller:'CompanyCreateController'
      })

      .state('crm.companies.view', {
        url: '/:companyId',
        templateUrl: 'crm/partials/companyPanel.html',
        controller: 'CompanyDetailController'
      })

      .state('crm.companies.edit', {
        url: '/:companyId/edit',
        templateUrl: 'crm/partials/companyEditCard.html',
        controller: 'CompanyEditController'
      })

      .state('crm.companies.createContact', {
        url: '/:companyId/contact/add',
        templateUrl:'crm/partials/contactCreateCard.html',
        controller: 'ContactCreateController'
      })

      .state('crm.companies.viewContact', {
        url: '/:companyId/contact/:contactId',
        templateUrl: 'crm/partials/contactPanel.html',
        controller: 'ContactDetailController'
      })

      .state('crm.companies.editContact', {
        url: '/:companyId/contact/:contactId/edit',
        templateUrl: 'crm/partials/contactEditCard.html',
        controller: 'ContactEditController'
      })

      .state('crm.companies.createEmail', {
        url: '/:companyId/email/add',
        params:{
          contactId:null
        },
        templateUrl:'crm/partials/emailCreateCard.html',
        controller: 'EmailCreateController'
      })

      .state('crm.companies.viewEmail', {
        url: '/:companyId/email/:emailId',
        templateUrl: 'crm/partials/emailViewCard.html',
        controller: 'EmailViewController'
      })

      .state('crm.companies.editEmail', {
        url: '/:companyId/email/:emailId/edit',
        templateUrl: 'crm/partials/emailEditCard.html',
        controller: 'EmailEditController'
      })

      .state('crm.companies.createAddress', {
        url: '/:companyId/address/add',
        params:{
          contactId:null
        },
        templateUrl:'crm/partials/addressCreateCard.html',
        controller: 'AddressCreateController'
      })

      .state('crm.companies.viewAddress', {
        url: '/:companyId/address/:addressId',
        templateUrl: 'crm/partials/addressViewCard.html',
        controller: 'AddressViewController'
      })

      .state('crm.companies.editAddress', {
        url: '/:companyId/address/:addressId/edit',
        templateUrl: 'crm/partials/addressEditCard.html',
        controller: 'AddressEditController'
      })

      .state('crm.companies.createPhone', {
        url: '/:companyId/phone/add',
        params:{
          contactId:null
        },
        templateUrl:'crm/partials/phoneCreateCard.html',
        controller: 'PhoneCreateController'
      })

      .state('crm.companies.viewPhone', {
        url: '/:companyId/phone/:phoneId',
        templateUrl: 'crm/partials/phoneViewCard.html',
        controller: 'PhoneViewController'
      })

      .state('crm.companies.editPhone', {
        url: '/:companyId/phone/:phoneId/edit',
        templateUrl: 'crm/partials/phoneEditCard.html',
        controller: 'PhoneEditController'
      })





      .state('crm.contacts', {
        url: '/contacts',
        templateUrl: 'crm/partials/contacts.list.html',
        controller:'ContactListController'
      })

      .state('crm.contacts.view', {
        url: '/:contactId',
        templateUrl: 'crm/partials/contactPanel.html',
        controller: 'ContactViewController'
      })

      .state('crm.contacts.edit', {
        url: '/contact/:contactId/edit',
        templateUrl: 'crm/partials/contactEditCard.html',
        controller: 'ContactEditController'
      })

      .state('crm.contacts.createEmail', {
        url: '/:contactId/email/add',
        templateUrl:'crm/partials/emailCreateCard.html',
        controller: 'EmailCreateController'
      })
}]);



