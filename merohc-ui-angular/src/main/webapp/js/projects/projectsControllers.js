var merohcProjectsControllers = angular.module('merohc.projects.controllers', ['ui.router', 'ui.bootstrap']);

merohcProjectsControllers.controller('ProjectListController',
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

  $scope.selectItem=function(selectedProject){
    $scope.selectedItem = selectedProject;
    $state.go('projects.projects.view', { projectId: selectedProject.id });
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
    $state.go('projects.projects.view', { projectId: newItem.id })
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
    $state.go("projects.projects");
    $scope.refresh();
  }

  $scope.refresh=function(){
    $http.get(merohcConfig.BASE_URL + '/project').success(function(data){
      $scope.items = data;
      $scope.filter();
    });
  };

  $scope.refresh();

}]);

merohcBillingControllers.controller('ProjectViewController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig){

  $scope.projectId=$state.params.projectId;

  $scope.updateProject=function(){
    if ($scope.projectId){
      $http.get(merohcConfig.BASE_URL + '/project/'+$scope.projectId).success(function(data){
        $scope.project = data;

      });
    }
  };

  $scope.deleteProject=function(){
    //FIXME JC 160219 - Should ask confirmation for deletion
    if ($scope.projectId){
      $http.delete(merohcConfig.BASE_URL + '/project/'+$scope.projectId).success(function(data){
        $scope.deleteItem($scope.project);
      });
    }
  }

  $scope.editProject=function(){
    $state.go('projects.projects.edit', { projectId: $scope.projectId });
  }

  $scope.updateProject();

}]);

merohcProjectsControllers.controller('ProjectCreateController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.saveProject = function(){
    $http({
            method  : 'PUT',
            url     : merohcConfig.BASE_URL + '/project',
            data    : $scope.project,  // pass in data as strings
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.addItem(data);
            $scope.$parent.selectedItem=data;

            $state.go('projects.projects.view', {projectId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('projects.projects');
  }

}]);

merohcProjectsControllers.controller('ProjectEditController',
    ['$scope', '$http', '$state', 'merohc-config',
    function ($scope, $http, $state, merohcConfig) {

  $scope.projectId=$state.params.projectId;

  $scope.updateProject=function(){
    if ($scope.projectId){
      $http.get(merohcConfig.BASE_URL + '/project/'+$scope.projectId).success(function(data){
        $scope.project = data;
        $scope.oldProject = angular.copy(data);
      });
    }
  };

  $scope.saveProject = function(){
    $http({
            method  : 'POST',
            url     : merohcConfig.BASE_URL + '/project',
            data    : $scope.project,  // pass in data as strings
         })
          .success(function(data) {

            //update company and selectedItem in parent scope
            $scope.updateItem($scope.oldProject, data);
            $scope.$parent.selectedItem=data;

            $state.go('projects.projects.view', {projectId : data.id});
          });
  };

  $scope.cancel = function(){
    $state.go('projects.projects.view', {projectId : $scope.projectId});
  }

  $scope.updateProject();

}]);