package com.mrs.dto.request;

import com.mrs.annotation.ValidEnum;
import com.mrs.annotation.ValidPhoneNumber;
import com.mrs.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserCreateRequest {
    @NotBlank(message = "Name should not be blank")
    @NotNull(message = "Name should not be null")
    private String name;

    @NotBlank(message = "Phone number should not be blank")
    @NotNull(message = "Password should not be null")
    @ValidPhoneNumber(message = "Invalid mobile number")
    private String phone;

    @NotBlank(message = "Email should not be blank")
    @NotNull(message = "Email should not be null")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Gender should not be blank")
    @NotNull(message = "Gender should not be null")
    @ValidEnum(enumClass = Gender.class, message = "Gender is not valid")
    private String gender;

    public UserCreateRequest() {}
    public UserCreateRequest(String name, String phone, String email, String gender) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserCreateRequestDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                '}';
    }
}
