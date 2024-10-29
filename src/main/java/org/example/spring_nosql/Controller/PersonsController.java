package org.example.spring_nosql.Controller;

import com.mongodb.client.result.UpdateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.*;
import org.example.spring_nosql.Service.PersonsService;
import org.example.spring_nosql.Service.RecipesService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final RecipesService recipesService;
    private Map<String, String> mapResults = new HashMap<>();

    public PersonsController(Validator validator, PersonsService personsService, RecipesService recipesService) {
        this.validator = validator;
        this.personsService = personsService;
        this.recipesService = recipesService;
    }

        @GetMapping("/listAll")
        @Operation(summary = "Buscar todos os usuários", description = "Faz a busca de todos os usuários cadastrados")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuários foram retornados com sucesso!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Persons.class))),
                @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "Erro interno com o servidor!")))

        })
        public ResponseEntity<?> listAllPersons() {
            return ResponseEntity.ok(personsService.findAllPersons());
        }

        @GetMapping("/listPersonById/{id}")
        @Operation(summary = "Busca usuário pelo ID", description = "Faz a busca do usuário a partir do seu ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuário foi encontrado com sucesso!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Persons.class))),
                @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
                @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "Erro interno com o servidor!")))

        })
        public ResponseEntity<?> listPersonById(@Parameter(description = "Inserir ID do usuário") @PathVariable String id) {
            try {
                return ResponseEntity.ok(Objects.requireNonNullElse(personsService.findPersonById(new ObjectId(id)), mapResults.put("message", "Usuário não encontrado")));
            } catch (RuntimeException nnn) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
            } catch (Exception npc) {
                return ResponseEntity.ok(mapResults.put("message", "Erro interno com o servidor"));
            }
        }

        @GetMapping("/listPersonByEmail/{email}")
        @Operation(summary = "Buscar usuário cadastrado", description = "Faz a busca do cadastro do usuário a partir do e-mail")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Informações de login estão corretas!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Persons.class))),
                @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
                @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "Erro interno com o servidor!")))

        })
        public ResponseEntity<?> listPersonByEmail(@Parameter(description = "Inserir e-mail do usuário", example = "testecassio@gmail.com") @PathVariable String email) {
            try {
                return ResponseEntity.ok(Objects.requireNonNullElse(personsService.findPersonByEmail(email), mapResults.put("message", "Usuário não encontrado!")));
            } catch (RuntimeException nnn) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
            } catch (Exception npc) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor"));
            }
        }

    @GetMapping("/listPersonByUsername/{username}")
    @Operation(summary = "Buscar usuário cadastrado", description = "Faz a busca do usuário a partir do seu username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username está correto!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> listPersonByUsername(@Parameter(description = "Inserir nome de usuário", example = "Gustavo") @PathVariable String username) {
        try {
            Persons persons = personsService.findPersonsByUsername(username);
            return ResponseEntity.ok(Objects.requireNonNullElse(persons, mapResults.put("message", "Apelido do usuário não existe")));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    //Buscando a restrição do usuário pelo seu id
    @GetMapping("/personRestriction/{email}")
    @Operation(summary = "Buscar restrição", description = "Faz a busca da restrição a partir do id do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restrição do Usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restrictions.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personRestrictionByEmail(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas restrições") @PathVariable String email) {
        try {
            Persons person = personsService.findPersonByEmail(email);
            return ResponseEntity.ok(person.getRestrictions());
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapResults.put("message", "URL incorreta"));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    @GetMapping("/personWishlist/{email}")
    @Operation(summary = "Buscar wishlist", description = "Faz a busca da wishlist a partir do e-mail do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist do Usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personWishListByEmail(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas salvas na wish list") @PathVariable String email) {
        try {
            List<Recipes> wishlist = personsService.findWishlistPersonByEmail(email);
            if (wishlist.isEmpty()) {
                return ResponseEntity.ok(mapResults.put("message", "Wishlist está vazia"));
            } else {
                return ResponseEntity.ok(wishlist);
            }
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapResults.put("message", "URL incorreta"));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    @GetMapping("/personsShoppingList/{email}")
    @Operation(summary = "Buscar Shopping List", description = "Faz a busca do shopping list a partir do e-mail do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist do Usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personsShoppingList(@Parameter(description = "Inserir o e-mail do usuário para encontrar seu shopping list") @PathVariable String email) {
        try {
            List<ShoppingList> shoppinglist = personsService.findShoppingListByEmail(email);

            if (shoppinglist.isEmpty()) {
                return ResponseEntity.ok(mapResults.put("message", "Shoppinglist está vazia"));
            } else {
                return ResponseEntity.ok(shoppinglist);
            }
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapResults.put("message", "URL incorreta"));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!" + nnn.getMessage()));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    @GetMapping("/personDirectionWeek/{email}")
    @Operation(summary = "Buscar receita da semana", description = "Faz a busca da receita da semana a partir do e-mail do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita da semana do usuário foi encontrada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DirectionsWeek.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar o usuário!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> personDirectionWeekEmail(@Parameter(description = "Inserir o e-mail do usuário para encontrar suas receitas da semana") @PathVariable String email) {
        try {
            List<Recipes> directionWeek = personsService.findDirectionWeekByEmail(email);
            if (directionWeek.isEmpty()) {
                return ResponseEntity.ok(mapResults.put("message", "Semana da receita está vazia"));
            } else {
                return ResponseEntity.ok(directionWeek);
            }
        } catch (HttpClientErrorException.NotFound ntf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapResults.put("message", "URL incorreta"));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    @PostMapping("/insertPerson")
    @Operation(summary = "Inserir Usuário", description = "Faz a inserção do gênero passado no body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário foi inserido com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Usuário foi inserido com sucesso!"))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = ""))), //Adicionar erro
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> insertPerson(@RequestBody PersonsMobile personsMobile, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            }
            else {
                //Setando a data e definindo o fuso horário para UTC
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                //Criando um objeto persons e setando seu ID e o creationDate
                Persons person = new Persons(personsMobile.getGender(), personsMobile.getName(), personsMobile.getNickname(), personsMobile.getEmail(), hashPassword(personsMobile.getPassword()), personsMobile.getIsPro(), personsMobile.getUrlPhoto(), formatter.parse(personsMobile.getBirthDate()), personsMobile.getCellphone(), personsMobile.getRegistrationCompleted(), personsMobile.getRestrictions(), new ArrayList<Wishlist>(), new DirectionsWeek(), new ArrayList<ShoppingList>());
                person.setId(new ObjectId());
                person.setCreationDate(new Date());

                //Inserindo o usuário
                Persons personInsert = personsService.insertPerson(person);

                //Verificando se caso o usuário tenha sido inserido ele retornara que o usuário foi inserido, caso não ele retorna falso
                if (personInsert != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Usuário foi inserido com sucesso"));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possivel inserir o usuário"));
                }
            }
        } catch (DataIntegrityViolationException ttt) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapResults.put("message", "Valores inseridos incorretamente!"));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor!" + npc.getMessage()));
        }
    }

    @PutMapping("/updatePerson/{email}")
    @Operation(summary = "Atualizando o usuário", description = "Atualizando o usuário encontrado a partir do email passado como parâmetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário foi atualizado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persons.class))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Valores inseridos incorretamente!"))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "URL Incorreta!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> updatePersonByEmail(@Parameter(description = "Inserir o e-mail do usuário") @PathVariable String email, @RequestBody Map<String, Object> updatedValues) {
        try {
            Persons updatedPersons = personsService.findPersonByEmail(email);

            if (updatedPersons != null) {
                Query query = new Query(Criteria.where("email").is(email));
                Update update = new Update();

                if (updatedValues.containsKey("_id")) {
                    return ResponseEntity.ok(mapResults.put("message", "ID do usuário não pode ser atualizado"));
                }

                if (updatedValues.containsKey("gender")) {
                    final String gender = String.valueOf(updatedValues.get("gender"));
                    update.set("gender", gender);
                }

                if (updatedValues.containsKey("name")) {
                    final String name = String.valueOf(updatedValues.get("name"));
                    update.set("name", name);
                }

                if (updatedValues.containsKey("nickname")) {
                    final String nickname = String.valueOf(updatedValues.get("nickname"));
                    update.set("nickname", nickname);
                }

                if (updatedValues.containsKey("email")) {
                    final String newEmail = String.valueOf(updatedValues.get("email"));
                    update.set("email", newEmail);
                }

                if (updatedValues.containsKey("password")) {
                    final String password = String.valueOf(updatedValues.get("password"));
                    update.set("password", hashPassword(password));
                }

                if (updatedValues.containsKey("isPro")) {
                    final boolean isPro = Boolean.parseBoolean(String.valueOf(updatedValues.get("isPro")));
                    update.set("is_pro", isPro);
                }

                if (updatedValues.containsKey("urlPhoto")) {
                    final String urlPhoto = String.valueOf(updatedValues.get("urlPhoto"));
                    update.set("url_photo", urlPhoto);
                }

                if (updatedValues.containsKey("birthDate")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    final String dateString = String.valueOf(updatedValues.get("birthDate"));

                    update.set("birth_date", dateFormat.parse(dateString));
                }

                if (updatedValues.containsKey("cellphone")) {
                    final String cellphone = String.valueOf(updatedValues.get("cellphone"));
                    update.set("cellphone", cellphone);
                }

                if (updatedValues.containsKey("registrationCompleted")) {
                    final boolean registrationCompleted = Boolean.parseBoolean(String.valueOf(updatedValues.get("registrationCompleted")));
                    update.set("registration_completed", registrationCompleted);
                }

                if (updatedValues.containsKey("restrictions")) {
                    // Primeiro, obtenha a lista como uma lista de LinkedHashMap
                    List<LinkedHashMap<String, Object>> restrictionsData = (List<LinkedHashMap<String, Object>>) updatedValues.get("restrictions");

                    // Agora converta para uma lista de PersonsRestrictions
                    List<PersonsRestrictions> restrictions = new ArrayList<>();
                    for (LinkedHashMap<String, Object> restrictionData : restrictionsData) {
                        PersonsRestrictions restriction = new PersonsRestrictions();
                        restriction.setRestrictionId((String) restrictionData.get("restrictionId"));
                        // Preencha outros campos conforme necessário
                        restrictions.add(restriction);
                    }

                    List<PersonsRestrictions> personsRestrictions = updatedPersons.getRestrictions();

                    for (PersonsRestrictions objectRestrictions : restrictions) {
                        for (PersonsRestrictions objectAddRestrictions : personsRestrictions) {
                            if (objectRestrictions.getRestrictionId().equalsIgnoreCase(objectAddRestrictions.getRestrictionId())) {
                                //Fazendo a query para remover a restrição
                                update.pull("restrictions", objectRestrictions);
                            }else{
                                update.push("restrictions", objectRestrictions);
                            }
                        }
                    }
                }

                if (updatedValues.containsKey("directionsWeek")) {
                    LinkedHashMap<String, Object> directionData = (LinkedHashMap<String, Object>) updatedValues.get("directionsWeek");
                    DirectionsWeek directionsWeek = new DirectionsWeek((String) directionData.get("recipesId"));

                    update.set("directions_week", directionsWeek);
                }

                if (updatedValues.containsKey("creationDate")) {
                    return ResponseEntity.ok(mapResults.put("message", "Não é possivel atualizar a data de criação do usuário!"));
                }

                if (updatedValues.containsKey("deactivationDate")) {
                    return ResponseEntity.ok(mapResults.put("message", "Não é possivel atualizar a data de deleção do usuário, caso queira você deverá excluir o mesmo!"));
                }

                //Validando os dados
                DataBinder dataBinder = new DataBinder(updatedPersons);
                dataBinder.setValidator(validator);
                dataBinder.validate();
                BindingResult result = dataBinder.getBindingResult();
                if (result.hasErrors()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
                }

                UpdateResult updateResult = personsService.updatePerson(query, update);

                if(updateResult.getModifiedCount() >= 1){
                    return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Atualização feita com sucesso!"));
                }else{
                    return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possivel fazer a atualização do usuário!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possivel encontrar o usuário!"));
            }
        } catch (DataIntegrityViolationException ttt) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapResults.put("message", "Valores inseridos incorretamente!"));
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!" + nnn.getMessage()));
        } catch (Exception npc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor!" + npc.getMessage()));
        }
    }

    @PutMapping("/saveRecipesIngredients")
    @Operation(summary = "Salvar receitas e seus ingredientes", description = "Faz a inserção da receita e de seus ingredientes no shoppinglist do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita e seus comentários foi adicionado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> saveRecipesIngredients(@Parameter(description = "Inserir ID da receita") @RequestParam String recipesId, @Parameter(description = "Inserir e-mail do usuário") @RequestParam String personsEmail) {
        try {
            //Pegando a receita e o usuário a partir do seu ID e do seu e-mail
            Recipes recipes = recipesService.findRecipesById(new ObjectId(recipesId), personsEmail);
            Persons persons = personsService.findPersonByEmail(personsEmail);

            //Criando um novo objeto shopping list e adicionando o ID da receita nele
            ShoppingList shoppingList = new ShoppingList();

            //Criando um for na qual caso a receita já esteja dentro do ShoppingList ele não ira salvar
            for (ShoppingList objectShoppingList : persons.getShoppingList()) {
                if (objectShoppingList.getRecipesId().equalsIgnoreCase(recipesId)) {
                    return ResponseEntity.ok(mapResults.put("message", "Receita já está salva!"));
                }
            }

            //Salvando o id da receita caso ela não esteja salva
            shoppingList.setRecipesId(recipesId);
            shoppingList.setCreationDate(new Date());

            //Pegando a lista dos ingredientes das receitas
            List<IngredientsRecipes> listIngredientsRecipes = recipes.getIngredients();

            //Criando uma lista de strings para guardar os IDS dos ingredients
            List<String> listIngredientsId = new ArrayList<>();

            //Fazendo um for para guardar os ids das strings na lista de String
            for (IngredientsRecipes ingredientsRecipes : listIngredientsRecipes) {
                listIngredientsId.add(ingredientsRecipes.getIngredientId());
            }

            //Criando uma lista de ingredients shopping list
            List<IngredientsShoppingList> ingredientsShoppingList = new ArrayList<IngredientsShoppingList>();

            for (String ingredientId : listIngredientsId) {
                IngredientsShoppingList objectIngredientsShoppingList = new IngredientsShoppingList(ingredientId, false);
                ingredientsShoppingList.add(objectIngredientsShoppingList);
            }

            //Salvando os ingredientes no novo shoppinglist
            shoppingList.setIngredients(ingredientsShoppingList);

            //Validando os dados
            DataBinder dataBinder = new DataBinder(persons);
            dataBinder.setValidator(validator);
            dataBinder.validate();
            BindingResult result = dataBinder.getBindingResult();
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            }

            Query query = new Query(Criteria.where("email").is(personsEmail));
            Update update = new Update().push("shopping_list", shoppingList);

            //Pegando os resultados do update
            UpdateResult results = personsService.updatePerson(query, update);

            if(results.getModifiedCount() >= 1 ){
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Atualização foi feita com sucesso!"));
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possível fazer a atualização!"));
            }
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar a receita ou o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.ok(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    @PutMapping("/checkIngredients/{personsEmail}")
    @Operation(summary = "Salvar ingredientes da shopping list", description = "Marca um check no shoppinglist do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentários foram checkados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingList.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> checkIngredients(@Parameter(description = "Inserir e-mail do usuário") @PathVariable String personsEmail, @RequestBody ShoppingList listShoppingList) {
        try {
            //Criando um persons e pegando o seu shoppingList
            Persons persons = personsService.findPersonByEmail(personsEmail);
            List<ShoppingList> personsShoppingList = persons.getShoppingList();
            int contIngredientsChecked = 0;

            //Percorrendo o shoppingList do usuário e verificando se a receita existe
            for(ShoppingList objectPersonsShoppingList : personsShoppingList){

            //Verificando se o ingredient enviado é igual ao já existente
                if(objectPersonsShoppingList.getRecipesId().equalsIgnoreCase(listShoppingList.getRecipesId())){
                    //Guardando os ingredientes e percorrendo eles
                    List<IngredientsShoppingList> personsIngredientsShoppingList = objectPersonsShoppingList.getIngredients();
                    List<IngredientsShoppingList> ingredientsShoppingList = listShoppingList.getIngredients();

                    for (IngredientsShoppingList personsIngredients : personsIngredientsShoppingList){
                        for (IngredientsShoppingList ingredients : ingredientsShoppingList){

                            //Verificando se o id existe, e caso exista eu dou um check nele
                            if(personsIngredients.getIngredientId().equalsIgnoreCase(ingredients.getIngredientId())){
                                personsIngredients.setIsChecked(ingredients.getIsChecked());
                            }
                        }
                    }

                    objectPersonsShoppingList.setIngredients(personsIngredientsShoppingList);
                }

                //Verificando se todos os ingredientes estão checkados
                for(IngredientsShoppingList ingredients : objectPersonsShoppingList.getIngredients()){
                    if(ingredients.getIsChecked()){
                        contIngredientsChecked ++;
                    }
                }

                //Caso esteja eu removo o personShoppingList
                if(contIngredientsChecked == objectPersonsShoppingList.getIngredients().size()){
                    personsShoppingList.remove(objectPersonsShoppingList);
                }
            }

            Query query = new Query(Criteria.where("email").is(personsEmail));
            Update update = new Update().set("shopping_list", personsShoppingList);

            UpdateResult results = personsService.updatePerson(query, update);

            if(results.getModifiedCount() >= 1 ){
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Atualização foi feita com sucesso!"));
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possível fazer a atualização!"));
            }
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar a receita ou o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.ok(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    //Método para curtir receita
    @PutMapping("/likeRecipes")
    @Operation(summary = "Curtir receita", description = "Faz a inserção da receita na wishlist do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita foi curtida com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipes.class))),
            @ApiResponse(responseCode = "404", description = "Erro na comunicação com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Não foi possivel encontrar a receita!"))),
            @ApiResponse(responseCode = "500", description = "Erro interno com o servidor!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "Erro interno com o servidor!")))

    })
    public ResponseEntity<?> likeRecipes(@Parameter(description = "Inserir ID da receita") @RequestParam String recipesId, @Parameter(description = "Inserir e-mail do usuário") @RequestParam String personsEmail) {
        try {
            //Pegando a receita e o usuário a partir do seu ID e do seu e-mail
            Persons persons = personsService.findPersonByEmail(personsEmail);

            //Pegando a wishlist do usuário e verificando se a receita já foi curtida
            List<Wishlist> personsWishlist = persons.getWishlist();

            for (Wishlist objectWishlist : personsWishlist){
                if(objectWishlist.getRecipesId().equalsIgnoreCase(recipesId)){
                    //Fazendo a query para inserir a receita curtida
                    Query query = new Query(Criteria.where("email").is(personsEmail));
                    Update update = new Update().pull("wishlist", objectWishlist);

                    //Pegando os resultados do update
                    UpdateResult results = personsService.updatePerson(query, update);

                    if(results.getModifiedCount() >= 1 ){
                        return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Receita foi descutida com sucesso!"));
                    }else{
                        return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possível descurtir a receita!"));
                    }
                }
            }

            //Criando um novo objeto de receita curtida caso ela já não esteja salva
            Wishlist wishlist = new Wishlist(recipesId);

            //Fazendo a query para inserir a receita curtida
            Query query = new Query(Criteria.where("email").is(personsEmail));
            Update update = new Update().push("wishlist", wishlist);

            //Pegando os resultados do update
            UpdateResult results = personsService.updatePerson(query, update);

            if(results.getModifiedCount() >= 1 ){
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Atualização foi feita com sucesso!"));
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(mapResults.put("message", "Não foi possível fazer a atualização!"));
            }
        } catch (RuntimeException nnn) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar a receita ou o usuário!"));
        } catch (Exception npc) {
            return ResponseEntity.ok(mapResults.put("message", "Erro interno com o servidor"));
        }
    }

    @DeleteMapping("/deleteUserById/{email}")
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
    public ResponseEntity<?> deleteUserByEmail(@Parameter(description = "Inserir o ID do usuário a ser excluido") @PathVariable String email){
        try{
            Persons deletePerson = personsService.findPersonByEmail(email);

            //Validando os dados
            DataBinder dataBinder = new DataBinder(deletePerson);
            dataBinder.setValidator(validator);
            dataBinder.validate();
            BindingResult result = dataBinder.getBindingResult();

            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors(result));
            }else {
                if(personsService.deletePerson(deletePerson) != null){
                    return ResponseEntity.ok(mapResults.put("message", "Usuário foi excluido com sucesso!"));
                }else{
                    return ResponseEntity.ok(mapResults.put("message", "Não foi possivel excluir o usuário!"));
                }
            }
        }catch (RuntimeException nnn){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Não foi possível encontrar o usuário!"));
        }catch (Exception npc){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapResults.put("message", "Erro interno com o servidor!"));
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
