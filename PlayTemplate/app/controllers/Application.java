package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {

    @Before
    public static void set_login_user() {
        User login_user = User.find("byEmail", Auth.connected()).first();
        renderArgs.put("login_user", login_user);
    }
    
    public static void index() {
        render();
    }
    
    @Check("AdminOnly")
    public static void user_list() {
        List<User> user_list = models.User.get_active_user_list();
        render(user_list);
    }
}