package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

/**
 * ユーザ情報エンティティ
 * @author maruyama
 */
@Entity
public class User extends Model {
    
    @Required
    @Email
    @Unique
    /**
     * ユーザID（メールアドレス）
     */
    public String email;
    
    @Required
    /**
     * パスワード（暗号化済み）
     */
    public String password;
    /**
     * 登録日時
     */
    public Long regist_date;
    /**
     * 最終ログイン日時
     */
    public Long latest_login_date;
    /**
     * ユーザ削除フラグ．削除済みならtrue
     */
    public Boolean isDelete;
    /**
     * 管理者かどうか．管理者ならtrue
     */
    public Boolean isAdmin;
    
    /**
     * コンストラクタ．パスワードを暗号化して保存する
     * @param user フォームから受け取ったUser
     */
    public User(User user){
        this.email = user.email;
        this.password = play.libs.Crypto.passwordHash(user.password);
        this.regist_date = Calendar.getInstance().getTimeInMillis();
        this.isDelete = false;
        this.isAdmin = false;
    }
    
    /**
     * 登録日時を取得する<br />
     * getTimeInMillisで取得して保存しておいたLong値をDate型に変更する
     * @return 登録日時（Date型）
     */
    public Date get_regist_date(){
        Date date = new Date(this.regist_date);
        return date;
    }
    
    /**
     * 有効なユーザーアカウント（isDeleteがfalse）のリストを取得する
     * @return 有効なユーザーアカウントのリスト
     */
    public static List<User> get_active_user_list(){
        return User.find("byIsDelete", false).fetch();
    }
}
