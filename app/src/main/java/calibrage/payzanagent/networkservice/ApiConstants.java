package calibrage.payzanagent.networkservice;

/**
 * Created by Calibrage11 on 9/8/2017.
 */

public interface ApiConstants {
    String REGISTER = "api/Register/Register";
    String LOGIN = "api/Register/Login";
    String STATES = "api/States/GetStates/";
    String DISTRICTS = "api/Districts/GetDistricts/";
    String MANDALS = "api/Mandals/GetMandals/";
    String VILLAGE = "api/Villages/GetVillages/";
    String WALLET = "api/UserWallet/AddMoneyToUserWallet";
    String MOBILE_SERVICES = "api/ServiceProvider/GetServicesByServiceProviderId/";
    String AGENT_REQUESTS = "api/AgentRequestInfo/GetAgentRequestInfo/";
    String BUSINESS_CAT_REQUESTS = "api/TypeCdDmts/GetTypeCdDmtsByClassType/";
    String PERSONAL_INFO_REQUESTS = "api/Agent/GetAgentsPersonalInfoByUserName/";
    String HOME_REQUEST = "api/AgentRequestInfo/GetAgentRequestCountsByUserId/";
    String BANK_REQUESTS = "api/Banks/GetAll/";
    String PROVINCE_REQUESTS = "api/Province/GetProvinces/";
    String DISTRICT_REQUESTS = "api/Province/GetDistrictsByProvinceId/";
    String MANDAL_REQUESTS = "api/Mandals/GetMandalByDistrict/";
    String VILLAGE_REQUESTS = "api/Villages/GetVillageByMandal/";
    String STATE_REQUESTS = "api/States/GetStateInfo/";
    /*String BRANCH_REQUESTS = "/api/Banks/GetBankInfo/";*/
    String BRANCH_REQUESTS = "api/Banks/GetBanksByBankTypeId/";
    String ADD_AGENT = "/api/Agent/AddAgent";
   String UPDATE_AGENT_REQUEST= "api/AgentRequestInfo/UpdateAgentRequestStatus";
   String REGISTER_AGENT_PERSONAL_INFO= "api/Agent/RegisterAgent";
   String REGISTER_AGENT_BANK_INFO= "api/AgentBank/insert";
   String UPDATE_AGENT_BANK_INFO= "api/AgentBank/UpdateAgentBank";
   String REGISTER_AGENT_ID_INFO= "api/AgentIdProof/AddAgentIdProofs";
   String GET_AGENT_BANK_INFO= "api/AgentBank/GetAgentBankByAgentId/";
   String UPDATE_PERSONAL_INFO= "api/Agent/UpdateAgentPersonalInfo";

    //  String LOGIN = "API
}
