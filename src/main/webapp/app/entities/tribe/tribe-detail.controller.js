(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('TribeDetailController', TribeDetailController);

    TribeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tribe', 'Picture', 'Post', 'Event', 'People'];

    function TribeDetailController($scope, $rootScope, $stateParams, previousState, entity, Tribe, Picture, Post, Event, People) {
        var vm = this;

        vm.tribe = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:tribeUpdate', function(event, result) {
            vm.tribe = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
