(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tarefa', {
            parent: 'entity',
            url: '/tarefa',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jirapsterApp.tarefa.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarefa/tarefas.html',
                    controller: 'TarefaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tarefa');
                    $translatePartialLoader.addPart('prioridadeTarefa');
                    $translatePartialLoader.addPart('statusTarefa');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tarefa-detail', {
            parent: 'entity',
            url: '/tarefa/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jirapsterApp.tarefa.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarefa/tarefa-detail.html',
                    controller: 'TarefaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tarefa');
                    $translatePartialLoader.addPart('prioridadeTarefa');
                    $translatePartialLoader.addPart('statusTarefa');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tarefa', function($stateParams, Tarefa) {
                    return Tarefa.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tarefa',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tarefa-detail.edit', {
            parent: 'tarefa-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-dialog.html',
                    controller: 'TarefaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarefa.new', {
            parent: 'tarefa',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-dialog.html',
                    controller: 'TarefaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titulo: null,
                                descricao: null,
                                prioridade: null,
                                status: null,
                                dataCriacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tarefa', null, { reload: 'tarefa' });
                }, function() {
                    $state.go('tarefa');
                });
            }]
        })
        .state('tarefa.edit', {
            parent: 'tarefa',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-dialog.html',
                    controller: 'TarefaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarefa', null, { reload: 'tarefa' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarefa.delete', {
            parent: 'tarefa',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-delete-dialog.html',
                    controller: 'TarefaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarefa', null, { reload: 'tarefa' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
