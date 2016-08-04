'use strict';

describe('Controller Tests', function() {

    describe('People Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPeople, MockUser, MockComment, MockSetting, MockSocialNetwork, MockEvent, MockPost, MockTribe, MockPicture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPeople = jasmine.createSpy('MockPeople');
            MockUser = jasmine.createSpy('MockUser');
            MockComment = jasmine.createSpy('MockComment');
            MockSetting = jasmine.createSpy('MockSetting');
            MockSocialNetwork = jasmine.createSpy('MockSocialNetwork');
            MockEvent = jasmine.createSpy('MockEvent');
            MockPost = jasmine.createSpy('MockPost');
            MockTribe = jasmine.createSpy('MockTribe');
            MockPicture = jasmine.createSpy('MockPicture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'People': MockPeople,
                'User': MockUser,
                'Comment': MockComment,
                'Setting': MockSetting,
                'SocialNetwork': MockSocialNetwork,
                'Event': MockEvent,
                'Post': MockPost,
                'Tribe': MockTribe,
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
