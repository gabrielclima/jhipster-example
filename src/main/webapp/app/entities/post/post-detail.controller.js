(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Post', 'Comment', 'People', 'Event', 'Tribe'];

    function PostDetailController($scope, $rootScope, $stateParams, previousState, entity, Post, Comment, People, Event, Tribe) {
        var vm = this;

        vm.post = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:postUpdate', function(event, result) {
            vm.post = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
