// Name: Yilin Wang
// USC loginid: wangyili
// CS 455 PA4
// Spring 2016

import java.io.IOException;

public class BadDataException extends IOException
{
    public BadDataException() {}
    public BadDataException(String message)
    {
        super(message);
    }
}

