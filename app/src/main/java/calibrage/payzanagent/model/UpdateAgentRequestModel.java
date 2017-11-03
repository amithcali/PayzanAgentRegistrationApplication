package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class UpdateAgentRequestModel {
    @SerializedName("AgentRequestId")
    @Expose
    private Integer agentRequestId;
    @SerializedName("StatusTypeId")
    @Expose
    private Integer statusTypeId;
    @SerializedName("AssignToUserId")
    @Expose
    private String assignToUserId;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("Modified")
    @Expose
    private String modified;

    public Integer getAgentRequestId() {
        return agentRequestId;
    }

    public void setAgentRequestId(Integer agentRequestId) {
        this.agentRequestId = agentRequestId;
    }

    public Integer getStatusTypeId() {
        return statusTypeId;
    }

    public void setStatusTypeId(Integer statusTypeId) {
        this.statusTypeId = statusTypeId;
    }

    public String getAssignToUserId() {
        return assignToUserId;
    }

    public void setAssignToUserId(String assignToUserId) {
        this.assignToUserId = assignToUserId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }


}
