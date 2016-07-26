'use strict';

describe('Controller Tests', function() {

    describe('Post Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPost, MockComment, MockPeople, MockEvent, MockTribe;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPost = jasmine.createSpy('MockPost');
            MockComment = jasmine.createSpy('MockComment');
            MockPeople = jasmine.createSpy('MockPeople');
            MockEvent = jasmine.createSpy('MockEvent');
            MockTribe = jasmine.createSpy('MockTribe');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Post': MockPost,
                'Comment': MockComment,
                'People': MockPeople,
                'Event': MockEvent,
                'Tribe': MockTribe
            };
            createController = function() {
                $injector.get('$controller')("PostDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tribosApp:postUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
