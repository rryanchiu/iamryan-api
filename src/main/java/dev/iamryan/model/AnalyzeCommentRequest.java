package me.rryan.tinyurl.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyzeCommentRequest {
    private String content;
    private String deviceId;
}
