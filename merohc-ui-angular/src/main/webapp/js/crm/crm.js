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
        templateUrl: 'crm/partials/contactViewCard.html',
        controller: 'ContactDetailController'
      })

      .state('crm.companies.editContact', {
        url: '/:companyId/contact/:contactId/edit',
        templateUrl: 'crm/partials/contactEditCard.html',
        controller: 'ContactEditController'
      })

      .state('crm.companies.createEmail', {
        url: '/:companyId/email/add',
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








      .state('crm.companies.viewnote', {
        url: '/:companyId/notes/:noteId',
        templateUrl: 'crm/partials/notePanel.html',
        controller: 'NoteController'
      })

      .state('crm.companies.viewcontactDetails', {
        url: '/:companyId/contactDetails/:contactDetailsId',
        templateUrl: 'crm/partials/contactDetailsPanel.html',
        controller: 'ContactDetailsController'
      })

      .state('crm.persons', {
        url: '/persons',
        templateUrl: 'crm/partials/personsList.html',
        controller: 'PersonListController'
      })

      .state('crm.persons.add', {
        url: '/add',
        templateUrl: 'crm/partials/personCreateCard.html',
        controller:'PersonCreateController'
      })

      .state('crm.persons.view', {
        url: '/:personId',
        templateUrl: 'crm/partials/personPanel.html',
        controller: 'PersonDetailController'
      })
}]);



