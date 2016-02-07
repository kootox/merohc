var merohcApp = angular.module('merohcApp', ['ngRoute','ui.select','ngSanitize', 'ui.bootstrap', 'ui.router', 'merohc.crm', 'merohc.referentials', 'merohc.billing', 'merohc.controllers']);

merohcApp.run(
  [        '$rootScope', '$state', '$stateParams',
  function ($rootScope,   $state,   $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
}]);


merohcApp.config(
  [        '$stateProvider', '$urlRouterProvider',
  function ($stateProvider,   $urlRouterProvider) {

  $urlRouterProvider.when('','/');

    //////////////////////////
    // State Configurations //
    //////////////////////////
    // Use $stateProvider to configure your states.
    $stateProvider
    //////////
    // Home //
    //////////
    .state("home", {
      url: "/",
      templateUrl: 'home.html'
    })

    ///////////
    // About //
    ///////////
    .state('about', {
      url: '/about',
      templateUrl: 'about.html'
    });
}]);

merohcApp.constant('merohc-config', {
  BASE_URL : "http://localhost:8081/services/v1",
});