(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Event', 'Post', 'SocialNetwork', 'People', 'Tribe'];

    function EventDetailController($scope, $rootScope, $stateParams, previousState, entity, Event, Post, SocialNetwork, People, Tribe) {
        var vm = this;

        vm.event = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
