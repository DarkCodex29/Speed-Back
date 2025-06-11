package com.hochschild.speed.back.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

public @Data
class ResponseModelFile {

    @JsonIgnore
    private HttpStatus httpSatus;
    private String message;
    private String random;
    private String archivo;
    private String nombreArchivoDisco;
}
