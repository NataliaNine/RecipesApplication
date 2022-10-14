package recipes.RecipesApplication;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "recipe")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column
    @JsonIgnore
    private String createdBy;

    @Column
    @NotBlank
    @NonNull
    private String name;

    @Column
    @NotBlank
    @NonNull
    private String description;

    @Column
    @NotBlank
    @NonNull
    private String category;

    @Column
    private String date;


    @Column
    @ElementCollection
    @NotEmpty
    @NonNull
    private List<String> ingredients;


    @Column
    @ElementCollection
    @NotEmpty
    @NonNull
    private List<String> directions;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                '}';
    }
}
