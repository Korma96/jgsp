package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ReflectionHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.repository.ExtendedBaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.GenericTypeResolver;

// PREREQUISITES: T must be class with name field !!!
public class ExtendedBaseServiceImpl<T> extends BaseServiceImpl<T> implements ExtendedBaseService<T>
{
    private Logger logger = LogManager.getLogger(this.getClass());
    private ExtendedBaseRepository<T> extendedBaseRepository;
    private String typeString;

    public ExtendedBaseServiceImpl(ExtendedBaseRepository<T> extendedBaseRepository)
    {
        super(extendedBaseRepository);
        this.extendedBaseRepository = extendedBaseRepository;
        typeString = GenericTypeResolver
                .resolveTypeArgument(getClass(), ExtendedBaseServiceImpl.class).getSimpleName();
    }

    @Override
    public Result<Boolean> save(T obj) throws Exception
    {
        if(obj == null) {
            throw new Exception(Messages.CantBeNull(typeString));
        }

        try {
            extendedBaseRepository.save(obj);

            String message = Messages.SuccessfullySaved(typeString,
                    ReflectionHelpers.InvokeGetNameMethod(obj));
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorSaving(typeString,
                    ReflectionHelpers.InvokeGetNameMethod(obj), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, Messages.DatabaseError());
        }
    }

    @Override
    public Result<Boolean> exists(String name)
    {
        T obj = extendedBaseRepository.findByName(name);
        if(obj != null)
        {
            String message = Messages.AlreadyExists(typeString, name);
            logger.warn(message);
            return new Result<>(true, message);
        }
        return new Result<>(false);
    }
}
