'use strict';

angular.module('jhip3App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('child', {
                parent: 'entity',
                url: '/childs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Childs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/child/childs.html',
                        controller: 'ChildController'
                    }
                },
                resolve: {
                }
            })
            .state('child.detail', {
                parent: 'entity',
                url: '/child/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Child'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/child/child-detail.html',
                        controller: 'ChildDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Child', function($stateParams, Child) {
                        return Child.get({id : $stateParams.id});
                    }]
                }
            })
            .state('child.new', {
                parent: 'child',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/child/child-dialog.html',
                        controller: 'ChildDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {first: null, last: null, dob: null, ssn: null, casefk: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('child', null, { reload: true });
                    }, function() {
                        $state.go('child');
                    })
                }]
            })
            .state('child.edit', {
                parent: 'child',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/child/child-dialog.html',
                        controller: 'ChildDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Child', function(Child) {
                                return Child.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('child', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
