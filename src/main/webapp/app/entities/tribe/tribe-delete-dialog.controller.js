(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('TribeDeleteController',TribeDeleteController);

    TribeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tribe'];

    function TribeDeleteController($uibModalInstance, entity, Tribe) {
        var vm = this;

        vm.tribe = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tribe.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
