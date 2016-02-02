'use strict';

angular.module('ifootballApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensorData', {
                parent: 'entity',
                url: '/sensorData',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ifootballApp.sensorData.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorData/sensorData.html',
                        controller: 'SensorDataController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorData');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                },
                onEnter: function (SensorData) {
                    SensorData.connect();
                    SensorData.subscribe();
                },
                onExit: function (SensorData) {
                    SensorData.unsubscribe();
                    SensorData.disconnect();
                },
            });
    });
