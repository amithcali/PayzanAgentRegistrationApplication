package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 10/27/2017.
 */

public class AgentIdProof {

    @SerializedName("AgentId")
    @Expose
    private String agentId;
    @SerializedName("IdProofs")
    @Expose
    private List<IdProofModel> idProofs = null;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public List<IdProofModel> getIdProofs() {
        return idProofs;
    }

    public void setIdProofs(List<IdProofModel> idProofs) {
        this.idProofs = idProofs;
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

