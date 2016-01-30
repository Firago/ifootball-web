'use strict';

describe('Controller Tests', function() {

    describe('StatusHistory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStatusHistory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStatusHistory = jasmine.createSpy('MockStatusHistory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StatusHistory': MockStatusHistory
            };
            createController = function() {
                $injector.get('$controller')("StatusHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ifootballApp:statusHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
