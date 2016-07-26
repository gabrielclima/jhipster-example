(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SocialNetworkDetailController', SocialNetworkDetailController);

    SocialNetworkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SocialNetwork', 'People', 'Event'];

    function SocialNetworkDetailController($scope, $rootScope, $stateParams, previousState, entity, SocialNetwork, People, Event) {
        var vm = this;

        vm.socialNetwork = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:socialNetworkUpdate', function(event, result) {
            vm.socialNetwork = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
