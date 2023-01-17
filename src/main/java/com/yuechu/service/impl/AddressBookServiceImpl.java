package com.yuechu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.entity.AddressBook;
import com.yuechu.mapper.AddressBookMapper;
import com.yuechu.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
