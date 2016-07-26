(function() {
    'use strict';
    angular
        .module('tribosApp')
        .factory('SocialNetwork', SocialNetwork);

    SocialNetwork.$inject = ['$resource'];

    function SocialNetwork ($resource) {
        var resourceUrl =  'api/social-networks/:id';

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
