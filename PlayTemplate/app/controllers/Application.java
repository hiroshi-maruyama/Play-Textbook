package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
        List<User> user_list = models.User.get_active_user_list();
        render(user_list);
    }
}