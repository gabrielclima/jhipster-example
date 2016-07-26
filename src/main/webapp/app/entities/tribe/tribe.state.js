(function() {
    'use strict';

    angular
        .module('tribosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tribe', {
            parent: 'entity',
            url: '/tribe',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tribes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tribe/tribes.html',
                    controller: 'TribeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('tribe-detail', {
            parent: 'entity',
            url: '/tribe/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tribe'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tribe/tribe-detail.html',
                    controller: 'TribeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tribe', function($stateParams, Tribe) {
                    return Tribe.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tribe',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tribe-detail.edit', {
            parent: 'tribe-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tribe/tribe-dialog.html',
                    controller: 'TribeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tribe', function(Tribe) {
                            return Tribe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tribe.new', {
            parent: 'tribe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tribe/tribe-dialog.html',
                    controller: 'TribeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tribeId: null,
                                tribeName: null,
                                tags: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tribe', null, { reload: true });
                }, function() {
                    $state.go('tribe');
                });
            }]
        })
        .state('tribe.edit', {
            parent: 'tribe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tribe/tribe-dialog.html',
                    controller: 'TribeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tribe', function(Tribe) {
                            return Tribe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tribe', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tribe.delete', {
            parent: 'tribe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tribe/tribe-delete-dialog.html',
                    controller: 'TribeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tribe', function(Tribe) {
                            return Tribe.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tribe', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
