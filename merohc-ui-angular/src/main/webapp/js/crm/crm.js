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



