package com.example.finalproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class HadistModel implements Parcelable {

    private String _id;

    @SerializedName("Chapter_English")
    private String chapterEnglish;

    @SerializedName("Chapter_Arabic")
    private String chapterArabic;

    @SerializedName("English_Hadith")
    private String englishHadith;

    @SerializedName("Arabic_Hadith")
    private String arabicHadith;

    protected HadistModel(Parcel in) {
        _id = in.readString();
        chapterEnglish = in.readString();
        chapterArabic = in.readString();
        englishHadith = in.readString();
        arabicHadith = in.readString();
    }

    public static final Creator<HadistModel> CREATOR = new Creator<HadistModel>() {
        @Override
        public HadistModel createFromParcel(Parcel in) {
            return new HadistModel(in);
        }

        @Override
        public HadistModel[] newArray(int size) {
            return new HadistModel[size];
        }
    };

    public String get_Id() {
        return _id;
    }

    public void set_Id(String id) {
        this._id = id;
    }

    public String getChapterEnglish() {
        return chapterEnglish;
    }

    public void setChapterEnglish(String chapterEnglish) {
        this.chapterEnglish = chapterEnglish;
    }

    public String getChapterArabic() {
        return chapterArabic;
    }

    public void setChapterArabic(String chapterArabic) {
        this.chapterArabic = chapterArabic;
    }

    public String getEnglishHadith() {
        return englishHadith;
    }

    public void setEnglishHadith(String englishHadith) {
        this.englishHadith = englishHadith;
    }

    public String getArabicHadith() {
        return arabicHadith;
    }

    public void setArabicHadith(String arabicHadith) {
        this.arabicHadith = arabicHadith;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(chapterEnglish);
        dest.writeString(chapterArabic);
        dest.writeString(englishHadith);
        dest.writeString(arabicHadith);
    }
}
