'use strict';

angular.module('jhip3App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


