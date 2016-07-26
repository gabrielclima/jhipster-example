(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('TribeDialogController', TribeDialogController);

    TribeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tribe', 'Picture', 'Post', 'Event', 'People'];

    function TribeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tribe, Picture, Post, Event, People) {
        var vm = this;

        vm.tribe = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pictures = Picture.query();
        vm.posts = Post.query();
        vm.events = Event.query();
        vm.people = People.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tribe.id !== null) {
                Tribe.update(vm.tribe, onSaveSuccess, onSaveError);
            } else {
                Tribe.save(vm.tribe, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tribosApp:tribeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
