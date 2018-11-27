package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ReflectionHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringExtensions;
import com.mjvs.jgsp.repository.BaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.GenericTypeResolver;

import java.util.List;

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
    public Result<Boolean> delete(T obj) {
        try {
            repository.delete(obj);

            String message = Messages.SuccessfullyDeleted(typeString,
                    ReflectionHelpers.InvokeGetNameMethod(obj));
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorDeleting(typeString,
                    ReflectionHelpers.InvokeGetNameMethod(obj), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, message);
        }
    }

    @Override
    public Result<Boolean> exists(Long id)
    {
        T obj = repository.findById(id);
        if(obj == null)
        {
            String message = Messages.AlreadyExists(typeString, id);
            logger.warn(message);
            return new Result<>(false, false, message);
        }
        return new Result<>(true);
    }

    @Override
    public Result<T> findById(Long id)
    {
        T obj = repository.findById(id);
        if(obj == null)
        {
            String message = Messages.DoesNotExists(typeString, id);
            logger.warn(message);
            return new Result<>(null, message);
        }
        return new Result<>(obj);
    }

    @Override
    public Result<List<T>> getAll()
    {
        try {
            List<T> data = repository.findAll();
            return new Result<>(data);
        }
        catch (Exception ex) {
            return new Result<>(null, false, ex.getMessage());
        }
    }

    @Override
    public Result<Boolean> save(T obj) throws Exception
    {
        if(obj == null) {
            throw new Exception(Messages.CantBeNull(typeString));
        }

        if(StringExtensions.isEmptyOrWhitespace(ReflectionHelpers.InvokeGetNameMethod(obj))) {
            throw new Exception(Messages.CantBeEmptyOrWhitespace(typeString));
        }

        try {
            repository.save(obj);

            String message = Messages.SuccessfullyAdded(typeString,
                    ReflectionHelpers.InvokeGetNameMethod(obj));
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorAdding(typeString,
                    ReflectionHelpers.InvokeGetNameMethod(obj), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, message);
        }
    }
}
