'use strict';

angular.module('ifootballApp')
    .controller('StatusHistoryDetailController', function ($scope, $rootScope, $stateParams, entity, StatusHistory) {
        $scope.statusHistory = entity;
        $scope.load = function (id) {
            StatusHistory.get({id: id}, function(result) {
                $scope.statusHistory = result;
            });
        };
        var unsubscribe = $rootScope.$on('ifootballApp:statusHistoryUpdate', function(event, result) {
            $scope.statusHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
