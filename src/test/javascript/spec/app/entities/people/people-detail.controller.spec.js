'use strict';

describe('Controller Tests', function() {

    describe('People Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPeople, MockSocialNetwork, MockUser, MockEvent, MockPost, MockComment, MockTribe, MockSetting, MockPicture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPeople = jasmine.createSpy('MockPeople');
            MockSocialNetwork = jasmine.createSpy('MockSocialNetwork');
            MockUser = jasmine.createSpy('MockUser');
            MockEvent = jasmine.createSpy('MockEvent');
            MockPost = jasmine.createSpy('MockPost');
            MockComment = jasmine.createSpy('MockComment');
            MockTribe = jasmine.createSpy('MockTribe');
            MockSetting = jasmine.createSpy('MockSetting');
            MockPicture = jasmine.createSpy('MockPicture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'People': MockPeople,
                'SocialNetwork': MockSocialNetwork,
                'User': MockUser,
                'Event': MockEvent,
                'Post': MockPost,
                'Comment': MockComment,
                'Tribe': MockTribe,
                'Setting': MockSetting,
                'Picture': MockPicture
            };
            createController = function() {
                $injector.get('$controller')("PeopleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tribosApp:peopleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
