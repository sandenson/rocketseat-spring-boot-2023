package com.sandenson.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sandenson.todolist.users.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (request.getServletPath().matches("^/tasks(?:/?$|/.*)")) {
            var encodedAuthorization = request.getHeader("Authorization").substring("Basic".length()).trim();
            var authorization = new String(Base64.getDecoder().decode(encodedAuthorization));
            var credentials = authorization.split(":");
            
            var username = credentials[0];
            var password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401, "Unauthorized user");
            } else {
                var verified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified;

                if (!verified) {
                    response.sendError(401, "Incorrect password");
                } else {
                    request.setAttribute("userId", user.getId());
                    chain.doFilter(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
