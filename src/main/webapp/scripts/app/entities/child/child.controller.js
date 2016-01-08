'use strict';

angular.module('jhip3App')
    .controller('ChildController', function ($scope, Child) {
        $scope.childs = [];
        $scope.loadAll = function() {
            Child.query(function(result) {
               $scope.childs = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Child.get({id: id}, function(result) {
                $scope.child = result;
                $('#deleteChildConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Child.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteChildConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.child = {first: null, last: null, dob: null, ssn: null, casefk: null, id: null};
        };
    });
