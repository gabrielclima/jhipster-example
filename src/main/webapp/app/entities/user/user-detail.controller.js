(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('UserDetailController', UserDetailController);

    UserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'User', 'People'];

    function UserDetailController($scope, $rootScope, $stateParams, previousState, entity, User, People) {
        var vm = this;

        vm.user = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:userUpdate', function(event, result) {
            vm.user = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
