'use strict';

angular.module('ifootballApp')
    .controller('StatusHistoryController', function ($scope, $state, StatusHistory) {

        $scope.statusHistorys = [];
        $scope.loadAll = function() {
            StatusHistory.query(function(result) {
               $scope.statusHistorys = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.statusHistory = {
                time: null,
                status: null,
                id: null
            };
        };
    });
