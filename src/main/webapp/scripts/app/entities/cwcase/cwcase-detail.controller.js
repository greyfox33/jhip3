'use strict';

angular.module('jhip3App')
    .controller('CwcaseDetailController', function ($scope, $rootScope, $stateParams, entity, Cwcase, Child) {
        $scope.cwcase = entity;
        $scope.load = function (id) {
            Cwcase.get({id: id}, function(result) {
                $scope.cwcase = result;
            });
        };
        $rootScope.$on('jhip3App:cwcaseUpdate', function(event, result) {
            $scope.cwcase = result;
        });
    });
