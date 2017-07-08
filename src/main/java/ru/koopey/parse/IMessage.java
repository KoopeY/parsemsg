package ru.koopey.parse;

import ru.koopey.entity.EmailResult;

import java.io.InputStream;

public interface IMessage {
    EmailResult parseMsg(InputStream inputStream) throws Exception;
}
