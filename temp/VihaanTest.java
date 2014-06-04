package com.inmobi.iat.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TException;
import org.codehaus.plexus.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.inmobi.iat.client.IATServiceClient;
import com.inmobi.iat.platform.TApplicationResponse;
import com.inmobi.iat.platform.TApplicationType;
import com.inmobi.iat.platform.TDeviceMacro;
import com.inmobi.iat.platform.TGoal;
import com.inmobi.iat.platform.TNetwork;
import com.inmobi.iat.platform.TNetworkOrderBy;
import com.inmobi.iat.platform.TNetworkTag;
import com.inmobi.iat.platform.TRequestToken;
import com.inmobi.iat.platform.TResult;
import com.inmobi.iat.platform.TTrackingKey;
import com.inmobi.iat.platform.TTrackingUrl;
import com.inmobi.iat.platform.TTrackingUrlResponse;
import com.inmobi.iat.util.GoalTemplateUtility;
import com.inmobi.iat.util.UrlExtractorUtility;
import com.inmobi.iat.util.exception.IatUtilException;
import com.inmobi.iat.utils.ConfigUtils;
import com.inmobi.iat.utils.DatabaseParserUtils;
import com.inmobi.iat.vihaan.client.IATVihaanClient;
import com.inmobi.lib.MessageType.MessageTypes;
import com.inmobi.lib.QALibrary;
import com.inmobi.platform.thrift.exception.ServiceException;


/**
 * @author graval
 * 
 */
public class VihaanTest extends org.databene.feed4testng.FeedTest

{
    String           WS_HOST        = ConfigUtils.getValue("WS_HOST");
    int              WS_PORT        = Integer.parseInt(ConfigUtils.getValue("WS_PORT"));
    // IATServiceClient client = new IATServiceClient("", WS_PORT);
    IATVihaanClient  vClient        = new IATVihaanClient(WS_HOST, WS_PORT);
    IATServiceClient iClient        = new IATServiceClient(WS_HOST, WS_PORT, "localhost");
    TRequestToken    dToken         = QALibrary.getReqTodken();
    String           accountID      = this.getaccountID(ConfigUtils.getValue("WS_Valid_User"));
    String           accountID_Aman = "bacd9d6281424defa449b01e880999ee";
    String           testLandingURL = "https://both.InDia.com/test?view.Aspx.India0101";
    // WebAppGuid
    String           webAppGuid     = ConfigUtils.getValue("webAppGuid");
    // Andorid AppGuid
    String           andAppGuid     = ConfigUtils.getValue("andAppGuid");
    // IOS AppGuid
    String           iosAppGuid     = ConfigUtils.getValue("iosAppGuid");
    TRequestToken    reqToekn       = new TRequestToken();

    /**
     * @param value
     * @return
     */

    private void test(final int a, final String two)
    {
        System.out.println("test1 st2");
    }

    private void test(final String two, final int a)
    {
        System.out.println("two a");
    }

    private String getaccountID(final String value)
    {
        try
        {
            String account_id = "";
            account_id = iClient.getUserbyEmailId(dToken).getUser().account.accountId;
            System.out.println("Account ID:: " + account_id);

            return account_id;

        }
        catch (Exception e)
        {

            QALibrary.logResult(MessageTypes.LOG_ERROR, e.getMessage());

        }

        return null;
    }

    /*
     * Validate getGoalByAccount. This returns all goals which are having acocunt id = 1 OR accont id = provided
     * acocunt_id
     */
    // @Test(dataProvider = "feeder")
    // public void getGoalByAccountTest(final String accountID) // Dv1
    @Test
    public void getGoalByAccountTest() // Dv1

    {

        QALibrary.logResult(MessageTypes.TEST_CASE_START, "getGoalByAccount()");
        int dbValues = 0;

        // TRequestToken reqToekn = new TRequestToken();
        try
        {

            List<TGoal> goals = vClient.getGoalByAccount(dToken, accountID);
            int goalSize = goals.size();
            int count = 0;
            // Assert.assertTrue(goalSize > 0, "No goals or goals ar NULL");
            // for (Iterator<TGoal> iter = goals.iterator(); iter.hasNext();)
            QALibrary.logResult(MessageTypes.LOG_INFO, "Goals::" + goals.toString());

            if (goalSize > 0)
            {
                for (TGoal goal : goals)
                {
                    QALibrary.logResult(MessageTypes.LOG_INFO, "Goal:: " + goal.goalName + " GUID:: " + goal.goalGuId
                            + "\n");
                    count++;
                }
            }
            QALibrary.logResult(MessageTypes.LOG_INFO, "Total Goals: " + goalSize + " for account " + accountID);
            ResultSet dbset = DatabaseParserUtils
                    .getDBDataFromQuery("select count(*) from goals  where account_id IN (1,(select id from account  where account_guid = '"
                            + accountID + "') );");

            if (dbset.next())
                dbValues = dbset.getInt("count");
            else
                dbValues = 0;

            Assert.assertEquals(true, dbValues == goalSize, "DB values doens't match with WS value");
            String sMessage = "Total DB Goals: " + dbValues;
            QALibrary.logResult(MessageTypes.LOG_INFO, sMessage);
        }
        catch (Exception e)
        {
            Assert.fail("Exception during getGoalByAccount():: " + e);
        }
        QALibrary.logResult(MessageTypes.TEST_CASE_END, "getGoalByAccount()");
    }

    // @Test(dataProvider = "feeder")
    @Test
    // public void getGoalByAppTest(final String andAppGuid)
    public void getGoalByAppTest()// validated
    {
        // QALibrary.logResult(MessageTypes.TEST_CASE_START, "getGoalByAppTest()");
        int dbValues = 0;
        try
        {
            List<TGoal> goals = new ArrayList<TGoal>();
            goals = vClient.getGoalByApp(dToken, accountID, webAppGuid);
            System.out.println(goals);
            // goals.addAll(vClient.getGoalByApp(dToken, accountID, andAppGuid));
            int goalSize = goals.size();
            int count = 0;
            // Assert.assertTrue(goalSize > 0, "No Goals or golas as null");
            // for (Iterator<TGoal> iter = goals.iterator(); iter.hasNext();)
            QALibrary.logResult(MessageTypes.LOG_INFO, "Goals::" + goals.toString());

            if (goalSize > 0)
            {
                for (TGoal goal : goals)
                {
                    QALibrary.logResult(MessageTypes.LOG_INFO, "Goal:: " + goal.goalName + " GUID:: " + goal.goalGuId
                            + "\n");
                    count++;
                }
            }
            QALibrary.logResult(MessageTypes.LOG_INFO, "Total Goals: " + goalSize + " for app " + webAppGuid);
            ResultSet dbset = DatabaseParserUtils
                    .getDBDataFromQuery("select count(*) from app_goal_assoc  where app_id IN (select id from app_site_master where app_guid ='"
                            + webAppGuid + "');");

            if (dbset.next())
                dbValues = dbset.getInt("count");
            else
                dbValues = 0;
            String sMessage = "Total DB Goals: " + dbValues;
            Assert.assertEquals(true, dbValues == goalSize, "DB values doens't match with WS value DB:" + dbValues
                    + " Service: " + goalSize);

            QALibrary.logResult(MessageTypes.LOG_INFO, sMessage);
        }
        catch (ServiceException s)
        {
            System.out.println("Service Exception" + s);
        }
        catch (Exception e)
        {
            Assert.fail("Exception during getGoalByApp():: " + e);
        }
        // QALibrary.logResult(MessageTypes.TEST_CASE_END, "getGoalByAppTest()");

    }

    // @Test(dataProvider = "feeder")
    @Test
    // public void addGoalToAppTest(final String new_Goal1, final String new_Goal2)
    public void addGoalToAppTest()

    // This will replace goals
    {
        long timeStamp = System.currentTimeMillis();
        String new_Goal1 = "Auto_goal1_" + timeStamp;
        String new_Goal2 = "Auto_Goal2_" + timeStamp;
        // String webAppGuid = "e39c0bd5-79dc-48e4-ba4a-6b5010f634c0";
        // System.out.println("Goals are::: " + new_Goal1 + "||And:: " + new_Goal2);
        TGoal tGoal = new TGoal();

        tGoal.setGoalName(new_Goal1);
        tGoal.setGoalGuIdIsSet(false);
        TGoal tGoal2 = new TGoal();
        tGoal2.setGoalGuIdIsSet(false);
        tGoal2.setGoalName(new_Goal2);

        try
        {
            List<TGoal> retrivedGoals = vClient.getGoalByApp(dToken, accountID, webAppGuid);
            System.out.println("ORG GOAL LIST::" + retrivedGoals);
            List<TGoal> orgGoalToList = Arrays.asList(new TGoal[]
            { tGoal, tGoal2 });
            // orgGoalToList.aretrivedGoals;
            retrivedGoals.addAll(orgGoalToList);
            List<TGoal> goals = vClient.addGoalToApp(dToken, webAppGuid, accountID, retrivedGoals);
            Assert.assertTrue(goals.size() > 0);
            // System.out.println("AddedGoal:" + tGoal.goalName);

            for (TGoal goal : goals)
            {
                if (goal.goalName.equalsIgnoreCase(new_Goal1))
                {
                    System.out.println("*******updated******");
                    QALibrary.logResult(MessageTypes.LOG_INFO, " Goal:: " + goal.goalName + " GUID:: " + goal.goalGuId
                            + "\n");
                    System.out.println("============");

                }
                else
                {
                    QALibrary.logResult(MessageTypes.LOG_INFO, " Goal:: " + goal.goalName + " GUID:: " + goal.goalGuId
                            + "\n");
                }

            }

        }
        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "For::Goals are::: " + new_Goal1 + "||And:: " + new_Goal2);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            Assert.fail("exception thrown" + e);
        }

    }

    @Test
    // @Test(dataProvider = "feeder")
    // public void getTrackingUrlByAppTest(final String andAppGuid)
    public void getTrackingUrlByAppTest()
    {

        int dbValues = 0;
        int serviceValues = 0;
        try
        {
            List<TTrackingUrl> trUrlList = vClient.getTrackingUrlByApp(dToken, accountID, webAppGuid);
            // TTrackingUrlResponse iCtrUrlList = iClient.getTrackingUrlsForApp(dToken, accountID, andAppGuid);
            // System.out.println(iCtrUrlList.trakcingUrls.toString());
            // trUrlList = iCtrUrlList.trakcingUrls;
            // Assert.assertTrue(trUrlList.size() > 0, "Tracking url is none or NULL");
            System.out.println("TrURL:: " + trUrlList);

            for (TTrackingUrl tUrl : trUrlList)
            {
                QALibrary.logResult(MessageTypes.LOG_INFO, "Network Name:: " + tUrl.networkName);// + " GUID:: "
                // + tUrl.trackingSet.toString() + "\n");
                for (TTrackingKey tset : tUrl.trackingSet)
                {
                    System.out.println("Key: " + tset.key + " || Landing::" + tset.landingUrl);
                    System.out.println("TrackingURL:: " + tset.trackingUrl);
                    serviceValues++;
                }

                System.out.println("==========");
            }
            ResultSet dbset = DatabaseParserUtils
                    .getDBDataFromQuery("select count(*) from tracking_urls  where app_id IN (select id from app_site_master where app_guid ='"
                            + webAppGuid + "') and is_active = 't';");

            if (dbset.next())
                dbValues = dbset.getInt("count");
            else
                dbValues = 0;
            String sMessage = "Total DB Goals: " + dbValues;
            Assert.assertEquals(true, dbValues == serviceValues, "DB values doens't match with WS value DB:" + dbValues
                    + " Service: " + serviceValues);

            // System.out.println("TrackingURL::"trUrlList.toString());

        }

        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: " + andAppGuid);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            Assert.fail("exception thrown. Message:: " + e);
        }

    }

    @Test
    // @Test(dataProvider = "feeder")
    public void updateLandingUrlByAppTagTest()
    // public void updateLandingUrlByAppTagTest(final String testLandingURL) // BUG: No error code/ message incase
    // landing
    // url is blank / null. No validation on
    // gpmId. Update tracking URLs even if the
    // gpmId is different for app.
    {
        String urlHash = "";
        TTrackingKey trackingKey = new TTrackingKey();
        // trackingKey.setKey(key);

        try
        {
            TTrackingUrlResponse urlHashesResp = iClient.getTrackingUrlsForApp(dToken, accountID, andAppGuid);
            List<TTrackingUrl> listURLs = urlHashesResp.trakcingUrls;
            boolean isEmpty = listURLs.iterator().hasNext();
            if (isEmpty)
            {
                urlHash = listURLs.iterator().next().getTrackingSet().get(0).key;
            }

            System.out.println("Updating LP for URL key : " + urlHash);
            // urlHash = urlHash.toUpperCase();

            trackingKey.setKey(urlHash);

            trackingKey.setLandingUrl(testLandingURL);

            vClient.updateLandingUrlByAppTag(dToken, accountID, "xyz", trackingKey);
            // iClient.updateLandingUrl(dToken, accountID, urlHash, "http://TesTurl.teSt.inm0bI.com");
            List<TTrackingUrl> trUrlList = vClient.getTrackingUrlByApp(dToken, accountID, andAppGuid);
            for (TTrackingUrl tUrl : trUrlList)
            {
                for (TTrackingKey tset : tUrl.trackingSet)
                {
                    System.out.println("Key: " + tset.key + "|| Landing::" + tset.landingUrl);
                    if (tset.key.equalsIgnoreCase(urlHash))
                    {
                        System.out.println("--------------- Changed ------------------");
                        System.out.println("LP:: " + tset.landingUrl + "|| Key ::" + tset.key);
                        System.out.println("--------------- Changed ------------------");
                    }
                }
                QALibrary.logResult(MessageTypes.LOG_INFO, "Network Name:: " + tUrl.networkName);// + " GUID:: "
                // + tUrl.trackingSet.toString() + "\n");
                System.out.println("==========");
            }

        }

        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: " + testLandingURL);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            Assert.fail("exception thrown" + e);
        }

    }

    @Test
    public void getLPByAppUrlHashTest()
    // @Test(dataProvider = "feeder")
    // public void getLPByAppUrlHashTest(final String urlHash)
    {
        String urlHash = "";
        try
        {
            TTrackingUrlResponse urlHashesResp = iClient.getTrackingUrlsForApp(dToken, accountID, andAppGuid);
            List<TTrackingUrl> listURLs = urlHashesResp.trakcingUrls;
            System.out.println("urlHashedResp==" + urlHashesResp.toString());
            boolean hasEntry = (listURLs.size() > 0);

            if (hasEntry)
            {
                urlHash = listURLs.iterator().next().getTrackingSet().get(0).key;
            }
            System.out.println("Tracking URL : " + urlHash);

            String landingUrl = vClient.getLPByAppUrlHash(dToken, accountID, andAppGuid, urlHash);

            Assert.assertTrue(!StringUtils.isBlank(landingUrl), "landing url is null or blank");

            QALibrary.logResult(MessageTypes.LOG_INFO, "Landing URL: " + landingUrl);
            ResultSet rs = DatabaseParserUtils.getDBData("tracking_urls", "landingpage_url",
                "335ba8d1-12d5-4237-8278-a83dec933f2e");
            while (rs.next())
            {
                // System.out.println("DB Value::"
                // + rs.getString("landingpage_url"));
            }

        }

        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: " + urlHash);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }

        catch (Exception e)
        {
            Assert.fail("exception thrown:: Message:: " + e);
        }

    }

    @Test
    public void getNetworkByAccountTest()
    // @Test(dataProvider = "feeder")
    // public void getNetworkByAccountTest(final String accountID)
    {
        int dbValues = 0;
        try
        {
            List<TNetwork> networks = vClient.getNetworksByAccount(dToken, accountID, TNetworkOrderBy.NAME);
            Assert.assertTrue(networks.size() > 0);
            for (TNetwork network : networks)
            {
                System.out.println("Network: " + network.name + "| Guid:" + network.networkGuid);
            }
            System.out.println("Size:: " + networks.size() + " Account ID: " + accountID);

            ResultSet dbset = DatabaseParserUtils
                    .getDBDataFromQuery("select count(*) from network_master where account_id IN ((select id from account where account_guid='"
                            + accountID + "'), 1) and is_active='true';");

            if (dbset.next())
                dbValues = dbset.getInt("count");
            else
                dbValues = 0;
            String sMessage = "Total DB Goals: " + dbValues;

        }

        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: " + accountID);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)

        {

            Assert.fail("exception thrown" + "||ACID::" + accountID + " ||Exception::" + e);

        }

    }

    @Test
    public void getNetworksByAppTest()
    // @Test(dataProvider = "feeder")
    // public void getNetworksByAppTest(final String andAppGuid)
    {
        try
        {
            List<TNetwork> networks = vClient.getNetworksByApp(dToken, accountID, andAppGuid);

            Assert.assertTrue(networks.size() > 0);
            for (TNetwork network : networks)
            {

                System.out.println("Network: " + network.name + "| Guid:" + network.networkGuid + " | TAG size::"
                        + network.getTagsSize() + " | TAGS: " + network.getTags());
            }

            System.out.println("Size::" + networks.size());
            System.out.println("data:: " + networks.toString());
            System.out.println("Account: " + accountID);
            System.out.println("App: " + andAppGuid);

        }

        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: " + andAppGuid + " || " + accountID);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            Assert.fail("exception thrown" + e);
        }

    }

    // @Test(dataProvider = "feeder")
    // public void createNetworkTagToAppTest(final String randomName)
    @Test
    public void createNetworkTagToAppTest()
    {
        TNetworkTag netTag = new TNetworkTag();
        TNetwork network = new TNetwork();
        network.setName("InMobi"); // BUG: Empty name throws persistance error. if GUID is set it replace admin
        // network name. Gives trackingUrl as IA-CR-A044
        network.networkGuid = "c3ec59f2-1e9c-11e2-8034-0664c0f1fe9d";
        network.callbackUrl = "https://xy.inmobi.com/call?test1=$IDA";
        network.imageUrl = "http://nourl.inmobi.com";
        netTag.setNetwork(network);

        // TNetworkTag netTag1 = new TNetworkTag();
        // TNetwork network1 = new TNetwork();
        // network1.setName("InMobi1234");
        // netTag1.setNetwork(network1);

        Map<String, String> tagUrlMap = new HashMap<String, String>();
        tagUrlMap.put("tag_automationv16", "http://test-landing-urlv5.com");
        // tagUrlMap.put("tag_automationv3", "http://test-landing-urlv4.com");
        netTag.setTagUrlMap(tagUrlMap);

        // netTag1.setTagUrlMap(tagUrlMap);

        List<TNetworkTag> networkTagList = new ArrayList<TNetworkTag>();
        networkTagList.add(netTag);
        // networkTagList.add(netTag1);

        try
        {
            List<TTrackingUrl> tUrls = vClient.createNetworkTagByApp(dToken, accountID, webAppGuid, networkTagList);

            for (TTrackingUrl tUrl : tUrls)
            {

                // System.out.println("~~~~~~~TURL:::: " + tUrl);
                List<TTrackingKey> trKeys = tUrl.getTrackingSet();
                for (TTrackingKey trKey : trKeys)
                {
                    if (trKey.tagName.equalsIgnoreCase("tag_automationv16"))
                    {
                        System.out.println("$$$$$$$$$");
                    }
                    System.out.println("Key:: " + trKey.key);
                    System.out.println("LP:: " + trKey.landingUrl);
                    System.out.println("Tag:: " + trKey.tagName);
                    System.out.println("TrackingUrl:: " + trKey.trackingUrl);
                }

                System.out.println("Network Name: " + tUrl.networkName);
                System.out.println("Network ImgUrl: " + tUrl.imageUrl);
                System.out.println("Network Guid: " + tUrl.networkGuid);
                System.out.println("Network id: " + tUrl.networkId);
            }
            List<TTrackingUrl> myTurls = vClient.getTrackingUrlByApp(dToken, accountID, andAppGuid);
            for (TTrackingUrl myTurl : myTurls)
            {
                System.out.println("=======" + myTurl);

            }

            // Assert.assertTrue(tUrls.size() > 0);
        }

        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: ");
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            Assert.fail("exception thrown" + e);
        }

    }

    @Test
    public void addTagByNetAppTest()
    // @Test(dataProvider = "feeder")
    // public void addNetworkTagToAppTest(final String LP, final String tag)
    {
        String tag, LP;
        tag = "gaurav_lp_eclipse_1234";
        LP = "https://test1234.inmobi.com";
        TNetworkTag netTag = new TNetworkTag();
        TNetwork network = new TNetwork();
        network.setName("InMobi");
        // network.networkGuid = "c3ec59f2-1e9c-11e2-8034-0664c0f1fe9d";
        netTag.setNetwork(network);

        Map<String, String> tagUrlMap = new HashMap<String, String>();
        tagUrlMap.put(tag, LP);
        // tagUrlMap.put("tag_AutomationV3", "http://test-landing-urlv3.com");
        netTag.setTagUrlMap(tagUrlMap);

        List<TNetworkTag> networkTagList = new ArrayList<TNetworkTag>();
        networkTagList.add(netTag);

        try
        {
            List<TTrackingUrl> tUrls = vClient.addTagByNetApp(dToken, accountID, iosAppGuid, netTag);// (dToken,
            // accountID,
            // andAppGuid,
            // networkTagList);
            System.out.println("TRURL SIZE " + tUrls.size());
            if (tUrls.size() > 0)
            {

                for (TTrackingUrl tUrl : tUrls)
                {
                    System.out.println("Network Name: " + tUrl.networkName);
                    // System.out.println("Network ImgUrl: " + tUrl.imageUrl);
                    // System.out.println("Network Guid: " + tUrl.networkGuid);
                    // System.out.println("Network id: " + tUrl.networkId);
                    System.out.println("------KEY DATA------INPUT " + tag + " || " + LP);
                    List<TTrackingKey> trUrls = tUrl.getTrackingSet();
                    for (TTrackingKey trUrl : trUrls)
                    {
                        if (trUrl.tagName.equalsIgnoreCase(tag))
                        {
                            System.out.println("==== Changed ====");
                            System.out.println("KY:: " + trUrl.key);
                            System.out.println("LP::" + trUrl.landingUrl);
                            System.out.println("TG::" + trUrl.tagName);
                            System.out.println("TR::" + trUrl.trackingUrl);
                            System.out.println("==== Changed ====");

                        }
                        System.out.println("KY:: " + trUrl.key);
                        System.out.println("LP::" + trUrl.landingUrl);
                        System.out.println("TG::" + trUrl.tagName);
                        System.out.println("TR::" + trUrl.trackingUrl);
                    }
                    System.out.println("-----KEY DATA ENDS-------");

                }
            }
            else
            {
                System.out.println("Respone is either null or zero");
            }

            // Assert.assertTrue(tUrls.size() > 0);
        }
        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: LP>" + LP + " || tag>" + tag);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            System.out.println("exception thrown:: " + e);
        }

    }

    @Test
    public void getTagByAppTest()
    // @Test(dataProvider = "feeder")
    // public void getTagByAppTest(final String iosAppGuid)
    {
        // String webAppGuid = "0e07b1f3-2471-4cd0-9c6f-1d22ba3592fb";
        try
        {
            List<String> tags = vClient.getTagByApp(dToken, accountID, andAppGuid); // ReqeustToken
            // null
            // creates
            // 500
            // exception on server
            for (String tag : tags)
            {
                System.out.println("Tag:: " + tag);
            }
        }
        catch (ServiceException s)
        {

            System.out.println("Service Exception" + s + "Inputs:: IOSAPPID:");
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            System.out.println("exception thrown druing request " + e);
        }

    }

    @Test
    public void provisionAppTest()
    // @Test(dataProvider = "feeder")
    // public void provisionAppTest(final String appId)
    {
        boolean isException = false;
        try
        {
            // accountID = "4028cb8b2dbd0408012e23adaa32096f";
            // dToken.userEmail = "giordano.buttazzo@digitouch.it";
            // System.out.println("Going to send request");
            TResult response = vClient.provisionApp(dToken, accountID, 1, "8ac10a046d87404aabca6e5964ae2456");
            System.out.println("Result::: " + response + " | To String:: " + response.toString());
            // Assert.fail("exception expected as app exists but no exception");
        }
        catch (ServiceException s)
        {
            isException = true;
            System.out.println("Service Exception:: " + s);
            if (s.toString().contains("IA-CR-A032"))
            {
                System.exit(0);
            }
        }
        catch (TException e)
        {

            QALibrary.logResult(MessageTypes.LOG_ERROR, "Main Exception" + e);
            // Assert.fail("Exception: " + e);
            // System.out.println("Main Exception.. exiting..");
            // System.exit(0);

        }

        // Assert.assertTrue(isException);c

    }

    // @Test(dataProvider = "feeder")
    // public void getDeviceMacroByAppTest(final String andAppGuid)
    @Test
    public void getDeviceMacroByAppTest()
    {
        try
        {
            Set<TDeviceMacro> macroSet = vClient.getDeviceMacroByApp(dToken, accountID, andAppGuid);
            if (macroSet.size() > 0)
            {

                for (TDeviceMacro macro1 : macroSet)
                {
                    System.out.println("Macro ID:: " + macro1.id);
                    System.out.println("Macro Name::" + macro1.name);
                }
            }
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            Assert.fail("exception thrown " + e);
        }

    }

    @Test
    public void updateMacroForAppTest()
    {

        TDeviceMacro macro = new TDeviceMacro();
        macro.setId(2);
        macro.setName("abcd");

        Set<TDeviceMacro> macros = new HashSet<TDeviceMacro>();
        macros.add(macro);
        // macro.setId(4);
        // macro.setName("abcd4");
        // macros.add(macro);

        try
        {
            Set<TDeviceMacro> macroSet = vClient.updateMacroForApp(dToken, accountID, andAppGuid, macros);
            for (TDeviceMacro macro1 : macroSet)
            {
                System.out.println(macro1.id + "<<ID");
                System.out.println(macro1.name + "<<Name");

                System.out.println(macro1.toString());
            }

        }
        catch (Exception e)
        {
            Assert.fail("exception thrown:: " + e);
        }

    }

    @Test
    public void getAllSupportedDeviceMacroTest()
    {
        try
        {
            List<TDeviceMacro> appDeviceMap = vClient.getAllSupportedDeviceMacro(dToken, TApplicationType.ANDROID);
            System.out.println("Client Config:: IP " + vClient.getServerIP() + " || PORT: " + vClient.getServerPort());

            for (TDeviceMacro t2 : appDeviceMap)
            {
                // System.out.println("Type::" + temp1.id);
                // for (TDeviceMacro t2 : temp1.getValue())
                {
                    System.out.println("Macros::: " + t2.id + " || " + t2.name);
                }
            }

        }
        catch (Exception e)
        {
            Assert.fail("exception thrown" + e);
        }

    }

    // private TRequestToken getReqToken(final String userEmail, final TProductType prodType)
    // {
    // TRequestToken reqToken = new TRequestToken();
    //
    // reqToken.setTraceId("b236c555-e38a-4c29-85bf-85ba1ea99b58");
    // reqToken.setSourceProduct(prodType);
    // reqToken.setUserEmail(userEmail);
    // reqToken.setAdmin(false);
    //
    // return reqToken;
    //
    // }

    /*
     * Vihaan utility Test
     */
    @Test
    public void vihaanUtilsGoaltest()
    {

        String goalCode = "";
        try
        {
            goalCode = GoalTemplateUtility.getGoalJsCode(webAppGuid, "download");
            System.out.println("GoalCode:" + goalCode);
            // Assert.assertTrue(goalCode.contains("e897df84-eb2b-41a2-aad3-af7b525cd222")
            // && goalCode.contains("download"));
        }
        catch (IatUtilException e)
        {
            System.out.println("Exception:: " + e);
        }

    }

    @Test
    public void vihaanUtilstest()
    {
        List<String> myList = new ArrayList<String>();
        myList.addAll(Arrays.asList("Gaurav", "gaurav Abhinav", "Aman", "Guru"));

        try
        {

            String goalResp = GoalTemplateUtility.getAllImgSrcCodes(webAppGuid, myList);
            System.out.println("GoalResp:: " + goalResp);
            System.out.println(UrlExtractorUtility.getUrlHash(""));
            // Assert.assertTrue(goalCode.contains("e897df84-eb2b-41a2-aad3-af7b525cd222")
            // && goalCode.contains("download"));
        }
        catch (IatUtilException e)
        {
            System.out.println("Exception:: " + e);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    // Removing as write operation for a while @Test
    public void provisionAppForIATtest()
    {
        // TODO:Need to add TC

        try
        {
            // dToken.setUserEmailIsSet(true);
            // dToken.setUserEmail("");
            TResult resUpdated = vClient.provisionAppForIAT(dToken, "469c9d2da5674af3ae8d5da034538bee");
            System.out.println(resUpdated);
        }
        catch (Exception e)
        {
            System.out.println("Exception during provisionAppForIAT() Message:: " + e);
        }

    }

    // Removing as write operation for a while @Test
    public void provisionUserInIATtest()
    {
        try
        {
            dToken.userEmail = "archanadby@gmail.com";
            vClient.provisionUserInIAT(dToken, "c3c3ec42-1e9c-11e2-9766-0664c0f1fe9d");

        }
        catch (TException t)
        {
            System.out.println("Thrift Exception:: " + t);
        }
        catch (ServiceException s)
        {
            System.out.println("Service Exception:: " + s);

        }
        catch (Exception e)
        {
            System.out.println("Unknown Exception:: " + e);

        }

    }

    @Test
    public void tempTest() throws Exception
    {
        TApplicationResponse impApps = iClient.getApplicationsToImportFromGPM(dToken, accountID);
        System.out.println(impApps);

    }
}