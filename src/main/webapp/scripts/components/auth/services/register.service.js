'use strict';

angular.module('ifootballApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


