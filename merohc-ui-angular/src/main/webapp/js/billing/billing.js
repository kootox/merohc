var merohcBilling = angular.module('merohc.billing', ['ui.router', 'merohc.billing.controllers']);

merohcBilling.config(
  [        '$stateProvider', '$urlRouterProvider',
  function ($stateProvider,   $urlRouterProvider) {
    $stateProvider
      .state('billing', {
        abstract: true,
        templateUrl: 'billing/partials/billing.html',
        url: '/billing'
      })

      .state('billing.invoices', {
        url: '/invoices',
        templateUrl: 'billing/partials/invoiceList.html',
        controller:'InvoiceListController'
      })

      .state('billing.invoices.add', {
        url: '/add',
        templateUrl: 'billing/partials/invoiceCreateCard.html',
        controller:'InvoiceCreateController'
      })

      .state('billing.invoices.view', {
        url: '/:invoiceId',
        templateUrl: 'billing/partials/invoiceViewCard.html',
        controller: 'InvoiceViewController'
      })

      .state('billing.invoices.edit', {
        url: '/:invoiceId/edit',
        templateUrl: 'billing/partials/invoiceEditCard.html',
        controller: 'InvoiceEditController'
      })
}]);