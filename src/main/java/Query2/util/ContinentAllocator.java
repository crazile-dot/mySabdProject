package Query2.util;

import java.util.ArrayList;
import java.util.Arrays;


public class ContinentAllocator {

    private final static ArrayList<String> Europe = new ArrayList(Arrays.asList("Faroe Islands",
            "Greenland", "Channel Islands", "Gibraltar", "Isle of Man","Bulgaria",
            "Latvia", "Albania", "Andorra", "Austria", "Belarus","Belgium","Bosnia and Herzegovina",
            "Croatia", "Cyprus", "Czechia","Denmark", "Estonia","Finland","France","Georgia","Germany",
            "Greece", "Holy See", "Hungary", "Iceland", "Ireland","Italy","Liechtenstein",
            "Lithuania","Luxembourg","Malta","Moldova","Monaco","Montenegro","Netherlands",
            "North Macedonia", "Norway", "Poland", "Portugal", "Romania", "San Marino", "Serbia",
            "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Ukraine", "United Kingdom",
            "Kosovo"));

    private final static ArrayList<String> Africa = new ArrayList(Arrays.asList("Algeria","Angola","Benin","Burkina Faso","Cabo Verde",
            "Cameroon","Central African Republic","Chad","Congo (Brazzaville)","Congo (Kinshasa)", "Cote d'Ivoire",
            "Djibouti","Egypt", "Equatorial Guinea","Eritrea","Eswatini","Ethiopia","Mayotte","Reunion","Gabon",
            "Gambia","Ghana","Kenya","Liberia","Madagascar", "Mauritania","Mauritius","Morocco", "Namibia","Niger",
            "Nigeria","Rwanda","Senegal","Seychelles","Somalia","South Africa", "Sudan","Tanzania","Togo", "Tunisia",
            "Uganda","Zambia","Zimbabwe","Mozambique", "Libya","Guinea-Bissau","Mali","Botswana","Burundi","Sierra Leone",
            "Malawi","South Sudan","Western Sahara","Sao Tome and Principe","Comoros"));

    private final static ArrayList<String> America = new ArrayList(Arrays.asList("Alberta", "British Columbia", "Grand Princess", "Manitoba",
            "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec",
            "Saskatchewan", "French Guiana", "Guadeloupe", "Saint Barthelemy", "St Martin", "Martinique",
            "Aruba", "Curacao", "Sint Maarten", "Bermuda", "Cayman Islands","Montserrat", "Northwest Territories", "Yukon",
            "Anguilla", "British Virgin Islands", "Turks and Caicos Islands", "Bonaire, Sint Eustatius and Saba",
            "Falkland Islands (Malvinas)", "Saint Pierre and Miquelon","Antigua and Barbuda", "Argentina",
            "Bahamas", "Barbados", "Bolivia", "Brazil", "Chile", "Colombia", "Costa Rica", "Cuba", "Dominican Republic",
            "Ecuador", "El Salvador", "Guatemala", "Guyana", "Haiti", "Honduras", "Jamaica", "Mexico", "Nicaragua",
            "Panama", "Paraguay", "Peru", "Saint Lucia", "Saint Vincent and the Grenadines", "Suriname", "Trinidad and Tobago"));

    private final static ArrayList<String> Oceania = new ArrayList(Arrays.asList("Australian Capital Territory", "New South Wales", "Northern Territory",
            "Queensland", "South Australia", "Tasmania", "Victoria", "Western Australia", "French Polynesia", "New Caledonia","Fiji",
            "Guinea", "New Zealand", "Papua New Guinea"));

    private final static ArrayList<String> Asia = new ArrayList(Arrays.asList("Anhui","Beijing", "Chongqing", "Fujian", "Gansu", "Guangdong", "Guangxi",
            "Guizhou", "Hainan", "Hebei", "Heilongjiang", "Henan", "Hong Kong", "Hubei", "Hunan", "Inner Mongolia", "Jiangsu",
            "Jiangxi", "Jilin", "Liaoning", "Macau", "Ningxia", "Qinghai", "Shaanxi", "Shandong", "Shanghai", "Shanxi", "Sichuan",
            "Tianjin", "Tibet", "Xinjiang","Yunnan", "Zhejiang","Afghanistan", "Armenia", "Azerbaijan", "Bahrain", "Bangladesh",
            "Bhutan", "Brunei", "Cambodia", "India", "Indonesia", "Iran", "Iraq", "Israel", "Japan", "Jordan", "Kazakhstan",
            "Korea, South", "Kuwait", "Kyrgyzstan", "Lebanon", "Malaysia", "Maldives", "Mongolia", "Nepal", "Oman", "Pakistan",
            "Philippines", "Qatar", "Russia", "Saudi Arabia", "Singapore", "Sri Lanka", "Taiwan", "Thailand", "Turkey",
            "United Arab Emirates", "Uzbekistan", "Vietnam", "Syria", "Timor-Leste", "Laos", "Burma", "Yemen", "Tajikistan"));

    public static State allocateContinent(State state) {
        String nation;
        if(!state.getState().equals("")) {
            nation = state.getState();
        } else {
            nation = state.getCountry();
        }

        if(Africa.contains(nation)) {
            state.setContinent("Africa");
        } else if(Europe.contains(nation)) {
            state.setContinent("Europe");
        } else if(America.contains(nation)) {
            state.setContinent("America");
        } else if(Oceania.contains(nation)) {
            state.setContinent("Oceania");
        } else if(Asia.contains(nation)) {
            state.setContinent("Asia");
        }
        return state;
    }

}



