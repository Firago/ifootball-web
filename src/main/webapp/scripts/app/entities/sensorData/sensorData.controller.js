'use strict';

angular.module('ifootballApp')
    .controller('SensorDataController', function ($scope, $state, $interval, SensorData, DateUtils) {

        var maxElements = 300;
        var labelsInterval = maxElements / 5;
        var firstLabelExists = false;
        var emptyLabel = '';
        $scope.data = [[]];
        $scope.labels = [];
        $scope.options = {
            animation: false,
            //showScale: false,
            scaleShowGridLines: false,
            showTooltips: false,
            pointDot: false,
            datasetStrokeWidth: 0.5
        };

        for (var i = 0; i < maxElements; i++) {
            $scope.data[0].push(0);
            $scope.labels.push(emptyLabel);
        }

        SensorData.receive().then(null, null, function (data) {
            updateChart(data);
        });

        function updateChart(data) {
            if ($scope.data[0].length >= maxElements) {
                $scope.data[0].shift();
                $scope.labels.shift();
            }
            $scope.data[0].push(data.value);
            $scope.labels.push(getNextLabel(data.time));
        }

        function getNextLabel(time) {
            var date = DateUtils.convertDateTimeFromServer(time);
            var formattedTime = DateUtils.timeFormat(date);

            if (firstLabelExists) {
                if ($scope.labels[maxElements - labelsInterval] != emptyLabel) {
                    return formattedTime;
                } else {
                    return emptyLabel;
                }
            } else {
                firstLabelExists = true;
                return formattedTime;
            }

        }

    });
