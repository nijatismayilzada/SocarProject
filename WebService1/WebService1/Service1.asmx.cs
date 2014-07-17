using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data;
using System.Data.SqlClient;
using Newtonsoft.Json;
using System.Data.Common;
using System.Web.Script.Services;

namespace WebService1
{
    /// <summary>
    /// Summary description for Service1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]

    [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {
        SqlConnection con = new SqlConnection(@"Server=(local);Database=Vacancy;Trusted_Connection=True;");

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
        public string getVacancy()
        {
            SqlDataAdapter da = new SqlDataAdapter("select id, name from Vacancy", con);
            DataTable dt = new DataTable();
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }


        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getApplicant(int vacancyID)
        {
            SqlDataAdapter da = new SqlDataAdapter("select * from APPLICANT where vacid =" + vacancyID + " ", con);
            DataTable dt = new DataTable();

            da.Fill(dt);

            string json = JsonConvert.SerializeObject(dt);
            return json;
        }


        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getApplicantStatus(int applicantID)
        {
            SqlDataAdapter da = new SqlDataAdapter("SELECT APPLICANT.NAME, vacancyStatus.status FROM APPLICANT LEFT JOIN vacancyStatus ON vacancyStatus.id=APPLICANT.Vac_Status_ID where APPLICANT.ID=" + applicantID + "ORDER BY APPLICANT.NAME;", con);
            DataTable dt = new DataTable();

            da.Fill(dt);

            string json = JsonConvert.SerializeObject(dt);
            return json;
        }
    }
}



 
