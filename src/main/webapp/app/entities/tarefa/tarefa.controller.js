(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .controller('TarefaController', TarefaController);

    TarefaController.$inject = ['$scope', '$state', 'Tarefa'];

    function TarefaController ($scope, $state, Tarefa) {
        var vm = this;

        vm.tarefas = [];

        loadAll();

        function loadAll() {
            Tarefa.query(function(result) {
                vm.tarefas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
