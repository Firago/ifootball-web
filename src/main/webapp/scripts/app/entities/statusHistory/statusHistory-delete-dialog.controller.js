'use strict';

angular.module('ifootballApp')
	.controller('StatusHistoryDeleteController', function($scope, $uibModalInstance, entity, StatusHistory) {

        $scope.statusHistory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            StatusHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
