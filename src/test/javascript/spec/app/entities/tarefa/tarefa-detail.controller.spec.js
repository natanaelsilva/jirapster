'use strict';

describe('Controller Tests', function() {

    describe('Tarefa Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTarefa, MockUser, MockProjeto;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTarefa = jasmine.createSpy('MockTarefa');
            MockUser = jasmine.createSpy('MockUser');
            MockProjeto = jasmine.createSpy('MockProjeto');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Tarefa': MockTarefa,
                'User': MockUser,
                'Projeto': MockProjeto
            };
            createController = function() {
                $injector.get('$controller')("TarefaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jirapsterApp:tarefaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
