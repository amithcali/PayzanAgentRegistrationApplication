package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 10/27/2017.
 */

public class AgentPersonalInfo {

    @SerializedName("TitleTypeId")
    @Expose
    private Integer titleTypeId;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleName")
    @Expose
    private String middleName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("GenderTypeId")
    @Expose
    private Integer genderTypeId;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("Address1")
    @Expose
    private String address1;
    @SerializedName("Address2")
    @Expose
    private String address2;
    @SerializedName("Landmark")
    @Expose
    private String landmark;
    @SerializedName("VillageId")
    @Expose
    private Integer villageId;
    @SerializedName("ParentAspNetUserId")
    @Expose
    private Object parentAspNetUserId;
    @SerializedName("AgentBusinessCategoryId")
    @Expose
    private Integer agentBusinessCategoryId;
    @SerializedName("AgentRequestId")
    @Expose
    private Integer agentRequestId;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;

    public Integer getTitleTypeId() {
        return titleTypeId;
    }

    public void setTitleTypeId(Integer titleTypeId) {
        this.titleTypeId = titleTypeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGenderTypeId() {
        return genderTypeId;
    }

    public void setGenderTypeId(Integer genderTypeId) {
        this.genderTypeId = genderTypeId;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public Object getParentAspNetUserId() {
        return parentAspNetUserId;
    }

    public void setParentAspNetUserId(Object parentAspNetUserId) {
        this.parentAspNetUserId = parentAspNetUserId;
    }

    public Integer getAgentBusinessCategoryId() {
        return agentBusinessCategoryId;
    }

    public void setAgentBusinessCategoryId(Integer agentBusinessCategoryId) {
        this.agentBusinessCategoryId = agentBusinessCategoryId;
    }

    public Integer getAgentRequestId() {
        return agentRequestId;
    }

    public void setAgentRequestId(Integer agentRequestId) {
        this.agentRequestId = agentRequestId;
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

}
