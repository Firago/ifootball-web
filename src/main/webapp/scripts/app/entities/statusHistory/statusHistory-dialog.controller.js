'use strict';

angular.module('ifootballApp').controller('StatusHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'StatusHistory',
        function($scope, $stateParams, $uibModalInstance, entity, StatusHistory) {

        $scope.datetimepicker_options = { format : moment().format() }

        $scope.statusHistory = entity;
        $scope.load = function(id) {
            StatusHistory.get({id : id}, function(result) {
                $scope.statusHistory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ifootballApp:statusHistoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.statusHistory.id != null) {
                StatusHistory.update($scope.statusHistory, onSaveSuccess, onSaveError);
            } else {
                StatusHistory.save($scope.statusHistory, onSaveSuccess, onSaveError);
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
