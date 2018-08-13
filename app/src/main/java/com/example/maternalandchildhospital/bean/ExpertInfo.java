package com.example.maternalandchildhospital.bean;

import java.util.List;

//{"status":1,"data":{"p":1,"num":20,"data":[{"user_id":215,"phone_number":"15840642820","password":"96e79218965eb72c92a549dd5a330112","push_token":null,"user_nickname":"阿达","user_image_url":"http:\\/\\/121.42.28.104:8080\\/fby\\/default_head.png","create_date":"2018-08-10 16:35:07","update_date":"2018-08-10 16:35:07","card_type":1,"token":null,"get_detail":null},{"user_id":214,"phone_number":"15840642821","password":"96e79218965eb72c92a549dd5a330112","push_token":null,"user_nickname":"阿达","user_image_url":"http:\\/\\/121.42.28.104:8080\\/fby\\/default_head.png","create_date":"2018-08-10 16:29:07","update_date":"2018-08-10 16:29:07","card_type":1,"token":null,"get_detail":null},{"user_id":212,"phone_number":"15840642823","password":"7AD2872B7A7C274F3010498C7477B82F","push_token":null,"user_nickname":null,"user_image_url":"http:\\/\\/121.42.28.104:8080\\/fby\\/default_head.png","create_date":"2018-08-08 14:30:56","update_date":"2018-08-08 14:31:21","card_type":1,"token":null,"get_detail":{"id":1,"nick_name":"西岗区妇幼保健医院-林医生","user_id":212,"weixin":"18863327002","qq":"349995988","text":"您好，这里是西岗区妇幼保健医院在线咨询和预约挂号平台","tel":"15840642823"}}]}}
public class ExpertInfo {
    private int status;
    private Data data;

    public int getStatus(){ return this.status; }
    public Data getData() { return this.data; }

    public class Data{
        private int p,num;
        private List<Ydata> data;

        public int getP(){return this.p;}
        public int getNum(){return this.num;}
        public List<Ydata> getData(){return this.data;}

        public class Ydata{
            private int user_id,card_type;
            private String phone_number,password,push_token,user_nickname,user_image_url,create_date
                    ,update_date,token;
            private GetDetail get_detail;

            public int getUser_id() {return this.user_id;}
            public int getCard_type() {return this.card_type;}
            public String getPhone_number() {return this.phone_number;}
            public String getPassword() {return this.password;}
            public String getPush_token() {return this.push_token;}
            public String getUser_nickname() {return this.user_nickname;}
            public String getUser_image_url() {return this.user_image_url;}
            public String getCreate_date() {return this.create_date;}
            public String getUpdate_date() {return this.update_date;}
            public String getToken() {return this.token;}
            public GetDetail getDetail() {return this.get_detail;}

            public class GetDetail{
                private int id,user_id;
                private String nick_name,weixin,qq,text,tel;

                public int getId() {return this.id;}
                public int getUser_id() {return this.user_id;}
                public String getNick_name() {return this.nick_name;}
                public String getWeixin() {return this.weixin;}
                public String getQq() {return this.qq;}
                public String getText() {return this.text;}
                public String getTel() {return this.tel;}
            }
        }
    }
}
