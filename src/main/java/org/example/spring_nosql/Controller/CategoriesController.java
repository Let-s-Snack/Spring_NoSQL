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
import org.example.spring_nosql.Repository.CategoriesRepository;
import org.example.spring_nosql.Service.CategoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private Gson gson = new Gson();
    private final CategoriesService categoriesService;
    public CategoriesController(CategoriesService categoriesService){
        this.categoriesService = categoriesService;
    }

    @GetMapping("/listAllCategories")
    @Operation(summary = "Lista as categorias", description = "Retorna todas as Categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"URL Incorreta!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar a categoria!\"}")))
    })
    public ResponseEntity<?> listAllRestrictions() {
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(categoriesService.findAllCategories(), "Categorias não foram encontradas"));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a categoria!")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/listCategoriesByName/{name}")
    @Operation(summary = "Lista as categorias pelo nome", description = "Retorna as categorias pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"URL Incorreta!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar a restrição!\"}")))
    })
    public ResponseEntity<?> listCategoriesByName(@Valid @PathVariable @Parameter(description = "Nome da categoria", example = "Swift") String name){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(categoriesService.findCategoriesByName(name), gson.toJson(new Message("Categoria não encontrada"))));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a categoria!")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/listCategoriesById/{id}")
    @Operation(summary = "Lista as categorias pelo id", description = "Retorna as categorias pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"URL Incorreta!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Não foi possível encontrar a restrição!\"}")))
    })
    public ResponseEntity<?> listCategoriesById(@Valid @PathVariable String id){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(categoriesService.findCategoriesById(new ObjectId(id)), gson.toJson(new Message("Categoria não encontrada"))));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a categoria!")));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }
}
