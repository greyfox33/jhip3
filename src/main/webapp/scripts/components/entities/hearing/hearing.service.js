'use strict';

angular.module('jhip3App')
    .factory('Hearing', function ($resource, DateUtils) {
        return $resource('api/hearings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    data.time = DateUtils.convertDateTimeFromServer(data.time);
                    return data; //info here
                }
            },
            'update': { method:'PUT' }
        });
    });
