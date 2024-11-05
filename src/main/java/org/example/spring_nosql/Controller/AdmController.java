package org.example.spring_nosql.Controller;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Adm;
import org.example.spring_nosql.Model.Message;
import org.example.spring_nosql.Service.AdmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/adm")
public class AdmController {
    private final AdmService admService;
    private final Gson gson = new Gson();

    public AdmController(AdmService admService){
        this.admService = admService;
    }

    @GetMapping("/listAll")
    @Operation(summary = "Buscar todos os administradores", description = "Faz a busca de todos os administradores cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "administradores foram retornados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Adm.class))),
    @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Erro interno com o servidor!\"}")))
})
    public ResponseEntity<?> listAllAdm() {
        return ResponseEntity.ok(admService.findAllAdm());
    }

    @GetMapping("/listAdmById/{id}")
    @Operation(summary = "Busca o administrador pelo ID", description = "Faz a busca do administradores a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador foi encontrado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Adm.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar o administrador!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Erro interno com o servidor!\"}")))
    })
    public ResponseEntity<?> listAdmById(@Parameter(description = "Inserir ID do administrador") @PathVariable String id) {
        try {
            return ResponseEntity.ok(Objects.requireNonNullElse(admService.findAdmById(new ObjectId(id)), gson.toJson(new Message("Administrador não encontrado"))));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar o administrador!")));
        } catch (Exception npc) {
            return ResponseEntity.ok(gson.toJson(new Message("Erro interno com o servidor")));
        }
    }

    @GetMapping("/listAdmByEmail/{email}")
    @Operation(summary = "Busca o administrador cadastrado", description = "Faz a busca do cadastro do administrador a partir do e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador foi encontrado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Adm.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar o administrador!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Erro interno com o servidor!\"}")))
    })
    public ResponseEntity<?> listPersonByEmail(@Parameter(description = "Inserir e-mail do administrador", example = "testecassio@gmail.com") @PathVariable String email) {
        try {
            return ResponseEntity.ok(Objects.requireNonNullElse(admService.findAdmByEmail(email), gson.toJson(new Message("Administrador não encontrado!"))));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar o administrador!")));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor")));
        }
    }

    @GetMapping("/listAdmByName/{name}")
    @Operation(summary = "Busca usuário cadastrado pelo nome", description = "Faz a busca do administrador a partir do seu nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador foi encontrado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Adm.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar o administrador!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Erro interno com o servidor!\"}")))
    })
    public ResponseEntity<?> listAdmByName(@Parameter(description = "Inserir nome de usuário", example = "Gustavo") @PathVariable String name) {
        try {
            return ResponseEntity.ok(Objects.requireNonNullElse(admService.findAdmByName(name), gson.toJson(new Message("Apelido do usuário não existe"))));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar o usuário!")));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor")));
        }
    }

}