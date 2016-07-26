(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PeopleDeleteController',PeopleDeleteController);

    PeopleDeleteController.$inject = ['$uibModalInstance', 'entity', 'People'];

    function PeopleDeleteController($uibModalInstance, entity, People) {
        var vm = this;

        vm.people = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            People.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
