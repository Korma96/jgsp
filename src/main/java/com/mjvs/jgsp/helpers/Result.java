package com.mjvs.jgsp.helpers;

public class Result<T>
{
    private T data;
    // method finished without errors
    private boolean isSuccess;
    private String message;

    public Result(T data)
    {
        this.data = data;
        isSuccess = true;
        message = "";
    }

    public Result(T data, boolean isSuccess)
    {
        this.data = data;
        this.isSuccess = isSuccess;
        message = "";
    }

    public Result(T data, boolean isSuccess, String message)
    {
        this.data = data;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public Result(T data, String message)
    {
        this.data = data;
        this.isSuccess = true;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public boolean hasData() { return data != null; }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure(){
        return !isSuccess;
    }

    public String getMessage() {
        return message;
    }

}
