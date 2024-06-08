package com.java.recruitment.service.model.message;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleReceiverRequest {
    private List<String> receivers;
    private String copy;
    private String hiddenCopy;
}
