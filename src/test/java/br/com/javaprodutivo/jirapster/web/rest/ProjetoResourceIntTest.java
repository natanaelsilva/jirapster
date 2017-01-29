package br.com.javaprodutivo.jirapster.web.rest;

import br.com.javaprodutivo.jirapster.JirapsterApp;

import br.com.javaprodutivo.jirapster.domain.Projeto;
import br.com.javaprodutivo.jirapster.repository.ProjetoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjetoResource REST controller.
 *
 * @see ProjetoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JirapsterApp.class)
public class ProjetoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Inject
    private ProjetoRepository projetoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjetoMockMvc;

    private Projeto projeto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjetoResource projetoResource = new ProjetoResource();
        ReflectionTestUtils.setField(projetoResource, "projetoRepository", projetoRepository);
        this.restProjetoMockMvc = MockMvcBuilders.standaloneSetup(projetoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projeto createEntity(EntityManager em) {
        Projeto projeto = new Projeto()
                .nome(DEFAULT_NOME);
        return projeto;
    }

    @Before
    public void initTest() {
        projeto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjeto() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // Create the Projeto

        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isCreated());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate + 1);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createProjetoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // Create the Projeto with an existing ID
        Projeto existingProjeto = new Projeto();
        existingProjeto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProjeto)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetoRepository.findAll().size();
        // set the field null
        projeto.setNome(null);

        // Create the Projeto, which fails.

        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjetos() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList
        restProjetoMockMvc.perform(get("/api/projetos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get the projeto
        restProjetoMockMvc.perform(get("/api/projetos/{id}", projeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projeto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjeto() throws Exception {
        // Get the projeto
        restProjetoMockMvc.perform(get("/api/projetos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Update the projeto
        Projeto updatedProjeto = projetoRepository.findOne(projeto.getId());
        updatedProjeto
                .nome(UPDATED_NOME);

        restProjetoMockMvc.perform(put("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjeto)))
            .andExpect(status().isOk());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Create the Projeto

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjetoMockMvc.perform(put("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isCreated());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        int databaseSizeBeforeDelete = projetoRepository.findAll().size();

        // Get the projeto
        restProjetoMockMvc.perform(delete("/api/projetos/{id}", projeto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
