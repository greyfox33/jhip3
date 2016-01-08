'use strict';

angular.module('jhip3App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hearing', {
                parent: 'entity',
                url: '/hearings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Hearings'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hearing/hearings.html',
                        controller: 'HearingController'
                    }
                },
                resolve: {
                }
            })
            .state('hearing.detail', {
                parent: 'entity',
                url: '/hearing/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Hearing'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hearing/hearing-detail.html',
                        controller: 'HearingDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Hearing', function($stateParams, Hearing) {
                        return Hearing.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hearing.new', {
                parent: 'hearing',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hearing/hearing-dialog.html',
                        controller: 'HearingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {countyCode: null, hearingType: null, courtDept: null, date: null, status: null, caseworker: null, cwcaseid: null, doc: null, image: null, summary: null, attendeeFirst: null, attendeeLast: null, time: null, language: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hearing', null, { reload: true });
                    }, function() {
                        $state.go('hearing');
                    })
                }]
            })
            .state('hearing.edit', {
                parent: 'hearing',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hearing/hearing-dialog.html',
                        controller: 'HearingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Hearing', function(Hearing) {
                                return Hearing.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hearing', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
