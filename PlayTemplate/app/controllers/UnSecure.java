package controllers;

import play.*;
import play.mvc.*;

import models.*;
import play.data.validation.Valid;

public class UnSecure extends Controller {

    public static void add_user(@Valid User user) {
        if (validation.hasErrors()) {
            params.flash();     // add http parameters to the flash scope
            validation.keep();  // keep the errors for the next request
            redirect("/secure/login");
        }
        
        User new_user = new User(user);
        new_user.save();
        flash.put("success", user.email + " のユーザー登録に成功しました");
        redirect("/secure/login");
    }
}