package com.PodologiaSProject.app.controllers;

import com.PodologiaSProject.app.models.*;
import com.PodologiaSProject.app.models.DiaSemanaEnum;
import com.PodologiaSProject.app.models.EstadoCitaEnum;
import com.PodologiaSProject.app.services.*;
import com.PodologiaSProject.app.utils.JwtUtils;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/podologo")
public class PodologoController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private CitaService citaService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AtencionService atencionService;

    @Autowired
    private HistorialMedicoService historialMedicoService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/disponibilidad")
    public String mostrarFormularioDisponibilidad(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Empleado podologo = obtenerPodologoAutenticado(authentication);

        List<Disponibilidad> disponibilidades = disponibilidadService.buscarDisponibilidadesPorPodologo(podologo);
        model.addAttribute("disponibilidades", disponibilidades);
        model.addAttribute("diasSemana", DiaSemanaEnum.values());
        model.addAttribute("disponibilidad", new Disponibilidad());

        return "podologo/gestionar-disponibilidad";
    }

    @PostMapping("/disponibilidad")
    public String guardarDisponibilidad(@ModelAttribute("disponibilidad") @Validated Disponibilidad disponibilidad, 
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("diasSemana", DiaSemanaEnum.values());
            return "podologo/gestionar-disponibilidad";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Empleado podologo = obtenerPodologoAutenticado(authentication);
        disponibilidad.setPodologo(podologo);

        disponibilidadService.crearDisponibilidad(disponibilidad);

        return "redirect:/podologo/disponibilidad";
    }

    @GetMapping("/citas")
    public String listarCitas(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Empleado podologo = obtenerPodologoAutenticado(authentication);

        List<Cita> citas = citaService.buscarCitasPorPodologo(podologo);
        model.addAttribute("citas", citas);

        return "podologo/listar-citas";
    }

    @GetMapping("/citas/{id}")
    public String mostrarDetallesCita(@PathVariable Integer id, Model model) {
        Cita cita = citaService.buscarCitaPorId(id);
        if (cita == null) {
            return "redirect:/podologo/citas";
        }
        model.addAttribute("cita", cita);
        return "podologo/detalles-cita";
    }

    @GetMapping("/citas/{id}/registrar-atencion")
    public String mostrarFormularioRegistrarAtencion(@PathVariable Integer id, Model model) {
        Cita cita = citaService.buscarCitaPorId(id);
        if (cita == null) {
            return "redirect:/podologo/citas";
        }
        model.addAttribute("cita", cita);
        model.addAttribute("atencion", new Atencion());
        return "podologo/registrar-atencion";
    }

    @PostMapping("/citas/{id}/registrar-atencion")
    public String registrarAtencion(@PathVariable Integer id, @ModelAttribute("atencion") @Validated Atencion atencion, 
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "podologo/registrar-atencion";
        }

        Cita cita = citaService.buscarCitaPorId(id);
        if (cita == null) {
            return "redirect:/podologo/citas";
        }

        atencion.setCita(cita);
        atencionService.crearAtencion(atencion);

        cita.setEstado(EstadoCitaEnum.ATENDIDA);
        citaService.actualizarCita(cita);

        return "redirect:/podologo/citas/" + id;
    }

    @GetMapping("/pacientes/{id}/historial")
    public String mostrarHistorialMedico(@PathVariable Integer id, Model model) {
        List<HistorialMedico> historial = historialMedicoService.buscarHistorialesMedicosPorPaciente(id);
        model.addAttribute("historial", historial);
        return "podologo/historial-medico";
    }

    // Método auxiliar para obtener el podólogo autenticado
    private Empleado obtenerPodologoAutenticado(Authentication authentication) {
    	String token = (String) authentication.getCredentials();
        Integer empleadoId = Integer.parseInt(jwtUtils.getClaimFromToken(token, Claims::getSubject));
        return empleadoService.buscarEmpleadoPorId(empleadoId);
    }
}