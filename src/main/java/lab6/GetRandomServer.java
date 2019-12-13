package lab6;

public class GetRandomServer {
    private String site;
    private Integer requestCount;

    public GetRandomServer(String site, int requestCount) {
        this.site = site;
        this.requestCount = requestCount;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public String getSite() {
        return site;
    }
}