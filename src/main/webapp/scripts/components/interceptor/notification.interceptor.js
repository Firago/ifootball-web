 'use strict';

angular.module('ifootballApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-ifootballApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-ifootballApp-params')});
                }
                return response;
            }
        };
    });
