(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .controller('ProjetoDetailController', ProjetoDetailController);

    ProjetoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Projeto'];

    function ProjetoDetailController($scope, $rootScope, $stateParams, previousState, entity, Projeto) {
        var vm = this;

        vm.projeto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jirapsterApp:projetoUpdate', function(event, result) {
            vm.projeto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
