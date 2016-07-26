(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SettingDialogController', SettingDialogController);

    SettingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Setting', 'People'];

    function SettingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Setting, People) {
        var vm = this;

        vm.setting = entity;
        vm.clear = clear;
        vm.save = save;
        vm.people = People.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.setting.id !== null) {
                Setting.update(vm.setting, onSaveSuccess, onSaveError);
            } else {
                Setting.save(vm.setting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tribosApp:settingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
