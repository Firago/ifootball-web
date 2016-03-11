'use strict';

angular.module('ifootballApp')
    .controller('ParameterDetailController', function ($scope, $rootScope, $stateParams, entity, Parameter) {
        $scope.parameter = entity;
        $scope.load = function (id) {
            Parameter.get({id: id}, function(result) {
                $scope.parameter = result;
            });
        };
        var unsubscribe = $rootScope.$on('ifootballApp:parameterUpdate', function(event, result) {
            $scope.parameter = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
