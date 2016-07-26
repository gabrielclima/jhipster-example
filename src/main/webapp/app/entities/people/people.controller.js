(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PeopleController', PeopleController);

    PeopleController.$inject = ['$scope', '$state', 'People'];

    function PeopleController ($scope, $state, People) {
        var vm = this;
        
        vm.people = [];

        loadAll();

        function loadAll() {
            People.query(function(result) {
                vm.people = result;
            });
        }
    }
})();
