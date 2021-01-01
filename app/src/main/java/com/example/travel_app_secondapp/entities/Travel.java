package com.example.travel_app_secondapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Entity (tableName = "travels")
public class Travel {
    @NonNull
    @PrimaryKey
    private String travelId = "id";
    private String clientName;
	private String clientPhone;
    private String clientEmail;
	private Integer numPassengers = 0;
	
	@TypeConverters(UserLocationConverter.class)
	private UserLocation travelLocation;

    @TypeConverters(UserLocationsConverter.class)
    private List<UserLocation> destLocations;
	
	@TypeConverters(RequestType.class)
    private RequestType requestType;

    @TypeConverters(DateConverter.class)
    private Date travelDate;
	
    @TypeConverters(DateConverter.class)
    private Date arrivalDate;
	
		
	private HashMap<String, Boolean> company;
    
    // with these methods the fire base can place all the members in our class
	public String getTravelId(){
	    return this.travelId;
    }
    public String getClientName() {return this.clientName; }
    public String getClientPhone() {return this.clientPhone; }
    public String getClientEmail() {return this.clientEmail; }
    public Integer getNumPassengers() { return this.numPassengers; }
    public UserLocation getTravelLocation() {return this.travelLocation;}
    public List<UserLocation> getDestLocations() { return destLocations; }
    public RequestType getRequestType() { return this.requestType;}
    public Date getTravelDate() { return this.travelDate;}
    public Date getArrivalDate() { return this.arrivalDate;}
    public HashMap<String, Boolean> getCompany() { return this.company;}


    public Travel() {
    }

    public void setTravelId(String id) {
	    if (id != null)
	        this.travelId = id;
    }

    public void setCompany(HashMap<String, Boolean> company) {
        this.company = company;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void setClientName(String clientName)  {
        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone)  {
        this.clientPhone = clientPhone;
    }



    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public void setNumPassengers(Integer numPassengers) {
        this.numPassengers = numPassengers;
    }

    public void setTravelLocation(UserLocation travelLocation)  {
        this.travelLocation = travelLocation;
    }

    public void setDestLocations(List<UserLocation> destLocations) {
        this.destLocations = destLocations;
    }

    public void setTravelDate(Date travelDate)  {
        this.travelDate = travelDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public static class DateConverter {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        @TypeConverter
        public Date fromTimestamp(String date) throws ParseException {
            return (date == null ? null : format.parse(date));
        }

        @TypeConverter
        public String dateToTimestamp(Date date) {
            return date == null ? null : format.format(date);
        }
    }
	
	
	
	public enum RequestType {
        sent(0), accepted(1), run(2), close(3);
        private final Integer code;
        RequestType(Integer value) {
            this.code = value;
        }
        public Integer getCode() {
            return code;
        }
        @TypeConverter
        public static RequestType getType(Integer numeral) {
            for (RequestType ds : values())
                if (ds.code.equals(numeral))
                    return ds;
            return null;
        }
        @TypeConverter
        public static Integer getTypeInt(RequestType requestType) {
            if (requestType != null)
                return requestType.code;
            return null;
        }
    }
	
	
	
	
	 public static class CompanyConverter {
        @TypeConverter
        public HashMap<String, Boolean> fromString(String value) {
            if (value == null || value.isEmpty())
                return null;
            String[] mapString = value.split(","); //split map into array of (string,boolean) strings
            HashMap<String, Boolean> hashMap = new HashMap<>();
            for (String s1 : mapString) //for all (string,boolean) in the map string
            {
                if (!s1.isEmpty()) {//is empty maybe will needed because the last char in the string is ","
                    String[] s2 = s1.split(":"); //split (string,boolean) to company string and boolean string.
                    Boolean aBoolean = Boolean.parseBoolean(s2[1]);
                    hashMap.put(/*company string:*/s2[0], aBoolean);
                }
            }
            return hashMap;
        }

        @TypeConverter
        public String asString(HashMap<String, Boolean> map) {
            if (map == null)
                return null;
            StringBuilder mapString = new StringBuilder();
            for (Map.Entry<String, Boolean> entry : map.entrySet())
                mapString.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            return mapString.toString();
        }
    }

    public static class UserLocationConverter {
        @TypeConverter
        public UserLocation fromString(String value) {
            if (value == null || value.equals(""))
                return null;
            double lat = Double.parseDouble(value.split(" ")[0]);
            double lang = Double.parseDouble(value.split(" ")[1]);
            return new UserLocation(lat, lang);
        }

        @TypeConverter
        public String asString(UserLocation warehouseUserLocation) {
            return warehouseUserLocation == null ? "" : warehouseUserLocation.getLat() + " " + warehouseUserLocation.getLon();
        }
    }


    //    private List<UserLocation> destLocations;
    public static class UserLocationsConverter {
        @TypeConverter
        public List<UserLocation> fromString(String value) {
            if (value == null || value.isEmpty())
                return null;
            String[] mapString = value.split(","); //split map into array of (string,boolean) strings
            List<UserLocation> destLocations = new ArrayList<UserLocation>();
            for (String s1 : mapString) //for all (string,boolean) in the map string
            {
                if (!s1.isEmpty()) {//is empty maybe will needed because the last char in the string is ","
                    double lat = Double.parseDouble(s1.split(" ")[0]);
                    double lang = Double.parseDouble(s1.split(" ")[1]);
                    destLocations.add(/*company string:*/new UserLocation(lat, lang));
                }
            }
            return destLocations;
        }

        @TypeConverter
        public String asString(List<UserLocation> warehouseUserLocation) {
            if (warehouseUserLocation == null)
                return null;
            StringBuilder locationsString = new StringBuilder();
            for (UserLocation ul : warehouseUserLocation)
                locationsString.append(ul.getLat()).append(" ").append(ul.getLon()).append(",");
            return locationsString.toString();
        }
    }
}
	
	