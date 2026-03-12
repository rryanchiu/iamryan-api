package dev.iamryan.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalyzeCommentVO {
    private Integer mood;
    private String moodDesc;

    private List<String> tags;

    private String reply;
}
