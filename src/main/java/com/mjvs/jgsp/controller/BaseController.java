package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.service.BaseService;
import com.mjvs.jgsp.helpers.ResponseHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class BaseController<T>
{
    private BaseService baseService;

    public BaseController(BaseService<T> baseService)
    {
        this.baseService = baseService;
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception
    {
        Result<T> findResult = baseService.findById(id);
        if(!findResult.hasData()){
            throw new BadRequestException(findResult.getMessage());
        }

        T data = findResult.getData();
        Result<Boolean> deleteResult = baseService.delete(data);
        if(deleteResult.isFailure()) {
            throw new DatabaseException(deleteResult.getMessage());
        }

        return ResponseHelpers.getResponseData(deleteResult);
    }
}
