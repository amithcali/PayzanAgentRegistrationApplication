package calibrage.payzanagent.networkservice;

import com.google.gson.JsonObject;

import calibrage.payzanagent.BuildConfig;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AddAgentResponseModel;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.BankInfoResponseModel;
import calibrage.payzanagent.model.Branch;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.DistrictModel;
import calibrage.payzanagent.model.GetBankInfoModel;
import calibrage.payzanagent.model.GetIdproofModel;
import calibrage.payzanagent.model.GetPersonalInfoModel;
import calibrage.payzanagent.model.HomeModel;
import calibrage.payzanagent.model.IdProofDeleteModel;
import calibrage.payzanagent.model.IdProofResponseModel;
import calibrage.payzanagent.model.LoginResponseModel;
import calibrage.payzanagent.model.MandalModel;
import calibrage.payzanagent.model.PersonalInfoResponseModel;
import calibrage.payzanagent.model.ProvinceModel;
import calibrage.payzanagent.model.StatesModel;
import calibrage.payzanagent.model.StatusCountModel;
import calibrage.payzanagent.model.UpdateAgentRequestResponceModel;
import calibrage.payzanagent.model.UploadDocumentResponseModel;
import calibrage.payzanagent.model.VillageModel;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Admin on 10/23/2017.
 */

public interface MyServices {

    // @Headers("Accept: application/json")

    @POST(ApiConstants.LOGIN)
    Observable<LoginResponseModel> UserLogin(@Body JsonObject data);

    @POST(ApiConstants.ADD_AGENT)
    Observable<AddAgentResponseModel> addAgent(@Body JsonObject data);

    @GET
    Observable<AgentRequestModel> getRequest(@Url String url);

    @GET
    Observable<BusinessCategoryModel> getBusinessRequest(@Url String url);

    @GET
    Observable<GetPersonalInfoModel> getUpdatePersonalInfoRequest(@Url String url);

    @GET
    Observable<HomeModel> getHomeRequest(@Url String url);

    @GET
    Observable<ProvinceModel> getProvinceRequest(@Url String url);

    @GET
    Observable<MandalModel> getMandalRequest(@Url String url);


    @GET
    Observable<VillageModel> getVillageRequest(@Url String url);

    @GET
    Observable<DistrictModel> getDistrictRequest(@Url String url);

    @GET
    Observable<StatesModel> getStates(@Url String url);

    @GET
    Observable<Branch> getBranchRequest(@Url String url);

    @POST(ApiConstants.UPDATE_AGENT_REQUEST)
    Observable<UpdateAgentRequestResponceModel> AgentUpdateRequest(@Body JsonObject data);

    @POST(ApiConstants.REGISTER_AGENT_PERSONAL_INFO)
    Observable<PersonalInfoResponseModel> postPersonalInfo(@Body JsonObject data);

    @POST(BuildConfig.LOCAL_URL+ApiConstants.UPDATE_PERSONAL_INFO)
    Observable<PersonalInfoResponseModel> updatePersonalInfo(@Body JsonObject data);

    @POST(ApiConstants.REGISTER_AGENT_BANK_INFO)
    Observable<BankInfoResponseModel> postBankInfo(@Body JsonObject data);

    @DELETE
    Observable<IdProofDeleteModel> deleteidInfo(@Url String data);

    @POST(ApiConstants.UPDATE_AGENT_BANK_INFO)
    Observable<BankInfoResponseModel> updateBankInfo(@Body JsonObject data);

    @POST(ApiConstants.REGISTER_AGENT_ID_INFO)
    Observable<IdProofResponseModel> postIdInfo(@Body JsonObject data);

    @POST(ApiConstants.UPDATE_AGENT_ID_INFO)
    Observable<IdProofResponseModel> updateIdInfo(@Body JsonObject data);

    @POST(ApiConstants.UPDATE_PERSONAL_INFO)
    Observable<AddAgentResponseModel> update(@Body JsonObject data);
    @POST(ApiConstants.UPLOAD_DOCUMENTS)
    Observable<UploadDocumentResponseModel> uploadDocument(@Body JsonObject data);


    @GET
    Observable<StatusCountModel> UpdateStatusCount(@Url String url);

    @GET
    Observable<GetBankInfoModel> GetAgentBankInfo(@Url String url);
    @GET
    Observable<GetIdproofModel> GetAgentIdproofInfo(@Url String url);

}
