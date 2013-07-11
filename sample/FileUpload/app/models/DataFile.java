package models;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import play.db.jpa.Model;
import play.libs.Files;

/**
 *
 * @author maruyama
 */
@Entity
public class DataFile extends Model {
    
    public static final String DATA_STORAGE = "data_storage/";
    
    public String file_name;
    public Long size;
    public Long upload_timestamp;
    public Boolean is_delete;
    
    public DataFile(play.data.Upload file){
        this.file_name = file.getFileName();
        this.size = file.getSize();
        this.upload_timestamp = Calendar.getInstance().getTimeInMillis();
        this.is_delete = false;
    }
    
    public static void upload_file(play.data.Upload file){
        DataFile data_file = new DataFile(file);
        data_file.save();
        File file_to = play.Play.getFile(DATA_STORAGE + data_file.id + "." + getSuffix(file.getFileName()));
        Files.copy(file.asFile(), file_to);
    }
    
    public void delete_file(Long id_of_DataFile){
        DataFile data_file = DataFile.findById(id_of_DataFile);
        if(data_file != null){
            data_file.is_delete = true;
            data_file.save();
        }
    }
    
    public File get_file() {
        File file = new File(DATA_STORAGE + this.id + "." + getSuffix(this.file_name));
        return file;
    }
    
    public static List<DataFile> get_list(){
        List<DataFile> list = DataFile.find("byIs_delete", false).fetch();
        return list;
    }
    
    public Date get_upload_date(){
        Date date = new Date(this.upload_timestamp);
        return date;
    }
    
    /**
     * ファイル名から拡張子を返す
     * @param file_name ファイル名
     * @return ファイルの拡張子（拡張子がない場合はファイル名を返す）
     */
    private static String getSuffix(String file_name){
        if(file_name == null){ return null; }
        
        int point = file_name.lastIndexOf(".");
        if(point != -1){
            return file_name.substring(point + 1);
        }
        return file_name;
    }
    
}
