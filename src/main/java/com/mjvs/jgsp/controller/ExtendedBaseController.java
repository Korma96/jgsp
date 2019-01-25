package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.BaseLiteDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.service.ExtendedBaseService;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// PREREQUISITES: T must be class with name field !!!
public class ExtendedBaseController<T> extends BaseController<T>
{
    private ExtendedBaseService extendedBaseService;
    private String typeString;

    public ExtendedBaseController(ExtendedBaseService<T> extendedBaseService)
    {
        super(extendedBaseService);
        this.extendedBaseService = extendedBaseService;
        typeString = GenericTypeResolver
                .resolveTypeArgument(getClass(), ExtendedBaseController.class).getSimpleName();
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody BaseLiteDTO baseLiteDTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(baseLiteDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(typeString));
        }

        Result<Boolean> existsResult = extendedBaseService.exists(baseLiteDTO.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Class<T> type = (Class<T>)GenericTypeResolver
                .resolveTypeArgument(getClass(), ExtendedBaseController.class);
        T obj = ReflectionHelpers.GetInstanceOfT(type);
        ReflectionHelpers.InvokeSetNameMethod(obj, baseLiteDTO.getName());

        Result addResult = extendedBaseService.save(obj);
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        return ResponseHelpers.getResponseData(addResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody BaseDTO baseDTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(baseDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(typeString));
        }

        Result<Boolean> existsResult = extendedBaseService.exists(baseDTO.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Result<T> findResult = extendedBaseService.findById(baseDTO.getId());
        if(!findResult.hasData()) {
            throw new BadRequestException(findResult.getMessage());
        }

        T obj = findResult.getData();
        ReflectionHelpers.InvokeSetNameMethod(obj, baseDTO.getName());

        Result<Boolean> saveResult = extendedBaseService.save(obj);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }
}
