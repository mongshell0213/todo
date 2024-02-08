package hello.todo.domain;

public enum FinishType {
    FINISHED("완료"), NOT_FINISHED("미완");
    private final String description;

    FinishType(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
}
