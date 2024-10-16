package org.example.spring_nosql.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.*;
import org.example.spring_nosql.Service.PersonsService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/persons")
public class PersonsController {
    private final Validator validator;
    private final PersonsService personsService;

    public PersonsController(Validator validator, PersonsService personsService) {
        this.validator = validator;
        this.personsService = personsService;
    }

    @GetMapping("/listAll")
    @Operation(summary = "Buscar todos os usuários", description = "Faz a busca de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Usuários foram retornados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listAllPersons(){
        return ResponseEntity.ok(personsService.findAllPersons());
    }

    @GetMapping("/listPersonById/{id}")
    @Operation(summary = "Busca usuário pelo ID", description = "Faz a busca do usuário a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Usuário foi encontrado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listPersonById(@Parameter(description = "Inserir ID do usuário") @PathVariable String id){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(personsService.findPersonById(new ObjectId(id)), "Usuário não encontrado"));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!");
        }catch (Exception npc){
            return ResponseEntity.ok("Erro interno com o servidor");
        }
    }

    @GetMapping("/listPersonByEmail/{email}")
    @Operation(summary = "Buscar usuário cadastrado", description = "Faz a busca do cadastro do usuário a partir do e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Informações de login estão corretas!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listPersonByEmail(@Parameter(description = "Inserir e-mail do usuário", example = "testecassio@gmail.com") @PathVariable String email){
        try{
            return ResponseEntity.ok(Objects.requireNonNullElse(personsService.findPersonByEmail(email), "Usuário não encontrado!"));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!");
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }

    @GetMapping("/listPersonByUsername/{username}")
    @Operation(summary = "Buscar usuário cadastrado", description = "Faz a busca do usuário a partir do seu username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Username está correto!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listPersonByUsername(@Parameter(description = "Inserir nome de usuário", example = "Gustavo") @PathVariable String username){
        try{
            Persons persons = personsService.findPersonsByUsername(username);
            System.out.println(persons);
            return ResponseEntity.ok(Objects.requireNonNullElse(persons, "Apelido do usuário não existe"));
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!");
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }

    //Buscando a restrição do usuário pelo seu id
    @GetMapping("/personRestriction/{email}")
    @Operation(summary = "Buscar restrição", description = "Faz a busca da restrição a partir do id do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Restrição do Usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personRestrictionByEmail(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas restrições") @PathVariable String email){
        try{
            Persons person = personsService.findPersonByEmail(email);
            return ResponseEntity.ok(person.getRestrictions());
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!");
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }

    @GetMapping("/personWishlist/{email}")
    @Operation(summary = "Buscar wishlist", description = "Faz a busca da wishlist a partir do e-mail do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Wishlist do Usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personWishListById(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas salvas na wish list") @PathVariable String email){
        try{
            List<Recipes> wishlist = personsService.findWishlistPersonById(email);
            if(wishlist.isEmpty()){
                return ResponseEntity.ok("Wishlist está vazia");
            }else{
                return ResponseEntity.ok(wishlist);
            }
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!");
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }

    @GetMapping("/personDirectionWeek/{email}")
    @Operation(summary = "Buscar receita da semana", description = "Faz a busca da receita da semana a partir do e-mail do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Receita da semana do usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DirectionsWeek.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personDirectionWeekId(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas da semana") @PathVariable String email){
        try{
            List<Recipes> directionWeek = personsService.findDirectionWeekById(email);
            if(directionWeek.isEmpty()){
                return ResponseEntity.ok("Semana da receita está vazia");
            }else{
                return ResponseEntity.ok(directionWeek);
            }
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!");
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }

    /*@GetMapping("/personShoppingList/{id}")
    @Operation(summary = "Buscar ingredientes Salvos", description = "Faz a busca dos ingredientes que foram salvos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Ingredientes foram encontrados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingList.class))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personShoppingList(@Parameter(description = "Inserir ID do usuário para encontrar os ingredientes que estão salvos") @PathVariable String id){
        try{
            return ResponseEntity.ok(personsService.findShoppingListById(new ObjectId(id)));
        }catch(HttpClientErrorException.NotFound ntf){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL incorreta");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!" + nnn.getMessage());
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor");
        }
    }*/

    @PostMapping("/insertPerson")
    @Operation(summary = "Inserir Usuário", description = "Faz a inserção do gênero passado no body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Usuário foi inserido com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Usuário foi inserido com sucesso!"))),
            @ApiResponse(responseCode = "400" , description = "Erro na requisição!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ""))), //Adicionar erro
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> insertPerson(@RequestBody Persons person, BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            }else{
                person.setId(new ObjectId());
                person.setPassword(hashPassword(person.getPassword()));
                person.setCreationDate(new Date());
                Persons personInsert = personsService.insertPerson(person);
                if(personInsert != null){
                    return ResponseEntity.status(HttpStatus.OK).body("Usuário foi inserido com sucesso");
                }else{
                    return ResponseEntity.status(HttpStatus.OK).body("Não foi possivel inserir o usuário");
                }
            }
        }catch(DataIntegrityViolationException ttt){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valores inseridos incorretamente!");
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor!" + npc.getMessage());
        }
    }

    @PutMapping("/updatePerson/{id}")
    @Operation(summary = "Atualizando o usuário", description = "Atualizando o usuário encontrado a partir do id passado como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Usuário foi atualizado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "400" , description = "Erro na requisição!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Valores inseridos incorretamente!"))),
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> updatePersonById(@Parameter(description = "Inserir o ID do usuário") @PathVariable String id, @RequestBody Map<String, Object> updatedValues)  {
        try {
            Persons updatedPersons = personsService.findPersonById(new ObjectId(id));

            if (updatedPersons != null) {
                if (updatedValues.containsKey("_id")) {
                    return ResponseEntity.ok("ID do usuário não pode ser atualizado");
                }

                if (updatedValues.containsKey("gender")) {
                    final String gender = String.valueOf(updatedValues.get("gender"));
                    updatedPersons.setGender(gender);
                }

                if (updatedValues.containsKey("name")) {
                    final String name = String.valueOf(updatedValues.get("name"));
                    updatedPersons.setName(name);
                }

                if (updatedValues.containsKey("nickname")) {
                    final String nickname = String.valueOf(updatedValues.get("nickname"));
                    updatedPersons.setNickname(nickname);
                }

                if (updatedValues.containsKey("email")) {
                    final String email = String.valueOf(updatedValues.get("email"));
                    updatedPersons.setEmail(email);
                }

                if (updatedValues.containsKey("password")) {
                    final String password = String.valueOf(updatedValues.get("password"));
                    updatedPersons.setPassword(hashPassword(password));
                }

                if (updatedValues.containsKey("isPro")) {
                    final boolean isPro = Boolean.parseBoolean(String.valueOf(updatedValues.get("isPro")));
                    updatedPersons.setIsPro(isPro);
                }

                if (updatedValues.containsKey("urlPhoto")) {
                    final String urlPhoto = String.valueOf(updatedValues.get("urlPhoto"));
                    updatedPersons.setUrlPhoto(urlPhoto);
                }

                if (updatedValues.containsKey("birthDate")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    final String dateString = String.valueOf(updatedValues.get("birthDate"));
                    updatedPersons.setBirthDate(dateFormat.parse(dateString));
                }

                if (updatedValues.containsKey("cellphone")) {
                    final String cellphone = String.valueOf(updatedValues.get("cellphone"));
                    updatedPersons.setCellphone(cellphone);
                }

                if (updatedValues.containsKey("registrationCompleted")) {
                    final boolean registrationCompleted = Boolean.parseBoolean(String.valueOf(updatedValues.get("registrationCompleted")));
                    updatedPersons.setRegistrationCompleted(registrationCompleted);
                }

                if (updatedValues.containsKey("restrictions")) {
                    final List<PersonsRestrictions> restrictions = (List<PersonsRestrictions>) updatedValues.get("restrictions");
                    updatedPersons.setRestrictions(restrictions);
                }

                if (updatedValues.containsKey("wishlist")) {
                    final List<Wishlist> wishlist = (List<Wishlist>) updatedValues.get("wishlist");
                    updatedPersons.setWishlist(wishlist);
                }

                if (updatedValues.containsKey("directionsWeeks")) {
                    final List<DirectionsWeek> directionsWeeks = (List<DirectionsWeek>) updatedValues.get("directionsWeeks");
                    updatedPersons.setDirectionsWeek(directionsWeeks);
                }

                if (updatedValues.containsKey("shoppingList")) {
                    final List<ShoppingList> shoppingList = (List<ShoppingList>) updatedValues.get("shoppingList");
                    updatedPersons.setShoppingList(shoppingList);
                }

                if (updatedValues.containsKey("creationDate")) {
                    return ResponseEntity.ok("Não é possivel atualizar a data de criação do usuário!");
                }

                if (updatedValues.containsKey("deactivationDate")) {
                    return ResponseEntity.ok("Não é possivel atualizar a data de deleção do usuário, caso queira você deverá excluir o mesmo!");
                }

                //Validando os dados
                DataBinder dataBinder = new DataBinder(updatedPersons);
                dataBinder.setValidator(validator);
                dataBinder.validate();
                BindingResult result = dataBinder.getBindingResult();
                if (result.hasErrors()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
                }

                personsService.updatePerson(updatedPersons);
                return ResponseEntity.status(HttpStatus.OK).body("Atualização feita com sucesso!");

            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Não foi possivel encontrar o usuário!");
            }

        }catch(DataIntegrityViolationException ttt){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valores inseridos incorretamente!");
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!" + nnn.getMessage());
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor!" + npc.getMessage());
        }
    }

    @DeleteMapping("/deleteUserById/{id}")
    @Transactional
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Usuário foi excluido com sucesso!",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "400" , description = "Erro na requisição",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ""))), //Adicionar erros
            @ApiResponse(responseCode = "404" , description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500" , description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    @Operation(summary = "Excluindo o usuário", description = "Excluindo o usuário encontrado a partir do id passado como parâmetro")
    public ResponseEntity<?> deleteUserById(@Parameter(description = "Inserir o ID do usuário a ser excluido") @PathVariable String id){
        try{
            Persons deletePerson = personsService.findPersonById(new ObjectId(id));

            //Validando os dados
            DataBinder dataBinder = new DataBinder(deletePerson);
            dataBinder.setValidator(validator);
            dataBinder.validate();
            BindingResult result = dataBinder.getBindingResult();

            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            }else {
                if(personsService.deletePerson(deletePerson) != null){
                    return ResponseEntity.ok("Usuário foi excluido com sucesso!");
                }else{
                    return ResponseEntity.ok("Não foi possivel excluir o usuário!");
                }
            }
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível encontrar o usuário!" + nnn.getMessage());
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno com o servidor!");
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
    // Gerando o HASH da senha
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
