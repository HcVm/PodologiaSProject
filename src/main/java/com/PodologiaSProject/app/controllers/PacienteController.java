package com.PodologiaSProject.app.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PodologiaSProject.app.models.Cita;
import com.PodologiaSProject.app.models.Disponibilidad;
import com.PodologiaSProject.app.models.Empleado;
import com.PodologiaSProject.app.models.Paciente;
import com.PodologiaSProject.app.models.Rol;
import com.PodologiaSProject.app.services.CitaService;
import com.PodologiaSProject.app.services.DisponibilidadService;
import com.PodologiaSProject.app.services.EmpleadoService;
import com.PodologiaSProject.app.services.PacienteService;


@Controller
@RequestMapping("/paciente")

public class PacienteController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @GetMapping("/citas/solicitar")
    public String mostrarFormularioSolicitudCita(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString())); 
        return "paciente/solicitar-cita";
    }

    @PostMapping("/citas/solicitar")
    public String solicitarCita(@ModelAttribute("cita") @Validated Cita cita, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString()));
            return "paciente/solicitar-cita";
        }

        Empleado podologo = empleadoService.buscarEmpleadoPorId(cita.getPodologo().getId());
        cita.setPodologo(podologo);

        if (!validarDisponibilidad(cita)) {
            model.addAttribute("errorDisponibilidad", "El podólogo no está disponible en ese horario.");
            model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString()));
            return "paciente/solicitar-cita";
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(cita.getPaciente().getNombre());
        paciente.setApellido(cita.getPaciente().getApellido());
        paciente.setDni(cita.getPaciente().getDni());
        paciente.setTelefono(cita.getPaciente().getTelefono());
        paciente.setEmail(cita.getPaciente().getEmail());
        paciente.setFechaNacimiento(cita.getPaciente().getFechaNacimiento());
        paciente.setDireccion(cita.getPaciente().getDireccion());
        paciente = pacienteService.crearPaciente(paciente);

        cita.setPaciente(paciente);
        citaService.crearCita(cita);

        return "redirect:/paciente/citas?dni=" + paciente.getDni(); 
    }

    @GetMapping("/citas")
    public String listarCitas(@RequestParam(name = "dni") String dni, Model model) {
        Paciente paciente = pacienteService.buscarPacientePorDni(dni);
        if (paciente != null) {
            List<Cita> citas = citaService.buscarCitasPorPaciente(paciente);
            model.addAttribute("citas", citas);
            model.addAttribute("paciente", paciente);
        } else {
            model.addAttribute("error", "No se encontraron citas para el DNI proporcionado.");
        }
        return "paciente/listar-citas";
    }


    private boolean validarDisponibilidad(Cita cita) {
        LocalDate fechaCita = cita.getFechaHora().toLocalDate();
        DayOfWeek diaSemana = fechaCita.getDayOfWeek();
        LocalTime horaInicio = cita.getFechaHora().toLocalTime();
        LocalTime horaFin = horaInicio.plusMinutes(30); 

        List<Disponibilidad> disponibilidades = disponibilidadService.buscarDisponibilidadesPorPodologoYDiaSemana(
                cita.getPodologo(), diaSemana);

        return disponibilidades.stream()
                .anyMatch(d -> horaInicio.isAfter(d.getHoraInicio()) && horaFin.isBefore(d.getHoraFin()));
    }
}