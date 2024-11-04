package org.example.spring_nosql.Controller;

import com.google.gson.Gson;
import com.mongodb.client.result.UpdateResult;
import jakarta.validation.Valid;
import org.example.spring_nosql.Model.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Service.PersonsService;
import org.example.spring_nosql.Service.RecipesService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@RestController
@RequestMapping("/recipes")
public class RecipesController {
    private final Validator validator;
    private final RecipesService recipesService;
    private Gson gson = new Gson();

    public RecipesController(Validator validator, RecipesService recipesService, PersonsService personsService) {
        this.validator = validator;
        this.recipesService = recipesService;
    }

    @GetMapping("/listAll")
    @Operation(summary = "Buscar todas as receitas", description = "Faz a busca de todas as receitas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Receitas foram retornados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listAllRecipes(){
        return ResponseEntity.ok(recipesService.findAllRecipes());
    }

    @GetMapping("/listRecipesById")
    @Operation(summary = "Busca receita pelo ID", description = "Faz a busca da receita a partir do seu ID, sendo necessário o e-mail do usuário para verificarmos se a receita é favorita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Receita foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
     public ResponseEntity<?> listRecipesById(@Parameter(description = "Adicionar o ID da receita") @RequestParam String recipesId, @Parameter(description = "Adicionar o e-mail do usuário") @RequestParam String personsEmail){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(recipesService.findRecipesById(new ObjectId(recipesId), personsEmail), gson.toJson(new Message("Não foi possível encontrar a receita!"))));
        }catch(DataIntegrityViolationException ttt){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(new Message("Valores inseridos incorretamente!")));
        }catch (IndexOutOfBoundsException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a receita ou o usuário!")));
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/listRecipesByName")
    @Operation(summary = "Busca receita pelo nome", description = "Faz a busca da receita a partir seu nome, ignorando se está em maiúsculo, minúsculo ou se possui acento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Receitas foram encontradas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listRecipesByName(@Parameter(description = "Adicionar o nome da receita") @RequestParam String recipesName, @Parameter(description = "Adicionar o e-mail do usuário") @RequestParam String personsEmail){
        try {
            List<Recipes> recipes = recipesService.findRecipesByName(recipesName, personsEmail);

            return (!recipes.isEmpty())
                    ? ResponseEntity.ok(recipes)
                    :ResponseEntity.ok(gson.toJson(new Message("Não foi possível encontrar a receita!")));

        } catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a receita ou o usuário!")));
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor")));
        }
    }

    @GetMapping("/listRecipesByRestriction")
    @Operation(summary = "Busca receita pela restrição", description = "Faz a busca da receita a partir do id da restrição e do e-mail do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Receitas foram encontradas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listRecipesByRestriction(@Parameter(description = "Adicionar o ID da restrição") @RequestParam String restrictionsId, @Parameter(description = "Adicionar o e-mail do usuário") @RequestParam String personsEmail) {
        try{
            List<Recipes> recipes = recipesService.findRecipesByRestriction(new ObjectId(restrictionsId), personsEmail);

            return (!recipes.isEmpty())
                    ?ResponseEntity.ok(recipes)
                    :ResponseEntity.ok(gson.toJson(new Message("Não foi possível encontrar as receitas!")));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar as receita ou o usuário!")));
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor")));
        }
    }

    @GetMapping("/personTrendingRecipes/{email}")
    @Operation(summary = "Buscar receitas em alta", description = "Faz a busca das receitas em alta a partir do e-mail e da restrição do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receitas em alta do usuário foram encontradas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DirectionsWeek.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personTrendingRecipes(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas em alta") @PathVariable String email) {
        try {
            List<Recipes> trendingRecipes = recipesService.findTrendingRecipes(email);
            if (trendingRecipes.isEmpty()) {
                return ResponseEntity.ok(gson.toJson(new Message("Receitas em Alta está vazia!")));
            } else {
                return ResponseEntity.ok(trendingRecipes);
            }
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta")));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar o usuário!")));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/personRecommendedRecipes/{email}")
    @Operation(summary = "Buscar receitas em alta", description = "Faz a busca das receitas recomendadas a partir do e-mail e da restrição do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receitas em alta do usuário foram encontradas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DirectionsWeek.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personRecommendedRecipes(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas recomendadas") @PathVariable String email) {
        try {
            List<Recipes> trendingRecipes = recipesService.findRecommendedRecipes(email);
            if (trendingRecipes.isEmpty()) {
                return ResponseEntity.ok(gson.toJson(new Message("Receitas recomendadas está vazia!")));
            } else {
                return ResponseEntity.ok(trendingRecipes);
            }
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta")));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar o usuário!")));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @GetMapping("/personMostCommentedRecipes/{email}")
    @Operation(summary = "Buscar receitas em alta", description = "Faz a busca das receitas recomendadas a partir do e-mail e da restrição do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receitas em alta do usuário foram encontradas com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DirectionsWeek.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personMostCommentedRecipes(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas mais comentadas") @PathVariable String email) {
        try {
            List<Recipes> trendingRecipes = recipesService.findMostCommentedRecipes(email);
            if (trendingRecipes.isEmpty()) {
                return ResponseEntity.ok(gson.toJson(new Message("Receitas mais comentadas está vazia!")));
            } else {
                return ResponseEntity.ok(trendingRecipes);
            }
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(new Message("URL incorreta")));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar o usuário!")));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor!")));
        }
    }

    @PutMapping("/insertComent/{recipesId}")
    @Operation(summary = "Adicionar comentários", description = "Faz a inserção de um novo comentário a partir do id da receita",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Faz a inserção do comentário",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"personsId\": \"6707fb6b93239565894241be\", \"rating\": \"4\", \"message\": \"Receita maravilhosa!\",}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Comentário foi adicionado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> insertComent(@Parameter(description = "Inserir ID da receita") @PathVariable String recipesId, @Valid @RequestBody Coments coment, BindingResult result){
        try {
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            } else {
                //Adicionando o nome do usuário que comentou e a data de criação
                coment.setCreationDate(new Date());
                coment.setComentId(new ObjectId());

                Query query = new Query(Criteria.where("_id").is(new ObjectId(recipesId)));
                Update update = new Update().push("coments", coment);

                UpdateResult results = recipesService.insertComent(query, update);

                if (results.getModifiedCount() >= 1) {
                    return ResponseEntity.ok(gson.toJson(new Message("Comentário foi inserido com sucesso!")));
                } else {
                    return ResponseEntity.ok(gson.toJson(new Message("Não foi possível fazer a inserção do comentário!")));
                }
            }
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Não foi possível encontrar a receita ou o usuário!")));
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new Message("Erro interno com o servidor")));
        }
    }

    public String errors(BindingResult result) {
        StringBuilder stringBuilderErro = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilderErro.append("Erro: ").append(error.getDefaultMessage()).append("  |  ");
            }
        }
        return stringBuilderErro.toString();
    }
}
