package com.may.linux;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.*;

/**
 * 连接linux并执行shell命令
 */
public class ConnectLinuxCommand {

    private static final Log log = LogFactory.get();

    private static  String DEFAULTCHARTSET = "UTF-8";
    private static Connection conn;

    /**
     * 以密码认证的方式登录linux服务器
     * @param remoteConnect
     * @return
     */
    public static Boolean login(RemoteConnect remoteConnect){
        boolean flag = false;
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();//连接
            flag = conn.authenticateWithPassword(remoteConnect.getUserName(), remoteConnect.getPassword());
            if (flag){
                log.info("连接成功");
            }else {
                log.info("连接失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 远程执行shell脚本或命令
     * @param cmd
     * @return
     */
    public static String execute(String cmd){
        String result = "";
        try {
            Session session = conn.openSession();//打开一个会话

            session.execCommand(cmd);//执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            //如果得到标准输出为空，说明脚本执行出错
            if (StrUtil.isBlank(result)){
                result = processStdout(session.getStderr(), DEFAULTCHARTSET);
            }
            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    /**
     * 解析脚本执行的返回结果
     * @param in
     * @param charset
     * @return
     */
    public static String processStdout(InputStream in, String charset){
        StreamGobbler stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line;
            while ((line = br.readLine()) != null){
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e){

        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        RemoteConnect dingnuo = new RemoteConnect("10.10.1.1", "dingnuo", "dn@123.com");
        login(dingnuo);
        execute("pwd;cd /mnt/lhdrcs/usbd1/鼎诺/dingnuo01/#recycle/个人文件夹/职能部/宋文霞/捷信/催记/催记导入/;pwd;ls");
    }
}
