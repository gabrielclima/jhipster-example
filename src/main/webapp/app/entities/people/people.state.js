(function() {
    'use strict';

    angular
        .module('tribosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('people', {
            parent: 'entity',
            url: '/people',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'People'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/people/people.html',
                    controller: 'PeopleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('people-detail', {
            parent: 'entity',
            url: '/people/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'People'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/people/people-detail.html',
                    controller: 'PeopleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'People', function($stateParams, People) {
                    return People.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'people',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('people-detail.edit', {
            parent: 'people-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/people/people-dialog.html',
                    controller: 'PeopleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['People', function(People) {
                            return People.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('people.new', {
            parent: 'people',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/people/people-dialog.html',
                    controller: 'PeopleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                peopleId: null,
                                birthDate: null,
                                gender: null,
                                prefferedTribes: null,
                                createdAt: null,
                                updatedAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('people', null, { reload: true });
                }, function() {
                    $state.go('people');
                });
            }]
        })
        .state('people.edit', {
            parent: 'people',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/people/people-dialog.html',
                    controller: 'PeopleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['People', function(People) {
                            return People.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('people', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('people.delete', {
            parent: 'people',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/people/people-delete-dialog.html',
                    controller: 'PeopleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['People', function(People) {
                            return People.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('people', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
