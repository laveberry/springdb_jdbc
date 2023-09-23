package laveberry.jdbc.repository.ex;

// 직접 만든 예외라 JDBC나 JPA같은 특정 기술에 종속되지 않음
public class MyDuplicationKeyException extends MyDbException{
    public MyDuplicationKeyException() {
        super();
    }

    public MyDuplicationKeyException(String message) {
        super(message);
    }

    public MyDuplicationKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicationKeyException(Throwable cause) {
        super(cause);
    }
}
