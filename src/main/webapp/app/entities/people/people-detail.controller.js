(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PeopleDetailController', PeopleDetailController);

    PeopleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'People', 'SocialNetwork', 'User', 'Event', 'Post', 'Comment', 'Tribe', 'Setting', 'Picture'];

    function PeopleDetailController($scope, $rootScope, $stateParams, previousState, entity, People, SocialNetwork, User, Event, Post, Comment, Tribe, Setting, Picture) {
        var vm = this;

        vm.people = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:peopleUpdate', function(event, result) {
            vm.people = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
