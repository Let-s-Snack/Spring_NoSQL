package org.example.spring_nosql.Controller;

import com.mongodb.client.result.UpdateResult;
import org.example.spring_nosql.Model.Wishlist;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/recipes")
public class RecipesController {
    private final Validator validator;
    private final RecipesService recipesService;

    public RecipesController(Validator validator, RecipesService recipesService, PersonsService personsService) {
        this.validator = validator;
        this.recipesService = recipesService;
    }

    //Funcionando
    @GetMapping("/listAll")
    @Operation(summary = "Buscar todas as receitas", description = "Faz a busca de todas as receitas cadastrados")
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

    //Funcionando
    @GetMapping("/listRecipesById")
    @Operation(summary = "Busca receita pelo ID", description = "Faz a busca da receita a partir do seu ID")
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
    public ResponseEntity<?> listRecipesById(@RequestBody Map<String, String> infos){
        try{
            if(infos.containsKey("recipesId") && infos.containsKey("personsId")){
                String recipesId = infos.get("recipesId");
                String personsId = infos.get("personsId");

                return ResponseEntity.ok(recipesService.findRecipesById(new ObjectId(recipesId), new ObjectId(personsId)));
            }else{
                return ResponseEntity.ok("Valores foram inseridos incorretamente!");
            }
        }catch(DataIntegrityViolationException ttt){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valores inseridos incorretamente!");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a receita ou o usuário!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
        }
    }

    @GetMapping("/listRecipesByName/{recipesName}")
    @Operation(summary = "Busca receita pelo nome", description = "Faz a busca da receita a partir seu nome, ignorando maiúsculo/minúsculo")
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
    public ResponseEntity<?> listRecipesByName(@Parameter(description = "Inserir nome da receita") @PathVariable String recipesName, @RequestBody Map<String, List<String>> listRestrictions){
        try{
            if(recipesName != null && listRestrictions.containsKey("restrictionsId")){
                return ResponseEntity.ok(recipesService.findRecipesByName(recipesName, listRestrictions.get("restrictionsId")));
            }else{
                return ResponseEntity.ok("Valores foram inseridos incorretamente!");
            }
        }catch(DataIntegrityViolationException ttt){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valores inseridos incorretamente!");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar a receita!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
        }
    }

    //Funcionando
    @GetMapping("/listRecipesByRestrictions")
    @Operation(summary = "Busca receita pela restrição", description = "Faz a busca da receita a partir do id da restrição e o id do usuário")
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
    public ResponseEntity<?> listRecipesByRestriction(@RequestBody Map<String, String> infos){
        try{
            if(infos.containsKey("personId") && infos.containsKey("restrictionId")){
                return ResponseEntity.ok(recipesService.findRecipesByRestriction(new ObjectId(infos.get("personId")), new ObjectId(infos.get("restrictionId"))));
            }else{
                return ResponseEntity.ok("Valores inseridos incorretamente!");
            }
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar as receita!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
        }
    }

    //Funcionando
    @PutMapping("/insertComent/{recipesId}")
    @Operation(summary = "Adicionar comentários", description = "Faz a inserção de um novo comentário a partir do id da receita")
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
