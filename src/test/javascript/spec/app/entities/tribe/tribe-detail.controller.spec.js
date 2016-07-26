'use strict';

describe('Controller Tests', function() {

    describe('Tribe Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTribe, MockPicture, MockPost, MockEvent, MockPeople;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTribe = jasmine.createSpy('MockTribe');
            MockPicture = jasmine.createSpy('MockPicture');
            MockPost = jasmine.createSpy('MockPost');
            MockEvent = jasmine.createSpy('MockEvent');
            MockPeople = jasmine.createSpy('MockPeople');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Tribe': MockTribe,
                'Picture': MockPicture,
                'Post': MockPost,
                'Event': MockEvent,
                'People': MockPeople
            };
            createController = function() {
                $injector.get('$controller')("TribeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tribosApp:tribeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
