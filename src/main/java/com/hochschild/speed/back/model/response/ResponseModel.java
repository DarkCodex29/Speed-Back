package com.hochschild.speed.back.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class ResponseModel {

    @JsonIgnore
    private HttpStatus httpSatus;
    private String message;
    private Integer id;


}
