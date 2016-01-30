'use strict';

angular.module('ifootballApp').controller('SensorDataDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SensorData',
        function($scope, $stateParams, $uibModalInstance, entity, SensorData) {

        $scope.sensorData = entity;
        $scope.load = function(id) {
            SensorData.get({id : id}, function(result) {
                $scope.sensorData = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ifootballApp:sensorDataUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sensorData.id != null) {
                SensorData.update($scope.sensorData, onSaveSuccess, onSaveError);
            } else {
                SensorData.save($scope.sensorData, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTime = {};

        $scope.datePickerForTime.status = {
            opened: false
        };

        $scope.datePickerForTimeOpen = function($event) {
            $scope.datePickerForTime.status.opened = true;
        };
}]);
