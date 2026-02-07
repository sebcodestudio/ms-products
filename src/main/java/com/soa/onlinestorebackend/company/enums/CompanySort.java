package com.soa.onlinestorebackend.company.enums;

import org.springframework.data.domain.Sort;

public enum CompanySort {
    ID_ASC("id", Sort.Direction.ASC),
    ID_DESC("id", Sort.Direction.DESC),
    LEGAL_NAME_ASC("legalName", Sort.Direction.ASC),
    LEGAL_NAME_DESC("legalName", Sort.Direction.DESC);


    private final String field;
    private final Sort.Direction direction;


    CompanySort(String field, Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }


    public Sort toSort() {
        return Sort.by(direction, field);
    }

}
