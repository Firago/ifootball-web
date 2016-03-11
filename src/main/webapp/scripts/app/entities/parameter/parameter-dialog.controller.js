'use strict';

angular.module('ifootballApp').controller('ParameterDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Parameter',
        function($scope, $stateParams, $uibModalInstance, entity, Parameter) {

        $scope.parameter = entity;
        $scope.load = function(id) {
            Parameter.get({id : id}, function(result) {
                $scope.parameter = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ifootballApp:parameterUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.parameter.id != null) {
                Parameter.update($scope.parameter, onSaveSuccess, onSaveError);
            } else {
                Parameter.save($scope.parameter, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
