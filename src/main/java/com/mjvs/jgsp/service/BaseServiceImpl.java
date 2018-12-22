package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ReflectionHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.repository.BaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.GenericTypeResolver;

import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<T> implements BaseService<T>
{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private BaseRepository<T> repository;
    private String typeString;

    public BaseServiceImpl(BaseRepository<T> baseRepository)
    {
        typeString = GenericTypeResolver
                .resolveTypeArgument(getClass(), BaseServiceImpl.class).getSimpleName();
        repository = baseRepository;
    }

    @Override
    public Result<Boolean> delete(T obj) throws Exception
    {
        Long id = ReflectionHelpers.InvokeGetIdMethod(obj);
        String message;
        try {
            repository.delete(obj);

            message = Messages.SuccessfullyDeleted(typeString, id);
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            message = Messages.ErrorDeleting(typeString, id, ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, Messages.DatabaseError());
        }
    }

    @Override
    public Result<Boolean> exists(Long id) throws Exception
    {
        Optional<T> obj = repository.findById(id);
        boolean isPresent = ReflectionHelpers.InvokeOptionalIsPresentMethod(obj);
        if(isPresent)
        {
            String message = Messages.AlreadyExists(typeString, id);
            logger.warn(message);
            return new Result<>(true, message);
        }
        return new Result<>(false);
    }

    @Override
    public Result<T> findById(Long id) throws Exception
    {
        Optional<T> obj = repository.findById(id);
        boolean isPresent = ReflectionHelpers.InvokeOptionalIsPresentMethod(obj);
        if(!isPresent)
        {
            String message = Messages.DoesNotExist(typeString, id);
            logger.warn(message);
            return new Result<>(null, message);
        }
        T result = (T)ReflectionHelpers.InvokeOptionalGetMethod(obj);
        return new Result<>(result);
    }

    @Override
    public Result<List<T>> getAll()
    {
        try {
            List<T> data = repository.findAll();
            return new Result<>(data);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage());
            return new Result<>(null, false, Messages.DatabaseError());
        }
    }

    @Override
    public Result<Boolean> save(T obj) throws Exception
    {
        if(obj == null) {
            throw new Exception(Messages.CantBeNull(typeString));
        }

        try {
            repository.save(obj);

            String message = Messages.SuccessfullySaved(typeString);
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorSaving(typeString, ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, Messages.DatabaseError());
        }
    }
}
