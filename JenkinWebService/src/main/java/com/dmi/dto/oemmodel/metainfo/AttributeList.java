
package com.dmi.dto.oemmodel.metainfo;

import java.io.Serializable;





import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonInclude(Include.NON_EMPTY)
public class AttributeList implements Serializable{

	private static final long serialVersionUID = 1L;
	@SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private String value;
    
    public AttributeList()
    {
    	
    }
    
    private AttributeList(final String key, final String value)
    	   {
    	      this.key = key;
    	      this.value = value;
    	   }
    
        /**
     * 
     * @return
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /*@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }*/
    
    public static class AttributeListBuilder
    {

        private String key;

        private String value;
        
        public AttributeListBuilder()
        {
        	
        }
        
        public AttributeListBuilder withKey(String key)
        {
        this.key = key;
        return this;
        }
        
        public AttributeListBuilder withValue(String value)
        {
        this.value = value;
        return this;
        }
        
        public AttributeList buildAttributeList()
        {
           return new AttributeList(key,value);
        }
    	
    }
}
