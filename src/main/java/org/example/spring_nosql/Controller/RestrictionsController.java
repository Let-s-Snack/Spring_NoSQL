package org.example.spring_nosql.Controller;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Message;
import org.example.spring_nosql.Model.Restrictions;
import org.example.spring_nosql.Service.RestrictionsService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/restrictions")
public class RestrictionsController {
    private final RestrictionsService restrictionsService;
    private Gson gson = new Gson();

    public RestrictionsController(RestrictionsService restrictionsService) {
        this.restrictionsService = restrictionsService;
    }

    @GetMapping("/listAllRestrictions")
    @Operation(summary = "Lista as restrições", description = "Retorna todas as restrições")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de restrições retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"URL Incorreta!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar a restrição!\"}")))
    })
    public ResponseEntity<?> listAllRestrictions() {
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(restrictionsService.findAllRestrictions(), "Restricões não foram encontradas"));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a restrição!")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/listRestrictionsByName/{name}")
    @Operation(summary = "Lista as restrições por nome", description = "Retorna as restrições por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de restrições retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"URL Incorreta!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar a restrição!\"}")))
    })
    public ResponseEntity<?> listAllRestrictionsByName(@Valid @PathVariable @Parameter(description = "Nome da restrição", example = "Vegetariana") String name){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(restrictionsService.findRestrictionsByName(name), gson.toJson(new Message("Restricão não encontrada"))));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a restrição!")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/listRestrictionsById/{id}")
    @Operation(summary = "Lista as restrições por id", description = "Retorna as restrições por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de restrições retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"URL Incorreta!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar a restrição!\"}")))
    })
    public ResponseEntity<?> listAllRestrictionsById(@Valid @PathVariable String id){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(restrictionsService.findRestrictionsById(new ObjectId(id)), gson.toJson(new Message("Restricão não encontrada"))));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a restrição!")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }
}
