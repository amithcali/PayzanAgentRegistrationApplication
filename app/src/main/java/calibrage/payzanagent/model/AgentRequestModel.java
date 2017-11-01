package calibrage.payzanagent.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 10/26/2017.
 */

public class AgentRequestModel implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public class ListResult {
        @SerializedName("AgentRequestCategoryName")
        @Expose
        private String agentRequestCategoryName;
        @SerializedName("TitleType")
        @Expose
        private String titleType;
        @SerializedName("CountryName")
        @Expose
        private Object countryName;
        @SerializedName("ProvinceName")
        @Expose
        private String provinceName;
        @SerializedName("DistrictName")
        @Expose
        private String districtName;
        @SerializedName("MandalName")
        @Expose
        private String mandalName;
        @SerializedName("VillageName")
        @Expose
        private String villageName;
        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("AgentRequestCategoryId")
        @Expose
        private Integer agentRequestCategoryId;
        @SerializedName("TitleTypeId")
        @Expose
        private Integer titleTypeId;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("MiddleName")
        @Expose
        private Object middleName;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("MobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("AddressLine1")
        @Expose
        private String addressLine1;
        @SerializedName("AddressLine2")
        @Expose
        private String addressLine2;
        @SerializedName("Landmark")
        @Expose
        private String landmark;
        @SerializedName("VillageId")
        @Expose
        private Integer villageId;
        @SerializedName("Comments")
        @Expose
        private String comments;
        @SerializedName("Created")
        @Expose
        private String created;

        public String getAgentRequestCategoryName() {
            return agentRequestCategoryName;
        }

        public void setAgentRequestCategoryName(String agentRequestCategoryName) {
            this.agentRequestCategoryName = agentRequestCategoryName;
        }

        public String getTitleType() {
            return titleType;
        }

        public void setTitleType(String titleType) {
            this.titleType = titleType;
        }

        public Object getCountryName() {
            return countryName;
        }

        public void setCountryName(Object countryName) {
            this.countryName = countryName;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getMandalName() {
            return mandalName;
        }

        public void setMandalName(String mandalName) {
            this.mandalName = mandalName;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getAgentRequestCategoryId() {
            return agentRequestCategoryId;
        }

        public void setAgentRequestCategoryId(Integer agentRequestCategoryId) {
            this.agentRequestCategoryId = agentRequestCategoryId;
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

        public Object getMiddleName() {
            return middleName;
        }

        public void setMiddleName(Object middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
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

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

    }
}
