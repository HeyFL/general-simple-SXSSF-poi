/*
 * Copyright (c) 2005-2018 , FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package com.chris.poi.generalsimplepoi.poi.domain;

import lombok.Data;

import java.security.Timestamp;

@Data
public class Snatched {
    private String id;
    private String torrentid;
    private String userid;
    private String ip;
    private String port;
    private String uploaded;
    private String downloaded;
    private String to_go;
    private String seedtime;
    private String leechtime;
    private Timestamp last_action;
    private Timestamp startdat;
    private Timestamp completedat;
    private String finished;

}
