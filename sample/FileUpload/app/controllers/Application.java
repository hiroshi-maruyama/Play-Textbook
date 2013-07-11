package controllers;

import java.io.File;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        List<DataFile> data_list = DataFile.get_list();
        render(data_list);
    }

    public static void upload(play.data.Upload data) {
        DataFile.upload_file(data);
        flash.put("success", data.getFileName() + " をアップロードしました");
        index();
    }

    public static void delete(Long id_of_DataFile) {
        DataFile data_file = DataFile.findById(id_of_DataFile);
        data_file.delete_file(id_of_DataFile);
        flash.put("success", data_file.file_name + " を削除しました");
        index();
    }

    public static void download(Long id_of_DataFile) {
        DataFile data_file = DataFile.findById(id_of_DataFile);
        if (data_file == null || data_file.is_delete) { // 不正なアクセス
            flash.put("error", "無効なファイルです");
            index();
        } else {
            File file = data_file.get_file();
            if (file.exists() == false) { // サーバー側のエラー
                flash.put("error", "無効なファイルです．管理者に問い合わせて下さい．");
                index();
            } else {
                renderBinary(file, data_file.file_name);
            }
        }
    }
}