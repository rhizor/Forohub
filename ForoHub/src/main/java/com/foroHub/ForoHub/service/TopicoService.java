// src/main/java/com/foroHub/ForoHub/service/TopicoService.java
package com.foroHub.ForoHub.service;

import com.foroHub.ForoHub.domain.dto.DatosRegistroTopico;
import com.foroHub.ForoHub.domain.dto.DatosRespuestaTopico;
import com.foroHub.ForoHub.domain.models.Curso;
import com.foroHub.ForoHub.domain.models.Topico;
import com.foroHub.ForoHub.domain.models.Usuario;
import com.foroHub.ForoHub.repository.CursoRepository;
import com.foroHub.ForoHub.repository.TopicoRepository;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public DatosRespuestaTopico registrar(DatosRegistroTopico datos) {
        Usuario autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));

        Curso curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        Topico topico = new Topico();
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        topico.setAutor(autor);
        topico.setCurso(curso);

        topicoRepository.save(topico);

        return convertirADatosRespuesta(topico);
    }

    public Page<DatosRespuestaTopico> listar(Pageable paginacion) {
        return topicoRepository.findAllByActivoTrue(paginacion)
                .map(this::convertirADatosRespuesta);
    }

    public DatosRespuestaTopico detallar(Long id) {
        Topico topico = topicoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        return convertirADatosRespuesta(topico);
    }

    @Transactional
    public DatosRespuestaTopico actualizar(Long id, DatosRegistroTopico datos) {
        Topico topico = topicoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));

        if (datos.autorId() != null) {
            Usuario autor = usuarioRepository.findById(datos.autorId())
                    .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado"));
            topico.setAutor(autor);
        }

        if (datos.cursoId() != null) {
            Curso curso = cursoRepository.findById(datos.cursoId())
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
            topico.setCurso(curso);
        }

        topico.actualizar(datos.titulo(), datos.mensaje());
        return convertirADatosRespuesta(topico);
    }

    @Transactional
    public void eliminar(Long id) {
        Topico topico = topicoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        topico.setActivo(false);
    }

    private DatosRespuestaTopico convertirADatosRespuesta(Topico topico) {
        return new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                new DatosRespuestaTopico.DatosAutor(
                        topico.getAutor().getId(),
                        topico.getAutor().getNombre()),
                new DatosRespuestaTopico.DatosCurso(
                        topico.getCurso().getId(),
                        topico.getCurso().getNombre())
        );
    }
}
