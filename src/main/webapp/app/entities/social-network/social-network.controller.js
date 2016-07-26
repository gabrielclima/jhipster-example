(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('SocialNetworkController', SocialNetworkController);

    SocialNetworkController.$inject = ['$scope', '$state', 'SocialNetwork'];

    function SocialNetworkController ($scope, $state, SocialNetwork) {
        var vm = this;
        
        vm.socialNetworks = [];

        loadAll();

        function loadAll() {
            SocialNetwork.query(function(result) {
                vm.socialNetworks = result;
            });
        }
    }
})();
