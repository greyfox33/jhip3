'use strict';

angular.module('jhip3App').controller('CwcaseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Cwcase', 'Child',
        function($scope, $stateParams, $modalInstance, entity, Cwcase, Child) {

        $scope.cwcase = entity;
        $scope.childs = Child.query();
        $scope.load = function(id) {
            Cwcase.get({id : id}, function(result) {
                $scope.cwcase = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhip3App:cwcaseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.cwcase.id != null) {
                Cwcase.update($scope.cwcase, onSaveFinished);
            } else {
                Cwcase.save($scope.cwcase, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
