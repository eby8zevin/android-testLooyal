package com.ahmadabuhasan.looyal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class City {

    @SerializedName("code")
    private Integer code;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<DataCity> dataCity;

    public City() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataCity> getDataCity() {
        return dataCity;
    }

    public void setDataCity(ArrayList<DataCity> dataCity) {
        this.dataCity = dataCity;
    }

    public static class DataCity {

        @SerializedName("code")
        private String code;

        @SerializedName("name")
        private String name;

        @SerializedName("state_code")
        private String stateCode;

        @SerializedName("state_name")
        private String stateName;

        public DataCity() {

        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }
    }
}
