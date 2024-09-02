package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "Persons")
public class Persons {
    @Id
    @Schema(name = "Id do usuário", example = "10")
    private int id;

    @Schema(name = "Id do gênero do usuário", example = "2")
    @Field(name = "genders_id")
    @NotNull(message = "ID do gênero não deve ser nulo")
    private int gendersId;

    @Schema(name = "Nome do usuário", example = "Jose")
    @Size(max = 200, message = "Nome do usuário não deve ter mais de 200 caracteres")
    @NotNull(message = "Nome do usuário não deve ser nulo")
    private String name;
    @Schema(name = "Apelido do usuário", example = "JosePeQuente")
    @Size(max = 45, message = "Apelido do usuário não deve ter mais de 45 caracteres")
    @NotNull(message = "Apelido do usuário não deve ser nulo")
    private String nickname;

    @Email(message = "e-mail deve ser válido")
    @NotNull(message = "E-mail não deve ser nulo")
    @Schema(name = "E-mail do usuário", example = "jose@gmail.com")
    private String email;
    @Schema(name = "Senha do usuário em HASH", example = "$2a$10$7EqJtq98hPqEX7fNZaFWoO7BiEXLYAK9Lk8rClTl6l5povF9QIJFu")
    @Min(value = 8, message = "Senha deve ter 8 caracteres ou mais")
    private String password;
    @Schema(name = "Usuário é Administrador", example = "false")
    @Field(name = "id_adm")
    @NotBlank(message = "Registro deve ser indicado por 1 ou 0")
    private Boolean idAdm;
    @Schema(name = "URL da foto", example = "Teste") //Adicionar um exemplo da URL da photo
    @Field(name = "url_photo")
    private String urlPhoto;
    @Schema(name = "Data de nascimento", example = "2007/09/25")
    @Field(name = "birth_date")
    @NotBlank(message = "Usuário deve conter data de nascimento")
    private Date birthDate;
    @Size(min = 11, message="O Número de telefone deve conter DDD: XXXXXXXXXXX")
    @NotNull(message = "número de telefone não deve ser nulo!")
    private String cellphone;
    @Field(name = "registration_completed")
    @Schema(name = "Cadastro foi completo", example = "true")
    @NotNull(message = "Registro deve ser indicado por 1 ou 0")
    private Boolean registrationCompleted;
    @Schema(name = "Lista de Restrição", example = "Teste") //Adicionar um exemplo
    private List<Restrictions> restrictions;
    @Schema(name = "Lista de receitas favoritas", example = "Teste") //Adicionar um exemplo
    private List<Favorites> favorites;
    @Schema(name = "Lista de receitas salvas", example = "Teste") //Adicionar um exemplo
    private List<Wishlist> wishlist;
    @Schema(name = "Receita da semana", example = "Teste") //Adicionar um exemplo
    private List<DirectionsWeek> directionsWeeks;
    /*@Schema(name = "Lista de ingredientes que o ingrediente não gosta", example = "") //Testar
    private List<>*/
    @Field(name = "creation_date")
    @Schema(name = "Data de criação do usuário", example = "2024/08/27")
    private Date creationDate;

    public Persons(){}

    public Persons(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Persons(int id, int gendersId, String name, String nickname, String email, String password, Boolean idAdm, String urlPhoto, Date birthDate, String cellphone, Boolean registrationCompleted, List<Restrictions> restrictions, List<Favorites> favorites, List<Wishlist> wishlist, List<DirectionsWeek> directionsWeeks, Date creationDate) {
        this.id = id;
        this.gendersId = gendersId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.idAdm = idAdm;
        this.urlPhoto = urlPhoto;
        this.birthDate = birthDate;
        this.cellphone = cellphone;
        this.registrationCompleted = registrationCompleted;
        this.restrictions = restrictions;
        this.favorites = favorites;
        this.wishlist = wishlist;
        this.directionsWeeks = directionsWeeks;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGendersId() {
        return gendersId;
    }

    public void setGendersId(int gendersId) {
        this.gendersId = gendersId;
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

    public Boolean getIdAdm() {
        return idAdm;
    }

    public void setIdAdm(Boolean idAdm) {
        this.idAdm = idAdm;
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

    public List<Restrictions> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restrictions> restrictions) {
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

    public List<DirectionsWeek> getDirectionsWeeks() {
        return directionsWeeks;
    }

    public void setDirectionsWeeks(List<DirectionsWeek> directionsWeeks) {
        this.directionsWeeks = directionsWeeks;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Persons{" +
                "id=" + id +
                ", gendersId=" + gendersId +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", idAdm=" + idAdm +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", birthDate=" + birthDate +
                ", cellphone='" + cellphone + '\'' +
                ", registrationCompleted=" + registrationCompleted +
                ", restrictions=" + restrictions +
                ", favorites=" + favorites +
                ", wishlist=" + wishlist +
                ", directionsWeeks=" + directionsWeeks +
                ", creationDate=" + creationDate +
                '}';
    }
}
