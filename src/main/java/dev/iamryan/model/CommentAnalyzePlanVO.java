package dev.iamryan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CommentAnalyzePlanVO implements Serializable {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate date;
    private String planName;
    private Integer totalLimit;
    private Integer todayUsedCount;
    private Boolean isBeta;
}
