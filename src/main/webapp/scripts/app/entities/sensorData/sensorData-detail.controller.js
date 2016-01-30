'use strict';

angular.module('ifootballApp')
    .controller('SensorDataDetailController', function ($scope, $rootScope, $stateParams, entity, SensorData) {
        $scope.sensorData = entity;
        $scope.load = function (id) {
            SensorData.get({id: id}, function(result) {
                $scope.sensorData = result;
            });
        };
        var unsubscribe = $rootScope.$on('ifootballApp:sensorDataUpdate', function(event, result) {
            $scope.sensorData = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
