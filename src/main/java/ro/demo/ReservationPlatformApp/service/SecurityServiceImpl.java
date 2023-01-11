package ro.demo.ReservationPlatformApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ro.demo.ReservationPlatformApp.model.CustomUserDetails;
import ro.demo.ReservationPlatformApp.model.User;

@Service
public class SecurityServiceImpl implements SecurityServiceApi{

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Override
    public User getUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }
}
