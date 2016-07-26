(function() {
    'use strict';
    angular
        .module('tribosApp')
        .factory('Setting', Setting);

    Setting.$inject = ['$resource'];

    function Setting ($resource) {
        var resourceUrl =  'api/settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
