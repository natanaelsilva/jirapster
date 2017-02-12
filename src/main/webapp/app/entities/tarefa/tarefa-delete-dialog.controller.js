(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .controller('TarefaDeleteController',TarefaDeleteController);

    TarefaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tarefa'];

    function TarefaDeleteController($uibModalInstance, entity, Tarefa) {
        var vm = this;

        vm.tarefa = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tarefa.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
