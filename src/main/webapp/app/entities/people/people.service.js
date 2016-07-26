(function() {
    'use strict';
    angular
        .module('tribosApp')
        .factory('People', People);

    People.$inject = ['$resource', 'DateUtils'];

    function People ($resource, DateUtils) {
        var resourceUrl =  'api/people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthDate = DateUtils.convertLocalDateFromServer(data.birthDate);
                        data.createdAt = DateUtils.convertLocalDateFromServer(data.createdAt);
                        data.updatedAt = DateUtils.convertLocalDateFromServer(data.updatedAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocalDateToServer(data.birthDate);
                    data.createdAt = DateUtils.convertLocalDateToServer(data.createdAt);
                    data.updatedAt = DateUtils.convertLocalDateToServer(data.updatedAt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocalDateToServer(data.birthDate);
                    data.createdAt = DateUtils.convertLocalDateToServer(data.createdAt);
                    data.updatedAt = DateUtils.convertLocalDateToServer(data.updatedAt);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
