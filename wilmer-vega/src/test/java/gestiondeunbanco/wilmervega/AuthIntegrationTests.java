package gestiondeunbanco.wilmervega;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.sql.init.mode=never",
                "spring.data.mongodb.uri=mongodb://127.0.0.1:27017/test_db?serverSelectionTimeoutMS=100",
                "app.mongodb.required=false"
        }
)
class AuthIntegrationTests {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerAndLoginShouldSucceed() throws Exception {
        String username = "it_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String password = "Clave123*";

        String registerBody = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"role\":\"NATURAL_CLIENT\"" +
                "}";

        HttpResponse<String> registerResponse = postJson("/auth/register", registerBody);
        assertEquals(HttpStatus.CREATED.value(), registerResponse.statusCode());
        JsonNode registerJson = objectMapper.readTree(registerResponse.body());
        assertEquals(username, registerJson.get("username").asText());
        assertNotNull(registerJson.get("token"));
        assertTrue(registerJson.get("token").asText().length() > 20);

        String loginBody = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";

        HttpResponse<String> loginResponse = postJson("/auth/login", loginBody);
        assertEquals(HttpStatus.OK.value(), loginResponse.statusCode());
        JsonNode loginJson = objectMapper.readTree(loginResponse.body());
        assertEquals(username, loginJson.get("username").asText());
        assertNotNull(loginJson.get("token"));
        assertTrue(loginJson.get("token").asText().length() > 20);
    }

    @Test
    void duplicateUsernameShouldReturnBadRequest() throws Exception {
        String username = "it_dup_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        String registerBody = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"Clave123*\"," +
                "\"role\":\"NATURAL_CLIENT\"" +
                "}";

        HttpResponse<String> firstResponse = postJson("/auth/register", registerBody);
        assertEquals(HttpStatus.CREATED.value(), firstResponse.statusCode());

        HttpResponse<String> secondResponse = postJson("/auth/register", registerBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), secondResponse.statusCode());
        JsonNode secondJson = objectMapper.readTree(secondResponse.body());
        assertEquals("Username already exists", secondJson.get("message").asText());
    }

        @Test
        void protectedEndpointShouldReturn401WithoutTokenAnd403WithInsufficientRole() throws Exception {
                HttpResponse<String> noTokenResponse = get("/api/admin/users");
                assertEquals(HttpStatus.UNAUTHORIZED.value(), noTokenResponse.statusCode());
                JsonNode noTokenJson = objectMapper.readTree(noTokenResponse.body());
                assertEquals(401, noTokenJson.get("status").asInt());
                assertEquals("No autenticado: se requiere un token valido", noTokenJson.get("message").asText());

                String username = "it_client_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                String registerBody = "{" +
                                "\"username\":\"" + username + "\"," +
                                "\"password\":\"Clave123*\"," +
                                "\"role\":\"NATURAL_CLIENT\"" +
                                "}";

                HttpResponse<String> registerResponse = postJson("/auth/register", registerBody);
                assertEquals(HttpStatus.CREATED.value(), registerResponse.statusCode());
                String token = objectMapper.readTree(registerResponse.body()).get("token").asText();

                HttpResponse<String> forbiddenResponse = getWithAuth("/api/admin/users", token);
                assertEquals(HttpStatus.FORBIDDEN.value(), forbiddenResponse.statusCode());
                JsonNode forbiddenJson = objectMapper.readTree(forbiddenResponse.body());
                assertEquals(403, forbiddenJson.get("status").asInt());
                assertEquals("Acceso denegado: no tiene permisos para este recurso", forbiddenJson.get("message").asText());
        }

    private HttpResponse<String> postJson(String path, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

        private HttpResponse<String> get(String path) throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:" + port + path))
                                .GET()
                                .build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }

        private HttpResponse<String> getWithAuth(String path, String token) throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:" + port + path))
                                .header("Authorization", "Bearer " + token)
                                .GET()
                                .build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }
}
