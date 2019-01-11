/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TimeUtil
/*      */ {
/*      */   static final Map ABBREVIATED_TIMEZONES;
/*   48 */   static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
/*      */   static final Map TIMEZONE_MAPPINGS;
/*      */   
/*      */   static
/*      */   {
/*   53 */     HashMap tempMap = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   58 */     tempMap.put("Romance", "Europe/Paris");
/*   59 */     tempMap.put("Romance Standard Time", "Europe/Paris");
/*   60 */     tempMap.put("Warsaw", "Europe/Warsaw");
/*   61 */     tempMap.put("Central Europe", "Europe/Prague");
/*   62 */     tempMap.put("Central Europe Standard Time", "Europe/Prague");
/*   63 */     tempMap.put("Prague Bratislava", "Europe/Prague");
/*   64 */     tempMap.put("W. Central Africa Standard Time", "Africa/Luanda");
/*   65 */     tempMap.put("FLE", "Europe/Helsinki");
/*   66 */     tempMap.put("FLE Standard Time", "Europe/Helsinki");
/*   67 */     tempMap.put("GFT", "Europe/Athens");
/*   68 */     tempMap.put("GFT Standard Time", "Europe/Athens");
/*   69 */     tempMap.put("GTB", "Europe/Athens");
/*   70 */     tempMap.put("GTB Standard Time", "Europe/Athens");
/*   71 */     tempMap.put("Israel", "Asia/Jerusalem");
/*   72 */     tempMap.put("Israel Standard Time", "Asia/Jerusalem");
/*   73 */     tempMap.put("Arab", "Asia/Riyadh");
/*   74 */     tempMap.put("Arab Standard Time", "Asia/Riyadh");
/*   75 */     tempMap.put("Arabic Standard Time", "Asia/Baghdad");
/*   76 */     tempMap.put("E. Africa", "Africa/Nairobi");
/*   77 */     tempMap.put("E. Africa Standard Time", "Africa/Nairobi");
/*   78 */     tempMap.put("Saudi Arabia", "Asia/Riyadh");
/*   79 */     tempMap.put("Saudi Arabia Standard Time", "Asia/Riyadh");
/*   80 */     tempMap.put("Iran", "Asia/Tehran");
/*   81 */     tempMap.put("Iran Standard Time", "Asia/Tehran");
/*   82 */     tempMap.put("Afghanistan", "Asia/Kabul");
/*   83 */     tempMap.put("Afghanistan Standard Time", "Asia/Kabul");
/*   84 */     tempMap.put("India", "Asia/Calcutta");
/*   85 */     tempMap.put("India Standard Time", "Asia/Calcutta");
/*   86 */     tempMap.put("Myanmar Standard Time", "Asia/Rangoon");
/*   87 */     tempMap.put("Nepal Standard Time", "Asia/Katmandu");
/*   88 */     tempMap.put("Sri Lanka", "Asia/Colombo");
/*   89 */     tempMap.put("Sri Lanka Standard Time", "Asia/Colombo");
/*   90 */     tempMap.put("Beijing", "Asia/Shanghai");
/*   91 */     tempMap.put("China", "Asia/Shanghai");
/*   92 */     tempMap.put("China Standard Time", "Asia/Shanghai");
/*   93 */     tempMap.put("AUS Central", "Australia/Darwin");
/*   94 */     tempMap.put("AUS Central Standard Time", "Australia/Darwin");
/*   95 */     tempMap.put("Cen. Australia", "Australia/Adelaide");
/*   96 */     tempMap.put("Cen. Australia Standard Time", "Australia/Adelaide");
/*   97 */     tempMap.put("Vladivostok", "Asia/Vladivostok");
/*   98 */     tempMap.put("Vladivostok Standard Time", "Asia/Vladivostok");
/*   99 */     tempMap.put("West Pacific", "Pacific/Guam");
/*  100 */     tempMap.put("West Pacific Standard Time", "Pacific/Guam");
/*  101 */     tempMap.put("E. South America", "America/Sao_Paulo");
/*  102 */     tempMap.put("E. South America Standard Time", "America/Sao_Paulo");
/*  103 */     tempMap.put("Greenland Standard Time", "America/Godthab");
/*  104 */     tempMap.put("Newfoundland", "America/St_Johns");
/*  105 */     tempMap.put("Newfoundland Standard Time", "America/St_Johns");
/*  106 */     tempMap.put("Pacific SA", "America/Caracas");
/*  107 */     tempMap.put("Pacific SA Standard Time", "America/Caracas");
/*  108 */     tempMap.put("SA Western", "America/Caracas");
/*  109 */     tempMap.put("SA Western Standard Time", "America/Caracas");
/*  110 */     tempMap.put("SA Pacific", "America/Bogota");
/*  111 */     tempMap.put("SA Pacific Standard Time", "America/Bogota");
/*  112 */     tempMap.put("US Eastern", "America/Indianapolis");
/*  113 */     tempMap.put("US Eastern Standard Time", "America/Indianapolis");
/*  114 */     tempMap.put("Central America Standard Time", "America/Regina");
/*  115 */     tempMap.put("Mexico", "America/Mexico_City");
/*  116 */     tempMap.put("Mexico Standard Time", "America/Mexico_City");
/*  117 */     tempMap.put("Canada Central", "America/Regina");
/*  118 */     tempMap.put("Canada Central Standard Time", "America/Regina");
/*  119 */     tempMap.put("US Mountain", "America/Phoenix");
/*  120 */     tempMap.put("US Mountain Standard Time", "America/Phoenix");
/*  121 */     tempMap.put("GMT", "Europe/London");
/*  122 */     tempMap.put("GMT Standard Time", "Europe/London");
/*  123 */     tempMap.put("Ekaterinburg", "Asia/Yekaterinburg");
/*  124 */     tempMap.put("Ekaterinburg Standard Time", "Asia/Yekaterinburg");
/*  125 */     tempMap.put("West Asia", "Asia/Karachi");
/*  126 */     tempMap.put("West Asia Standard Time", "Asia/Karachi");
/*  127 */     tempMap.put("Central Asia", "Asia/Dhaka");
/*  128 */     tempMap.put("Central Asia Standard Time", "Asia/Dhaka");
/*  129 */     tempMap.put("N. Central Asia Standard Time", "Asia/Novosibirsk");
/*  130 */     tempMap.put("Bangkok", "Asia/Bangkok");
/*  131 */     tempMap.put("Bangkok Standard Time", "Asia/Bangkok");
/*  132 */     tempMap.put("North Asia Standard Time", "Asia/Krasnoyarsk");
/*  133 */     tempMap.put("SE Asia", "Asia/Bangkok");
/*  134 */     tempMap.put("SE Asia Standard Time", "Asia/Bangkok");
/*  135 */     tempMap.put("North Asia East Standard Time", "Asia/Ulaanbaatar");
/*  136 */     tempMap.put("Singapore", "Asia/Singapore");
/*  137 */     tempMap.put("Singapore Standard Time", "Asia/Singapore");
/*  138 */     tempMap.put("Taipei", "Asia/Taipei");
/*  139 */     tempMap.put("Taipei Standard Time", "Asia/Taipei");
/*  140 */     tempMap.put("W. Australia", "Australia/Perth");
/*  141 */     tempMap.put("W. Australia Standard Time", "Australia/Perth");
/*  142 */     tempMap.put("Korea", "Asia/Seoul");
/*  143 */     tempMap.put("Korea Standard Time", "Asia/Seoul");
/*  144 */     tempMap.put("Tokyo", "Asia/Tokyo");
/*  145 */     tempMap.put("Tokyo Standard Time", "Asia/Tokyo");
/*  146 */     tempMap.put("Yakutsk", "Asia/Yakutsk");
/*  147 */     tempMap.put("Yakutsk Standard Time", "Asia/Yakutsk");
/*  148 */     tempMap.put("Central European", "Europe/Belgrade");
/*  149 */     tempMap.put("Central European Standard Time", "Europe/Belgrade");
/*  150 */     tempMap.put("W. Europe", "Europe/Berlin");
/*  151 */     tempMap.put("W. Europe Standard Time", "Europe/Berlin");
/*  152 */     tempMap.put("Tasmania", "Australia/Hobart");
/*  153 */     tempMap.put("Tasmania Standard Time", "Australia/Hobart");
/*  154 */     tempMap.put("AUS Eastern", "Australia/Sydney");
/*  155 */     tempMap.put("AUS Eastern Standard Time", "Australia/Sydney");
/*  156 */     tempMap.put("E. Australia", "Australia/Brisbane");
/*  157 */     tempMap.put("E. Australia Standard Time", "Australia/Brisbane");
/*  158 */     tempMap.put("Sydney Standard Time", "Australia/Sydney");
/*  159 */     tempMap.put("Central Pacific", "Pacific/Guadalcanal");
/*  160 */     tempMap.put("Central Pacific Standard Time", "Pacific/Guadalcanal");
/*  161 */     tempMap.put("Dateline", "Pacific/Majuro");
/*  162 */     tempMap.put("Dateline Standard Time", "Pacific/Majuro");
/*  163 */     tempMap.put("Fiji", "Pacific/Fiji");
/*  164 */     tempMap.put("Fiji Standard Time", "Pacific/Fiji");
/*  165 */     tempMap.put("Samoa", "Pacific/Apia");
/*  166 */     tempMap.put("Samoa Standard Time", "Pacific/Apia");
/*  167 */     tempMap.put("Hawaiian", "Pacific/Honolulu");
/*  168 */     tempMap.put("Hawaiian Standard Time", "Pacific/Honolulu");
/*  169 */     tempMap.put("Alaskan", "America/Anchorage");
/*  170 */     tempMap.put("Alaskan Standard Time", "America/Anchorage");
/*  171 */     tempMap.put("Pacific", "America/Los_Angeles");
/*  172 */     tempMap.put("Pacific Standard Time", "America/Los_Angeles");
/*  173 */     tempMap.put("Mexico Standard Time 2", "America/Chihuahua");
/*  174 */     tempMap.put("Mountain", "America/Denver");
/*  175 */     tempMap.put("Mountain Standard Time", "America/Denver");
/*  176 */     tempMap.put("Central", "America/Chicago");
/*  177 */     tempMap.put("Central Standard Time", "America/Chicago");
/*  178 */     tempMap.put("Eastern", "America/New_York");
/*  179 */     tempMap.put("Eastern Standard Time", "America/New_York");
/*  180 */     tempMap.put("E. Europe", "Europe/Bucharest");
/*  181 */     tempMap.put("E. Europe Standard Time", "Europe/Bucharest");
/*  182 */     tempMap.put("Egypt", "Africa/Cairo");
/*  183 */     tempMap.put("Egypt Standard Time", "Africa/Cairo");
/*  184 */     tempMap.put("South Africa", "Africa/Harare");
/*  185 */     tempMap.put("South Africa Standard Time", "Africa/Harare");
/*  186 */     tempMap.put("Atlantic", "America/Halifax");
/*  187 */     tempMap.put("Atlantic Standard Time", "America/Halifax");
/*  188 */     tempMap.put("SA Eastern", "America/Buenos_Aires");
/*  189 */     tempMap.put("SA Eastern Standard Time", "America/Buenos_Aires");
/*  190 */     tempMap.put("Mid-Atlantic", "Atlantic/South_Georgia");
/*  191 */     tempMap.put("Mid-Atlantic Standard Time", "Atlantic/South_Georgia");
/*  192 */     tempMap.put("Azores", "Atlantic/Azores");
/*  193 */     tempMap.put("Azores Standard Time", "Atlantic/Azores");
/*  194 */     tempMap.put("Cape Verde Standard Time", "Atlantic/Cape_Verde");
/*  195 */     tempMap.put("Russian", "Europe/Moscow");
/*  196 */     tempMap.put("Russian Standard Time", "Europe/Moscow");
/*  197 */     tempMap.put("New Zealand", "Pacific/Auckland");
/*  198 */     tempMap.put("New Zealand Standard Time", "Pacific/Auckland");
/*  199 */     tempMap.put("Tonga Standard Time", "Pacific/Tongatapu");
/*  200 */     tempMap.put("Arabian", "Asia/Muscat");
/*  201 */     tempMap.put("Arabian Standard Time", "Asia/Muscat");
/*  202 */     tempMap.put("Caucasus", "Asia/Tbilisi");
/*  203 */     tempMap.put("Caucasus Standard Time", "Asia/Tbilisi");
/*  204 */     tempMap.put("GMT Standard Time", "GMT");
/*  205 */     tempMap.put("Greenwich", "GMT");
/*  206 */     tempMap.put("Greenwich Standard Time", "GMT");
/*  207 */     tempMap.put("UTC", "GMT");
/*      */     
/*  209 */     TIMEZONE_MAPPINGS = Collections.unmodifiableMap(tempMap);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  214 */     tempMap = new HashMap();
/*      */     
/*  216 */     tempMap.put("ACST", new String[] { "America/Porto_Acre" });
/*  217 */     tempMap.put("ACT", new String[] { "America/Porto_Acre" });
/*  218 */     tempMap.put("ADDT", new String[] { "America/Pangnirtung" });
/*  219 */     tempMap.put("ADMT", new String[] { "Africa/Asmera", "Africa/Addis_Ababa" });
/*      */     
/*  221 */     tempMap.put("ADT", new String[] { "Atlantic/Bermuda", "Asia/Baghdad", "America/Thule", "America/Goose_Bay", "America/Halifax", "America/Glace_Bay", "America/Pangnirtung", "America/Barbados", "America/Martinique" });
/*      */     
/*      */ 
/*      */ 
/*  225 */     tempMap.put("AFT", new String[] { "Asia/Kabul" });
/*  226 */     tempMap.put("AHDT", new String[] { "America/Anchorage" });
/*  227 */     tempMap.put("AHST", new String[] { "America/Anchorage" });
/*  228 */     tempMap.put("AHWT", new String[] { "America/Anchorage" });
/*  229 */     tempMap.put("AKDT", new String[] { "America/Juneau", "America/Yakutat", "America/Anchorage", "America/Nome" });
/*      */     
/*  231 */     tempMap.put("AKST", new String[] { "Asia/Aqtobe", "America/Juneau", "America/Yakutat", "America/Anchorage", "America/Nome" });
/*      */     
/*  233 */     tempMap.put("AKT", new String[] { "Asia/Aqtobe" });
/*  234 */     tempMap.put("AKTST", new String[] { "Asia/Aqtobe" });
/*  235 */     tempMap.put("AKWT", new String[] { "America/Juneau", "America/Yakutat", "America/Anchorage", "America/Nome" });
/*      */     
/*  237 */     tempMap.put("ALMST", new String[] { "Asia/Almaty" });
/*  238 */     tempMap.put("ALMT", new String[] { "Asia/Almaty" });
/*  239 */     tempMap.put("AMST", new String[] { "Asia/Yerevan", "America/Cuiaba", "America/Porto_Velho", "America/Boa_Vista", "America/Manaus" });
/*      */     
/*  241 */     tempMap.put("AMT", new String[] { "Europe/Athens", "Europe/Amsterdam", "Asia/Yerevan", "Africa/Asmera", "America/Cuiaba", "America/Porto_Velho", "America/Boa_Vista", "America/Manaus", "America/Asuncion" });
/*      */     
/*      */ 
/*      */ 
/*  245 */     tempMap.put("ANAMT", new String[] { "Asia/Anadyr" });
/*  246 */     tempMap.put("ANAST", new String[] { "Asia/Anadyr" });
/*  247 */     tempMap.put("ANAT", new String[] { "Asia/Anadyr" });
/*  248 */     tempMap.put("ANT", new String[] { "America/Aruba", "America/Curacao" });
/*  249 */     tempMap.put("AQTST", new String[] { "Asia/Aqtobe", "Asia/Aqtau" });
/*  250 */     tempMap.put("AQTT", new String[] { "Asia/Aqtobe", "Asia/Aqtau" });
/*  251 */     tempMap.put("ARST", new String[] { "Antarctica/Palmer", "America/Buenos_Aires", "America/Rosario", "America/Cordoba", "America/Jujuy", "America/Catamarca", "America/Mendoza" });
/*      */     
/*      */ 
/*  254 */     tempMap.put("ART", new String[] { "Antarctica/Palmer", "America/Buenos_Aires", "America/Rosario", "America/Cordoba", "America/Jujuy", "America/Catamarca", "America/Mendoza" });
/*      */     
/*      */ 
/*  257 */     tempMap.put("ASHST", new String[] { "Asia/Ashkhabad" });
/*  258 */     tempMap.put("ASHT", new String[] { "Asia/Ashkhabad" });
/*  259 */     tempMap.put("AST", new String[] { "Atlantic/Bermuda", "Asia/Bahrain", "Asia/Baghdad", "Asia/Kuwait", "Asia/Qatar", "Asia/Riyadh", "Asia/Aden", "America/Thule", "America/Goose_Bay", "America/Halifax", "America/Glace_Bay", "America/Pangnirtung", "America/Anguilla", "America/Antigua", "America/Barbados", "America/Dominica", "America/Santo_Domingo", "America/Grenada", "America/Guadeloupe", "America/Martinique", "America/Montserrat", "America/Puerto_Rico", "America/St_Kitts", "America/St_Lucia", "America/Miquelon", "America/St_Vincent", "America/Tortola", "America/St_Thomas", "America/Aruba", "America/Curacao", "America/Port_of_Spain" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  270 */     tempMap.put("AWT", new String[] { "America/Puerto_Rico" });
/*  271 */     tempMap.put("AZOST", new String[] { "Atlantic/Azores" });
/*  272 */     tempMap.put("AZOT", new String[] { "Atlantic/Azores" });
/*  273 */     tempMap.put("AZST", new String[] { "Asia/Baku" });
/*  274 */     tempMap.put("AZT", new String[] { "Asia/Baku" });
/*  275 */     tempMap.put("BAKST", new String[] { "Asia/Baku" });
/*  276 */     tempMap.put("BAKT", new String[] { "Asia/Baku" });
/*  277 */     tempMap.put("BDT", new String[] { "Asia/Dacca", "America/Nome", "America/Adak" });
/*      */     
/*  279 */     tempMap.put("BEAT", new String[] { "Africa/Nairobi", "Africa/Mogadishu", "Africa/Kampala" });
/*      */     
/*  281 */     tempMap.put("BEAUT", new String[] { "Africa/Nairobi", "Africa/Dar_es_Salaam", "Africa/Kampala" });
/*      */     
/*  283 */     tempMap.put("BMT", new String[] { "Europe/Brussels", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Bucharest", "Europe/Zurich", "Asia/Baghdad", "Asia/Bangkok", "Africa/Banjul", "America/Barbados", "America/Bogota" });
/*      */     
/*      */ 
/*      */ 
/*  287 */     tempMap.put("BNT", new String[] { "Asia/Brunei" });
/*  288 */     tempMap.put("BORT", new String[] { "Asia/Ujung_Pandang", "Asia/Kuching" });
/*      */     
/*  290 */     tempMap.put("BOST", new String[] { "America/La_Paz" });
/*  291 */     tempMap.put("BOT", new String[] { "America/La_Paz" });
/*  292 */     tempMap.put("BRST", new String[] { "America/Belem", "America/Fortaleza", "America/Araguaina", "America/Maceio", "America/Sao_Paulo" });
/*      */     
/*      */ 
/*  295 */     tempMap.put("BRT", new String[] { "America/Belem", "America/Fortaleza", "America/Araguaina", "America/Maceio", "America/Sao_Paulo" });
/*      */     
/*  297 */     tempMap.put("BST", new String[] { "Europe/London", "Europe/Belfast", "Europe/Dublin", "Europe/Gibraltar", "Pacific/Pago_Pago", "Pacific/Midway", "America/Nome", "America/Adak" });
/*      */     
/*      */ 
/*  300 */     tempMap.put("BTT", new String[] { "Asia/Thimbu" });
/*  301 */     tempMap.put("BURT", new String[] { "Asia/Dacca", "Asia/Rangoon", "Asia/Calcutta" });
/*      */     
/*  303 */     tempMap.put("BWT", new String[] { "America/Nome", "America/Adak" });
/*  304 */     tempMap.put("CANT", new String[] { "Atlantic/Canary" });
/*  305 */     tempMap.put("CAST", new String[] { "Africa/Gaborone", "Africa/Khartoum" });
/*      */     
/*  307 */     tempMap.put("CAT", new String[] { "Africa/Gaborone", "Africa/Bujumbura", "Africa/Lubumbashi", "Africa/Blantyre", "Africa/Maputo", "Africa/Windhoek", "Africa/Kigali", "Africa/Khartoum", "Africa/Lusaka", "Africa/Harare", "America/Anchorage" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  312 */     tempMap.put("CCT", new String[] { "Indian/Cocos" });
/*  313 */     tempMap.put("CDDT", new String[] { "America/Rankin_Inlet" });
/*  314 */     tempMap.put("CDT", new String[] { "Asia/Harbin", "Asia/Shanghai", "Asia/Chungking", "Asia/Urumqi", "Asia/Kashgar", "Asia/Taipei", "Asia/Macao", "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Menominee", "America/Rainy_River", "America/Winnipeg", "America/Pangnirtung", "America/Iqaluit", "America/Rankin_Inlet", "America/Cambridge_Bay", "America/Cancun", "America/Mexico_City", "America/Chihuahua", "America/Belize", "America/Costa_Rica", "America/Havana", "America/El_Salvador", "America/Guatemala", "America/Tegucigalpa", "America/Managua" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  326 */     tempMap.put("CEST", new String[] { "Europe/Tirane", "Europe/Andorra", "Europe/Vienna", "Europe/Minsk", "Europe/Brussels", "Europe/Sofia", "Europe/Prague", "Europe/Copenhagen", "Europe/Tallinn", "Europe/Berlin", "Europe/Gibraltar", "Europe/Athens", "Europe/Budapest", "Europe/Rome", "Europe/Riga", "Europe/Vaduz", "Europe/Vilnius", "Europe/Luxembourg", "Europe/Malta", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Monaco", "Europe/Amsterdam", "Europe/Oslo", "Europe/Warsaw", "Europe/Lisbon", "Europe/Kaliningrad", "Europe/Madrid", "Europe/Stockholm", "Europe/Zurich", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Europe/Belgrade", "Africa/Algiers", "Africa/Tripoli", "Africa/Tunis", "Africa/Ceuta" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  340 */     tempMap.put("CET", new String[] { "Europe/Tirane", "Europe/Andorra", "Europe/Vienna", "Europe/Minsk", "Europe/Brussels", "Europe/Sofia", "Europe/Prague", "Europe/Copenhagen", "Europe/Tallinn", "Europe/Berlin", "Europe/Gibraltar", "Europe/Athens", "Europe/Budapest", "Europe/Rome", "Europe/Riga", "Europe/Vaduz", "Europe/Vilnius", "Europe/Luxembourg", "Europe/Malta", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Monaco", "Europe/Amsterdam", "Europe/Oslo", "Europe/Warsaw", "Europe/Lisbon", "Europe/Kaliningrad", "Europe/Madrid", "Europe/Stockholm", "Europe/Zurich", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Europe/Belgrade", "Africa/Algiers", "Africa/Tripoli", "Africa/Casablanca", "Africa/Tunis", "Africa/Ceuta" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  354 */     tempMap.put("CGST", new String[] { "America/Scoresbysund" });
/*  355 */     tempMap.put("CGT", new String[] { "America/Scoresbysund" });
/*  356 */     tempMap.put("CHDT", new String[] { "America/Belize" });
/*  357 */     tempMap.put("CHUT", new String[] { "Asia/Chungking" });
/*  358 */     tempMap.put("CJT", new String[] { "Asia/Tokyo" });
/*  359 */     tempMap.put("CKHST", new String[] { "Pacific/Rarotonga" });
/*  360 */     tempMap.put("CKT", new String[] { "Pacific/Rarotonga" });
/*  361 */     tempMap.put("CLST", new String[] { "Antarctica/Palmer", "America/Santiago" });
/*      */     
/*  363 */     tempMap.put("CLT", new String[] { "Antarctica/Palmer", "America/Santiago" });
/*      */     
/*  365 */     tempMap.put("CMT", new String[] { "Europe/Copenhagen", "Europe/Chisinau", "Europe/Tiraspol", "America/St_Lucia", "America/Buenos_Aires", "America/Rosario", "America/Cordoba", "America/Jujuy", "America/Catamarca", "America/Mendoza", "America/Caracas" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  370 */     tempMap.put("COST", new String[] { "America/Bogota" });
/*  371 */     tempMap.put("COT", new String[] { "America/Bogota" });
/*  372 */     tempMap.put("CST", new String[] { "Asia/Harbin", "Asia/Shanghai", "Asia/Chungking", "Asia/Urumqi", "Asia/Kashgar", "Asia/Taipei", "Asia/Macao", "Asia/Jayapura", "Australia/Darwin", "Australia/Adelaide", "Australia/Broken_Hill", "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Menominee", "America/Rainy_River", "America/Winnipeg", "America/Regina", "America/Swift_Current", "America/Pangnirtung", "America/Iqaluit", "America/Rankin_Inlet", "America/Cambridge_Bay", "America/Cancun", "America/Mexico_City", "America/Chihuahua", "America/Hermosillo", "America/Mazatlan", "America/Belize", "America/Costa_Rica", "America/Havana", "America/El_Salvador", "America/Guatemala", "America/Tegucigalpa", "America/Managua" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  392 */     tempMap.put("CUT", new String[] { "Europe/Zaporozhye" });
/*  393 */     tempMap.put("CVST", new String[] { "Atlantic/Cape_Verde" });
/*  394 */     tempMap.put("CVT", new String[] { "Atlantic/Cape_Verde" });
/*  395 */     tempMap.put("CWT", new String[] { "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Menominee" });
/*      */     
/*      */ 
/*      */ 
/*  399 */     tempMap.put("CXT", new String[] { "Indian/Christmas" });
/*  400 */     tempMap.put("DACT", new String[] { "Asia/Dacca" });
/*  401 */     tempMap.put("DAVT", new String[] { "Antarctica/Davis" });
/*  402 */     tempMap.put("DDUT", new String[] { "Antarctica/DumontDUrville" });
/*  403 */     tempMap.put("DFT", new String[] { "Europe/Oslo", "Europe/Paris" });
/*  404 */     tempMap.put("DMT", new String[] { "Europe/Belfast", "Europe/Dublin" });
/*  405 */     tempMap.put("DUSST", new String[] { "Asia/Dushanbe" });
/*  406 */     tempMap.put("DUST", new String[] { "Asia/Dushanbe" });
/*  407 */     tempMap.put("EASST", new String[] { "Pacific/Easter" });
/*  408 */     tempMap.put("EAST", new String[] { "Indian/Antananarivo", "Pacific/Easter" });
/*      */     
/*  410 */     tempMap.put("EAT", new String[] { "Indian/Comoro", "Indian/Antananarivo", "Indian/Mayotte", "Africa/Djibouti", "Africa/Asmera", "Africa/Addis_Ababa", "Africa/Nairobi", "Africa/Mogadishu", "Africa/Khartoum", "Africa/Dar_es_Salaam", "Africa/Kampala" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  415 */     tempMap.put("ECT", new String[] { "Pacific/Galapagos", "America/Guayaquil" });
/*      */     
/*  417 */     tempMap.put("EDDT", new String[] { "America/Iqaluit" });
/*  418 */     tempMap.put("EDT", new String[] { "America/New_York", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Montreal", "America/Thunder_Bay", "America/Nipigon", "America/Pangnirtung", "America/Iqaluit", "America/Cancun", "America/Nassau", "America/Santo_Domingo", "America/Port-au-Prince", "America/Jamaica", "America/Grand_Turk" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  426 */     tempMap.put("EEMT", new String[] { "Europe/Minsk", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Kaliningrad", "Europe/Moscow" });
/*      */     
/*  428 */     tempMap.put("EEST", new String[] { "Europe/Minsk", "Europe/Sofia", "Europe/Tallinn", "Europe/Helsinki", "Europe/Athens", "Europe/Riga", "Europe/Vilnius", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Warsaw", "Europe/Bucharest", "Europe/Kaliningrad", "Europe/Moscow", "Europe/Istanbul", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Asia/Nicosia", "Asia/Amman", "Asia/Beirut", "Asia/Gaza", "Asia/Damascus", "Africa/Cairo" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  436 */     tempMap.put("EET", new String[] { "Europe/Minsk", "Europe/Sofia", "Europe/Tallinn", "Europe/Helsinki", "Europe/Athens", "Europe/Riga", "Europe/Vilnius", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Warsaw", "Europe/Bucharest", "Europe/Kaliningrad", "Europe/Moscow", "Europe/Istanbul", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Asia/Nicosia", "Asia/Amman", "Asia/Beirut", "Asia/Gaza", "Asia/Damascus", "Africa/Cairo", "Africa/Tripoli" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */     tempMap.put("EGST", new String[] { "America/Scoresbysund" });
/*  446 */     tempMap.put("EGT", new String[] { "Atlantic/Jan_Mayen", "America/Scoresbysund" });
/*      */     
/*  448 */     tempMap.put("EHDT", new String[] { "America/Santo_Domingo" });
/*  449 */     tempMap.put("EST", new String[] { "Australia/Brisbane", "Australia/Lindeman", "Australia/Hobart", "Australia/Melbourne", "Australia/Sydney", "Australia/Broken_Hill", "Australia/Lord_Howe", "America/New_York", "America/Chicago", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Knox", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Menominee", "America/Montreal", "America/Thunder_Bay", "America/Nipigon", "America/Pangnirtung", "America/Iqaluit", "America/Cancun", "America/Antigua", "America/Nassau", "America/Cayman", "America/Santo_Domingo", "America/Port-au-Prince", "America/Jamaica", "America/Managua", "America/Panama", "America/Grand_Turk" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  463 */     tempMap.put("EWT", new String[] { "America/New_York", "America/Indianapolis", "America/Indiana/Marengo", "America/Indiana/Vevay", "America/Louisville", "America/Detroit", "America/Jamaica" });
/*      */     
/*      */ 
/*      */ 
/*  467 */     tempMap.put("FFMT", new String[] { "America/Martinique" });
/*  468 */     tempMap.put("FJST", new String[] { "Pacific/Fiji" });
/*  469 */     tempMap.put("FJT", new String[] { "Pacific/Fiji" });
/*  470 */     tempMap.put("FKST", new String[] { "Atlantic/Stanley" });
/*  471 */     tempMap.put("FKT", new String[] { "Atlantic/Stanley" });
/*  472 */     tempMap.put("FMT", new String[] { "Atlantic/Madeira", "Africa/Freetown" });
/*      */     
/*  474 */     tempMap.put("FNST", new String[] { "America/Noronha" });
/*  475 */     tempMap.put("FNT", new String[] { "America/Noronha" });
/*  476 */     tempMap.put("FRUST", new String[] { "Asia/Bishkek" });
/*  477 */     tempMap.put("FRUT", new String[] { "Asia/Bishkek" });
/*  478 */     tempMap.put("GALT", new String[] { "Pacific/Galapagos" });
/*  479 */     tempMap.put("GAMT", new String[] { "Pacific/Gambier" });
/*  480 */     tempMap.put("GBGT", new String[] { "America/Guyana" });
/*  481 */     tempMap.put("GEST", new String[] { "Asia/Tbilisi" });
/*  482 */     tempMap.put("GET", new String[] { "Asia/Tbilisi" });
/*  483 */     tempMap.put("GFT", new String[] { "America/Cayenne" });
/*  484 */     tempMap.put("GHST", new String[] { "Africa/Accra" });
/*  485 */     tempMap.put("GILT", new String[] { "Pacific/Tarawa" });
/*  486 */     tempMap.put("GMT", new String[] { "Atlantic/St_Helena", "Atlantic/Reykjavik", "Europe/London", "Europe/Belfast", "Europe/Dublin", "Europe/Gibraltar", "Africa/Porto-Novo", "Africa/Ouagadougou", "Africa/Abidjan", "Africa/Malabo", "Africa/Banjul", "Africa/Accra", "Africa/Conakry", "Africa/Bissau", "Africa/Monrovia", "Africa/Bamako", "Africa/Timbuktu", "Africa/Nouakchott", "Africa/Niamey", "Africa/Sao_Tome", "Africa/Dakar", "Africa/Freetown", "Africa/Lome" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  495 */     tempMap.put("GST", new String[] { "Atlantic/South_Georgia", "Asia/Bahrain", "Asia/Muscat", "Asia/Qatar", "Asia/Dubai", "Pacific/Guam" });
/*      */     
/*      */ 
/*  498 */     tempMap.put("GYT", new String[] { "America/Guyana" });
/*  499 */     tempMap.put("HADT", new String[] { "America/Adak" });
/*  500 */     tempMap.put("HART", new String[] { "Asia/Harbin" });
/*  501 */     tempMap.put("HAST", new String[] { "America/Adak" });
/*  502 */     tempMap.put("HAWT", new String[] { "America/Adak" });
/*  503 */     tempMap.put("HDT", new String[] { "Pacific/Honolulu" });
/*  504 */     tempMap.put("HKST", new String[] { "Asia/Hong_Kong" });
/*  505 */     tempMap.put("HKT", new String[] { "Asia/Hong_Kong" });
/*  506 */     tempMap.put("HMT", new String[] { "Atlantic/Azores", "Europe/Helsinki", "Asia/Dacca", "Asia/Calcutta", "America/Havana" });
/*      */     
/*  508 */     tempMap.put("HOVST", new String[] { "Asia/Hovd" });
/*  509 */     tempMap.put("HOVT", new String[] { "Asia/Hovd" });
/*  510 */     tempMap.put("HST", new String[] { "Pacific/Johnston", "Pacific/Honolulu" });
/*      */     
/*  512 */     tempMap.put("HWT", new String[] { "Pacific/Honolulu" });
/*  513 */     tempMap.put("ICT", new String[] { "Asia/Phnom_Penh", "Asia/Vientiane", "Asia/Bangkok", "Asia/Saigon" });
/*      */     
/*  515 */     tempMap.put("IDDT", new String[] { "Asia/Jerusalem", "Asia/Gaza" });
/*  516 */     tempMap.put("IDT", new String[] { "Asia/Jerusalem", "Asia/Gaza" });
/*  517 */     tempMap.put("IHST", new String[] { "Asia/Colombo" });
/*  518 */     tempMap.put("IMT", new String[] { "Europe/Sofia", "Europe/Istanbul", "Asia/Irkutsk" });
/*      */     
/*  520 */     tempMap.put("IOT", new String[] { "Indian/Chagos" });
/*  521 */     tempMap.put("IRKMT", new String[] { "Asia/Irkutsk" });
/*  522 */     tempMap.put("IRKST", new String[] { "Asia/Irkutsk" });
/*  523 */     tempMap.put("IRKT", new String[] { "Asia/Irkutsk" });
/*  524 */     tempMap.put("IRST", new String[] { "Asia/Tehran" });
/*  525 */     tempMap.put("IRT", new String[] { "Asia/Tehran" });
/*  526 */     tempMap.put("ISST", new String[] { "Atlantic/Reykjavik" });
/*  527 */     tempMap.put("IST", new String[] { "Atlantic/Reykjavik", "Europe/Belfast", "Europe/Dublin", "Asia/Dacca", "Asia/Thimbu", "Asia/Calcutta", "Asia/Jerusalem", "Asia/Katmandu", "Asia/Karachi", "Asia/Gaza", "Asia/Colombo" });
/*      */     
/*      */ 
/*      */ 
/*  531 */     tempMap.put("JAYT", new String[] { "Asia/Jayapura" });
/*  532 */     tempMap.put("JMT", new String[] { "Atlantic/St_Helena", "Asia/Jerusalem" });
/*      */     
/*  534 */     tempMap.put("JST", new String[] { "Asia/Rangoon", "Asia/Dili", "Asia/Ujung_Pandang", "Asia/Tokyo", "Asia/Kuala_Lumpur", "Asia/Kuching", "Asia/Manila", "Asia/Singapore", "Pacific/Nauru" });
/*      */     
/*      */ 
/*      */ 
/*  538 */     tempMap.put("KART", new String[] { "Asia/Karachi" });
/*  539 */     tempMap.put("KAST", new String[] { "Asia/Kashgar" });
/*  540 */     tempMap.put("KDT", new String[] { "Asia/Seoul" });
/*  541 */     tempMap.put("KGST", new String[] { "Asia/Bishkek" });
/*  542 */     tempMap.put("KGT", new String[] { "Asia/Bishkek" });
/*  543 */     tempMap.put("KMT", new String[] { "Europe/Vilnius", "Europe/Kiev", "America/Cayman", "America/Jamaica", "America/St_Vincent", "America/Grand_Turk" });
/*      */     
/*      */ 
/*  546 */     tempMap.put("KOST", new String[] { "Pacific/Kosrae" });
/*  547 */     tempMap.put("KRAMT", new String[] { "Asia/Krasnoyarsk" });
/*  548 */     tempMap.put("KRAST", new String[] { "Asia/Krasnoyarsk" });
/*  549 */     tempMap.put("KRAT", new String[] { "Asia/Krasnoyarsk" });
/*  550 */     tempMap.put("KST", new String[] { "Asia/Seoul", "Asia/Pyongyang" });
/*  551 */     tempMap.put("KUYMT", new String[] { "Europe/Samara" });
/*  552 */     tempMap.put("KUYST", new String[] { "Europe/Samara" });
/*  553 */     tempMap.put("KUYT", new String[] { "Europe/Samara" });
/*  554 */     tempMap.put("KWAT", new String[] { "Pacific/Kwajalein" });
/*  555 */     tempMap.put("LHST", new String[] { "Australia/Lord_Howe" });
/*  556 */     tempMap.put("LINT", new String[] { "Pacific/Kiritimati" });
/*  557 */     tempMap.put("LKT", new String[] { "Asia/Colombo" });
/*  558 */     tempMap.put("LPMT", new String[] { "America/La_Paz" });
/*  559 */     tempMap.put("LRT", new String[] { "Africa/Monrovia" });
/*  560 */     tempMap.put("LST", new String[] { "Europe/Riga" });
/*  561 */     tempMap.put("M", new String[] { "Europe/Moscow" });
/*  562 */     tempMap.put("MADST", new String[] { "Atlantic/Madeira" });
/*  563 */     tempMap.put("MAGMT", new String[] { "Asia/Magadan" });
/*  564 */     tempMap.put("MAGST", new String[] { "Asia/Magadan" });
/*  565 */     tempMap.put("MAGT", new String[] { "Asia/Magadan" });
/*  566 */     tempMap.put("MALT", new String[] { "Asia/Kuala_Lumpur", "Asia/Singapore" });
/*      */     
/*  568 */     tempMap.put("MART", new String[] { "Pacific/Marquesas" });
/*  569 */     tempMap.put("MAWT", new String[] { "Antarctica/Mawson" });
/*  570 */     tempMap.put("MDDT", new String[] { "America/Cambridge_Bay", "America/Yellowknife", "America/Inuvik" });
/*      */     
/*  572 */     tempMap.put("MDST", new String[] { "Europe/Moscow" });
/*  573 */     tempMap.put("MDT", new String[] { "America/Denver", "America/Phoenix", "America/Boise", "America/Regina", "America/Swift_Current", "America/Edmonton", "America/Cambridge_Bay", "America/Yellowknife", "America/Inuvik", "America/Chihuahua", "America/Hermosillo", "America/Mazatlan" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  578 */     tempMap.put("MET", new String[] { "Europe/Tirane", "Europe/Andorra", "Europe/Vienna", "Europe/Minsk", "Europe/Brussels", "Europe/Sofia", "Europe/Prague", "Europe/Copenhagen", "Europe/Tallinn", "Europe/Berlin", "Europe/Gibraltar", "Europe/Athens", "Europe/Budapest", "Europe/Rome", "Europe/Riga", "Europe/Vaduz", "Europe/Vilnius", "Europe/Luxembourg", "Europe/Malta", "Europe/Chisinau", "Europe/Tiraspol", "Europe/Monaco", "Europe/Amsterdam", "Europe/Oslo", "Europe/Warsaw", "Europe/Lisbon", "Europe/Kaliningrad", "Europe/Madrid", "Europe/Stockholm", "Europe/Zurich", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol", "Europe/Belgrade", "Africa/Algiers", "Africa/Tripoli", "Africa/Casablanca", "Africa/Tunis", "Africa/Ceuta" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  592 */     tempMap.put("MHT", new String[] { "Pacific/Majuro", "Pacific/Kwajalein" });
/*      */     
/*  594 */     tempMap.put("MMT", new String[] { "Indian/Maldives", "Europe/Minsk", "Europe/Moscow", "Asia/Rangoon", "Asia/Ujung_Pandang", "Asia/Colombo", "Pacific/Easter", "Africa/Monrovia", "America/Managua", "America/Montevideo" });
/*      */     
/*      */ 
/*      */ 
/*  598 */     tempMap.put("MOST", new String[] { "Asia/Macao" });
/*  599 */     tempMap.put("MOT", new String[] { "Asia/Macao" });
/*  600 */     tempMap.put("MPT", new String[] { "Pacific/Saipan" });
/*  601 */     tempMap.put("MSK", new String[] { "Europe/Minsk", "Europe/Tallinn", "Europe/Riga", "Europe/Vilnius", "Europe/Chisinau", "Europe/Kiev", "Europe/Uzhgorod", "Europe/Zaporozhye", "Europe/Simferopol" });
/*      */     
/*      */ 
/*      */ 
/*  605 */     tempMap.put("MST", new String[] { "Europe/Moscow", "America/Denver", "America/Phoenix", "America/Boise", "America/Regina", "America/Swift_Current", "America/Edmonton", "America/Dawson_Creek", "America/Cambridge_Bay", "America/Yellowknife", "America/Inuvik", "America/Mexico_City", "America/Chihuahua", "America/Hermosillo", "America/Mazatlan", "America/Tijuana" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  612 */     tempMap.put("MUT", new String[] { "Indian/Mauritius" });
/*  613 */     tempMap.put("MVT", new String[] { "Indian/Maldives" });
/*  614 */     tempMap.put("MWT", new String[] { "America/Denver", "America/Phoenix", "America/Boise" });
/*      */     
/*  616 */     tempMap.put("MYT", new String[] { "Asia/Kuala_Lumpur", "Asia/Kuching" });
/*      */     
/*      */ 
/*  619 */     tempMap.put("NCST", new String[] { "Pacific/Noumea" });
/*  620 */     tempMap.put("NCT", new String[] { "Pacific/Noumea" });
/*  621 */     tempMap.put("NDT", new String[] { "America/Nome", "America/Adak", "America/St_Johns", "America/Goose_Bay" });
/*      */     
/*  623 */     tempMap.put("NEGT", new String[] { "America/Paramaribo" });
/*  624 */     tempMap.put("NFT", new String[] { "Europe/Paris", "Europe/Oslo", "Pacific/Norfolk" });
/*      */     
/*  626 */     tempMap.put("NMT", new String[] { "Pacific/Norfolk" });
/*  627 */     tempMap.put("NOVMT", new String[] { "Asia/Novosibirsk" });
/*  628 */     tempMap.put("NOVST", new String[] { "Asia/Novosibirsk" });
/*  629 */     tempMap.put("NOVT", new String[] { "Asia/Novosibirsk" });
/*  630 */     tempMap.put("NPT", new String[] { "Asia/Katmandu" });
/*  631 */     tempMap.put("NRT", new String[] { "Pacific/Nauru" });
/*  632 */     tempMap.put("NST", new String[] { "Europe/Amsterdam", "Pacific/Pago_Pago", "Pacific/Midway", "America/Nome", "America/Adak", "America/St_Johns", "America/Goose_Bay" });
/*      */     
/*      */ 
/*  635 */     tempMap.put("NUT", new String[] { "Pacific/Niue" });
/*  636 */     tempMap.put("NWT", new String[] { "America/Nome", "America/Adak" });
/*  637 */     tempMap.put("NZDT", new String[] { "Antarctica/McMurdo" });
/*  638 */     tempMap.put("NZHDT", new String[] { "Pacific/Auckland" });
/*  639 */     tempMap.put("NZST", new String[] { "Antarctica/McMurdo", "Pacific/Auckland" });
/*      */     
/*  641 */     tempMap.put("OMSMT", new String[] { "Asia/Omsk" });
/*  642 */     tempMap.put("OMSST", new String[] { "Asia/Omsk" });
/*  643 */     tempMap.put("OMST", new String[] { "Asia/Omsk" });
/*  644 */     tempMap.put("PDDT", new String[] { "America/Inuvik", "America/Whitehorse", "America/Dawson" });
/*      */     
/*  646 */     tempMap.put("PDT", new String[] { "America/Los_Angeles", "America/Juneau", "America/Boise", "America/Vancouver", "America/Dawson_Creek", "America/Inuvik", "America/Whitehorse", "America/Dawson", "America/Tijuana" });
/*      */     
/*      */ 
/*      */ 
/*  650 */     tempMap.put("PEST", new String[] { "America/Lima" });
/*  651 */     tempMap.put("PET", new String[] { "America/Lima" });
/*  652 */     tempMap.put("PETMT", new String[] { "Asia/Kamchatka" });
/*  653 */     tempMap.put("PETST", new String[] { "Asia/Kamchatka" });
/*  654 */     tempMap.put("PETT", new String[] { "Asia/Kamchatka" });
/*  655 */     tempMap.put("PGT", new String[] { "Pacific/Port_Moresby" });
/*  656 */     tempMap.put("PHOT", new String[] { "Pacific/Enderbury" });
/*  657 */     tempMap.put("PHST", new String[] { "Asia/Manila" });
/*  658 */     tempMap.put("PHT", new String[] { "Asia/Manila" });
/*  659 */     tempMap.put("PKT", new String[] { "Asia/Karachi" });
/*  660 */     tempMap.put("PMDT", new String[] { "America/Miquelon" });
/*  661 */     tempMap.put("PMMT", new String[] { "Pacific/Port_Moresby" });
/*  662 */     tempMap.put("PMST", new String[] { "America/Miquelon" });
/*  663 */     tempMap.put("PMT", new String[] { "Antarctica/DumontDUrville", "Europe/Prague", "Europe/Paris", "Europe/Monaco", "Africa/Algiers", "Africa/Tunis", "America/Panama", "America/Paramaribo" });
/*      */     
/*      */ 
/*      */ 
/*  667 */     tempMap.put("PNT", new String[] { "Pacific/Pitcairn" });
/*  668 */     tempMap.put("PONT", new String[] { "Pacific/Ponape" });
/*  669 */     tempMap.put("PPMT", new String[] { "America/Port-au-Prince" });
/*  670 */     tempMap.put("PST", new String[] { "Pacific/Pitcairn", "America/Los_Angeles", "America/Juneau", "America/Boise", "America/Vancouver", "America/Dawson_Creek", "America/Inuvik", "America/Whitehorse", "America/Dawson", "America/Hermosillo", "America/Mazatlan", "America/Tijuana" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  675 */     tempMap.put("PWT", new String[] { "Pacific/Palau", "America/Los_Angeles", "America/Juneau", "America/Boise", "America/Tijuana" });
/*      */     
/*      */ 
/*  678 */     tempMap.put("PYST", new String[] { "America/Asuncion" });
/*  679 */     tempMap.put("PYT", new String[] { "America/Asuncion" });
/*  680 */     tempMap.put("QMT", new String[] { "America/Guayaquil" });
/*  681 */     tempMap.put("RET", new String[] { "Indian/Reunion" });
/*  682 */     tempMap.put("RMT", new String[] { "Atlantic/Reykjavik", "Europe/Rome", "Europe/Riga", "Asia/Rangoon" });
/*      */     
/*  684 */     tempMap.put("S", new String[] { "Europe/Moscow" });
/*  685 */     tempMap.put("SAMMT", new String[] { "Europe/Samara" });
/*  686 */     tempMap.put("SAMST", new String[] { "Europe/Samara", "Asia/Samarkand" });
/*      */     
/*      */ 
/*  689 */     tempMap.put("SAMT", new String[] { "Europe/Samara", "Asia/Samarkand", "Pacific/Pago_Pago", "Pacific/Apia" });
/*      */     
/*  691 */     tempMap.put("SAST", new String[] { "Africa/Maseru", "Africa/Windhoek", "Africa/Johannesburg", "Africa/Mbabane" });
/*      */     
/*  693 */     tempMap.put("SBT", new String[] { "Pacific/Guadalcanal" });
/*  694 */     tempMap.put("SCT", new String[] { "Indian/Mahe" });
/*  695 */     tempMap.put("SDMT", new String[] { "America/Santo_Domingo" });
/*  696 */     tempMap.put("SGT", new String[] { "Asia/Singapore" });
/*  697 */     tempMap.put("SHEST", new String[] { "Asia/Aqtau" });
/*  698 */     tempMap.put("SHET", new String[] { "Asia/Aqtau" });
/*  699 */     tempMap.put("SJMT", new String[] { "America/Costa_Rica" });
/*  700 */     tempMap.put("SLST", new String[] { "Africa/Freetown" });
/*  701 */     tempMap.put("SMT", new String[] { "Atlantic/Stanley", "Europe/Stockholm", "Europe/Simferopol", "Asia/Phnom_Penh", "Asia/Vientiane", "Asia/Kuala_Lumpur", "Asia/Singapore", "Asia/Saigon", "America/Santiago" });
/*      */     
/*      */ 
/*      */ 
/*  705 */     tempMap.put("SRT", new String[] { "America/Paramaribo" });
/*  706 */     tempMap.put("SST", new String[] { "Pacific/Pago_Pago", "Pacific/Midway" });
/*      */     
/*  708 */     tempMap.put("SVEMT", new String[] { "Asia/Yekaterinburg" });
/*  709 */     tempMap.put("SVEST", new String[] { "Asia/Yekaterinburg" });
/*  710 */     tempMap.put("SVET", new String[] { "Asia/Yekaterinburg" });
/*  711 */     tempMap.put("SWAT", new String[] { "Africa/Windhoek" });
/*  712 */     tempMap.put("SYOT", new String[] { "Antarctica/Syowa" });
/*  713 */     tempMap.put("TAHT", new String[] { "Pacific/Tahiti" });
/*  714 */     tempMap.put("TASST", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*      */     
/*      */ 
/*  717 */     tempMap.put("TAST", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*  718 */     tempMap.put("TBIST", new String[] { "Asia/Tbilisi" });
/*  719 */     tempMap.put("TBIT", new String[] { "Asia/Tbilisi" });
/*  720 */     tempMap.put("TBMT", new String[] { "Asia/Tbilisi" });
/*  721 */     tempMap.put("TFT", new String[] { "Indian/Kerguelen" });
/*  722 */     tempMap.put("TJT", new String[] { "Asia/Dushanbe" });
/*  723 */     tempMap.put("TKT", new String[] { "Pacific/Fakaofo" });
/*  724 */     tempMap.put("TMST", new String[] { "Asia/Ashkhabad" });
/*  725 */     tempMap.put("TMT", new String[] { "Europe/Tallinn", "Asia/Tehran", "Asia/Ashkhabad" });
/*      */     
/*  727 */     tempMap.put("TOST", new String[] { "Pacific/Tongatapu" });
/*  728 */     tempMap.put("TOT", new String[] { "Pacific/Tongatapu" });
/*  729 */     tempMap.put("TPT", new String[] { "Asia/Dili" });
/*  730 */     tempMap.put("TRST", new String[] { "Europe/Istanbul" });
/*  731 */     tempMap.put("TRT", new String[] { "Europe/Istanbul" });
/*  732 */     tempMap.put("TRUT", new String[] { "Pacific/Truk" });
/*  733 */     tempMap.put("TVT", new String[] { "Pacific/Funafuti" });
/*  734 */     tempMap.put("ULAST", new String[] { "Asia/Ulaanbaatar" });
/*  735 */     tempMap.put("ULAT", new String[] { "Asia/Ulaanbaatar" });
/*  736 */     tempMap.put("URUT", new String[] { "Asia/Urumqi" });
/*  737 */     tempMap.put("UYHST", new String[] { "America/Montevideo" });
/*  738 */     tempMap.put("UYT", new String[] { "America/Montevideo" });
/*  739 */     tempMap.put("UZST", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*  740 */     tempMap.put("UZT", new String[] { "Asia/Samarkand", "Asia/Tashkent" });
/*  741 */     tempMap.put("VET", new String[] { "America/Caracas" });
/*  742 */     tempMap.put("VLAMT", new String[] { "Asia/Vladivostok" });
/*  743 */     tempMap.put("VLAST", new String[] { "Asia/Vladivostok" });
/*  744 */     tempMap.put("VLAT", new String[] { "Asia/Vladivostok" });
/*  745 */     tempMap.put("VUST", new String[] { "Pacific/Efate" });
/*  746 */     tempMap.put("VUT", new String[] { "Pacific/Efate" });
/*  747 */     tempMap.put("WAKT", new String[] { "Pacific/Wake" });
/*  748 */     tempMap.put("WARST", new String[] { "America/Jujuy", "America/Mendoza" });
/*      */     
/*  750 */     tempMap.put("WART", new String[] { "America/Jujuy", "America/Mendoza" });
/*      */     
/*      */ 
/*  753 */     tempMap.put("WAST", new String[] { "Africa/Ndjamena", "Africa/Windhoek" });
/*      */     
/*  755 */     tempMap.put("WAT", new String[] { "Africa/Luanda", "Africa/Porto-Novo", "Africa/Douala", "Africa/Bangui", "Africa/Ndjamena", "Africa/Kinshasa", "Africa/Brazzaville", "Africa/Malabo", "Africa/Libreville", "Africa/Banjul", "Africa/Conakry", "Africa/Bissau", "Africa/Bamako", "Africa/Nouakchott", "Africa/El_Aaiun", "Africa/Windhoek", "Africa/Niamey", "Africa/Lagos", "Africa/Dakar", "Africa/Freetown" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  762 */     tempMap.put("WEST", new String[] { "Atlantic/Faeroe", "Atlantic/Azores", "Atlantic/Madeira", "Atlantic/Canary", "Europe/Brussels", "Europe/Luxembourg", "Europe/Monaco", "Europe/Lisbon", "Europe/Madrid", "Africa/Algiers", "Africa/Casablanca", "Africa/Ceuta" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  767 */     tempMap.put("WET", new String[] { "Atlantic/Faeroe", "Atlantic/Azores", "Atlantic/Madeira", "Atlantic/Canary", "Europe/Andorra", "Europe/Brussels", "Europe/Luxembourg", "Europe/Monaco", "Europe/Lisbon", "Europe/Madrid", "Africa/Algiers", "Africa/Casablanca", "Africa/El_Aaiun", "Africa/Ceuta" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  772 */     tempMap.put("WFT", new String[] { "Pacific/Wallis" });
/*  773 */     tempMap.put("WGST", new String[] { "America/Godthab" });
/*  774 */     tempMap.put("WGT", new String[] { "America/Godthab" });
/*  775 */     tempMap.put("WMT", new String[] { "Europe/Vilnius", "Europe/Warsaw" });
/*  776 */     tempMap.put("WST", new String[] { "Antarctica/Casey", "Pacific/Apia", "Australia/Perth" });
/*      */     
/*  778 */     tempMap.put("YAKMT", new String[] { "Asia/Yakutsk" });
/*  779 */     tempMap.put("YAKST", new String[] { "Asia/Yakutsk" });
/*  780 */     tempMap.put("YAKT", new String[] { "Asia/Yakutsk" });
/*  781 */     tempMap.put("YAPT", new String[] { "Pacific/Yap" });
/*  782 */     tempMap.put("YDDT", new String[] { "America/Whitehorse", "America/Dawson" });
/*      */     
/*  784 */     tempMap.put("YDT", new String[] { "America/Yakutat", "America/Whitehorse", "America/Dawson" });
/*      */     
/*  786 */     tempMap.put("YEKMT", new String[] { "Asia/Yekaterinburg" });
/*  787 */     tempMap.put("YEKST", new String[] { "Asia/Yekaterinburg" });
/*  788 */     tempMap.put("YEKT", new String[] { "Asia/Yekaterinburg" });
/*  789 */     tempMap.put("YERST", new String[] { "Asia/Yerevan" });
/*  790 */     tempMap.put("YERT", new String[] { "Asia/Yerevan" });
/*  791 */     tempMap.put("YST", new String[] { "America/Yakutat", "America/Whitehorse", "America/Dawson" });
/*      */     
/*  793 */     tempMap.put("YWT", new String[] { "America/Yakutat" });
/*      */     
/*  795 */     ABBREVIATED_TIMEZONES = Collections.unmodifiableMap(tempMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Time changeTimezone(Connection conn, Calendar sessionCalendar, Calendar targetCalendar, Time t, TimeZone fromTz, TimeZone toTz, boolean rollForward)
/*      */   {
/*  819 */     if (conn != null) {
/*  820 */       if ((conn.getUseTimezone()) && (!conn.getNoTimezoneConversionForTimeType()))
/*      */       {
/*      */ 
/*  823 */         Calendar fromCal = Calendar.getInstance(fromTz);
/*  824 */         fromCal.setTime(t);
/*      */         
/*  826 */         int fromOffset = fromCal.get(15) + fromCal.get(16);
/*      */         
/*  828 */         Calendar toCal = Calendar.getInstance(toTz);
/*  829 */         toCal.setTime(t);
/*      */         
/*  831 */         int toOffset = toCal.get(15) + toCal.get(16);
/*      */         
/*  833 */         int offsetDiff = fromOffset - toOffset;
/*  834 */         long toTime = toCal.getTime().getTime();
/*      */         
/*  836 */         if ((rollForward) || ((conn.isServerTzUTC()) && (!conn.isClientTzUTC()))) {
/*  837 */           toTime += offsetDiff;
/*      */         } else {
/*  839 */           toTime -= offsetDiff;
/*      */         }
/*      */         
/*  842 */         Time changedTime = new Time(toTime);
/*      */         
/*  844 */         return changedTime; }
/*  845 */       if ((conn.getUseJDBCCompliantTimezoneShift()) && 
/*  846 */         (targetCalendar != null))
/*      */       {
/*  848 */         Time adjustedTime = new Time(jdbcCompliantZoneShift(sessionCalendar, targetCalendar, t));
/*      */         
/*      */ 
/*      */ 
/*  852 */         return adjustedTime;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  857 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Timestamp changeTimezone(Connection conn, Calendar sessionCalendar, Calendar targetCalendar, Timestamp tstamp, TimeZone fromTz, TimeZone toTz, boolean rollForward)
/*      */   {
/*  881 */     if (conn != null) {
/*  882 */       if (conn.getUseTimezone())
/*      */       {
/*  884 */         Calendar fromCal = Calendar.getInstance(fromTz);
/*  885 */         fromCal.setTime(tstamp);
/*      */         
/*  887 */         int fromOffset = fromCal.get(15) + fromCal.get(16);
/*      */         
/*  889 */         Calendar toCal = Calendar.getInstance(toTz);
/*  890 */         toCal.setTime(tstamp);
/*      */         
/*  892 */         int toOffset = toCal.get(15) + toCal.get(16);
/*      */         
/*  894 */         int offsetDiff = fromOffset - toOffset;
/*  895 */         long toTime = toCal.getTime().getTime();
/*      */         
/*  897 */         if ((rollForward) || ((conn.isServerTzUTC()) && (!conn.isClientTzUTC()))) {
/*  898 */           toTime += offsetDiff;
/*      */         } else {
/*  900 */           toTime -= offsetDiff;
/*      */         }
/*      */         
/*  903 */         Timestamp changedTimestamp = new Timestamp(toTime);
/*      */         
/*  905 */         return changedTimestamp; }
/*  906 */       if ((conn.getUseJDBCCompliantTimezoneShift()) && 
/*  907 */         (targetCalendar != null))
/*      */       {
/*  909 */         Timestamp adjustedTimestamp = new Timestamp(jdbcCompliantZoneShift(sessionCalendar, targetCalendar, tstamp));
/*      */         
/*      */ 
/*      */ 
/*  913 */         adjustedTimestamp.setNanos(tstamp.getNanos());
/*      */         
/*  915 */         return adjustedTimestamp;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  920 */     return tstamp;
/*      */   }
/*      */   
/*      */ 
/*      */   private static long jdbcCompliantZoneShift(Calendar sessionCalendar, Calendar targetCalendar, java.util.Date dt)
/*      */   {
/*  926 */     if (sessionCalendar == null) {
/*  927 */       sessionCalendar = new GregorianCalendar();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  934 */     java.util.Date origCalDate = targetCalendar.getTime();
/*  935 */     java.util.Date origSessionDate = sessionCalendar.getTime();
/*      */     try
/*      */     {
/*  938 */       sessionCalendar.setTime(dt);
/*      */       
/*  940 */       targetCalendar.set(1, sessionCalendar.get(1));
/*  941 */       targetCalendar.set(2, sessionCalendar.get(2));
/*  942 */       targetCalendar.set(5, sessionCalendar.get(5));
/*      */       
/*  944 */       targetCalendar.set(11, sessionCalendar.get(11));
/*  945 */       targetCalendar.set(12, sessionCalendar.get(12));
/*  946 */       targetCalendar.set(13, sessionCalendar.get(13));
/*  947 */       targetCalendar.set(14, sessionCalendar.get(14));
/*      */       
/*  949 */       return targetCalendar.getTime().getTime();
/*      */     }
/*      */     finally {
/*  952 */       sessionCalendar.setTime(origSessionDate);
/*  953 */       targetCalendar.setTime(origCalDate);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final java.sql.Date fastDateCreate(boolean useGmtConversion, Calendar gmtCalIfNeeded, Calendar cal, int year, int month, int day)
/*      */   {
/*  965 */     Calendar dateCal = cal;
/*      */     
/*  967 */     if (useGmtConversion)
/*      */     {
/*  969 */       if (gmtCalIfNeeded == null) {
/*  970 */         gmtCalIfNeeded = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */       }
/*  972 */       gmtCalIfNeeded.clear();
/*      */       
/*  974 */       dateCal = gmtCalIfNeeded;
/*      */     }
/*      */     
/*  977 */     dateCal.clear();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  982 */     dateCal.set(year, month - 1, day, 0, 0, 0);
/*      */     
/*  984 */     long dateAsMillis = 0L;
/*      */     try
/*      */     {
/*  987 */       dateAsMillis = dateCal.getTimeInMillis();
/*      */     }
/*      */     catch (IllegalAccessError iae) {
/*  990 */       dateAsMillis = dateCal.getTime().getTime();
/*      */     }
/*      */     
/*  993 */     return new java.sql.Date(dateAsMillis);
/*      */   }
/*      */   
/*      */   static final Time fastTimeCreate(Calendar cal, int hour, int minute, int second) throws SQLException
/*      */   {
/*  998 */     if ((hour < 0) || (hour > 23)) {
/*  999 */       throw SQLError.createSQLException("Illegal hour value '" + hour + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1004 */     if ((minute < 0) || (minute > 59)) {
/* 1005 */       throw SQLError.createSQLException("Illegal minute value '" + minute + "'" + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1010 */     if ((second < 0) || (second > 59)) {
/* 1011 */       throw SQLError.createSQLException("Illegal minute value '" + second + "'" + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1016 */     cal.clear();
/*      */     
/*      */ 
/* 1019 */     cal.set(1970, 0, 1, hour, minute, second);
/*      */     
/* 1021 */     long timeAsMillis = 0L;
/*      */     try
/*      */     {
/* 1024 */       timeAsMillis = cal.getTimeInMillis();
/*      */     }
/*      */     catch (IllegalAccessError iae) {
/* 1027 */       timeAsMillis = cal.getTime().getTime();
/*      */     }
/*      */     
/* 1030 */     return new Time(timeAsMillis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final Timestamp fastTimestampCreate(boolean useGmtConversion, Calendar gmtCalIfNeeded, Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart)
/*      */   {
/* 1038 */     cal.clear();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1043 */     cal.set(year, month - 1, day, hour, minute, seconds);
/*      */     
/* 1045 */     int offsetDiff = 0;
/*      */     
/* 1047 */     if (useGmtConversion) {
/* 1048 */       int fromOffset = cal.get(15) + cal.get(16);
/*      */       
/*      */ 
/* 1051 */       if (gmtCalIfNeeded == null) {
/* 1052 */         gmtCalIfNeeded = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */       }
/* 1054 */       gmtCalIfNeeded.clear();
/*      */       
/* 1056 */       gmtCalIfNeeded.setTimeInMillis(cal.getTimeInMillis());
/*      */       
/* 1058 */       int toOffset = gmtCalIfNeeded.get(15) + gmtCalIfNeeded.get(16);
/*      */       
/* 1060 */       offsetDiff = fromOffset - toOffset;
/*      */     }
/*      */     
/* 1063 */     long tsAsMillis = 0L;
/*      */     try
/*      */     {
/* 1066 */       tsAsMillis = cal.getTimeInMillis();
/*      */     }
/*      */     catch (IllegalAccessError iae) {
/* 1069 */       tsAsMillis = cal.getTime().getTime();
/*      */     }
/*      */     
/* 1072 */     Timestamp ts = new Timestamp(tsAsMillis + offsetDiff);
/* 1073 */     ts.setNanos(secondsPart);
/*      */     
/* 1075 */     return ts;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getCanoncialTimezone(String timezoneStr)
/*      */   {
/* 1090 */     if (timezoneStr == null) {
/* 1091 */       return null;
/*      */     }
/*      */     
/* 1094 */     timezoneStr = timezoneStr.trim();
/*      */     
/*      */ 
/*      */ 
/* 1098 */     int daylightIndex = StringUtils.indexOfIgnoreCase(timezoneStr, "DAYLIGHT");
/*      */     
/*      */ 
/* 1101 */     if (daylightIndex != -1) {
/* 1102 */       StringBuffer timezoneBuf = new StringBuffer();
/* 1103 */       timezoneBuf.append(timezoneStr.substring(0, daylightIndex));
/* 1104 */       timezoneBuf.append("Standard");
/* 1105 */       timezoneBuf.append(timezoneStr.substring(daylightIndex + "DAYLIGHT".length(), timezoneStr.length()));
/*      */       
/* 1107 */       timezoneStr = timezoneBuf.toString();
/*      */     }
/*      */     
/* 1110 */     String canonicalTz = (String)TIMEZONE_MAPPINGS.get(timezoneStr);
/*      */     
/*      */ 
/* 1113 */     if (canonicalTz == null) {
/* 1114 */       String[] abbreviatedTimezone = (String[])ABBREVIATED_TIMEZONES.get(timezoneStr);
/*      */       
/*      */ 
/* 1117 */       if (abbreviatedTimezone != null)
/*      */       {
/* 1119 */         if (abbreviatedTimezone.length == 1) {
/* 1120 */           canonicalTz = abbreviatedTimezone[0];
/*      */         } else {
/* 1122 */           StringBuffer errorMsg = new StringBuffer("The server timezone value '");
/*      */           
/* 1124 */           errorMsg.append(timezoneStr);
/* 1125 */           errorMsg.append("' represents more than one timezone. You must ");
/*      */           
/* 1127 */           errorMsg.append("configure either the server or client to use a ");
/*      */           
/* 1129 */           errorMsg.append("more specifc timezone value if you want to enable ");
/*      */           
/* 1131 */           errorMsg.append("timezone support. The timezones that '");
/* 1132 */           errorMsg.append(timezoneStr);
/* 1133 */           errorMsg.append("' maps to are: ");
/* 1134 */           errorMsg.append(abbreviatedTimezone[0]);
/*      */           
/* 1136 */           for (int i = 1; i < abbreviatedTimezone.length; i++) {
/* 1137 */             errorMsg.append(", ");
/* 1138 */             errorMsg.append(abbreviatedTimezone[i]);
/*      */           }
/*      */           
/* 1141 */           throw new IllegalArgumentException(errorMsg.toString());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1146 */     return canonicalTz;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String timeFormattedString(int hours, int minutes, int seconds)
/*      */   {
/* 1155 */     StringBuffer buf = new StringBuffer(8);
/*      */     
/* 1157 */     if (hours < 10) {
/* 1158 */       buf.append("0");
/*      */     }
/*      */     
/* 1161 */     buf.append(hours);
/* 1162 */     buf.append(":");
/*      */     
/* 1164 */     if (minutes < 10) {
/* 1165 */       buf.append("0");
/*      */     }
/*      */     
/* 1168 */     buf.append(minutes);
/* 1169 */     buf.append(":");
/*      */     
/* 1171 */     if (seconds < 10) {
/* 1172 */       buf.append("0");
/*      */     }
/*      */     
/* 1175 */     buf.append(seconds);
/*      */     
/* 1177 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\TimeUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */