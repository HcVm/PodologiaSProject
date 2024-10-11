package com.PodologiaSProject.app.controllers;

import com.PodologiaSProject.app.models.*;
import com.PodologiaSProject.app.models.Rol;
import com.PodologiaSProject.app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recepcionista")

public class RecepcionistaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    // Gestión de citas
    @GetMapping("/citas")
    public String listarCitas(Model model) {
        List<Cita> citas = citaService.listarCitas();
        model.addAttribute("citas", citas);
        return "recepcionista/listar-citas";
    }

    @GetMapping("/citas/nueva")
    public String mostrarFormularioNuevaCita(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("pacientes", pacienteService.listarPacientes());
        model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString()));
        return "recepcionista/formulario-cita";
    }

    @PostMapping("/citas/nueva")
    public String crearCita(@ModelAttribute("cita") @Validated Cita cita, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteService.listarPacientes());
            model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString()));
            return "recepcionista/formulario-cita";
        }
        citaService.crearCita(cita);
        return "redirect:/recepcionista/citas";
    }

    @GetMapping("/citas/{id}/editar")
    public String mostrarFormularioEditarCita(@PathVariable Integer id, Model model) {
        Cita cita = citaService.buscarCitaPorId(id);
        if (cita == null) {
            return "redirect:/recepcionista/citas";
        }
        model.addAttribute("cita", cita);
        model.addAttribute("pacientes", pacienteService.listarPacientes());
        model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString()));
        return "recepcionista/formulario-cita";
    }

    @PostMapping("/citas/{id}/editar")
    public String actualizarCita(@PathVariable Integer id, @ModelAttribute("cita") @Validated Cita cita, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteService.listarPacientes());
            model.addAttribute("podologos", empleadoService.buscarEmpleadosPorCargo(Rol.PODOLOGO.toString()));
            return "recepcionista/formulario-cita";
        }
        cita.setId(id);
        citaService.actualizarCita(cita);
        return "redirect:/recepcionista/citas";
    }

    @GetMapping("/citas/{id}/eliminar")
    public String eliminarCita(@PathVariable Integer id) {
        citaService.eliminarCita(id);
        return "redirect:/recepcionista/citas";
    }

    // Gestión de pacientes
    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        model.addAttribute("pacientes", pacientes);
        return "recepcionista/listar-pacientes";
    }

    @GetMapping("/pacientes/nuevo")
    public String mostrarFormularioNuevoPaciente(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "recepcionista/formulario-paciente";
    }

    @PostMapping("/pacientes/nuevo")
    public String crearPaciente(@ModelAttribute("paciente") @Validated Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            return "recepcionista/formulario-paciente";
        }
        pacienteService.crearPaciente(paciente);
        return "redirect:/recepcionista/pacientes";
    }

    @GetMapping("/pacientes/{id}/editar")
    public String mostrarFormularioEditarPaciente(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        if (paciente == null) {
            return "redirect:/recepcionista/pacientes";
        }
        model.addAttribute("paciente", paciente);
        return "recepcionista/formulario-paciente";
    }

    @PostMapping("/pacientes/{id}/editar")
    public String actualizarPaciente(@PathVariable Integer id, @ModelAttribute("paciente") @Validated Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            return "recepcionista/formulario-paciente";
        }
        paciente.setId(id);
        pacienteService.actualizarPaciente(paciente);
        return "redirect:/recepcionista/pacientes";
    }

    @GetMapping("/pacientes/{id}/eliminar")
    public String eliminarPaciente(@PathVariable Integer id) {
        pacienteService.eliminarPaciente(id);
        return "redirect:/recepcionista/pacientes";
    }

    // Gestión de pagos
    @GetMapping("/pagos")
    public String listarPagos(Model model) {
        List<Pago> pagos = pagoService.listarPagos();
        model.addAttribute("pagos", pagos);
        return "recepcionista/listar-pagos";
    }

    @GetMapping("/pagos/nuevo")
    public String mostrarFormularioNuevoPago(Model model) {
        model.addAttribute("pago", new Pago());
        model.addAttribute("citas", citaService.listarCitas());
        return "recepcionista/formulario-pago";
    }

    @PostMapping("/pagos/nuevo")
    public String crearPago(@ModelAttribute("pago") @Validated Pago pago, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("citas", citaService.listarCitas());
            return "recepcionista/formulario-pago";
        }
        pagoService.crearPago(pago);
        return "redirect:/recepcionista/pagos";
    }


    // Gestión de ventas
    @GetMapping("/ventas")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.listarVentas();
        model.addAttribute("ventas", ventas);
        return "recepcionista/listar-ventas";
    }

    @GetMapping("/ventas/nueva")
    public String mostrarFormularioNuevaVenta(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("pacientes", pacienteService.listarPacientes());
        model.addAttribute("productos", productoService.listarProductos());
        return "recepcionista/formulario-venta";
    }

    @PostMapping("/ventas/nueva")
    public String crearVenta(@ModelAttribute("venta") @Validated Venta venta, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteService.listarPacientes());
            model.addAttribute("productos", productoService.listarProductos());
            return "recepcionista/formulario-venta";
        }
        ventaService.crearVenta(venta);
        return "redirect:/recepcionista/ventas";
    }

}