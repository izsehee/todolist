package io.inforzia.todolist;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "TODOITEM")
@Entity
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "ITEM")
    private String item;

    @Column(name = "CREATED")
    private Long created = System.currentTimeMillis();

    @Column(name = "COMPLETED")
    private Boolean completed;

    @Column(name = "USER")
    private String user;
}
