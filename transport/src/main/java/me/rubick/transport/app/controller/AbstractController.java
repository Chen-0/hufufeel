package me.rubick.transport.app.controller;

import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.service.MessageService;
import me.rubick.transport.app.service.UserService;

import javax.annotation.Resource;

public abstract class AbstractController {

    @Resource
    protected UserService userService;

    @Resource
    protected MessageService messageService;

    @Resource
    protected DocumentService documentService;
}
