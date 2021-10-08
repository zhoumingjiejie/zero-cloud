package com.github.icezerocat.zeroxml.model.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 金三税率
 * CreateDate:  2021/9/23 17:06
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("taxML")
public class JsSl implements Serializable {
    private String yhsl;
    private String fdsl;
    private String yssdl;
}
