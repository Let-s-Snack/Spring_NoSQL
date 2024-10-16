package org.example.spring_nosql.Controller;

import com.mongodb.client.result.UpdateResult;
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
import org.example.spring_nosql.Model.Coments;
import org.example.spring_nosql.Model.Recipes;
import org.example.spring_nosql.Service.PersonsService;
import org.example.spring_nosql.Service.RecipesService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/recipes")
public class RecipesController {
    private final Validator validator;
    private final RecipesService recipesService;

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
            return ResponseEntity.ok(Objects.requireNonNullElse(recipesService.findRecipesById(new ObjectId(recipesId), personsEmail), "Não foi possível encontrar a receita!"));
        }catch(DataIntegrityViolationException ttt){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valores inseridos incorretamente!");
        }catch (IndexOutOfBoundsException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a receita ou o usuário!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
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
                    :ResponseEntity.ok("Não foi possível encontrar a receita!");

        } catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a receita ou o usuário!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
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
                    ? ResponseEntity.ok(recipes)
                    :ResponseEntity.ok("Não foi possível encontrar as receitas!");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar as receita ou o usuário!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
        }
    }

    @PutMapping("/insertComent/{recipesId}")
    @Operation(summary = "Adicionar comentários", description = "Faz a inserção de um novo comentário a partir do id da receita",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Retorna as receitas a partir da sua restrição, sendo necessário o ID do usuário para verificar se a receita é favorita ou não ",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    //example = "{\"restrictionsId\": \"670661af8cbdb8537c0229fb\", \"personsId\": \"66f295e435644057236fec24\"}"
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
    public ResponseEntity<?> insertComent(@Parameter(description = "Inserir ID da receita") @PathVariable String recipesId, @RequestBody Coments coment, BindingResult result){
        try {
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            } else {
                //Adicionando o nome do usuário que comentou e a data de criação
                coment.setCreationDate(new Date());
                coment.setComentId(new ObjectId());

                //System.out.println(updatedRecipes);
                Query query = new Query(Criteria.where("_id").is(new ObjectId(recipesId)));
                Update update = new Update().push("coments", coment);

                UpdateResult results = recipesService.insertComent(query, update);

                if (results.getModifiedCount() >= 1) {
                    return ResponseEntity.ok("Comentário foi inserido com sucesso");
                } else {
                    return ResponseEntity.ok("Não foi possivel inserir o comentário");
                }
            }
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a receita ou o usuário!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
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
