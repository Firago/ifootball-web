'use strict';

angular.module('ifootballApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('statusHistory', {
                parent: 'entity',
                url: '/statusHistorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.statusHistory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/statusHistory/statusHistorys.html',
                        controller: 'StatusHistoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('statusHistory');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('statusHistory.detail', {
                parent: 'entity',
                url: '/statusHistory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.statusHistory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/statusHistory/statusHistory-detail.html',
                        controller: 'StatusHistoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('statusHistory');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StatusHistory', function($stateParams, StatusHistory) {
                        return StatusHistory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('statusHistory.new', {
                parent: 'statusHistory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/statusHistory/statusHistory-dialog.html',
                        controller: 'StatusHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    time: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('statusHistory', null, { reload: true });
                    }, function() {
                        $state.go('statusHistory');
                    })
                }]
            })
            .state('statusHistory.edit', {
                parent: 'statusHistory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/statusHistory/statusHistory-dialog.html',
                        controller: 'StatusHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StatusHistory', function(StatusHistory) {
                                return StatusHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('statusHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('statusHistory.delete', {
                parent: 'statusHistory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/statusHistory/statusHistory-delete-dialog.html',
                        controller: 'StatusHistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['StatusHistory', function(StatusHistory) {
                                return StatusHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('statusHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
