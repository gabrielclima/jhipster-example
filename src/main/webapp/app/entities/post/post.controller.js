(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PostController', PostController);

    PostController.$inject = ['$scope', '$state', 'Post'];

    function PostController ($scope, $state, Post) {
        var vm = this;
        
        vm.posts = [];

        loadAll();

        function loadAll() {
            Post.query(function(result) {
                vm.posts = result;
            });
        }
    }
})();
