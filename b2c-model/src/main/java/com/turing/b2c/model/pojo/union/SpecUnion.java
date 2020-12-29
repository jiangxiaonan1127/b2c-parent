package com.turing.b2c.model.pojo.union;

import com.turing.b2c.model.pojo.Specification;
import com.turing.b2c.model.pojo.SpecificationOption;

import java.io.Serializable;
import java.util.List;

// 规格对象
//多个规格详情对象
//序列化   反序列化   必须添加无参构造方法
public class SpecUnion implements Serializable {
    private static final long serialVersionUID = 432817122109114492L;
    private Specification spec;
    private List<SpecificationOption> specOptionList;

    public Specification getSpec() {
        return spec;
    }

    public void setSpec(Specification spec) {
        this.spec = spec;
    }

    public List<SpecificationOption> getSpecOptionList() {
        return specOptionList;
    }

    public void setSpecOptionList(List<SpecificationOption> specOptionList) {
        this.specOptionList = specOptionList;
    }

    public SpecUnion(Specification spec, List<SpecificationOption> specOptionList) {
        this.spec = spec;
        this.specOptionList = specOptionList;
    }

    public SpecUnion() {
    }
}
