package org.celltinel.e2e.encryption.common.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private ErrorLevel level;
    private String property;
    private String message;

}