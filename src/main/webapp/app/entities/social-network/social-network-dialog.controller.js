(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SocialNetworkDialogController', SocialNetworkDialogController);

    SocialNetworkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SocialNetwork', 'People', 'Event'];

    function SocialNetworkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SocialNetwork, People, Event) {
        var vm = this;

        vm.socialNetwork = entity;
        vm.clear = clear;
        vm.save = save;
        vm.people = People.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.socialNetwork.id !== null) {
                SocialNetwork.update(vm.socialNetwork, onSaveSuccess, onSaveError);
            } else {
                SocialNetwork.save(vm.socialNetwork, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tribosApp:socialNetworkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
