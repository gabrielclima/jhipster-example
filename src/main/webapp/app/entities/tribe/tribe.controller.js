(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('TribeController', TribeController);

    TribeController.$inject = ['$scope', '$state', 'Tribe'];

    function TribeController ($scope, $state, Tribe) {
        var vm = this;
        
        vm.tribes = [];

        loadAll();

        function loadAll() {
            Tribe.query(function(result) {
                vm.tribes = result;
            });
        }
    }
})();
