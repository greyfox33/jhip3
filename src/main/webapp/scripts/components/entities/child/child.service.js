'use strict';

angular.module('jhip3App')
    .factory('Child', function ($resource, DateUtils) {
        return $resource('api/childs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dob = DateUtils.convertDateTimeFromServer(data.dob);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
