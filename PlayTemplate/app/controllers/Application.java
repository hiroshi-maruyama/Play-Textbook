package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import play.data.validation.Valid;

public class Application extends Controller {

    public static void index() {
        List<User> user_list = models.User.get_active_user_list();
        render(user_list);
    }

    public static void add_user(@Valid User user) {
        if (validation.hasErrors()) {
            params.flash();     // add http parameters to the flash scope
            validation.keep();  // keep the errors for the next request
            index();
        }
        
        User new_user = new User(user);
        new_user.save();
        index();
    }
}