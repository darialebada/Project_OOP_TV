package fileio;

public final class Action {
    private String type;
    private String page;
    private String feature;
    private String movie;
    private String startsWith;
    private String count;
    private int rate;
    //private String objectType;
    private Credentials credentials;
    private FilterInput filters;

    public Action() {
    }

    public Action(final String startsWith, final String count,
                  final FilterInput filters, final int rate) {
        this.startsWith = startsWith;
        this.count = count;
        this.filters = filters;
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public String getCount() {
        return count;
    }

    public int getRate() {
        return rate;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public FilterInput getFilters() {
        return filters;
    }
}
