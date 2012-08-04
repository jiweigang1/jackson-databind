package com.fasterxml.jackson.databind.struct;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.databind.*;

public class TestFormatForCollections extends BaseMapTest
{
    // [issue#40]: Allow serialization 'as POJO' (resulting in JSON Object) 
    @JsonPropertyOrder({ "size", "value" })
    @JsonFormat(shape=Shape.OBJECT)
    @JsonIgnoreProperties({ "empty" }) // from 'isEmpty()'
    static class CollectionAsPOJO
        extends ArrayList<String>
    {
        private static final long serialVersionUID = 1L;

        @JsonProperty("size")
        public int foo() { return size(); }
        
        public String[] getValues() {
            return toArray(new String[size()]);
        }
    }

    /*
    /**********************************************************
    /* Test methods
    /**********************************************************
     */

    private final static ObjectMapper MAPPER = new ObjectMapper();    


    // [Issue#40]
    public void testListAsObject() throws Exception
    {
        // First, serialize a "POJO-List"
        CollectionAsPOJO list = new CollectionAsPOJO();
        list.add("a");
        list.add("b");
        String json = MAPPER.writeValueAsString(list);
        assertEquals("{\"size\":2,\"values\":[\"a\",\"b\"]}", json);

        // and then bring it back!
        CollectionAsPOJO result = MAPPER.readValue(json, CollectionAsPOJO.class);
        assertEquals(2, result.size());
    }

}
