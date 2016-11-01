(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'Upload', 'entity', 'Event', 'Post', 'SocialNetwork', 'People', 'Tribe'];

    function EventDialogController($timeout, $scope, $stateParams, $uibModalInstance, Upload, entity, Event, Post, SocialNetwork, People, Tribe) {
        var vm = this;
        
        vm.event = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.posts = Post.query();
        vm.socialnetworks = SocialNetwork.query();
        vm.people = People.query();
        vm.tribes = Tribe.query();

        $timeout(function() {
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            //vm.isSaving = true;
            console.log(angular.toJson(vm.event));

            var file = vm.event.picture;
            var data = {
                    pictureTitle: vm.event.eventName,
                    description: vm.event.description,
                    type: 'EVENT',
                    file: file,
                };

            
            if(file != null) {
                file.upload = Upload.upload({
                    url: '/api/pictures/upload',
                    data: data
                });

                file.upload.then(function(response) {
                    
                    vm.event.picture = response.data;
                    console.log(response.data.id);

                    //Send the event data using the picture uploaded.
                    if (vm.event.id !== null) {
                        Event.update(vm.event, onSaveSuccess, onSaveError);
                    } else {
                        Event.save(vm.event, onSaveSuccess, onSaveError);
                    }
                    $timeout(function() {
                        file.result = response.data;
                    });

                }, function(response) {

                    console.log(response);

                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;

                }, function(evt) {
                    // Math.min is to fix IE which reports 200% sometimes
                    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
            } else {
                //Send the event data using the picture uploaded.
                if (vm.event.id !== null) {
                    Event.update(vm.event, onSaveSuccess, onSaveError);
                } else {
                    Event.save(vm.event, onSaveSuccess, onSaveError);
                }
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('tribosApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();