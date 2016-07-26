(function() {
    'use strict';
    angular
        .module('tribosApp')
        .factory('Post', Post);

    Post.$inject = ['$resource', 'DateUtils'];

    function Post ($resource, DateUtils) {
        var resourceUrl =  'api/posts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertLocalDateFromServer(data.createdAt);
                        data.updatedAt = DateUtils.convertLocalDateFromServer(data.updatedAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createdAt = DateUtils.convertLocalDateToServer(data.createdAt);
                    data.updatedAt = DateUtils.convertLocalDateToServer(data.updatedAt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createdAt = DateUtils.convertLocalDateToServer(data.createdAt);
                    data.updatedAt = DateUtils.convertLocalDateToServer(data.updatedAt);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
