本ひな形作成までの手順
=============

# プロジェクトの作成
```
$ play new PlayTemplate
```

## application.confのコピー

　application.confには将来的にDBのパスワードを書き込んだりするので，共有しない．
そのため，_baseファイルを作って，設定の変更はこちらにも反映して共有する．

```
$ cd PlayTemplate
$ cp conf/application.conf conf/application.conf_base
```

# ユーザーモデルの作成

　データベースを使えるように設定を変更し，models.User.javaを作成する．

　また，動作確認用のページを作る．

## データベースを使えるように設定を変更する

　application.confの85行目をコメントインする．

修正前
```
# db=mem
```

修正後
```
db=mem
```

　取りあえずメモリデータベース（再起動したら全部消える）にする．

## models.User.javaを作成する

　email(ユーザーIDとして扱う)，パスワード，管理者かどうか，削除済みかどうか，登録日時と最終ログイン日時を保持する．

app/models/User.java
```java
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
```

## app/controllers/Application.javaにユーザー登録機能を作成しつつ一覧表示できるようにする

```java
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
```

## app/views/Application/index.htmlの編集

　ユーザー登録フォームを作り，ユーザー一覧表示を行う．

```html
#{extends 'main.html' /}
#{set title:'PlayTemplate' /}

<h1>ユーザー追加</h1>
#{form @add_user()}
メールアドレス：<input type="email" name="user.email" value="${flash['user.email']}" placeholder="メールアドレス" />
    <span class="error">#{error 'user.email' /}</span><br />
パスワード：<input type="password" name="user.password" value="${flash['user.password']}" placeholder="パスワード" />
    <span class="error">#{error 'user.password' /}</span><br />
<input type='submit' value='ユーザー追加' />
#{/form}

<h1>ユーザー一覧</h1>
<ol>
    #{list items:user_list, as:'user'}
    <li>${user.email} (${user.get_regist_date().format('yyyy/MM/dd HH:mm:ss')})</li>
    #{/list}
</ol>
```

# 起動時に管理者用アカウントを生成するようにする

　@OnApplicationStartを使い，Userテーブルが空だったら管理者用アカウントを作成する．

app/Bootstrap.javaを作成
```java
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
```

models.User.javaに以下のメソッドを追加する
```java
/**
 * コンストラクタ．システム初回起動時の管理者ユーザー作成用
 * @param email
 * @param password  
 */
public User(String email, String password){
    this.email = email;
    this.password = play.libs.Crypto.passwordHash(password);
    this.regist_date = Calendar.getInstance().getTimeInMillis();
    this.isDelete = false;
    this.isAdmin = true;        // 管理者！
}
```

# ログイン，ログアウト機能を作る

　ユーザー登録フォームとユーザー一覧表示はログイン必須エリアにする．

# ログイン画面をカスタムする

## カスタムできる状態にする

## ユーザー登録フォームをログイン画面に用意する