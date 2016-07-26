(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PeopleDialogController', PeopleDialogController);

    PeopleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'People', 'SocialNetwork', 'User', 'Event', 'Post', 'Comment', 'Tribe', 'Setting', 'Picture'];

    function PeopleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, People, SocialNetwork, User, Event, Post, Comment, Tribe, Setting, Picture) {
        var vm = this;

        vm.people = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.socialnetworks = SocialNetwork.query();
        vm.users = User.query();
        vm.events = Event.query();
        vm.posts = Post.query();
        vm.comments = Comment.query();
        vm.tribes = Tribe.query();
        vm.settings = Setting.query();
        vm.pictures = Picture.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.people.id !== null) {
                People.update(vm.people, onSaveSuccess, onSaveError);
            } else {
                People.save(vm.people, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tribosApp:peopleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthDate = false;
        vm.datePickerOpenStatus.createdAt = false;
        vm.datePickerOpenStatus.updatedAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
