var merohcProjects = angular.module('merohc.projects', ['ui.router', 'merohc.projects.controllers']);

merohcBilling.config(
  [        '$stateProvider', '$urlRouterProvider',
  function ($stateProvider,   $urlRouterProvider) {
    $stateProvider
      .state('projects', {
        abstract: true,
        templateUrl: 'projects/partials/projects.html',
        url: '/projects'
      })

      .state('projects.projects', {
        url: '/projects',
        templateUrl: 'projects/partials/projectList.html',
        controller:'ProjectListController'
      })

      .state('projects.projects.add', {
        url: '/add',
        templateUrl: 'projects/partials/projectCreateCard.html',
        controller:'ProjectCreateController'
      })

      .state('projects.projects.view', {
        url: '/:projectId',
        templateUrl: 'projects/partials/projectViewCard.html',
        controller: 'ProjectViewController'
      })

      .state('projects.projects.edit', {
        url: '/:projectId/edit',
        templateUrl: 'projects/partials/projectEditCard.html',
        controller: 'ProjectEditController'
      })
}]);