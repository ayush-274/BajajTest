public class SubmissionRequest {
    private String finalQuery;
    
    public SubmissionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }
    
    // Getters and setters
    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}
