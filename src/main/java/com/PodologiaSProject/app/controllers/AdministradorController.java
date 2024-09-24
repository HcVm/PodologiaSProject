package com.PodologiaSProject.app.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.PodologiaSProject.app.models.Empleado;
import com.PodologiaSProject.app.models.MovimientoInventario;
import com.PodologiaSProject.app.models.Producto;
import com.PodologiaSProject.app.models.Rol;
import com.PodologiaSProject.app.models.TipoEmpleadoEnum;
import com.PodologiaSProject.app.models.Usuario;
import com.PodologiaSProject.app.services.CitaService;
import com.PodologiaSProject.app.services.EmpleadoService;
import com.PodologiaSProject.app.services.MovimientoInventarioService;
import com.PodologiaSProject.app.services.PacienteService;
import com.PodologiaSProject.app.services.PagoService;
import com.PodologiaSProject.app.services.ProductoService;
import com.PodologiaSProject.app.services.TratamientoService;
import com.PodologiaSProject.app.services.UsuarioService;
import com.PodologiaSProject.app.services.VentaService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private TratamientoService tratamientoService;

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    // Gestión de usuarios
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return  "administrador/listar-usuarios"; 
    }

    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());

        model.addAttribute("roles", Rol.values()); 
        return "administrador/formulario-usuario";
    }

    @PostMapping("/usuarios")
    public String crearUsuario(@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Rol.values());
            return "administrador/formulario-usuario";
        } 
        String contrasena = usuario.getContrasena();

        usuarioService.crearUsuario(usuario, contrasena);
        return "redirect:/administrador/usuarios";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String mostrarFormularioEditarUsuario(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        if (usuario == null) {
            return "redirect:/administrador/usuarios"; 
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "administrador/formulario-usuario";
    }

    @PostMapping("/usuarios/{id}")
    public String actualizarUsuario(@PathVariable Integer id, @ModelAttribute("usuario") @Validated Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Rol.values());
            return "administrador/formulario-usuario";
        }

        usuario.setId(id);
        usuarioService.actualizarUsuario(usuario);
        return "redirect:/administrador/usuarios";
    }

    @GetMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/administrador/usuarios";
    }

    // Gestión de empleados
    @GetMapping("/empleados")
    public String listarEmpleados(Model model) {
        List<Empleado> empleados = empleadoService.listarEmpleados();
        model.addAttribute("empleados", empleados);
        return "administrador/listar-empleados";
    }

    @GetMapping("/empleados/nuevo")
    public String mostrarFormularioNuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("cargos", TipoEmpleadoEnum.values());
        return "administrador/formulario-empleado";
    }

    @PostMapping("/empleados")
    public String crearEmpleado(@ModelAttribute("empleado") @Validated Empleado empleado, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cargos", TipoEmpleadoEnum.values());
            return "administrador/formulario-empleado";
        }

        String contrasena = ""; //Aún tengo que verificar el seteo de contraseñas en usuario o empleado

        empleadoService.crearEmpleado(empleado, contrasena);
        return "redirect:/administrador/empleados";
    }

    @GetMapping("/empleados/{id}/editar")
    public String mostrarFormularioEditarEmpleado(@PathVariable Integer id, Model model) {
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        if (empleado == null) {
            return "redirect:/administrador/empleados";
        }
        model.addAttribute("empleado", empleado);
        model.addAttribute("cargos", TipoEmpleadoEnum.values());
        return "administrador/formulario-empleado";
    }

    @PostMapping("/empleados/{id}")
    public String actualizarEmpleado(@PathVariable Integer id, @ModelAttribute("empleado") @Validated Empleado empleado, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cargos", TipoEmpleadoEnum.values());
            return "administrador/formulario-empleado";
        }

        empleado.setId(id); 
        empleadoService.actualizarEmpleado(empleado);
        return "redirect:/administrador/empleados";
    }

    @GetMapping("/empleados/{id}/eliminar")
    public String eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.eliminarEmpleado(id);
        return "redirect:/administrador/empleados";
    }

    // Generación de informes
    @GetMapping("/informes")
    public String mostrarPaginaInformes() {
        return "administrador/informes"; 
    }

    @GetMapping("/informes/citas-por-podologo")
    public String generarInformeCitasPorPodologo(
            @RequestParam(name = "fechaInicio", required = false) LocalDate fechaInicio,
            @RequestParam(name = "fechaFin", required = false) LocalDate fechaFin,
            Model model) {
        List<Object[]> citasPorPodologo = citaService.obtenerCitasPorPodologo(fechaInicio, fechaFin);
        model.addAttribute("citasPorPodologo", citasPorPodologo);
        return "administrador/informe-citas-por-podologo"; 
    }

    @GetMapping("/informes/ingresos")
    public String generarInformeIngresos(
            @RequestParam(name = "fechaInicio", required = false) LocalDate fechaInicio,
            @RequestParam(name = "fechaFin", required = false) LocalDate fechaFin,
            Model model) {
        BigDecimal totalIngresosCitas = pagoService.calcularTotalIngresosPorCitas(fechaInicio, fechaFin);
        BigDecimal totalIngresosVentas = ventaService.calcularTotalIngresosPorVentas(fechaInicio, fechaFin);
        model.addAttribute("totalIngresosCitas", totalIngresosCitas);
        model.addAttribute("totalIngresosVentas", totalIngresosVentas);
        return "administrador/informe-ingresos"; 
    }

    @GetMapping("/informes/inventario")
    public String generarInformeInventario(Model model) {
        List<Producto> productos = productoService.listarProductos();
        List<MovimientoInventario> movimientos = movimientoInventarioService.listarMovimientos();
        model.addAttribute("productos", productos);
        model.addAttribute("movimientos", movimientos);
        return "administrador/informe-inventario"; 
    }
}
