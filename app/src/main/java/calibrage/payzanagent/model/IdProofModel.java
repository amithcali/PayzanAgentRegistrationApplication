package calibrage.payzanagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 11/21/2017.
 */

public class IdProofModel {
    @SerializedName("IdProofTypeId")
    @Expose
    private Integer idProofTypeId;
    @SerializedName("IdProofNumber")
    @Expose
    private String idProofNumber;

    public Integer getIdProofTypeId() {
        return idProofTypeId;
    }

    public void setIdProofTypeId(Integer idProofTypeId) {
        this.idProofTypeId = idProofTypeId;
    }

    public String getIdProofNumber() {
        return idProofNumber;
    }

    public void setIdProofNumber(String idProofNumber) {
        this.idProofNumber = idProofNumber;
    }
}
