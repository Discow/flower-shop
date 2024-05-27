package com.example.flowershop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`flower_category`")
public class FlowerCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String description;

    //建立关联
    @OneToMany(mappedBy = "flowerCategory", cascade = CascadeType.REMOVE)
    @Where(clause = "status='已上架'")
    @JsonManagedReference
    List<Flower> flowers;
}
