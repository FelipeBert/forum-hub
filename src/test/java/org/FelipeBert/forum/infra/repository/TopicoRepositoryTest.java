package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicoRepositoryTest {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private TestEntityManager em;

    private Long cursoId;

    @BeforeEach
    void setUp(){
        var curso = cadastrarCurso();

        var usuario = cadastrarUsuario();

        cadastrarTopico(usuario, curso);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tópicos ao buscar por ID de curso válido")
    void testFindByCursoIdCenario1(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topico> topicos = topicoRepository.findTopicosByCursoId(cursoId,pageable);

        assertThat(topicos).isNotEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar por ID de curso inválido")
    void testFindByCursoIdCenario2(){
        Pageable pageable = PageRequest.of(0, 10);
        Long idInvalido = 999L;

        Page<Topico> topicos = topicoRepository.findTopicosByCursoId(idInvalido,pageable);

        assertThat(topicos).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar tópicos ao buscar por ano de criação válido")
    void testFindByYearCenario1(){
        int year = 2021;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topico> topicos = topicoRepository.findTopicosByYear(year,pageable);

        assertThat(topicos).isNotEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar por ano de criação inexistente")
    void testFindByYearCenario2(){
        int year = 3000;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topico> topicos = topicoRepository.findTopicosByYear(year,pageable);

        assertThat(topicos).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar os 10 tópicos mais antigos ordenados por data de criação")
    void testFindTop10ByDataCriacaoAscCenario1(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Topico> topicos = topicoRepository.findTop10ByOrderByDataCriacaoAsc(pageable);

        assertThat(topicos).isNotEmpty();
    }

    private Curso cadastrarCurso(){
        Curso curso = (new Curso(null, "nome", Categoria.exatas, null));
        em.persist(curso);
        cursoId = curso.getId();
        return curso;
    }

    private Usuario cadastrarUsuario(){
        Usuario usuario = (new Usuario(null, "nome", "email@email.com", "senha", null, null, true));
        em.persist(usuario);
        return usuario;
    }

    private void cadastrarTopico(Usuario usuario, Curso curso){
        Topico topico = new Topico();
        topico.setTitulo("Dúvida sobre Spring Boot");
        topico.setMensagem("Como usar DataJpaTest?");
        topico.setDataCriacao(LocalDateTime.now().minusYears(3));
        topico.setStatus(Status.NAORESPONDIDO);
        topico.setAutor(usuario);
        topico.setCurso(curso);

        em.persist(topico);
    }
}