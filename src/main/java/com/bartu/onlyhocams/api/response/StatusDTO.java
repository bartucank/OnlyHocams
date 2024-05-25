package com.bartu.onlyhocams.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusDTO {
    private String statusCode;
    private String msg;
    private Object additionalInformation;
}