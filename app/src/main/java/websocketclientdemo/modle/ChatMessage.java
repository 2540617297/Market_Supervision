package websocketclientdemo.modle;

public class ChatMessage {
    private String content;
    private String time;
    private int isMeSend;//0是对方发送 1是自己发送
    private int isRead;//是否已读（0未读 1已读）
    private String mybSendUser;//被发送的对象
    private String mynSendUser;//当前对象
    private String getbSendUser;//获得消息
    private String getnSendUser;//获得消息
    private String userNameCN;

    public String getUserNameCN() {
        return userNameCN;
    }

    public void setUserNameCN(String userNameCN) {
        this.userNameCN = userNameCN;
    }


    public String getMybSendUser() {
        return mybSendUser;
    }

    public void setMybSendUser(String mybSendUser) {
        this.mybSendUser = mybSendUser;
    }

    public String getMynSendUser() {
        return mynSendUser;
    }

    public void setMynSendUser(String mynSendUser) {
        this.mynSendUser = mynSendUser;
    }

    public String getGetbSendUser() {
        return getbSendUser;
    }

    public void setGetbSendUser(String getbSendUser) {
        this.getbSendUser = getbSendUser;
    }

    public String getGetnSendUser() {
        return getnSendUser;
    }

    public void setGetnSendUser(String getnSendUser) {
        this.getnSendUser = getnSendUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(int isMeSend) {
        this.isMeSend = isMeSend;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
