'use strict';

angular.module('jhip3App')
    .controller('HearingDetailController', function ($scope, $rootScope, $stateParams, entity, Hearing, Cwcase) {
        $scope.hearing = entity;
        $scope.load = function (id) {
            Hearing.get({id: id}, function(result) {
                $scope.hearing = result;
            });
        };
        $rootScope.$on('jhip3App:hearingUpdate', function(event, result) {
            $scope.hearing = result;
        });
    });
