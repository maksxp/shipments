package com.siaivo.shipments.configuration;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            if(authority.getAuthority().equalsIgnoreCase("admin")) {
                try {
                    redirectStrategy.sendRedirect(request, response, "/admin/allUsers");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(authority.getAuthority().equalsIgnoreCase("salesManagement")) {
                try {
                    redirectStrategy.sendRedirect(request, response, "/salesManagement/thisWeekShipments");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(authority.getAuthority().equalsIgnoreCase("salesSupport")) {
                try {
                    redirectStrategy.sendRedirect(request, response, "/salesSupport/thisWeekShipments");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else if(authority.getAuthority().equalsIgnoreCase("logist")) {
                try {
                    redirectStrategy.sendRedirect(request, response, "/logist/logistBidsList");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                throw new IllegalStateException();
            }
        });

    }

}
