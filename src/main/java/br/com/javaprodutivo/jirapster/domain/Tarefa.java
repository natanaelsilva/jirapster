package br.com.javaprodutivo.jirapster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import br.com.javaprodutivo.jirapster.domain.enumeration.PrioridadeTarefa;

import br.com.javaprodutivo.jirapster.domain.enumeration.StatusTarefa;

/**
 * A Tarefa.
 */
@Entity
@Table(name = "tarefa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tarefa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 10, max = 100)
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Size(min = 100, max = 255)
    @Column(name = "descricao", length = 255, nullable = false)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false)
    private PrioridadeTarefa prioridade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusTarefa status;

    @NotNull
    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @ManyToOne
    @NotNull
    private User usuarioReq;

    @ManyToOne
    @NotNull
    private User usuarioDev;

    @ManyToOne
    @NotNull
    private Projeto projeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Tarefa titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Tarefa descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PrioridadeTarefa getPrioridade() {
        return prioridade;
    }

    public Tarefa prioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
        return this;
    }

    public void setPrioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public Tarefa status(StatusTarefa status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public Tarefa dataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
        return this;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public User getUsuarioReq() {
        return usuarioReq;
    }

    public Tarefa usuarioReq(User user) {
        this.usuarioReq = user;
        return this;
    }

    public void setUsuarioReq(User user) {
        this.usuarioReq = user;
    }

    public User getUsuarioDev() {
        return usuarioDev;
    }

    public Tarefa usuarioDev(User user) {
        this.usuarioDev = user;
        return this;
    }

    public void setUsuarioDev(User user) {
        this.usuarioDev = user;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public Tarefa projeto(Projeto projeto) {
        this.projeto = projeto;
        return this;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tarefa tarefa = (Tarefa) o;
        if (tarefa.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tarefa{" +
            "id=" + id +
            ", titulo='" + titulo + "'" +
            ", descricao='" + descricao + "'" +
            ", prioridade='" + prioridade + "'" +
            ", status='" + status + "'" +
            ", dataCriacao='" + dataCriacao + "'" +
            '}';
    }
}
