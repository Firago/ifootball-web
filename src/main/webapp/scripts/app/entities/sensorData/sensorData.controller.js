'use strict';

angular.module('ifootballApp')
    .controller('SensorDataController', function ($scope, $state, SensorData) {

        $scope.sensorDatas = [];
        $scope.loadAll = function() {
            SensorData.query(function(result) {
               $scope.sensorDatas = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sensorData = {
                time: null,
                value: null,
                id: null
            };
        };
    });
