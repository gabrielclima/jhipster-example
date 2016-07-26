(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PictureController', PictureController);

    PictureController.$inject = ['$scope', '$state', 'Picture'];

    function PictureController ($scope, $state, Picture) {
        var vm = this;
        
        vm.pictures = [];

        loadAll();

        function loadAll() {
            Picture.query(function(result) {
                vm.pictures = result;
            });
        }
    }
})();
