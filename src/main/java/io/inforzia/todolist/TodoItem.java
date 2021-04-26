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
    private Long created = System.currentTimeMillis()/1000;

    @Column(name = "COMPLETED")
    private Boolean completed;

    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public void setItem(String item){
        this.item = item;
    }
    public String getItem(){
        return item;
    }
    public Long getCreated(){
        return this.created;
    }
    public void setCompleted(boolean Completed){
        this.completed = Completed;
    }
    public boolean getCompleted(){
        return completed;
    }
}
