package persistency;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by timos on 7-11-2017.
 */

public class MatchRestResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MatchRest> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MatchRest> getResults() {
        return results;
    }

    public void setResults(List<MatchRest> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
