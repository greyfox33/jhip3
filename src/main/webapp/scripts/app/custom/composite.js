'use strict';

// this is where app.js definitions are held, telling angular what to do when state changes
angular.module('jhip3App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('composite', {
                parent: 'entity',
                url: '/composite/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Maintain a Case'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/custom/composite.html',
                        controller: 'CompositeController'
                    }
                },
                resolve: {
                }
            })
    });
