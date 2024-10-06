package com.PodologiaSProject.app.config;

import com.PodologiaSProject.app.models.Usuario;
import com.PodologiaSProject.app.dao.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
        }

        return new User(usuario.getNombreUsuario(), usuario.getContrasena(), getAuthorities(usuario));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        return Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().toString())); 
    }
}