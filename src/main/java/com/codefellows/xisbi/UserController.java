package com.codefellows.xisbi;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    // This annotation allows Spring to resolve and inject collaborating beans into this bean.
    @Autowired
    private UserRepository userRepo;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Displays XISBI homepage via index.html template
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String displayIndexTemplate() {
        return "index";
    }

}
