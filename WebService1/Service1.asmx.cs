using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Script.Services;
using System.Web.Script.Serialization;
using System.Data.SqlClient;
using System.Data;
using Newtonsoft.Json;
using System.Xml;

namespace WebService1
{
    [WebService(Namespace = "Vac/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {
        public static SqlConnection con = new SqlConnection(@"Data Source=10.23.14.94;Initial Catalog=vacancy;Persist Security Info=True;User ID=vacancy2014;Password=1234");

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getVacancyList()
        {
            SqlDataAdapter da = new SqlDataAdapter("SELECT Vacancy.ID, Vacancy.NAME, Vacancy.COUNT, VACANCY.COUNT_INF, COMPANY.NAME, DEPARTMENT.NAME, vacancyStatus.status " + 
                                                       "FROM Vacancy LEFT JOIN COMPANY ON VACANCY.COMPANYID = COMPANY.ID LEFT JOIN DEPARTMENT on Vacancy.DEPARTMENTID = DEPARTMENT.ID " +
                                                       "LEFT JOIN vacancyStatus on Vacancy.vacancyStatusId = vacancyStatus.id", con);
            DataTable dt = new DataTable();
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }


        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getApplicants(int vacancyID)
        {
            SqlDataAdapter da = new SqlDataAdapter("select * from APPLICANT where VACID = " + vacancyID + " ", con);
            DataTable dt = new DataTable();
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }


        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getApplicantStatus(int applicantID, int vacID)
        {
            SqlDataAdapter da = new SqlDataAdapter("select resultType.result ,appSel.APPID, appSel.selectionState_id from "+
            " applicantSelection appSel LEFT JOIN (select id, result from resultType where type in (0,1)) resultType " +
            " ON resultType.id = appSel.selectionState_id  where appSel.VACID = " + vacID + " AND appSel.APPID = " + applicantID, con);


            
            DataTable dt = new DataTable();
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getLoginPassword(String LoginNameUser, String passwordUser)
        {
            string str = "SELECT count(id) as SuccessResult FROM Login WHERE LoginName = N'" + LoginNameUser + "' and Password = N'" + passwordUser + "'";
            
            DataTable dt = new DataTable();
            SqlDataAdapter da = new SqlDataAdapter(str, con);
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getAllFailReasons()
        {
            string str = "select * from failreason";

            DataTable dt = new DataTable();
            SqlDataAdapter da = new SqlDataAdapter(str, con);
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getApplicantFailReasons(int vacID, int applicantID)
        {
            string str = "select * from failreason LEFT JOIN (select * from applicantFailReason where vacid = " + vacID +
                         ") appFailReason ON appFailReason.failreason_id = failreason.id " +
                         " where appFailReason.appid = " + applicantID;

            DataTable dt = new DataTable();
            SqlDataAdapter da = new SqlDataAdapter(str, con);
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }
    }
}