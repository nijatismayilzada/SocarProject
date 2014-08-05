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
        public string getApplicantStatus(int applicantID)
        {
            SqlDataAdapter da = new SqlDataAdapter("SELECT APPLICANT.NAME, vacancyStatus.status FROM APPLICANT LEFT JOIN vacancyStatus ON vacancyStatus.id = APPLICANT.Vac_Status_ID " +
                                                       "WHERE APPLICANT.ID = " + applicantID + " ORDER BY APPLICANT.NAME", con);
            DataTable dt = new DataTable();
            da.Fill(dt);
            string json = JsonConvert.SerializeObject(dt);
            return json;
        }

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json, UseHttpGet = true)]
        public string getLoginPassword(XmlDocument LoginNameUser, XmlDocument passwordUser)
        {
            string json = null;
            try
            {
                DataTable dt = new DataTable();

                String a = LoginNameUser.OuterXml;
                String b = passwordUser.OuterXml;

                string str = "SELECT count(id) as SuccessResult FROM Login WHERE LoginName = N'" + a + "' and Password = N'" + b + "'";
                SqlDataAdapter da = new SqlDataAdapter(str, con);



                da.Fill(dt);
                json = JsonConvert.SerializeObject(dt);
            }
            catch (Exception e)
            {
            }

            return json;
        }


    }
}