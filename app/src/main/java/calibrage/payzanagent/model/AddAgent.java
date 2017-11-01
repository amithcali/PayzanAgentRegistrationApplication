package calibrage.payzanagent.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Calibrage11 on 10/27/2017.
 */

public class AddAgent implements Parcelable {

    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("AgentPersonalInfo")
    @Expose
    private AgentPersonalInfo agentPersonalInfo;
    @SerializedName("AgentBankInfo")
    @Expose
    private AgentBankInfo agentBankInfo;
    @SerializedName("AgentIdProofs")
    @Expose
    private List<AgentIdProof> agentIdProofs = null;
    @SerializedName("AgentDocs")
    @Expose
    private List<AgentDoc> agentDocs = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public AgentPersonalInfo getAgentPersonalInfo() {
        return agentPersonalInfo;
    }

    public void setAgentPersonalInfo(AgentPersonalInfo agentPersonalInfo) {
        this.agentPersonalInfo = agentPersonalInfo;
    }

    public AgentBankInfo getAgentBankInfo() {
        return agentBankInfo;
    }

    public void setAgentBankInfo(AgentBankInfo agentBankInfo) {
        this.agentBankInfo = agentBankInfo;
    }

    public List<AgentIdProof> getAgentIdProofs() {
        return agentIdProofs;
    }

    public void setAgentIdProofs(List<AgentIdProof> agentIdProofs) {
        this.agentIdProofs = agentIdProofs;
    }

    public List<AgentDoc> getAgentDocs() {
        return agentDocs;
    }

    public void setAgentDocs(List<AgentDoc> agentDocs) {
        this.agentDocs = agentDocs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }




    public class AgentDoc {

        @SerializedName("FileBytes")
        @Expose
        private String fileBytes;
        @SerializedName("AgentId")
        @Expose
        private String agentId;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("FileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("FileExtension")
        @Expose
        private String fileExtension;
        @SerializedName("FileTypeId")
        @Expose
        private Integer fileTypeId;
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

        public String getFileBytes() {
            return fileBytes;
        }

        public void setFileBytes(String fileBytes) {
            this.fileBytes = fileBytes;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public Integer getFileTypeId() {
            return fileTypeId;
        }

        public void setFileTypeId(Integer fileTypeId) {
            this.fileTypeId = fileTypeId;
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


}
