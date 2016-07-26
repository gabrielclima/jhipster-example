(function() {
    'use strict';

    angular
        .module('tribosApp')
        .controller('PictureDetailController', PictureDetailController);

    PictureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Picture', 'People'];

    function PictureDetailController($scope, $rootScope, $stateParams, previousState, entity, Picture, People) {
        var vm = this;

        vm.picture = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tribosApp:pictureUpdate', function(event, result) {
            vm.picture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
