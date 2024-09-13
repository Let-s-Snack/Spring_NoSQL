package org.example.spring_nosql.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Gender;
import org.example.spring_nosql.Model.Ingredients;
import org.example.spring_nosql.Model.Restrictions;
import org.example.spring_nosql.Service.IngredientsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.validation.Validator;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientsController {

    private final Validator validator;
    private final IngredientsService ingredientsService;

    public IngredientsController(Validator validator, IngredientsService ingredientsService) {
        this.validator = validator;
        this.ingredientsService = ingredientsService;
    }


    @GetMapping("/listAllIngredients")
    @Operation(summary = "Lista os ingredientes", description = "Retorna todos os ingredientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ingredientes retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ingredients.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar os ingredientes!")))
    })
    public ResponseEntity<?> listAllIngredients() {
        try {
            List<Ingredients> ingredients = ingredientsService.findAllIngredients();
            return ResponseEntity.ok(ingredients);
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        } catch (HttpServerErrorException.InternalServerError ise) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar os ingredientes.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor.");
        }
    }

    @GetMapping("/findIngredientById/{id}")
    @Operation(summary = "Busca ingrediente por ID", description = "Retorna o ingrediente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrediente retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ingredients.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o ingrediente!")))
    })
    public ResponseEntity<?> findIngredientById(@Parameter(description = "Inserir o ID do ingrediente") @PathVariable ObjectId id) {
        try {
            Ingredients ingredient = ingredientsService.findIngredientsById(id);
            return ResponseEntity.ok(ingredient);
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta.");
        } catch (HttpServerErrorException.InternalServerError ise) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o ingrediente.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor.");
        }
    }

    @GetMapping("/findIngredientByName/{name}")
    @Operation(summary = "Lista os ingredientes por nome", description = "Retorna todos os ingredientes baseado no nome pesquisado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ingredientes retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ingredients.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar os ingredientes!")))
    })
    public ResponseEntity<?> findIngredientsByName(@Parameter(description = "Inserir o nome do ingrediente") @PathVariable String name) {
        try {
            List<Ingredients> ingredients = ingredientsService.findIngredientsByName(name);
            return ResponseEntity.ok(ingredients);
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta.");
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar os ingredientes.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor.");
        }
    }
}
