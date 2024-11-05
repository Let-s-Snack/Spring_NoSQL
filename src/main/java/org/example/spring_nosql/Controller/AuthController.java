package org.example.spring_nosql.Controller;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.spring_nosql.Model.Adm;
import org.example.spring_nosql.Model.Message;
import org.example.spring_nosql.Service.AdmService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //Inicializando a chave secreta em tempo de execução
    private final SecretKey secretKey; //Chave secreta segura
    private final AdmService admService;
    private final Gson gson = new Gson();
    public AuthController(AdmService admService, SecretKey secretKey){
        this.admService = admService;
        this.secretKey = secretKey;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> objectAdm) {
        if (objectAdm.containsKey("email") && objectAdm.containsKey("password")) {
            String email = objectAdm.get("email");
            String password = objectAdm.get("password");

            Adm adm = admService.findAdmByEmail(email);

            if (adm != null && checkPassword(password, adm.getPassword())) {
                try {
                    String token = Jwts.builder()
                            .setSubject(adm.getEmail())
                            .claim("roles", Collections.emptyList())
                            .setExpiration(new Date(System.currentTimeMillis() + 180_000)) // Token válido por 3 minutos
                            .signWith(secretKey, SignatureAlgorithm.HS512) //Usa a chave secreta para assinar
                            .compact();

                    return ResponseEntity.ok(token);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro ao gerar o Token JWT")));
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("E-mail ou senha inválidos")));
            }
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Valores foram inseridos incorretamente!"))) ;
        }
    }

    // Verifica se a senha corresponde ao hash
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}