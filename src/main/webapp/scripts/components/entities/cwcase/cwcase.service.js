'use strict';

angular.module('jhip3App')
    .factory('Cwcase', function ($resource, DateUtils) {
        return $resource('api/cwcases/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdate = DateUtils.convertDateTimeFromServer(data.createdate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
