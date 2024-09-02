package org.example.spring_nosql.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.spring_nosql.Model.Gender;
import org.example.spring_nosql.Service.GenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("gender")
public class GenderController {
    private final Validator validator;
    private final GenderService genderService;

    //Objeto de criação do bean
    public GenderController(GenderService genderService, Validator validator){
        this.genderService = genderService;
        this.validator = validator;
    }

    @GetMapping("/listAllGenders")
    @Operation(summary = "Lista os gêneros", description = "Retorna todos os gêneros")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de gêneros retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Gender.class))),
        @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(example = "URL Incorreta!"))),
        @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(example = "Não foi possivel encontrar o gênero!")))
    })
    public Object listAllGenders(){
        try{
            return genderService.findAllGenders();
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (HttpServerErrorException.InternalServerError nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o gênero!");
        }
    }

    @GetMapping("/ListGenderById/{id}")
    @Operation(summary = "Busca Gênero pelo ID", description = "Retorna o gênero que possui o ID passado como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero foi encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gender.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o gênero!")))
    })
    public Object findGenderById(@Parameter(description = "Inserir o ID do gênero") @PathVariable int id){
        try {
            return genderService.findGenderById(id);
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta!");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o gênero!");
        }
    }

    @GetMapping("/listGenderByName/{name}")
    @Operation(summary = "Busca Gênero pelo nome", description = "Retorna o gênero que possui o nome passado como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero foi encontrado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gender.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o gênero!")))
    })
    public Object findGenderByName(@Parameter(description = "Inserir o nome do gênero") @PathVariable String name){
        try {
            return genderService.findByGenderName(name);
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (HttpServerErrorException.InternalServerError nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o gênero!");
        }
    }

    @PostMapping("/insertGender")
    @Operation(summary = "Inserir Gênero", description = "Insere o gênero passado no body da requisição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero foi inserido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gender.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))
    })
    public ResponseEntity<?> insertGender(@RequestBody Gender gender, BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retornaErro(result));
            }else{
                if(genderService.insertGender(gender) != null){
                    return ResponseEntity.ok("Gênero foi inserida com sucesso");
                }else{
                    return ResponseEntity.ok("Gênero já existente, caso queira atualizar o gênero, usar o endPoint /updateGender");
                }
            }
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta!");
        }catch (HttpServerErrorException.InternalServerError nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor!");
        }
    }

    @PutMapping("/updateGender/{id}")
    @Operation(summary = "Atualizando o gênero", description = "Atualiza o gênero que foi encontrado a partir do ID passado como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero foi atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gender.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))
    })
    public ResponseEntity<?> updateGender(@Parameter(description = "Inserir o ID do gênero") @PathVariable int id, @RequestBody Map<String, Object> updatedValues){
        try{
            Gender updatedGender = genderService.findGenderById(id);

            if(updatedGender != null){
                if(updatedValues.containsKey("id")){
                    return ResponseEntity.ok("ID do gênero não pode ser atualizado");
                }

                if(updatedValues.containsKey("nome")){
                    final String name = String.valueOf(updatedValues.get("nome"));
                    updatedGender.setName(name);
                }

                if(updatedValues.containsKey("creationDate")){
                    return ResponseEntity.ok("Data de criação do gênero não pode ser atualizado");
                }

                //Validando os dados
                DataBinder dataBinder = new DataBinder(updatedGender);
                dataBinder.setValidator(validator);
                dataBinder.validate();
                BindingResult result = dataBinder.getBindingResult();
                if (result.hasErrors()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retornaErro(result));
                }

                genderService.updateGender(updatedGender);
                return ResponseEntity.ok("Alteração feita com sucesso!");
            }else{
                return ResponseEntity.ok("Não foi possivel encontrar o gênero");
            }
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta!");
        }catch (HttpServerErrorException.InternalServerError nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor!");
        }
    }

    @DeleteMapping("/deleteGenderById/{id}")
    @Operation(summary = "Excluindo o gênero", description = "Excluindo o gênero encontrado a partir do ID passado como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero foi deletado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gender.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o gênero!")))
    })
    public ResponseEntity<?> deleteGender(@Parameter(description = "Inserir o ID do gênero a ser excluido") @PathVariable int id){
        try{
            Gender excludeGender = genderService.findGenderById(id);

            //Validando os dados
            DataBinder dataBinder = new DataBinder(excludeGender);
            dataBinder.setValidator(validator);
            dataBinder.validate();
            BindingResult result = dataBinder.getBindingResult();
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(retornaErro(result));
            }else {
                genderService.deleteGender(id);
                return ResponseEntity.ok("Gênero foi excluido com sucesso!");
            }
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (HttpServerErrorException.InternalServerError npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel encontrar o gênero!");
        }
    }

    public String retornaErro(BindingResult result) {
        StringBuilder stringBuilderErro = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilderErro.append("Erro: ").append(error.getDefaultMessage()).append("  |  ");
            }
        }
        return stringBuilderErro.toString();
    }
}
