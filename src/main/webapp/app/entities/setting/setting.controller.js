(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SettingController', SettingController);

    SettingController.$inject = ['$scope', '$state', 'Setting'];

    function SettingController ($scope, $state, Setting) {
        var vm = this;
        
        vm.settings = [];

        loadAll();

        function loadAll() {
            Setting.query(function(result) {
                vm.settings = result;
            });
        }
    }
})();
