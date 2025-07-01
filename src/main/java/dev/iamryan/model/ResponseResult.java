package me.rryan.tinyurl.model;


public class ResponseResult<T> {

    private Integer code;

    private String message;
    private String msg;

    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(0);
        result.setMessage("OK");
        result.setData(data);
        return result;
    }

    public static ResponseResult<Void> success() {
        ResponseResult<Void> result = new ResponseResult<>();
        result.setCode(0);
        result.setMessage("OK");
        return result;
    }

    public static <T> ResponseResult<T> error(int code, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
