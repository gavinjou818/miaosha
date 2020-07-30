package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.valtdator.ValidationResult;
import com.miaoshaproject.valtdator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService
{
    
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id)
    {
        UserDO userDo = userDOMapper.selectByPrimaryKey(id);

        if(userDo == null)
        {
            return null;
        }

        // 通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDo.getId());


        return convertFromDataObject(userDo,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {

        if(userModel == null)
        {
            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR);
        }

//        if(StringUtils.isEmpty(userModel.getName())
//            || userModel.getGender() == null
//                || userModel.getAge() == null
//                || StringUtils.isEmpty(userModel.getTelphone()))
//        {
//            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR);
//        }

        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors())
        {
            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        UserDO userDO = convertFromModel(userModel);
        try
        {
            userDOMapper.insertSelective(userDO);
        }
        catch (DuplicateKeyException ex)
        {
            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
        return ;
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {

        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null)
        {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword()))
        {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel)
    {
        if(userModel == null)
        {
            return null;
        }

        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }


    private UserDO convertFromModel(UserModel userModel)
    {
        if(userModel == null)
        {
            return null;
        }
        UserDO userDo = new UserDO();
        BeanUtils.copyProperties(userModel,userDo);
        return userDo;
    }


    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO)
    {
        if(userDO == null)
        {
            return  null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);

        if(userPasswordDO != null)
        {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}
