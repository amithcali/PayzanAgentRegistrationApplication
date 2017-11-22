package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 11/15/2017.
 */

public class HomeModel {

    @SerializedName("Result")
    @Expose
    private Result result;
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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
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
    public class Result {

        @SerializedName("StatusCounts")
        @Expose
        private List<StatusCount> statusCounts = null;
        @SerializedName("Agents")
        @Expose
        private Integer agents;
        @SerializedName("Consumers")
        @Expose
        private Integer consumers;

        public List<StatusCount> getStatusCounts() {
            return statusCounts;
        }

        public void setStatusCounts(List<StatusCount> statusCounts) {
            this.statusCounts = statusCounts;
        }

        public Integer getAgents() {
            return agents;
        }

        public void setAgents(Integer agents) {
            this.agents = agents;
        }

        public Integer getConsumers() {
            return consumers;
        }

        public void setConsumers(Integer consumers) {
            this.consumers = consumers;
        }


        public class StatusCount {

            @SerializedName("StatusTypeId")
            @Expose
            private Integer statusTypeId;
            @SerializedName("StatusType")
            @Expose
            private String statusType;
            @SerializedName("Count")
            @Expose
            private Integer count;

            public Integer getStatusTypeId() {
                return statusTypeId;
            }

            public void setStatusTypeId(Integer statusTypeId) {
                this.statusTypeId = statusTypeId;
            }

            public String getStatusType() {
                return statusType;
            }

            public void setStatusType(String statusType) {
                this.statusType = statusType;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

        }
}


}

