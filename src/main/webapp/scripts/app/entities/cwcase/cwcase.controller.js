'use strict';

angular.module('jhip3App')
    .controller('CwcaseController', function ($scope, Cwcase) {
        $scope.cwcases = [];
        $scope.loadAll = function() {
            Cwcase.query(function(result) {
               $scope.cwcases = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Cwcase.get({id: id}, function(result) {
                $scope.cwcase = result;
                $('#deleteCwcaseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Cwcase.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCwcaseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.cwcase = {createdate: null, casestatus: null, id: null};
        };
    });
