var merohcCRM = angular.module('merohc.referentials', ['ui.router', 'merohc.referentials.controllers']);

merohcCRM.config(
  [        '$stateProvider', '$urlRouterProvider',
  function ($stateProvider,   $urlRouterProvider) {
    $stateProvider
      .state('referentials', {
        abstract: true,
        templateUrl: 'referentials/partials/referentials.html',
        url: '/referentials'
      })

      .state('referentials.invoiceCategory', {
        url: '/invoiceCategories',
        templateUrl: 'referentials/partials/invoiceCategoryList.html',
        controller:'InvoiceCategoryListController'
      })

      .state('referentials.invoiceCategory.add', {
        url: '/add',
        templateUrl: 'referentials/partials/invoiceCategoryCreateCard.html',
        controller:'InvoiceCategoryCreateController'
      })

      .state('referentials.invoiceCategory.view', {
        url: '/:categoryId',
        templateUrl: 'referentials/partials/invoiceCategoryViewCard.html',
        controller: 'InvoiceCategoryViewController'
      })

      .state('referentials.invoiceCategory.edit', {
        url: '/:categoryId/edit',
        templateUrl: 'referentials/partials/invoiceCategoryEditCard.html',
        controller: 'InvoiceCategoryEditController'
      })

      .state('referentials.billCategory', {
        url: '/billCategories',
        templateUrl: 'referentials/partials/billCategoryList.html',
        controller:'BillCategoryListController'
      })

      .state('referentials.billCategory.add', {
        url: '/add',
        templateUrl: 'referentials/partials/billCategoryCreateCard.html',
        controller:'BillCategoryCreateController'
      })

      .state('referentials.billCategory.view', {
        url: '/:categoryId',
        templateUrl: 'referentials/partials/billCategoryViewCard.html',
        controller: 'BillCategoryViewController'
      })

      .state('referentials.billCategory.edit', {
        url: '/:categoryId/edit',
        templateUrl: 'referentials/partials/billCategoryEditCard.html',
        controller: 'BillCategoryEditController'
      })
}]);