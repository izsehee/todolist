package io.inforzia.todolist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "TODOITEM")
@Entity
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @Column(name = "ITEM")
    @ApiModelProperty(example = "TO DO")
    private String item;

    @Column(name = "CREATED")
    @ApiModelProperty(hidden = true)
    private Long created = System.currentTimeMillis();

    @Column(name = "COMPLETED")
    @ApiModelProperty(example = "false")
    private Boolean completed;

    @Column(name = "USER")
    @ApiModelProperty(hidden = true)
    private String user;
}
