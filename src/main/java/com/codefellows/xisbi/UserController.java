package com.codefellows.xisbi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class UserController {

    // This annotation allows Spring to resolve and inject collaborating beans into this bean.
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Displays XISBI homepage via index.html template
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String displayIndexTemplate(Model model, Principal p) {

        if (p != null) {
            XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();
            if (userRepo.findById(user.id).isPresent()) {
                model.addAttribute("user", userRepo.findById(user.id).get());
            }
            else {
                model.addAttribute("user", false);
            }
        }else {
            model.addAttribute("user", false);
        }
        return "index";
    }


    // Displays XISBI signup via signup.html template
    @RequestMapping(value="/signup", method= RequestMethod.GET)
    public String displaySignupTemplate(Model model, Principal p) {

        if(p != null){
            XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();
            if (userRepo.findById(user.id).isPresent()) {
                model.addAttribute("user", userRepo.findById(user.id).get());
            } else {
                model.addAttribute("user", false);
            }
        } else{
            model.addAttribute("user", false);
        }

        return "signup";
    }

    // Take in a new XISBI user's information and add that user to the database
    @RequestMapping(value="/signup", method=RequestMethod.POST)
    public RedirectView createNewXisbiUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String dateOfBirth,
            // Since this is required, if I don't check any boxes, I get an error.
            // You could make this optional to allow people to sign up without specifying any interests!
            @RequestParam String partyInterests) {

        // Season the password with some salt
        password = bCryptPasswordEncoder.encode(password);
        XisbiUser newXisbiUser = new XisbiUser(username, password, firstName, lastName, dateOfBirth, partyInterests);
        userRepo.save(newXisbiUser);

        // Auto login for users
        Authentication authentication = new UsernamePasswordAuthenticationToken(newXisbiUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // redirect user back to their profile once created
        return new RedirectView("/my-dashboard");
    }

    // Displays XISBI login via login.html template
    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String displayLoginTemplate(Model model, Principal p) {

        if(p != null){
            XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();

            // I'm surprised by this: if you figure out that someone is logged in, it might be more useful to redirect them somewhere other than the login page.
            if (userRepo.findById(user.id).isPresent()) {
                model.addAttribute("user", userRepo.findById(user.id).get());
            } else {
                model.addAttribute("user", false);
            }
        } else {
            model.addAttribute("user", false);
        }

        return "login";
    }

    @RequestMapping(value = "/login-error", method = RequestMethod.GET)
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        model.addAttribute("user", false);

        return "login";
    }

    // Displays a XISBI user's own dashboard via my-my-dashboard.html template
    @RequestMapping(value="/my-dashboard", method= RequestMethod.GET)
    public String displayMyDashboard(Principal p, Model model) {

        if(p != null){
            XisbiUser user = (XisbiUser) ((UsernamePasswordAuthenticationToken) p).getPrincipal();
            if (userRepo.findById(user.id).isPresent()) {
                model.addAttribute("user", userRepo.findById(user.id).get());
            } else {
                // ditto in these cases: if you determine that nobody is logged in, this should probably redirect somewhere else! Your web security config handles that, but this code still confuses me within the controller.
                model.addAttribute("user", false);
            }
        } else {
            model.addAttribute("user", false);
        }
        return "my-dashboard";
    }
}
