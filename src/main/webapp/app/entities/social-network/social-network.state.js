(function() {
    'use strict';

    angular
        .module('tribosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('social-network', {
            parent: 'entity',
            url: '/social-network',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SocialNetworks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/social-network/social-networks.html',
                    controller: 'SocialNetworkController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('social-network-detail', {
            parent: 'entity',
            url: '/social-network/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SocialNetwork'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/social-network/social-network-detail.html',
                    controller: 'SocialNetworkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SocialNetwork', function($stateParams, SocialNetwork) {
                    return SocialNetwork.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'social-network',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('social-network-detail.edit', {
            parent: 'social-network-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network/social-network-dialog.html',
                    controller: 'SocialNetworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SocialNetwork', function(SocialNetwork) {
                            return SocialNetwork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('social-network.new', {
            parent: 'social-network',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network/social-network-dialog.html',
                    controller: 'SocialNetworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                socialNetworkId: null,
                                socialNetworkName: null,
                                description: null,
                                icon: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('social-network', null, { reload: true });
                }, function() {
                    $state.go('social-network');
                });
            }]
        })
        .state('social-network.edit', {
            parent: 'social-network',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network/social-network-dialog.html',
                    controller: 'SocialNetworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SocialNetwork', function(SocialNetwork) {
                            return SocialNetwork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('social-network', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('social-network.delete', {
            parent: 'social-network',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network/social-network-delete-dialog.html',
                    controller: 'SocialNetworkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SocialNetwork', function(SocialNetwork) {
                            return SocialNetwork.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('social-network', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
