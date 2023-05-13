package Model;

import java.io.Serializable;

public class IconChucNang implements Serializable {
    Integer imgIcon;
    String chucNang;

    public IconChucNang(Integer imgIcon, String chucNang) {
        this.imgIcon = imgIcon;
        this.chucNang = chucNang;
    }

    public Integer getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(Integer imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getChucNang() {
        return chucNang;
    }

    public void setChucNang(String chucNang) {
        this.chucNang = chucNang;
    }
}
