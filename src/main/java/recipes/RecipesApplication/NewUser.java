package recipes.RecipesApplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
    @NotBlank
    @NonNull
    @Email(regexp = ".+@.+\\..+")
    private String email;


    @NotBlank
    @NonNull
    @Size(min = 8)
    private String password;

}
