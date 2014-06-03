/*
 * SSHManager
 * 
 * @author cabbott
 * 
 * @version 1.0
 */
package Temp;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class SSHManager
{
    private static final Logger LOGGER = Logger.getLogger(SSHManager.class.getName());
    private JSch                jschSSHChannel;
    private String              strUserName;
    private String              strConnectionIP;
    private final int           intConnectionPort;
    private String              strPassword;
    private Session             sesConnection;
    private final int           intTimeOut;

    private void doCommonConstructorActions(final String userName, final String password, final String connectionIP,
            String knownHostsFileName)
    {
        jschSSHChannel = new JSch();

        try
        {
            knownHostsFileName = "/Users/gauravkumar.raval/.ssh/known_hosts";
            jschSSHChannel.setKnownHosts(knownHostsFileName);
        }
        catch (JSchException jschX)
        {
            logError(jschX.getMessage());
        }

        strUserName = "USER";
        strPassword = "PASS";
        strConnectionIP = "HOST";
    }

    public SSHManager(final String userName, final String password, final String connectionIP,
            final String knownHostsFileName)
    {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = 22;
        intTimeOut = 60000;
    }

    public SSHManager(final String userName, final String password, final String connectionIP,
            final String knownHostsFileName, final int connectionPort)
    {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = connectionPort;
        intTimeOut = 60000;
    }

    public SSHManager(final String userName, final String password, final String connectionIP,
            final String knownHostsFileName, final int connectionPort, final int timeOutMilliseconds)
    {
        doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
        intConnectionPort = connectionPort;
        intTimeOut = timeOutMilliseconds;
    }

    public String connect()
    {
        String errorMessage = null;

        try
        {
            sesConnection = jschSSHChannel.getSession(strUserName, strConnectionIP, intConnectionPort);
            sesConnection.setPassword(strPassword);
            // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
            // sesConnection.setConfig("StrictHostKeyChecking", "no");
            sesConnection.connect(intTimeOut);
        }
        catch (JSchException jschX)
        {
            errorMessage = jschX.getMessage();
        }

        return errorMessage;
    }

    private String logError(final String errorMessage)
    {
        if (errorMessage != null)
        {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}", new Object[]
            { strConnectionIP, intConnectionPort, errorMessage });
        }

        return errorMessage;
    }

    private String logWarning(final String warnMessage)
    {
        if (warnMessage != null)
        {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}", new Object[]
            { strConnectionIP, intConnectionPort, warnMessage });
        }

        return warnMessage;
    }

    public String sendCommand(final String command)
    {
        int line = 0;
        StringBuilder outputBuffer = new StringBuilder();

        try
        {
            Channel channel = sesConnection.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.connect();
            InputStream commandOutput = channel.getInputStream();
            int readByte = commandOutput.read();

            while (!((outputBuffer.length() > 15000) || ((((outputBuffer.toString()
                    .contains("successfully tracked the click"))) && (outputBuffer.toString().contains("tdl2: "))))))
            {

                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
                if (readByte == '\n')
                {
                    line = line + 1;
                    if (line == 80)
                        System.out.println("OutBufferLength>>> " + outputBuffer.length());

                }
                System.out.println("...Lines ::" + line);
                // System.out.println("Buffer Length::" + outputBuffer.length());
                System.out.println("Is contain:"
                        + (((outputBuffer.toString().contains("successfully tracked the click"))) || (outputBuffer
                                .toString().contains("rq-ip"))));
            }

            channel.disconnect();
        }
        catch (IOException ioX)
        {
            logWarning(ioX.getMessage());
            return null;
        }
        catch (JSchException jschX)
        {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    public void close()
    {
        sesConnection.disconnect();
    }

    @Test
    public static void testSendCommandTest() throws InterruptedException, IOException
    {
        System.out.println("sendCommand");

        /**
         * YOU MUST CHANGE THE FOLLOWING FILE_NAME: A FILE IN THE DIRECTORY USER: LOGIN USER NAME PASSWORD: PASSWORD FOR
         * THAT USER HOST: IP ADDRESS OF THE SSH SERVER
         **/
        String command = "tail -0f /var/log/*.log";
        String userName = "USER";
        String password = "PASS";
        String connectionIP = "HOST";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "");
        String errorMessage = instance.connect();

        if (errorMessage != null)
        {
            System.out.println(errorMessage);
            // fail();
        }

        String expResult = "FILE_NAME\n";
        // sendCommand("ls -lrt");
        // call sendCommand for each command and the output
        // (without prompts) is returned
        String result = instance.sendCommand(command);
        System.out.println("command send :: Waiting...");
        // close only after all commands are sent
        // URL yahoo = new URL(
        // "http://YAHOO.COM);
        // URLConnection connection = yahoo.openConnection();
        // Scanner scanner = new Scanner(connection.getInputStream());
        // scanner.useDelimiter("\\Z");
        // result = scanner.next();

        // BufferedReader in = new BufferedReader(new InputStreamReader(yahoo.openStream()));

        // String inputLine;

        // while ((inputLine = in.readLine()) != null)
        // System.out.println(inputLine);

        // in.close();

        // Thread.sleep(10000);
        System.out.println("Waiting done for 10 seconds::");
        instance.sendCommand("0x03");

        System.out.println(result);

        instance.close();
        // assertEquals(expResult, result);
    }

}
