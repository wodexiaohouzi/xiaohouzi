package ai.houzi.xiao.entity;

import java.util.List;

/**
 * Created by hp on 2016/5/19.
 */
public class TianQi {

    /**
     * desc : OK
     * status : 1000
     * data : {"wendu":"24","ganmao":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。","forecast":[{"fengxiang":"东风","fengli":"微风级","high":"高温 26℃","type":"多云","low":"低温 18℃","date":"19日星期四"},{"fengxiang":"东风","fengli":"微风级","high":"高温 28℃","type":"多云","low":"低温 17℃","date":"20日星期五"},{"fengxiang":"东风","fengli":"微风级","high":"高温 27℃","type":"多云","low":"低温 18℃","date":"21日星期六"},{"fengxiang":"东风","fengli":"微风级","high":"高温 26℃","type":"阴","low":"低温 18℃","date":"22日星期天"},{"fengxiang":"东风","fengli":"微风级","high":"高温 28℃","type":"多云","low":"低温 17℃","date":"23日星期一"}],"yesterday":{"fl":"微风","fx":"东风","high":"高温 28℃","type":"多云","low":"低温 18℃","date":"18日星期三"},"aqi":"88","city":"西安"}
     */

    private String desc;
    private int status;
    /**
     * wendu : 24
     * ganmao : 各项气象条件适宜，无明显降温过程，发生感冒机率较低。
     * forecast : [{"fengxiang":"东风","fengli":"微风级","high":"高温 26℃","type":"多云","low":"低温 18℃","date":"19日星期四"},{"fengxiang":"东风","fengli":"微风级","high":"高温 28℃","type":"多云","low":"低温 17℃","date":"20日星期五"},{"fengxiang":"东风","fengli":"微风级","high":"高温 27℃","type":"多云","low":"低温 18℃","date":"21日星期六"},{"fengxiang":"东风","fengli":"微风级","high":"高温 26℃","type":"阴","low":"低温 18℃","date":"22日星期天"},{"fengxiang":"东风","fengli":"微风级","high":"高温 28℃","type":"多云","low":"低温 17℃","date":"23日星期一"}]
     * yesterday : {"fl":"微风","fx":"东风","high":"高温 28℃","type":"多云","low":"低温 18℃","date":"18日星期三"}
     * aqi : 88
     * city : 西安
     */

    private DataEntity data;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String wendu;
        private String ganmao;
        /**
         * fl : 微风
         * fx : 东风
         * high : 高温 28℃
         * type : 多云
         * low : 低温 18℃
         * date : 18日星期三
         */

        private YesterdayEntity yesterday;
        private String aqi;
        private String city;
        /**
         * fengxiang : 东风
         * fengli : 微风级
         * high : 高温 26℃
         * type : 多云
         * low : 低温 18℃
         * date : 19日星期四
         */

        private List<ForecastEntity> forecast;

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public YesterdayEntity getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayEntity yesterday) {
            this.yesterday = yesterday;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<ForecastEntity> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastEntity> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayEntity {
            private String fl;
            private String fx;
            private String high;
            private String type;
            private String low;
            private String date;

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }

        public static class ForecastEntity {
            private String fengxiang;
            private String fengli;
            private String high;
            private String type;
            private String low;
            private String date;

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
