package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Calibrage11 on 11/22/2017.
 */

public class UploadDocumentResponseModel {
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
    private List<ValidationError> validationErrors = null;
    @SerializedName("Exception")
    @Expose
    private Exception exception;

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

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public class ListResult {

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




    public class ValidationError {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Description")
        @Expose
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

}