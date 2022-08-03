package ru.otr.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Member {

    @Id
    private Long id;

    private String name;
}