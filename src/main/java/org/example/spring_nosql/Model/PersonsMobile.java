package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

public class PersonsMobile {
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

    @Schema(description = "Usuário é premium", example = "true")
    @Field(name = "is_pro")
    @NotNull(message = "Registro deve ser indicado por true ou false!")
    private boolean isPro;

    @Schema(description = "URL da foto", example = "https://i.pinimg.com/originals/5e/6f/7a/8b9d0a2b1c2.jpg")
    @Field(name = "url_photo")
    private String urlPhoto;

    @Schema(description = "Data de nascimento", example = "2007/09/25")
    @Field(name = "birth_date")
    @NotNull(message = "Usuário deve conter data de nascimento")
    private String birthDate;

    @Schema(description = "Número de telefone do usuário", example = "11999999999")
    @Size(min = 11, message = "O Número de telefone deve conter DDD: XXXXXXXXXXX")
    @NotBlank(message = "número de telefone não deve ser nulo!")
    private String cellphone;

    @Schema(description = "Cadastro foi completo", example = "true")
    @Field(name = "registration_completed")
    @NotNull(message = "Registro deve ser indicado por true ou false")
    private boolean registrationCompleted;

    @Schema(description = "Lista de Restrição", example = "{\n" +
            "        \"restrictionId\": \"60d5f4832f8fb814b56fa287\",\n" +
            "        \"name\": \"alérgica a amendoim\",\n" +
            "        \"description\": \"Não pode consumir amendoim\",\n" +
            "        \"urlPhoto\": \"https://i.pinimg.com/originals/5e/6f/7a/8b9d0a2b1c2.jpg\",\n" +
            "        \"creationDate\": \"2024-09-23T16:08:35.143+00:00\"\n" +
            "      }")
    private List<PersonsRestrictions> restrictions;


    @Schema(description = "Data de criação do usuário", example = "2024/08/27")
    @Field(name = "creation_date")
    private String creationDate;


    public PersonsMobile(){
    }

    public PersonsMobile(String gender, String name, String nickname, String email, String password, boolean isPro, String urlPhoto, String birthDate, String cellphone, boolean registrationCompleted, List<PersonsRestrictions> restrictions, String creationDate) {
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
        this.creationDate = creationDate;
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

    public boolean getIsPro() {
        return this.isPro;
    }

    public void setIsPro(boolean isPro) {
        this.isPro = isPro;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public boolean getRegistrationCompleted() {
        return this.registrationCompleted;
    }

    public void setRegistrationCompleted(boolean registrationCompleted) {
        this.registrationCompleted = registrationCompleted;
    }

    public List<PersonsRestrictions> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<PersonsRestrictions> restrictions) {
        this.restrictions = restrictions;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String toString() {
        return "PersonsMobile{" +
                "gender='" + gender + '\'' +
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
                ", creationDate='" + creationDate + '\'' +
                '}';
    }
}
