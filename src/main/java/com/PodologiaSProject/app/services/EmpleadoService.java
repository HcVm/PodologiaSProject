package com.PodologiaSProject.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.EmpleadoRepository;
import com.PodologiaSProject.app.dao.UsuarioRepository;
import com.PodologiaSProject.app.models.Empleado;
import com.PodologiaSProject.app.models.Rol;
import com.PodologiaSProject.app.models.Usuario;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Empleado crearEmpleado(Empleado empleado) {
        validarDatosEmpleado(empleado);

        if (empleadoRepository.findByDni(empleado.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un empleado con ese DNI.");
        }

        return empleadoRepository.save(empleado);
    }

    public Empleado buscarEmpleadoPorId(Integer id) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(id);
        return empleadoOptional.orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + id));
    }

    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    public Empleado actualizarEmpleado(Empleado empleado) {
        validarDatosEmpleado(empleado);

        Empleado empleadoExistente = buscarEmpleadoPorId(empleado.getId());

        empleadoExistente.setNombre(empleado.getNombre());
        empleadoExistente.setApellido(empleado.getApellido());
        empleadoExistente.setDni(empleado.getDni());
        empleadoExistente.setCargo(empleado.getCargo());

        empleadoExistente.setTelefono(empleado.getTelefono());
        empleadoExistente.setEmail(empleado.getEmail());
        empleadoExistente.setFechaContratacion(empleado.getFechaContratacion());

        if (empleado.getUsuario() != null && empleado.getUsuario().getContrasena() != null && !empleado.getUsuario().getContrasena().isEmpty()) {
            empleadoExistente.getUsuario().setContrasena(passwordEncoder.encode(empleado.getUsuario().getContrasena()));
            usuarioRepository.save(empleadoExistente.getUsuario()); 
        }

        return empleadoRepository.save(empleadoExistente);
    }

    public void eliminarEmpleado(Integer id) {
        Empleado empleado = buscarEmpleadoPorId(id);

        usuarioRepository.delete(empleado.getUsuario());

        empleadoRepository.deleteById(id);
    }

    private void validarDatosEmpleado(Empleado empleado) {
        if (empleado.getNombre() == null || empleado.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }
        if (empleado.getApellido() == null || empleado.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido del empleado es obligatorio.");
        }
        if (empleado.getDni() == null || empleado.getDni().isEmpty()) {
            throw new IllegalArgumentException("El DNI del empleado es obligatorio.");
        }

    }

    public List<Empleado> buscarEmpleadosPorCargo(String cargo) {
        return empleadoRepository.findByCargo(cargo);
    }

    public Empleado buscarEmpleadoPorDni(String dni) {
        return empleadoRepository.findByDni(dni);
    }
}