/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ring.welkin.common.persistence.mybatis.mapper.example;

import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 通用Mapper接口,Example查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface SelectOneColumnByExampleMapper<T> {

    /**
     * 根据Example条件查询一个String类型的列,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return String列数据
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<String> selectStringListByExample(Example example);

    /**
     * 根据Example条件查询一个String类型的列,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Integer列数据
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<Integer> selectIntegerListByExample(Example example);

    /**
     * 根据Example条件查询一个String类型的列,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Long列数据
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<Long> selectLongListByExample(Example example);

    /**
     * 根据Example条件查询一个Float类型的列,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Float列数据
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<Float> selectFloatListByExample(Example example);

    /**
     * 根据Example条件查询一个Double类型的列,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Double列数据
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<Double> selectDoubleListByExample(Example example);

    /**
     * 根据Example条件查询一个Object类型的列,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Object列数据
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    List<Object> selectObjectListByExample(Example example);

    /**
     * 根据Example条件查询一个String类型的唯一值,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return String唯一值
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    String selectStringOneByExample(Example example);

    /**
     * 根据Example条件查询一个String类型的唯一值,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Integer唯一值
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    Integer selectIntegerOneByExample(Example example);

    /**
     * 根据Example条件查询一个String类型的唯一值,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Long唯一值
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    Long selectLongOneByExample(Example example);

    /**
     * 根据Example条件查询一个Float类型的唯一值,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Float唯一值
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    Float selectFloatOneByExample(Example example);

    /**
     * 根据Example条件查询一个Double类型的唯一值,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Double唯一值
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    Double selectDoubleOneByExample(Example example);

    /**
     * 根据Example条件查询一个Object类型的唯一值,需要在example中设置唯一的查询列
     *
     * @param example 查询条件
     * @return Object唯一值
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    Object selectObjectOneByExample(Example example);

}