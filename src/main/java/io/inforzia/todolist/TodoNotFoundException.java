package io.inforzia.todolist;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(int id){
        log.warn(id + "번 TODO를 조회할 수 없습니다.");
    }
}
