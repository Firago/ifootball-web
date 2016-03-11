'use strict';

angular.module('ifootballApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parameter', {
                parent: 'entity',
                url: '/parameters',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.parameter.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parameter/parameters.html',
                        controller: 'ParameterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parameter');
                        $translatePartialLoader.addPart('parameterKey');
                        $translatePartialLoader.addPart('parameterType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parameter.detail', {
                parent: 'entity',
                url: '/parameter/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.parameter.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parameter/parameter-detail.html',
                        controller: 'ParameterDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parameter');
                        $translatePartialLoader.addPart('parameterKey');
                        $translatePartialLoader.addPart('parameterType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Parameter', function($stateParams, Parameter) {
                        return Parameter.get({id : $stateParams.id});
                    }]
                }
            })
            .state('parameter.new', {
                parent: 'parameter',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/parameter/parameter-dialog.html',
                        controller: 'ParameterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    key: null,
                                    type: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('parameter', null, { reload: true });
                    }, function() {
                        $state.go('parameter');
                    })
                }]
            })
            .state('parameter.edit', {
                parent: 'parameter',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/parameter/parameter-dialog.html',
                        controller: 'ParameterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Parameter', function(Parameter) {
                                return Parameter.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('parameter', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('parameter.delete', {
                parent: 'parameter',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/parameter/parameter-delete-dialog.html',
                        controller: 'ParameterDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Parameter', function(Parameter) {
                                return Parameter.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('parameter', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
