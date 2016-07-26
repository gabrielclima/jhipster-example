'use strict';

describe('Controller Tests', function() {

    describe('SocialNetwork Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSocialNetwork, MockPeople, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSocialNetwork = jasmine.createSpy('MockSocialNetwork');
            MockPeople = jasmine.createSpy('MockPeople');
            MockEvent = jasmine.createSpy('MockEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SocialNetwork': MockSocialNetwork,
                'People': MockPeople,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("SocialNetworkDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tribosApp:socialNetworkUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
