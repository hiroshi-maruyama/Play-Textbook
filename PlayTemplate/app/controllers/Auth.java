package controllers;

import models.User;

/**
 * Play framework組み込みのログイン制御クラス
 * @author maruyama
 */
public class Auth extends Secure.Security {

    /**
     * 組み込みの認証メソッド．ログイン成功ならtrue，失敗ならfalse
     * @param username ユーザー名（メールアドレス）
     * @param password パスワード
     * @return 
     */
    static boolean authentify(String username, String password) {
        return User.authentify(username, password);
    }

    static boolean check(String profile) {
        
        User user = User.find("byEmail", Auth.connected()).first();
        if(user != null){
            if(profile.equals("AdminOnly")){
                return user.isAdmin;
            }
        }
        return false;
    }
}
