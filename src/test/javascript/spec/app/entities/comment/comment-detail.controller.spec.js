'use strict';

describe('Controller Tests', function() {

    describe('Comment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComment, MockPeople, MockPost;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComment = jasmine.createSpy('MockComment');
            MockPeople = jasmine.createSpy('MockPeople');
            MockPost = jasmine.createSpy('MockPost');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Comment': MockComment,
                'People': MockPeople,
                'Post': MockPost
            };
            createController = function() {
                $injector.get('$controller')("CommentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tribosApp:commentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
