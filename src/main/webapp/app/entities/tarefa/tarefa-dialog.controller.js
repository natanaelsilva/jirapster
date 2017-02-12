(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .controller('TarefaDialogController', TarefaDialogController);

    TarefaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tarefa', 'User', 'Projeto'];

    function TarefaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tarefa, User, Projeto) {
        var vm = this;

        vm.tarefa = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.projetos = Projeto.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tarefa.id !== null) {
                Tarefa.update(vm.tarefa, onSaveSuccess, onSaveError);
            } else {
                Tarefa.save(vm.tarefa, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jirapsterApp:tarefaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataCriacao = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
