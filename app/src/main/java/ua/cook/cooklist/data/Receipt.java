package ua.cook.cooklist.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;


public class Receipt implements Parcelable {
    private String name;
    private String description;
    private String imageUrl;
    private String author;
    private String id;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Receipt() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeString(this.author);
        dest.writeString(this.id);
    }

    protected Receipt(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.author = in.readString();
        this.id = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel source) {
            return new Receipt(source);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }

    };

    public HashMap<String, String> toMap(){
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("name", name);
        tempMap.put("description", description);
        tempMap.put("author",author);
        tempMap.put("imageUrl", imageUrl);
        tempMap.put("id",id);
        return tempMap;
    }
}
