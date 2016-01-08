'use strict';

angular.module('jhip3App').controller('ChildDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Child', 'Cwcase',
        function($scope, $stateParams, $modalInstance, entity, Child, Cwcase) {

        $scope.child = entity;
        $scope.cwcases = Cwcase.query();
        $scope.load = function(id) {
            Child.get({id : id}, function(result) {
                $scope.child = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhip3App:childUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.child.id != null) {
                Child.update($scope.child, onSaveFinished);
            } else {
                Child.save($scope.child, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
