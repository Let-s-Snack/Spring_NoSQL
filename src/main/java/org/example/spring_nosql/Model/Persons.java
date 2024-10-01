package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;
import java.util.List;

@Document(collection = "Persons")
public class Persons {
    @Id
    @Schema(description = "Id do usuário", example = "10")
    @Field(name = "_id")
    private ObjectId id;

    @Schema(description = "Gênero do usuário", example = "Feminino")
    @NotBlank(message = "Gênero não deve ser nulo")
    private String gender;

    @Schema(description = "Nome do usuário", example = "Jose")
    @Size(max = 200, message = "Nome do usuário não deve ter mais de 200 caracteres")
    @NotBlank(message = "Nome do usuário não deve ser nulo")
    private String name;

    @Schema(description = "Apelido do usuário", example = "JosePeQuente")
    @Size(max = 45, message = "Apelido do usuário não deve ter mais de 45 caracteres")
    @NotBlank(message = "Apelido do usuário não deve ser nulo")
    private String nickname;

    @Schema(description = "E-mail do usuário", example = "jose@gmail.com")
    @Email(message = "E-mail deve ser válido")
    @NotBlank(message = "E-mail não deve ser nulo!")
    private String email;

    @Schema(description = "Senha do usuário em HASH", example = "$2a$10$7EqJtq98hPqEX7fNZaFWoO7BiEXLYAK9Lk8rClTl6l5povF9QIJFu")
    @Size(min = 8, message = "Senha deve ter 8 caracteres ou mais")
    @NotBlank(message = "Senha não deve ser nula!")
    private String password;

    @Schema(description = "Usuário é profissional", example = "true")
    @Field(name = "is_pro")
    @NotNull(message = "Registro deve ser indicado por true ou false!")
    private boolean isPro;

    @Schema(description = "URL da foto", example = "Teste") //Adicionar um exemplo da URL da photo
    @Field(name = "url_photo")
    private String urlPhoto;

    @Schema(description = "Data de nascimento", example = "2007/09/25")
    @Field(name = "birth_date")
    @NotNull(message = "Usuário deve conter data de nascimento")
    private Date birthDate;

    @Schema(description = "Número de telefone do usuário", example = "11999999999")
    @Size(min = 11, message = "O Número de telefone deve conter DDD: XXXXXXXXXXX")
    @NotBlank(message = "número de telefone não deve ser nulo!")
    private String cellphone;

    @Schema(description = "Cadastro foi completo", example = "true")
    @Field(name = "registration_completed")
    @NotNull(message = "Registro deve ser indicado por true ou false")
    private boolean registrationCompleted;

    @Schema(description = "Lista de Restrição", example = "") //Adicionar um exemplo
    private List<PersonsRestrictions> restrictions;

    @Schema(description = "Lista de receitas favoritas", example = "Teste") //Adicionar um exemplo
    private List<Favorites> favorites;

    @Schema(description = "Lista de receitas salvas", example = "Teste") //Adicionar um exemplo
    private List<Wishlist> wishlist;

    @Schema(description = "Receita da semana", example = "Teste") //Adicionar um exemplo
    @Field(name = "directions_week")
    private List<DirectionsWeek> directionsWeek;
    @Schema(description = "Ingredientes salvos do usuários", example = "Teste") //Adicionar um exemplo
    @Field(name = "shopping_list")
    private List<ShoppingList> shoppingList;

    @Schema(description = "Data de criação do usuário", example = "2024/08/27")
    @Field(name = "creation_date")
    private Date creationDate;
    @Schema(description = "Data de exclusão do usuário", example = "2024/08/27")
    @Field(name = "deactivation_date")
    private Date deactivationDate;

    public Persons(){
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone, boolean registrationCompleted, List<PersonsRestrictions> restrictions, List<Favorites> favorites, List<Wishlist> wishlist, List<DirectionsWeek> directionsWeek, List<ShoppingList> shoppingList) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.registrationCompleted = registrationCompleted;
        this.restrictions = restrictions;
        this.favorites = favorites;
        this.wishlist = wishlist;
        this.directionsWeek = directionsWeek;
        this.shoppingList = shoppingList;
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone, List<PersonsRestrictions> restrictions) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.restrictions = restrictions;
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone, List<PersonsRestrictions> restrictions, List<Favorites> favorites) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.restrictions = restrictions;
        this.favorites = favorites;
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone, List<PersonsRestrictions> restrictions, List<Favorites> favorites, List<Wishlist> wishlist) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.restrictions = restrictions;
        this.favorites = favorites;
        this.wishlist = wishlist;
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone, List<PersonsRestrictions> restrictions, List<Favorites> favorites, List<Wishlist> wishlist, List<DirectionsWeek> directionsWeek) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.restrictions = restrictions;
        this.favorites = favorites;
        this.wishlist = wishlist;
        this.directionsWeek = directionsWeek;
    }

    public Persons(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, Date birthDate, String cellphone, List<PersonsRestrictions> restrictions, List<Favorites> favorites, List<Wishlist> wishlist, List<DirectionsWeek> directionsWeek, List<ShoppingList> shoppingList) {
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isPro = isPro;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.restrictions = restrictions;
        this.favorites = favorites;
        this.wishlist = wishlist;
        this.directionsWeek = directionsWeek;
        this.shoppingList = shoppingList;
    }

    public Persons(ObjectId id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Persons(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsPro() {
        return isPro;
    }

    public void setIsPro(Boolean isPro) {
        this.isPro = isPro;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Boolean getRegistrationCompleted() {
        return registrationCompleted;
    }

    public void setRegistrationCompleted(Boolean registrationCompleted) {
        this.registrationCompleted = registrationCompleted;
    }

    public List<PersonsRestrictions> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<PersonsRestrictions> restrictions) {
        this.restrictions = restrictions;
    }

    public List<Favorites> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorites> favorites) {
        this.favorites = favorites;
    }

    public List<Wishlist> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }

    public List<DirectionsWeek> getDirectionsWeek() {
        return directionsWeek;
    }

    public void setDirectionsWeek(List<DirectionsWeek> directionsWeek) {
        this.directionsWeek = directionsWeek;
    }

    public List<ShoppingList> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingList> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Date deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    @Override
    public String toString() {
        return "Persons{" +
                "id=" + id.toHexString() +
                ", gender='" + gender + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isPro=" + isPro +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", birthDate=" + birthDate +
                ", cellphone='" + cellphone + '\'' +
                ", registrationCompleted=" + registrationCompleted +
                ", restrictions=" + restrictions +
                ", favorites=" + favorites +
                ", wishlist=" + wishlist +
                ", directionsWeek=" + directionsWeek +
                ", shoppingList=" + shoppingList +
                ", creationDate=" + creationDate +
                ", deactivationDate=" + deactivationDate +
                '}';
    }
}
