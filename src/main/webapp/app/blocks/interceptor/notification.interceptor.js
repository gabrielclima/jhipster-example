(function() {
    'use strict';

    angular
        .module('tribosApp')
        .factory('notificationInterceptor', notificationInterceptor);

    notificationInterceptor.$inject = ['$q', 'AlertService'];

    function notificationInterceptor ($q, AlertService) {
        var service = {
            response: response
        };

        return service;

        function response (response) {
            var alertKey = response.headers('X-tribosApp-alert');
            if (angular.isString(alertKey)) {
                AlertService.success(alertKey, { param : response.headers('X-tribosApp-params')});
            }
            return response;
        }
    }
})();
