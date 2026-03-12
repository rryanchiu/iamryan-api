package dev.iamryan.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CommentAnalyzePlanGetVO {
    private String url;
    private String title;
    private String key;
    private String price;
}
