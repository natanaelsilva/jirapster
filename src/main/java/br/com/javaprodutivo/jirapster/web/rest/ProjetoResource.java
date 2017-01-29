package br.com.javaprodutivo.jirapster.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.javaprodutivo.jirapster.domain.Projeto;

import br.com.javaprodutivo.jirapster.repository.ProjetoRepository;
import br.com.javaprodutivo.jirapster.web.rest.util.HeaderUtil;
import br.com.javaprodutivo.jirapster.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Projeto.
 */
@RestController
@RequestMapping("/api")
public class ProjetoResource {

    private final Logger log = LoggerFactory.getLogger(ProjetoResource.class);
        
    @Inject
    private ProjetoRepository projetoRepository;

    /**
     * POST  /projetos : Create a new projeto.
     *
     * @param projeto the projeto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projeto, or with status 400 (Bad Request) if the projeto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projetos")
    @Timed
    public ResponseEntity<Projeto> createProjeto(@Valid @RequestBody Projeto projeto) throws URISyntaxException {
        log.debug("REST request to save Projeto : {}", projeto);
        if (projeto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projeto", "idexists", "A new projeto cannot already have an ID")).body(null);
        }
        Projeto result = projetoRepository.save(projeto);
        return ResponseEntity.created(new URI("/api/projetos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projeto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projetos : Updates an existing projeto.
     *
     * @param projeto the projeto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projeto,
     * or with status 400 (Bad Request) if the projeto is not valid,
     * or with status 500 (Internal Server Error) if the projeto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projetos")
    @Timed
    public ResponseEntity<Projeto> updateProjeto(@Valid @RequestBody Projeto projeto) throws URISyntaxException {
        log.debug("REST request to update Projeto : {}", projeto);
        if (projeto.getId() == null) {
            return createProjeto(projeto);
        }
        Projeto result = projetoRepository.save(projeto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projeto", projeto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projetos : get all the projetos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projetos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/projetos")
    @Timed
    public ResponseEntity<List<Projeto>> getAllProjetos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Projetos");
        Page<Projeto> page = projetoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projetos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projetos/:id : get the "id" projeto.
     *
     * @param id the id of the projeto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projeto, or with status 404 (Not Found)
     */
    @GetMapping("/projetos/{id}")
    @Timed
    public ResponseEntity<Projeto> getProjeto(@PathVariable Long id) {
        log.debug("REST request to get Projeto : {}", id);
        Projeto projeto = projetoRepository.findOne(id);
        return Optional.ofNullable(projeto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projetos/:id : delete the "id" projeto.
     *
     * @param id the id of the projeto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projetos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjeto(@PathVariable Long id) {
        log.debug("REST request to delete Projeto : {}", id);
        projetoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projeto", id.toString())).build();
    }

}
