import play.*;
import models.User;
import play.jobs.*;

/**
 * 初回起動時の処理
 * @author maruyama
 */
@OnApplicationStart
public class Bootstrap extends Job {

    @Override
    public void doJob() {
        // 管理者ユーザーの作成
        if(User.count() == 0){
            User user = new User("admin@example.com", "admin");
            user.save();
        }
    }
}
