'use strict';

angular.module('jhip3App')
    .controller('ChildDetailController', function ($scope, $rootScope, $stateParams, entity, Child, Cwcase) {
        $scope.child = entity;
        $scope.load = function (id) {
            Child.get({id: id}, function(result) {
                $scope.child = result;
            });
        };
        $rootScope.$on('jhip3App:childUpdate', function(event, result) {
            $scope.child = result;
        });
    });
