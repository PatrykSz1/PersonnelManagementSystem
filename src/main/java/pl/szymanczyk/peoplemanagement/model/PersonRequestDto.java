package pl.szymanczyk.peoplemanagement.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PersonRequestDto {

    @NotNull(message = "Type is required")
    private String type;

    @NotNull(message = "Map is required")
    private Map<String, String> args;
}