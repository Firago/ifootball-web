'use strict';

angular.module('ifootballApp')
    .factory('Parameter', function ($resource, DateUtils) {
        return $resource('api/parameters/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
