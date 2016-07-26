(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SettingDetailController', SettingDetailController);

    SettingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Setting', 'People'];

    function SettingDetailController($scope, $rootScope, $stateParams, previousState, entity, Setting, People) {
        var vm = this;

        vm.setting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:settingUpdate', function(event, result) {
            vm.setting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
