package com.lufax.jijin.base.repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lufax.jijin.base.constant.InstructionType;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class InstructionNoGenerator extends AbstractRepository {

    private static final String dateFormatForInstructions = "yyyyMMddHHmmss";
    private static final String zeroDigits = "00000";

    @Resource(name = "sqlMapClient_busdata")
    protected SqlMapClient sqlMapClient;

    public String generateBusinessNo() {
        String formattedDate = new SimpleDateFormat(dateFormatForInstructions).format(new Date());
        String formattedInstruction = new DecimalFormat(zeroDigits).format(Integer.parseInt(getNextInstruction()));
        return String.format("%s%s", formattedDate, formattedInstruction);
    }

    public String generate(InstructionType instructionType) {
        String formattedDate = new SimpleDateFormat(dateFormatForInstructions).format(new Date());
        String formattedInstruction = new DecimalFormat(zeroDigits).format(Integer.parseInt(getNextInstruction()));
        return String.format("%s%s%s", formattedDate, formattedInstruction, instructionType.getValue());
    }

    @Override
    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

    @Override
    protected String nameSpace() {
        return "INSTRUCTION_SEQ";
    }


    public String getNextInstruction() {
        return String.valueOf(queryObject("getSeq"));
    }
    
    public Long getNext(){
    	return (Long)queryObject("getSeq");
    }
}

