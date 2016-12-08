package io.robe.hibernate.criteria.api;

import io.robe.hibernate.test.entity.UserDTO;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kamilbukum on 06/12/16.
 */
public class CriteriaUtilTest {

    @Test
    public void fromEntityFields2SearchFieldArray(){

        String[] expectedFields = new String[] {
                "email",
                "roleOidName"
        };

        String[] fields = CriteriaUtil.fromEntityFields2SearchFieldArray(UserDTO.class);

        Assert.assertArrayEquals(expectedFields, fields);
    }
}