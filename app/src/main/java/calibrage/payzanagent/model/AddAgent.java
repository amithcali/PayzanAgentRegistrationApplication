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






}
