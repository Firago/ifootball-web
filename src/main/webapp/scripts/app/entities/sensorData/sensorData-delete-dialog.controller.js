'use strict';

angular.module('ifootballApp')
	.controller('SensorDataDeleteController', function($scope, $uibModalInstance, entity, SensorData) {

        $scope.sensorData = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SensorData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
