
package com.dmi.dto.oemmodel.rawmessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawMessage implements Serializable{

private static final long serialVersionUID = 1L;
	@SerializedName("messageModelVersion")
    @Expose
    private String messageModelVersion;
    @SerializedName("assetId")
    @Expose
    private String assetId;
    @SerializedName("domainId")
    @Expose
    private String domainId;
    @SerializedName("assetDataList")
    @Expose
    private List<AssetDataList> assetDataList = new ArrayList<AssetDataList>();

    /**
     * 
     * @return
     *     The messageModelVersion
     */
    public String getMessageModelVersion() {
        return messageModelVersion;
    }

    /**
     * 
     * @param messageModelVersion
     *     The messageModelVersion
     */
    public void setMessageModelVersion(String messageModelVersion) {
        this.messageModelVersion = messageModelVersion;
    }

    /**
     * 
     * @return
     *     The assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * 
     * @param assetId
     *     The assetId
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    /**
     * 
     * @return
     *     The domainId
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * 
     * @param domainId
     *     The domainId
     */
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    /**
     * 
     * @return
     *     The assetDataList
     */
    public List<AssetDataList> getAssetDataList() {
        return assetDataList;
    }

    /**
     * 
     * @param assetDataList
     *     The assetDataList
     */
    public void setAssetDataList(List<AssetDataList> assetDataList) {
        this.assetDataList = assetDataList;
    }

    /*@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
*/
}
