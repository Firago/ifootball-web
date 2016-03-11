'use strict';

angular.module('ifootballApp')
	.controller('ParameterDeleteController', function($scope, $uibModalInstance, entity, Parameter) {

        $scope.parameter = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Parameter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
