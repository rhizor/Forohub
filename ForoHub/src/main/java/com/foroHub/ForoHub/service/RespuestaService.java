package com.foroHub.ForoHub.service;

import com.foroHub.ForoHub.dto.RespuestaCreateRequest;
import com.foroHub.ForoHub.dto.RespuestaUpdateRequest;
import com.foroHub.ForoHub.exception.ResourceNotFoundException;
import com.foroHub.ForoHub.model.Respuesta;
import com.foroHub.ForoHub.model.Topico;
import com.foroHub.ForoHub.model.Usuario;
import com.foroHub.ForoHub.repository.RespuestaRepository;
import com.foroHub.ForoHub.repository.TopicoRepository;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RespuestaService {

    private final RespuestaRepository respuestaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TopicoRepository topicoRepository;

    @Autowired
    public RespuestaService(RespuestaRepository respuestaRepository, UsuarioRepository usuarioRepository, TopicoRepository topicoRepository) {
        this.respuestaRepository = respuestaRepository;
        this.usuarioRepository = usuarioRepository;
        this.topicoRepository = topicoRepository;
    }

    public Respuesta crearRespuesta(@Valid RespuestaCreateRequest request) {
        Usuario autor = usuarioRepository.findById(request.autorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Topico topico = topicoRepository.findById(request.topicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Tópico no encontrado"));

        Respuesta respuesta = new Respuesta();
        respuesta.setMensaje(request.mensaje());
        respuesta.setFechaCreacion(LocalDateTime.now());
        respuesta.setSolucion(false); // Por defecto, no es una solución
        respuesta.setAutor(autor);
        respuesta.setTopico(topico);

        return respuestaRepository.save(respuesta);
    }

    public List<Respuesta> listarRespuestas() {
        return respuestaRepository.findAll();
    }

    public Respuesta obtenerRespuesta(Integer id) {
        return respuestaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Respuesta no encontrada"));
    }

    public Respuesta actualizarRespuesta(Integer id, @Valid RespuestaUpdateRequest request) {
        Optional<Respuesta> respuestaOptional = respuestaRepository.findById(id);
        if (respuestaOptional.isPresent()) {
            Respuesta respuesta = respuestaOptional.get();
            respuesta.setMensaje(request.mensaje());
            respuesta.setSolucion(request.solucion());
            // No actualizamos fechaCreacion, autor, ni topico ya que son relaciones o datos que no deberían cambiar
            return respuestaRepository.save(respuesta);
        } else {
            throw new ResourceNotFoundException("Respuesta no encontrada");
        }
    }

    public void eliminarRespuesta(Integer id) {
        if (respuestaRepository.existsById(id)) {
            respuestaRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Respuesta no encontrada");
        }
    }
}
