(function() {
    'use strict';
    angular
        .module('tribosApp')
        .factory('Tribe', Tribe);

    Tribe.$inject = ['$resource'];

    function Tribe ($resource) {
        var resourceUrl =  'api/tribes/:id';

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
