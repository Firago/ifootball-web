'use strict';

angular.module('ifootballApp')
    .factory('StatusHistory', function ($resource, DateUtils) {
        return $resource('api/statusHistorys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.time = DateUtils.convertDateTimeFromServer(data.time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('LastStatusHistory', function ($resource, DateUtils) {
        return $resource('api/statusHistorys/last', {}, {
            'get': {
              method: 'GET',
              transformResponse: function (data) {
                  data = angular.fromJson(data);
                  data.time = DateUtils.convertDateTimeFromServer(data.time);
                  return data;
              }
            }
        });
  });
