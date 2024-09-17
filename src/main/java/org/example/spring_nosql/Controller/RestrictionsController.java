package org.example.spring_nosql.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Restrictions;
import org.example.spring_nosql.Service.RestrictionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/restrictions")
public class RestrictionsController {
    private final RestrictionsService restrictionsService;

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
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a restrição!")))
    })
    public ResponseEntity<?> listAllRestrictions() {
        try{
            List<Restrictions> restrictions = restrictionsService.findAllRestrictions();
            return ResponseEntity.ok(restrictions);
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a restrição!");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
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
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a restrição!")))
    })
    public ResponseEntity<?> listAllRestrictionsByName(@Valid @PathVariable String name){
        try{
            List<Restrictions> restrictions = restrictionsService.findRestrictionsByName(name);
            return ResponseEntity.ok(restrictions);
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a restrição!");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
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
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a restrição!")))
    })
    public ResponseEntity<?> listAllRestrictionsById(@Valid @PathVariable ObjectId id){
        try{
            Restrictions restrictions = restrictionsService.findRestrictionsById(id);
            return ResponseEntity.ok(restrictions);
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a restrição!");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }
}
