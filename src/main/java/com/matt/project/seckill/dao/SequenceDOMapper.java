package com.matt.project.seckill.dao;

import com.matt.project.seckill.dataobject.SequenceDO;

public interface SequenceDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Sun Dec 13 10:14:42 CST 2020
     */
    int insert(SequenceDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Sun Dec 13 10:14:42 CST 2020
     */
    int insertSelective(SequenceDO record);

    SequenceDO getSequenceByName(String name);

    int updateByPramKeySelective(SequenceDO sequenceDO);
}