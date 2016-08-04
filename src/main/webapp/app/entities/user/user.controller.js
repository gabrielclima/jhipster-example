(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('UserController', UserController);

    UserController.$inject = ['$scope', '$state', 'User'];

    function UserController ($scope, $state, User) {
        var vm = this;
        
        vm.users = [];

        loadAll();

        function loadAll() {
            User.query(function(result) {
                vm.users = result;
            });
        }
    }
})();
