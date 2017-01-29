(function() {
    'use strict';

    angular
        .module('jirapsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('projeto', {
            parent: 'entity',
            url: '/projeto?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jirapsterApp.projeto.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/projeto/projetos.html',
                    controller: 'ProjetoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projeto');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('projeto-detail', {
            parent: 'entity',
            url: '/projeto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jirapsterApp.projeto.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/projeto/projeto-detail.html',
                    controller: 'ProjetoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projeto');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Projeto', function($stateParams, Projeto) {
                    return Projeto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'projeto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('projeto-detail.edit', {
            parent: 'projeto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projeto/projeto-dialog.html',
                    controller: 'ProjetoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Projeto', function(Projeto) {
                            return Projeto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('projeto.new', {
            parent: 'projeto',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projeto/projeto-dialog.html',
                    controller: 'ProjetoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('projeto', null, { reload: 'projeto' });
                }, function() {
                    $state.go('projeto');
                });
            }]
        })
        .state('projeto.edit', {
            parent: 'projeto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projeto/projeto-dialog.html',
                    controller: 'ProjetoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Projeto', function(Projeto) {
                            return Projeto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projeto', null, { reload: 'projeto' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('projeto.delete', {
            parent: 'projeto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projeto/projeto-delete-dialog.html',
                    controller: 'ProjetoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Projeto', function(Projeto) {
                            return Projeto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projeto', null, { reload: 'projeto' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
