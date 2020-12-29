package com.lig.intermediate.tmdbclient.model;


import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDBResponse implements Parcelable
{

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Movie> Movies = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalMovies;
    public final static Parcelable.Creator<MovieDBResponse> CREATOR = new Creator<MovieDBResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieDBResponse createFromParcel(Parcel in) {
            return new MovieDBResponse(in);
        }

        public MovieDBResponse[] newArray(int size) {
            return (new MovieDBResponse[size]);
        }

    };


    protected MovieDBResponse(Parcel in) {
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.Movies, (com.lig.intermediate.tmdbclient.model.Movie.class.getClassLoader()));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalMovies = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public MovieDBResponse() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return Movies;
    }

    public void setMovies(List<Movie> Movies) {
        this.Movies = Movies;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(Integer totalMovies) {
        this.totalMovies = totalMovies;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeList(Movies);
        dest.writeValue(totalPages);
        dest.writeValue(totalMovies);
    }

    public int describeContents() {
        return 0;
    }

}