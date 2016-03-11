'use strict';

angular.module('ifootballApp')
    .controller('ParameterController', function ($scope, $state, Parameter) {

        $scope.parameters = [];
        $scope.loadAll = function() {
            Parameter.query(function(result) {
               $scope.parameters = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parameter = {
                key: null,
                type: null,
                value: null,
                id: null
            };
        };
    });
