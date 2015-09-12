package com.apecalc.wificapture.dao;

import com.apecalc.wificapture.models.Capture;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;


public class CaptureDao extends BaseDaoImpl<Capture, Integer>{ // Classe e tipo da chave primaria

    public CaptureDao(ConnectionSource connectionSource) throws SQLException {
        super(Capture.class);
        setConnectionSource(connectionSource);
        initialize();
    }
}
