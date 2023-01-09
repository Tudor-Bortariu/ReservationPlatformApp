package ro.demo.ReservationPlatformApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ro.demo.ReservationPlatformApp.api.HomeControllerApi;
import ro.demo.ReservationPlatformApp.model.CustomUserDetails;
import ro.demo.ReservationPlatformApp.model.User;
import ro.demo.ReservationPlatformApp.service.IAuthenticationFacade;

@Controller
public class HomeController implements HomeControllerApi {

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Override
    public String homePage(Model model) {
        Authentication authentication = authenticationFacade.getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

            model.addAttribute("userFirstName", user.getFirstName());
        }

        return "home/homePage";
    }
}
