(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SocialNetworkDeleteController',SocialNetworkDeleteController);

    SocialNetworkDeleteController.$inject = ['$uibModalInstance', 'entity', 'SocialNetwork'];

    function SocialNetworkDeleteController($uibModalInstance, entity, SocialNetwork) {
        var vm = this;

        vm.socialNetwork = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SocialNetwork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
