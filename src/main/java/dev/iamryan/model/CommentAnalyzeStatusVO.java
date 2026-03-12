package dev.iamryan.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentAnalyzeStatusVO {
    private String version;

    private Integer status;
}
