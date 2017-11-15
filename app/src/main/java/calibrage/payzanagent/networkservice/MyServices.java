package calibrage.payzanagent.networkservice;

import com.google.gson.JsonObject;

import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AddAgentResponseModel;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.Branch;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.DistrictModel;
import calibrage.payzanagent.model.HomeModel;
import calibrage.payzanagent.model.LoginResponseModel;
import calibrage.payzanagent.model.MandalModel;
import calibrage.payzanagent.model.ProvinceModel;
import calibrage.payzanagent.model.StatesModel;
import calibrage.payzanagent.model.UpdateAgentRequestResponceModel;
import calibrage.payzanagent.model.VillageModel;
import retrofit2.http.Body;
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

}
