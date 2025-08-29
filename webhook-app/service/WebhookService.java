// Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

// Jakarta Annotation (NEW in Spring Boot 3.x)
import jakarta.annotation.PostConstruct;

// Your Model Classes
import com.example.webhookapp.model.RegistrationRequest;
import com.example.webhookapp.model.SubmissionRequest;
import com.example.webhookapp.model.WebhookResponse;

@Service
public class WebhookService {
    
    private static final String GENERATE_WEBHOOK_URL = 
        "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String SUBMIT_WEBHOOK_URL = 
        "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    
    private final RestTemplate restTemplate;
    
    public WebhookService() {
        this.restTemplate = new RestTemplate();
    }
    
    @PostConstruct
    public void executeWorkflow() {
        try {
            // Step 1: Generate webhook
            WebhookResponse webhookResponse = generateWebhook();
            
            // Step 2: Solve SQL problem
            String sqlQuery = solveSqlProblem();
            
            // Step 3: Submit solution
            submitSolution(webhookResponse.getWebhook(), 
                          webhookResponse.getAccessToken(), 
                          sqlQuery);
                          
        } catch (Exception e) {
            System.err.println("Error in webhook workflow: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private WebhookResponse generateWebhook() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "John Doe");
        requestBody.put("regNo", "REG12347");
        requestBody.put("email", "john@example.com");
        
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
            GENERATE_WEBHOOK_URL, request, WebhookResponse.class);
            
        return response.getBody();
    }
    
    private String solveSqlProblem() {
        // Return the SQL solution for Question 1 (odd regNo)
        return "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
               "FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365.25) AS AGE, d.DEPARTMENT_NAME " +
               "FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
               "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
               "WHERE DAY(p.PAYMENT_TIME) != 1 ORDER BY p.AMOUNT DESC LIMIT 1;";
    }
    
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        
        SubmissionRequest submissionRequest = new SubmissionRequest(sqlQuery);
        HttpEntity<SubmissionRequest> request = new HttpEntity<>(submissionRequest, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            SUBMIT_WEBHOOK_URL, request, String.class);
            
        System.out.println("Submission response: " + response.getBody());
    }
}
