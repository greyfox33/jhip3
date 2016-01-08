'use strict';

angular.module('jhip3App').controller('CompositeController',
    ['$scope', '$http', '$stateParams', 'Cwcase', 'Child', 'Hearing', function($scope, $http, $stateParams, Cwcase, Child, Hearing) {

        $scope.cwcases = [];
        // this is really bad, needs to be cleaned up to not hard code first array. Get rid of repeat in the composite.html
        $scope.load = function(id) {
        	Cwcase.get({id: $stateParams.id}, function(result) {
                $scope.cwcases[0] = result;
            });
        };
        $scope.load($stateParams.id);
  
        $scope.children = [];
        $scope.loadChildren = function() {
            Child.query(function(result) {
               $scope.children = result;
            });
        };
        $scope.loadChildren();
        
        $scope.hearings = [];
        $scope.loadHearings = function() {
            Hearing.query(function(result) {
               $scope.hearings = result;
            });
        };
        $scope.loadHearings();
        // add a refresh or clean here
//        $scope.refresh = function () {
//            $scope.loadAll();
//            $scope.clear();
//        };

    }]);
