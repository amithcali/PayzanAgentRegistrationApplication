package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 11/22/2017.
 */

public class GetPersonalInfoModel {
    @SerializedName("ListResult")
    @Expose
    private List<ListResult> listResult = null;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("AffectedRecords")
    @Expose
    private Integer affectedRecords;
    @SerializedName("EndUserMessage")
    @Expose
    private String endUserMessage;
    @SerializedName("ValidationErrors")
    @Expose
    private List<Object> validationErrors = null;
    @SerializedName("Exception")
    @Expose
    private Object exception;

    public List<ListResult> getListResult() {
        return listResult;
    }

    public void setListResult(List<ListResult> listResult) {
        this.listResult = listResult;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getAffectedRecords() {
        return affectedRecords;
    }

    public void setAffectedRecords(Integer affectedRecords) {
        this.affectedRecords = affectedRecords;
    }

    public String getEndUserMessage() {
        return endUserMessage;
    }

    public void setEndUserMessage(String endUserMessage) {
        this.endUserMessage = endUserMessage;
    }

    public List<Object> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<Object> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }
    public class ListResult {

        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("TitleTypeName")
        @Expose
        private String titleTypeName;
        @SerializedName("GenderType")
        @Expose
        private String genderType;
        @SerializedName("BusinessCategoryTypeId")
        @Expose
        private Integer businessCategoryTypeId;
        @SerializedName("BusinessCategoryName")
        @Expose
        private String businessCategoryName;
        @SerializedName("StateName")
        @Expose
        private String stateName;
        @SerializedName("StateId")
        @Expose
        private Integer stateId;
        @SerializedName("ProvinceName")
        @Expose
        private String provinceName;
        @SerializedName("ProvinceId")
        @Expose
        private Integer provinceId;
        @SerializedName("DistrictName")
        @Expose
        private String districtName;
        @SerializedName("DistrictId")
        @Expose
        private Integer districtId;
        @SerializedName("CountryId")
        @Expose
        private Integer countryId;
        @SerializedName("CountryName")
        @Expose
        private String countryName;
        @SerializedName("VillageName")
        @Expose
        private String villageName;
        @SerializedName("MandalName")
        @Expose
        private String mandalName;
        @SerializedName("MandalId")
        @Expose
        private Integer mandalId;
        @SerializedName("PostCode")
        @Expose
        private Integer postCode;
        @SerializedName("AgentRequestId")
        @Expose
        private Integer agentRequestId;
        @SerializedName("ParentAspNetUserFirstName")
        @Expose
        private Object parentAspNetUserFirstName;
        @SerializedName("ParentAspNetUserMiddleName")
        @Expose
        private Object parentAspNetUserMiddleName;
        @SerializedName("ParentAspNetUserLastName")
        @Expose
        private Object parentAspNetUserLastName;
        @SerializedName("ParentAspNetUserPhoneNumber")
        @Expose
        private Object parentAspNetUserPhoneNumber;
        @SerializedName("AspNetUserId")
        @Expose
        private String aspNetUserId;
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
        @SerializedName("EducationTypeId")
        @Expose
        private Object educationTypeId;
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
        private Object modifiedBy;
        @SerializedName("Created")
        @Expose
        private String created;
        @SerializedName("Modified")
        @Expose
        private String modified;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTitleTypeName() {
            return titleTypeName;
        }

        public void setTitleTypeName(String titleTypeName) {
            this.titleTypeName = titleTypeName;
        }

        public String getGenderType() {
            return genderType;
        }

        public void setGenderType(String genderType) {
            this.genderType = genderType;
        }

        public Integer getBusinessCategoryTypeId() {
            return businessCategoryTypeId;
        }

        public void setBusinessCategoryTypeId(Integer businessCategoryTypeId) {
            this.businessCategoryTypeId = businessCategoryTypeId;
        }

        public String getBusinessCategoryName() {
            return businessCategoryName;
        }

        public void setBusinessCategoryName(String businessCategoryName) {
            this.businessCategoryName = businessCategoryName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public Integer getStateId() {
            return stateId;
        }

        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public Integer getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Integer provinceId) {
            this.provinceId = provinceId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public Integer getDistrictId() {
            return districtId;
        }

        public void setDistrictId(Integer districtId) {
            this.districtId = districtId;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public String getMandalName() {
            return mandalName;
        }

        public void setMandalName(String mandalName) {
            this.mandalName = mandalName;
        }

        public Integer getMandalId() {
            return mandalId;
        }

        public void setMandalId(Integer mandalId) {
            this.mandalId = mandalId;
        }

        public Integer getPostCode() {
            return postCode;
        }

        public void setPostCode(Integer postCode) {
            this.postCode = postCode;
        }

        public Integer getAgentRequestId() {
            return agentRequestId;
        }

        public void setAgentRequestId(Integer agentRequestId) {
            this.agentRequestId = agentRequestId;
        }

        public Object getParentAspNetUserFirstName() {
            return parentAspNetUserFirstName;
        }

        public void setParentAspNetUserFirstName(Object parentAspNetUserFirstName) {
            this.parentAspNetUserFirstName = parentAspNetUserFirstName;
        }

        public Object getParentAspNetUserMiddleName() {
            return parentAspNetUserMiddleName;
        }

        public void setParentAspNetUserMiddleName(Object parentAspNetUserMiddleName) {
            this.parentAspNetUserMiddleName = parentAspNetUserMiddleName;
        }

        public Object getParentAspNetUserLastName() {
            return parentAspNetUserLastName;
        }

        public void setParentAspNetUserLastName(Object parentAspNetUserLastName) {
            this.parentAspNetUserLastName = parentAspNetUserLastName;
        }

        public Object getParentAspNetUserPhoneNumber() {
            return parentAspNetUserPhoneNumber;
        }

        public void setParentAspNetUserPhoneNumber(Object parentAspNetUserPhoneNumber) {
            this.parentAspNetUserPhoneNumber = parentAspNetUserPhoneNumber;
        }

        public String getAspNetUserId() {
            return aspNetUserId;
        }

        public void setAspNetUserId(String aspNetUserId) {
            this.aspNetUserId = aspNetUserId;
        }

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

        public Object getEducationTypeId() {
            return educationTypeId;
        }

        public void setEducationTypeId(Object educationTypeId) {
            this.educationTypeId = educationTypeId;
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

        public Object getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Object modifiedBy) {
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

}
