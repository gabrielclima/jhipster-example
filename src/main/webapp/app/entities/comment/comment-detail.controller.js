(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('CommentDetailController', CommentDetailController);

    CommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Comment', 'People', 'Post'];

    function CommentDetailController($scope, $rootScope, $stateParams, previousState, entity, Comment, People, Post) {
        var vm = this;

        vm.comment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:commentUpdate', function(event, result) {
            vm.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
