package com.github.icezerocat.zeroxml.model.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 学生
 * CreateDate:  2021/9/23 10:26
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias(value = "Student")
public class Student implements Serializable {

    private String firstName;
    private String lastName;
    private int rollNo;
    private String className;
    private Address address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address implements Serializable {
        private String area;
        private String city;
        private String state;
        private String country;
    }
}
