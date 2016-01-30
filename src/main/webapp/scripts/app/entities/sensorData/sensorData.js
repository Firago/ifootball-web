'use strict';

angular.module('ifootballApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensorData', {
                parent: 'entity',
                url: '/sensorDatas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.sensorData.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorData/sensorDatas.html',
                        controller: 'SensorDataController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorData');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sensorData.detail', {
                parent: 'entity',
                url: '/sensorData/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.sensorData.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorData/sensorData-detail.html',
                        controller: 'SensorDataDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorData');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SensorData', function($stateParams, SensorData) {
                        return SensorData.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sensorData.new', {
                parent: 'sensorData',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sensorData/sensorData-dialog.html',
                        controller: 'SensorDataDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    time: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sensorData', null, { reload: true });
                    }, function() {
                        $state.go('sensorData');
                    })
                }]
            })
            .state('sensorData.edit', {
                parent: 'sensorData',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sensorData/sensorData-dialog.html',
                        controller: 'SensorDataDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SensorData', function(SensorData) {
                                return SensorData.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sensorData', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sensorData.delete', {
                parent: 'sensorData',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sensorData/sensorData-delete-dialog.html',
                        controller: 'SensorDataDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SensorData', function(SensorData) {
                                return SensorData.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sensorData', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
