/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.core.testData.model.table.setting.mapping;

import java.util.List;
import java.util.Stack;

import org.robotframework.ide.core.testData.model.FilePosition;
import org.robotframework.ide.core.testData.model.RobotFileOutput;
import org.robotframework.ide.core.testData.model.table.SettingTable;
import org.robotframework.ide.core.testData.model.table.mapping.ElementPositionResolver;
import org.robotframework.ide.core.testData.model.table.mapping.ElementPositionResolver.PositionExpected;
import org.robotframework.ide.core.testData.model.table.mapping.IParsingMapper;
import org.robotframework.ide.core.testData.model.table.setting.Metadata;
import org.robotframework.ide.core.testData.text.read.IRobotTokenType;
import org.robotframework.ide.core.testData.text.read.ParsingState;
import org.robotframework.ide.core.testData.text.read.RobotLine;
import org.robotframework.ide.core.testData.text.read.recognizer.RobotToken;
import org.robotframework.ide.core.testData.text.read.recognizer.RobotTokenType;

import com.google.common.annotations.VisibleForTesting;


public class MetadataMapper implements IParsingMapper {

    private final ElementPositionResolver positionResolver;


    public MetadataMapper() {
        this.positionResolver = new ElementPositionResolver();
    }


    @Override
    public RobotToken map(RobotLine currentLine,
            Stack<ParsingState> processingState,
            RobotFileOutput robotFileOutput, RobotToken rt, FilePosition fp,
            String text) {
        rt.setType(RobotTokenType.SETTING_METADATA_DECLARATION);
        rt.setText(new StringBuilder(text));
        rt.setRaw(new StringBuilder(text));

        SettingTable settings = robotFileOutput.getFileModel()
                .getSettingTable();
        Metadata metadata = new Metadata(rt);
        settings.addMetadata(metadata);
        processingState.push(ParsingState.SETTING_METADATA);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(RobotFileOutput robotFileOutput,
            RobotLine currentLine, RobotToken rt, String text,
            Stack<ParsingState> processingState) {
        boolean result = false;
        List<IRobotTokenType> types = rt.getTypes();
        if (types.size() == 1
                && types.get(0) == RobotTokenType.SETTING_METADATA_DECLARATION) {
            if (positionResolver.isCorrectPosition(
                    PositionExpected.SETTING_TABLE_ELEMENT_DECLARATION,
                    robotFileOutput.getFileModel(), currentLine, rt)) {
                if (isIncludedInSettingTable(currentLine, processingState)) {
                    result = true;
                } else {
                    // FIXME: it is in wrong place means no settings table
                    // declaration
                }
            } else {
                // FIXME: wrong place | | Library or | Library | Library X |
                // case.
            }
        }

        return result;
    }


    @VisibleForTesting
    protected boolean isIncludedInSettingTable(final RobotLine line,
            final Stack<ParsingState> processingState) {
        boolean result;
        if (!processingState.isEmpty()) {
            result = (processingState.get(processingState.size() - 1) == ParsingState.SETTING_TABLE_INSIDE);
        } else {
            result = false;
        }

        return result;
    }
}
