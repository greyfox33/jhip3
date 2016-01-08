'use strict';

angular.module('jhip3App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cwcase', {
                parent: 'entity',
                url: '/cwcases',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Cwcases'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cwcase/cwcases.html',
                        controller: 'CwcaseController'
                    }
                },
                resolve: {
                }
            })
            .state('cwcase.detail', {
                parent: 'entity',
                url: '/cwcase/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Cwcase'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cwcase/cwcase-detail.html',
                        controller: 'CwcaseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Cwcase', function($stateParams, Cwcase) {
                        return Cwcase.get({id : $stateParams.id});
                    }]
                }
            })
            .state('cwcase.new', {
                parent: 'cwcase',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cwcase/cwcase-dialog.html',
                        controller: 'CwcaseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {createdate: null, casestatus: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('cwcase', null, { reload: true });
                    }, function() {
                        $state.go('cwcase');
                    })
                }]
            })
            .state('cwcase.edit', {
                parent: 'cwcase',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cwcase/cwcase-dialog.html',
                        controller: 'CwcaseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Cwcase', function(Cwcase) {
                                return Cwcase.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('cwcase', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
