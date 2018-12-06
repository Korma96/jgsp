package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ReflectionHelpers;
import com.mjvs.jgsp.helpers.Result;
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
            return new Result<>(false, false, message);
        }
    }

    @Override
    public Result<Boolean> exists(Long id) throws Exception
    {
        // findById returns Optional :/
        T obj = repository.findById(id);
        boolean isPresent = ReflectionHelpers.InvokeOptionalIsPresentMethod(obj);
        if(isPresent)
        {
            String message = Messages.AlreadyExists(typeString, id);
            logger.warn(message);
            return new Result<>(true, false, message);
        }
        return new Result<>(false);
    }

    @Override
    public Result<T> findById(Long id) throws Exception
    {
        T obj = repository.findById(id);
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
            return new Result<>(null, false, ex.getMessage());
        }
    }

    @Override
    public Result<List<T>> getAllUndeletd() {
        List<T> data = repository.findByDeleted(false);
        return new Result<>(data);
    }

    @Override
    public Result<Boolean> save(T obj) throws Exception
    {
        if(obj == null) {
            throw new Exception(Messages.CantBeNull(typeString));
        }

        String message;
        String name = null;
        try{
            name = ReflectionHelpers.InvokeGetNameMethod(obj);
        }catch (Exception ex) {
        }
        try {
            repository.save(obj);

            if(name == null) {
                message = Messages.SuccessfullySaved(typeString);
            }
            else{
                message = Messages.SuccessfullySaved(typeString, name);
            }
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            if(name == null) {
                message = Messages.ErrorSaving(typeString, ex.getMessage());
            }
            else{
                message = Messages.ErrorSaving(typeString, name, ex.getMessage());
            }
            logger.error(message);
            return new Result<>(false, false, message);
        }
    }
}
