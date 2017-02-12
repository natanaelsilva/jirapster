(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .controller('TarefaDetailController', TarefaDetailController);

    TarefaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tarefa', 'User', 'Projeto'];

    function TarefaDetailController($scope, $rootScope, $stateParams, previousState, entity, Tarefa, User, Projeto) {
        var vm = this;

        vm.tarefa = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jirapsterApp:tarefaUpdate', function(event, result) {
            vm.tarefa = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
